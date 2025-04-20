package assignment2002.user;

import assignment2002.BTOProperty;
import assignment2002.ManagerController;
import assignment2002.utils.DateCheck;
import assignment2002.utils.FileManifest;
import assignment2002.utils.FileManifest.PROPERTY_COLUMNS;
import assignment2002.utils.ProjectPrinter;
import java.util.ArrayList;
import java.util.List;


public class Manager extends User implements FileManifest {
    
    public Manager(String name, String NRIC, int age, String maritalStatus, String password){
        super(name, NRIC, age, maritalStatus, password);
    }
    

    private BTOProperty fromTabString(String data, ArrayList<Manager> managerList, ArrayList<Officer> officerList, ArrayList<Officer> pendingOfficersList, ArrayList<Officer> rejectedOfficersList){
        String[] fragments = data.split("\t");

        String projName = fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.PROJECT_NAME.toString())];
        String neighbourhood = fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.NEIGHBOURHOOD.toString())];
        String twoRoom = fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.TWO_ROOM.toString())];
        int twoRoomAmt = Integer.parseInt(fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.TWO_ROOM_AMT.toString())]);
        int twoRoomPrice = Integer.parseInt(fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.TWO_ROOM_PRICE.toString())]);
        String threeRoom = fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.THREE_ROOM.toString())];
        int threeRoomAmt = Integer.parseInt(fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.THREE_ROOM_AMT.toString())]);
        int threeRoomPrice = Integer.parseInt(fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.THREE_ROOM_PRICE.toString())]);
        String openDate = fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.OPEN_DATE.toString())];
        String closeDate = fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.CLOSE_DATE.toString())];
        int officerSlot= Integer.parseInt(fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.OFFICER_SLOT.toString())]);
        String visible = fragments[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.VISIBLE.toString())];


        return new BTOProperty(projName, neighbourhood, twoRoom, twoRoomAmt, twoRoomPrice, threeRoom,
         threeRoomAmt, threeRoomPrice, openDate, closeDate, managerList, officerSlot, officerList, pendingOfficersList,rejectedOfficersList,visible);


    }
    
    public void createBTOListing(ArrayList<BTOProperty> btoList, ArrayList<Manager> managerICRef, ArrayList<Officer> officerRef, ArrayList<Officer> pendingOfficerRef, ArrayList<Officer> rejectedOfficerRef, String dataString){
        BTOProperty p = fromTabString(dataString, managerICRef, officerRef, pendingOfficerRef, rejectedOfficerRef);
        btoList.add(p);
    }


    //Idk if boolean or just void error checking all done at ManagerController
    public boolean deleteBTOListing(ArrayList<BTOProperty> btoList, BTOProperty propertyToDel){ 
        return btoList.remove(propertyToDel) ? true : false;
    }
    


    public void toggleProjectVisiblity(BTOProperty btoChosen){
        btoChosen.setVisible(!btoChosen.isVisible());
    }

    
    public void viewAllProjects(ArrayList<BTOProperty> btoList){
        ProjectPrinter.viewProjects(btoList);
    }

    public void viewMyProjs (ArrayList<BTOProperty> btoList){
        List<BTOProperty> myP = getMyProjects(btoList);
        ProjectPrinter.viewProjects(myP);
    }

    public void viewProjectsVisibility(ArrayList<BTOProperty> btoList){
        ProjectPrinter.viewProjectsVisibility(btoList);
    }

    public List<BTOProperty> getMyProjects(ArrayList<BTOProperty> btoList){
        return btoList.stream()
        .filter(p-> p.getManagerIC().stream().anyMatch(m -> m.getNRIC().equals(this.getNRIC())))
        .toList();
    }

    public boolean projNameExists(ArrayList<BTOProperty> btoList, String projName){
        return btoList.stream().anyMatch(p -> p.getProjectName().equalsIgnoreCase(projName));
    }
        
    public void updateProjectName(ArrayList<BTOProperty> btoList, String oldName, String newName){
        for(BTOProperty p : btoList){
            if(p.getProjectName().equalsIgnoreCase(oldName)){
                p.setProjectName(newName);
                break;
            }
        }
    }

    public void updateBTOByColumn(ArrayList<BTOProperty> btoList, String projName, PROPERTY_COLUMNS fieldName, String newValue) {
        for (BTOProperty p : btoList) {
            if (p.getProjectName().equalsIgnoreCase(projName)) {
                switch (fieldName) {
                    case PROPERTY_COLUMNS.NEIGHBOURHOOD -> p.setNeighbourhood(newValue);
                    case PROPERTY_COLUMNS.TWO_ROOM_AMT -> p.setTwoRoomAmt(Integer.parseInt(newValue));
                    case PROPERTY_COLUMNS.TWO_ROOM_PRICE -> p.setTwoRoomPrice(Integer.parseInt(newValue));
                    case PROPERTY_COLUMNS.THREE_ROOM_AMT -> p.setThreeRoomAmt(Integer.parseInt(newValue));
                    case PROPERTY_COLUMNS.THREE_ROOM_PRICE -> p.setThreeRoomPrice(Integer.parseInt(newValue));
                    case PROPERTY_COLUMNS.OPEN_DATE -> p.setOpenDate(newValue);
                    case PROPERTY_COLUMNS.CLOSE_DATE -> p.setCloseDate(newValue);
                    case PROPERTY_COLUMNS.OFFICER_SLOT -> p.setOfficerSlot(Integer.parseInt(newValue));
                    default -> System.out.println("Unsupported Column");
                }
                break;
            }
        }
    }
    
    public boolean isCurrentlyManaging(ArrayList <BTOProperty> btoList){
        List <BTOProperty> myP = getMyProjects(btoList);
        for (BTOProperty p : myP){
            if(DateCheck.isTodayWithin(p.getOpenDate(), p.getCloseDate())){
                return true;
            }
        }
        return false;
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

    // ArrayList<User> userList, ArrayList<BTOProperty> btoList
    public void viewMenu(){
        ManagerController mController = new ManagerController(this);
        mController.showMenu();

    }


        
}
