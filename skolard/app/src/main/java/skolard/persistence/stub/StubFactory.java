package skolard.persistence.stub;

import skolard.persistence.SessionPersistence;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

public class StubFactory {
    
    public static StudentPersistence createStudentPersistence() {
        return new StudentStub(); // in-memory student persistence
    }

    public static TutorPersistence createTutorPersistence() {
        return new TutorStub(); // in-memory tutor persistence
    }

    public static SessionPersistence createSessionPersistence() {
        return new SessionStub(); // in-memory session persistence
    }
}





