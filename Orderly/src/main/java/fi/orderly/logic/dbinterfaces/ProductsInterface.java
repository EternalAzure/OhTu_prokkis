package fi.orderly.logic.dbinterfaces;
import java.sql.*;

/**
 * Sisältää SQL komentoja. Metodit, jotka ottavat parametreja eivät käsittele virheitä.
 * Käsittelee taulua products.
 */
public class ProductsInterface {

    Connection connection;
    public ProductsInterface(Connection connection) {
        this.connection = connection;
    }

    /**
     * Yrittää lisätä tauluun rivin.
     * @param product tuotteen nimi
     * @param code tuotteen koodi
     * @param unit mittayksikkö
     * @param temperature tavoite lämpötila
     * @param roomId huoneen id (vierasavain)
     * @throws SQLException annettu parametri on virheellinen
     */
    public void insertProduct(String product, int code, String unit, double temperature, int roomId) throws SQLException {
        String insert = "INSERT INTO products (product, code, unit, temperature, room_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setString(1, product);
        sql.setInt(2, code);
        sql.setString(3, unit);
        sql.setDouble(4, temperature);
        sql.setInt(5, roomId);
        sql.executeUpdate();
    }

    /**
     * Yrittää lisätä tuluun rivin.
     * @param product tuotteen nimi
     * @param code tuotteen koodi
     * @param unit mittayksikkö
     * @param roomId huoneen id (vierasavain)
     * @throws SQLException annettu parametri on virheellinen
     */
    public void insertProduct(String product, int code, String unit, int roomId) throws SQLException {
        String insert = "INSERT INTO products (product, code, unit, room_id) VALUES (?, ?, ?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setString(1, product);
        sql.setInt(2, code);
        sql.setString(3, unit);
        sql.setInt(4, roomId);
        sql.executeUpdate();
    }

    /**
     * Yrittää poistaa taulusta rivin.
     * @param product tuotteen nimi
     * @throws SQLException annettu parametri on virheellinen
     */
    public void deleteProduct(String product) throws SQLException {
        String delete = "DELETE FROM products WHERE product=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setString(1, product);
        sql.executeUpdate();
    }

    /**
     * Ytittää poistaa taulusta rivin.
     * @param code tuotteen koodi
     * @throws SQLException annettu parametri on virheellinen
     */
    public void deleteProduct(int code) throws SQLException {
        String delete = "DELETE FROM products WHERE code=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, code);
        sql.executeUpdate();
    }

    /**
     * Yrittää kysellä löytyykö taulusta tuotetta.
     * @param name tuotteen nimi
     * @return löytyikö tuote
     * @throws SQLException annettu parametri on virheellinen
     */
    public boolean foundProduct(String name) throws SQLException {
        String select = "SELECT COUNT(*) FROM products WHERE product=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setString(1, name);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    /**
     * Yrittää kysellä löytyykö taulusta tuotetta.
     * @param code tuotteen koodi
     * @return löytyikö tuotetta
     * @throws SQLException annettu parametri on virheellinen
     */
    public boolean foundProduct(int code) throws SQLException {
        String select = "SELECT COUNT(*) FROM products WHERE code=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, code);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)") > 0;
    }

    /**
     * Yrittää löytää id nimen perusteella.
     * @param product tuotteen nimi
     * @return id
     * @throws SQLException annettu parametri on virheellinen
     */
    public int findIdByName(String product) throws SQLException {
        String select = "SELECT id FROM products WHERE product=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setString(1, product);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("id");
    }

    /**
     * Yrittää löytää id koodin perusteella.
     * @param code tuotteen koodi
     * @return id
     * @throws SQLException annettu parametri on virheellinen
     */
    public int findIdByCode(int code) throws SQLException {
        String select = "SELECT id FROM products WHERE code=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, code);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("id");
    }

    /**
     * Yrittää löytää koodin id perusteella.
     * @param id id
     * @return koodi
     * @throws SQLException annettu parametri on virheellinen
     */
    public int findCodeById(int id) throws SQLException {
        String select = "SELECT code FROM products WHERE id=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("code");
    }

    public int[] load50(int id) throws SQLException {
        String query = "SELECT id FROM products WHERE id>? LIMIT 50";
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
            PreparedStatement sql = connection.prepareStatement("SELECT COUNT(*) FROM products");
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
            PreparedStatement b = connection.prepareStatement("DELETE FROM rooms;");
            PreparedStatement c = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;");
            PreparedStatement d = connection.prepareStatement("TRUNCATE TABLE products;");
            PreparedStatement e = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1;");
            PreparedStatement f = connection.prepareStatement("COMMIT;");
            a.execute();
            b.executeUpdate();
            c.execute();
            d.execute();
            e.execute();
            f.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
