package fi.orderly.dao;

import fi.orderly.logic.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Statement;

public class BalanceTable implements ITable{

    private String room = null;
    private String name = null;
    private String code = null;
    private String batch = null;
    private String amount = null;
    Utils utils;

    public BalanceTable(int index, Statement statement) {
        utils = new Utils(statement);
        fetchData(index);
        setRoomName(room);
        setProductName(name);
        setProductCode(code);
        setProductBatch(batch);
        setProductAmount(amount);
    }

    private StringProperty roomName;
    public void setRoomName(String value) {
        roomNameProperty().set(value);
    }
    public String getRoomName() {
        return roomNameProperty().get();
    }
    public StringProperty roomNameProperty() {
        if (roomName == null) {
            roomName = new SimpleStringProperty(this, "roomName");
        }
        return roomName;
    }

    private StringProperty productName;
    public void setProductName(String value) {
        productNameProperty().set(value);
    }
    public String getProductName() {
        return productNameProperty().get();
    }
    public StringProperty productNameProperty() {
        if (productName == null) productName = new SimpleStringProperty(this, "productName");
        return productName;
    }

    private StringProperty productCode;
    public void setProductCode(String value) {
        productCodeProperty().set(value);
    }
    public String getProductCode() {
        return productCodeProperty().get();
    }
    public StringProperty productCodeProperty() {
        if (productCode == null) {
            productCode = new SimpleStringProperty(this, "productCode");
        }
        return productCode;
    }

    private StringProperty productBatch;
    public void setProductBatch(String value) {
        productBatchProperty().set(value);
    }
    public String getProductBatch() {
        return productBatchProperty().get();
    }
    public StringProperty productBatchProperty() {
        if (productBatch == null) {
            productBatch = new SimpleStringProperty(this, "productUnit");
        }
        return productBatch;
    }

    private StringProperty productAmount;
    public void setProductAmount(String value) {
        productAmountProperty().set(value);
    }
    public String getProductAmount() {
        return productAmountProperty().get();
    }
    public StringProperty productAmountProperty() {
        if (productAmount == null) {
            productAmount = new SimpleStringProperty(this, "0");
        }
        return productAmount;
    }


    private void fetchData(int id) {
        String query = "SELECT products.product, products.code, balance.batch, balance.amount, rooms.room " +
                "FROM products, rooms, balance WHERE balance.id=" + id + " AND products.id=product_id AND rooms.id=balance.room_id";
        name = utils.getResultString(query, "product");
        code = utils.getResultString(query, "code");
        batch = utils.getResultString(query, "batch");
        amount = utils.getResultString(query, "amount");
        room = utils.getResultString(query, "room");
    }
}
