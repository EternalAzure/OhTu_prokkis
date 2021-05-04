package fi.orderly.dao.tables;

import fi.orderly.logic.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Connection;

public class DeliveriesTable implements ITable {

    private String number;
    private String product;
    private String amount;

    Utils utils;
    public DeliveriesTable(int index, Connection connection) {
        utils = new Utils(connection);
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

    private void fetchData(int id) {
        String query = "SELECT deliveries.number, products.product, deliveries.amount " +
                "FROM products, deliveries WHERE deliveries.id=" + id + " AND products.id=product_id";
        number = utils.getResultString(query, "number");
        product = utils.getResultString(query, "product");
        amount = utils.getResultString(query, "amount");
    }
}
