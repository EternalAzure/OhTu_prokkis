import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class HubController {

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


    public void addRoom(TextField name, TextField temperature, Statement statement){
        if (name.getText().isEmpty()) return;
        if (!isNumeric(new TextField[] {temperature})) return;

        String sql = "INSERT INTO rooms (room, temperature) VALUES ('"+name.getText()+"', NULLIF('"+temperature.getText()+"', ''));";
        try {
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println("Bad SQL in HubController.addProduct()");
        }
        name.clear(); temperature.clear();
    }

    public void addProduct(TextField product, TextField code, TextField unit, TextField temperature, Statement statement){
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

    public int amountOfRooms(Statement statement) throws SQLException{
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
    public int amountOfProducts(Statement statement) throws SQLException{
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
}
