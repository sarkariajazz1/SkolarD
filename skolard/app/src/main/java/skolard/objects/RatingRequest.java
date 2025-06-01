package skolard.objects;

import java.time.LocalDateTime;

/**
 * Represents a single rating request sent to a student after a session.
 * Tracks whether it was filled or skipped, and stores rating data.
 */
public class RatingRequest {
    private final Session session;
    private final Student student;
    private boolean skipped;
    private boolean completed;
    private int tutorRating;
    private int courseRating;
    private String feedback;
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

    public String getFeedback() {
        return feedback;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void submit(int tutorRating, int courseRating, String feedback) {
        this.tutorRating = tutorRating;
        this.courseRating = courseRating;
        this.feedback = feedback;
        this.completed = true;
        this.skipped = false;
    }

    public void skip() {
        this.completed = true;
        this.skipped = true;
    }
}
