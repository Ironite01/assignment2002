package assignment2002.application;

import assignment2002.user.Applicant;
import assignment2002.BTOProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationService {

    private static final List<Application> applications = new ArrayList<>();

    public static Application apply(Applicant applicant, BTOProperty property, String flatType) {
        Application app = new Application(applicant, property, flatType);
        applications.add(app);
        return app;
    }

    public static List<Application> getApplicationsByApplicant(Applicant applicant) {
        return applications.stream()
                .filter(app -> app.getApplicant().equals(applicant))
                .collect(Collectors.toList());
    }

    public static List<Application> getApplicationsByProperty(BTOProperty property) {
        return applications.stream()
                .filter(app -> app.getProperty().equals(property))
                .collect(Collectors.toList());
    }

    public static boolean approveApplication(Application application) {
        BTOProperty property = application.getProperty();
        String flatType = application.getFlatType();

        if (flatType.equals("2-Room") && property.getTwoRoomAmt() > 0) {
            property.setTwoRoomAmt(property.getTwoRoomAmt() - 1);
        } else if (flatType.equals("3-Room") && property.getThreeRoomAmt() > 0) {
            property.setThreeRoomAmt(property.getThreeRoomAmt() - 1);
        } else {
            System.out.println("No available units for flat type: " + flatType);
            return false;
        }

        application.setStatus(Application.ApplicationStatus.SUCCESSFUL);
        return true;
    }

    public static void rejectApplication(Application application) {
        application.setStatus(Application.ApplicationStatus.UNSUCCESSFUL);
    }

    public static boolean bookFlat(Application application) {
        if (!application.isSuccessful()) {
            System.out.println("Cannot book — application not successful.");
            return false;
        }

        if (application.isBooked()) {
            System.out.println("Application already booked.");
            return false;
        }

        application.setStatus(Application.ApplicationStatus.BOOKED);
        return true;
    }

    public static void generateReceipt(Application application) {
        Applicant a = application.getApplicant();
        BTOProperty p = application.getProperty();
        System.out.printf("""
        ====== RECEIPT ======
        Applicant: %s (%s)
        Age: %d | Marital Status: %s
        Flat Type: %s
        Project: %s (%s)
        Status: %s
        =====================
        """,
        a.getName(), a.getNRIC(), a.getAge(), a.getMaritalStatus(),
        application.getFlatType(), p.getProjectName(), p.getNeighbourhood(),
        application.getStatus());
    }
}
