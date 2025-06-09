package skolard.persistence;

import java.util.List;

import skolard.objects.Message;

public interface MessagePersistence {
    List<Message> getMessageHistory(String studentEmail, String tutorEmail);
    List<String> getTutorsMessaged(String studentEmail);
    List<String> getStudentsMessaged(String tutorEmail);
    Message addMessage(Message message);
    void deleteMessageById(int id);
    void updateMessage(Message updatedMessage);
    void deleteMessageHistory(String studentEmail, String tutorEmail);
}
