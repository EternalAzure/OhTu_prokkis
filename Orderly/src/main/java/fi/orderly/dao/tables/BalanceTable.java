package fi.orderly.dao.tables;

import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BalanceTable implements ITable {

    private String name = null;
    private String code = null;
    private String batch = null;
    private String amount = null;
    private String room = null;
    DatabaseAccess db;

    public BalanceTable(int index, Connection connection) {
        db = new DatabaseAccess(connection);
        fetchData(index);
        setProductName(name);
        setProductCode(code);
        setProductBatch(batch);
        setProductAmount(amount);
        setRoomName(room);
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
        if (productName == null) {
            productName = new SimpleStringProperty(this, "productName");
        }
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
        try {
            PreparedStatement sql = db.tableBalance(id);
            ResultSet resultSet = sql.executeQuery();
            resultSet.next();
            name = resultSet.getString("product");
            code = String.valueOf(resultSet.getInt("code"));
            batch = String.valueOf(resultSet.getInt("batch"));
            amount = String.valueOf(resultSet.getDouble("amount"));
            room = resultSet.getString("room");
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }
}
