import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Utils {

    private Statement statement;
    public Utils(Statement statement){
        this.statement = statement;
    }

    public int amountOfRooms(){
        String sql = "SELECT COUNT(*) FROM rooms";
        return getResultInt(sql, "COUNT(*)");
    }
    public int amountOfProducts(){
        String sql = "SELECT COUNT(*) FROM products";
        return getResultInt(sql, "COUNT(*)");
    }

    public Double getBalance(String room, String code, String batch) {
        String room_idQuery = "SELECT id FROM rooms WHERE room='"+room+"'";
        String product_idQuery = "SELECT id FROM products WHERE code='"+code+"'";
        String room_id = getResultString(room_idQuery, "id");
        String product_id = getResultString(product_idQuery, "id");
        String sql = "SELECT amount FROM balance WHERE " +
                "room_id='"+room_id+"' AND " +
                "product_id='"+product_id+"' AND " +
                "batch='"+batch+"'";
        return getResultDouble(sql, "amount");
    }
    public String getResultString(String query, String column){
        try{
            ResultSet result = statement.executeQuery(query);
            while (result.next()){
                return result.getString(column);
            }
        }catch (SQLException e){e.printStackTrace();}

        return "0";
    }
    public int getResultInt(String query, String column){
        try{
            ResultSet result = statement.executeQuery(query);
            while (result.next()){
                return result.getInt(column);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public double getResultDouble(String query, String column){
        try{
            ResultSet result = statement.executeQuery(query);
            while (result.next()){
                return result.getDouble(column);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0.0;
    }

    public static boolean isEmpty(String[] input){
        for (String string: input) {
            if(string == null || string.isEmpty())
                return true;
        }
        return false;
    }

    public static boolean isInt(String input){
        if (input.isEmpty()) return false;
        try {
            int integer = Integer.parseInt(input);
            return true;
        }catch (NumberFormatException e){ }
        return false;
    }
    public static boolean isDouble(String input){
        if (input.isEmpty()) return false;
        try {
            double d = Double.parseDouble(input);
            return true;
        }catch (NumberFormatException e){ }
        return false;
    }

    public static boolean isNumeric(String[] textFields) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        int count = 0;
        for (String string: textFields) {

            if(pattern.matcher(string).matches()) count++;
            if(string.isEmpty()) count++;
        }

        if (count == textFields.length)
            return true;
        return false;
    }

    public boolean hasItem(String value, String table, String column){
        String query = "SELECT COUNT("+column+") FROM "+table+" WHERE "+column+"='"+value+"'";
        if (getResultInt(query, "COUNT("+column+")") > 0) return true;
        return false;
    }

    public boolean hasRoom(String room){
        String query = "SELECT COUNT(room) FROM rooms WHERE room='"+room+"'";
        try {
            ResultSet result = statement.executeQuery(query);
            while(result.next()){
                if (result.getInt("COUNT(room)") > 0){
                    return true;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
