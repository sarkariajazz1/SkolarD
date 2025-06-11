package skolard.objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SupportTest {

    @Test
    public void testConstructorAndFields() {
        Support support = new Support("Sam Support", "sam@skolard.com");

        assertEquals("Sam Support", support.getName());
        assertEquals("sam@skolard.com", support.getEmail());
        assertTrue(support instanceof User);
    }

    @Test
    public void testToString() {
        Support support = new Support("Rita Helper", "rita@skolard.com");
        String expected = "Support Staff: Rita Helper (rita@skolard.com)";

        assertEquals(expected, support.toString());
    }
}
