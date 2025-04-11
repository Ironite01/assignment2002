package assignment2002.utils;

import java.util.List;

import assignment2002.BTOProperty;
import assignment2002.Manager;
import assignment2002.Officer;

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

    public static void viewProjectsVisibility(List<BTOProperty> btoList){
        System.out.println("Project List: ");

        for(int i = 0; i < btoList.size(); i++){
            BTOProperty projects = btoList.get(i);
            System.out.printf("%d. %s (%s): %s\n", i + 1, projects.getProjectName(), projects.getNeighbourhood(), projects.isVisible() ? "VISIBLE" : "HIDDEN");
        }
    }
    
}
