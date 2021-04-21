package ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.HubController;
import logic.Utils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ShipmentWorkspace {

    Statement statement;
    Utils utils;
    HubController hubController;
    //ArrayList<Product> products;

    public ShipmentWorkspace(Statement statement){
        this.statement = statement;
        utils = new Utils(statement);
        hubController = new HubController(statement);
        //products = new ArrayList<>();
    }

    public VBox getShipmentWorkspace(String shipmentNumber){
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
        header.setId("row1");

        //2. Get data from database
        ArrayList<String[]> someName = fetchShipment(shipmentNumber);

        //3. build and add rows underneath of header using that data
        for (String[] qres: someName
        ) {
            vBox.getChildren().add(
                addRow(qres[0], qres[1])
            );
        }

        //4. Add button underneath of rows
        Button apply = new Button("Apply");
        vBox.getChildren().add(apply);

        //5. Add function to button
        apply.setOnAction(event -> hubController.receiveShipment());
        return vBox;
    }

    public HBox addRow(String id, String batch){
        HBox row = new HBox();
        Label productName = new Label();
        Label productCode = new Label();
        Label batchNumber = new Label();
        TextField amount = new TextField();
        Label unit = new Label();
        TextField room = new TextField();

        productName.setText(fillName(id));
        productCode.setText(fillCode(id));
        batchNumber.setText(batch);
        amount.setPromptText("amount");
        unit.setText(fillUnit(id));
        room.setText(fillRoom(id));

        row.getChildren().addAll(productName, productCode, batchNumber, amount, unit, room);
        row.setMaxWidth(500);
        row.setId("row1");

        for (Node node: row.getChildren()
             ) {
            node.setId("small-field");
        }

        return row;
    }

    private String fillName(String productId){
        //Local variable is NOT redundant
        String sql = "SELECT product FROM products WHERE id="+productId;
        return utils.getResultString(sql, "product");
    }

    private String fillUnit(String productId){
        String sql = "SELECT unit FROM products WHERE id="+productId;
        return utils.getResultString(sql, "unit");
    }

    private String fillRoom(String productId){
        String sql = "SELECT defaultroom_id FROM products WHERE id="+productId;
        return utils.getResultString(sql, "defaultroom_id");
    }

    private String fillCode(String productId){
        String sql = "SELECT code FROM products WHERE id="+productId;
        return utils.getResultString(sql, "code");
    }

    public ArrayList<String[]> fetchShipment(String shipmentNumber){
        ArrayList<String[]> queryResults = new ArrayList<>();
        String queryShipment = "SELECT product_id, batch, id FROM shipments WHERE shipment_number="+shipmentNumber;
        try(ResultSet resultSet = statement.executeQuery(queryShipment)){
            while (resultSet.next()){
                String product_id = resultSet.getString("product_id");
                String batch = resultSet.getString("batch");
                queryResults.add(new String[] {product_id, batch});
            }
        }catch (SQLException e){e.printStackTrace();}
        return queryResults;
    }

    public String validateInput(String input){
        if (input.isEmpty() || !Utils.isInt(input)) return "Input needs to be integer";
        String query = "SELECT COUNT(*) FROM shipments WHERE shipment_number="+input;
        int deliverySize = utils.getResultInt(query, "COUNT(*)");
        if (deliverySize == 0) return "No shipments found";

        return "";
    }
}
