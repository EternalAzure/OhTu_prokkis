package fi.orderly.dao.tables;

import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShipmentsTable implements ITable {

    private String number;
    private String name;
    private String batch;
    private String amount;

    DatabaseAccess db;

    public ShipmentsTable(int index, Connection connection) {
        db = new DatabaseAccess(connection);
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
        try {
            PreparedStatement sql = db.tableShipments(id);
            ResultSet resultSet = sql.executeQuery();
            resultSet.next();
            number = String.valueOf(resultSet.getInt("number"));
            name = resultSet.getString("product");
            batch = String.valueOf(resultSet.getInt("batch"));
            amount = String.valueOf(resultSet.getDouble("amount"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
