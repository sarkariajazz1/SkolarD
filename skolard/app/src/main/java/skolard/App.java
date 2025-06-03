package skolard;

import javax.swing.SwingUtilities;

import skolard.logic.auth.LoginHandler;
import skolard.logic.faq.FAQHandler;
import skolard.logic.matching.MatchingHandler;
import skolard.logic.message.MessageHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.session.SessionHandler;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceRegistry;
import skolard.persistence.PersistenceType;

/**
 * Main entry point for the SkolarD application.
 * Initializes the persistence layer and launches the authentication-based GUI.
 */
public class App {
    public static void main(String[] args) {
        // Initialize the persistence layer (PROD mode + seed)
        PersistenceFactory.initialize(PersistenceType.PROD, true);

        // Get instances from the registry
        ProfileHandler profileHandler = new ProfileHandler(
                PersistenceRegistry.getStudentPersistence(),
                PersistenceRegistry.getTutorPersistence());

        MatchingHandler matchingHandler = new MatchingHandler(PersistenceRegistry.getSessionPersistence());
        MessageHandler messageHandler = new MessageHandler(PersistenceRegistry.getMessagePersistence());
        SessionHandler sessionHandler = new SessionHandler(PersistenceRegistry.getSessionPersistence());
        // SupportHandler supportHandler = new SupportHandler(PersistenceRegistry.getSupportPersistence());
        FAQHandler faqHandler = new FAQHandler(); 
        LoginHandler loginHandler = new LoginHandler();

        // Start UI
        SwingUtilities.invokeLater(() -> {
            new skolard.presentation.SkolardApp(
                profileHandler, 
                matchingHandler, 
                sessionHandler, 
                messageHandler, 
                faqHandler, 
                loginHandler
            );
        });
    }
}
