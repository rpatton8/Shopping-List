package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemData {

    private ArrayList<Item> items;

    private Map<String, Item> itemMap;
    private Map<String, Category> categoryMap;
    private Map<String, Store> storeMap;

    public ItemData() {
        items = new ArrayList<>();
        itemMap = new HashMap<>();
        categoryMap = new HashMap<>();
        storeMap = new HashMap<>();
    }

    public ArrayList<Item> getItemList() {
        return items;
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
        for(int i = 0; i < items.size(); i++) {

            items.get(i).setStatus(statusMap.get(items.get(i).getName()));
        }
    }

    public void readLineOfData(String item, String brandType, String category, String store) {
        Item newItem = new Item(item, brandType, category, store);
        Category newCategory = new Category(category, newItem);
        Store newStore = new Store(store, newItem);
        if (itemMap.containsKey(item)) {
            if (categoryMap.containsKey(category)) {
                if (storeMap.containsKey(store)) {
                    /* item, category, and store all exist */
                    itemMap.get(item).addCategory(newCategory);
                    itemMap.get(item).addStore(newStore);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.get(store).addItem(newItem);
                } else {
                    /* item and category exist, but store doesn't */
                    itemMap.get(item).addCategory(newCategory);
                    itemMap.get(item).addStore(newStore);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.put(store, newStore);
                }
            } else {
                if (storeMap.containsKey(store)) {
                    /* item and store exist, but category doesn't */
                    itemMap.get(item).addCategory(newCategory);
                    itemMap.get(item).addStore(newStore);
                    storeMap.get(store).addItem(newItem);
                    categoryMap.put(category, newCategory);
                } else {
                    /* item exists, but category and store don't */
                    itemMap.get(item).addCategory(newCategory);
                    itemMap.get(item).addStore(newStore);
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