package fi.orderly.ui.tables;

import fi.orderly.dao.tables.DeliveriesTableRow;
import fi.orderly.dao.tables.ITable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;

/**
 * Luokan tarkoitus on näyttää käyttäjälle ikkuna, jossa näkyy tietokannan taulu.
 * Ikkunaa voi rullata alaspäin ja luokka lataa lisää rivejä tauluun 50 kerrallaan.
 */
public class DeliveriesTableView extends TableViewInfiniteScrolling {

    @Override
    void addItems() {
        int[] ids;
        try {
            if (items.size() == 0) {
                ids = db.deliveries.load50(0);
            } else {
                int id = getDbIndexOfLastItemOnItems();
                ids = db.deliveries.load50(id);
            }
            for (Integer id: ids) {
                DeliveriesTableRow d = new DeliveriesTableRow(id, db);
                items.add(d);
            }
        } catch (SQLException e) {
            //ignore
        }
    }

    /**
     * PropertyValueFactory() is convenience implementation of Callback interface,
     * designed specifically for use within the TableColumn cell value factory.
     * In this case 'deliveryNumber' string is used as a reference to an assumed
     * deliveryNumberProperty() method in the ITable interface type
     * (which is the interface type of the TableView items list. Defined in
     * TableViewInfiniteScrolling parent class).
     * deliveryNumberProperty() is found in dao.tables.DeliveriesTable.
     *
     * This implementation is found in all *.tables.* classes
     * Author: EternalAzure 10.5.2021
     */

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

    private int getDbIndexOfLastItemOnItems() throws SQLException {
        DeliveriesTableRow dt = (DeliveriesTableRow) items.get(items.size()-1);
        int name = Integer.parseInt(dt.getProductName());
        int number = Integer.parseInt(dt.getProductName());
        return db.deliveries.findId(number, name);
    }
}
