package skolard.persistence.stub;

import skolard.persistence.LoginPersistence;

public class LoginStub implements LoginPersistence {
    @Override
    public boolean authenticateStudent(String email, String password) {
        return email.equals("test@student.com") && password.equals("pass123");
    }

    @Override
    public boolean authenticateTutor(String email, String password) {
        return email.equals("test@tutor.com") && password.equals("pass123");
    }
}
