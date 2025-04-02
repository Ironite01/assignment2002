package assignment2002;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authenticator {
	public static User getAuthenticatedUser(String NRIC, String password) {
		// Forward any errors to main thread - we do not perform any exception handling here.
		User temp = getUserByNRIC(NRIC);
		if (isAuthenticated(temp, password)) {
			return temp;
		}
		throw new IllegalArgumentException("Unable to authenticate user");
	}
	
	private static boolean isValidNRIC(String NRIC) {
		// Regex expression to validate NRIC.
		Pattern pattern = Pattern.compile("^[ST]\\d{7}[A-Z]$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(NRIC);
		return matcher.find();
	}
	
	private static User getUserByNRIC(String NRIC) {
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
	private static boolean isAuthenticated(User user, String password) {
		return user.getPassword().equals(password);
	}
}
