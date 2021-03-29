import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmWindow {

    static Boolean answer;

    public static boolean display(String title, String message){
        Stage window = new Stage();
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label(message);
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

        VBox vBox = new VBox();
        vBox.getChildren().add(label);
        vBox.getChildren().add(yes);
        vBox.getChildren().add(no);

        yes.setAlignment(Pos.CENTER_LEFT);
        no.setAlignment(Pos.CENTER_RIGHT);
        label.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(vBox, 200, 200);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
