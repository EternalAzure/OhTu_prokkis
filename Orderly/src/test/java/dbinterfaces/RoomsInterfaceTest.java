package dbinterfaces;

import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoomsInterfaceTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        assert connection != null;
        PreparedStatement sql1 = connection.prepareStatement("TRUNCATE TABLE rooms");
        sql1.executeUpdate();

        String insertRooms = "INSERT INTO rooms (room) VALUES ('Room 1')";
        PreparedStatement sql2 = connection.prepareStatement(insertRooms);
        sql2.executeUpdate();
    }

    @Test
    public void insertRoom() throws SQLException {
        db.rooms.insertRoom("Room 2");
        assertEquals(2, db.rooms.size());

        db.rooms.insertRoom("Room 3");
        assertEquals(3, db.rooms.size());
    }

    @Test
    public void deleteRoom() throws SQLException {
        assertEquals(1, db.rooms.size());

        db.rooms.deleteRoom("Room 1");
        assertEquals(0, db.rooms.size());
    }
}
