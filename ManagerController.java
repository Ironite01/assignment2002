package assignment2002;

import java.util.Scanner;
import assignment2002.user.Manager;
import assignment2002.user.UserService;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.Data;
import assignment2002.utils.FileManifest;

public class ManagerController {
    private Manager manager;
    private BTOProjectController projectController;
    private ApplicationMgmtController appMgmtController;
    private OfficerRegisController officerController;
    private ProjectFilterController filterController;
    private final Scanner sc = new Scanner(System.in);


    public ManagerController(Manager manager){
        this.manager = manager;
        this.projectController = new BTOProjectController(manager, sc);
        this.appMgmtController = new ApplicationMgmtController(manager, sc);
        this.officerController = new OfficerRegisController(manager, sc);
        this.filterController = new ProjectFilterController(manager, sc);
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
            System.out.println("6: Change Password");
            System.out.println("7: Logout"); //Temp Numbering

            int choice = sc.nextInt();

            switch (choice) {
                case 1-> projectController.showProjectMenu();
                case 2 -> viewVisibilityMenu();
                case 3 -> appMgmtController.viewApplicationsMenu();
                case 4 -> officerController.viewOfficerRegisMenu();
                case 5 -> filterController.viewProjsMenu();
                case 6 -> UserService.resetPasswordPrompt(manager);
                case 7 -> run = false;
                default -> System.out.println("Retry");
        } 

    } while (run);
    sc.close();
    System.out.println("User Has Logged out!");
        
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
