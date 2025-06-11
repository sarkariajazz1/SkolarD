package skolard.persistence;

import java.util.List;

import skolard.objects.Message;

/**
 * Interface defining methods for managing message data between students and tutors.
 */
public interface MessagePersistence {
    
    /**
     * Retrieves the full message history between a student and a tutor.
     * @param studentEmail the student's email
     * @param tutorEmail the tutor's email
     * @return a list of messages exchanged between the student and tutor
     */
    List<Message> getMessageHistory(String studentEmail, String tutorEmail);
    
    /**
     * Retrieves a list of tutor emails that a student has messaged.
     * @param studentEmail the student's email
     * @return a list of tutor emails
     */
    List<String> getTutorsMessaged(String studentEmail);
    
    /**
     * Retrieves a list of student emails that a tutor has messaged.
     * @param tutorEmail the tutor's email
     * @return a list of student emails
     */
    List<String> getStudentsMessaged(String tutorEmail);
    
    /**
     * Adds a new message record to the database.
     * @param message the message object to add
     * @return the added message, potentially with an assigned ID
     */
    Message addMessage(Message message);
    
    /**
     * Deletes a message by its unique identifier.
     * @param id the message ID to delete
     */
    void deleteMessageById(int id);
    
    /**
     * Updates the details of an existing message.
     * @param updatedMessage the message object with updated information
     */
    void updateMessage(Message updatedMessage);
    
    /**
     * Deletes the entire message history between a student and a tutor.
     * @param studentEmail the student's email
     * @param tutorEmail the tutor's email
     */
    void deleteMessageHistory(String studentEmail, String tutorEmail);
}
