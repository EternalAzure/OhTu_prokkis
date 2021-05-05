package fi.orderly.dao.tables;

import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Connection;
import java.sql.SQLException;

public class RoomsTable implements ITable {

    private String name = null;
    private String temperature = null;
    DatabaseAccess db;

    public RoomsTable(int index, Connection connection) {
        db = new DatabaseAccess(connection);
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

    private void fetchData(int id) {
        try {
            name = db.rooms.queryRoom(id)[0];
            temperature = db.rooms.queryRoom(id)[1];
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
