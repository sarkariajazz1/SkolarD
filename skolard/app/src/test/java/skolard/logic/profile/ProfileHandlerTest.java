package skolard.logic.profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.logic.session.SessionHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
        handler = new ProfileHandler(studentDB, tutorDB, sessionHandler);
    }

    @Test
    void testAddStudent() {
        handler.addStudent("Sam", "Sam@skolard.ca", "pass");
        verify(studentDB).addStudent(any());
    }

    @Test
    void testUpdateStudent() {
        Student student = mock(Student.class);
        handler.updateStudent(student);
        verify(studentDB).updateStudent(student);
    }

    @Test
    void testUpdateTutor() {
        Tutor tutor = mock(Tutor.class);
        handler.updateTutor(tutor);
        verify(tutorDB).updateTutor(tutor);
    }

    @Test
    void testUpdateTutorBio() {
        Tutor tutor = new Tutor("John", "john@example.com", "computer science student");
        handler.updateBio(tutor, "test");
        assert(tutor.getBio().equals("test"));
        verify(tutorDB).updateTutor(tutor);
    }

    @Test
    void testAddCourse(){
        Tutor tutor = new Tutor("Alice", "alice@example.com", "Bio");
        String course = "COMP3350";
        Double grade = 4.0;

        handler.addCourse(tutor, course, grade);
        verify(tutorDB).addCourseToTutor(tutor, course, grade);
    }

    @Test
    void testRemoveCourse(){
        Tutor tutor = new Tutor("Bob", "bob@example.com", "Physics tutor");
        String course = "PHYS1050";

        handler.removeCourse(tutor, course);

        verify(tutorDB).removeCourseFromTutor(tutor, course);
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
