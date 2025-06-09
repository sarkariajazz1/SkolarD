
package skolard.logic.message;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

import skolard.objects.Message;
import skolard.persistence.MessagePersistence;
import skolard.utils.MessageUtil;

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

    /**
     * Gets all conversation partners for a user (both students and tutors they've messaged)
     * @param userEmail the email of the current user
     * @return list of email addresses of conversation partners
     */
    public List<String> getAllConversationPartners(String userEmail) {
        if (userEmail == null) {
            throw new IllegalArgumentException("Email address cannot be null.");
        }

        Set<String> partners = new HashSet<>();

        // Try as student first
        try {
            List<String> tutors = getTutorsMessaged(userEmail);
            partners.addAll(tutors);
        } catch (Exception e) {
            // User might not be a student, continue
        }

        // Try as tutor
        try {
            List<String> students = getStudentsMessaged(userEmail);
            partners.addAll(students);
        } catch (Exception e) {
            // User might not be a tutor, continue
        }

        return new ArrayList<>(partners);
    }

    /**
     * Gets the most recent message between two users
     * @param userEmail1 first user's email
     * @param userEmail2 second user's email
     * @return the most recent message, or null if no messages exist
     */
    public Message getMostRecentMessage(String userEmail1, String userEmail2) {
        if (userEmail1 == null || userEmail2 == null) {
            throw new IllegalArgumentException("Email addresses cannot be null.");
        }

        // Determine which is student and which is tutor
        List<Message> messages;
        try {
            messages = getMessageHistory(userEmail1, userEmail2);
        } catch (Exception e) {
            try {
                messages = getMessageHistory(userEmail2, userEmail1);
            } catch (Exception e2) {
                return null;
            }
        }

        if (messages.isEmpty()) {
            return null;
        }

        // Return the most recent message (assuming messages are sorted by time)
        return messages.get(messages.size() - 1);
    }

    public Message sendMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }
        if (!MessageUtil.validMessage(message)) {
            throw new IllegalArgumentException("Message is invalid");
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