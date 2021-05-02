import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterface.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAccessTest {

    final String database = ServerConnection.TEST_DATABASE;
    Statement statement = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(statement);

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
