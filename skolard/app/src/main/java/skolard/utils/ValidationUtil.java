package skolard.utils;

/**
 * Shared utility methods for validating user input.
 * This class provides static methods to enforce various validation rules,
 * primarily by throwing {@link IllegalArgumentException} for invalid inputs.
 */
public final class ValidationUtil {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * All methods in this class are static.
     */
    private ValidationUtil() {
        // Static class: no instantiation allowed
    }

    /**
     * Validates input for creating a new user, specifically checking the name and email.
     *
     * @param name The user's name, which must be at least 2 characters long after trimming whitespace.
     * @param email The user's email address, which must be a valid format according to {@link EmailUtil#isValid(String)}.
     * @throws IllegalArgumentException If the name is null, too short, or the email is invalid.
     */
    public static void validateNewUser(String name, String email) {
        // Validate that the name is not null and has a length of at least 2 characters after trimming.
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters.");
        }

        // Validate that the email address is in a valid format using EmailUtil.
        if (!EmailUtil.isValid(email)) {
            throw new IllegalArgumentException("Invalid email address.");
        }
    }

    /**
     * Ensures that a given object is not null.
     * This is useful for validating method parameters or class fields that cannot be null.
     *
     * @param obj The object to check for nullity.
     * @param fieldName The name of the field or parameter being checked, used in the exception message.
     * @throws IllegalArgumentException If the object is null.
     */
    public static void requireNonNull(Object obj, String fieldName) {
        // Throw an IllegalArgumentException if the object is null.
        if (obj == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
    }

    /**
     * Ensures that a given string is not null and not empty (after trimming whitespace).
     * This is commonly used for validating string inputs like names, addresses, or other required text fields.
     *
     * @param str The string to check for null or emptiness.
     * @param fieldName The name of the field or parameter being checked, used in the exception message.
     * @throws IllegalArgumentException If the string is null or empty after trimming.
     */
    public static void requireNonEmpty(String str, String fieldName) {
        // Throw an IllegalArgumentException if the string is null or empty after trimming.
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }
}
