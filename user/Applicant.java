package assignment2002.user;

import assignment2002.ApplicantController;

public class Applicant extends User{

    private String appliedProjects;
    private String flatType;

    APPLICATION_STATUS applicationStatus;

    enum APPLICATION_STATUS {
        SUCCESSFUL,
        UNSUCCESSFUL,
        PENDING,
        BOOKED,
        NOTAPPLIED,
        PENDINGWITHDRAWN,
        WITHDRAWN
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
    public void viewMenu() {
        ApplicantController aController = new ApplicantController(this);
        aController.showMenu();
    }

}
