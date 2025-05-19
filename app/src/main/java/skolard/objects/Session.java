package skolard.objects;// updated package names

import java.time.LocalDateTime;

public class Session {
    private String sessionId;
    private Tutor tutor;
    private Student student;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String courseName; // New field
    private boolean booked;

    public Session(String sessionId, Tutor tutor, Student student, LocalDateTime startDateTime, LocalDateTime endDateTime, String courseName) {
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
