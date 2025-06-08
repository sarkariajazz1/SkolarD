package skolard.persistence.stub;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import skolard.objects.Message;
import skolard.persistence.MessagePersistence;

public class MessageStub implements MessagePersistence{
    private Map<Integer, Message> messages;
    private static int uniqueID = 0;

    public MessageStub() {
        confirmCreation();

        // Sample Messages for testing
        addSampleMessages();
    }

    private void confirmCreation() {
        if(messages == null) {
            messages = new HashMap<>();
        }
    }

    private void addSampleMessages() {
        addMessage(new Message(uniqueID++, LocalDateTime.of(2025, 5, 26, 11, 30, 0), 
            "yabm@myumanitoba.ca", "mattyab@myumanitoba.ca", "yabm@myumanitoba.ca", "Hello"));
        addMessage(new Message(uniqueID++, LocalDateTime.of(2025, 5, 26, 11, 32, 0), 
            "yabm@myumanitoba.ca", "mattyab@myumanitoba.ca", "mattyab@myumanitoba.ca", "Hi!"));
        addMessage(new Message(uniqueID++, LocalDateTime.of(2025, 5, 26, 11, 35, 0), 
            "yabm@myumanitoba.ca", "mattyab@myumanitoba.ca", "yabm@myumanitoba.ca", "Can you tutor me?"));
    }

    private boolean messageHistory(Message message, String studentEmail, String tutorEmail) {
        return (message.getStudentEmail() == studentEmail && message.getTutorEmail() == tutorEmail);
    }

    public Message addMessage(Message message) {
        confirmCreation();
        if(messages.containsKey(message.getMessageId())) {
            throw new RuntimeException("Updated Existing Message, that was an update, not an add");
        }

        Message newMessage = new Message(uniqueID++, message.getTimeSent(),message.getStudentEmail(),
            message.getTutorEmail(), message.getSenderEmail(), message.getMessage());
        messages.put(newMessage.getMessageId(), newMessage);
        return newMessage;
    }

    @Override
    public List<Message> getMessageHistory(String studentEmail, String tutorEmail) {
        confirmCreation();
        List<Message> messageList = new ArrayList<Message>();
        for(Message message : messages.values()) {
            if(messageHistory(message, studentEmail, tutorEmail)) {
                messageList.add(message);
            }
        }
        return messageList;
    }

    @Override
    public List<String> getTutorsMessaged(String studentEmail) {
        confirmCreation();
        List<String> tutors = new ArrayList<>();
        Set<String> uniqueTutors = new HashSet<>(tutors);

        for(Message message : messages.values()) {
            if(message.getStudentEmail().equalsIgnoreCase(studentEmail)) {
                if(uniqueTutors.add(message.getTutorEmail())) {
                    tutors.add(message.getTutorEmail());
                }
            }
        }
        return tutors;
    }

    @Override
    public List<String> getStudentsMessaged(String tutorEmail) {
        confirmCreation();
        List<String> students = new ArrayList<>();
        Set<String> uniqueStudents = new HashSet<>(students);

        for(Message message : messages.values()) {
            if(message.getTutorEmail().equalsIgnoreCase(tutorEmail)) {
                if(uniqueStudents.add(message.getStudentEmail())) {
                    students.add(message.getStudentEmail());
                }
            }
        }
        return students;
    }

    @Override
    public void deleteMessageById(int id) {
        confirmCreation();
        if(messages.containsKey(id)) {
            messages.remove(id);
        } else {
            throw new RuntimeException("Cannot delete a Message that doesn't exist");
        }
    }

    @Override
    public void updateMessage(Message updatedMessage) {
        confirmCreation();
        if(!messages.containsKey(updatedMessage.getMessageId())) {
            throw new RuntimeException("Cannot update a Message that does not exist.");
        } 
        messages.replace(updatedMessage.getMessageId(), updatedMessage);
    }

    
    @Override
    public void deleteMessageHistory(String studentEmail, String tutorEmail) {
        confirmCreation();
        List<Integer> idsToDelete = new ArrayList<>();
        for (Message message : messages.values()) {
            if (messageHistory(message, studentEmail, tutorEmail)) {
                idsToDelete.add(message.getMessageId());
            }
        }
        for (int id : idsToDelete) {
            messages.remove(id);
        }
    }
}
