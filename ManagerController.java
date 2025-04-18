package assignment2002;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

import assignment2002.user.Manager;
import assignment2002.user.Officer;
import assignment2002.user.User;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.Data;
import assignment2002.utils.DateCheck;

public class ManagerController {
    private Manager manager;


    public ManagerController(Manager manager){
        this.manager = manager;
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
    
    public void showMenu(){
        Scanner sc = new Scanner(System.in);        
        boolean run = true;

        do {
            System.out.println("==== MANAGER MENU ====");
            System.out.println("1: Manage BTO Properties");
            System.out.println("2: Toggle Project Visibility");
            System.out.println("4: View All Projects");
            System.out.println("100: Logout"); //Temp Numbering

            int choice = sc.nextInt();

            switch (choice) {
                case 1-> manageBTOProjects(sc);
                case 2 -> viewVisibilityMenu(sc);
                case 4-> viewProjsMenu(sc);
                case 100-> run = false;
                default -> System.out.println("Retry");
        } 

    } while (run);
    sc.close();
    System.out.println("User Has Logged out!");
        
    }

    private void manageBTOProjects(Scanner sc){

        System.out.println("1: Add BTO Project");
        System.out.println("2: Edit BTO Project");
        System.out.println("3: Delete BTO Project");
        System.out.println("4: Back to Main Menu");
        int mode = sc.nextInt();
        sc.nextLine();
        

        switch(mode){
            case 1 -> createBTOMenu(sc);
            case 2 -> editBTOMenu(sc);
            case 3 -> deleteBTOMenu(sc);
            case 4 -> System.out.println("Returning Back");
        }
        
    }


    private void createBTOMenu(Scanner sc){
        String twoRoom = "2-Room", threeRoom = "3-Room";
        int twoRoomAmt = 0, twoRoomPrice = 0;
        int threeRoomAmt = 0, threeRoomPrice = 0;
        int officerSlot = 0;
        String openDate, closeDate;
        String projName;

        while (true) {
            System.out.print("Project Name: ");
            projName = sc.nextLine().trim();

            if(manager.projNameExists(Data.btoList, projName)){
                System.out.println("Duplicate Project Name");
                System.out.println("Please Try Again");
                continue;
            } break;

        }

        System.out.print("Neighbourhood: ");
        String neighbourhood = sc.nextLine();

        System.out.println("=== Room Types ===");
        System.out.println("1: 2 Room Only");
        System.out.println("2: 3 Room Only");
        System.out.println("3: 2 & 3 Room");
        int roomVal = sc.nextInt();
        sc.nextLine();

        switch (roomVal) {
            case 1:
                System.out.println("=== 2-Room Only ==="); 
                System.out.print("Number of units available: ");
                twoRoomAmt = sc.nextInt();
                System.out.print("Cost Per Unit: $ ");
                twoRoomPrice = sc.nextInt();
                break;
            case 2:
                System.out.println("=== 3-Room Only ==="); 
                System.out.print("Number of units available: ");
                threeRoomAmt = sc.nextInt();
                System.out.print("Cost Per Unit: $ ");
                threeRoomPrice = sc.nextInt();
                break;

            case 3: 
                System.out.println("=== 2 & 3 Rooms ==="); 
                System.out.print("(2-Room) Number of units available: ");
                twoRoomAmt = sc.nextInt();
                System.out.print("(2-Room) Cost Per Unit: $ ");
                twoRoomPrice = sc.nextInt();
                System.out.print("(3-Room) Number of units available: ");
                threeRoomAmt = sc.nextInt();
                System.out.print("(3-Room) Cost Per Unit: $ ");
                threeRoomPrice = sc.nextInt();
                break;
        
            default: 
                System.out.println("Try Again");
                break;
        }

        sc.nextLine();
        while(true){ //Ensures that closeDate must come after openDate
            do {
                System.out.print("Opening Date for HDB (MM/DD/YYYY): "); //Idk could be better
                openDate = sc.nextLine(); 
                
            } while (!DateCheck.dateValidator(openDate));
                       

            do {
                System.out.print("Closing Date for HDB (MM/DD/YYYY): "); //Idk could be better
                closeDate = sc.nextLine();
                
            } while (!DateCheck.dateValidator(closeDate));


            if(DateCheck.dateComparator(openDate, closeDate)){
                break;
            } else{
                System.out.println("Error with Date Ranges");
                System.out.println("Try Again\n");
            }

        }

        
        while(true){
            System.out.println("Max Number of Officers (1 ~ 10): ");
            officerSlot = sc.nextInt();
            sc.nextLine();

            if(officerSlot > 10 || officerSlot < 1){
                System.out.println("Error! Range between 1 ~ 10");
            } else{
                break;
            }

        }


        String visibleString;
        while (true) {
            System.out.println("== Visibility of Property ==");
            System.out.println("Visible = Y");
            System.out.println("Not Visible = N");
            System.out.print("Choice: ");
            visibleString = sc.nextLine().toLowerCase().trim();

            switch (visibleString) {
                case "y":
                    System.out.println("Visibility Selected: Visible");
                    visibleString = "TRUE";
                    break;
                case "n":
                    System.out.println("Visibility Selected: Not Visible");
                    visibleString = "FALSE";
                    break;
                default:
                    System.out.println("Invalid Input Try Again!");
                    break;
            }
            break;
            
        }

        System.out.printf("Project: %s Has Been Successfully Added\n", projName);
        String managerIC = manager.getName();
        ArrayList<Manager> managerICRef = new ArrayList<>();
        ArrayList<Officer> officerRef = new ArrayList<>();
        managerICRef.add(manager);

        String formattedString = String.join("\t", 
                projName, neighbourhood, twoRoom, String.valueOf(twoRoomAmt), String.valueOf(twoRoomPrice),
                threeRoom, String.valueOf(threeRoomAmt),String.valueOf(threeRoomPrice), openDate, closeDate,
                managerIC, String.valueOf(officerSlot), "Empty", visibleString); //14 Total Inputs

        manager.createBTOListing(Data.btoList, managerICRef, officerRef, formattedString);
        BTOFileService.appendBTO(formattedString);
    }

    private void deleteBTOMenu(Scanner sc){
        int choice;
        BTOProperty propertyToDel;
        System.out.println("Project List: ");

        if (Data.btoList.isEmpty()) {
            System.out.println("No projects available to delete.");
            return;
        }

        // Show all projects with index
        while(true){
            for (int i = 0; i < Data.btoList.size(); i++) {
                BTOProperty p = Data.btoList.get(i);
                System.out.printf("%d. %s (%s)\n", i + 1, p.getProjectName(), p.getNeighbourhood());
            }
    
            System.out.print("Enter the number of the project to delete (0 to cancel): ");
            choice = sc.nextInt();
            sc.nextLine();
    
            if (choice == 0) {
                System.out.println("Cancelled.");
                return;
            }
        
            if (choice < 1 || choice > Data.btoList.size()) {
                System.out.println("Invalid choice.");
                continue;
            }

            propertyToDel  = Data.btoList.get(choice - 1);
            System.out.println("=== Project DELETION Confirmation ===");
            System.out.printf("Project Chosen: %s\n", propertyToDel.getProjectName());
            System.out.printf("Project Neighbourhood: %s\n", propertyToDel.getNeighbourhood());
            System.out.println("CONFIRM? (Y/N)");

            String confirm = sc.nextLine().trim().toLowerCase();

            if (confirm.equals("y")) {
                break;
            }
            else{
                continue;
            }

        }


        if(manager.deleteBTOListing(Data.btoList, propertyToDel)){
            System.out.println("Project deleted successfully.");
            BTOFileService.removeBTO(propertyToDel.getProjectName());

        } else{
            System.out.println("An error has occurred");
            return;
        }
    }

    private void editBTOMenu(Scanner sc){
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
                case 1-> editProjNameMenu("projName", sc);
                case 2-> genericEditMenu("neighbourhood","Neighbourhood", sc);
                case 3 -> genericEditMenu("twoRoomAmt", "2-Room Amount", sc);
                case 4 -> genericEditMenu("twoRoomPrice", "2-Room Price", sc);
                case 5 -> genericEditMenu("threeRoomAmt", "3-Room Amount", sc);
                case 6 -> genericEditMenu("threeRoomPrice", "3-Room Price", sc);
                case 7 -> editOpenDateMenu("openDate", sc);
                case 8 -> editCloseDateMenu("closeDate",sc);
                case 9 -> editOfficerSlotsMenu("officerSlot",sc);
                default-> System.out.println("Invalid Choice Try Again");
            }
        }
        System.out.println("Exiting Menu");
    }

    private void editProjNameMenu(String colName, Scanner sc){
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

    private void editOpenDateMenu(String colName, Scanner sc){
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



    private void editCloseDateMenu(String colName, Scanner sc){
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

    private void editOfficerSlotsMenu(String colName, Scanner sc){
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

    private void genericEditMenu(String colName, String displayName, Scanner sc) {
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




    private void viewProjsMenu(Scanner sc){
        boolean running = true;
        while (running) {
            System.out.println("\n=== View Projects: Filter Menu ===");
            System.out.println("1: View All Projects");
            System.out.println("2: View Personally Created Projects");
            System.out.println("3: Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1 -> manager.viewAllProjects(Data.btoList);
                case 2 -> manager.viewMyProjs(Data.btoList);
                case 3 -> running = false;
                default -> System.out.println("Invalid Input");
            }

            
        }
    }

    

    private void viewVisibilityMenu(Scanner sc){
        manager.viewProjectsVisibility(Data.btoList);

        System.out.print("Enter project number to toggle visibility (0 to cancel): ");
        int choice = sc.nextInt();

        if (choice == 0) {
            System.out.println("Cancelled.");
            return;
        }

        if (choice < 1 || choice > Data.btoList.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        BTOProperty selected = Data.btoList.get(choice - 1);
        manager.toggleProjectVisiblity(selected);
        BTOFileService.editBTOByColumn(selected.getProjectName(), "visible", Boolean.toString(selected.isVisible()).toUpperCase());
        System.out.printf("Project '%s' is now %s.\n", selected.getProjectName(), selected.isVisible() ? "VISIBLE" : "HIDDEN");


    }
    
    
}
