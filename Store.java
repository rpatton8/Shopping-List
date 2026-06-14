package ryan.android.shopping;

import android.content.Context;
import java.util.ArrayList;

class Store {

    private Context context;
    private String storeName;
    private ArrayList<Item> storeItems;
    private boolean isExpanded;
    private boolean isContracted;

    Store(Context context, String name, Item item) {
        setContext(context);
        setStoreName(name);
        setStoreItems(new ArrayList<Item>());
        getStoreItems().add(item);
        setExpanded(true);
        setContracted(false);
    }

    private Store getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        getThis().context = context;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        getThis().storeName = storeName;
    }

    public ArrayList<Item> getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(ArrayList<Item> storeItems) {
        getThis().storeItems = storeItems;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isContracted() {
        return isContracted;
    }

    public void setContracted(boolean contracted) {
        isContracted = contracted;
    }

    void addItem(Item item) {
        getStoreItems().add(item);
    }

    ArrayList<Item> getStoreItemsList() {
        return getStoreItems();
    }

    boolean storeIsExpanded() {
        return isExpanded();
    }

    void setStoreAsExpanded() {
        setExpanded(true);
        setContracted(false);
    }

    boolean storeIsContracted() {
        return isContracted();
    }

    void setStoreAsContracted() {
        setExpanded(false);
        setContracted(true);
    }

    public String toString() {
        return getStoreName();
    }

    void printStore() {
        System.out.println(getContext().getString(R.string.psStoreName) + getStoreName());
        for (int i = 0; i < getStoreItems().size(); i++) {
            System.out.println(getContext().getString(R.string.psItemNum) + (i + 1) + getContext().getString(R.string.psColon) + getStoreItems().get(i).getItemName());
        }
    }
}