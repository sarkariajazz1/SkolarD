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
        loadConversations();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Left panel - Conversation list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Conversations"), BorderLayout.NORTH);

        conversationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conversationList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedConversation();
            }
        });

        JScrollPane conversationScrollPane = new JScrollPane(conversationList);
        leftPanel.add(conversationScrollPane, BorderLayout.CENTER);

        JPanel leftButtonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        leftButtonPanel.add(newConversationBtn);
        leftButtonPanel.add(refreshButton);
        leftPanel.add(leftButtonPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        // Right panel - Chat area
        JPanel rightPanel = new JPanel(new BorderLayout());

        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        rightPanel.add(chatScrollPane, BorderLayout.CENTER);

        // Message input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);

        // Bottom panel with back button
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event listeners
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        newConversationBtn.addActionListener(e -> startNewConversation());
        refreshButton.addActionListener(e -> {
            loadConversations();
            if (selectedConversationEmail != null) {
                loadMessagesForConversation(selectedConversationEmail);
            }
        });
        backButton.addActionListener(e -> dispose());
    }

    private void loadConversations() {
        conversationListModel.clear();

        // Load conversations based on user type
        if (currentUser instanceof Student) {
            List<String> tutorsMessaged = handler.getTutorsMessaged(currentUser.getEmail());
            for (String tutorEmail : tutorsMessaged) {
                conversationListModel.addElement(tutorEmail);
            }
        } else if (currentUser instanceof Tutor) {
            List<String> studentsMessaged = handler.getStudentsMessaged(currentUser.getEmail());
            for (String studentEmail : studentsMessaged) {
                conversationListModel.addElement(studentEmail);
            }
        }

        if (conversationListModel.isEmpty()) {
            conversationListModel.addElement("Click 'New Conversation' to start messaging");
        }
    }

    private void loadSelectedConversation() {
        int selectedIndex = conversationList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedValue = conversationListModel.getElementAt(selectedIndex);
            if (!selectedValue.startsWith("Click")) {
                selectedConversationEmail = selectedValue;
                loadMessagesForConversation(selectedConversationEmail);
            }
        }
    }

    private void loadMessagesForConversation(String otherUserEmail) {
        chatArea.setText("");

        // Determine which email is student and which is tutor for the getMessageHistory call
        String studentEmail;
        String tutorEmail;

        if (currentUser instanceof Student) {
            studentEmail = currentUser.getEmail();
            tutorEmail = otherUserEmail;
        } else {
            studentEmail = otherUserEmail;
            tutorEmail = currentUser.getEmail();
        }

        List<Message> messages = handler.getMessageHistory(studentEmail, tutorEmail);

        for (Message message : messages) {
            String sender = message.getSenderEmail().equals(currentUser.getEmail()) ? "You" : otherUserEmail;
            String formattedTime = message.getTimeSent().format(MESSAGE_TIME_FORMATTER);
            chatArea.append(String.format("[%s] %s: %s\n", formattedTime, sender, message.getMessage()));

        }

        // Scroll to bottom
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void startNewConversation() {
        String targetEmail = JOptionPane.showInputDialog(this,
                "Enter the email of the person you want to message:");

        if (targetEmail != null && !targetEmail.trim().isEmpty()) {
            selectedConversationEmail = targetEmail.trim();

            // Add to conversation list if not already there
            boolean found = false;
            for (int i = 0; i < conversationListModel.getSize(); i++) {
                if (conversationListModel.getElementAt(i).equals(targetEmail)) {
                    found = true;
                    conversationList.setSelectedIndex(i);
                    break;
                }
            }

            if (!found) {
                // Remove placeholder message if it exists
                if (conversationListModel.getSize() == 1 &&
                        conversationListModel.getElementAt(0).startsWith("Click")) {
                    conversationListModel.clear();
                }
                conversationListModel.addElement(targetEmail);
                conversationList.setSelectedValue(targetEmail, true);
            }

            loadMessagesForConversation(selectedConversationEmail);
        }
    }

    private void sendMessage() {
        if (selectedConversationEmail == null) {
            JOptionPane.showMessageDialog(this, "Please select a conversation first.");
            return;
        }

        String messageText = messageField.getText().trim();
        if (messageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a message.");
            return;
        }

        // Determine which email is student and which is tutor
        String studentEmail;
        String tutorEmail;

        if (currentUser instanceof Student) {
            studentEmail = currentUser.getEmail();
            tutorEmail = selectedConversationEmail;
        } else {
            studentEmail = selectedConversationEmail;
            tutorEmail = currentUser.getEmail();
        }

        // Create Message object with proper parameters
        Message newMessage = new Message(
                0, // messageId will be set by persistence layer
                LocalDateTime.now(),
                studentEmail,
                tutorEmail,
                currentUser.getEmail(),
                messageText
        );

        try {
            Message sentMessage = handler.sendMessage(newMessage);
            if (sentMessage != null) {
                messageField.setText("");
                loadMessagesForConversation(selectedConversationEmail);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to send message. Please try again.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error sending message: " + e.getMessage());
        }
    }
}