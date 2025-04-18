package assignment2002;

import assignment2002.user.Applicant;
import assignment2002.user.User;
import assignment2002.utils.Data;
import assignment2002.utils.ProjectPrinter;
import java.util.ArrayList;
import java.util.Scanner;
public class ApplicantController {
    private Applicant applicant;

    public ApplicantController(Applicant applicant) {
        this.applicant = applicant;
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
            System.out.println("5: Submit Enquiry"); // For future
            System.out.println("6: View/Edit/Delete Enquiries"); // For future
            System.out.println("7: Logout");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> viewProjects();
                case 2 -> applyToProject(sc);
                case 3 -> viewStatus();
                case 4 -> ApplicantService.withdraw(applicant);
                case 5 -> System.out.println("Enquiry submission coming soon...");
                case 6 -> System.out.println("Enquiry viewing/editing coming soon...");
                case 7 -> run = false;
                default -> System.out.println("Invalid input. Try again.");
            }

        } while (run);

        sc.close();
        System.out.println("You have logged out.");
    }

    private void viewProjects() {
        ProjectPrinter.viewProjectsForApplicant(applicant, Data.btoList);
    }

    private void viewStatus() {
        String status = ApplicantService.getApplicationStatus(applicant);
        System.out.println("Your current application status: " + status);
    }

    private void applyToProject(Scanner sc) {
        viewProjects();

        System.out.print("Enter Project Name to apply for: ");
        String projName = sc.nextLine();

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

        System.out.print("Enter flat type (2-Room / 3-Room): ");
        String flatType = sc.nextLine();

        ApplicantService.apply(applicant, selected, flatType);
    }
}
