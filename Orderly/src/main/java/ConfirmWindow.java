import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfirmWindow {

    static Boolean answer;

    public static boolean display(String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);

        Label label = new Label(message);
        label.setPadding(new Insets(20,20,20,20));
        Button yes = new Button("Yes");
        Button no = new Button("No");

        yes.setOnAction(event -> {
            answer = true;
            window.close();
        });
        no.setOnAction(event -> {
            answer = false;
            window.close();
        });

        BorderPane bp = new BorderPane();
        VBox vBox = new VBox(10);
        HBox hBox = new HBox(10);

        vBox.setAlignment(Pos.CENTER);
        bp.setCenter(vBox);
        hBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(label, hBox);
        hBox.getChildren().add(yes);
        hBox.getChildren().add(no);
        bp.setStyle("-fx-background-color: SlateGrey");

        Scene scene = new Scene(bp, 300, 200);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
