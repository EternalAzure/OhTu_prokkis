package fi.orderly.logic.dbinterfaces;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseAccess {

    final public RoomsInterface rooms;
    final public ProductsInterface products;
    final public BalanceInterface balance;
    final public ShipmentsInterface shipments;
    final public DeliveriesInterface deliveries;

    public DatabaseAccess(Connection connection) {
        rooms = new RoomsInterface(connection);
        products = new ProductsInterface(connection);
        balance = new BalanceInterface(connection);
        shipments = new ShipmentsInterface(connection);
        deliveries = new DeliveriesInterface(connection);
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
