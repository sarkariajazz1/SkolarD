package skolard.utils;

public class GradeUtil {

    /**
     * Converts a letter grade (e.g., "A+", "B") or a numeric grade (as a string) into a standardized double value.
     * This utility function helps in consistently representing grades for calculations or comparisons.
     *
     * @param grade The grade as a {@link String} (e.g., "A+", "85.5", "F").
     * @return A {@code double} representing the numeric equivalent of the grade.
     * Returns {@code -1.0} if the input string is null, unrecognized, or cannot be parsed into a number.
     */
    public static double toNumeric(String grade) {
        // Return -1.0 immediately if the input grade string is null.
        if (grade == null) {
            return -1.0;
        }

        // Use a switch expression to handle common letter grades and convert them to numeric values.
        // The input grade is converted to uppercase to ensure case-insensitive matching.
        return switch (grade.toUpperCase()) {
            case "A+" -> 90.0; // Assign 90.0 for "A+".
            case "A" -> 80.0;  // Assign 80.0 for "A".
            case "B+" -> 75.0; // Assign 75.0 for "B+".
            case "B" -> 70.0;  // Assign 70.0 for "B".
            case "C+" -> 65.0; // Assign 65.0 for "C+".
            case "C" -> 60.0;  // Assign 60.0 for "C".
            case "D" -> 50.0;  // Assign 50.0 for "D".
            case "F" -> 40.0;  // Assign 40.0 for "F".
            // If the grade doesn't match a predefined letter grade, attempt to parse it as a double.
            default -> {
                try {
                    yield Double.parseDouble(grade); // Attempt to parse the string directly to a double.
                } catch (NumberFormatException e) {
                    yield -1.0; // If parsing fails (e.g., "invalid" or "X"), return -1.0.
                }
            }
        };
    }
}

