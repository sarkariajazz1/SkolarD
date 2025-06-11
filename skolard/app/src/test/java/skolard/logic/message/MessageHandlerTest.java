package skolard.logic.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Message;
import skolard.persistence.MessagePersistence;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MessageHandlerTest {

    private MessagePersistence mockDb;
    private MessageHandler handler;

    private final String studentEmail = "student@skolard.ca";
    private final String tutorEmail = "tutor@skolard.ca";

    @BeforeEach
    void setup() {
        mockDb = mock(MessagePersistence.class);
        handler = new MessageHandler(mockDb);
    }

    @Test
    void testConstructorWithValidPersistence() {
        assertNotNull(new MessageHandler(mockDb));
    }

    @Test
    void testGetMessageHistory() {
        List<Message> expected = List.of(dummyMessage());
        when(mockDb.getMessageHistory(studentEmail, tutorEmail)).thenReturn(expected);

        List<Message> result = handler.getMessageHistory(studentEmail, tutorEmail);
        assertEquals(1, result.size());
    }

    @Test
    void testGetMessageHistory_NullInputs() {
        assertThrows(IllegalArgumentException.class, () -> handler.getMessageHistory(null, tutorEmail));
        assertThrows(IllegalArgumentException.class, () -> handler.getMessageHistory(studentEmail, null));
    }

    @Test
    void testGetTutorsMessaged() {
        when(mockDb.getTutorsMessaged(studentEmail)).thenReturn(List.of(tutorEmail));
        List<String> result = handler.getTutorsMessaged(studentEmail);
        assertTrue(result.contains(tutorEmail));
    }

    @Test
    void testGetTutorsMessaged_NullInput() {
        assertThrows(IllegalArgumentException.class, () -> handler.getTutorsMessaged(null));
    }

    @Test
    void testGetStudentsMessaged() {
        when(mockDb.getStudentsMessaged(tutorEmail)).thenReturn(List.of(studentEmail));
        List<String> result = handler.getStudentsMessaged(tutorEmail);
        assertTrue(result.contains(studentEmail));
    }

    @Test
    void testGetStudentsMessaged_NullInput() {
        assertThrows(IllegalArgumentException.class, () -> handler.getStudentsMessaged(null));
    }

    @Test
    void testGetAllConversationPartners_StudentAndTutor() {
        when(mockDb.getTutorsMessaged(studentEmail)).thenReturn(List.of(tutorEmail));
        when(mockDb.getStudentsMessaged(studentEmail)).thenReturn(List.of("other@student.ca"));

        List<String> partners = handler.getAllConversationPartners(studentEmail);
        assertTrue(partners.contains(tutorEmail));
        assertTrue(partners.contains("other@student.ca"));
    }

    @Test
    void testGetMostRecentMessage_WithMessages() {
        List<Message> messages = List.of(
                new Message(1, LocalDateTime.now().minusMinutes(10), studentEmail, tutorEmail, studentEmail, "Hi"),
                new Message(2, LocalDateTime.now(), studentEmail, tutorEmail, tutorEmail, "Hey")
        );

        when(mockDb.getMessageHistory(studentEmail, tutorEmail)).thenReturn(messages);

        Message recent = handler.getMostRecentMessage(studentEmail, tutorEmail);
        assertEquals("Hey", recent.getMessage());
    }

    @Test
    void testGetMostRecentMessage_TrySwapOrder() {
        when(mockDb.getMessageHistory(studentEmail, tutorEmail)).thenThrow(new RuntimeException());
        when(mockDb.getMessageHistory(tutorEmail, studentEmail)).thenReturn(List.of(dummyMessage()));

        Message msg = handler.getMostRecentMessage(studentEmail, tutorEmail);
        assertNotNull(msg);
    }

    @Test
    void testGetMostRecentMessage_EmptyList() {
        when(mockDb.getMessageHistory(studentEmail, tutorEmail)).thenReturn(List.of());
        Message result = handler.getMostRecentMessage(studentEmail, tutorEmail);
        assertNull(result);
    }

    @Test
    void testSendMessage_NullOrInvalid() {
        assertThrows(IllegalArgumentException.class, () -> handler.sendMessage(null));

        Message invalid = new Message(0, null, "same@skolard.ca", "same@skolard.ca", "same@skolard.ca", "");
        assertThrows(IllegalArgumentException.class, () -> handler.sendMessage(invalid));
    }

    @Test
    void testSendMessage_Valid() {
        Message valid = dummyMessage();
        when(mockDb.addMessage(valid)).thenReturn(valid);

        Message sent = handler.sendMessage(valid);
        assertEquals("Hello", sent.getMessage());
    }

    @Test
    void testUpdateMessage() {
        assertDoesNotThrow(() -> handler.updateMessage(dummyMessage()));
        verify(mockDb).updateMessage(any());
    }

    @Test
    void testUpdateMessage_Null() {
        assertThrows(IllegalArgumentException.class, () -> handler.updateMessage(null));
    }

    @Test
    void testDeleteMessageById() {
        assertDoesNotThrow(() -> handler.deleteMessageById(123));
        verify(mockDb).deleteMessageById(123);
    }

    @Test
    void testDeleteMessageHistory_NullInput() {
        assertThrows(IllegalArgumentException.class, () -> handler.deleteMessageHistory(null, tutorEmail));
        assertThrows(IllegalArgumentException.class, () -> handler.deleteMessageHistory(studentEmail, null));
    }

    @Test
    void testDeleteMessageHistory_Valid() {
        assertDoesNotThrow(() -> handler.deleteMessageHistory(studentEmail, tutorEmail));
        verify(mockDb).deleteMessageHistory(studentEmail, tutorEmail);
    }

    private Message dummyMessage() {
        return new Message(1, LocalDateTime.now(), studentEmail, tutorEmail, studentEmail, "Hello");
    }
}
