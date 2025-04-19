package assignment2002;

import java.util.Scanner;
import assignment2002.user.Manager;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.Data;
import assignment2002.utils.FileManifest;

public class ManagerController {
    private Manager manager;
    private BTOProjectController projectController;
    private ApplicationMgmtController appMgmtController;
    private OfficerRegisController officerController;
    private final Scanner sc = new Scanner(System.in);


    public ManagerController(Manager manager){
        this.manager = manager;
        this.projectController = new BTOProjectController(manager, sc);
        this.appMgmtController = new ApplicationMgmtController(manager, sc);
        this.officerController = new OfficerRegisController(manager, sc);
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
                case 4 -> officerController.viewOfficerRegisMenu();
                case 5 -> viewProjsMenu();
                case 100-> run = false;
                default -> System.out.println("Retry");
        } 

    } while (run);
    sc.close();
    System.out.println("User Has Logged out!");
        
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
        BTOFileService.editBTOByColumn(selected.getProjectName(), FileManifest.PROPERTY_COLUMNS.VISIBLE, Boolean.toString(selected.isVisible()).toUpperCase(), false);
        System.out.printf("Project '%s' is now %s.\n", selected.getProjectName(), selected.isVisible() ? "VISIBLE" : "HIDDEN");


    }


    
    
    
}
