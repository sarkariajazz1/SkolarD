package skolard.logic;

import skolard.objects.Student;
import skolard.objects.Card;
import skolard.persistence.CardPersistence;
import skolard.persistence.stub.CardStub;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before; 
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class PaymentHandlerTest {
    private CardPersistence cp;
    private PaymentHandler handler;
    private Student student;

    @Before
    public void setup(){
        cp = new CardStub();
        handler = new PaymentHandler(cp);

    }

    @Test
    public void testValidCard(){
        String name = "John Doe";
        String number = "4111 1111 1111 1111"; // Valid Visa Luhn number
        String expiry = "12/29"; // Future expiry
        String cvv = "123";      // Valid CVV

        assertTrue(handler.payWithCard(name, number, expiry, cvv, false, student));
    }

    @Test
    public void testInvalidCardNumber_LuhnFail() {
        String name = "John Doe";
        String number = "4111 1111 1111 1112"; // Invalid Luhn
        String expiry = "12/29";
        String cvv = "123";

        assertFalse(handler.payWithCard(name, number, expiry, cvv, false, student));
    }

    @Test
    public void testInvalidCardNumber_SameDigits() {
        String name = "John Doe";
        String number = "1111 1111 1111 1111"; // All same digits
        String expiry = "12/29";
        String cvv = "123";

        assertFalse(handler.payWithCard(name, number, expiry, cvv, false, student));
    }

    @Test
    public void testInvalidCardNumber_LengthTooShort() {
        String name = "John Doe";
        String number = "123456789012";
        String expiry = "12/29";
        String cvv = "123";

        assertFalse(handler.payWithCard(name, number, expiry, cvv, false, student));
    }

    @Test
    public void testInvalidCardNumber_LengthTooLong() {
        String name = "John Doe";
        String number = "12345678901234567890";
        String expiry = "12/29";
        String cvv = "123";

        assertFalse(handler.payWithCard(name, number, expiry, cvv, false, student));
    }

    @Test
    public void testInvalidCardNumber_NoNumbers() {
        String name = "John Doe";
        String number = "abcdefghijklmnop";
        String expiry = "12/29";
        String cvv = "123";

        assertFalse(handler.payWithCard(name, number, expiry, cvv, false, student));
    }

    @Test
    public void testInvalidExpiry_PastDate() {
        String name = "John Doe";
        String number = "4111 1111 1111 1111";
        String expiry = "01/20"; // Past date
        String cvv = "123";

        assertFalse(handler.payWithCard(name, number, expiry, cvv, false, student));
    }

    @Test
    public void testInvalidExpiry_Format() {
        String name = "John Doe";
        String number = "4111 1111 1111 1111";
        String expiry = "2025-12"; // Invalid format
        String cvv = "123";

        assertFalse(handler.payWithCard(name, number, expiry, cvv, false, student));
    }

    @Test
    public void testInvalidCVV_TooShort() {
        String name = "John Doe";
        String number = "4111 1111 1111 1111";
        String expiry = "12/29";
        String cvv = "12";

        assertFalse(handler.payWithCard(name, number, expiry, cvv, false, student));
    }

    @Test
    public void testInvalidCVV_TooLong() {
        String name = "John Doe";
        String number = "4111 1111 1111 1111";
        String expiry = "12/29";
        String cvv = "12345";

        assertFalse(handler.payWithCard(name, number, expiry, cvv, false, student));
    }

    @Test
    public void testInvalidCVV_NonNumeric() {
        String name = "John Doe";
        String number = "4111 1111 1111 1111";
        String expiry = "12/29";
        String cvv = "12a"; // Invalid characters

        assertFalse(handler.payWithCard(name, number, expiry, cvv, false, student));
    }

    @Test
    public void testEmptyFields() {
        assertFalse(handler.payWithCard("", "", "", "", false, student));
    }

    @Test
    public void testNullSafeCheck() {
        assertFalse(handler.payWithCard(null, null, null,null, false, student));
    }

    @Test
    public void testSaveCard(){
        String name = "John Doe";
        String number = "4111 1111 1111 1111"; // Valid Visa Luhn number
        String expiry = "12/29"; // Future expiry
        String cvv = "123";      // Valid CVV
        student = new Student("John Doe", "johndoe@example.com");

        assertTrue(handler.payWithCard(name, number, expiry, cvv, true, student));
    }

    @Test
    public void testSaveCard_Exception(){
        CardPersistence cardPersistence = null; //Null database
        PaymentHandler newHandler = new PaymentHandler(cardPersistence);
        String name = "John Doe";
        String number = "4111 1111 1111 1111"; // Valid Visa Luhn number
        String expiry = "12/29"; // Future expiry
        String cvv = "123";      // Valid CVV
        student = new Student("John Doe", "johndoe@example.com");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            newHandler.payWithCard(name, number, expiry, cvv, true, student);
        });
        assertEquals("Card information could not be encrypted", ex.getMessage());
    }

    @Test
    public void testRetrieveRecordedCards_Successfull(){
        String name = "John Doe";
        String number = "4111 1111 1111 1111"; // Valid Visa Luhn number
        String expiry = "12/29"; // Future expiry
        student = new Student(name, "johndoe@example.com");
        handler.saveCard(name, number, expiry, student);
        List<Card> cards = handler.retrieveRecordedCards(student);

        assertEquals(1, cards.size());

    }

    @Test
    public void testRetrieveRecordedCards_ThrowException(){
        PaymentHandler newHandler = new PaymentHandler(null);
        String name = "John Doe";
        student = new Student(name, "johndoe@example.com");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> newHandler.retrieveRecordedCards(student)
        );

        assertEquals("Card information could not be decrypted", exception.getMessage());

    }

    @Test
    public void testDeleteRecordedCard(){
        String name = "John Doe";
        String number = "4111 1111 1111 1111"; // Valid Visa Luhn number
        String expiry = "12/29"; // Future expiry
        String cvv = "123";      // Valid CVV
        student = new Student("John Doe", "johndoe@example.com");
        handler.saveCard(name, number, expiry, student);
        List<Card> cardsOne = handler.retrieveRecordedCards(student);
        handler.deleteRecordedCard(student, cardsOne.get(0));
        List<Card> cardsTwo = handler.retrieveRecordedCards(student);
        assertEquals(0, cardsTwo.size());
    }
}
