package assignment2002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import assignment2002.enquiry.Enquiry;
import assignment2002.enquiry.EnquiryService;
import assignment2002.user.Manager;
import assignment2002.utils.Data;
import assignment2002.utils.InputUtil;

public class ManagerEnquiryController {
    private Manager manager;
    private final Scanner sc;

    public ManagerEnquiryController(Manager manager, Scanner sc){
        this.manager = manager;
        this.sc = sc;
    }

    public void viewMenu(){
        boolean run = true;
        while(run){
            System.out.println("===== ENQUIRY MGMT ====");
            System.out.println("1: View All Enquiries");
            System.out.println("2: View & Reply to Your Project Enquiries");
            System.out.println("3: Exit");
        }

        int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 3);

        switch(choice){
            case 1 -> viewAllEnquiries();
            case 2 -> viewAndReplyOwnEnquiries(Data.btoList);
            case 3 -> run = false;
            default -> System.out.println("Invalid Input");
        }
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
                    if (InputUtil.getConfirmationBool(sc, "Reply to this enquiry?")) {
                        String reply = InputUtil.getNonEmptyString(sc, "Enter your reply: ");
                        enquiry.addMessage(manager.getNRIC(), reply);
                        EnquiryService.saveEnquiriesToFile();
                        System.out.println("Reply sent.");
                    }

                    if (InputUtil.getConfirmationBool(sc, "Do you want to resolve this enquiry?")) {
                        EnquiryService.markEnquiryAsResolved(enquiry.getApplicantNric(), enquiry.getProjectName());
                        System.out.println("Marked as resolved.");
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
