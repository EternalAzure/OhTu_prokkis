package fi.orderly.logic;

import fi.orderly.logic.dbinterfaces.DeliveriesInterface;
import fi.orderly.logic.dbinterfaces.ProductsInterface;
import fi.orderly.logic.dbinterfaces.RoomsInterface;
import fi.orderly.logic.dbinterfaces.ShipmentsInterface;

import java.sql.Connection;
import java.sql.SQLException;

public class TestData {

    RoomsInterface roomsInterface;
    ProductsInterface productsInterface;
    ShipmentsInterface shipmentsInterface;
    DeliveriesInterface deliveriesInterface;

    public TestData(Connection connection) {
        roomsInterface = new RoomsInterface(connection);
        productsInterface = new ProductsInterface(connection);
        shipmentsInterface = new ShipmentsInterface(connection);
        deliveriesInterface = new DeliveriesInterface(connection);
    }

    public void createShipmentAndDelivery() {
        addRooms();
        addProducts();
        addShipments();
        addDeliveries();
        System.out.println("Created test data");
    }

    private void addRooms() {
        try {
            roomsInterface.insertRoom("Room 1", 8);
            roomsInterface.insertRoom("Room 2");
            roomsInterface.insertRoom("Room 3", 4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addProducts() {
        try {
            productsInterface.insertProduct("Kaali", 1000, "KG", 8, roomsInterface.findIdByName("Room 1"));
            productsInterface.insertProduct("Porkkana", 2000, "KG", 8, roomsInterface.findIdByName("Room 1"));
            productsInterface.insertProduct("Peruna", 3000, "KG", 8, roomsInterface.findIdByName("Room 1"));
            productsInterface.insertProduct("Kurpitsa", 4000, "KG", 14, roomsInterface.findIdByName("Room 2"));
            productsInterface.insertProduct("Sipuli", 5000, "KG", 14, roomsInterface.findIdByName("Room 2"));

            productsInterface.insertProduct("Kaalilaatikko", 1100, "KG", 4, roomsInterface.findIdByName("Room 3"));
            productsInterface.insertProduct("Porkkanasuikaleet", 2200, "KG", 4, roomsInterface.findIdByName("Room 3"));
            productsInterface.insertProduct("Perunamuussi", 3300, "KG", 4, roomsInterface.findIdByName("Room 3"));
            productsInterface.insertProduct("Kurpitsapalat", 4400, "KG", 4, roomsInterface.findIdByName("Room 3"));
            productsInterface.insertProduct("Sipulirenkaat", 5500, "KG", 4, roomsInterface.findIdByName("Room 3"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addShipments() {
        try {
            shipmentsInterface.insertShipment(1, productsInterface.findIdByName("Kaali"), 1, 10);
            shipmentsInterface.insertShipment(1, productsInterface.findIdByName("Porkkana"), 1, 40);
            shipmentsInterface.insertShipment(1, productsInterface.findIdByName("Peruna"), 1, 160);
            shipmentsInterface.insertShipment(1, productsInterface.findIdByName("Kurpitsa"), 1, 640);
            shipmentsInterface.insertShipment(1, productsInterface.findIdByName("Sipuli"), 1, 2560);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDeliveries() {
        try {
            deliveriesInterface.insertDelivery(1, productsInterface.findIdByName("Kaalilaatikko"), 5);
            deliveriesInterface.insertDelivery(1, productsInterface.findIdByName("Porkkanasuikaleet"), 20);
            deliveriesInterface.insertDelivery(1, productsInterface.findIdByName("Perunamuussi"), 80);
            deliveriesInterface.insertDelivery(1, productsInterface.findIdByName("Kurpitsapalat"), 320);
            deliveriesInterface.insertDelivery(1, productsInterface.findIdByName("Sipulirenkaat"), 1280);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
