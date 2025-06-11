package skolard;

import javax.swing.SwingUtilities;

import skolard.logic.auth.LoginHandler;
import skolard.logic.booking.BookingHandler;
import skolard.logic.faq.FAQHandler;
import skolard.logic.message.MessageHandler;
import skolard.logic.payment.PaymentHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.rating.RatingHandler;
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
        

        BookingHandler bookingHandler = new BookingHandler(PersistenceRegistry.getSessionPersistence());
        MessageHandler messageHandler = new MessageHandler(PersistenceRegistry.getMessagePersistence());
        SessionHandler sessionHandler = new SessionHandler(PersistenceRegistry.getSessionPersistence(),
            PersistenceRegistry.getRatingRequestPersistence());
        ProfileHandler profileHandler = new ProfileHandler(PersistenceRegistry.getStudentPersistence(),
            PersistenceRegistry.getTutorPersistence(), sessionHandler);
        FAQHandler faqHandler = new FAQHandler(PersistenceRegistry.getFAQPersistence()); 
        LoginHandler loginHandler = new LoginHandler();
        RatingHandler ratingHandler = new RatingHandler(PersistenceRegistry.getRatingRequestPersistence(),
            PersistenceRegistry.getRatingPersistence());
        PaymentHandler paymentHandler = new PaymentHandler(PersistenceRegistry.getCardPersistence());

        // Start UI
        SwingUtilities.invokeLater(() -> {
            new skolard.presentation.SkolardApp(
                profileHandler, 
                bookingHandler, 
                sessionHandler, 
                messageHandler, 
                faqHandler, 
                loginHandler,
                ratingHandler,
                paymentHandler
            );
        });
    }
}
