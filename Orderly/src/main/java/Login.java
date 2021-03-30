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
    public Button exit;
    public ImageView forkliftView;
    public ImageView logoView;
    private Image image;
    public TextField username;
    public PasswordField password;
    public Button login;
    public Hyperlink credit;
    public Image logo;
    public static void main(String[] args) throws Exception{ launch(args);}


    @Override
    public void start(Stage loginWindow) throws Exception{
        window = loginWindow;
        window.setResizable(false);
        window.setTitle("Orderly");
        window.initStyle(StageStyle.UNDECORATED);

        //region Pane set up
        BorderPane layout = new BorderPane();
        AnchorPane leftPane = new AnchorPane();
        AnchorPane rightPane = new AnchorPane();
        layout.setLeft(leftPane);
        layout.setRight(rightPane);
        leftPane.setPrefHeight(400);
        leftPane.setPrefWidth(300);
        rightPane.setPrefHeight(400);
        rightPane.setPrefWidth(300);
        //endregion
        //region Initialize UI elements
        forkliftView = new ImageView();
        logoView = new ImageView();
        image = new Image("forklift.png");
        exit = new Button("X");
        username = new TextField();
        password = new PasswordField();
        login = new Button("Login");
        credit = new Hyperlink("Image source");
        logo = new Image("ORDERLY_Logo.png");
        //endregion
        //region Set children
        leftPane.getChildren().addAll(forkliftView, logoView);
        rightPane.getChildren().addAll(exit, username, password, login, credit);
        //endregion
        //region Set UI positions
        forkliftView.setLayoutX(75);
        forkliftView.setLayoutY(150);
        username.setLayoutX(50);
        username.setLayoutY(130);
        password.setLayoutX(50);
        password.setLayoutY(200);
        login.setLayoutX(115);
        login.setLayoutY(270);
        credit.setLayoutX(107);
        credit.setLayoutY(300);
        exit.setLayoutX(260);
        exit.setLayoutY(10);
        logoView.setLayoutX(150);
        layout.setLayoutY(200);
        //endregion

        leftPane.setId("blue-pane");
        forkliftView.setImage(image);
        forkliftView.setPreserveRatio(true);
        forkliftView.setFitHeight(100);

        logoView.setImage(logo);
        logoView.setPreserveRatio(true);
        logoView.setFitHeight(80);

        username.setPromptText("Username");
        password.setPromptText("Password");
        exit.setId("exit");

        LoginController logInCont = new LoginController();
        login.setOnAction(event -> logInCont.login());
        exit.setOnAction(event -> logInCont.exit());
        credit.setOnAction(event -> logInCont.link());

        Scene scene = new Scene(layout, 600, 400);
        scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
        window.setScene(scene);
        window.show();
    }
}
