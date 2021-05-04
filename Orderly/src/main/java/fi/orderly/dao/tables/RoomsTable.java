package fi.orderly.dao.tables;

import fi.orderly.logic.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.sql.Connection;

public class RoomsTable implements ITable {

    private String name = null;
    private String temperature = null;
    Utils utils;

    public RoomsTable(int index, Connection connection) {
        utils = new Utils(connection);
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
        String query = "SELECT room, temperature FROM rooms WHERE id=" + id;
        name = utils.getResultString(query, "room");
        temperature = utils.getResultString(query, "temperature");
    }
}
