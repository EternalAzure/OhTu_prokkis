package dbinterfaces;

import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class DeliveriesInterfaceTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        assert connection != null;
        db.truncateAll();
    }

    @Test
    public void insertDelivery() throws  SQLException {
        db.rooms.insertRoom("Room 3");
        db.products.insertProduct("Selleri", 1000, "PSS", 1);
        db.products.insertProduct("Mustajuuri", 2000, "KG", 1);

        //Finds 1 delivery with number 1
        db.deliveries.insertDelivery(1, 1, 100);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM deliveries WHERE number=1");
        resultSet.next();
        int n = resultSet.getInt("COUNT(*)");
        assertEquals(1, n);

        //Finds 1 delivery with number 2
        db.deliveries.insertDelivery(2, 1, 400);
        resultSet = statement.executeQuery("SELECT COUNT(*) FROM deliveries WHERE number=2");
        resultSet.next();
        n = resultSet.getInt("COUNT(*)");
        assertEquals(1, n);

        /**
         * This should be in HubControllerTest once there is method for it
         *
         *         //Does not accept same delivery number with same product (duplicate)
         *         db.deliveries.insertDelivery(2, 1, 400);
         *         resultSet = statement.executeQuery("SELECT COUNT(*) FROM deliveries WHERE number=2");
         *         resultSet.next();
         *         n = resultSet.getInt("COUNT(*)");
         *         assertEquals(1, n);
         */


        //Does accepts same delivery number with different product
        db.deliveries.insertDelivery(2, 2, 500);
        resultSet = statement.executeQuery("SELECT COUNT(*) FROM deliveries WHERE number=2");
        resultSet.next();
        n = resultSet.getInt("COUNT(*)");
        assertEquals(2, n);

        //Now there are 2 deliveries 1 and 2. 1 has one product, 2 has 2 products
        resultSet = statement.executeQuery("SELECT COUNT(*) FROM deliveries");
        resultSet.next();
        n = resultSet.getInt("COUNT(*)");
        assertEquals(3, n); // 2 + 1 = 3
    }

    @Test
    public void deleteDelivery() throws  SQLException {
        db.rooms.insertRoom("Room");
        db.products.insertProduct("Etiketti", 9000, "KPL", 1);
        db.deliveries.insertDelivery(1, 1, 20000);
        assertEquals(1, db.deliveries.size());

        db.deliveries.deleteDelivery(1);
        assertEquals(0, db.deliveries.size());
    }

    @Test
    public void foundDelivery() throws  SQLException {
        Statement statement = connection.createStatement();
        db.rooms.insertRoom("Room 3");
        db.products.insertProduct("Selleri", 1000, "PSS", 1);
        statement.executeUpdate("INSERT INTO deliveries (number, product_id, amount) VALUES (1, 1, 200)");

        assertTrue(db.deliveries.foundDelivery(1));
        assertFalse(db.deliveries.foundDelivery(2));
    }

    @Test
    public void queryDelivery() throws  SQLException {
        //TODO
    }

    @Test
    public void numberOfDelivery() throws  SQLException {
        Statement statement = connection.createStatement();
        db.rooms.insertRoom("Room 3");
        db.products.insertProduct("Selleri", 1000, "PSS", 1);
        statement.executeUpdate("INSERT INTO deliveries (number, product_id, amount) VALUES (1, 1, 200)");

        assertEquals(1, db.deliveries.numberOfDeliveries(1));
        assertEquals(0, db.deliveries.numberOfDeliveries(2));
    }

    @Test
    public void size() throws SQLException {
        assertEquals(0, db.deliveries.size());

        Statement statement = connection.createStatement();
        db.rooms.insertRoom("Room 3");
        db.products.insertProduct("Selleri", 1000, "PSS", 1);
        statement.executeUpdate("INSERT INTO deliveries (number, product_id, amount) VALUES (1, 1, 200)");

        assertEquals(1, db.deliveries.size());
    }

    @Test
    public void truncate() throws SQLException {
        Statement statement = connection.createStatement();
        db.rooms.insertRoom("Room 3");
        db.products.insertProduct("Selleri", 1000, "PSS", 1);
        statement.executeUpdate("INSERT INTO deliveries (number, product_id, amount) VALUES (1, 1, 200)");

        db.deliveries.truncate();
        assertEquals(0, db.deliveries.size());
    }
}
