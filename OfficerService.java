package assignment2002;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import assignment2002.user.Officer;
import assignment2002.utils.Data;
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
}
