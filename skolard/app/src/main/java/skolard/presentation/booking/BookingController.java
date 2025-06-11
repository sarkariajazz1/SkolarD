package skolard.presentation.booking;

import skolard.logic.rating.RatingHandler;
import skolard.logic.session.SessionHandler;
import skolard.logic.booking.BookingHandler;
import skolard.logic.payment.PaymentHandler;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.presentation.payment.PaymentView;

import javax.swing.*;
import java.util.List;

public class BookingController {
    // Reference to the UI view for booking
    private final BookingView view;
    // Business logic handler for booking operations
    private final BookingHandler bookingHandler;
    // Business logic handler for session management
    private final SessionHandler sessionHandler;
    // Business logic handler for rating management
    private final RatingHandler ratingHandler;
    // Business logic handler for payment processing
    private final PaymentHandler paymentHandler;
    // The currently logged-in student using the booking system
    private final Student student;

    // Constructor initializing all handlers, the view, and current student
    public BookingController(BookingView view, BookingHandler bookingHandler, SessionHandler sessionHandler,
                             RatingHandler ratingHandler, PaymentHandler paymentHandler, Student student) {
        this.view = view;
        this.bookingHandler = bookingHandler;
        this.sessionHandler = sessionHandler;
        this.ratingHandler = ratingHandler;
        this.paymentHandler = paymentHandler;
        this.student = student;
    }

    // Handles search action with filters for course, filter type, and time range
    public void onSearch(String courseInput, String filter, String start, String end) {
        try {
            // Get filtered sessions based on input criteria and student context
            List<Session> results = BookingInputHandler.getFilteredSessions(
                    courseInput, filter, start, end, student, bookingHandler
            );
            // Update the view table with search results
            view.updateSessionTable(results);
        } catch (Exception e) {
            // Show error status on the view if search fails
            view.showStatus("Error: " + e.getMessage());
        }
    }

    // Handles booking action when user selects a session row
    public void onBook(int rowIndex) {
        // Retrieve the selected session from the view by index
        Session session = view.getSelectedSession(rowIndex);
        if (session == null) return; // Do nothing if no session selected

        // Ask user to confirm booking with pre-payment warning
        int confirm = JOptionPane.showConfirmDialog(view,
                "Do you want to book this session with " + session.getTutor().getName() + "? Pre-payment is required.",
                "Confirm Booking", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Open payment dialog for processing payment
            PaymentView dialog = new PaymentView(view, paymentHandler, student);
            dialog.setVisible(true);

            if (dialog.wasPaid()) {
                // If payment successful, book session and create rating request
                sessionHandler.bookASession(student, session.getSessionId());
                ratingHandler.createRatingRequest(session, student);
                // Remove booked session from the view table
                view.removeSessionFromTable(rowIndex);
                // Inform user of successful booking
                JOptionPane.showMessageDialog(view, "Booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Inform user if payment was not completed
                JOptionPane.showMessageDialog(view, "Payment not completed.", "Cancelled", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Handles request to view detailed session info for a selected row
    public void onViewInfo(int rowIndex) {
        // Retrieve session from view by index
        Session session = view.getSelectedSession(rowIndex);
        if (session != null) {
            // Show detailed info dialog or panel for the selected session
            view.showSessionDetails(session);
        }
    }
}