package assignment2002.utils;

import java.util.Scanner;

public class InputUtil {
    public static int getValidatedIntRange(Scanner sc, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = Integer.parseInt(sc.nextLine().trim());

                if (input < min || input > max) {
                    System.out.println("Please enter a valid choice");
                    continue;
                }

                return input;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please Try Again!");
            }
        }
    }

    public static String getNonEmptyString(Scanner sc, String prompt){
        while (true) {
            System.out.print(prompt);
            String inputString = sc.nextLine().trim();
            if(!inputString.isEmpty()){ //Not empty
                return inputString;
            }            

            System.out.println("String cannot be empty!");
        }
    }

    public static String getConfirmation(Scanner sc, String prompt) {
        while (true) {
            System.out.printf(prompt + " (Y/N): ");
            String input = sc.nextLine().trim().toLowerCase();
    
            switch (input) {
                case "y" -> {return "TRUE";}
                case "n" -> {return "FALSE";}
                default -> System.out.println("Invalid input. Try Again!");
            }
        }
    }
    

}
