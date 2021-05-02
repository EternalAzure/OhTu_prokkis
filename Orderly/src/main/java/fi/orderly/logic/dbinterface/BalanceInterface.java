package fi.orderly.logic.dbinterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BalanceInterface {

    Statement statement;
    public BalanceInterface(Statement statement) {
        this.statement = statement;
    }

    public void insertBalance(int roomId, int productId, int batch, double amount) throws SQLException {
        String insert = "INSERT INTO balance (room_id, product_id, batch, amount) " +
                "VALUES (" + roomId + ", " + productId + ", " + batch + "," + amount + ")";
        statement.execute(insert);
    }

    public void updateBalance(int roomId, int productId, int batch, double amount) throws SQLException {
        String update = "UPDATE balance SET amount=" + amount + " " +
                "WHERE room_id=" + roomId + " AND product_id=" + productId + " AND batch=" + batch;
        statement.executeUpdate(update);
    }

    public void deleteBalance(int roomId, int productId, int batch) throws SQLException {
        String delete = "DELETE FROM balance WHERE room_id=" + roomId + " AND product_id=" + productId + " AND batch=" + batch;
        statement.execute(delete);
    }

    public double queryBalance(int roomId, int productId, int batch) throws SQLException {
        String select = "SELECT amount FROM balance WHERE room_id=" + roomId + " AND product_id=" + productId + " AND batch=" + batch;
        ResultSet resultSet = statement.executeQuery(select);
        resultSet.next();
        return resultSet.getDouble("amount");
    }

    public boolean foundBalance(int roomId, int productId, int batch) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE room_id=" + roomId + " AND product_id=" + productId + " AND batch=" + batch;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public int size() {
        try{
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM balance");
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean foundBatch(int batchNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE batch=" + batchNumber;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public boolean foundProduct(int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE product_id=" + productId;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    public int numberOfRoom(int roomId) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE room_id=" + roomId;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int numberOfProduct(int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE product_id=" + productId;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int numberOfZero() throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE amount=0";
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int numberOfBatch(int batch) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE batch=" + batch;
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public void truncate() {
        try {
            statement.execute("TRUNCATE TABLE balance;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
