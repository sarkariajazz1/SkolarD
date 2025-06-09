package skolard.persistence;

import java.util.List;

import skolard.objects.Card;

public interface CardPersistence {
    Card addAccountCard(String accountEmail, Card card);
    List<Card> getCardsByAccount(String accountEmail);
    void deleteCard(String accountEmail, Card card);
}
