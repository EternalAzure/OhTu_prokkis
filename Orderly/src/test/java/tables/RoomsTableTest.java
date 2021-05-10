package tables;

import fi.orderly.dao.tables.ProductsTable;
import fi.orderly.dao.tables.RoomsTable;
import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class RoomsTableTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        db.truncateAll();
        db.rooms.insertRoom("Room 1");
        db.rooms.insertRoom("Room 2", 4);
    }

    @Test
    public void fetchData() {
        RoomsTable rt1 = new RoomsTable(1, connection);
        assertEquals("Room 1", rt1.getRoomName());
        assertEquals("-", rt1.getRoomTemperature());

        RoomsTable rt2 = new RoomsTable(2, connection);
        assertEquals("Room 2", rt2.getRoomName());
        assertEquals("4.0", rt2.getRoomTemperature());

    }
}
