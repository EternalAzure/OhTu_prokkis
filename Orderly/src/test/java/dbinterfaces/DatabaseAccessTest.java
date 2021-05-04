package dbinterfaces;

import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseAccessTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() {
        db.rooms.truncate();
        db.shipments.truncate();
        db.balance.truncate();
    }


    @Test
    public void foundBatch() throws SQLException {
        assertFalse(db.foundBatch(1));

        db.shipments.insertShipment(1, 1, 1, 100);
        assertTrue(db.foundBatch(1));

        db.balance.insertBalance(1, 1, 2, 100);
        assertTrue(db.foundBatch(2));
    }

    @Test
    public void foundRooms() throws SQLException {
        assertFalse(db.foundRooms(new String[] { "Room 1", "Room 2", "Room 3" }));

        db.rooms.insertRoom("Room 1");
        assertFalse(db.foundRooms(new String[] { "Room 1", "Room 2", "Room 3" }));

        db.rooms.insertRoom("Room 2");
        assertFalse(db.foundRooms(new String[] { "Room 1", "Room 2", "Room 3" }));

        db.rooms.insertRoom("Room 3");
        assertTrue(db.foundRooms(new String[] { "Room 1", "Room 2", "Room 3" }));
    }

}
