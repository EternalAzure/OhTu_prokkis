package fi.orderly.logic;

import fi.orderly.logic.dbinterfaces.BalanceInterface;
import fi.orderly.logic.dbinterfaces.ProductsInterface;
import fi.orderly.logic.dbinterfaces.RoomsInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SaldoOperations {

    Statement statement;
    BalanceInterface balanceInterface;
    RoomsInterface roomsInterface;
    ProductsInterface productsInterface;

    public SaldoOperations(Connection connection) {
        this.balanceInterface = new BalanceInterface(connection);
        this.roomsInterface = new RoomsInterface(connection);
        this.productsInterface = new ProductsInterface(connection);
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
