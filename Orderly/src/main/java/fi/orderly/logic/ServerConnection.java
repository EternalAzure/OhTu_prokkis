package fi.orderly.logic;

import fi.orderly.ui.AlertWindow;

import java.sql.*;

public class ServerConnection {

    public static final String DATABASE = "warehouse";
    public static final String TEST_DATABASE = "test";

    public static Connection createConnection(String database) {
        final String dbUrl = "jdbc:mysql://mysql-demo-varasto.mysql.database.azure.com:3306/login?useSSL=true&requireSSL=false";
        final String user = "mefistofeles@mysql-demo-varasto";
        final String pass = "#demoDatabase30";

        try {
            Connection connection = DriverManager.getConnection(dbUrl, user, pass);
            Statement statement = connection.createStatement();
            statement.execute("USE " + database);
            return connection;

        } catch (SQLException exception) {
            AlertWindow.display("Could not establish connection\n" +
                    "to SQL database.");
        }
        return null;
    }

    public static Connection customConnection(String database, String dbUrl, String user, String password) {
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
        return null;
    }
}
