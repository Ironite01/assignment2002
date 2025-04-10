package assignment2002;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import assignment2002.utils.DateCheck;


public class Manager extends User{
    
    public Manager(String name, String NRIC, int age, String maritalStatus, String password){
        super(name, NRIC, age, maritalStatus, password);
    }
    
    public void manageBTOProjects(ArrayList<BTOProperty> btoList){

        Scanner sc = new Scanner(System.in);

        String fileLoc = "Information/ProjectList.txt";

        System.out.println("1: Add BTO Project");
        System.out.println("2: Edit BTO Project");
        System.out.println("3: Delete BTO Project");
        int mode = sc.nextInt();
        sc.nextLine();
        

        switch(mode){
            //Create
            case 1:
            createBTOListing(btoList, sc, fileLoc);
            break;


            //Edit
            case 2:
            break;

            
            //Delete
            case 3:
            deleteBTOListing(btoList, sc);
            break;
        }
        
    }

    private void createBTOListing(ArrayList<BTOProperty> btoList, Scanner sc, String fileLoc){
        String twoRoom = "2-Room", threeRoom = "3-Room";
        int twoRoomAmt = 0, twoRoomPrice = 0;
        int threeRoomAmt = 0, threeRoomPrice = 0;
        int officerSlot = 0;
        String openDate, closeDate;

        System.out.print("Project Name: ");
        String projName = sc.nextLine();
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

        String managerIC = this.getName();
        ArrayList<Manager> managerICRef = new ArrayList<>();
        ArrayList<Officer> officerRef = new ArrayList<>();
        managerICRef.add(this);

        sc.nextLine();
        
        btoList.add(new BTOProperty(projName, neighbourhood, twoRoom, twoRoomAmt, twoRoomPrice, threeRoom, threeRoomAmt, threeRoomPrice, openDate, closeDate, managerICRef, roomVal, officerRef));

        String formattedString = String.join("\t", 
                projName, neighbourhood,twoRoom, String.valueOf(twoRoomAmt), String.valueOf(twoRoomPrice),
                threeRoom, String.valueOf(threeRoomAmt),String.valueOf(threeRoomPrice), openDate, closeDate,
                managerIC, String.valueOf(officerSlot), "Empty");


        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileLoc,true))){
            writer.newLine();
            writer.write(formattedString);

        } catch (IOException e){
            System.out.println("Exception: " + e);
            e.printStackTrace();
        } 

    }

    private void editBTOListing(){


    }

    private void deleteBTOListing(ArrayList<BTOProperty> btoList, Scanner sc){
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
        


    
        btoList.remove(propertyToDel);
        System.out.println("Project deleted successfully.");

        String filePath = "Information/ProjectList.txt";

        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));

            if (allLines.isEmpty()) {
                System.out.println("File is empty.");
                return;
            }

            String header = allLines.get(0);
            List<String> filtered = new ArrayList<>();

            // Keep only lines that DON'T match the project to delete
            for (int i = 1; i < allLines.size(); i++) {
                String line = allLines.get(i).trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\t");
                    String projectName = parts[0].trim();
                    if (!projectName.equalsIgnoreCase(propertyToDel.getProjectName())) {
                        filtered.add(line);
                    }
                }
            }

            // Write back header and filtered lines without trailing newline
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(header);
                if (!filtered.isEmpty()) writer.newLine();
                for (int i = 0; i < filtered.size(); i++) {
                    writer.write(filtered.get(i));
                    if (i != filtered.size() - 1) writer.newLine(); // no trailing newline
                }
            }

            System.out.println("Deleted project: " + propertyToDel.getProjectName());

        } catch (IOException e) {
            System.out.println("Error processing file: " + e.getMessage());
        }
    }
    


    public void toggleProjectVisiblity(ArrayList<BTOProperty> btolist, Scanner sc){
        System.out.println("Project List: ");

        for(int i = 0; i < btolist.size(); i++){
            BTOProperty projects = btolist.get(i);
            System.out.printf("%d. %s (%s): %s\n", i + 1, projects.getProjectName(), projects.getNeighbourhood(), projects.isVisible() ? "VISIBLE" : "HIDDEN");
        }

        System.out.print("Enter project number to toggle visibility (0 to cancel): ");
        int choice = sc.nextInt();

        if (choice == 0) {
            System.out.println("Cancelled.");
            return;
        }

        if (choice < 1 || choice > btolist.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        BTOProperty selected = btolist.get(choice - 1);
        selected.setVisible(!selected.isVisible());

        System.out.printf("Project '%s' is now %s.\n", selected.getProjectName(), selected.isVisible() ? "VISIBLE" : "HIDDEN");
    }

    //I renamed this 
    public void viewProjects(ArrayList<BTOProperty> btoList, Scanner sc){ //Filterable Function 
        boolean running = true;
        while (running) {
            System.out.println("\n=== View Projects: Filter Menu ===");
            System.out.println("1: View All Projects");
            System.out.println("2: View Personally Created Projects");
            System.out.println("3: Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1: //View all projects
                    //TODO: Maybe make this into a helper function? Rewrite this lol
                    System.out.printf("| %-15s | %-20s | %-15s | %-8s | %-11s | %-12s | %-10s | %-14s | %-14s | %-12s | %-12s | %-14s | %-30s |\n", "MANAGER","PROJECT NAME", "NEIGHBOURHOOD", "2-ROOM", "2-ROOM AMOUNT", "2-ROOM PRICE", "3-ROOM", "3-ROOM AMOUNT", "3-ROOM PRICE", "OPEN DATE", "CLOSE DATE", "OFFICER SLOTS", "OFFICERS REGISTERED");
                    
                    for(BTOProperty project: btoList){
                        String collatedOfficers = "";
                        String managerIC = "";


                        for(Officer offList:project.getOfficers()){
                            collatedOfficers += offList.getName() + ",";
                        }

                        for(Manager m: project.getManagerIC()){
                            managerIC = m.getName();
                        }
                        System.out.printf("| %-15s | %-20s | %-15s | %-8s | %-12d | %-13d | %-10s | %-14d | %-14d | %-12s | %-12s | %-14d | %-30s |\n", 
                        managerIC, project.getProjectName(), project.getNeighbourhood(), project.getTwoRoom(), 
                        project.getTwoRoomAmt(), project.getTwoRoomPrice(), project.getThreeRoom(), project.getThreeRoomAmt(),
                        project.getThreeRoomPrice(), project.getOpenDate(), project.getCloseDate(), project.getOfficerSlot(), collatedOfficers);
                        
                    }
                    break;
                case 2:
                    //TODO: Make this pretty
                    System.out.println("\n==== Projects Managed By You ====");
                    
                    for(BTOProperty property: btoList){
                        for(Manager m: property.getManagerIC()){
                            if(m.getNRIC().equals(this.getNRIC())){
                                property.allInfo();
                                break;
                            }
                        }
                    }

                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid Input");
                    break;
            }
        
            
        }

        



        
        
    }

    public void manageOfficerRegis(){

    }

    public void manageBTOApp(){

    }

    public void manageBTOWithdraw(){

    }

    public void generateReport(){ //Filterable

    }

    public void viewAllEnq(){

    }

    //TODO: 2 Methods Here
    // View & Reply Enquires
    // View Project Details

    public void viewMenu(ArrayList<User> userList, ArrayList<BTOProperty> btoList, Scanner sc){
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
                case 2 -> toggleProjectVisiblity(btoList, sc);
                case 4-> viewProjects(btoList,sc);
                case 100-> run = false;
                default -> System.out.println("Retry");
            
        } 


    }while (run);

    System.out.println("You have logged out!");
        
    }

}
