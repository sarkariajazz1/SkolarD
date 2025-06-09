package skolard.objects;
public class Feedback {
    private final int sessionId;
    private final String courseName;
    private final String tutorName;
    private final String studentName;
    private final int courseRating;
    private final int tutorRating;

    public Feedback(int sessionId, String courseName, String tutorName,
                    String studentName, int courseRating, int tutorRating) {
        this.sessionId = sessionId;
        this.courseName = courseName;
        this.tutorName = tutorName;
        this.studentName = studentName;
        this.courseRating = courseRating;
        this.tutorRating = tutorRating;
    }

    public int getSessionId() { return sessionId; }
    public String getCourseName() { return courseName; }
    public String getTutorName() { return tutorName; }
    public String getStudentName() { return studentName; }
    public int getCourseRating() { return courseRating; }
    public int getTutorRating() { return tutorRating; }

    @Override
    public String toString() {
        return "Session: " + sessionId + ", Course: " + courseName +
               ", Tutor: " + tutorName + ", Student: " + studentName +
               ", Course Rating: " + courseRating + ", Tutor Rating: " + tutorRating;
    }
}
