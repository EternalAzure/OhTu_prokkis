package fi.orderly.ui.tables;

import fi.orderly.dao.tables.DeliveriesTable;
import fi.orderly.dao.tables.ITable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class DeliveriesTableView extends TableViewInfiniteScrolling{

    @Override
    void addItems() {
        int limit = items.size() + 30;
        for (int i = items.size(); i < limit; i++) {
            if (i >= db.deliveries.size()) {
                break;
            }
            DeliveriesTable s = new DeliveriesTable(items.size() + 1, statement);
            if (s.getDeliveryNumber().isEmpty()) {
                continue;
            }
            items.add(s);
        }
    }

    @Override
    void setUp() {
        TableColumn<ITable, String> number = new TableColumn<>("Delivery number");
        number.setCellValueFactory(new PropertyValueFactory<>("deliveryNumber"));

        TableColumn<ITable, String> product = new TableColumn<>("Product name");
        product.setCellValueFactory(new PropertyValueFactory<>("productName"));


        TableColumn<ITable, String> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("expectedAmount"));
        table.getColumns().setAll(number, product, amount);
    }
}
