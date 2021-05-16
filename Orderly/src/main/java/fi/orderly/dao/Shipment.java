package fi.orderly.dao;

import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import java.sql.*;

/**
 * Luokan tarkoitus on helpottaa käyttäjälle näytettävän taulukon luomista ja tallettaa käyttäjän
 * tekemät muutokset tietoihin, jotta ne voidaan välittää eteenpäin.
 * Luokka sisältää DataPackage[] taulun, jossa jokainen DataPackage vastaa yhtä riviä käyttäjälle
 * esitettävästä taulukosta. Lista muodostetaan luokan luomisen yhteydessä hakemalla toimitusnumeroa
 * vastaavat rivit tietokannan taulusta shipments ja hakemalla vierasavainten kautta muu oleellinen tieto.
 */

public class Shipment {

    final private DatabaseAccess db;
    private DataPackage[] list;
    final private int shipmentNumber;

    public Shipment(int shipmentNumber, Connection connection) {
        db = new DatabaseAccess(connection);
        this.shipmentNumber = shipmentNumber;
        fetchData(shipmentNumber);
    }

    public DataPackage getDataPackage(int index) {
        if (index < 0 || index >= list.length) {
            return null;
        }
        return list[index];
    }

    private void fetchData(int shipmentNumber) {
        try {
            list = new DataPackage[db.shipments.numberOfShipment(shipmentNumber)];
            PreparedStatement sql = db.queryShipment(shipmentNumber);
            ResultSet result = sql.executeQuery();
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

    /**
     * Aliluokan tarkoitus on vain tallettaa tietoa ja mahdollistaa vain määrän ja huoneen muuttamisen.
     */
    public static class DataPackage {

        String name;
        String code;
        String batch;
        String amount;
        String unit;
        String storageRoom;

        /**
         * Konstuktoria on tarkoitus käyttää vain yläluokan fetchData() metodissa.
         * @param name Tuotteen nimi
         * @param code Tuotteen koodi
         * @param batch Eränumero
         * @param amount Tilattu määrä
         * @param unit Mittayksikkö
         * @param storageRoom Huone, jonne tuote oletusarvoisesti varastoidaan
         */
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

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setStorageRoom(String storageRoom) {
            this.storageRoom = storageRoom;
        }
    }
}
