package skolard.persistence;

import skolard.persistence.stub.StubFactory;

/**
 * A factory class for managing access to persistence layer objects (Student, Tutor, and Session).
 * This class supports switching between different persistence types (e.g., STUB, DATABASE)
 * by instantiating the appropriate concrete implementation.
 *
 * Note: Currently only STUB-based persistence is supported.
 */
public class PersistenceFactory {

    // Singleton instances of each persistence interface
    private static SessionPersistence sessionPersistence;
    private static StudentPersistence studentPersistence;
    private static TutorPersistence tutorPersistence;

    /**
     * Generic method to retrieve a persistence implementation for a given type and class.
     * Primarily intended for internal use or flexible factory scenarios.
     *
     * @param type  the persistence type (e.g., STUB)
     * @param clazz the interface class to retrieve (e.g., StudentPersistence.class)
     * @param <T>   the type of persistence interface
     * @return the corresponding stub implementation for the requested interface
     * @throws UnsupportedOperationException if the persistence type is unsupported
     */
    public static <T> T getPersistence(PersistenceType type, Class<T> clazz) {
        if (type == PersistenceType.STUB) {
            return StubFactory.getStub(clazz);
        }
        throw new UnsupportedOperationException("Only STUB persistence is supported.");
    }

    /**
     * Initializes all stub persistence objects. This method should be called once at startup
     * before accessing any persistence interface. Repeated calls will overwrite existing instances.
     */
    public static void initializeStubPersistence() {
        sessionPersistence = StubFactory.getStub(SessionPersistence.class);
        studentPersistence = StubFactory.getStub(StudentPersistence.class);
        tutorPersistence = StubFactory.getStub(TutorPersistence.class);
    }

    /**
     * Retrieves the singleton instance of SessionPersistence.
     * If the persistence layer has not been initialized yet, it initializes it with STUB.
     *
     * @return the SessionPersistence instance
     */
    public static SessionPersistence getSessionPersistence() {
        if (sessionPersistence == null) {
            initializeStubPersistence();
        }
        return sessionPersistence;
    }

    /**
     * Retrieves the singleton instance of StudentPersistence.
     * If the persistence layer has not been initialized yet, it initializes it with STUB.
     *
     * @return the StudentPersistence instance
     */
    public static StudentPersistence getStudentPersistence() {
        if (studentPersistence == null) {
            initializeStubPersistence();
        }
        return studentPersistence;
    }

    /**
     * Retrieves the singleton instance of TutorPersistence.
     * If the persistence layer has not been initialized yet, it initializes it with STUB.
     *
     * @return the TutorPersistence instance
     */
    public static TutorPersistence getTutorPersistence() {
        if (tutorPersistence == null) {
            initializeStubPersistence();
        }
        return tutorPersistence;
    }
}
