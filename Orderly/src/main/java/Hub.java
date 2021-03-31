import javafx.application.Application;
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

        VBox leftVBox = new VBox();
        AnchorPane rightPane = new AnchorPane();
        //endregion
        //region Menubar
        MenuBar menuBar = new MenuBar();
        Button logout = new Button("Logout");
        logout.setMinWidth(30);
        Button exit = new Button("Exit");
        HBox hBox = new HBox(menuBar, logout, exit);
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(logout, Priority.NEVER);
        HBox.setHgrow(exit, Priority.NEVER);

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
        //endregion
        //region Panes
        borderPane.setLeft(leftVBox);
        borderPane.setTop(hBox);
        borderPane.setRight(rightPane);

        leftVBox.setId("blue-Vbox");
        leftVBox.setSpacing(10);
        leftVBox.setPadding(new Insets(10,10,10,10));
        leftVBox.setPrefSize(200, 400);
        leftVBox.setMinWidth(100);

        rightPane.setId("workspace");
        rightPane.setMinWidth(300);
        //endregion
        //region Initialize UI elements
        Hyperlink addRoom = new Hyperlink("Add a new room");
        Hyperlink addProduct = new Hyperlink("Add a new product");
        Hyperlink removeRoom = new Hyperlink("Remove a room");
        Hyperlink removeProduct = new Hyperlink("Remove a product");
        Hyperlink changeBalance = new Hyperlink("Change a balance");
        Hyperlink transfer = new Hyperlink("Transfer");
        Hyperlink receive = new Hyperlink("Receive cargo");
        Hyperlink collect = new Hyperlink("Collect (Beta)");
        //endregion
        //region Set children
        menuBar.getMenus().addAll(colours, numbers);
        leftVBox.getChildren().addAll(
                addRoom,
                addProduct,
                removeRoom,
                removeProduct,
                changeBalance,
                transfer,
                receive,
                collect
        );
        //endregion

        logout.setStyle("-fx-padding-left: 200px");

        logout.setOnAction(event -> HubController.logout(window));
        exit.setOnAction(event -> HubController.exit());

        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add("hub.css");
        hub.setScene(scene);
        hub.show();

        double menubarWidth = hub.getWidth() - logout.getWidth() - exit.getWidth();
        menuBar.setMaxWidth(menubarWidth);

        leftVBox.setMinWidth(hub.getWidth()/3);
        rightPane.setMinWidth(hub.getWidth()/3*2);
    }
}
