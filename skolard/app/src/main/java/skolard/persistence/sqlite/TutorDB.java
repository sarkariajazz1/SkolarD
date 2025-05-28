package skolard.persistence.sqlite;

import skolard.objects.Tutor;
import skolard.persistence.TutorPersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite implementation of the TutorPersistence interface.
 * Provides methods for CRUD operations on the 'tutor' table.
 */
public class TutorDB implements TutorPersistence {

    // Active database connection
    private final Connection connection;

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
        List<Tutor> tutors = new ArrayList<>();
        String sql = "SELECT name, email, bio FROM tutor";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Build Tutor objects from the result set
            while (rs.next()) {
                tutors.add(new Tutor(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("bio")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving tutors", e);
        }

        return tutors;
    }

    /**
     * Retrieves a single tutor by their unique email.
     *
     * @param email the email of the tutor to find
     * @return the matching Tutor object, or null if not found
     */
    @Override
    public Tutor getTutorByEmail(String email) {
        String sql = "SELECT name, email, bio FROM tutor WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            // Return the tutor if found
            if (rs.next()) {
                return new Tutor(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("bio")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding tutor by email", e);
        }

        return null;
    }

    /**
     * Inserts a new tutor into the database.
     *
     * @param newTutor the tutor object to insert
     * @return the same tutor object
     */
    @Override
    public Tutor addTutor(Tutor newTutor) {
        String sql = "INSERT INTO tutor (name, email, bio) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newTutor.getName());
            stmt.setString(2, newTutor.getEmail());
            stmt.setString(3, newTutor.getBio());
            stmt.executeUpdate();
            return newTutor;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding tutor", e);
        }
    }

    /**
     * Deletes a tutor from the database using their email as the key.
     *
     * @param email the email of the tutor to delete
     */
    @Override
    public void deleteTutorByEmail(String email) {
        String sql = "DELETE FROM tutor WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting tutor", e);
        }
    }

    /**
     * Updates an existing tutor's name and bio in the database.
     *
     * @param updatedTutor the tutor object containing updated fields
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
}
