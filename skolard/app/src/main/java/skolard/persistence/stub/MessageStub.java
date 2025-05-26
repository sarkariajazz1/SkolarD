package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.exceptions.MessageExistsException;
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
        addMessage(new Message(uniqueID++, , ));
        addMessage(new Message(uniqueID++, ));
        addMessage(new Message(uniqueID++, ));
    }

    public Message addMessage(Message message) {
        confirmCreation();
        if(messages.containsKey(message.getMessageId())) {
            throw new MessageExistsException("Replaced Existing Message, that was an update, not an add");
        }

        Message newMessage = new Message(uniqueID++,);
        messages.put(newMessage.getMessageId(), newMessage);
        return newMessage;
    }

    @Override
    public List<Message> getMessageHistory(String studentEmail, String tutorEmail) {
        confirmCreation();
        List<Message> messageList = new ArrayList<Message>();
        for (Message message : messages.values()) {
            messageList.add(message);
        }
        return messageList;
    }

    @Override
    public Message getMessageById(int id) {
        confirmCreation();
        return messages.get(id);
    }

    @Override
    public void deleteMessageById(int id) {
        confirmCreation();
        if(messages.containsKey(id)) {
            messages.remove(id);
        } else {
            throw new MessageExistsException("How did you get here? You cannot delete a Message that doesn't exist");
        }
    }

    @Override
    public void updateMessage(Message updatedMessage) {
        confirmCreation();
        if(!messages.containsKey(updatedMessage.getMessageId())) {
            throw new MessageExistsException("Cannot update a Message that does not exist.");
        } 
        messages.replace(updatedMessage.getMessageId(), updatedMessage);
    }

    @Override
    public void deleteMessageHistory(String studentEmail, String tutorEmail) {
        this.messages = new HashMap<>();
    }

    public void close() {
        this.messages = null;
    }

}
