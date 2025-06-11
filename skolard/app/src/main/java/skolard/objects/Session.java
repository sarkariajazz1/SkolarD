package skolard.objects;

import java.time.LocalDateTime;

/**
 * Represents a tutoring session with details including tutor, student, timing, and course.
 */
public class Session {
    // Unique identifier for this session
    private int sessionId;

    // Tutor offering the session
    private Tutor tutor;

    // Student booked for the session; null if not booked
    private Student student;

    // Start time of the session
    private LocalDateTime startDateTime;

    // End time of the session
    private LocalDateTime endDateTime;

    // Name of the course for this session
    private String courseName;

    /**
     * Constructs a Session with all details specified.
     *
     * @param sessionId unique ID for the session
     * @param tutor     tutor offering the session
     * @param student   student booked (or null if unbooked)
     * @param startDateTime session start time
     * @param endDateTime   session end time
     * @param courseName    name of the course
     */
    public Session(int sessionId, Tutor tutor, Student student, LocalDateTime startDateTime, LocalDateTime endDateTime, String courseName) {
        this.sessionId = sessionId;
        this.tutor = tutor;
        this.student = student;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.courseName = courseName;
    }

    /** @return session's unique ID */
    public int getSessionId() {
        return sessionId;
    }

    /** Set the session ID */
    public void setSessionId(int id) {
        this.sessionId = id;
    }

    /** @return the tutor for this session */
    public Tutor getTutor() {
        return tutor;
    }

    /** Set the tutor */
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    /** @return the student booked for this session, or null if none */
    public Student getStudent() {
        return student;
    }

    /** Set the student */
    public void setStudent(Student student) {
        this.student = student;
    }

    /** @return the start time of the session */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /** Set the start time */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /** @return the end time of the session */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /** Set the end time */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /** @return the course name for the session */
    public String getCourseName() {
        return courseName;
    }

    /** Set the course name */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Checks if the session is currently booked by a student.
     *
     * @return true if booked, false otherwise
     */
    public boolean isBooked() {
        return student != null;
    }

    /**
     * Books this session for the specified student.
     * Throws if already booked.
     * Also adds this session to the tutor's and student's upcoming sessions.
     *
     * @param student student booking the session
     */
    public void bookSession(Student student) {
        if (this.student != null) {
            throw new IllegalStateException("Session " + sessionId + " is already booked.");
        }

        this.student = student;
        tutor.addUpcomingSession(this);
        student.addUpcomingSession(this);

        System.out.println("Session " + sessionId + " booked by " + student.getName());
    }

    /**
     * Unbooks this session if it is booked by the specified student.
     * Throws if session not booked or if the student trying to unbook is not the one who booked it.
     * Removes the session from the student's upcoming sessions.
     *
     * @param student student attempting to unbook the session
     */
    public void unbookSession(Student student) {
        if (!isBooked()) {
            throw new IllegalArgumentException("This session is not booked");
        }

        if (!this.student.getEmail().equals(student.getEmail())) {
            throw new IllegalArgumentException("You can only unbook sessions you booked");
        }

        this.student = null;
        student.removeUpcomingSession(this);

        System.out.println("Session " + sessionId + " unbooked by " + student.getName());
    }
}
