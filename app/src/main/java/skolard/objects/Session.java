package skolard.objects;// updated package names

import java.time.LocalDateTime;

public class Session {
    private String sessionId;
    private Tutor tutor;
    private Student student;
    private LocalDateTime dateTime;
    private int durationMinutes;
    private String courseName; // New field
    private boolean booked;

    public Session(String sessionId, Tutor tutor, Student student, LocalDateTime dateTime, int durationMinutes, String courseName) {
        this.sessionId = sessionId;
        this.tutor = tutor;
        this.student = student;
        this.dateTime = dateTime;
        this.durationMinutes = durationMinutes;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
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
                ", duration=" + durationMinutes +
                " minutes}";
    }
}
