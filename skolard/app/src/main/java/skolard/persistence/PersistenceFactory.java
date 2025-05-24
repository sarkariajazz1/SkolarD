package skolard.persistence;

import skolard.persistence.stub.StubFactory;

public class PersistenceFactory {

    private static StudentPersistence studentPersistence;
    private static TutorPersistence tutorPersistence;
    private static SessionPersistence sessionPersistence;

    public static void initialize(PersistenceType type, boolean seed) {
        switch(type) {
            case PROD, TEST -> {
                fallBackToStub(); //Will always use stub for iteration 1
            }
            case STUB -> fallBackToStub();
        }
    }

    private static void fallBackToStub() {
        studentPersistence = StubFactory.createStudentPersistence();
        tutorPersistence = StubFactory.createTutorPersistence();
        sessionPersistence = StubFactory.createSessionPersistence();
    }

    public static StudentPersistence getStudentPersistence() {
        return studentPersistence;
    }

    public static TutorPersistence getTutorPersistence() {
        return tutorPersistence;
    }

    public static SessionPersistence getSessionPersistence() {
        return sessionPersistence;
    }

    public static void reset() {
        sessionPersistence = null;
        studentPersistence = null;
        tutorPersistence = null;
    }
}

