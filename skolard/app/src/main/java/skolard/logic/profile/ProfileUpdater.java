package skolard.logic.profile;

import java.util.Map;

import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

public class ProfileUpdater {
    private final StudentPersistence studentDB;
    private final TutorPersistence tutorDB;

    public ProfileUpdater(StudentPersistence sp, TutorPersistence tp) {
        this.studentDB = sp;
        this.tutorDB = tp;
    }

    public void updateStudent(Student s) {
        studentDB.updateStudent(s);
    }

    public void updateTutor(Tutor t) {
        tutorDB.updateTutor(t);
    }

    public void updateBio(Tutor tutor, String newBio) {
        tutor.setBio(newBio);
        tutorDB.updateTutor(tutor);
    }

    public void addCourse(Tutor tutor, String course, Double grade) {
        String c = course.toLowerCase();
        if (!tutor.getCourses().contains(c)) {
            tutor.addCourse(c, grade);
            tutorDB.addCourseToTutor(tutor, course, grade);
        }
    }

    public void removeCourse(Tutor tutor, String course) {
        String c = course.toLowerCase();
        Map<String, Double> courses = tutor.getCoursesWithGrades();
        courses.remove(c);
        tutor.setCourses(courses);
        tutorDB.removeCourseFromTutor(tutor, course);
    }
}
