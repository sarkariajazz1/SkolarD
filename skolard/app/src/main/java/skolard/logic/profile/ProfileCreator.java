package skolard.logic.profile;

import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;
import skolard.utils.EmailUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileCreator {
    private final StudentPersistence studentDB;
    private final TutorPersistence tutorDB;

    public ProfileCreator(StudentPersistence sp, TutorPersistence tp) {
        this.studentDB = sp;
        this.tutorDB = tp;
    }

    public void addStudent(String name, String email, String hashedPassword) {
        ValidationUtil.validateNewUser(name, email);
        studentDB.addStudent(new Student(name.trim(), EmailUtil.normalize(email), hashedPassword));
    }

    public void addTutor(String name, String email, String hashedPassword) {
        ValidationUtil.validateNewUser(name, email);
        tutorDB.addTutor(new Tutor(name.trim(), EmailUtil.normalize(email), hashedPassword,
                                   "Edit your bio...", new ArrayList<>(), new HashMap<>()));
    }
}
