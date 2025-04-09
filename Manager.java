package assignment2002;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

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

        //NEEDS TO BE ADJUSTED FIXED TO ACTUAL DATES TO MAKE SURE OPEN IS NOT AFTER CLOSING ETC
        //Need to add validator to compare that closeDate is after startDate

        sc.nextLine();
        while(true){ //Ensures that closeDate must come after openDate
            do {
                System.out.print("Opening Date for HDB (MM/DD/YYYY): "); //Idk could be better
                openDate = sc.nextLine(); 
                
            } while (!dateValidator(openDate));
                       
    
            do {
                System.out.print("Closing Date for HDB (MM/DD/YYYY): "); //Idk could be better
                closeDate = sc.nextLine();
                
            } while (!dateValidator(closeDate));

            if(dateComparator(openDate, closeDate)){
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
        System.out.println("Project List: ");

        if (btoList.isEmpty()) {
            System.out.println("No projects available to delete.");
            return;
        }

        // Show all projects with index
        for (int i = 0; i < btoList.size(); i++) {
            BTOProperty p = btoList.get(i);
            System.out.printf("%d. %s (%s)\n", i + 1, p.getProjectName(), p.getNeighbourhood());
        }

        System.out.print("Enter the number of the project to delete (0 to cancel): ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 0) {
            System.out.println("Cancelled.");
            return;
        }
    
        if (choice < 1 || choice > btoList.size()) {
            System.out.println("Invalid choice.");
            return;
        }
    
        BTOProperty selected = btoList.get(choice - 1);
        btoList.remove(selected);
        System.out.println("Project deleted successfully.");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Information/ProjectList.txt"))) {
            // Write header
            writer.write("ProjectName\tNeighbourhood\tTwoRoom\tTwoRoomAmt\tTwoRoomPrice\tThreeRoom\tThreeRoomAmt\tThreeRoomPrice\tOpenDate\tCloseDate\tManagerNRIC\tOfficerSlot\tOfficerNames");
            writer.newLine();
    
            for (BTOProperty p : btoList) {
                StringBuilder officerNames = new StringBuilder();
                for (Officer o : p.getOfficers()) {
                    officerNames.append(o.getName()).append(",");
                }
    
                // Remove trailing comma if needed
                if (officerNames.length() > 0) {
                    officerNames.setLength(officerNames.length() - 1);
                }
    
                String managerNRIC = p.getManagerIC().get(0).getName(); // assuming 1 manager
    
                String formatted = String.join("\t",
                    p.getProjectName(),
                    p.getNeighbourhood(),
                    p.getTwoRoom(),
                    String.valueOf(p.getTwoRoomAmt()),
                    String.valueOf(p.getTwoRoomPrice()),
                    p.getThreeRoom(),
                    String.valueOf(p.getThreeRoomAmt()),
                    String.valueOf(p.getThreeRoomPrice()),
                    p.getOpenDate(),
                    p.getCloseDate(),
                    managerNRIC,
                    String.valueOf(p.getOfficerSlot()),
                    officerNames.toString()
                );
    
                writer.write(formatted);
                writer.newLine();
            }
    
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }
    

    private boolean dateValidator(String inputDate){ //Return true if valid
        String dateRegex = "^(0[1-9]|1[0-2])/([0][1-9]|[12][0-9]|3[01])/(202[5-9]|20[3-9][0-9]|2[1-9][0-9]{2})$";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        if(!inputDate.matches(dateRegex)){ //Input does not match Date regex
            System.out.println("Date is invalid");
            return false;
        }

        try {
            LocalDate.parse(inputDate, formatter);
            System.out.println("Date Accepted");
            return true;
            
        } catch (DateTimeParseException e) {
                System.out.println("Date is invalid 2");
                return false;
            }

    }

    private boolean dateComparator(String openDate, String closeDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            
        LocalDate opDate = LocalDate.parse(openDate, formatter);
        LocalDate clDate = LocalDate.parse(closeDate, formatter);

        if(clDate.isAfter(opDate)){
            return true;
        } 

        return false;


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
                    //TODO: Maybe make this into a helper function?
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
