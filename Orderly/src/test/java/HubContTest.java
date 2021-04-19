import java.sql.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class HubContTest {

    final String database = "warehousetest";
    Statement statement = ServerConnection.createConnection(database);
    HubController hubController = new HubController(database);

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

        assertEquals(0, hubController.amountOfRooms());

        statement.execute(insert1);
        assertEquals(1, hubController.amountOfRooms());

        statement.execute(insert2);
        assertEquals(2, hubController.amountOfRooms());

        statement.execute(delete);
        assertEquals(1, hubController.amountOfRooms());
    }

    @Test
    public void amountOfProducts()throws SQLException{
        String insert1 = "INSERT INTO products (product, code, unit) VALUES ('Banaani', '0001', 'KG')";
        String insert2 = "INSERT INTO products (product, code, unit, temperature) VALUES ('Kurpitsa', '0002', 'KG', 12.0)";
        String delete = "DELETE FROM products WHERE product='Kurpitsa'";

        assertEquals(0, hubController.amountOfProducts());

        statement.execute(insert1);
        assertEquals(1, hubController.amountOfProducts());

        statement.execute(insert2);
        assertEquals(2, hubController.amountOfProducts());

        statement.execute(delete);
        assertEquals(1, hubController.amountOfProducts());
    }

    @Test
    public void addRoom()throws SQLException{
        //When both name and temperature is given
        hubController.addRoom("Hedelmät", "14");
        assertEquals(1, hubController.amountOfRooms());

        //Duplicate room
        hubController.addRoom("Hedelmät", "14");
        assertEquals(1, hubController.amountOfRooms());

        //When only name is given
        hubController.addRoom("Marjat", "");
        assertEquals(2, hubController.amountOfRooms());

        //When only temperature is given
        hubController.addRoom("", "7");
        assertEquals(2, hubController.amountOfRooms());

        //Non numeric temperature
        hubController.addRoom("Kalat", "17a");
        assertEquals(2, hubController.amountOfRooms());

        //decimal temperature
        hubController.addRoom("Kalat", "2.0");
        assertEquals(3, hubController.amountOfRooms());
    }

    @Test
    public void addProduct() throws SQLException{
        //When all is given and valid
        hubController.addProduct("Porkkana", "0011", "KG", "4");
        assertEquals(1, hubController.amountOfProducts());

        //Duplicate product
        hubController.addProduct("Porkkana", "0011", "KG", "4");
        assertEquals(1, hubController.amountOfProducts());

        //Temperature not given
        hubController.addProduct("Etiketti", "1", "KPL", "");
        assertEquals(2, hubController.amountOfProducts());

        //Name not given
        hubController.addProduct("", "12", "KPL", "");
        assertEquals(2, hubController.amountOfProducts());

        //Code not given
        hubController.addProduct("Etiketti", "", "KPL", "");
        assertEquals(2, hubController.amountOfProducts());

        //Unit not given
        hubController.addProduct("Etiketti", "0033", "", "");
        assertEquals(2, hubController.amountOfProducts());

        //Non numeric code given
        hubController.addProduct("Etiketti", "abc", "KPL", "");
        assertEquals(2, hubController.amountOfProducts());

        //Non numeric temperature
        hubController.addProduct("Etiketti", "0022", "KPL", "20A");
        assertEquals(2, hubController.amountOfProducts());

        //Same name given twice
        hubController.addProduct("Etiketti", "0022", "KPL", "");
        assertEquals(2, hubController.amountOfProducts());
    }

    @Test
    public void removeRoom() throws SQLException{
        hubController.addRoom("Hedelmät", "14");
        assertEquals(1, hubController.amountOfRooms());

        hubController.removeRoom("");
        assertEquals(1, hubController.amountOfRooms());

        hubController.removeRoom("Hedelmät");
        assertEquals(0, hubController.amountOfRooms());
    }

    @Test
    public void removeProduct() throws SQLException{
        hubController.addProduct("Herne", "92", "PKT", "7");
        assertEquals(1, hubController.amountOfProducts());

        hubController.removeProduct("");
        assertEquals(1, hubController.amountOfProducts());

        hubController.removeProduct("Herne");
        assertEquals(0, hubController.amountOfProducts());
    }

    @Test
    public void balance() throws SQLException{
        hubController.addRoom("Kalat", "2");
        hubController.addProduct("Kuha", "9920", "KG", "2");
        hubController.addProduct("Lohi", "9030", "KG", "2");
        String query = "SELECT amount FROM balance WHERE room_id=1 AND product_id=1 AND batch='0001'";
        String insert = "INSERT INTO balance (room_id, product_id, batch, amount) VALUES ('1', '1', '0001', 300.0)";
        double amount;

        statement.execute(insert);
        amount = getResult(query, "amount");
        assertEquals(300.0, amount, 0);

        //Test getBalance
        assertEquals(300.0, hubController.getBalance("Kalat", "Kuha", "0001"), 0);

        //Updates existing row
        hubController.changeBalance("Kalat", "9920", "0001", 50.0);
        String query2 = "SELECT amount FROM balance WHERE room_id=1 AND product_id=1 AND batch='0001'";
        amount = getResult(query2, "amount");
        assertEquals(50, amount, 0);

        //Creates new row to table balance
        hubController.changeBalance("Kalat", "9930", "0002", 60.0);
        String query3 = "SELECT amount FROM balance WHERE room_id=1 AND batch='0002'";
        amount = getResult(query3, "amount");
        assertEquals(60, amount, 0);

        //TODO
        //Without batch
    }

    @Test
    public void tranfer(){
        hubController.addRoom("Room 1", "");
        hubController.addRoom("Room 2", "");
        String setBalance = "";
        //TODO

    }

    @Test
    public void receive(){
        //TODO
    }

    @Test
    public void collect(){
        //TODO
    }

    @Test
    public void isNumeric(){
        //Normal numbers
        assertTrue(hubController.isNumeric(new String[]{"1","22","3.3","33.3"}));

        //Still numeric
        assertTrue(hubController.isNumeric(new String[]{"0001","002.2",""}));

        //Not numeric
        assertFalse(hubController.isNumeric(new String[]{"A1"}));
        assertFalse(hubController.isNumeric(new String[]{"A.1"}));
        assertFalse(hubController.isNumeric(new String[]{"ABC"}));
        assertFalse(hubController.isNumeric(new String[]{"123","ABC"}));
    }

    @Test
    public void isEmpty(){
        //Empty
        assertTrue(hubController.isEmpty(new String[]{"",""}));
        assertTrue(hubController.isEmpty(new String[]{"", "ABC"}));
        //Not empty
        assertFalse(hubController.isEmpty(new String[]{"ABC", "123"}));
    }

    public double getResult(String query, String column) throws SQLException{
        ResultSet result = statement.executeQuery(query);
        while (result.next()){
            return result.getDouble(column);
        }
        return -1;
    }

}

