package fi.orderly.logic.dbinterfaces;
import java.sql.*;

/**
 * Luokan tarkoitus on tarjota pääsy tietokannan tauluun rooms.
 * Sisältää SQL komentoja. Metodit, jotka ottavat parametreja eivät käsittele virheitä.
 */
public class RoomsInterface {

    Connection connection;

    public RoomsInterface(Connection connection) {
        this.connection = connection;
    }

    /**
     * Yrittää lisätä tauluun rivin.
     * @param room huoneen nimi
     * @param temperature lämpötila
     * @throws SQLException annettu parametri on väärä
     */
    public void insertRoom(String room, double temperature) throws SQLException {
        String insert = "INSERT INTO rooms (room, temperature) VALUES (?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setString(1, room);
        sql.setDouble(2, temperature);
        sql.executeUpdate();
        sql.close();
    }

    /**
     * Yrittää lisätä tauluun rivin.
     * @param room huoneen nimi
     * @throws SQLException annettu parametri on väärä
     */
    public void insertRoom(String room) throws SQLException {
        String insert = "INSERT INTO rooms (room) VALUES (?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setString(1, room);
        sql.executeUpdate();
        sql.close();
    }

    /**
     * Yrittää poistaa taulusta rivin.
     * @param room huoneen nimi
     * @throws SQLException annettu parametri on väärä
     */
    public void deleteRoom(String room) throws SQLException {
        String delete = "DELETE FROM rooms WHERE room=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setString(1, room);
        sql.executeUpdate();
        sql.close();
    }

    /**
     * Yrittää palauttaa nimen ja lämpötilan annetulta riviltä
     * @param id rivi
     * @return taulukko, jossa result[0] = nimi ja result[1] = lämpötila
     * @throws SQLException annettu parametri on väärä
     */
    public String[] queryRoom(int id) throws SQLException {
        String select = "SELECT room, temperature FROM rooms WHERE id=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();

        String[] result = new String[2];
        result[1] = Double.toString(resultSet.getDouble("temperature"));
        if (resultSet.wasNull()) {
            result[1] = "-";
        }
        result[0] = resultSet.getString("room");
        sql.close();
        return result;

    }

    /**
     * Yrittää kysellö löytyykö huonetta.
     * @param name huoneen rivi
     * @return löytyikö huonetta
     * @throws SQLException annettu parametri on väärä
     */
    public boolean foundRoom(String name) throws SQLException {
        String query = "SELECT COUNT(*) FROM rooms WHERE room=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setString(1, name);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        boolean result = resultSet.getInt("COUNT(*)") > 0;
        sql.close();
        return result;
    }

    /**
     * Yrittää palauttaa id annetulla nimellä
     * @param room huoneen nimi
     * @return id
     * @throws SQLException annettu parametri on väärä
     */
    public int findIdByName(String room) throws SQLException {
        String select = "SELECT id FROM rooms WHERE room=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setString(1, room);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        int result = resultSet.getInt("id");
        sql.close();
        return result;
    }

    /**
     * Yrittää palauttaa annetusta id:stä seuraavat 50 id:tä
     * @param id lähtökohta
     * @return taulukko jossa id:t
     * @throws SQLException annettu parametri on virheellinen
     */
    public int[] load50(int id) throws SQLException {
        String query = "SELECT id FROM rooms WHERE id>? LIMIT 50";
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
            PreparedStatement sql = connection.prepareStatement("SELECT COUNT(*) FROM rooms");
            ResultSet resultSet = sql.executeQuery();
            resultSet.next();
            int result = resultSet.getInt("COUNT(*)");
            sql.close();
            return result;
        } catch (SQLException e) {
            System.out.println("RoomsInterface.size() failed:");
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
            PreparedStatement d = connection.prepareStatement("TRUNCATE TABLE rooms;");
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
