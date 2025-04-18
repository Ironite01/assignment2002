package assignment2002.utils;

import java.util.ArrayList;

import assignment2002.BTOProperty;
import assignment2002.user.User;

public interface Data {
	public static final ArrayList<User> userList = LoadInfo.loadUsers();
	public static final ArrayList<BTOProperty> btoList = LoadInfo.loadProperties(userList);
}
