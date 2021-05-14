package fi.orderly.ui;

import fi.orderly.dao.Shipment;
import fi.orderly.logic.HubController;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import fi.orderly.logic.Utils;
import java.sql.Connection;
import java.sql.SQLException;

public class ShipmentWorkspace {

    Connection connection;
    DatabaseAccess db;
    HubController hubController;
    private Shipment shipment;
    final private Label errorMessage = new Label();
    // Has dependency
    // Shipment in getShipmentWorkspace()

    public ShipmentWorkspace(Connection connection) {
        this.connection = connection;
        db = new DatabaseAccess(connection);
        hubController = new HubController(connection);
        errorMessage.setId("error");
    }

    /**
     * Rakentaa ja palauttaa työtilan, joka on taulukko vastaanotettavasta toimituksesta.
     * @param shipmentNumber toimitusnumero
     * @param applyButton nappi
     * @return taulukko vastaanotettavasta toimituksesta
     */
    public VBox getShipmentWorkspace(String shipmentNumber, Button applyButton){
        if (!validateInput(shipmentNumber).isEmpty()) return null;

        //1. Add, set and style header
        VBox vBox = new VBox();
        HBox header = new HBox(new Label("Name"),
                new Label("Code"),
                new Label("Batch"),
                new Label("Amount"),
                new Label("Unit"),
                new Label("Room"));
        for (Node node: header.getChildren()
        ) {
            node.setId("small-field");
        }
        vBox.getChildren().add(header);
        header.setPrefWidth(400);
        header.setId("header");

        //2. Fetch data
        shipment = new Shipment(Integer.parseInt(shipmentNumber), connection);

        //3. Put data to visual table rows
        for (int i = 0; i < shipment.getLength(); i++) {
            vBox.getChildren().add(addRow(shipment.getDataPackage(i)));
        }

        //4. Add button underneath the rows
        vBox.getChildren().addAll(applyButton, errorMessage);
        return vBox;
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

    public Shipment getShipment() {
        return shipment;
    }

    public String validateInput(String input){
        try {
            if (input.isEmpty() || Utils.notInt(input)) return "Input needs to be integer";
            if (!db.shipments.foundShipment(Integer.parseInt(input))) return "No shipments found";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setErrorMessage(String message) {
        errorMessage.setText(message);
        if (message.equals("Success")) errorMessage.setId("success");
    }

}
