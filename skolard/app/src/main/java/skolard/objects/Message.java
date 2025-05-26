package skolard.objects;

import java.time.LocalDateTime;

public class Message {
    private final String senderEmail;
    private final String content;
    private final LocalDateTime timestamp;

    public Message(String senderEmail, String content) {
        this.senderEmail = senderEmail;
        this.content = content;
        this.timestamp = LocalDateTime.now(); // Auto timestamp on creation
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + senderEmail + ": " + content;
    }
}