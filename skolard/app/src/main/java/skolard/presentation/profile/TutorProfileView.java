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
    // The Tutor object representing the currently logged-in tutor.
    private final Tutor currentTutor;
    // Handler for tutor profile-related business logic.
    private final ProfileHandler profileHandler;
    // Handler for session-related business logic.
    private final SessionHandler sessionHandler;
    // Handler for message-related business logic.
    private final MessageHandler messageHandler;
    // Handler for FAQ-related business logic.
    private final FAQHandler faqHandler;
    // A boolean indicating if this is the tutor's first login.
    private final boolean isFirstLogin;

    /**
     * Constructs a new TutorProfileView window.
     *
     * @param tutor The {@link Tutor} object for the logged-in tutor.
     * @param profileHandler The {@link ProfileHandler} instance for managing tutor profiles.
     * @param sessionHandler The {@link SessionHandler} instance for managing sessions.
     * @param messageHandler The {@link MessageHandler} instance for managing messages.
     * @param faqHandler The {@link FAQHandler} instance for managing FAQs.
     * @param isFirstLogin A boolean flag indicating if this is the user's first login.
     */
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

        // Initialize the graphical user interface components.
        initializeUI();
        // Set the default close operation for the frame.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the size of the window.
        setSize(700, 600);
        // Center the window on the screen.
        setLocationRelativeTo(null);
        // Make the window visible to the user.
        setVisible(true);
    }

    /**
     * Initializes and arranges all UI components within the dashboard frame.
     * This includes the welcome message, instructions, and functional buttons specific to a tutor.
     *
     * @return void
     */
    private void initializeUI() {
        // Set the layout manager for the main frame.
        setLayout(new BorderLayout(10, 10));

        // Determine the welcome message based on whether it's the first login.
        String welcomeText = isFirstLogin ?
                "Welcome " + currentTutor.getName() + "!" :
                "Welcome back " + currentTutor.getName() + "!";

        // Create and configure the welcome label.
        JLabel welcomeLabel = new JLabel(welcomeText, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 18f));
        add(welcomeLabel, BorderLayout.NORTH);

        // Create and configure the instructions text area.
        JTextArea instructionsArea = new JTextArea(12, 60);
        instructionsArea.setEditable(false); // Make it read-only.
        instructionsArea.setBackground(getBackground()); // Match background to frame.
        instructionsArea.setText(getInstructionsText()); // Set the instructional text.
        instructionsArea.setWrapStyleWord(true); // Wrap words at line breaks.
        instructionsArea.setLineWrap(true); // Wrap lines if too long.
        instructionsArea.setFont(instructionsArea.getFont().deriveFont(14f)); // Set font size.

        // Add a scroll pane around the instructions area.
        JScrollPane instructionsScrollPane = new JScrollPane(instructionsArea);
        add(instructionsScrollPane, BorderLayout.CENTER);

        // Create a panel for the action buttons with a grid layout.
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        // Initialize buttons for various tutor functionalities.
        JButton myStudentsBtn = new JButton("My Students");
        JButton manageProfileBtn = new JButton("Manage Profile");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");
        JButton backBtn = new JButton("Back");

        // Add buttons to the button panel.
        buttonPanel.add(myStudentsBtn);
        buttonPanel.add(manageProfileBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);
        buttonPanel.add(backBtn);

        // Create a bottom panel to hold the button panel.
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Setup event listeners for each button.
        myStudentsBtn.addActionListener(e -> {
            // Open the TutorView (dashboard for managing students) and dispose the current dashboard.
            new TutorView(profileHandler, sessionHandler, messageHandler, currentTutor);
            dispose();
        });

        manageProfileBtn.addActionListener(e -> {
            showManageProfileView(); // Display the tutor's profile management view.
        });

        sessionBtn.addActionListener(e -> {
            // Open the SessionView (for managing tutoring sessions) and dispose the current dashboard.
            new SessionView(sessionHandler, currentTutor);
            dispose();
        });

        messageBtn.addActionListener(e -> {
            // Open the MessageView and dispose the current dashboard.
            new MessageView(messageHandler, currentTutor);
            dispose();
        });

        supportBtn.addActionListener(e -> {
            // Open the SupportView (requires a new SupportHandler instance) and dispose the current dashboard.
            new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentTutor);
            dispose();
        });

        faqBtn.addActionListener(e -> {
            // Open the FAQView and dispose the current dashboard.
            new FAQView(faqHandler);
            dispose();
        });

        backBtn.addActionListener(e -> dispose()); // Close the current dashboard window.
    }

    /**
     * Provides the instructional text for the tutor dashboard.
     *
     * @return A {@link String} containing instructions for using the dashboard.
     */
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

    /**
     * Creates and displays a separate JFrame to allow the tutor to view and manage their profile details.
     * This includes an option to edit their bio.
     *
     * @return void
     */
    private void showManageProfileView() {
        // Create a new JFrame for the profile management display.
        JFrame profileFrame = new JFrame("Manage Profile - " + currentTutor.getName());
        profileFrame.setLayout(new BorderLayout(10, 10));

        // Create a non-editable JTextArea to display the current profile information.
        JTextArea profileArea = new JTextArea(15, 40);
        profileArea.setEditable(false);
        // Get the full profile string from the profile handler and set it to the text area.
        profileArea.setText(profileHandler.viewFullProfile(currentTutor));

        // Add a scroll pane around the text area for content that might exceed visible bounds.
        JScrollPane scrollPane = new JScrollPane(profileArea);
        profileFrame.add(scrollPane, BorderLayout.CENTER);

        // Create a button panel for "Edit Bio" and "Back" buttons.
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton updateBioBtn = new JButton("Edit Bio");
        JButton backBtn = new JButton("Back");

        // Add action listener for the "Edit Bio" button.
        updateBioBtn.addActionListener(e -> {
            String currentBio = currentTutor.getBio();
            // Show an input dialog pre-filled with the current bio for editing.
            String newBio = JOptionPane.showInputDialog(profileFrame,
                    "Enter your new bio:", currentBio);

            if (newBio != null && !newBio.trim().isEmpty()) {
                // Update the tutor's bio using the profile handler.
                profileHandler.updateBio(currentTutor, newBio.trim());
                // Refresh the displayed profile text.
                profileArea.setText(profileHandler.viewFullProfile(currentTutor));
                JOptionPane.showMessageDialog(profileFrame, "Bio updated successfully!");
            }
        });

        // Add action listener for the "Back" button to close the profile management frame.
        backBtn.addActionListener(e -> profileFrame.dispose());

        buttonPanel.add(updateBioBtn);
        buttonPanel.add(backBtn);
        profileFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Set default close operation, size, and position for the profile management frame.
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(500, 400);
        profileFrame.setLocationRelativeTo(this); // Center relative to the dashboard.
        profileFrame.setVisible(true);
    }
}