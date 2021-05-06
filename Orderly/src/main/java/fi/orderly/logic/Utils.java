package fi.orderly.logic;

import java.sql.*;
import java.util.regex.Pattern;

public class Utils {

    final private Connection connection;
    public Utils(Connection connection) {
        this.connection = connection;
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
    public static boolean notInt(String[] input) {
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
    public static boolean notDouble(String[] input) {
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
