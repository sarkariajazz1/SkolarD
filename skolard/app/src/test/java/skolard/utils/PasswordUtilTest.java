package skolard.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    void testSameInputProducesSameHash() {
        String hash1 = PasswordUtil.hash("securePassword123");
        String hash2 = PasswordUtil.hash("securePassword123");
        assertEquals(hash1, hash2);
    }

    @Test
    void testDifferentInputsProduceDifferentHashes() {
        String hash1 = PasswordUtil.hash("password1");
        String hash2 = PasswordUtil.hash("password2");
        assertNotEquals(hash1, hash2);
    }

    @Test
    void testHashOutputIsNotNullOrEmpty() {
        String hash = PasswordUtil.hash("nonempty");
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }
}
