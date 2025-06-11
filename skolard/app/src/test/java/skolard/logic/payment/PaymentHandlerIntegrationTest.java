package skolard.logic.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import skolard.objects.Student;
import skolard.objects.Card;
import skolard.persistence.CardPersistence;
import skolard.persistence.EnvironmentInitializer;
import skolard.persistence.PersistenceProvider;
import skolard.persistence.PersistenceRegistry;
import skolard.persistence.PersistenceType;

import java.sql.Connection;

public class PaymentHandlerIntegrationTest {
    private Connection conn;
    private PaymentHandler paymentHandler;
    private CardPersistence cardPersistence;
    private Student student;

    @BeforeEach
    void setUp() throws Exception {
        conn = EnvironmentInitializer.setupEnvironment(PersistenceType.TEST, true);
        PersistenceProvider.initializeSqlite(conn);

        cardPersistence = PersistenceRegistry.getCardPersistence();
        paymentHandler = new PaymentHandler(cardPersistence);
        student = new Student("Jane Doe", "jane@example.com", "pass123");
    }

    @Test
    void testValidPaymentAndCardStorage() {
        boolean result = paymentHandler.payWithCard("Jane Doe", "4111111111111111", "12/30", "123", true, student);
        assertTrue(result, "Payment should be successful");

        List<Card> cards = paymentHandler.retrieveRecordedCards(student);
        assertEquals(1, cards.size(), "One card should be saved");

        Card savedCard = cards.get(0);
        assertEquals("4111111111111111", savedCard.getCardNumber());
        assertEquals("12/30", savedCard.getExpiry());
        assertEquals("Jane Doe", savedCard.getName());
    }

    @Test
    void testDeleteSavedCard() {
        paymentHandler.payWithCard("Jane Doe", "4111111111111111", "12/30", "123", true, student);

        List<Card> cards = paymentHandler.retrieveRecordedCards(student);
        assertEquals(1, cards.size());

        paymentHandler.deleteRecordedCard(student, cards.get(0));
        List<Card> afterDelete = paymentHandler.retrieveRecordedCards(student);

        assertTrue(afterDelete.isEmpty(), "Card should be deleted");
    }

    @Test
    void testInvalidCardRejected() {
        // Invalid number and past expiry
        boolean result = paymentHandler.payWithCard("Fake Name", "1111222233334444", "01/20", "12", false, student);
        assertFalse(result, "Invalid card should be rejected");
    }
}
