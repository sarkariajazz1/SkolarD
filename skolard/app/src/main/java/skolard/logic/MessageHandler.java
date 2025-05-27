package skolard.logic;

import java.util.List;

import skolard.objects.Message;
import skolard.persistence.MessagePersistence;
import skolard.persistence.PersistenceFactory;

/**
 * Handles logic related to sending, retrieving, and managing messages
 * between students and tutors.
 */
public class MessageHandler {

    private final MessagePersistence messageDb;

    /**
     * Constructs a MessageHandler using the default persistence implementation.
     */
    public MessageHandler() {
        this.messageDb = PersistenceFactory.getMessagePersistence();
    }

    /**
     * Constructs a MessageHandler with a custom MessagePersistence (useful for testing).
     *
     * @param persistence the injected message persistence implementation
     */
    public MessageHandler(MessagePersistence persistence) {
        this.messageDb = persistence;
    }

    /**
     * Retrieves the full message history between a student and a tutor.
     *
     * @param studentEmail the student's email
     * @param tutorEmail   the tutor's email
     * @return list of messages in chronological order
     */
    public List<Message> getMessageHistory(String studentEmail, String tutorEmail) {
        if (studentEmail == null || tutorEmail == null) {
            throw new IllegalArgumentException("Email addresses cannot be null.");
        }
        return messageDb.getMessageHistory(studentEmail, tutorEmail);
    }

    /**
     * Sends a new message.
     *
     * @param message the message to be added
     * @return the persisted message with assigned ID
     */
    public Message sendMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }
        return messageDb.addMessage(message);
    }

    /**
     * Updates an existing message’s content.
     *
     * @param updatedMessage the message with new content
     */
    public void updateMessage(Message updatedMessage) {
        if (updatedMessage == null) {
            throw new IllegalArgumentException("Updated message cannot be null.");
        }
        messageDb.updateMessage(updatedMessage);
    }

    /**
     * Deletes a single message by its unique ID.
     *
     * @param id the ID of the message to delete
     */
    public void deleteMessageById(int id) {
        messageDb.deleteMessageById(id);
    }

    /**
     * Deletes the full conversation history between a student and tutor.
     *
     * @param studentEmail the student's email
     * @param tutorEmail   the tutor's email
     */
    public void deleteMessageHistory(String studentEmail, String tutorEmail) {
        if (studentEmail == null || tutorEmail == null) {
            throw new IllegalArgumentException("Email addresses cannot be null.");
        }
        messageDb.deleteMessageHistory(studentEmail, tutorEmail);
    }
}

