package skolard.persistence;

import java.util.List;

import skolard.objects.Student;

public interface StudentPersistence {
    List<Student> getAllStudents();
    Student addStudent(Student newStudent);
    void deleteStudentByEmail(String email);
    void updateStudent(Student updatedStudent);
}
