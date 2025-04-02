
public class Applicant extends User{

    private boolean hasApplied;//I adding this cos apparently each user can only apply a hdb once
    private String appliedProjects;
    private String applicationStatus;// idk what name(e.g. Successful, unsuccessful, pending or whatever)
    private String flatType;

    public Applicant(String name, String NRIC, int age, String maritalStatus, String password){
        super(name, NRIC, age, maritalStatus, password);
        this.hasApplied = false;
        this.appliedProjects = " ";
        this.applicationStatus = " ";
        this.flatType = " ";
    }

    public boolean isEligible(BTOProperties project, String flatType) { //needed flattype to check if the applicant can apply 2 room or 3 room or both

        if (!project.isVisible()) {
            return false;
        }

        if (this.getMaritalStatus().equalsIgnoreCase("Single")) {
            if (this.getAge() >= 35 && flatType.equals("2-Room") && project.twoRoomAmt > 0) {
                return true;
            }
        } else if (this.getMaritalStatus().equalsIgnoreCase("Married")) {
            if (this.getAge() >= 21) {
                if ((flatType.equals("2-Room") && project.twoRoomAmt > 0) ||
                    (flatType.equals("3-Room") && project.threeRoomAmt > 0)) {
                    return true;
                }
            }
        }
    
        return false;
    }

    public boolean apply(BTOProperties project, String flatType){
        if (hasApplied) {
            System.out.println("Cannot apply more than 1 project bozo.");
            return false;
        }

        if (!isEligible(project, flatType)){
            System.out.println("Ur not eligable :(");
            return false;
        }

        this.hasApplied = true;
        this.appliedProjects = project.projectName;
        this.flatType = flatType;
        this.applicationStatus = "Pending";

        return true;
    }

    public void withdrawApplication(){
        if(hasApplied){
            this.applicationStatus = "Withdrawn";
            System.out.println("Application withdrawn successfully.");
        } else {
            System.out.println("Nothing to withdraw bro...");
        }
    }

    public void updateApplicationStatus(String newStatus) {
        this.applicationStatus = newStatus;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public String getAppliedProject() {
        return appliedProjects;
    }

    public String getFlatType() {
        return flatType;
    }

    public void viewMenu() {
        System.out.println("==== APPLICANT MENU ====");
        System.out.println("1: View Available Projects");
        System.out.println("2: Apply for a Project");
        System.out.println("3: View Application Status");
        System.out.println("4: Withdraw Application");
        System.out.println("5: Submit Enquiry");
        System.out.println("6: View/Edit/Delete Enquiries");
    }

}
