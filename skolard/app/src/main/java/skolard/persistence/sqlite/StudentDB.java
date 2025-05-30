package skolard.persistence.sqlite;

import skolard.objects.Student;
import skolard.persistence.StudentPersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite implementation of the StudentPersistence interface.
 * Manages CRUD operations for students stored in the 'student' table.
 */
public class StudentDB implements StudentPersistence {

    // Active database connection used for all queries
    private final Connection connection;

    /**
     * Constructor that accepts a database connection.
     * 
     * @param connection an open SQLite connection
     */
    public StudentDB(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves all students from the database.
     * 
     * @return a list of all student objects found
     */
    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT name, email FROM student";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterate over result set and build Student objects
            while (rs.next()) {
                students.add(new Student(
                    rs.getString("name"),
                    rs.getString("email")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving students", e);
        }

        return students;
    }

    /**
     * Finds and returns a student by their email.
     * 
     * @param email the unique email of the student
     * @return the matching Student object, or null if not found
     */
    @Override
    public Student getStudentByEmail(String email) {
        String sql = "SELECT name, email FROM student WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            // Return the student if found
            if (rs.next()) {
                return new Student(
                    rs.getString("name"),
                    rs.getString("email")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding student by email", e);
        }

        return null;
    }

    /**
     * Inserts a new student into the database.
     * 
     * @param newStudent the student object to insert
     * @return the same student object that was added
     */
    @Override
    public Student addStudent(Student newStudent) {
        String sql = "INSERT INTO student (name, email) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStudent.getName());
            stmt.setString(2, newStudent.getEmail());
            stmt.executeUpdate();
            return newStudent;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding student", e);
        }
    }

    /**
     * Deletes a student from the database based on their email.
     * 
     * @param email the unique email of the student to delete
     */
    @Override
    public void deleteStudentByEmail(String email) {
        String sql = "DELETE FROM student WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting student", e);
        }
    }

    /**
     * Updates an existing student's name using their email as the identifier.
     * 
     * @param updatedStudent the student object containing updated data
     */
    @Override
    public void updateStudent(Student updatedStudent) {
        String sql = "UPDATE student SET name = ? WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, updatedStudent.getName());
            stmt.setString(2, updatedStudent.getEmail());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating student", e);
        }
    }

    @Override
    public Student authenticate(String email, String hashedPassword) {
        String sql = "SELECT name, email, password FROM student WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (storedHash.equals(hashedPassword)) {
                    return new Student(rs.getString("name"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error during student authentication", e);
        }

        return null;
    }

}
