import java.sql.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class UtilsTest {
    final String database = ServerConnection.testDatabase;
    Statement statement = ServerConnection.createConnection(database);
    Utils utils = new Utils(statement);
    //Be sure to use test database when using methods outside of test class

    @Before
    public void setUp() throws SQLException{
        assert statement != null;
        statement.execute("TRUNCATE TABLE products");
        statement.execute("TRUNCATE TABLE rooms");
        statement.execute("TRUNCATE TABLE balance");
    }

    @Test
    public void amountOfRooms() throws SQLException{
        String insert1 = "INSERT INTO rooms (room, temperature) VALUES ('Hedelmät', 14)";
        String insert2 = "INSERT INTO rooms (room, temperature) VALUES ('Marjat', 14)";
        String delete = "DELETE FROM rooms WHERE room='Marjat'";

        assertEquals(0, utils.amountOfRooms());

        statement.execute(insert1);
        assertEquals(1, utils.amountOfRooms());

        statement.execute(insert2);
        assertEquals(2, utils.amountOfRooms());

        statement.execute(delete);
        assertEquals(1, utils.amountOfRooms());
    }

    @Test
    public void amountOfProducts()throws SQLException{
        String insert1 = "INSERT INTO products (product, code, unit) VALUES ('Banaani', '0001', 'KG')";
        String insert2 = "INSERT INTO products (product, code, unit, temperature) VALUES ('Kurpitsa', '0002', 'KG', 12.0)";
        String delete = "DELETE FROM products WHERE product='Kurpitsa'";

        assertEquals(0, utils.amountOfProducts());

        statement.execute(insert1);
        assertEquals(1, utils.amountOfProducts());

        statement.execute(insert2);
        assertEquals(2, utils.amountOfProducts());

        statement.execute(delete);
        assertEquals(1, utils.amountOfProducts());
    }

    @Test
    public void isNumeric(){
        //Normal numbers
        assertTrue(Utils.isNumeric(new String[]{"1","22","3.3","33.3"}));

        //Still numeric
        assertTrue(Utils.isNumeric(new String[]{"0001","002.2",""}));

        //Not numeric
        assertFalse(Utils.isNumeric(new String[]{"A1"}));
        assertFalse(Utils.isNumeric(new String[]{"A.1"}));
        assertFalse(Utils.isNumeric(new String[]{"ABC"}));
        assertFalse(Utils.isNumeric(new String[]{"123","ABC"}));
    }

    @Test
    public void isEmpty(){
        //Empty
        assertTrue(Utils.isEmpty(new String[]{"",""}));
        assertTrue(Utils.isEmpty(new String[]{"", "ABC"}));
        //Not empty
        assertFalse(Utils.isEmpty(new String[]{"ABC", "123"}));
    }
}
