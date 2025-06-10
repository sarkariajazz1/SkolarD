package skolard.presentation.profile;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import skolard.logic.faq.FAQHandler;
import skolard.logic.message.MessageHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.session.SessionHandler;
import skolard.logic.support.SupportHandler;
import skolard.objects.Tutor;
import skolard.persistence.PersistenceRegistry;
import skolard.presentation.dashboard.TutorView;
import skolard.presentation.faq.FAQView;
import skolard.presentation.message.MessageView;
import skolard.presentation.session.SessionView;
import skolard.presentation.support.SupportView;

/**
 * GUI window for tutor profile dashboard in SkolarD.
 * Displays personalized welcome message and tutor-specific functionality.
 */
public class TutorProfileView extends JFrame {
    private final Tutor currentTutor;
    private final ProfileHandler profileHandler;
    private final SessionHandler sessionHandler;
    private final MessageHandler messageHandler;
    private final FAQHandler faqHandler;
    private final boolean isFirstLogin;

    public TutorProfileView(Tutor tutor, ProfileHandler profileHandler,
                            SessionHandler sessionHandler, MessageHandler messageHandler,
                            FAQHandler faqHandler, boolean isFirstLogin) {
        super("SkolarD - Tutor Dashboard");
        this.currentTutor = tutor;
        this.profileHandler = profileHandler;
        this.sessionHandler = sessionHandler;
        this.messageHandler = messageHandler;
        this.faqHandler = faqHandler;
        this.isFirstLogin = isFirstLogin;

        initializeUI();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Welcome message at the top
        String welcomeText = isFirstLogin ?
                "Welcome " + currentTutor.getName() + "!" :
                "Welcome back " + currentTutor.getName() + "!";

        JLabel welcomeLabel = new JLabel(welcomeText, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 18f));
        add(welcomeLabel, BorderLayout.NORTH);

        // Instructions panel
        JTextArea instructionsArea = new JTextArea(12, 60);
        instructionsArea.setEditable(false);
        instructionsArea.setBackground(getBackground());
        instructionsArea.setText(getInstructionsText());
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setLineWrap(true);
        instructionsArea.setFont(instructionsArea.getFont().deriveFont(14f));

        JScrollPane instructionsScrollPane = new JScrollPane(instructionsArea);
        add(instructionsScrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JButton myStudentsBtn = new JButton("My Students");
        JButton manageProfileBtn = new JButton("Manage Profile");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");
        JButton backBtn = new JButton("Back");

        buttonPanel.add(myStudentsBtn);
        buttonPanel.add(manageProfileBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);
        buttonPanel.add(backBtn);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Setup event listeners
        myStudentsBtn.addActionListener(e -> {
            new TutorView(profileHandler, sessionHandler, messageHandler, currentTutor);
            dispose();
        });

        manageProfileBtn.addActionListener(e -> {
            showManageProfileView();
        });

        sessionBtn.addActionListener(e -> {
            new SessionView(sessionHandler, currentTutor);
            dispose();
        });

        messageBtn.addActionListener(e -> {
            new MessageView(messageHandler, currentTutor);
            dispose();
        });

        supportBtn.addActionListener(e -> {
            new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentTutor);
            dispose();
        });

        faqBtn.addActionListener(e -> {
            new FAQView(faqHandler);
            dispose();
        });

        backBtn.addActionListener(e -> dispose());
    }

    private String getInstructionsText() {
        return "Welcome to your Tutor Dashboard! Here's what each button does:\n\n" +
                "• My Students: View and manage your current students, see who has booked your sessions, and track your tutoring relationships.\n\n" +
                "• Manage Profile: Update your bio, view your complete profile information, and modify your tutoring details.\n\n" +
                "• Session Management: Create new tutoring sessions, view your upcoming and past sessions, and manage session details.\n\n" +
                "• Messages: Communicate with students who have booked your sessions or are interested in your tutoring services.\n\n" +
                "• Support: Submit support tickets if you encounter any issues or need assistance with the platform.\n\n" +
                "• FAQs: Access frequently asked questions and helpful information about using SkolarD as a tutor.\n\n" +
                "Select any option below to get started!";
    }

    private void showManageProfileView() {
        JFrame profileFrame = new JFrame("Manage Profile - " + currentTutor.getName());
        profileFrame.setLayout(new BorderLayout(10, 10));

        // Display current profile
        JTextArea profileArea = new JTextArea(15, 40);
        profileArea.setEditable(false);
        profileArea.setText(profileHandler.viewFullProfile(currentTutor));

        JScrollPane scrollPane = new JScrollPane(profileArea);
        profileFrame.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton updateBioBtn = new JButton("Update My Bio");
        JButton backBtn = new JButton("Back");

        updateBioBtn.addActionListener(e -> {
            String currentBio = currentTutor.getBio();
            String newBio = JOptionPane.showInputDialog(profileFrame,
                    "Enter your new bio:", currentBio);

            if (newBio != null && !newBio.trim().isEmpty()) {
                profileHandler.updateBio(currentTutor, newBio.trim());
                profileArea.setText(profileHandler.viewFullProfile(currentTutor));
                JOptionPane.showMessageDialog(profileFrame, "Bio updated successfully!");
            }
        });

        backBtn.addActionListener(e -> profileFrame.dispose());

        buttonPanel.add(updateBioBtn);
        buttonPanel.add(backBtn);
        profileFrame.add(buttonPanel, BorderLayout.SOUTH);

        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(500, 400);
        profileFrame.setLocationRelativeTo(this);
        profileFrame.setVisible(true);
    }
}