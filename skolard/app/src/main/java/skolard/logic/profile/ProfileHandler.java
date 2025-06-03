package skolard.logic.profile;

import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.PersistenceRegistry;

public class ProfileHandler {
    private final ProfileCreator creator;
    private final ProfileUpdater updater;
    private final ProfileViewer viewer;

    public ProfileHandler() {
        this.creator = new ProfileCreator(PersistenceRegistry.getStudentPersistence(),
                                          PersistenceRegistry.getTutorPersistence());
        this.updater = new ProfileUpdater(PersistenceRegistry.getStudentPersistence(),
                                          PersistenceRegistry.getTutorPersistence());
        this.viewer  = new ProfileViewer(new DefaultProfileFormatter());
    }

    public void addStudent(String name, String email, String hash) {
        creator.addStudent(name, email, hash);
    }

    public void addTutor(String name, String email, String hash) {
        creator.addTutor(name, email, hash);
    }

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

    public String viewBasicProfile(User u) {
        return viewer.viewBasicProfile(u);
    }

    public String viewFullProfile(User u) {
        return viewer.viewFullProfile(u);
    }
}
