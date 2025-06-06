package skolard.logic.session;

import java.time.LocalDateTime;
import java.util.List;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.SessionPersistence;

/**
 * Handles the creation and booking of tutoring sessions.
 */
public class SessionHandler {
    private SessionPersistence sessionPersistence;

    /**
     * Constructor that initializes the session persistence layer.
     *
     * @param sessionPersistence the persistence layer to handle session data
     */
    public SessionHandler(SessionPersistence sessionPersistence){
        this.sessionPersistence = sessionPersistence;
    }

    /**
     * Creates a new session for a tutor if there is no scheduling conflict.
     *
     * @param user the user attempting to create the session (must be a Tutor)
     * @param start the start time of the session
     * @param end the end time of the session
     * @param courseName the name of the course for the session
     */
    public void createSession(User user, LocalDateTime start, LocalDateTime end, String courseName){
        if(user instanceof Tutor){
            Tutor tutor = (Tutor) user;
            
            // Get all sessions for the tutor
            List<Session> tutorSessions = sessionPersistence.getSessionsByTutorEmail(tutor.getEmail());

            // Check for scheduling conflicts with existing sessions
            for(int i = 0; i < tutorSessions.size(); i++){
                LocalDateTime tutorStartTime = tutorSessions.get(i).getStartDateTime();
                LocalDateTime tutorEndTime = tutorSessions.get(i).getEndDateTime();

                // Conflict if new session overlaps with existing session
                if(start.isAfter(tutorStartTime) && start.isBefore(tutorEndTime) 
                        || end.isAfter(tutorStartTime) && end.isBefore(tutorEndTime)){
                    throw new IllegalArgumentException("Session conflicts within existing sessions");
                } else if(tutorStartTime.isAfter(start) && tutorStartTime.isBefore(end) 
                        || tutorEndTime.isAfter(start) && tutorEndTime.isBefore(end)){
                    throw new IllegalArgumentException("Existing sessions conflicts within session");
                }
            }

            // No conflicts, add new session with unassigned student (null), Session id is -1 and will auto increment in database
            sessionPersistence.addSession(new Session(-1, tutor, null, start, end, courseName));
        }
    }

    /**
     * Books a session for a student if it is not already booked.
     *
     * @param user the user attempting to book the session (must be a Student)
     * @param sessionID the ID of the session to be booked
     */
    public void bookASession(User user, int sessionID){
        if(user instanceof Student){
            Student student = (Student) user;

            // Retrieve the session to be booked
            Session session = sessionPersistence.getSessionById(sessionID);

            // Check if the session is already booked
            if(!session.isBooked()){
                session.bookSession(student); // Book the session for the student
            } else {
                // Session is booked — check if by the same student or another
                if(session.getStudent().equals(user)){
                    throw new IllegalArgumentException("Session is already booked");
                } else {
                    throw new IllegalArgumentException("Session is already booked by someone else");
                }
            }
        }
    }
}
