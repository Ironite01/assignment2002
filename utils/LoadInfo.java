package assignment2002.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import assignment2002.BTOProperty;
import assignment2002.user.Applicant;
import assignment2002.user.Manager;
import assignment2002.user.Officer;
import assignment2002.user.User;

import java.util.ArrayList;

public class LoadInfo implements FilePath {

    public static ArrayList<User> loadUsers(){ //Static function no need init.
        ArrayList<User> users = new ArrayList<>();
        String[] fileLocations = {APPLICANT_TXT_PATH, OFFICER_TXT_PATH, MANAGER_TXT_PATH};
        

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

                    if (file.equals(APPLICANT_TXT_PATH)){
                        users.add(new Applicant(info[0],info[1],Integer.parseInt(info[2]),info[3],info[4]));
                        
                    } else if (file.equals(OFFICER_TXT_PATH)){
                        users.add(new Officer(info[0],info[1],Integer.parseInt(info[2]),info[3],info[4]));

                    } else if (file.equals(MANAGER_TXT_PATH)){
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

        try {
            File propertyFile = new File(PROJECT_TXT_PATH);
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