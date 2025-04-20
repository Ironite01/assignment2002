package assignment2002.application;

import assignment2002.BTOProperty;
import assignment2002.user.Applicant;
import assignment2002.utils.Status;

public class Application implements Status {

    private Applicant applicant;
    private BTOProperty property;
    private String flatType;
    private APPLICATION_STATUS status;

    public Application(Applicant applicant, BTOProperty property, String flatType) {
        this.applicant = applicant;
        this.property = property;
        this.flatType = flatType;
        this.status = APPLICATION_STATUS.PENDING;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public BTOProperty getProperty() {
        return property;
    }

    public String getFlatType() {
        return flatType;
    }
    
    public void setFlatType(String flatType) {
    	this.flatType = flatType;
    }

    public APPLICATION_STATUS getStatus() {
        return status;
    }

    public void setStatus(APPLICATION_STATUS status) {
        this.status = status;
    }

    public boolean isSuccessful() {
        return status == APPLICATION_STATUS.SUCCESSFUL;
    }

    public boolean isBooked() {
        return status == APPLICATION_STATUS.BOOKED;
    }
}
