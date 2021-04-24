
import fi.orderly.logic.ServerConnection;
import fi.orderly.dao.Shipment;
import fi.orderly.logic.HubController;
import fi.orderly.logic.Utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.*;

public class HubControllerTest {

    final String database = ServerConnection.TEST_DATABASE;
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
        //-- SHOULD PASS --//
        //When both name and temperature is given
        hubController.addRoom("Hedelmät", "14");
        assertEquals(1, utils.amountOfRooms());

        //decimal temperature
        hubController.addRoom("Kalat", "2.0");
        assertEquals(2, utils.amountOfRooms());

        //When only name is given
        hubController.addRoom("Marjat", "");
        assertEquals(3, utils.amountOfRooms());

        //-- SHOULD NOT PASS --//
        //Duplicate room
        hubController.addRoom("Hedelmät", "14");
        assertEquals(3, utils.amountOfRooms());

        //When only temperature is given
        hubController.addRoom("", "7");
        assertEquals(3, utils.amountOfRooms());

        //Non numeric temperature
        hubController.addRoom("Kalat", "17a");
        assertEquals(3, utils.amountOfRooms());


    }

    @Test
    public void addProduct() throws SQLException{
        //-- SHOULD PASS --//
        hubController.addRoom("Room", "10");
        //When all is given and valid
        hubController.addProduct("Porkkana", "1111", "KG", "4", "Room");
        assertEquals(1, utils.amountOfProducts());
        //Again
        hubController.addProduct("Fenkoli", "0000", "KG", "4", "Room");
        assertEquals(2, utils.amountOfProducts());
        //Temperature not given
        hubController.addProduct("Etiketti", "2222", "KPL", "", "Room");
        assertEquals(3, utils.amountOfProducts());

        //-- SHOULD NOT PASS --//
        //Duplicate product
        hubController.addProduct("Porkkana", "1111", "KG", "4", "Room");
        assertEquals(3, utils.amountOfProducts());
        //Duplicate name
        hubController.addProduct("Porkkana", "1010", "KG", "4", "Room");
        assertEquals(3, utils.amountOfProducts());
        //Duplicate code
        hubController.addProduct("Sipuli", "1111", "KG", "4", "Room");
        assertEquals(3, utils.amountOfProducts());
        //Name not given
        hubController.addProduct("", "3333", "KPL", "3", "Room");
        assertEquals(3, utils.amountOfProducts());
        //Name and temp not given
        hubController.addProduct("", "4444", "KPL", "", "Room");
        assertEquals(3, utils.amountOfProducts());
        //Code not given
        hubController.addProduct("Palsternakka", "", "KPL", "", "Room");
        assertEquals(3, utils.amountOfProducts());
        //Unit not given
        hubController.addProduct("Lehtikaali", "5555", "", "", "Room");
        assertEquals(3, utils.amountOfProducts());
        //Non numeric code given
        hubController.addProduct("Keräkaali", "abc", "KPL", "", "Room");
        assertEquals(3, utils.amountOfProducts());
        //Non numeric temperature
        hubController.addProduct("Ananas", "6666", "KPL", "20A", "Room");
        assertEquals(3, utils.amountOfProducts());
        //Storage not given
        hubController.addProduct("Kurpitsa", "7777", "KPL", "20A", "");
        assertEquals(3, utils.amountOfProducts());
        //Faulty storage given
        hubController.addProduct("Minttu", "9999", "KPL", "20A", "Falty");
        assertEquals(3, utils.amountOfProducts());
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
        hubController.addRoom("Room", "");
        hubController.addProduct("Herne", "92", "PKT", "7", "Room");
        assertEquals(1, utils.amountOfProducts());

        hubController.removeProduct("");
        assertEquals(1, utils.amountOfProducts());

        hubController.removeProduct("Herne");
        assertEquals(0, utils.amountOfProducts());
    }

    @Test
    public void balance() throws SQLException{
        hubController.addRoom("Room", "2");
        hubController.addProduct("A", "1000", "KG", "2", "Room");
        hubController.addProduct("B", "2000", "KG", "2", "Room");
        hubController.addProduct("NoBatch", "0000", "KPL", "", "Room");
        String query = "SELECT amount FROM balance WHERE room_id=1 AND product_id=1 AND batch='0001'";
        String insert = "INSERT INTO balance (room_id, product_id, batch, amount) VALUES ('1', '1', '0001', 300.0)";
        statement.execute(insert);
        String shipment = "INSERT INTO shipments (shipment_number, product_id, batch, amount) VALUES (1, 2, '0002', 10)";
        statement.execute(shipment);

        double amount;
        amount = utils.getResultDouble(query, "amount");
        assertEquals(300.0, amount, 0);
        //Test getBalance()
        assertEquals(300.0, utils.getBalance("Room", "1000", "0001"), 0);
        //Non existing room
        assertEquals(0, utils.getBalance("Marjat", "1000", "0001"), 0);

        //-- SHOULD PASS --//
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
        //When all is valid but balance has not been set previously
        hubController.changeBalance("Room", "2000", "0002", "60.0");
        String query3 = "SELECT amount FROM balance WHERE room_id=1 AND batch='0002'";
        amount = utils.getResultDouble(query3, "amount");
        assertEquals(60, amount, 0);
        assertEquals(60, utils.getBalance("Room", "2000", "0002"), 0);

        //-- SHOULD NOT PASS --//
        //Negative number
        hubController.changeBalance("Room", "2000", "0002", "-60.0");
        assertEquals(60, utils.getBalance("Room", "2000", "0002"), 0);
        //Non numeric amount
        hubController.changeBalance("Room", "2000", "0002", "ABC");
        assertEquals(60, utils.getBalance("Room", "2000", "0002"), 0);
        //Non existing batch
        hubController.changeBalance("Room", "2000", "9090", "500");
        assertEquals(0, utils.getBalance("Room", "2000", "9090"), 0);
        assertEquals(60, utils.getBalance("Room", "2000", "0002"), 0);

    }

    @Test
    public void transfer() throws SQLException{
        hubController.addRoom("Room 1", "");
        hubController.addRoom("Room 2", "");
        hubController.addProduct("Kuha", "9920", "KG", "2", "Room 1");
        String shipment = "INSERT INTO shipments (shipment_number, product_id, batch, amount) VALUES (1, 1, 1, 10)";
        statement.execute(shipment);

        hubController.changeBalance("Room 1", "9920", "1", "45.0");
        assertEquals(45, utils.getBalance("Room 1", "9920", "1"), 0);

        //-- SHOULD PASS --//
        //Valid input
        hubController.transfer("Room 1", "Room 2", "9920", "1", "25.0");
        assertEquals(25, utils.getBalance("Room 2", "9920", "1"), 0);
        assertEquals(20, utils.getBalance("Room 1", "9920", "1"), 0);

        //-- SHOULD NOT PASS --//
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

    }

    @Test
    public void receiveShipment(){
        hubController.createTestShipment();
        hubController.receiveShipment(new Shipment("1", statement));

        assertEquals(10, utils.getBalance("Room 1", "1000", "1"), 0);
        assertEquals(40, utils.getBalance("Room 1", "2000", "1"), 0);
        assertEquals(160, utils.getBalance("Room 1", "3000", "1"), 0);
        assertEquals(640, utils.getBalance("Room 2", "4000", "1"), 0);
        assertEquals(2560, utils.getBalance("Room 2", "5000", "1"), 0);
        //Receiving shipment destroys it, thus stopping it being received again
        assertFalse(utils.hasItem("1", "shipments", "shipment_number"));
    }

    @Test
    public void collect(){
        //TODO
    }

    @Test
    public void createTestShipment(){
        hubController.createTestShipment();
        assertEquals(5, utils.amountOfProducts());
        String ka = "SELECT COUNT(*) FROM products WHERE product='Kaali' AND code='1000' AND unit='KG' AND temperature='4'";
        String po = "SELECT COUNT(*) FROM products WHERE product='Porkkana' AND code='2000' AND unit='KG' AND temperature='4'";
        String pe = "SELECT COUNT(*) FROM products WHERE product='Peruna' AND code='3000' AND unit='KG' AND temperature='4'";
        String ku = "SELECT COUNT(*) FROM products WHERE product='Kurpitsa' AND code='4000' AND unit='KG' AND temperature='4'";
        String si = "SELECT COUNT(*) FROM products WHERE product='Sipuli' AND code='5000' AND unit='KG' AND temperature='4'";
        assertEquals(1, utils.getResultInt(ka, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(po, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(pe, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(ku, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(si, "COUNT(*)"));

        String kaali = "SELECT COUNT(*) FROM shipments WHERE shipment_number=1 AND product_id=1 AND batch=1";
        String porkkana = "SELECT COUNT(*) FROM shipments WHERE shipment_number=1 AND product_id=2 AND batch=1";
        String peruna = "SELECT COUNT(*) FROM shipments WHERE shipment_number=1 AND product_id=3 AND batch=1";
        String kurpitsa = "SELECT COUNT(*) FROM shipments WHERE shipment_number=1 AND product_id=4 AND batch=1";
        String sipuli = "SELECT COUNT(*) FROM shipments WHERE shipment_number=1 AND product_id=5 AND batch=1";
        assertEquals(1, utils.getResultInt(kaali, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(porkkana, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(peruna, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(kurpitsa, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(sipuli, "COUNT(*)"));
    }


}

