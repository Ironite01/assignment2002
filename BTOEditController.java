package assignment2002;
import assignment2002.user.Manager;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.Data;
import assignment2002.utils.DateCheck;
import assignment2002.utils.FileManifest;
import assignment2002.utils.InputUtil;

import java.util.List;
import java.util.Scanner;

public class BTOEditController implements FileManifest {
    private Manager manager;
    private final Scanner sc;

    public BTOEditController (Manager manager, Scanner sc){
        this.manager = manager;
        this.sc = sc;
    }

    private String getColValue(BTOProperty p, PROJECT_COLUMNS colName) {
        return switch (colName) {
            case PROJECT_COLUMNS.NEIGHBOURHOOD -> p.getNeighbourhood();
            case PROJECT_COLUMNS.TWO_ROOM_AMT -> String.valueOf(p.getTwoRoomAmt());
            case PROJECT_COLUMNS.TWO_ROOM_PRICE -> String.valueOf(p.getTwoRoomPrice());
            case PROJECT_COLUMNS.THREE_ROOM_AMT -> String.valueOf(p.getThreeRoomAmt());
            case PROJECT_COLUMNS.THREE_ROOM_PRICE -> String.valueOf(p.getThreeRoomPrice());
            case PROJECT_COLUMNS.OPEN_DATE -> p.getOpenDate();
            case PROJECT_COLUMNS.CLOSE_DATE -> p.getCloseDate();
            case PROJECT_COLUMNS.OFFICER_SLOT -> String.valueOf(p.getOfficerSlot());
            case PROJECT_COLUMNS.VISIBLE -> Boolean.toString(p.isVisible());
            default -> "Undefined";
        };
    }
    

    public void editBTOMenu(){
        boolean running = true;

        while (running) {
            System.out.println("==== EDIT MENU ====");
            System.out.println("1. Edit Project Name");
            System.out.println("2. Edit Neighbourhood");
            System.out.println("3. Edit Two Room Amount");
            System.out.println("4. Edit Two Room Price");
            System.out.println("5. Edit Three Room Amount");
            System.out.println("6. Edit Three Room Price");
            System.out.println("7. Edit Open Date");
            System.out.println("8. Edit Close Date");
            System.out.println("9. Adjust Maximum Officer Slots");
            System.out.println("10. Edit Visibility"); //Idk if this should be here
            System.out.println("11. Exit Menu"); 

            int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 11);

            switch (choice) {
                case 11-> running = false;
                case 1-> editProjNameMenu(PROJECT_COLUMNS.PROJECT_NAME);
                case 2-> genericEditMenu(PROJECT_COLUMNS.NEIGHBOURHOOD,"Neighbourhood");
                case 3 -> genericEditMenu(PROJECT_COLUMNS.TWO_ROOM_AMT, "2-Room Amount");
                case 4 -> genericEditMenu(PROJECT_COLUMNS.TWO_ROOM_PRICE, "2-Room Price");
                case 5 -> genericEditMenu(PROJECT_COLUMNS.THREE_ROOM_AMT, "3-Room Amount");
                case 6 -> genericEditMenu(PROJECT_COLUMNS.THREE_ROOM_PRICE, "3-Room Price");
                case 7 -> editOpenDateMenu(PROJECT_COLUMNS.OPEN_DATE);
                case 8 -> editCloseDateMenu(PROJECT_COLUMNS.CLOSE_DATE);
                case 9 -> editOfficerSlotsMenu(PROJECT_COLUMNS.OFFICER_SLOT);
                default-> System.out.println("Invalid Choice Try Again");
            }
        }
        System.out.println("Exiting Menu");
    }

    private String inputProjName(){
        String projName;
        while (true){ //Pick a Project
            projName = InputUtil.getNonEmptyString(sc, "Project Name: ");

            if(manager.projNameExists(Data.btoList, projName)){
                break;
            }
            System.out.println("Invalid Project Try Again!\n");
        }
        return projName;
    }

    private void editProjNameMenu(PROJECT_COLUMNS colName){
        manager.viewAllProjects(Data.btoList);
        String oldProjName, newProjName;

        System.out.println("==== PROJ NAME EDIT ====");
        oldProjName = inputProjName();

        while (true) { //New Name
            newProjName = InputUtil.getNonEmptyString(sc, "New Project Name: ");

            if(manager.projNameExists(Data.btoList, newProjName)){
                System.out.println("Name Already Taken");
                System.out.println("Please Try Again!");
                continue;
            } break;
        }


        while (true) { //Confirmation
            System.out.println("=== PROJ NAME CHANGES ===");
            System.out.println(oldProjName + " -> " + newProjName);
            System.out.println("Old Proj Name: " + oldProjName);
            System.out.println("New Proj Name: " + newProjName);
            String confirm = InputUtil.getConfirmationString(sc, "Confirm");

            if(confirm.equalsIgnoreCase("true")){
                manager.updateProjectName(Data.btoList, oldProjName, newProjName);
                BTOFileService.editBTOByColumn(oldProjName, colName, newProjName, false);
                break;
            } 
            else if(confirm.equalsIgnoreCase("false")){
                System.out.println("Exiting out");
                break;
            }
            else{
                System.out.println("Try Again");
            }
        }


    }

    private void editOpenDateMenu(PROJECT_COLUMNS colName){
        String projName;

        manager.viewAllProjects(Data.btoList);
        System.out.println("=== OPEN DATE EDIT ===");

        projName = inputProjName();

        String checkName = projName;
        // Extract old value
        String oldOpenDate = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> getColValue(p, colName))
            .findFirst()
            .orElse("Unknown");

        String closeDate = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> getColValue(p, PROJECT_COLUMNS.CLOSE_DATE))
            .findFirst()
            .orElse("Unknown");


        while (true) {
            System.out.println("Old Open Date: " + oldOpenDate);
            System.out.println("Current Close Date: " + closeDate);
            String newOpenDate = InputUtil.getNonEmptyString(sc, "New Open Date (MM/DD/YYYY): ");
    
            if(!DateCheck.dateValidator(newOpenDate)){
                System.out.println("Try Again");
                continue;
            }

            if (!DateCheck.dateComparator(newOpenDate, closeDate)) {
                System.out.println("=== ERROR! ===");
                System.out.println("Open Date comes after Close Date! Try Again!\n");
                continue;
            }

            System.out.printf("Confirm change from %s -> %s\n", oldOpenDate, newOpenDate);
            String confirm = InputUtil.getConfirmationString(sc, "Confirm");
    
            if (confirm.equalsIgnoreCase("true")) {
                manager.updateBTOByColumn(Data.btoList, projName, colName, newOpenDate); // optional
                BTOFileService.editBTOByColumn(projName, colName, newOpenDate, false);
                break;
            } else if(confirm.equalsIgnoreCase("false")){
                System.out.println("Cancelled.");
                System.out.println("Exiting....");
            }
            else{
                System.out.println("Invalid Input Try Again!");
            }
        }


    }



    private void editCloseDateMenu(PROJECT_COLUMNS colName){
        String projName;

        manager.viewAllProjects(Data.btoList);
        System.out.println("=== CLOSE DATE EDIT ===");

        projName = inputProjName();

        String checkName = projName;
        // Extract old value
        String oldCloseDate = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> getColValue(p, colName))
            .findFirst()
            .orElse("Unknown");

        String openDate = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> getColValue(p, PROJECT_COLUMNS.OPEN_DATE))
            .findFirst()
            .orElse("Unknown");


        while (true) {
            System.out.println("Old Close Date: " + oldCloseDate);
            System.out.println("Current Open Date: " + openDate);
            String newCloseDate = InputUtil.getNonEmptyString(sc, "New Close Date (MM/DD/YYYY): ");
    
            if(!DateCheck.dateValidator(newCloseDate)){
                System.out.println("Try Again");
                continue;
            }

            if (!DateCheck.dateComparator(openDate, newCloseDate)) {
                System.out.println("=== ERROR! ===");
                System.out.println("Close Date Comes Before Open Date! Try Again!\n");
                continue;
            }

            System.out.printf("Confirm change from %s -> %s?\n", oldCloseDate, newCloseDate);
            String confirm = InputUtil.getConfirmationString(sc, "Confirm");
    
            if (confirm.equalsIgnoreCase("true")) {
                manager.updateBTOByColumn(Data.btoList, projName, colName, newCloseDate); // optional
                BTOFileService.editBTOByColumn(projName, colName, newCloseDate, false);
                break;
            } else if(confirm.equalsIgnoreCase("false")){
                System.out.println("Cancelled.");
                System.out.println("Exiting....");
            }
            else{
                System.out.println("Invalid Input Try Again!");
            }
        }
    }

    private void editOfficerSlotsMenu(PROJECT_COLUMNS colName){
        String projName;

        manager.viewAllProjects(Data.btoList);
        System.out.println("=== OFFICER SLOT EDIT ===");

        projName = inputProjName();

        String checkName = projName;
        // Extract old value
        String oldOfficerSlots = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> getColValue(p, colName))
            .findFirst()
            .orElse("Unknown");
        
        int currentOfficersRegistered = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> p.getOfficers().size())  
            .findFirst()
            .orElse(0); 
        

        while (true) {
            System.out.println("=== EDIT SLOT AMOUNT ===");
            System.out.println("Current Total Officer Slots: " + oldOfficerSlots);
            System.out.println("Current Registered Officer Amount: " + currentOfficersRegistered);
            int newOfficerSlots = InputUtil.getValidatedIntRange(sc,"New Officer Slots (1 ~ 10): ", 1, 10);

            if(newOfficerSlots < currentOfficersRegistered){
                System.out.println("\n!! Invalid Input new amount LESS THAN currently registered !!\n");
                continue;
            }


            System.out.printf("Confirm change from %s -> %d?\n", oldOfficerSlots, newOfficerSlots);
            String confirm = InputUtil.getConfirmationString(sc, "Confirm");
    
            if (confirm.equalsIgnoreCase("true")) {
                manager.updateBTOByColumn(Data.btoList, projName, colName, String.valueOf(newOfficerSlots));
                BTOFileService.editBTOByColumn(projName, colName, String.valueOf(newOfficerSlots),false);
                break;
            } else if(confirm.equalsIgnoreCase("false")){
                System.out.println("Cancelled.");
                System.out.println("Exiting....");
            }
            else{
                System.out.println("Invalid Input Try Again!");
            }
        }
    }

    private void genericEditMenu(PROJECT_COLUMNS colName, String displayName) {
        manager.viewAllProjects(Data.btoList);
    
        System.out.printf("==== %s EDIT ====\n", displayName.toUpperCase());

        String projName = inputProjName();
        
        String checkName = projName;
        // Extract old value
        String oldValue = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> getColValue(p, colName))
            .findFirst()
            .orElse("Unknown");
    
        String newValue;
        while (true) {
            System.out.println("Old " + displayName + ": " + oldValue);

            if (List.of(PROJECT_COLUMNS.TWO_ROOM_AMT, PROJECT_COLUMNS.TWO_ROOM_PRICE,
            		PROJECT_COLUMNS.THREE_ROOM_AMT, PROJECT_COLUMNS.THREE_ROOM_PRICE).contains(colName)) {
                   int intVal = InputUtil.getValidatedIntRange(sc, "New " + displayName + ": ", 0, Integer.MAX_VALUE);
                   newValue = String.valueOf(intVal);
            } else{
                newValue = InputUtil.getNonEmptyString(sc, "New " + displayName + ": ");
            }
    
            System.out.printf("Confirm change from %s -> %s?\n", oldValue, newValue);
            String confirm = InputUtil.getConfirmationString(sc, "Confirm");
    
            if (confirm.equalsIgnoreCase("true")) {
                manager.updateBTOByColumn(Data.btoList, projName, colName, newValue); // optional
                BTOFileService.editBTOByColumn(projName, colName, newValue,false);
                break;
            } else if(confirm.equalsIgnoreCase("false")){
                System.out.println("Cancelled.");
                System.out.println("Exiting....");
            }
            else{
                System.out.println("Invalid Input Try Again!");
            }
        }
        
    }
}
