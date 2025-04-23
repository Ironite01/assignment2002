package assignment2002;

import assignment2002.application.ApplicationService;
import assignment2002.user.Applicant;
import assignment2002.user.UserService;
import assignment2002.utils.Data;
import assignment2002.utils.InputUtil;
import assignment2002.utils.ProjectPrinter;
import java.util.Scanner;
public class ApplicantController {
    private Applicant applicant;
    private ApplicantEnquiryController enquiryController;

    public ApplicantController(Applicant applicant) {
        this.applicant = applicant;
        this.enquiryController = new ApplicantEnquiryController(applicant);
    }


    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        boolean run = true;

        do {
            System.out.println("==== APPLICANT MENU ====");
            System.out.println("1: View Available Projects");
            System.out.println("2: Apply for a Project");
            System.out.println("3: View Application Status");
            System.out.println("4: Withdraw Application");
            System.out.println("5: Submit Enquiry");
            System.out.println("6: View/Edit/Close Enquiries");
            System.out.println("7: Change Password");
            System.out.println("8: Logout");

            int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 8);

            switch (choice) {
                case 1 -> viewProjects();
                case 2 -> applyToProject(sc);
                case 3 -> viewStatus();
                case 4 -> ApplicationService.withdraw(applicant);
                case 5 -> enquiryController.submitEnquiry(sc);
                case 6 -> enquiryController.manageEnquiries(sc);
                case 7 -> UserService.resetPasswordPrompt(applicant);
                case 8 -> run = false;
                default -> System.out.println("Invalid input. Try again.");
            }

        } while (run);

        System.out.println("You have logged out.");
    }

    private void viewProjects() {
        ProjectPrinter.viewProjectsForApplicant(applicant, Data.btoList);
    }

    private void viewStatus() {
        String status = ApplicationService.getApplicationStatus(applicant);
        System.out.println("Your current application status: " + status);
    }

    private void applyToProject(Scanner sc) {
        viewProjects();
    
        String projName = InputUtil.getNonEmptyString(sc, "Enter Project Name to apply for: ");
    
        BTOProperty selected = null;
        for (BTOProperty p : Data.btoList) {
            if (p.getProjectName().equalsIgnoreCase(projName)) {
                selected = p;
                break;
            }
        }
    
        if (selected == null) {
            System.out.println("Project not found.");
            return;
        }
    
        String flatType = InputUtil.getNonEmptyString(sc, "Enter flat type (2-Room / 3-Room): ");
    
        boolean success = ApplicationService.apply(applicant, selected, flatType);
        if (success) {
            System.out.println("Application submitted.");
        } else {
            System.out.println("Application failed.");
        }
    }
    
}
