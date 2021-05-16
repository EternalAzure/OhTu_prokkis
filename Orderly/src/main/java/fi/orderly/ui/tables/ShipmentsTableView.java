package fi.orderly.ui.tables;

import fi.orderly.dao.tables.ITable;
import fi.orderly.dao.tables.ShipmentsTableRow;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;

/**
 * Luokan tarkoitus on näyttää käyttäjälle ikkuna, jossa näkyy tietokannan taulu.
 * Ikkunaa voi rullata alaspäin ja luokka lataa lisää rivejä tauluun 50 kerrallaan.
 */
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
                ShipmentsTableRow s = new ShipmentsTableRow(id, db);
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
        ShipmentsTableRow dt = (ShipmentsTableRow) items.get(items.size()-1);
        String name = dt.getProductName();
        int productId = db.products.findIdByName(name);
        int number = Integer.parseInt(dt.getProductName());
        int batch = Integer.parseInt(dt.getBatchNumber());
        return db.shipments.findId(number, productId, batch);
    }
}
