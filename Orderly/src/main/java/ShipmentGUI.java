import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ShipmentGUI {
    VBox vBox = new VBox();
    HBox header = new HBox(new Label("Name"),
                new Label("Code"),
                new Label("Batch"),
                new Label("Amount"),
                new Label("Unit"),
                new Label("Room"));

    Statement statement;
    Utils utils;
    public ShipmentGUI(Statement statement){
        this.statement = statement;
        utils = new Utils(statement);
        setGUI();
    }

    private void setGUI(){
        vBox.getChildren().add(header);
        header.setPrefWidth(400);
        header.setId("row1");


    }


    Button apply = new Button("Apply");

    public void addRow(String id, String batch){
        System.out.println("id:"+id+" batch:"+batch);
        HBox row = new HBox();
        Label productName = new Label();
        Label productCode = new Label();
        Label batchNumber = new Label();
        TextField amount = new TextField();
        Label unit = new Label();
        TextField room = new TextField();
        productName.setId("small-field");
        productCode.setId("small-field");
        batchNumber.setId("small-field");
        amount.setId("small-field");
        unit.setId("small-field");
        room.setId("small-field");

        productName.setText(fillName(id));
        productCode.setText(fillCode(id));
        batchNumber.setText(batch);
        amount.setPromptText("amount");
        unit.setText(fillUnit(id));
        room.setText(fillRoom(id));

        row.getChildren().addAll(productName, productCode, batchNumber, amount, unit, room);
        row.setMaxWidth(400);
        row.setId("row1");
        vBox.getChildren().add(row);
    }

    private String fillName(String productId){
        //Local variable is NOT redundant
        String sql = "SELECT product FROM products WHERE id="+productId;
        String name = utils.getResultString(sql, "product");
        return name;
    }

    private String fillUnit(String productId){
        String sql = "SELECT unit FROM products WHERE id="+productId;
        return utils.getResultString(sql, "unit");
    }

    private String fillRoom(String productId){
        String sql = "SELECT defaultroom_id FROM products WHERE id="+productId;
        String room = utils.getResultString(sql, "defaultroom_id");
        return room;
    }

    private String fillCode(String productId){
        String sql = "SELECT code FROM products WHERE id="+productId;
        String code = utils.getResultString(sql, "code");
        return code;
    }

    public VBox display(){
        vBox.getChildren().add(apply);
        return vBox;
    }

    public void receiveShipment(String shipmentNumber){
        if (shipmentNumber.isEmpty()) return;
        if (!Utils.isInt(shipmentNumber)) return;

        String query = "SELECT COUNT(*) FROM shipments WHERE shipment_number="+shipmentNumber;
        int deliverySize = utils.getResultInt(query, "COUNT(*)");
        if (deliverySize == 0) return;

        ArrayList<String[]> queryResults = new ArrayList<>();
        String queryShipment = "SELECT product_id, batch, id FROM shipments WHERE shipment_number="+shipmentNumber;
        try(ResultSet resultSet = statement.executeQuery(queryShipment)){
            while (resultSet.next()){
                String product_id = resultSet.getString("product_id");
                String batch = resultSet.getString("batch");
                queryResults.add(new String[] {product_id, batch});
                System.out.println("round");
            }

        }catch (SQLException e){e.printStackTrace();}


        for (String[] qres: queryResults
             ) {

            addRow(qres[0], qres[1]);
        }
        System.out.println("The end");

    }
}
