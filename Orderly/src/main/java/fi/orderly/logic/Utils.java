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
            return null;
        }
    }
    public int getResultInt(String query, String column) {
        try {
            ResultSet result = statement.executeQuery(query);
            result.next();
            return result.getInt(column);
        } catch (SQLException e) {
            return 0;
        }
    }
    public double getResultDouble(String query, String column) {
        try {
            ResultSet result = statement.executeQuery(query);
            result.next();
            return result.getDouble(column);
        } catch (SQLException e) {
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
    public static boolean notInt(String input) {
        try {
            Integer.parseInt(input); //int integer =
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }
    public boolean notInt(String[] input) {
        for (String s: input) {
            try {
                Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }
    public static boolean notDouble(String input) {
        try {
            Double.parseDouble(input); //double d =
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
    public boolean notDouble(String[] input) {
        for (String s: input) {
            try {
                Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }
    public static boolean isNumeric(String value) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (pattern.matcher(value).matches()) {
            return true;
        }
        return false;
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
}
