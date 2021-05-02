package fi.orderly.dao;

import fi.orderly.logic.Utils;
import fi.orderly.logic.dbinterface.DatabaseAccess;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Delivery {

    int index = 0;
    DataPackage[] collected;
    HashMap<String, Double> requested;
    Statement statement;
    Utils utils;
    DatabaseAccess db;
    int deliveryNumber;

    public Delivery(int deliveryNumber, Statement statement) {
        this.statement = statement;
        utils = new Utils(statement);
        db = new DatabaseAccess(statement);
        this.deliveryNumber = deliveryNumber;
        requested = new HashMap<>();
        fetchRequest(deliveryNumber);
    }

    public void collect(String code, String batch, String amount, String room) {
        if (!validateInput(code, batch, amount, room)) {
            return;
        }
        requested.remove(code);
        collected[index] = new DataPackage(code, Integer.parseInt(batch), Double.parseDouble(amount), room);
        index++;
    }

    private boolean validateInput(String code, String batch, String amount, String room) {
        if (Utils.isEmpty(new String[] { code, batch, amount, room})) {
            return false;
        }
        if (Utils.notInt(batch)) {
            return false;
        }
        if (Utils.notDouble(amount)) {
            return false;
        }
        if (!requested.containsKey(code)) {
            return false;
        }
        try {
            if (!db.foundBatch(Integer.parseInt(batch))) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQLException. \nContact your service provider");
        }

        return !(Double.parseDouble(amount) < 0);
    }

    public void fetchRequest(int deliveryNumber) {
        try {
            String select = "SELECT products.code, deliveries.amount FROM deliveries, products WHERE deliveries.product_id=products.id AND number=" + deliveryNumber;
            collected = new DataPackage[db.deliveries.numberOfDeliveries(deliveryNumber)];
            ResultSet result = statement.executeQuery(select);

            while (result.next()) {
                String i = result.getString("code");
                double j = result.getDouble("amount");
                requested.put(i, j);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DataPackage getDataPackage(int index) {
        if (index < 0 || index >= collected.length) {
            return null;
        }
        return collected[index];
    }

    public int getLength() {
        return collected.length;
    }

    public int getDeliveryNumber() {
        return deliveryNumber;
    }

    public static class DataPackage {
        String code;
        int batch;
        double amount;
        String from;

        public DataPackage(String code, int batch, double amount, String from) {
            this.code = code;
            this.batch = batch;
            this.amount = amount;
            this.from = from;
        }

        public String getCode() {
            return code;
        }
        public int getBatch() {
            return batch;
        }
        public double getAmount() {
            return amount;
        }
        public String getFrom() {
            return from;
        }
    }
}
