import fi.orderly.dao.Delivery;
import fi.orderly.logic.HubController;
import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;

public class DeliveryTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    HubController hubController = new HubController(connection);
    DatabaseAccess db = new DatabaseAccess(connection);
    Delivery delivery;

    @Before
    public void setUp() throws SQLException {
        assert connection != null;
        db.truncateAll();

        hubController.createTestData();
        delivery = new Delivery(1, connection);
    }
    @Test
    public void fetchData() {
        //-- SHOULD PASS --//
        //Collected is initialized right
        assertEquals(5, delivery.getLength());
        //Delivery number is saved and can be accessed
        assertEquals(1, delivery.getDeliveryNumber());

        //Requested has right keys
        assertTrue(delivery.getRequested().containsKey(1100));
        assertTrue(delivery.getRequested().containsKey(2200));
        assertTrue(delivery.getRequested().containsKey(3300));
        assertTrue(delivery.getRequested().containsKey(4400));
        assertTrue(delivery.getRequested().containsKey(5500));

        //Requested has right values
        assertEquals(5, delivery.getRequested().get(1100), 0);
        assertEquals(20, delivery.getRequested().get(2200), 0);
        assertEquals(80, delivery.getRequested().get(3300), 0);
        assertEquals(320, delivery.getRequested().get(4400), 0);
        assertEquals(1280, delivery.getRequested().get(5500), 0);

        //-- SHOULD NOT PASS --//
        //Input validation is done in DeliveryWorkspace.validateInput(String deliveryNumber)
    }

    @Test
    public void collect() throws SQLException {
        //BEFORE
        //requested should have product code 1100 as key
        assertTrue(delivery.getRequested().containsKey(1100));
        //Index is set to zero
        assertEquals(0, delivery.getIndex());

        delivery.collect("1100", "1", "5", "Room 3");

        //AFTER
        //requested should no longer have key 1100
        assertFalse(delivery.getRequested().containsKey(1100));
        //Index is increased to one
        assertEquals(1, delivery.getIndex());

        Delivery.DataPackage data = delivery.getDataPackage(0);
        assertEquals(1100, data.getCode());
        assertEquals(1, data.getBatch());
        assertEquals(5.0, data.getAmount(), 0);
        assertEquals("Room 3", data.getFrom());
    }
}
