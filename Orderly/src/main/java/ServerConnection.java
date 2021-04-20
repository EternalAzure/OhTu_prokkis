import java.sql.*;

public class ServerConnection {

    public static final String database = "warehouse";
    public static final String testDatabase = "warehousetest";
    public static Statement createConnection(String database){

        // JDBC driver & database URL
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://visitor@localhost:3306";

        final String USER = "visitor"; //CREDENTIALS
        final String PASS = "y";

        try {
            //Connect to server
            Connection server = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = server.createStatement();
            //System.out.println("Connected to "+ DB_URL+"...");

            //Select right database
            statement.execute("USE "+database);
            System.out.println("Selected: "+database);
            return statement;

        }catch (SQLException exception){
            AlertWindow.display("Could not establish connection\n" +
                    "to SQL database.");
            exception.printStackTrace();
        }
        return null;
    }
}
