package fi.orderly.ui;

import fi.orderly.logic.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login extends Application {

    public static Stage window;
    final private LoginController loginCont = new LoginController(new Hub());

    public static void main(String[] args){ launch(args);}


    @Override
    public void start(Stage loginWindow){
        window = loginWindow;
        window.setResizable(false);
        window.setTitle("Orderly");
        window.initStyle(StageStyle.UNDECORATED);

        //region UI
        BorderPane layout = new BorderPane();
        AnchorPane leftPane = new AnchorPane();
        AnchorPane rightPane = new AnchorPane();
        layout.setRight(rightPane);
        layout.setLeft(leftPane);
        leftPane.setMaxSize(300,400);
        leftPane.setMinSize(300,400);
        rightPane.setMinSize(300,400);
        rightPane.setMaxSize(33,400);

        ImageView userIconView = new ImageView();
        ImageView logoView = new ImageView();
        Image userIcon = new Image("user icon.png");
        Image logo = new Image("ORDERLY_Logo_fix.png");

        Button exit = new Button("X");
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        Button login = new Button("login");
        Hyperlink credit = new Hyperlink("Link for fun");

        leftPane.getChildren().addAll(logoView);
        rightPane.getChildren().addAll(exit, username, password, login, credit, userIconView);

        userIconView.setLayoutX(125);
        userIconView.setLayoutY(50);
        logoView.setLayoutX(-50);
        logoView.setLayoutY(60);

        login.setLayoutX(120);
        login.setLayoutY(270);

        credit.setLayoutX(107);
        credit.setLayoutY(300);

        exit.setLayoutX(260);
        exit.setLayoutY(10);

        username.setLayoutX(70);
        username.setLayoutY(130);

        password.setLayoutX(70);
        password.setLayoutY(200);

        leftPane.setId("blue-pane");
        userIconView.setImage(userIcon);
        userIconView.setPreserveRatio(true);
        userIconView.setFitHeight(50);

        logoView.setImage(logo);
        logoView.setPreserveRatio(true);
        logoView.setFitHeight(280);

        username.setPromptText("Username");
        password.setPromptText("Password");
        exit.setId("exit");
        //endregion

        //Actions
        login.setOnAction(event -> loginCont.passwordlessLogin()); //login(username.getText(), password.getText())
        exit.setOnAction(event -> loginCont.exit());
        credit.setOnAction(event -> loginCont.link());

        Scene scene = new Scene(layout, 600, 400);
        scene.getStylesheets().add("login.css");
        window.setScene(scene);
        window.show();
    }
}
