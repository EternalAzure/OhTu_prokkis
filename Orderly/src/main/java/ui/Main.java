import ui.Login;

public class Main {
    //This exists so app can be run as .jar outside of IDE with Java 8
    //Main class does NOT inherit from Application, and thus works. Magic
    public static void main(String[] args){
        ui.Login.main(args);
    }
}
