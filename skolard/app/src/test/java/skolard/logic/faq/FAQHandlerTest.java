package skolard.logic.faq;

import skolard.objects.FAQ;
import skolard.persistence.FAQPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class FAQHandlerTest {

    private FAQPersistence mockPersistence;
    private FAQHandler faqHandler;

    @BeforeEach
    public void setup() {
        mockPersistence = mock(FAQPersistence.class);
        faqHandler = new FAQHandler(mockPersistence);
    }

    @Test
    public void testGetAllFAQs() {
        List<FAQ> sample = Arrays.asList(
                new FAQ("Q1", "A1"),
                new FAQ("Q2", "A2")
        );
        when(mockPersistence.getAllFAQs()).thenReturn(sample);

        List<FAQ> result = faqHandler.getAllFAQs();

        assertEquals(2, result.size());
        assertEquals("Q1", result.get(0).getQuestion());
        verify(mockPersistence, times(1)).getAllFAQs();
    }

    @Test
    public void testAddFAQ() {
        FAQ faq = new FAQ("What is SkolarD?", "It's a tutor matching platform.");
        faqHandler.addFAQ(faq.getQuestion(), faq.getAnswer());

        verify(mockPersistence, times(1)).addFAQ(argThat(f ->
                f.getQuestion().equals("What is SkolarD?") &&
                f.getAnswer().equals("It's a tutor matching platform.")
        ));
    }

    @Test
    public void testDeleteFAQ() {
        String question = "How do I reset my password?";
        faqHandler.deleteFAQ(question);

        verify(mockPersistence, times(1)).deleteFAQByQuestion(eq(question));
    }

    @Test
    public void testSearchFAQsReturnsMatch() {
        FAQ match = new FAQ("How to reschedule?", "Go to dashboard > sessions.");
        when(mockPersistence.searchFAQs("reschedule")).thenReturn(List.of(match));

        List<FAQ> results = faqHandler.searchFAQs("reschedule");

        assertEquals(1, results.size());
        assertEquals("How to reschedule?", results.get(0).getQuestion());
    }

    @Test
    public void testSearchFAQsNoMatch() {
        when(mockPersistence.searchFAQs("invalid")).thenReturn(Collections.emptyList());

        List<FAQ> results = faqHandler.searchFAQs("invalid");

        assertTrue(results.isEmpty());
    }

    @Test
    public void testAddFAQ_NullQuestionAndAnswer() {
        faqHandler.addFAQ(null, null);

        verify(mockPersistence, times(1)).addFAQ(argThat(f ->
            f.getQuestion() == null && f.getAnswer() == null
        ));
    }
    
    @Test
    public void testDeleteFAQ_Null() {
        faqHandler.deleteFAQ(null);
        verify(mockPersistence, times(1)).deleteFAQByQuestion(null);
    }

    @Test
    public void testSearchFAQs_NullKeyword() {
        when(mockPersistence.searchFAQs(null)).thenReturn(Collections.emptyList());

        List<FAQ> result = faqHandler.searchFAQs(null);
        assertTrue(result.isEmpty());
        verify(mockPersistence).searchFAQs(null);
    }

    @Test
    public void testGetAllFAQsEmpty() {
        when(mockPersistence.getAllFAQs()).thenReturn(Collections.emptyList());

        List<FAQ> result = faqHandler.getAllFAQs();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAddFAQWithEmptyValues() {
        faqHandler.addFAQ("", "");
        verify(mockPersistence, times(1)).addFAQ(argThat(f ->
                f.getQuestion().isEmpty() && f.getAnswer().isEmpty()
        ));
    }
}
