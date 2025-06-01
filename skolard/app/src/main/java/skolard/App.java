package skolard;

import javax.swing.SwingUtilities;

import skolard.logic.FAQHandler;
import skolard.logic.LoginHandler;
import skolard.logic.MatchingHandler;
import skolard.logic.MessageHandler;
import skolard.logic.ProfileHandler;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;

/**
 * Main entry point for the SkolarD application.
 * Initializes the persistence layer and launches the authentication-based GUI.
 */
public class App {
    public static void main(String[] args) {
        // Initialize the persistence layer (PROD mode + seed)
        PersistenceFactory.initialize(PersistenceType.PROD, true);

        ProfileHandler profileHandler = new ProfileHandler(PersistenceFactory.getStudentPersistence(),
                PersistenceFactory.getTutorPersistence());
        MatchingHandler matchingHandler = new MatchingHandler(PersistenceFactory.getSessionPersistence());
        MessageHandler messageHandler = new MessageHandler(PersistenceFactory.getMessagePersistence());
        FAQHandler faqHandler = new FAQHandler(); // if used elsewhere
        LoginHandler loginHandler = new LoginHandler();
        // Start UI
        SwingUtilities.invokeLater(() -> {
            new skolard.presentation.SkolardApp(profileHandler, matchingHandler,faqHandler,loginHandler );
        });
    }
}