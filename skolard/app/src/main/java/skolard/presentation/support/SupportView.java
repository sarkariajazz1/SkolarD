
package skolard.presentation.support;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import skolard.logic.message.MessageHandler;
import skolard.logic.support.SupportHandler;
import skolard.objects.Message;
import skolard.objects.Student;
import skolard.objects.SupportTicket;
import skolard.objects.User;
import skolard.persistence.PersistenceRegistry;

/**
 * GUI window for managing support tickets in SkolarD.
 * Students and tutors can submit tickets. Support users can view and close tickets.
 */
public class SupportView extends JFrame {

    // UI Components
    private final JTextField titleField = new JTextField(30);
    private final JTextArea descriptionArea = new JTextArea(8, 30);
    private final JButton submitTicketBtn = new JButton("Submit Ticket");
    private final JButton viewActiveBtn = new JButton("View Active Tickets");
    private final JButton viewHandledBtn = new JButton("View Handled Tickets");
    private final JButton closeTicketBtn = new JButton("Close Selected Ticket");
    private final JButton messageUserBtn = new JButton("Message User");
    private final JButton backButton = new JButton("Back");
    private final DefaultListModel<String> ticketListModel = new DefaultListModel<>();
    private final JList<String> ticketList = new JList<>(ticketListModel);

    private final SupportHandler handler;
    private final User currentUser;
    private List<SupportTicket> currentTickets;
    private final MessageHandler messageHandler;

    public SupportView(SupportHandler supportHandler, User user) {
        super("SkolarD - Support Center");

        this.handler = supportHandler;
        this.currentUser = user;
        this.messageHandler = new MessageHandler(PersistenceRegistry.getMessagePersistence());
        boolean isSupportUser = user instanceof skolard.objects.Support;

        setLayout(new BorderLayout(10, 10));

        // Submit Panel (only for students/tutors)
        if (!isSupportUser) {
            JPanel submitPanel = new JPanel(new BorderLayout(5, 5));
            submitPanel.setBorder(BorderFactory.createTitledBorder("Submit New Support Ticket"));

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(new JLabel("Title:"));
            titlePanel.add(titleField);
            submitPanel.add(titlePanel, BorderLayout.NORTH);

            JPanel descPanel = new JPanel(new BorderLayout());
            descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
            submitPanel.add(descPanel, BorderLayout.CENTER);

            JPanel submitBtnPanel = new JPanel(new FlowLayout());
            submitBtnPanel.add(submitTicketBtn);
            submitPanel.add(submitBtnPanel, BorderLayout.SOUTH);

            add(submitPanel, BorderLayout.NORTH);

            submitTicketBtn.addActionListener(e -> {
                String title = titleField.getText().trim();
                String description = descriptionArea.getText().trim();

                if (title.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter both title and description",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    SupportTicket ticket = new SupportTicket(currentUser, title, description);
                    handler.submitTicket(ticket);

                    JOptionPane.showMessageDialog(this,
                            "Support ticket submitted successfully!\nTicket ID: " + ticket.getTicketId(),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    titleField.setText("");
                    descriptionArea.setText("");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error submitting ticket: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        // View Panel (only for support users)
        if (isSupportUser) {
            JPanel viewPanel = new JPanel(new BorderLayout(5, 5));
            viewPanel.setBorder(BorderFactory.createTitledBorder("Support Tickets"));

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(viewActiveBtn);
            buttonPanel.add(viewHandledBtn);
            buttonPanel.add(closeTicketBtn);
            buttonPanel.add(messageUserBtn);
            viewPanel.add(buttonPanel, BorderLayout.NORTH);

            ticketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            viewPanel.add(new JScrollPane(ticketList), BorderLayout.CENTER);

            add(viewPanel, BorderLayout.CENTER);

            // Event handlers
            viewActiveBtn.addActionListener(e -> loadActiveTickets());
            viewHandledBtn.addActionListener(e -> loadHandledTickets());

            closeTicketBtn.addActionListener(e -> {
                int selectedIndex = ticketList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(this,
                            "Please select a ticket to close",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (currentTickets == null || selectedIndex >= currentTickets.size()) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid ticket selection",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SupportTicket selectedTicket = currentTickets.get(selectedIndex);
                if (selectedTicket.isHandled()) {
                    JOptionPane.showMessageDialog(this,
                            "This ticket is already closed",
                            "Already Closed",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                try {
                    handler.closeTicket(selectedTicket);
                    JOptionPane.showMessageDialog(this,
                            "Ticket closed successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadActiveTickets();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error closing ticket: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            // Message User button functionality
            messageUserBtn.addActionListener(e -> {
                int selectedIndex = ticketList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(this,
                            "Please select a ticket to message the user",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (currentTickets == null || selectedIndex >= currentTickets.size()) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid ticket selection",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SupportTicket selectedTicket = currentTickets.get(selectedIndex);
                showMessageDialog(selectedTicket);
            });
        }

        // Back button panel at bottom
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);

        // Back button event handler
        backButton.addActionListener(e -> dispose());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadActiveTickets() {
        try {
            currentTickets = handler.getActiveTickets();
            ticketListModel.clear();

            if (currentTickets.isEmpty()) {
                ticketListModel.addElement("No active tickets found");
            } else {
                for (SupportTicket ticket : currentTickets) {
                    ticketListModel.addElement(ticket.toString());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading active tickets: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHandledTickets() {
        try {
            currentTickets = handler.getHandledTickets();
            ticketListModel.clear();

            if (currentTickets.isEmpty()) {
                ticketListModel.addElement("No handled tickets found");
            } else {
                for (SupportTicket ticket : currentTickets) {
                    ticketListModel.addElement(ticket.toString());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading handled tickets: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMessageDialog(SupportTicket ticket) {
        JFrame messageFrame = new JFrame("Message User - Ticket #" + ticket.getTicketId());
        messageFrame.setLayout(new BorderLayout(10, 10));
        messageFrame.setSize(500, 400);

        // Ticket information panel
        JPanel ticketInfoPanel = new JPanel(new BorderLayout());
        ticketInfoPanel.setBorder(BorderFactory.createTitledBorder("Ticket Information"));

        JTextArea ticketInfoArea = new JTextArea(4, 40);
        ticketInfoArea.setEditable(false);
        ticketInfoArea.setText("Ticket ID: " + ticket.getTicketId() + "\n" +
                "User: " + ticket.getRequester().getName() + " (" + ticket.getRequester().getEmail() + ")\n" +
                "Title: " + ticket.getTitle() + "\n" +
                "Description: " + ticket.getDescription());
        ticketInfoArea.setWrapStyleWord(true);
        ticketInfoArea.setLineWrap(true);

        ticketInfoPanel.add(new JScrollPane(ticketInfoArea), BorderLayout.CENTER);
        messageFrame.add(ticketInfoPanel, BorderLayout.NORTH);

        // Message composition panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createTitledBorder("Compose Message"));

        JTextArea messageArea = new JTextArea(8, 40);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setText("Type your message to the user here...");

        messagePanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        messageFrame.add(messagePanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton sendBtn = new JButton("Send Message");
        JButton cancelBtn = new JButton("Cancel");

        sendBtn.addActionListener(e -> {
            String messageText = messageArea.getText().trim();
            if (messageText.isEmpty() || messageText.equals("Type your message to the user here...")) {
                JOptionPane.showMessageDialog(messageFrame,
                        "Please enter a message before sending",
                        "Empty Message",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Create a message regarding the support ticket
                String subject = "Support Response - Ticket #" + ticket.getTicketId() + ": " + ticket.getTitle();
                String fullMessage = "Dear " + ticket.getRequester().getName() + ",\n\n" +
                        "This is a response regarding your support ticket:\n\n" +
                        "Ticket ID: " + ticket.getTicketId() + "\n" +
                        "Title: " + ticket.getTitle() + "\n\n" +
                        "Support Response:\n" + messageText + "\n\n" +
                        "Best regards,\n" +
                        "SkolarD Support Team";

                // Determine student and tutor emails based on the requester type
                String studentEmail;
                String tutorEmail;

                if (ticket.getRequester() instanceof skolard.objects.Student) {
                    studentEmail = ticket.getRequester().getEmail();
                    tutorEmail = currentUser.getEmail(); // Support staff acts as tutor for messaging
                } else {
                    studentEmail = currentUser.getEmail(); // Support staff acts as student for messaging
                    tutorEmail = ticket.getRequester().getEmail();
                }

                // Create Message with proper constructor parameters
                Message supportMessage = new Message(
                        0, // messageId - will be set by persistence layer
                        LocalDateTime.now(), // timeSent
                        studentEmail, // studentEmail
                        tutorEmail, // tutorEmail
                        currentUser.getEmail(), // senderEmail (support staff)
                        fullMessage // message content
                );

                messageHandler.sendMessage(supportMessage);

                JOptionPane.showMessageDialog(messageFrame,
                        "Message sent successfully to " + ticket.getRequester().getName(),
                        "Message Sent",
                        JOptionPane.INFORMATION_MESSAGE);

                messageFrame.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(messageFrame,
                        "Error sending message: " + ex.getMessage(),
                        "Send Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> messageFrame.dispose());

        buttonPanel.add(sendBtn);
        buttonPanel.add(cancelBtn);
        messageFrame.add(buttonPanel, BorderLayout.SOUTH);

        messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messageFrame.setLocationRelativeTo(this);
        messageFrame.setVisible(true);
    }
}