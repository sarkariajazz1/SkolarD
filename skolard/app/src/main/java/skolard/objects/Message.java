package skolard.objects;

import java.time.LocalDateTime;

/**
 * Represents a message exchanged between a student and a tutor in the SkolarD system.
 * Contains sender and recipient information, content, timestamp, and a unique ID.
 */
public class Message {
    // Unique identifier for the message
    private final int messageId;

    // Timestamp of when the message was sent
    private final LocalDateTime timeSent;

    // Email of the student involved in the conversation
    private final String studentEmail;

    // Email of the tutor involved in the conversation
    private final String tutorEmail;

    // Email of the sender (either student or tutor)
    private final String senderEmail;

    // Content of the message
    private String message;

    /**
     * Constructs a new Message with full details.
     *
     * @param messageId    the unique ID of the message
     * @param timeSent     the time the message was sent
     * @param studentEmail the student's email
     * @param tutorEmail   the tutor's email
     * @param senderEmail  the sender's email
     * @param message      the message content
     */
    public Message(int messageId, LocalDateTime timeSent, String studentEmail, String tutorEmail, 
        String senderEmail, String message) {
        this.messageId = messageId; 
        this.timeSent = timeSent;
        this.studentEmail = studentEmail;
        this.tutorEmail = tutorEmail;
        this.senderEmail = senderEmail;
        this.message = message;
    }

    /**
     * @return the unique message ID
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * @return the email of the student involved
     */
    public String getStudentEmail() {
        return studentEmail;
    }

    /**
     * @return the email of the tutor involved
     */
    public String getTutorEmail() {
        return tutorEmail;
    }

    /**
     * @return the email of the sender
     */
    public String getSenderEmail() {
        return senderEmail;
    }

    /**
     * @return the time the message was sent
     */
    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    /**
     * @return the message content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Edits the message content.
     *
     * @param message the new message content
     */
    public void editMessage(String message) {
        this.message = message;
    }
}
