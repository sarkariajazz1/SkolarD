// package skolard.logic;

// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertTrue;
// import org.junit.Before;
// import org.junit.Test;

// import skolard.objects.Message;
// import skolard.persistence.MessagePersistence;

// /**
//  * Unit tests for MessageHandler using a mock MessagePersistence implementation.
//  */
// public class MessageHandlerTest {

//     private MessageHandler handler;
//     private MockMessagePersistence mockDb;

//     @Before
//     public void setUp() {
//         mockDb = new MockMessagePersistence();
//         handler = new MessageHandler(mockDb);
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testGetMessageHistory_NullStudentEmail() {
//         handler.getMessageHistory(null, "tutor@email.com");
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testGetMessageHistory_NullTutorEmail() {
//         handler.getMessageHistory("student@email.com", null);
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testSendMessage_NullInput() {
//         handler.sendMessage(null);
//     }

//     @Test
//     public void testSendMessage_StoresMessage() {
//         Message m = new Message(0, now(), "s", "t", "Hi");
//         Message result = handler.sendMessage(m);
//         assertEquals(1, mockDb.store.size());
//         assertEquals("Hi", result.getMessage());
//         assertTrue(result.getMessageId() > 0);
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testUpdateMessage_Null() {
//         handler.updateMessage(null);
//     }

//     @Test
//     public void testUpdateMessage_Valid() {
//         Message m = new Message(1, now(), "s", "t", "original");
//         mockDb.addMessage(m);

//         Message updated = new Message(1, now(), "s", "t", "edited");
//         handler.updateMessage(updated);

//         assertEquals("edited", mockDb.store.get(1).getMessage());
//     }

//     @Test
//     public void testDeleteMessageById() {
//         Message m = new Message(1, now(), "s", "t", "Bye");
//         mockDb.addMessage(m);

//         handler.deleteMessageById(1);
//         assertFalse(mockDb.store.containsKey(1));
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testDeleteMessageHistory_NullStudent() {
//         handler.deleteMessageHistory(null, "t");
//     }

//     @Test(expected = IllegalArgumentException.class)
//     public void testDeleteMessageHistory_NullTutor() {
//         handler.deleteMessageHistory("s", null);
//     }

//     @Test
//     public void testDeleteMessageHistory_RemovesAllBetweenUsers() {
//         Message m1 = new Message(1, now(), "s", "t", "Msg1");
//         Message m2 = new Message(2, now(), "t", "s", "Msg2");
//         Message m3 = new Message(3, now(), "x", "t", "Msg3");

//         mockDb.addMessage(m1);
//         mockDb.addMessage(m2);
//         mockDb.addMessage(m3);

//         handler.deleteMessageHistory("s", "t");

//         assertEquals(1, mockDb.store.size());
//         assertFalse(mockDb.store.containsKey(1));
//         assertFalse(mockDb.store.containsKey(2));
//         assertTrue(mockDb.store.containsKey(3));
//     }

//     // Utility: current timestamp
//     private LocalDateTime now() {
//         return LocalDateTime.now();
//     }

//     /**
//      * A lightweight mock of MessagePersistence for unit testing.
//      */
//     private static class MockMessagePersistence implements MessagePersistence {

//         Map<Integer, Message> store = new HashMap<>();
//         private int idCounter = 1;

//         @Override
//         public List<Message> getMessageHistory(String studentEmail, String tutorEmail) {
//             List<Message> result = new ArrayList<>();
//             for (Message m : store.values()) {
//                 boolean match = (m.getSenderEmail().equals(studentEmail) && m.getReceiverEmail().equals(tutorEmail)) ||
//                                 (m.getSenderEmail().equals(tutorEmail) && m.getReceiverEmail().equals(studentEmail));
//                 if (match) {
//                     result.add(m);
//                 }
//             }
//             return result;
//         }

//         @Override
//         public Message addMessage(Message message) {
//             int id = idCounter++;
//             Message saved = new Message(id, message.getTimeSent(), message.getSenderEmail(), message.getReceiverEmail(), message.getMessage());
//             store.put(id, saved);
//             return saved;
//         }

//         @Override
//         public void deleteMessageById(int id) {
//             store.remove(id);
//         }

//         @Override
//         public void updateMessage(Message updatedMessage) {
//             store.put(updatedMessage.getMessageId(), updatedMessage);
//         }

//         @Override
//         public void deleteMessageHistory(String studentEmail, String tutorEmail) {
//             store.values().removeIf(m ->
//                     (m.getSenderEmail().equals(studentEmail) && m.getReceiverEmail().equals(tutorEmail)) ||
//                     (m.getSenderEmail().equals(tutorEmail) && m.getReceiverEmail().equals(studentEmail))
//             );
//         }
//     }
// }

