package assignment2002.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateCheck {

    //TODO: Rework these functions maybe;
    // Account for current date? Perhaps make the year Regex modular checking current year onwards;


    public static boolean dateValidator(String inputDate){ 
        String dateRegex = "^(0[1-9]|1[0-2])/([0][1-9]|[12][0-9]|3[01])/(202[5-9]|20[3-9][0-9]|2[1-9][0-9]{2})$";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        if(!inputDate.matches(dateRegex)){ //Input does not match Date regex
            System.out.println("Date is invalid");
            return false;
        }

        try {
            LocalDate.parse(inputDate, formatter);
            System.out.println("Date Accepted");
            return true;
            
        } catch (DateTimeParseException e) {
                System.out.println("Date is invalid 2");
                return false;
            }

    }

    public static boolean dateComparator(String openDate, String closeDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            
        LocalDate opDate = LocalDate.parse(openDate, formatter);
        LocalDate clDate = LocalDate.parse(closeDate, formatter);

        if(clDate.isAfter(opDate)){
            return true;
        } 

        return false;

    }
    
}
