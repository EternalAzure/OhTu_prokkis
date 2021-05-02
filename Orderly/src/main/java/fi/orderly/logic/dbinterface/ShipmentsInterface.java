package fi.orderly.logic.dbinterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShipmentsInterface {

    Statement statement;
    public ShipmentsInterface(Statement statement) {
        this.statement = statement;
    }

    public void insertShipment(int number, int productId, int batch, double amount) throws SQLException {
        String insert = "INSERT INTO shipments (number, product_id, batch, amount) " +
                "VALUES (" + number + ", " + productId + ", " + batch + ", " + amount + ")";
        statement.execute(insert);
    }

    public void deleteShipment(int number) throws SQLException {
        String delete = "DELETE FROM shipments WHERE number=" + number;
        statement.execute(delete);
    }

    public boolean hasShipment(int number) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE number=" + number;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public int countBatch(int batch) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE batch=" + batch;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public boolean foundBatch(int batchNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE batch=" + batchNumber;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public int size() {
        try{
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM shipments");
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void truncate() {
        try {
            statement.execute("TRUNCATE TABLE shipments;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
