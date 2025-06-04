package skolard.logic.message;

import java.util.List;

import skolard.objects.Message;
import skolard.persistence.MessagePersistence;

/**
 * Handles logic related to sending, retrieving, and managing messages
 * between students and tutors.
 */
public class MessageHandler {

    private final MessagePersistence messageDb;

    /**
     * Constructor for injecting a custom MessagePersistence (for testing).
     *
     * @param persistence the injected message persistence implementation
     */
    public MessageHandler(MessagePersistence persistence) {
        this.messageDb = persistence;
    }

    public List<Message> getMessageHistory(String studentEmail, String tutorEmail) {
        if (studentEmail == null || tutorEmail == null) {
            throw new IllegalArgumentException("Email addresses cannot be null.");
        }
        return messageDb.getMessageHistory(studentEmail, tutorEmail);
    }

    public List<String> getTutorsMessaged(String studentEmail) {
        if (studentEmail == null) {
            throw new IllegalArgumentException("Email address cannot be null.");
        }
        return messageDb.getTutorsMessaged(studentEmail);
    }

    public List<String> getStudentsMessaged(String tutorEmail) {
        if (tutorEmail == null) {
            throw new IllegalArgumentException("Email address cannot be null.");
        }
        return messageDb.getStudentsMessaged(tutorEmail);
    }

    public Message sendMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }
        return messageDb.addMessage(message);
    }

    public void updateMessage(Message updatedMessage) {
        if (updatedMessage == null) {
            throw new IllegalArgumentException("Updated message cannot be null.");
        }
        messageDb.updateMessage(updatedMessage);
    }

    public void deleteMessageById(int id) {
        messageDb.deleteMessageById(id);
    }

    public void deleteMessageHistory(String studentEmail, String tutorEmail) {
        if (studentEmail == null || tutorEmail == null) {
            throw new IllegalArgumentException("Email addresses cannot be null.");
        }
        messageDb.deleteMessageHistory(studentEmail, tutorEmail);
    }
}
