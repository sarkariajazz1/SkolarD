package skolard.presentation;

import javax.swing.*;

import skolard.logic.FAQHandler;
import skolard.logic.MatchingHandler;
import skolard.logic.ProfileHandler;

import java.awt.*;

/**
 * The main dashboard window of SkolarD that allows navigation to other views.
 */
public class SkolardApp extends JFrame {

    public SkolardApp(ProfileHandler profileHandler, MatchingHandler matchingHandler) {
        this(profileHandler, matchingHandler, new FAQHandler());
    }

    public SkolardApp(ProfileHandler profileHandler, MatchingHandler matchingHandler, FAQHandler faqHandler) {
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

        // Open Matching View
        matchBtn.addActionListener(e -> new MatchingView(matchingHandler));

        // Open FAQ View
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        setDefaultCloseOperation(EXIT_ON_CLOSE); // Full app exits
        setSize(500, 150);
        setLocationRelativeTo(null); // Center
        setVisible(true);
    }
}
