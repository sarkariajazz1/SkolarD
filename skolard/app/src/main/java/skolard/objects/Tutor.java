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
    private List<String> courses;
    private Map<String, Double> courseGrades; // course → numeric grade
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
                 List<String> courses, Map<String, Double> courseGrades) {
        super(name, email, hashedPassword);
        this.bio = bio != null ? bio : "Edit your bio...";
        this.courses = courses != null ? new ArrayList<>(courses) : new ArrayList<>();
        this.courseGrades = courseGrades != null ? new HashMap<>(courseGrades) : new HashMap<>();
        this.pastSessions = new ArrayList<>();
        this.upcomingSessions = new ArrayList<>();
    }

    /**
     * Constructor for public profile viewing (e.g., student browsing tutor list).
     * Password is not included here.
     */
    public Tutor(String name, String email, String bio) {
        this(name, email, null, bio, null, null);
    }

    // ===========================
    // Getters
    // ===========================

    public String getBio() {
        return bio;
    }

    public List<String> getCourses() {
        return new ArrayList<>(courses); // return copy for safety
    }

    public Map<String, Double> getCourseGrades() {
        return new HashMap<>(courseGrades); // return copy for safety
    }

    public List<Session> getPastSessions() {
        return new ArrayList<>(pastSessions); // return copy for safety
    }

    public List<Session> getUpcomingSessions() {
        return new ArrayList<>(upcomingSessions); // return copy for safety
    }

    // ===========================
    // Setters
    // ===========================

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses != null ? new ArrayList<>(courses) : new ArrayList<>();
    }

    public void setCourseGrades(Map<String, Double> grades) {
        this.courseGrades = grades != null ? new HashMap<>(grades) : new HashMap<>();
    }

    public void setPastSessions(List<Session> sessions) {
        this.pastSessions = sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    public void setUpcomingSessions(List<Session> sessions) {
        this.upcomingSessions = sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    // ===========================
    // Functional Methods
    // ===========================

    /**
     * Adds or updates a course grade for the tutor.
     */
    public void addCourseGrade(String course, Double grade) {
        if (course != null && grade != null) {
            this.courseGrades.put(course, grade);
        }
    }

    /**
     * Retrieves a tutor's grade for a specific course.
     * Returns 1.0 by default if course not found.
     */
    public Double getGradeForCourse(String course) {
        return this.courseGrades.getOrDefault(course, 1.0);
    }

    /**
     * Calculates the average rating across all graded courses.
     */
    public double getAverageRating() {
        if (courseGrades.isEmpty()) return 0.0;

        double total = 0.0;
        for (double grade : courseGrades.values()) {
            total += grade;
        }
        return total / courseGrades.size();
    }

    /**
     * Adds a session to the list of upcoming sessions (no duplicates).
     */
    public void addUpcomingSession(Session session) {
        if (session != null && !upcomingSessions.contains(session)) {
            upcomingSessions.add(session);
        }
    }

    /**
     * Adds a session to the list of past sessions (no duplicates).
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
