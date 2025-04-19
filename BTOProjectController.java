package assignment2002;
import assignment2002.user.Manager;
import assignment2002.user.Officer;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.Data;
import assignment2002.utils.DateCheck;

import java.util.ArrayList;
import java.util.List;
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
        System.out.println("1: Add BTO Project");
        System.out.println("2: Edit BTO Project");
        System.out.println("3: Delete BTO Project");
        System.out.println("4: Back to Main Menu");
        int mode = sc.nextInt();
        sc.nextLine();
        

        switch(mode){
            case 1 -> createBTOMenu();
            case 2 -> editContoller.editBTOMenu();
            case 3 -> deleteBTOMenu();
            case 4 -> System.out.println("Returning Back");
        }
    }

    private void createBTOMenu(){
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
        managerICRef.add(manager);

        String formattedString = String.join("\t", 
                projName, neighbourhood, twoRoom, String.valueOf(twoRoomAmt), String.valueOf(twoRoomPrice),
                threeRoom, String.valueOf(threeRoomAmt),String.valueOf(threeRoomPrice), openDate, closeDate,
                managerIC, String.valueOf(officerSlot), "Empty", visibleString); //14 Total Inputs


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



    
}
