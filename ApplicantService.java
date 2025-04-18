package assignment2002;

import assignment2002.user.Applicant;
import java.io.*;
import java.util.*;

public class ApplicantService {

    public static boolean isEligible(Applicant applicant, BTOProperty project, String flatType) {
        if (!project.isVisible()) return false;

        if (applicant.getMaritalStatus().equalsIgnoreCase("Single")) {
            return applicant.getAge() >= 35 && flatType.equals("2-Room") && project.getTwoRoomAmt() > 0;
        } else if (applicant.getMaritalStatus().equalsIgnoreCase("Married")) {
            if (applicant.getAge() >= 21) {
                return (flatType.equals("2-Room") && project.getTwoRoomAmt() > 0)
                    || (flatType.equals("3-Room") && project.getThreeRoomAmt() > 0);
            }
        }
        return false;
    }

    public static boolean apply(Applicant applicant, BTOProperty project, String flatType) {
        String status = getApplicationStatus(applicant);
        if (!status.equals("NOTAPPLIED") && !status.equals("NOT FOUND")) {
            System.out.println("You have already applied for a flat. Status: " + status);
            return false;
        }

        applicant.setFlatType(flatType);
        applicant.setAppliedProject(project.getProjectName());
        applicant.setApplicationStatus("PENDING");
        project.addApplicant(applicant, flatType);

        return updateApplicantFile(applicant, project, flatType, "PENDING");
    }

    public static void withdraw(Applicant applicant) {
        String status = getApplicationStatus(applicant);
        if (!status.equals("PENDING")) {
            System.out.println("No active application to withdraw.");
            return;
        }

        applicant.setFlatType("");
        applicant.setAppliedProject("");
        applicant.setApplicationStatus("NOTAPPLIED");

        updateApplicantFile(applicant, null, "", "NOTAPPLIED");
        System.out.println("Application withdrawn successfully.");
    }

    public static String getApplicationStatus(Applicant applicant) {
        return applicant.getApplicationStatus();
    }
    

    private static boolean updateApplicantFile(Applicant applicant, BTOProperty project, String flatType, String status) {
        String filePath = "assignment2002/Information/ApplicantList.txt";
        File file = new File(filePath);
        String desiredHeader = "Name\tNRIC\tAge\tMaritalStatus\tPassword\tFlatType\tProjectName\tApplicationStatus";
        ArrayList<String> updatedLines = new ArrayList<>();
        boolean fileExists = file.exists();
        boolean updated = false;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Name\t")) {
                    updatedLines.add(desiredHeader);
                    continue;
                }

                String[] parts = line.split("\t");
                if (parts.length >= 2 && parts[1].equals(applicant.getNRIC())) {
                    String newLine = String.join("\t",
                            applicant.getName(),
                            applicant.getNRIC(),
                            String.valueOf(applicant.getAge()),
                            applicant.getMaritalStatus(),
                            applicant.getPassword(),
                            flatType,
                            (project == null ? "" : project.getProjectName()),
                            status
                    );
                    updatedLines.add(newLine);
                    updated = true;
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }

        if (!updated) {
            updatedLines.add(desiredHeader);
            String newLine = String.join("\t",
                    applicant.getName(),
                    applicant.getNRIC(),
                    String.valueOf(applicant.getAge()),
                    applicant.getMaritalStatus(),
                    applicant.getPassword(),
                    flatType,
                    (project == null ? "" : project.getProjectName()),
                    status
            );
            updatedLines.add(newLine);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
            return false;
        }
    }
}
