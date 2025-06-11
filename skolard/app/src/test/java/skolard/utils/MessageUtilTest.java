package skolard.utils;

import org.junit.jupiter.api.Test;
import skolard.objects.Message;

import static org.junit.jupiter.api.Assertions.*;

public class MessageUtilTest {

    @Test
    void testValidMessage() {
        Message m = new Message(0, null, "student@skolard.ca", "tutor@skolard.ca", "student@skolard.ca", "Hello tutor!");
        assertTrue(MessageUtil.validMessage(m));
    }

    @Test
    void testEmptyMessageFails() {
        Message m = new Message(0, null, "student@skolard.ca", "tutor@skolard.ca", "student@skolard.ca", "");
        assertFalse(MessageUtil.validMessage(m));
    }

    @Test
    void testInvalidEmailsFails() {
        Message m = new Message(0, null, "invalid", "also-invalid", "invalid", "Hi");
        assertFalse(MessageUtil.validMessage(m));
    }

    @Test
    void testSenderNotMatchingFails() {
        Message m = new Message(0, null, "student@skolard.ca", "tutor@skolard.ca", "random@skolard.ca", "Hey");
        assertFalse(MessageUtil.validMessage(m));
    }

    @Test
    void testStudentEqualsTutorFails() {
        Message m = new Message(0, null, "same@skolard.ca", "same@skolard.ca", "same@skolard.ca", "Hi");
        assertFalse(MessageUtil.validMessage(m));
    }
}
