package skolard.persistence.sqlite;

import skolard.objects.FAQ;
import skolard.persistence.FAQPersistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FAQDB implements FAQPersistence {

    private final Connection connection;

    public FAQDB(Connection connection) {
        this.connection = connection;
    }

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
