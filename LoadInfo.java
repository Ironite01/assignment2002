import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;



public class LoadInfo{

    public static ArrayList<User> loadUsers(){ //Static function no need init.
        ArrayList<User> users = new ArrayList<>();
        String applicantLoc = "Information/ApplicantList.txt";
        String officerLoc = "Information/OfficerList.txt";
        String managerLoc = "Information/ManagerList.txt";
        String[] fileLocations = {applicantLoc, officerLoc, managerLoc};
        

        for(String file : fileLocations){
            System.out.println(file); //Troubleshooting delete later
            try{ 
                File genericFile = new File(file);
                Scanner genericReader = new Scanner(genericFile);
                genericReader.nextLine(); //Skip header

                while(genericReader.hasNextLine()){
                    String line = genericReader.nextLine();
                    System.out.println(line); //Troubleshooting delete later
                    String[] info = line.split("\t");

                    if (file.equals(applicantLoc)){
                        users.add(new Applicant(info[0],info[1],Integer.parseInt(info[2]),info[3],info[4]));
                        
                    } else if (file.equals(officerLoc)){
                        users.add(new Officer(info[0],info[1],Integer.parseInt(info[2]),info[3],info[4]));

                    } else if (file.equals(managerLoc)){
                        users.add(new Manager(info[0],info[1],Integer.parseInt(info[2]),info[3],info[4]));
                    }
                }
    
                genericReader.close();
    
            } catch (FileNotFoundException e){
                System.out.println("File with error: " + file);
                System.out.println("Error has occurred: " + e);
                e.printStackTrace();
            }

        }
        
        return users;
    }

    //add another static function loadProperties() or something similar here.
    
}