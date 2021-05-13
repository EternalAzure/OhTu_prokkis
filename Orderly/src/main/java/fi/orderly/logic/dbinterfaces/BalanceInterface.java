package fi.orderly.logic.dbinterfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Sisältää SQL komentoja. Metodit, jotka ottavat parametreja eivät käsittele virheitä.
 * Käsittelee taulua balance.
 */
public class BalanceInterface {

    Connection connection;
    public BalanceInterface(Connection connection) {
        this.connection = connection;
    }

    /**
     * Yrittää lisätä tauluun rivin.
     * @param roomId huoneen id (vierasavain)
     * @param productId tuotteen id (vierasavain)
     * @param batch tuotteen erö
     * @param amount määrä
     * @throws SQLException annettu parametri on virheellinen
     */
    public void insertBalance(int roomId, int productId, int batch, double amount) throws SQLException {
        String insert = "INSERT INTO balance (room_id, product_id, batch, amount) VALUES (?, ?, ?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setInt(1, roomId);
        sql.setInt(2, productId);
        sql.setInt(3, batch);
        sql.setDouble(4, amount);
        sql.executeUpdate();
    }

    /**
     * Yrittää päivittää taulun riviä.
     * @param roomId huoneen id (vierasavain)
     * @param productId tuotteen id (vierasavain)
     * @param batch tuotteen erä
     * @param amount määrä
     * @throws SQLException annettu parametri on virheellinen
     */
    public void updateBalance(int roomId, int productId, int batch, double amount) throws SQLException {
        String update = "UPDATE balance SET amount=? WHERE room_id=? AND product_id=? AND batch=?";
        PreparedStatement sql = connection.prepareStatement(update);
        sql.setDouble(1, amount);
        sql.setInt(2, roomId);
        sql.setInt(3, productId);
        sql.setInt(4, batch);
        sql.executeUpdate();
    }

    /**
     * Yritää poistaa taulusta rivin.
     * ON DELETE CASCADE!
     * @param roomId huoneen id (vierasavain)
     * @param productId tuotteen id (vierasavain)
     * @param batch tuotteen erä
     * @throws SQLException annettu parametri on virheellinen
     */
    public void deleteBalance(int roomId, int productId, int batch) throws SQLException {
        String delete = "DELETE FROM balance WHERE room_id=? AND product_id=? AND batch=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, roomId);
        sql.setInt(2, productId);
        sql.setInt(3, batch);
        sql.executeUpdate();
    }

    /**
     * Yrittää kysellä saldoa.
     * @param roomId huoneen id (vierasavain)
     * @param productId tuotteen id (vierasavain)
     * @param batch tuotteen erä
     * @return saldo
     * @throws SQLException annettu parametri on virheellinen
     */
    public double queryBalance(int roomId, int productId, int batch) throws SQLException {
        String select = "SELECT amount FROM balance WHERE room_id=? AND product_id=? AND batch=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, roomId);
        sql.setInt(2, productId);
        sql.setInt(3, batch);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getDouble("amount");
    }

    /**
     * Yrittää kysellä löytyykö taulusta riviä.
     * @param roomId huoneen id (vierasavain)
     * @param productId tuotteen id (vierasavain)
     * @param batch tuotteen erä
     * @return löytyikö riviä
     * @throws SQLException annettu parametri on virheellinen
     */
    public boolean foundBalance(int roomId, int productId, int batch) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE room_id=? AND product_id=? AND batch=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, roomId);
        sql.setInt(2, productId);
        sql.setInt(3, batch);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    /**
     * Yrittää kysellä löytyykö taulusta erää.
     * @param batchNumber eränumero
     * @return löytyikö eränumero
     * @throws SQLException annettu parametri on virheellinen
     */
    public boolean foundBatch(int batchNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE batch=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, batchNumber);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        boolean result = resultSet.getInt("COUNT(*)") > 0;
        resultSet.close();
        return result;
    }

    /**
     * Yrittää kysellä löytyykö taulusta huonetta
     * @param id huoneen id (vierasavain)
     * @return löytyikö huonetta
     * @throws SQLException annettu parametri on virheellinen
     */
    public boolean foundRoom(int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM balance WHERE room_id=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setInt(1, id);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        boolean result = resultSet.getInt("COUNT(*)") > 0;
        resultSet.close();
        return result;
    }

    /**
     * Palauttaa taulun rivien lukumäärän, joissa määrä on nolla.
     * @return lukumäärä
     */
    public int numberOfZero() {
        ResultSet resultSet = null;
        try {
            String query = "SELECT COUNT(*) FROM balance WHERE amount=0";
            PreparedStatement sql = connection.prepareStatement(query);
            resultSet = sql.executeQuery();
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        } catch (SQLException e) {
            return 0;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                //ignore
            }
        }
    }

    /**
     * Palauttaa taulun rivien lukumäärän.
     * @return lukumäärä
     */
    public int size() {
        ResultSet resultSet = null;
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT COUNT(*) FROM balance");
            resultSet = sql.executeQuery();
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                //ignore
            }
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
            PreparedStatement c = connection.prepareStatement("TRUNCATE TABLE balance;");
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
