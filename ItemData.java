package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

class ItemData {

    private LinkedList<Item> itemsAZ;
    private ArrayList<Item> itemsByCategory;
    private ArrayList<Item> itemsByStore;

    private HashMap<String, Item> itemMap;
    private HashMap<String, Category> categoryMap;
    private HashMap<String, Store> storeMap;

    ItemData() {
        itemsAZ = new LinkedList<>();
        itemsByCategory = new ArrayList<>();
        itemsByStore = new ArrayList<>();
        itemMap = new HashMap<>();
        categoryMap = new HashMap<>();
        storeMap = new HashMap<>();
    }

    LinkedList<Item> getItemListAZ() {
        return itemsAZ;
    }

    ArrayList<Item> getItemListByCategory() {
        return itemsByCategory;
    }

    ArrayList<Item> getItemListByStore() {
        return itemsByStore;
    }

    HashMap<String, Item> getItemMap() {
        return itemMap;
    }

    HashMap<String, Category> getCategoryMap() {
        return categoryMap;
    }

    HashMap<String, Store> getStoreMap() {
        return storeMap;
    }

    void updateStatuses(StatusData statusData) {
        HashMap<String, Status> statusMap = statusData.getStatusMap();
        for(int i = 0; i < itemsAZ.size(); i++) {
            itemsAZ.get(i).setStatus(statusMap.get(itemsAZ.get(i).getName()));
        }
    }

    void readLineOfDataByCategory(String item, String brandType, String category, String store, int itemCategoryOrder) {

        Item newItem;
        Category newCategory;
        Store newStore;
        if (!itemMap.containsKey(item)) {
            newItem = new Item(item, brandType, category, store);
            newItem.setCategoryOrder(itemCategoryOrder);
        } else {
            // item already exists
            itemMap.get(item).setCategoryOrder(itemCategoryOrder);
            return;
        }
        if (!categoryMap.containsKey(category)) {
            newCategory = new Category(category, newItem);
            categoryMap.put(category, newCategory);
        } else {
            // category already exists
            newCategory = categoryMap.get(category);
            categoryMap.get(category).addItem(newItem);
        }
        if (!storeMap.containsKey(store)) {
            newStore = new Store(store, newItem);
            storeMap.put(store, newStore);
        } else {
            // store already exists
            newStore = storeMap.get(store);
            storeMap.get(store).addItem(newItem);
        }
        newItem.setCategory(newCategory);
        newItem.setStore(newStore);
        itemMap.put(item, newItem);
        itemsByCategory.add(newItem);

        insertSorted(itemsAZ, newItem);  // alphabetical

    }

    void readLineOfDataByStore(String item, String brandType, String category, String store, int itemStoreOrder) {

        itemMap.get(item).setStoreOrder(itemStoreOrder);
        if (itemsByStore.contains(itemMap.get(item))) return;
        itemsByStore.add(itemMap.get(item));

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

    void printData() {

        System.out.println("itemsAZ:");
        for (int i = 1; i <= itemsAZ.size(); i++) {
            Item item = itemsAZ.get(i - 1);
            System.out.println("az item #" + i + " = " + item.getName() + "; brandType = " + item.getBrandType() + "; category order ("
                    + item.getCategory() + ") = " + item.getCategoryOrder() + "; store order (" + item.getStore() + ") = " + item.getStoreOrder());
        }
        System.out.println("itemsByCategory:");
        for (int i = 1; i <= itemsByCategory.size(); i++) {
            Item item = itemsByCategory.get(i - 1);
            System.out.println("category item #" + i + " = " + item.getName() + "; brandType = " + item.getBrandType() + "; category order ("
                    + item.getCategory() + ") = " + item.getCategoryOrder() + "; store order (" + item.getStore() + ") = " + item.getStoreOrder());
        }
        System.out.println("itemsByStore:");
        for (int i = 1; i <= itemsByStore.size(); i++) {
            Item item = itemsByStore.get(i - 1);
            System.out.println("store item #" + i + " = " + item.getName() + "; brandType = " + item.getBrandType() + "; category order ("
                    + item.getCategory() + ") = " + item.getCategoryOrder() + "; store order (" + item.getStore() + ") = " + item.getStoreOrder());
        }
    }
}