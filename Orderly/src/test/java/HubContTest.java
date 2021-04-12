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
    Statement statement;
    TextField name;
    TextField temperature;
    HubController hubController;

    @Before
    public void setUp() throws SQLException{
        name = new TextField();
        temperature = new TextField();
        statement = ServerConnection.createConnection(database);
        hubController = new HubController();

        assert statement != null;
        statement.execute("TRUNCATE TABLE products;");
        statement.execute("TRUNCATE TABLE rooms;");

    }

    @Test
    public void amountOfRooms() throws SQLException{
        String firstSQL = "INSERT INTO rooms (room, temperature) VALUES ('Hedelmät', 14)";
        String secondSQL = "INSERT INTO rooms (room, temperature) VALUES ('Marjat', 14)";
        String thirdSQL = "DELETE FROM rooms WHERE room='Marjat'";


        assertEquals(0, hubController.amountOfRooms(statement));

        statement.execute(firstSQL);
        assertEquals(1, hubController.amountOfRooms(statement));

        statement.execute(secondSQL);
        assertEquals(2, hubController.amountOfRooms(statement));

        statement.execute(thirdSQL);
        assertEquals(1, hubController.amountOfRooms(statement));
    }

    @Test
    public void addRoom()throws SQLException{
        TextField name = new TextField();
        TextField temperature = new TextField();

        //When both name and temperature is given
        name.setText("Hedelmät");
        temperature.setText("14");
        hubController.addRoom(name, temperature, statement);
        assertEquals(1, hubController.amountOfRooms(statement));

        //When only name is given
        name.setText("Marjat");
        temperature.setText("");
        hubController.addRoom(name, temperature, statement);
        assertEquals(2, hubController.amountOfRooms(statement));

        //When only temperature is given
        name.setText("");
        temperature.setText("7");
        hubController.addRoom(name, temperature, statement);
        assertEquals(2, hubController.amountOfRooms(statement));

        //Non numeric temperature
        name.setText("Kalat");
        temperature.setText("17a");
        hubController.addRoom(name, temperature, statement);
        assertEquals(2, hubController.amountOfRooms(statement));

        //decimal temperature
        name.setText("Kalat");
        temperature.setText("2.0");
        hubController.addRoom(name, temperature, statement);
        assertEquals(3, hubController.amountOfRooms(statement));
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
        hubController.addProduct(product, code, unit, temperature, statement);
       assertEquals(1, hubController.amountOfProducts(statement));

        //Temperature not given
        product.setText("Etiketti");
        code.setText("1");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        assertEquals(2, hubController.amountOfProducts(statement));

        //Name not given
        product.setText("");
        code.setText("12");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        assertEquals(2, hubController.amountOfProducts(statement));

        //Code not given
        product.setText("Etiketti");
        code.setText("");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        assertEquals(2, hubController.amountOfProducts(statement));

        //Unit not given
        product.setText("Etiketti");
        code.setText("0033");
        unit.setText("");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        assertEquals(2, hubController.amountOfProducts(statement));

        //Non numeric code given
        product.setText("Etiketti");
        code.setText("abc");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        assertEquals(2, hubController.amountOfProducts(statement));

        //Non numeric temperature
        product.setText("Etiketti");
        code.setText("0022");
        unit.setText("KPL");
        temperature.setText("20A");
        hubController.addProduct(product, code, unit, temperature, statement);
        assertEquals(2, hubController.amountOfProducts(statement));

        //Same name given twice
        product.setText("Etiketti");
        code.setText("0022");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        assertEquals(2, hubController.amountOfProducts(statement));
    }

    @Test
    public void removeRoom(){
        //assertThat();
    }

    @Test
    public void removeProduct(){

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

