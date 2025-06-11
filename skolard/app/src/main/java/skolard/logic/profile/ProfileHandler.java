package skolard.logic.profile;

import skolard.logic.session.SessionHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

/**
 * Main facade class for managing user profiles in SkolarD.
 * Combines profile creation, updating, and viewing logic into a single handler.
 * Makes use of ProfileCreator, ProfileUpdater, and ProfileViewer for separation of concerns.
 */
public class ProfileHandler {
    
    // Handles profile creation (student/tutor)
    private final ProfileCreator creator;

    // Handles updating profile data (bio, courses, etc.)
    private final ProfileUpdater updater;

    // Handles formatting and viewing profile info
    private final ProfileViewer viewer;

    /**
     * Default constructor that uses the default profile formatter.
     * Useful for application-level initialization.
     *
     * @param sp Student persistence implementation
     * @param tp Tutor persistence implementation
     * @param sessionHandler handler to assist in session-related context (e.g., rating logic)
     */
    public ProfileHandler(StudentPersistence sp, TutorPersistence tp, SessionHandler sessionHandler) {
        this(sp,
             tp,
             new DefaultProfileFormatter(), // Use default profile formatting
             sessionHandler);
    }

    /**
     * Constructor that supports injecting a custom profile formatter.
     * Useful for testing or UI variations.
     *
     * @param sp Student persistence
     * @param tp Tutor persistence
     * @param formatter custom profile formatter
     * @param sessionHandler session handler (for session-related display logic)
     */
    public ProfileHandler(StudentPersistence sp, TutorPersistence tp, ProfileFormatter formatter, SessionHandler sessionHandler) {
        this.creator = new ProfileCreator(sp, tp);
        this.updater = new ProfileUpdater(sp, tp);
        this.viewer = new ProfileViewer(formatter, sessionHandler);
    }

    // === Profile Creation Methods ===

    /**
     * Creates and stores a new student profile.
     *
     * @param name  student's name
     * @param email student's email
     * @param hash  hashed password
     */
    public void addStudent(String name, String email, String hash) {
        creator.addStudent(name, email, hash);
    }

    /**
     * Creates and stores a new tutor profile.
     *
     * @param name  tutor's name
     * @param email tutor's email
     * @param hash  hashed password
     */
    public void addTutor(String name, String email, String hash) {
        creator.addTutor(name, email, hash);
    }

    // === Profile Update Methods ===

    /**
     * Updates an existing student’s profile information.
     *
     * @param s student object with updated data
     */
    public void updateStudent(Student s) {
        updater.updateStudent(s);
    }

    /**
     * Updates an existing tutor’s profile information.
     *
     * @param t tutor object with updated data
     */
    public void updateTutor(Tutor t) {
        updater.updateTutor(t);
    }

    /**
     * Updates a tutor’s biography.
     *
     * @param t tutor object
     * @param newBio updated bio text
     */
    public void updateBio(Tutor t, String newBio) {
        updater.updateBio(t, newBio);
    }

    /**
     * Adds a course with a grade to a tutor’s profile.
     *
     * @param t tutor object
     * @param course course name
     * @param grade grade received
     */
    public void addCourse(Tutor t, String course, Double grade) {
        updater.addCourse(t, course, grade);
    }

    /**
     * Removes a course from a tutor’s profile.
     *
     * @param t tutor object
     * @param course course to be removed
     */
    public void removeCourse(Tutor t, String course) {
        updater.removeCourse(t, course);
    }

    // === Profile View Methods ===

    /**
     * Returns a formatted basic profile (name and email) for a user.
     *
     * @param u user (student or tutor)
     * @return string representation of basic profile
     */
    public String viewBasicProfile(User u) {
        return viewer.viewBasicProfile(u);
    }

    /**
     * Returns a formatted full profile (includes bio, courses, sessions, etc.).
     *
     * @param u user (student or tutor)
     * @return string representation of full profile
     */
    public String viewFullProfile(User u) {
        return viewer.viewFullProfile(u);
    }

    // === Profile Lookup Methods ===

    /**
     * Retrieves a student profile by email.
     *
     * @param email student email
     * @return matching Student object
     */
    public Student getStudent(String email) {
        return creator.getStudent(email);
    }

    /**
     * Retrieves a tutor profile by email.
     *
     * @param email tutor email
     * @return matching Tutor object
     */
    public Tutor getTutor(String email) {
        return creator.getTutor(email);
    }
}
