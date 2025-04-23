package assignment2002;


import assignment2002.application.ApplicationService;
import assignment2002.user.User;
import assignment2002.user.UserService;
import assignment2002.utils.Data;
import assignment2002.utils.LoadInfo;

public class MainApp {
    public static void main(String[] args) {
    	User user = null;
    	
        ApplicationService.loadApplications(Data.userList, Data.btoList);
        LoadInfo.autoCloseExpiredProjects();

        while (true) {
            try{
                user = UserService.loginPrompt();
                if (user == null) {
                    System.out.println("Unable to authenticate user!");
                    continue;
                }
            } catch(IllegalArgumentException e){
                System.out.println("Error: " + e.getMessage());
                continue;
            } catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
            user.viewMenu();
            break;
        }
    }
}
