package fi.orderly.ui;

import fi.orderly.dao.Shipment;
import fi.orderly.logic.HubController;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.sql.Connection;

public class ShipmentWorkspace {

    Connection connection;
    DatabaseAccess db;
    HubController hubController;

    public ShipmentWorkspace(Connection connection) {
        this.connection = connection;
        db = new DatabaseAccess(connection);
        hubController = new HubController(connection);
    }

    public HBox addRow(Shipment.DataPackage data){
        HBox row = new HBox();
        Label productName = new Label(data.getName());
        Label productCode = new Label(data.getCode());
        Label batchNumber = new Label(data.getBatch());
        TextField amount = new TextField(data.getAmount());
        Label unit = new Label(data.getUnit());
        TextField room = new TextField(data.getStorageRoom());

        row.getChildren().addAll(productName, productCode, batchNumber, amount, unit, room);
        row.setId("row1");

        for (Node node: row.getChildren()
             ) {
            node.setId("small-field");
        }
        return row;
    }
}
