package fi.orderly.ui.tables;

import fi.orderly.dao.tables.ITable;
import fi.orderly.dao.tables.RoomsTable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class RoomsTableView extends TableViewInfiniteScrolling {


    @Override
     void addItems() {
        int limit = items.size() + 30;
        for (int i = items.size(); i < limit; i++) {
            if (i >= db.rooms.size()) {
                break;
            }

            RoomsTable r = new RoomsTable(items.size() + 1, connection);
            if (r.getRoomName() == null || r.getRoomName().isEmpty()) {
                continue;
            }
            items.add(r);
        }
    }

    @Override
    void setUp() {
        TableColumn<ITable, String> name = new TableColumn<>("Room Name");
        name.setCellValueFactory(new PropertyValueFactory<>("roomName"));

        TableColumn<ITable, String> temperature = new TableColumn<>("Room temperature");
        temperature.setCellValueFactory(new PropertyValueFactory<>("roomTemperature"));
        table.getColumns().setAll(name, temperature);
    }
}
