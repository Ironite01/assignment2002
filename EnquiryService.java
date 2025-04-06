package assignment2002;

import java.util.ArrayList;
import java.util.List;

public class EnquiryService {
	private static List<Enquiry> enquiries = new ArrayList<Enquiry>();
	
	public List<Enquiry> viewAll() {
		return enquiries;
	}
	
	public void reply(int enquiryId, String reply) {
		
	}
	
	public static Enquiry getEnquiry(String applicantNric, int projectId) {
		for (Enquiry e : enquiries) {
			if (e.getApplicantNric().equalsIgnoreCase(applicantNric) && e.getProjectId() == projectId) {
				return e;
			}
		}
		throw new NullPointerException("Unable to find enquiry");
	}
	
	public static void addNewEnquiry(String applicantNric, int projectId, String message) {
		enquiries.add(new Enquiry(applicantNric, projectId, message));
	}
}
