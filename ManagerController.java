package assignment2002;

import java.util.ArrayList;
import java.util.Scanner;

import assignment2002.user.Manager;
import assignment2002.user.Officer;
import assignment2002.user.User;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.ProjectPrinter;
import assignment2002.utils.DateCheck;

public class ManagerController {
    private Manager manager;
    private ArrayList<BTOProperty> btoList;
    private ArrayList<User> userList;


    public ManagerController(Manager manager, ArrayList<BTOProperty> btoList, ArrayList<User> userList){
        this.manager = manager;
        this.btoList = btoList;
        this.userList = userList;
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
                case 1-> manageBTOProjects(btoList);
                case 2 -> viewVisibilityMenu(btoList, sc);
                case 4-> viewProjsMenu(btoList, sc);
                case 100-> run = false;
                default -> System.out.println("Retry");
        } 

    } while (run);
    sc.close();
    System.out.println("User Has Logged out!");
        
    }

    private void manageBTOProjects(ArrayList<BTOProperty> btoList){

        Scanner sc = new Scanner(System.in);

        System.out.println("1: Add BTO Project");
        System.out.println("2: Edit BTO Project");
        System.out.println("3: Delete BTO Project");
        System.out.println("4: Back to Main Menu");
        int mode = sc.nextInt();
        sc.nextLine();
        

        switch(mode){
            //Create
            case 1->createBTOMenu(this.manager, btoList, sc);


            //Edit
            case 2 -> System.out.println("TEMP");

            
            //Delete
            case 3 -> deleteBTOMenu(btoList, sc);
            
            case 4 -> System.out.println("Returning Back");
        }
        
    }


    private void createBTOMenu(Manager manager, ArrayList<BTOProperty> btoList, Scanner sc){
        String twoRoom = "2-Room", threeRoom = "3-Room";
        int twoRoomAmt = 0, twoRoomPrice = 0;
        int threeRoomAmt = 0, threeRoomPrice = 0;
        int officerSlot = 0;
        String openDate, closeDate;
        String projName;

        while (true) {
            System.out.print("Project Name: ");
            projName = sc.nextLine().trim();

            String temp = projName;
            boolean exists = btoList.stream()
            .anyMatch(p -> p.getProjectName().equalsIgnoreCase(temp));

            if(exists){
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

            if(officerSlot > 10 || officerSlot < 1){
                System.out.println("Error! Range between 1 ~ 10");
            } else{
                break;
            }

        }

        String managerIC = manager.getName();
        ArrayList<Manager> managerICRef = new ArrayList<>();
        ArrayList<Officer> officerRef = new ArrayList<>();
        managerICRef.add(manager);

        sc.nextLine();

        String formattedString = String.join("\t", 
                projName, neighbourhood, twoRoom, String.valueOf(twoRoomAmt), String.valueOf(twoRoomPrice),
                threeRoom, String.valueOf(threeRoomAmt),String.valueOf(threeRoomPrice), openDate, closeDate,
                managerIC, String.valueOf(officerSlot), "Empty"); //13 Total Inputs

        manager.createBTOListing(btoList, managerICRef, officerRef, formattedString);
        BTOFileService.appendBTO(formattedString);
    }

    private void deleteBTOMenu(ArrayList<BTOProperty> btoList, Scanner sc){
        int choice;
        BTOProperty propertyToDel;
        System.out.println("Project List: ");

        if (btoList.isEmpty()) {
            System.out.println("No projects available to delete.");
            return;
        }

        // Show all projects with index
        while(true){
            for (int i = 0; i < btoList.size(); i++) {
                BTOProperty p = btoList.get(i);
                System.out.printf("%d. %s (%s)\n", i + 1, p.getProjectName(), p.getNeighbourhood());
            }
    
            System.out.print("Enter the number of the project to delete (0 to cancel): ");
            choice = sc.nextInt();
            sc.nextLine();
    
            if (choice == 0) {
                System.out.println("Cancelled.");
                return;
            }
        
            if (choice < 1 || choice > btoList.size()) {
                System.out.println("Invalid choice.");
                continue;
            }

            propertyToDel  = btoList.get(choice - 1);
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


        if(manager.deleteBTOListing(btoList, propertyToDel)){
            System.out.println("Project deleted successfully.");
            BTOFileService.removeBTO(propertyToDel.getProjectName());

        } else{
            System.out.println("An error has occurred");
            return;
        }
    }

    private void viewProjsMenu(ArrayList<BTOProperty> btoList, Scanner sc){
        boolean running = true;
        while (running) {
            System.out.println("\n=== View Projects: Filter Menu ===");
            System.out.println("1: View All Projects");
            System.out.println("2: View Personally Created Projects");
            System.out.println("3: Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1 -> manager.viewAllProjects(btoList);
                case 2 -> manager.viewMyProjs(btoList);
                case 3 -> running = false;
                default -> System.out.println("Invalid Input");
            }

            
        }
    }

    private void viewVisibilityMenu(ArrayList<BTOProperty> btoList, Scanner sc){
        ProjectPrinter.viewProjectsVisibility(btoList);

        System.out.print("Enter project number to toggle visibility (0 to cancel): ");
        int choice = sc.nextInt();

        if (choice == 0) {
            System.out.println("Cancelled.");
            return;
        }

        if (choice < 1 || choice > btoList.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        BTOProperty selected = btoList.get(choice - 1);
        manager.toggleProjectVisiblity(selected);
        System.out.printf("Project '%s' is now %s.\n", selected.getProjectName(), selected.isVisible() ? "VISIBLE" : "HIDDEN");


    }
    
    
}
