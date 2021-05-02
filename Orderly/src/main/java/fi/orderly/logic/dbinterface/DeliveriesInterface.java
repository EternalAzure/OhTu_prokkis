package fi.orderly.logic.dbinterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DeliveriesInterface {

    Statement statement;
    public DeliveriesInterface(Statement statement) {
        this.statement = statement;
    }

    public void insertDelivery(int number, int productId, double amount) throws SQLException {
        String insert = "INSERT INTO deliveries (number, product_id, amount) " +
                "VALUES (" + number + ", " + productId + ", " + amount + ")";
        statement.execute(insert);
    }

    public void deleteDelivery(int number) throws SQLException {
        String delete = "DELETE FROM deliveries WHERE number=" + number;
        statement.execute(delete);
    }

    public boolean hasDelivery(int number) throws SQLException {
        String query = "SELECT COUNT(*) FROM deliveries WHERE number=" + number;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public int numberOfDeliveries(int number) throws SQLException {
        String query = "SELECT COUNT(*) FROM deliveries WHERE number=" + number;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public ResultSet queryDelivery(int deliveryNumber) throws SQLException {
        String select = "SELECT product_id, amount FROM deliveries WHERE number=" + deliveryNumber;
        ResultSet resultSet = statement.executeQuery(select);
        return resultSet;
    }

    public int size() {
        try{
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM deliveries");
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void truncate() {
        try {
            statement.execute("TRUNCATE TABLE deliveries;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
