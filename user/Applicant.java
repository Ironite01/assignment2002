package assignment2002.user;

import java.util.List;

import assignment2002.ApplicantController;
import assignment2002.utils.Status;
import assignment2002.application.Application;
import assignment2002.application.ApplicationService;

public class Applicant extends User implements Status {

    private List<Application> applications;

    public Applicant(String name, String NRIC, int age, String maritalStatus, String password){
        super(name, NRIC, age, maritalStatus, password);
        this.applications = ApplicationService.getApplicationsByApplicant(this);
    }
    
    public Application getCurrentApplication() {
    	return applications.stream().filter((app) -> {
    		APPLICATION_STATUS status = app.getStatus();
    		switch (status) {
    			case APPLICATION_STATUS.SUCCESSFUL:
    			case APPLICATION_STATUS.PENDING:
    			case APPLICATION_STATUS.BOOKED:
    			case APPLICATION_STATUS.PENDINGWITHDRAWN:
    				return true;
    			case APPLICATION_STATUS.UNSUCCESSFUL:
    			case APPLICATION_STATUS.WITHDRAWN:
    			case APPLICATION_STATUS.NOTAPPLIED:
    			default:
    				return false;
    		}
    	}).findFirst().orElse(null);
    }
    
    // Current application status
    public String getApplicationStatus() {
    	return getCurrentApplication().getStatus().toString();
    }

    public String getApplicationStatus(String projectName) {
    	Application a = applications.stream().filter(app -> app.getProperty().getProjectName().equalsIgnoreCase(projectName)).findFirst().orElse(null);
    	return a == null ? "" : a.getStatus().toString();
    }

    public String getAppliedProject() {
    	return getCurrentApplication().getProperty().getProjectName();
    }

    public String getFlatType() {
        return getCurrentApplication().getFlatType();
    }
    

    @Override
    public void viewMenu() {
        ApplicantController aController = new ApplicantController(this);
        aController.showMenu();
    }

}
