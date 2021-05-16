package fi.orderly.ui.tables;

import fi.orderly.dao.tables.ITable;
import fi.orderly.dao.tables.BalanceTableRow;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;

/**
 * Luokan tarkoitus on näyttää käyttäjälle ikkuna, jossa näkyy tietokannan taulu.
 * Ikkunaa voi rullata alaspäin ja luokka lataa lisää rivejä tauluun 50 kerrallaan.
 */
public class BalanceTableView extends TableViewInfiniteScrolling {

    @Override
    void addItems() {
        int[] ids;
        try {
            if (items.size() == 0) {
                ids = db.balance.load50(0);
            } else {
                int id = getDbIndexOfLastItemOnItems();
                ids = db.balance.load50(id);
            }
            for (Integer id: ids) {
                BalanceTableRow b = new BalanceTableRow(id, db);
                items.add(b);
            }
        } catch (SQLException e) {
            //ignore
        }
    }

    /**
     * PropertyValueFactory() is convenience implementation of Callback interface,
     * designed specifically for use within the TableColumn cell value factory.
     * In this case 'roomName' string is used as a reference to an assumed
     * roomNameProperty() method in the ITable interface type
     * (which is the interface type of the TableView items list. Defined in
     * TableViewInfiniteScrolling parent class).
     * roomNameProperty() is found in dao.tables.RoomsTableRow.
     *
     * This implementation is found in all *.tables.* classes
     * Author: EternalAzure 10.5.2021
     */
    @Override
    void setUp() {
        TableColumn<ITable, String> room = new TableColumn<>("Room Name");
        room.setCellValueFactory(new PropertyValueFactory<>("roomName"));

        TableColumn<ITable, String> productName = new TableColumn<>("Product name");
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<ITable, String> code = new TableColumn<>("Product code");
        code.setCellValueFactory(new PropertyValueFactory<>("productCode"));

        TableColumn<ITable, String> batch = new TableColumn<>("Batch");
        batch.setCellValueFactory(new PropertyValueFactory<>("productBatch"));

        TableColumn<ITable, String> amount = new TableColumn<>("amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("productAmount"));

        table.getColumns().setAll(room, productName, code, batch, amount);
    }

    private int getDbIndexOfLastItemOnItems() throws SQLException {
        BalanceTableRow bt = (BalanceTableRow) items.get(items.size()-1);
        int code = Integer.parseInt(bt.getProductCode());
        int productId = db.products.findIdByCode(code);
        int roomId = db.rooms.findIdByName(bt.getRoomName());
        int batch = Integer.parseInt(bt.getProductBatch());
        return db.balance.findId(roomId, productId, batch);
    }
}
