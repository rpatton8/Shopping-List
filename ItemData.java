package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemData {

    private final ArrayList<Item> items;
    private final ArrayList<Item> itemsByCategory;
    private final ArrayList<Item> itemsByStore;

    private final Map<String, Item> itemMap;
    private final Map<String, Category> categoryMap;
    private final Map<String, Store> storeMap;

    public ItemData() {
        items = new ArrayList<>();
        itemsByCategory = new ArrayList<>();
        itemsByStore = new ArrayList<>();
        itemMap = new HashMap<>();
        categoryMap = new HashMap<>();
        storeMap = new HashMap<>();
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

    public void updateStatusesByCategory(StatusData statusData) {
        Map<String, Status> statusMap = statusData.getStatusMap();
        for(int i = 0; i < itemsByCategory.size(); i++) {

            itemsByCategory.get(i).setStatus(statusMap.get(itemsByCategory.get(i).getName()));
        }
    }

    public void updateStatusesByStore(StatusData statusData) {
        Map<String, Status> statusMap = statusData.getStatusMap();
        for(int i = 0; i < itemsByStore.size(); i++) {

            itemsByStore.get(i).setStatus(statusMap.get(itemsByStore.get(i).getName()));
        }
    }

    public void readLineOfDataByCategory(String item, String brandType, String category, String store, int itemOrder) {
        Item newItem = new Item(item, brandType, category, store);
        Category newCategory = new Category(category, newItem);
        Store newStore = new Store(store, newItem);
        if (itemMap.containsKey(item)) {
            if (categoryMap.containsKey(category)) {
                if (storeMap.containsKey(store)) {
                    /* item, category, and store all exist */
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.get(store).addItem(newItem);
                } else {
                    /* item and category exist, but store doesn't */
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.put(store, newStore);
                }
            } else {
                if (storeMap.containsKey(store)) {
                    /* item and store exist, but category doesn't */
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    storeMap.get(store).addItem(newItem);
                    categoryMap.put(category, newCategory);
                } else {
                    /* item exists, but category and store don't */
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    categoryMap.put(category, newCategory);
                    storeMap.put(store, newStore);
                }
            }
        } else {
            if (categoryMap.containsKey(category)) {
                if (storeMap.containsKey(store)) {
                    /* store and category exist, but item doesn't */
                    itemMap.put(item, newItem);
                    items.add(newItem);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.get(store).addItem(newItem);
                } else {
                    /* category exists, but item and store don't */
                    itemMap.put(item, newItem);
                    items.add(newItem);
                    storeMap.put(store, newStore);
                    categoryMap.get(category).addItem(newItem);
                }
            } else {
                if (storeMap.containsKey(store)) {
                    /* store exists, but item and category don't */
                    itemMap.put(item, newItem);
                    items.add(newItem);
                    categoryMap.put(category, newCategory);
                    storeMap.get(store).addItem(newItem);
                } else {
                    /* item, category and store all don't exist */
                    itemMap.put(item, newItem);
                    items.add(newItem);
                    categoryMap.put(category, newCategory);
                    storeMap.put(store, newStore);
                }
            }
        }
    }

    public void readLineOfDataByStore(String item, String brandType, String category, String store, int itemOrder) {
        Item newItem = new Item(item, brandType, category, store);
        Category newCategory = new Category(category, newItem);
        Store newStore = new Store(store, newItem);
        if (itemMap.containsKey(item)) {
            if (categoryMap.containsKey(category)) {
                if (storeMap.containsKey(store)) {
                    /* item, category, and store all exist */
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.get(store).addItem(newItem);
                } else {
                    /* item and category exist, but store doesn't */
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.put(store, newStore);
                }
            } else {
                if (storeMap.containsKey(store)) {
                    /* item and store exist, but category doesn't */
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    storeMap.get(store).addItem(newItem);
                    categoryMap.put(category, newCategory);
                } else {
                    /* item exists, but category and store don't */
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    categoryMap.put(category, newCategory);
                    storeMap.put(store, newStore);
                }
            }
        } else {
            if (categoryMap.containsKey(category)) {
                if (storeMap.containsKey(store)) {
                    /* store and category exist, but item doesn't */
                    itemMap.put(item, newItem);
                    items.add(newItem);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.get(store).addItem(newItem);
                } else {
                    /* category exists, but item and store don't */
                    itemMap.put(item, newItem);
                    items.add(newItem);
                    storeMap.put(store, newStore);
                    categoryMap.get(category).addItem(newItem);
                }
            } else {
                if (storeMap.containsKey(store)) {
                    /* store exists, but item and category don't */
                    itemMap.put(item, newItem);
                    items.add(newItem);
                    categoryMap.put(category, newCategory);
                    storeMap.get(store).addItem(newItem);
                } else {
                    /* item, category and store all don't exist */
                    itemMap.put(item, newItem);
                    items.add(newItem);
                    categoryMap.put(category, newCategory);
                    storeMap.put(store, newStore);
                }
            }
        }
    }
}