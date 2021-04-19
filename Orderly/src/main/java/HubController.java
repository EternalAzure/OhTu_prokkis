import javafx.application.Platform;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class HubController {

    final private Statement statement;
    public HubController(String database){
        this.statement = ServerConnection.createConnection(database);
    }

    final private String numeric = "Check your numbers";
    final private String empty = "Non allowed empty value";

    //Functionality
    public static void logout(Stage currentWindow){
        try {
            new Login().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentWindow.close();
    }
    public static void exit(){
        Platform.exit();
    }
    public String addRoom(String name, String temperature){
        if (name.isEmpty()) return "Name cannot be empty";
        if (!isNumeric(new String[] {temperature})) return empty;

        String sql = "INSERT INTO rooms (room, temperature) VALUES ('"+name+"', NULLIF('"+temperature+"', ''));";
        try {
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.addRoom()");
            if (e.getErrorCode() == 1062){
                return "'"+name+"' already exists";
            }
        }
        return "";
    }
    public String addProduct(String product, String code, String unit, String temperature){
        if (isEmpty(new String[] {product, code, unit})) return empty;
        if (!isNumeric(new String[]{code, temperature})) return numeric;
        String sql = "INSERT INTO products (product, code, unit, temperature) VALUES (" +
                "'" + product + "'," +
                "'" + code + "'," +
                "'" + unit + "'," +
                "NULLIF('"+temperature+"', '')" +
                ");";
        try {
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.addProduct()");
            if (e.getErrorCode() == 1062){
                return "'"+product+"' already exists";
            }
        }

        return "";
    }
    public String removeRoom(String room){
        if (room.isEmpty()) return empty;
        String sql = "DELETE FROM rooms WHERE room='"+room+"';";
        try {
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.removeRoom()");
            return "SQL error"; //TODO
        }
        return "";
    }
    public String removeProduct(String product){
        if (product.isEmpty()) return empty;
        String sql = "DELETE FROM products WHERE product='"+product+"';";
        try {
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.removeProduct()");
            return "SQL error"; //TODO
        }
        return "";
    }
    public String changeBalance(String room, String code, String batch, Double newBalance){
        if (isEmpty(new String[] {room, code})) return empty;
        if (!isNumeric(new String[]{ code, batch})) return numeric;

        String room_id = "";
        String product_id = "";
        String room_idQuery = "SELECT id FROM rooms WHERE room='"+room+"'";
        String product_idQuery = "SELECT id FROM products WHERE code='"+code+"'";
        try{
            room_id = getResultString(room_idQuery, "id");
            product_id = getResultString(product_idQuery, "id");
        }catch (SQLException e){
            System.out.println("ERROR in id query");
            e.printStackTrace();
        }

        String countQuery = "SELECT COUNT(*) FROM balance WHERE product_id='"+product_id+"' AND room_id='"+room_id+"' AND batch='"+batch+"'";
        String insert = "INSERT INTO balance (room_id, product_id, batch, amount) " +
                "VALUES ('"+room_id+"', "+product_id+", '"+batch+"', '"+newBalance+"')";
        String change = "UPDATE balance SET amount="+newBalance+" WHERE product_id='"+product_id+"' AND room_id='"+room_id+"' AND batch='"+batch+"'";
        try {
            if (getResultInt(countQuery, "COUNT(*)") == 0){
                statement.execute(insert);
                System.out.println("Did insert row into table balance");
            }else{
                statement.executeUpdate(change);
            }
        }catch (SQLException e){
            e.printStackTrace();
            return "SQL error";
        }


        String sql = "UPDATE balance SET amount="+newBalance+" WHERE product='"+code+"';";
        try {
            statement.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.changeBalance()");
            return "SQL error"; //TODO
        }
        return "";
    }
    public void tranfer(){

    }
    public void receive(){

    }
    public void collect(){

    }

    //Auxiliary
    public int amountOfRooms() throws SQLException{
        String sql = "SELECT COUNT(*) FROM rooms";
        return getResultInt(sql, "COUNT(*)");
    }
    public int amountOfProducts() throws SQLException{
        String sql = "SELECT COUNT(*) FROM products";
        return getResultInt(sql, "COUNT(*)");
    }
    public boolean isEmpty(String[] input){
        for (String string: input) {
            if(string == null || string.isEmpty())
                return true;
        }
        return false;
    }
    public boolean isNumeric(String[] textFields) {
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
    public Double getBalance(String room, String code, String batch)throws SQLException {
        String room_idQuery = "SELECT id FROM rooms WHERE room='"+room+"'";
        String product_idQuery = "SELECT id FROM products WHERE code='"+code+"'";
        String room_id = getResultString(room_idQuery, "id");
        String product_id = getResultString(room_idQuery, "id");
        String sql = "SELECT amount FROM balance WHERE " +
                "room_id='"+room_id+"' AND " +
                "product_id='"+product_id+"' AND " +
                "batch='"+batch+"'";
        return getResultDouble(sql, "amount");
    }
    public String getResultString(String query, String column) throws SQLException{
        ResultSet result = statement.executeQuery(query);
        while (result.next()){
            return result.getString(column);
        }
        return "-9999999";
    }
    public int getResultInt(String query, String column) throws SQLException{
        ResultSet result = statement.executeQuery(query);
        while (result.next()){
            return result.getInt(column);
        }
        return -9999999;
    }
    public double getResultDouble(String query, String column) throws SQLException{
        ResultSet result = statement.executeQuery(query);
        while (result.next()){
            return result.getDouble(column);
        }
        return -9999999;
    }

}
