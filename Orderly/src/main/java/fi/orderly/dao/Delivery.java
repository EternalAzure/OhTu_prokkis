package fi.orderly.dao;

import fi.orderly.logic.Utils;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Luokkan tarkoitus on säilyttää tieto siitä mitä pitää vielä kerätä ja mitä on jo kerätty.
 * Luomisen yhteydessä haetaan tietokannasta tieto kerättävistä tuotteista
 * toimitusnumeron perusteella. Tieto kerättävistä tallennetaan HasMappiin requested, josta
 * ne poistetaan sitä mukaa kuin tuotteita kerätään. Tieto kerätyistä tuotteista tallennetaan
 * taulukkoon DataPackage[] collected.
 */

public class Delivery {

    int index;
    DataPackage[] collected;
    HashMap<Integer, Double> requested;
    Connection connection;
    DatabaseAccess db;
    int deliveryNumber;

    public Delivery(int deliveryNumber, Connection connection) {
        index = 0;
        this.connection = connection;
        db = new DatabaseAccess(connection);
        this.deliveryNumber = deliveryNumber;
        requested = new HashMap<>();
        fetchRequest(deliveryNumber);
    }

    /**
     * Käytetään, kun keräilijä ottaa tuotteen hyllystä ja lisää sen lähetykseen.
     * Poistaa tuotteen kerättävien listasta ja lisää sen kerättyjen listaan.
     * @param code tuotteen koodi
     * @param batch eränumero
     * @param amount kerätty määrä
     * @param room huoneen nimi
     */
    public void collect(String code, String batch, String amount, String room) {
        if (validateInput(code, batch, amount, room)) {
            return;
        }
        requested.remove(Integer.parseInt(code));
        collected[index] = new DataPackage(Integer.parseInt(code), Integer.parseInt(batch), Double.parseDouble(amount), room);
        index++;
    }

    private boolean validateInput(String code, String batch, String amount, String room) {
        if (Utils.isEmpty(new String[] { code, batch, amount, room})) {
            return true;
        }
        if (Utils.notInt(new String[] { code, batch }) || Utils.notDouble(amount)) {
            return true;
        }
        if (!requested.containsKey(Integer.parseInt(code))) {
            return true;
        }
        try {
            if (!db.foundBatch(Integer.parseInt(batch))) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLException. \nContact tech support");
        }
        return (Double.parseDouble(amount) < 0);
    }

    private void fetchRequest(int deliveryNumber) {
        try {
            collected = new DataPackage[db.deliveries.numberOfDeliveries(deliveryNumber)];
            PreparedStatement sql = db.queryDelivery(deliveryNumber);
            ResultSet result = sql.executeQuery();
            while (result.next()) {
                int i = result.getInt("code");
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

    /**
     * Aliluokka vastaa vain tiedon talletuksesta.
     */
    public static class DataPackage {
        int code;
        int batch;
        double amount;
        String from;

        public DataPackage(int code, int batch, double amount, String from) {
            this.code = code;
            this.batch = batch;
            this.amount = amount;
            this.from = from;
        }

        public int getCode() {
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


    //ONLY FOR TESTING
    public HashMap<Integer, Double> getRequested() {
        return requested;
    }
    public int getIndex() {
        return index;
    }
}
