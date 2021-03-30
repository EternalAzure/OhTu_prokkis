import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class HubController {

    public Button logout;
    public Hyperlink link1;

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

    public void selectTask(){
        System.out.println("Selecting task");
    }

    public void addTask(){

    }

    public void insertTasks(VBox layout){

    }

}
