package tables;

import fi.orderly.dao.tables.ProductsTable;
import fi.orderly.dao.tables.ShipmentsTable;
import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class ProductsTableTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        db.truncateAll();
        db.rooms.insertRoom("Room 1");
        db.products.insertProduct("Etiketti", 9000, "KPL", 4, 1);
    }

    @Test
    public void fetchData() throws SQLException {
        ProductsTable pt = new ProductsTable(1, db);
        assertEquals("Etiketti", pt.getProductName());
        assertEquals("9000", pt.getProductCode());
        assertEquals("KPL", pt.getProductUnit());
        assertEquals("Room 1", pt.getDefaultRoom());
        assertEquals("4.0", pt.getProductTemp());
    }
}
