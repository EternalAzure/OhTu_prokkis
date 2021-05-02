package fi.orderly.logic;

import fi.orderly.logic.dbinterface.BalanceInterface;
import fi.orderly.logic.dbinterface.ProductsInterface;
import fi.orderly.logic.dbinterface.RoomsInterface;

import java.sql.SQLException;
import java.sql.Statement;

public class SaldoOperations {

    Statement statement;
    BalanceInterface balanceInterface;
    RoomsInterface roomsInterface;
    ProductsInterface productsInterface;

    public SaldoOperations(Statement statement) {
        this.balanceInterface = new BalanceInterface(statement);
        this.roomsInterface = new RoomsInterface(statement);
        this.productsInterface = new ProductsInterface(statement);
    }

    //Add and subtract
    public double subractBalance(String roomName, String productCode, int batch, double amount) throws SQLException {
        int roomId = roomsInterface.findIdByName(roomName);
        int productId = productsInterface.findIdByCode(productCode);
        double currentBalance = balanceInterface.queryBalance(roomId, productId, batch);
        double newBalance = currentBalance - amount;

        balanceInterface.updateBalance(roomId, productId, batch, newBalance);

        return newBalance;
    }
    public double subractBalance(int roomId, int productId, int batch, double amount) throws SQLException {
        double currentBalance = balanceInterface.queryBalance(roomId, productId, batch);
        double newBalance = currentBalance - amount;

        balanceInterface.updateBalance(roomId, productId, batch, newBalance);

        return newBalance;
    }
    public double addBalance(String roomName, String productCode, int batch, double amount) throws SQLException {
        int roomId = roomsInterface.findIdByName(roomName);
        int productId = productsInterface.findIdByCode(productCode);
        double currentBalance = balanceInterface.queryBalance(roomId, productId, batch);
        double newBalance = currentBalance + amount;

        balanceInterface.updateBalance(roomId, productId, batch, newBalance);

        return newBalance;
    }
    public double addBalance(int roomId, int productId, int batch, double amount) throws SQLException {
        double currentBalance = balanceInterface.queryBalance(roomId, productId, batch);
        double newBalance = currentBalance + amount;

        balanceInterface.updateBalance(roomId, productId, batch, newBalance);

        return newBalance;
    }

    //
}
