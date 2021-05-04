package fi.orderly.logic;

import fi.orderly.dao.Delivery;
import fi.orderly.dao.Shipment;
import fi.orderly.logic.dbinterfaces.*;
import fi.orderly.ui.Login;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Random;

public class HubController {

    final private Utils utils;
    final private TestData testData;
    final private SaldoOperations engine;
    DatabaseAccess db;

    public HubController(Connection connection) {
        utils = new Utils(connection);
        testData = new TestData(connection);
        engine = new SaldoOperations(connection);
        db = new DatabaseAccess(connection);
    }

    public static void logout(Stage currentWindow) {
        new Login().start(new Stage());
        currentWindow.close();
    }
    public static void exit() {
        Platform.exit();
    }


    public String addRoom(String name, String temperature) {
        if (!validate01(name, temperature).isEmpty()) {
            return validate01(name, temperature);
        }
        if (!execute01(name, temperature).isEmpty()) {
            return execute01(name, temperature);
        }
        return "Success";
    }
    private String validate01(String name, String temperature) {
        try {
            if (name.isEmpty()) {
                return "Non allowed empty values";
            }
            if (db.rooms.countRoom(name) > 0) {
                return "Duplicate. Not allowed";
            }
            if (Utils.notDouble(temperature) && !temperature.isEmpty()) {
                return "Temperature has to be \neither empty or decimal";
            }
        } catch (SQLException e) {
            return "SQLException. \nContact your service provider";
        }
        return "";
    }
    private String execute01(String name, String temperature) {
        try {
            if (temperature.isEmpty()) {
                db.rooms.insertRoom(name);
            } else {
                db.rooms.insertRoom(name, Double.parseDouble(temperature));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 1062) {
                return "Duplicate. Not accepted";
            }
            return "SQLException. \nContact your service provider";
        }
        return "";
    }

    public String addProduct(String product, String code, String unit, String temperature, String storage) {
        if (!validate02(product, code, unit, temperature, storage).isEmpty()) {
            return validate02(product, code, unit, temperature, storage);
        }
        if (!execute02(product, code, unit, storage, temperature).isEmpty()) {
            return execute02(product, code, unit, storage, temperature);
        }
        return "Success";
    }
    private String validate02(String product, String code, String unit, String temperature, String storage) {
        if (Utils.isEmpty(new String[] { product, code, unit, storage })) {
            return "Non allowed empty values";
        }
        if (Utils.notInt(code)) {
            return "Code has to be integer";
        }
        if (Utils.notDouble(temperature) && !temperature.isEmpty()) {
            return "Temperature has to be decimal";
        }
        try {
            if (db.rooms.countRoom(storage) == 0) {
                return "Room not found";
            }
        } catch (NumberFormatException n) {
            return "Temperature has to be \neither empty or decimal";
        } catch (SQLException e) { }
        return "";
    }
    private String execute02(String product, String code, String unit, String storage, String temperature) {
        try {
            int roomId = db.rooms.findIdByName(storage);
            if (temperature.isEmpty()) {
                db.products.insertProduct(product, code, unit, roomId);
            } else {
                db.products.insertProduct(product, code, unit, Double.parseDouble(temperature), roomId);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return "Duplicate. Not allowed";
        } catch (SQLException e) {
            e.printStackTrace();
            return "SQLException. \nContact your service provider";
        }
        return "";
    }

    public String removeRoom(String room) {
        if (!validate03(room).isEmpty()) {
            return validate03(room);
        }
        if (!execute03(room).isEmpty()) {
            return execute03(room);
        }
        return "Success";
    }
    private String validate03(String room) {
        try {
            if (room.isEmpty()) {
                return "Non allowed empty values";
            }
            if (db.rooms.countRoom(room) == 0) {
                return "Room not found";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "SQLException. \nContact your service provider";
        }
        return "";
    }
    private String execute03(String room) {
        try {
            db.rooms.deleteRoom(room);
        } catch (SQLException e) {
            e.printStackTrace();
            return "SQLException. \nContact your service provider";
        }
        return "";
    }

    public String removeProduct(String product) {
        if (!validate04(product).isEmpty()) {
            return validate04(product);
        }
        if (!execute04(product).isEmpty()) {
            return execute04(product);
        }
        return "Success";
    }
    private String validate04(String product) {
        try {
            if (product.isEmpty()) {
                return "Non allowed empty values";
            }
            if (db.products.countProductName(product) == 0) {
                return "Product not found";
            }
        } catch (SQLException e) {
            e.getErrorCode();
            e.printStackTrace();
            return "SQLException. \nContact your service provider";
        }
        return "";
    }
    private String execute04(String product) {
        try {
            db.products.deleteProduct(product);
        } catch (SQLException e) {
            e.printStackTrace();
            return "SQLException. \nContact your service provider";
        }
        return "";
    }

    public String changeBalance(String room, String code, String batch, String newBalance) {

        try {

            if (!validate05(room, code, batch, newBalance).isEmpty()) {
                return validate05(room, code, batch, newBalance);
            }
            if (!execute05(room, code, batch, newBalance).isEmpty()) {
                return execute05(room, code, batch, newBalance);
            }

        } catch (SQLException e) {
            return "SQLException. \nContact your service provider";
        } catch (NumberFormatException n) {
            return "Wrong number format";
        }
        return "Success";
    }
    private String validate05(String room, String code, String batch, String newBalance) throws SQLException, NumberFormatException {
        if (db.rooms.countRoom(room) == 0) {
            return "Room not found";
        }
        if (db.products.countProductCode(code) == 0) {
            return "Product code not found";
        }
        if (!db.foundBatch(Integer.parseInt(batch))) {
            return "Batch number not found";
        }
        if (Double.parseDouble(newBalance) < 0) {
            return "Negative balance not accepted";
        }
        return "";
    }
    private String execute05(String room, String code, String batch, String newBalance) throws SQLException {
        int roomId = db.rooms.findIdByName(room);
        int productId = db.products.findIdByCode(code);
        int batchNumber = Integer.parseInt(batch);
        double newAmount = Double.parseDouble(newBalance);

        if (db.balance.foundBalance(roomId, productId, batchNumber)) {
            db.balance.updateBalance(roomId, productId, batchNumber, newAmount);
        } else {
            db.balance.insertBalance(roomId, productId, batchNumber, newAmount);
        }
        return "";
    }

    public String transfer(String from, String to, String code, String batch, String amount) {
        try {

            if (!validate06(from, to, code, batch, amount).isEmpty()) {
                return validate06(from, to, code, batch, amount);
            }
            if (!execute06(from, to, code, batch, amount).isEmpty()) {
                return validate06(from, to, code, batch, amount);
            }

        } catch (SQLException e) {
            return "SQLException. \nContact your service provider";
        } catch (NumberFormatException n) {
            return "Wrong number format";
        }
        return "Success";
    }
    private String validate06(String from, String to, String code, String batch, String amount) throws SQLException, NumberFormatException {
        if (!db.foundRooms(new String[] { from, to })) {
            return "Room/s not found";
        }
        if (db.products.countProductCode(code) == 0) {
            return "Product code not found";
        }
        if (!db.foundBatch(Integer.parseInt(batch))) {
            return "Batch number not found";
        }
        if (Double.parseDouble(amount) < 0) {
            return "Negative values not allowed";
        }
        return "";
    }
    private String execute06(String from, String to, String code, String batch, String amount) {
        double oldBalance = utils.getBalance(from, code, batch);
        double newBalance = oldBalance - Double.parseDouble(amount);

        if (newBalance < 0) {
            return "Would result in negative";
        }

        double targetOriginal = utils.getBalance(to, code, batch);
        double targetNewBalance = targetOriginal + Double.parseDouble(amount);
        changeBalance(from, code, batch, String.valueOf(newBalance));
        changeBalance(to, code, batch, String.valueOf(targetNewBalance));
        return "";
    }


    public String receiveShipment(Shipment shipment) {
        for (int i = 0; i < shipment.getLength(); i++) {
            String room = shipment.getDataPackage(i).getStorageRoom();
            String code = shipment.getDataPackage(i).getCode();
            String batch = shipment.getDataPackage(i).getBatch();
            String amount = shipment.getDataPackage(i).getAmount();
            changeBalance(room, code, batch, amount);
        }
        try {
            db.shipments.deleteShipment(shipment.getShipmentNumber());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Success";
    }

    public void sendDelivery(Delivery delivery) {
        try {
            for (int i = 0; i < delivery.getLength(); i++) {
                String code = delivery.getDataPackage(i).getCode();
                int batch = delivery.getDataPackage(i).getBatch();
                double amount = delivery.getDataPackage(i).getAmount();
                String room = delivery.getDataPackage(i).getFrom();

                engine.subractBalance(room, code, batch, amount);
            }

            db.deliveries.deleteDelivery(delivery.getDeliveryNumber());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    // MENUBAR TRUNCATE
    public void truncateRooms() {
        db.rooms.truncate();
    }
    public void truncateProducts() {
        db.products.truncate();
    }
    public void truncateBalance() {
        db.balance.truncate();
    }
    public void truncateShipments() {
        db.shipments.truncate();
    }
    public void truncateDeliveries() {
        db.deliveries.truncate();
    }
    public void truncateAll() {
        db.rooms.truncate();
        db.products.truncate();
        db.balance.truncate();
        db.shipments.truncate();
        db.deliveries.truncate();
    }

    //MENUBAR POPULATE
    public void generateRooms() {
        int n = db.rooms.size();
        for (int i = n; i < n + 10; i++) {
            Random r = new Random();
            int temp = r.nextInt(44) - 22;
            addRoom("Room " + i, "" + temp);
        }
        System.out.println("10 rooms generated");
    }
    public void generateProducts() {
        addRoom("Room 1", "");
        String[] prefixes = new String[]
        { "ba", "bo", "bi", "ce", "ci", "ca", "doo", "fu", "gua", "gas", "hedd", "hull", "hym", "j", "kappa", "luu", "luo", "las", "mello" };
        String[] suffixes = new String[] {"aas", "er", "nips", "seed", "ror", "berry", "beet", "root", "leaf", "ddi", "llon", "sprout"};
        Random r = new Random();

        int counter = 0;
        int i = db.products.size();
        while (counter < 50) {
            int pre = r.nextInt(prefixes.length);
            int suf = r.nextInt(suffixes.length);
            int n = r.nextInt(1000);

            if (addProduct(prefixes[pre] + suffixes[suf] + n, "" + (i + counter), "KG", "", "Room 1").equals("Success")) {
                counter++;
            }
        }
        System.out.println("50 products generated");
    }

    //Strictly for testing purposes
    public void createTestData() {
        testData.createShipmentAndDelivery();
    }
}
