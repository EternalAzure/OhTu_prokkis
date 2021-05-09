package dbinterfaces;

import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseAccessTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException{
        db.truncateAll();
        db.rooms.insertRoom("Room 98");
        db.rooms.insertRoom("Room 99");
        db.products.insertProduct("Kaali", 1000, "KG", 1);
        db.products.insertProduct("Porkkana", 2000, "KG", 4, 1);
        db.products.insertProduct("Peruna", 3000, "KG", 1);
        db.products.insertProduct("Kurpitsa", 4000, "KG", 2);
        db.products.insertProduct("Sipuli", 5000, "KG", 2);
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

    @Test
    public void tableProducts() throws SQLException {
        PreparedStatement sql = db.tableProducts(1);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        assertEquals("Kaali", resultSet.getString(1));
        assertEquals(1000, resultSet.getInt(2));
        assertEquals("KG", resultSet.getString(3));
        resultSet.getInt(4);
        boolean boo = resultSet.wasNull();
        assertTrue(boo);
        assertEquals("Room 98", resultSet.getString(5));

        PreparedStatement sql1 = db.tableProducts(2);
        ResultSet resultSet1 = sql1.executeQuery();
        resultSet1.next();
        assertEquals("Porkkana", resultSet1.getString(1));
        assertEquals(2000, resultSet1.getInt(2));
        assertEquals("KG", resultSet1.getString(3));
        assertEquals(4, resultSet1.getDouble(4), 0);
        assertEquals("Room 98", resultSet.getString(5));
    }

    @Test
    public void tableBalance() throws SQLException {
        db.balance.insertBalance(1, 1, 1, 1);
        db.balance.insertBalance(2, 2, 2, 2);

        PreparedStatement sql = db.tableBalance(1);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        assertEquals("Kaali", resultSet.getString(1));
        assertEquals(1000, resultSet.getInt(2));
        assertEquals(1, resultSet.getInt(3));
        assertEquals(1, resultSet.getDouble(4), 0);
        assertEquals("Room 98", resultSet.getString(5));

        PreparedStatement sql1 = db.tableBalance(2);
        ResultSet resultSet1 = sql1.executeQuery();
        resultSet1.next();
        assertEquals("Porkkana", resultSet1.getString(1));
        assertEquals(2000, resultSet1.getInt(2));
        assertEquals(2, resultSet1.getInt(3));
        assertEquals(2, resultSet1.getDouble(4), 0);
        assertEquals("Room 99", resultSet1.getString(5));
    }

    @Test
    public void tableShipments() throws  SQLException {
        db.shipments.insertShipment(1, 1, 1, 1);
        db.shipments.insertShipment(2, 2, 2, 2);

        PreparedStatement sql = db.tableShipments(1);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        assertEquals(1, resultSet.getInt(1));
        assertEquals("Kaali", resultSet.getString(2));
        assertEquals(1, resultSet.getInt(3));
        assertEquals(1, resultSet.getDouble(4), 0);

        PreparedStatement sql1 = db.tableShipments(2);
        ResultSet resultSet1 = sql1.executeQuery();
        resultSet1.next();
        assertEquals(2, resultSet1.getInt(1));
        assertEquals("Porkkana", resultSet1.getString(2));
        assertEquals(2, resultSet1.getInt(3));
        assertEquals(2, resultSet1.getDouble(4), 0);

    }

    @Test
    public void tableDeliveries() throws SQLException {
        db.deliveries.insertDelivery(1, 1, 1);
        db.deliveries.insertDelivery(2, 2, 2);

        PreparedStatement sql = db.tableDeliveries(1);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        assertEquals(1, resultSet.getInt(1));
        assertEquals("Kaali", resultSet.getString(2));
        assertEquals(1, resultSet.getDouble(3), 0);

        PreparedStatement sql1 = db.tableDeliveries(2);
        ResultSet resultSet1 = sql1.executeQuery();
        resultSet1.next();
        assertEquals(2, resultSet1.getInt(1));
        assertEquals("Porkkana", resultSet1.getString(2));
        assertEquals(2, resultSet1.getDouble(3), 0);
    }
}
