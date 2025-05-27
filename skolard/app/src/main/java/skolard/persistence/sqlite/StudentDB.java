package skolard.persistence.sqlite;

import skolard.objects.Student;
import skolard.persistence.StudentPersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDB implements StudentPersistence {

    private final Connection connection;

    public StudentDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT name, email FROM student";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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

    @Override
    public Student getStudentByEmail(String email) {
        String sql = "SELECT name, email FROM student WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

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
}
