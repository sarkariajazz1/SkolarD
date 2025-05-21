package skolard.persistence.stub;

import skolard.persistence.SessionPersistence;
import skolard.persistence.StudentPersistence;
import skolard.persistence.TutorPersistence;

/**
 * A simple factory to instantiate in-memory (stub) persistence classes.
 * These are useful for early development and testing without a database.
 */
public class StubFactory {

    /**
     * Creates and returns an in-memory session database.
     *
     * @return Stub implementation of SessionPersistence
     */
    public static SessionPersistence createSessionPersistence() {
        return new SessionStub(); // In-memory session persistence
    }

    /**
     * Creates and returns an in-memory student database.
     *
     * @return Stub implementation of StudentPersistence
     */
    public static StudentPersistence createStudentPersistence() {
        return new StudentStub(); // In-memory student persistence
    }

    /**
     * Creates and returns an in-memory tutor database.
     *
     * @return Stub implementation of TutorPersistence
     */
    public static TutorPersistence createTutorPersistence() {
        return new TutorStub(); // In-memory tutor persistence
    }
}
