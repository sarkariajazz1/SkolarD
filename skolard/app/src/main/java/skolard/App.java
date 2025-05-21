package skolard;

import java.time.LocalDateTime;
import javax.swing.SwingUtilities;

import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;
import skolard.presentation.ui.ProfileView;
import skolard.presentation.ui.MatchingView;
import skolard.logic.TutorList;
import skolard.logic.MatchingHandler;

public class App {
    public static void main(String[] args) {

        PersistenceFactory.initialize(PersistenceType.STUB, false);

        SwingUtilities.invokeLater(() -> {
            new ProfileView();
            new MatchingView();
        });

        LocalDateTime dateTime = LocalDateTime.now();
        MatchingHandler matcher = new MatchingHandler(new TutorList());

    }
}
