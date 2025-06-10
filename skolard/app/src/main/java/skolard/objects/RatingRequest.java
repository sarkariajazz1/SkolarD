package skolard.objects;

import java.time.LocalDateTime;

/**
 * Represents a single rating request sent to a student after a session.
 * Tracks completion and numeric ratings (1–5) only.
 */
public class RatingRequest {
    private final int id;
    private final Session session;
    private final Student student;
    private boolean completed;
    private boolean skipped;
    private int rating;
    private final LocalDateTime createdAt;

    public RatingRequest(int id, Session session, Student student, 
        LocalDateTime time, boolean completed, boolean skipped) {
        this.id = id;
        this.session = session;
        this.student = student;
        this.createdAt = LocalDateTime.now();
        this.completed = completed;
        this.skipped = skipped;
    }

    public int getId() {
        return id;
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

    public int getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void submit(int rating) {
        this.rating = rating;
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
            session.getTutor().getEmail(),
            student.getEmail(),
            rating
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
