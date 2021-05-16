package fi.orderly.logic.dbinterfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Luokan tarkoitus on tarjota pääsy tietokannan tauluun deliveries.
 * Sisältää SQL komentoja. Metodit, jotka ottavat parametreja eivät käsittele virheitä.
 */
public class DeliveriesInterface {

    Connection connection;
    public DeliveriesInterface(Connection connection) {
        this.connection = connection;
    }

    /**
     * Yrittää lisätä tauluun rivin.
     * @param number toimitusnumero
     * @param productId tuotteen id (vierasavain)
     * @param amount määrä
     * @throws SQLException annettu parametri on virheellinen
     */
    public void insertDelivery(int number, int productId, double amount) throws SQLException {
        String insert = "INSERT INTO deliveries (number, product_id, amount) VALUES (?, ?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setInt(1, number);
        sql.setInt(2, productId);
        sql.setDouble(3, amount);
        sql.executeUpdate();
    }

    /**
     * Yrittää poistaa taulusta rivin.
     * @param number toimitusnumero
     * @throws SQLException annettu parametri on virheellinen
     */
    public void deleteDelivery(int number) throws SQLException {
        String delete = "DELETE FROM deliveries WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, number);
        sql.executeUpdate();
    }

    /**
     * Yrittää kysellä löytyykö taulusta rivi.
     * @param number toimitusnumero
     * @param productId tuotteen id (vierasavain)
     * @return löytyikö rivi
     * @throws SQLException annettu parametri on virheellinen
     */
    public boolean foundDelivery(int number, int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM deliveries WHERE number=? AND product_id=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, number);
        sql.setInt(2, productId);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    /**
     * Yrittää kysellä löytyykö taulusta toimitusnumero.
     * @param number toimitusnumero
     * @return löytyikö toimitusnumero
     * @throws SQLException annettu parametri on virheellinen
     */
    public boolean foundDelivery(int number) throws SQLException {
        String query = "SELECT COUNT(*) FROM deliveries WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, number);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    /**
     * Yrittää kysellä rivien lukumäärää annetulla toimitusnumerolla.
     * Käytetään vain yksikkötestauksessa. Muuten käytössä foundDelivery().
     * @param number toimitusnumero
     * @return lukumäärä
     * @throws SQLException annettu parametri on virheellinen
     */
    public int numberOfDeliveries(int number) throws SQLException {
        String query = "SELECT COUNT(*) FROM deliveries WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, number);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    /**
     * Yrittää palauttaa id, joka löytyy annetuilla parametreilla.
     * @param number lähtevän toimituksen numero
     * @param productId tuotteen id (vierasavain)
     * @return id
     * @throws SQLException annettu parametri on väärä
     */
    public int findId(int number, int productId) throws SQLException {
        String select = "SELECT id FROM deliveries WHERE number=? AND product_id=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, number);
        sql.setInt(2, productId);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("id");
    }

    /**
     * Yrittää palauttaa annetusta id:stä seuraavat 50 id:tä
     * @param id lähtökohta
     * @return taulukko jossa id:t
     * @throws SQLException annettu parametri on väärä
     */
    public int[] load50(int id) throws SQLException {
        String query = "SELECT id FROM deliveries WHERE id>? LIMIT 50";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, id);
        ResultSet resultSet = sql.executeQuery();
        int[] list = new int[50];
        int i = 0;
        while (resultSet.next()) {
            list[i] = resultSet.getInt(1);
            i++;
        }
        return list;
    }

    /**
     * Palauttaa taulun rivien lukumäärän.
     * @return lukumäärä
     */
    public int size() {
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT COUNT(*) FROM deliveries");
            ResultSet resultSet = sql.executeQuery();
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Tyhjentää taulun ja nollaa indeksit.
     */
    public void truncate() {
        try {
            PreparedStatement a = connection.prepareStatement("BEGIN;");
            PreparedStatement b = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;");
            PreparedStatement c = connection.prepareStatement("TRUNCATE TABLE deliveries;");
            PreparedStatement d = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1;");
            PreparedStatement e = connection.prepareStatement("COMMIT;");
            a.execute();
            b.execute();
            c.executeUpdate();
            d.execute();
            e.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
