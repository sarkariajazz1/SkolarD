package skolard.objects;

import java.util.ArrayList;
import java.util.Map;
import java.util.OptionalDouble;

public class Tutor extends User {
    private String bio;
    private ArrayList<String> courses;
    private Map<String, String> courseGrades;

    public Tutor(String id, String name, String email, String bio,
                 ArrayList<String> courses, Map<String, String> courseGrades) {
        super(id, name, email);
        this.bio = bio;
        this.courses = courses;
        this.courseGrades = courseGrades;
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
}
