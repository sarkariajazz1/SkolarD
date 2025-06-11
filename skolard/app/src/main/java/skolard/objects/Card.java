package skolard.objects;

public class Card {
    private final String name;
    private final String cardNumber;
    private final String expiry;

    public Card(String cardNumber, String expiry, String name){
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.name = name;
    }

    public String getCardNumber(){
        return cardNumber;
    }

    public String getExpiry(){
        return expiry;
    }

    public String getName(){
        return name;
    }

}
