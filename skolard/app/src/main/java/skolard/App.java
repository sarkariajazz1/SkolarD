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
        // Initialize the persistence layer with PROD mode
        // The second argument 'true' indicates that seed SQL files should be executed
        PersistenceFactory.initialize(PersistenceType.PROD, true);

        // Launch the GUI on the Event Dispatch Thread (recommended for Swing applications)
        SwingUtilities.invokeLater(() -> {
            new skolard.presentation.SkolardApp();
        });

        // Use this to close the database connection and reset persistence
        // Uncomment if you're terminating the application manually or running non-GUI tests (currently makes the code crash)
        // PersistenceFactory.reset(); // ← Enable only if you want to shut down DB after GUI closes
    }
}
