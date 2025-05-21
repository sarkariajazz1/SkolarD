package skolard;

import javax.swing.SwingUtilities;

import skolard.persistence.PersistenceFactory;
import skolard.presentation.SkolardApp;

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
    }
}
