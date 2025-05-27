/*
package skolard.persistence;

import skolard.persistence.stub.StubFactory;

public class PersistenceFactory {

    private static StudentPersistence studentPersistence;
    private static TutorPersistence tutorPersistence;
    private static SessionPersistence sessionPersistence;
    private static MessagePersistence messagePersistence;
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
        //messagePersistence = StubFactory.createMessagePersistence();
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

    public static MessagePersistence getMessagePersistence() {
        return messagePersistence;
    }

    public static void reset() {
        sessionPersistence = null;
        studentPersistence = null;
        tutorPersistence = null;
    }
}

*/
package skolard.persistence;

import java.sql.Connection;
import java.util.List;

import skolard.persistence.sqlite.SchemaInitializer;
import skolard.persistence.sqlite.SessionDB;
import skolard.persistence.sqlite.StudentDB;
import skolard.persistence.sqlite.TutorDB;
import skolard.persistence.stub.StubFactory;

public class PersistenceFactory {

    private static StudentPersistence studentPersistence;
    private static TutorPersistence tutorPersistence;
    private static SessionPersistence sessionPersistence;
    private static MessagePersistence messagePersistence;
    public static void initialize(PersistenceType type, boolean seed) {
        if (studentPersistence != null || tutorPersistence != null || sessionPersistence != null) return;

        switch (type) {
            case PROD, TEST -> {
                String dbPath = type == PersistenceType.PROD ? "skolard.db" : "test.db";
                try {
                    ConnectionManager.initialize(dbPath);
                    Connection conn = ConnectionManager.get();

                    SchemaInitializer.initializeSchema(conn);

                    if (seed) {
                        DatabaseSeeder.seed(conn, List.of(
                            "/seed_students.sql",
                            "/seed_tutors.sql",
                            "/seed_sessions.sql"
                        ));
                    }

                    studentPersistence = new StudentDB(conn);
                    tutorPersistence = new TutorDB(conn);
                    sessionPersistence = new SessionDB(conn, studentPersistence, tutorPersistence);

                } catch (Exception e) {
                    fallbackToStubs(e);
                }
            }
            case STUB -> fallbackToStubs(null);
        }
    }

    private static void fallbackToStubs(Exception e) {
        studentPersistence = StubFactory.createStudentPersistence();
        tutorPersistence = StubFactory.createTutorPersistence();
        sessionPersistence = StubFactory.createSessionPersistence();
        if (e != null) {
            System.err.println("⚠️ Falling back to stubs due to: " + e.getMessage());
        }
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

    public static MessagePersistence getMessagePersistence() {
        return messagePersistence;
    }
    public static void reset() {
        ConnectionManager.close();
        studentPersistence = null;
        tutorPersistence = null;
        sessionPersistence = null;
    }
}
