package skolard.logic.session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
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
    public void createSession(Tutor tutor, LocalDateTime start, LocalDateTime end, String courseName){
        // Get all sessions for the tutor
        List<Session> tutorSessions = sessionPersistence.getSessionsByTutorEmail(tutor.getEmail());

        // Check for scheduling conflicts with existing sessions
        boolean hasConflict = tutorSessions.stream().anyMatch(session -> {
            LocalDateTime tutorStart = session.getStartDateTime();
            LocalDateTime tutorEnd = session.getEndDateTime();
            // Overlap if start < existingEnd && end > existingStart
            return start.isBefore(tutorEnd) && end.isAfter(tutorStart);
        });

        if (hasConflict) {
            throw new IllegalArgumentException("Session conflicts with existing sessions");
        }

        // No conflicts, add new session with unassigned student (null), Session id is -1 and will auto increment in database
        sessionPersistence.addSession(new Session(-1, tutor, null, start, end, courseName));
    }

    public void deleteSession(Tutor tutor, Session session){
        List<Session> tutorSessions = sessionPersistence.getSessionsByTutorEmail(tutor.getEmail());

        if(tutorSessions.contains(session)){
            sessionPersistence.removeSession(session.getSessionId());
        } else{
            throw new IllegalArgumentException("Session not found in database, cannot remove");
        }
    }

    /**
     * Books a session for a student if it is not already booked.
     *
     * @param user the user attempting to book the session (must be a Student)
     * @param sessionID the ID of the session to be booked
     */
    public void bookASession(Student student, int sessionID){
        // Retrieve the session to be booked
        Session session = sessionPersistence.getSessionById(sessionID);

        // Check if the session is already booked
        if(!session.isBooked()){
            session.bookSession(student); // Book the session for the student
            sessionPersistence.updateSession(session);
        } else {
            // Session is booked — check if by the same student or another
            if(session.getStudent().equals(student)){
                throw new IllegalArgumentException("Session is already booked");
            } else {
                throw new IllegalArgumentException("Session is already booked by someone else");
            }
        }
    }

    public void unbookASession(Student student, int sessionID){
        Session session = sessionPersistence.getSessionById(sessionID);

        if(session.isBooked()){
            session.unbookSession(student);
            sessionPersistence.updateSession(session);
        } else {
            throw new IllegalArgumentException("Session has not been booked");
        }
    }

    public void setStudentSessionLists(Student student){
        List<Session> studentSessions = sessionPersistence.getSessionsByStudentEmail(student.getEmail());
        LocalDateTime now = LocalDateTime.now();

        List<Session> upcomingSessions = studentSessions.stream()
            .filter(session -> session.getStartDateTime().isAfter(now))
            .collect(Collectors.toList());

        List<Session> pastSessions = studentSessions.stream()
            .filter(session -> session.getEndDateTime().isBefore(now))
            .collect(Collectors.toList());

        student.setPastSessions(pastSessions);
        student.setUpcomingSessions(upcomingSessions);
    }

    public void setTutorSessionLists(Tutor tutor){
        List<Session> tutorSessions = sessionPersistence.getSessionsByTutorEmail(tutor.getEmail());
        LocalDateTime now = LocalDateTime.now();

        List<Session> upcomingSessions = tutorSessions.stream()
            .filter(session -> session.getStartDateTime().isAfter(now))
            .collect(Collectors.toList());

        List<Session> pastSessions = tutorSessions.stream()
            .filter(session -> session.getEndDateTime().isBefore(now))
            .collect(Collectors.toList());

        tutor.setPastSessions(pastSessions);
        tutor.setUpcomingSessions(upcomingSessions);
    }
}
