package skolard.persistence;

import java.util.List;
import skolard.objects.Student;

/**
 * Interface that defines CRUD operations for student data.
 * Allows the system to store, retrieve, and update student records.
 */
public interface StudentPersistence {

    /**
     * Get all registered students in the system.
     *
     * @return list of all students
     */
    List<Student> getAllStudents();

    Student getStudentByEmail(String email);

    /**
     * Add a new student to the system.
     *
     * @param newStudent student to add
     * @return the added student (with any modifications applied)
     */
    Student addStudent(Student newStudent);

    /**
     * Remove a student from the system using their email.
     *
     * @param email email of the student to delete
     */
    void deleteStudentByEmail(String email);

    /**
     * Update the information of an existing student.
     *
     * @param updatedStudent updated student object
     */
    void updateStudent(Student updatedStudent);
}


