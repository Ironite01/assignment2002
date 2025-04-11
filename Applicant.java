package assignment2002;

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
                System.out.printf("Project: %s\nNeighbourhood: %s\n", project.projectName, project.neighbourhood);
                if (eligibleFor2Rooms) {
                    System.out.printf(" - 2-Room: %d units available, Price: $%d\n", project.twoRoomAmt, project.twoRoomPrice);
                }
                if (eligibleFor3Rooms) {
                    System.out.printf(" - 3-Room: %d units available, Price: $%d\n", project.threeRoomAmt, project.threeRoomPrice);
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
            if (this.getAge() >= 35 && flatType.equals("2-Room") && project.twoRoomAmt > 0) {
                return true;
            }
        } else if (this.getMaritalStatus().equalsIgnoreCase("Married")) {
            if (this.getAge() >= 21) {
                if ((flatType.equals("2-Room") && project.twoRoomAmt > 0) ||
                    (flatType.equals("3-Room") && project.threeRoomAmt > 0)) {
                    return true;
                }
            }
        }
    
        return false;
    }

    public boolean apply(BTOProperty project, String flatType) {
        if (applicationStatus != APPLICATION_STATUS.NOTAPPLIED) {
            System.out.println("You have already applied for a flat. ");
            return false;
        }

        if (!isEligible(project, flatType)) {
            System.out.println("You are not eligible to apply for this flat type.");
            return false;
        }

        this.appliedProjects = project.projectName;
        this.flatType = flatType;
        this.applicationStatus = APPLICATION_STATUS.PENDING;

        project.addApplicant(this, flatType);

        System.out.println("Application submitted successfully!");

        String filePath = "assignment2002/Information/ApplicantList.txt";
        File file = new File(filePath);
        ArrayList<String> updatedLines = new ArrayList<>();
        boolean fileExists = file.exists();
        boolean updated = false;

        if (fileExists) {
            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    if (line.startsWith("Name\t")) {
                        updatedLines.add(line); // keep header
                        continue;
                    }

                    String[] parts = line.split("\t");
                    if (parts.length >= 2 && parts[1].equals(this.getNRIC())) {
                        // Found matching NRIC — replace the line
                        String newLine = String.join("\t",
                            this.getName(),
                            this.getNRIC(),
                            String.valueOf(this.getAge()),
                            this.getMaritalStatus(),
                            this.getPassword(),
                            flatType,
                            project.getProjectName()
                        );
                        updatedLines.add(newLine);
                        updated = true;
                    } else {
                        updatedLines.add(line); // keep others
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading applicant file: " + e.getMessage());
            }
        }

        // If not updated, add as new
        if (!updated) {
            if (!fileExists) {
                updatedLines.add("Name\tNRIC\tAge\tMaritalStatus\tPassword\tFlatType\tProjectName"); // header
            }
            String newLine = String.join("\t",
                this.getName(),
                this.getNRIC(),
                String.valueOf(this.getAge()),
                this.getMaritalStatus(),
                this.getPassword(),
                flatType,
                project.getProjectName()
            );
            updatedLines.add(newLine);
        }

        // Write the updated list back to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing updated applicant file: " + e.getMessage());
        }

        return true;

    }
    

    public void withdrawApplication() {
        if (applicationStatus == APPLICATION_STATUS.NOTAPPLIED) {
            System.out.println("No active application to withdraw.");
            return;
        }

        this.applicationStatus = APPLICATION_STATUS.NOTAPPLIED;
        this.appliedProjects = " ";
        this.flatType = " ";
        System.out.println("Application withdrawn successfully.");
    }

    public void updateApplicationStatus(String newStatus) {
        this.applicationStatus = APPLICATION_STATUS.valueOf(newStatus.toUpperCase()); // do i need to account for error checking?
        
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
    public void viewMenu(ArrayList<User> userList, ArrayList<BTOProperty> btoList, Scanner sc) {
        boolean run = true;

        do { 
            System.out.println("==== APPLICANT MENU ====");
            System.out.println("1: View Available Projects"); 
            System.out.println("2: Apply for a Project"); //done?
            System.out.println("3: View Application Status"); //getapplicationstatus()
            System.out.println("4: Withdraw Application"); //done?
            System.out.println("5: Submit Enquiry"); 
            System.out.println("6: View/Edit/Delete Enquiries");
            System.out.println("7: Exit");

            int choice = sc.nextInt();

            switch(choice){
                case 1 -> {
                    viewProjects(btoList);
                    break;
                }
                case 2 -> {
                    // TODO: ADD the check to see if the applicant applied for the house alr before all the code below
                    viewProjects(btoList);
                    sc.nextLine();
                    System.out.println("Enter Project Name to apply for: ");
                    String projName = sc.nextLine();

                    BTOProperty selected = null;
                    for (BTOProperty projects: btoList){
                        if (projects.projectName.equalsIgnoreCase(projName)){
                            selected = projects;
                            break;
                        }
                    }

                    if (selected == null){
                        System.out.println("Project not found.");
                        break;
                    }

                    System.out.print("Enter flat type (2-Room / 3-Room): ");
                    String selectedflatType = sc.nextLine();

                    this.apply(selected, selectedflatType);
                    break;
                }
                case 3 -> {
                    System.out.println("Status: " + getApplicationStatus());
                    break;
                }
                case 4 -> {
                    withdrawApplication();
                    break;
                }
                case 7 -> {
                    run = false;
                    break;
                }
                default -> {
                    System.out.println("Retry");
                    break;
                }
            }
        } while (run);
        
    }

}
