import fi.orderly.dao.Shipment;
import fi.orderly.logic.HubController;
import fi.orderly.logic.ServerConnection;

import fi.orderly.logic.Utils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Statement;

public class ShipmentTest {

    final String database = ServerConnection.TEST_DATABASE;
    Statement statement = ServerConnection.createConnection(database);
    HubController hubController = new HubController(statement);
    Utils utils = new Utils(statement);
    Shipment shipment;
    @Before
    public void setUp() throws SQLException {
        assert statement != null;
        statement.execute("TRUNCATE TABLE products");
        statement.execute("TRUNCATE TABLE rooms");
        statement.execute("TRUNCATE TABLE balance");
        statement.execute("TRUNCATE TABLE shipments");

        hubController.createTestShipment();
    }
    @Test
    public void fetchData() {
        shipment = new Shipment("1", statement);
        //Check that test shipment makes 5 products
        assertEquals(5, utils.amountOfProducts());

        //-- SHOULD PASS --//
        //Right number of DataPackages (1 for each product)
        shipment.forTestingOnly("1");
        assertEquals(5, shipment.getLength());
        //Shipment number is saved and can be accessed
        assertEquals("1", shipment.getShipmentNumber());
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
