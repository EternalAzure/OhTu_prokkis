package fi.orderly.logic.dbinterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductsInterface {

    Statement statement;
    public ProductsInterface(Statement statement) {
        this.statement = statement;
    }

    public void insertProduct(String product, String code, String unit, double temperature, int roomId) throws SQLException {
        String insert = "INSERT INTO products (product, code, unit, temperature, room_id) " +
                "VALUES ('" + product + "', '" + code + "', '" + unit + "', " + temperature + ", " + roomId + ")";
        statement.execute(insert);
    }

    public void insertProduct(String product, String code, String unit, int roomId) throws SQLException {
        String insert = "INSERT INTO products (product, code, unit, room_id) " +
                "VALUES ('" + product + "', '" + code + "', '" + unit + "', " + roomId + ")";
        statement.execute(insert);
    }

    public void deleteProduct(String product) throws SQLException {
        String delete = "DELETE FROM products WHERE product='" + product + "'";
        statement.execute(delete);
    }

    public void deleteProduct(int product) throws SQLException {
        String delete = "DELETE FROM products WHERE id=" + product;
        statement.execute(delete);
    }

    public int findIdByName(String product) throws SQLException {
        String select = "SELECT id FROM products WHERE product='" + product + "'";
        ResultSet resultSet = statement.executeQuery(select);
        resultSet.next();
        return resultSet.getInt("id");
    }

    public int findIdByCode(String code) throws SQLException {
        String select = "SELECT id FROM products WHERE code='" + code + "'";
        ResultSet resultSet = statement.executeQuery(select);
        resultSet.next();
        return resultSet.getInt("id");
    }

    public String findCodeById(int id) throws SQLException {
        String select = "SELECT code FROM products WHERE id=" + id;
        ResultSet resultSet = statement.executeQuery(select);
        resultSet.next();
        return resultSet.getString("code");
    }

    public ResultSet queryProduct(int id) throws SQLException {
        String query = "SELECT * FROM products WHERE id=" + id;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet;
    }

    public int countProductName(String name) throws SQLException {
        String query = "SELECT COUNT(*) FROM products WHERE product='" + name + "'";
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int countProductCode(String code) throws SQLException {
        String query = "SELECT COUNT(*) FROM products WHERE code='" + code + "'";
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int size() {
        try{
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM products");
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void truncate() {
        try {
            statement.execute("TRUNCATE TABLE products;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
