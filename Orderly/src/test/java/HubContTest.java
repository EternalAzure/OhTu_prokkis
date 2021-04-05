
import javafx.scene.control.TextField;
import org.junit.Before;
import java.sql.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;


import de.saxsys.javafx.test.JfxRunner;
@RunWith(JfxRunner.class)

public class HubContTest {

    final String database = "warehousetest";
    Statement statement;
    TextField name;
    TextField temperature;
    HubController hubController;

    @Before
    public void setUp() throws SQLException, NullPointerException{
        name = new TextField();
        temperature = new TextField();
        statement = ServerConnection.defaultConnection(database);
        hubController = new HubController();

        statement.execute("TRUNCATE TABLE products;");
        statement.execute("TRUNCATE TABLE rooms;");

    }

    @Test
    public void amountOfRooms() throws SQLException{
        String firstSQL = "INSERT INTO rooms (room, temperature) VALUES ('Hedelmät', 14)";
        String secondSQL = "INSERT INTO rooms (room, temperature) VALUES ('Marjat', 14)";
        String thirdSQL = "DELETE FROM rooms WHERE room='Marjat'";

        Assertions.assertEquals(0, hubController.amountOfRooms(statement));

        statement.execute(firstSQL);
        Assertions.assertEquals(1, hubController.amountOfRooms(statement));

        statement.execute(secondSQL);
        Assertions.assertEquals(2, hubController.amountOfRooms(statement));

        statement.execute(thirdSQL);
        Assertions.assertEquals(1, hubController.amountOfRooms(statement));
    }

    @Test
    public void addRoom()throws SQLException{
        TextField name = new TextField();
        TextField temperature = new TextField();

        //When both name and temperature is given
        name.setText("Hedelmät");
        temperature.setText("14");
        hubController.addRoom(name, temperature, statement);
        Assertions.assertEquals(1, hubController.amountOfRooms(statement));

        //When only name is given
        name.setText("Marjat");
        temperature.setText("");
        hubController.addRoom(name, temperature, statement);
        Assertions.assertEquals(2, hubController.amountOfRooms(statement));

        //When only temperature is given
        name.setText("");
        temperature.setText("7");
        hubController.addRoom(name, temperature, statement);
        Assertions.assertEquals(2, hubController.amountOfRooms(statement));

        //Non numeric temperature
        name.setText("Kalat");
        temperature.setText("17a");
        hubController.addRoom(name, temperature, statement);
        Assertions.assertEquals(2, hubController.amountOfRooms(statement));

        //decimal temperature
        name.setText("Kalat");
        temperature.setText("2.0");
        hubController.addRoom(name, temperature, statement);
        Assertions.assertEquals(3, hubController.amountOfRooms(statement));
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
        Assertions.assertEquals(1, hubController.amountOfProducts(statement));

        //Temperature not given
        product.setText("Etiketti");
        code.setText("1");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        Assertions.assertEquals(2, hubController.amountOfProducts(statement));

        //Name not given
        product.setText("");
        code.setText("12");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        Assertions.assertEquals(2, hubController.amountOfProducts(statement));

        //Code not given
        product.setText("Etiketti");
        code.setText("");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        Assertions.assertEquals(2, hubController.amountOfProducts(statement));

        //Unit not given
        product.setText("Etiketti");
        code.setText("0033");
        unit.setText("");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        Assertions.assertEquals(2, hubController.amountOfProducts(statement));

        //Non numeric code given
        product.setText("Etiketti");
        code.setText("abc");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        Assertions.assertEquals(2, hubController.amountOfProducts(statement));

        //Non numeric temperature
        product.setText("Etiketti");
        code.setText("0022");
        unit.setText("KPL");
        temperature.setText("20A");
        hubController.addProduct(product, code, unit, temperature, statement);
        Assertions.assertEquals(2, hubController.amountOfProducts(statement));

        //Same name given twice
        product.setText("Etiketti");
        code.setText("0022");
        unit.setText("KPL");
        temperature.setText("");
        hubController.addProduct(product, code, unit, temperature, statement);
        Assertions.assertEquals(2, hubController.amountOfProducts(statement));
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
        Assertions.assertTrue(hubController.isNumeric(input));

        product.setText("0001");
        code.setText("0002.2");
        //unit.setText("0003,3");
        temperature.setText("");
        Assertions.assertTrue(hubController.isNumeric(input));

        product.setText("A1");
        code.setText("");
        unit.setText("");
        temperature.setText("");
        Assertions.assertFalse(hubController.isNumeric(input));

        product.setText("A.1");
        Assertions.assertFalse(hubController.isNumeric(input));

        product.setText("ABC");
        Assertions.assertFalse(hubController.isNumeric(input));
    }

    @Test
    public void isEmpty(){
        TextField product = new TextField();
        TextField code = new TextField();
        TextField[] input = new TextField[] {product, code};

        product.setText("");
        code.setText("");
        Assertions.assertTrue(hubController.isEmpty(input));

        product.setText(null);
        code.setText(null);
        Assertions.assertTrue(hubController.isEmpty(input));

        product.setText("");
        code.setText("ABC");
        Assertions.assertTrue(hubController.isEmpty(input));

        product.setText("ABC");
        code.setText("123");
        Assertions.assertFalse(hubController.isEmpty(input));
    }
}

