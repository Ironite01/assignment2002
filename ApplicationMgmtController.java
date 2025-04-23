package assignment2002;
import assignment2002.application.Application;
import assignment2002.utils.InputUtil;
import assignment2002.utils.Status.APPLICATION_STATUS;
import assignment2002.application.ApplicationService;
import assignment2002.user.Manager;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

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
                System.out.println("\n==== APPLICATION MGMT MENU ====");
                System.out.println("1. View All Applications");
                System.out.println("2. View Pending Applications");
                System.out.println("3. View Successful Applications");
                System.out.println("4. View Unsuccessful Applications");
                System.out.println("5. Approve Applications");
                System.out.println("6. Reject Applications");
                System.out.println("7. View Pending Withdrawals");
                System.out.println("8. Finalize Withdrawals");
                System.out.println("9. Back");    
                choice = InputUtil.getValidatedIntRange(sc, "Choice: ",1 , 9);

                switch(choice){
                    case 1 -> viewApplicationsByStatus(null, "All");
                    case 2 -> viewApplicationsByStatus(APPLICATION_STATUS.PENDING, "Pending");
                    case 3 -> viewApplicationsByStatus(APPLICATION_STATUS.SUCCESSFUL, "Successful");
                    case 4 -> viewApplicationsByStatus(APPLICATION_STATUS.UNSUCCESSFUL, "Unsuccessful");
                    case 5 -> approveApplication();
                    case 6 -> rejectApplication();
                    case 7 -> viewPendingWithdrawals();
                    case 8 -> finalizeWithdrawals();
                    case 9 -> running = false; 
                }
            }
            System.out.println("Exiting\n");
            
        }
    

    private void viewApplicationsByStatus(APPLICATION_STATUS status, String label) {
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
    
    private void approveApplication() {
        List<Application> pendingApps = ApplicationService.getMyManagedApplicationsByStatus(manager, APPLICATION_STATUS.PENDING);
    
        if (pendingApps.isEmpty()) {
            System.out.println("No Pending Applications to Approve.");
            return;
        }
    
        viewApplicationsByStatus(APPLICATION_STATUS.PENDING, "Pending");
    
        String nric = InputUtil.getNonEmptyString(sc, "Enter NRIC of Applicant to Approve: ");
    
        Application toApprove = pendingApps.stream()
            .filter(app -> app.getApplicant().getNRIC().equalsIgnoreCase(nric))
            .findFirst()
            .orElse(null);
    
        if (toApprove == null) {
            System.out.println("No pending application found for NRIC: " + nric);
            return;
        }

        String projectName = toApprove.getProperty().getProjectName();
        String flatType = toApprove.getFlatType();
    
        long approvedCount = ApplicationService.getApplications().stream()
        .filter(app ->
            app.getProperty().getProjectName().equalsIgnoreCase(projectName) &&
            app.getFlatType().equalsIgnoreCase(flatType) &&
            (app.getStatus() == APPLICATION_STATUS.SUCCESSFUL ||
             app.getStatus() == APPLICATION_STATUS.BOOKED)
        )
        .count();

        int maxUnits = flatType.equalsIgnoreCase("2-Room")
        ? toApprove.getProperty().getTwoRoomAmt()
        : toApprove.getProperty().getThreeRoomAmt();

    
        if (approvedCount < maxUnits) {
            ApplicationService.approveApplication(toApprove);
            ApplicationService.saveToFile(toApprove);
            System.out.println("Application approved successfully.");
        } else {
            ApplicationService.rejectApplication(toApprove);
            ApplicationService.saveToFile(toApprove);
            System.out.println("No units left. Application has been rejected.");

            boolean confirm = InputUtil.getConfirmationBool(sc,"\n!! This flat type is now full. Would you like to reject remaining applicants? !!");

            if (confirm) {
                List<Application> toReject = ApplicationService.getMyManagedApplicationsByStatus(manager, APPLICATION_STATUS.PENDING).stream()
                    .filter(app ->
                        app.getProperty().getProjectName().equalsIgnoreCase(projectName) &&
                        app.getFlatType().equalsIgnoreCase(flatType))
                    .collect(Collectors.toList());

                for (Application app : toReject) {
                    ApplicationService.rejectApplication(app);
                    ApplicationService.saveToFile(app);
                }

                int rejSize;
                if(toReject.size() == 0){
                    rejSize = 1;
                } else{
                    rejSize = toReject.size();
                }

                System.out.printf("Rejected %d remaining pending applications for %s [%s].\n",rejSize, projectName, flatType);
            } else {
                System.out.println("Pending applications were not modified.");
            }

        }
    }

    private void rejectApplication() {
        List<Application> pendingApps = ApplicationService.getMyManagedApplicationsByStatus(manager, APPLICATION_STATUS.PENDING);
    
        if (pendingApps.isEmpty()) {
            System.out.println("No Pending Applications to Reject.");
            return;
        }
    
        viewApplicationsByStatus(APPLICATION_STATUS.PENDING, "Pending");
    
        String nric = InputUtil.getNonEmptyString(sc, "Enter NRIC of Applicant to Reject: ");
    
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
    
    private void viewPendingWithdrawals() {
        List<Application> withdrawals = ApplicationService.getPendingWithdrawalApplications();
    
        if (withdrawals.isEmpty()) {
            System.out.println("No Pending Withdrawals.");
            return;
        }
    
        System.out.println("=== PENDING WITHDRAWALS ===");
        for (Application app : withdrawals) {
            System.out.printf("NRIC: %s | Name: %s | Status: %s\n",
                    app.getApplicant().getNRIC(),
                    app.getApplicant().getName(),
                    app.getStatus());
        }
    }

    private void finalizeWithdrawals() {
        List<Application> withdrawals = ApplicationService.getPendingWithdrawalApplications();

        if (withdrawals.isEmpty()) {
            System.out.println("No Pending Withdrawals to Finalize.");
            return;
        }

        viewPendingWithdrawals(); // Reuse display

        String input = InputUtil.getNonEmptyString(sc, "Enter NRICs to finalize (comma separated): ");
        Set<String> selectedNrics = Arrays.stream(input.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        ApplicationService.finalizeWithdrawals(selectedNrics);
        System.out.println("Selected withdrawal(s) have been finalized.");
    }
    
}
