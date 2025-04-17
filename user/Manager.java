package assignment2002.user;

import java.util.ArrayList;
import java.util.List;

import assignment2002.BTOProperty;
import assignment2002.ManagerController;
import assignment2002.utils.BTOFileService;
import assignment2002.utils.ProjectPrinter;


public class Manager extends User{
    
    public Manager(String name, String NRIC, int age, String maritalStatus, String password){
        super(name, NRIC, age, maritalStatus, password);
    }
    

    private BTOProperty fromTabString(String data, ArrayList<Manager> managerList, ArrayList<Officer> officerList){
        String[] fragments = data.split("\t");

        String projName = fragments[0];
        String neighbourhood = fragments[1];
        String twoRoom = fragments[2];
        int twoRoomAmt = Integer.parseInt(fragments[3]);
        int twoRoomPrice = Integer.parseInt(fragments[4]);
        String threeRoom = fragments[5];
        int threeRoomAmt = Integer.parseInt(fragments[6]);
        int threeRoomPrice = Integer.parseInt(fragments[7]);
        String openDate = fragments[8];
        String closeDate = fragments[9];
        int officerSlot= Integer.parseInt(fragments[11]);


        return new BTOProperty(projName, neighbourhood, twoRoom, twoRoomAmt, twoRoomPrice, threeRoom,
         threeRoomAmt, threeRoomPrice, openDate, closeDate, managerList, officerSlot, officerList);


    }
    

    public void createBTOListing(ArrayList<BTOProperty> btoList, ArrayList<Manager> managerICRef, ArrayList<Officer> officerRef, String dataString){
        BTOProperty p = fromTabString(dataString, managerICRef, officerRef);
        btoList.add(p);
    }

    private void editBTOListing(){
        //Redundant maybe
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

    public void updateBTOByColumn(ArrayList<BTOProperty> btoList, String projName, String fieldName, String newValue) {
        for (BTOProperty p : btoList) {
            if (p.getProjectName().equalsIgnoreCase(projName)) {
                switch (fieldName) {
                    case "neighbourhood" -> p.setNeighbourhood(newValue);
                    case "twoRoomAmt" -> p.setTwoRoomAmt(Integer.parseInt(newValue));
                    case "twoRoomPrice" -> p.setTwoRoomPrice(Integer.parseInt(newValue));
                    case "threeRoomAmt" -> p.setThreeRoomAmt(Integer.parseInt(newValue));
                    case "threeRoomPrice" -> p.setThreeRoomPrice(Integer.parseInt(newValue));
                    case "openDate" -> p.setOpenDate(newValue);
                    case "closeDate" -> p.setCloseDate(newValue);
                    case "officerSlot" -> p.setOfficerSlot(Integer.parseInt(newValue));
                }
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

    public void viewMenu(ArrayList<User> userList, ArrayList<BTOProperty> btoList){
        ManagerController mController = new ManagerController(this, btoList, userList);
        mController.showMenu();

    }

        
}
