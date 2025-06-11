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
    private final BookingView view;
    private final BookingHandler bookingHandler;
    private final SessionHandler sessionHandler;
    private final RatingHandler ratingHandler;
    private final PaymentHandler paymentHandler;
    private final Student student;

    public BookingController(BookingView view, BookingHandler bookingHandler, SessionHandler sessionHandler,
                             RatingHandler ratingHandler, PaymentHandler paymentHandler, Student student) {
        this.view = view;
        this.bookingHandler = bookingHandler;
        this.sessionHandler = sessionHandler;
        this.ratingHandler = ratingHandler;
        this.paymentHandler = paymentHandler;
        this.student = student;
    }

    public void onSearch(String courseInput, String filter, String start, String end) {
        try {
            List<Session> results = BookingInputHandler.getFilteredSessions(
                    courseInput, filter, start, end, student, bookingHandler
            );
            view.updateSessionTable(results);
        } catch (Exception e) {
            view.showStatus("Error: " + e.getMessage());
        }
    }

    public void onBook(int rowIndex) {
        Session session = view.getSelectedSession(rowIndex);
        if (session == null) return;

        int confirm = JOptionPane.showConfirmDialog(view,
                "Do you want to book this session with " + session.getTutor().getName() + "? Pre-payment is required.",
                "Confirm Booking", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            PaymentView dialog = new PaymentView(view, paymentHandler, student);
            dialog.setVisible(true);

            if (dialog.wasPaid()) {
                sessionHandler.bookASession(student, session.getSessionId());
                ratingHandler.createRatingRequest(session, student);
                view.removeSessionFromTable(rowIndex);
                JOptionPane.showMessageDialog(view, "Booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "Payment not completed.", "Cancelled", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void onViewInfo(int rowIndex) {
        Session session = view.getSelectedSession(rowIndex);
        if (session != null) {
            view.showSessionDetails(session);
        }
    }
}
