package ryan.android.shopping;

import android.content.Context;
import java.util.ArrayList;

class Store {

    private Context context;
    private Shopping shopping;
    private String storeName;
    private ArrayList<Item> storeItems;
    private boolean isExpanded;
    private boolean isContracted;

    Store(Context context, Shopping shopping, String name, Item item) {
        setContext(context);
        setShopping(shopping);
        setStoreName(name);
        setStoreItems(new ArrayList<>());
        getStoreItems().add(item);

        if (Shopping.STORE_TITLES_EXPANDED.equals(getShopping().getStoreTitles())) {
            setStoreAsExpanded();
        } else if (Shopping.STORE_TITLES_CONTRACTED.equals(getShopping().getStoreTitles())) {
            setStoreAsContracted();
        }
    }

    private Store getThis() {
        return this;
    }

    private Context getContext() {
        return getThis().context;
    }

    private void setContext(Context context) {
        getThis().context = context;
    }

    public Shopping getShopping() {
        return getThis().shopping;
    }

    public void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    private String getStoreName() {
        return getThis().storeName;
    }

    private void setStoreName(String storeName) {
        getThis().storeName = storeName;
    }

    private ArrayList<Item> getStoreItems() {
        return getThis().storeItems;
    }

    private void setStoreItems(ArrayList<Item> storeItems) {
        getThis().storeItems = storeItems;
    }

    private boolean isExpanded() {
        return getThis().isExpanded;
    }

    private void setExpanded(boolean expanded) {
        getThis().isExpanded = expanded;
    }

    private boolean isContracted() {
        return getThis().isContracted;
    }

    private void setContracted(boolean contracted) {
        getThis().isContracted = contracted;
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