package skolard.persistence;

import java.util.List;

public interface CardPersistence {
    String addAccountCard(String accountEmail, String cardInfo);
    List<String> getCardsByAccount(String accountEmail);
    void deleteCard(String accountEmail, String cardInfo);
}
