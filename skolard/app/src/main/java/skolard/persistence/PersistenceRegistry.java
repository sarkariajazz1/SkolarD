package skolard.persistence;

public class PersistenceRegistry {
    private static StudentPersistence studentPersistence;
    private static TutorPersistence tutorPersistence;
    private static SessionPersistence sessionPersistence;
    private static MessagePersistence messagePersistence;
    private static LoginPersistence loginPersistence;
    private static CardPersistence cardPersistence;
    private static SupportPersistence supportPersistence;
    private static RatingRequestPersistence ratingRequestPersistence;
    private static RatingPersistence ratingPersistence;

    public static StudentPersistence getStudentPersistence() {
        return studentPersistence;
    }

    public static void setStudentPersistence(StudentPersistence sp) {
        studentPersistence = sp;
    }

    public static TutorPersistence getTutorPersistence() {
        return tutorPersistence;
    }

    public static void setTutorPersistence(TutorPersistence tp) {
        tutorPersistence = tp;
    }

    public static SessionPersistence getSessionPersistence() {
        return sessionPersistence;
    }

    public static void setSessionPersistence(SessionPersistence sp) {
        sessionPersistence = sp;
    }

    public static MessagePersistence getMessagePersistence() {
        return messagePersistence;
    }

    public static void setMessagePersistence(MessagePersistence mp) {
        messagePersistence = mp;
    }

    public static LoginPersistence getLoginPersistence() {
        return loginPersistence;
    }

    public static void setLoginPersistence(LoginPersistence lp) {
        loginPersistence = lp;
    }

    public static CardPersistence getCardPersistence() {
        return cardPersistence;
    }

    public static void setCardPersistence(CardPersistence cp) {
        cardPersistence = cp;
    }

    public static SupportPersistence getSupportPersistence() {
        return supportPersistence;
    }

    public static void setSupportPersistence(SupportPersistence sp) {
        supportPersistence = sp;
    }

    public static RatingRequestPersistence getRatingRequestPersistence() {
        return ratingRequestPersistence;
    }

    public static void setRatingRequestPersistence(RatingRequestPersistence rrp) {
        ratingRequestPersistence = rrp;
    }

    public static RatingPersistence getRatingPersistence() {
        return ratingPersistence;
    }
    public static void setRatingPersistence(RatingPersistence rp) {
        ratingPersistence = rp;
    }
}
