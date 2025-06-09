// package skolard.logic;

// import java.time.LocalDateTime;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertThrows;
// import static org.junit.Assert.assertTrue;
// import org.junit.Before;
// import org.junit.Test;

// import skolard.objects.Session;
// import skolard.objects.Student;
// import skolard.objects.Tutor;
// import skolard.persistence.PersistenceFactory;
// import skolard.persistence.PersistenceType;
// import skolard.persistence.SessionPersistence;

// public class SessionHandlerTest {

//     private SessionPersistence sessionPersistence;
//     private SessionHandler sessionHandler;

//     @Before
//     public void setup() {
//         PersistenceFactory.initialize(PersistenceType.STUB, false);
//         sessionPersistence = PersistenceFactory.getSessionPersistence();
//         sessionHandler = new SessionHandler(sessionPersistence);
//     }

//     // ─── createSession ──────────────────────────────────────────────

//     // @Test
//     // public void testCreateSession_NoConflicts() {
//     //     Tutor tutor = new Tutor("Tutor", "tutor@myumanitoba.ca", "Bio");
//     //     LocalDateTime start = LocalDateTime.of(2025, 5, 30, 10, 0);
//     //     LocalDateTime end = LocalDateTime.of(2025, 5, 30, 11, 0);

//     //     int initialSize = sessionPersistence.getAllSessions().size();
//     //     sessionHandler.createSession(tutor, start, end, "Math 101");

//     //     List<Session> allSessions = sessionPersistence.getAllSessions();
//     //     assertEquals(initialSize + 1, allSessions.size());

//     //     Session lastSession = allSessions.get(allSessions.size() - 1);
//     //     assertEquals("MATH 1500"||"Physics", lastSession.getCourseName());
//     //     assertEquals("Amrit Singh", lastSession.getTutor().getName());
//     //     // assertEquals(start, lastSession.getStartDateTime());
//     //     // assertEquals(end, lastSession.getEndDateTime());
//     // }

//     @Test
//     public void testCreateSession_WithConflict() {
//         Tutor tutor = new Tutor("Tutor", "tutor@myumanitoba.ca", "Bio");

//         sessionPersistence.addSession(new Session(
//                 1000, tutor, null,
//                 LocalDateTime.of(2025, 5, 30, 10, 0),
//                 LocalDateTime.of(2025, 5, 30, 11, 0),
//                 "Physics"));

//         LocalDateTime newStart = LocalDateTime.of(2025, 5, 30, 10, 30);
//         LocalDateTime newEnd = LocalDateTime.of(2025, 5, 30, 11, 30);

//         assertThrows(IllegalArgumentException.class, () ->
//                 sessionHandler.createSession(tutor, newStart, newEnd, "Chemistry"));
//     }

//     // ─── bookASession ──────────────────────────────────────────────

//     @Test
//     public void testBookASession_NotBooked() {
//         Tutor tutor = new Tutor("Tutor", "tutor@myumanitoba.ca", "Bio");
//         Student student = new Student("Student", "student@myumanitoba.ca");

//         Session session = new Session(2000, tutor, null,
//                 LocalDateTime.of(2025, 6, 1, 14, 0),
//                 LocalDateTime.of(2025, 6, 1, 15, 0),
//                 "Biology");

//         sessionPersistence.addSession(session);

//         sessionHandler.bookASession(student, 2000);

//         assertTrue(session.isBooked());
//         assertEquals(student, session.getStudent());
//     }

//     @Test
//     public void testBookASession_AlreadyBookedBySameStudent() {
//         Tutor tutor = new Tutor("Tutor", "tutor@myumanitoba.ca", "Bio");
//         Student student = new Student("Student", "student@myumanitoba.ca");

//         Session session = new Session(3000, tutor, student,
//                 LocalDateTime.of(2025, 6, 1, 16, 0),
//                 LocalDateTime.of(2025, 6, 1, 17, 0),
//                 "Chemistry");

//         session.bookSession(student); // pre-book it

//         sessionPersistence.addSession(session);

//         IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
//                 sessionHandler.bookASession(student, 3000));

//         assertEquals("Session is already booked", ex.getMessage());
//     }

//     @Test
//     public void testBookASession_AlreadyBookedByAnotherStudent() {
//         Tutor tutor = new Tutor("Tutor", "tutor@myumanitoba.ca", "Bio");
//         Student student1 = new Student("Student A", "a@myumanitoba.ca");
//         Student student2 = new Student("Student B", "b@myumanitoba.ca");

//         Session session = new Session(4000, tutor, student1,
//                 LocalDateTime.of(2025, 6, 2, 10, 0),
//                 LocalDateTime.of(2025, 6, 2, 11, 0),
//                 "English");

//         session.bookSession(student1);

//         sessionPersistence.addSession(session);

//         IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
//                 sessionHandler.bookASession(student2, 4000));

//         assertEquals("Session is already booked by someone else", ex.getMessage());
//     }
// }
