package skolard.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    @Test
    void testValidateNewUser_validInput() {
        assertDoesNotThrow(() -> ValidationUtil.validateNewUser("Amrit", "amrit@skolard.ca"));
    }

    @Test
    void testValidateNewUser_invalidName() {
        Exception ex1 = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateNewUser("A", "amrit@skolard.ca"));
        assertEquals("Name must be at least 2 characters.", ex1.getMessage());

        Exception ex2 = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateNewUser(null, "amrit@skolard.ca"));
        assertEquals("Name must be at least 2 characters.", ex2.getMessage());
    }

    @Test
    void testValidateNewUser_invalidEmail() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateNewUser("Amrit", "invalid-email"));
        assertEquals("Invalid email address.", ex.getMessage());
    }

    @Test
    void testRequireNonNull_passesWhenNotNull() {
        assertDoesNotThrow(() -> ValidationUtil.requireNonNull("someValue", "Field"));
    }

    @Test
    void testRequireNonNull_throwsWhenNull() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requireNonNull(null, "Username"));
        assertEquals("Username cannot be null.", ex.getMessage());
    }

    @Test
    void testRequireNonEmpty_validString() {
        assertDoesNotThrow(() -> ValidationUtil.requireNonEmpty("hello", "Comment"));
    }

    @Test
    void testRequireNonEmpty_blankOrNull() {
        Exception ex1 = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requireNonEmpty("   ", "Comment"));
        assertEquals("Comment cannot be empty.", ex1.getMessage());

        Exception ex2 = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requireNonEmpty(null, "Comment"));
        assertEquals("Comment cannot be empty.", ex2.getMessage());
    }
}
