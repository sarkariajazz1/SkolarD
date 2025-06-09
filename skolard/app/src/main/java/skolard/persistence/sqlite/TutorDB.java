package skolard.persistence.sqlite;

<<<<<<< HEAD
import skolard.objects.Tutor;
import skolard.persistence.TutorPersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

=======
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

>>>>>>> dev
/**
 * SQLite implementation of the TutorPersistence interface.
 * Provides methods for CRUD operations on the 'tutor' table.
 */
public class TutorDB implements TutorPersistence {

    // Active database connection
    private final Connection connection;
<<<<<<< HEAD

    /**
     * Constructor that accepts a SQLite connection.
     *
     * @param connection the database connection to use
     */
    public TutorDB(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves all tutor records from the database.
     *
     * @return a list of all tutors
     */
    @Override
    public List<Tutor> getAllTutors() {
=======
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
>>>>>>> dev
        List<Tutor> tutors = new ArrayList<>();
        String sql = "SELECT name, email, bio FROM tutor";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

<<<<<<< HEAD
            // Build Tutor objects from the result set
            while (rs.next()) {
                tutors.add(new Tutor(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("bio")
=======
            while (rs.next()) {
                email = rs.getString("email");
                courses = tutorCoursesDB.getTutorCourses(email);
                tutors.add(new Tutor(
                    rs.getString("name"),
                    rs.getString(email),
                    null,
                    rs.getString("bio"),
                    courses
>>>>>>> dev
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving tutors", e);
        }

        return tutors;
    }

    /**
<<<<<<< HEAD
     * Retrieves a single tutor by their unique email.
     *
     * @param email the email of the tutor to find
     * @return the matching Tutor object, or null if not found
     */
    @Override
    public Tutor getTutorByEmail(String email) {
=======
     * Retrieves a tutor by email (for profile display only).
     */
    @Override
    public Tutor getTutorByEmail(String email) {
        Map<String, Double> courses;
>>>>>>> dev
        String sql = "SELECT name, email, bio FROM tutor WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

<<<<<<< HEAD
            // Return the tutor if found
            if (rs.next()) {
                return new Tutor(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("bio")
=======
            if (rs.next()) {
                email = rs.getString("email");
                courses = tutorCoursesDB.getTutorCourses(email);
                return new Tutor(
                    rs.getString("name"),
                    rs.getString("email"),
                    null,
                    rs.getString("bio"),
                    courses
>>>>>>> dev
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding tutor by email", e);
        }

        return null;
    }

    /**
<<<<<<< HEAD
     * Inserts a new tutor into the database.
     *
     * @param newTutor the tutor object to insert
     * @return the same tutor object
     */
    @Override
    public Tutor addTutor(Tutor newTutor) {
        String sql = "INSERT INTO tutor (name, email, bio) VALUES (?, ?, ?)";
=======
     * Adds a new tutor to the database (includes hashed password).
     */
    @Override
    public Tutor addTutor(Tutor newTutor) {
        String sql = "INSERT INTO tutor (name, email, password, bio) VALUES (?, ?, ?, ?)";
>>>>>>> dev

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newTutor.getName());
            stmt.setString(2, newTutor.getEmail());
<<<<<<< HEAD
            stmt.setString(3, newTutor.getBio());
=======
            stmt.setString(3, newTutor.getHashedPassword());
            stmt.setString(4, newTutor.getBio());
>>>>>>> dev
            stmt.executeUpdate();
            return newTutor;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding tutor", e);
        }
    }

    /**
<<<<<<< HEAD
     * Deletes a tutor from the database using their email as the key.
     *
     * @param email the email of the tutor to delete
=======
     * Deletes a tutor by email.
>>>>>>> dev
     */
    @Override
    public void deleteTutorByEmail(String email) {
        String sql = "DELETE FROM tutor WHERE email = ?";

<<<<<<< HEAD
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();

=======
        tutorCoursesDB.deleteAllTutorCourses(email);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
>>>>>>> dev
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting tutor", e);
        }
    }

    /**
<<<<<<< HEAD
     * Updates an existing tutor's name and bio in the database.
     *
     * @param updatedTutor the tutor object containing updated fields
=======
     * Updates an existing tutor's name and bio.
     * Password is not updated here.
>>>>>>> dev
     */
    @Override
    public void updateTutor(Tutor updatedTutor) {
        String sql = "UPDATE tutor SET name = ?, bio = ? WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, updatedTutor.getName());
            stmt.setString(2, updatedTutor.getBio());
            stmt.setString(3, updatedTutor.getEmail());
<<<<<<< HEAD
            stmt.executeUpdate();

=======
            stmt.executeUpdate();            
>>>>>>> dev
        } catch (SQLException e) {
            throw new RuntimeException("Error updating tutor", e);
        }
    }
<<<<<<< HEAD
=======

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
>>>>>>> dev
}
