package skolard.objects;

public class Card {
    private String cardNumber;
    private String expiry;
    private String cvv;

    public Card(String cardNumber, String expiry, String cvv){
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
    }
    
}
