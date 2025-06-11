package skolard.logic.session;

import skolard.objects.Student;
import skolard.objects.RatingRequest;

import java.util.List;

import skolard.objects.Session;
import skolard.persistence.RatingRequestPersistence;
import skolard.persistence.SessionPersistence;


/**
 * Handles booking and unbooking of tutoring sessions for students.
 *
 */
public class SessionBooking {
    private final SessionPersistence sessionPersistence;
    private final RatingRequestPersistence requestPersistence;

    /**
     * Constructor that initializes the session persistence layer.
     *
     * @param sessionPersistence the persistence layer to handle session data
     */
    public SessionBooking(SessionPersistence sessionPersistence, RatingRequestPersistence requestPersistence) {
        this.sessionPersistence = sessionPersistence;
        this.requestPersistence = requestPersistence;
    }

    /**
     * Books a session for a student if it is not already booked.
     *
     * @param user the user attempting to book the session (must be a Student)
     * @param sessionID the ID of the session to be booked
     */
    public void bookASession(Student student, int sessionID) {
        Session session = sessionPersistence.getSessionById(sessionID);
        if (!session.isBooked()) {
            session.bookSession(student);
            sessionPersistence.updateSession(session);
        } else {
            if (session.getStudent().equals(student)) {
                throw new IllegalArgumentException("Session is already booked");
            } else {
                throw new IllegalArgumentException("Session is already booked by someone else");
            }
        }
    }

    /**
     * Unbooks a session for a student if the session is currently booked.
     *
     * @param student the student attempting to unbook the session
     * @param sessionID the ID of the session to be unbooked
     */
    public void unbookASession(Student student, int sessionID) {
        Session session = sessionPersistence.getSessionById(sessionID);
        if (session.isBooked()) {
            session.unbookSession(student);
            sessionPersistence.updateSession(session);
            skipRequests(sessionID);
        } else {
            throw new IllegalArgumentException("Session has not been booked");
        }
    }

    /**
     * Marks all pending rating requests for the given session as skipped.
     *
     * @param sessionID the ID of the session
     */
    private void skipRequests(int sessionID) {
        // Get all pending requests for the session
        List<RatingRequest> requests = requestPersistence.getPendingSessionRequest(sessionID);

        // Skip each request and update it in storage
        for (RatingRequest r : requests) {
            r.skip();
            requestPersistence.updateRequest(r);
        }
    }
}
