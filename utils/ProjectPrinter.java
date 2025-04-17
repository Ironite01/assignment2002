package assignment2002.utils;

import assignment2002.ApplicantService;
import assignment2002.BTOProperty;
import assignment2002.user.Applicant;
import assignment2002.user.Manager;
import assignment2002.user.Officer;
import java.util.List;

public class ProjectPrinter {
    
    public static void viewProjects(List<BTOProperty> btoList){
        System.out.printf("| %-15s | %-20s | %-15s | %-8s | %-11s | %-12s | %-10s | %-14s | %-14s | %-12s | %-12s | %-14s | %-30s |\n", "MANAGER","PROJECT NAME", "NEIGHBOURHOOD", "2-ROOM", "2-ROOM AMOUNT", "2-ROOM PRICE", "3-ROOM", "3-ROOM AMOUNT", "3-ROOM PRICE", "OPEN DATE", "CLOSE DATE", "OFFICER SLOTS", "OFFICERS REGISTERED");
        for(BTOProperty project : btoList){
                String collatedOfficers = "";
                String managerIC = "";


                for(Officer offList:project.getOfficers()){
                    collatedOfficers += offList.getName() + ",";
                }

                if (!collatedOfficers.isEmpty()) {
                    collatedOfficers = collatedOfficers.substring(0, collatedOfficers.length() - 1);
                }

                for(Manager m: project.getManagerIC()){
                    managerIC = m.getName();
                }
                System.out.printf("| %-15s | %-20s | %-15s | %-8s | %-12d | %-13d | %-10s | %-14d | %-14d | %-12s | %-12s | %-14d | %-30s |\n", 
                managerIC, project.getProjectName(), project.getNeighbourhood(), project.getTwoRoom(), 
                project.getTwoRoomAmt(), project.getTwoRoomPrice(), project.getThreeRoom(), project.getThreeRoomAmt(),
                project.getThreeRoomPrice(), project.getOpenDate(), project.getCloseDate(), project.getOfficerSlot(), collatedOfficers);
                
            }
    }

    public static void viewProjectsForApplicant(Applicant applicant, List<BTOProperty> btoList) {
        System.out.println("Some available projects:");
        boolean found = false;

        for (BTOProperty project : btoList) {
            if (!project.isVisible()) continue;

            boolean eligibleFor2Rooms = ApplicantService.isEligible(applicant, project, "2-Room");
            boolean eligibleFor3Rooms = ApplicantService.isEligible(applicant, project, "3-Room");

            if (eligibleFor2Rooms || eligibleFor3Rooms) {
                found = true;
                System.out.printf("Project: %s\nNeighbourhood: %s\n", project.getProjectName(), project.getNeighbourhood());
                if (eligibleFor2Rooms) {
                    System.out.printf(" - 2-Room: %d units available, Price: $%d\n", project.getTwoRoomAmt(), project.getTwoRoomPrice());
                }
                if (eligibleFor3Rooms) {
                    System.out.printf(" - 3-Room: %d units available, Price: $%d\n", project.getThreeRoomAmt(), project.getThreeRoomPrice());
                }
                System.out.println("-------------------------");
            }
        }

        if (!found) {
            System.out.println("No available projects matching your eligibility.");
        }
    }


    public static void viewProjectsVisibility(List<BTOProperty> btoList){
        System.out.println("Project List: ");

        for(int i = 0; i < btoList.size(); i++){
            BTOProperty projects = btoList.get(i);
            System.out.printf("%d. %s (%s): %s\n", i + 1, projects.getProjectName(), projects.getNeighbourhood(), projects.isVisible() ? "VISIBLE" : "HIDDEN");
        }
    }
    
}
