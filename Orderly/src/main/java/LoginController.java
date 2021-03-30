import javafx.application.Platform;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LoginController {

    public void login() {
        new Hub().start(new Stage());
        Login.window.close();
    }

    public void exit(){
        Platform.exit();
    }

    public void link(){
        try {
            Desktop desk = Desktop.getDesktop();
            desk.browse(new URI("https://www.onlinewebfonts.com/icon/406280"));
        }catch (Exception e){
            AlertWindow.display("Wrong URL. Link could not be opened");
        }

    }

}
