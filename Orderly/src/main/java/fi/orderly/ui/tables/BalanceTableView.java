package fi.orderly.ui.tables;
import fi.orderly.dao.tables.ITable;
import fi.orderly.dao.tables.BalanceTable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

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
                BalanceTable b = new BalanceTable(id, db);
                items.add(b);
            }
        } catch (SQLException e) {
            //ignore
        }
    }

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
        BalanceTable bt = (BalanceTable) items.get(items.size()-1);
        int code = Integer.parseInt(bt.getProductCode());
        int productId = db.products.findIdByCode(code);
        int roomId = db.rooms.findIdByName(bt.getRoomName());
        int batch = Integer.parseInt(bt.getProductBatch());
        return db.balance.findId(roomId, productId, batch);
    }
}
