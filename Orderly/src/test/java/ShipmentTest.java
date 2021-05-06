import fi.orderly.dao.Shipment;
import fi.orderly.logic.HubController;
import fi.orderly.logic.ServerConnection;

import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ShipmentTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    HubController hubController = new HubController(connection);
    DatabaseAccess db = new DatabaseAccess(connection);
    Shipment shipment;

    @Before
    public void setUp() throws SQLException {
        assert connection != null;
        db.rooms.truncate();
        db.products.truncate();
        db.balance.truncate();
        db.shipments.truncate();
        db.deliveries.truncate();

        hubController.createTestData();
    }
    @Test
    public void fetchData() {
        shipment = new Shipment(1, connection);

        //-- SHOULD PASS --//
        //Right number of DataPackages (1 for each product)
        assertEquals(5, shipment.getLength());
        //Shipment number is saved and can be accessed
        assertEquals(1, shipment.getShipmentNumber());
        //Content of DataPackage is correct
        Shipment.DataPackage data = shipment.getDataPackage(0);
        assertEquals("Kaali", data.getName());
        assertEquals("1000", data.getCode());
        assertEquals("1", data.getBatch());
        assertEquals("10.0", data.getAmount());
        assertEquals("KG", data.getUnit());
        assertEquals("Room 1", data.getStorageRoom());

        //-- SHOULD NOT PASS --//
        //Input validation is done in ShipmentWorkspace.validateInput(String shipmentNumber)
    }
}
