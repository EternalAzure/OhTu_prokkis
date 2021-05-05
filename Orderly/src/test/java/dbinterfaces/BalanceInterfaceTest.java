package dbinterfaces;

import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class BalanceInterfaceTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        assert connection != null;
        PreparedStatement sql1 = connection.prepareStatement("TRUNCATE TABLE products");
        PreparedStatement sql2 = connection.prepareStatement("TRUNCATE TABLE rooms");
        PreparedStatement sql3 = connection.prepareStatement("TRUNCATE TABLE balance");
        PreparedStatement sql4 = connection.prepareStatement("TRUNCATE TABLE shipments");
        PreparedStatement sql5 = connection.prepareStatement("TRUNCATE TABLE deliveries");

        sql1.executeUpdate();
        sql2.executeUpdate();
        sql3.executeUpdate();
        sql4.executeUpdate();
        sql5.executeUpdate();

        String insertRooms = "INSERT INTO rooms (room) VALUES ('Room 1')";
        String insertProducts = "INSERT INTO products (product, code, unit, room_id) VALUES ('Nauris', '1000', 'KG', 1)";
        String insertBalance = "INSERT INTO balance (room_id, product_id, batch, amount) VALUES (1, 1, 1, 100.0)";
        PreparedStatement sql6 = connection.prepareStatement(insertRooms);
        PreparedStatement sql7 = connection.prepareStatement(insertProducts);
        PreparedStatement sql8 = connection.prepareStatement(insertBalance);

        sql6.executeUpdate(insertRooms);
        sql7.executeUpdate(insertProducts);
        sql8.executeUpdate(insertBalance);
    }

    @Test
    public void size() throws SQLException {
        assertEquals(1, db.balance.size());

        String insertBalance = "INSERT INTO balance (room_id, product_id, batch, amount) VALUES (1, 1, 2, 100.0)";
        PreparedStatement sql = connection.prepareStatement(insertBalance);
        sql.executeUpdate(insertBalance);
        assertEquals(2, db.balance.size());
    }

    @Test
    public void insertBalance() throws SQLException {
        db.balance.insertBalance(1, 1, 2, 200);
        assertEquals(2, db.balance.size());

        db.balance.insertBalance(1, 1, 3, 300);
        assertEquals(3, db.balance.size());
    }

    @Test
    public void updateBalance() throws SQLException {
        db.balance.updateBalance(1, 1, 1, 200);

        String select = "SELECT amount FROM balance WHERE room_id=1 AND product_id=1 AND batch=1";
        PreparedStatement sql = connection.prepareStatement(select);
        ResultSet resultSet = sql.executeQuery(select);
        resultSet.next();
        double saldo = resultSet.getDouble("amount");

        assertEquals(200, saldo, 0);
    }

    @Test
    public void deleteBalance() throws SQLException {
        db.balance.deleteBalance(1, 1, 1);
        assertEquals(0, db.balance.size());
    }

    @Test
    public void queryBalance() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO rooms (room) VALUES ('Room 2')");
        statement.executeUpdate("INSERT INTO products (product, code, unit, room_id) VALUES ('Kaali', 2000, 'KG', 2)");
        statement.executeUpdate("INSERT INTO balance (room_id, product_id, batch, amount) VALUES (2, 2, 2, 200)");
        statement.executeUpdate("INSERT INTO balance (room_id, product_id, batch, amount) VALUES (1, 1, 2, 300)");

        assertEquals(100, db.balance.queryBalance(1, 1, 1), 0);

        assertEquals(200, db.balance.queryBalance(2, 2, 2), 0);

        assertEquals(300, db.balance.queryBalance(1, 1, 2), 0);
    }

    @Test
    public void foundBalance() throws SQLException {
        assertTrue(db.balance.foundBalance(1, 1, 1));
        assertFalse(db.balance.foundBalance(1, 1, 2));
    }

}
