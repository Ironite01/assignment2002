package assignment2002;
import java.util.Scanner;

import assignment2002.user.Manager;
import assignment2002.utils.Data;

public class ProjectFilterController {
    private Manager manager;
    private final Scanner sc;
    private String roomFilter = null;
    private String neighbourhoodFilter = null;
    private Boolean maritalStatusFilter = null;
    private Integer minAge = null;
    private Integer maxAge = null;

    public ProjectFilterController (Manager manager, Scanner sc){
        this.manager = manager;
        this.sc = sc;
    }

    public void viewProjsMenu(){
        boolean running = true;
        while (running) {
            System.out.println("\n=== View Projects: Filter Menu ===");
            System.out.println("1: View All Projects");
            System.out.println("2: Filter All Projects");
            System.out.println("3: View Personally Created Projects");
            System.out.println("4: Filter Personally Created Projects");
            System.out.println("5: Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1 -> manager.viewAllProjects(Data.btoList);
                case 2 -> filterConfigMenu(true);
                case 3 -> manager.viewMyProjs(Data.btoList);
                case 4 -> filterConfigMenu(false);
                case 5 -> running = false;
                default -> System.out.println("Invalid Input");
            }
        }
    }

    private void filterConfigMenu(boolean all){

        //Use boolean flag to determine if all or my

        boolean running = true;
        while (running) {
            System.out.println("\n=== FILTER CONFIGURATION ===");
            System.out.println("1. Set Room Type Filter      (Current: " + roomFilter + ")");
            System.out.println("2. Set Neighbourhood Filter  (Current: " + neighbourhoodFilter + ")");
            System.out.println("3. Set Marital Status Filter (Current: " + maritalStatusFilter + ")");
            System.out.println("4. Set Age Range Filter      (Current: " + minAge + " - " + maxAge + ")");
            System.out.println("5. Apply Filters");
            System.out.println("6. Clear All Filters");
            System.out.println("7. View");
            System.out.println("8. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1 -> setRoomTypeFilter();
                case 8 -> running = false;
                default -> System.out.println("Try Again");
            }
        }

    }
    private void setRoomTypeFilter(){
        while (true) {
            System.out.println("=== ROOM FILTER ===");
            System.out.println("1. 2-Room");
            System.out.println("2. 3-Room");
            System.out.println("3. Both");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1 -> {roomFilter = "2-Room"; return;}
                case 2 -> {roomFilter = "3-Room"; return;}
                case 3 -> {roomFilter = "Both"; return;}
                default -> System.out.println("Try Again");
            }
            
        }
            
    }
    

}
