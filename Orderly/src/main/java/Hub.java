import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Hub  extends Application {

    public static Stage hub;
    public static final String database = "warehouse";
    private AnchorPane rightPane;

    WorkSpaces workSpaces = new WorkSpaces();

    @Override
    public void start(Stage window){
        hub = window;
        hub.initStyle(StageStyle.UNDECORATED);
        hub.setMinHeight(400);
        hub.setMinWidth(600);
        hub.setTitle("Orderly - Hub");
        BorderPane borderPane = new BorderPane();

        VBox leftVBox = new VBox();
        rightPane = new AnchorPane();

        //region Menubar
        MenuBar menuBar = new MenuBar();
        Button logout = new Button("Logout");
        logout.setMinWidth(30);
        Button exit = new Button("Exit");
        HBox hBox = new HBox(menuBar, logout, exit);
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(logout, Priority.NEVER);
        HBox.setHgrow(exit, Priority.NEVER);

        Menu settings = new Menu("settings");
        settings.getItems().addAll(
                new MenuItem("red"),
                new MenuItem("white"),
                new MenuItem("blue")
        );
        Menu numbers = new Menu("database");
        numbers.getItems().addAll(
                new MenuItem("one"),
                new MenuItem("two"),
                new MenuItem("three")
        );
        //endregion

        borderPane.setLeft(leftVBox);
        borderPane.setTop(hBox);
        borderPane.setRight(rightPane);

        leftVBox.setId("blue-Vbox");
        leftVBox.setSpacing(10);
        leftVBox.setPadding(new Insets(10,10,10,10));

        rightPane.setId("workspace");
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
        menuBar.getMenus().addAll(settings, numbers);
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

        logout.setOnAction(event -> HubController.logout(window));
        exit.setOnAction(event -> HubController.exit());

        addRoom.setOnAction(event -> setWorkSpace(workSpaces.addRoom()));
        addProduct.setOnAction(event -> setWorkSpace(workSpaces.addProduct()));
        removeRoom.setOnAction(event -> setWorkSpace(WorkSpaces.removeRoom()));
        removeProduct.setOnAction(event -> setWorkSpace(WorkSpaces.removeProduct()));
        changeBalance.setOnAction(event -> setWorkSpace(WorkSpaces.changeBalance()));
        transfer.setOnAction(event -> setWorkSpace(WorkSpaces.transfer()));
        receive.setOnAction(event -> setWorkSpace(WorkSpaces.receive()));
        collect.setOnAction(event -> setWorkSpace(WorkSpaces.collect()));

        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add("hub.css");
        hub.setScene(scene);
        hub.show();

        double menuBarWidth = hub.getWidth() - logout.getWidth() - exit.getWidth();
        menuBar.setMaxWidth(menuBarWidth);

        leftVBox.setMinWidth(hub.getWidth()/3);
        rightPane.setMinWidth(hub.getWidth()/3*2);

        ServerConnection.defaultConnection(database);
    }
    private void setWorkSpace(Pane workSpaceLayout){
        rightPane.getChildren().clear();
        rightPane.getChildren().add(workSpaceLayout);
        workSpaceLayout.setLayoutX((rightPane.getWidth()/3));
        workSpaceLayout.setLayoutY((rightPane.getHeight()/3));
    }
}
