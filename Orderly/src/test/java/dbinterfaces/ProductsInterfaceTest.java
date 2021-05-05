package dbinterfaces;
import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ProductsInterfaceTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        assert connection != null;
        PreparedStatement sql1 = connection.prepareStatement("TRUNCATE TABLE products");
        PreparedStatement sql2 = connection.prepareStatement("TRUNCATE TABLE rooms");

        sql1.executeUpdate();
        sql2.executeUpdate();

        String insertRooms = "INSERT INTO rooms (room) VALUES ('Room 1')";
        String insertProducts = "INSERT INTO products (product, code, unit, room_id) VALUES ('Nauris', '1000', 'KG', 1)";

        PreparedStatement sql6 = connection.prepareStatement(insertRooms);
        PreparedStatement sql7 = connection.prepareStatement(insertProducts);

        sql6.executeUpdate(insertRooms);
        sql7.executeUpdate(insertProducts);
    }

    @Test
    public void insertProduct() throws SQLException {
        db.products.insertProduct("Kaali", 2000, "KG", 1);
        assertEquals(2, db.products.size());

        db.products.insertProduct("Porkkana", 3000, "KG", 6, 1);
        assertEquals(3, db.products.size());

        db.products.insertProduct("Peruna", 4000, "KG", 1);
        assertEquals(4, db.products.size());
    }

    @Test
    public void deleteProduct() throws SQLException {
        db.products.deleteProduct("Nauris");
        assertEquals(0, db.products.size());

        db.products.insertProduct("Kaali", 1000, "KG", 1);
        db.products.deleteProduct(1000);
        assertEquals(0, db.products.size());
    }

    @Test
    public void foundProduct() throws SQLException {
        assertTrue(db.products.foundProduct("Nauris"));
        assertFalse(db.products.foundProduct("Peruna"));
    }

    @Test
    public void findIdByName() throws SQLException {
        assertEquals(1, db.products.findIdByName("Nauris"));
    }

    @Test
    public void findIdByCode() throws SQLException {
        assertEquals(1, db.products.findIdByCode(1000));
    }

    @Test
    public void findCodeById() throws SQLException {
        assertEquals(1000, db.products.findCodeById(1));
    }

    @Test
    public void queryProduct() throws SQLException {
        //TODO
    }
}
