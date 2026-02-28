package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ItemData {

    private final LinkedList<Item> itemsAZ;
    private final ArrayList<Item> itemsByCategory;
    private final ArrayList<Item> itemsByStore;

    private final Map<String, Item> itemMap;
    private final Map<String, Category> categoryMap;
    private final Map<String, Store> storeMap;

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

        Item newItem = null;
        Category newCategory = null;
        Store newStore = null;
        if (!itemMap.containsKey(item)) {
            newItem = new Item(item, brandType, category, store);
            newItem.setCategoryOrder(itemCategoryOrder);
            //System.out.println("Category order = " + itemCategoryOrder);
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

    }

    public void readLineOfDataByStore(String item, String brandType, String category, String store, int itemStoreOrder) {

        //Item newItem = null;
        if (itemMap.containsKey(item)) {
            itemMap.get(item).setStoreOrder(itemStoreOrder);
            //newItem = itemMap.get(item);
            //newItem.setStoreOrder(itemStoreOrder);
            //System.out.println("Store order = " + itemStoreOrder);
        } else {
            // item doesn't exist
            //System.out.println("Item doesn't exist");
            return;
        }
        //newItem.printItem();
        //itemMap.put(item, newItem);
        itemsByStore.add(itemMap.get(item));

    }

    /*public void readLineOfDataByCategory(String item, String brandType, String category, String store, int itemCategoryOrder) {
        Item newItem = new Item(item, brandType, category, store);
        newItem.setCategoryOrder(itemCategoryOrder);
        Category newCategory = new Category(category, newItem);
        Store newStore = new Store(store, newItem);
        if (itemMap.containsKey(item)) {
            if (categoryMap.containsKey(category)) {
                if (storeMap.containsKey(store)) {
                    // item, category, and store all exist
                    System.out.println("Category - item, category, and store all exist");
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.get(store).addItem(newItem);
                } else {
                    // item and category exist, but store doesn't
                    System.out.println("Category - item and category exist, but store doesn't");
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.put(store, newStore);
                }
            } else {
                if (storeMap.containsKey(store)) {
                    // item and store exist, but category doesn't
                    System.out.println("Category - item and store exist, but category doesn't");
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    storeMap.get(store).addItem(newItem);
                    categoryMap.put(category, newCategory);
                } else {
                    // item exists, but category and store don't
                    System.out.println("Category - item exists, but category and store don't");
                    itemMap.get(item).setCategory(newCategory);
                    itemMap.get(item).setStore(newStore);
                    categoryMap.put(category, newCategory);
                    storeMap.put(store, newStore);
                }
            }
        } else {
            if (categoryMap.containsKey(category)) {
                if (storeMap.containsKey(store)) {
                    // store and category exist, but item doesn't
                    System.out.println("Category - store and category exist, but item doesn't");
                    itemMap.put(item, newItem);
                    itemsAZ.add(newItem);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.get(store).addItem(newItem);
                } else {
                    // category exists, but item and store don't
                    System.out.println("Category - category exists, but item and store don't");
                    itemMap.put(item, newItem);
                    itemsAZ.add(newItem);
                    storeMap.put(store, newStore);
                    categoryMap.get(category).addItem(newItem);
                }
            } else {
                if (storeMap.containsKey(store)) {
                    // store exists, but item and category don't
                    System.out.println("Category - store exists, but item and category don't");
                    itemMap.put(item, newItem);
                    itemsAZ.add(newItem);
                    categoryMap.put(category, newCategory);
                    storeMap.get(store).addItem(newItem);
                } else {
                    // item, category and store all don't exist
                    System.out.println("Category - item, category and store all don't exist");
                    itemMap.put(item, newItem);
                    itemsAZ.add(newItem);
                    categoryMap.put(category, newCategory);
                    storeMap.put(store, newStore);
                }
            }
        }
    }*/

    /*public void readLineOfDataByStore(String item, String brandType, String category, String store, int itemStoreOrder) {
        Item newItem = new Item(item, brandType, category, store);
        newItem.setStoreOrder(itemStoreOrder);
        Store newStore = new Store(store, newItem);
        Category newCategory = new Category(category, newItem);
        if (itemMap.containsKey(item)) {
            if (storeMap.containsKey(store)) {
                if (categoryMap.containsKey(category)) {
                    // item, store, and category all exist
                    System.out.println("Store - item, store, and category all exist");
                    itemMap.get(item).setStore(newStore);
                    itemMap.get(item).setCategory(newCategory);
                    storeMap.get(store).addItem(newItem);
                    categoryMap.get(category).addItem(newItem);
                } else {
                    // item and store exist, but category doesn't
                    System.out.println("Store - item and store exist, but category doesn't");
                    itemMap.get(item).setStore(newStore);
                    itemMap.get(item).setCategory(newCategory);
                    storeMap.get(store).addItem(newItem);
                    categoryMap.put(category, newCategory);
                }
            } else {
                if (categoryMap.containsKey(category)) {
                    // item and category exist, but store doesn't
                    System.out.println("Store - item and category exist, but store doesn't");
                    itemMap.get(item).setStore(newStore);
                    itemMap.get(item).setCategory(newCategory);
                    categoryMap.get(category).addItem(newItem);
                    storeMap.put(store, newStore);
                } else {
                    // item exists, but store and category don't
                    System.out.println("Store - item exists, but store and category don't");
                    itemMap.get(item).setStore(newStore);
                    itemMap.get(item).setCategory(newCategory);
                    storeMap.put(store, newStore);
                    categoryMap.put(category, newCategory);
                }
            }
        } else {
            if (storeMap.containsKey(store)) {
                if (categoryMap.containsKey(category)) {
                    // category and store exist, but item doesn't
                    System.out.println("Store - category and store exist, but item doesn't");
                    itemMap.put(item, newItem);
                    itemsAZ.add(newItem);
                    storeMap.get(store).addItem(newItem);
                    categoryMap.get(category).addItem(newItem);
                } else {
                    // store exists, but item and category don't
                    System.out.println("Store - store exists, but item and category don't");
                    itemMap.put(item, newItem);
                    itemsAZ.add(newItem);
                    categoryMap.put(category, newCategory);
                    storeMap.get(store).addItem(newItem);
                }
            } else {
                if (categoryMap.containsKey(category)) {
                    // category exists, but item and store don't
                    System.out.println("Store - category exists, but item and store don't");
                    itemMap.put(item, newItem);
                    itemsAZ.add(newItem);
                    storeMap.put(store, newStore);
                    categoryMap.get(category).addItem(newItem);
                } else {
                    // item, store and category all don't exist
                    System.out.println("Store - item, store and category all don't exist");
                    itemMap.put(item, newItem);
                    itemsAZ.add(newItem);
                    storeMap.put(store, newStore);
                    categoryMap.put(category, newCategory);
                }
            }
        }
    }*/

    public void printData() {

        /*System.out.println("itemsAZ:");
        for (int i = 0; i < itemsAZ.size(); i++) {
            Item  item = itemsAZ.get(i);
            System.out.println("az item #" + i + " = " + item.getName());
        }*/
        System.out.println("itemsByCategory:");
        for (int i = 0; i < itemsByCategory.size(); i++) {
            Item  item = itemsByCategory.get(i);
            System.out.println("category item #" + i + " = " + item.getName());
        }
        System.out.println("itemsByStore:");
        for (int i = 0; i < itemsByStore.size(); i++) {
            Item  item = itemsByStore.get(i);
            System.out.println("store item #" + i + " = " + item.getName());
        }
    }
}