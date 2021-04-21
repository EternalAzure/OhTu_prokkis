package logic;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertWindow {

    public static void display(String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);

        BorderPane bp = new BorderPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        bp.setCenter(vBox);

        Label label = new Label(message);
        label.setPadding(new Insets(20, 20, 20, 20));
        Button exit = new Button("exit");

        vBox.getChildren().addAll(label, exit);

        exit.setOnAction(event -> window.close());
        bp.setStyle("-fx-background-color: SlateGrey");

        Scene scene = new Scene(bp, 300, 200);
        window.setScene(scene);
        window.showAndWait();
    }
}
