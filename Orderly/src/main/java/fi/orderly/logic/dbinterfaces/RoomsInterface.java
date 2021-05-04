package fi.orderly.logic.dbinterfaces;

import java.sql.*;

public class RoomsInterface {

    Connection connection;
    public RoomsInterface(Connection connection) {
        this.connection = connection;
    }

    public void insertRoom(String room, double temperature) throws SQLException {
        String insert = "INSERT INTO rooms (room, temperature) VALUES (?, ?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setString(1, room);
        sql.setDouble(2, temperature);
        sql.executeUpdate();
    }

    public void insertRoom(String room) throws SQLException {
        String insert = "INSERT INTO rooms (room) VALUES (?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setString(1, room);
        sql.executeUpdate();
    }

    public void deleteRoom(String room) throws SQLException {
        String delete = "DELETE FROM rooms WHERE room=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setString(1, room);
        sql.executeUpdate();
    }

    public void deleteRoom(int room) throws SQLException {
        String delete = "DELETE FROM rooms WHERE id=" + room;
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, room);
        sql.executeUpdate();
    }

    public int countRoom(String room) throws SQLException {
        String query = "SELECT COUNT(*) FROM rooms WHERE room=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setString(1, room);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("COUNT(*)");
    }

    public int findIdByName(String room) throws SQLException {
        String select = "SELECT id FROM rooms WHERE room=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setString(1, room);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getInt("id");
    }

    public String findRoomById(int id) throws SQLException {
        String select = "SELECT room FROM rooms WHERE id=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        return resultSet.getString("room");
    }

    public int size() {
        try{
            PreparedStatement sql = connection.prepareStatement("SELECT COUNT(*) FROM rooms");
            ResultSet resultSet = sql.executeQuery();
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void truncate() {
        try {
            PreparedStatement sql = connection.prepareStatement("TRUNCATE TABLE rooms");
            sql.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
