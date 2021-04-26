package fi.orderly.ui.tables;

import fi.orderly.dao.ITable;
import fi.orderly.dao.RoomsTable;
import fi.orderly.dao.ShipmentsTable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class ShipmentsTableView extends TableViewInfiniteScrolling{

    @Override
    void addItems() {
        int limit = items.size() + 10;
        for (int i = items.size(); i < limit; i++) {
            if (i >= utils.tableSizeShipments()) {
                break;
            }

            ShipmentsTable r = new ShipmentsTable(items.size() + 1, statement);
            items.add(r);
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
}
