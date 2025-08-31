package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreData {

    private ArrayList<String> storeList;
    private ArrayList<String> storeListWithBlank;
    private ArrayList<String> storeListWithAddNew;
    private Map<String, Integer> storeItemsNeededMap;

    public StoreData () {
        storeList = new ArrayList<>();
        storeListWithBlank = new ArrayList<>();
        storeListWithAddNew = new ArrayList<>();
        storeItemsNeededMap = new HashMap<>();
        storeListWithBlank.add("");
        storeListWithAddNew.add("");
        storeListWithAddNew.add("(add new store)");
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

    public Map<String, Integer> getStoreItemsNeededMap() {
        return storeItemsNeededMap;
    }

    public void readStore(String storeName, int numItemsNeeded) {
        storeList.add(storeName);
        storeListWithBlank.add(storeName);
        storeListWithAddNew.add(storeName);
        storeItemsNeededMap.put(storeName, numItemsNeeded);
    }

}