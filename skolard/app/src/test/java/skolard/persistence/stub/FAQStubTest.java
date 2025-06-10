package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.FAQ;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FAQStubTest {

    private FAQStub faqStub;

    @BeforeEach
    void setUp() {
        faqStub = new FAQStub();
    }

    @Test
    void testGetAllFAQsReturnsSeededData() {
        List<FAQ> faqs = faqStub.getAllFAQs();
        assertEquals(2, faqs.size());
        assertTrue(faqs.stream().anyMatch(f -> f.getQuestion().contains("book a session")));
    }

    @Test
    void testAddFAQIncreasesListSize() {
        FAQ newFaq = new FAQ("What is Skolard?", "Skolard is a tutoring platform.");
        faqStub.addFAQ(newFaq);

        List<FAQ> faqs = faqStub.getAllFAQs();
        assertEquals(3, faqs.size());
        assertTrue(faqs.contains(newFaq));
    }

    @Test
    void testDeleteFAQByQuestionRemovesCorrectFAQ() {
        faqStub.deleteFAQByQuestion("How do I book a session?");
        List<FAQ> faqs = faqStub.getAllFAQs();

        assertEquals(1, faqs.size());
        assertFalse(faqs.stream().anyMatch(f -> f.getQuestion().equalsIgnoreCase("How do I book a session?")));
    }

    @Test
    void testDeleteFAQByQuestionCaseInsensitive() {
        faqStub.deleteFAQByQuestion("how DO i BOOK a session?");
        List<FAQ> faqs = faqStub.getAllFAQs();

        assertEquals(1, faqs.size());
    }

    @Test
    void testSearchFAQsByKeywordInQuestion() {
        List<FAQ> results = faqStub.searchFAQs("book");
        assertEquals(1, results.size());
        assertTrue(results.get(0).getQuestion().contains("book"));
    }

    @Test
    void testSearchFAQsByKeywordInAnswer() {
        List<FAQ> results = faqStub.searchFAQs("messaging");
        assertEquals(1, results.size());
        assertTrue(results.get(0).getAnswer().contains("messaging"));
    }

    @Test
    void testSearchFAQsNoMatchReturnsEmptyList() {
        List<FAQ> results = faqStub.searchFAQs("nonexistent");
        assertTrue(results.isEmpty());
    }
}

