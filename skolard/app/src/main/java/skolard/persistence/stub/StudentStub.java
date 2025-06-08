package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.objects.Student;
import skolard.persistence.StudentPersistence;

/**
 * In-memory stub implementation of StudentPersistence.
 * Used for development and testing without needing a real database.
 */
public class StudentStub implements StudentPersistence {

    private Map<String, Student> students; // Map of students keyed by email

    /**
     * Constructor initializes student storage and adds demo data.
     */
    public StudentStub() {
        confirmCreation();
        addSampleStudents();
    }

    /**
     * Ensures the students map is initialized before use.
     */
    private void confirmCreation() {
        if (students == null) {
            students = new HashMap<>();
        }
    }

    /**
     * Simple stub hash function (placeholder).
     */
    private String hash(String plain) {
        return Integer.toHexString(plain.hashCode()); // For development only
    }

    /**
     * Adds a few predefined student accounts for testing purposes.
     */
    private void addSampleStudents() {
        addStudent(new Student("Matt Yab", "yabm@myumanitoba.ca", hash("pass123")));
        addStudent(new Student("Group Six", "sixg@myumanitoba.ca", hash("sixgroup")));
        addStudent(new Student("John Wick", "wickj@myumanitoba.ca", hash("babaYaga")));
    }

    /**
     * Adds a new student if they don't already exist.
     *
     * @param student Student to add
     * @return The new student object, or null if email already exists
     */
    public Student addStudent(Student student) {
        confirmCreation();

        if (!students.containsKey(student.getEmail())) {
            students.put(student.getEmail(), student); // Store original object with password
            return student;
        }

        return null;
    }

    /**
     * Retrieves a student by their email address.
     *
     * @param email The student's email
     * @return Student object if found, otherwise null
     */
    @Override
    public Student getStudentByEmail(String email) {
        confirmCreation();
        return students.get(email);
    }

    /**
     * Deletes a student record using their email.
     *
     * @param email Email of the student to delete
     */
    @Override
    public void deleteStudentByEmail(String email) {
        confirmCreation();
        students.remove(email);
    }

    /**
     * Updates a student record if it already exists.
     *
     * @param updatedStudent Updated student object
     */
    @Override
    public void updateStudent(Student updatedStudent) {
        confirmCreation();
        if (students.containsKey(updatedStudent.getEmail())) {
            students.replace(updatedStudent.getEmail(), updatedStudent);
        }
    }

    /**
     * Retrieves all students in the system.
     *
     * @return List of Student objects
     */
    @Override
    public List<Student> getAllStudents() {
        confirmCreation();
        return new ArrayList<>(students.values());
    }

    /**
     * Authenticates a student based on email and hashed password.
     *
     * @param email          Student email
     * @param hashedPassword Hashed password
     * @return The matching student or null if authentication fails
     */
    @Override
    public Student authenticate(String email, String hashedPassword) {
        confirmCreation();
        Student student = students.get(email);
        if (student != null && student.getHashedPassword().equals(hashedPassword)) {
            return student;
        }
        return null;
    }
}
