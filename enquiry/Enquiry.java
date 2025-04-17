package assignment2002.enquiry;

import java.util.Date;
import java.util.HashMap;

import assignment2002.utils.Authenticator;

public class Enquiry {
	private int projectId;
	private String applicantNric;
	private HashMap<Date, Message> messages = new HashMap<Date, Message>();
	private Date endDate; // if null = unresolved enquiry
	
	public Enquiry(String applicantNric, int projectId, String msg) {
		if (Authenticator.isValidNRIC(applicantNric)) {
			this.applicantNric = applicantNric;
			this.projectId = projectId;
			this.addMessage(applicantNric, msg);
			return;
		}
		System.out.println("Unable to generate new enquiry as NRIC = " + applicantNric + " is not a valid NRIC!");
	}
	
	public String getApplicantNric() {
		return this.applicantNric;
	}
	
	public int getProjectId() {
		return this.projectId;
	}
	
	public HashMap<Date, Message> getAllMessages() {
		return messages;
	}
	
	public void addMessage(String nric, String msg) {
		if (endDate == null) {
			messages.put(new Date(), new Message(msg, nric));
			return;
		}
		System.out.println("User is not allowed to add a new message!");
	}
	
	// Officer only access
	protected void closeEnquiry() {
		this.endDate = new Date();
	}
	
	public boolean isResolved() {
		return endDate != null;
	}
}
