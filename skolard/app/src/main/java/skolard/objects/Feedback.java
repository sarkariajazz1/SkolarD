package skolard.objects;
public class Feedback {
    private final int sessionId;
    private final String courseName;
    private final String tutorEmail;
    private final String studentEmail;
    private final int courseRating;
    private final int tutorRating;

    public Feedback(int sessionId, String courseName, String tutorEmail,
                    String studentEmail, int courseRating, int tutorRating) {
        this.sessionId = sessionId;
        this.courseName = courseName;
        this.tutorEmail = tutorEmail;
        this.studentEmail = studentEmail;
        this.courseRating = courseRating;
        this.tutorRating = tutorRating;
    }

    public int getSessionId() { return sessionId; }
    public String getCourseName() { return courseName; }
    public String getTutorEmail() { return tutorEmail; }
    public String getStudentEmail() { return studentEmail; }
    public int getCourseRating() { return courseRating; }
    public int getTutorRating() { return tutorRating; }

    @Override
    public String toString() {
        return "Session: " + sessionId + ", Course: " + courseName +
               ", Tutor: " + tutorEmail + ", Student: " + studentEmail +
               ", Course Rating: " + courseRating + ", Tutor Rating: " + tutorRating;
    }
}
