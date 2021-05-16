package dbinterfaces;

import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.*;

public class RoomsInterfaceTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        assert connection != null;
        db.truncateAll();

        String insertRooms = "INSERT INTO rooms (room) VALUES ('Room 1')";
        PreparedStatement sql = connection.prepareStatement(insertRooms);
        sql.executeUpdate();
    }

    @Test
    public void insertRoom() throws SQLException {
        db.rooms.insertRoom("Room 2");
        assertEquals(2, db.rooms.size());

        db.rooms.insertRoom("Room 3");
        assertEquals(3, db.rooms.size());

        db.rooms.insertRoom("Room 4", 8);
        assertEquals(4, db.rooms.size());
    }

    @Test
    public void deleteRoom() throws SQLException {
        assertEquals(1, db.rooms.size());

        db.rooms.deleteRoom("Room 1");
        assertEquals(0, db.rooms.size());

        db.rooms.truncate();
        //Test ON DELETE CASCADE
        db.rooms.insertRoom("Room 1");
        db.rooms.insertRoom("Room 2");
        db.products.insertProduct("Nauris", 6000, "KG", 1);
        db.products.insertProduct("Lehtikaali", 7000, "KG", 1);
        db.products.insertProduct("Etiketti", 9000, "KPL", 2);
        assertEquals(3, db.products.size());

        db.rooms.deleteRoom("Room 1");
        assertEquals(1, db.products.size());
    }

    @Test
    public void truncate() throws SQLException {
        //Test ON DELETE CASCADE
        db.rooms.insertRoom("Room 2");
        db.products.insertProduct("Nauris", 6000, "KG", 1);
        db.products.insertProduct("Lehtikaali", 7000, "KG", 1);
        db.products.insertProduct("Etiketti", 9000, "KPL", 2);
        assertEquals(3, db.products.size());

        db.rooms.truncate();
        assertEquals(0, db.products.size());
    }

    @Test
    public void queryRoom() throws SQLException {
        String name = db.rooms.queryRoom(1)[0];
        String temperature = db.rooms.queryRoom(1)[1];
        assertEquals("Room 1", name);
        assertEquals("-", temperature);
    }

    @Test
    public void foundRoom() throws SQLException {
        assertTrue(db.rooms.foundRoom("Room 1"));
        assertFalse(db.rooms.foundRoom("Room 2"));

        db.rooms.insertRoom("Room 2");
        assertTrue(db.rooms.foundRoom("Room 2"));
    }

    @Test
    public void load50() throws SQLException {
        int[] list = db.rooms.load50(0);
        assertEquals(50, list.length);
        assertEquals(1, list[0]);
        assertEquals(0, list[1]);

        db.rooms.insertRoom("Room 2");
        db.rooms.insertRoom("Room 3");
        db.rooms.insertRoom("Room 4");

        list = db.rooms.load50(0);
        assertEquals(1, list[0]);
        assertEquals(2, list[1]);
        assertEquals(3, list[2]);
        assertEquals(4, list[3]);
        assertEquals(0, list[4]);

        list = db.rooms.load50(3);
        assertEquals(4, list[0]);
        assertEquals(0, list[1]);
        assertEquals(0, list[2]);
        assertEquals(0, list[3]);
        assertEquals(0, list[4]);
        assertEquals(0, list[49]);
    }
}
