package fi.orderly.logic;

import fi.orderly.ui.AlertWindow;
import java.sql.*;

/**
 * Luokan tarkoitus on ottaa yhteystietokantaan.
 */
public class ServerConnection {

    public static final String DATABASE = "warehouse";
    public static String TEST_DATABASE = "test";

    /**
     * Oletus yhteys sqlite3 tietokantaan.
     * @param database tietokatatiedoston nimi
     * @return yhteys
     */
    public static Connection createConnection(String database) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:"+ database +".db");
            return connection;

        } catch (SQLException exception) {
            AlertWindow.display("Could not establish connection\n" +
                    "to SQL database.");
            System.out.println(exception.getMessage());
        } catch (Exception e) {
            System.out.println("Class not found for org.sqlite.JDBC");
            System.out.println("error: " + e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * Kun oletus tietokanta halutaan vaihtaa toiseen.
     * @param database tietokanta
     * @param dbUrl serverin url osoite
     * @param user käyttäjänimi
     * @param password salasana
     * @return yhteys
     */
    public static Connection customConnection(String database, String dbUrl, String user, String password, String testDatabaseName) {
        String driver = "jdbc:mysql://";
        String url = driver + dbUrl;
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);
            System.out.println("Connection created");
            return connection;

        } catch (SQLException exception) {
            AlertWindow.display("Could not establish connection\n" +
                    "to SQL database.");
        }

        TEST_DATABASE = testDatabaseName;
        return null;
    }
}
