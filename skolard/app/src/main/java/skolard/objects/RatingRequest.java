package skolard.objects;

import java.time.LocalDateTime;

/**
 * Represents a single rating request sent to a student after a tutoring session.
 * Tracks whether the request was completed or skipped, along with the numeric rating (1–5).
 */
public class RatingRequest {
    // Unique identifier for this rating request
    private final int id;

    // The session this rating request is associated with
    private final Session session;

    // The student who should respond to this rating request
    private final Student student;

    // Flag indicating if the rating request has been completed (either submitted or skipped)
    private boolean completed;

    // Flag indicating if the rating request was explicitly skipped
    private boolean skipped;

    // Numeric rating value submitted by the student (1 to 5)
    private int rating;

    // Timestamp when the rating request was created
    private final LocalDateTime createdAt;

    /**
     * Constructs a new RatingRequest.
     *
     * @param id         unique ID of the request
     * @param session    session to be rated
     * @param student    student who will rate
     * @param time       timestamp (ignored; createdAt is set to now)
     * @param completed  whether request is completed
     * @param skipped    whether request is skipped
     */
    public RatingRequest(int id, Session session, Student student, 
        LocalDateTime time, boolean completed, boolean skipped) {
        this.id = id;
        this.session = session;
        this.student = student;
        this.createdAt = LocalDateTime.now();  // overrides passed time with current time
        this.completed = completed;
        this.skipped = skipped;
    }

    /** @return unique rating request ID */
    public int getId() {
        return id;
    }

    /** @return the session being rated */
    public Session getSession() {
        return session;
    }

    /** @return the student asked to provide the rating */
    public Student getStudent() {
        return student;
    }

    /** @return true if rating was completed or skipped */
    public boolean isCompleted() {
        return completed;
    }

    /** @return true if the rating request was skipped */
    public boolean isSkipped() {
        return skipped;
    }

    /** @return the numeric rating submitted (1–5) */
    public int getRating() {
        return rating;
    }

    /** @return timestamp when the rating request was created */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Submits a rating, marking the request completed and not skipped.
     *
     * @param rating numeric rating value (1–5)
     */
    public void submit(int rating) {
        this.rating = rating;
        this.completed = true;
        this.skipped = false;
    }

    /**
     * Marks the request as completed by skipping it (no rating submitted).
     */
    public void skip() {
        this.completed = true;
        this.skipped = true;
    }

    /**
     * Converts this rating request into a Feedback object
     * containing the course rating and associated session info.
     *
     * @return Feedback representing the completed rating
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

    /**
     * Provides a string representation useful for debugging.
     */
    @Override
    public String toString() {
        return "RatingRequest for Session: " + session.getSessionId() +
               ", Student: " + student.getName() +
               ", Completed: " + completed +
               ", Skipped: " + skipped;
    }
}
