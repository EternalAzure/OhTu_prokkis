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

/**
 * Luokan tarkoitus on näyttää käyttäjälle ikkuna, jossa näkyy tietokannan taulu.
 * Ikkunaa voi rullata alaspäin ja luokka lataa lisää rivejä tauluun 50 kerrallaan.
 * Metodit addItems() ja setUp() toteutetaan luokissa, jotka perivät tämän luokan.
 */
public class TableViewInfiniteScrolling {

    ObservableList<ITable> items = FXCollections.observableArrayList();
    TableView<ITable> table = new TableView<>();
    DatabaseAccess db;
    Stage stage = new Stage();
    Scene scene;

    /**
     * Lisää ITable listaan items. ITable vastaa yhtä riviä taulussa.
     */
    void addItems() {

    }

    /**
     * Asettaa sarakkeet tauluun ja dao.tables StringPropertyt soluihin.
     */
    void setUp() {

    }

    /**
     * Luo ikkunan, jossa näytetään jokin tietokantataulu.
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