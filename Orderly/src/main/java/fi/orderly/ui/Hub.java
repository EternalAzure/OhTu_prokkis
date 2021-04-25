package fi.orderly.ui;
import fi.orderly.logic.HubController;
import fi.orderly.logic.ServerConnection;
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
    final private Statement statement = ServerConnection.createConnection(ServerConnection.DATABASE);
    final private BorderPane workspaceParent = new BorderPane();

    final private WorkSpaces workSpaces = new WorkSpaces(statement);
    final private HubController hubController = new HubController(statement);

    @Override
    public void start(Stage window){
        hub = window;
        hub.initStyle(StageStyle.UNDECORATED);
        hub.setMinHeight(400);
        hub.setMinWidth(600);
        hub.setTitle("Orderly - fi.orderly.dao.ui.Hub");
        BorderPane borderPane = new BorderPane();
        VBox leftMenu = new VBox();

        //region Initialize and set UI elements
        MenuBar menuBar = new MenuBar();
        Label logout = new Label("logout");
        logout.setId("menu-bar-logout");
        Label exit = new Label("exit");
        exit.setId("menu-bar-exit");
        HBox hBox = new HBox(menuBar, logout, exit);
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(logout, Priority.NEVER);
        HBox.setHgrow(exit, Priority.NEVER);

        Menu testing = new Menu("Testing");
        Menu terminal = new Menu("Receiving terminal");
        MenuItem create = new MenuItem("Create shipment");
        MenuItem delete = new MenuItem("Delete shipment");
        terminal.getItems().addAll(
                create, delete
        );

        testing.getItems().addAll(terminal);

        Menu dataBase = new Menu("Database");
        MenuItem truncateRooms = new MenuItem("Truncate 'rooms'");
        MenuItem truncateProducts = new MenuItem("Truncate 'products'");
        MenuItem truncateBalance = new MenuItem("Truncate 'balance'");
        MenuItem truncateShipments = new MenuItem("Truncate 'shipments'");
        dataBase.getItems().addAll(truncateRooms, truncateProducts, truncateBalance, truncateShipments);

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
        Hyperlink collect = new Hyperlink("Send shipment");

        menuBar.getMenus().addAll(testing, dataBase); //MENUBAR
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

        create.setOnAction(event -> hubController.createTestShipment()); //SAME AS...
        delete.setOnAction(event -> hubController.deleteTestShipment()); //THIS TOO...
        truncateRooms.setOnAction(event -> hubController.truncateRooms());
        truncateProducts.setOnAction(event -> hubController.truncateProducts());
        truncateBalance.setOnAction(event -> hubController.truncateBalance());
        truncateShipments.setOnAction(event -> hubController.truncateShipments());
        logout.setOnMouseClicked(event -> HubController.logout(window));
        exit.setOnMouseClicked(event -> HubController.exit());

        addRoom.setOnAction(event -> setWorkSpace(workSpaces.addRoomWorkspace()));
        addProduct.setOnAction(event -> setWorkSpace(workSpaces.addProductWorkspace()));
        removeRoom.setOnAction(event -> setWorkSpace(workSpaces.removeRoomWorkspace()));
        removeProduct.setOnAction(event -> setWorkSpace(workSpaces.removeProductWorkspace()));
        changeBalance.setOnAction(event -> setWorkSpace(workSpaces.changeBalanceWorkspace()));
        transfer.setOnAction(event -> setWorkSpace(workSpaces.transferWorkspace()));
        receive.setOnAction(event -> setWorkSpace(workSpaces.receiveWorkspace(this)));
        collect.setOnAction(event -> setWorkSpace(workSpaces.collectWorkspace(this)));

        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add("hub.css");
        hub.setScene(scene);
        hub.show();

        leftMenu.setMinWidth(hub.getWidth()/3);
        workspaceParent.setMinWidth(hub.getWidth()/3*2);
        workspaceParent.setStyle("-fx-background-color: White");
    }


    public void setWorkSpace(VBox workSpaceLayout){
        workspaceParent.getChildren().clear();
        workspaceParent.setCenter(workSpaceLayout);
    }
}
