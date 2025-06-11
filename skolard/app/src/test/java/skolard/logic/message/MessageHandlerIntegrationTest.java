package skolard.logic.message;

import org.junit.jupiter.api.*;
import skolard.objects.Message;
import skolard.persistence.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageHandlerIntegrationTest {

    private Connection conn;
    private MessageHandler messageHandler;
    private MessagePersistence messagePersistence;

    private final String studentEmail = "alice@example.com";
    private final String tutorEmail = "bob@example.com";

    @BeforeEach
    void setup() throws Exception {
        conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, false);
        PersistenceProvider.initializeSqlite(conn);
        messagePersistence = PersistenceRegistry.getMessagePersistence();
        messageHandler = new MessageHandler(messagePersistence);
    }

    @Test
    void testSendMessageAndRetrieveHistory() {
        Message message = new Message(
            0,
            LocalDateTime.now(),
            studentEmail,
            tutorEmail,
            studentEmail,
            "Hello Tutor!"
        );

        Message sent = messageHandler.sendMessage(message);
        assertNotNull(sent);
        assertTrue(sent.getMessageId() > 0);

        List<Message> history = messageHandler.getMessageHistory(studentEmail, tutorEmail);
        assertFalse(history.isEmpty());
        assertEquals("Hello Tutor!", history.get(history.size() - 1).getMessage());
    }

    @Test
    void testGetMostRecentMessage() {
        Message recent = new Message(
            0,
            LocalDateTime.now(),
            studentEmail,
            tutorEmail,
            tutorEmail,
            "Latest reply!"
        );
        messageHandler.sendMessage(recent);

        Message last = messageHandler.getMostRecentMessage(studentEmail, tutorEmail);
        assertNotNull(last);
        assertEquals("Latest reply!", last.getMessage());
    }

    @Test
    void testGetConversationPartners() {
        List<String> partners = messageHandler.getAllConversationPartners(studentEmail);
        assertTrue(partners.contains(tutorEmail));

        partners = messageHandler.getAllConversationPartners(tutorEmail);
        assertTrue(partners.contains(studentEmail));
    }

    @Test
    void testUpdateMessage() {
        Message original = new Message(
            0,
            LocalDateTime.now(),
            studentEmail,
            tutorEmail,
            studentEmail,
            "Edit me"
        );
        Message saved = messageHandler.sendMessage(original);

        saved.editMessage("Edited message");
        messageHandler.updateMessage(saved);

        List<Message> history = messageHandler.getMessageHistory(studentEmail, tutorEmail);
        boolean match = history.stream().anyMatch(m -> m.getMessage().equals("Edited message"));
        assertTrue(match);
    }

    @Test
    void testDeleteMessageById() {
        Message toDelete = new Message(
            0,
            LocalDateTime.now(),
            studentEmail,
            tutorEmail,
            studentEmail,
            "To be deleted"
        );
        Message saved = messageHandler.sendMessage(toDelete);
        messageHandler.deleteMessageById(saved.getMessageId());

        List<Message> history = messageHandler.getMessageHistory(studentEmail, tutorEmail);
        boolean exists = history.stream().anyMatch(m -> m.getMessageId() == saved.getMessageId());
        assertFalse(exists);
    }

    @Test
    void testDeleteMessageHistory() {
        messageHandler.sendMessage(new Message(
            0, LocalDateTime.now(), studentEmail, tutorEmail, studentEmail, "Hi again"));
        messageHandler.sendMessage(new Message(
            0, LocalDateTime.now(), studentEmail, tutorEmail, tutorEmail, "Hello again"));

        messageHandler.deleteMessageHistory(studentEmail, tutorEmail);

        List<Message> history = messageHandler.getMessageHistory(studentEmail, tutorEmail);
        assertTrue(history.isEmpty());
    }

    @AfterEach
    void cleanup() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}
