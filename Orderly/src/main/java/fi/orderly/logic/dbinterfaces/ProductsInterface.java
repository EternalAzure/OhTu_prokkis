package fi.orderly.logic.dbinterfaces;

import java.sql.*;

public class ProductsInterface {

    Connection connection;
    public ProductsInterface(Connection connection) {
        this.connection = connection;
    }

    public void insertProduct(String product, int code, String unit, double temperature, int roomId) throws SQLException {
        String insert = "INSERT INTO products (product, code, unit, temperature, room_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setString(1, product);
        sql.setInt(2, code);
        sql.setString(3, unit);
        sql.setDouble(4, temperature);
        sql.setInt(5, roomId);
        sql.executeUpdate();
    }

    public void insertProduct(String product, int code, String unit, int roomId) throws SQLException {
        String insert = "INSERT INTO products (product, code, unit, room_id) VALUES (?, ?, ?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setString(1, product);
        sql.setInt(2, code);
        sql.setString(3, unit);
        sql.setInt(4, roomId);
        sql.executeUpdate();
    }

    public void deleteProduct(String product) throws SQLException {
        String delete = "DELETE FROM products WHERE product=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setString(1, product);
        sql.executeUpdate();
    }

    public void deleteProduct(int code) throws SQLException {
        String delete = "DELETE FROM products WHERE code=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, code);
        sql.executeUpdate();
    }

    public boolean foundProduct(String name) throws SQLException {
        String select = "SELECT COUNT(*) FROM products WHERE product=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setString(1, name);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public boolean foundProduct(int code) throws SQLException {
        String select = "SELECT COUNT(*) FROM products WHERE code=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, code);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public int findIdByName(String product) throws SQLException {
        String select = "SELECT id FROM products WHERE product=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setString(1, product);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("id");
    }

    public int findIdByCode(int code) throws SQLException {
        String select = "SELECT id FROM products WHERE code=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, code);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("id");
    }

    public int findCodeById(int id) throws SQLException {
        String select = "SELECT code FROM products WHERE id=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("code");
    }

    public ResultSet queryProduct(int id) throws SQLException {
        String query = "SELECT * FROM products WHERE id=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, id);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet;
    }

    public int countProductName(String name) throws SQLException {
        String query = "SELECT COUNT(*) FROM products WHERE product=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setString(1, name);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int countProductCode(int code) throws SQLException {
        String query = "SELECT COUNT(*) FROM products WHERE code=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, code);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int size() {
        try{
            PreparedStatement sql = connection.prepareStatement("SELECT COUNT(*) FROM products");
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
            PreparedStatement sql = connection.prepareStatement("TRUNCATE TABLE products");
            sql.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
