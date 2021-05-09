package fi.orderly.logic.dbinterfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveriesInterface {

    Connection connection;
    public DeliveriesInterface(Connection connection) {
        this.connection = connection;
    }

    public void insertDelivery(int number, int productId, double amount) throws SQLException {
        String insert = "INSERT INTO deliveries (number, product_id, amount) VALUES (?, ?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setInt(1, number);
        sql.setInt(2, productId);
        sql.setDouble(3, amount);
        sql.executeUpdate();
    }

    public void deleteDelivery(int number) throws SQLException {
        String delete = "DELETE FROM deliveries WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, number);
        sql.executeUpdate();
    }

    public boolean foundDelivery(int number, int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM deliveries WHERE number=? AND product_id=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, number);
        sql.setInt(2, productId);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public boolean foundDelivery(int number) throws SQLException {
        String query = "SELECT COUNT(*) FROM deliveries WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, number);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public int numberOfDeliveries(int number) throws SQLException {
        String query = "SELECT COUNT(*) FROM deliveries WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, number);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public ResultSet queryDelivery(int deliveryNumber) throws SQLException {
        String select = "SELECT product_id, amount FROM deliveries WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, deliveryNumber);
        ResultSet resultSet = sql.executeQuery();
        return resultSet;
    }

    public int size() {
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT COUNT(*) FROM deliveries");
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
            PreparedStatement a = connection.prepareStatement("BEGIN;");
            PreparedStatement b = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;");
            PreparedStatement c = connection.prepareStatement("TRUNCATE TABLE deliveries;");
            PreparedStatement d = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1;");
            PreparedStatement e = connection.prepareStatement("COMMIT;");
            a.execute();
            b.execute();
            c.executeUpdate();
            d.execute();
            e.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            String delete = "DELETE FROM deliveries";
            PreparedStatement sql = connection.prepareStatement(delete);
            sql.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Didn't delete rows in deliveries");
        }
    }
}
