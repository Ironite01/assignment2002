package assignment2002;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import assignment2002.user.Manager;
import assignment2002.user.Officer;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.Data;
import assignment2002.utils.FileManifest;
import assignment2002.utils.InputUtil;

public class OfficerRegisController {

    private Manager manager;
    private final Scanner sc;

    public OfficerRegisController(Manager manager, Scanner sc){
        this.manager = manager;
        this.sc = sc;
    }

    public void viewOfficerRegisMenu(){
        boolean running = true;
        while (running) {
            System.out.println("\n=== Officer Registration Menu ===");
            System.out.println("1: View All Registrations");
            System.out.println("2: Approve Registrations");
            System.out.println("3: Reject Registrations");
            System.out.println("4: Exit");
            int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 4);

            switch(choice){
                case 1 -> viewAllRegistrations();
                case 2 -> approveOfficerRegisMenu();
                case 3 -> rejectOfficerRegisMenu();
                case 4 -> running = false;
                default -> System.out.println("Invalid Input");
            }

            
        }
    }
    
    private boolean viewAllRegistrations(){
        List<BTOProperty> myBTO = manager.getMyProjects(Data.btoList);

        if(myBTO.isEmpty()){
            System.out.println("You are not managing any BTOs");
            return false;
        }

        boolean pendingOfficer = false;

        for (BTOProperty p: myBTO) {
            List<Officer> pendingOfficers = p.getAppliedOfficers();

            if(pendingOfficers.isEmpty()){
                System.out.printf("Project: %s Has No Pending Registrations\n", p.getProjectName());
                continue;
            }

            pendingOfficer = true;

            System.out.printf("%s PENDING REGISTRATIONS\n", p.getProjectName());

            for (Officer o : pendingOfficers) {
                System.out.printf("| Name: %-15s | NRIC: %-10s |\n", o.getName(), o.getNRIC());
            }
        }
        return pendingOfficer;

    }

    private void approveOfficerRegisMenu() {
        if(!viewAllRegistrations()){
            System.out.println("No Pending Registrations");
            return;
        }

        String projName = InputUtil.getNonEmptyString(sc,"Enter Project Name: " );

        // Find matching project
        BTOProperty targetProject = manager.getMyProjects(Data.btoList).stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(projName))
            .findFirst()
            .orElse(null);

        if (targetProject == null) {
            System.out.println("Project not found.");
            return;
        }

        String nric = InputUtil.getNonEmptyString(sc,"Enter NRIC of Officer to Approve: " );

        
        Officer officer = targetProject.getAppliedOfficers().stream()
            .filter(o -> o.getNRIC().equalsIgnoreCase(nric))
            .findFirst()
            .orElse(null);

        if (officer == null) {
            System.out.println("Officer not found in pending registrations.");
            return;
        }

        targetProject.getAppliedOfficers().remove(officer);
        targetProject.getOfficers().add(officer);
        BTOFileService.editBTOByColumn(targetProject.getProjectName(), FileManifest.PROPERTY_COLUMNS.PENDING_OFFICERS,
            targetProject.getAppliedOfficers().stream().map(Officer::getName).collect(Collectors.joining(",")),false);
        BTOFileService.editBTOByColumn(targetProject.getProjectName(), FileManifest.PROPERTY_COLUMNS.APPROVED_OFFICERS,
            targetProject.getOfficers().stream().map(Officer::getName).collect(Collectors.joining(",")),false);

        System.out.printf("Approved officer %s for project '%s'.\n", officer.getName(), projName);
    }


    private void rejectOfficerRegisMenu() {
        if(!viewAllRegistrations()){
            System.out.println("No Pending Registrations");
            return;
        }
    
        String projName = InputUtil.getNonEmptyString(sc,"Enter Project Name: " );
    
        BTOProperty targetProject = manager.getMyProjects(Data.btoList).stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(projName))
            .findFirst()
            .orElse(null);
    
        if (targetProject == null) {
            System.out.println("Project not found.");
            return;

        }
        String nric = InputUtil.getNonEmptyString(sc,"Enter NRIC of Officer to Reject: " );
    
        Officer officer = targetProject.getAppliedOfficers().stream()
            .filter(o -> o.getNRIC().equalsIgnoreCase(nric))
            .findFirst()
            .orElse(null);
    
        if (officer == null) {
            System.out.println("Officer not found in pending registrations.");
            return;
        }
    
        // Reject: remove from pending, add to rejected
        targetProject.getAppliedOfficers().remove(officer);
        targetProject.getRejectedOfficers().add(officer);
        BTOFileService.editBTOByColumn(targetProject.getProjectName(), FileManifest.PROPERTY_COLUMNS.PENDING_OFFICERS,
            targetProject.getAppliedOfficers().stream().map(Officer::getName).collect(Collectors.joining(",")),false);
        BTOFileService.editBTOByColumn(targetProject.getProjectName(), FileManifest.PROPERTY_COLUMNS.REJECTED_OFFICERS,
            targetProject.getRejectedOfficers().stream().map(Officer::getName).collect(Collectors.joining(",")),false);
    
        System.out.printf("Rejected officer %s for project '%s'.\n", officer.getName(), projName);
    }
    

}
