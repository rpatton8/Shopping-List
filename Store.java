package ryan.android.shopping;

import java.util.ArrayList;

public class Store {

    private final String storeName;
    private final ArrayList<Item> storeItems;

    Store(String name, Item item) {
        this.storeName = name;
        storeItems = new ArrayList<>();
        storeItems.add(item);
    }

    public String getName() {
        return storeName;
    }

    public void addItem(Item item) {
        storeItems.add(item);
    }

    public ArrayList<Item> getItemList() {
        return storeItems;
    }

    @Override
    public String toString() {
        return storeName;
    }

}