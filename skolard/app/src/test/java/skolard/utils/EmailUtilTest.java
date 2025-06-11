package skolard.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailUtilTest {

    @Test
    void testIsValid() {
        assertTrue(EmailUtil.isValid("test@example.com"));
        assertFalse(EmailUtil.isValid("invalid-email"));
        assertFalse(EmailUtil.isValid(null));
        assertTrue(EmailUtil.isValid("  USER@Email.ca  "));
    }

    @Test
    void testNormalize() {
        assertEquals("user@email.com", EmailUtil.normalize("  User@Email.com "));
        assertNull(EmailUtil.normalize(null));
    }
}
