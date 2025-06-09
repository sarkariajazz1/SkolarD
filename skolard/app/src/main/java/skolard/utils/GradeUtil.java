package skolard.utils;

public class GradeUtil {

    /**
     * Converts letter or numeric grades (as strings) into a numeric double value.
     * Returns -1.0 if the grade is unrecognized or invalid.
     */
    public static double toNumeric(String grade) {
        if (grade == null) return -1.0;

        return switch (grade.toUpperCase()) {
            case "A+" -> 90.0;
            case "A"  -> 80.0;
            case "B+" -> 75.0;
            case "B"  -> 70.0;
            case "C+" -> 65.0;
            case "C"  -> 60.0;
            case "D"  -> 50.0;
            case "F"  -> 40.0;
            default -> {
                try {
                    yield Double.parseDouble(grade);
                } catch (NumberFormatException e) {
                    yield -1.0;
                }
            }
        };
    }
}

