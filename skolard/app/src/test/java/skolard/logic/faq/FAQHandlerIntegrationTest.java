package skolard.logic.faq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import skolard.objects.FAQ;
import skolard.persistence.FAQPersistence;
import skolard.persistence.sqlite.FAQDB;

public class FAQHandlerIntegrationTest {

    private Connection connection;
    private FAQPersistence faqPersistence;
    private FAQHandler faqHandler;

    @BeforeEach
    public void setupDatabase() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE faq (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT UNIQUE, answer TEXT)");
        }
        faqPersistence = new FAQDB(connection);
        faqHandler = new FAQHandler(faqPersistence);
    }

    @BeforeEach
    public void clearTable() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM faq");
        }
    }

    @Test
    public void testAddAndGetAllFAQs() {
        faqHandler.addFAQ("What is SkolarD?", "A tutor matching app.");
        faqHandler.addFAQ("How do I sign up?", "Click on the signup button.");

        List<FAQ> faqs = faqHandler.getAllFAQs();

        assertEquals(2, faqs.size());
        assertEquals("What is SkolarD?", faqs.get(0).getQuestion());
        assertEquals("How do I sign up?", faqs.get(1).getQuestion());
    }

    @Test
    public void testDeleteFAQ() {
        faqHandler.addFAQ("Delete me?", "Yes please.");
        faqHandler.deleteFAQ("Delete me?");

        List<FAQ> faqs = faqHandler.getAllFAQs();
        assertTrue(faqs.isEmpty());
    }

    @Test
    public void testSearchFAQMatch() {
        faqHandler.addFAQ("How to book a session?", "Go to the matching page.");
        faqHandler.addFAQ("What is the rating system?", "Rate tutors after sessions.");

        List<FAQ> results = faqHandler.searchFAQs("book");

        assertEquals(1, results.size());
        assertEquals("How to book a session?", results.get(0).getQuestion());
    }

    @Test
    public void testSearchFAQNoMatch() {
        faqHandler.addFAQ("Can I cancel?", "Yes.");

        List<FAQ> results = faqHandler.searchFAQs("reschedule");
        assertTrue(results.isEmpty());
    }

    @AfterEach
    public void closeDatabase() throws Exception {
        connection.close();
    }
}
