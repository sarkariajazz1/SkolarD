package skolard.persistence.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import skolard.persistence.LoginPersistence;
import skolard.utils.PasswordUtil;

public class LoginDB implements LoginPersistence {
    private final Connection connection;

    public LoginDB(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean authenticateStudent(String email, String plainPassword) {
        String sql = "SELECT password FROM student WHERE email = ?";
        return checkPassword(email, plainPassword, sql);
    }

    @Override
    public boolean authenticateTutor(String email, String plainPassword) {
        String sql = "SELECT password FROM tutor WHERE email = ?";
        return checkPassword(email, plainPassword, sql);
    }

    @Override
    public boolean authenticateSupport(String email, String plainPassword) {
        String sql = "SELECT password FROM support WHERE email = ?";
        return checkPassword(email, plainPassword, sql);
    }


    private boolean checkPassword(String email, String plainPassword, String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                return PasswordUtil.hash(plainPassword).equals(storedHash);
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return false;
    }
}
