package fi.orderly.logic.dbinterfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BalanceInterface {

    Connection connection;
    public BalanceInterface(Connection connection) {
        this.connection = connection;
    }

    public void insertBalance(int roomId, int productId, int batch, double amount) throws SQLException {
        String insert = "INSERT INTO balance (room_id, product_id, batch, amount) VALUES (?, ?, ?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setInt(1, roomId);
        sql.setInt(2, productId);
        sql.setInt(3, batch);
        sql.setDouble(4, amount);
        sql.executeUpdate();
    }

    public void updateBalance(int roomId, int productId, int batch, double amount) throws SQLException {
        String update = "UPDATE balance SET amount=? WHERE room_id=? AND product_id=? AND batch=?";
        PreparedStatement sql = connection.prepareStatement(update);
        sql.setDouble(1, amount);
        sql.setInt(2, roomId);
        sql.setInt(3, productId);
        sql.setInt(4, batch);
        sql.executeUpdate();
    }

    public void deleteBalance(int roomId, int productId, int batch) throws SQLException {
        String delete = "DELETE FROM balance WHERE room_id=? AND product_id=? AND batch=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, roomId);
        sql.setInt(2, productId);
        sql.setInt(3, batch);
        sql.executeUpdate();
    }

    public double queryBalance(int roomId, int productId, int batch) throws SQLException {
        String select = "SELECT amount FROM balance WHERE room_id=? AND product_id=? AND batch=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, roomId);
        sql.setInt(2, productId);
        sql.setInt(3, batch);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getDouble("amount");
    }

    public boolean foundBalance(int roomId, int productId, int batch) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE room_id=? AND product_id=? AND batch=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, roomId);
        sql.setInt(2, productId);
        sql.setInt(3, batch);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public boolean foundBatch(int batchNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE batch=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, batchNumber);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public boolean foundProduct(int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE product_id=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, productId);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public int numberOfRoom(int roomId) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE room_id=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, roomId);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int numberOfProduct(int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE product_id=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, productId);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int numberOfBatch(int batch) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE batch=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, batch);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int numberOfZero() {
        try {
            String query = "SELECT COUNT(*) FROM balance WHERE amount=0";
            PreparedStatement sql = connection.prepareStatement(query);
            ResultSet resultSet = sql.executeQuery();
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        } catch (SQLException e) {
            return 0;
        }
    }

    public int size() {
        try{
            PreparedStatement sql = connection.prepareStatement("SELECT COUNT(*) FROM balance");
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
            PreparedStatement sql = connection.prepareStatement("TRUNCATE TABLE balance;");
            sql.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
