package assignment2002;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import assignment2002.application.Application;
import assignment2002.application.ApplicationService;
import assignment2002.user.Officer;
import assignment2002.user.UserService;
import assignment2002.utils.Authenticator;
import assignment2002.utils.InputUtil;
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
            		+ "\n3: Change password"
            		+ "\n4: Logout");

            int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 4);

            switch (choice) {
                case 1 -> {
                	ApplicantController aController = new ApplicantController(officer);
                    aController.showMenu();
                }
                case 2 -> exclusiveOfficerMenu();
                case 3 -> UserService.resetPasswordPrompt(officer);
                case 4 -> run = false;
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
            		+ "\n5. View/update applicant's detail"
            		+ "\n6. Generate receipts for booked flats"
            		+ "\n7. Back");

            int choice = InputUtil.getValidatedIntRange(sc, "Choice: ", 1, 7);

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
            			
                		if (!OfficerService.registerProject(officer, InputUtil.getNonEmptyString(sc, "\nPlease type the project name you want to register in: "))) {
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
            	case 3 -> ProjectPrinter.viewProjects(officer.getRegisteredProjects());
            	case 4 -> {}// TODO
            	case 5 -> {
            		List<Application> apps = officer.getAllSuccessfulApplications();
            		if (apps.size() <= 0) {
            			System.out.println("No applications at the moment!");
            			continue;
            		}
            		System.out.println("Here are all successful applications:");
            		for (Application app1 : apps) {
            			ApplicationService.generateReceipt(app1);
            		}
            		String nric = "";
            		do {
            			nric = InputUtil.getNonEmptyString(sc, "Please enter applicant's NRIC to update applicant's detail (invalid input to exit):");
            			if (Authenticator.isValidNRIC(nric)) {
            				Application app = null;
            				for (Application app2 : apps) {
                    			if (app2.getApplicant().getNRIC().equalsIgnoreCase(nric)) {
                    				app = app2;
                    			}
                    		}
            				if (app == null) {
            					System.out.println("Unable to find application with this NRIC");
            					continue;
            				}
            				String roomType = InputUtil.getNonEmptyString(sc, "Please confirm applicant's room type (2-Room / 3-Room): ");
            				if (!roomType.equals("2-Room") && !roomType.equals("3-Room")) {
            					System.out.println("Invalid input! Please try again!");
            					continue;
            				}
            				OfficerService.updateBTOApplication(officer, app, roomType);
            			} else {
            				System.out.println("Not valid NRIC! Please try again...");
            			}
            		} while (nric.equals("0"));
            	}
            	case 6 -> {
            		String projectName = InputUtil.getNonEmptyString(sc, "Enter the project name: ");
            		String applicantNric = InputUtil.getNonEmptyString(sc, "Enter the applicant's NRIC: ");
            		Application app = OfficerService.getSuccessfulApplicationByApplicantNRIC(officer, projectName, applicantNric);
            		if (app == null) {
            			System.out.println("Unable to retrieve application by details you have entered!");
            			continue;
            		}
            		OfficerService.generateReceiptOfApplication(officer, app);
            	}
                case 7 -> run = false;
                default -> System.out.println("Invalid input. Try again.");
            }

        } while (run);
        System.out.println("You have logged out.");
	}
}
