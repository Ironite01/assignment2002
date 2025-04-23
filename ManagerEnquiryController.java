package assignment2002;

import assignment2002.enquiry.Enquiry;
import assignment2002.enquiry.EnquiryService;
import assignment2002.user.Manager;
import assignment2002.utils.Data;
import assignment2002.utils.InputUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ManagerEnquiryController {
    private Manager manager;
    private final Scanner sc;

    public ManagerEnquiryController(Manager manager, Scanner sc){
        this.manager = manager;
        this.sc = sc;
    }

    public void viewMenu(){
        boolean run = true;
        do {
            System.out.println("===== ENQUIRY MGMT ====");
            System.out.println("1: View All Enquiries");
            System.out.println("2: View & Reply to Your Project Enquiries");
            System.out.println("3: Exit");

            int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 3);

            switch(choice){
                case 1 -> viewAllEnquiries();
                case 2 -> viewAndReplyOwnEnquiries(Data.btoList);
                case 3 -> run = false;
                default -> System.out.println("Invalid Input");
            }
        } while(run);
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

    private void viewAndReplyOwnEnquiries(ArrayList<BTOProperty> btoList) {
        EnquiryService.loadEnquiriesFromFile();
        List<Enquiry> all = EnquiryService.viewAll();
    
        List<String> managedProjectNames = manager.getMyProjects(btoList).stream()
            .map(BTOProperty::getProjectName)
            .toList();
    
        List<Enquiry> relatedEnquiries = all.stream()
            .filter(e -> managedProjectNames.contains(e.getProjectName()))
            .toList();
    
        if (relatedEnquiries.isEmpty()) {
            System.out.println("No enquiries found for your managed projects.");
            return;
        }
    
        System.out.println("== Enquiries for Your Projects ==");
        for (int i = 0; i < relatedEnquiries.size(); i++) {
            Enquiry e = relatedEnquiries.get(i);
            System.out.printf("%d. Project: %s | From: %s | Resolved: %s\n",
                i + 1, e.getProjectName(), e.getApplicantNric(), e.isResolved());
        }
    
        int choice = InputUtil.getValidatedIntRange(sc, "Select an enquiry to view (0 to cancel): ", 0, relatedEnquiries.size());
        if (choice == 0) return;
    
        Enquiry selected = relatedEnquiries.get(choice - 1);
    
        System.out.println("\n=== Enquiry Details ===");
        System.out.println("Project: " + selected.getProjectName());
        System.out.println("From: " + selected.getApplicantNric());
        System.out.println("Resolved: " + selected.isResolved());
    
        selected.getAllMessages().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                String timestamp = entry.getKey().toString();
                String author = entry.getValue().getAuthor().getName();
                String message = entry.getValue().getMessage();
                System.out.printf("[%s] %s: %s\n", timestamp, author, message);
            });
    
        if (!selected.isResolved()) {
            if (InputUtil.getConfirmationBool(sc, "Reply to this enquiry?")) {
                String reply = InputUtil.getNonEmptyString(sc, "Enter your reply: ");
                selected.addMessage(manager.getNRIC(), reply);
                EnquiryService.saveEnquiriesToFile();
                System.out.println("Reply sent.");
            }
    
            if (InputUtil.getConfirmationBool(sc, "Do you want to mark this enquiry as resolved?")) {
                selected.setResolved();
                EnquiryService.saveEnquiriesToFile();
                System.out.println("Marked as resolved.");
            }            
        } else {
            System.out.println("This enquiry is already resolved.");
        }
    }
    
}
