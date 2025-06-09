package skolard.utils;

public class CourseUtil {

    /**
     * Normalize a course code by removing all spaces, converting to uppercase,
     * and validating it matches the standard format (e.g., COMP1010).
     */
    public static String normalizeCourseCode(String code) {
        if (code == null) return null;

        // Remove all whitespace and convert to uppercase
        String cleaned = code.replaceAll("\\s+", "").toUpperCase();

        return cleaned;
    }

    /**
     * Check if a course code matches the standard 4-letter + 4-digit format.
     */
    public static boolean isValidCourseCode(String code) {
        String normalized = normalizeCourseCode(code);
        return normalized != null && normalized.matches("[A-Z]{4}\\d{4}");
    }

    /**
     * Compare two course codes flexibly.
     */
    public static boolean areSameCourse(String code1, String code2) {
        return normalizeCourseCode(code1).equals(normalizeCourseCode(code2));
    }
}
