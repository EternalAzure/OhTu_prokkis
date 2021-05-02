import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterface.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class BalanceInterfaceTest {

    final String database = ServerConnection.TEST_DATABASE;
    Statement statement = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(statement);

    @Before
    public void setUp() throws SQLException {
        assert statement != null;
        statement.execute("TRUNCATE TABLE products");
        statement.execute("TRUNCATE TABLE rooms");
        statement.execute("TRUNCATE TABLE balance");
        statement.execute("TRUNCATE TABLE shipments");
        statement.execute("TRUNCATE TABLE deliveries");

        String insertRooms = "INSERT INTO rooms (room) VALUES ('Room 1')";
        String insertProducts = "INSERT INTO products (product, code, unit, room_id) VALUES ('Nauris', '1000', 'KG', 1)";
        String insertBalance = "INSERT INTO balance (room_id, product_id, batch, amount) VALUES (1, 1, 1, 100.0)";
        statement.execute(insertRooms);
        statement.execute(insertProducts);
        statement.execute(insertBalance);
    }

    @Test
    public void size() throws SQLException {
        assertEquals(1, db.balance.size());

        String insertBalance = "INSERT INTO balance (room_id, product_id, batch, amount) VALUES (1, 1, 2, 100.0)";
        statement.execute(insertBalance);
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
        ResultSet resultSet = statement.executeQuery(select);
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
        assertEquals(100, db.balance.queryBalance(1, 1, 1), 0);
    }

    @Test
    public void foundBalance() throws SQLException {
        assertTrue(db.balance.foundBalance(1, 1, 1));
        assertFalse(db.balance.foundBalance(1, 1, 2));
    }

}
