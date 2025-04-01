import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Manager extends User{
    
    public Manager(String name, String NRIC, int age, String maritalStatus, String password){
        super(name, NRIC, age, maritalStatus, password);
    }
    
    public void manageBTOProjects(Manager u, ArrayList<BTOProperties> btoList){

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

            String twoRoom = "2-Room", threeRoom = "3-Room";
            int twoRoomAmt = 0, twoRoomPrice = 0;
            int threeRoomAmt = 0, threeRoomPrice = 0;
            int officerSlot = 0;

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
            sc.nextLine();
            System.out.print("Opening Date for HDB (MM/DD/YYYY): "); //Idk could be better
            String openDate = sc.nextLine();            

            System.out.print("Closing Date for HDB (MM/DD/YYYY): "); //Idk could be better
            String closeDate = sc.nextLine();            


            while(true){
                System.out.println("Max Number of Officers (1 ~ 10): ");
                officerSlot = sc.nextInt();

                if(officerSlot > 10 || officerSlot < 1){
                    System.out.println("Error! Range between 1 ~ 10");
                } else{
                    break;
                }

            }

            String managerIC = u.getName();
            ArrayList<Manager> managerICRef = new ArrayList<>();
            ArrayList<Officer> officerRef = new ArrayList<>();
            managerICRef.add(u);

            sc.nextLine();
            
            btoList.add(new BTOProperties(projName, neighbourhood, twoRoom, twoRoomAmt, twoRoomPrice, threeRoom, threeRoomAmt, threeRoomPrice, openDate, closeDate, managerICRef, roomVal, officerRef));

            String formattedString = String.join("\t", 
                    projName, neighbourhood,twoRoom, String.valueOf(twoRoomAmt), String.valueOf(twoRoomPrice),
                    threeRoom, String.valueOf(threeRoomAmt),String.valueOf(threeRoomPrice), openDate, closeDate,
                    managerIC, String.valueOf(officerSlot), "Sheesh");


            try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileLoc,true))){
                writer.newLine();
                writer.write(formattedString);

            } catch (IOException e){
                System.out.println("Exception: " + e);
                e.printStackTrace();
            } 
            break;


            //Edit
            case 2:
            break;

            
            //Delete
            case 3:
            break;
        }
        
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

    //ADD 2 Methods Here
    // View & Reply Enquires
    // View Project Details

    public void viewMenu(){

        System.out.println("==== MANAGER MENU ====");
        System.out.println("1: Manage BTO Properties");
    }

}
