package skolard.utils;

public class GradeUtil {

    /**
     * Converts letter or numeric grades (as strings) into a numeric double value.
     * Returns -1.0 if the grade is unrecognized or invalid.
     */
    public static double toNumeric(String grade) {
        if (grade == null) return -1.0;

        switch (grade.toUpperCase()) {
            case "A+": return 90.0;
            case "A":  return 80.0;
            case "B+": return 75.0;
            case "B":  return 70.0;
            case "C+": return 65.0;
            case "C":  return 60.0;
            case "D":  return 50.0;
            case "F":  return 40.0;
            default:
                try {
                    return Double.parseDouble(grade);
                } catch (NumberFormatException e) {
                    return -1.0;
                }
        }
    }
}

