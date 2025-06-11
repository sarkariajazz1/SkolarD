package skolard.persistence;

import java.sql.Connection;

import skolard.persistence.sqlite.CardDB;
import skolard.persistence.sqlite.FAQDB;
import skolard.persistence.sqlite.LoginDB;
import skolard.persistence.sqlite.MessageDB;
import skolard.persistence.sqlite.RatingDB;
import skolard.persistence.sqlite.RatingRequestDB;
import skolard.persistence.sqlite.SessionDB;
import skolard.persistence.sqlite.StudentDB;
import skolard.persistence.sqlite.SupportDB;
import skolard.persistence.sqlite.TutorDB;
import skolard.persistence.stub.StubFactory;

public class PersistenceProvider {

    public static void initializeSqlite(Connection conn) {
        StudentPersistence studentPersistence = new StudentDB(conn);
        TutorPersistence tutorPersistence = new TutorDB(conn);
        SessionPersistence sessionPersistence = new SessionDB(conn, studentPersistence, tutorPersistence);
        SupportPersistence supportPersistence = new SupportDB(conn, studentPersistence, tutorPersistence);
        RatingRequestPersistence ratingRequestPersistence = new RatingRequestDB(conn, studentPersistence, sessionPersistence);

        PersistenceRegistry.setStudentPersistence(studentPersistence);
        PersistenceRegistry.setTutorPersistence(tutorPersistence);
        PersistenceRegistry.setSessionPersistence(sessionPersistence);
        PersistenceRegistry.setMessagePersistence(new MessageDB(conn));
        PersistenceRegistry.setLoginPersistence(new LoginDB(conn));
        PersistenceRegistry.setCardPersistence(new CardDB(conn));
        PersistenceRegistry.setSupportPersistence(supportPersistence);
        PersistenceRegistry.setRatingRequestPersistence(ratingRequestPersistence);
        PersistenceRegistry.setRatingPersistence(new RatingDB(conn));
        PersistenceRegistry.setFAQPersistence(new FAQDB(conn));
    }

    public static void initializeStubs() {
        StudentPersistence studentPersistence = StubFactory.createStudentPersistence();
        TutorPersistence tutorPersistence = StubFactory.createTutorPersistence();
        LoginPersistence loginPersistence = StubFactory.createLoginPersistence(studentPersistence, tutorPersistence);

        PersistenceRegistry.setStudentPersistence(studentPersistence);
        PersistenceRegistry.setTutorPersistence(tutorPersistence);
        PersistenceRegistry.setLoginPersistence(loginPersistence);
        PersistenceRegistry.setSessionPersistence(StubFactory.createSessionPersistence());
        PersistenceRegistry.setMessagePersistence(StubFactory.createMessagePersistence());
        PersistenceRegistry.setCardPersistence(StubFactory.createCardPersistence());
        PersistenceRegistry.setSupportPersistence(StubFactory.createSupportPersistence()); 
        PersistenceRegistry.setRatingRequestPersistence(StubFactory.createRatingRequestStub());
        PersistenceRegistry.setRatingPersistence(StubFactory.createRatingStub());
        PersistenceRegistry.setFAQPersistence(StubFactory.createFAQPersistence());
    }
}
