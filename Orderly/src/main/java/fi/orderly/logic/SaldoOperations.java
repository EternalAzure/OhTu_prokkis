package fi.orderly.logic;

import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import java.sql.Connection;
import java.sql.SQLException;

public class SaldoOperations {

    DatabaseAccess db;

    public SaldoOperations(Connection connection) {
        db = new DatabaseAccess(connection);
    }

    public double subtractBalance(String roomName, int productCode, int batch, double amount) throws SQLException {
        int roomId = db.rooms.findIdByName(roomName);
        int productId = db.products.findIdByCode(productCode);
        double currentBalance = db.balance.queryBalance(roomId, productId, batch);
        double newBalance = currentBalance - amount;

        db.balance.updateBalance(roomId, productId, batch, newBalance);
        return newBalance;
    }
}
