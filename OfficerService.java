package assignment2002;

import java.util.List;
import java.util.stream.Collectors;

import assignment2002.user.Officer;
import assignment2002.utils.Data;
import assignment2002.utils.Status;

public class OfficerService implements Status {
	public static List<BTOProperty> getAvailableProjectsToRegister(Officer officer) {
		return Data.btoList.stream()
			.filter((BTOProperty p) ->
					p.getOfficers().stream().noneMatch(o -> o.getNRIC().equalsIgnoreCase(officer.getNRIC()))
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
}
