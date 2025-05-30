package skolard.presentation;

import javax.swing.*;

import skolard.logic.FAQHandler;
import skolard.logic.MatchingHandler;
import skolard.logic.ProfileHandler;
import skolard.logic.MessageHandler;

import java.awt.*;

/**
 * The main dashboard window of SkolarD that allows navigation to other views.
 */
public class SkolardApp extends JFrame {

    public SkolardApp(ProfileHandler profileHandler, MatchingHandler matchingHandler, FAQHandler faqHandler, MessageHandler messageHandler) {
        super("SkolarD - Dashboard"); 

        setLayout(new BorderLayout());

        // Create navigation buttons for profile, matching, FAQ, and messages
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // Adjust grid for 4 buttons
        JButton profileBtn = new JButton("View Profiles");
        JButton matchBtn = new JButton("Find Tutors");
        JButton faqBtn = new JButton("FAQs");
        JButton messageBtn = new JButton("Messages"); // NEW

        // Add buttons to panel
        buttonPanel.add(profileBtn);
        buttonPanel.add(matchBtn);
        buttonPanel.add(faqBtn);
        buttonPanel.add(messageBtn); // NEW
        add(buttonPanel, BorderLayout.CENTER);

        // Open Profile View
        profileBtn.addActionListener(e -> new ProfileView(profileHandler));

        // Open Matching View
        matchBtn.addActionListener(e -> new MatchingView(matchingHandler));

        // Open FAQ View
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        // Open Message View
        messageBtn.addActionListener(e -> new MessageView(messageHandler)); // NEW

        setDefaultCloseOperation(EXIT_ON_CLOSE); // Full app exits
        setSize(500, 200);
        setLocationRelativeTo(null); // Center
        setVisible(true);
    }

    // Overloaded constructor for legacy usage without MessageHandler
    public SkolardApp(ProfileHandler profileHandler, MatchingHandler matchingHandler, FAQHandler faqHandler) {
        this(profileHandler, matchingHandler, faqHandler, null);
    }
}
