package skolard.utils;

/**
 * Shared utility methods for validating user input.
 */
public final class ValidationUtil {

    private ValidationUtil() {
        // Static class: no instantiation allowed
    }

    public static void validateNewUser(String name, String email) {
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters.");
        }

        if (!EmailUtil.isValid(email)) {
            throw new IllegalArgumentException("Invalid email address.");
        }
    }

    public static void requireNonNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
    }

    public static void requireNonEmpty(String str, String fieldName) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }
}
