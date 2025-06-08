package skolard.persistence.stub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.objects.Card;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardStubTest {

    private CardStub cardStub;

    @BeforeEach
    void setUp() {
        cardStub = new CardStub();
    }

    @Test
    void testAddAccountCard_Success() {
        Card card = new Card("1234567890123456", "12/25", "Alice Smith");
        Card added = cardStub.addAccountCard("alice@example.com", card);
        assertNotNull(added);
        assertEquals("1234567890123456", added.getCardNumber());
    }

    @Test
    void testAddAccountCard_Duplicate() {
        Card card = new Card("1234567890123456", "12/25", "Alice Smith");
        cardStub.addAccountCard("alice@example.com", card);
        Card duplicate = cardStub.addAccountCard("alice@example.com", card);
        assertNull(duplicate);
    }

    @Test
    void testGetCardsByAccount_OnlyReturnsMatching() {
        Card card1 = new Card("1111222233334444", "01/26", "John Doe");
        Card card2 = new Card("5555666677778888", "02/27", "Jane Doe");

        cardStub.addAccountCard("john@example.com", card1);
        cardStub.addAccountCard("jane@example.com", card2);

        List<Card> johnsCards = cardStub.getCardsByAccount("john@example.com");

        // Due to the bug in getEmailFromKey, this test may fail unless fixed
        assertTrue(johnsCards.stream().noneMatch(c -> c.getCardNumber().equals("5555666677778888")));
    }

    @Test
    void testDeleteCard_RemovesCard() {
        Card card = new Card("9999888877776666", "03/28", "Bob Smith");
        cardStub.addAccountCard("bob@example.com", card);

        cardStub.deleteCard("bob@example.com", card);
        List<Card> cards = cardStub.getCardsByAccount("bob@example.com");

        assertTrue(cards.isEmpty());
    }

    @Test
    void testDeleteCard_NonexistentCard() {
        Card card = new Card("0000111122223333", "04/29", "Ghost User");
        // Not added, so deletion should be safe
        assertDoesNotThrow(() -> cardStub.deleteCard("ghost@example.com", card));
    }
}

