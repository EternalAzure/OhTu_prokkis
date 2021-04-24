package fi.orderly.logic;

import fi.orderly.dao.Shipment;
import fi.orderly.ui.Login;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.sql.Statement;

public class HubController {

    final private Statement statement;
    final private Utils utils;
    public HubController(Statement statement) {
        this.statement = statement;
        this.utils = new Utils(statement);
    }

    final private String numeric = "Numeric input needed";
    final private String empty = "Non allowed empty value";
    final private String decimal = "Decimal number needed";
    final private String integer = "Integer needed";

    public static void logout(Stage currentWindow) {
        new Login().start(new Stage());
        currentWindow.close();
    }
    public static void exit() {
        Platform.exit();
    }
    public String addRoom(String name, String temperature) {
        if (!validateAddRoomInput(name, temperature).isEmpty()) {
            return validateAddRoomInput(name, temperature);
        }
        String sql = "INSERT INTO rooms (room, temperature) VALUES ('" + name + "', NULLIF('" + temperature + "', ''))";
        return executeSQL(sql, "'" + name + "' already exists");
    }

    public String addProduct(String product, String code, String unit, String temperature, String storage) {
        if (!validateAddProductInput(product, code, unit, temperature, storage).isEmpty()) {
            return validateAddProductInput(product, code, unit, temperature, storage);
        }

        String query = "SELECT id FROM rooms WHERE room='" + storage + "'";
        String sql = "INSERT INTO products (product, code, unit, temperature, defaultroom_id) VALUES (" +
                "'" + product + "', " +
                "'" + code + "', " +
                "'" + unit + "', " +
                "NULLIF('" + temperature + "', ''), " +
                "" + utils.getResultInt(query, "id") + ")";
        return executeSQL(sql, "'" + product + "' already exists");
    }

    public String removeRoom(String room) {
        if (room.isEmpty()) {
            return empty;
        }
        String sql = "DELETE FROM rooms WHERE room='" + room + "';";
        return executeSQL(sql, "unknown SQL error");
    }

    public String removeProduct(String product) {
        if (product.isEmpty()) {
            return empty;
        }
        String sql = "DELETE FROM products WHERE product='" + product + "';";
        return executeSQL(sql, "unknown SQL error");
    }

    public String changeBalance(String room, String code, String batch, String newBalance) {
        if (!validateChangeBalanceInput(room, code, batch, newBalance).isEmpty()) {
            return validateChangeBalanceInput(room, code, batch, newBalance);
        }

        String roomIdQuery = "SELECT id FROM rooms WHERE room='" + room + "'";
        String productIdQuery = "SELECT id FROM products WHERE code='" + code + "'";
        String roomId = utils.getResultString(roomIdQuery, "id");
        String productId = utils.getResultString(productIdQuery, "id");

        if (!validateChangeBalanceAction(room, code, batch).isEmpty()) {
            return validateChangeBalanceAction(room, code, batch);
        }

        String countQuery = "SELECT COUNT(*) FROM balance WHERE product_id='" + productId + "' AND room_id='" + roomId + "' AND batch='" + batch + "'";
        String insert = "INSERT INTO balance (room_id, product_id, batch, amount) " +
                "VALUES ('" + roomId + "', " + productId + ", '" + batch + "', '" + newBalance + "')";
        String change = "UPDATE balance SET amount=" + newBalance + " WHERE product_id='" + productId + "' AND room_id='" + roomId + "' AND batch='" + batch + "'";

        return executeChangeBalance(countQuery, insert, change);
    }

    public String transfer(String from, String to, String code, String batch, String amount) {
        if (!validateTransferInput(from, to, code, batch, amount).isEmpty()) {
            return validateTransferInput(from, to, code, batch, amount);
        }
        double oldBalance = utils.getBalance(from, code, batch);
        double newBalance = oldBalance - Double.parseDouble(amount);

        if (!validateTransferAction(newBalance, to, from).isEmpty()) {
            return validateTransferAction(newBalance, to, from);
        }

        double targetOriginal = utils.getBalance(to, code, batch);
        double targetNewBalance = targetOriginal + Double.parseDouble(amount);
        changeBalance(from, code, batch, String.valueOf(newBalance));
        changeBalance(to, code, batch, String.valueOf(targetNewBalance));
        return "";
    }

    public String receiveShipment(Shipment shipment) {
        System.out.println("receiveShipment()");
        for (int i = 0; i < shipment.getLength(); i++) {
            String room = shipment.getDataPackage(i).getStorageRoom();
            String code = shipment.getDataPackage(i).getCode();
            String batch = shipment.getDataPackage(i).getBatch();
            String amount = shipment.getDataPackage(i).getAmount();
            changeBalance(room, code, batch, amount);
        }
        String sql = "DELETE FROM shipments WHERE shipment_number='" + shipment.getShipmentNumber() + "'";
        executeSQL(sql, "Shipment was not removed as should");
        return "";
    }

    public void sendShipment(Shipment shipment) {
        System.out.println("sendShipment()");
    }

    //region Validation methods
    private String validateAddRoomInput(String name, String temperature) {
        if (name.isEmpty()) {
            return empty;
        }
        if (!Utils.isDouble(temperature) && !temperature.isEmpty()) {
            return decimal;
        }
        return "";
    }

    private String validateAddProductInput(String product, String code, String unit, String temperature, String storage) {
        if (Utils.isEmpty(new String[] { product, code, unit, storage })) {
            return empty;
        }
        if (!Utils.isNumeric(new String[] { code })) {
            return numeric;
        }
        if (!Utils.isDouble(temperature) && !temperature.isEmpty()) {
            return decimal;
        }
        if (!utils.hasRoom(storage)) {
            return "No such room";
        }

        return "";
    }

    private String validateChangeBalanceInput(String room, String code, String batch, String newBalance) {
        if (Utils.isEmpty(new String[] { room, code, batch, newBalance })) {
            return empty;
        }
        if (!Utils.isNumeric(new String[] { code, batch })) {
            return numeric;
        }
        if (!Utils.isDouble(newBalance)) {
            return decimal;
        }
        if (Double.parseDouble(newBalance) <= 0) {
            return "Only positive numbers are allowed";
        }
        return "";
    }

    private String validateTransferInput(String from, String to, String code, String batch, String amount) {
        if (Utils.isEmpty(new String[] { from, to, code, batch })) {
            return empty;
        }
        if (!Utils.isNumeric(new String[] { code, batch })) {
            return numeric;
        }
        if (Double.parseDouble(amount) <= 0) {
            return "Only positive numbers are allowed";
        }
        return "";
    }

    private String validateChangeBalanceAction(String room, String code, String batch) {
        if (!utils.hasItem(room, "rooms", "room")) {
            return "Room does not exist";
        }
        if (!utils.hasItem(code, "products", "code")) {
            return "Product does not exist";
        }
        boolean notInShipments = !utils.hasItem(batch, "shipments", "batch");
        boolean notInBalance = !utils.hasItem(batch, "balance", "batch");
        if (notInShipments && notInBalance) {
            return "Batch does not exist. \nBatches are created along side shipments";
        }
        return "";
    }

    private String validateTransferAction(double newBalance, String to, String from) {
        if (newBalance < 0) {
            return "Would result in negative balance";
        }
        if (!utils.hasRoom(to)) {
            return "Destination does not exist";
        }
        if (!utils.hasRoom(from)) {
            return "Origin does not exist";
        }
        return "";
    }

    //endregion

    private String executeSQL(String sql, String errorMessage) {
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                return errorMessage;
            }
        }
        return "";
    }
    private String executeChangeBalance(String countQuery, String insert, String change) {
        try {
            if (utils.getResultInt(countQuery, "COUNT(*)") == 0) {
                statement.execute(insert);
            } else {
                statement.executeUpdate(change);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "SQL error";
        }
        return "";
    }

    //Strictly for testing purposes
    public void createTestShipment() {
        addRoom("Room 1", "4");
        addRoom("Room 2", "");
        addProduct("Kaali", "1000", "KG", "4", "Room 1");
        addProduct("Porkkana", "2000", "KG", "4", "Room 1");
        addProduct("Peruna", "3000", "KG", "4", "Room 1");
        addProduct("Kurpitsa", "4000", "KG", "4", "Room 2");
        addProduct("Sipuli", "5000", "KG", "4", "Room 2");

        String insert1 = "INSERT INTO shipments (shipment_number, product_id, batch, amount) VALUES (1, 1, 1, 10)";
        String insert2 = "INSERT INTO shipments (shipment_number, product_id, batch, amount) VALUES (1, 2, 1, 40)";
        String insert3 = "INSERT INTO shipments (shipment_number, product_id, batch, amount) VALUES (1, 3, 1, 160)";
        String insert4 = "INSERT INTO shipments (shipment_number, product_id, batch, amount) VALUES (1, 4, 1, 640)";
        String insert5 = "INSERT INTO shipments (shipment_number, product_id, batch, amount) VALUES (1, 5, 1, 2560)";
        String[] commandList = new String[] {insert1, insert2, insert3, insert4, insert5};

        for (int i = 0; i < commandList.length; i++) {
            executeSQL(commandList[i], "");
        }
    }
    public void deleteTestShipment() {
        String sql = "TRUNCATE TABLE shipments";
        String sql2 = "TRUNCATE TABLE products";
        try {
            statement.execute(sql);
            statement.execute(sql2);
        } catch (SQLException e) {

        }
    }
}
