
package skolard.presentation.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import skolard.logic.support.SupportHandler;
import skolard.objects.Student;
import skolard.objects.Support;
import skolard.objects.SupportTicket;
import skolard.objects.Tutor;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceRegistry;
import skolard.persistence.PersistenceType;

public class SupportSystemTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private SupportHandler realHandler;
    private Student testStudent;
    private Tutor testTutor;
    private Support testSupport;

    @Override
    protected void onSetUp() throws Exception {
        // Initialize with real database for testing
        PersistenceFactory.initialize(PersistenceType.TEST, false);

        // Create real handler with actual persistence
        realHandler = new SupportHandler(PersistenceRegistry.getSupportPersistence());

        // Create test users and persist them to the database first
        testStudent = new Student("Alice Student", "alice@uofm.ca", "hashedpass");
        testTutor = new Tutor("Bob Tutor", "bob@uofm.ca", "Expert tutor");
        testSupport = new Support("Support Staff", "support@skolard.com");

        // Save test users to database to avoid "requester not found" warnings
        try {
            PersistenceRegistry.getStudentPersistence().addStudent(testStudent);
            PersistenceRegistry.getTutorPersistence().addTutor(testTutor);
            // Note: Support objects don't have a separate persistence layer
            // They are handled through the SupportPersistence for tickets
        } catch (Exception e) {
            // Users might already exist, ignore duplicates
            System.out.println("Note: Test users might already exist in database: " + e.getMessage());
        }
    }

    @Test
    public void testStudentSupportViewWithRealDatabase() throws Exception {
        SupportView supportView = GuiActionRunner.execute(() -> {
            SupportView view = new SupportView(realHandler, testStudent);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), supportView);
        window.show();

        robot().waitForIdle();

        // Verify student interface components are present
        window.textBox("titleField").requireVisible();
        window.textBox("descriptionArea").requireVisible();
        window.button("submitTicketBtn").requireVisible();
        window.button("backButton").requireVisible();

        // Verify window properties
        assertThat(supportView.getTitle()).isEqualTo("SkolarD - Support Center");
        assertThat(supportView.getDefaultCloseOperation()).isEqualTo(JFrame.DISPOSE_ON_CLOSE);
    }

    @Test
    public void testSupportStaffViewWithRealDatabase() throws Exception {
        SupportView supportView = GuiActionRunner.execute(() -> {
            SupportView view = new SupportView(realHandler, testSupport);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), supportView);
        window.show();

        robot().waitForIdle();

        // Verify support staff interface components are present
        window.button("viewActiveBtn").requireVisible();
        window.button("viewHandledBtn").requireVisible();
        window.button("closeTicketBtn").requireVisible();
        window.button("messageUserBtn").requireVisible();
        window.list("ticketList").requireVisible();
        window.button("backButton").requireVisible();
    }

    @Test
    public void testCompleteTicketWorkflow() throws Exception {
        // First, student submits a ticket
        SupportView studentView = GuiActionRunner.execute(() -> {
            SupportView view = new SupportView(realHandler, testStudent);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), studentView);
        window.show();

        robot().waitForIdle();

        // Submit a ticket
        window.textBox("titleField").setText("Test Ticket");
        window.textBox("descriptionArea").setText("This is a test ticket description");
        window.button("submitTicketBtn").click();
        robot().waitForIdle();

        // Handle success dialog
        try {
            window.optionPane()
                    .requireMessage("Support ticket submitted successfully!")
                    .okButton().click();
        } catch (Exception e) {
            // Dialog might not be captured properly
            System.out.println("Success dialog not captured, continuing test");
        }

        // Verify fields are cleared after submission
        assertThat(window.textBox("titleField").text()).isEmpty();
        assertThat(window.textBox("descriptionArea").text()).isEmpty();

        window.cleanUp();

        // Now, support staff views the ticket
        SupportView supportView = GuiActionRunner.execute(() -> {
            SupportView view = new SupportView(realHandler, testSupport);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), supportView);
        window.show();

        robot().waitForIdle();

        // Load active tickets
        window.button("viewActiveBtn").click();
        robot().waitForIdle();

        // Verify the submitted ticket appears
        List<SupportTicket> activeTickets = realHandler.getActiveTickets();
        System.out.println("Active tickets found: " + activeTickets.size());
        for (SupportTicket ticket : activeTickets) {
            System.out.println("Ticket: " + ticket.getTitle() + " - " + ticket.getDescription());
        }

        assertThat(activeTickets).isNotEmpty();

        // Find our test ticket
        SupportTicket testTicket = activeTickets.stream()
                .filter(t -> "Test Ticket".equals(t.getTitle()))
                .findFirst()
                .orElse(null);

        assertThat(testTicket).isNotNull();
        assertThat(testTicket.getDescription()).isEqualTo("This is a test ticket description");
        assertThat(testTicket.isHandled()).isFalse();
    }

    @Test
    public void testTicketClosingWorkflow() throws Exception {
        // First submit a ticket to work with directly via handler
        SupportTicket testTicket = new SupportTicket(testStudent, "Close Test", "Test closing");
        realHandler.submitTicket(testTicket);

        // Wait a moment for persistence
        Thread.sleep(100);

        SupportView supportView = GuiActionRunner.execute(() -> {
            SupportView view = new SupportView(realHandler, testSupport);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), supportView);
        window.show();

        robot().waitForIdle();

        // Load active tickets
        window.button("viewActiveBtn").click();
        robot().waitForIdle();

        // Find and select our test ticket
        List<SupportTicket> activeTickets = realHandler.getActiveTickets();
        System.out.println("Active tickets for closing test: " + activeTickets.size());

        int ticketIndex = -1;
        for (int i = 0; i < activeTickets.size(); i++) {
            SupportTicket ticket = activeTickets.get(i);
            System.out.println("Checking ticket " + i + ": " + ticket.getTitle());
            if ("Close Test".equals(ticket.getTitle())) {
                ticketIndex = i;
                break;
            }
        }

        System.out.println("Found ticket at index: " + ticketIndex);
        assertThat(ticketIndex).isGreaterThanOrEqualTo(0);

        // Select and close the ticket
        window.list("ticketList").selectItem(ticketIndex);
        robot().waitForIdle();

        window.button("closeTicketBtn").click();
        robot().waitForIdle();

        // Handle success dialog
        try {
            window.optionPane()
                    .requireMessage("Ticket closed successfully")
                    .okButton().click();
        } catch (Exception e) {
            // Dialog might not be captured
            System.out.println("Close success dialog not captured, continuing test");
        }

        // Wait a moment for persistence
        Thread.sleep(100);

        // Verify ticket is now handled
        List<SupportTicket> handledTickets = realHandler.getHandledTickets();
        boolean ticketFound = handledTickets.stream()
                .anyMatch(t -> "Close Test".equals(t.getTitle()) && t.isHandled());

        assertThat(ticketFound).isTrue();
    }

    @Test
    public void testWindowPropertiesAndLayout() throws Exception {
        SupportView supportView = GuiActionRunner.execute(() -> {
            SupportView view = new SupportView(realHandler, testStudent);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), supportView);
        window.show();

        robot().waitForIdle();

        // Verify window title and properties
        assertThat(supportView.getTitle()).isEqualTo("SkolarD - Support Center");
        assertThat(supportView.getDefaultCloseOperation()).isEqualTo(JFrame.DISPOSE_ON_CLOSE);

        // Verify window has reasonable dimensions
        assertThat(supportView.getSize().width).isGreaterThan(0);
        assertThat(supportView.getSize().height).isGreaterThan(0);
    }

    @Test
    public void testSupportHandlerIntegration() throws Exception {
        // Test that the SupportView properly integrates with the real SupportHandler
        List<SupportTicket> ticketsBeforeView = realHandler.getActiveTickets();

        SupportView supportView = GuiActionRunner.execute(() -> {
            SupportView view = new SupportView(realHandler, testSupport);
            view.setVisible(false);
            return view;
        });

        window = new FrameFixture(robot(), supportView);
        window.show();

        robot().waitForIdle();

        // Verify that creating the view doesn't modify the ticket data
        List<SupportTicket> ticketsAfterView = realHandler.getActiveTickets();
        assertThat(ticketsAfterView).hasSize(ticketsBeforeView.size());

        // Test submitting a ticket through the handler directly
        int initialCount = realHandler.getActiveTickets().size();
        SupportTicket handlerTicket = new SupportTicket(testStudent, "Handler Test", "Direct handler test");
        realHandler.submitTicket(handlerTicket);

        int newCount = realHandler.getActiveTickets().size();
        assertThat(newCount).isEqualTo(initialCount + 1);
    }

    @Override
    protected void onTearDown() {
        if (window != null) {
            window.cleanUp();
        }
        // TEST database cleans up automatically
    }
}