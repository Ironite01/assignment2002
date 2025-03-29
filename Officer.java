public class Officer extends Applicant{

    public Officer(String name, String NRIC, int age, String maritalStatus, String password){
        super(name, NRIC, age, maritalStatus, password);
    }

    public boolean test(){
        return true;
    }
}