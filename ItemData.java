package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

public class ItemData {

    private LinkedList<Item> itemsAZ;
    private ArrayList<Item> itemsByCategory;
    private ArrayList<Item> itemsByStore;

    private Map<String, Item> itemMap;
    private Map<String, Category> categoryMap;
    private Map<String, Store> storeMap;

    public ItemData() {
        itemsAZ = new LinkedList<>();
        itemsByCategory = new ArrayList<>();
        itemsByStore = new ArrayList<>();
        itemMap = new HashMap<>();
        categoryMap = new HashMap<>();
        storeMap = new HashMap<>();
    }

    public LinkedList<Item> getItemListAZ() {
        return itemsAZ;
    }

    public ArrayList<Item> getItemListByCategory() {
        return itemsByCategory;
    }

    public ArrayList<Item> getItemListByStore() {
        return itemsByStore;
    }

    public Map<String, Item> getItemMap() {
        return itemMap;
    }

    public Map<String, Category> getCategoryMap() {
        return categoryMap;
    }

    public Map<String, Store> getStoreMap() {
        return storeMap;
    }

    public void updateStatuses(StatusData statusData) {
        Map<String, Status> statusMap = statusData.getStatusMap();
        for(int i = 0; i < itemsByCategory.size(); i++) {
            itemsByCategory.get(i).setStatus(statusMap.get(itemsByCategory.get(i).getName()));
        }
    }

    public void readLineOfDataByCategory(String item, String brandType, String category, String store, int itemCategoryOrder) {

        Item newItem;
        Category newCategory;
        Store newStore;
        if (!itemMap.containsKey(item)) {
            newItem = new Item(item, brandType, category, store);
            newItem.setCategoryOrder(itemCategoryOrder);
        } else {
            // item already exists
            return;
        }
        if (!categoryMap.containsKey(category)) {
            newCategory = new Category(category, newItem);
            categoryMap.put(category, newCategory);
        } else {
            // category already exists
            newCategory = categoryMap.get(category);
        }
        if (!storeMap.containsKey(store)) {
            newStore = new Store(store, newItem);
            storeMap.put(store, newStore);
        } else {
            // store already exists
            newStore = storeMap.get(store);
        }
        newItem.setCategory(newCategory);
        newItem.setStore(newStore);
        itemMap.put(item, newItem);
        itemsByCategory.add(newItem);
        insertSorted(itemsAZ, newItem);

    }

    public void readLineOfDataByStore(String item, String brandType, String category, String store, int itemStoreOrder) {

        Item thisItem = itemMap.get(item);
        thisItem.setStoreOrder(itemStoreOrder);
        itemsByStore.add(thisItem);
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

    public void printData() {

        System.out.println("itemsAZ:");
        for (int i = 1; i <= itemsAZ.size(); i++) {
            Item item = itemsAZ.get(i - 1);
            System.out.println("az item #" + i + " = " + item.getName());
        }
        System.out.println("itemsByCategory:");
        for (int i = 1; i <= itemsByCategory.size(); i++) {
            Item item = itemsByCategory.get(i - 1);
            System.out.println("category item #" + i + " = " + item.getName());
        }
        System.out.println("itemsByStore:");
        for (int i = 1; i <= itemsByStore.size(); i++) {
            Item item = itemsByStore.get(i - 1);
            System.out.println("store item #" + i + " = " + item.getName());
        }
    }
}