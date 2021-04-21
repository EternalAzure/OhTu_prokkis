package ui;

public class Main {
    //This exists so app can be run as .jar outside of IDE with Java 8
    //JavaFX is not included in Java 11
    //ui.Main class does NOT inherit from Application, and thus works. Magic
    public static void main(String[] args){
        ui.Login.main(args);
    }
}
