package skolard.persistence.stub;

import skolard.persistence.SessionPersistence;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

public class StubFactory {
    public static SessionPersistence createSessionPersistence() {
        return new SessionStub(); // in-memory note persistence
    }

    public static StudentPersistence createStudentPersistence() {
        return new StudentStub(); // in-memory category persistence
    }

    public static TutorPersistence createTutorPersistence() {
        return new TutorStub(); // in-memory category persistence
    }
}
