package fi.orderly.dao.tables;

import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Luokan tarkoitus on helpottaa taulukon täyttämistä DeliveriesTableView luokassa.
 * Jokainen instanssi vastaa yhtä riviä tietokannan taulussa balance. Tieto haetaan
 * instanssin luomisen yhteydessä fetchData() metodilla. Tieto esitetään
 * StringPropertyjen avulla. Luokka implementoi tyhjän rajapinnan ITable, koska
 * tarvittiin tapa asettaa kaikki *TableRow-luokat listaan ObservableList<ITable> items.
 * Rajapinta ei vaadi metodia fetchData koska se on parempi pitää yksityisenä ja kutsua
 * konstruktorista.
 */
public class DeliveriesTableRow implements ITable {

    private String number;
    private String product;
    private String amount;

    DatabaseAccess db;
    public DeliveriesTableRow(int index, DatabaseAccess db) throws SQLException {
        this.db = db;
        fetchData(index);
        setDeliveryNumber(number);
        setProductName(product);
        setExpectedAmount(amount);
    }

    private StringProperty deliveryNumber;
    public void setDeliveryNumber(String value) {
        deliveryNumberProperty().set(value);
    }
    public String getDeliveryNumber() {
        return deliveryNumberProperty().get();
    }
    public StringProperty deliveryNumberProperty() {
        if (deliveryNumber == null) {
            deliveryNumber = new SimpleStringProperty(this, "deliveryNumber");
        }
        return deliveryNumber;
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

    private void fetchData(int id) throws  SQLException {
        PreparedStatement sql = db.tableDeliveries(id);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        number = String.valueOf(resultSet.getInt("number"));
        product = resultSet.getString("product");
        amount = String.valueOf(resultSet.getDouble("amount"));
    }
}
