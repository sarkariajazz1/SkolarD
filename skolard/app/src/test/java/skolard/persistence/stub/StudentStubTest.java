package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentStubTest {

    private StudentStub studentStub;

    @BeforeEach
    void setUp() {
        studentStub = new StudentStub();
    }

    @Test
    void testAddStudent_Success() {
        Student newStudent = new Student("Alice Smith", "alice@example.com", "abc123");
        Student added = studentStub.addStudent(newStudent);
        assertNotNull(added);
        assertEquals("alice@example.com", added.getEmail());
    }

    @Test
    void testAddStudent_DuplicateEmail() {
        Student duplicate = new Student("Matt Yab", "yabm@myumanitoba.ca", "pass123");
        Student result = studentStub.addStudent(duplicate);
        assertNull(result);
    }

    @Test
    void testGetStudentByEmail_Exists() {
        Student student = studentStub.getStudentByEmail("yabm@myumanitoba.ca");
        assertNotNull(student);
        assertEquals("Matt Yab", student.getName());
    }

    @Test
    void testGetStudentByEmail_NotExists() {
        Student student = studentStub.getStudentByEmail("nonexistent@example.com");
        assertNull(student);
    }

    @Test
    void testDeleteStudentByEmail() {
        studentStub.deleteStudentByEmail("yabm@myumanitoba.ca");
        assertNull(studentStub.getStudentByEmail("yabm@myumanitoba.ca"));
    }

    @Test
    void testUpdateStudent() {
        Student updated = new Student("Matt Yab", "yabm@myumanitoba.ca", "newpass");
        studentStub.updateStudent(updated);
        Student result = studentStub.getStudentByEmail("yabm@myumanitoba.ca");
        assertEquals("newpass", result.getHashedPassword());
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = studentStub.getAllStudents();
        assertEquals(3, students.size()); // Based
    }

    @Test
    void testAuthenticate_Success() {
        // "pass123" hashed using Integer.toHexString("pass123".hashCode())
        String hashedPassword = Integer.toHexString("pass123".hashCode());
        Student student = studentStub.authenticate("yabm@myumanitoba.ca", hashedPassword);
        assertNotNull(student);
        assertEquals("Matt Yab", student.getName());
    }

    @Test
    void testAuthenticate_Failure_WrongPassword() {
        String wrongHash = Integer.toHexString("wrongpass".hashCode());
        Student student = studentStub.authenticate("yabm@myumanitoba.ca", wrongHash);
        assertNull(student);
    }

    @Test
    void testAuthenticate_Failure_NonexistentEmail() {
        String hashedPassword = Integer.toHexString("pass123".hashCode());
        Student student = studentStub.authenticate("nonexistent@example.com", hashedPassword);
        assertNull(student);
    }

}