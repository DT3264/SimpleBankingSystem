package banking;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    String url;
    public Database(String path){
        url = "jdbc:sqlite:"+path;
        initTable();
    }
    private void initTable(){
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	number text,\n"
                + "	pin text,\n"
                + "	balance INTEGER DEFAULT 0\n"
                + ");";
        executeSQL(sql);
    }
    private Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    private void executeSQL(String sql){
        try (Connection connection = getConnection()) {
            connection.createStatement().execute(sql);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Card> getAllCards(){
        String sql = "SELECT * FROM card";
        ArrayList<Card> cards = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                cards.add(new Card(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cards;
    }

    public void insertCard(Card card){
        String sql = String.format(
                "insert into card (number, pin) values ('%s', '%s')",
                card.getCardNumber(),
                card.getPin()
        );
        executeSQL(sql);
    }

    public void addIncome(String cardNumber, int income){
        String sql = String.format(
                "update card set balance=balance+%d where number='%s'",
                income,
                cardNumber
        );
        executeSQL(sql);
    }

    public void transferMoney(String actualCard, String receiverCard, int amount){
        String sql=String.format(
                "update card set balance=balance-%d where number='%s'",
                amount,
                actualCard
        );
        executeSQL(sql);
        sql=String.format(
                "update card set balance=balance+%d where number='%s'",
                amount,
                receiverCard
        );
        executeSQL(sql);
    }

    public void closeAccount(String cardNumber){
        String sql=String.format("delete from card where number='%s'", cardNumber);
        executeSQL(sql);
    }
}
