package banking;

import java.util.Scanner;

public class Main {
    static CardManager cardManager;
    static Scanner sc = new Scanner(System.in);

    static void printMenu(){
        if(cardManager.selectedCard==-1) printMainMenu();
        else printLoggedMenu();
    }

    static void printMainMenu(){
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    static void printLoggedMenu(){
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    static void createAccount(){
        Card card = cardManager.generateCard();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(card.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(card.getPin());
        System.out.println();
    }


    static void login(){
        System.out.println("Enter your card number:");
        String cardNumber = sc.nextLine();
        System.out.println("Enter your pin:");
        String pin = sc.nextLine();
        System.out.println();
        cardManager.validLogin(cardNumber, pin);
        if(cardManager.selectedCard==-1){
            System.out.println("Wrong card number or PIN!");
        }
        else{
            System.out.println("You have successfully logged in!");
        }
        System.out.println();

    }

    static void printBalance(){
        System.out.println("Balance: " + cardManager.getActualCard().getBalance());
        System.out.println();
    }

    static void addIncome(){
        System.out.println("Enter income:");
        int income=Integer.parseInt(sc.nextLine());
        cardManager.addIncome(income);
        System.out.println("Income was added!");
        System.out.println();
    }

    static void transfer(){
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String cardNumber=sc.nextLine();
        if(cardNumber.equals(cardManager.getActualCard().getCardNumber())){
            System.out.println("You can't transfer money to the same account!");
            System.out.println();
        }
        else if(cardManager.isValidCard(cardNumber)){
            if(cardManager.existsCard(cardNumber)){
                System.out.println("Enter how much money you want to transfer:");
                int toTransfer=Integer.parseInt(sc.nextLine());
                if(cardManager.getActualCard().getBalance()>=toTransfer){
                    cardManager.transferTo(cardNumber, toTransfer);
                    System.out.println("Success!");
                    System.out.println();
                }
                else{
                    System.out.println("Not enough money!");
                    System.out.println();
                }
            }
            else{
                System.out.println("Such a card does not exist.");
                System.out.println();
            }
        }
        else{
            System.out.println("Probably you made mistake in the card number. Please try again!");
            System.out.println();
        }
    }

    static void closeAccount(){
        cardManager.closeAccount();
        System.out.println("The account has been closed!");
        System.out.println();
    }

    static void logOut(){
        cardManager.logOut();
        System.out.println("You have successfully logged out!");
        System.out.println();
    }

    static void handleArguments(String[] args){
        for(int i=0; i< args.length; i++){
            if(args[i].equals("-fileName")){
                cardManager=new CardManager(args[i+1]);
            }
        }
    }

    public static void main(String[] args) {
        handleArguments(args);
        int option=9;
        while(option!=0) {
            printMenu();
            option= Integer.parseInt(sc.nextLine());
            System.out.println();
            if(option==0) System.out.println("Bye!");
            if(cardManager.selectedCard==-1) {
                if (option == 1) createAccount();
                else if (option == 2) login();
            }
            else{
                if(option==1) printBalance();
                else if(option==2) addIncome();
                else if(option==3) transfer();
                else if(option==4) closeAccount();
                else if(option==5) logOut();
            }
        }
    }

}