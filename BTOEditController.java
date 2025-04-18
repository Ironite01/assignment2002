package assignment2002;
import java.util.List;
import java.util.Scanner;

import assignment2002.user.Manager;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.Data;
import assignment2002.utils.DateCheck;

public class BTOEditController {
    private Manager manager;
    private final Scanner sc;

    public BTOEditController (Manager manager, Scanner sc){
        this.manager = manager;
        this.sc = sc;
    }

    private String getColValue(BTOProperty p, String colName) {
        return switch (colName) {
            case "neighbourhood" -> p.getNeighbourhood();
            case "twoRoomAmt" -> String.valueOf(p.getTwoRoomAmt());
            case "twoRoomPrice" -> String.valueOf(p.getTwoRoomPrice());
            case "threeRoomAmt" -> String.valueOf(p.getThreeRoomAmt());
            case "threeRoomPrice" -> String.valueOf(p.getThreeRoomPrice());
            case "openDate" -> p.getOpenDate();
            case "closeDate" -> p.getCloseDate();
            case "officerSlot" -> String.valueOf(p.getOfficerSlot());
            case "visible" -> Boolean.toString(p.isVisible());
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
            System.out.println("0. Exit Menu"); 

            int choice = sc.nextInt();
            sc.nextLine();



            switch (choice) {
                case 0-> running = false;
                case 1-> editProjNameMenu("projName");
                case 2-> genericEditMenu("neighbourhood","Neighbourhood");
                case 3 -> genericEditMenu("twoRoomAmt", "2-Room Amount");
                case 4 -> genericEditMenu("twoRoomPrice", "2-Room Price");
                case 5 -> genericEditMenu("threeRoomAmt", "3-Room Amount");
                case 6 -> genericEditMenu("threeRoomPrice", "3-Room Price");
                case 7 -> editOpenDateMenu("openDate");
                case 8 -> editCloseDateMenu("closeDate");
                case 9 -> editOfficerSlotsMenu("officerSlot");
                default-> System.out.println("Invalid Choice Try Again");
            }
        }
        System.out.println("Exiting Menu");
    }

    private void editProjNameMenu(String colName){
        manager.viewAllProjects(Data.btoList);
        String oldProjName, newProjName;

        while (true){ //Pick a Project
            System.out.println("==== PROJ NAME EDIT ====");
            System.out.print("Project Name: ");
            oldProjName = sc.nextLine();

            if(manager.projNameExists(Data.btoList, oldProjName)){
                break;
            }
            System.out.println("Try Again!\n");
        }

        while (true) { //New Name
            System.out.print("New Project Name: ");
            newProjName = sc.nextLine();

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
            System.out.print("Confirm (Y/N): ");

            String confirm = sc.nextLine().toLowerCase().trim();

            if(confirm.equals("y")){
                //Make Manager Function + BTOFileService to Edit Name only
                manager.updateProjectName(Data.btoList, oldProjName, newProjName);
                BTOFileService.editBTOByColumn(oldProjName, colName, newProjName);
                // BTOFileService.editBTOProjectName(oldProjName,newProjName);
                break;
            } 
            else if(confirm.equals("n")){
                System.out.println("Exiting out");
                break;
            }
            else{
                System.out.println("Try Again");
            }
        }


    }

    private void editOpenDateMenu(String colName){
        String projName;

        manager.viewAllProjects(Data.btoList);
        System.out.println("=== OPEN DATE EDIT ===");

        while (true) {
            System.out.print("Enter Project Name to Edit: ");
            projName = sc.nextLine().trim();

            if (manager.projNameExists(Data.btoList, projName)) {
                break;
            } else{
                System.out.println("Project Name Not Found");
            }
        }

        String checkName = projName;
        // Extract old value
        String oldOpenDate = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> getColValue(p, colName))
            .findFirst()
            .orElse("Unknown");

        String closeDate = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> getColValue(p, "closeDate"))
            .findFirst()
            .orElse("Unknown");


        while (true) {
            System.out.println("Old Open Date: " + oldOpenDate);
            System.out.println("Current Close Date: " + closeDate);
            System.out.print("New Open Date (MM/DD/YYYY): ");
            String newOpenDate = sc.nextLine().trim();
    
            if(!DateCheck.dateValidator(newOpenDate)){
                System.out.println("Try Again");
                continue;
            }

            if (!DateCheck.dateComparator(newOpenDate, closeDate)) {
                System.out.println("=== ERROR! ===");
                System.out.println("Open Date comes after Close Date! Try Again!\n");
                continue;
            }

            System.out.printf("Confirm change from %s to %s? (Y/N): ", oldOpenDate, newOpenDate);
            String confirm = sc.nextLine().trim().toLowerCase();
    
            if (confirm.equals("y")) {
                manager.updateBTOByColumn(Data.btoList, projName, colName, newOpenDate); // optional
                BTOFileService.editBTOByColumn(projName, colName, newOpenDate);
                break;
            } else if(confirm.equals("n")){
                System.out.println("Cancelled.");
                System.out.println("Exiting....");
            }
            else{
                System.out.println("Invalid Input Try Again!");
            }
        }


    }



    private void editCloseDateMenu(String colName){
        String projName;

        manager.viewAllProjects(Data.btoList);
        System.out.println("=== CLOSE DATE EDIT ===");

        while (true) {
            System.out.print("Enter Project Name to Edit: ");
            projName = sc.nextLine().trim();

            if (manager.projNameExists(Data.btoList, projName)) {
                break;
            } else{
                System.out.println("Project Name Not Found");
            }
        }

        String checkName = projName;
        // Extract old value
        String oldCloseDate = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> getColValue(p, colName))
            .findFirst()
            .orElse("Unknown");

        String openDate = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> getColValue(p, "openDate"))
            .findFirst()
            .orElse("Unknown");


        while (true) {
            System.out.println("Old Close Date: " + oldCloseDate);
            System.out.println("Current Open Date: " + openDate);
            System.out.print("New Close Date (MM/DD/YYYY): ");
            String newCloseDate = sc.nextLine().trim();
    
            if(!DateCheck.dateValidator(newCloseDate)){
                System.out.println("Try Again");
                continue;
            }

            if (!DateCheck.dateComparator(openDate, newCloseDate)) {
                System.out.println("=== ERROR! ===");
                System.out.println("Close Date Comes Before Open Date! Try Again!\n");
                continue;
            }

            System.out.printf("Confirm change from %s to %s? (Y/N): ", oldCloseDate, newCloseDate);
            String confirm = sc.nextLine().trim().toLowerCase();
    
            if (confirm.equals("y")) {
                manager.updateBTOByColumn(Data.btoList, projName, colName, newCloseDate); // optional
                BTOFileService.editBTOByColumn(projName, colName, newCloseDate);
                break;
            } else if(confirm.equals("n")){
                System.out.println("Cancelled.");
                System.out.println("Exiting....");
            }
            else{
                System.out.println("Invalid Input Try Again!");
            }
        }
    }

    private void editOfficerSlotsMenu(String colName){
        String projName;

        manager.viewAllProjects(Data.btoList);
        System.out.println("=== OFFICER SLOT EDIT ===");

        while (true) {
            System.out.print("Enter Project Name to Edit: ");
            projName = sc.nextLine().trim();

            if (manager.projNameExists(Data.btoList, projName)) {
                break;
            } else{
                System.out.println("Project Name Not Found");
            }
        }

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
            System.out.print("New Officer Slots (1 ~ 10): ");
            int newOfficerSlots = sc.nextInt();
            sc.nextLine();

            if(newOfficerSlots < 1 || newOfficerSlots > 10){
                System.out.println("Invalid Slots Try Again!\n");
                continue;
            }

            if(newOfficerSlots < currentOfficersRegistered){
                System.out.println("Currently Registered Officers > New Slot Amount");
                System.out.println("Try Again!");
                continue;
            }


            System.out.printf("Confirm change from %s to %d? (Y/N): ", oldOfficerSlots, newOfficerSlots);
            String confirm = sc.nextLine().trim().toLowerCase();
    
            if (confirm.equals("y")) {
                manager.updateBTOByColumn(Data.btoList, projName, colName, String.valueOf(newOfficerSlots)); // optional
                BTOFileService.editBTOByColumn(projName, colName, String.valueOf(newOfficerSlots));
                break;
            } else if(confirm.equals("n")){
                System.out.println("Cancelled.");
                System.out.println("Exiting....");
            }
            else{
                System.out.println("Invalid Input Try Again!");
            }
        }
    }

    private void genericEditMenu(String colName, String displayName) {
        String projName;
        manager.viewAllProjects(Data.btoList);
    
        System.out.printf("==== %s EDIT ====\n", displayName.toUpperCase());

        while (true) {
            System.out.print("Enter Project Name to Edit: ");
            projName = sc.nextLine().trim();

            if (manager.projNameExists(Data.btoList, projName)) {
                break;
            } else{
                System.out.println("Project Name Not Found");
            }
        }
        
        String checkName = projName;
        // Extract old value
        String oldValue = Data.btoList.stream()
            .filter(p -> p.getProjectName().equalsIgnoreCase(checkName))
            .map(p -> getColValue(p, colName))
            .findFirst()
            .orElse("Unknown");
    
        while (true) {
            System.out.println("Old " + displayName + ": " + oldValue);
            System.out.print("New " + displayName + ": ");
            String newValue = sc.nextLine().trim();

            if (List.of("twoRoomAmt", "twoRoomPrice", "threeRoomAmt", "threeRoomPrice").contains(colName)) {
                try {
                    int parsed = Integer.parseInt(newValue);
                    if (parsed < 0) {
                        System.out.println("Value cannot be negative. Try again!");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format. Please enter a valid integer.");
                    continue;
                }
            }
    
            System.out.printf("Confirm change from %s to %s? (Y/N): ", oldValue, newValue);
            String confirm = sc.nextLine().trim().toLowerCase();
    
            if (confirm.equals("y")) {
                manager.updateBTOByColumn(Data.btoList, projName, colName, newValue); // optional
                BTOFileService.editBTOByColumn(projName, colName, newValue);
                break;
            } else if(confirm.equals("n")){
                System.out.println("Cancelled.");
                System.out.println("Exiting....");
            }
            else{
                System.out.println("Invalid Input Try Again!");
            }
        }
        
    }
}
