package assignment2002.user;

import java.util.ArrayList;
import java.util.Scanner;

import assignment2002.BTOProperty;

public interface UserInterface {
    public abstract void viewMenu(ArrayList<User> userList, ArrayList<BTOProperty> btoList, Scanner sc);
    public abstract void allInfo();
    public abstract String getName();
    public abstract String getNRIC();
    public abstract int getAge();
    public abstract String getMaritalStatus();
}
