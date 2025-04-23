package assignment2002.enquiry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import assignment2002.utils.FileManifest;

import java.util.Date;

public class EnquiryService implements FileManifest {
	private static List<Enquiry> enquiries = new ArrayList<Enquiry>();
	
	public static List<Enquiry> viewAll() {
		return enquiries;
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
	
		Enquiry newEnquiry = new Enquiry(applicantNric, projectName, message);
		enquiries.add(newEnquiry);
	
		saveEnquiriesToFile();
	}
	

	public static void saveEnquiriesToFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(ENQUIRIES_TXT_PATH))) {
	
			List<Enquiry> sortedEnquiries = enquiries.stream()
				.sorted((e1, e2) -> {
					Date d1 = e1.getAllMessages().keySet().stream().min(Date::compareTo).orElse(new Date(0));
					Date d2 = e2.getAllMessages().keySet().stream().min(Date::compareTo).orElse(new Date(0));
					return d1.compareTo(d2);
				})
				.toList();
	
			for (Enquiry e : sortedEnquiries) {
				if (e.getAllMessages().isEmpty() && e.isResolved()) continue;
	
				writer.write("NRIC:" + e.getApplicantNric());
				writer.newLine();
				writer.write("Project:" + e.getProjectName());
				writer.newLine();
				writer.write("Resolved:" + e.isResolved());
				writer.newLine();
	
				e.getAllMessages().entrySet().stream()
					.sorted(Map.Entry.comparingByKey())
					.forEach(entry -> {
						try {
							writer.write(entry.getKey().getTime() + "|" +
										 entry.getValue().getMessage() + "|" +
										 entry.getValue().getAuthor().getNRIC());
							writer.newLine();
						} catch (IOException ex) {
							System.out.println("Error writing message: " + ex.getMessage());
						}
					});
	
				writer.write("===");
				writer.newLine();
			}
	
		} catch (IOException e) {
			System.out.println("Error saving enquiries: " + e.getMessage());
		}
	}

	public static void loadEnquiriesFromFile() {
		enquiries.clear();
		File file = new File(ENQUIRIES_TXT_PATH);
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
				
					if (resolvedLine.contains("true")) {
						current.setResolved();
					}
				
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
		loadEnquiriesFromFile();
	
		enquiries.removeIf(e ->
			e.getApplicantNric().equalsIgnoreCase(applicantNric)
			&& e.getProjectName().equalsIgnoreCase(projectName)
		);
	
		saveEnquiriesToFile();
	}
	
	public static void markEnquiryAsResolved(String applicantNric, String projectName) {
		loadEnquiriesFromFile();
	
		for (Enquiry e : enquiries) {
			if (e.getApplicantNric().equalsIgnoreCase(applicantNric) &&
				e.getProjectName().equalsIgnoreCase(projectName)) {
				e.setResolved();
				System.out.println("Marked enquiry as resolved.");
				break;
			}
		}
	
		saveEnquiriesToFile();
	}
	

}
