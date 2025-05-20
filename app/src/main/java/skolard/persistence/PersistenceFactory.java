package skolard.persistence;

import skolard.persistence.stub.StubFactory;

public class PersistenceFactory {

    private static SessionPersistence sessionPersistence;
    private static StudentPersistence studentPersistence;
    private static TutorPersistence tutorPersistence;

    public static void initialize(PersistenceType type, boolean seed) {
        switch(type) {
            case PROD, TEST -> {
                
            }
            case STUB -> fallBackToStub();

    }

    private static void fallBackToStub() {
        sessionPersistence = StubFactory.createSessionPersistence();
        studentPersistence = StubFactory.createStudentPersistence();
        tutorPersistence = StubFactory.createTutorPersistence();
    }

    public static SessionPersistence getSessionPersistence() {
        return sessionPersistence;
    }

    public static StudentPersistence getStudentPersistence() {
        return studentPersistence;
    }

    public static TutorPersistence getTutorPersistence() {
        return tutorPersistence;
    }

    public static void reset() {
        sessionPersistence = null;
        studentPersistence = null;
        tutorPersistence = null;
    }
}
