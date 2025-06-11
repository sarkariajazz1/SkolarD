package skolard.logic.profile;

import java.util.Map;

import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

/**
 * ProfileUpdater is responsible for modifying student and tutor profiles.
 * It provides operations for updating personal details, biographies, and course records.
 */
public class ProfileUpdater {

    // Persistence layer for accessing and modifying student data
    private final StudentPersistence studentDB;

    // Persistence layer for accessing and modifying tutor data
    private final TutorPersistence tutorDB;

    /**
     * Constructor allowing dependency injection of persistence layers.
     *
     * @param sp StudentPersistence implementation
     * @param tp TutorPersistence implementation
     */
    public ProfileUpdater(StudentPersistence sp, TutorPersistence tp) {
        this.studentDB = sp;
        this.tutorDB = tp;
    }

    /**
     * Updates a student's full profile in the database.
     *
     * @param s the Student object with updated data
     */
    public void updateStudent(Student s) {
        studentDB.updateStudent(s);
    }

    /**
     * Updates a tutor's full profile in the database.
     *
     * @param t the Tutor object with updated data
     */
    public void updateTutor(Tutor t) {
        tutorDB.updateTutor(t);
    }

    /**
     * Updates the bio field of a Tutor profile and persists the change.
     *
     * @param tutor the Tutor whose bio is to be updated
     * @param newBio the new biography string
     */
    public void updateBio(Tutor tutor, String newBio) {
        tutor.setBio(newBio);          // Update object in memory
        tutorDB.updateTutor(tutor);    // Persist updated tutor object
    }

    /**
     * Adds a course and associated grade to the tutor’s profile if it does not already exist.
     * Normalizes course name to lowercase for consistency.
     *
     * @param tutor  the Tutor to update
     * @param course the name of the course
     * @param grade  the grade achieved in the course
     */
    public void addCourse(Tutor tutor, String course, Double grade) {
        String c = course.toLowerCase();  // Normalize for consistency

        // Only add the course if it's not already in the list
        if (!tutor.getCourses().contains(c)) {
            tutor.addCourse(c, grade);                        // Update in memory
            tutorDB.addCourseToTutor(tutor, course, grade);   // Persist change
        }
    }

    /**
     * Removes a course from the tutor’s profile.
     * Updates both the in-memory object and the persistent storage.
     *
     * @param tutor  the Tutor to update
     * @param course the course name to be removed
     */
    public void removeCourse(Tutor tutor, String course) {
        String c = course.toLowerCase(); // Normalize course name

        // Modify the course-grade mapping in the tutor object
        Map<String, Double> courses = tutor.getCoursesWithGrades();
        courses.remove(c);                // Remove from the map
        tutor.setCourses(courses);        // Apply change in object

        tutorDB.removeCourseFromTutor(tutor, course); // Persist course removal
    }
}
