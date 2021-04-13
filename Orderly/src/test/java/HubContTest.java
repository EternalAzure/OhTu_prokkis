import java.sql.*;
import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
@RunWith(JfxRunner.class)

public class HubContTest {

    final String database = "warehousetest";
    Statement statement = ServerConnection.createConnection(database);
    HubController hubController = new HubController(database);

    @Before
    public void setUp() throws SQLException{
        assert statement != null;
        statement.execute("TRUNCATE TABLE products;");
        statement.execute("TRUNCATE TABLE rooms;");
        //Muutos jotta voidaan kokeilla .gitignore
    }

    @Test
    public void amountOfRooms() throws SQLException{
        String firstSQL = "INSERT INTO rooms (room, temperature) VALUES ('Hedelmät', 14)";
        String secondSQL = "INSERT INTO rooms (room, temperature) VALUES ('Marjat', 14)";
        String thirdSQL = "DELETE FROM rooms WHERE room='Marjat'";


        assertEquals(0, hubController.amountOfRooms());

        statement.execute(firstSQL);
        assertEquals(1, hubController.amountOfRooms());

        statement.execute(secondSQL);
        assertEquals(2, hubController.amountOfRooms());

        statement.execute(thirdSQL);
        assertEquals(1, hubController.amountOfRooms());
    }

    @Test
    public void addRoom()throws SQLException{
        TextField name = new TextField();
        TextField temperature = new TextField();

        //When both name and temperature is given
        name.setText("Hedelmät");
        temperature.setText("14");
        hubController.addRoom(name, temperature);
        assertEquals(1, hubController.amountOfRooms());

        //When only name is given
        name.setText("Marjat");
        temperature.setText("");
        hubController.addRoom(name, temperature);
        assertEquals(2, hubController.amountOfRooms());

        //When only temperature is given
        name.setText("");
        temperature.setText("7");
        hubController.addRoom(name, temperature);
        assertEquals(2, hubController.amountOfRooms());

        //Non numeric temperature
        name.setText("Kalat");
        temperature.setText("17a");
        hubController.addRoom(name, temperature);
        assertEquals(2, hubController.amountOfRooms());

        //decimal temperature
        name.setText("Kalat");
        temperature.setText("2.0");
        hubController.addRoom(name, temperature);
        assertEquals(3, hubController.amountOfRooms());
    }

    @Test
    public void addProduct() throws SQLException{
        TextField product = new TextField();
        TextField code = new TextField();
        TextField unit = new TextField();
        TextField temperature = new TextField();

        //When all is given and valid
        product.setText("Porkkana");
        code.setText("0011");
        unit.setText("KG");
        temperature.setText("4");
        hubController.addProduct(product, code, unit, temperature);
       assertEquals(1, hubController.amountOfProducts());

        //Temperature not given
        product.setText("Etiketti");
        code.setText("1");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature);
        assertEquals(2, hubController.amountOfProducts());

        //Name not given
        product.setText("");
        code.setText("12");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature);
        assertEquals(2, hubController.amountOfProducts());

        //Code not given
        product.setText("Etiketti");
        code.setText("");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature);
        assertEquals(2, hubController.amountOfProducts());

        //Unit not given
        product.setText("Etiketti");
        code.setText("0033");
        unit.setText("");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature);
        assertEquals(2, hubController.amountOfProducts());

        //Non numeric code given
        product.setText("Etiketti");
        code.setText("abc");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature);
        assertEquals(2, hubController.amountOfProducts());

        //Non numeric temperature
        product.setText("Etiketti");
        code.setText("0022");
        unit.setText("KPL");
        temperature.setText("20A");
        hubController.addProduct(product, code, unit, temperature);
        assertEquals(2, hubController.amountOfProducts());

        //Same name given twice
        product.setText("Etiketti");
        code.setText("0022");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature);
        assertEquals(2, hubController.amountOfProducts());
    }

    @Test
    public void removeRoom() throws SQLException{
        TextField room = new TextField("Hedelmät");
        TextField temperature = new TextField("14");
        hubController.addRoom(room, temperature);
        assertEquals(1, hubController.amountOfRooms());

        hubController.removeRoom(room); //room text is empty cuz addRoom()
        assertEquals(1, hubController.amountOfRooms());

        room.setText("Hedelmät");
        hubController.removeRoom(room);
        assertEquals(0, hubController.amountOfRooms());
    }

    @Test
    public void removeProduct() throws SQLException{
        TextField product = new TextField("Herne");
        TextField code = new TextField("0992");
        TextField unit = new TextField("PKT");
        TextField temperature = new TextField("7");
        hubController.addProduct(product, code, unit, temperature);
        assertEquals(1, hubController.amountOfProducts());

        hubController.removeProduct(product);
        assertEquals(1, hubController.amountOfProducts());

        product.setText("Herne");
        hubController.removeProduct(product);
        assertEquals(0, hubController.amountOfProducts());
    }

    @Test
    public void changeBalance() throws SQLException{
        TextField room = new TextField("Kalat");
        TextField temperature = new TextField();
        TextField product = new TextField("Kuha");
        TextField batch = new TextField("9921");
        TextField unit = new TextField("KG");
        TextField code = new TextField("0132");

        hubController.addRoom(room, temperature);
        hubController.addProduct(product, code, unit, temperature);

        String insert = "INSERT INTO balance (room, product, batch, amount) VALUES ()";


        String sql = "UPDATE balance SET amount=60 WHERE id=1";
        statement.executeUpdate(sql);
        String query = "SELECT amount FROM balance WHERE id=1";
        ResultSet result = statement.executeQuery(query);
        while(result.next()) {
            String amount = result.getString("amount");
            System.out.println("amount: " + amount);
        }

        room.setText("Kalat"); product.setText("Kuha");
        assertEquals("60", hubController.getBalance(room, product, batch));
    }

    @Test
    public void tranfer(){

    }

    @Test
    public void receive(){

    }

    @Test
    public void collect(){
        //TODO
    }

    @Test
    public void isNumeric(){
        TextField product = new TextField();
        TextField code = new TextField();
        TextField unit = new TextField();
        TextField temperature = new TextField();
        TextField[] input = new TextField[] {product, code, unit, temperature};

        product.setText("1");
        code.setText("22");
        unit.setText("3.3");
        //temperature.setText("4,4");
        assertTrue(hubController.isNumeric(input));

        product.setText("0001");
        code.setText("0002.2");
        //unit.setText("0003,3");
        temperature.setText("");
        assertTrue(hubController.isNumeric(input));

        product.setText("A1");
        code.setText("");
        unit.setText("");
        temperature.setText("");
        assertFalse(hubController.isNumeric(input));

        product.setText("A.1");
        assertFalse(hubController.isNumeric(input));

        product.setText("ABC");
        assertFalse(hubController.isNumeric(input));
    }

    @Test
    public void isEmpty(){
        TextField product = new TextField();
        TextField code = new TextField();
        TextField[] input = new TextField[] {product, code};

        product.setText("");
        code.setText("");
        assertTrue(hubController.isEmpty(input));

        product.setText(null);
        code.setText(null);
        assertTrue(hubController.isEmpty(input));

        product.setText("");
        code.setText("ABC");
        assertTrue(hubController.isEmpty(input));

        product.setText("ABC");
        code.setText("123");
        assertFalse(hubController.isEmpty(input));
    }

}

