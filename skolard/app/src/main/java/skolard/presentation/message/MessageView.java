
package skolard.presentation.message;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import skolard.objects.Tutor;
import skolard.objects.User;

/**
 * Enhanced GUI for viewing and sending messages with conversation list.
 */
public class MessageView extends JFrame {
    private final MessageHandler handler;
    private final User currentUser;

    private final DefaultListModel<String> conversationListModel = new DefaultListModel<>();
    private final JList<String> conversationList = new JList<>(conversationListModel);
    private final JTextArea chatArea = new JTextArea(15, 40);
    private final JTextField messageField = new JTextField(30);
    private final JButton sendButton = new JButton("Send");
    private final JButton newConversationBtn = new JButton("New Conversation");
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton backButton = new JButton("Back");

    private String selectedConversationEmail = null;

    private static final DateTimeFormatter MESSAGE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public MessageView(MessageHandler handler, User currentUser) {
        super("Messages - " + currentUser.getName());
        this.handler = handler;
        this.currentUser = currentUser;

        initializeUI();
        setupComponentNames(); // Add this line
        loadConversations();

        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Set component names for testing purposes.
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

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Left panel - conversations
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Conversations"), BorderLayout.NORTH);

        conversationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conversationList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedConversation();
            }
        });

        leftPanel.add(new JScrollPane(conversationList), BorderLayout.CENTER);

        // Conversation control buttons
        JPanel convButtonPanel = new JPanel(new FlowLayout());
        convButtonPanel.add(newConversationBtn);
        convButtonPanel.add(refreshButton);
        leftPanel.add(convButtonPanel, BorderLayout.SOUTH);

        // Right panel - chat area
        JPanel rightPanel = new JPanel(new BorderLayout());

        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        rightPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Message input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(messageField);
        inputPanel.add(sendButton);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        // Main layout
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Back button at bottom
        JPanel backPanel = new JPanel(new FlowLayout());
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);

        // Event listeners
        sendButton.addActionListener(e -> sendMessage());
        newConversationBtn.addActionListener(e -> startNewConversation());
        refreshButton.addActionListener(e -> loadConversations());
        backButton.addActionListener(e -> dispose());

        messageField.addActionListener(e -> sendMessage()); // Send on Enter
    }

    private void loadConversations() {
        conversationListModel.clear();

        List<String> conversations;
        if (currentUser instanceof Student) {
            conversations = handler.getTutorsMessaged(currentUser.getEmail());
        } else {
            conversations = handler.getStudentsMessaged(currentUser.getEmail());
        }

        if (conversations.isEmpty()) {
            conversationListModel.addElement("Click 'New Conversation' to start messaging");
        } else {
            for (String email : conversations) {
                conversationListModel.addElement(email);
            }
        }
    }

    private void loadSelectedConversation() {
        int selectedIndex = conversationList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String selected = conversationListModel.getElementAt(selectedIndex);
            if (!selected.startsWith("Click")) { // Not the placeholder text
                selectedConversationEmail = selected;
                loadMessagesForConversation(selected);
            }
        }
    }

    private void loadMessagesForConversation(String otherUserEmail) {
        List<Message> messages = handler.getMessageHistory(currentUser.getEmail(), otherUserEmail);

        chatArea.setText("");
        for (Message msg : messages) {
            String timestamp = msg.getTimeSent().format(MESSAGE_TIME_FORMATTER);
            String sender = msg.getSenderEmail().equals(currentUser.getEmail()) ? "You" : msg.getSenderEmail();
            chatArea.append(String.format("[%s] %s: %s\n", timestamp, sender, msg.getMessage()));
        }

        // Scroll to bottom
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void startNewConversation() {
        String otherUserType = (currentUser instanceof Student) ? "tutor" : "student";
        String email = JOptionPane.showInputDialog(this,
                "Enter " + otherUserType + " email to start conversation:");

        if (email != null && !email.trim().isEmpty()) {
            selectedConversationEmail = email.trim();
            loadMessagesForConversation(selectedConversationEmail);

            // Add to conversation list if not already present
            boolean found = false;
            for (int i = 0; i < conversationListModel.size(); i++) {
                if (conversationListModel.getElementAt(i).equals(email.trim())) {
                    found = true;
                    conversationList.setSelectedIndex(i);
                    break;
                }
            }

            if (!found) {
                // Remove placeholder text if present
                if (conversationListModel.size() == 1 &&
                        conversationListModel.getElementAt(0).startsWith("Click")) {
                    conversationListModel.clear();
                }
                conversationListModel.addElement(email.trim());
                conversationList.setSelectedIndex(conversationListModel.size() - 1);
            }
        }
    }

    private void sendMessage() {
        String messageText = messageField.getText().trim();

        if (messageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a message.");
            return;
        }

        if (selectedConversationEmail == null) {
            JOptionPane.showMessageDialog(this, "Please select a conversation first.");
            return;
        }

        Message message = new Message(0, LocalDateTime.now(),
                currentUser.getEmail(), selectedConversationEmail,
                currentUser.getEmail(), messageText);

        handler.sendMessage(message);
        messageField.setText("");
        loadMessagesForConversation(selectedConversationEmail);
        loadConversations(); // Refresh conversation list
    }
}