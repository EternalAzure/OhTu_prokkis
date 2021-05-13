import java.sql.*;

import fi.orderly.logic.Utils;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;
import fi.orderly.logic.ServerConnection;

import static org.junit.Assert.*;


public class UtilsTest {
    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    DatabaseAccess db = new DatabaseAccess(connection);
    //Be sure to use test database when using methods outside of test class

    @Before
    public void setUp() throws SQLException{
        assert connection != null;
        db.truncateAll();
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

    @Test
    public void notInt() {
        assertTrue(Utils.notInt("abc"));
        assertTrue(Utils.notInt("a1"));
        assertTrue(Utils.notInt("0.1"));
        assertFalse(Utils.notInt("5"));

        assertTrue(Utils.notInt(new String[] { "abc", "1.0", "#4" }));
        assertTrue(Utils.notInt(new String[] { "5", "1.0", "4" }));
        assertFalse(Utils.notInt(new String[] { "5" }));
        assertFalse(Utils.notInt(new String[] { "1", "2", "3" }));
    }

    @Test
    public void notDouble() {
        //TODO
    }

}
