package skolard.persistence;

import skolard.persistence.stub.StubFactory;

/**
 * Factory class responsible for initializing and providing access
 * to the persistence layer (Session, Student, Tutor).
 * 
 * Supports switching between stub, test, or production implementations.
 */
public class PersistenceFactory {

    private static SessionPersistence sessionPersistence;
    private static StudentPersistence studentPersistence;
    private static TutorPersistence tutorPersistence;

    /**
     * Initializes the persistence layer depending on the selected type.
     * Currently, only stub mode is used (Iteration 1).
     *
     * @param type The type of persistence (STUB, TEST, PROD)
     * @param seed Whether to preload data (unused for stub)
     */
    public static void initialize(PersistenceType type, boolean seed) {
        switch(type) {
            case PROD, TEST -> {
                fallBackToStub(); // Always uses stub implementation in Iteration 1
            }
            case STUB -> fallBackToStub();
        }
    }

    /**
     * Fallback method that assigns stub implementations for all persistence types.
     * Used when DB functionality is unavailable or not yet implemented.
     */
    private static void fallBackToStub() {
        sessionPersistence = StubFactory.createSessionPersistence();
        studentPersistence = StubFactory.createStudentPersistence();
        tutorPersistence = StubFactory.createTutorPersistence();
    }

    // Returns the current SessionPersistence instance
    public static SessionPersistence getSessionPersistence() {
        return sessionPersistence;
    }

    // Returns the current StudentPersistence instance
    public static StudentPersistence getStudentPersistence() {
        return studentPersistence;
    }

    // Returns the current TutorPersistence instance
    public static TutorPersistence getTutorPersistence() {
        return tutorPersistence;
    }

    /**
     * Resets all persistence instances to null.
     * Useful for testing or re-initialization.
     */
    public static void reset() {
        sessionPersistence = null;
        studentPersistence = null;
        tutorPersistence = null;
    }
}
