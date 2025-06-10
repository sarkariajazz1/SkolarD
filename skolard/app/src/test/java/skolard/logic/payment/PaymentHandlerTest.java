package skolard.logic.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import skolard.objects.Card;
import skolard.objects.Student;
import skolard.persistence.CardPersistence;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentHandlerTest {

    private CardPersistence mockCardDB;
    private PaymentHandler handler;
    private Student dummyStudent;

    @BeforeEach
    void setup() {
        mockCardDB = mock(CardPersistence.class);
        handler = new PaymentHandler(mockCardDB);
        dummyStudent = new Student("John", "john@skolard.ca", "pass");
    }

    @Test
    void testPayWithCard_validAndSaved() {
        boolean result = handler.payWithCard("John", "4111111111111111", "12/30", "123", true, dummyStudent);
        assertTrue(result);
        verify(mockCardDB, times(1)).addAccountCard(eq(dummyStudent.getEmail()), any(Card.class));
    }

    @Test
    void testPayWithCard_validNotSaved() {
        boolean result = handler.payWithCard("John", "4111111111111111", "12/30", "123", false, dummyStudent);
        assertTrue(result);
        verify(mockCardDB, never()).addAccountCard(anyString(), any());
    }

    @Test
    void testPayWithCard_invalid() {
        boolean result = handler.payWithCard("", "123", "00/00", "abc", true, dummyStudent);
        assertFalse(result);
        verify(mockCardDB, never()).addAccountCard(anyString(), any());
    }

    @Test
    void testRetrieveRecordedCards_valid() {
        List<Card> fake = new ArrayList<>();
        fake.add(new Card("4111111111111111", "12/30", "John"));
        when(mockCardDB.getCardsByAccount(dummyStudent.getEmail())).thenReturn(fake);

        List<Card> result = handler.retrieveRecordedCards(dummyStudent);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    void testRetrieveRecordedCards_invalid() {
        when(mockCardDB.getCardsByAccount(dummyStudent.getEmail())).thenThrow(new RuntimeException("DB error"));
        Exception ex = assertThrows(IllegalArgumentException.class, () -> handler.retrieveRecordedCards(dummyStudent));
        assertTrue(ex.getMessage().contains("could not be found"));
    }

    @Test
    void testDeleteRecordedCard() {
        Card c = new Card("4111111111111111", "12/30", "John");
        handler.deleteRecordedCard(dummyStudent, c);
        verify(mockCardDB).deleteCard(dummyStudent.getEmail(), c);
    }

    @Test
    void testSaveCard_success() {
        handler.saveCard("John", "4111111111111111", "12/30", dummyStudent);
        verify(mockCardDB).addAccountCard(eq(dummyStudent.getEmail()), any(Card.class));
    }

    @Test
    void testSaveCard_failure() {
        doThrow(new RuntimeException()).when(mockCardDB).addAccountCard(anyString(), any());
        assertThrows(IllegalArgumentException.class, () -> handler.saveCard("John", "4111111111111111", "12/30", dummyStudent));
    }

    @Test
    void testValidateCard_allInvalid() {
        boolean result = handler.payWithCard("", "", "", "", false, dummyStudent);
        assertFalse(result);
    }

    @Test
    void testValidateCard_expiredCard() {
        boolean result = handler.payWithCard("John", "4111111111111111", "01/20", "123", false, dummyStudent);
        assertFalse(result);
    }

    @Test
    void testValidateCard_invalidCVV() {
        boolean result = handler.payWithCard("John", "4111111111111111", "12/30", "12a", false, dummyStudent);
        assertFalse(result);
    }

    @Test
    void testValidateCard_invalidNumber() {
        boolean result = handler.payWithCard("John", "1111111111111111", "12/30", "123", false, dummyStudent);
        assertFalse(result); // invalid Luhn
    }
}
