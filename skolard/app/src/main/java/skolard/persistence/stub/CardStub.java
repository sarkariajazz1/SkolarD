package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.objects.Card;
import skolard.persistence.CardPersistence;

public class CardStub implements CardPersistence{
    private Map<String, Card> cards;

    public CardStub() {
        cards = new HashMap<>();
    }

    private String generateKey(String email, Card card) {
        return email + "," + card.getCardNumber() + "," + card.getExpiry();
    }

    private String getEmailFromKey(String key) {
        String tok[];
        tok = key.split(",");

        //The account email should be at tok[0] after spliting the key
        return tok[0];
    }

    @Override
    public Card addAccountCard(String accountEmail, Card card) {
        Card newCard = null;
        String key = generateKey(accountEmail, card);

        if(!cards.containsKey(key)) {
            newCard = new Card(card.getCardNumber(), card.getExpiry(), card.getName());
            cards.put(key, newCard);
        }

        return newCard;
    }

    @Override
    public List<Card> getCardsByAccount(String accountEmail) {
        List<Card> cardList = new ArrayList<>();

        for (String key : cards.keySet()) {
            if(getEmailFromKey(key).equals(accountEmail)) {
                cardList.add(cards.get(key));
            }
        }
        return cardList;
    }

    @Override
    public void deleteCard(String accountEmail, Card card) {
        String key = generateKey(accountEmail, card);

        if(cards.containsKey(key)) {
            cards.remove(key);
        }
    }
}
