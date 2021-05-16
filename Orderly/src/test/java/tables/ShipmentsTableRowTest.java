package tables;

import fi.orderly.dao.tables.ShipmentsTableRow;
import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class ShipmentsTableRowTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        db.truncateAll();
        db.rooms.insertRoom("Room 1");
        db.products.insertProduct("Etiketti", 9000, "KPL", 1);
        db.shipments.insertShipment(1, 1, 1, 1);
    }

    @Test
    public void fetchData() throws SQLException {
        ShipmentsTableRow st = new ShipmentsTableRow(1, db);
        assertEquals("1", st.getShipmentNumber());
        assertEquals("Etiketti", st.getProductName());
        assertEquals("1.0", st.getExpectedAmount());
        assertEquals("1", st.getBatchNumber());
    }
}
