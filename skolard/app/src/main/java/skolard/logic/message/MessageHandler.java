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
 * Acts as a bridge between the UI/business logic and the persistence layer.
 */
public class MessageHandler {

    // Reference to the persistence layer responsible for storing/retrieving messages
    private final MessagePersistence messageDb;

    /**
     * Constructor for injecting a custom MessagePersistence (useful for mocking in tests).
     *
     * @param persistence the injected message persistence implementation
     */
    public MessageHandler(MessagePersistence persistence) {
        this.messageDb = persistence;
    }

    /**
     * Retrieves the full message history between a student and a tutor.
     *
     * @param studentEmail the email address of the student
     * @param tutorEmail the email address of the tutor
     * @return list of Message objects exchanged between them
     * @throws IllegalArgumentException if either email is null
     */
    public List<Message> getMessageHistory(String studentEmail, String tutorEmail) {
        if (studentEmail == null || tutorEmail == null) {
            throw new IllegalArgumentException("Email addresses cannot be null.");
        }
        return messageDb.getMessageHistory(studentEmail, tutorEmail);
    }

    /**
     * Retrieves a list of tutors that a given student has messaged.
     *
     * @param studentEmail the email address of the student
     * @return list of tutor email addresses
     * @throws IllegalArgumentException if the student email is null
     */
    public List<String> getTutorsMessaged(String studentEmail) {
        if (studentEmail == null) {
            throw new IllegalArgumentException("Email address cannot be null.");
        }
        return messageDb.getTutorsMessaged(studentEmail);
    }

    /**
     * Retrieves a list of students that a given tutor has messaged.
     *
     * @param tutorEmail the email address of the tutor
     * @return list of student email addresses
     * @throws IllegalArgumentException if the tutor email is null
     */
    public List<String> getStudentsMessaged(String tutorEmail) {
        if (tutorEmail == null) {
            throw new IllegalArgumentException("Email address cannot be null.");
        }
        return messageDb.getStudentsMessaged(tutorEmail);
    }

    /**
     * Retrieves all unique conversation partners for a given user.
     * Attempts to check both student and tutor roles for completeness.
     *
     * @param userEmail the email of the user
     * @return list of email addresses of all users they've messaged with
     * @throws IllegalArgumentException if the user email is null
     */
    public List<String> getAllConversationPartners(String userEmail) {
        if (userEmail == null) {
            throw new IllegalArgumentException("Email address cannot be null.");
        }

        Set<String> partners = new HashSet<>();

        // Attempt to retrieve as student
        try {
            List<String> tutors = getTutorsMessaged(userEmail);
            partners.addAll(tutors);
        } catch (Exception e) {
            // User might not be a student — ignore and continue
        }

        // Attempt to retrieve as tutor
        try {
            List<String> students = getStudentsMessaged(userEmail);
            partners.addAll(students);
        } catch (Exception e) {
            // User might not be a tutor — ignore and continue
        }

        return new ArrayList<>(partners); // Return list version of the unique set
    }

    /**
     * Retrieves the most recent message exchanged between two users.
     * Tries both directions (A → B and B → A) to support flexible sender/receiver roles.
     *
     * @param userEmail1 email of one user
     * @param userEmail2 email of the other user
     * @return the latest Message exchanged, or null if none found
     * @throws IllegalArgumentException if either email is null
     */
    public Message getMostRecentMessage(String userEmail1, String userEmail2) {
        if (userEmail1 == null || userEmail2 == null) {
            throw new IllegalArgumentException("Email addresses cannot be null.");
        }

        List<Message> messages;

        // Attempt to fetch history assuming userEmail1 is the student
        try {
            messages = getMessageHistory(userEmail1, userEmail2);
        } catch (Exception e) {
            // Try the reverse direction
            try {
                messages = getMessageHistory(userEmail2, userEmail1);
            } catch (Exception e2) {
                return null; // No valid conversation found
            }
        }

        // If no messages exist, return null
        if (messages.isEmpty()) {
            return null;
        }

        // Return the last message (assuming messages are chronologically ordered)
        return messages.get(messages.size() - 1);
    }

    /**
     * Sends a new message after validating its contents.
     *
     * @param message the Message object to send
     * @return the saved Message object
     * @throws IllegalArgumentException if the message is null or fails validation
     */
    public Message sendMessage(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }
        if (!MessageUtil.validMessage(message)) {
            throw new IllegalArgumentException("Message is invalid");
        }
        return messageDb.addMessage(message);
    }

    /**
     * Updates an existing message in the database.
     *
     * @param updatedMessage the updated Message object
     * @throws IllegalArgumentException if the updated message is null
     */
    public void updateMessage(Message updatedMessage) {
        if (updatedMessage == null) {
            throw new IllegalArgumentException("Updated message cannot be null.");
        }
        messageDb.updateMessage(updatedMessage);
    }

    /**
     * Deletes a message by its unique ID.
     *
     * @param id the ID of the message to delete
     */
    public void deleteMessageById(int id) {
        messageDb.deleteMessageById(id);
    }

    /**
     * Deletes the entire message history between a student and a tutor.
     *
     * @param studentEmail the student's email
     * @param tutorEmail the tutor's email
     * @throws IllegalArgumentException if either email is null
     */
    public void deleteMessageHistory(String studentEmail, String tutorEmail) {
        if (studentEmail == null || tutorEmail == null) {
            throw new IllegalArgumentException("Email addresses cannot be null.");
        }
        messageDb.deleteMessageHistory(studentEmail, tutorEmail);
    }
}
