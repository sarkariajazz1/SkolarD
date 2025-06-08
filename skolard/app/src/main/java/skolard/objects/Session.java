package skolard.objects;

import java.time.LocalDateTime;

public class Session {
    private int sessionId;
    private Tutor tutor;
    private Student student;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String courseName;

    public Session(int sessionId, Tutor tutor, Student student, LocalDateTime startDateTime, LocalDateTime endDateTime, String courseName) {
        this.sessionId = sessionId;
        this.tutor = tutor;
        this.student = student;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.courseName = courseName;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int id) {
        this.sessionId = id;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean isBooked() {
        return student != null;
    }

    public void bookSession(Student student) {
        if (this.booked) {
            throw new IllegalStateException("Session " + sessionId + " is already booked.");
        }

        this.student = student;
        this.tutor.addUpcomingSession(this);
        student.addUpcomingSession(this);

        System.out.println("Session " + sessionId + " booked by " + student.getName());
    }

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
