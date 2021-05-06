package fi.orderly.dao.tables;

import fi.orderly.logic.Utils;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductsTable implements ITable {

    private String name = null;
    private String code = null;
    private String unit = null;
    private String temperature = null;
    private String room = null;
    DatabaseAccess db;

    public ProductsTable(int index, Connection connection) {
        db = new DatabaseAccess(connection);
        fetchData(index);
        setProductName(name);
        setProductCode(code);
        setProductUnit(unit);
        setProductTemp(temperature);
        setDefaultRoom(room);
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

    private StringProperty productUnit;
    public void setProductUnit(String value) {
        productUnitProperty().set(value);
    }
    public String getProductUnit() {
        return productUnitProperty().get();
    }
    public StringProperty productUnitProperty() {
        if (productUnit == null) {
            productUnit = new SimpleStringProperty(this, "productUnit");
        }
        return productUnit;
    }

    private StringProperty productTemp;
    public void setProductTemp(String value) {
        productTempProperty().set(value);
    }
    public String getProductTemp() {
        return productTempProperty().get();
    }
    public StringProperty productTempProperty() {
        if (productTemp == null) {
            productTemp = new SimpleStringProperty(this, "productTemp");
        }
        return productTemp;
    }

    private StringProperty defaultRoom;
    public void setDefaultRoom(String value) {
        defaultRoomProperty().set(value);
    }
    public String getDefaultRoom() {
        return defaultRoomProperty().get();
    }
    public StringProperty defaultRoomProperty() {
        if (defaultRoom == null) {
            defaultRoom = new SimpleStringProperty(this, "null");
        }
        return defaultRoom;
    }

    private void fetchData(int id) {
        try {
            PreparedStatement sql = db.tableProducts(id);
            ResultSet resultSet = sql.executeQuery();
            resultSet.next();
            name = String.valueOf(resultSet.getString("product"));
            code = String.valueOf(resultSet.getInt("code"));
            unit = String.valueOf(resultSet.getString("unit"));
            temperature = String.valueOf(resultSet.getDouble("temperature"));
            room = resultSet.getString("room");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
