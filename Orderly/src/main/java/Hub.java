import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.sql.Statement;

public class Hub  extends Application {

    public static Stage hub;
    final private Statement statement = ServerConnection.createConnection(ServerConnection.database);
    WorkSpaces workSpaces = new WorkSpaces(statement);
    HubController hubController = new HubController(statement);
    BorderPane workspaceParent = new BorderPane();

    @Override
    public void start(Stage window){
        hub = window;
        hub.initStyle(StageStyle.UNDECORATED);
        hub.setMinHeight(400);
        hub.setMinWidth(600);
        hub.setTitle("Orderly - Hub");
        BorderPane borderPane = new BorderPane();
        VBox leftMenu = new VBox();

        //region Initialize and set UI elements
        MenuBar menuBar = new MenuBar();
        Button logout = new Button("Logout");
        logout.setMinWidth(30);
        logout.setMaxHeight(menuBar.getHeight());
        Button exit = new Button("Exit");
        exit.setMaxHeight(menuBar.getHeight());
        HBox hBox = new HBox(menuBar, logout, exit);
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(logout, Priority.NEVER);
        HBox.setHgrow(exit, Priority.NEVER);

        Menu shipment = new Menu("shipment");
        MenuItem create = new MenuItem("Create");
        MenuItem delete = new MenuItem("Delete");
        shipment.getItems().addAll(
                create, delete
        );
        create.setOnAction(event -> hubController.createTestShipment());
        delete.setOnAction(event -> hubController.deleteTestShipment());

        borderPane.setLeft(leftMenu);
        borderPane.setTop(hBox);
        borderPane.setRight(workspaceParent);

        leftMenu.setId("blue-menu");
        leftMenu.setSpacing(10);
        leftMenu.setPadding(new Insets(10,10,10,10));

        Hyperlink addRoom = new Hyperlink("Add new room");
        Hyperlink addProduct = new Hyperlink("Add new product");
        Hyperlink removeRoom = new Hyperlink("Remove room");
        Hyperlink removeProduct = new Hyperlink("Remove product");
        Hyperlink changeBalance = new Hyperlink("Change balance");
        Hyperlink transfer = new Hyperlink("Transfer");
        Hyperlink receive = new Hyperlink("Receive shipment");
        Hyperlink collect = new Hyperlink("Collect");

        menuBar.getMenus().addAll(shipment);
        leftMenu.getChildren().addAll(
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
        removeRoom.setOnAction(event -> setWorkSpace(workSpaces.removeRoom()));
        removeProduct.setOnAction(event -> setWorkSpace(workSpaces.removeProduct()));
        changeBalance.setOnAction(event -> setWorkSpace(workSpaces.changeBalance()));
        transfer.setOnAction(event -> setWorkSpace(workSpaces.transfer()));
        receive.setOnAction(event -> setWorkSpace(workSpaces.receive(this)));
        collect.setOnAction(event -> setWorkSpace(workSpaces.collect()));

        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add("hub.css");
        hub.setScene(scene);
        hub.show();

        double menuBarWidth = hub.getWidth() - logout.getWidth() - exit.getWidth();
        menuBar.setMaxWidth(menuBarWidth);
        leftMenu.setMinWidth(hub.getWidth()/3);
        workspaceParent.setMinWidth(hub.getWidth()/3*2);
        workspaceParent.setStyle("-fx-background-color: White");
    }


    public void setWorkSpace(VBox workSpaceLayout){
        workspaceParent.getChildren().clear();
        workspaceParent.setCenter(workSpaceLayout);
    }
}
