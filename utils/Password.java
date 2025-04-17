package assignment2002.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Password {
	public static boolean isStrong(String password) {
		Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(password);
		return matcher.find();
	}
}