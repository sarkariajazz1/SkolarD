package skolard.logic;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PaymentHandler {
    //Insert private Database variable

    //Default Constructor
    public PaymentHandler(){
        //Insert Database initialization
    }

    public void payWithCard(String number, String expiry, String cvv){
        //TO DO
    }

    public void saveCard(String number, String expiry, String cvv){
        //TO DO with Database and encryption
    }

    public boolean validateCard(String number, String expiry, String cvv){
        boolean validNumber = false;
        boolean validExpiry = false;
        boolean validCVV = false;
        
        if(!number.isEmpty() && !expiry.isEmpty() && !cvv.isEmpty() && !number.equals(null)
                && !expiry.equals(null) && !cvv.equals(null)){

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

    private boolean validateNumber(String number){
        boolean validNumber = true;
        long sameDigits = number.chars().distinct().count();

        //Check if number is between lengths of 13 and 19
        if(number.length() < 13 || number.length() > 19){
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

        if(!number.matches("\\d+")){
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
            if (!Character.isDigit(c)) return false; // Invalid character

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
