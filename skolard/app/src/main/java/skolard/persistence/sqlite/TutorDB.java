package skolard.persistence.sqlite;

import skolard.objects.Tutor;
import skolard.persistence.TutorPersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TutorDB implements TutorPersistence {

    private final Connection connection;

    public TutorDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Tutor> getAllTutors() {
        List<Tutor> tutors = new ArrayList<>();
        String sql = "SELECT id, name, email, bio FROM tutor";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tutors.add(new Tutor(
                    rs.getString("id"),
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

    @Override
    public Tutor getTutorByEmail(String email) {
        String sql = "SELECT id, name, email, bio FROM tutor WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Tutor(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("bio")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding tutor", e);
        }

        return null;
    }

    @Override
    public Tutor addTutor(Tutor newTutor) {
        String sql = "INSERT INTO tutor (id, name, email, bio) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newTutor.getId());
            stmt.setString(2, newTutor.getName());
            stmt.setString(3, newTutor.getEmail());
            stmt.setString(4, newTutor.getBio());
            stmt.executeUpdate();
            return newTutor;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding tutor", e);
        }
    }

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

    @Override
    public void updateTutor(Tutor updatedTutor) {
        String sql = "UPDATE tutor SET name = ?, id = ?, bio = ? WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, updatedTutor.getName());
            stmt.setString(2, updatedTutor.getId());
            stmt.setString(3, updatedTutor.getBio());
            stmt.setString(4, updatedTutor.getEmail());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating tutor", e);
        }
    }
}
