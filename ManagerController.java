package assignment2002;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.List;
import assignment2002.user.Manager;
import assignment2002.user.Officer;
import assignment2002.user.User;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.Data;
import assignment2002.utils.FileManifest;

public class ManagerController {
    private Manager manager;
    private BTOProjectController projectController;
    private ApplicationMgmtController appMgmtController;
    private final Scanner sc = new Scanner(System.in);


    public ManagerController(Manager manager){
        this.manager = manager;
        this.projectController = new BTOProjectController(manager, sc);
        this.appMgmtController = new ApplicationMgmtController(manager, sc);
    }

    
    public void showMenu(){
        boolean run = true;

        do {
            System.out.println("==== MANAGER MENU ====");
            System.out.println("1: Manage BTO Properties");
            System.out.println("2: Manage Project Visibility");
            System.out.println("3: Manage Applications");
            System.out.println("4: Manage Officer Registration");
            System.out.println("5: View Projects Menu");
            System.out.println("100: Logout"); //Temp Numbering

            int choice = sc.nextInt();

            switch (choice) {
                case 1-> projectController.showProjectMenu();
                case 2 -> viewVisibilityMenu();
                case 3 -> appMgmtController.viewApplicationsMenu();
                case 4 -> viewOfficerRegisMenu();
                case 5 -> viewProjsMenu();
                case 100-> run = false;
                default -> System.out.println("Retry");
        } 

    } while (run);
    sc.close();
    System.out.println("User Has Logged out!");
        
    }

    private void viewOfficerRegisMenu(){
        boolean running = true;
        while (running) {
            System.out.println("\n=== Officer Registration Menu ===");
            System.out.println("1: View All Registrations");
            System.out.println("2: Approve Registrations");
            System.out.println("3: Reject Registrations");
            System.out.println("4: Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1 -> viewAllRegistrations();
                case 2 -> approveOfficerRegisMenu();
                case 3 -> rejectOfficerRegisMenu();
                case 4 -> running = false;
                default -> System.out.println("Invalid Input");
            }

            
        }
    }
    
    private boolean viewAllRegistrations(){
        List<BTOProperty> myBTO = manager.getMyProjects(Data.btoList);

        if(myBTO.isEmpty()){
            System.out.println("You are not managing any BTOs");
            return false;
        }

        boolean pendingOfficer = false;

        for (BTOProperty p: myBTO) {
            List<Officer> pendingOfficers = p.getAppliedOfficers();

            if(pendingOfficers.isEmpty()){
                System.out.printf("Project: %s Has No Pending Registrations\n", p.getProjectName());
                continue;
            }

            pendingOfficer = true;

            System.out.printf("%s PENDING REGISTRATIONS\n", p.getProjectName());

            for (Officer o : pendingOfficers) {
                System.out.printf("| Name: %-15s | NRIC: %-10s |\n", o.getName(), o.getNRIC());
            }
        }
        return pendingOfficer;

    }

    private void approveOfficerRegisMenu() {
        if(!viewAllRegistrations()){
            System.out.println("No Pending Registrations");
            return;
        }

        System.out.print("Enter Project Name: ");
        String projName = sc.nextLine().trim();

        System.out.print("Enter NRIC of Officer to Approve: ");
        String nric = sc.nextLine().trim();

        // Find matching project
        BTOProperty targetProject = manager.getMyProjects(Data.btoList).stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(projName))
            .findFirst()
            .orElse(null);

        if (targetProject == null) {
            System.out.println("Project not found.");
            return;
        }

        Officer officer = targetProject.getAppliedOfficers().stream()
            .filter(o -> o.getNRIC().equalsIgnoreCase(nric))
            .findFirst()
            .orElse(null);

        if (officer == null) {
            System.out.println("Officer not found in pending registrations.");
            return;
        }

        targetProject.getAppliedOfficers().remove(officer);
        targetProject.getOfficers().add(officer);
        BTOFileService.editBTOByColumn(targetProject.getProjectName(), FileManifest.PROPERTY_COLUMNS.PENDING_OFFICERS,
            targetProject.getAppliedOfficers().stream().map(Officer::getName).collect(Collectors.joining(",")));
        BTOFileService.editBTOByColumn(targetProject.getProjectName(), FileManifest.PROPERTY_COLUMNS.APPROVED_OFFICERS,
            targetProject.getOfficers().stream().map(Officer::getName).collect(Collectors.joining(",")));

        System.out.printf("Approved officer %s for project '%s'.\n", officer.getName(), projName);
    }


    private void rejectOfficerRegisMenu() {
        if(!viewAllRegistrations()){
            System.out.println("No Pending Registrations");
            return;
        }
    
        System.out.print("Enter Project Name: ");
        String projName = sc.nextLine().trim();
    
        System.out.print("Enter NRIC of Officer to Reject: ");
        String nric = sc.nextLine().trim();
    
        BTOProperty targetProject = manager.getMyProjects(Data.btoList).stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(projName))
            .findFirst()
            .orElse(null);
    
        if (targetProject == null) {
            System.out.println("Project not found.");
            return;
        }
    
        Officer officer = targetProject.getAppliedOfficers().stream()
            .filter(o -> o.getNRIC().equalsIgnoreCase(nric))
            .findFirst()
            .orElse(null);
    
        if (officer == null) {
            System.out.println("Officer not found in pending registrations.");
            return;
        }
    
        // Reject: remove from pending, add to rejected
        targetProject.getAppliedOfficers().remove(officer);
        targetProject.getRejectedOfficers().add(officer);
        BTOFileService.editBTOByColumn(targetProject.getProjectName(), FileManifest.PROPERTY_COLUMNS.PENDING_OFFICERS,
            targetProject.getAppliedOfficers().stream().map(Officer::getName).collect(Collectors.joining(",")));
        BTOFileService.editBTOByColumn(targetProject.getProjectName(), FileManifest.PROPERTY_COLUMNS.REJECTED_OFFICERS,
            targetProject.getRejectedOfficers().stream().map(Officer::getName).collect(Collectors.joining(",")));
    
        System.out.printf("Rejected officer %s for project '%s'.\n", officer.getName(), projName);
    }
    



    private void viewProjsMenu(){
        boolean running = true;
        while (running) {
            System.out.println("\n=== View Projects: Filter Menu ===");
            System.out.println("1: View All Projects");
            System.out.println("2: View Personally Created Projects");
            System.out.println("3: Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1 -> manager.viewAllProjects(Data.btoList);
                case 2 -> manager.viewMyProjs(Data.btoList);
                case 3 -> running = false;
                default -> System.out.println("Invalid Input");
            }

            
        }
    }
    

    private void viewVisibilityMenu(){
        manager.viewProjectsVisibility(Data.btoList);

        System.out.print("Enter project number to toggle visibility (0 to cancel): ");
        int choice = sc.nextInt();

        if (choice == 0) {
            System.out.println("Cancelled.");
            return;
        }

        if (choice < 1 || choice > Data.btoList.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        BTOProperty selected = Data.btoList.get(choice - 1);
        manager.toggleProjectVisiblity(selected);
        BTOFileService.editBTOByColumn(selected.getProjectName(), FileManifest.PROPERTY_COLUMNS.VISIBLE, Boolean.toString(selected.isVisible()).toUpperCase());
        System.out.printf("Project '%s' is now %s.\n", selected.getProjectName(), selected.isVisible() ? "VISIBLE" : "HIDDEN");


    }


    
    
    
}
