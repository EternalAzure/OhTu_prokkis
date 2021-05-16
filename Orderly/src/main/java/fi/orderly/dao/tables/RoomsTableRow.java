package fi.orderly.dao.tables;

import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.SQLException;

/**
 * Luokan tarkoitus on helpottaa taulukon täyttämistä RoomsTableView luokassa.
 * Jokainen instanssi vastaa yhtä riviä tietokannan taulussa balance. Tieto haetaan
 * instanssin luomisen yhteydessä fetchData() metodilla. Tieto esitetään
 * StringPropertyjen avulla. Luokka implementoi tyhjän rajapinnan ITable, koska
 * tarvittiin tapa asettaa kaikki *TableRow-luokat listaan ObservableList<ITable> items.
 * Rajapinta ei vaadi metodia fetchData koska se on parempi pitää yksityisenä ja kutsua
 * konstruktorista.
 */
public class RoomsTableRow implements ITable {

    private String name = null;
    private String temperature = null;
    DatabaseAccess db;

    public RoomsTableRow(int index, DatabaseAccess db) throws SQLException {
        this.db = db;
        fetchData(index);
        setRoomName(name);
        setRoomTemperature(temperature);
    }

    private StringProperty roomName;
    public void setRoomName(String value) {
        roomNameProperty().set(value);
    }
    public String getRoomName() {
        return roomNameProperty().get();
    }
    public StringProperty roomNameProperty() {
        if (roomName == null) {
            roomName = new SimpleStringProperty(this, "roomName");
        }
        return roomName;
    }

    private StringProperty roomTemperature;
    public void setRoomTemperature(String value) {
        roomTemperatureProperty().set(value);
    }
    public String getRoomTemperature() {
        return roomTemperatureProperty().get();
    }
    public StringProperty roomTemperatureProperty() {
        if (roomTemperature == null) {
            roomTemperature = new SimpleStringProperty(this, "null");
        }
        return roomTemperature;
    }

    private void fetchData(int id) throws SQLException {
        name = db.rooms.queryRoom(id)[0];
        temperature = db.rooms.queryRoom(id)[1];
    }
}
