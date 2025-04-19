package assignment2002.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateCheck {
        public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    //TODO: Rework these functions maybe;
    // Account for current date? Perhaps make the year Regex modular checking current year onwards;


    public static boolean dateValidator(String inputDate){ 
        String dateRegex = "^(0[1-9]|1[0-2])/([0][1-9]|[12][0-9]|3[01])/(202[5-9]|20[3-9][0-9]|2[1-9][0-9]{2})$";

        if(!inputDate.matches(dateRegex)){ //Input does not match Date regex
            System.out.println("Date is invalid");
            return false;
        }

        try {
            LocalDate.parse(inputDate, formatter);
            System.out.println("This is a valid Date");
            return true;
            
        } catch (DateTimeParseException e) {
                System.out.println("Date is invalid 2");
                return false;
            }

    }

    public static boolean dateComparator(String openDate, String closeDate){
        LocalDate opDate = LocalDate.parse(openDate, formatter);
        LocalDate clDate = LocalDate.parse(closeDate, formatter);

        return clDate.isAfter(opDate);
    }

    public static boolean dateComparatorVisiblity(String closeDate){
        return dateComparator(getTodayDate(), closeDate);
    }

    public static String getTodayDate(){
        LocalDate today = LocalDate.now();
        return today.format(formatter);
    }
    
}
