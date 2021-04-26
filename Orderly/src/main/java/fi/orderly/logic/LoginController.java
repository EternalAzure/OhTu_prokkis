package fi.orderly.logic;

import fi.orderly.ui.AlertWindow;
import fi.orderly.ui.Hub;
import fi.orderly.ui.Login;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.awt.*;
import java.net.URI;
import java.sql.Statement;

public class LoginController {

    final private Hub hub;
    Utils utils;
    Statement statement = ServerConnection.createConnection("login");
    public LoginController(Hub hub) {
        this.hub = hub;
        utils = new Utils(statement);
    }

    public void login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return;
        }
        String sql = "SELECT name, password, role FROM users WHERE name LIKE '" + username + "'";
        String name = utils.getResultString(sql, "name");
        String pw = utils.getResultString(sql, "password");
        String role = utils.getResultString(sql, "role"); //waits to be used

        System.out.println(username + "::" + name);
        System.out.println(password + "::" + pw);
        if (name.isEmpty()) {
            AlertWindow.display("Incorrect name");
        }
        if (pw.equals(password)) {
            hub.start(new Stage());
            Login.window.close();
        } else {
            AlertWindow.display("Incorrect password");
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
