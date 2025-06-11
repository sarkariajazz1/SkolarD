package skolard.persistence.sqlite;

import org.junit.jupiter.api.*;
import skolard.objects.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MessageDBTest {

    private Connection connection;
    private MessageDB messageDB;

    @BeforeAll
    void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SchemaInitializer.initializeSchema(connection);
        messageDB = new MessageDB(connection);
    }

    @BeforeEach
    void clearMessages() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM messages");
        }
    }

    private Message createMessage(String student, String tutor, String sender, String msg) {
        return new Message(0, LocalDateTime.now(), student, tutor, sender, msg);
    }

    @Test
    void testAddAndRetrieveMessage() {
        Message inserted = messageDB.addMessage(createMessage("alice@skolard.ca", "bob@skolard.ca", "alice@skolard.ca", "Hey Bob"));
        assertTrue(inserted.getMessageId() > 0);
        assertEquals("Hey Bob", inserted.getMessage());
    }

    @Test
    void testGetMessageHistory() {
        messageDB.addMessage(createMessage("alice@skolard.ca", "bob@skolard.ca", "alice@skolard.ca", "Hello"));
        messageDB.addMessage(createMessage("alice@skolard.ca", "bob@skolard.ca", "bob@skolard.ca", "Hi"));

        List<Message> history = messageDB.getMessageHistory("alice@skolard.ca", "bob@skolard.ca");
        assertEquals(2, history.size());
    }

    @Test
    void testGetMessageHistoryEmpty() {
        List<Message> history = messageDB.getMessageHistory("ghost@skolard.ca", "none@skolard.ca");
        assertTrue(history.isEmpty());
    }

    @Test
    void testGetTutorsMessaged() {
        messageDB.addMessage(createMessage("student1@skolard.ca", "tutor1@skolard.ca", "student1@skolard.ca", "Hi"));
        messageDB.addMessage(createMessage("student1@skolard.ca", "tutor2@skolard.ca", "student1@skolard.ca", "Hey"));

        List<String> tutors = messageDB.getTutorsMessaged("student1@skolard.ca");
        assertEquals(2, tutors.size());
        assertTrue(tutors.contains("tutor1@skolard.ca"));
        assertTrue(tutors.contains("tutor2@skolard.ca"));
    }

    @Test
    void testGetStudentsMessaged() {
        messageDB.addMessage(createMessage("stud1@skolard.ca", "tut1@skolard.ca", "stud1@skolard.ca", "Message"));
        messageDB.addMessage(createMessage("stud2@skolard.ca", "tut1@skolard.ca", "stud2@skolard.ca", "Another"));

        List<String> students = messageDB.getStudentsMessaged("tut1@skolard.ca");
        assertEquals(2, students.size());
        assertTrue(students.contains("stud1@skolard.ca"));
        assertTrue(students.contains("stud2@skolard.ca"));
    }

    @Test
    void testDeleteMessageById() {
        Message msg = messageDB.addMessage(createMessage("s@a.ca", "t@a.ca", "s@a.ca", "bye"));
        messageDB.deleteMessageById(msg.getMessageId());

        List<Message> history = messageDB.getMessageHistory("s@a.ca", "t@a.ca");
        assertTrue(history.isEmpty());
    }

    @Test
    void testUpdateMessage() {
        Message msg = messageDB.addMessage(createMessage("x@y.com", "z@y.com", "x@y.com", "Initial"));
        msg.editMessage("Updated");
        messageDB.updateMessage(msg);

        List<Message> updated = messageDB.getMessageHistory("x@y.com", "z@y.com");
        assertEquals("Updated", updated.get(0).getMessage());
    }

    @Test
    void testDeleteMessageHistory() {
        messageDB.addMessage(createMessage("p@x.ca", "q@x.ca", "p@x.ca", "One"));
        messageDB.addMessage(createMessage("p@x.ca", "q@x.ca", "q@x.ca", "Two"));
        messageDB.deleteMessageHistory("p@x.ca", "q@x.ca");

        List<Message> result = messageDB.getMessageHistory("p@x.ca", "q@x.ca");
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteNonexistentMessageById() {
        assertDoesNotThrow(() -> messageDB.deleteMessageById(99999));
    }

    @Test
    void testUpdateNonexistentMessage() {
        Message fake = new Message(999, LocalDateTime.now(), "a@b.ca", "b@a.ca", "a@b.ca", "ghost");
        fake.editMessage("phantom");
        assertDoesNotThrow(() -> messageDB.updateMessage(fake));
    }
}
