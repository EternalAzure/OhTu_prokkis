package fi.orderly.logic;

import fi.orderly.ui.Hub;
import fi.orderly.ui.Login;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.sql.*;

/**
 * Tarjoaa logiikan sisäänkirjautumisikkunalle.
 */
public class LoginController {

    final private Hub hub;

    Connection connection = ServerConnection.createConnection("login");
    public LoginController(Hub hub) {
        this.hub = hub;
    }

    /**
     * Salasanallinen kirjautuminen.
     * @param username käyttäjänimi
     * @param password salasana
     */
    public void login(String username, String password) {
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT password FROM users WHERE BINARY name=?");
            sql.setString(1, username);
            ResultSet resultSet = sql.executeQuery();
            resultSet.next();
            String pw = resultSet.getString("password");

            if (pw.equals(password)) {
                hub.start(new Stage());
                Login.window.close();
            }
        } catch (SQLException e) {
            //ignore
        }
    }

    /**
     * Salasanaton kirjautuminen testauksen nopeuttamiseksi.
     */
    public void passwordlessLogin() {
        //For testing purposes
        new Hub().start(new Stage());
        Login.window.close();
    }

    public void exit() {
        Platform.exit();
    }
}
