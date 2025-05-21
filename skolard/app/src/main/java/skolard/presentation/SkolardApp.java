package skolard.presentation;

import javax.swing.*;
import java.awt.*;

public class SkolardApp extends JFrame {
    public SkolardApp() {
        super("SkolarD - Dashboard");

        setLayout(new BorderLayout());

        // Simple button panel to launch views
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton profileBtn = new JButton("View Profiles");
        JButton matchBtn = new JButton("Find Tutors");

        buttonPanel.add(profileBtn);
        buttonPanel.add(matchBtn);
        add(buttonPanel, BorderLayout.CENTER);

        profileBtn.addActionListener(e -> new ProfileView());
        matchBtn.addActionListener(e -> new MatchingView());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
