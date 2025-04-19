package assignment2002;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import assignment2002.user.Officer;
import assignment2002.utils.ProjectPrinter;
import assignment2002.utils.Status.REGISTRATION;

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

        System.out.println("You have logged out.");
    }
	
	private void exclusiveOfficerMenu() {
		Scanner sc = new Scanner(System.in);
        boolean run = true;

        do {
            System.out.println("==== OFFICER MENU ===="
            		+ "\n1: Register to join a project team"
            		+ "\n2: View status of projects as officer"
            		+ "\n3: View registered projects"
            		+ "\n4. View enquiries"
            		+ "\n5. Generate receipts for booked flats"
            		+ "\n6. Back");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
            	case 1 -> {
            		List<BTOProperty> proj = OfficerService.getAvailableProjectsToRegister(officer);
            		if (proj.size() == 0) {
            			System.out.println("Sorry, there are no projects that you can register!");
            		} else {
            			System.out.println("There are " + proj.size() + " you can choose to register!\n");
            			for (BTOProperty p : proj) {
            				System.out.println(p.getProjectName());
            			}
            			
                		System.out.println("\nPlease type the project name you want to register in:");
                		if (!OfficerService.registerProject(officer, sc.nextLine())) {
                			System.out.println("Unable to register for specified project");
                		} else {
                			System.out.println("Registeration has been successful. Please wait for the relevant manager for it's updated status!");
                		}
            		}
            	}
            	case 2 -> {
            		Map<String, REGISTRATION> temp = OfficerService.getAllProjectStatus(officer);
            		if (temp.size() > 0) {
            			System.out.println("Listing " + temp.size() + " project status:");
            			temp.forEach((k, v) -> System.out.println(k + " : " + v.toString()));
            		} else {
            			System.out.println("You have not registered for any projects!");
            		}
            	}
            	case 3 -> ProjectPrinter.viewProjects(OfficerService.getRegisteredProjects(officer));
                case 6 -> run = false;
                default -> System.out.println("Invalid input. Try again.");
            }

        } while (run);
        System.out.println("You have logged out.");
	}
}
