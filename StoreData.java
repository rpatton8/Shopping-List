package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;

class StoreData {

    private ArrayList<String> storeList;
    private ArrayList<String> storeListWithBlank;
    private ArrayList<String> storeListWithAddNew;
    private HashMap<String, Integer> storeViewAllMap;
    private HashMap<String, Integer> storeViewInStockMap;
    private HashMap<String, Integer> storeViewNeededMap;
    private HashMap<String, Integer> storeViewPausedMap;

    StoreData () {
        storeList = new ArrayList<>();
        storeListWithBlank = new ArrayList<>();
        storeListWithAddNew = new ArrayList<>();
        storeListWithBlank.add("");
        storeListWithAddNew.add("");
        storeListWithAddNew.add("(add new store)");
        storeViewAllMap = new HashMap<>();
        storeViewInStockMap = new HashMap<>();
        storeViewNeededMap = new HashMap<>();
        storeViewPausedMap = new HashMap<>();
    }

    ArrayList<String> getStoreList() {
        return storeList;
    }

    ArrayList<String> getStoreListWithBlank() {
        return storeListWithBlank;
    }

    ArrayList<String> getStoreListWithAddNew() {
        return storeListWithAddNew;
    }

    HashMap<String, Integer> getStoreViewAllMap() {
        return storeViewAllMap;
    }

    HashMap<String, Integer> getStoreViewInStockMap() {
        return storeViewInStockMap;
    }

    HashMap<String, Integer> getStoreViewNeededMap() {
        return storeViewNeededMap;
    }

    HashMap<String, Integer> getStoreViewPausedMap() {
        return storeViewPausedMap;
    }

    void readStore(String storeName, int numItemsInViewAll, int numItemsInViewInStock,
                             int numItemsInViewNeeded, int numItemsInViewPaused) {
        storeList.add(storeName);
        storeListWithBlank.add(storeName);
        storeListWithAddNew.add(storeName);
        storeViewAllMap.put(storeName, numItemsInViewAll);
        storeViewInStockMap.put(storeName, numItemsInViewInStock);
        storeViewNeededMap.put(storeName, numItemsInViewNeeded);
        storeViewPausedMap.put(storeName, numItemsInViewPaused);
    }

}