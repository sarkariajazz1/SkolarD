package skolard;

import javax.swing.SwingUtilities;
import skolard.logic.ProfileHandler;
import skolard.objects.Student;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;

import java.util.List;

/**
 * Main entry point for the SkolarD application.
 * Initializes the persistence layer and prints students from the database.
 */
public class App {
    public static void main(String[] args) {
        // ✅ Initialize real DB (set to true to seed sample data)
        PersistenceFactory.initialize(PersistenceType.PROD, true);

        // Optional: load and print students from DB to verify seeding
        try {
            List<Student> students = PersistenceFactory.getStudentPersistence().getAllStudents();
            System.out.println("? Students in database:");
            for (Student s : students) {
                System.out.println("- " + s.getName() + " (" + s.getEmail() + ")");
            }
        } catch (Exception e) {
            System.err.println("? Failed to query students: " + e.getMessage());
            e.printStackTrace();
        }

        // Launch the GUI (if needed)
        SwingUtilities.invokeLater(() -> {
            new skolard.presentation.SkolardApp();
        });

        // ✅ Safely close connection after usage (if appropriate)
       //ersistenceFactory.reset(); // ← Enable only if you want to shut down DB after GUI closes
    }
}
