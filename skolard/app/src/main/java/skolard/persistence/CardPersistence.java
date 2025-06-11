package skolard.persistence;

import java.util.List;

import skolard.objects.Card;

/**
 * Interface defining persistence operations for Card objects.
 * Includes methods to add, retrieve, and delete cards linked to an account.
 */
public interface CardPersistence {
    
    /**
     * Adds a new card to the specified account.
     * @param accountEmail the email of the account to add the card to
     * @param card the Card object to add
     * @return the added Card
     */
    Card addAccountCard(String accountEmail, Card card);
    
    /**
     * Retrieves all cards associated with the specified account email.
     * @param accountEmail the email of the account to fetch cards for
     * @return a list of Card objects linked to the account
     */
    List<Card> getCardsByAccount(String accountEmail);
    
    /**
     * Deletes the specified card from the account.
     * @param accountEmail the email of the account
     * @param card the Card to be deleted
     */
    void deleteCard(String accountEmail, Card card);
}
