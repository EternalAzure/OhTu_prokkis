import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;
    Scene scene;
    TaskWindow taskWindow;

    public static void main(String[] args){
        launch(args);
    }


    @Override
    public void start(Stage loginWindow) { //Could throw Exception
        taskWindow = new TaskWindow();
        window = loginWindow;
        window.setTitle("Orderly");
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setHgap(10);
        layout.setVgap(8);

        Button loginB = new Button("Login");
        loginB.setOnAction(event -> {
            window.setScene(taskWindow.display(this));
        });
        GridPane.setConstraints(loginB, 1,2);

        Label usernameL = new Label("Username: ");
        GridPane.setConstraints(usernameL, 0,0);

        TextField usernameT = new TextField();
        usernameT.setPromptText("015");
        GridPane.setConstraints(usernameT, 1,0);

        Label passwordL = new Label("Password: ");
        GridPane.setConstraints(passwordL, 0,1);

        TextField passwordF = new TextField();
        passwordF.setPromptText("letmein");
        GridPane.setConstraints(passwordF, 1,1);

        layout.getChildren().addAll(loginB, usernameL, usernameT, passwordL, passwordF);

        scene = new Scene(layout, 300, 200);
        scene.getStylesheets().add("stylesheet.css");
        window.setScene(scene);
        window.show();
    }

    public void logout(){
        window.setScene(scene);
    }

}
