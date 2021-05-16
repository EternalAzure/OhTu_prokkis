package tables;

import fi.orderly.dao.tables.RoomsTableRow;
import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class RoomsTableRowTest {

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
    public void fetchData() throws SQLException {
        RoomsTableRow rt1 = new RoomsTableRow(1, db);
        assertEquals("Room 1", rt1.getRoomName());
        assertEquals("-", rt1.getRoomTemperature());

        RoomsTableRow rt2 = new RoomsTableRow(2, db);
        assertEquals("Room 2", rt2.getRoomName());
        assertEquals("4.0", rt2.getRoomTemperature());

    }
}
