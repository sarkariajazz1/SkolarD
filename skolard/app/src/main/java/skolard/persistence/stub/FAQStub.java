package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import skolard.objects.FAQ;
import skolard.persistence.FAQPersistence;

public class FAQStub implements FAQPersistence {
    private final List<FAQ> faqs;

    public FAQStub() {
        this.faqs = new ArrayList<>();
        seedFAQs();
    }

    private void seedFAQs() {
        faqs.add(new FAQ("How do I book a session?", "Go to the session view and choose an available time slot."));
        faqs.add(new FAQ("How can I contact a tutor?", "Use the messaging view to send a direct message."));
    }
    @Override
    public List<FAQ> getAllFAQs() {
        return new ArrayList<>(faqs);
    }

    @Override
    public void addFAQ(FAQ faq) {
        faqs.add(faq);
    }

    @Override
    public void deleteFAQByQuestion(String question) {
        faqs.removeIf(f -> f.getQuestion().equalsIgnoreCase(question));
    }

    @Override
    public List<FAQ> searchFAQs(String keyword) {
        return faqs.stream()
            .filter(f -> f.getQuestion().toLowerCase().contains(keyword.toLowerCase()) ||
                         f.getAnswer().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }
}
