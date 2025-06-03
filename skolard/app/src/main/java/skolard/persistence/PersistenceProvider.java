package skolard.persistence;

import java.sql.Connection;

import skolard.persistence.sqlite.*;
import skolard.persistence.stub.StubFactory;

public class PersistenceProvider {

    public static void initializeSqlite(Connection conn) {
        StudentPersistence studentPersistence = new StudentDB(conn);
        TutorPersistence tutorPersistence = new TutorDB(conn);
        SessionPersistence sessionPersistence = new SessionDB(conn, studentPersistence, tutorPersistence);
        SupportPersistence supportPersistence = new SupportDB(conn, studentPersistence, tutorPersistence);

        PersistenceRegistry.setStudentPersistence(studentPersistence);
        PersistenceRegistry.setTutorPersistence(tutorPersistence);
        PersistenceRegistry.setSessionPersistence(sessionPersistence);
        PersistenceRegistry.setMessagePersistence(new MessageDB(conn));
        PersistenceRegistry.setLoginPersistence(new LoginDB(conn));
        PersistenceRegistry.setCardPersistence(new CardDB(conn));
        PersistenceRegistry.setSupportPersistence(supportPersistence); // NEW
    }

    public static void initializeStubs() {
        PersistenceRegistry.setStudentPersistence(StubFactory.createStudentPersistence());
        PersistenceRegistry.setTutorPersistence(StubFactory.createTutorPersistence());
        PersistenceRegistry.setSessionPersistence(StubFactory.createSessionPersistence());
        PersistenceRegistry.setMessagePersistence(StubFactory.createMessagePersistence());
        PersistenceRegistry.setLoginPersistence(StubFactory.createLoginPersistence());
        PersistenceRegistry.setCardPersistence(StubFactory.createCardPersistence());
        PersistenceRegistry.setSupportPersistence(StubFactory.createSupportPersistence()); // NEW
    }
}
