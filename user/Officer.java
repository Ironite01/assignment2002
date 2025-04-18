package assignment2002.user;

import java.util.List;

import assignment2002.OfficerController;

public class Officer extends Applicant {
	private enum REGISTRATION_STATUS { PENDING, REJECTED, SUCCESSFUL };
	
	private REGISTRATION_STATUS registrationStatus;
	private String projectName; // Which the officer is handling
	
    public Officer(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password);
    }
    
    public REGISTRATION_STATUS viewRegistrationStatus() {
    	return registrationStatus;
    }

    public Object registerToHandleProject() {
    	if (applicationStatus == APPLICATION_STATUS.NOTAPPLIED) {
    		// TODO: Send a
    	}
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
    
    @Override
    public void viewMenu() {
        OfficerController aController = new OfficerController(this);
        aController.showMenu();
    }
}