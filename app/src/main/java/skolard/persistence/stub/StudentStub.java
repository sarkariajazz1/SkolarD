package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.objects.Student;
import skolard.persistence.StudentPersistence;

public class StudentStub implements StudentPersistence {
    private Map<String, Student> students;
    private static int uniqueID = 0;

    public StudentStub() {
        confirmCreation();

        addSampleStudents();
    }

    private void confirmCreation() {
        if(students == null) {
            students = new HashMap<>();
        }
    }

    private void addSampleStudents() {
        addStudent(new Student("" + uniqueID++, "Matt Yab", "yabm@myumanitoba.ca"));
        addStudent(new Student("" + uniqueID++, "Group Six", "sixg@myumanitoba.ca"));
        addStudent(new Student("" + uniqueID++, "John Wick", "wickj@myumanitoba.ca"));
    }

    public Student addStudent(Student student) {
        confirmCreation();

        Student newStudent = null;

        if(students.containsKey(student.getEmail())) {
            newStudent = new Student("" + uniqueID++, student.getName(), student.getEmail());
            students.put(newStudent.getEmail(), newStudent);
        }

        return newStudent;
    }

    @Override
    public void deleteStudentByEmail(String email) {
        confirmCreation();

        if(students.containsKey(email)) {
            students.remove(email);
        }
    }

    
    @Override
    public void updateStudent(Student updatedStudent) {
        confirmCreation();

        if(students.containsKey(updatedStudent.getEmail())) {
            students.replace(updatedStudent.getEmail(), updatedStudent);
        } 
    }

    @Override
    public List<Student> getAllStudents() {
        confirmCreation();

        List<Student> studentList = new ArrayList<Student>();
        for (Student student : students.values()) {
            studentList.add(student);
        }
        return studentList;
    }
}
