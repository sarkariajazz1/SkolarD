package skolard;

import java.time.LocalDateTime;
import javax.swing.SwingUtilities;

import skolard.persistence.PersistenceFactory;
import skolard.presentation.SkolardApp;
import skolard.logic.TutorList;
import skolard.logic.MatchingHandler;

/**
 * Main entry point for the SkolarD application.
 * Initializes the stub persistence layer and launches the dashboard.
 */
public class App {
    public static void main(String[] args) {

        // Initialize the stub-based data system (used during development)
        PersistenceFactory.initializeStubPersistence();

        // Launch the Swing GUI in the event-dispatch thread
        SwingUtilities.invokeLater(SkolardApp::new);

        // Example use of logic classes (not required for UI)
        LocalDateTime dateTime = LocalDateTime.now();
        MatchingHandler matcher = new MatchingHandler(new TutorList());
    }
}
