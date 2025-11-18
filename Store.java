package ryan.android.shopping;

import java.util.ArrayList;

public class Store {

    private final String storeName;
    private final ArrayList<Item> storeItems;
    private boolean isExpanded;
    private boolean isContracted;

    Store(String name, Item item) {
        this.storeName = name;
        storeItems = new ArrayList<>();
        storeItems.add(item);
        isExpanded = true;
        isContracted = false;
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

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setAsExpanded() {
        isExpanded = true;
        isContracted = false;
    }

    public boolean isContracted() {
        return isExpanded;
    }

    public void setAsContracted() {
        isExpanded = false;
        isContracted = true;
    }

    @Override
    public String toString() {
        return storeName;
    }

}