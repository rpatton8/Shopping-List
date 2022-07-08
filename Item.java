import java.util.List;
import java.util.LinkedList;

public class Item {

    private String name;
    private String brand;
    private String store;
    private String price;
    private String category;

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public Item(String name, String store, String category) {
        this.name = name;
        this.store = store;
        this.category = category;
    }

}