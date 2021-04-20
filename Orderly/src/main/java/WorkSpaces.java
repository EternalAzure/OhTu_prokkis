import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.Statement;

public class WorkSpaces {

    private double parentWidth;

    final private Statement statement;
    HubController hubController;
    public WorkSpaces(Statement statement){
        this.statement = statement;
        this.hubController = new HubController(statement);
    }

    public VBox addRoom(){
        VBox vBox = new VBox();

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
        styleSimple(vBox);
        return vBox;
    }
    public VBox addProduct(){
        VBox vBox = new VBox();
        styleSimple(vBox);

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

    public VBox removeRoom(){
        VBox vBox = new VBox();
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
        styleSimple(vBox);
        return vBox;
    }

    public VBox removeProduct(){
        VBox vBox = new VBox();
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
        styleSimple(vBox);
        return vBox;
    }

    public VBox changeBalance(){
        VBox vBox = new VBox();
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
        styleSimple(vBox);
        return vBox;
    }

    public VBox transfer(){
        VBox vBox = new VBox();
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
        styleSimple(vBox);
        return vBox;
    }

    public VBox receive(Hub hub){
        VBox vBox = new VBox();
        TextField shipmentNumber = new TextField();
        shipmentNumber.setPromptText("Shipment number");
        Button apply = new Button("Apply");
        Label message = new Label();
        message.setId("error");

        vBox.getChildren().addAll(shipmentNumber, apply, message);
        apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (shipmentNumber.getText().isEmpty()) {
                    message.setText("Non allowed empty value");
                    return;
                }
                if (!Utils.isNumeric(new String[]{shipmentNumber.getText()})){
                    message.setText("Numeric input needed");
                    return;
                }
                ShipmentGUI shipmentGUI = new ShipmentGUI(statement);
                shipmentGUI.receiveShipment(shipmentNumber.getText());
                hub.setWorkSpace(shipmentGUI.display());
            }
        });
        styleSimple(vBox);
        return vBox;
    }

    public VBox collect(){
        VBox vBox = new VBox();
        TextField roomName = new TextField();
        roomName.setPromptText("");
        Button apply = new Button("Apply");

        vBox.getChildren().addAll(roomName, apply);
        apply.setOnAction(event -> hubController.collect());
        return vBox;
    }

    private void styleSimple(VBox layout){
        layout.setAlignment(Pos.CENTER);
        layout.setId("workspace");
    }
}
