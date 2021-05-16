package fi.orderly.dao.tables;

import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Luokan tarkoitus on helpottaa taulukon täyttämistä BalanceTableView luokassa.
 * Jokainen instanssi vastaa yhtä riviä tietokannan taulussa balance. Tieto haetaan
 * instanssin luomisen yhteydessä fetchData() metodilla. Tieto esitetään
 * StringPropertyjen avulla. Luokka implementoi tyhjän rajapinnan ITable, koska
 * tarvittiin tapa asettaa kaikki *TableRow-luokat listaan ObservableList<ITable> items.
 * Rajapinta ei vaadi metodia fetchData koska se on parempi pitää yksityisenä ja kutsua
 * konstruktorista.
 */
public class BalanceTableRow implements ITable {

    private String name = null;
    private String code = null;
    private String batch = null;
    private String amount = null;
    private String room = null;
    DatabaseAccess db;

    public BalanceTableRow(int index, DatabaseAccess db) throws SQLException {
        this.db = db;
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
    public StringProperty productAmountProperty() {
        if (productAmount == null) {
            productAmount = new SimpleStringProperty(this, "0");
        }
        return productAmount;
    }

    private void fetchData(int id) throws SQLException {
        PreparedStatement sql = db.tableBalance(id);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        name = resultSet.getString("product");
        code = String.valueOf(resultSet.getInt("code"));
        batch = String.valueOf(resultSet.getInt("batch"));
        amount = String.valueOf(resultSet.getDouble("amount"));
        room = resultSet.getString("room");
    }
}
