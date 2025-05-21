package skolard.persistence;

import skolard.persistence.stub.StubFactory;

public class PersistenceFactory {
    private static SessionPersistence sessionPersistence;
    private static StudentPersistence studentPersistence;
    private static TutorPersistence tutorPersistence;

    public static <T> T getPersistence(PersistenceType type, Class<T> clazz) {
        if (type == PersistenceType.STUB) {
            return StubFactory.getStub(clazz);
        }

        throw new UnsupportedOperationException("Only STUB persistence is supported.");
    }

    public static void initializeStubPersistence() {
        sessionPersistence = StubFactory.getStub(SessionPersistence.class);
        studentPersistence = StubFactory.getStub(StudentPersistence.class);
        tutorPersistence = StubFactory.getStub(TutorPersistence.class);
    }

    public static SessionPersistence getSessionPersistence() {
        if (sessionPersistence == null) {
            initializeStubPersistence();
        }
        return sessionPersistence;
    }

    public static StudentPersistence getStudentPersistence() {
        if (studentPersistence == null) {
            initializeStubPersistence();
        }
        return studentPersistence;
    }

    public static TutorPersistence getTutorPersistence() {
        if (tutorPersistence == null) {
            initializeStubPersistence();
        }
        return tutorPersistence;
    }
}


