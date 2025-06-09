package skolard.logic.session;

import skolard.objects.Student;
import skolard.objects.Session;
import skolard.persistence.SessionPersistence;

public class SessionBooking {
    private final SessionPersistence sessionPersistence;

    public SessionBooking(SessionPersistence sessionPersistence) {
        this.sessionPersistence = sessionPersistence;
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
        } else {
            throw new IllegalArgumentException("Session has not been booked");
        }
    }
}
