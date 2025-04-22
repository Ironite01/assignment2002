package assignment2002.enquiry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class EnquiryService {
	private static List<Enquiry> enquiries = new ArrayList<Enquiry>();
	private static final String FILE_PATH = "Information/Enquiries.txt";
	
	public List<Enquiry> viewAll() {
		return enquiries;
	}
	
	public void reply(int enquiryId, String reply) {
		
	}
	
	public static Enquiry getEnquiry(String applicantNric, String projectName) {
		loadEnquiriesFromFile();
		for (Enquiry e : enquiries) {
			if (e.getApplicantNric().equalsIgnoreCase(applicantNric)
				&& e.getProjectName().equalsIgnoreCase(projectName)) {
				return e;
			}
		}
		return null;
	}
	
	
	public static void addNewEnquiry(String applicantNric, String projectName, String message) {
		loadEnquiriesFromFile();
		enquiries.removeIf(e ->
			e.getApplicantNric().equalsIgnoreCase(applicantNric)
			&& e.getProjectName().equalsIgnoreCase(projectName)
		);
		enquiries.add(new Enquiry(applicantNric, projectName, message));
	}
	

	public static void saveEnquiriesToFile() {
		if (enquiries == null || enquiries.isEmpty()) {
			loadEnquiriesFromFile();
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
			for (Enquiry e : enquiries) {
				writer.write("NRIC:" + e.getApplicantNric());
				writer.newLine();
				writer.write("Project:" + e.getProjectName());
				writer.newLine();
				writer.write("Resolved:" + e.isResolved());
				writer.newLine();
				for (var entry : e.getAllMessages().entrySet()) {
					writer.write(entry.getKey().getTime() + "|" + entry.getValue().getMessage() + "|" + entry.getValue().getAuthor().getNRIC());
					writer.newLine();
				}
				writer.write("===");
				writer.newLine();
			}
		} catch (IOException e) {
			System.out.println("Error saving enquiries: " + e.getMessage());
		}
	}

	public static void loadEnquiriesFromFile() {
		enquiries.clear();
		File file = new File(FILE_PATH);
		if (!file.exists()) return;

		try (Scanner scanner = new Scanner(file)) {
			Enquiry current = null;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.startsWith("NRIC:")) {
					String nric = line.substring(5).trim();
					String project = scanner.nextLine().substring(8).trim();
					String resolvedLine = scanner.nextLine();
					current = new Enquiry(nric, project, "");
					current.getAllMessages().clear();
					if (resolvedLine.contains("true")) current.getAllMessages().clear();
				} else if (line.equals("===")) {
					enquiries.add(current);
					current = null;
				} else if (current != null) {
					String[] parts = line.split("\\|");
					if (parts.length >= 3) {
						Date date = new Date(Long.parseLong(parts[0]));
						String msg = parts[1];
						String author = parts[2];
						current.getAllMessages().put(date, new Message(msg, author));
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error loading enquiries: " + e.getMessage());
		}
	}

	public static void deleteEnquiry(String applicantNric, String projectName) {
		enquiries.removeIf(e ->
			e.getApplicantNric().equalsIgnoreCase(applicantNric)
			&& e.getProjectName().equalsIgnoreCase(projectName)
		);
	}
	

}
