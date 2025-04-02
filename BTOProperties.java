import java.util.ArrayList;

public class BTOProperties {
    String projectName;
    String neighbourhood;
    String twoRoom; 
    int twoRoomAmt;
    int twoRoomPrice; //Float Double?
    String threeRoom; 
    int threeRoomAmt;
    int threeRoomPrice;
    String openDate;
    String closeDate; //Maybe import module for dates?
    ArrayList<Manager> managerIC;
    int officerSlot;
    ArrayList<Officer> officers;
    boolean visible = true; // i shall set the default to be true 
    

    public BTOProperties(String projName, String neighbourhood, String twoRoom,
    int twoRoomAmt, int twoRoomPrice, String threeRoom, int threeRoomAmt,
    int threeRoomPrice, String openDate, String closeDate, ArrayList<Manager> managerICRef, int officerSlot, ArrayList<Officer> officerList){
        
        this.projectName = projName;
        this.neighbourhood = neighbourhood;
        this.twoRoom = twoRoom;
        this.twoRoomAmt = twoRoomAmt;
        this.twoRoomPrice = twoRoomPrice;
        this.threeRoom = threeRoom;
        this.threeRoomAmt = threeRoomAmt;
        this.threeRoomPrice = threeRoomPrice;
        this.openDate = openDate;
        this.closeDate = closeDate;
        managerIC = managerICRef;
        this.officerSlot = officerSlot;
        officers = officerList; //Holds Object references to the officers

    }

    public ArrayList<Officer> getOfficers(){
        return officers;
    }

    public void allInfo(){ //Troubleshooting
        System.out.printf("Project Name: %s Neighbourhood: %s\n"
        + "twoRoom: %s twoRoomAmt: %d twoRoomPrice: %d\n"
        + "threeRoom: %s threeRoomAmt: %d threeRoomPrice: %d\n"
        + "openDate: %s closeDate: %s Officer Slot: %d\n"
        ,projectName,neighbourhood,twoRoom,twoRoomAmt,twoRoomPrice,threeRoom,threeRoomAmt,threeRoomPrice,openDate,closeDate,officerSlot);

        System.out.println("Managers Involved:");
        for(Manager m: managerIC){
            System.out.println(m.getName());
        }

        System.out.println("Officers Involved:");
        for(Officer o : officers){
            System.out.println(o.getName());
        }

    }

    public boolean isVisible() { 
        return visible;
    }
    
    public void setVisible(boolean visible) { // maybe manager can use to make certain project invisible
        this.visible = visible;
    }

}
