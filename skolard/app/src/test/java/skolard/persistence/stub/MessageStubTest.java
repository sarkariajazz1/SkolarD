package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Message;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageStubTest {

    private MessageStub messageStub;

    @BeforeEach
    void setUp() {
        messageStub = new MessageStub();
    }

    @Test
    void testAddMessage() {
        Message newMsg = new Message(999, LocalDateTime.now(), "new@student.com", "new@tutor.com", "new@student.com", "Hello Tutor!");
        Message added = messageStub.addMessage(newMsg);

        assertNotNull(added);
        assertNotEquals(999, added.getMessageId()); // ID should be auto-assigned
        assertEquals("Hello Tutor!", added.getMessage());
    }

    @Test
    void testGetMessageHistory() {
        List<Message> history = messageStub.getMessageHistory("yabm@myumanitoba.ca", "mattyab@myumanitoba.ca");
        assertEquals(3, history.size());
    }

    @Test
    void testGetTutorsMessaged() {
        List<String> tutors = messageStub.getTutorsMessaged("yabm@myumanitoba.ca");
        assertEquals(1, tutors.size());
        assertTrue(tutors.contains("mattyab@myumanitoba.ca"));
    }

    @Test
    void testGetStudentsMessaged() {
        List<String> students = messageStub.getStudentsMessaged("mattyab@myumanitoba.ca");
        assertEquals(1, students.size());
        assertTrue(students.contains("yabm@myumanitoba.ca"));
    }

    @Test
    void testDeleteMessageById() {
        Message msg = new Message(999, LocalDateTime.now(), "a@b.com", "c@d.com", "a@b.com", "To be deleted");
        Message added = messageStub.addMessage(msg);

        assertDoesNotThrow(() -> messageStub.deleteMessageById(added.getMessageId()));
    }

    @Test
    void testDeleteMessageByIdThrows() {
        assertThrows(RuntimeException.class, () -> messageStub.deleteMessageById(9999));
    }

    @Test
    void testUpdateMessage() {
        Message original = new Message(999, LocalDateTime.now(), "a@b.com", "c@d.com", "a@b.com", "Original");
        Message added = messageStub.addMessage(original);

        Message updated = new Message(added.getMessageId(), added.getTimeSent(), "a@b.com", "c@d.com", "a@b.com", "Updated");
        messageStub.updateMessage(updated);

        List<Message> history = messageStub.getMessageHistory("a@b.com", "c@d.com");
        assertEquals("Updated", history.get(0).getMessage());
    }

    @Test
    void testUpdateMessageThrows() {
        Message fake = new Message(9999, LocalDateTime.now(), "x@y.com", "z@w.com", "x@y.com", "Fake");
        assertThrows(RuntimeException.class, () -> messageStub.updateMessage(fake));
    }

    @Test
    void testDeleteMessageHistory() {
        messageStub.deleteMessageHistory("yabm@myumanitoba.ca", "mattyab@myumanitoba.ca");
        List<Message> history = messageStub.getMessageHistory("yabm@myumanitoba.ca", "mattyab@myumanitoba.ca");
        assertTrue(history.isEmpty());
    }

    @Test
    void testAddMessageWithExistingIdThrows() {
        Message msg = new Message(999, LocalDateTime.now(), "a@b.com", "c@d.com", "a@b.com", "First");
        Message added = messageStub.addMessage(msg);

        // Try to add the same message again with the same ID
        Message duplicate = new Message(added.getMessageId(), added.getTimeSent(), "a@b.com", "c@d.com", "a@b.com", "Duplicate");
        assertThrows(RuntimeException.class, () -> messageStub.addMessage(duplicate));
    }

    @Test
    void testGetMessageHistoryEmpty() {
        List<Message> history = messageStub.getMessageHistory("nonexistent@student.com", "nonexistent@tutor.com");
        assertTrue(history.isEmpty());
    }

    @Test
    void testGetTutorsMessagedEmpty() {
        List<String> tutors = messageStub.getTutorsMessaged("ghost@student.com");
        assertTrue(tutors.isEmpty());
    }

    @Test
    void testGetStudentsMessagedEmpty() {
        List<String> students = messageStub.getStudentsMessaged("ghost@tutor.com");
        assertTrue(students.isEmpty());
    } 
}
