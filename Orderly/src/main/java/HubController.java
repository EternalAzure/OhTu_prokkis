import javafx.application.Platform;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.sql.Statement;

public class HubController {

    final private Statement statement;
    final private Utils utils;
    public HubController(Statement statement){
        this.statement = statement;
        this.utils = new Utils(statement);
    }

    final private String numeric = "Numeric input needed";
    final private String empty = "Non allowed empty value";
    final private String decimal = "Decimal number needed";
    final private String integer = "Integer needed";

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
        if (name.isEmpty()) return empty;
        if (!Utils.isDouble(temperature) && !temperature.isEmpty()) return decimal;

        String sql = "INSERT INTO rooms (room, temperature) VALUES ('"+name+"', NULLIF('"+temperature+"', ''));";
        try {
            statement.execute(sql);
        }catch (SQLException e){
            if (e.getErrorCode() == 1062){
                return "'"+name+"' already exists";
            }
        }
        return "";
    }
    public String addProduct(String product, String code, String unit, String temperature){
        if (Utils.isEmpty(new String[] {product, code, unit})) return empty;
        if (!Utils.isNumeric(new String[]{code})) return numeric;
        if (!Utils.isDouble(temperature) && !temperature.isEmpty()) return decimal;

        String sql = "INSERT INTO products (product, code, unit, temperature) VALUES (" +
                "'" + product + "', " +
                "'" + code + "', " +
                "'" + unit + "', " +
                "NULLIF('"+temperature+"', ''));";
        try {
            statement.execute(sql);
        }catch (SQLException e){
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
            return "SQL error";
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
            return "SQL error";
        }
        return "";
    }
    public String changeBalance(String room, String code, String batch, String newBalance){
        if (Utils.isEmpty(new String[] {room, code, batch, newBalance})) return empty;
        if (!Utils.isNumeric(new String[]{code, batch})) return numeric;
        if (!Utils.isDouble(newBalance)) return decimal;
        if (Double.parseDouble(newBalance) <= 0) return "Only positive numbers are allowed";

        String room_idQuery = "SELECT id FROM rooms WHERE room='"+room+"'";
        String product_idQuery = "SELECT id FROM products WHERE code='"+code+"'";
        String room_id = utils.getResultString(room_idQuery, "id");
        String product_id = utils.getResultString(product_idQuery, "id");

        if(!utils.hasItem(room, "rooms", "room"))  return "Room does not exist";
        if(!utils.hasItem(code, "products", "code"))  return "Product does not exist";

        String countQuery = "SELECT COUNT(*) FROM balance WHERE product_id='"+product_id+"' AND room_id='"+room_id+"' AND batch='"+batch+"'";
        String insert = "INSERT INTO balance (room_id, product_id, batch, amount) " +
                "VALUES ('"+room_id+"', "+product_id+", '"+batch+"', '"+newBalance+"')";
        String change = "UPDATE balance SET amount="+newBalance+" WHERE product_id='"+product_id+"' AND room_id='"+room_id+"' AND batch='"+batch+"'";
        try {
            if (utils.getResultInt(countQuery, "COUNT(*)") == 0){
                statement.execute(insert);
            }else{
                statement.executeUpdate(change);
            }
        }catch (SQLException e){
            e.printStackTrace();
            return "SQL error";
        }
        return "";
    }
    public String transfer(String from, String to, String code, String batch, String amount){
        if (Utils.isEmpty(new String[]{from, to, code, batch})) return empty;
        if (!Utils.isNumeric(new String[]{code, batch})) return numeric;
        if (Double.parseDouble(amount) <= 0) return "Only positive numbers are allowed";

        double oldBalance = utils.getBalance(from, code, batch);
        double newBalance = oldBalance - Double.parseDouble(amount);
        if (newBalance < 0) return "Would result in negative balance";
        if (!utils.hasRoom(to)) return "Destination does not exist";
        if (!utils.hasRoom(from)) return "Origin does not exist";

        double targetOriginal = utils.getBalance(to, code, batch);
        double targetNewBalance = targetOriginal + Double.parseDouble(amount);
        changeBalance(from, code, batch, String.valueOf(newBalance));
        changeBalance(to, code, batch, String.valueOf(targetNewBalance));

        return "";
    }
    public String receiveShipment(){


        return "";
    }
    public void collect(){

    }

    //Strictly for testing purposes
    public void createTestShipment(){
        addRoom("Room", "4");
        addProduct("Kaali", "1000", "KG", "4");
        addProduct("Porkkana", "2000", "KG", "4");
        addProduct("Peruna", "3000", "KG", "4");
        addProduct("Kurpitsa", "4000", "KG", "4");
        addProduct("Sipuli", "5000", "KG", "4");

        String insert1 = "INSERT INTO shipments (shipment_number, product_id, batch) VALUES (1, 1, 1)";
        String insert2 = "INSERT INTO shipments (shipment_number, product_id, batch) VALUES (1, 2, 1)";
        String insert3 = "INSERT INTO shipments (shipment_number, product_id, batch) VALUES (1, 3, 1)";
        String insert4 = "INSERT INTO shipments (shipment_number, product_id, batch) VALUES (1, 4, 1)";
        String insert5 = "INSERT INTO shipments (shipment_number, product_id, batch) VALUES (1, 5, 1)";

        try{
            statement.execute(insert1);
            statement.execute(insert2);
            statement.execute(insert3);
            statement.execute(insert4);
            statement.execute(insert5);
        }catch (SQLException e){e.printStackTrace();}
    }
    public void deleteTestShipment(){
        String sql = "TRUNCATE TABLE shipments";
        String sql2 = "TRUNCATE TABLE products";
        try{
            statement.execute(sql);
            statement.execute(sql2);
        }catch (SQLException e){}
    }


}
