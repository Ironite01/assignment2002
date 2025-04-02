package assignment2002;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
            break;
        }
        
    }

    //TODO: Test this function
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


    public void toggleProjectVisiblity(){

    }

    public void viewOtherProjects(){ //Filterable Function
        
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

            int choice = sc.nextInt();

            switch (choice) {
                case 1-> manageBTOProjects(btoList);
                case 100-> run = false;
                default -> System.out.println("Retry");
            
        } 


    }while (run);
        
    }

}
