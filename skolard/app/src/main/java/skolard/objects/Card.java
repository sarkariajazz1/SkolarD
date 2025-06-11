package skolard.objects;

/**
 * Represents a credit/debit card used for payments.
 * Stores the cardholder's name, card number, and expiry date.
 */
public class Card {
    // Full name of the cardholder
    private final String name;

    // The card number (should be stored securely or encrypted in real applications)
    private final String cardNumber;

    // Expiry date of the card (e.g., "MM/YY")
    private final String expiry;

    /**
     * Constructs a Card with the given card number, expiry date, and cardholder name.
     *
     * @param cardNumber the card number
     * @param expiry     the expiry date
     * @param name       the name of the cardholder
     */
    public Card(String cardNumber, String expiry, String name){
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.name = name;
    }

    /**
     * Returns the card number.
     *
     * @return the card number
     */
    public String getCardNumber(){
        return cardNumber;
    }

    /**
     * Returns the expiry date.
     *
     * @return the expiry date
     */
    public String getExpiry(){
        return expiry;
    }

    /**
     * Returns the name of the cardholder.
     *
     * @return the cardholder's name
     */
    public String getName(){
        return name;
    }
}
