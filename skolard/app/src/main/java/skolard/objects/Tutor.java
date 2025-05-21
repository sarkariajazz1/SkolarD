package skolard.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

public class Tutor extends User {
    private String bio;
    private ArrayList<String> courses;
    private Map<String, String> courseGrades;
    private List<Session> upcomingSessions;

    public Tutor(String id, String name, String email, String bio,
                 ArrayList<String> courses, Map<String, String> courseGrades) {
        super(id, name, email);
        this.bio = bio;
        this.courses = courses;
        this.courseGrades = courseGrades;
        this.upcomingSessions = new ArrayList<>();
    }

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

    public void addCourseGrade(String course, String grade) {
        this.courseGrades.put(course, grade);
    }

    public String getGradeForCourse(String course) {
        return this.courseGrades.getOrDefault(course, "N/A");
    }

    // Method to calculate average rating
    public double getAverageRating() {
        if (courseGrades == null || courseGrades.isEmpty()) return 0.0;

        OptionalDouble average = courseGrades.values().stream()
                .filter(grade -> grade.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .average();

        return average.orElse(0.0);
    }

    public void setUpcomingSession(Session session) {
        this.upcomingSessions.add(session);
        System.out.println("Session " + session.getSessionId() + " added to tutor " + getName() + "'s upcoming sessions.");
    }

    public List<Session> getUpcomingSessions() {
        return upcomingSessions;
    }
}


