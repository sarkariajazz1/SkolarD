package skolard.utils;

public class CourseUtil {

    /**
     * Normalizes a course code by removing all whitespace, converting it to uppercase,
     * and preparing it for validation or comparison.
     *
     * @param code The course code string to normalize. Can be null.
     * @return The normalized course code (e.g., "COMP1010"), or null if the input was null.
     */
    public static String normalizeCourseCode(String code) {
        if (code == null) {
            return null; // Return null if the input code is null.
        }

        // Remove all whitespace characters (spaces, tabs, newlines, etc.) and convert the string to uppercase.
        String cleaned = code.replaceAll("\\s+", "").toUpperCase();

        return cleaned; // Return the cleaned and normalized course code.
    }

    /**
     * Checks if a given course code matches the standard format of four uppercase letters followed by four digits (e.g., "COMP1010").
     * The input code is first normalized before validation.
     *
     * @param code The course code string to validate.
     * @return True if the normalized course code matches the expected format, false otherwise.
     */
    public static boolean isValidCourseCode(String code) {
        // Normalize the course code to ensure consistent validation.
        String normalized = normalizeCourseCode(code);
        // Check if the normalized code is not null and matches the regex pattern:
        // [A-Z]{4} - exactly four uppercase letters
        // \\d{4}   - exactly four digits
        return normalized != null && normalized.matches("[A-Z]{4}\\d{4}");
    }

    /**
     * Compares two course codes for equality after normalizing them.
     * This provides a flexible comparison that ignores case and whitespace.
     *
     * @param code1 The first course code string to compare.
     * @param code2 The second course code string to compare.
     * @return True if both normalized course codes are identical, false otherwise.
     * @throws NullPointerException If either code1 or code2 is null, as normalizeCourseCode returns null,
     * and .equals() would then be called on a null object.
     */
    public static boolean areSameCourse(String code1, String code2) {
        // Normalize both course codes before comparing them.
        // Note: This method will throw a NullPointerException if normalizeCourseCode(code1) returns null,
        // which happens if code1 itself is null. Consider adding null checks for robustness.
        return normalizeCourseCode(code1).equals(normalizeCourseCode(code2));
    }
}