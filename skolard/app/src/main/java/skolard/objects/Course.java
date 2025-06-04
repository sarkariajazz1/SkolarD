package skolard.objects;

import java.util.Objects;

/**
 * Represents a course that a tutor can teach in the SkolarD platform.
 * Contains course information including name/code and the tutor's grade in that course.
 */
public class Course {
    private String courseCode;    // e.g., "COMP3350", "MATH1500"
    private String courseName;    // e.g., "Software Engineering", "Calculus I"
    private Double grade;         // Tutor's grade in this course (0.0-4.0 scale)

    /**
     * Full constructor for creating a course with all details.
     */
    public Course(String courseCode, String courseName, Double grade) {
        this.courseCode = courseCode != null ? courseCode.trim().toUpperCase() : "";
        this.courseName = courseName != null ? courseName.trim() : "";
        this.grade = grade;
    }

    /**
     * Constructor for course without grade (grade can be added later).
     */
    public Course(String courseCode, String courseName) {
        this(courseCode, courseName, null);
    }

    /**
     * Simple constructor using course code only (common case).
     */
    public Course(String courseCode) {
        this(courseCode, "", null);
    }

    // ===========================
    // Getters
    // ===========================

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public Double getGrade() {
        return grade;
    }

    /**
     * Returns a display-friendly string for the course.
     * Format: "COMP3350 - Software Engineering" or just "COMP3350" if no name.
     */
    public String getDisplayName() {
        if (courseName != null && !courseName.isEmpty()) {
            return courseCode + " - " + courseName;
        }
        return courseCode;
    }

    /**
     * Returns grade as formatted string, or "No Grade" if null.
     */
    public String getGradeDisplay() {
        return grade != null ? String.format("%.1f", grade) : "No Grade";
    }

    // ===========================
    // Setters
    // ===========================

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode != null ? courseCode.trim().toUpperCase() : "";
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName != null ? courseName.trim() : "";
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    // ===========================
    // Utility Methods
    // ===========================

    /**
     * Checks if this course has a valid grade.
     */
    public boolean hasGrade() {
        return grade != null;
    }

    /**
     * Checks if the grade is within valid range (0.0-4.0).
     */
    public boolean hasValidGrade() {
        return grade != null && grade >= 0.0 && grade <= 4.0;
    }

    /**
     * Returns a unique identifier for this course (typically the course code).
     */
    public String getUniqueId() {
        return courseCode.toLowerCase();
    }

    // ===========================
    // Object Overrides
    // ===========================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return Objects.equals(courseCode.toLowerCase(), course.courseCode.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode.toLowerCase());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Course{");
        sb.append("code='").append(courseCode).append("'");
        if (courseName != null && !courseName.isEmpty()) {
            sb.append(", name='").append(courseName).append("'");
        }
        if (grade != null) {
            sb.append(", grade=").append(grade);
        }
        sb.append("}");//
        return sb.toString();
    }
}