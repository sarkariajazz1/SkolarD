package skolard.utils;

import java.util.regex.Pattern;

<<<<<<< HEAD
public class EmailUtil {
    private static final Pattern EMAIL_REGEX =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

=======
/**
 * Utility class for validating and normalizing email addresses.
 */
public final class EmailUtil {

    // Standard email pattern
    private static final Pattern EMAIL_REGEX =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    private EmailUtil() {
        // Prevent instantiation
    }

    /**
     * Checks whether the provided email address is syntactically valid.
     *
     * @param email the raw email input
     * @return true if valid format, false otherwise
     */
>>>>>>> dev
    public static boolean isValid(String email) {
        if (email == null) return false;
        String trimmed = email.trim().toLowerCase();
        return EMAIL_REGEX.matcher(trimmed).matches();
    }
<<<<<<< HEAD
=======

    /**
     * Normalizes the given email by trimming and converting to lowercase.
     * 
     * @param email the email input
     * @return normalized email string, or null if input was null
     */
    public static String normalize(String email) {
        return (email == null) ? null : email.trim().toLowerCase();
    }
>>>>>>> dev
}
