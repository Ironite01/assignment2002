package assignment2002;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticatedUser {
	private final User u;
	
	public AuthenticatedUser(String NRIC, String password) {
		// Forward any errors to main thread - we do not perform any exception handling here.
		User temp = getUserByNRIC(NRIC);
		if (isAuthenticated(temp, password)) {
			this.u = temp;
			return;
		}
		throw new IllegalArgumentException("Unable to authenticate user");
	}
	
	private boolean isValidNRIC(String NRIC) {
		// Regex expression to validate NRIC.
		Pattern pattern = Pattern.compile("^[ST]\\d{7}[A-Z]$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(NRIC);
		return matcher.find();
	}
	
	private User getUserByNRIC(String NRIC) {
		if (!isValidNRIC(NRIC)) {
			throw new IllegalArgumentException("NRIC is not valid!");
		}
		ArrayList<User> userList = LoadInfo.loadUsers();
		for (User u : userList) {
			if (u.getNRIC().equals(NRIC)) {
				return u;
			}
		}
		throw new NullPointerException("No user found with NRIC = " + NRIC);
	}
	
	// Rationale of seperating the authorisation is as follows:
	// In most cases, we would want to handle hashing - which would be good to have a seperate function.
	private boolean isAuthenticated(User user, String password) {
		return user.getPassword().equals(password);
	}

	// Getters
    public String getName() {
        return u.getName();
    }

    public String getNRIC() {
        return u.getNRIC();
    }

    public int getAge() {
        return u.getAge();
    }

    public String getMaritalStatus() {
        return u.getMaritalStatus();
    }
    
    public void allInfo(){//Troubleshooting;
        System.out.printf("Name: %s \nNRIC: %s\nAge: %d\nMarital Status: %s\n\n",getName(), getNRIC(), getAge(), getMaritalStatus());
    }
}
