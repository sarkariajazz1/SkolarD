package skolard.objects;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final String tutorEmail;
    private final String studentEmail;
    private final List<Message> messages;

    public Chat(String tutorEmail, String studentEmail) {
        this.tutorEmail = tutorEmail;
        this.studentEmail = studentEmail;
        this.messages = new ArrayList<>();
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getChatId() {
        return tutorEmail + "_" + studentEmail;
    }

    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        for (Message m : messages) {
            sb.append(m.toString()).append("\n");
        }
        return sb.toString();
    }
}