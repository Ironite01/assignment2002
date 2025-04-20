package assignment2002;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import assignment2002.application.Application;
import assignment2002.application.ApplicationService;
import assignment2002.user.Applicant;
import assignment2002.user.Officer;
import assignment2002.user.User;
import assignment2002.utils.Authenticator;
import assignment2002.utils.Data;
import assignment2002.utils.DateCheck;
import assignment2002.utils.LoadInfo;
import assignment2002.utils.Status;

public class OfficerService implements Status {
	public static List<BTOProperty> getAvailableProjectsToRegister(Officer officer) {
		return Data.btoList.stream()
			.filter((BTOProperty p) ->
					p.getOfficers().stream().noneMatch(o -> o.getNRIC().equalsIgnoreCase(officer.getNRIC()))
					&& p.getAppliedOfficers().stream().noneMatch(o -> o.getNRIC().equalsIgnoreCase(officer.getNRIC()))
					&& p.getRejectedOfficers().stream().noneMatch(o -> o.getNRIC().equalsIgnoreCase(officer.getNRIC()))
					&& p.getOfficers().size() < p.getOfficerSlot()
					&& DateCheck.dateComparator(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toString(), p.getCloseDate())
					&& !(officer.getAppliedProject().equalsIgnoreCase(p.getProjectName())
					&& !officer.getApplicationStatus().equalsIgnoreCase(APPLICATION_STATUS.NOTAPPLIED.toString()))
					)
			.collect(Collectors.toList());
	}
	
	public static boolean registerProject(Officer officer, String projectName) {
		List<BTOProperty> proj = getAvailableProjectsToRegister(officer);
		for (BTOProperty p : proj)
		{
			if (p.getProjectName().equalsIgnoreCase(projectName) ) {
				p.addAppliedOfficer(officer);
				return true;
			}
		}
		return false;
	}
	
	public static ArrayList<BTOProperty> getRegisteredProjects(Officer o) {
		ArrayList<BTOProperty> registeredProperties = new ArrayList<>();
		ArrayList<BTOProperty> properties = LoadInfo.loadProperties(LoadInfo.loadUsers());
		o.getAllProjectStatus().forEach((k, v) -> {
			if (v.equals(REGISTRATION.SUCCESSFUL)) {
				for (BTOProperty p : properties) {
					if (p.getProjectName().equalsIgnoreCase(k)) {
						registeredProperties.add(p);
					}
				}
			}
		});
		return registeredProperties;
	}
	
	public static Map<String, REGISTRATION> getAllProjectStatus(Officer o) {
		Map<String, REGISTRATION> temp = new HashMap<>();
		ArrayList<BTOProperty> properties = LoadInfo.loadProperties(LoadInfo.loadUsers());
		for (BTOProperty p : properties) {
			for (Officer o1 : p.getOfficers()) {
				if (o.getNRIC().equalsIgnoreCase(o1.getNRIC())) {
					temp.put(p.getProjectName(), REGISTRATION.SUCCESSFUL);
				}
			}
			for (Officer o1 : p.getAppliedOfficers()) {
				if (o.getNRIC().equalsIgnoreCase(o1.getNRIC())) {
					temp.put(p.getProjectName(), REGISTRATION.PENDING);
				}
			}
			for (Officer o1 : p.getRejectedOfficers()) {
				if (o.getNRIC().equalsIgnoreCase(o1.getNRIC())) {
					temp.put(p.getProjectName(), REGISTRATION.REJECTED);
				}
			}
		}
		return temp;
	}
	
	public static Application getSuccessfulApplicationByApplicantNRIC(Officer o, String projectName, String applicantNric) {
		if (!Authenticator.isValidNRIC(applicantNric)) {
			return null;
		}
		ArrayList<User> a = LoadInfo.loadUsers();
		User user = a.stream().filter(u -> u.getNRIC().equalsIgnoreCase(applicantNric)).collect(Collectors.toList()).getFirst();
		
		BTOProperty p = o.getRegisteredProject(projectName);
		
		if (p == null) return null;
		Application app = ApplicationService.getApplicationByApplicantAndProperty(o, p);
		return app.getStatus() == APPLICATION_STATUS.SUCCESSFUL ? app : null;
	}
	
	public static void updateApplicantProfile(Application app, String flatType) {
		if (flatType.equalsIgnoreCase("2-Room") && !app.getFlatType().equalsIgnoreCase("2-Room")) {
			app.setFlatType(flatType);
			// TODO: Update the applicant's app to correct room type
			ApplicationService.editApplicationByColumn(app, "FlatType", "2-Room");
		} else if (flatType.equalsIgnoreCase("3-Room") && !app.getFlatType().equalsIgnoreCase("3-Room")) {
			app.setFlatType(flatType);
			ApplicationService.editApplicationByColumn(app, "FlatType", "3-Room");
		} else {} // This case means no change
	}
	
	// This is NOT referring to ApplicationService
	// Here we update the application via Officer's control - Successful -> Booked flow
	public static void updateBTOApplication(Officer o, Application app, String flatType) {
		BTOProperty p = o.getRegisteredProject(app.getProperty().getProjectName());
		if (p == null) return;
		if (!ApplicationService.getApplicationStatus(app.getApplicant())
				.equalsIgnoreCase(APPLICATION_STATUS.SUCCESSFUL.toString())) {
			System.out.println("Application status is not successful!");
			return;
		}
			
		app.getApplicant().setApplicationStatus(APPLICATION_STATUS.BOOKED.toString());
		ApplicationService.editApplicationByColumn(app, "Status", APPLICATION_STATUS.BOOKED.toString());
		
		updateApplicantProfile(app, flatType);
		
		if (app.getFlatType().equalsIgnoreCase("2-Room")) {
			p.setTwoRoomAmt(p.getTwoRoomAmt() - 1);
		} else if (app.getFlatType().equalsIgnoreCase("3-Room")) {
			p.setTwoRoomAmt(p.getThreeRoomAmt() - 1);
		}
		
		System.out.println("Application has been updated!");
		
		generateReceiptOfApplication(o, app);
	}
	
	// This will be called in 2 ways:
	// 1. After applicant application from successful -> booked
	// 2. From officer menu
	public static void generateReceiptOfApplication(Officer o, Application app) {
		BTOProperty p = o.getRegisteredProject(app.getProperty().getProjectName());
		if (p == null) return;
		
		ApplicationService.generateReceipt(app);
	}
}
