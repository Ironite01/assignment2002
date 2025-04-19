package assignment2002.utils;

import assignment2002.BTOProperty;
import assignment2002.user.Applicant;
import assignment2002.user.Manager;
import assignment2002.user.Officer;
import assignment2002.user.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadInfo implements FileManifest {

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

                    if (file.equals(APPLICANT_TXT_PATH)) {
                        Applicant a = new Applicant(info[0], info[1], Integer.parseInt(info[2]), info[3], info[4]);
                    
                        // These are the 3 new columns i added, i put it as a seperate if statement cos not all user have info on these columns
                        if (info.length >= 8) {
                            a.setFlatType(info[5]);
                            a.setAppliedProject(info[6]);
                            a.setApplicationStatus(info[7]);
                        }
                    
                        users.add(a);
                        
                    } else if (file.equals(OFFICER_TXT_PATH)){
                        Officer o = new Officer(info[0],info[1],Integer.parseInt(info[2]),info[3],info[4]);

                        if (info.length >= 8) {
                            o.setFlatType(info[5]);
                            o.setAppliedProject(info[6]);
                            o.setApplicationStatus(info[7]);
                        }

                        users.add(o);

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
                
                String[] officerList = info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.APPROVED_OFFICERS.toString())].split(",");
                String[] pendingOfficerList = info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.PENDING_OFFICERS.toString())].split(",");
                String[] rejectedOfficerList = info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.REJECTED_OFFICERS.toString())].split(",");
                ArrayList<Officer> officerRef = new ArrayList<>();
                ArrayList<Officer> pendingOfficerRef = new ArrayList<>();
                ArrayList<Officer> rejectedOfficerRef = new ArrayList<>();
                ArrayList<Manager> managerRef = new ArrayList<>();
                
                for(User u: users){
                    if(info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.MANAGER.toString())].equals(u.getName()) && u instanceof Manager){
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
                for(String name: pendingOfficerList){
                    for(User userList: users){
                        if(userList.getName().equals(name) && userList instanceof Officer){
                        	pendingOfficerRef.add((Officer) userList);
                            break;
                        }
                    }
                }
                for(String name: rejectedOfficerList){
                    for(User userList: users){
                        if(userList.getName().equals(name) && userList instanceof Officer){
                        	rejectedOfficerRef.add((Officer) userList);
                            break;
                        }
                    }
                }
                

                btoList.add(new BTOProperty(info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.PROJECT_NAME.toString())],
                		info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.NEIGHBOURHOOD.toString())],
                		info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.TWO_ROOM.toString())],
                		Integer.parseInt(info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.TWO_ROOM_AMT.toString())]),
                		Integer.parseInt(info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.TWO_ROOM_PRICE.toString())]),
                		info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.THREE_ROOM.toString())],
                		Integer.parseInt(info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.THREE_ROOM_AMT.toString())]),
                		Integer.parseInt(info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.THREE_ROOM_PRICE.toString())]),
                		info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.OPEN_DATE.toString())],
                		info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.CLOSE_DATE.toString())],
                		managerRef,
                		Integer.parseInt(info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.OFFICER_SLOT.toString())]),
                		officerRef, pendingOfficerRef, rejectedOfficerRef,
                		info[PROPERTY_COLUMNS_MAP.get(PROPERTY_COLUMNS.VISIBLE.toString())]));
                
                
            }
            propertyReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error has occurred: " + e);
            e.printStackTrace();
        }

        return btoList;
    }


    public static void autoCloseExpiredProjects(){
        for(BTOProperty p : Data.btoList){
            if(p.isVisible() && !DateCheck.dateComparatorVisiblity(p.getCloseDate())){ 
                p.setVisible(false);
                BTOFileService.editBTOByColumn(p.getProjectName(), PROPERTY_COLUMNS.VISIBLE, "FALSE", true);
            }
        }

    }
    


}