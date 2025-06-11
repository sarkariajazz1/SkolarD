package skolard.presentation.dashboard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import skolard.logic.message.MessageHandler;
import skolard.logic.support.SupportHandler;
import skolard.objects.Support;
import skolard.presentation.message.MessageView;
import skolard.presentation.support.SupportView;

/**
 * GUI window for support staff dashboard in SkolarD.
 * Displays personalized welcome message and support-specific functionality.
 */
public class SupportDashboard extends JFrame {
    // The Support object representing the currently logged-in support staff.
    private final Support currentSupport;
    // Handler for support-related business logic.
    private final SupportHandler supportHandler;
    // Handler for message-related business logic.
    private final MessageHandler messageHandler;
    // A boolean indicating if this is the support staff's first login.
    private final boolean isFirstLogin;

    /**
     * Constructs a new SupportDashboard window.
     *
     * @param support The {@link Support} object for the logged-in staff member.
     * @param supportHandler The {@link SupportHandler} instance for managing support tickets.
     * @param messageHandler The {@link MessageHandler} instance for managing messages.
     * @param isFirstLogin A boolean flag indicating if this is the user's first login.
     */
    public SupportDashboard(Support support, SupportHandler supportHandler,
                            MessageHandler messageHandler, boolean isFirstLogin) {
        super("SkolarD - Support Dashboard");
        this.currentSupport = support;
        this.supportHandler = supportHandler;
        this.messageHandler = messageHandler;
        this.isFirstLogin = isFirstLogin;

        // Initialize the graphical user interface components.
        initializeUI();
        // Set the default close operation for the frame.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the size of the window.
        setSize(600, 500);
        // Center the window on the screen.
        setLocationRelativeTo(null);
        // Make the window visible to the user.
        setVisible(true);
    }

    /**
     * Initializes and arranges all UI components within the dashboard frame.
     * This includes the welcome message, instructions, and functional buttons.
     *
     * @return void
     */
    private void initializeUI() {
        // Set the layout manager for the main frame.
        setLayout(new BorderLayout(10, 10));

        // Determine the welcome message based on whether it's the first login.
        String welcomeText = isFirstLogin ?
                "Welcome " + currentSupport.getName() + "!" :
                "Welcome back " + currentSupport.getName() + "!";

        // Create and configure the welcome label.
        JLabel welcomeLabel = new JLabel(welcomeText, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 18f));
        add(welcomeLabel, BorderLayout.NORTH);

        // Create and configure the instructions text area.
        JTextArea instructionsArea = new JTextArea(8, 50);
        instructionsArea.setEditable(false);
        instructionsArea.setBackground(getBackground());
        instructionsArea.setText(getInstructionsText());
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setLineWrap(true);
        instructionsArea.setFont(instructionsArea.getFont().deriveFont(14f));

        // Add the instructions area to a scroll pane and then to the center of the frame.
        JScrollPane instructionsScrollPane = new JScrollPane(instructionsArea);
        add(instructionsScrollPane, BorderLayout.CENTER);

        // Create a panel for the action buttons.
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        // Initialize buttons for dashboard functionalities.
        JButton ticketHandlingBtn = new JButton("Ticket Handling");
        JButton messagesBtn = new JButton("Messages");
        JButton logoutBtn = new JButton("Logout");

        // Add buttons to the button panel.
        buttonPanel.add(ticketHandlingBtn);
        buttonPanel.add(messagesBtn);
        buttonPanel.add(logoutBtn);

        // Create a bottom panel to hold the button panel.
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Setup event listeners for the buttons.
        ticketHandlingBtn.addActionListener(e -> {
            // Open the SupportView window.
            new SupportView(supportHandler, currentSupport);
        });

        messagesBtn.addActionListener(e -> {
            // Open the MessageView window.
            new MessageView(messageHandler, currentSupport);
        });

        logoutBtn.addActionListener(e -> {
            // Show a confirmation dialog for logout.
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                // Dispose the dashboard frame if logout is confirmed.
                dispose();
            }
        });
    }

    /**
     * Provides the instructional text displayed on the support dashboard.
     *
     * @return A {@link String} containing the instructions for support staff.
     */
    private String getInstructionsText() {
        return "As a Support Staff member, you have access to the following features:\n\n" +
                "• Ticket Handling: View and manage support tickets submitted by students and tutors.\n\n" +
                "• Messages: Communicate with users regarding their support tickets.\n\n" +
                "Select an option below to get started with your support duties.";
    }
}