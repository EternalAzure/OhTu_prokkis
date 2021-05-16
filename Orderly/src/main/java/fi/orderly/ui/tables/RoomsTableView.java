package fi.orderly.ui.tables;

import fi.orderly.dao.tables.ITable;
import fi.orderly.dao.tables.RoomsTableRow;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;

/**
 * Luokan tarkoitus on näyttää käyttäjälle ikkuna, jossa näkyy tietokannan taulu.
 * Ikkunaa voi rullata alaspäin ja luokka lataa lisää rivejä tauluun 50 kerrallaan.
 */
public class RoomsTableView extends TableViewInfiniteScrolling {

    @Override
     void addItems() {
        int[] ids;
        try {
            if (items.size() == 0) {
                ids = db.rooms.load50(0);
            } else {
                int id = getDbIndexOfLastItemOnItems();
                ids = db.rooms.load50(id);
            }
            for (Integer id: ids) {
                RoomsTableRow r = new RoomsTableRow(id, db);
                items.add(r);
            }
        } catch (SQLException e) {
            //ignore
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

    private int getDbIndexOfLastItemOnItems() throws SQLException {
        RoomsTableRow rt = (RoomsTableRow) items.get(items.size()-1);
        String name = rt.getRoomName();
        return db.rooms.findIdByName(name);
    }
}
