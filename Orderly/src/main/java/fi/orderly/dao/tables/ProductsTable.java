package fi.orderly.dao.tables;

import fi.orderly.logic.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Statement;

public class ProductsTable implements ITable {

    private String name = null;
    private String code = null;
    private String unit = null;
    private String temperature = null;
    private String room = null;
    Utils utils;

    public ProductsTable(int index, Statement statement) {
        utils = new Utils(statement);
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
        String query = "SELECT products.product, products.code, products.unit, products.temperature, rooms.room " +
                "FROM products, rooms WHERE products.id=" + id + " AND rooms.id=products.room_id";
        name = utils.getResultString(query, "product");
        code = utils.getResultString(query, "code");
        unit = utils.getResultString(query, "unit");
        temperature = utils.getResultString(query, "temperature");
        room = utils.getResultString(query, "room");
    }
}
