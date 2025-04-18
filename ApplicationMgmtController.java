package assignment2002;
import java.util.List;
import java.util.Scanner;

import assignment2002.application.Application;
import assignment2002.user.Manager;
import assignment2002.application.Application.ApplicationStatus;
import assignment2002.application.ApplicationService;

public class ApplicationMgmtController {
    private Manager manager;
    private final Scanner sc;

    public ApplicationMgmtController(Manager manager, Scanner sc){
        this.manager = manager;
        this.sc = sc;
    }


    public void viewApplicationsMenu(){
            boolean running = true;
            int choice;
            while (running) {
                System.out.println("==== APPLICATION MGMT MENU ====");
                System.out.println("1. View All Applications");
                System.out.println("2. View Pending Applications");
                System.out.println("3. View Successful Applications");
                System.out.println("4. View Unsuccessful Applications");
                System.out.println("5. Approve Applications");
                System.out.println("6. Reject Applications");
                System.out.println("0. Back");    
                choice = sc.nextInt();
                sc.nextLine();

                switch(choice){
                    case 1 -> viewApplicationsByStatus(null, "All");
                    case 2 -> viewApplicationsByStatus(ApplicationStatus.PENDING, "Pending");
                    case 3 -> viewApplicationsByStatus(ApplicationStatus.SUCCESSFUL, "Successful");
                    case 4 -> viewApplicationsByStatus(ApplicationStatus.UNSUCCESSFUL, "Unsuccessful");
                    case 5 -> approveApplication(sc);
                    case 6 -> rejectApplication(sc);
                    case 0 -> running = false; 
                }
            }
            System.out.println("Exiting\n");
            
        }
    

    private void viewApplicationsByStatus(Application.ApplicationStatus status, String label) {
        List<Application> apps = null;

        if(status == null){
            apps = ApplicationService.getMyManagedApplications(manager);
        } else{
            apps = ApplicationService.getMyManagedApplicationsByStatus(manager, status);
        }
    
        if (apps.isEmpty()) {
            System.out.printf("No %s Applications Found.%n", label);
            return;
        }
    
        System.out.printf("==== %s APPLICATIONS ====%n", label.toUpperCase());
        apps.forEach(ApplicationService::printApplication);
    }
    
    private void approveApplication(Scanner sc) {
        List<Application> pendingApps = ApplicationService.getMyManagedApplicationsByStatus(manager, ApplicationStatus.PENDING);
    
        if (pendingApps.isEmpty()) {
            System.out.println("No Pending Applications to Approve.");
            return;
        }
    
        viewApplicationsByStatus(ApplicationStatus.PENDING, "Pending");
    
        System.out.print("Enter NRIC of Applicant to Approve: ");
        String nric = sc.nextLine().trim();
    
        Application toApprove = pendingApps.stream()
            .filter(app -> app.getApplicant().getNRIC().equalsIgnoreCase(nric))
            .findFirst()
            .orElse(null);
    
        if (toApprove == null) {
            System.out.println("No pending application found for NRIC: " + nric);
            return;
        }
    
        boolean success = ApplicationService.approveApplication(toApprove);
    
        if (success) {
            ApplicationService.saveToFile(toApprove);
            System.out.println("Application approved successfully.");
        } else {
            System.out.println("Could not approve application (possibly no units left).");
        }
    }

    private void rejectApplication(Scanner sc) {
        List<Application> pendingApps = ApplicationService.getMyManagedApplicationsByStatus(manager, ApplicationStatus.PENDING);
    
        if (pendingApps.isEmpty()) {
            System.out.println("No Pending Applications to Reject.");
            return;
        }
    
        viewApplicationsByStatus(ApplicationStatus.PENDING, "Pending");
    
        System.out.print("Enter NRIC of Applicant to Reject: ");
        String nric = sc.nextLine().trim();
    
        Application toReject = pendingApps.stream()
            .filter(app -> app.getApplicant().getNRIC().equalsIgnoreCase(nric))
            .findFirst()
            .orElse(null);
    
        if (toReject == null) {
            System.out.println("No pending application found for NRIC: " + nric);
            return;
        }
    
        ApplicationService.rejectApplication(toReject);
        ApplicationService.saveToFile(toReject);
        System.out.println("Application has been rejected.");
    }
    
    
}
