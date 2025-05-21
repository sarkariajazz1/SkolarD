package skolard;

import java.time.LocalDateTime;
import javax.swing.SwingUtilities;

import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;
import skolard.presentation.SkolardApp;
import skolard.logic.TutorList;
import skolard.logic.MatchingHandler;

public class App {
    public static void main(String[] args) {

        PersistenceFactory.initializeStubPersistence();

        SwingUtilities.invokeLater(SkolardApp::new);

        LocalDateTime dateTime = LocalDateTime.now();
        MatchingHandler matcher = new MatchingHandler(new TutorList());
    }
}
