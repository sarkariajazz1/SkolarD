package skolard.presentation;

import javax.swing.*;

import skolard.logic.FAQHandler;
import skolard.logic.MatchingHandler;
import skolard.logic.ProfileHandler;
import skolard.logic.SupportHandler;

import java.awt.*;

/**
 * The main dashboard window of SkolarD that allows navigation to other views.
 */
public class SkolardApp extends JFrame {

    public SkolardApp(ProfileHandler profileHandler, MatchingHandler matchingHandler, SupportHandler supportHandler) {
        this(profileHandler, matchingHandler, new FAQHandler());
    }

    public SkolardApp(ProfileHandler profileHandler, MatchingHandler matchingHandler, FAQHandler faqHandler) {
        super("SkolarD - Dashboard");

        setLayout(new BorderLayout());

        // Create navigation buttons for all views
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        JButton loginBtn = new JButton("Login");
        JButton signUpBtn = new JButton("Sign Up");
        JButton profileBtn = new JButton("View Profiles");
        JButton matchBtn = new JButton("Find Tutors");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");

        buttonPanel.add(loginBtn);
        buttonPanel.add(signUpBtn);
        buttonPanel.add(profileBtn);
        buttonPanel.add(matchBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);
        add(buttonPanel, BorderLayout.CENTER);

        // Open Login View
        loginBtn.addActionListener(e -> new LoginView(profileHandler));

        // Open Sign Up View
        signUpBtn.addActionListener(e -> new SignUpView(profileHandler));

        // Open Profile View
        profileBtn.addActionListener(e -> new ProfileView(profileHandler));

        // Open Matching View
        matchBtn.addActionListener(e -> new MatchingView(matchingHandler));

        // Open Support View
        //supportBtn.addActionListener(e -> new SupportView(supportHandler));

        // Open FAQ View
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        setDefaultCloseOperation(EXIT_ON_CLOSE); // Full app exits
        setSize(600, 200);
        setLocationRelativeTo(null); // Center
        setVisible(true);
    }
}