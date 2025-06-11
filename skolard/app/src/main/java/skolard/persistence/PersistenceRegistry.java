package skolard.persistence;

/**
 * Registry class holding static references to all persistence interfaces.
 * Acts as a centralized access point for all persistence implementations
 * used across the application.
 */
public class PersistenceRegistry {
    // Static fields for each persistence interface
    private static StudentPersistence studentPersistence;
    private static TutorPersistence tutorPersistence;
    private static SessionPersistence sessionPersistence;
    private static MessagePersistence messagePersistence;
    private static LoginPersistence loginPersistence;
    private static CardPersistence cardPersistence;
    private static SupportPersistence supportPersistence;
    private static RatingRequestPersistence ratingRequestPersistence;
    private static RatingPersistence ratingPersistence;
    private static FAQPersistence faqPersistence; // ✅ NEW

    // Getter and setter for StudentPersistence
    public static StudentPersistence getStudentPersistence() {
        return studentPersistence;
    }
    public static void setStudentPersistence(StudentPersistence sp) {
        studentPersistence = sp;
    }

    // Getter and setter for TutorPersistence
    public static TutorPersistence getTutorPersistence() {
        return tutorPersistence;
    }
    public static void setTutorPersistence(TutorPersistence tp) {
        tutorPersistence = tp;
    }

    // Getter and setter for SessionPersistence
    public static SessionPersistence getSessionPersistence() {
        return sessionPersistence;
    }
    public static void setSessionPersistence(SessionPersistence sp) {
        sessionPersistence = sp;
    }

    // Getter and setter for MessagePersistence
    public static MessagePersistence getMessagePersistence() {
        return messagePersistence;
    }
    public static void setMessagePersistence(MessagePersistence mp) {
        messagePersistence = mp;
    }

    // Getter and setter for LoginPersistence
    public static LoginPersistence getLoginPersistence() {
        return loginPersistence;
    }
    public static void setLoginPersistence(LoginPersistence lp) {
        loginPersistence = lp;
    }

    // Getter and setter for CardPersistence
    public static CardPersistence getCardPersistence() {
        return cardPersistence;
    }
    public static void setCardPersistence(CardPersistence cp) {
        cardPersistence = cp;
    }

    // Getter and setter for SupportPersistence
    public static SupportPersistence getSupportPersistence() {
        return supportPersistence;
    }
    public static void setSupportPersistence(SupportPersistence sp) {
        supportPersistence = sp;
    }

    // Getter and setter for RatingRequestPersistence
    public static RatingRequestPersistence getRatingRequestPersistence() {
        return ratingRequestPersistence;
    }
    public static void setRatingRequestPersistence(RatingRequestPersistence rrp) {
        ratingRequestPersistence = rrp;
    }

    // Getter and setter for RatingPersistence
    public static RatingPersistence getRatingPersistence() {
        return ratingPersistence;
    }
    public static void setRatingPersistence(RatingPersistence rp) {
        ratingPersistence = rp;
    }

    // Getter and setter for FAQPersistence
    public static FAQPersistence getFAQPersistence() {
        return faqPersistence;
    }
    public static void setFAQPersistence(FAQPersistence fp) {
        faqPersistence = fp;
    }
}