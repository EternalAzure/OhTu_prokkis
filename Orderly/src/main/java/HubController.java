import javafx.application.Platform;
import javafx.stage.Stage;

public class HubController {

    public static void logout(Stage currentWindow){
        try {
            new Login().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentWindow.close();
    }

    public static void exit(){
        Platform.exit();
    }


}
