package skolard.logic;

import skolard.objects.Student;
import skolard.objects.Card;
import skolard.persistence.CardPersistence;
import skolard.utils.CardUtil;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.crypto.SecretKey;

public class PaymentHandler {
    private CardPersistence cardDB;

    //Default Constructor
    public PaymentHandler(CardPersistence cardPersistence){
        this.cardDB = cardPersistence;
    }

    public boolean payWithCard(String number, String expiry, String cvv, boolean saveInfo, Student student){
        boolean validCard = validateCard(number, expiry, cvv);

        if(saveInfo && validCard){
            saveCard(number, expiry, student);
        }

        return validCard;
    }

    public List<Card> payWithRecordedCard(Student student){
        List<Card> encryptedCards = cardDB.getCardsByAccount(student.getEmail());
        List<Card> decryptedCard = new ArrayList<>();

        try {
            SecretKey key = CardUtil.generateKey();
            for(int i = 0; i < encryptedCards.size(); i++){
                //TODO decrypt from database the string
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Card information could not be decrypted");
        }
        

        return decryptedCard;
    }

    public void deleteRecordedCard(Student student, Card card){
        cardDB.deleteCard(student.getEmail(), card);
    }

    private void saveCard(String number, String expiry, Student student){
        String combinedInfo = number + "|" + expiry;

        try {
            SecretKey key = CardUtil.generateKey();
            String encryptedData = CardUtil.encrypt(combinedInfo, key);
            cardDB.addAccountCard(student.getEmail(), encryptedData);

        } catch (Exception e) {
            throw new IllegalArgumentException("Card information could not be encrypted");
        }
        
    }

    private boolean validateCard(String number, String expiry, String cvv){
        boolean validNumber = false;
        boolean validExpiry = false;
        boolean validCVV = false;
        
        if(isNotNullOrEmpty(number) && isNotNullOrEmpty(expiry) && isNotNullOrEmpty(cvv)){
            validNumber = validateNumber(number.replaceAll("\\s+", ""));
            // Expiry is in the form MM/YY
            validExpiry = validateExpiry(expiry.replaceAll("\\s+", ""));
            validCVV = validateCVV(cvv.replaceAll("\\s+", ""));
        }

        if(validNumber && validExpiry && validCVV){
            return true;
        } else{
            return false;
        }
    }

    private boolean isNotNullOrEmpty(String s){
        return s != null && !s.isEmpty();
    }
    private boolean validateNumber(String number){
        boolean validNumber = true;
        long sameDigits = number.chars().distinct().count();

        //Check if number is between lengths of 13 and 19
        if(number.length() < 13 || number.length() > 19){
            validNumber = false;
        }

        //Checks if all characters are digits
        if(!number.matches("\\d+")){
            validNumber = false;
        }

        //Checks if number passes Luhn's algorithm 
        if(!isValidLuhn(number)){
            validNumber = false;
        }

        //Checks if all digits are the same.
        if(sameDigits == 1){
            validNumber = false;
        }

        

        return validNumber;
    }

    private boolean isValidLuhn(String number) {
        int sum = 0;
        boolean doubleDigit = false;

        // Process digits from right to left
        for (int i = number.length() - 1; i >= 0; i--) {
            char c = number.charAt(i);

            int digit = c - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return (sum % 10 == 0);
    }

    private boolean validateExpiry(String expiry){
        boolean validExpiry = true;
        try {
            // Parse expiry in MM/yy format to YearMonth
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiryYearMonth = YearMonth.parse(expiry, formatter);

            // Get current YearMonth
            YearMonth currentYearMonth = YearMonth.from(LocalDateTime.now());

            // Card is valid if expiry month/year is this month or in the future
            return !expiryYearMonth.isBefore(currentYearMonth);

        } catch (DateTimeParseException e) {
            validExpiry = false;
        }

        return validExpiry;
    }

    private boolean validateCVV(String cvv){
        boolean validCVV = true;

        if(cvv.length() < 3 || cvv.length() > 4){
            validCVV = false;
        }

        if(!cvv.matches("\\d+")){
            validCVV = false;
        }

        return validCVV;
    }

}
