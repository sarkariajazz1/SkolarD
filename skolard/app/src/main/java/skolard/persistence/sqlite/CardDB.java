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
     * Adds a new card record for a given account email.
     * 
     * @param accountEmail the email associated with the account
     * @param card the Card object to add
     * @return the same Card object after insertion
     * @throws RuntimeException if any SQL error occurs
     */
    @Override
    public Card addAccountCard(String accountEmail, Card card) {
        String sql = "INSERT INTO card (accountEmail, name, cardNumber, expiry) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountEmail);
            stmt.setString(2, card.getName());
            stmt.setString(3, card.getCardNumber());
            stmt.setString(4, card.getExpiry());
            stmt.executeUpdate();
            return card;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding card", e);
        }
    }

    /**
     * Retrieves all cards associated with a given account email.
     * 
     * @param accountEmail the email associated with the account
     * @return a list of Card objects belonging to the account
     * @throws RuntimeException if any SQL error occurs
     */
    @Override
    public List<Card> getCardsByAccount(String accountEmail) {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT name, cardNumber, expiry FROM card WHERE accountEmail = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountEmail);
            ResultSet rs = stmt.executeQuery();
            // Iterate over result set and build Card objects
            while (rs.next()) {
                cards.add(new Card(
                    rs.getString("cardNumber"),
                    rs.getString("expiry"),
                    rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving students", e);
        }

        return cards;
    }

    /**
     * Deletes a specific card associated with an account email.
     * 
     * @param accountEmail the email associated with the account
     * @param card the Card object to delete
     * @throws RuntimeException if any SQL error occurs
     */
    @Override
    public void deleteCard(String accountEmail, Card card) {
        String sql = "DELETE FROM card WHERE accountEmail = ? AND name = ? AND cardNumber = ? AND expiry = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountEmail);
            stmt.setString(2, card.getName());
            stmt.setString(3, card.getCardNumber());
            stmt.setString(4, card.getExpiry());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting card", e);
        }
    }
}
