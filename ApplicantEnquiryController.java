package assignment2002;

import assignment2002.enquiry.Enquiry;
import assignment2002.enquiry.EnquiryService;
import assignment2002.enquiry.Message;
import assignment2002.user.Applicant;
import assignment2002.utils.Data;
import assignment2002.utils.InputUtil;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

public class ApplicantEnquiryController {
    private Applicant applicant;

    public ApplicantEnquiryController(Applicant applicant) {
        this.applicant = applicant;
    }

    public void submitEnquiry(Scanner sc) {
        String rawProject = applicant.getAppliedProject();
        if (rawProject == null || rawProject.trim().isEmpty()) {
            System.out.println("You haven't applied for any project. Please apply before submitting an enquiry.");
            return;
        }
        String project = rawProject.trim();

        boolean projectExists = Data.btoList.stream()
            .anyMatch(p -> p.getProjectName().equalsIgnoreCase(project));

        if (!projectExists) {
            System.out.println("The project you applied to no longer exists.");
            return;
        }

        String msg = InputUtil.getNonEmptyString(sc, "Enter your enquiry message: ");
        EnquiryService.addNewEnquiry(applicant.getNRIC(), project, msg);
        EnquiryService.saveEnquiriesToFile();

        System.out.println("Enquiry submitted successfully.");
    }

    public void manageEnquiries(Scanner sc) {
        String project = applicant.getAppliedProject();
        if (project == null) {
            System.out.println("No project applied.");
            return;
        }

        Enquiry enquiry = EnquiryService.getEnquiry(applicant.getNRIC(), project);
        if (enquiry == null) {
            System.out.println("No enquiry found.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println("== ENQUIRY MENU ==");
            System.out.println("1. View Messages");
            System.out.println("2. Add Message");
            System.out.println("3. Close the Enquiries");
            System.out.println("4. Edit Your Message");
            System.out.println("5. Delete Message from Enquiry");
            System.out.println("6. Back");

            int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 6);

            switch (choice) {
                case 1 -> viewMessages(sc);
                case 2 -> addMessage(sc);
                case 3 -> closeEnquiry(sc);
                case 4 -> editMessage(sc);
                case 5 -> deleteMessage(sc);
                case 6 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void viewMessages(Scanner sc) {
        var userEnquiries = EnquiryService.viewAll().stream()
            .filter(e -> e.getApplicantNric().equalsIgnoreCase(applicant.getNRIC()))
            .toList();
    
        if (userEnquiries.isEmpty()) {
            System.out.println("You have no enquiries.");
            return;
        }
    
        System.out.println("== Your Enquiries ==");
        for (int i = 0; i < userEnquiries.size(); i++) {
            Enquiry e = userEnquiries.get(i);
            System.out.printf("%d. Project: %s\n", i + 1, e.getProjectName());
        }
    
        int sel = InputUtil.getValidatedIntRange(sc, "Select an enquiry (0 to cancel): ", 0, userEnquiries.size());
        if (sel == 0) return;
    
        Enquiry selected = userEnquiries.get(sel - 1);
    
        if (selected.getAllMessages().isEmpty()) {
            System.out.println("No messages in this enquiry.");
            return;
        }
    
        System.out.println("=== Enquiry Messages ===");
        selected.getAllMessages().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                String timestamp = entry.getKey().toString();
                String sender = entry.getValue().getAuthor().getName();
                String msg = entry.getValue().getMessage();
                System.out.printf("[%s] %s: %s\n", timestamp, sender, msg);
            });
    }
    

    private void addMessage(Scanner sc) {
        var userEnquiries = EnquiryService.viewAll().stream()
            .filter(e -> e.getApplicantNric().equalsIgnoreCase(applicant.getNRIC()))
            .toList();
    
        if (userEnquiries.isEmpty()) {
            System.out.println("You have no enquiries to add a message to.");
            return;
        }
    
        System.out.println("== Your Enquiries ==");
        for (int i = 0; i < userEnquiries.size(); i++) {
            Enquiry e = userEnquiries.get(i);
            System.out.printf("%d. Project: %s\n", i + 1, e.getProjectName());
        }
    
        int sel = InputUtil.getValidatedIntRange(sc, "Select an enquiry (0 to cancel): ", 0, userEnquiries.size());
        if (sel == 0) return;
    
        Enquiry selected = userEnquiries.get(sel - 1);
    
        if (selected.isResolved()) {
            System.out.println("This enquiry is marked as resolved. You cannot add more messages.");
            return;
        }
    
        String msg = InputUtil.getNonEmptyString(sc, "Enter your message: ");
        selected.addMessage(applicant.getNRIC(), msg);
        EnquiryService.saveEnquiriesToFile();
    
        System.out.println("Message added successfully.");
    }
    

    private void closeEnquiry(Scanner sc) {
        var userEnquiries = EnquiryService.viewAll().stream()
            .filter(e -> e.getApplicantNric().equalsIgnoreCase(applicant.getNRIC()) && e.isResolved())
            .toList();
    
        if (userEnquiries.isEmpty()) {
            System.out.println("No resolved enquiries to close.");
            return;
        }
    
        System.out.println("== Resolved Enquiries ==");
        for (int i = 0; i < userEnquiries.size(); i++) {
            Enquiry e = userEnquiries.get(i);
            System.out.printf("%d. Project: %s\n", i + 1, e.getProjectName());
        }
    
        System.out.print("Enter enquiry numbers to close (e.g., 1,3,4) or 0 to cancel: ");
        String input = sc.nextLine().trim();
        if (input.equals("0")) return;
    
        String[] tokens = input.split(",");
        int closedCount = 0;
    
        for (String token : tokens) {
            try {
                int index = Integer.parseInt(token.trim());
                if (index >= 1 && index <= userEnquiries.size()) {
                    Enquiry toClose = userEnquiries.get(index - 1);
                    toClose.getAllMessages().clear();
                    closedCount++;
                }
            } catch (NumberFormatException ignored) {

            }
        }
    
        if (closedCount > 0) {
            EnquiryService.saveEnquiriesToFile();
            System.out.println(closedCount + " enquiry(s) closed successfully.");
        } else {
            System.out.println("No valid selections made.");
        }
    }
    

    private void editMessage(Scanner sc) {
        var userEnquiries = EnquiryService.viewAll().stream()
            .filter(e -> e.getApplicantNric().equalsIgnoreCase(applicant.getNRIC()))
            .toList();

        if (userEnquiries.isEmpty()) {
            System.out.println("You have no enquiries.");
            return;
        }

        System.out.println("== Your Enquiries ==");
        for (int i = 0; i < userEnquiries.size(); i++) {
            Enquiry e = userEnquiries.get(i);
            System.out.printf("%d. Project: %s\n", i + 1, e.getProjectName());
        }

        int sel = InputUtil.getValidatedIntRange(sc, "Select an enquiry (0 to cancel): ", 0, userEnquiries.size());
        if (sel == 0) return;

        Enquiry selected = userEnquiries.get(sel - 1);

        if (selected.isResolved()) {
            System.out.println("This enquiry is resolved. Editing is not allowed.");
            return;
        }

        var ownMessages = selected.getAllMessages().entrySet().stream()
            .filter(entry -> entry.getValue().getAuthor().getNRIC().equalsIgnoreCase(applicant.getNRIC()))
            .toList();

        if (ownMessages.isEmpty()) {
            System.out.println("You have no messages to edit in this enquiry.");
            return;
        }

        System.out.println("== Your Messages in This Enquiry ==");
        for (int i = 0; i < ownMessages.size(); i++) {
            var entry = ownMessages.get(i);
            System.out.printf("%d. [%s] %s\n", i + 1, entry.getKey().toString(), entry.getValue().getMessage());
        }

        int msgSel = InputUtil.getValidatedIntRange(sc, "Select message to edit (0 to cancel): ", 0, ownMessages.size());
        if (msgSel == 0) return;

        Date timestamp = ownMessages.get(msgSel - 1).getKey();
        Message msg = selected.getAllMessages().get(timestamp);

        System.out.println("Old message: " + msg.getMessage());
        String newContent = InputUtil.getNonEmptyString(sc, "Enter new content: ");
        msg.setMessage(newContent);

        EnquiryService.saveEnquiriesToFile();
        System.out.println("Message updated.");
    }


    private void deleteMessage(Scanner sc) {
        var userEnquiries = EnquiryService.viewAll().stream()
            .filter(e -> e.getApplicantNric().equalsIgnoreCase(applicant.getNRIC()))
            .toList();

        if (userEnquiries.isEmpty()) {
            System.out.println("You have no enquiries.");
            return;
        }

        System.out.println("== Your Enquiries ==");
        for (int i = 0; i < userEnquiries.size(); i++) {
            Enquiry e = userEnquiries.get(i);
            System.out.printf("%d. Project: %s\n", i + 1, e.getProjectName());
        }

        int sel = InputUtil.getValidatedIntRange(sc, "Select an enquiry (0 to cancel): ", 0, userEnquiries.size());
        if (sel == 0) return;

        Enquiry selected = userEnquiries.get(sel - 1);

        var ownMessages = selected.getAllMessages().entrySet().stream()
            .filter(entry -> entry.getValue().getAuthor().getNRIC().equalsIgnoreCase(applicant.getNRIC()))
            .toList();

        if (ownMessages.isEmpty()) {
            System.out.println("You have no messages to delete in this enquiry.");
            return;
        }

        System.out.println("== Your Messages in This Enquiry ==");
        for (int i = 0; i < ownMessages.size(); i++) {
            var entry = ownMessages.get(i);
            System.out.printf("%d. [%s] %s\n", i + 1, entry.getKey().toString(), entry.getValue().getMessage());
        }

        int msgSel = InputUtil.getValidatedIntRange(sc, "Select message to delete (0 to cancel): ", 0, ownMessages.size());
        if (msgSel == 0) return;

        Date targetTimestamp = ownMessages.get(msgSel - 1).getKey();
        selected.getAllMessages().remove(targetTimestamp);

        EnquiryService.saveEnquiriesToFile();
        System.out.println("Message deleted successfully.");
    }
}
