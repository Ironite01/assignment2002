package assignment2002;

import java.util.ArrayList;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
    	Scanner s = new Scanner(System.in);
    	String nric;
    	String password;
    	User user;
    	
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
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	System.exit(0);
        }
    }
    




}
