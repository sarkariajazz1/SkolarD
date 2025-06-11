package skolard.logic.faq;

import skolard.objects.FAQ;
import skolard.persistence.FAQPersistence;

import java.util.List;

/**
 * Handles the business logic for accessing and managing FAQ entries.
 * Acts as a service layer between the user interface and persistence layer.
 */
public class FAQHandler {

    // Reference to the persistence layer for FAQ operations
    private final FAQPersistence faqPersistence;

    /**
     * Constructor for injecting a custom FAQPersistence implementation.
     * Allows for flexibility in testing or switching databases.
     *
     * @param faqPersistence the persistence implementation to be used
     */
    public FAQHandler(FAQPersistence faqPersistence) {
        this.faqPersistence = faqPersistence;
    }

    /**
     * Retrieves all FAQ entries from the database.
     *
     * @return list of all FAQs
     */
    public List<FAQ> getAllFAQs() {
        return faqPersistence.getAllFAQs();
    }

    /**
     * Adds a new FAQ entry to the database.
     *
     * @param question the question part of the FAQ
     * @param answer the answer part of the FAQ
     */
    public void addFAQ(String question, String answer) {
        faqPersistence.addFAQ(new FAQ(question, answer));
    }

    /**
     * Deletes an FAQ entry based on the provided question.
     * Assumes questions are unique identifiers for FAQs.
     *
     * @param question the question of the FAQ to delete
     */
    public void deleteFAQ(String question) {
        faqPersistence.deleteFAQByQuestion(question);
    }

    /**
     * Searches for FAQ entries that match the given keyword.
     * May search within questions, answers, or both, depending on implementation.
     *
     * @param keyword the keyword to search for
     * @return list of matching FAQs
     */
    public List<FAQ> searchFAQs(String keyword) {
        return faqPersistence.searchFAQs(keyword);
    }
}
