package assignment2002.application;

import assignment2002.BTOProperty;
import assignment2002.user.Applicant;
import assignment2002.user.Officer;
import assignment2002.user.User;
import assignment2002.utils.FilePath;
import assignment2002.user.Manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ApplicationService {

    private static final List<Application> applications = new ArrayList<>();
    private static final String FILE_PATH = "Information/Application.txt";

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

        if (!isEligible(applicant, project, flatType)) {
            System.out.println("You are not eligible to apply for this flat type.");
            return false;
        }

        applicant.setFlatType(flatType);
        applicant.setAppliedProject(project.getProjectName());
        applicant.setApplicationStatus("PENDING");
        project.addApplicant(applicant, flatType);

        Application app = new Application(applicant, project, flatType);
        applications.add(app);
        saveToFile(app);

        return updateApplicantFile(applicant, project, flatType, "PENDING");
    }

    public static String getApplicationStatus(Applicant applicant) {
        return applicant.getApplicationStatus();
    }

    private static boolean updateApplicantFile(Applicant applicant, BTOProperty project, String flatType, String status) {
        String filePath = "";

        if (applicant instanceof Officer) {
            filePath = FilePath.OFFICER_TXT_PATH;
        } else if (applicant instanceof Applicant) {
            filePath = FilePath.APPLICANT_TXT_PATH;
        }
        
        
        String desiredHeader = "Name\tNRIC\tAge\tMaritalStatus\tPassword\tFlatType\tProjectName\tApplicationStatus";
        ArrayList<String> updatedLines = new ArrayList<>();
        boolean updated = false;

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
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

    public static boolean saveToFile(Application app) {
        File file = new File(FILE_PATH);
        boolean updated = false;
        ArrayList<String> updatedLines = new ArrayList<>();
        String header = "NRIC\tName\tFlatType\tProjectName\tStatus";

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            Scanner sc = new Scanner(file);

            boolean headerExists = false;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.startsWith("NRIC\t")) {
                    headerExists = true;
                    updatedLines.add(line);
                    continue;
                }

                String[] parts = line.split("\t");
                if (parts.length >= 5 && parts[0].equals(app.getApplicant().getNRIC())) {
                    String newLine = String.join("\t",
                            app.getApplicant().getNRIC(),
                            app.getApplicant().getName(),
                            app.getFlatType(),
                            app.getProperty().getProjectName(),
                            app.getStatus().toString()
                    );
                    updatedLines.add(newLine);
                    updated = true;
                } else {
                    updatedLines.add(line);
                }
            }
            sc.close();

            // Add header if file was empty
            if (updatedLines.isEmpty() && !headerExists) {
                updatedLines.add(header);
            }

            // Add new entry if not already updated
            if (!updated) {
                updatedLines.add(String.join("\t",
                        app.getApplicant().getNRIC(),
                        app.getApplicant().getName(),
                        app.getFlatType(),
                        app.getProperty().getProjectName(),
                        app.getStatus().toString()
                ));
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();

            return true;
        } catch (IOException e) {
            System.out.println("Error writing to application.txt: " + e.getMessage());
            return false;
        }
    }



    public static List<Application> getApplications() {
        return applications;
    }

    public static List<Application> getMyManagedApplications(Manager m) {
        return applications.stream().filter(p -> p.getProperty().getManagerIC().stream()
        .anyMatch(managerIC -> managerIC.getNRIC().equalsIgnoreCase(m.getNRIC()))).toList();
    }

    public static List<Application> getMyManagedApplicationsByStatus(Manager m, Application.ApplicationStatus status) {
        return getMyManagedApplications(m).stream()
            .filter(app -> app.getStatus() == status)
            .toList();
    }
    

    public static List<Application> getApplicationsByApplicant(Applicant applicant) {
        return applications.stream()
                .filter(app -> app.getApplicant().equals(applicant))
                .collect(Collectors.toList());
    }

    public static List<Application> getApplicationsByProperty(BTOProperty property) {
        return applications.stream()
                .filter(app -> app.getProperty().equals(property))
                .collect(Collectors.toList());
    }

    public static boolean approveApplication(Application application) {
        BTOProperty property = application.getProperty();
        String flatType = application.getFlatType();

        if (flatType.equals("2-Room") && property.getTwoRoomAmt() > 0) {
            property.setTwoRoomAmt(property.getTwoRoomAmt() - 1);
        } else if (flatType.equals("3-Room") && property.getThreeRoomAmt() > 0) {
            property.setThreeRoomAmt(property.getThreeRoomAmt() - 1);
        } else {
            System.out.println("No available units for flat type: " + flatType);
            return false;
        }
        
        updateApplicantFile(application.getApplicant(), 
        property, flatType, "SUCCESSFUL");
        
        application.setStatus(Application.ApplicationStatus.SUCCESSFUL);
        return true;
    }

    public static void rejectApplication(Application application) {
        BTOProperty property = application.getProperty();
        String flatType = application.getFlatType();

        updateApplicantFile(application.getApplicant(), 
        property, flatType, "UNSUCCESSFUL");

        application.setStatus(Application.ApplicationStatus.UNSUCCESSFUL);
    }

    public static boolean bookFlat(Application application) {
        if (!application.isSuccessful()) {
            System.out.println("Cannot book — application not successful.");
            return false;
        }

        if (application.isBooked()) {
            System.out.println("Application already booked.");
            return false;
        }

        application.setStatus(Application.ApplicationStatus.BOOKED);
        return true;
    }

    public static void generateReceipt(Application application) {
        Applicant a = application.getApplicant();
        BTOProperty p = application.getProperty();
        System.out.printf("""
        ====== RECEIPT ======
        Applicant: %s (%s)
        Age: %d | Marital Status: %s
        Flat Type: %s
        Project: %s (%s)
        Status: %s
        =====================
        """,
        a.getName(), a.getNRIC(), a.getAge(), a.getMaritalStatus(),
        application.getFlatType(), p.getProjectName(), p.getNeighbourhood(),
        application.getStatus());
    }

    public static void loadApplications(List<User> userList, List<BTOProperty> btoList) {
    File file = new File(FilePath.APPLICATION_TXT_PATH);
    if (!file.exists()) {
        System.out.println("No existing applications found.");
        return;
    }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] info = line.split("\t");
                if (info.length < 5) continue;

                String nric = info[0];
                String flatType = info[2];
                String projectName = info[3];
                String status = info[4];

                // Find Applicant by NRIC
                Applicant applicant = (Applicant) userList.stream()
                    .filter(u -> u instanceof Applicant && u.getNRIC().equalsIgnoreCase(nric))
                    .findFirst()
                    .orElse(null);

                // Find Project by name
                BTOProperty property = btoList.stream()
                    .filter(p -> p.getProjectName().equalsIgnoreCase(projectName))
                    .findFirst()
                    .orElse(null);

                if (applicant != null && property != null) {
                    Application app = new Application(applicant, property, flatType);
                    app.setStatus(Application.ApplicationStatus.valueOf(status));
                    applications.add(app);
                }
            }

            System.out.println("Applications loaded successfully.");

        } catch (IOException e) {
            System.out.println("Failed to load applications: " + e.getMessage());
        }
    }

    public static void printApplication(Application app) {
        System.out.printf("NRIC: %s | Name: %s | Flat: %s | Project: %s | Status: %s\n",
            app.getApplicant().getNRIC(),
            app.getApplicant().getName(),
            app.getFlatType(),
            app.getProperty().getProjectName(),
            app.getStatus());
    }
}
