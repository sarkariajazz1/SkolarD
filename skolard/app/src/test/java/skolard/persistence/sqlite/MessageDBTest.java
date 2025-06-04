// package skolard.persistence.sqlite;

// import org.junit.jupiter.api.*;
// import skolard.objects.Message;

// import java.sql.*;
// import java.time.LocalDateTime;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;

// public class MessageDBTest {

//     private static Connection connection;
//     private MessageDB messageDB;

//     @BeforeAll
//     static void setup() throws Exception {
//         connection = DriverManager.getConnection("jdbc:sqlite::memory:");
//         try (Statement stmt = connection.createStatement()) {
//             stmt.execute("CREATE TABLE messages (id INTEGER PRIMARY KEY AUTOINCREMENT, timeSent TEXT, senderEmail TEXT, receiverEmail TEXT, message TEXT);");
//         }
//     }

//     @BeforeEach
//     void init() throws Exception {
//         try (Statement stmt = connection.createStatement()) {
//             stmt.execute("DELETE FROM messages");
//         }
//         messageDB = new MessageDB(connection);
//     }

//     private Message createMsg(String from, String to, String text) {
//         return new Message(0, LocalDateTime.now(), from, to, text);
//     }

//     @Test
//     void testAddAndGetMessages() {
//         messageDB.addMessage(createMsg("alice@skolard.ca", "bob@skolard.ca", "Hello!"));
//         messageDB.addMessage(createMsg("bob@skolard.ca", "alice@skolard.ca", "Hi there!"));

//         List<Message> history = messageDB.getMessageHistory("alice@skolard.ca", "bob@skolard.ca");
//         assertEquals(2, history.size());
//     }

//     @Test
//     void testGetMessageHistoryEmpty() {
//         List<Message> history = messageDB.getMessageHistory("noone@skolard.ca", "nobody@skolard.ca");
//         assertTrue(history.isEmpty());
//     }

//     @Test
//     void testDeleteMessageById() {
//         Message msg = messageDB.addMessage(createMsg("x@skolard.ca", "y@skolard.ca", "Bye"));
//         messageDB.deleteMessageById(msg.getMessageId());
//         List<Message> history = messageDB.getMessageHistory("x@skolard.ca", "y@skolard.ca");
//         assertTrue(history.isEmpty());
//     }

//     @Test
//     void testUpdateMessage() {
//         Message msg = messageDB.addMessage(createMsg("editor@skolard.ca", "peer@skolard.ca", "Draft"));
//         msg.editMessage("Final version");
//         messageDB.updateMessage(msg);

//         List<Message> history = messageDB.getMessageHistory("editor@skolard.ca", "peer@skolard.ca");
//         assertEquals("Final version", history.get(0).getMessage());
//     }

//     @Test
//     void testDeleteMessageHistory() {
//         messageDB.addMessage(createMsg("student@skolard.ca", "tutor@skolard.ca", "One"));
//         messageDB.addMessage(createMsg("tutor@skolard.ca", "student@skolard.ca", "Two"));
//         messageDB.deleteMessageHistory("student@skolard.ca", "tutor@skolard.ca");

//         List<Message> history = messageDB.getMessageHistory("student@skolard.ca", "tutor@skolard.ca");
//         assertTrue(history.isEmpty());
//     }

//     @Test
//     void testUpdateNonExistentMessage() {
//         Message ghost = new Message(999, LocalDateTime.now(), "a@b.ca", "b@a.ca", "ghost");
//         ghost.editMessage("nothing");
//         assertDoesNotThrow(() -> messageDB.updateMessage(ghost));
//     }

//     @Test
//     void testDeleteNonExistentMessageById() {
//         assertDoesNotThrow(() -> messageDB.deleteMessageById(404));
//     }
// }
