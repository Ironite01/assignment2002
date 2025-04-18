package assignment2002.application;

import assignment2002.user.Applicant;
import assignment2002.BTOProperty;

public class Application {

    public enum ApplicationStatus {
        PENDING,
        SUCCESSFUL,
        UNSUCCESSFUL,
        BOOKED
    }

    private Applicant applicant;
    private BTOProperty property;
    private String flatType;
    private ApplicationStatus status;

    public Application(Applicant applicant, BTOProperty property, String flatType) {
        this.applicant = applicant;
        this.property = property;
        this.flatType = flatType;
        this.status = ApplicationStatus.PENDING;
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

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public boolean isSuccessful() {
        return status == ApplicationStatus.SUCCESSFUL;
    }

    public boolean isBooked() {
        return status == ApplicationStatus.BOOKED;
    }
}
