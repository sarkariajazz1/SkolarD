package skolard.objects;

import java.time.LocalDateTime;

public class Message {
    private int messageId;
    private LocalDateTime timeSent;
    private String senderEmail;
    private String receiverEmail;
    private String message;

    public Message(int messageId, LocalDateTime timeSent, String senderEmail,
        String receiverEmail, String message) {
        this.messageId = messageId; 
        this.timeSent = timeSent;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.message = message;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
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
