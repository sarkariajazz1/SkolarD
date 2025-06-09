package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.utils.PasswordUtil;
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
        students = new HashMap<>();
        addSampleStudents();
    }

    /**
     * Simple stub hash function (placeholder).
     */
    private String hash(String plain) {
        return PasswordUtil.hash(plain);
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
<<<<<<< HEAD
        confirmCreation();

        Student newStudent = null;

        if(!students.containsKey(student.getEmail())) {
            newStudent = new Student(student.getName(), student.getEmail());
            students.put(newStudent.getEmail(), newStudent);
=======
        if (students.containsKey(student.getEmail())) {
            throw new RuntimeException("Student account already exists");
>>>>>>> dev
        }
        students.put(student.getEmail(), student); // Store original object with password
        return student;
    }

    /**
     * Retrieves a student by their email address.
     *
     * @param email The student's email
     * @return Student object if found, otherwise null
     */
    @Override
    public Student getStudentByEmail(String email) {
        return students.get(email);
    }

    /**
     * Deletes a student record using their email.
     *
     * @param email Email of the student to delete
     */
    @Override
    public void deleteStudentByEmail(String email) {
        students.remove(email);
    }

    /**
     * Updates a student record if it already exists.
     *
     * @param updatedStudent Updated student object
     */
    @Override
    public void updateStudent(Student updatedStudent) {
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
<<<<<<< HEAD
        confirmCreation();
        List<Student> studentList = new ArrayList<>();
        for (Student student : students.values()) {
            studentList.add(student);
        }
        return studentList;
=======
        return new ArrayList<>(students.values());
>>>>>>> dev
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
        Student student = students.get(email);
        if (student != null && student.getHashedPassword().equals(hashedPassword)) {
            return student;
        }
        return null;
    }
}
