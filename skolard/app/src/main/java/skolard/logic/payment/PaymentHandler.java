package skolard.logic.payment;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import skolard.objects.Card;
import skolard.objects.Student;
import skolard.persistence.CardPersistence;

/**
 * Handles payment processing logic for students,
 * including card validation, storage, retrieval, and deletion.
 */
public class PaymentHandler {

    // Reference to the persistence layer responsible for storing and retrieving cards
    private final CardPersistence cardDB;

    /**
     * Constructor for injecting a custom CardPersistence (e.g., for testing).
     *
     * @param cardPersistence the card persistence implementation to use
     */
    public PaymentHandler(CardPersistence cardPersistence) {
        this.cardDB = cardPersistence;
    }

    /**
     * Processes a payment using card details. Validates the card and optionally saves it.
     *
     * @param name      the cardholder's name
     * @param number    the card number
     * @param expiry    the expiry date in MM/yy format
     * @param cvv       the card's CVV
     * @param saveInfo  flag indicating if the card should be saved
     * @param student   the student making the payment
     * @return true if the card is valid, false otherwise
     */
    public boolean payWithCard(String name, String number, String expiry, String cvv, boolean saveInfo, Student student) {
        boolean validCard = validateCard(name, number, expiry, cvv);

        // Save card only if valid and user wants to store it
        if (saveInfo && validCard) {
            saveCard(name, number, expiry, student);
        }

        return validCard;
    }

    /**
     * Retrieves a list of stored (decrypted) cards for the given student.
     *
     * @param student the student whose cards are being fetched
     * @return list of decrypted Card objects
     * @throws IllegalArgumentException if retrieval fails
     */
    public List<Card> retrieveRecordedCards(Student student) {
        try {
            List<Card> encryptedCards = cardDB.getCardsByAccount(student.getEmail());
            List<Card> decryptedCards = new ArrayList<>();

            // Simulate decryption by creating a new instance of each card
            for (Card currentCard : encryptedCards) {
                decryptedCards.add(new Card(currentCard.getCardNumber(), currentCard.getExpiry(), currentCard.getName()));
            }

            return decryptedCards;
        } catch (Exception e) {
            throw new IllegalArgumentException("Card information could not be found or was corrupted", e);
        }
    }

    /**
     * Deletes a specific card associated with a student.
     *
     * @param student the student who owns the card
     * @param card    the card to be deleted
     */
    public void deleteRecordedCard(Student student, Card card) {
        cardDB.deleteCard(student.getEmail(), card);
    }

    /**
     * Saves a new card for the specified student.
     *
     * @param name     cardholder's name
     * @param number   card number
     * @param expiry   expiry date
     * @param student  student associated with the card
     * @throws IllegalArgumentException if saving fails
     */
    public void saveCard(String name, String number, String expiry, Student student) {
        try {
            Card savedCard = new Card(number, expiry, name);
            cardDB.addAccountCard(student.getEmail(), savedCard);
        } catch (Exception e) {
            throw new IllegalArgumentException("Card information could not be saved into database", e);
        }
    }

    // --- Validation Logic ---

    /**
     * Validates all fields of a card (name, number, expiry, CVV).
     *
     * @param name    cardholder name
     * @param number  card number
     * @param expiry  expiry date
     * @param cvv     CVV code
     * @return true if all validations pass
     */
    private boolean validateCard(String name, String number, String expiry, String cvv) {
        if (!isNotNullOrEmpty(name) || !isNotNullOrEmpty(number) ||
            !isNotNullOrEmpty(expiry) || !isNotNullOrEmpty(cvv)) {
            return false;
        }

        // Remove whitespace from all fields before validation
        number = number.replaceAll("\\s+", "");
        expiry = expiry.replaceAll("\\s+", "");
        cvv = cvv.replaceAll("\\s+", "");

        return validateNumber(number) && validateExpiry(expiry) && validateCVV(cvv);
    }

    /**
     * Utility method to check if a string is not null and not empty.
     *
     * @param s the input string
     * @return true if not null or empty
     */
    private boolean isNotNullOrEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    /**
     * Validates the card number using format, uniqueness of digits, and Luhn algorithm.
     *
     * @param number the card number
     * @return true if the number is valid
     */
    private boolean validateNumber(String number) {
        long uniqueDigits = number.chars().distinct().count();
        return number.matches("\\d{13,19}") &&  // Must be 13 to 19 digits
               isValidLuhn(number) &&           // Must pass Luhn check
               uniqueDigits > 1;                // Prevent all-digits-same cards (e.g., "0000000000000")
    }

    /**
     * Performs Luhn algorithm check to validate card numbers.
     *
     * @param number card number string
     * @return true if number passes the checksum
     */
    private boolean isValidLuhn(String number) {
        int sum = 0;
        boolean doubleDigit = false;

        // Iterate from last digit to first
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

    /**
     * Validates the expiry date is in MM/yy format and not in the past.
     *
     * @param expiry the expiry date string
     * @return true if valid and not expired
     */
    private boolean validateExpiry(String expiry) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiryYearMonth = YearMonth.parse(expiry, formatter);
            YearMonth currentYearMonth = YearMonth.from(LocalDateTime.now());

            // Expiry must be this month or in the future
            return !expiryYearMonth.isBefore(currentYearMonth);
        } catch (DateTimeParseException e) {
            return false; // Invalid format
        }
    }

    /**
     * Validates the CVV is a 3 or 4 digit number.
     *
     * @param cvv the CVV code
     * @return true if valid
     */
    private boolean validateCVV(String cvv) {
        return cvv.matches("\\d{3,4}");
    }
}
