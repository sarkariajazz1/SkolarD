package skolard.persistence.sqlite;

import skolard.objects.FAQ;
import skolard.persistence.FAQPersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FAQDB implements FAQPersistence {

    private final Connection connection;

    /**
     * Constructor that accepts a database connection.
     * 
     * @param connection an open SQLite connection
     */
    public FAQDB(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves all FAQs from the database, ordered by their ID.
     * 
     * @return a List of FAQ objects
     * @throws RuntimeException if a database access error occurs
     */
    @Override
    public List<FAQ> getAllFAQs() {
        List<FAQ> faqs = new ArrayList<>();
        String query = "SELECT question, answer FROM faq ORDER BY id ASC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                faqs.add(new FAQ(rs.getString("question"), rs.getString("answer")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch FAQs", e);
        }

        return faqs;
    }

    /**
     * Adds a new FAQ entry to the database.
     * Uses INSERT OR IGNORE to avoid duplicates.
     * 
     * @param faq the FAQ object to add
     * @throws RuntimeException if a database access error occurs
     */
    @Override
    public void addFAQ(FAQ faq) {
        String query = "INSERT OR IGNORE INTO faq (question, answer) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, faq.getQuestion());
            stmt.setString(2, faq.getAnswer());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add FAQ", e);
        }
    }

    /**
     * Deletes an FAQ from the database based on its question text.
     * 
     * @param question the question string to identify the FAQ to delete
     * @throws RuntimeException if a database access error occurs
     */
    @Override
    public void deleteFAQByQuestion(String question) {
        String query = "DELETE FROM faq WHERE question = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, question);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete FAQ", e);
        }
    }

    /**
     * Searches FAQs by matching the keyword in either question or answer fields.
     * 
     * @param keyword the search keyword to look for
     * @return a list of FAQs matching the keyword
     * @throws RuntimeException if a database access error occurs
     */
    @Override
    public List<FAQ> searchFAQs(String keyword) {
        List<FAQ> faqs = new ArrayList<>();
        String query = "SELECT question, answer FROM faq WHERE question LIKE ? OR answer LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String kw = "%" + keyword + "%";
            stmt.setString(1, kw);
            stmt.setString(2, kw);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    faqs.add(new FAQ(rs.getString("question"), rs.getString("answer")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search FAQs", e);
        }

        return faqs;
    }
}
