package skolard;

import java.time.LocalDateTime;
import javax.swing.SwingUtilities;

import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;
import skolard.presentation.ui.ProfileView;
import skolard.logic.TutorList;
import skolard.logic.matchingHandler;

public class App {
    public static void main(String[] args) {
        // 1) Initialize persistence (stub for Iteration 1)
        PersistenceFactory.initialize(PersistenceType.STUB, false);

        // 2) Launch the ProfileView UI on the Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new ProfileView();
        });

        // 3) (Optional) Your matching demonstration can still run here
        LocalDateTime dateTime = LocalDateTime.now();
        matchingHandler matcher = new matchingHandler(new TutorList());
        // matcher.addSession(...);
        // List<Session> results = matcher.getAvailableSessions("COMP1010");
        // System.out.println(results);
    }
}
