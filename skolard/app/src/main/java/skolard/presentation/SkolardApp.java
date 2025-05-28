package skolard.presentation;

import javax.swing.*;

import skolard.logic.MatchingHandler;
import skolard.logic.ProfileHandler;

import java.awt.*;

/**
 * The main dashboard window of SkolarD that allows navigation to other views.
 */
public class SkolardApp extends JFrame {

    public SkolardApp(ProfileHandler profileHandler, MatchingHandler matchingHandler) {
        super("SkolarD - Dashboard"); 

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
        profileBtn.addActionListener(e -> new ProfileView(profileHandler));

        // Open Matching View
        matchBtn.addActionListener(e -> new MatchingView(matchingHandler));
        matchBtn.addActionListener(e -> new MatchingView(matchingHandler));

        setDefaultCloseOperation(EXIT_ON_CLOSE); // Full app exits
        setSize(400, 150);
        setLocationRelativeTo(null); // Center
        setVisible(true);
    }
}
