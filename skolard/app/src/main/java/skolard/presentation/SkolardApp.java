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

        // Create navigation buttons for profile, matching, and FAQ views
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton profileBtn = new JButton("View Profiles");
        JButton matchBtn = new JButton("Find Tutors");
        JButton faqBtn = new JButton("FAQs");

        buttonPanel.add(profileBtn);
        buttonPanel.add(matchBtn);
        buttonPanel.add(faqBtn);
        add(buttonPanel, BorderLayout.CENTER);

        // Open Profile View
        profileBtn.addActionListener(e -> new ProfileView(profileHandler));
<<<<<<< HEAD
        profileBtn.addActionListener(e -> new ProfileView(profileHandler));

        // Open Matching View
        matchBtn.addActionListener(e -> new MatchingView(matchingHandler));
        matchBtn.addActionListener(e -> new MatchingView(matchingHandler));
=======

        // Open Matching View
        matchBtn.addActionListener(e -> new MatchingView(matchingHandler));
>>>>>>> 99cac762440978524c2469e9ea2bc07ffc382f0b

        // Open FAQ View
        faqBtn.addActionListener(e -> new FAQView());

        setDefaultCloseOperation(EXIT_ON_CLOSE); // Full app exits
        setSize(500, 150);
        setLocationRelativeTo(null); // Center
        setVisible(true);
    }
}
