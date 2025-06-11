package skolard.persistence;
public interface LoginPersistence {
    boolean authenticateStudent(String email, String password);
    boolean authenticateTutor(String email, String password);
    boolean authenticateSupport(String email, String password);
}
