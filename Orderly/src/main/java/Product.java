public class Product {

    final private String code;
    final private String batch;
    final private String amount;
    final private String room;

    public Product(String code,
                   String batch,
                   String amount,
                   String room) {
        this.code = code;
        this.batch = batch;
        this.amount = amount;
        this.room = room;
    }


    public String getCode() {
        return code;
    }

    public String getBatch() {
        return batch;
    }

    public String getAmount() {
        return amount;
    }

    public String getRoom() {
        return room;
    }
}
