package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import logic.HubController;

import java.sql.Statement;

public class WorkSpaces {

    ShipmentWorkspace shipmentWorkspace;
    HubController hubController;

    public WorkSpaces(Statement statement){
        this.hubController = new HubController(statement);
         shipmentWorkspace = new ShipmentWorkspace(statement);
    }

    public VBox addRoomWorkspace(){
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
        apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                message.setText(hubController.addRoom(roomName.getText(), temperature.getText()));
                roomName.clear(); temperature.clear();
            }
        });

        return vBox;
    }
    public VBox addProductWorkspace(){
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField product = new TextField();
        TextField code = new TextField();
        TextField temperature = new TextField();
        TextField unit = new TextField();
        product.setPromptText("Product name");
        code.setPromptText("Product code");
        temperature.setPromptText("Temperature(optional)");
        unit.setPromptText("unit of measure");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(product, code, temperature, unit, apply, message);
        apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                message.setText(hubController.addProduct(product.getText(), code.getText(), unit.getText(), temperature.getText()));
                product.clear(); code.clear(); temperature.clear(); unit.clear();

            }
        });

        return vBox;
    }

    public VBox removeRoomWorkspace(){
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField roomName = new TextField();
        roomName.setPromptText("Room");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(roomName, apply, message);
        apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                message.setText(hubController.removeRoom(roomName.getText()));
                roomName.clear();
            }
        });

        return vBox;
    }

    public VBox removeProductWorkspace(){
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField product = new TextField();
        product.setPromptText("Name of the product");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(product, apply, message);
        apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                message.setText(hubController.removeProduct(product.getText()));
                product.clear();
            }
        });

        return vBox;
    }

    public VBox changeBalanceWorkspace(){
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField roomName = new TextField();
        TextField product = new TextField();
        TextField batch = new TextField();
        TextField newBalance = new TextField();
        roomName.setPromptText("Room");
        product.setPromptText("Product");
        batch.setPromptText("Batch number");
        newBalance.setPromptText("new balance");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(roomName, product, batch, newBalance, apply, message);
        apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                message.setText(hubController.changeBalance(roomName.getText(), product.getText(), batch.getText(), newBalance.getText()));
                roomName.clear(); product.clear(); batch.clear(); newBalance.clear();
            }
        });

        return vBox;
    }

    public VBox transferWorkspace(){
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
        apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                message.setText(hubController.transfer(from.getText(), to.getText(), code.getText(), batch.getText(),amount.getText()));
                from.clear(); to.clear(); code.clear(); batch.clear(); amount.clear();
            }
        });

        return vBox;
    }

    public VBox selectShipmentWorkspace(Hub hub){
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField shipmentNumber = new TextField();
        shipmentNumber.setPromptText("Shipment number");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(shipmentNumber, apply, message);
        apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String validationMessage = shipmentWorkspace.validateInput(shipmentNumber.getText());
                if(!validationMessage.isEmpty()){
                    message.setText(validationMessage);
                    return;
                }
                VBox SWSLayout = shipmentWorkspace.getShipmentWorkspace(shipmentNumber.getText());
                SWSLayout.setId("workspace");
                hub.setWorkSpace(SWSLayout);
            }
        });

        return vBox;
    }

    public VBox collectDeliveryWorkspace(){
        VBox vBox = new VBox();
        vBox.setId("workspace");
        TextField roomName = new TextField();
        roomName.setPromptText("");
        Button apply = new Button("Apply");

        vBox.getChildren().addAll(roomName, apply);
        apply.setOnAction(event -> hubController.collect());
        return vBox;
    }

    private void styleSimple(VBox layout){
        //layout.setAlignment(Pos.CENTER);
        layout.setId("workspace");
    }
}
