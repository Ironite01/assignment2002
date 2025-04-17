package assignment2002.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import assignment2002.BTOProperty;
import assignment2002.ManagerController;
import assignment2002.utils.BTOFileService;


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


    }

    //Idk if boolean or just void error checking all done at ManagerController
    public boolean deleteBTOListing(ArrayList<BTOProperty> btoList, BTOProperty propertyToDel){ 
        return btoList.remove(propertyToDel) ? true : false;
    }
    


    public void toggleProjectVisiblity(BTOProperty btoChosen){
        btoChosen.setVisible(!btoChosen.isVisible());
    }

    
    public void getAllProjs(ArrayList<BTOProperty> btoList){
        //TODO: Kind of redundant now lol

    }

    //TODO: Make this pretty
    public List<BTOProperty> getMyProjects(ArrayList<BTOProperty> btoList){
        return btoList.stream()
        .filter(p-> p.getManagerIC().stream().anyMatch(m -> m.getNRIC().equals(this.getNRIC())))
        .toList();


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
        ManagerController mController = new ManagerController(this, btoList, userList);
        mController.showMenu(sc);

    }

        
}
