package skolard.objects;

public class Card {
    private String cardNumber;
    private String expiry;

    public Card(String cardNumber, String expiry, String cvv){
        this.cardNumber = cardNumber;
        this.expiry = expiry;
    }

}
