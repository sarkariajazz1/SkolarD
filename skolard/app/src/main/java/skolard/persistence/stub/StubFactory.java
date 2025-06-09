package skolard.persistence.stub;

import skolard.persistence.*;

public class StubFactory {

    public static StudentPersistence createStudentPersistence() {
        return new StudentStub();
    }

    public static TutorPersistence createTutorPersistence() {
        return new TutorStub();
    }

    public static SessionPersistence createSessionPersistence() {
        return new SessionStub();
    }

    public static MessagePersistence createMessagePersistence() {
        return new MessageStub();
    }

    public static LoginPersistence createLoginPersistence(StudentPersistence sp, TutorPersistence tp) {
        return new LoginStub(sp, tp);
    }

    public static CardPersistence createCardPersistence() {
        return new CardStub();
    }

    public static SupportPersistence createSupportPersistence() {
        return new SupportStub();
    }
}
