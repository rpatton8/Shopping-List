package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreData {

    private ArrayList<String> storeList;
    private ArrayList<String> storeListWithBlank;
    private ArrayList<String> storeListWithAddNew;
    private Map<String, Integer> storeViewAllMap;
    private Map<String, Integer> storeViewInStockMap;
    private Map<String, Integer> storeViewNeededMap;
    private Map<String, Integer> storeViewPausedMap;

    public StoreData () {
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

    public ArrayList<String> getStoreList() {
        return storeList;
    }

    public ArrayList<String> getStoreListWithBlank() {
        return storeListWithBlank;
    }

    public ArrayList<String> getStoreListWithAddNew() {
        return storeListWithAddNew;
    }

    public Map<String, Integer> getStoreViewAllMap() {
        return storeViewAllMap;
    }

    public Map<String, Integer> getStoreViewInStockMap() {
        return storeViewInStockMap;
    }

    public Map<String, Integer> getStoreViewNeededMap() {
        return storeViewNeededMap;
    }

    public Map<String, Integer> getStoreViewPausedMap() {
        return storeViewPausedMap;
    }

    public void readStore(String storeName, int numItemsInViewAll, int numItemsInViewInStock,
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