package logic;

import ui.Hub;
import ui.Login;
import logic.AlertWindow;
import logic.ServerConnection;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.awt.*;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {

    private Hub hub;
    public LoginController(Hub hub){
        this.hub = hub;
    }

    public void login( String username, String password){
        if(username.isEmpty()||password.isEmpty()) return;
        try {
            String sql = "SELECT name, password, role FROM user WHERE name LIKE '"+ username+"';";
            Statement statement = ServerConnection.createConnection("login");
            ResultSet queryResult = statement.executeQuery(sql);
            while (queryResult.next()){
                String name = queryResult.getString("name");
                String pw = queryResult.getString("password");
                String role = queryResult.getString("role"); //waits to be used
                if (name.isEmpty()) AlertWindow.display("Incorrect name");
                if (pw.equals(password)){
                    hub.start(new Stage());
                    Login.window.close();
                }else{ AlertWindow.display("Incorrect password");}
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void passwordlessLogin(){
        //For testing purposes
        new Hub().start(new Stage());
        Login.window.close();
    }

    public void exit(){
        Platform.exit();
    }

    public void link(){
        try {
            Desktop desk = Desktop.getDesktop();
            desk.browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
        }catch (Exception e){
            AlertWindow.display("Deprecated URL. Link could not be opened");
        }

    }

}
