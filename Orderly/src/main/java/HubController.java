import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class HubController {

    private Statement statement;
    public HubController(String database){
        this.statement = ServerConnection.createConnection(database);
    }

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
    public void addRoom(TextField name, TextField temperature){
        if (name.getText().isEmpty()) return;
        if (!isNumeric(new TextField[] {temperature})) return;

        String sql = "INSERT INTO rooms (room, temperature) VALUES ('"+name.getText()+"', NULLIF('"+temperature.getText()+"', ''));";
        try {
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.addRoom()");
        }
        name.clear(); temperature.clear();
    }

    public void addProduct(TextField product, TextField code, TextField unit, TextField temperature){
        if (isEmpty(new TextField[] {product, code, unit})) return;
        if (!isNumeric(new TextField[]{code, temperature})) return;
        String sql = "INSERT INTO products (product, code, unit, temperature) VALUES (" +
                "'" + product.getText() + "'," +
                "'" + code.getText() + "'," +
                "'" + unit.getText() + "'," +
                "NULLIF('"+temperature.getText()+"', '')" +
                ");";
        try {
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.addProduct()");
        }

        product.clear(); code.clear(); unit.clear(); temperature.clear();
    }

    public void removeRoom(TextField room){
        if (room.getText().isEmpty()||room.getText().equals("")) return;
        String sql = "DELETE FROM rooms WHERE room='"+room.getText()+"';";
        try {
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.removeRoom()");
            e.printStackTrace();
        }
        room.clear();
    }
    public void removeProduct(TextField product){
        if (product.getText().isEmpty()||product.getText().equals("")) return;
        String sql = "DELETE FROM products WHERE product='"+product.getText()+"';";
        try {
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.removeProduct()");
        }
        product.clear();
    }
    public void changeBalance(TextField room, TextField product, TextField batch, TextField newBalance){
        if (isEmpty(new TextField[] {room, product, batch, newBalance})) return;
        System.out.println("Ei tyhjiä");
        if (!isNumeric(new TextField[]{newBalance})) return;
        System.out.println("On numero");

        String sql = "UPDATE balance SET amount="+newBalance.getText()+" WHERE product='"+product.getText()+"';";
        try {
            statement.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.changeBalance()");
        }
        room.clear(); product.clear(); batch.clear(); newBalance.clear();
    }
    public void tranfer(){

    }
    public void receive(){

    }
    public void collect(){

    }

    public int amountOfRooms() throws SQLException{
        String sql = "SELECT COUNT(*) FROM rooms";
        ResultSet result = statement.executeQuery(sql);

        while(result.next()) {
            try {
                return result.getInt("COUNT(*)");
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }
    public int amountOfProducts() throws SQLException{
        String sql = "SELECT COUNT(*) FROM products";
        ResultSet result = statement.executeQuery(sql);
        while(result.next()) {
            try {
                return result.getInt("COUNT(*)");
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public boolean isEmpty(TextField[] textFields){
        for (TextField textField: textFields) {
            if(textField.getText() == null || textField.getText().isEmpty())
                return true;
        }
        return false;
    }

    public boolean isNumeric(TextField[] textFields) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        int count = 0;
        for (TextField t: textFields) {
            String string = t.getText();

            if(pattern.matcher(string).matches()) count++;
            if(string.isEmpty()) count++;
        }

        if (count == textFields.length)
        return true;
        return false;
    }

    public String getBalance(TextField room, TextField product, TextField batch){
        //if (product.getText().isEmpty()||product.getText().equals("")) return;
        String sql = "SELECT amount FROM balance WHERE " +
                "room='"+room.getText()+"'";
        try {
            ResultSet result = statement.executeQuery(sql);
            while (result.next()){
                System.out.println(result.getString("amount"));
                return result.getString("amount");
            }
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.getBalance()");
        }
        return "error"; //return type could be changed
    }
}
