package ryan.android.shopping;

import java.util.ArrayList;

class Store {

    private String storeName;
    private ArrayList<Item> storeItems;

    private boolean isExpanded;
    private boolean isContracted;

    Store(String name, Item item) {
        this.storeName = name;
        storeItems = new ArrayList<>();
        storeItems.add(item);
        isExpanded = true;
        isContracted = false;
    }

    String getName() {
        return storeName;
    }

    void addItem(Item item) {
        storeItems.add(item);
    }

    ArrayList<Item> getStoreItemsList() {
        return storeItems;
    }

    boolean storeIsExpanded() {
        return isExpanded;
    }

    void setStoreAsExpanded() {
        isExpanded = true;
        isContracted = false;
    }

    boolean storeIsContracted() {
        return isContracted;
    }

    void setStoreAsContracted() {
        isExpanded = false;
        isContracted = true;
    }

    public String toString() {
        return storeName;
    }

}