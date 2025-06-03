package skolard.persistence;

import skolard.objects.FAQ;
import java.util.List;

public interface FAQPersistence {
    List<FAQ> getAllFAQs();
    
    void addFAQ(FAQ faq);
}