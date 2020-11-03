package banking;

import java.util.ArrayList;

public class CardManager {
    int selectedCard=-1;
    private long actualCard;
    private ArrayList<Card> cards;
    private Database database;

    public CardManager(String dbPath){
        database=new Database(dbPath);

        cards=database.getAllCards();
        if(cards.size()==0) actualCard=400000111111111L;
        else actualCard=(Long.parseLong(cards.get(cards.size()-1).getCardNumber())/10)+1;
    }

    public Card generateCard(){
        String cardNumber=getNewCardNumber();
        String pin=getNewPin();
        Card card = new Card(cardNumber, pin);
        cards.add(card);
        database.insertCard(card);
        return card;
    }

    private void reloadCards(){
        cards=database.getAllCards();
    }

    public boolean existsCard(String cardNumber){
        for(Card card : cards){
            if(card.getCardNumber().equals(cardNumber)) return true;
        }
        return false;
    }

    public void validLogin(String cardNumber, String pin){
        selectedCard=-1;
        for(int i=0; i<cards.size(); i++) {
            if(cards.get(i).getCardNumber().equals(cardNumber) &&
                    cards.get(i).getPin().equals(pin)) selectedCard=i;
        }
    }

    public boolean isValidCard(String card){
        if(card.length()<16) return false;
        char checksum=getChecksumFor(card);
        if(card.charAt(15)!=checksum) return false;
        return true;
    }

    private String getNewCardNumber(){
        String cardNumber;
        do {
            cardNumber=Long.toString(actualCard);
            actualCard++;
            char checksum=getChecksumFor(cardNumber);
            cardNumber+=checksum;
        }while(!isValidCard(cardNumber));
        return cardNumber;
    }

    private char getChecksumFor(String cardNumber){
        int checksum=0;
        for(int i=0; i<15; i++){
            int num=cardNumber.charAt(i)-'0';
            if(i%2==0) num*=2;
            if(num>9) num-=9;
            checksum+=num;
        }
        int charNum=10-(checksum%10);
        return (char)(charNum+'0');
    }
    private String getNewPin(){
        StringBuilder pin= new StringBuilder();
        for(int i=0; i<4; i++){
            pin.append((char) ((int) ((Math.random() * 10)) + '0'));
        }
        return pin.toString();
    }

    public Card getActualCard(){
        return cards.get(selectedCard);
    }

    public void addIncome(int income){
        Card actualCard=cards.get(selectedCard);
        database.addIncome(actualCard.getCardNumber(), income);
        reloadCards();
    }

    public void transferTo(String receiverCard, int amount){
        String actualCard=getActualCard().getCardNumber();
        database.transferMoney(actualCard, receiverCard, amount);
        reloadCards();
    }

    public void closeAccount(){
        database.closeAccount(getActualCard().getCardNumber());
        logOut();
        reloadCards();
    }

    public void logOut(){
        selectedCard=-1;
    }
}