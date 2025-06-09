package skolard.objects;

import java.time.LocalDateTime;

/**
 * Represents a single rating request sent to a student after a session.
 * Tracks completion and numeric ratings (1–5) only.
 */
public class RatingRequest {
    private final Session session;
    private final Student student;
    private boolean skipped;
    private boolean completed;
    private int tutorRating;
    private int courseRating;
    private final LocalDateTime createdAt;

    public RatingRequest(Session session, Student student) {
        this.session = session;
        this.student = student;
        this.createdAt = LocalDateTime.now();
        this.completed = false;
        this.skipped = false;
    }

    public Session getSession() {
        return session;
    }

    public Student getStudent() {
        return student;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public int getTutorRating() {
        return tutorRating;
    }

    public int getCourseRating() {
        return courseRating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void submit(int tutorRating, int courseRating) {
        this.tutorRating = tutorRating;
        this.courseRating = courseRating;
        this.completed = true;
        this.skipped = false;
    }

    public void skip() {
        this.completed = true;
        this.skipped = true;
    }

    /**
     * Converts this request into a Feedback object using course rating only.
     */
    public Feedback toFeedback() {
        return new Feedback(
            session.getSessionId(),
            session.getCourseName(),
            session.getTutor().getName(),
            student.getName(),
            courseRating,
            tutorRating
        );
    }

    @Override
    public String toString() {
        return "RatingRequest for Session: " + session.getSessionId() +
               ", Student: " + student.getName() +
               ", Completed: " + completed +
               ", Skipped: " + skipped;
    }
}
