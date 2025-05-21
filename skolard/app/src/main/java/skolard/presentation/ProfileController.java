package skolard.presentation;

import java.util.List;
import java.util.stream.Collectors;

import skolard.logic.ProfileHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

/**
 * Presentation‐layer façade for all profile operations.
 * UI code (Swing dialogs or frames) should go through this class.
 */
public class ProfileController {
    private final StudentPersistence mockStudent;
    private final TutorPersistence  mockTutor;

    /** Default constructor wires in the STUB implementations. */
    public ProfileController() {
        this.mockStudent = PersistenceFactory.getStudentPersistence();
        this.mockTutor   = PersistenceFactory.getTutorPersistence();
    }

    /** Returns all students in the system. */
    public List<Student> getAllStudents() {
        return mockStudent.getAllStudents();
    }

    /** Returns all tutors in the system. */
    public List<Tutor> getAllTutors() {
        return mockTutor.getAllTutors();
    }

    /** Find a student by their email address (case‐insensitive). */
    public Student getStudentByEmail(String email) {
        return mockStudent.getAllStudents().stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /** Find a tutor by their email address (case‐insensitive). */
    public Tutor getTutorByEmail(String email) {
        return mockTutor.getAllTutors().stream()
                .filter(t -> t.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /** Show the basic profile (name & email) for any user. */
    public String viewBasicProfile(User user) {
        return ProfileHandler.viewBasicProfile(user);
    }

    /** Show the full profile (courses, grades, bio) for any user. */
    public String viewFullProfile(User user) {
        return ProfileHandler.viewFullProfile(user);
    }

    /** Update a user’s bio and persist the change. */
    public void updateBio(User user, String newBio) {
        ProfileHandler.updateBio(user, newBio);
        if (user instanceof Student) {
            mockStudent.updateStudent((Student) user);
        } else if (user instanceof Tutor) {
            mockTutor.updateTutor((Tutor) user);
        }
    }

    /** Add a new course‐grade entry to a tutor and persist. */
    public void addTutoringCourse(Tutor tutor, String course, String grade) {
        ProfileHandler.addTutoringCourse(tutor, course, grade);
        mockTutor.updateTutor(tutor);
    }

    /** Remove a course from a tutor’s profile and persist. */
    public void removeTutoringCourse(Tutor tutor, String course) {
        ProfileHandler.removeTutoringCourse(tutor, course);
        mockTutor.updateTutor(tutor);
    }

    /** Promote a generic User to a Student object (in memory). */
    public Student promoteToStudent(User user) {
        Student s = ProfileHandler.promoteToStudent(user);
        mockStudent.addStudent(s);
        return s;
    }

    /** Promote a generic User to a Tutor object (in memory). */
    public Tutor promoteToTutor(User user) {
        Tutor t = ProfileHandler.promoteToTutor(user);
        mockTutor.addTutor(t);
        return t;
    }
}
