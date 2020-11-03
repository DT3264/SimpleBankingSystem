package banking;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Card{
    private final String cardNumber;
    private final String pin;
    private int balance;

    public Card(ResultSet resultSet) throws SQLException {
        cardNumber=resultSet.getString("number");
        pin=resultSet.getString("pin");
        balance=resultSet.getInt("balance");
    }

    public Card(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        balance=0;
    }

    public void addBalance(int balance){
        this.balance+=balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance(){
        return balance;
    }
}