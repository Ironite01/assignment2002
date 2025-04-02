package assignment2002;

import java.util.ArrayList;

public class MainApp {
    public static void main(String[] args) {
        ArrayList<User> userList = LoadInfo.loadUsers();  
        ArrayList<BTOProperties> btoList = LoadInfo.loadProperties(userList);

        System.out.println(btoList);

        //Showing that the load properties works
        btoList.get(0).allInfo();


        //ArrayList object memory
        System.out.println(userList);


        //If you want to see how the ArrayList works
        // for(User u: userList){
        //     u.allInfo();
        // }
        
    }
    




}
