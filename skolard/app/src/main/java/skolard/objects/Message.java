package skolard.objects;

import java.time.LocalDateTime;

public class Message {
    private int messageId;
    private LocalDateTime timeSent;
    private String studentEmail;
    private String tutorEmail;
    private String senderEmail;
    private String message;

    public Message(int messageId, LocalDateTime timeSent, String studentEmail, String tutorEmail, 
        String senderEmail, String message) {
        this.messageId = messageId; 
        this.timeSent = timeSent;
        this.studentEmail = studentEmail;
        this.tutorEmail = tutorEmail;
        this.senderEmail = senderEmail;
        this.message = message;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }


    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public String getMessage() {
        return message;
    }

    public void editMessage(String message) {
        this.message = message;
    }
}
