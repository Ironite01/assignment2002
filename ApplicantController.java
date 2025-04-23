package assignment2002;

import assignment2002.application.ApplicationService;
import assignment2002.enquiry.Enquiry;
import assignment2002.enquiry.EnquiryService;
import assignment2002.user.Applicant;
import assignment2002.user.UserService;
import assignment2002.utils.Data;
import assignment2002.utils.InputUtil;
import assignment2002.utils.ProjectPrinter;
import java.util.Date;
import java.util.Map;
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
                case 5 -> submitEnquiry(sc);
                case 6 -> manageEnquiries(sc);
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
    

    private void submitEnquiry(Scanner sc) {
        String project = applicant.getAppliedProject();
        if (project == null) {
            System.out.println("You haven't applied for a project.");
            return;
        }

        Enquiry existing = EnquiryService.getEnquiry(applicant.getNRIC(), project);
        if (existing != null) {
            System.out.println("Enquiry already exists. Use option 6 to view/edit.");
            return;
        }

        String msg = InputUtil.getNonEmptyString(sc, "Enter your enquiry message: ");

        EnquiryService.addNewEnquiry(applicant.getNRIC(), project, msg);
        EnquiryService.saveEnquiriesToFile();
        System.out.println("Enquiry submitted.");
    }

    private void manageEnquiries(Scanner sc) {
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
            System.out.println("4. Edit Your Most Recent Message");
            System.out.println("5. Delete Enquiry");
            System.out.println("6. Back");
            int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 6);

            switch (choice) {
                case 1 -> {
                    if (enquiry.getAllMessages().isEmpty()) {
                        System.out.println("No messages.");
                    } else {
                        System.out.println("=== Enquiry Messages ===");
                        enquiry.getAllMessages().entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .forEach(entry -> {
                                String timestamp = entry.getKey().toString();
                                String sender = entry.getValue().getAuthor().getName();
                                String msg = entry.getValue().getMessage();
                                System.out.printf("[%s] %s: %s\n", timestamp, sender, msg);
                            });
                        System.out.println("------------------------");
                    }
                }
                

                case 2 -> {
                    if (enquiry.isResolved()) {
                        System.out.println("Enquiry is resolved. Cannot add messages.");
                        break;
                    }
                    String msg = InputUtil.getNonEmptyString(sc, "Enter your message: ");
                    enquiry.addMessage(applicant.getNRIC(), msg);
                    EnquiryService.saveEnquiriesToFile();
                    System.out.println("Message added.");
                }

                case 3 -> {
                    if (!enquiry.isResolved()) {
                        System.out.println("Enquiry is not resolved yet. You can only delete messages after it is resolved by the manager.");
                        break;
                    }
                
                    enquiry.getAllMessages().clear();
                    EnquiryService.saveEnquiriesToFile();
                    System.out.println("All messages deleted.");
                }
                

                case 4 -> {
                    if (enquiry.isResolved()) {
                        System.out.println("Cannot edit a resolved enquiry.");
                        break;
                    }
                
                    // Find most recent message by this applicant
                    Date latest = null;
                    for (var entry : enquiry.getAllMessages().entrySet()) {
                        String senderNric = entry.getValue().getAuthor().getNRIC();
                        if (senderNric.equalsIgnoreCase(applicant.getNRIC())) {
                            if (latest == null || entry.getKey().after(latest)) {
                                latest = entry.getKey();
                            }
                        }
                    }
                
                    if (latest == null) {
                        System.out.println("You have no messages to edit.");
                        break;
                    }
                
                    System.out.println("Your latest message:");
                    System.out.println("- " + enquiry.getAllMessages().get(latest).getMessage());
                
                    String newMsg = InputUtil.getNonEmptyString(sc, "Enter new content: ");
                    enquiry.getAllMessages().get(latest).setMessage(newMsg);
                
                    EnquiryService.saveEnquiriesToFile();
                    System.out.println("Message updated.");
                } 
                
                case 5 -> {
                    var userEnquiries = EnquiryService.viewAll().stream()
                        .filter(e -> e.getApplicantNric().equalsIgnoreCase(applicant.getNRIC()))
                        .toList();
                
                    if (userEnquiries.isEmpty()) {
                        System.out.println("You have no enquiries.");
                        break;
                    }
                
                    System.out.println("== Your Enquiries ==");
                    for (int i = 0; i < userEnquiries.size(); i++) {
                        Enquiry e = userEnquiries.get(i);
                        System.out.printf("%d. Project: %s\n", i + 1, e.getProjectName());
                    }
                
                    int sel = InputUtil.getValidatedIntRange(sc, "Select an enquiry (0 to cancel): ", 0, userEnquiries.size());
                    if (sel == 0) break;
                
                    Enquiry selected = userEnquiries.get(sel - 1);
                
                    // Show user's messages within that enquiry
                    var ownMessages = selected.getAllMessages().entrySet().stream()
                        .filter(entry -> entry.getValue().getAuthor().getNRIC().equalsIgnoreCase(applicant.getNRIC()))
                        .toList();
                
                    if (ownMessages.isEmpty()) {
                        System.out.println("You have no messages to delete in this enquiry.");
                        break;
                    }
                
                    System.out.println("== Your Messages in This Enquiry ==");
                    for (int i = 0; i < ownMessages.size(); i++) {
                        var entry = ownMessages.get(i);
                        System.out.printf("%d. [%s] %s\n", i + 1, entry.getKey().toString(), entry.getValue().getMessage());
                    }
                
                    int msgSel = InputUtil.getValidatedIntRange(sc, "Select message to delete (0 to cancel): ", 0, ownMessages.size());
                    if (msgSel == 0) break;
                
                    Date targetTimestamp = ownMessages.get(msgSel - 1).getKey();
                    selected.getAllMessages().remove(targetTimestamp);
                
                    EnquiryService.saveEnquiriesToFile();
                    System.out.println("Message deleted successfully.");
                }
                
            
                case 6 -> running = false;

                default -> System.out.println("Invalid option.");
            }
        }
    }
}
