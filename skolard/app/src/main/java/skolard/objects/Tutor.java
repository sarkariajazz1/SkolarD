package skolard.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a tutor in the SkolarD platform.
 * Stores bio, courses taught, numeric course grades, and upcoming/past sessions.
 */
public class Tutor extends User {
    private String bio;
    private Map<String, Double> courses; // course → numeric grade
    private List<Session> pastSessions;
    private List<Session> upcomingSessions;

    /**
     * Full constructor used during registration or database hydration.
     * 
     * @param name           Tutor's name
     * @param email          Tutor's email
     * @param hashedPassword Hashed password for authentication
     * @param bio            Tutor's biography
     * @param courses        Courses the tutor can teach
     * @param courseGrades   Grades received for each course
     */
    public Tutor(String name, String email, String hashedPassword, String bio,
        Map<String, Double> courses) {
        super(name, email, hashedPassword);
        this.bio = bio != null ? bio : "Edit your bio...";
        this.courses = courses != null ? new HashMap<>(courses) : new HashMap<>();
        this.pastSessions = new ArrayList<>();
        this.upcomingSessions = new ArrayList<>();
    }

    /**
     * Constructor for public profile viewing (e.g., student browsing tutor list).
     * Password is not included here.
     */
    public Tutor(String name, String email, String bio) {
        this(name, email, null, bio, null);
    }

    // ===========================
    // Getters
    // ===========================

    /** Returns the tutor's biography */
    public String getBio() {
        return bio;
    }

    /** Returns a list of courses taught by the tutor (copy) */
    public List<String> getCourses() {
        return new ArrayList<>(courses.keySet()); // return copy for safety
    }

    /** Returns a map of courses and corresponding grades (copy) */
    public Map<String, Double> getCoursesWithGrades() {
        return new HashMap<>(courses); // return copy for safety
    }

    /** Returns a list of past sessions (copy) */
    public List<Session> getPastSessions() {
        return new ArrayList<>(pastSessions); // return copy for safety
    }

    /** Returns a list of upcoming sessions (copy) */
    public List<Session> getUpcomingSessions() {
        return new ArrayList<>(upcomingSessions); // return copy for safety
    }

    // ===========================
    // Setters
    // ===========================

    /** Sets or updates the biography */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /** Sets the courses and grades map (copy) */
    public void setCourses(Map<String, Double> grades) {
        this.courses = grades != null ? new HashMap<>(grades) : new HashMap<>();
    }

    /** Sets the list of past sessions (copy) */
    public void setPastSessions(List<Session> sessions) {
        this.pastSessions = sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    /** Sets the list of upcoming sessions (copy) */
    public void setUpcomingSessions(List<Session> sessions) {
        this.upcomingSessions = sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    // ===========================
    // Functional Methods
    // ===========================

    /**
     * Adds or updates a course grade for the tutor.
     */
    public void addCourse(String course, Double grade) {
        if (course != null && grade != null) {
            this.courses.put(course, grade);
        }
    }

    /**
     * Retrieves a tutor's grade for a specific course.
     * Returns 1.0 by default if course not found.
     */
    public Double getGradeForCourse(String course) {
        return this.courses.getOrDefault(course, 1.0);
    }

    /**
     * Calculates the average rating across all graded courses.
     * Returns 0.0 if no courses are graded.
     */
    public double getAverageRating() {
        if (courses.isEmpty()) return 0.0;

        double total = 0.0;
        for (double grade : courses.values()) {
            total += grade;
        }
        return total / courses.size();
    }

    /**
     * Adds a session to the list of upcoming sessions if not already present.
     */
    public void addUpcomingSession(Session session) {
        if (session != null && !upcomingSessions.contains(session)) {
            upcomingSessions.add(session);
        }
    }

    /**
     * Adds a session to the list of past sessions if not already present.
     */
    public void addPastSession(Session session) {
        if (session != null && !pastSessions.contains(session)) {
            pastSessions.add(session);
        }
    }

    /**
     * String representation of a tutor's public info.
     */
    @Override
    public String toString() {
        return "Tutor{name='" + getName() + "', email='" + getEmail() + "', bio='" + bio + "'}";
    }
}