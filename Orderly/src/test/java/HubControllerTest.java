
import fi.orderly.dao.Delivery;
import fi.orderly.logic.ServerConnection;
import fi.orderly.dao.Shipment;
import fi.orderly.logic.HubController;
import fi.orderly.logic.Utils;

import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.*;

public class HubControllerTest {

    final String database = ServerConnection.TEST_DATABASE;
    Connection connection = ServerConnection.createConnection(database);
    HubController hubController = new HubController(connection);
    Utils utils = new Utils(connection);
    DatabaseAccess db = new DatabaseAccess(connection);
    //BE SURE TO USE TEST DATABASE IN EVERY STEP
    //Methods outside of test class will have their own dependencies!!!

    @Before
    public void setUp() throws SQLException{
        assert connection != null;
        PreparedStatement sql1 = connection.prepareStatement("TRUNCATE TABLE products");
        PreparedStatement sql2 = connection.prepareStatement("TRUNCATE TABLE rooms");
        PreparedStatement sql3 = connection.prepareStatement("TRUNCATE TABLE balance");
        PreparedStatement sql4 = connection.prepareStatement("TRUNCATE TABLE shipments");
        PreparedStatement sql5 = connection.prepareStatement("TRUNCATE TABLE deliveries");
        sql1.executeUpdate();
        sql2.executeUpdate();
        sql3.executeUpdate();
        sql4.executeUpdate();
        sql5.executeUpdate();
    }

    @Test
    public void addRoom(){
        //-- SHOULD PASS --//
        //When both name and temperature is given
        hubController.addRoom("Hedelmät", "14");
        assertEquals(1, db.rooms.size());

        //decimal temperature
        hubController.addRoom("Kalat", "2.0");
        assertEquals(2, db.rooms.size());

        //When only name is given
        hubController.addRoom("Marjat", "");
        assertEquals(3, db.rooms.size());

        //-- SHOULD NOT PASS --//
        //Duplicate room
        hubController.addRoom("Hedelmät", "14");
        assertEquals(3, db.rooms.size());

        //When only temperature is given
        hubController.addRoom("", "7");
        assertEquals(3, db.rooms.size());

        //Non numeric temperature
        hubController.addRoom("Kalat", "17a");
        assertEquals(3, db.rooms.size());


    }

    @Test
    public void addProduct(){
        //-- SHOULD PASS --//
        hubController.addRoom("Room", "10");
        //When all is given and valid
        hubController.addProduct("Porkkana", "1111", "KG", "4", "Room");
        assertEquals(1, db.products.size());
        //Again
        hubController.addProduct("Fenkoli", "0000", "KG", "4", "Room");
        assertEquals(2, db.products.size());
        //Temperature not given
        hubController.addProduct("Etiketti", "2222", "KPL", "", "Room");
        assertEquals(3, db.products.size());

        //-- SHOULD NOT PASS --//
        //Duplicate product
        hubController.addProduct("Porkkana", "1111", "KG", "4", "Room");
        assertEquals(3, db.products.size());
        //Duplicate name
        hubController.addProduct("Porkkana", "1010", "KG", "4", "Room");
        assertEquals(3, db.products.size());
        //Duplicate code
        hubController.addProduct("Sipuli", "1111", "KG", "4", "Room");
        assertEquals(3, db.products.size());
        //Name not given
        hubController.addProduct("", "3333", "KPL", "3", "Room");
        assertEquals(3, db.products.size());
        //Name and temp not given
        hubController.addProduct("", "4444", "KPL", "", "Room");
        assertEquals(3, db.products.size());
        //Code not given
        hubController.addProduct("Palsternakka", "", "KPL", "", "Room");
        assertEquals(3, db.products.size());
        //Unit not given
        hubController.addProduct("Lehtikaali", "5555", "", "", "Room");
        assertEquals(3, db.products.size());
        //Non numeric code given
        hubController.addProduct("Keräkaali", "abc", "KPL", "", "Room");
        assertEquals(3, db.products.size());
        //Non numeric temperature
        hubController.addProduct("Ananas", "6666", "KPL", "20A", "Room");
        assertEquals(3, db.products.size());
        //Storage not given
        hubController.addProduct("Kurpitsa", "7777", "KPL", "20A", "");
        assertEquals(3, db.products.size());
        //Faulty storage given
        hubController.addProduct("Minttu", "9999", "KPL", "20A", "Falty");
        assertEquals(3, db.products.size());
    }

    @Test
    public void removeRoom(){
        hubController.addRoom("Hedelmät", "14");
        assertEquals(1, db.rooms.size());

        hubController.removeRoom("");
        assertEquals(1, db.rooms.size());

        hubController.removeRoom("Hedelmät");
        assertEquals(0, db.rooms.size());
    }

    @Test
    public void removeProduct(){
        hubController.addRoom("Room", "");
        hubController.addProduct("Herne", "92", "PKT", "7", "Room");
        assertEquals(1, db.products.size());

        hubController.removeProduct("");
        assertEquals(1, db.products.size());

        hubController.removeProduct("Herne");
        assertEquals(0, db.products.size());
    }

    @Test
    public void balance() throws SQLException{
        db.rooms.insertRoom("Room", 2);
        db.products.insertProduct("A", "1000", "KG", 2, 1);
        db.products.insertProduct("B", "2000", "KG", 2, 1);
        db.products.insertProduct("NoBatch", "0000", "KPL", 1);

        db.balance.insertBalance(1, 1, 1, 300.0);
        db.shipments.insertShipment(1, 2, 2, 10);
        double amount = db.balance.queryBalance(1, 1, 1);

        assertEquals(300.0, amount, 0);
        //Test getBalance()
        assertEquals(300.0, utils.getBalance("Room", "1000", "1"), 0);
        //Non existing room
        assertEquals(0, utils.getBalance("Marjat", "1000", "1"), 0);

        //-- SHOULD PASS --//
        //Updates existing row
        hubController.changeBalance("Room", "1000", "1", "10.0");
        assertEquals(10, db.balance.queryBalance(1, 1, 1), 0);
        //Update with integer
        hubController.changeBalance("Room", "1000", "1", "20");
        assertEquals(20, db.balance.queryBalance(1, 1, 1), 0);
        //When all is valid but balance has not been set previously
        hubController.changeBalance("Room", "2000", "2", "60.0");
        assertEquals(60, db.balance.queryBalance(1, 2, 2), 0);

        //-- SHOULD NOT PASS --//
        //Negative number
        hubController.changeBalance("Room", "2000", "2", "-60.0");
        assertEquals(60, db.balance.queryBalance(1, 2, 2), 0);
        //Non numeric amount
        hubController.changeBalance("Room", "2000", "2", "ABC");
        assertEquals(60, db.balance.queryBalance(1, 2, 2), 0);
        //Non existing batch
        hubController.changeBalance("Room", "2000", "9090", "500");
        assertEquals(2, db.balance.size(), 0);
        assertEquals(60, utils.getBalance("Room", "2000", "2"), 0);
        //Update without batch. Should fail to change balance and NOT create new "batch"
        hubController.changeBalance("Room", "1000", "", "30.0");
        assertEquals(2, db.balance.size(), 0);
        assertEquals(20, db.balance.queryBalance(1, 1, 1), 0);

    }

    @Test
    public void transfer() throws SQLException{
        db.rooms.insertRoom("Room 1");
        db.rooms.insertRoom("Room 2");
        db.products.insertProduct("Kuha", "9920", "KG", 2, 1);
        db.shipments.insertShipment(1, 1, 1, 10);

        db.balance.insertBalance(1, 1, 1, 45.0);
        assertEquals(45, utils.getBalance("Room 1", "9920", "1"), 0);

        //-- SHOULD PASS --//
        //Valid input
        hubController.transfer("Room 1", "Room 2", "9920", "1", "30.0");
        assertEquals(30, db.balance.queryBalance(2, 1, 1), 0);
        assertEquals(15, db.balance.queryBalance(1, 1, 1), 0);
        //Valid input
        hubController.transfer("Room 2", "Room 1", "9920", "1", "5");
        assertEquals(25, db.balance.queryBalance(2, 1, 1), 0);
        assertEquals(20, db.balance.queryBalance(1, 1, 1), 0);

        //-- SHOULD NOT PASS --//
        //Would result in negative balance
        hubController.transfer("Room 1", "Room 2", "9920", "1", "25.0");
        assertEquals(25, db.balance.queryBalance(2, 1, 1), 0);
        assertEquals(20, db.balance.queryBalance(1, 1, 1), 0);
        //Non existing destination
        hubController.transfer("Room 2", "Room 3", "9920", "1", "20.0");
        assertEquals(25, db.balance.queryBalance(2, 1, 1), 0);
        assertEquals(2, db.balance.size());
        //Missing product code
        hubController.transfer("Room 2", "Room 1", "", "1", "20.0");
        assertEquals(25, db.balance.queryBalance(2, 1, 1), 0);
        //Missing batch
        hubController.transfer("Room 2", "Room 1", "9920", "", "20.0");
        assertEquals(25, db.balance.queryBalance(2, 1, 1), 0);
        //Negative amount
        hubController.transfer("Room 2", "Room 1", "9920", "1", "-25.0");
        assertEquals(25, db.balance.queryBalance(2, 1, 1), 0);

    }

    @Test
    public void receiveShipment() throws SQLException {
        hubController.createTestData();
        hubController.receiveShipment(new Shipment("1", connection));

        assertEquals(10, db.balance.queryBalance(db.rooms.findIdByName("Room"), db.products.findIdByCode("1000"), 1), 0);
        assertEquals(10, utils.getBalance("Room 1", "1000", "1"), 0);
        assertEquals(40, utils.getBalance("Room 1", "2000", "1"), 0);
        assertEquals(160, utils.getBalance("Room 1", "3000", "1"), 0);
        assertEquals(640, utils.getBalance("Room 2", "4000", "1"), 0);
        assertEquals(2560, utils.getBalance("Room 2", "5000", "1"), 0);
        //Receiving shipment destroys it, thus stopping it being received again
        assertFalse(db.shipments.foundShipment(1));
    }

    @Test
    public void collect(){
        //TODO
    }

    @Test
    public void createTestShipment(){
        hubController.createTestData();
        assertEquals(10, db.products.size());
        String ka = "SELECT COUNT(*) FROM products WHERE product='Kaali' AND code='1000' AND unit='KG' AND temperature='8' AND room_id=1";
        String po = "SELECT COUNT(*) FROM products WHERE product='Porkkana' AND code='2000' AND unit='KG' AND temperature='8' AND room_id=1";
        String pe = "SELECT COUNT(*) FROM products WHERE product='Peruna' AND code='3000' AND unit='KG' AND temperature='8' AND room_id=1";
        String ku = "SELECT COUNT(*) FROM products WHERE product='Kurpitsa' AND code='4000' AND unit='KG' AND temperature='14' AND room_id=2";
        String si = "SELECT COUNT(*) FROM products WHERE product='Sipuli' AND code='5000' AND unit='KG' AND temperature='14' AND room_id=2";
        assertEquals(1, utils.getResultInt(ka, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(po, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(pe, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(ku, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(si, "COUNT(*)"));

        String ka1 = "SELECT COUNT(*) FROM products WHERE product='Kaalilaatikko' AND code='1100' AND unit='KG' AND temperature='4' AND room_id=3";
        String po1 = "SELECT COUNT(*) FROM products WHERE product='Porkkanasuikaleet' AND code='2200' AND unit='KG' AND temperature='4' AND room_id=3";
        String pe1 = "SELECT COUNT(*) FROM products WHERE product='Perunamuussi' AND code='3300' AND unit='KG' AND temperature='4' AND room_id=3";
        String ku1 = "SELECT COUNT(*) FROM products WHERE product='Kurpitsapalat' AND code='4400' AND unit='KG' AND temperature='4' AND room_id=3";
        String si1 = "SELECT COUNT(*) FROM products WHERE product='Sipulirenkaat' AND code='5500' AND unit='KG' AND temperature='4' AND room_id=3";
        assertEquals(1, utils.getResultInt(ka1, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(po1, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(pe1, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(ku1, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(si1, "COUNT(*)"));

        String kaali = "SELECT COUNT(*) FROM shipments WHERE number=1 AND product_id=1 AND batch=1";
        String porkkana = "SELECT COUNT(*) FROM shipments WHERE number=1 AND product_id=2 AND batch=1";
        String peruna = "SELECT COUNT(*) FROM shipments WHERE number=1 AND product_id=3 AND batch=1";
        String kurpitsa = "SELECT COUNT(*) FROM shipments WHERE number=1 AND product_id=4 AND batch=1";
        String sipuli = "SELECT COUNT(*) FROM shipments WHERE number=1 AND product_id=5 AND batch=1";
        assertEquals(1, utils.getResultInt(kaali, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(porkkana, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(peruna, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(kurpitsa, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(sipuli, "COUNT(*)"));

        String kaali1 = "SELECT COUNT(*) FROM deliveries WHERE number=1 AND product_id=6 AND amount=5";
        String porkkana1 = "SELECT COUNT(*) FROM deliveries WHERE number=1 AND product_id=7 AND amount=20";
        String peruna1 = "SELECT COUNT(*) FROM deliveries WHERE number=1 AND product_id=8 AND amount=80";
        String kurpitsa1 = "SELECT COUNT(*) FROM deliveries WHERE number=1 AND product_id=9 AND amount=320";
        String sipuli1 = "SELECT COUNT(*) FROM deliveries WHERE number=1 AND product_id=10 AND amount=1280";
        assertEquals(1, utils.getResultInt(kaali1, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(porkkana1, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(peruna1, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(kurpitsa1, "COUNT(*)"));
        assertEquals(1, utils.getResultInt(sipuli1, "COUNT(*)"));
    }

    @Test
    public void sendDelivery() throws SQLException {
        //HUGE SET UP
        db.rooms.insertRoom("Room 3");
        db.products.insertProduct("Kaalilaatikko", "1100","KG", 1);
        db.products.insertProduct("Porkkanasuikaleet", "2200","KG", 1);
        db.products.insertProduct("Perunamuussi", "3300","KG", 1);
        db.products.insertProduct("Kurpitsapalat", "4400","KG", 1);
        db.products.insertProduct("Sipulirenkaat", "5500","KG", 1);
        assertEquals(5, db.products.size());

        db.balance.insertBalance(1, 1, 1, 5);
        db.balance.insertBalance(1, 2, 1, 20);
        db.balance.insertBalance(1, 3, 1, 80);
        db.balance.insertBalance(1, 4, 1, 320);
        db.balance.insertBalance(1, 5, 1, 1280);
        assertEquals(5, db.balance.size());
        assertEquals(0, db.balance.numberOfZero());

        db.deliveries.insertDelivery(1, 1, 5);
        db.deliveries.insertDelivery(1, 2, 20);
        db.deliveries.insertDelivery(1, 3, 80);
        db.deliveries.insertDelivery(1, 4, 320);
        db.deliveries.insertDelivery(1, 5, 1280);
        assertEquals(5, db.deliveries.size());

        Delivery delivery = new Delivery(1, connection);
        delivery.collect("1100", "1", "5", "Room 3");
        delivery.collect("2200", "1", "20", "Room 3");
        delivery.collect("3300", "1", "80", "Room 3");
        delivery.collect("4400", "1", "320", "Room 3");
        delivery.collect("5500", "1", "1280", "Room 3");
        assertEquals(5, delivery.getLength());

        assertEquals("1100", delivery.getDataPackage(0).getCode());
        assertEquals(1, delivery.getDataPackage(0).getBatch());
        assertEquals(5, delivery.getDataPackage(0).getAmount(), 0);
        assertEquals("Room 3", delivery.getDataPackage(0).getFrom());

        //ACTUAL TEST
        hubController.sendDelivery(delivery);
        System.out.println(db.deliveries.numberOfDeliveries(1));
        assertEquals(0, db.deliveries.numberOfDeliveries(1));
        assertEquals(5, db.balance.numberOfZero());
    }

    @Test
    public void generateProducts() {
        hubController.generateProducts();
        assertEquals(50, db.products.size());

        hubController.generateProducts();
        assertEquals(100, db.products.size());

        hubController.generateProducts();
        assertEquals(150, db.products.size());
    }

    @Test
    public void generateRooms() {
        hubController.generateRooms();
        assertEquals(10, db.rooms.size());

        hubController.generateRooms();
        assertEquals(20, db.rooms.size());

        hubController.generateRooms();
        assertEquals(30, db.rooms.size());
    }

}

