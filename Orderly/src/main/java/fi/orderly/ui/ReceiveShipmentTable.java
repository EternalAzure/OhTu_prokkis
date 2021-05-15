package fi.orderly.ui;

import fi.orderly.dao.Shipment;
import fi.orderly.logic.Utils;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.SQLException;

public class ReceiveShipmentTable {
    DatabaseAccess db;
    private Button apply;
    private Label message;
    private TextField[] amounts;
    private TextField[] rooms;
    Shipment shipment;
    public ReceiveShipmentTable(Shipment shipment, Connection connection) {
        this.shipment = shipment;
        this.db = new DatabaseAccess(connection);
    }
    public VBox getTable() {
        amounts = new TextField[shipment.getLength()];
        rooms = new TextField[shipment.getLength()];

        //1. Set header
        VBox layout = new VBox();
        layout.setId("workspace");
        HBox header = new HBox(
                new Label("Name"),
                new Label("Code"),
                new Label("Batch"),
                new Label("Amount"),
                new Label("Unit"),
                new Label("Room"));
        for (Node node: header.getChildren()
        ) {
            node.setId("small-field");
        }
        layout.getChildren().add(header);
        header.setPrefWidth(400);
        header.setId("header");

        //2. Build table with data
        for (int i = 0; i < shipment.getLength(); i++) {
            HBox tableRow = new HBox();
            tableRow.setId("row1");
            Label productName = new Label();
            Label productCode = new Label();
            Label batchNumber = new Label();
            TextField amount = new TextField();
            Label unit = new Label();
            TextField room = new TextField();
            tableRow.getChildren().addAll(productName, productCode, batchNumber, amount, unit, room);
            amounts[i] = amount;
            rooms[i] = room;

            for (Node node: tableRow.getChildren()
            ) {
                node.setId("small-field");
            }

            productName.setText(shipment.getDataPackage(i).getName());
            productCode.setText(shipment.getDataPackage(i).getCode());
            batchNumber.setText(shipment.getDataPackage(i).getBatch());
            amount.setText(shipment.getDataPackage(i).getAmount());
            unit.setText(shipment.getDataPackage(i).getUnit());
            room.setText(shipment.getDataPackage(i).getStorageRoom());
            layout.getChildren().add(tableRow);
        }

        //3.Add button and label
        apply = new Button("Apply");
        message = new Label();
        message.setId("error");
        layout.getChildren().addAll(apply, message);
        return  layout;
    }

    public Button getApply() {
        return apply;
    }

    public Label getMessage() {
        return message;
    }

    public boolean updateShipment() {
        boolean valid = validateInput();
        if (!valid) return false;

        for (int i = 0; i < shipment.getLength(); i++) {
            shipment.getDataPackage(i).setAmount(amounts[i].getText());
            shipment.getDataPackage(i).setStorageRoom(rooms[i].getText());
        }
        return true;
    }

    private boolean validateInput() {
        for (int i = 0; i < shipment.getLength(); i++) {
            if (Utils.notDouble(amounts[i].getText())) {
                message.setText("Invalid input:\ncolumn: Amount \nrow: " + i);
                return false;
            }
        }
        int row = 0;
        try {
            for (int i = 0; i < shipment.getLength(); i++) {
                row++;
                if (!db.rooms.foundRoom(rooms[i].getText())) {
                    message.setText("Invalid input:\ncolumn: Room \nrow: " + i);
                    return false;
                }
            }
        } catch (SQLException e) {
            message.setText("Invalid input:\ncolumn: Room \nrow: " + row);
            return false;
        }
        return true;
    }
}
