package skolard.presentation.message;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import skolard.objects.Message;
import skolard.objects.Student;
import skolard.objects.User;

/**
 * Enhanced GUI for viewing and sending messages with conversation list.
 */
public class MessageView extends JFrame {
    // The handler responsible for message-related business logic.
    private final MessageHandler handler;
    // The currently logged-in user (either Student or Tutor).
    private final User currentUser;

    // Model for the list of conversations.
    private final DefaultListModel<String> conversationListModel = new DefaultListModel<>();
    // JList component to display the list of conversations.
    private final JList<String> conversationList = new JList<>(conversationListModel);
    // Text area to display the chat history of the selected conversation.
    private final JTextArea chatArea = new JTextArea(15, 40);
    // Text field for typing new messages.
    private final JTextField messageField = new JTextField(30);
    // Button to send the typed message.
    private final JButton sendButton = new JButton("Send");
    // Button to start a new conversation.
    private final JButton newConversationBtn = new JButton("New Conversation");
    // Button to refresh the list of conversations.
    private final JButton refreshButton = new JButton("Refresh");
    // Button to close the message view.
    private final JButton backButton = new JButton("Back");

    // Stores the email of the currently selected conversation partner.
    private String selectedConversationEmail = null;

    // Date and time formatter for displaying message timestamps.
    private static final DateTimeFormatter MESSAGE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /**
     * Constructs a new MessageView window.
     *
     * @param handler The {@link MessageHandler} instance for managing messages.
     * @param currentUser The {@link User} (Student or Tutor) who is currently logged in.
     */
    public MessageView(MessageHandler handler, User currentUser) {
        super("Messages - " + currentUser.getName());
        this.handler = handler;
        this.currentUser = currentUser;

        // Initialize and arrange the UI components.
        initializeUI();
        // Set names for UI components, useful for testing.
        setupComponentNames();
        // Load the list of existing conversations for the current user.
        loadConversations();

        // Set the size of the frame.
        setSize(700, 500);
        // Center the frame on the screen.
        setLocationRelativeTo(null);
        // Set the default close operation for the frame.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Make the frame visible.
        setVisible(true);
    }

    /**
     * Sets component names for testing purposes.
     * These names can be used by automated UI testing frameworks to identify components.
     *
     * @return void
     */
    private void setupComponentNames() {
        conversationList.setName("conversationList");
        chatArea.setName("chatArea");
        messageField.setName("messageField");
        sendButton.setName("sendButton");
        newConversationBtn.setName("newConversationBtn");
        refreshButton.setName("refreshButton");
        backButton.setName("backButton");
    }

    /**
     * Initializes and arranges all UI components within the message view frame.
     * This includes panels for conversations, chat area, message input, and buttons.
     *
     * @return void
     */
    private void initializeUI() {
        // Set the main layout manager.
        setLayout(new BorderLayout());

        // Setup the left panel for conversations.
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Conversations"), BorderLayout.NORTH);

        // Configure the conversation list and its listener.
        conversationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conversationList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Load messages for the newly selected conversation.
                loadSelectedConversation();
            }
        });

        // Add the conversation list to a scroll pane and then to the left panel.
        leftPanel.add(new JScrollPane(conversationList), BorderLayout.CENTER);

        // Setup conversation control buttons.
        JPanel convButtonPanel = new JPanel(new FlowLayout());
        convButtonPanel.add(newConversationBtn);
        convButtonPanel.add(refreshButton);
        leftPanel.add(convButtonPanel, BorderLayout.SOUTH);

        // Setup the right panel for the chat area.
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Configure the chat area.
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        rightPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Setup the message input panel.
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(messageField);
        inputPanel.add(sendButton);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        // Add the left and right panels to the main frame.
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Setup the back button panel at the bottom.
        JPanel backPanel = new JPanel(new FlowLayout());
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);

        // Setup event listeners for buttons and message field.
        sendButton.addActionListener(e -> sendMessage());
        newConversationBtn.addActionListener(e -> startNewConversation());
        refreshButton.addActionListener(e -> loadConversations());
        backButton.addActionListener(e -> dispose());

        // Allow sending message by pressing Enter in the message field.
        messageField.addActionListener(e -> sendMessage());
    }

    /**
     * Loads the list of conversations for the current user and populates
     * the {@code conversationListModel}.
     *
     * @return void
     */
    private void loadConversations() {
        conversationListModel.clear();

        List<String> conversations;
        // Determine which type of users to retrieve conversations from based on the current user's role.
        if (currentUser instanceof Student) {
            conversations = handler.getTutorsMessaged(currentUser.getEmail());
        } else {
            conversations = handler.getStudentsMessaged(currentUser.getEmail());
        }

        // Add conversations to the list model, or a placeholder if none exist.
        if (conversations.isEmpty()) {
            conversationListModel.addElement("Click 'New Conversation' to start messaging");
        } else {
            for (String email : conversations) {
                conversationListModel.addElement(email);
            }
        }
    }

    /**
     * Loads messages for the conversation selected in the {@code conversationList}.
     * Updates {@code selectedConversationEmail} and calls {@link #loadMessagesForConversation(String)}.
     *
     * @return void
     */
    private void loadSelectedConversation() {
        int selectedIndex = conversationList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String selected = conversationListModel.getElementAt(selectedIndex);
            // Ensure the selected item is not the placeholder text.
            if (!selected.startsWith("Click")) {
                selectedConversationEmail = selected;
                // Load messages for the selected conversation.
                loadMessagesForConversation(selected);
            }
        }
    }

    /**
     * Displays the message history between the current user and another specified user
     * in the {@code chatArea}.
     *
     * @param otherUserEmail The email of the other participant in the conversation.
     * @return void
     */
    private void loadMessagesForConversation(String otherUserEmail) {
        // Retrieve message history from the handler.
        List<Message> messages = handler.getMessageHistory(currentUser.getEmail(), otherUserEmail);

        // Clear the chat area and append each message.
        chatArea.setText("");
        for (Message msg : messages) {
            String timestamp = msg.getTimeSent().format(MESSAGE_TIME_FORMATTER);
            // Determine if the sender is the current user or the other user for display.
            String sender = msg.getSenderEmail().equals(currentUser.getEmail()) ? "You" : msg.getSenderEmail();
            chatArea.append(String.format("[%s] %s: %s\n", timestamp, sender, msg.getMessage()));
        }

        // Scroll the chat area to the bottom to show the latest messages.
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    /**
     * Prompts the user to enter an email for a new conversation, then loads the
     * message history for that new conversation and updates the conversation list.
     *
     * @return void
     */
    private void startNewConversation() {
        // Determine the type of user (tutor/student) to prompt for.
        String otherUserType = (currentUser instanceof Student) ? "tutor" : "student";
        // Show input dialog to get the email of the new conversation partner.
        String email = JOptionPane.showInputDialog(this,
                "Enter " + otherUserType + " email to start conversation:");

        if (email != null && !email.trim().isEmpty()) {
            selectedConversationEmail = email.trim();
            // Load messages for the newly chosen conversation.
            loadMessagesForConversation(selectedConversationEmail);

            // Add to conversation list if not already present.
            boolean found = false;
            for (int i = 0; i < conversationListModel.size(); i++) {
                if (conversationListModel.getElementAt(i).equals(email.trim())) {
                    found = true;
                    // Select the existing conversation.
                    conversationList.setSelectedIndex(i);
                    break;
                }
            }

            if (!found) {
                // If a placeholder message is present, clear it before adding the new conversation.
                if (conversationListModel.size() == 1 &&
                        conversationListModel.getElementAt(0).startsWith("Click")) {
                    conversationListModel.clear();
                }
                // Add the new email to the conversation list and select it.
                conversationListModel.addElement(email.trim());
                conversationList.setSelectedIndex(conversationListModel.size() - 1);
            }
        }
    }

    /**
     * Sends the message typed in {@code messageField} to the currently selected conversation.
     * Clears the message field and refreshes the chat area and conversation list.
     *
     * @return void
     */
    private void sendMessage() {
        String messageText = messageField.getText().trim();

        // Validate message input.
        if (messageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a message.");
            return;
        }

        // Validate that a conversation is selected.
        if (selectedConversationEmail == null) {
            JOptionPane.showMessageDialog(this, "Please select a conversation first.");
            return;
        }

        // Create a new Message object.
        Message message = new Message(0, LocalDateTime.now(),
                currentUser.getEmail(), selectedConversationEmail,
                currentUser.getEmail(), messageText);

        // Send the message using the handler.
        handler.sendMessage(message);
        // Clear the message input field.
        messageField.setText("");
        // Reload messages to show the newly sent message.
        loadMessagesForConversation(selectedConversationEmail);
        // Refresh the conversation list, in case the new message created a new conversation.
        loadConversations();
    }
}