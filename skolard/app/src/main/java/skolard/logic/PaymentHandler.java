package skolard.logic;

public class PaymentHandler {
    //Insert private Database variable

    //Default Constructor
    public PaymentHandler(){
        //Insert Database initialization
    }

    public boolean validateCard(String number, String expiry, String cvv){
        boolean validNumber = false;
        boolean validExpiry = false;
        boolean validCVV = false;

        if(validNumber && validExpiry && validCVV){
            return true;
        } else{
            return false;
        }
    }

    public boolean validateNumber(){

    }

    public boolean validateExpiry(){

    }

    public boolean validateCVV(){

    }

}
