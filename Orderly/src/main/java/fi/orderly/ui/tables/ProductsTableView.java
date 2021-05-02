package fi.orderly.ui.tables;
import fi.orderly.dao.tables.ProductsTable;
import fi.orderly.dao.tables.ITable;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductsTableView extends TableViewInfiniteScrolling {

    @Override
    void addItems() {
        int limit = items.size() + 30;
        for (int i = items.size(); i < limit; i++) {
            if (i >= db.products.size()) {
                break;
            }
            ProductsTable p = new ProductsTable(items.size() + 1, statement);
            if (p.getProductCode().isEmpty()) {
                continue;
            }
            items.add(p);
        }
    }

    @Override
    void setUp() {
        TableColumn<ITable, String> name = new TableColumn<>("Product name");
        name.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<ITable, String> code = new TableColumn<>("Product code");
        code.setCellValueFactory(new PropertyValueFactory<>("productCode"));

        TableColumn<ITable, String> unit = new TableColumn<>("Unit");
        unit.setCellValueFactory(new PropertyValueFactory<>("productUnit"));

        TableColumn<ITable, String> temperature = new TableColumn<>("Temperature");
        temperature.setCellValueFactory(new PropertyValueFactory<>("productTemp"));

        TableColumn<ITable, String> room = new TableColumn<>("Default storage");
        room.setCellValueFactory(new PropertyValueFactory<>("defaultRoom"));
        table.getColumns().setAll(name, code, unit, temperature, room);
    }
}
