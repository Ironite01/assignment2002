package assignment2002.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import assignment2002.BTOProperty;
import assignment2002.OfficerController;
import assignment2002.OfficerService;
import assignment2002.utils.Status;

public class Officer extends Applicant {
	private Map<String, REGISTRATION> allProjectStatus;
	
    public Officer(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password);
    }
    
    public Map<String, REGISTRATION> getAllProjectStatus() {
    	if (allProjectStatus == null) {
    		this.allProjectStatus = OfficerService.getAllProjectStatus(this); // putting this on constructor causes an error
    	}
    	return allProjectStatus;
    }
    
    public ArrayList<BTOProperty> getRegisteredProperties() {
    	return OfficerService.getRegisteredProjects(this);
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