package skolard;

import javax.swing.SwingUtilities;

import skolard.logic.FAQHandler;
import skolard.logic.MatchingHandler;
import skolard.logic.ProfileHandler;
import skolard.logic.MessageHandler;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;

/**
 * Main entry point for the SkolarD application.
 * Initializes the persistence layer and launches the main UI.
 */
public class App {
    public static void main(String[] args) {
        // Initialize the persistence layer (PROD mode + seed)
        PersistenceFactory.initialize(PersistenceType.PROD, true);

        // Logic handlers
        ProfileHandler profileHandler = new ProfileHandler(
            PersistenceFactory.getStudentPersistence(),
            PersistenceFactory.getTutorPersistence()
        );
        MatchingHandler matchingHandler = new MatchingHandler(PersistenceFactory.getSessionPersistence());
        MessageHandler messageHandler = new MessageHandler(PersistenceFactory.getMessagePersistence());
        FAQHandler faqHandler = new FAQHandler(); // if used elsewhere

        // Start UI
        SwingUtilities.invokeLater(() -> {
            new skolard.presentation.SkolardApp(profileHandler, matchingHandler, faqHandler, messageHandler);
        });
    }
}
