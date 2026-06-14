package ryan.android.shopping;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

class ItemData {

    private Context context;

    private LinkedList<Item> itemsAZ;
    private ArrayList<Item> itemsByCategory;
    private ArrayList<Item> itemsByStore;
    private HashMap<String, Item> itemMap;
    private HashMap<String, Category> categoryMap;
    private HashMap<String, Store> storeMap;

    ItemData(Context context) {
        setContext(context);
        setItemsAZ(new LinkedList<Item>());
        setItemsByCategory(new ArrayList<Item>());
        setItemsByStore(new ArrayList<Item>());
        setItemMap(new HashMap<String, Item>());
        setCategoryMap(new HashMap<String, Category>());
        setStoreMap(new HashMap<String, Store>());
    }

    private ItemData getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        getThis().context = context;
    }

    public LinkedList<Item> getItemsAZ() {
        return itemsAZ;
    }

    public void setItemsAZ(LinkedList<Item> itemsAZ) {
        getThis().itemsAZ = itemsAZ;
    }

    public ArrayList<Item> getItemsByCategory() {
        return itemsByCategory;
    }

    public void setItemsByCategory(ArrayList<Item> itemsByCategory) {
        getThis().itemsByCategory = itemsByCategory;
    }

    public ArrayList<Item> getItemsByStore() {
        return itemsByStore;
    }

    public void setItemsByStore(ArrayList<Item> itemsByStore) {
        getThis().itemsByStore = itemsByStore;
    }

    public HashMap<String, Item> getItemMap() {
        return itemMap;
    }

    public void setItemMap(HashMap<String, Item> itemMap) {
        getThis().itemMap = itemMap;
    }

    public HashMap<String, Category> getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(HashMap<String, Category> categoryMap) {
        getThis().categoryMap = categoryMap;
    }

    public HashMap<String, Store> getStoreMap() {
        return storeMap;
    }

    public void setStoreMap(HashMap<String, Store> storeMap) {
        getThis().storeMap = storeMap;
    }
    
    LinkedList<Item> getItemListAZ() {
        return getThis().getItemsAZ();
    }

    ArrayList<Item> getItemListByCategory() {
        return getThis().getItemsByCategory();
    }

    ArrayList<Item> getItemListByStore() {
        return getThis().getItemsByStore();
    }

    void updateStatuses(StatusData statusData) {
        HashMap<String, Status> statusMap = statusData.getStatusMap();
        for(int i = 0; i < getItemsAZ().size(); i++) {
            getItemsAZ().get(i).setStatus(statusMap.get(getItemsAZ().get(i).getItemName()));
        }
    }

    void readLineOfDataByCategory(String itemName, String brandType, String category, String store, int itemCategoryOrder) {

        Item newItem;
        Category newCategory;
        Store newStore;
        if (!getItemMap().containsKey(itemName)) {
            newItem = new Item(getContext(), itemName, brandType, category, store);
            newItem.setCategoryOrder(itemCategoryOrder);
        } else {
            // item already exists
            getItemMap().get(itemName).setCategoryOrder(itemCategoryOrder);
            return;
        }
        if (!getCategoryMap().containsKey(category)) {
            newCategory = new Category(getContext(), category, newItem);
            getCategoryMap().put(category, newCategory);
        } else {
            // category already exists
            newCategory = getCategoryMap().get(category);
            getCategoryMap().get(category).addItem(newItem);
        }
        if (!getStoreMap().containsKey(store)) {
            newStore = new Store(getContext(), store, newItem);
            getStoreMap().put(store, newStore);
        } else {
            // store already exists
            newStore = getStoreMap().get(store);
            getStoreMap().get(store).addItem(newItem);
        }
        newItem.setCategory(newCategory);
        newItem.setStore(newStore);
        getItemMap().put(itemName, newItem);
        getItemsByCategory().add(newItem);

        insertSorted(getItemsAZ(), newItem);  // alphabetical

    }

    void readLineOfDataByStore(String item, String brandType, String category, String store, int itemStoreOrder) {

        getItemMap().get(item).setStoreOrder(itemStoreOrder);
        if (getItemsByStore().contains(getItemMap().get(item))) return;
        getItemsByStore().add(getItemMap().get(item));

    }

    private void insertSorted(LinkedList<Item> list, Item item) {

        ListIterator<Item> itemsAZiterator = list.listIterator();
        int index = 0;
        while (itemsAZiterator.hasNext()) {
            if (item.compareTo(itemsAZiterator.next()) < 0) {
                itemsAZiterator.previous();
                break;
            }
            index++;
        }
        list.add(index, item);
    }

    void printDataAZ() {

        System.out.println(getContext().getString(R.string.pdItemsAZ));
        for (int i = 1; i <= getItemsAZ().size(); i++) {
            Item item = getItemsAZ().get(i - 1);
            System.out.println(getContext().getString(R.string.pdAzItem) + i + getContext().getString(R.string.pdEquals) + item.getItemName()
                    + getContext().getString(R.string.pdBrandTypeEquals) + item.getBrandType() + getContext().getString(R.string.pdCategoryOrder)
                    + item.getCategory() + getContext().getString(R.string.pdEqualsWithParenthesis) + item.getCategoryOrder()
                    + getContext().getString(R.string.pdStoreOrder) + item.getStore() + getContext().getString(R.string.pdEqualsWithParenthesis)
                    + item.getStoreOrder());
        }
    }

    void printDataByCategory() {
        System.out.println(getContext().getString(R.string.pdItemsByCategory));
        for (int i = 1; i <= getItemsByCategory().size(); i++) {
            Item item = getItemsByCategory().get(i - 1);
            System.out.println(getContext().getString(R.string.pdCategoryItem) + i + getContext().getString(R.string.pdEquals) + item.getItemName()
                    + getContext().getString(R.string.pdBrandTypeEquals) + item.getBrandType() + getContext().getString(R.string.pdCategoryOrder)
                    + item.getCategory() + getContext().getString(R.string.pdEqualsWithParenthesis) + item.getCategoryOrder()
                    + getContext().getString(R.string.pdStoreOrder) + item.getStore() + getContext().getString(R.string.pdEqualsWithParenthesis)
                    + item.getStoreOrder());
        }
    }

    void printDataByStore() {
        System.out.println(getContext().getString(R.string.pdItemsByStore));
        for (int i = 1; i <= getItemsByStore().size(); i++) {
            Item item = getItemsByStore().get(i - 1);
            System.out.println(getContext().getString(R.string.pdStoreItem) + i + getContext().getString(R.string.pdEquals) + item.getItemName()
                    + getContext().getString(R.string.pdBrandTypeEquals) + item.getBrandType() + getContext().getString(R.string.pdCategoryOrder)
                    + item.getCategory() + getContext().getString(R.string.pdEqualsWithParenthesis) + item.getCategoryOrder()
                    + getContext().getString(R.string.pdStoreOrder) + item.getStore() + getContext().getString(R.string.pdEqualsWithParenthesis)
                    + item.getStoreOrder());
        }
    }
}