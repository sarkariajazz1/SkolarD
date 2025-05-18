import java.time.LocalDateTime;

public class Session {
    private String sessionId;
    private Tutor tutor;
    private Student student;
    private LocalDateTime dateTime;
    private int durationMinutes;

    public Session(String sessionId, Tutor tutor, Student student, LocalDateTime dateTime, int durationMinutes) {
        this.sessionId = sessionId;
        this.tutor = tutor;
        this.student = student;
        this.dateTime = dateTime;
        this.durationMinutes = durationMinutes;
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

}
