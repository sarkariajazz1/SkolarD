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

    private Map<String, Student> students;      // Map of students keyed by email
    private static int uniqueID = 0;            // Counter for assigning unique IDs

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
        if(students == null) {
            students = new HashMap<>();
        }
    }

    /**
     * Adds a few predefined student accounts for testing purposes.
     */
    private void addSampleStudents() {
        addStudent(new Student("" + uniqueID++, "Matt Yab", "yabm@myumanitoba.ca"));
        addStudent(new Student("" + uniqueID++, "Group Six", "sixg@myumanitoba.ca"));
        addStudent(new Student("" + uniqueID++, "John Wick", "wickj@myumanitoba.ca"));
    }

    /**
     * Adds a new student if they don't already exist.
     *
     * @param student Student to add
     * @return The new student object, or null if email already exists
     */
    public Student addStudent(Student student) {
        confirmCreation();

        Student newStudent = null;

        if(students.containsKey(student.getEmail())) {
            newStudent = new Student("" + uniqueID++, student.getName(), student.getEmail());
            students.put(newStudent.getEmail(), newStudent);
        }

        return newStudent;
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
        if(students.containsKey(email)) {
            students.remove(email);
        }
    }

    /**
     * Updates a student record if it already exists.
     *
     * @param updatedStudent Updated student object
     */
    @Override
    public void updateStudent(Student updatedStudent) {
        confirmCreation();
        if(students.containsKey(updatedStudent.getEmail())) {
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
     * Clears all student records from memory.
     */
    public void close() {
        this.students = null;
    }
}
