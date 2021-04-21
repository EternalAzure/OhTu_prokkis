import java.sql.*;

import logic.HubController;
import logic.Utils;
import org.junit.Before;
import org.junit.Test;
import logic.ServerConnection;

import static org.junit.Assert.*;


public class HubContTest {

    final String database = ServerConnection.testDatabase;
    Statement statement = ServerConnection.createConnection(database);
    HubController hubController = new HubController(statement);
    Utils utils = new Utils(statement);
    //BE SURE TO USE TEST DATABASE IN EVERY STEP
    //Methods outside of test class will have their own dependencies!!!

    @Before
    public void setUp() throws SQLException{
        assert statement != null;
        statement.execute("TRUNCATE TABLE products");
        statement.execute("TRUNCATE TABLE rooms");
        statement.execute("TRUNCATE TABLE balance");
        statement.execute("TRUNCATE TABLE shipments");
    }

    @Test
    public void addRoom()throws SQLException{
        //When both name and temperature is given
        hubController.addRoom("Hedelmät", "14");
        assertEquals(1, utils.amountOfRooms());

        //Duplicate room
        hubController.addRoom("Hedelmät", "14");
        assertEquals(1, utils.amountOfRooms());

        //When only name is given
        hubController.addRoom("Marjat", "");
        assertEquals(2, utils.amountOfRooms());

        //When only temperature is given
        hubController.addRoom("", "7");
        assertEquals(2, utils.amountOfRooms());

        //Non numeric temperature
        hubController.addRoom("Kalat", "17a");
        assertEquals(2, utils.amountOfRooms());

        //decimal temperature
        hubController.addRoom("Kalat", "2.0");
        assertEquals(3, utils.amountOfRooms());
    }

    @Test
    public void addProduct() throws SQLException{
        //When all is given and valid
        hubController.addProduct("Porkkana", "0011", "KG", "4");
        assertEquals(1, utils.amountOfProducts());

        //Duplicate product
        hubController.addProduct("Porkkana", "0011", "KG", "4");
        assertEquals(1, utils.amountOfProducts());

        //Temperature not given
        hubController.addProduct("Etiketti", "1", "KPL", "");
        assertEquals(2, utils.amountOfProducts());

        //Name not given
        hubController.addProduct("", "12", "KPL", "3");
        assertEquals(2, utils.amountOfProducts());

        //Name and temp not given
        hubController.addProduct("", "12", "KPL", "");
        assertEquals(2, utils.amountOfProducts());

        //Code not given
        hubController.addProduct("Etiketti", "", "KPL", "");
        assertEquals(2, utils.amountOfProducts());

        //Unit not given
        hubController.addProduct("Etiketti", "0033", "", "");
        assertEquals(2, utils.amountOfProducts());

        //Non numeric code given
        hubController.addProduct("Etiketti", "abc", "KPL", "");
        assertEquals(2, utils.amountOfProducts());

        //Non numeric temperature
        hubController.addProduct("Etiketti", "0022", "KPL", "20A");
        assertEquals(2, utils.amountOfProducts());
    }

    @Test
    public void removeRoom() throws SQLException{
        hubController.addRoom("Hedelmät", "14");
        assertEquals(1, utils.amountOfRooms());

        hubController.removeRoom("");
        assertEquals(1, utils.amountOfRooms());

        hubController.removeRoom("Hedelmät");
        assertEquals(0, utils.amountOfRooms());
    }

    @Test
    public void removeProduct() throws SQLException{
        hubController.addProduct("Herne", "92", "PKT", "7");
        assertEquals(1, utils.amountOfProducts());

        hubController.removeProduct("");
        assertEquals(1, utils.amountOfProducts());

        hubController.removeProduct("Herne");
        assertEquals(0, utils.amountOfProducts());
    }

    @Test
    public void balance() throws SQLException{
        hubController.addRoom("Room", "2");
        hubController.addProduct("A", "1000", "KG", "2");
        hubController.addProduct("B", "2000", "KG", "2");
        hubController.addProduct("NoBatch", "0000", "KPL", "");
        String query = "SELECT amount FROM balance WHERE room_id=1 AND product_id=1 AND batch='0001'";
        String insert = "INSERT INTO balance (room_id, product_id, batch, amount) VALUES ('1', '1', '0001', 300.0)";
        double amount;

        statement.execute(insert);
        amount = utils.getResultDouble(query, "amount");
        assertEquals(300.0, amount, 0);
        //Test getBalance
        assertEquals(300.0, utils.getBalance("Room", "1000", "0001"), 0);
        //Non existing room
        assertEquals(0, utils.getBalance("Marjat", "2233", "0003"), 0);

        //Updates existing row
        hubController.changeBalance("Room", "1000", "0001", "10.0");
        String query2 = "SELECT amount FROM balance WHERE room_id=1 AND product_id=1 AND batch='0001'";
        amount = utils.getResultDouble(query2, "amount");
        assertEquals(10, amount, 0);
        assertEquals(10, utils.getBalance("Room", "1000", "0001"), 0);
        //Update with integer
        hubController.changeBalance("Room", "1000", "0001", "20");
        assertEquals(20, utils.getBalance("Room", "1000", "0001"), 0);
        //Update without batch. Should fail to change balance and NOT create new "batch"
        hubController.changeBalance("Room", "1000", "", "30.0");
        assertEquals(20, utils.getBalance("Room", "1000", "0001"), 0);
        assertEquals(0, utils.getBalance("Room", "1000", ""), 0);

        //Creates new row
        hubController.changeBalance("Room", "2000", "0002", "60.0");
        String query3 = "SELECT amount FROM balance WHERE room_id=1 AND batch='0002'";
        amount = utils.getResultDouble(query3, "amount");
        assertEquals(60, amount, 0);
        assertEquals(60, utils.getBalance("Room", "2000", "0002"), 0);
        //Negative number
        hubController.changeBalance("Room", "2000", "0002", "-60.0");
        assertEquals(60, utils.getBalance("Room", "2000", "0002"), 0);

    }

    @Test
    public void transfer() throws SQLException{
        hubController.addRoom("Room 1", "");
        hubController.addRoom("Room 2", "");
        hubController.addProduct("Kuha", "9920", "KG", "2");
        hubController.changeBalance("Room 1", "9920", "1", "45.0");
        assertEquals(45, utils.getBalance("Room 1", "9920", "1"), 0);

        //Valid input
        hubController.transfer("Room 1", "Room 2", "9920", "1", "25.0");
        assertEquals(25, utils.getBalance("Room 2", "9920", "1"), 0);
        assertEquals(20, utils.getBalance("Room 1", "9920", "1"), 0);
        //Would result in negative balance
        hubController.transfer("Room 1", "Room 2", "9920", "1", "25.0");
        assertEquals(25, utils.getBalance("Room 2", "9920", "1"), 0);
        assertEquals(20, utils.getBalance("Room 1", "9920", "1"), 0);
        //Non existing destination
        hubController.transfer("Room 2", "Room 3", "9920", "1", "20.0");
        assertEquals(25, utils.getBalance("Room 2", "9920", "1"), 0);
        //Missing product code
        hubController.transfer("Room 2", "Room 1", "", "1", "20.0");
        assertEquals(25, utils.getBalance("Room 2", "9920", "1"), 0);
        //Missing batch
        hubController.transfer("Room 2", "Room 1", "9920", "", "20.0");
        assertEquals(25, utils.getBalance("Room 2", "9920", "1"), 0);
        //Negative amount
        hubController.transfer("Room 2", "Room 1", "9920", "1", "-25.0");
        assertEquals(25, utils.getBalance("Room 2", "9920", "1"), 0);
        //Transfer successfully product that has NO batch assigned

        //TODO
    }

    @Test
    public void collect(){
        //TODO
    }


}

