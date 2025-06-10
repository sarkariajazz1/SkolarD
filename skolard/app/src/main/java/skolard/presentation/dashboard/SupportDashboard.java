
package skolard.presentation.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
    private final Support currentSupport;
    private final SupportHandler supportHandler;
    private final MessageHandler messageHandler;
    private final boolean isFirstLogin;

    public SupportDashboard(Support support, SupportHandler supportHandler,
                            MessageHandler messageHandler, boolean isFirstLogin) {
        super("SkolarD - Support Dashboard");
        this.currentSupport = support;
        this.supportHandler = supportHandler;
        this.messageHandler = messageHandler;
        this.isFirstLogin = isFirstLogin;

        initializeUI();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Welcome message at the top
        String welcomeText = isFirstLogin ?
                "Welcome " + currentSupport.getName() + "!" :
                "Welcome back " + currentSupport.getName() + "!";

        JLabel welcomeLabel = new JLabel(welcomeText, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 18f));
        add(welcomeLabel, BorderLayout.NORTH);

        // Instructions panel - simplified without button descriptions
        JTextArea instructionsArea = new JTextArea(8, 50);
        instructionsArea.setEditable(false);
        instructionsArea.setBackground(getBackground());
        instructionsArea.setText(getInstructionsText());
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setLineWrap(true);
        instructionsArea.setFont(instructionsArea.getFont().deriveFont(14f));

        JScrollPane instructionsScrollPane = new JScrollPane(instructionsArea);
        add(instructionsScrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JButton ticketHandlingBtn = new JButton("Ticket Handling");
        JButton messagesBtn = new JButton("Messages");
        JButton logoutBtn = new JButton("Logout");

        buttonPanel.add(ticketHandlingBtn);
        buttonPanel.add(messagesBtn);
        buttonPanel.add(logoutBtn);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Setup event listeners
        ticketHandlingBtn.addActionListener(e -> {
            new SupportView(supportHandler, currentSupport);
            dispose();
        });

        messagesBtn.addActionListener(e -> {
            new MessageView(messageHandler, currentSupport);
            dispose();
        });

        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
    }

    private String getInstructionsText() {
        return "As a Support Staff member, you have access to the following features:\n\n" +
                "• Ticket Handling: View and manage support tickets submitted by students and tutors.\n\n" +
                "• Messages: Communicate with users regarding their support tickets.\n\n" +
                "Select an option below to get started with your support duties.";
    }
}