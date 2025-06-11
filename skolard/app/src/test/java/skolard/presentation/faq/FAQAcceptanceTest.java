
package skolard.presentation.faq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.Mockito;

import skolard.logic.faq.FAQHandler;
import skolard.objects.FAQ;

public class FAQAcceptanceTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private FAQHandler mockHandler;

    @Override
    protected void onSetUp() {
        mockHandler = Mockito.mock(FAQHandler.class);
    }

    @Test
    public void testFAQViewDisplaysQuestions() throws Exception {
        // Mock FAQ data - use two-parameter constructor
        List<FAQ> mockFAQs = Arrays.asList(
                new FAQ("How do I sign up?", "Click the Sign Up button and fill in your details."),
                new FAQ("How do I book a session?", "Go to the booking page and select a tutor."),
                new FAQ("How do I reset my password?", "Contact support for password reset assistance.")
        );
        when(mockHandler.getAllFAQs()).thenReturn(mockFAQs);

        FAQView faqView = GuiActionRunner.execute(() -> {
            FAQView view = new FAQView(mockHandler);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), faqView);
        window.show();

        robot().waitForIdle();

        // Verify the header is displayed
        window.label("headerLabel").requireText("Frequently Asked Questions");

        // Verify FAQ questions are displayed (using index-based naming)
        window.label("question_0").requireVisible();
        window.label("question_1").requireVisible();
        window.label("question_2").requireVisible();

        // Verify FAQ answers are displayed
        window.label("answer_0").requireVisible();
        window.label("answer_1").requireVisible();
        window.label("answer_2").requireVisible();

        // Verify scroll pane is present
        window.scrollPane("faqScrollPane").requireVisible();

        // Verify handler was called
        verify(mockHandler).getAllFAQs();
    }

    @Test
    public void testBackButtonClosesWindow() throws Exception {
        // Mock empty FAQ list
        when(mockHandler.getAllFAQs()).thenReturn(Arrays.asList());

        FAQView faqView = GuiActionRunner.execute(() -> {
            FAQView view = new FAQView(mockHandler);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), faqView);
        window.show();

        robot().waitForIdle();

        // Verify back button exists
        window.button("backButton").requireVisible();
        window.button("backButton").requireText("Back");

        // Click back button - this should dispose the window
        window.button("backButton").click();
        robot().waitForIdle();

        // Window should be disposed (no longer visible)
        assertThat(faqView.isDisplayable()).isFalse();
    }

    @Test
    public void testEmptyFAQListHandling() throws Exception {
        // Mock empty FAQ list
        when(mockHandler.getAllFAQs()).thenReturn(Arrays.asList());

        FAQView faqView = GuiActionRunner.execute(() -> {
            FAQView view = new FAQView(mockHandler);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), faqView);
        window.show();

        robot().waitForIdle();

        // Verify basic components still exist
        window.label("headerLabel").requireVisible();
        window.button("backButton").requireVisible();
        window.scrollPane("faqScrollPane").requireVisible();

        // Verify handler was called
        verify(mockHandler).getAllFAQs();
    }

    @Test
    public void testSingleFAQDisplay() throws Exception {
        // Mock single FAQ
        List<FAQ> singleFAQ = Arrays.asList(
                new FAQ("What is SkolarD?", "SkolarD is a tutoring platform connecting students with tutors.")
        );
        when(mockHandler.getAllFAQs()).thenReturn(singleFAQ);

        FAQView faqView = GuiActionRunner.execute(() -> {
            FAQView view = new FAQView(mockHandler);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), faqView);
        window.show();

        robot().waitForIdle();

        // Verify the single FAQ is displayed (using index 0)
        window.label("question_0").requireVisible();
        window.label("answer_0").requireVisible();

        // Verify the question text contains expected content
        String questionText = window.label("question_0").text();
        assertThat(questionText).contains("What is SkolarD?");

        // Verify the answer text contains expected content
        String answerText = window.label("answer_0").text();
        assertThat(answerText).contains("tutoring platform");

        verify(mockHandler).getAllFAQs();
    }

    @Test
    public void testFAQContentFormatting() throws Exception {
        // Mock FAQ with special characters and longer text
        List<FAQ> mockFAQs = Arrays.asList(
                new FAQ("How do I use special characters: @, #, &?",
                        "Special characters are supported. You can use @ for emails, # for tags, & for references.")
        );
        when(mockHandler.getAllFAQs()).thenReturn(mockFAQs);

        FAQView faqView = GuiActionRunner.execute(() -> {
            FAQView view = new FAQView(mockHandler);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), faqView);
        window.show();

        robot().waitForIdle();

        // Verify FAQ with special characters is displayed correctly
        window.label("question_0").requireVisible();
        window.label("answer_0").requireVisible();

        // Verify HTML formatting is applied (questions should be bold, answers italic)
        String questionText = window.label("question_0").text();
        String answerText = window.label("answer_0").text();

        assertThat(questionText).contains("<html><b>Q:");
        assertThat(answerText).contains("<html><i>A:");

        verify(mockHandler).getAllFAQs();
    }

    @Override
    protected void onTearDown() {
        if (window != null) {
            window.cleanUp();
        }
    }
}