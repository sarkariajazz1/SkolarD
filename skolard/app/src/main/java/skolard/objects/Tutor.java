package skolard.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

/**
 * Represents a tutor in the SkolarD platform.
 * Stores bio, courses taken, grades, and upcoming sessions.
 */
public class Tutor extends User {
    private String bio;
    private ArrayList<String> courses;
    private Map<String, String> courseGrades; // Mapping of course name to grade
    private List<Session> upcomingSessions;

    public Tutor(String name, String email, String bio,
                 ArrayList<String> courses, Map<String, String> courseGrades) {
        super(name, email);
        this.bio = bio;
        this.courses = courses;
        this.courseGrades = courseGrades;
        this.upcomingSessions = new ArrayList<>();
    }

    public Tutor(String name, String email, String bio) {
        super(name, email);
        this.bio = bio;
        this.courses = new ArrayList<String>();
        this.courseGrades = new java.util.HashMap<String,String>();
        this.upcomingSessions = new ArrayList<Session>();
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }

    public Map<String, String> getCourseGrades() {
        return courseGrades;
    }

    public void setCourseGrades(Map<String, String> courseGrades) {
        this.courseGrades = courseGrades;
    }

    // Add or update a grade for a course
    public void addCourseGrade(String course, String grade) {
        this.courseGrades.put(course, grade);
    }

    // Retrieve grade for a specific course
    public String getGradeForCourse(String course) {
        return this.courseGrades.getOrDefault(course, "N/A");
    }

    /**
     * Calculates the average rating based on numeric grades.
     * Ignores non-numeric grades like A/B+ etc.
     */
    public double getAverageRating() {
        if (courseGrades == null || courseGrades.isEmpty()) return 0.0;

        OptionalDouble average = courseGrades.values().stream()
                .filter(grade -> grade.matches("\\d+")) // Only numeric strings
                .mapToInt(Integer::parseInt)
                .average();

        return average.orElse(0.0);
    }

    // Adds a session to the tutor's upcoming sessions
    public void setUpcomingSession(Session session) {
        this.upcomingSessions.add(session);
        System.out.println("Session " + session.getSessionId() + " added to tutor " + getName() + "'s upcoming sessions.");
    }

    public List<Session> getUpcomingSessions() {
        return upcomingSessions;
    }
}
