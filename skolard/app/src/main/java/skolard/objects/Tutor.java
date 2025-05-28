package skolard.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.logic.utils.GradeUtil;

/**
 * Represents a tutor in the SkolarD platform.
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
        this.upcomingSessions = new ArrayList<>();
    }

    public Tutor(String name, String email, String bio) {
        this(name, email, bio, new ArrayList<>(), new HashMap<>());
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

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

    public void addCourseGrade(String course, String grade) {
        try {
            Double courseGrade = Double.parseDouble(grade);
            this.courseGrades.put(course, courseGrade);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Grade is not a double");
        }
        
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
        }
    }

    public List<Session> getUpcomingSessions() {
        return upcomingSessions;
    }
    
    public List<Session> getPastSessions(){
        return pastSessions;
    }
}
