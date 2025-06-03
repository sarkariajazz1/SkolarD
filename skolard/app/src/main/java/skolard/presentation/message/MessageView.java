package skolard.presentation.message;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import skolard.logic.message.MessageHandler;
import skolard.objects.Message;

/**
 * GUI for viewing and sending messages between a student and a tutor.
 */
public class MessageView extends JFrame {
    private final MessageHandler handler;

    private final JTextField studentEmailField = new JTextField(20);
    private final JTextField tutorEmailField = new JTextField(20);
    private final JTextArea chatArea = new JTextArea(15, 40);
    private final JTextField messageField = new JTextField(30);
    private final JButton sendButton = new JButton("Send");
    private final JButton refreshButton = new JButton("Refresh");

    /**
     * Constructs the MessageView UI with a MessageHandler.
     *
     * @param handler the logic handler used for message operations
     */
    public MessageView(MessageHandler handler) {
        super("SkolarD - Message View");
        this.handler = handler;

        setLayout(new BorderLayout(10, 10));

        // Top panel for emails
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Student Email:"));
        topPanel.add(studentEmailField);
        topPanel.add(new JLabel("Tutor Email:"));
        topPanel.add(tutorEmailField);
        topPanel.add(refreshButton);
        add(topPanel, BorderLayout.NORTH);

        // Center chat display
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Bottom panel for message input
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(new JLabel("Message:"));
        bottomPanel.add(messageField);
        bottomPanel.add(sendButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadMessages());
        sendButton.addActionListener(e -> sendMessage());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Loads and displays message history for the given emails.
     */
    private void loadMessages() {
        chatArea.setText("");
        String student = studentEmailField.getText().trim();
        String tutor = tutorEmailField.getText().trim();

        if (!student.isEmpty() && !tutor.isEmpty()) {
            List<Message> history = handler.getMessageHistory(student, tutor);
            for (Message m : history) {
                chatArea.append(m.getTimeSent() + " | " + m.getSenderEmail() + ": " + m.getMessage() + "\n");
            }
        }
    }

    /**
     * Sends a new message and updates the chat area.
     */
    private void sendMessage() {
        String student = studentEmailField.getText().trim();
        String tutor = tutorEmailField.getText().trim();
        String content = messageField.getText().trim();

        if (student.isEmpty() || tutor.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        Message msg = new Message(student, tutor, LocalDateTime.now(), content);
        handler.sendMessage(msg);
        messageField.setText("");
        loadMessages();
    }
}
