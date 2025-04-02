package assignment2002;

public class User {
    private String name;
    private String NRIC;
    private int age;
    private String maritalStatus; //Could be boolean
    private String password;

    public User(String name, String NRIC, int age, String maritalStatus, String password){
        this.name = name;
        this.NRIC = NRIC;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getNRIC() {
        return NRIC;
    }

    public int getAge() {
        return age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getPassword() {
        return password;
    }
}
