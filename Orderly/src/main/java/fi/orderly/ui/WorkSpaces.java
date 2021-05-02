package fi.orderly.ui;
import fi.orderly.dao.Delivery;
import fi.orderly.logic.Utils;
import fi.orderly.logic.dbinterface.DatabaseAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import fi.orderly.logic.HubController;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class WorkSpaces {

    ShipmentWorkspace shipmentWorkspace;
    HubController hubController;
    Utils utils;
    HashMap<Integer, Delivery> deliveries;
    DatabaseAccess db;

    public WorkSpaces(Statement statement) {
        this.hubController = new HubController(statement);
         shipmentWorkspace = new ShipmentWorkspace(statement);
         utils = new Utils(statement);
         deliveries = new HashMap<>();
         db = new DatabaseAccess(statement);
    }

    public VBox addRoomWorkspace() {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField roomName = new TextField();
        TextField temperature = new TextField();
        temperature.setPromptText("Temperature(optional)");
        roomName.setPromptText("Name of the room");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(roomName, temperature, apply, message);
        apply.setOnAction(event -> {
            message.setText(hubController.addRoom(roomName.getText(), temperature.getText()));
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
            message.setText(hubController.addProduct(product.getText(), code.getText(), unit.getText(), temperature.getText(), storage.getText()));
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
        message.setId("error");

        vBox.getChildren().addAll(roomName, apply, message);
        apply.setOnAction(event -> {
            message.setText(hubController.removeRoom(roomName.getText()));
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
        message.setId("error");

        vBox.getChildren().addAll(product, apply, message);
        apply.setOnAction(event -> {
            message.setText(hubController.removeProduct(product.getText()));
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
        message.setId("error");

        vBox.getChildren().addAll(roomName, product, batch, newBalance, apply, message);
        apply.setOnAction(event -> {
            message.setText(hubController.changeBalance(roomName.getText(), product.getText(), batch.getText(), newBalance.getText()));
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
        message.setId("error");

        vBox.getChildren().addAll(from, to, amount, code, batch, apply, message);
        apply.setOnAction(event -> {
            message.setText(hubController.transfer(from.getText(), to.getText(), code.getText(), batch.getText(),amount.getText()));
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
        message.setId("error");

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

    public VBox collectDeliveryWorkspace(Hub hub, Statement statement) {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField deliveryNumber = new TextField();
        deliveryNumber.setPromptText("Delivery number");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(deliveryNumber, apply, message);
        apply.setOnAction(event -> chooseAction(deliveryNumber.getText(), message, statement, hub));
        return vBox;
    }

    private void chooseAction(String delNumb, Label message, Statement statement, Hub hub) {
        //Non valid input
        if (delNumb.isEmpty()) {
            message.setText("Non allowed empty value");
            return;
        }
        if (!Utils.notInt(delNumb)) {
            message.setText("Some values need to be integer");
            return;
        }
        //Valid input
        try {
            if (db.deliveries.hasDelivery(Integer.parseInt(delNumb))) {
                hub.setWorkSpace(collectDelivery(Integer.parseInt(delNumb), statement));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        message.setText("delivery not found");
    }

    private VBox collectDelivery(int deliveryNumber, Statement statement) {
        Delivery delivery;
        if (deliveries.containsKey(deliveryNumber)) {
            delivery = deliveries.get(deliveryNumber);
        } else {
            delivery = new Delivery(deliveryNumber, statement);
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
            deliveries.put(deliveryNumber, delivery);
            delivery.collect(code.getText(), batch.getText(), amount.getText(), from.getText());
        });
        return vBox;
    }

    public VBox sendDeliveryWorkspace(Hub hub) {
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField deliveryNumber = new TextField();
        deliveryNumber.setPromptText("Delivary number");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(deliveryNumber, apply, message);
        apply.setOnAction(event -> validate(deliveryNumber.getText(), message, hub));
        return vBox;
    }

    private void validate(String deliveryNumber, Label message, Hub hub) {
        if (deliveryNumber.isEmpty()) {
            message.setText("Non allowed empty field");
            return;
        }
        if (!Utils.notInt(deliveryNumber)) {
            message.setText("Integer needed");
            return;
        }
        if (deliveries.containsKey(Integer.parseInt(deliveryNumber))) {
            hub.setWorkSpace(sendDelivery(Integer.parseInt(deliveryNumber)));
        } else {
            message.setText("delivery not found");
        }
    }

    public VBox sendDelivery(int deliveryNumber) {
        VBox vBox = new VBox();
        TableView<Delivery.DataPackage> table = new TableView<>();
        ObservableList<Delivery.DataPackage> packageList = FXCollections.observableArrayList();

        Delivery delivery = deliveries.get(deliveryNumber);
        for (int i = 0; i < delivery.getLength(); i++) {
            packageList.add(delivery.getDataPackage(i));
        }
        table.setItems(packageList);
        Button apply = new Button("Apply");
        vBox.getChildren().addAll(table, apply);
        apply.setOnAction(event -> hubController.sendDelivery(delivery));

        return vBox;
    }

    public VBox newShipmentWorkspace() {
        VBox vBox = new VBox();
        TextField code = new TextField();
        TextField batch = new TextField();
        TextField amount = new TextField();
        Button apply = new Button("Apply");
        Label message = new Label();
        code.setPromptText("Product code");
        batch.setPromptText("Batch number");
        amount.setPromptText("Expected amount");

        vBox.getChildren().addAll(code, batch, amount, apply, message);
        apply.setOnAction(event -> System.out.println("pressed button"));
        return vBox;
    }

    public VBox newDeliveryWorkspace() {
        VBox vBox = new VBox();
        TextField code = new TextField();
        TextField amount = new TextField();
        Button apply = new Button("Apply");
        Label message = new Label();
        code.setPromptText("Product code");
        amount.setPromptText("Requested amount");

        vBox.getChildren().addAll(code, amount, apply, message);
        apply.setOnAction(event -> System.out.println("pressed button"));
        return vBox;
    }
}
