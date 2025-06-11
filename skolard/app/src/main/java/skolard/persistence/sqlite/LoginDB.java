package skolard.persistence.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import skolard.persistence.LoginPersistence;
import skolard.utils.PasswordUtil;

public class LoginDB implements LoginPersistence {
    private final Connection connection;

    /**
     * Constructor that accepts a database connection.
     * 
     * @param connection an open SQLite connection
     */
    public LoginDB(Connection connection) {
        this.connection = connection;
    }

    /**
     * Authenticates a student by email and plaintext password.
     * 
     * @param email         student's email
     * @param plainPassword plaintext password to verify
     * @return true if authentication succeeds, false otherwise
     */
    @Override
    public boolean authenticateStudent(String email, String plainPassword) {
        String sql = "SELECT password FROM student WHERE email = ?";
        return checkPassword(email, plainPassword, sql);
    }

    /**
     * Authenticates a tutor by email and plaintext password.
     * 
     * @param email         tutor's email
     * @param plainPassword plaintext password to verify
     * @return true if authentication succeeds, false otherwise
     */
    @Override
    public boolean authenticateTutor(String email, String plainPassword) {
        String sql = "SELECT password FROM tutor WHERE email = ?";
        return checkPassword(email, plainPassword, sql);
    }

    /**
     * Authenticates a support staff by email and plaintext password.
     * 
     * @param email         support staff's email
     * @param plainPassword plaintext password to verify
     * @return true if authentication succeeds, false otherwise
     */
    @Override
    public boolean authenticateSupport(String email, String plainPassword) {
        String sql = "SELECT password FROM support WHERE email = ?";
        return checkPassword(email, plainPassword, sql);
    }

    /**
     * Internal helper method to check if the provided plaintext password matches
     * the stored hashed password for the given email using the provided SQL.
     * 
     * @param email         user email to search for
     * @param plainPassword plaintext password to verify
     * @param sql           SQL query string to fetch password hash
     * @return true if passwords match, false otherwise
     */
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

