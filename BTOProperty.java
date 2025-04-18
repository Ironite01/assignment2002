package assignment2002;

import java.util.ArrayList;

import assignment2002.user.Applicant;
import assignment2002.user.Manager;
import assignment2002.user.Officer;

public class BTOProperty {
    protected String projectName; // Unique identifier
    protected String neighbourhood;
    protected String twoRoom; 
    protected int twoRoomAmt;
    protected int twoRoomPrice; //Float Double?
    protected String threeRoom; 
    protected int threeRoomAmt;
    protected int threeRoomPrice;
    protected String openDate;
    protected String closeDate; //Maybe import module for dates?
    protected ArrayList<Manager> managerIC;
    protected int officerSlot;
    private ArrayList<Officer> appliedOfficers;
    protected ArrayList<Officer> officers; // Approved officers
    protected boolean visible = true; // i shall set the default to be true 
    private ArrayList<Applicant> twoRoomApplicants = new ArrayList<>();
    private ArrayList<Applicant> threeRoomApplicants = new ArrayList<>();
    

    public BTOProperty(String projName, String neighbourhood, String twoRoom,
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


    public void allInfo(){ //Troubleshooting
        System.out.printf("Project Name: %s Neighbourhood: %s\n"
        + "twoRoom: %s twoRoomAmt: %d twoRoomPrice: %d\n"
        + "threeRoom: %s threeRoomAmt: %d threeRoomPrice: %d\n"
        + "openDate: %s closeDate: %s Officer Slot: %d\n"
        ,getProjectName(),getNeighbourhood(),getTwoRoom(),getTwoRoomAmt(),getTwoRoomPrice(),getThreeRoom(),getThreeRoomAmt(),getThreeRoomPrice(),getOpenDate(),getCloseDate(),getOfficerSlot());

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


    public String getProjectName() {
        return projectName;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public String getTwoRoom() {
        return twoRoom;
    }

    public int getTwoRoomAmt() {
        return twoRoomAmt;
    }

    public int getTwoRoomPrice() {
        return twoRoomPrice;
    }

    public String getThreeRoom() {
        return threeRoom;
    }

    public int getThreeRoomAmt() {
        return threeRoomAmt;
    }

    public int getThreeRoomPrice() {
        return threeRoomPrice;
    }

    public String getOpenDate() {
        return openDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public ArrayList<Manager> getManagerIC() {
        return managerIC;
    }
    public int getOfficerSlot() {
        return officerSlot;
    }

    public ArrayList<Officer> getOfficers() {
        return officers;
    }

    public ArrayList<Applicant> getTwoRoomApplicants() {
        return twoRoomApplicants;
    }
    
    public ArrayList<Applicant> getThreeRoomApplicants() {
        return threeRoomApplicants;
    }

    public void addApplicant(Applicant applicant, String flatType) {
        if (flatType.equalsIgnoreCase("2-Room")) {
            twoRoomApplicants.add(applicant);
        } else if (flatType.equalsIgnoreCase("3-Room")) {
            threeRoomApplicants.add(applicant);
        }
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public void setTwoRoomAmt(int twoRoomAmt) {
        this.twoRoomAmt = twoRoomAmt;
    }

    public void setTwoRoomPrice(int twoRoomPrice) {
        this.twoRoomPrice = twoRoomPrice;
    }

    public void setThreeRoomAmt(int threeRoomAmt) {
        this.threeRoomAmt = threeRoomAmt;
    }

    public void setThreeRoomPrice(int threeRoomPrice) {
        this.threeRoomPrice = threeRoomPrice;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public void setOfficerSlot(int officerSlot) {
        this.officerSlot = officerSlot;
    }
    

}
