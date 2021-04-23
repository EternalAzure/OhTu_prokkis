package logic;

import java.sql.*;

public class ServerConnection {

    public static final String DATABASE = "warehouse";
    public static final String TEST_DATABASE = "warehousetest";

    public static Statement createConnection(String database) {
        final String dbUrl = "jdbc:mysql://visitor@localhost:3306";
        final String user = "visitor"; //CREDENTIALS
        final String pass = "y";

        try {
            //Connect to server
            Connection server = DriverManager.getConnection(dbUrl, user, pass);
            Statement statement = server.createStatement();

            //Select right database
            statement.execute("USE " + database);
            return statement;

        } catch (SQLException exception) {
            AlertWindow.display("Could not establish connection\n" +
                    "to SQL database.");
            exception.printStackTrace();
        }
        return null;
    }
}
