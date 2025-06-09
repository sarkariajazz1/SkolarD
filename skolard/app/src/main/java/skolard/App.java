package skolard;

import javax.swing.SwingUtilities;

<<<<<<< HEAD
import skolard.logic.FAQHandler;
import skolard.logic.MatchingHandler;
import skolard.logic.ProfileHandler;
=======
import skolard.logic.auth.LoginHandler;
import skolard.logic.faq.FAQHandler;
import skolard.logic.matching.MatchingHandler;
import skolard.logic.message.MessageHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.rating.RatingHandler;
import skolard.logic.session.SessionHandler;
>>>>>>> dev
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceRegistry;
import skolard.persistence.PersistenceType;

/**
 * Main entry point for the SkolarD application.
<<<<<<< HEAD
 * Initializes the persistence layer and prints students from the database.
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
=======
 * Initializes the persistence layer and launches the authentication-based GUI.
 */
public class App {
    public static void main(String[] args) {
        // Initialize the persistence layer (PROD mode + seed)
        PersistenceFactory.initialize(PersistenceType.PROD, true);

        // Get instances from the registry
        

        MatchingHandler matchingHandler = new MatchingHandler(PersistenceRegistry.getSessionPersistence());
        MessageHandler messageHandler = new MessageHandler(PersistenceRegistry.getMessagePersistence());
        SessionHandler sessionHandler = new SessionHandler(PersistenceRegistry.getSessionPersistence());
        ProfileHandler profileHandler = new ProfileHandler(sessionHandler);
        FAQHandler faqHandler = new FAQHandler(); 
        LoginHandler loginHandler = new LoginHandler();
        RatingHandler ratingHandler = new RatingHandler(PersistenceRegistry.getRatingPersistence());

        // Start UI
        SwingUtilities.invokeLater(() -> {
            new skolard.presentation.SkolardApp(
                profileHandler, 
                matchingHandler, 
                sessionHandler, 
                messageHandler, 
                faqHandler, 
                loginHandler,
                ratingHandler
            );
>>>>>>> dev
        });
    }
}
