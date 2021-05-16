package tables;

import fi.orderly.dao.tables.BalanceTableRow;
import fi.orderly.logic.ServerConnection;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class BalanceTableRowTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);

    @Before
    public void setUp() throws SQLException {
        db.truncateAll();
        db.rooms.insertRoom("Room 1");
        db.products.insertProduct("Etiketti", 9000, "KPL", 1);
        db.balance.insertBalance(1, 1, 1, 1);
    }

    @Test
    public void fetchData() throws SQLException {
        BalanceTableRow bt = new BalanceTableRow(1, db);
        assertEquals("Etiketti", bt.getProductName());
        assertEquals("9000", bt.getProductCode());
        assertEquals("1", bt.getProductBatch());
        assertEquals("Room 1", bt.getRoomName());
    }
}
