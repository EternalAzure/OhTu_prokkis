package fi.orderly.ui;
import fi.orderly.dao.Delivery;
import fi.orderly.logic.Utils;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import fi.orderly.logic.HubController;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class WorkSpaces {

    ShipmentWorkspace shipmentWorkspace;
    HubController hubController;
    HashMap<Integer, Delivery> deliveries;
    DatabaseAccess db;
    Connection connection;

    public WorkSpaces(Connection connection) {
        this.connection = connection;
        this.hubController = new HubController(connection);
         shipmentWorkspace = new ShipmentWorkspace(connection);
         deliveries = new HashMap<>();
         db = new DatabaseAccess(connection);
    }

    public VBox addRoomWorkspace() {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField roomName = new TextField();
        TextField temperature = new TextField();
        temperature.setPromptText("Temperature(optional)");
        roomName.setPromptText("Room name");
        Button apply = new Button("Apply");
        Label message = new Label();

        vBox.getChildren().addAll(roomName, temperature, apply, message);
        apply.setOnAction(event -> {
            String feedback = hubController.addRoom(roomName.getText(), temperature.getText());
            if (feedback.equals("Success")) {
                message.setId("success");
            }else {
                message.setId("error");
            }
            message.setText(feedback);
            roomName.clear(); temperature.clear();
        });
        return vBox;
    }

    public VBox addProductWorkspace() {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField product = new TextField();
        TextField code = new TextField();
        TextField temperature = new TextField();
        TextField unit = new TextField();
        TextField storage = new TextField();
        product.setPromptText("Product name");
        code.setPromptText("Product code");
        temperature.setPromptText("Temperature(optional)");
        unit.setPromptText("unit of measurement");
        storage.setPromptText("Default storage room");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(product, code, temperature, unit, storage, apply, message);
        apply.setOnAction(event -> {
            String feedback = hubController.addProduct(product.getText(), code.getText(), unit.getText(), temperature.getText(), storage.getText());
            if (feedback.equals("Success")) {
                message.setId("success");
            }else {
                message.setId("error");
            }
            message.setText(feedback);
            product.clear(); code.clear(); temperature.clear(); unit.clear(); storage.clear();
        });

        return vBox;
    }

    public VBox removeRoomWorkspace() {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField roomName = new TextField();
        roomName.setPromptText("Room name");
        Button apply = new Button("Apply");
        Label message = new Label();

        vBox.getChildren().addAll(roomName, apply, message);
        apply.setOnAction(event -> {
            String feedback = hubController.removeRoom(roomName.getText());
            if (feedback.equals("Success")) {
                message.setId("success");
            }else {
                message.setId("error");
            }
            message.setText(feedback);
            roomName.clear();
        });

        return vBox;
    }

    public VBox removeProductWorkspace() {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField product = new TextField();
        product.setPromptText("Product name");
        Button apply = new Button("Apply");
        Label message = new Label();

        vBox.getChildren().addAll(product, apply, message);
        apply.setOnAction(event -> {
            String feedback = hubController.removeProduct(product.getText());
            if (feedback.equals("Success")) {
                message.setId("success");
            }else {
                message.setId("error");
            }
            message.setText(feedback);
            product.clear();
        });

        return vBox;
    }

    public VBox changeBalanceWorkspace() {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField roomName = new TextField();
        TextField product = new TextField();
        TextField batch = new TextField();
        TextField newBalance = new TextField();
        roomName.setPromptText("Room name");
        product.setPromptText("Product code");
        batch.setPromptText("Batch number");
        newBalance.setPromptText("new balance");
        Button apply = new Button("Apply");
        Label message = new Label();

        vBox.getChildren().addAll(roomName, product, batch, newBalance, apply, message);
        apply.setOnAction(event -> {
            String feedback = hubController.changeBalance(roomName.getText(), product.getText(), batch.getText(), newBalance.getText());
            if (feedback.equals("Success")) {
                message.setId("success");
            }else {
                message.setId("error");
            }
            message.setText(feedback);
            roomName.clear(); product.clear(); batch.clear(); newBalance.clear();
        });

        return vBox;
    }

    public VBox transferWorkspace() {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField from = new TextField();
        TextField to = new TextField();
        TextField amount = new TextField();
        TextField code = new TextField();
        TextField batch = new TextField();
        from.setPromptText("From room");
        to.setPromptText("To room");
        amount.setPromptText("Amount");
        code.setPromptText("Product code");
        batch.setPromptText("Batch number");
        Button apply = new Button("Apply");
        Label message = new Label();

        vBox.getChildren().addAll(from, to, amount, code, batch, apply, message);
        apply.setOnAction(event -> {
            String feedback = hubController.transfer(from.getText(), to.getText(), code.getText(), batch.getText(),amount.getText());
            if (feedback.equals("Success")) {
                message.setId("success");
            }else {
                message.setId("error");
            }
            message.setText(feedback);
            from.clear(); to.clear(); code.clear(); batch.clear(); amount.clear();
        });

        return vBox;
    }

    public VBox receiveWorkspace(Hub hub) {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField shipmentNumber = new TextField();
        shipmentNumber.setPromptText("Shipment number");
        Button apply = new Button("Apply");
        Label message = new Label();

        vBox.getChildren().addAll(shipmentNumber, apply, message);
        apply.setOnAction(event -> {
            String validationMessage = shipmentWorkspace.validateInput(shipmentNumber.getText());
            if (!validationMessage.isEmpty()) {
                message.setText(validationMessage);
                return;
            }
            Button otherApply = new Button("Apply");
            VBox SWSLayout = shipmentWorkspace.getShipmentWorkspace(shipmentNumber.getText(), otherApply);
            otherApply.setOnAction(event1 -> shipmentWorkspace.setErrorMessage(hubController.receiveShipment(shipmentWorkspace.getShipment())));
            SWSLayout.setId("workspace");
            hub.setWorkSpace(SWSLayout);
        });

        return vBox;
    }

    public VBox collectDeliveryWorkspace1(Hub hub) {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField deliveryNumber = new TextField();
        deliveryNumber.setPromptText("Delivery number");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");
        vBox.getChildren().addAll(deliveryNumber, apply, message);
        apply.setOnAction(event -> {
            //1. Validate input
            String delNum = deliveryNumber.getText();
            if (delNum.isEmpty() || Utils.notInt(delNum)) {
                message.setText("Integer needed");
                return;
            }
            try {
                //2. Set next workspace
                if (db.deliveries.foundDelivery(Integer.parseInt(delNum))) {
                    hub.setWorkSpace(collectDeliveryWorkspace2(Integer.parseInt(delNum)));
                }
                message.setText("Delivery was not found");
            } catch (SQLException e) {
                message.setText("SQLException. \nContact tech support");
            }
        });
        return vBox;
    }

    private VBox collectDeliveryWorkspace2(int deliveryNumber) {
        Delivery delivery;
        if (deliveries.containsKey(deliveryNumber)) {
            System.out.println("Had key");
            delivery = deliveries.get(deliveryNumber);
        } else {
            System.out.println("Hadn't key");
            delivery = new Delivery(deliveryNumber, connection);
        }

        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField code = new TextField();
        TextField batch = new TextField();
        TextField amount = new TextField();
        TextField from = new TextField();
        code.setPromptText("Product code");
        batch.setPromptText("Batch number");
        amount.setPromptText("Amount");
        from.setPromptText("From room");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(code, batch, amount, from, apply, message);
        apply.setOnAction(event -> {
            try {
                boolean p = db.products.foundProduct(Integer.parseInt(code.getText()));
                boolean b = db.foundBatch(Integer.parseInt(batch.getText()));
                boolean r = db.rooms.foundRoom(from.getText());
                Double.parseDouble(amount.getText());
                if (p && b && r) {
                    deliveries.put(deliveryNumber, delivery);
                    delivery.collect(code.getText(), batch.getText(), amount.getText(), from.getText());
                    message.setId("success");
                    message.setText("Success");
                }
            } catch (SQLException e) {
                message.setText("SQLException. \nContact tech support");
            } catch (NumberFormatException n) {
                message.setText("Number format error");
            }
            code.clear(); batch.clear(); amount.clear(); from.clear();
        });
        return vBox;
    }

    public VBox sendDeliveryWorkspace1(Hub hub) {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField deliveryNumber = new TextField();
        deliveryNumber.setPromptText("Delivery number");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(deliveryNumber, apply, message);
        apply.setOnAction(event -> {
            //1. Validate Input
            String delNum = deliveryNumber.getText();
            if (Utils.notInt(delNum) || delNum.isEmpty()) {
                message.setText("Integer needed");
                return;
            }
            if (deliveries.containsKey(Integer.parseInt(delNum))) {
                //Set next workspace
                hub.setWorkSpace(sendDeliveryWorkspace2(Integer.parseInt(delNum)));
            } else {
                message.setText("Delivery not found");
            }
        });
        return vBox;
    }

    public VBox sendDeliveryWorkspace2(int deliveryNumber) {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TableView<Delivery.DataPackage> table = new TableView<>();
        ObservableList<Delivery.DataPackage> packageList = FXCollections.observableArrayList();

        Delivery delivery = deliveries.get(deliveryNumber);
        for (int i = 0; i < delivery.getLength(); i++) {
            packageList.add(delivery.getDataPackage(i));
        }
        table.setItems(packageList);
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");
        vBox.getChildren().addAll(table, apply);
        apply.setOnAction(event -> {
            String feedback = hubController.sendDelivery(delivery);
            if (feedback.equals("Success")) {
                message.setId("success");
            }
            message.setText(feedback);
        });
        return vBox;
    }

    public VBox newShipmentWorkspace() {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField number = new TextField();
        TextField code = new TextField();
        TextField batch = new TextField();
        TextField amount = new TextField();
        Button apply = new Button("Apply");
        Label message = new Label();
        number.setPromptText("Shipment number");
        code.setPromptText("Product code");
        batch.setPromptText("Batch number");
        amount.setPromptText("Expected amount");

        vBox.getChildren().addAll(number, code, batch, amount, apply, message);
        apply.setOnAction(event -> {
            String feedback = hubController.newShipment(number.getText(), code.getText(), batch.getText(), amount.getText());
            if (feedback.equals("Success")) {
                message.setId("success");
            }else {
                message.setId("error");
            }
            message.setText(feedback);
        });
        return vBox;
    }

    public VBox newDeliveryWorkspace() {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField number = new TextField();
        TextField code = new TextField();
        TextField amount = new TextField();
        Button apply = new Button("Apply");
        Label message = new Label();
        number.setPromptText("Delivery number");
        code.setPromptText("Product code");
        amount.setPromptText("Requested amount");

        vBox.getChildren().addAll(number, code, amount, apply, message);
        apply.setOnAction(event -> {
            String feedback = hubController.newDelivery(number.getText(), code.getText(), amount.getText());
            if (feedback.equals("Success")) {
                message.setId("success");
            }else {
                message.setId("error");
            }
            message.setText(feedback);
        });
        return vBox;
    }

}
