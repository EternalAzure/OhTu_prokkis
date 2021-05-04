package fi.orderly.ui;
import fi.orderly.logic.HubController;
import fi.orderly.logic.ServerConnection;
import fi.orderly.ui.tables.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;

public class Hub  extends Application {

    public static Stage hub;
    final private Connection connection = ServerConnection.createConnection(ServerConnection.DATABASE);
    final private BorderPane workspaceParent = new BorderPane();

    final private WorkSpaces workSpaces = new WorkSpaces(connection);
    final private HubController hubController = new HubController(connection);
    final private TableViewInfiniteScrolling roomsView = new RoomsTableView();
    final private TableViewInfiniteScrolling productView = new ProductsTableView();
    final private TableViewInfiniteScrolling balanceView = new BalanceTableView();
    final private TableViewInfiniteScrolling shipmentsView = new ShipmentsTableView();
    final private TableViewInfiniteScrolling deliveriesView = new DeliveriesTableView();

    @Override
    public void start(Stage window){
        hub = window;
        hub.getIcons().add(new Image("Orderly_icon_bw.png"));
        hub.initStyle(StageStyle.UNDECORATED);
        hub.setMinHeight(400);
        hub.setMinWidth(600);
        hub.setTitle("Orderly - Hub");
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

        Menu testing = new Menu("Populate");
        MenuItem createTestData = new MenuItem("Create test data");
        MenuItem generateProducts = new MenuItem("Generate 50 products");
        MenuItem generateRooms = new MenuItem("Generate 10 rooms");
        testing.getItems().addAll(createTestData, generateRooms, generateProducts);

        //TRUNCATE MENU
        Menu dataBase = new Menu("Truncate");
        MenuItem truncateRooms = new MenuItem("Truncate 'rooms'");
        MenuItem truncateProducts = new MenuItem("Truncate 'products'");
        MenuItem truncateBalance = new MenuItem("Truncate 'balance'");
        MenuItem truncateShipments = new MenuItem("Truncate 'shipments'");
        MenuItem truncateDeliveries = new MenuItem("Truncate 'deliveries'");
        MenuItem truncateAll = new MenuItem("Truncate all");
        dataBase.getItems().addAll(truncateAll, truncateRooms, truncateProducts, truncateBalance, truncateShipments, truncateDeliveries);

        //SHOW MENU
        Menu showTables = new Menu("Show tables");
        MenuItem showRooms = new MenuItem("Show rooms");
        MenuItem showProducts = new MenuItem("Show products");
        MenuItem showBalance = new MenuItem("Show balance");
        MenuItem showShipments = new MenuItem("Show shipments");
        MenuItem showDeliveries = new MenuItem("Show deliveries");
        showTables.getItems().addAll(showRooms, showProducts, showBalance, showShipments, showDeliveries);

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
        Hyperlink collectDelivery = new Hyperlink("Collect delivery");
        Hyperlink sendDelivery = new Hyperlink("Send delivery");
        Hyperlink newShipment = new Hyperlink("New shipment");
        Hyperlink newDelivery = new Hyperlink("New delivery");

        menuBar.getMenus().addAll(testing, dataBase, showTables); //MENUBAR
        leftMenu.getChildren().addAll(
                addRoom,
                addProduct,
                removeRoom,
                removeProduct,
                changeBalance,
                transfer,
                receive,
                collectDelivery,
                sendDelivery,
                newShipment,
                newDelivery
        );
        //endregion

        //MENUBAR SHOW
        showRooms.setOnAction(event -> roomsView.display(connection));
        showProducts.setOnAction(event -> productView.display(connection));
        showBalance.setOnAction(event -> balanceView.display(connection));
        showShipments.setOnAction(event -> shipmentsView.display(connection));
        showDeliveries.setOnAction(event -> deliveriesView.display(connection));

        //MENUBAR POPULATE
        createTestData.setOnAction(event -> hubController.createTestData()); //TESTING
        generateRooms.setOnAction(event -> hubController.generateRooms());
        generateProducts.setOnAction(event -> hubController.generateProducts());

        //MENUBAR TRUNCATE
        truncateRooms.setOnAction(event -> hubController.truncateRooms());
        truncateProducts.setOnAction(event -> hubController.truncateProducts());
        truncateBalance.setOnAction(event -> hubController.truncateBalance());
        truncateShipments.setOnAction(event -> hubController.truncateShipments());
        truncateDeliveries.setOnAction(event -> hubController.truncateDeliveries());
        truncateAll.setOnAction(event -> hubController.truncateAll());

        logout.setOnMouseClicked(event -> HubController.logout(window));
        exit.setOnMouseClicked(event -> HubController.exit());

        //MENU
        addRoom.setOnAction(event -> setWorkSpace(workSpaces.addRoomWorkspace()));
        addProduct.setOnAction(event -> setWorkSpace(workSpaces.addProductWorkspace()));
        removeRoom.setOnAction(event -> setWorkSpace(workSpaces.removeRoomWorkspace()));
        removeProduct.setOnAction(event -> setWorkSpace(workSpaces.removeProductWorkspace()));
        changeBalance.setOnAction(event -> setWorkSpace(workSpaces.changeBalanceWorkspace()));
        transfer.setOnAction(event -> setWorkSpace(workSpaces.transferWorkspace()));
        receive.setOnAction(event -> setWorkSpace(workSpaces.receiveWorkspace(this)));
        collectDelivery.setOnAction(event -> setWorkSpace(workSpaces.collectDeliveryWorkspace(this, connection)));
        sendDelivery.setOnAction(event -> setWorkSpace(workSpaces.sendDeliveryWorkspace(this)));
        //newShipment.setOnAction(event -> );
        //newDelivery.setOnAction(event -> );

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
