package fi.orderly.ui.tables;
import fi.orderly.dao.tables.ITable;
import fi.orderly.logic.dbinterfaces.DatabaseAccess;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class TableViewInfiniteScrolling {

    ObservableList<ITable> items = FXCollections.observableArrayList();
    TableView<ITable> table = new TableView<>();
    DatabaseAccess db;
    Stage stage = new Stage();
    Scene scene;

    void addItems() {

    }

    void setUp() {

    }

    /**
     * Luo ikkunan, jossa näytetään jokin tietokantataulu.
     * Jos ikkuna on jo avattu, päivittää tiedot.
     * @param db yhteys tietokantaan
     */
    public void display(DatabaseAccess db) {
        this.db = db;
        table.setItems(items);
        setUp();

        if (scene == null) {
            scene = new Scene(table, 400, 400);
            stage.setScene(scene);
            items.clear();
            addItems();
        } else {
            items.clear();
            addItems();
            setUp();
            table.refresh();
        }

        stage.show();
        ScrollBar bar = getVerticalScrollbar(table);
        bar.valueProperty().addListener(this::scrolled);
    }

    private ScrollBar getVerticalScrollbar(TableView<?> table) {
        ScrollBar result = null;
        for (Node n : table.lookupAll(".scroll-bar")) {
            if (n instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) n;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    result = bar;
                }
            }
        }
        return result;
    }

    void scrolled(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        double value = newValue.doubleValue();
        ScrollBar bar = getVerticalScrollbar(table);
        if (value == bar.getMax()) {
            double targetValue = value * items.size();
            addItems();
            bar.setValue(targetValue / items.size());
        }
    }
}