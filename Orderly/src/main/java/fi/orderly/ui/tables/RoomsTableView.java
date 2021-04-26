package fi.orderly.ui.tables;

import fi.orderly.dao.ITable;
import fi.orderly.dao.RoomsTable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class RoomsTableView extends TableViewInfiniteScrolling {


    @Override
     void addItems() {
        int limit = items.size() + 10;
        for (int i = items.size(); i < limit; i++) {
            if (i >= utils.tableSizeRooms()) {
                break;
            }

            RoomsTable r = new RoomsTable(items.size() + 1, statement);
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
