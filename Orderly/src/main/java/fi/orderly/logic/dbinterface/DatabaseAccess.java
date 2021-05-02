package fi.orderly.logic.dbinterface;

import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAccess {

    final public RoomsInterface rooms;
    final public ProductsInterface products;
    final public BalanceInterface balance;
    final public ShipmentsInterface shipments;
    final public DeliveriesInterface deliveries;

    public DatabaseAccess(Statement statement) {
        rooms = new RoomsInterface(statement);
        products = new ProductsInterface(statement);
        balance = new BalanceInterface(statement);
        shipments = new ShipmentsInterface(statement);
        deliveries = new DeliveriesInterface(statement);
    }

    public boolean foundRooms(String[] list) throws SQLException {
        for (String room: list) {
            if (rooms.countRoom(room) == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean foundBatch(int batchNumber) throws SQLException {
        return shipments.foundBatch(batchNumber) || balance.foundBatch(batchNumber);
    }
}
