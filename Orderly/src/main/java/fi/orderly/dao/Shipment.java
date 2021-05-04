package fi.orderly.dao;

import fi.orderly.logic.Utils;

import java.sql.*;

public class Shipment {

    final private Connection connection;
    final private Utils utils;
    private DataPackage[] list;
    final private int shipmentNumber;

    public Shipment(String shipmentNumber, Connection connection) {
        this.connection = connection;
        utils = new Utils(connection);
        this.shipmentNumber = Integer.parseInt(shipmentNumber);
        fetchData(shipmentNumber);
    }

    public DataPackage getDataPackage(int index) {
        if (index < 0 || index > list.length) {
            return null;
        }
        return list[index];
    }

    private void fetchData(String shipmentNumber) {
        String query = "SELECT products.product, products.code, shipments.batch, shipments.amount, products.unit, rooms.room FROM shipments, products, rooms " +
                "WHERE number=? AND products.id=shipments.product_id AND rooms.id=products.room_id";
        String sizeQuery = "SELECT COUNT(*) FROM shipments, products, rooms WHERE number=? AND products.id=shipments.product_id AND rooms.id=products.room_id";
        
        try {
            PreparedStatement sql1 = connection.prepareStatement(sizeQuery);
            sql1.setString(1, shipmentNumber);
            list = new DataPackage[utils.getResultInt(sizeQuery, "COUNT(*)")];

            PreparedStatement sql2 = connection.prepareStatement(query);
            sql2.setString(1, shipmentNumber);
            ResultSet result = sql2.executeQuery(query);
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
            System.out.println("SQL fail at Shipment.fetchData()");
        }
    }

    public int getLength() {
        return list.length;
    }

    public int getShipmentNumber() {
        return shipmentNumber;
    }

    public void forTestingOnly(String shipmentNumber) {
        //Use only to gain access from test class
        fetchData(shipmentNumber);
    }

    public static class DataPackage {

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
