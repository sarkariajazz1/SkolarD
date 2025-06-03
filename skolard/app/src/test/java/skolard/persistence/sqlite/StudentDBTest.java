package skolard.persistence.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import skolard.objects.Student;
import skolard.utils.PasswordUtil;

public class StudentDBTest {

    private static Connection connection;
    private StudentDB studentDB;

    @BeforeAll
    static void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE student (email TEXT PRIMARY KEY, name TEXT, password TEXT);");
        }
    }

    @BeforeEach
    void init() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM student");
        }
        studentDB = new StudentDB(connection);
    }

    private void insert(String email, String name, String plainPassword) throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO student (email, name, password) VALUES (?, ?, ?);")) {
            stmt.setString(1, email);
            stmt.setString(2, name);
            stmt.setString(3, PasswordUtil.hash(plainPassword));
            stmt.executeUpdate();
        }
    }

    @Test
    void testAddAndGetStudent() {
        Student student = new Student("Test Student", "test@student.ca");
        studentDB.addStudent(student);
        Student result = studentDB.getStudentByEmail("test@student.ca");
        assertNotNull(result);
        assertEquals("Test Student", result.getName());
    }

    @Test
    void testGetAllStudents() {
        studentDB.addStudent(new Student("Alice", "a@student.ca"));
        studentDB.addStudent(new Student("Bob", "b@student.ca"));
        List<Student> students = studentDB.getAllStudents();
        assertEquals(2, students.size());
    }

    @Test
    void testDeleteStudent() {
        studentDB.addStudent(new Student("Charlie", "c@student.ca"));
        studentDB.deleteStudentByEmail("c@student.ca");
        assertNull(studentDB.getStudentByEmail("c@student.ca"));
    }

    @Test
    void testUpdateStudent() {
        studentDB.addStudent(new Student("Dana", "d@student.ca"));
        studentDB.updateStudent(new Student("Dana Updated", "d@student.ca"));
        assertEquals("Dana Updated", studentDB.getStudentByEmail("d@student.ca").getName());
    }

    @Test
    void testGetStudentByEmail_NotFound() {
        assertNull(studentDB.getStudentByEmail("ghost@student.ca"));
    }

    // @Test
    // void testAuthenticate_Success() throws Exception {
    //     insert("auth@student.ca", "Auth Student", "mypassword");
    //     Student result = studentDB.authenticate("auth@student.ca", "mypassword");
    //     assertNotNull(result);
    //     assertEquals("Auth Student", result.getName());
    // }

    @Test
    void testAuthenticate_WrongPassword() throws Exception {
        insert("wrongpass@student.ca", "Wrong Pass", "realpass");
        assertNull(studentDB.authenticate("wrongpass@student.ca", "fakepass"));
    }

    @Test
    void testAuthenticate_NotFound() {
        assertNull(studentDB.authenticate("ghost@student.ca", "anything"));
    }
} 