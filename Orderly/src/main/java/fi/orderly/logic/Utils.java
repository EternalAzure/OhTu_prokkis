package fi.orderly.logic;

/**
 * Luokka tarjoaa työkaluja syötteiden validointiin.
 */
public class Utils {

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
            Integer.parseInt(input);
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
}
