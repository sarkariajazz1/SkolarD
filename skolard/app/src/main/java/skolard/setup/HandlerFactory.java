package skolard.setup;

import skolard.logic.auth.LoginHandler;
import skolard.logic.matching.MatchingHandler;
import skolard.logic.message.MessageHandler;
import skolard.logic.profile.DefaultProfileFormatter;
import skolard.logic.profile.ProfileHandler;
import skolard.persistence.PersistenceRegistry;
import skolard.persistence.stub.LoginStub;
import skolard.persistence.stub.MessageStub;
import skolard.persistence.stub.SessionStub;
import skolard.persistence.stub.StudentStub;
import skolard.persistence.stub.TutorStub;

public class HandlerFactory {
    private static boolean useStub = false; // toggle this for real vs test mode

    public static void useStubPersistence(boolean value) {
        useStub = value;
    }

    public static ProfileHandler createProfileHandler() {
        if (useStub) {
            return new ProfileHandler(new StudentStub(), new TutorStub(), new DefaultProfileFormatter());
        } else {
            return new ProfileHandler(
                PersistenceRegistry.getStudentPersistence(),
                PersistenceRegistry.getTutorPersistence(),
                new DefaultProfileFormatter()
            );
        }
    }

    public static LoginHandler createLoginHandler() {
        return new LoginHandler(useStub ? new LoginStub() : PersistenceRegistry.getLoginPersistence());
    }

    public static MessageHandler createMessageHandler() {
        return new MessageHandler(useStub ? new MessageStub() : PersistenceRegistry.getMessagePersistence());
    }

    public static MatchingHandler createMatchingHandler() {
        return new MatchingHandler(useStub ? new SessionStub() : PersistenceRegistry.getSessionPersistence());
    }
}
