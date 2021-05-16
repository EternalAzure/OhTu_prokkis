package fi.orderly.ui.tables;

import fi.orderly.dao.tables.DeliveriesTable;
import fi.orderly.dao.tables.ITable;
import fi.orderly.dao.tables.ShipmentsTable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class ShipmentsTableView extends TableViewInfiniteScrolling{

    @Override
    void addItems() {
        int[] ids;
        try {
            if (items.size() == 0) {
                ids = db.shipments.load50(0);
            } else {
                int id = getDbIndexOfLastItemOnItems();
                ids = db.shipments.load50(id);
            }
            for (Integer id: ids) {
                ShipmentsTable s = new ShipmentsTable(id, connection);
                items.add(s);
            }
        } catch (SQLException e) {
            //ignore
        }
    }

    @Override
    void setUp() {
        TableColumn<ITable, String> number = new TableColumn<>("Shipment number");
        number.setCellValueFactory(new PropertyValueFactory<>("shipmentNumber"));

        TableColumn<ITable, String> product = new TableColumn<>("Product name");
        product.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<ITable, String> batch = new TableColumn<>("Batch number");
        batch.setCellValueFactory(new PropertyValueFactory<>("batchNumber"));

        TableColumn<ITable, String> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("expectedAmount"));
        table.getColumns().setAll(number, product, batch, amount);
    }

    private int getDbIndexOfLastItemOnItems() throws SQLException {
        ShipmentsTable dt = (ShipmentsTable) items.get(items.size()-1);
        String name = dt.getProductName();
        int productId = db.products.findIdByName(name);
        int number = Integer.parseInt(dt.getProductName());
        int batch = Integer.parseInt(dt.getBatchNumber());
        return db.shipments.findId(number, productId, batch);
    }
}
