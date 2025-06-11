package skolard.logic.profile;

import java.util.HashMap;

import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;
import skolard.utils.EmailUtil;
import skolard.utils.ValidationUtil;

/**
 * Responsible for creating and retrieving student and tutor profiles.
 * Uses injected persistence layers to store and fetch user data.
 */
public class ProfileCreator {

    // Reference to the student persistence layer
    private final StudentPersistence studentDB;

    // Reference to the tutor persistence layer
    private final TutorPersistence tutorDB;

    /**
     * Constructor for dependency injection.
     * Allows use of different implementations for testing or storage backends.
     *
     * @param sp Student persistence implementation
     * @param tp Tutor persistence implementation
     */
    public ProfileCreator(StudentPersistence sp, TutorPersistence tp) {
        this.studentDB = sp;
        this.tutorDB = tp;
    }

    /**
     * Creates and stores a new student profile in the database.
     *
     * @param name           the student's full name
     * @param email          the student's email address
     * @param hashedPassword the student's hashed password
     * @throws IllegalArgumentException if name or email is invalid
     */
    public void addStudent(String name, String email, String hashedPassword) {
        // Validate format and uniqueness of the name and email
        ValidationUtil.validateNewUser(name, email);

        // Normalize email and trim name before storing
        studentDB.addStudent(new Student(name.trim(), EmailUtil.normalize(email), hashedPassword));
    }

    /**
     * Creates and stores a new tutor profile in the database.
     * Initializes bio to default text and courses map to empty.
     *
     * @param name           the tutor's full name
     * @param email          the tutor's email address
     * @param hashedPassword the tutor's hashed password
     * @throws IllegalArgumentException if name or email is invalid
     */
    public void addTutor(String name, String email, String hashedPassword) {
        // Validate name and email first
        ValidationUtil.validateNewUser(name, email);

        // Normalize and store with default bio and empty course-grade map
        tutorDB.addTutor(new Tutor(name.trim(), EmailUtil.normalize(email), hashedPassword,
                                   "Edit your bio...", new HashMap<>()));
    }

    /**
     * Retrieves a student object based on email.
     *
     * @param email the student's email
     * @return the matching Student object or null if not found
     */
    public Student getStudent(String email) {
        return studentDB.getStudentByEmail(email.trim().toLowerCase());
    }

    /**
     * Retrieves a tutor object based on email.
     *
     * @param email the tutor's email
     * @return the matching Tutor object or null if not found
     */
    public Tutor getTutor(String email) {
        return tutorDB.getTutorByEmail(email.trim().toLowerCase());
    }
}
