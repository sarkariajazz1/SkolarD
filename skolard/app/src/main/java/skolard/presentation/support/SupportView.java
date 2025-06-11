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
import skolard.objects.SupportTicket;
import skolard.objects.User;
import skolard.persistence.PersistenceRegistry;

/**
 * GUI window for managing support tickets in SkolarD.
 * Allows students and tutors to submit tickets. Support users can view and close tickets,
 * and message users regarding their tickets.
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

    // Handlers and current user
    private final SupportHandler handler;
    private final User currentUser;
    private List<SupportTicket> currentTickets; // Stores the currently displayed list of tickets.
    private final MessageHandler messageHandler;

    /**
     * Constructs a new SupportView window.
     *
     * @param supportHandler The {@link SupportHandler} instance for managing support tickets.
     * @param user The {@link User} object representing the currently logged-in user.
     */
    public SupportView(SupportHandler supportHandler, User user) {
        super("SkolarD - Support Center");

        this.handler = supportHandler;
        this.currentUser = user;
        // Initialize MessageHandler using the PersistenceRegistry for message persistence.
        this.messageHandler = new MessageHandler(PersistenceRegistry.getMessagePersistence());
        // Check if the current user is a support staff member.
        boolean isSupportUser = user instanceof skolard.objects.Support;

        // Set component names for testing purposes.
        titleField.setName("titleField");
        descriptionArea.setName("descriptionArea");
        submitTicketBtn.setName("submitTicketBtn");
        viewActiveBtn.setName("viewActiveBtn");
        viewHandledBtn.setName("viewHandledBtn");
        closeTicketBtn.setName("closeTicketBtn");
        messageUserBtn.setName("messageUserBtn");
        backButton.setName("backButton");
        ticketList.setName("ticketList");

        // Set the main layout for the frame.
        setLayout(new BorderLayout(10, 10)); // BorderLayout with gaps.

        // Panel for submitting new tickets (visible only to students/tutors).
        if (!isSupportUser) {
            JPanel submitPanel = new JPanel(new BorderLayout(5, 5));
            submitPanel.setBorder(BorderFactory.createTitledBorder("Submit New Support Ticket"));

            // Title input.
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(new JLabel("Title:"));
            titlePanel.add(titleField);
            submitPanel.add(titlePanel, BorderLayout.NORTH);

            // Description input.
            JPanel descPanel = new JPanel(new BorderLayout());
            descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
            descriptionArea.setLineWrap(true);  // Enable line wrapping.
            descriptionArea.setWrapStyleWord(true); // Wrap at word boundaries.
            descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER); // Add scroll pane for description.
            submitPanel.add(descPanel, BorderLayout.CENTER);

            // Submit button.
            JPanel submitBtnPanel = new JPanel(new FlowLayout());
            submitBtnPanel.add(submitTicketBtn);
            submitPanel.add(submitBtnPanel, BorderLayout.SOUTH);

            add(submitPanel, BorderLayout.NORTH);

            // Event handler for submit ticket button.
            submitTicketBtn.addActionListener(e -> {
                String title = titleField.getText().trim();
                String description = descriptionArea.getText().trim();

                // Validate input fields.
                if (title.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter both title and description",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    // Create and submit the support ticket.
                    SupportTicket ticket = new SupportTicket(currentUser, title, description);
                    handler.submitTicket(ticket);

                    JOptionPane.showMessageDialog(this,
                            "Support ticket submitted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Clear fields after successful submission.
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

        // View Panel (visible only for support users).
        if (isSupportUser) {
            JPanel viewPanel = new JPanel(new BorderLayout(5, 5));
            viewPanel.setBorder(BorderFactory.createTitledBorder("Support Tickets"));

            // Buttons for viewing and managing tickets.
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(viewActiveBtn);
            buttonPanel.add(viewHandledBtn);
            buttonPanel.add(closeTicketBtn);
            buttonPanel.add(messageUserBtn);
            viewPanel.add(buttonPanel, BorderLayout.NORTH);

            // List to display tickets.
            ticketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow single selection.
            viewPanel.add(new JScrollPane(ticketList), BorderLayout.CENTER); // Add scroll pane for ticket list.

            add(viewPanel, BorderLayout.CENTER);

            // Event handlers for support user buttons.
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

                // Ensure a valid ticket is selected from the current list.
                if (currentTickets == null || selectedIndex >= currentTickets.size()) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid ticket selection",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SupportTicket selectedTicket = currentTickets.get(selectedIndex);
                // Prevent closing an already handled ticket.
                if (selectedTicket.isHandled()) {
                    JOptionPane.showMessageDialog(this,
                            "This ticket is already closed",
                            "Already Closed",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                try {
                    handler.closeTicket(selectedTicket); // Close the selected ticket.
                    JOptionPane.showMessageDialog(this,
                            "Ticket closed successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadActiveTickets(); // Refresh to show updated active tickets.
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error closing ticket: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            // Message User button functionality.
            messageUserBtn.addActionListener(e -> {
                int selectedIndex = ticketList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(this,
                            "Please select a ticket to message the user",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Ensure a valid ticket is selected from the current list.
                if (currentTickets == null || selectedIndex >= currentTickets.size()) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid ticket selection",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SupportTicket selectedTicket = currentTickets.get(selectedIndex);
                showMessageDialog(selectedTicket); // Open the message composition dialog.
            });
        }

        // Back button panel at bottom.
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);

        // Back button event handler to close the window.
        backButton.addActionListener(e -> dispose());

        // Set default close operation, pack components, and center the window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack(); // Adjusts window size to fit components.
        setLocationRelativeTo(null); // Centers the window on the screen.
        setVisible(true); // Makes the window visible.
    }

    /**
     * Loads and displays all active (unhandled) support tickets in the ticket list.
     * Updates the {@code currentTickets} list with the fetched tickets.
     *
     * @return void
     */
    private void loadActiveTickets() {
        try {
            currentTickets = handler.getActiveTickets(); // Fetch active tickets.
            ticketListModel.clear(); // Clear existing items from the list model.

            if (currentTickets.isEmpty()) {
                ticketListModel.addElement("No active tickets found");
            } else {
                // Add each active ticket's string representation to the list model.
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

    /**
     * Loads and displays all handled (closed) support tickets in the ticket list.
     * Updates the {@code currentTickets} list with the fetched tickets.
     *
     * @return void
     */
    private void loadHandledTickets() {
        try {
            currentTickets = handler.getHandledTickets(); // Fetch handled tickets.
            ticketListModel.clear(); // Clear existing items from the list model.

            if (currentTickets.isEmpty()) {
                ticketListModel.addElement("No handled tickets found");
            } else {
                // Add each handled ticket's string representation to the list model.
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

    /**
     * Displays a dialog for composing and sending a message to the user who submitted a support ticket.
     * The message includes ticket details and the support staff's response.
     *
     * @param ticket The {@link SupportTicket} for which to compose a message.
     * @return void
     */
    private void showMessageDialog(SupportTicket ticket) {
        JFrame messageFrame = new JFrame("Message User - Ticket #" + ticket.getTicketId());
        messageFrame.setLayout(new BorderLayout(10, 10));
        messageFrame.setSize(500, 400); // Set a preferred size for the dialog.

        // Panel to display ticket information.
        JPanel ticketInfoPanel = new JPanel(new BorderLayout());
        ticketInfoPanel.setBorder(BorderFactory.createTitledBorder("Ticket Information"));

        JTextArea ticketInfoArea = new JTextArea(4, 40); // 4 rows, 40 columns.
        ticketInfoArea.setEditable(false); // Make it read-only.
        // Populate ticket information.
        ticketInfoArea.setText("Ticket ID: " + ticket.getTicketId() + "\n" +
                "User: " + ticket.getRequester().getName() + " (" + ticket.getRequester().getEmail() + ")\n" +
                "Title: " + ticket.getTitle() + "\n" +
                "Description: " + ticket.getDescription());
        ticketInfoArea.setWrapStyleWord(true);
        ticketInfoArea.setLineWrap(true);

        ticketInfoPanel.add(new JScrollPane(ticketInfoArea), BorderLayout.CENTER);
        messageFrame.add(ticketInfoPanel, BorderLayout.NORTH);

        // Panel for composing the message.
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createTitledBorder("Compose Message"));

        JTextArea messageArea = new JTextArea(8, 40); // 8 rows, 40 columns.
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setText("Type your message to the user here..."); // Placeholder text.

        messagePanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        messageFrame.add(messagePanel, BorderLayout.CENTER);

        // Button panel for send and cancel actions.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton sendBtn = new JButton("Send Message");
        JButton cancelBtn = new JButton("Cancel");

        sendBtn.addActionListener(e -> {
            String messageText = messageArea.getText().trim();
            // Validate message content.
            if (messageText.isEmpty() || messageText.equals("Type your message to the user here...")) {
                JOptionPane.showMessageDialog(messageFrame,
                        "Please enter a message before sending",
                        "Empty Message",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Construct the subject and full message content.
                String subject = "Support Response - Ticket #" + ticket.getTicketId() + ": " + ticket.getTitle();
                String fullMessage = "Dear " + ticket.getRequester().getName() + ",\n\n" +
                        "This is a response regarding your support ticket:\n\n" +
                        "Ticket ID: " + ticket.getTicketId() + "\n" +
                        "Title: " + ticket.getTitle() + "\n\n" +
                        "Support Response:\n" + messageText + "\n\n" +
                        "Best regards,\n" +
                        "SkolarD Support Team";

                // Determine recipient emails based on the requester type.
                String studentEmail;
                String tutorEmail;

                if (ticket.getRequester() instanceof skolard.objects.Student) {
                    studentEmail = ticket.getRequester().getEmail();
                    tutorEmail = currentUser.getEmail(); // Support staff acts as tutor for messaging.
                } else { // Requester is a Tutor.
                    studentEmail = currentUser.getEmail(); // Support staff acts as student for messaging.
                    tutorEmail = ticket.getRequester().getEmail();
                }

                // Create a new Message object.
                Message supportMessage = new Message(
                        0, // messageId will be set by the persistence layer.
                        LocalDateTime.now(), // Current time for sending.
                        studentEmail, // Recipient's email (student's email).
                        tutorEmail, // Sender's email (tutor's email).
                        currentUser.getEmail(), // Actual sender (support staff's email).
                        fullMessage // The composed message content.
                );

                // Send the message using the message handler.
                messageHandler.sendMessage(supportMessage);

                JOptionPane.showMessageDialog(messageFrame,
                        "Message sent successfully to " + ticket.getRequester().getName(),
                        "Message Sent",
                        JOptionPane.INFORMATION_MESSAGE);

                messageFrame.dispose(); // Close the message dialog.

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(messageFrame,
                        "Error sending message: " + ex.getMessage(),
                        "Send Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> messageFrame.dispose()); // Close dialog on cancel.

        buttonPanel.add(sendBtn);
        buttonPanel.add(cancelBtn);
        messageFrame.add(buttonPanel, BorderLayout.SOUTH);

        messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messageFrame.setLocationRelativeTo(this); // Center relative to the main SupportView.
        messageFrame.setVisible(true);
    }
}