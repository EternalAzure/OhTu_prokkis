package fi.orderly.logic.dbinterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RoomsInterface {

    Statement statement;
    public RoomsInterface(Statement statement) {
        this.statement = statement;
    }

    public void insertRoom(String room, double temperature) throws SQLException {
        String insert = "INSERT INTO rooms (room, temperature) VALUES ('" + room + "', " + temperature + ")";
        statement.execute(insert);
    }

    public void insertRoom(String room) throws SQLException {
        String insert = "INSERT INTO rooms (room) VALUES ('" + room + "')";
        statement.execute(insert);
    }

    public void deleteRoom(String room) throws SQLException {
        String delete = "DELETE FROM rooms WHERE room='" + room + "'";
        statement.execute(delete);
    }

    public void deleteRoom(int room) throws SQLException {
        String delete = "DELETE FROM rooms WHERE id=" + room;
        statement.execute(delete);
    }

    public int countRoom(String room) throws SQLException {
        String query = "SELECT COUNT(*) FROM rooms WHERE room='" + room + "'";
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int findIdByName(String room) throws SQLException {
        String select = "SELECT id FROM rooms WHERE room='" + room + "'";
        ResultSet resultSet = statement.executeQuery(select);
        resultSet.next();
        return resultSet.getInt("id");
    }

    public String findRoomById(int id) throws SQLException {
        String select = "SELECT room FROM rooms WHERE id=" + id;
        ResultSet resultSet = statement.executeQuery(select);
        resultSet.next();
        return resultSet.getString("room");
    }

    public int size() {
        try{
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM rooms");
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void truncate() {
        try {
            statement.execute("TRUNCATE TABLE rooms;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
