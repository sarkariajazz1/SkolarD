package skolard.logic.profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.logic.session.SessionHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ProfileHandlerTest {

    private ProfileHandler handler;
    private StudentPersistence studentDB;
    private TutorPersistence tutorDB;
    private SessionHandler sessionHandler;

    @BeforeEach
    void setup() {
        studentDB = mock(StudentPersistence.class);
        tutorDB = mock(TutorPersistence.class);
        sessionHandler = mock(SessionHandler.class);
        handler = new ProfileHandler(studentDB, tutorDB, new DefaultProfileFormatter(), sessionHandler);
    }

    @Test
    void testAddStudent() {
        handler.addStudent("Sam", "Sam@skolard.ca", "pass");
        verify(studentDB).addStudent(any());
    }

    @Test
    void testUpdateTutor() {
        Tutor tutor = mock(Tutor.class);
        handler.updateTutor(tutor);
        verify(tutorDB).updateTutor(tutor);
    }

    @Test
    void testViewBasicProfile() {
        Student s = new Student("Simran", "s@skolard.ca", "pass");
        String result = handler.viewBasicProfile(s);
        assertTrue(result.contains("Simran"));
    }

    @Test
    void testViewFullProfile() {
        Tutor t = new Tutor("T", "t@skolard.ca", "pass", "Bio", null);
        handler.viewFullProfile(t);
        verify(sessionHandler).setTutorSessionLists(t);
    }
}
