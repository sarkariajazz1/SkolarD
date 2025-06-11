package skolard.persistence;

import skolard.objects.FAQ;
import java.util.List;

/**
 * Interface for FAQ persistence operations.
 * Defines methods to retrieve, add, delete, and search FAQs.
 */
public interface FAQPersistence {
    
    /**
     * Retrieves all FAQ entries.
     * 
     * @return a list of all FAQs
     */
    List<FAQ> getAllFAQs();
    
    /**
     * Adds a new FAQ entry.
     * 
     * @param faq the FAQ object to add
     */
    void addFAQ(FAQ faq);

    /**
     * Deletes an FAQ entry by its question.
     * 
     * @param question the question string identifying the FAQ to delete
     */
    void deleteFAQByQuestion(String question);

    /**
     * Searches FAQs containing the given keyword in their question or answer.
     * 
     * @param keyword the search term to look for
     * @return a list of matching FAQs
     */
    List<FAQ> searchFAQs(String keyword);
}
