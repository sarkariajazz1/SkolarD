package skolard.presentation;

import javax.swing.*;

import skolard.logic.MatchingHandler;
import skolard.logic.ProfileHandler;

import java.awt.*;

/**
 * The main dashboard window of SkolarD that allows navigation to other views.
 */
public class SkolardApp extends JFrame {
    private ProfileHandler profileHandler;
    private MatchingHandler matchingHandler;

    public SkolardApp(ProfileHandler profileHandler, MatchingHandler matchingHandler) {
<<<<<<< HEAD
        super("SkolarD - Dashboard"); 
=======
        super("SkolarD - Dashboard");
        this.profileHandler = profileHandler;
        this.matchingHandler = matchingHandler; 
>>>>>>> 63c339be8ea797f66cf290dea6a4cbb28f29d364

        setLayout(new BorderLayout());

        // Create navigation buttons for profile and matching views
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton profileBtn = new JButton("View Profiles");
        JButton matchBtn = new JButton("Find Tutors");

        buttonPanel.add(profileBtn);
        buttonPanel.add(matchBtn);
        add(buttonPanel, BorderLayout.CENTER);

        // Open Profile View
        profileBtn.addActionListener(e -> new ProfileView(profileHandler));

        // Open Matching View
        matchBtn.addActionListener(e -> new MatchingView(matchingHandler));

        setDefaultCloseOperation(EXIT_ON_CLOSE); // Full app exits
        setSize(400, 150);
        setLocationRelativeTo(null); // Center
        setVisible(true);
    }
}
