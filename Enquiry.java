package assignment2002;

import java.util.Date;
import java.util.HashMap;

public class Enquiry {
	private int projectId;
	private String applicantNric;
	private HashMap<Date, Message> messages = new HashMap<Date, Message>();
	private Date endDate; // if null = unresolved enquiry
	
	public Enquiry(String applicantNric, int projectId, String msg) {
		this.applicantNric = applicantNric;
		this.projectId = projectId;
		this.addMessage(applicantNric, msg);
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
		throw new Error("User is not allowed to add a new message!");
	}
	
	// Officer only access
	protected void closeEnquiry() {
		this.endDate = new Date();
	}
	
	public boolean isResolved() {
		return endDate != null;
	}
}
