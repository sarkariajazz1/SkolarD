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
}
