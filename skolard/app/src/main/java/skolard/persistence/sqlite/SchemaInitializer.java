package skolard.persistence.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is responsible for initializing the SQLite database schema
 * for the SkolarD application. It creates all required tables if they do not exist.
 */
public class SchemaInitializer {

    public static void initializeSchema(Connection connection) {
        String createCourseTable = "CREATE TABLE IF NOT EXISTS courses (" +
                "id TEXT PRIMARY KEY NOT NULL," +
                "name TEXT NOT NULL" +
                ");";

        String createTutorTable = "CREATE TABLE IF NOT EXISTS tutor (" +
                "email TEXT PRIMARY KEY NOT NULL UNIQUE," +
                "name TEXT NOT NULL," +
                "bio TEXT NOT NULL" +
                ");";

        String createStudentTable = "CREATE TABLE IF NOT EXISTS student (" +
                "email TEXT PRIMARY KEY NOT NULL UNIQUE," +
                "name TEXT NOT NULL" +
                ");";

        String createSessionTable = "CREATE TABLE IF NOT EXISTS session (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tutorEmail TEXT NOT NULL," +
                "studentEmail TEXT NOT NULL," +
                "startTime TEXT NOT NULL," +
                "endTime TEXT NOT NULL," +
                "courseID TEXT NOT NULL," +
                "FOREIGN KEY(tutorEmail) REFERENCES tutor(email)," +
                "FOREIGN KEY(studentEmail) REFERENCES student(email)," +
                "FOREIGN KEY(courseID) REFERENCES courses(id)" +
                ");";

        String createTutorCourseTable = "CREATE TABLE IF NOT EXISTS tutorCourse (" +
                "tutorEmail TEXT NOT NULL," +
                "grade TEXT," +
                "courseID TEXT NOT NULL," +
                "PRIMARY KEY(tutorEmail, courseID)," +
                "FOREIGN KEY(courseID) REFERENCES courses(id)," +
                "FOREIGN KEY(tutorEmail) REFERENCES tutor(email)" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCourseTable);
            stmt.execute(createTutorTable);
            stmt.execute(createStudentTable);
            stmt.execute(createSessionTable);
            stmt.execute(createTutorCourseTable);
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage()); // Add for debug
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
}

