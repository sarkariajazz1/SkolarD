package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Student;
import skolard.utils.PasswordUtil;

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
        String hashedPassword = PasswordUtil.hash("abc123");
        Student newStudent = new Student("Alice Smith", "alice@example.com", hashedPassword);
        Student added = studentStub.addStudent(newStudent);
        assertNotNull(added);
        assertEquals("alice@example.com", added.getEmail());
    }

    @Test
    void testAddStudent_DuplicateEmail() {
        Student duplicate = new Student("Matt Yab", "yabm@myumanitoba.ca", PasswordUtil.hash("pass123"));
        assertThrows(RuntimeException.class, () -> studentStub.addStudent(duplicate));
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
        String newHashedPassword = PasswordUtil.hash("newpass");
        Student updated = new Student("Matt Yab", "yabm@myumanitoba.ca", newHashedPassword);
        studentStub.updateStudent(updated);
        Student result = studentStub.getStudentByEmail("yabm@myumanitoba.ca");
        assertEquals(newHashedPassword, result.getHashedPassword());
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = studentStub.getAllStudents();
        assertEquals(3, students.size()); // Based on sample data
    }

    @Test
    void testAuthenticate_Success() {
        String hashedPassword = PasswordUtil.hash("pass123");
        Student student = studentStub.authenticate("yabm@myumanitoba.ca", hashedPassword);
        assertNotNull(student);
        assertEquals("Matt Yab", student.getName());
    }

    @Test
    void testAuthenticate_Failure_WrongPassword() {
        String wrongHash = PasswordUtil.hash("wrongpass");
        Student student = studentStub.authenticate("yabm@myumanitoba.ca", wrongHash);
        assertNull(student);
    }

    @Test
    void testAuthenticate_Failure_NonexistentEmail() {
        String hashedPassword = PasswordUtil.hash("pass123");
        Student student = studentStub.authenticate("nonexistent@example.com", hashedPassword);
        assertNull(student);
    }
}
