package dbinterfaces;

import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ShipmentInterfaceTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        assert connection != null;
        db.truncateAll();

        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO rooms (room) VALUES ('Room 1')");
        statement.executeUpdate("INSERT INTO products (product, code, unit, room_id) VALUES ('Selleri', '1000', 'KG', 1)");
        statement.executeUpdate("INSERT INTO shipments (number, product_id, batch, amount) VALUES (1, 1, 1, 100)");
    }

    @Test
    public void insertShipment() throws SQLException {
        db.shipments.insertShipment(1, 1, 1, 200);
        assertEquals(2, db.shipments.size());

        db.shipments.insertShipment(1, 1, 2, 200);
        assertEquals(3, db.shipments.size());

        //TODO
    }

    @Test
    public void deleteShipment() throws SQLException {
        //Does not anything with wrong number
        db.shipments.deleteShipment(2);
        assertEquals(1, db.shipments.size());

        //Does delete stuff with right number
        db.shipments.deleteShipment(1);
        assertEquals(0, db.shipments.size());
    }

    @Test
    public void foundShipment() throws SQLException {
        assertTrue(db.shipments.foundShipment(1));
    }

    @Test
    public void foundBatch() throws SQLException {
        assertTrue(db.shipments.foundBatch(1));
    }

    @Test
    public void countBatch() throws SQLException {
        assertEquals(1, db.shipments.countBatch(1));
    }
}
