import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TaskWindow {

    Main main;
    public Scene display(Main main){
        this.main = main;
        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(10,10,10,10));

        Menu colours = new Menu("Colours");
        colours.getItems().addAll(
                new MenuItem("red"),
                new MenuItem("white"),
                new MenuItem("blue")
        );

        Menu numbers = new Menu("Numbers");
        numbers.getItems().addAll(
                new MenuItem("one"),
                new MenuItem("two"),
                new MenuItem("three")
        );

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(colours, numbers);

        Label label = new Label("Welcome to task view");
        Button logout = new Button("Logout");
        logout.setOnAction(e -> logout());

        HBox bottom = new HBox();
        bottom.getChildren().add(logout);
        layout.getChildren().add(label);
        //layout.getChildren().add(logout);


        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5,5,5,5));
        borderPane.setLeft(layout);
        borderPane.setTop(menuBar);
        borderPane.setBottom(bottom);
        Scene scene = new Scene(borderPane, 600, 400);
        scene.getStylesheets().add("stylesheet.css");
        return scene;
    }

    public void logout(){
        main.logout();
    }
}
