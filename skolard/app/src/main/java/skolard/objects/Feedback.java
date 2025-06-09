package skolard.objects;
public class Feedback {
    private final int sessionId;
    private final String courseName;
    private final String tutorEmail;
    private final String studentEmail;
    private final int rating;

    public Feedback(int sessionId, String courseName, String tutorEmail,
                    String studentEmail, int rating) {
        this.sessionId = sessionId;
        this.courseName = courseName;
        this.tutorEmail = tutorEmail;
        this.studentEmail = studentEmail;
        this.rating = rating;
    }

    public int getSessionId() { return sessionId; }
    public String getCourseName() { return courseName; }
    public String getTutorEmail() { return tutorEmail; }
    public String getStudentEmail() { return studentEmail; }
    public int getRating() { return rating; }

    @Override
    public String toString() {
        return "Session: " + sessionId + ", Course: " + courseName +
               ", Tutor: " + tutorEmail + ", Student: " + studentEmail +
               ", Tutor Rating: " + rating;
    }
}
