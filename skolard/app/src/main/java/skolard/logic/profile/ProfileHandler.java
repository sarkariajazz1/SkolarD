package skolard.logic.profile;

import skolard.logic.session.SessionHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.PersistenceRegistry;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

public class ProfileHandler {
    private final ProfileCreator creator;
    private final ProfileUpdater updater;
    private final ProfileViewer viewer;

    /**
     * Default constructor using the real database from PersistenceRegistry.
     */
    public ProfileHandler(StudentPersistence sp, TutorPersistence tp, SessionHandler sessionHandler) {
        this(sp,
             tp,
             new DefaultProfileFormatter(),
             sessionHandler);
    }

    /**
     * Constructor allowing injection of formatter.
     */
    public ProfileHandler(StudentPersistence sp, TutorPersistence tp, ProfileFormatter formatter, SessionHandler sessionHandler) {
        this.creator = new ProfileCreator(sp, tp);
        this.updater = new ProfileUpdater(sp, tp);
        this.viewer = new ProfileViewer(formatter, sessionHandler);
    }

    // Profile creation
    public void addStudent(String name, String email, String hash) {
        creator.addStudent(name, email, hash);
    }

    public void addTutor(String name, String email, String hash) {
        creator.addTutor(name, email, hash);
    }

    // Profile update
    public void updateStudent(Student s) {
        updater.updateStudent(s);
    }

    public void updateTutor(Tutor t) {
        updater.updateTutor(t);
    }

    public void updateBio(Tutor t, String newBio) {
        updater.updateBio(t, newBio);
    }

    public void addCourse(Tutor t, String course, Double grade) {
        updater.addCourse(t, course, grade);
    }

    public void removeCourse(Tutor t, String course) {
        updater.removeCourse(t, course);
    }

    // Profile view
    public String viewBasicProfile(User u) {
        return viewer.viewBasicProfile(u);
    }

    public String viewFullProfile(User u) {
        return viewer.viewFullProfile(u);
    }

    public Student getStudent(String email) {
        return creator.getStudent(email);
    }

    public Tutor getTutor(String email) {
        return creator.getTutor(email);
    }
}
