package skolard.logic.faq;

import skolard.objects.FAQ;
import skolard.persistence.FAQPersistence;

import java.util.List;

/**
 * Handles the business logic for accessing FAQ entries.
 */
public class FAQHandler {

    private final FAQPersistence faqPersistence;

    public FAQHandler(FAQPersistence faqPersistence) {
        this.faqPersistence = faqPersistence;
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
