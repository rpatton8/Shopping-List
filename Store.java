import java.util.List;

public class Store {

    private String name;
    private List<Item> items;

    public Store(String name) {
        this.name = name;
    }

    public Store(String name, List<Item> items) {
        this.name = name;
        this.items = items;
    }

}