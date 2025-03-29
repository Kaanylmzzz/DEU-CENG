package ticket;
public class Payment {
    private String cardholderName;
    private String cardNumber;
    private String expiryDate;
    private String cvv;

    public Payment(String cardholderName, String cardNumber, String expiryDate, String cvv) {
        this.cardholderName = cardholderName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public boolean validateCardholderName(String cardholderName)
    {
        String[] data = cardholderName.split(" ");
        if(data.length < 2)
        {
            return false;
        }
        return true;
    }

    public boolean validateCardNumber(String cardNumber)
    {

        if(cardNumber.length() != 16 || !cardNumber.matches("\\d+"))
        {
            return false;
        }
        return true;
    }

    public boolean validateCVV(String cvv)
    {
        if(cvv.length() !=3)
            return false;
        return true;
    }


    // Getter ve setter metotları
    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}