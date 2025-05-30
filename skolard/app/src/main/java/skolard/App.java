package skolard;

import skolard.logic.*;
import skolard.persistence.*;
import skolard.presentation.*;

import javax.swing.*;

/**
 * Main entry point for the SkolarD application.
 */
public class App {
    public static void main(String[] args) {
        PersistenceFactory.initialize(PersistenceType.PROD, true);

        LoginHandler loginHandler = new LoginHandler(PersistenceFactory.getLoginPersistence());

        SwingUtilities.invokeLater(() -> {
            while (true) {
                LoginView loginView = new LoginView();
                while (loginView.getCredentials() == null) {
                    try {
                        Thread.sleep(100); // Wait until login view is closed
                    } catch (InterruptedException ignored) {}
                }

                if (loginHandler.login(loginView.getCredentials())) {
                    // Load and launch dashboard
                    ProfileHandler profileHandler = new ProfileHandler(
                            PersistenceFactory.getStudentPersistence(),
                            PersistenceFactory.getTutorPersistence());
                    MatchingHandler matchingHandler = new MatchingHandler(PersistenceFactory.getSessionPersistence());
                    MessageHandler messageHandler = new MessageHandler(PersistenceFactory.getMessagePersistence());
                    FAQHandler faqHandler = new FAQHandler();

                    new SkolardApp(profileHandler, matchingHandler, faqHandler, messageHandler);
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed. Please try again.");
                }
            }
        });
    }
}
