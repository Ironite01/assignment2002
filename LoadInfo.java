package assignment2002;

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
            // System.out.println(file); //Troubleshooting delete later
            try{ 
                File genericFile = new File(file);
                Scanner genericReader = new Scanner(genericFile);
                genericReader.nextLine(); //Skip header

                while(genericReader.hasNextLine()){
                    String line = genericReader.nextLine();
                    // System.out.println(line); //Troubleshooting delete later
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
    public static ArrayList<BTOProperty> loadProperties(ArrayList<User> users){
        ArrayList<BTOProperty> btoList = new ArrayList<>();
        String propertyLoc = "Information/ProjectList.txt";

        try {
            File propertyFile = new File(propertyLoc);
            Scanner propertyReader = new Scanner(propertyFile);
            propertyReader.nextLine(); //Skip header

            while (propertyReader.hasNextLine()) {
                String line = propertyReader.nextLine();
                String[] info = line.split("\t");
                
                String[] officerList = info[12].split(",");
                ArrayList<Officer> officerRef = new ArrayList<>();
                ArrayList<Manager> managerRef = new ArrayList<>();
                
                for(User u: users){
                    if(info[10].equals(u.getName()) && u instanceof Manager){
                        managerRef.add((Manager)u);
                    }
                }

                
                for(String name: officerList){
                    for(User userList: users){
                        if(userList.getName().equals(name) && userList instanceof Officer){
                            officerRef.add((Officer) userList);
                            break;
                        }
                    }
                }
                

                btoList.add(new BTOProperty(info[0], info[1], info[2], Integer.parseInt(info[3]), Integer.parseInt(info[4]),
                info[5],Integer.parseInt(info[6]),Integer.parseInt(info[7]), info[8], info[9], managerRef,
                 Integer.parseInt(info[11]), officerRef));
                
                
            }
            propertyReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error has occurred: " + e);
            e.printStackTrace();
        }

        return btoList;
    }
    
}