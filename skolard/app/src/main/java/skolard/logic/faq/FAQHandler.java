package skolard.logic.faq;

import skolard.objects.FAQ;
import skolard.persistence.FAQPersistence;
import skolard.persistence.sqlite.FAQDB;
import skolard.persistence.stub.FAQStub;

import java.sql.Connection;
import java.util.List;

/**
 * Handles the business logic for accessing FAQ entries.
 */
public class FAQHandler {

    private final FAQPersistence faqPersistence;

    /**
     * Default constructor uses in-memory stub (for development or testing).
     */
    public FAQHandler() {
        this.faqPersistence = new FAQStub();
    }

    /**
     * Constructor for injecting custom FAQ persistence (e.g., for testing or swapping implementations).
     */
    public FAQHandler(FAQPersistence faqPersistence) {
        this.faqPersistence = faqPersistence;
    }

    /**
     * Constructor to use SQLite-backed FAQDB with a provided connection.
     */
    public FAQHandler(Connection connection) {
        this.faqPersistence = new FAQDB(connection);
    }

    public List<FAQ> getAllFAQs() {
        return faqPersistence.getAllFAQs();
    }

    public void addFAQ(String question, String answer) {
        faqPersistence.addFAQ(new FAQ(question, answer));
    }

    public void deleteFAQ(String question) {
        faqPersistence.deleteFAQByQuestion(question);
    }

    public List<FAQ> searchFAQs(String keyword) {
        return faqPersistence.searchFAQs(keyword);
    }
}
