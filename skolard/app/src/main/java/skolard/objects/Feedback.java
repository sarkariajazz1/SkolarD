package skolard.objects;

public class Feedback {
    private final int sessionId;
    private final String courseName;
    private final String tutorName;
    private final String studentName;
    private final int rating; // Typically the course/session rating

    public Feedback(int sessionId, String courseName, String tutorName, String studentId, int rating) {
        this.sessionId = sessionId;
        this.courseName = courseName;
        this.tutorName = tutorName;
        this.studentName = studentId;
        this.rating = rating;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTutorId() {
        return tutorName;
    }

    public String getStudentId() {
        return studentName;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Session: " + sessionId + ", Course: " + courseName +
               ", Tutor: " + tutorName + ", Student: " + studentName +
               ", Rating: " + rating;
    }
}
