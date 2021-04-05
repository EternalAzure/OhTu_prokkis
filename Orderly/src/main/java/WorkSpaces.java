import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.Statement;

public class WorkSpaces {

    HubController hubController = new HubController();
    String database = "warehouse";

    public VBox addRoom(){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        TextField roomName = new TextField();
        TextField temperature = new TextField();
        temperature.setPromptText("Temperature(optional)");
        roomName.setPromptText("Name of the room");
        Button apply = new Button("Apply");

        vBox.getChildren().addAll(roomName, temperature, apply);
        Statement statement = ServerConnection.defaultConnection(database);
        apply.setOnAction(event -> hubController.addRoom(roomName, temperature, statement));
        return vBox;
    }
    public VBox addProduct(){
        VBox vBox = new VBox();
        TextField product = new TextField();
        TextField code = new TextField();
        TextField temperature = new TextField();
        TextField unit = new TextField();
        product.setPromptText("Product name");
        code.setPromptText("Product code");
        temperature.setPromptText("Temperature(optional)");
        unit.setPromptText("unit of measure");
        Button apply = new Button("Apply");

        vBox.getChildren().addAll(product, code, temperature, unit, apply);

        Statement statement = ServerConnection.defaultConnection(database);
        apply.setOnAction(event -> hubController.addProduct(product, code, temperature, unit, statement));
        return vBox;
    }

    public static VBox removeRoom(){
        VBox vBox = new VBox();
        TextField roomName = new TextField();
        roomName.setPromptText("Name of the room");
        Button apply = new Button("Apply");

        vBox.getChildren().addAll(roomName, apply);
        return vBox;
    }

    public static VBox removeProduct(){
        VBox vBox = new VBox();
        TextField roomName = new TextField();
        roomName.setPromptText("Name of the product");
        Button apply = new Button("Apply");

        vBox.getChildren().addAll(roomName, apply);
        return vBox;
    }

    public static VBox changeBalance(){
        VBox vBox = new VBox();
        TextField roomName = new TextField();
        TextField newBalance = new TextField();
        TextField batch = new TextField();
        newBalance.setPromptText("new balance");
        roomName.setPromptText("Product");
        batch.setPromptText("Batch number(Not implemented)");
        Button apply = new Button("Apply");

        vBox.getChildren().addAll(roomName, apply);
        return vBox;
    }

    public static VBox transfer(){
        VBox vBox = new VBox();
        TextField from = new TextField();
        TextField to = new TextField();
        TextField amount = new TextField();
        TextField product = new TextField();
        TextField batch = new TextField();
        from.setPromptText("From");
        to.setPromptText("To");
        amount.setPromptText("Amount");
        product.setPromptText("Product");
        batch.setPromptText("Batch(not implemented)");
        Button apply = new Button("Apply");

        vBox.getChildren().addAll(from, to, amount, product, batch, apply);
        return vBox;
    }

    public static VBox receive(){
        VBox vBox = new VBox();
        TextArea text = new TextArea("Something similar to changing balance\n" +
                "but for many products at once");
        Button apply = new Button("Apply");

        vBox.getChildren().addAll(text, apply);
        return vBox;
    }

    public static VBox collect(){
        VBox vBox = new VBox();
        TextField roomName = new TextField();
        roomName.setPromptText("Yet to be implemented");
        Button apply = new Button("Apply");

        vBox.getChildren().addAll(roomName, apply);
        return vBox;
    }




}
