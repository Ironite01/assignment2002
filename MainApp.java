import java.util.ArrayList;

public class MainApp {
    public static void main(String[] args) {
        ArrayList<User> userList = LoadInfo.loadUsers();  


        System.out.println(userList);

        for(User u: userList){
            u.allInfo();
        }
        
    }
    




}
