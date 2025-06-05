package skolard.logic.payment;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import javax.crypto.SecretKey;

import skolard.objects.Card;
import skolard.objects.Student;
import skolard.persistence.CardPersistence;
import skolard.persistence.PersistenceRegistry;
import skolard.utils.CardUtil;

public class PaymentHandler {
    private final CardPersistence cardDB;
    private final SecretKey key;

    /**
     * Default constructor using real persistence layer.
     */
    public PaymentHandler() {
        this(PersistenceRegistry.getCardPersistence());
    }

    /**
     * Constructor for injecting a custom CardPersistence (mockable).
     */
    public PaymentHandler(CardPersistence cardPersistence) {
        this.cardDB = cardPersistence;
        // No need for try/catch since generateKey() generates a constant key
        this.key = CardUtil.generateKey();

    }

    public boolean payWithCard(String name, String number, String expiry, String cvv, boolean saveInfo, Student student) {
        boolean validCard = validateCard(name, number, expiry, cvv);

        if (saveInfo && validCard) {
            saveCard(name, number, expiry, student);
        }

        return validCard;
    }

    public List<Card> retrieveRecordedCards(Student student) {
        try {
            List<Card> encryptedCards = cardDB.getCardsByAccount(student.getEmail());
            List<Card> decryptedCards = new ArrayList<>();

            for (Card currentCard : encryptedCards) {
                String decryptedData = CardUtil.decrypt(currentCard.getCardNumber(), key);
                decryptedCards.add(new Card(decryptedData, currentCard.getExpiry(), currentCard.getName()));
            }

            return decryptedCards;
        } catch ( NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException( "Card information could not be decrypted.", e);
        } catch (NullPointerException npEx){
            throw new IllegalArgumentException("Card information could not be retrieved since database is null", npEx);
        }
    }

    public void deleteRecordedCard(Student student, Card card) {
        cardDB.deleteCard(student.getEmail(), card);
    }

    public void saveCard(String name, String number, String expiry, Student student) {
        try {
            String encryptedData = CardUtil.encrypt(number, key);
            Card savedCard = new Card(encryptedData, expiry, name);
            cardDB.addAccountCard(student.getEmail(), savedCard);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException e) {
            throw new IllegalArgumentException("Card information could not be encrypted", e);
        } catch (NullPointerException npEx){
            throw new IllegalArgumentException("Card information could not be saved since database is null", npEx);
        }
    }

    // --- Validation Logic ---

    private boolean validateCard(String name, String number, String expiry, String cvv) {
        if (!isNotNullOrEmpty(name) || !isNotNullOrEmpty(number) ||
            !isNotNullOrEmpty(expiry) || !isNotNullOrEmpty(cvv)) {
            return false;
        }

        number = number.replaceAll("\\s+", "");
        expiry = expiry.replaceAll("\\s+", "");
        cvv = cvv.replaceAll("\\s+", "");

        return validateNumber(number) && validateExpiry(expiry) && validateCVV(cvv);
    }

    private boolean isNotNullOrEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    private boolean validateNumber(String number) {
        long uniqueDigits = number.chars().distinct().count();
        return number.matches("\\d{13,19}") &&
               isValidLuhn(number) &&
               uniqueDigits > 1;
    }

    private boolean isValidLuhn(String number) {
        int sum = 0;
        boolean doubleDigit = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = number.charAt(i) - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return (sum % 10 == 0);
    }

    private boolean validateExpiry(String expiry) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiryYearMonth = YearMonth.parse(expiry, formatter);
            YearMonth currentYearMonth = YearMonth.from(LocalDateTime.now());
            return !expiryYearMonth.isBefore(currentYearMonth);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean validateCVV(String cvv) {
        return cvv.matches("\\d{3,4}");
    }
}
