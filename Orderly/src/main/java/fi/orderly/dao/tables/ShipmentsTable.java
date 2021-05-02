package fi.orderly.dao.tables;

import fi.orderly.logic.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Statement;

public class ShipmentsTable implements ITable {

    private String number;
    private String name;
    private String batch;
    private String amount;

    Utils utils;
    public ShipmentsTable(int index, Statement statement) {
        utils = new Utils(statement);
        fetchData(index);
        setShipmentNumber(number);
        setProductName(name);
        setBatchNumber(batch);
        setExpectedAmount(amount);
    }

    private StringProperty shipmentNumber;
    public void setShipmentNumber(String value) {
        shipmentNumberProperty().set(value);
    }
    public String getShipmentNumber() {
        return shipmentNumberProperty().get();
    }
    public StringProperty shipmentNumberProperty() {
        if (shipmentNumber == null) {
            shipmentNumber = new SimpleStringProperty(this, "shipmentNumber");
        }
        return shipmentNumber;
    }

    private StringProperty productName;
    public void setProductName(String value) {
        productNameProperty().set(value);
    }
    public String getProductName() {
        return productNameProperty().get();
    }
    public StringProperty productNameProperty() {
        if (productName == null) {
            productName = new SimpleStringProperty(this, "productName");
        }
        return productName;
    }

    private StringProperty batchNumber;
    public void setBatchNumber(String value) {
        batchNumberProperty().set(value);
    }
    public String getBatchNumber() {
        return batchNumberProperty().get();
    }
    public StringProperty batchNumberProperty() {

        if (batchNumber == null) {
            batchNumber = new SimpleStringProperty(this, "batchNumber");
        }
        return batchNumber;
    }

    private StringProperty expectedAmount;
    public void setExpectedAmount(String value) {
        expectedAmountProperty().set(value);
    }
    public String getExpectedAmount() {
        return expectedAmountProperty().get();
    }
    public StringProperty expectedAmountProperty() {
        if (expectedAmount == null) {
            expectedAmount = new SimpleStringProperty(this, "expectedAmount");
        }
        return expectedAmount;
    }

    private void fetchData(int id) {
        String query = "SELECT shipments.number, products.product, shipments.batch, shipments.amount " +
                "FROM products, shipments WHERE shipments.id=" + id + " AND products.id=product_id";
        number = utils.getResultString(query, "number");
        name = utils.getResultString(query, "product");
        batch = utils.getResultString(query, "batch");
        amount = utils.getResultString(query, "amount");
    }
}
