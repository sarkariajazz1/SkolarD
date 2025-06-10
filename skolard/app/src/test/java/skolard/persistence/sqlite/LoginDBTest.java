package skolard.persistence.sqlite;

import org.junit.jupiter.api.*;
import skolard.utils.PasswordUtil;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class LoginDBTest {

    private static Connection connection;
    private LoginDB loginDB;

    @BeforeAll
    static void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE student (email TEXT PRIMARY KEY, password TEXT NOT NULL);");
            stmt.execute("CREATE TABLE tutor (email TEXT PRIMARY KEY, password TEXT NOT NULL);");
        }
    }

    @BeforeEach
    void init() {
        loginDB = new LoginDB(connection);
    }

    private void insert(String table, String email, String plainPassword) throws Exception {
        String hash = PasswordUtil.hash(plainPassword);
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO " + table + " (email, password) VALUES (?, ?);")) {
            stmt.setString(1, email);
            stmt.setString(2, hash);
            stmt.executeUpdate();
        }
    }

    @Test
    void testAuthenticateStudentSuccess() throws Exception {
        insert("student", "student@skolard.ca", "mypassword");
        assertTrue(loginDB.authenticateStudent("student@skolard.ca", "mypassword"));
    }

    @Test
    void testAuthenticateStudentWrongPassword() throws Exception {
        insert("student", "fail@student.ca", "correct");
        assertFalse(loginDB.authenticateStudent("fail@student.ca", "wrong"));
    }

    @Test
    void testAuthenticateStudentNotFound() {
        assertFalse(loginDB.authenticateStudent("ghost@student.ca", "irrelevant"));
    }

    @Test
    void testAuthenticateTutorSuccess() throws Exception {
        insert("tutor", "tutor@skolard.ca", "secure123");
        assertTrue(loginDB.authenticateTutor("tutor@skolard.ca", "secure123"));
    }

    @Test
    void testAuthenticateTutorWrongPassword() throws Exception {
        insert("tutor", "fail@tutor.ca", "tutorpass");
        assertFalse(loginDB.authenticateTutor("fail@tutor.ca", "badpass"));
    }

    @Test
    void testAuthenticateTutorNotFound() {
        assertFalse(loginDB.authenticateTutor("ghost@tutor.ca", "nope"));
    }
} 