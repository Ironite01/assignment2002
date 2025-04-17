package assignment2002.enquiry;

import java.util.ArrayList;
import java.util.List;

public class EnquiryService {
	private static List<Enquiry> enquiries = new ArrayList<Enquiry>();
	
	public List<Enquiry> viewAll() {
		return enquiries;
	}
	
	public void reply(int enquiryId, String reply) {
		
	}
	
	public static Enquiry getEnquiry(String applicantNric, String projectName) {
		for (Enquiry e : enquiries) {
			if (e.getApplicantNric().equalsIgnoreCase(applicantNric) && e.getProjectName() == projectName) {
				return e;
			}
		}
		return null;
	}
	
	public static void addNewEnquiry(String applicantNric, String projectName, String message) {
		enquiries.add(new Enquiry(applicantNric, projectName, message));
	}
}
