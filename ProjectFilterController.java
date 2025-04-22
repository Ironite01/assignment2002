package assignment2002;
import java.util.Scanner;
import java.util.List;

import assignment2002.user.Manager;
import assignment2002.utils.Data;
import assignment2002.utils.InputUtil;
import assignment2002.utils.ProjectPrinter;

public class ProjectFilterController {
    private Manager manager;
    private final Scanner sc;
    private String roomFilter = null;
    private String neighbourhoodFilter = null;
    private String maritalStatusFilter = null;
    // private Integer minAge = null;
    // private Integer maxAge = null;
    private String visibleFilter = null;

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
        List<BTOProperty> filteredBTOs =  all ? Data.btoList: manager.getMyProjects(Data.btoList);
        String type = all ? "ALL" : "PERSONAL";
        boolean running = true;
        while (running) {
            System.out.printf("\n=== FILTER CONFIGURATION [%s] ===\n", type);
            System.out.printf("1. Set Room Type Filter      (Current: %s)\n", roomFilter);
            System.out.printf("2. Set Neighbourhood Filter  (Current: %s)\n", neighbourhoodFilter);
            System.out.printf("3. Set Marital Status Filter (Current: %s)\n", maritalStatusFilter);
            // System.out.printf("4. Set Age Range Filter      (Current: %d ~ %d  )\n", minAge, maxAge); //This one kinda questionable
            System.out.printf("4. Set Visibility Filter (Current: %s)\n", visibleFilter);
            System.out.println("5. Clear All Filters");
            System.out.println("6. View with Applied Filters");
            System.out.println("7. Exit");

            int choice = InputUtil.getValidatedIntRange(sc,"Choice: ", 1, 7);

            switch(choice){
                case 1 -> setRoomTypeFilter();
                case 2 -> setNeighbourhoodFilter(filteredBTOs);
                case 3 -> setMaritalStatusFilter();
                case 4 -> setVisibilityFilter();
                case 5 -> resetFilters();
                case 6 -> viewChosenProperties(filteredBTOs);
                case 7 -> {running = false; resetFilters();}
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
            int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 3);

            switch(choice){
                case 1 -> {roomFilter = "2-Room"; return;}
                case 2 -> {roomFilter = "3-Room"; return;}
                case 3 -> {roomFilter = "Both"; return;}
                default -> System.out.println("Invalid Option, Try Again!");
            }
        }
            
    }

    private void setNeighbourhoodFilter(List<BTOProperty> btoList){
        if(btoList.isEmpty()){
            System.out.println("No Neighbourhoods Available!");
            return;
        }

        List<String> neighbourhoodList = btoList.stream().map(BTOProperty::getNeighbourhood)
                                                .distinct().sorted().toList();

        while (true) {
            System.out.println("=== Available Neighbourhoods ===");
            
            int count = 1;
            for(String s: neighbourhoodList){
                System.out.printf("%d. %s\n", count++, s);
            }
            
            System.out.printf("%d. Clear Filter\n", count);

            int choice = InputUtil.getValidatedIntRange(sc, "Neighbourhood: ", 1, count);

            if(choice == count){
                neighbourhoodFilter = null;
                System.out.println("Filter Cleared");
                return;
            }

            neighbourhoodFilter = neighbourhoodList.get(choice - 1);
            System.out.printf("Chosen Neighbourhood: %s\n", neighbourhoodFilter);
            return;
        }
    }

    private void setMaritalStatusFilter(){
        System.out.println("=== MARITAL STATUS ===");
        System.out.println("1. Single");
        System.out.println("2. Married");
        System.out.println("3. Clear Filter");

        int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 3);

        switch(choice){
            case 1 -> maritalStatusFilter = "Single";
            case 2 -> maritalStatusFilter = "Married";
            case 3 -> maritalStatusFilter = null;
            default -> System.out.println("Try Again!");
        }
        return;
    }
    

    private void setVisibilityFilter(){
        System.out.println("=== VISIBILITY ===");
        System.out.println("1. Visible");
        System.out.println("2. Not Visible");
        System.out.println("3. Clear Filter");

        int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 3);

        switch(choice){
            case 1 -> visibleFilter = "TRUE";
            case 2 -> visibleFilter = "FALSE";
            case 3 -> visibleFilter = null;
            default -> System.out.println("Try Again!");
        }
        return;

    }

    private void resetFilters(){
        roomFilter = null;
        neighbourhoodFilter = null;
        maritalStatusFilter = null;
        visibleFilter = null;
    }
    
    private void viewChosenProperties(List<BTOProperty> btoList){
        List<BTOProperty> filtered = btoList.stream()
        .filter(p -> {
            if (visibleFilter != null) {
                boolean shouldBeVisible = visibleFilter.equalsIgnoreCase("TRUE");
                if (p.isVisible() != shouldBeVisible) return false;
            }

            if (neighbourhoodFilter != null) {
                if (!p.getNeighbourhood().equalsIgnoreCase(neighbourhoodFilter)) {
                    return false;
                }
            }

            if (roomFilter != null && !roomFilter.equalsIgnoreCase("Both")) {
                if (roomFilter.equalsIgnoreCase("2-Room")) {
                    if (p.getTwoRoom().equalsIgnoreCase("NA") || p.getTwoRoomAmt() <= 0) {
                        return false;
                    }
                } else if (roomFilter.equalsIgnoreCase("3-Room")) {
                    if (p.getThreeRoom().equalsIgnoreCase("NA") || p.getThreeRoomAmt() <= 0) {
                        return false;
                    }
                }
            }

            if (maritalStatusFilter != null) {
                if (maritalStatusFilter.equalsIgnoreCase("Single")) {
                    if (p.getTwoRoom().equalsIgnoreCase("NA") || p.getTwoRoomAmt() <= 0) {
                        return false;
                    }
                } else if (maritalStatusFilter.equalsIgnoreCase("Married")) {
                    if ((p.getTwoRoom().equalsIgnoreCase("NA") || p.getTwoRoomAmt() <= 0) &&
                        (p.getThreeRoom().equalsIgnoreCase("NA") || p.getThreeRoomAmt() <= 0)) {
                        return false;
                    }
                }
            }

            return true;
        })
        .toList();

    if (filtered.isEmpty()) {
        System.out.println("!!! No projects match the selected filters !!!");
        return;
    }

    System.out.println("=== FILTERED PROJECT RESULTS ===");
    ProjectPrinter.viewProjects(filtered, roomFilter);

    }

}
