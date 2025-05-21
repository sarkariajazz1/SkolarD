package skolard.objects; // updated package names

import java.time.LocalDateTime;

/**
 * Represents a tutoring session scheduled between a tutor and a student.
 */
public class Session {
    private String sessionId;                     // Unique identifier for this session
    private Tutor tutor;                          // Tutor leading the session
    private Student student;                      // Student attending the session (null if not booked)
    private LocalDateTime startDateTime;          // Start time of the session
    private LocalDateTime endDateTime;            // End time of the session
    private String courseName;                    // Course associated with this session
    private boolean booked;                       // Flag to indicate if the session has been booked

    public Session(String sessionId, Tutor tutor, Student student,
                   LocalDateTime startDateTime, LocalDateTime endDateTime, String courseName) {
        this.sessionId = sessionId;
        this.tutor = tutor;
        this.student = student;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.courseName = courseName;
        this.booked = false;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void getEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean isBooked() {
        return booked;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Marks the session as booked and sets the student.
     * Also adds this session to the student's and tutor’s upcoming sessions.
     *
     * @param student the student booking the session
     */
    public void bookSession(Student student) {
        if (this.booked) {
            throw new IllegalStateException("This session is already booked.");
        }
        this.booked = true;
        this.student = student;
        this.tutor.setUpcomingSession(this);
        student.setUpcomingSession(this);
        System.out.println("Session " + sessionId + " booked by " + student.getName());
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", tutor=" + tutor.getName() +
                ", courseName='" + courseName + '\'' +
                ", startDateTime=" + startDateTime + '\'' +
                ", endDateTime=" + endDateTime;
    }
}
