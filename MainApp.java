package assignment2002;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import assignment2002.utils.Authenticator;
import assignment2002.utils.LoadInfo;

public class MainApp {
    public static void main(String[] args) {
    	Scanner s = new Scanner(System.in);
    	String nric;
    	String password;
    	User user = null;
    	
    	// TODO: Menu based application
    	ArrayList<User> userList = LoadInfo.loadUsers();
        ArrayList<BTOProperty> btoList = LoadInfo.loadProperties(userList);

        System.out.println(btoList);

        //Showing that the load properties works
        btoList.get(0).allInfo();

        //If you want to see how the ArrayList works
        // for(User u: userList){
        //     u.allInfo();
        // }
        
        // FOR TESTING PURPOSES ONLY
        try {
        	System.out.println("System login...\nEnter your NRIC: ");
        	nric = s.nextLine();
            System.out.println("Enter your password: ");
            password = s.nextLine();
            user = Authenticator.getAuthenticatedUser(nric, password);
            user.allInfo();
            
            System.out.println("Enter message for enquiry for project id = 1:");
            String msg = s.nextLine();
            
            EnquiryService.addNewEnquiry(user.getNRIC(), 1, msg);
            Enquiry e = EnquiryService.getEnquiry(user.getNRIC(), 1);
            e.getAllMessages().forEach((Date d, Message m) -> {
            	System.out.println(d.toGMTString() + " : " + m.getMessage());
            });
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	System.exit(0);
        }
        user.viewMenu(userList, btoList, s);
        
        s.close();
    }
    




}
