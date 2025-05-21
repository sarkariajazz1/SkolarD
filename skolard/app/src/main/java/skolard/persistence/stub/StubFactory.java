package skolard.persistence.stub;

import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;
import skolard.persistence.SessionPersistence;

public class StubFactory {
    private static final StudentStub studentStub = new StudentStub();
    private static final TutorStub tutorStub = new TutorStub();
    private static final SessionStub sessionStub = new SessionStub();

    public static <T> T getStub(Class<T> clazz) {
        if (clazz.equals(StudentPersistence.class)) {
            return clazz.cast(studentStub);
        } else if (clazz.equals(TutorPersistence.class)) {
            return clazz.cast(tutorStub);
        } else if (clazz.equals(SessionPersistence.class)) {
            return clazz.cast(sessionStub);
        } else {
            throw new IllegalArgumentException("No stub available for class: " + clazz.getName());
        }
    }
}
