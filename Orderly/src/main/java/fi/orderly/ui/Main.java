package fi.orderly.ui;

public class Main {
    //This exists so app can be run as .jar outside of IDE with Java 8
    //JavaFX is not included in Java 11
    //fi.orderly.dao.ui.Main class does NOT inherit from Application, and thus works. Magic
    public static void main(String[] args){
        Login.main(args);
    }
}
