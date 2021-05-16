package fi.orderly.ui.tables;
import fi.orderly.dao.tables.ProductsTable;
import fi.orderly.dao.tables.ITable;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class ProductsTableView extends TableViewInfiniteScrolling {

    @Override
    void addItems() {
        int[] ids;
        try {
            if (items.size() == 0) {
                ids = db.products.load50(0);
            } else {
                int id = getDbIndexOfLastItemOnItems();
                ids = db.products.load50(id);
            }
            for (Integer id: ids) {
                ProductsTable p = new ProductsTable(id, db);
                items.add(p);
            }
        } catch (SQLException e) {
            //ignore
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

    private int getDbIndexOfLastItemOnItems() throws SQLException {
        ProductsTable pt = (ProductsTable) items.get(items.size()-1);
        int code = Integer.parseInt(pt.getProductCode());
        return db.products.findIdByCode(code);
    }
}
