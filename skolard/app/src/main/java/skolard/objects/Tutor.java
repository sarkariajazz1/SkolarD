package skolard.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a tutor in the SkolarD platform.
 * Stores bio, courses taught, string-based grades, and upcoming sessions.
 */
public class Tutor extends User {
    private String bio;
    private List<String> courses;
    private Map<String, String> courseGrades; // course → string grade (e.g., "A", "95")
    private List<Session> upcomingSessions;

    public Tutor(String name, String email, String bio,
                 List<String> courses, Map<String, String> courseGrades) {
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

    public Map<String, String> getCourseGrades() {
        return courseGrades;
    }

    public void setCourseGrades(Map<String, String> courseGrades) {
        this.courseGrades = courseGrades;
    }

    public void addCourseGrade(String course, String grade) {
        this.courseGrades.put(course, grade);
    }

    public String getGradeForCourse(String course) {
        return this.courseGrades.getOrDefault(course, "N/A");
    }

public double getAverageRating() {
    double total = 0.0;
    int count = 0;

    for (String grade : courseGrades.values()) {
        try {
            double numeric = Double.parseDouble(grade);
            total += numeric;
            count++;
        } catch (NumberFormatException e) {
            // skip non-numeric grades (e.g., "A", "B+")
        }
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
}
