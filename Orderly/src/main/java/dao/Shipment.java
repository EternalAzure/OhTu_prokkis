package dao;

import logic.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Shipment {

    private Statement statement;
    private Utils utils;
    private DataPackage[] list;

    public Shipment(String shipmentNumber, Statement statement) {
        this.statement = statement;
        utils = new Utils(statement);
        fetchData(shipmentNumber);
    }

    public DataPackage getDataPackage(int index) {
        if (index < 0 || index > list.length) return null;

        return list[index];
    }

    private void fetchData(String shipmentNumber) {
        String query = "SELECT products.product, products.code, shipments.batch, shipments.amount, products.unit, rooms.room FROM shipments, products, rooms " +
                "WHERE shipment_number='"+shipmentNumber+"' AND products.id=shipments.product_id AND rooms.id=products.defaultroom_id";

        String sizeQuery = "SELECT COUNT(*) FROM shipments, products, rooms WHERE shipment_number='" + shipmentNumber + "' AND products.id=shipments.product_id AND rooms.id=products.defaultroom_id";
        list = new DataPackage[utils.getResultInt(sizeQuery, "COUNT(*)")];

        try (ResultSet result = statement.executeQuery(query)) {
            int i = 0;
            while (result.next()) {
                String name = result.getString("products.product");
                String code = result.getString("products.code");
                String batch = result.getString("shipments.batch");
                String amount = result.getString("shipments.amount");
                String unit = result.getString("products.unit");
                String room = result.getString("rooms.room");

                list[i] = new DataPackage(name, code, batch, amount, unit, room);
                i++;
            }

        } catch (SQLException e) {

        }
    }

    public int getLength() {
        return list.length;
    }

    public class DataPackage {
        String name;
        String code;
        String batch;
        String amount;
        String unit;
        String storageRoom;

        public DataPackage(String name, String code, String batch, String amount, String unit, String storageRoom) {
            this.name = name;
            this.code = code;
            this.batch = batch;
            this.amount = amount;
            this.unit = unit;
            this.storageRoom = storageRoom;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public String getBatch() {
            return batch;
        }

        public String getAmount() {
            return amount;
        }

        public String getUnit() {
            return unit;
        }

        public String getStorageRoom() {
            return storageRoom;
        }
    }
}
