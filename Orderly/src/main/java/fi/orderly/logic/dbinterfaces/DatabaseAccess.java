package fi.orderly.logic.dbinterfaces;

import java.sql.*;

public class DatabaseAccess {

    final public RoomsInterface rooms;
    final public ProductsInterface products;
    final public BalanceInterface balance;
    final public ShipmentsInterface shipments;
    final public DeliveriesInterface deliveries;
    Connection connection;

    public DatabaseAccess(Connection connection) {
        this.connection = connection;
        rooms = new RoomsInterface(connection);
        products = new ProductsInterface(connection);
        balance = new BalanceInterface(connection);
        shipments = new ShipmentsInterface(connection);
        deliveries = new DeliveriesInterface(connection);
    }

    public double queryBalance(String room, int code, int batch) throws SQLException {
        int roomId = rooms.findIdByName(room);
        int productId = products.findIdByCode(code);
        return balance.queryBalance(roomId, productId, batch);
    }

    public PreparedStatement queryShipment(int shipmentNumber) throws SQLException {
        //Returning ResultSet is not a good idea
        String select = "SELECT products.product, products.code, shipments.batch, shipments.amount, products.unit, rooms.room FROM shipments, products, rooms " +
                "WHERE number=? AND products.id=shipments.product_id AND rooms.id=products.room_id";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, shipmentNumber);
        return sql;
    }

    public PreparedStatement tableShipments(int id) throws SQLException {
        String select = "SELECT number, product, batch, amount FROM products, shipments " +
                "WHERE shipments.id=? AND products.id=product_id";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        return sql;
    }
    public PreparedStatement tableProducts(int id) throws SQLException {
        String select = "SELECT product, code, unit, temperature, room FROM products, rooms " +
                "WHERE room.id=products.room_id AND products.id=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        return sql;
    }

    public PreparedStatement tableDeliveries(int id) throws SQLException {
        String select = "SELECT number, product, amount " +
                "FROM products, deliveries WHERE deliveries.id=? AND products.id=product_id";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        return sql;
    }

    public PreparedStatement tableBalance(int id) throws SQLException {
        String select = "SELECT product, code, batch, amount, room " +
                "FROM products, rooms, balance WHERE balance.id=? AND products.id=product_id AND rooms.id=balance.room_id";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        return sql;
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

    public int customQuery(String query, String column) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt(column);
    }
}
