package skolard.persistence.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import skolard.objects.Tutor;
import skolard.persistence.TutorPersistence;

/**
 * SQLite implementation of the TutorPersistence interface.
 * Provides methods for CRUD operations on the 'tutor' table.
 */
public class TutorDB implements TutorPersistence {

    // Active database connection
    private final Connection connection;
    private final TutorCoursesDB tutorCoursesDB;

    /**
     * Constructor that accepts a SQLite connection.
     */
    public TutorDB(Connection connection) {
        this.connection = connection;
        this.tutorCoursesDB = new TutorCoursesDB(connection);
    }

    /**
     * Retrieves all tutors for public viewing.
     * Does not include password hash.
     */
    @Override
    public List<Tutor> getAllTutors() {
        String email;
        Map<String, Double> courses;
        List<Tutor> tutors = new ArrayList<>();
        String sql = "SELECT name, email, bio FROM tutor";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                email = rs.getString("email");
                courses = tutorCoursesDB.getTutorCourses(email);
                tutors.add(new Tutor(
                    rs.getString("name"),
                    rs.getString(email),
                    null,
                    rs.getString("bio"),
                    courses
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving tutors", e);
        }

        return tutors;
    }

    /**
     * Retrieves a tutor by email (for profile display only).
     */
    @Override
    public Tutor getTutorByEmail(String email) {
        Map<String, Double> courses;
        String sql = "SELECT name, email, bio FROM tutor WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                email = rs.getString("email");
                courses = tutorCoursesDB.getTutorCourses(email);
                return new Tutor(
                    rs.getString("name"),
                    rs.getString("email"),
                    null,
                    rs.getString("bio"),
                    courses
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding tutor by email", e);
        }

        return null;
    }

    /**
     * Adds a new tutor to the database (includes hashed password).
     */
    @Override
    public Tutor addTutor(Tutor newTutor) {
        String sql = "INSERT INTO tutor (name, email, password, bio) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newTutor.getName());
            stmt.setString(2, newTutor.getEmail());
            stmt.setString(3, newTutor.getHashedPassword());
            stmt.setString(4, newTutor.getBio());
            stmt.executeUpdate();
            return newTutor;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding tutor", e);
        }
    }

    /**
     * Deletes a tutor by email.
     */
    @Override
    public void deleteTutorByEmail(String email) {
        String sql = "DELETE FROM tutor WHERE email = ?";

        tutorCoursesDB.deleteAllTutorCourses(email);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting tutor", e);
        }
    }

    /**
     * Updates an existing tutor's name and bio.
     * Password is not updated here.
     */
    @Override
    public void updateTutor(Tutor updatedTutor) {
        String sql = "UPDATE tutor SET name = ?, bio = ? WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, updatedTutor.getName());
            stmt.setString(2, updatedTutor.getBio());
            stmt.setString(3, updatedTutor.getEmail());
            stmt.executeUpdate();            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating tutor", e);
        }
    }

    @Override
    public void addCourseToTutor(Tutor tutor, String course, Double grade) {
        tutorCoursesDB.addCourse(tutor.getEmail(), course, grade);
    }

    @Override
    public void removeCourseFromTutor(Tutor tutor, String course) {
        tutorCoursesDB.deleteTutorCourse(tutor.getEmail(), course);
    }

    /**
     * Authenticates a tutor using email and hashed password.
     * Returns a full Tutor object (for login session).
     */
    @Override
    public Tutor authenticate(String email, String hashedPassword) {
        String sql = "SELECT name, email, bio, password FROM tutor WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (storedHash.equals(hashedPassword)) {
                    return new Tutor(
                        rs.getString("name"),
                        rs.getString("email"),
                        hashedPassword,                        // known match
                        rs.getString("bio"),
                        null                            // courses and grades null for now
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error authenticating tutor", e);
        }

        return null;
    }
}
