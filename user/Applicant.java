package assignment2002.user;

import assignment2002.ApplicantController;
import assignment2002.BTOProperty;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Applicant extends User{

    private String appliedProjects;
    private String flatType;

    private APPLICATION_STATUS applicationStatus;

    private enum APPLICATION_STATUS {
        SUCCESSFUL,
        UNSUCCESSFUL,
        PENDING,
        BOOKED,
        NOTAPPLIED
    }

    public Applicant(String name, String NRIC, int age, String maritalStatus, String password){
        super(name, NRIC, age, maritalStatus, password);
        this.appliedProjects = " ";
        this.flatType = " ";
        this.applicationStatus = APPLICATION_STATUS.NOTAPPLIED;
    }

    public void viewProjects(ArrayList<BTOProperty> allBTOs){
        System.out.println("Some available projects: ");
        boolean found = false;

        for (BTOProperty project : allBTOs) {
            if (!project.isVisible()) continue;

            boolean eligibleFor2Rooms = isEligible(project, "2-Room");
            boolean eligibleFor3Rooms = isEligible(project, "3-Room");

            if (eligibleFor2Rooms || eligibleFor3Rooms){
                found = true;
                System.out.printf("Project: %s\nNeighbourhood: %s\n", project.getProjectName(), project.getNeighbourhood());
                if (eligibleFor2Rooms) {
                    System.out.printf(" - 2-Room: %d units available, Price: $%d\n", project.getTwoRoomAmt(), project.getTwoRoomPrice());
                }
                if (eligibleFor3Rooms) {
                    System.out.printf(" - 3-Room: %d units available, Price: $%d\n", project.getThreeRoomAmt(), project.getThreeRoomPrice());
                }
                System.out.println("-------------------------");
            }
        }

        
        if (!found) {
            System.out.println("No available projects matching your eligibility.");
        }
    }

    public boolean isEligible(BTOProperty project, String flatType) { //needed flattype to check if the applicant can apply 2 room or 3 room or both

        if (!project.isVisible()) {
            return false;
        }

        if (this.getMaritalStatus().equalsIgnoreCase("Single")) {
            if (this.getAge() >= 35 && flatType.equals("2-Room") && project.getTwoRoomAmt()> 0) {
                return true;
            }
        } else if (this.getMaritalStatus().equalsIgnoreCase("Married")) {
            if (this.getAge() >= 21) {
                if ((flatType.equals("2-Room") && project.getTwoRoomAmt() > 0) ||
                    (flatType.equals("3-Room") && project.getThreeRoomAmt() > 0)) {
                    return true;
                }
            }
        }
    
        return false;
    }

    public boolean apply(BTOProperty project, String flatType) {
        String status = getApplicationStatusFromFile();
        if (!status.equals("NOTAPPLIED") && !status.equals("NOT FOUND")) {
            System.out.println("You have already applied for a flat. Status: " + status);
            return false;
        }

        this.appliedProjects = project.getProjectName();
        this.flatType = flatType;
        this.applicationStatus = APPLICATION_STATUS.PENDING;

        project.addApplicant(this, flatType);

        System.out.println("Application submitted successfully!");

        String filePath = "assignment2002/Information/ApplicantList.txt";
        File file = new File(filePath);
        boolean fileExists = file.exists();
        String desiredHeader = "Name\tNRIC\tAge\tMaritalStatus\tPassword\tFlatType\tProjectName\tApplicationStatus";
        ArrayList<String> updatedLines = new ArrayList<>();
        boolean updated = false;

        if (fileExists) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("Name\t")) {
                        updatedLines.add(desiredHeader);
                        continue;
                    }

                    String[] parts = line.split("\t");
                    if (parts.length >= 2 && parts[1].equals(this.getNRIC())) {
                        String newLine = String.join("\t",
                            this.getName(),
                            this.getNRIC(),
                            String.valueOf(this.getAge()),
                            this.getMaritalStatus(),
                            this.getPassword(),
                            flatType,
                            project.getProjectName(),
                            "PENDING"
                        );
                        updatedLines.add(newLine);
                        updated = true;
                    } else {
                        updatedLines.add(line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading applicant file: " + e.getMessage());
                return false;
            }
        }

        if (!updated) {
            if (!fileExists) {
                updatedLines.add(desiredHeader);
            }
            String newLine = String.join("\t",
                this.getName(),
                this.getNRIC(),
                String.valueOf(this.getAge()),
                this.getMaritalStatus(),
                this.getPassword(),
                flatType,
                project.getProjectName(),
                "PENDING"
            );
            updatedLines.add(newLine);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing applicant file: " + e.getMessage());
            return false;
        }

        return true;
    }

    public void withdrawApplication() {
        String status = getApplicationStatusFromFile();
        if (!status.equals("PENDING")) {
            System.out.println("No active application to withdraw. Current status: " + status);
            return;
        }

        String filePath = "assignment2002/Information/ApplicantList.txt";
        File file = new File(filePath);
        ArrayList<String> updatedLines = new ArrayList<>();
        String desiredHeader = "Name\tNRIC\tAge\tMaritalStatus\tPassword\tFlatType\tProjectName\tApplicationStatus";
        boolean found = false;
        boolean withdrawn = false;

        if (!file.exists()) {
            System.out.println("Applicant file not found.");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                if (line.startsWith("Name\t")) {
                    updatedLines.add(desiredHeader);
                    continue;
                }

                String[] parts = line.split("\t");

                if (parts.length >= 8 && parts[1].equals(this.getNRIC())) {
                    found = true;

                    if (!parts[7].equalsIgnoreCase("WITHDRAWN")) {
                        String updatedLine = String.join("\t",
                            parts[0],
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4],
                            "",
                            "",
                            "NOTAPPLIED"
                        );
                        updatedLines.add(updatedLine);
                        withdrawn = true;

                        this.flatType = "";
                        this.appliedProjects = "";
                        this.applicationStatus = APPLICATION_STATUS.NOTAPPLIED;
                    } else {
                        updatedLines.add(line);
                    }
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading applicant file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("No applicant record found.");
            return;
        }

        if (!withdrawn) {
            System.out.println("No active application to withdraw.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String l : updatedLines) {
                writer.write(l);
                writer.newLine();
            }
            System.out.println("Application withdrawn successfully.");
        } catch (IOException e) {
            System.out.println("Error writing updated file: " + e.getMessage());
        }
    }

    public String getApplicationStatusFromFile() {
        String filePath = "assignment2002/Information/ApplicantList.txt";
        File file = new File(filePath);

        if (!file.exists()) return "NOT FOUND";

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Name\t")) continue;

                String[] parts = line.split("\t");
                if (parts.length >= 8 && parts[1].equals(this.getNRIC())) {
                    return parts[7].trim();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading applicant file: " + e.getMessage());
        }

        return "NOT FOUND";
    }
    

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public void setAppliedProject(String appliedProjects) {
        this.appliedProjects = appliedProjects;
    }

    public void setApplicationStatus(String status) {
        this.applicationStatus = APPLICATION_STATUS.valueOf(status.toUpperCase());
    }

    public String getApplicationStatus() {
        return applicationStatus.toString();
    }

    public String getAppliedProject() {
        return appliedProjects;
    }

    public String getFlatType() {
        return flatType;
    }
    

    @Override
    public void viewMenu(ArrayList<User> userList, ArrayList<BTOProperty> btoList) {
        ApplicantController aController = new ApplicantController(this, btoList, userList);
        aController.showMenu();
    }

}
