import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerConnection {

    public static void defaultConnection(){

        // JDBC driver name and database URL
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://root@localhost:3306";
        //  Database credentials
        final String USER = "root";
        final String PASS = "<password>";

        try {
            //Register jdbc driver
            Class.forName(JDBC_DRIVER);

            //Connect to server
            System.out.println("Connecting to server...");
            Connection database = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = database.createStatement();
            System.out.println("Connected to "+ DB_URL+"...");
            //Select right database
            statement.execute("USE warehouse");
            System.out.println("Database 'warehouse' selected successfully.");

        }catch (SQLException exception){
            AlertWindow.display("Could not establish connection\n" +
                    "to SQL database.");
            exception.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
