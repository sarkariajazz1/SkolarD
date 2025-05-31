package skolard.persistence;

import skolard.objects.Card;

public interface CardPersistence {
    Card addAccountCard(String accountEmail, String cardInfo);
    Card getCardByAccount(String accountEmail);
    void deleteCard(String accountEmail);
}
