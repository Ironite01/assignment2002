package assignment2002.application;

import assignment2002.BTOProperty;
import assignment2002.user.Applicant;
import assignment2002.user.Manager;
import assignment2002.user.Officer;
import assignment2002.user.User;
import assignment2002.utils.Data;
import assignment2002.utils.FileManifest;
import assignment2002.utils.Status;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationService implements FileManifest, Status {

    private static final List<Application> applications = new ArrayList<>();

    public static boolean isEligible(Applicant applicant, BTOProperty project, String flatType) {
    	if (applicant instanceof Officer) {
    		Set<String> projs = ((Officer) applicant).getAllProjectStatus().keySet();
    		// Officer cannot 
    		if (projs.contains(project.getProjectName())) {
    			return false;
    		}
    	}
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
        if (status.equals(APPLICATION_STATUS.PENDING.toString())
        		|| status.equals(APPLICATION_STATUS.PENDINGWITHDRAWN.toString())
        		|| status.equals(APPLICATION_STATUS.BOOKED.toString())) {
            System.out.println("You have already applied for a flat. Status: " + status);
            return false;
        }

        if (!isEligible(applicant, project, flatType)) {
            System.out.println("You are not eligible to apply for this flat type.");
            return false;
        }
        
        project.addApplicant(applicant, flatType);

        Application app = new Application(applicant, project, flatType);
        applications.add(app);
        return saveToFile(app);
    }

    public static void withdraw(Applicant applicant) {
        String status = getApplicationStatus(applicant);
        if (!status.equals(APPLICATION_STATUS.PENDING.toString())) {
            System.out.println("No active application to withdraw.");
            return;
        }

        editApplicationByColumn(applicant.getCurrentApplication(), APPLICATION_COLUMNS.STATUS, APPLICATION_STATUS.PENDINGWITHDRAWN.toString());
        System.out.println("Withdrawal Request Submmitted");
    }

    public static List<Application> getPendingWithdrawalApplications() {
        List<Application> pendingWithdrawals = new ArrayList<>();
    
        try{
            Scanner scanner = new Scanner(new File(APPLICATION_TXT_PATH));
            scanner.nextLine(); // skip header
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split("\t");
                if (parts.length >= APPLICATION_COLUMNS_MAP.get(APPLICATION_COLUMNS.STATUS.toString())
                		&& parts[APPLICATION_COLUMNS_MAP.get(APPLICATION_COLUMNS.STATUS.toString())].equalsIgnoreCase(APPLICATION_STATUS.PENDINGWITHDRAWN.toString())) {
                    String nric = parts[APPLICATION_COLUMNS_MAP.get(APPLICATION_COLUMNS.NRIC.toString())];
                    String name = parts[APPLICATION_COLUMNS_MAP.get(APPLICATION_COLUMNS.NAME.toString())];
                    String flatType = parts[APPLICATION_COLUMNS_MAP.get(APPLICATION_COLUMNS.FLATTYPE.toString())];
                    String projectName = parts[APPLICATION_COLUMNS_MAP.get(APPLICATION_COLUMNS.PROJECTNAME.toString())];
    
                    Applicant dummy = new Applicant(name, nric, 0, "", "");
                    BTOProperty p = Data.btoList.stream().filter(prop -> prop.getProjectName().equalsIgnoreCase(projectName)).findFirst().orElse(null);
                    Application app = new Application(dummy, p, flatType);
                    app.setStatus(Status.APPLICATION_STATUS.PENDINGWITHDRAWN);
                    pendingWithdrawals.add(app);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading application.txt: " + e.getMessage());
        }
    
        return pendingWithdrawals;
    }    

    public static void finalizeWithdrawals(Set<String> selectedNrics) {
        removeFromApplicationFile(selectedNrics);
    }
    
    private static void removeFromApplicationFile(Set<String> targetNrics) {
        File file = new File(APPLICATION_TXT_PATH);
        List<String> updatedLines = new ArrayList<>();
    
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("NRIC\t")) {
                    updatedLines.add(line); // keep the header
                    continue;
                }
    
                String[] parts = line.split("\t");
                if (parts.length >= 5) {
                    String nric = parts[0];
                    String status = parts[4];
    
                    // Only keep entries that are not pending withdrawal OR not in the selected NRICs
                    if (!(targetNrics.contains(nric) && status.equalsIgnoreCase(APPLICATION_STATUS.PENDINGWITHDRAWN.toString()))) {
                        updatedLines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading application.txt: " + e.getMessage());
            return;
        }
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to application.txt: " + e.getMessage());
        }
    }
    

    private static void updateStatusInFile(String path, Set<String> targetNrics, String fromStatus, String toStatus, int statusColumnIndex) {
        File file = new File(path);
        List<String> updatedLines = new ArrayList<>();
    
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\t");
    
                if (line.startsWith("Name\t") || parts.length <= statusColumnIndex) {
                    updatedLines.add(line);
                    continue;
                }
    
                String nric = parts[1]; // NRIC is column 1 in Applicant/Officer txt
                String currentStatus = parts[statusColumnIndex];
    
                if (targetNrics.contains(nric) && currentStatus.equalsIgnoreCase(fromStatus)) {
                    parts[statusColumnIndex] = toStatus;
                    updatedLines.add(String.join("\t", parts));
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to read " + path + ": " + e.getMessage());
            return;
        }
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to write to " + path + ": " + e.getMessage());
        }
    }
    
    public static String getApplicationStatus(Applicant applicant) {
        return applicant.getApplicationStatus();
    }

    public static boolean saveToFile(Application app) {
        File file = new File(APPLICATION_TXT_PATH);
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
                            app.getFlatType() == null ? "" : app.getFlatType(),
                            app.getProperty() == null ? "" : app.getProperty().getProjectName(),
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
    
    public static List<Application> getSuccessfulApplicationsFromOfficer(Officer o) {
    	return applications
    			.stream()
    			.filter(app -> o.getRegisteredProject(app.getProperty().getProjectName()) != null && app.getStatus() == APPLICATION_STATUS.SUCCESSFUL)
    			.toList();
    }
    
    public static List<Application> getMyManagedApplications(Manager m) {
        return applications.stream().filter(p -> p.getProperty().getManagerIC().stream()
        .anyMatch(managerIC -> managerIC.getNRIC().equalsIgnoreCase(m.getNRIC()))).toList();
    }

    public static List<Application> getMyManagedApplicationsByStatus(Manager m, Status.APPLICATION_STATUS status) {
        return getMyManagedApplications(m).stream()
            .filter(app -> app.getStatus() == status)
            .toList();
    }
    
    public static Application getApplicationByApplicantAndProperty(Applicant applicant, BTOProperty property) {
        return applications.stream()
                .filter(app -> app.getApplicant().getNRIC().equalsIgnoreCase(applicant.getNRIC())
                		&& app.getProperty().getProjectName().equalsIgnoreCase(property.getProjectName()))
                .findFirst()
                .orElse(null);
    }
    

    public static List<Application> getApplicationsByApplicant(Applicant applicant) {
        return applications.stream()
                .filter(app -> app.getApplicant().getNRIC().equalsIgnoreCase(applicant.getNRIC()))
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
        
        application.setStatus(Status.APPLICATION_STATUS.SUCCESSFUL);
        return true;
    }

    public static void rejectApplication(Application application) {
        BTOProperty property = application.getProperty();
        String flatType = application.getFlatType();


        application.setStatus(Status.APPLICATION_STATUS.UNSUCCESSFUL);
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

        application.setStatus(Status.APPLICATION_STATUS.BOOKED);
        return true;
    }
    
    public static void editApplicationByColumn(Application app, APPLICATION_COLUMNS col, String newValue) {
        try {
            List<String> allLines = Files.readAllLines(Paths.get(APPLICATION_TXT_PATH));
            String header = allLines.get(0);
            List<String> updatedLines = new ArrayList<>();
    
            for (int i = 1; i < allLines.size(); i++) {
                String line = allLines.get(i).trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\t");
    
                    if (parts[0].equalsIgnoreCase(app.getApplicant().getNRIC())) {
                        parts[APPLICATION_COLUMNS_MAP.get(col.toString())] = newValue;
                        updatedLines.add(String.join("\t", parts));
    
                        for (int j = i + 1; j < allLines.size(); j++) {
                            updatedLines.add(allLines.get(j));
                        }
                        break;
                    } else {
                        updatedLines.add(line);
                    }
                }
            }
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPLICATION_TXT_PATH))) {
                writer.write(header);
                if (!updatedLines.isEmpty()) writer.newLine();
                for (int i = 0; i < updatedLines.size(); i++) {
                    writer.write(updatedLines.get(i));
                    if (i != updatedLines.size() - 1) writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error editing field: " + e.getMessage());
        }
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
    File file = new File(APPLICATION_TXT_PATH);
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
                    app.setStatus(Status.APPLICATION_STATUS.valueOf(status));
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
