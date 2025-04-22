package assignment2002;
import assignment2002.user.Manager;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.Data;
import assignment2002.utils.DateCheck;
import assignment2002.utils.InputUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class BTOProjectController {
    private Manager manager;
    private BTOEditController editContoller;
    private final Scanner sc;

    public BTOProjectController(Manager manager, Scanner sc){
        this.manager = manager;
        this.sc = sc;
        editContoller = new BTOEditController(manager, sc);
    }

    public void showProjectMenu(){
        System.out.println("1: Create BTO Project");
        System.out.println("2: Edit BTO Project");
        System.out.println("3: Delete BTO Project");
        System.out.println("4: Back to Main Menu");
        int mode = InputUtil.getValidatedIntRange(sc, "Choice ", 1, 4);
        

        switch(mode){
            case 1 -> createBTOMenu();
            case 2 -> editContoller.editBTOMenu();
            case 3 -> deleteBTOMenu();
            case 4 -> System.out.println("Returning Back");
        }
    }

    private void createBTOMenu(){
        if (manager.isCurrentlyManaging(Data.btoList)) {
            System.out.println("!!! UNABLE TO CREATE BTO !!!");
            System.out.println("!!! CURRENTLY MANAGING ANOTHER PROJECT !!!");
            return;
        }


        String twoRoom = "2-Room", threeRoom = "3-Room";
        int twoRoomAmt = 0, twoRoomPrice = 0;
        int threeRoomAmt = 0, threeRoomPrice = 0;
        int officerSlot = 0;
        String openDate, closeDate;
        String projName;

        while (true) {
            projName = InputUtil.getNonEmptyString(sc, "Project Name: ");

            if(manager.projNameExists(Data.btoList, projName)){
                System.out.println("\n !!Duplicate Project Name");
                System.out.println("Please Try Again");
                continue;
            } break;

        }

        // System.out.print("Neighbourhood: ");
        String neighbourhood = InputUtil.getNonEmptyString(sc, "Neighbourhood: ");

        System.out.println("=== Room Types ===");
        System.out.println("1: 2 Room Only");
        System.out.println("2: 3 Room Only");
        System.out.println("3: 2 & 3 Room");
        int roomVal = InputUtil.getValidatedIntRange(sc, "Room Choice: ", 1, 3);

        switch (roomVal) {
            case 1 ->{
                System.out.println("=== 2-Room Only ==="); 
                twoRoomAmt = InputUtil.getValidatedIntRange(sc, "Number of Units: ", 0, Integer.MAX_VALUE);
                twoRoomPrice = InputUtil.getValidatedIntRange(sc, "Cost Per Unit: $", 0, Integer.MAX_VALUE);}
            case 2 -> {
                System.out.println("=== 3-Room Only ==="); 
                threeRoomAmt = InputUtil.getValidatedIntRange(sc, "Number of Units: ", 0, Integer.MAX_VALUE);
                threeRoomPrice = InputUtil.getValidatedIntRange(sc, "Cost Per Unit: $", 0, Integer.MAX_VALUE);}

            case 3 -> {
                System.out.println("=== 2 & 3 Rooms ==="); 
                twoRoomAmt = InputUtil.getValidatedIntRange(sc, "[2-Room] Number of units available: ", 0, Integer.MAX_VALUE);
                twoRoomPrice = InputUtil.getValidatedIntRange(sc, "[2-Room] Cost Per Unit: $ ", 0, Integer.MAX_VALUE);
                threeRoomAmt = InputUtil.getValidatedIntRange(sc, "[3-Room] Number of units available: ", 0, Integer.MAX_VALUE);
                threeRoomPrice = InputUtil.getValidatedIntRange(sc, "[3-Room] Cost Per Unit: $ ", 0, Integer.MAX_VALUE);}
            default -> System.out.println("Try Again");
        }

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

        officerSlot = InputUtil.getValidatedIntRange(sc, "Max Number of Officers (1 ~ 10): ", 1, 10);


        System.out.println("== Visibility of Property ==");
        System.out.println("Visible = Y");
        System.out.println("Not Visible = N");
        String visibleString = InputUtil.getConfirmationString(sc, "Choice");

        switch (visibleString) {
            case "TRUE" -> System.out.println("Visibility Selected: Visible");
            case "FALSE" -> System.out.println("Visibility Selected: Not Visible");
            default -> System.out.println("Invalid Input Try Again!");
        }
            

        System.out.printf("Project: %s Has Been Successfully Added\n", projName);
        

        //APPEND LOGIC
        String managerIC = manager.getName();
        ArrayList<Manager> managerICRef = new ArrayList<>();
        managerICRef.add(manager);

        String formattedString = String.join("\t", 
                projName, neighbourhood, twoRoom, String.valueOf(twoRoomAmt), String.valueOf(twoRoomPrice),
                threeRoom, String.valueOf(threeRoomAmt),String.valueOf(threeRoomPrice), openDate, closeDate,
                managerIC, String.valueOf(officerSlot), "Empty", "Empty", "Empty", visibleString); //16 Total Inputs


        manager.createBTOListing(Data.btoList, managerICRef, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), formattedString);
        BTOFileService.appendBTO(formattedString);
    }

    private void deleteBTOMenu(){
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
            choice = InputUtil.getValidatedIntRange(sc, "Enter the number of the project to delete (0 to cancel): ", 0, Data.btoList.size());

            if(choice == 0){
                return;
            }

            propertyToDel  = Data.btoList.get(choice - 1);
            System.out.println("=== Project DELETION Confirmation ===");
            System.out.printf("Project Chosen: %s\n", propertyToDel.getProjectName());
            System.out.printf("Project Neighbourhood: %s\n", propertyToDel.getNeighbourhood());
            String confirm = InputUtil.getConfirmationString(sc, "Confirm ");

            if (confirm.equalsIgnoreCase("false")){
                continue;
            }
            break;

        }


        if(manager.deleteBTOListing(Data.btoList, propertyToDel)){
            System.out.println("Project deleted successfully.");
            BTOFileService.removeBTO(propertyToDel.getProjectName());

        } else{
            System.out.println("An error has occurred");
            return;
        }
    }
 
    
}
