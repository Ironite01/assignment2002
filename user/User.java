package assignment2002.user;



public class User implements UserInterface {
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
    
    public void allInfo(){//Troubleshooting;
        System.out.printf("Name: %s \nNRIC: %s\nAge: %d\nMarital Status: %s\nPassword: %s\n\n",getName(), getNRIC(), getAge(), getMaritalStatus(), getPassword());
    }

    public void viewMenu(){
        System.out.println();
        return;
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

    public String getPassword() { // TODO: Remove
        return password;
    }
}
