package assignment2002.user;

import java.util.List;

public class Officer extends Applicant {
	enum REGISTRATION_STATUS {PENDING, REJECTED, SUCCESSFUL};
	
	// private Project handledProject;
	private REGISTRATION_STATUS registrationStatus;
	
    public Officer(String name, String NRIC, int age, String maritalStatus, String password){
        super(name, NRIC, age, maritalStatus, password);
    }
    
    public REGISTRATION_STATUS viewRegistrationStatus() {
    	return registrationStatus;
    }

    public Object registerToHandleProject() {
    	return null;
    	
    }
    
    public List<Object> viewEnquiry() {
    	return null;
    }
    
    public void replyToEnquiry(int enquiryId, String reply) {
    	
    }
    
    public void bookFlat(Applicant applicant, String flatType) {
    	
    }
    
    public Object generateReceipt(Applicant applicant) {
    	return null;
    }
}