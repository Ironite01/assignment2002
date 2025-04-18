package assignment2002;

import java.util.Scanner;

import assignment2002.user.Officer;

public class OfficerController {
	private Officer officer;
	
	public OfficerController(Officer officer) {
		this.officer = officer;
    }
	
	public void showMenu() {
        Scanner sc = new Scanner(System.in);
        boolean run = true;

        do {
            System.out.println("==== ROLE MENU ===="
            		+ "\n1: Applicant menu"
            		+ "\n2: Officer menu"
            		+ "\n3: Logout");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                	ApplicantController aController = new ApplicantController(officer);
                    aController.showMenu();
                }
                case 2 -> exclusiveOfficerMenu();
                case 3 -> run = false;
                default -> System.out.println("Invalid input. Try again.");
            }

        } while (run);

        sc.close();
        System.out.println("You have logged out.");
    }
	
	private void exclusiveOfficerMenu() {
		Scanner sc = new Scanner(System.in);
        boolean run = true;

        do {
            System.out.println("==== OFFICER MENU ===="
            		+ "\n1: Register to join a project team"
            		+ "\n2: View status of project as officer"
            		+ "\n3: View ALL projects"
            		+ "\n4. View enquiries"
            		+ "\n5. Generate receipts for booked flats"
            		+ "\n6. Logout");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 6 -> run = false;
                default -> System.out.println("Invalid input. Try again.");
            }

        } while (run);

        sc.close();
        System.out.println("You have logged out.");
	}
}
