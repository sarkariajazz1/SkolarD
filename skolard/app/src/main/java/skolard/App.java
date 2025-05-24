package skolard;

import javax.swing.SwingUtilities;

import skolard.logic.ProfileHandler;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;
import skolard.presentation.SkolardApp;

/**
 * Main entry point for the SkolarD application.
 * Initializes the stub persistence layer and launches the dashboard.
 */
public class App {
    public static void main(String[] args) {

        // Initialize the data system
        PersistenceFactory.initialize(PersistenceType.STUB, false);

        ProfileHandler profileHandler = new ProfileHandler(PersistenceFactory.getStudentPersistence(), 
            PersistenceFactory.getTutorPersistence());

        // Launch the Swing GUI in the event-dispatch thread
        SwingUtilities.invokeLater(SkolardApp::new);
    }
}
