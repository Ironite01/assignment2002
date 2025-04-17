package assignment2002.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import assignment2002.utils.Authenticator;
import assignment2002.utils.FilePath;
import assignment2002.utils.Password;

public class UserService implements FilePath {
	private static final String passwordField = "Password";
	private static final String nricField = "NRIC";
	
	public static User loginPrompt() {
		Scanner s = new Scanner(System.in);
	    String nric;
	    String password;
	    	
        System.out.println("System login...\nEnter your NRIC: ");
        nric = s.nextLine();
        System.out.println("Enter your password: ");
        password = s.nextLine();
        return Authenticator.getAuthenticatedUser(nric, password);
	}
	
	public static void resetPasswordPrompt(User u) {
		Scanner s = new Scanner(System.in);
		String newPassword;
		String path;
		int passwordHeaderIndex = -1;
		int nricHeaderIndex = -1;
		List<String> lines = new ArrayList<>();
		
		System.out.println("--- Password reset ---");
		while (true) {
			System.out.println("\nPlease enter your new password: ");
	        newPassword = s.nextLine();
	        if (Password.isStrong(newPassword)) break;
	        System.out.println("Your password is not strong enough!");
		}
        System.out.println("Please enter again to confirm...\nNew password: ");
        if (!s.nextLine().equals(newPassword)) {
        	System.out.println("Your 2nd attempt mismatched the 1st...\nExiting reset password...");
        	return;
        }
		
		if (u instanceof Manager) {
			path = MANAGER_TXT_PATH;
		} else if (u instanceof Officer) {
			path = OFFICER_TXT_PATH;
		} else if (u instanceof Applicant) {
			path = APPLICANT_TXT_PATH;
		} else {
			throw new Error("Invalid user instance!");
		}
		
		try{ 
            File genericFile = new File(path);
            Scanner genericReader = new Scanner(genericFile);
            String[] headers = genericReader.nextLine().split("\t");
            lines.add(String.join("\t", headers));
            
            for (int i = 0; i < headers.length; i++) {
            	if (headers[i].equals(passwordField)) {
            		passwordHeaderIndex = i;
            	}
            	if (headers[i].equals(nricField)) {
            		nricHeaderIndex = i;
            	}
            	if (nricHeaderIndex != -1 && passwordHeaderIndex != -1) {
            		break;
            	}
            }

            while(genericReader.hasNextLine()){
                String line = genericReader.nextLine();
                // System.out.println(line); //Troubleshooting delete later
                String[] info = line.split("\t");
                
                if (info[nricHeaderIndex].equalsIgnoreCase(u.getNRIC())) {
                	info[passwordHeaderIndex] = newPassword;
                }
                lines.add(String.join("\t", info));
            }

            genericReader.close();
            
            PrintWriter writer = new PrintWriter(new FileWriter(path));
            for (String line : lines) {
                writer.println(line);
            }
            writer.close();
            
            System.out.println("Password has been updated!");
        } catch (FileNotFoundException e){
            System.out.println("Error has occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}
}
