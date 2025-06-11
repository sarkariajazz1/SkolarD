package skolard.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordUtil {
    /**
     * Hashes a given password using the SHA-256 algorithm.
     * This method is used to securely store passwords by converting them into an irreversible hash string,
     * rather than storing them in plain text.
     *
     * @param password The plain-text password string to be hashed.
     * @return A hexadecimal string representation of the SHA-256 hash of the input password.
     * @throws RuntimeException If the SHA-256 algorithm is not available in the environment,
     * indicating a critical security library issue.
     */
    public static String hash(String password) {
        try {
            // Obtain an instance of the SHA-256 message digest algorithm.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Compute the hash of the password bytes using UTF-8 encoding.
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            // Convert the byte array into a hexadecimal string representation.
            StringBuilder sb = new StringBuilder();
            for (byte b : encodedHash) {
                sb.append(String.format("%02x", b)); // Format each byte as a two-digit hexadecimal number.
            }
            return sb.toString(); // Return the complete hexadecimal hash string.
        } catch (java.security.NoSuchAlgorithmException e) {
            // Catch the exception if SHA-256 algorithm is not found, and re-throw as a RuntimeException.
            throw new RuntimeException("Password hashing failed", e);
        }
    }
}
