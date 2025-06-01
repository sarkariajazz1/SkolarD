package skolard.objects;

public class Feedback {
    private final String sessionId;
    private final String courseName;
    private final String tutorId;
    private final String studentId;
    private final int rating;
    private final String comment;

    public Feedback(String sessionId, String courseName, String tutorId, String studentId, int rating, String comment) {
        this.sessionId = sessionId;
        this.courseName = courseName;
        this.tutorId = tutorId;
        this.studentId = studentId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getSessionId() { return sessionId; }
    public String getCourseName() { return courseName; }
    public String getTutorId() { return tutorId; }
    public String getStudentId() { return studentId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
}
