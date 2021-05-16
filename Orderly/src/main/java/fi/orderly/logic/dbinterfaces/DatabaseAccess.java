package fi.orderly.logic.dbinterfaces;
import java.sql.*;

/**
 * Luokan tarkoitus on tarjota pääsy tietokannan tauluihin.
 * Luokkamuuttujat ovat julkisia. Tämä mahdollistaa erittäin miellyttävän
 * konvention, jossa tietokantarajapintaa kutsutaan db.*.metodi().
 * Sisältää SQL komentoja, jotka koskettavat useampaa kuin yhtä taulua.
 * Metodit, jotka ottavat parametreja eivät käsittele virheitä.
 */
public class DatabaseAccess {

    final public RoomsInterface rooms;
    final public ProductsInterface products;
    final public BalanceInterface balance;
    final public ShipmentsInterface shipments;
    final public DeliveriesInterface deliveries;
    Connection connection;

    public DatabaseAccess(Connection connection) {
        this.connection = connection;
        rooms = new RoomsInterface(connection);
        products = new ProductsInterface(connection);
        balance = new BalanceInterface(connection);
        shipments = new ShipmentsInterface(connection);
        deliveries = new DeliveriesInterface(connection);
    }

    /**
     * Selvittää huoneen ja tuotteen id ja käyttää niitä hakeakseen saldon.
     * @param room huoneen nimi
     * @param code tuotteen koodi
     * @param batch eränumero
     * @return saldo
     * @throws SQLException annettu parametri on väärä
     */
    public double queryBalance(String room, int code, int batch) throws SQLException {
        int roomId = rooms.findIdByName(room);
        int productId = products.findIdByCode(code);
        return balance.queryBalance(roomId, productId, batch);
    }

    /**
     * Palauttaa SQL-koodin, jolla saadaan saapuvaan toimituseen liittyvät tiedot.
     * Tämä siistii koodia siellä, missä metodia kutsutaan. Valmista ResultSet oliota
     * ei voi palauttaa.
     * @param shipmentNumber toimitusnumero
     * @return SQL-koodi
     * @throws SQLException annettu parametri on väärä
     */
    public PreparedStatement queryShipment(int shipmentNumber) throws SQLException {
        //Returning ResultSet is not a good idea
        //Used in dao.Shipment fetchData()
        String select = "SELECT product, code, batch, amount, unit, room FROM shipments, products, rooms " +
                "WHERE number=? AND products.id=shipments.product_id AND rooms.id=products.room_id";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, shipmentNumber);
        return sql;
    }

    /**
     * Palauttaa SQL-koodin, jolla saadaan lähtevään toimitukseen liittyvät tiedot.
     * Tämä siistii koodia siellä, missä metodia kutsutaan. Valmista ResultSet oliota
     * ei voi palauttaa.
     * @param deliveryNumber toimitusnumero
     * @return SQL-koodi
     * @throws SQLException annettu parametri on väärä
     */
    public PreparedStatement queryDelivery(int deliveryNumber) throws SQLException {
        //Returning ResultSet is not a good idea
        //Used in dao.Delivery fetchRequest()
        String select = "SELECT code, amount FROM deliveries, products " +
                "WHERE deliveries.product_id=products.id AND number=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, deliveryNumber);
        return sql;
    }

    /**
     * Palauttaa SQL-koodin, jolla saadaan shipments taulun tiedot.
     * Tämä siistii koodia siellä, missä metodia kutsutaan. Valmista ResultSet oliota
     * ei voi palauttaa.
     * @param id id
     * @return SQL-koodi
     * @throws SQLException annettu parametri on väärä
     */
    public PreparedStatement tableShipments(int id) throws SQLException {
        String select = "SELECT number, product, batch, amount FROM products, shipments " +
                "WHERE shipments.id=? AND products.id=product_id";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        return sql;
    }

    /**
     * Palauttaa SQL-koodin, jolla saadaan products taulun tiedot.
     * Tämä siistii koodia siellä, missä metodia kutsutaan. Valmista ResultSet oliota
     * ei voi palauttaa.
     * @param id id
     * @return SQL-koodi
     * @throws SQLException annettu parametri on väärä
     */
    public PreparedStatement tableProducts(int id) throws SQLException {
        String select = "SELECT product, code, unit, products.temperature, room FROM products, rooms " +
                "WHERE rooms.id=products.room_id AND products.id=?";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        return sql;
    }

    /**
     * Palauttaa SQL-koodin, jolla saadaan deliveries taulun tiedot.
     * Tämä siistii koodia siellä, missä metodia kutsutaan. Valmista ResultSet oliota
     * ei voi palauttaa.
     * @param id id
     * @return SQL-koodi
     * @throws SQLException annettu parametri on väärä
     */
    public PreparedStatement tableDeliveries(int id) throws SQLException {
        String select = "SELECT number, product, amount " +
                "FROM products, deliveries WHERE deliveries.id=? AND products.id=product_id";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        return sql;
    }

    /**
     * Palauttaa SQL-koodin, jolla saadaan balance taulun tiedot.
     * Tämä siistii koodia siellä, missä metodia kutsutaan. Valmista ResultSet oliota
     * ei voi palauttaa.
     * @param id id
     * @return SQL-koodi
     * @throws SQLException annettu parametri on väärä
     */
    public PreparedStatement tableBalance(int id) throws SQLException {
        String select = "SELECT product, code, batch, amount, room " +
                "FROM products, rooms, balance WHERE balance.id=? AND products.id=product_id AND rooms.id=balance.room_id";
        PreparedStatement sql = connection.prepareStatement(select);
        sql.setInt(1, id);
        return sql;
    }

    /**
     * Tarkistaa listan huoneiden olemassa olon.
     * @param list lista huoneista
     * @return löytyikö kaikki huoneet
     * @throws SQLException annettu parametri on väärä
     */
    public boolean foundRooms(String[] list) throws SQLException {
        for (String room: list) {
            if (!rooms.foundRoom(room)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tarkistaa löytyykö eränumeroa shipments tai balance taulusta.
     * @param batchNumber eränumero
     * @return löytyikö eränumeroa kummastakaan
     * @throws SQLException annettu parametri on väärä
     */
    public boolean foundBatch(int batchNumber) throws SQLException {
        return shipments.foundBatch(batchNumber) || balance.foundBatch(batchNumber);
    }

    /**
     * Toteuttaa parametrina annetun kyselyn ja palauttaa parametrina annetun sarakkeen arvon.
     * Käytetään vain yksikkötestauksessa.
     * @param query SQL-kysely
     * @param column palautettava sarake
     * @return palauttaa mitä sarakkeesta löytyy
     * @throws SQLException annatte kysely tai sarake on virheellinen
     */
    public int customQuery(String query, String column) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        return resultSet.getInt(column);
    }

    /**
     * Tyhjentää kaikki taulut ja nollaa indeksit
     */
    public void truncateAll() {
        rooms.truncate();
        products.truncate();
        balance.truncate();
        shipments.truncate();
        deliveries.truncate();
    }
}
