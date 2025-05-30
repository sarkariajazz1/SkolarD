package skolard.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class PaymentHandlerTest {
    private PaymentHandler handler;

    @Before
    public void setup(){
        handler = new PaymentHandler();
    }

    @Test
    public void testValidCard(){
        String number = "4111 1111 1111 1111"; // Valid Visa Luhn number
        String expiry = "12/29"; // Future expiry
        String cvv = "123";      // Valid CVV

        assertTrue(handler.validateCard(number, expiry, cvv));
    }

    @Test
    public void testInvalidCardNumber_LuhnFail() {
        String number = "4111 1111 1111 1112"; // Invalid Luhn
        String expiry = "12/29";
        String cvv = "123";

        assertFalse(handler.validateCard(number, expiry, cvv));
    }

    @Test
    public void testInvalidCardNumber_SameDigits() {
        String number = "1111 1111 1111 1111"; // All same digits
        String expiry = "12/29";
        String cvv = "123";

        assertFalse(handler.validateCard(number, expiry, cvv));
    }

    @Test
    public void testInvalidCardNumber_LengthTooShort() {
        String number = "123456789012"; // Too short
        String expiry = "12/29";
        String cvv = "123";

        assertFalse(handler.validateCard(number, expiry, cvv));
    }

    @Test
    public void testInvalidExpiry_PastDate() {
        String number = "4111 1111 1111 1111";
        String expiry = "01/20"; // Past date
        String cvv = "123";

        assertFalse(handler.validateCard(number, expiry, cvv));
    }

    @Test
    public void testInvalidExpiry_Format() {
        String number = "4111 1111 1111 1111";
        String expiry = "2025-12"; // Invalid format
        String cvv = "123";

        assertFalse(handler.validateCard(number, expiry, cvv));
    }

    @Test
    public void testInvalidCVV_TooShort() {
        String number = "4111 1111 1111 1111";
        String expiry = "12/29";
        String cvv = "12"; // Too short

        assertFalse(handler.validateCard(number, expiry, cvv));
    }

    @Test
    public void testInvalidCVV_NonNumeric() {
        String number = "4111 1111 1111 1111";
        String expiry = "12/29";
        String cvv = "12a"; // Invalid characters

        assertFalse(handler.validateCard(number, expiry, cvv));
    }

    @Test
    public void testEmptyFields() {
        assertFalse(handler.validateCard("", "", ""));
    }

    @Test
    public void testNullSafeCheck() {
        // Should safely return false if CVV is empty
        assertFalse(handler.validateCard("4111 1111 1111 1111", "12/29", ""));
    }

}
