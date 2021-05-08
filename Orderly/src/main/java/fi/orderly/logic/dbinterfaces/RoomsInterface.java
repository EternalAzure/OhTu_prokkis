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
        sql.close();
    }

    public void insertRoom(String room) throws SQLException {
        String insert = "INSERT INTO rooms (room) VALUES (?)";
        PreparedStatement sql = connection.prepareStatement(insert);
        sql.setString(1, room);
        sql.executeUpdate();
        sql.close();
    }

    public void deleteRoom(String room) throws SQLException {
        String delete = "DELETE FROM rooms WHERE room=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setString(1, room);
        sql.executeUpdate();
        sql.close();
    }

    public void deleteRoom(int room) throws SQLException {
        String delete = "DELETE FROM rooms WHERE id=?";
        PreparedStatement sql = connection.prepareStatement(delete);
        sql.setInt(1, room);
        sql.executeUpdate();
        sql.close();
    }

    public String[] queryRoom(int id) throws SQLException {
        String select = "SELECT room, temperature FROM rooms WHERE id=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();

        String[] result = new String[2];
        result[1] = Double.toString(resultSet.getDouble("temperature"));
        if (resultSet.wasNull()) {
            result[1] = "-";
        }
        result[0] = resultSet.getString("room");
        sql.close();
        return result;

    }

    public boolean foundRoom(String name) throws SQLException {
        String query = "SELECT COUNT(*) FROM rooms WHERE room=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setString(1, name);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        boolean result = resultSet.getInt("COUNT(*)") > 0;
        sql.close();
        return result;
    }

    public int countRoom(String room) throws SQLException {
        String query = "SELECT COUNT(*) FROM rooms WHERE room=?";
        PreparedStatement sql = connection.prepareStatement(query);
        sql.setString(1, room);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        int result = resultSet.getInt("COUNT(*)");
        sql.close();
        return result;
    }

    public int findIdByName(String room) throws SQLException {
        String select = "SELECT id FROM rooms WHERE room=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setString(1, room);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        int result = resultSet.getInt("id");
        sql.close();
        return result;
    }

    public String findRoomById(int id) throws SQLException {
        String select = "SELECT room FROM rooms WHERE id=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        ResultSet resultSet = sql.executeQuery();
        resultSet.next();
        String result = resultSet.getString("room");
        sql.close();
        return result;
    }

    public int size() {
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT COUNT(*) FROM rooms");
            ResultSet resultSet = sql.executeQuery();
            resultSet.next();
            int result = resultSet.getInt("COUNT(*)");
            sql.close();
            return result;
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
