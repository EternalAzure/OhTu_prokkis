package fi.orderly.logic.dbinterfaces;

import java.sql.*;

public class ShipmentsInterface {

    Connection connection;
    public ShipmentsInterface(Connection connection) {
        this.connection = connection;
    }

    public void insertShipment(int number, int productId, int batch, double amount) throws SQLException {
        String insert = "INSERT INTO shipments (number, product_id, batch, amount) VALUES (?, ?, ?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setInt(1, number);
        sql.setInt(2, productId);
        sql.setInt(3, batch);
        sql.setDouble(4, amount);
        sql.executeUpdate();
    }

    public void deleteShipment(int number) throws SQLException {
        String delete = "DELETE FROM shipments WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, number);
        sql.executeUpdate();
    }

    public boolean foundShipment(int number) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, number);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public int countBatch(int batch) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE batch=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, batch);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public boolean foundBatch(int batchNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE batch=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, batchNumber);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public int numberOfShipment(int number) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, number);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int size() {
        try{
            PreparedStatement sql = connection.prepareStatement("SELECT COUNT(*) FROM shipments");
            ResultSet resultSet = sql.executeQuery();
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void truncate() {
        try {
            PreparedStatement sql = connection.prepareStatement("TRUNCATE TABLE shipments");
            sql.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
