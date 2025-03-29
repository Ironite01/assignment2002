public class Applicant extends User{

    public Applicant(String name, String NRIC, int age, String maritalStatus, String password){
        super(name, NRIC, age, maritalStatus, password);
    }


    public boolean iscompleted(){
        return true;
    }
}
