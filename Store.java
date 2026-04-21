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

    ArrayList<Item> getItemList() {
        return storeItems;
    }

    boolean isExpanded() {
        return isExpanded;
    }

    void setAsExpanded() {
        isExpanded = true;
        isContracted = false;
    }

    boolean isContracted() {
        return isContracted;
    }

    void setAsContracted() {
        isExpanded = false;
        isContracted = true;
    }

    public String toString() {
        return storeName;
    }

}