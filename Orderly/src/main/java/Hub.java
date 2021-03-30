import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Hub  extends Application {

    public static Stage hub;
    @Override
    public void start(Stage window){
        hub = window;
        //region Set up
        hub.initStyle(StageStyle.UNDECORATED);
        hub.setMinHeight(400);
        hub.setMinWidth(600);
        hub.setTitle("Orderly - Hub");
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add("hub.css");
        window.setScene(scene);
        VBox leftVBox = new VBox();
        AnchorPane rightPane = new AnchorPane();
        //endregion
        //region Menubar
        MenuBar menuBar = new MenuBar();
        Button logout = new Button("Logout");
        Button exit = new Button("Exit");
        HBox hBox = new HBox(menuBar, logout, exit);
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(logout, Priority.NEVER);
        HBox.setHgrow(exit, Priority.NEVER);
        //endregion
        //region Panes
        borderPane.setLeft(leftVBox);
        borderPane.setTop(hBox);
        borderPane.setRight(rightPane);

        leftVBox.setId("blue-Vbox");
        leftVBox.setSpacing(10);
        leftVBox.setPadding(new Insets(10,10,10,10));
        leftVBox.setPrefSize(400, 750);
        leftVBox.setMinWidth(300);

        rightPane.setId("azure");
        rightPane.setMinWidth(300);
        //endregion
        //region Initialize UI elements
        Menu colours = new Menu("Colours");
        colours.getItems().addAll(
                new MenuItem("red"),
                new MenuItem("white"),
                new MenuItem("blue")
        );
        Menu numbers = new Menu("Numbers");
        numbers.getItems().addAll(
                new MenuItem("one"),
                new MenuItem("two"),
                new MenuItem("three")
        );
        Label label = new Label("Welcome to task view");
        //endregion
        //region Set children
        menuBar.getMenus().addAll(colours, numbers);
        leftVBox.getChildren().addAll(label);
        //endregion
        //region Set UI positions
        logout.setStyle("-fx-padding-left: 200px");
        //endregion

        logout.setOnAction(event -> HubController.logout(window));
        exit.setOnAction(event -> HubController.exit());


        hub.show();

    }
}
