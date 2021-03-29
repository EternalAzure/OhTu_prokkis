import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertWindow {

    public static void display(String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label(message);
        Button button = new Button("Close");
        button.setOnAction(event -> window.close());
        VBox vBox = new VBox();
        vBox.getChildren().add(label);
        vBox.getChildren().add(button);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox, 200, 200);
        window.setScene(scene);
        window.showAndWait();
    }
}
