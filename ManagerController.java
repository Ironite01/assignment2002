package assignment2002;

import assignment2002.user.Manager;
import assignment2002.user.UserService;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.Data;
import assignment2002.utils.FileManifest;
import assignment2002.utils.InputUtil;
import java.util.Scanner;


public class ManagerController {
    private Manager manager;
    private BTOProjectController projectController;
    private ApplicationMgmtController appMgmtController;
    private OfficerRegisController officerController;
    private ProjectFilterController filterController;
    private ManagerEnquiryController enqController;
    private final Scanner sc = new Scanner(System.in);


    public ManagerController(Manager manager){
        this.manager = manager;
        this.projectController = new BTOProjectController(manager, sc);
        this.appMgmtController = new ApplicationMgmtController(manager, sc);
        this.officerController = new OfficerRegisController(manager, sc);
        this.filterController = new ProjectFilterController(manager, sc);
        this.enqController = new ManagerEnquiryController(manager, sc);
    }

    
    public void showMenu(){
        boolean run = true;

        do {
            System.out.println("==== MANAGER MENU ====");
            System.out.println("1: Manage BTO Properties");
            System.out.println("2: Manage Project Visibility");
            System.out.println("3: Manage Applications");
            System.out.println("4: Manage Officer Registration");
            System.out.println("5: Manage Equiries");
            System.out.println("6: View Projects Menu");
            System.out.println("7: Change Password");
            System.out.println("8: Logout"); //Temp Numbering

            int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 8);

            switch (choice) {
                case 1-> projectController.showProjectMenu();
                case 2 -> viewVisibilityMenu();
                case 3 -> appMgmtController.viewApplicationsMenu();
                case 4 -> officerController.viewOfficerRegisMenu();
                case 5 -> enqController.viewMenu();
                case 6 -> filterController.viewProjsMenu();
                case 7 -> UserService.resetPasswordPrompt(manager);
                case 8 -> run = false;
                default -> System.out.println("Retry");
        } 

    } while (run);
    sc.close();
    System.out.println("User Has Logged out!");
        
    }


    private void viewVisibilityMenu(){
        manager.viewProjectsVisibility(Data.btoList);

        int choice = InputUtil.getValidatedIntRange(sc, "Enter project number to toggle visibility (0 to cancel): ", 0, Data.btoList.size());

        if (choice == 0) {
            System.out.println("Cancelled.");
            return;
        }

        BTOProperty selected = Data.btoList.get(choice - 1);
        manager.toggleProjectVisiblity(selected);
        BTOFileService.editBTOByColumn(selected.getProjectName(), FileManifest.PROJECT_COLUMNS.VISIBLE, Boolean.toString(selected.isVisible()).toUpperCase(), false);
        System.out.printf("Project '%s' is now %s.\n", selected.getProjectName(), selected.isVisible() ? "VISIBLE" : "HIDDEN");


    }
    
    
}
