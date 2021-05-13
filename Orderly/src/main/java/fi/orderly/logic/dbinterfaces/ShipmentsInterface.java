package fi.orderly.logic.dbinterfaces;
import java.sql.*;

/**
 * Sisältää SQL komentoja. Metodit, jotka ottavat parametreja eivät käsittele virheitä.
 * Käsittelee taulua shipments.
 */
public class ShipmentsInterface {

    Connection connection;
    public ShipmentsInterface(Connection connection) {
        this.connection = connection;
    }

    /**
     * Yrittää lisätä tauluun rivin.
     * @param number toimitusnumero
     * @param productId tuotteen id (vierasavain)
     * @param batch eränumero
     * @param amount määrä
     * @throws SQLException annettu parametri on väärä
     */
    public void insertShipment(int number, int productId, int batch, double amount) throws SQLException {
        String insert = "INSERT INTO shipments (number, product_id, batch, amount) VALUES (?, ?, ?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setInt(1, number);
        sql.setInt(2, productId);
        sql.setInt(3, batch);
        sql.setDouble(4, amount);
        sql.executeUpdate();
    }

    /**
     * Yrittää poistaa taulusta rivin.
     * @param number toimitusnumero
     * @throws SQLException annettu parametri on väärä
     */
    public void deleteShipment(int number) throws SQLException {
        String delete = "DELETE FROM shipments WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, number);
        sql.executeUpdate();
    }

    /**
     * Yrittää kysellä löytyykö taulusta riviä.
     * @param number toimitusnumero
     * @param productId tuotteen id (vierasavain)
     * @param batch eränumero
     * @return löytyikö rivi
     * @throws SQLException annettu parametri on väärä
     */
    public boolean foundShipment(int number, int productId, int batch) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE number=? AND product_id=? AND batch=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, number);
        sql.setInt(2, productId);
        sql.setInt(3, batch);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    /**
     * Yrittää kysellä löytyykö taulusta toimitus.
     * @param number toimitusnumero
     * @return löytyikö toimitus
     * @throws SQLException annettu parametri on väärä
     */
    public boolean foundShipment(int number) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, number);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    /**
     * Yrittää kysellä löytyykö eränumeroa.
     * @param batchNumber eränumero
     * @return löytyikö eränumeroa
     * @throws SQLException annettu parametri on väärä
     */
    public boolean foundBatch(int batchNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE batch=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, batchNumber);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    /**
     * Yrittää kysellä taulusta rivien lukumäärää annetulla toimitusnumerolla.
     * @param number toimitusnumero
     * @return lukumäärä
     * @throws SQLException annettu parametri on väärä
     */
    public int numberOfShipment(int number) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE number=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, number);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    /**
     * Palauttaa taulusta rivien lukumäärän.
     * @return lukumäärä
     */
    public int size() {
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT COUNT(*) FROM shipments");
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
            PreparedStatement c = connection.prepareStatement("TRUNCATE TABLE shipments;");
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
