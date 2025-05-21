package skolard.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

/**
 * Represents a tutor who offers sessions to students.
 * Tutors list courses they've taken and grades received.
 */
public class Tutor extends User {
    private String bio;                            // Short description about the tutor
    private ArrayList<String> courses;             // List of course names the tutor has completed
    private Map<String, String> courseGrades;      // Map of course name to grade received
    private List<Session> upcomingSessions;        // List of upcoming sessions this tutor is scheduled for

    public Tutor(String id, String name, String email, String bio,
                 ArrayList<String> courses, Map<String, String> courseGrades) {
        super(id, name, email);
        this.bio = bio;
        this.courses = courses;
        this.courseGrades = courseGrades;
        this.upcomingSessions = new ArrayList<>();
    }

    // Alternate constructor (used when courses or grades may be added later)
    public Tutor(String id, String name, String email, String bio) {
        super(id, name, email);
        this.bio = bio;
        this.courses = new ArrayList<>();
        this.courseGrades = new java.util.HashMap<>();
        this.upcomingSessions = new ArrayList<>();
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

    // Adds or updates a course and the grade the tutor received
    public void addCourseGrade(String course, String grade) {
        this.courseGrades.put(course, grade);
    }

    // Returns the grade for a given course, or "N/A" if not found
    public String getGradeForCourse(String course) {
        return this.courseGrades.getOrDefault(course, "N/A");
    }

    // Calculates and returns the average of numeric grades
    public double getAverageRating() {
        if (courseGrades == null || courseGrades.isEmpty()) return 0.0;

        OptionalDouble average = courseGrades.values().stream()
                .filter(grade -> grade.matches("\\d+"))       // Accepts only numeric grades
                .mapToInt(Integer::parseInt)
                .average();

        return average.orElse(0.0);
    }

    // Adds a session to the tutor’s upcoming schedule
    public void setUpcomingSession(Session session) {
        this.upcomingSessions.add(session);
        System.out.println("Session " + session.getSessionId() + " added to tutor " + getName() + "'s upcoming sessions.");
    }

    public List<Session> getUpcomingSessions() {
        return upcomingSessions;
    }
}
