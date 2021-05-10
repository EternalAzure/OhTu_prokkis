package tables;

import fi.orderly.dao.tables.DeliveriesTable;
import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class DeliveriesTableTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        db.truncateAll();
        db.rooms.insertRoom("Room 1");
        db.products.insertProduct("Etiketti", 9000, "KPL", 1);
        db.deliveries.insertDelivery(1, 1, 1);
    }

    @Test
    public void fetchData() {
        DeliveriesTable dt = new DeliveriesTable(1, connection);
        assertEquals("1", dt.getDeliveryNumber());
        assertEquals("Etiketti", dt.getProductName());
        assertEquals("1.0", dt.getExpectedAmount());
    }
}
