package fi.orderly.logic;

import fi.orderly.ui.AlertWindow;
import java.sql.*;

/**
 * Luokan tarkoitus on ottaa yhteystietokantaan.
 */
public class ServerConnection {

    public static String DATABASE = "warehouse";
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
     * @param dbUrl serverin url osoite
     * @return yhteys
     */
    public static Connection customConnection(String dbUrl) {
        String driver = "jdbc:sqlite:";
        String url = driver + dbUrl;
        try {
            Connection connection = DriverManager.getConnection(url);
            return connection;

        } catch (SQLException exception) {
            AlertWindow.display("Could not establish connection\n" +
                    "to SQL database.");
        }

        TEST_DATABASE = "test.db";
        DATABASE = dbUrl;
        return null;
    }
}
