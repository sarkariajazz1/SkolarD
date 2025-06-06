package skolard.objects;

import java.time.LocalDateTime;

/**
 * Represents a tutoring session between a Tutor and a Student.
 * Tracks scheduling details and booking status.
 */
public class Session {
    private int sessionId; // Unique identifier for the session
    private Tutor tutor;      // Tutor assigned to the session
    private Student student;  // Student attending the session
    private LocalDateTime startDateTime; // When the session starts
    private LocalDateTime endDateTime;   // When the session ends
    private String courseName;           // Course associated with the session
    private boolean booked;              // Whether the session is booked

    public Session(int sessionId, Tutor tutor, Student student, LocalDateTime startDateTime, LocalDateTime endDateTime, String courseName) {
        this.sessionId = sessionId;
        this.tutor = tutor;
        this.student = student;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.courseName = courseName;
        this.booked = false; // Default to unbooked
    }

    public int getSessionId() {
        return sessionId;
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

    public void setEndDateTime(LocalDateTime endDateTime) {
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
     * Books the session with a student.
     * Also updates tutor and student with the session info.
     * Throws an exception if the session is already booked.
     */
    public void bookSession(Student student) {
        if (this.booked) {
            throw new IllegalStateException("This session is already booked.");
        }
        this.booked = true;
        this.student = student;
        this.tutor.addUpcomingSession(this); // Add to tutor's schedule
        student.addUpcomingSession(this);    // Add to student's schedule
        System.out.println("Session " + sessionId + " booked by " + student.getName());
    }

    public void unbookSession(Student student){
        Session oldSession = this;

        if(!this.booked){
            throw new IllegalArgumentException("This session is not booked");
        }

        this.booked = false;
        this.student = null;
        student.replaceUpcomingSession(oldSession, this);

        System.out.println("Session " + sessionId + " unbooked by " + student.getName());
    }

}
