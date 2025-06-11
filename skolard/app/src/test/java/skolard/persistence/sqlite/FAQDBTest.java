package skolard.persistence.sqlite;

import skolard.objects.FAQ;
import skolard.persistence.FAQPersistence;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FAQDBTest {

    private Connection connection;
    private FAQPersistence faqDB;

    @BeforeAll
    public void setupDatabase() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SchemaInitializer.initializeSchema(connection);
        faqDB = new FAQDB(connection);
    }

    @BeforeEach
    public void clearFAQTable() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM faq");
        }
    }

    @AfterAll
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testAddAndGetAllFAQs() {
        FAQ faq = new FAQ("Can I cancel a session?", "Yes, from your dashboard.");
        faqDB.addFAQ(faq);

        List<FAQ> faqs = faqDB.getAllFAQs();
        assertEquals(1, faqs.size());
        assertEquals("Can I cancel a session?", faqs.get(0).getQuestion());
        assertEquals("Yes, from your dashboard.", faqs.get(0).getAnswer());
    }

    @Test
    public void testDeleteFAQ() {
        FAQ faq = new FAQ("Will I be rated?", "Yes, after each session.");
        faqDB.addFAQ(faq);

        faqDB.deleteFAQByQuestion(faq.getQuestion());
        List<FAQ> faqs = faqDB.getAllFAQs();

        assertTrue(faqs.stream().noneMatch(f -> f.getQuestion().equals(faq.getQuestion())));
    }

    @Test
    public void testSearchFAQs() {
        faqDB.addFAQ(new FAQ("Can I rebook a tutor?", "Yes, from session history."));
        faqDB.addFAQ(new FAQ("How do I rate a tutor?", "Go to the Ratings section."));

        List<FAQ> results = faqDB.searchFAQs("rebook");

        assertEquals(1, results.size());
        assertEquals("Can I rebook a tutor?", results.get(0).getQuestion());
    }

    @Test
    public void testDuplicateFAQInsertionIgnored() {
        FAQ faq = new FAQ("What is SkolarD?", "It’s a tutor matching platform.");
        faqDB.addFAQ(faq);
        faqDB.addFAQ(faq); // Should be ignored due to UNIQUE constraint

        List<FAQ> faqs = faqDB.getAllFAQs();
        assertEquals(1, faqs.size());
    }
}
