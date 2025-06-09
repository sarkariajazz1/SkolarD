package skolard.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a tutor in the SkolarD platform.
<<<<<<< HEAD
 * Stores bio, courses taught, string-based grades, and upcoming sessions.
 */
public class Tutor extends User {
    private String bio;
    private List<String> courses;
    private Map<String, Double> courseGrades; // course → string grade (e.g., "A", "95")
    private List<Session> pastSessions;
    private List<Session> upcomingSessions;

    public Tutor(String name, String email, String bio,
                 List<String> courses, Map<String, Double> courseGrades) {
        super(name, email);
        this.bio = bio;
        this.courses = courses != null ? courses : new ArrayList<>();
        this.courseGrades = courseGrades != null ? courseGrades : new HashMap<>();
=======
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
>>>>>>> dev
        this.upcomingSessions = new ArrayList<>();
    }

    /**
     * Constructor for public profile viewing (e.g., student browsing tutor list).
     * Password is not included here.
     */
    public Tutor(String name, String email, String bio) {
<<<<<<< HEAD
        this(name, email, bio, new ArrayList<>(), new HashMap<>());
=======
        this(name, email, null, bio, null);
>>>>>>> dev
    }

    // ===========================
    // Getters
    // ===========================

    public String getBio() {
        return bio;
    }

    public List<String> getCourses() {
        return new ArrayList<>(courses.keySet()); // return copy for safety
    }

    public Map<String, Double> getCoursesWithGrades() {
        return new HashMap<>(courses); // return copy for safety
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

<<<<<<< HEAD
    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public Map<String, Double> getCourseGrades() {
        return courseGrades;
    }

    public void setCourseGrades(Map<String, Double> courseGrades) {
        this.courseGrades = courseGrades;
    }

    public void addCourseGrade(String course, Double grade) {
        this.courseGrades.put(course, grade);
    }

    public Double getGradeForCourse(String course) {
        return this.courseGrades.getOrDefault(course, 1.0);
    }

    public double getAverageRating() {
        double total = 0.0;
        int count = 0;

        for (double grade : courseGrades.values()) {
            total += grade;
            count++;
        }

        return count > 0 ? total / count : 0.0;
    }

    public void setUpcomingSession(Session session) {
        if (!upcomingSessions.contains(session)) {
            this.upcomingSessions.add(session);
            System.out.println("Session " + session.getSessionId() +
                " added to tutor " + getName() + "'s upcoming sessions.");
=======
    public void setCourses(Map<String, Double> grades) {
        this.courses = grades != null ? new HashMap<>(grades) : new HashMap<>();
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
     * Adds a session to the list of upcoming sessions (no duplicates).
     */
    public void addUpcomingSession(Session session) {
        if (session != null && !upcomingSessions.contains(session)) {
            upcomingSessions.add(session);
>>>>>>> dev
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
    
    public List<Session> getPastSessions(){
        return pastSessions;
    }
}
