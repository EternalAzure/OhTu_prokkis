package fi.orderly.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Utils {

    final private Statement statement;
    public Utils(Statement statement) {
        this.statement = statement;
    }

    public int amountOfRooms() {
        String sql = "SELECT COUNT(*) FROM rooms";
        return getResultInt(sql, "COUNT(*)");
    }
    public int amountOfProducts() {
        String sql = "SELECT COUNT(*) FROM products";
        return getResultInt(sql, "COUNT(*)");
    }

    public Double getBalance(String room, String code, String batch) {
        String roomIdQuery = "SELECT id FROM rooms WHERE room='" + room + "'";
        String productIdQuery = "SELECT id FROM products WHERE code='" + code + "'";
        String roomId = getResultString(roomIdQuery, "id");
        String productId = getResultString(productIdQuery, "id");
        String sql = "SELECT amount FROM balance WHERE " +
                "room_id='" + roomId + "' AND " +
                "product_id='" + productId + "' AND " +
                "batch='" + batch + "'";
        return getResultDouble(sql, "amount");
    }

    public String getResultString(String query, String column) {
        try {
            ResultSet result = statement.executeQuery(query);
            result.next();
            return result.getString(column);
        } catch (SQLException e) {
            return "0";
        }
    }

    public int getResultInt(String query, String column) {
        try {
            ResultSet result = statement.executeQuery(query);
            result.next();
            System.out.println("getResInt::" + result.getInt(column));
            return result.getInt(column);
        } catch (SQLException e) {
            System.out.println("Faulty SQL was run in Utils.getResultInt()");
            e.printStackTrace();
            return 0;
        }
    }

    public double getResultDouble(String query, String column) {
        try {
            ResultSet result = statement.executeQuery(query);
            result.next();
            return result.getDouble(column);
        } catch (SQLException e) {
            System.out.println("Faulty SQL was run in Utils.getResultDouble()");
            return 0.0;
        }
    }

    public static boolean isEmpty(String[] input) {
        for (String string: input) {
            if (string == null || string.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInt(String input) {
        if (input.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(input); //int integer =
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String input) {
        if (input.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(input); //double d =
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNumeric(String[] textFields) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        int count = 0;
        for (String string: textFields) {

            if (pattern.matcher(string).matches()) {
                count++;
            }
            if (string.isEmpty()) {
                count++;
            }
        }
        return count == textFields.length;
    }

    public boolean hasItem(String value, String table, String column) {
        String query = "SELECT COUNT(" + column + ") FROM " + table + " WHERE " + column + "='" + value + "'";
        return getResultInt(query, "COUNT(" + column + ")") > 0;
    }

    public boolean hasRoom(String[] rooms) {

        for (String room: rooms) {
            String query = "SELECT COUNT(room) FROM rooms WHERE room='" + room + "'";
            try {
                ResultSet result = statement.executeQuery(query);
                result.next();
                if (result.getInt("COUNT(room)") == 0) {
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("Faulty SQL was run in Utils.hasRoom()");
            }
        }
        return true;
    }
}
