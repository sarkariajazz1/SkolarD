package skolard.persistence.sqlite;

import org.junit.jupiter.api.*;
import skolard.objects.Card;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardDBTest {

    private static Connection connection;
    private CardDB cardDB;

    @BeforeAll
    static void init() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SchemaInitializer.initializeSchema(connection);
    }

    @BeforeEach
    void setUp() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM card");
        }
        cardDB = new CardDB(connection);
    }

    @Test
    void testAddAndRetrieveCard() {
        Card card = new Card("1234567890123456", "12/25", "John Doe");
        cardDB.addAccountCard("john@example.com", card);

        List<Card> cards = cardDB.getCardsByAccount("john@example.com");
        assertEquals(1, cards.size());
        assertEquals("John Doe", cards.get(0).getName());
    }

    @Test
    void testDeleteCard() {
        Card card = new Card("1111222233334444", "11/24", "Jane Doe");
        cardDB.addAccountCard("jane@example.com", card);
        cardDB.deleteCard("jane@example.com", card);

        List<Card> cards = cardDB.getCardsByAccount("jane@example.com");
        assertTrue(cards.isEmpty());
    }
}
