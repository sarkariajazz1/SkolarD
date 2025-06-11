package skolard.objects;

/**
 * Represents feedback given by a student for a completed tutoring session.
 * Includes session details, involved users, and a numerical rating.
 */
public class Feedback {
    // ID of the session the feedback is for
    private final int sessionId;

    // Name of the course associated with the session
    private final String courseName;

    // Email of the tutor who conducted the session
    private final String tutorEmail;

    // Email of the student who attended the session and gave the feedback
    private final String studentEmail;

    // Numerical rating given by the student (e.g., 1-5)
    private final int rating;

    /**
     * Constructs a Feedback object with the specified details.
     *
     * @param sessionId     the ID of the session
     * @param courseName    the name of the course
     * @param tutorEmail    the tutor's email
     * @param studentEmail  the student's email
     * @param rating        the rating given by the student
     */
    public Feedback(int sessionId, String courseName, String tutorEmail,
                    String studentEmail, int rating) {
        this.sessionId = sessionId;
        this.courseName = courseName;
        this.tutorEmail = tutorEmail;
        this.studentEmail = studentEmail;
        this.rating = rating;
    }

    /**
     * @return the session ID
     */
    public int getSessionId() { return sessionId; }

    /**
     * @return the course name
     */
    public String getCourseName() { return courseName; }

    /**
     * @return the tutor's email
     */
    public String getTutorEmail() { return tutorEmail; }

    /**
     * @return the student's email
     */
    public String getStudentEmail() { return studentEmail; }

    /**
     * @return the rating value
     */
    public int getRating() { return rating; }

    /**
     * @return a formatted string representing the feedback details
     */
    @Override
    public String toString() {
        return "Session: " + sessionId + ", Course: " + courseName +
               ", Tutor: " + tutorEmail + ", Student: " + studentEmail +
               ", Tutor Rating: " + rating;
    }
}
