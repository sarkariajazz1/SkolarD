package skolard.utils;

import java.util.regex.Pattern;

public class EmailUtil {
    private static final Pattern EMAIL_REGEX =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static boolean isValid(String email) {
        if (email == null) return false;
        String trimmed = email.trim().toLowerCase();
        return EMAIL_REGEX.matcher(trimmed).matches();
    }
}
