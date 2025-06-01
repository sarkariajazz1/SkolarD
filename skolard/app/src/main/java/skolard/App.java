package skolard;

import javax.swing.SwingUtilities;

import skolard.logic.FAQHandler;
import skolard.logic.MatchingHandler;
import skolard.logic.ProfileHandler;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;

/**
 * Main entry point for the SkolarD application.
 * Initializes the persistence layer and launches the authentication-based GUI.
 */
public class App {
    public static void main(String[] args) {
        // Initialize the persistence layer with PROD mode
        // The second argument 'true' indicates that seed SQL files should be executed
        PersistenceFactory.initialize(PersistenceType.PROD, true);

        ProfileHandler profileHandler = new ProfileHandler(PersistenceFactory.getStudentPersistence(),
                PersistenceFactory.getTutorPersistence());
        MatchingHandler matchingHandler = new MatchingHandler(PersistenceFactory.getSessionPersistence());
        FAQHandler faqHandler = new FAQHandler();

        // Launch the GUI on the Event Dispatch Thread (recommended for Swing applications)
        SwingUtilities.invokeLater(() -> {
            new skolard.presentation.SkolardApp(profileHandler, matchingHandler, faqHandler);
        });
    }
}