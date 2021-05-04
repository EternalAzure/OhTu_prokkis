package fi.orderly.logic;

import fi.orderly.ui.AlertWindow;
import fi.orderly.ui.Hub;
import fi.orderly.ui.Login;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.awt.*;
import java.net.URI;
import java.sql.*;

public class LoginController {

    final private Hub hub;

    //Statement statement = ServerConnection.createConnection("login");
    Connection connection = ServerConnection.createConnection("login");
    public LoginController(Hub hub) {
        this.hub = hub;
        //utils = new Utils(statement);
    }

    public void login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return;
        }
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT name, password, role FROM users WHERE name=?");
            sql.setString(1, username);
            ResultSet resultSet = sql.executeQuery();
            String name = resultSet.getString("name");
            String pw = resultSet.getString("password");
            String role = resultSet.getString("role"); //waits to be implemented

            if (name == null) {
                AlertWindow.display("Incorrect name");
                return;
            }
            if (pw.equals(password)) {
                hub.start(new Stage());
                Login.window.close();
            } else {
                AlertWindow.display("Incorrect password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void passwordlessLogin() {
        //For testing purposes
        new Hub().start(new Stage());
        Login.window.close();
    }

    public void exit() {
        Platform.exit();
    }

    public void link() {
        try {
            Desktop desk = Desktop.getDesktop();
            desk.browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
        } catch (Exception e) {
            AlertWindow.display("Deprecated URL. Link could not be opened");
        }
    }
}
