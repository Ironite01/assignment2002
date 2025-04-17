package assignment2002.user;

import assignment2002.ApplicantController;
import assignment2002.BTOProperty;
import java.util.ArrayList;

public class Applicant extends User{

    private String appliedProjects;
    private String flatType;

    private APPLICATION_STATUS applicationStatus;

    private enum APPLICATION_STATUS {
        SUCCESSFUL,
        UNSUCCESSFUL,
        PENDING,
        BOOKED,
        NOTAPPLIED
    }

    public Applicant(String name, String NRIC, int age, String maritalStatus, String password){
        super(name, NRIC, age, maritalStatus, password);
        this.appliedProjects = " ";
        this.flatType = " ";
        this.applicationStatus = APPLICATION_STATUS.NOTAPPLIED;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public void setAppliedProject(String appliedProjects) {
        this.appliedProjects = appliedProjects;
    }

    public void setApplicationStatus(String status) {
        this.applicationStatus = APPLICATION_STATUS.valueOf(status.toUpperCase());
    }

    public String getApplicationStatus() {
        return applicationStatus.toString();
    }

    public String getAppliedProject() {
        return appliedProjects;
    }

    public String getFlatType() {
        return flatType;
    }
    

    @Override
    public void viewMenu(ArrayList<User> userList, ArrayList<BTOProperty> btoList) {
        ApplicantController aController = new ApplicantController(this, btoList, userList);
        aController.showMenu();
    }

}
