package assignment2002;

import assignment2002.enquiry.Enquiry;
import assignment2002.enquiry.EnquiryService;
import assignment2002.user.Manager;
import assignment2002.user.UserService;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.Data;
import assignment2002.utils.FileManifest;
<<<<<<< Updated upstream
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
=======
import assignment2002.utils.InputUtil;
>>>>>>> Stashed changes

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
            System.out.println("7: View All Enquiries");
            System.out.println("8: View & Reply to Your Project Enquiries");
            System.out.println("9: Logout"); //Temp Numbering

            int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 7);

            switch (choice) {
                case 1-> projectController.showProjectMenu();
                case 2 -> viewVisibilityMenu();
                case 3 -> appMgmtController.viewApplicationsMenu();
                case 4 -> officerController.viewOfficerRegisMenu();
                case 5 -> filterController.viewProjsMenu();
                case 6 -> UserService.resetPasswordPrompt(manager);
                case 7 -> viewAllEnquiries();
                case 8 -> viewAndReplyOwnEnquiries(Data.btoList);
                case 9 -> run = false;
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

    private void viewAllEnquiries() {
        EnquiryService.loadEnquiriesFromFile();
        var all = EnquiryService.viewAll();

        if (all.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }

        System.out.println("=== All Enquiries ===");
        for (Enquiry e : all) {
            System.out.println("Project: " + e.getProjectName());
            System.out.println("From: " + e.getApplicantNric());
            System.out.println("Resolved: " + e.isResolved());

            for (var entry : e.getAllMessages().entrySet()) {
                System.out.printf("[%s] %s: %s\n",
                        entry.getKey(),
                        entry.getValue().getAuthor().getName(),
                        entry.getValue().getMessage());
            }
            System.out.println("-----");
        }
    }

    // View and reply only to projects the manager owns
    private void viewAndReplyOwnEnquiries(ArrayList<BTOProperty> btoList) {
        EnquiryService.loadEnquiriesFromFile();
        List<Enquiry> all = EnquiryService.viewAll();

        List<String> managedProjectNames = manager.getMyProjects(btoList).stream()
            .map(BTOProperty::getProjectName)
            .toList();

        boolean found = false;
        for (Enquiry enquiry : all) {
            if (managedProjectNames.contains(enquiry.getProjectName())) {
                found = true;

                System.out.println("\nProject: " + enquiry.getProjectName());
                System.out.println("From: " + enquiry.getApplicantNric());
                System.out.println("Resolved: " + enquiry.isResolved());

                enquiry.getAllMessages().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        String timestamp = entry.getKey().toString();
                        String author = entry.getValue().getAuthor().getName();
                        String message = entry.getValue().getMessage();
                        System.out.printf("[%s] %s: %s\n", timestamp, author, message);
                    });

                if (!enquiry.isResolved()) {
                    Scanner sc = new Scanner(System.in);
                    System.out.print("Reply to this enquiry? (y/n): ");
                    if (sc.nextLine().equalsIgnoreCase("y")) {
                        System.out.print("Enter your reply: ");
                        String reply = sc.nextLine();
                        enquiry.addMessage(manager.getNRIC(), reply);
                        EnquiryService.saveEnquiriesToFile();
                        System.out.println("Reply sent.");
                    }
                }
                System.out.println("-----");
            }
        }

        if (!found) {
            System.out.println("No enquiries found for your managed projects.");
        }
    }



    
    
}
