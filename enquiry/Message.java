package assignment2002.enquiry;

import java.util.ArrayList;
import java.util.Date;

import assignment2002.user.User;
import assignment2002.utils.Authenticator;
import assignment2002.utils.LoadInfo;

public class Message {
	private String msg;
	private Date date;
	private String authorNric;
	
	public Message(String msg, String authorNric) {
		if (Authenticator.isValidNRIC(authorNric)) {
			this.msg = msg;
			this.date = new Date();
			this.authorNric = authorNric;
			return;
		}
		System.out.println("Unable to add message as NRIC = " + authorNric + " is not a valid NRIC!");
	}

	public String getMessage() {
		return this.msg;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public User getAuthor() {
		ArrayList<User> userList = LoadInfo.loadUsers();
		for (User u : userList) {
			if (u.getNRIC().equalsIgnoreCase(authorNric)) {
				return u;
			}
		}
		return null;
	}
}
