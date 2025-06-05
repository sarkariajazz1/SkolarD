package skolard.logic;

import skolard.logic.payment.PaymentHandler;
import skolard.objects.Student;
import skolard.objects.Card;
import skolard.persistence.CardPersistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before; 
import org.junit.Test;


import java.util.List;

public class PaymentHandlerTest {
    private CardPersistence cp;
    private PaymentHandler handler;
    private Student student;

    @Before
    public void setup(){
        cp = mock(CardPersistence.class);
        handler = new PaymentHandler(cp);
        student = new Student("John Doe", "johndoe@example.com");

    }

    @Test 
    public void testPayWithCard(){
        //Valid number, expiry and cvv
        assertTrue(handler.payWithCard("John Doe", "4111 1111 1111 1111", "12/29", "123", false, student));
        //Invalid luhn number
        assertFalse(handler.payWithCard("John Doe", "4111 1111 1111 1112", "12/29", "123", false, student));
        //Invalid same digit number
        assertFalse(handler.payWithCard("John Doe", "1111 1111 1111 1111", "12/29", "123", false, student));
        //Invalid number, too short
        assertFalse(handler.payWithCard("John Doe", "1234 5678 9012", "12/29", "123", false, student));
        //Invalid number, too long
        assertFalse(handler.payWithCard("John Doe", "1234 5678 9012 3456 7890", "12/29", "123", false, student));
        //Invalid number, has letters
        assertFalse(handler.payWithCard("John Doe", "abcd efgh ijkl mnop", "12/29", "123", false, student));
        //Invalid expiry date, past date already passed
        assertFalse(handler.payWithCard("John Doe", "4111 1111 1111 1111", "01/20", "123", false, student));
        //Invalid expiry, wrong format
        assertFalse(handler.payWithCard("John Doe", "4111 1111 1111 1111", "2025-12", "123", false, student));
        //Invalid cvv, too short
        assertFalse(handler.payWithCard("John Doe", "4111 1111 1111 1111", "12/29", "12", false, student));
        //Invalid cvv, too long
        assertFalse(handler.payWithCard("John Doe", "4111 1111 1111 1111", "12/29", "12345", false, student));
        //Invalid cvv, has letters
        assertFalse(handler.payWithCard("John Doe", "4111 1111 1111 1111", "12/29", "12a", false, student));
        //Testing empty fields
        assertFalse(handler.payWithCard("", "", "", "", false, student));
        //Testing null fields
        assertFalse(handler.payWithCard(null, null, null,null, false, student));
    }

    @Test
    public void testSaveCard(){
        student = new Student("John Doe", "johndoe@example.com");
        
        assertTrue(handler.payWithCard("John Doe", "4111 1111 1111 1111", "12/29", "123", true, student));
        verify(cp, times(1)).addAccountCard(eq(student.getEmail()), any(Card.class));
        //Invalid same digit number
        assertFalse(handler.payWithCard("John Doe", "1111 1111 1111 1111", "12/29", "123", true, student));

        // Should only save once (for the valid card)
        verify(cp, times(1)).addAccountCard(anyString(), any(Card.class));
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
        assertEquals("Card information could not be encrypted or database is null.", ex.getMessage());
    }

    @Test
    public void testRetrieveRecordedCards_Successfull(){
        handler.saveCard("John Doe", "4111 1111 1111 1111", "12/29", student);
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

        assertEquals("Card information could not be decrypted or database is null.", exception.getMessage());

    }

    @Test
    public void testDeleteRecordedCard(){
        String name = "John Doe";
        String number = "4111 1111 1111 1111"; // Valid Visa Luhn number
        String expiry = "12/29"; // Future expiry
        student = new Student("John Doe", "johndoe@example.com");
        handler.saveCard(name, number, expiry, student);
        List<Card> cardsOne = handler.retrieveRecordedCards(student);
        handler.deleteRecordedCard(student, cardsOne.get(0));
        List<Card> cardsTwo = handler.retrieveRecordedCards(student);
        assertEquals(0, cardsTwo.size());
    }
}
