package skolard.persistence.sqlite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import skolard.objects.Card;
import skolard.persistence.CardPersistence;

public class CardDB implements CardPersistence{
     private final Connection connection;

    /**
     * Constructor that accepts a database connection.
     * 
     * @param connection an open SQLite connection
     */
    public CardDB(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new student into the database.
     * 
     * @param newStudent the student object to insert
     * @return the same student object that was added
     */
    @Override
    public Card addAccountCard(String accountEmail, Card card) {
        String sql = "INSERT INTO student (name, email) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            //stmt.setString(1, newStudent.getName());
            //stmt.setString(2, newStudent.getEmail());
            stmt.executeUpdate();
            return card;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding student", e);
        }
    }

    /**
     * Retrieves all students from the database.
     * 
     * @return a list of all student objects found
     */
    @Override
    public List<Card> getCardsByAccount(String accountEmail) {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT name, email FROM student";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterate over result set and build Student objects
            while (rs.next()) {
                cards.add(new Card(
                    rs.getString("name"),
                    rs.getString("email"),
                    null
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving students", e);
        }

        return cards;
    }

    /**
     * Deletes a student from the database based on their email.
     * 
     * @param email the unique email of the student to delete
     */
    @Override
    public void deleteCard(String accountEmail, Card card) {
        String sql = "DELETE FROM student WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountEmail);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting student", e);
        }
    }
}
