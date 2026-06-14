package ryan.android.shopping;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;

class StoreData {

    private Context context;
    private ArrayList<String> storeList;
    private ArrayList<String> storeListWithBlank;
    private ArrayList<String> storeListWithAddNew;
    private HashMap<String, Integer> storeViewAllMap;
    private HashMap<String, Integer> storeViewInStockMap;
    private HashMap<String, Integer> storeViewNeededMap;
    private HashMap<String, Integer> storeViewPausedMap;

    StoreData (Context context) {
        setContext(context);
        setStoreList(new ArrayList<String>());
        setStoreListWithBlank(new ArrayList<String>());
        setStoreListWithAddNew(new ArrayList<String>());
        getStoreListWithBlank().add(getContext().getString(R.string.emptyString));
        getStoreListWithAddNew().add(getContext().getString(R.string.emptyString));
        getStoreListWithAddNew().add(getContext().getString(R.string.addNewStore));
        setStoreViewAllMap(new HashMap<String, Integer>());
        setStoreViewInStockMap(new HashMap<String, Integer>());
        setStoreViewNeededMap(new HashMap<String, Integer>());
        setStoreViewPausedMap(new HashMap<String, Integer>());
    }

    private StoreData getThis() {
        return this;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<String> getStoreList() {
        return storeList;
    }

    public void setStoreList(ArrayList<String> storeList) {
        this.storeList = storeList;
    }

    public ArrayList<String> getStoreListWithBlank() {
        return storeListWithBlank;
    }

    public void setStoreListWithBlank(ArrayList<String> storeListWithBlank) {
        this.storeListWithBlank = storeListWithBlank;
    }

    public ArrayList<String> getStoreListWithAddNew() {
        return storeListWithAddNew;
    }

    public void setStoreListWithAddNew(ArrayList<String> storeListWithAddNew) {
        this.storeListWithAddNew = storeListWithAddNew;
    }

    public HashMap<String, Integer> getStoreViewAllMap() {
        return storeViewAllMap;
    }

    public void setStoreViewAllMap(HashMap<String, Integer> storeViewAllMap) {
        this.storeViewAllMap = storeViewAllMap;
    }

    public HashMap<String, Integer> getStoreViewInStockMap() {
        return storeViewInStockMap;
    }

    public void setStoreViewInStockMap(HashMap<String, Integer> storeViewInStockMap) {
        this.storeViewInStockMap = storeViewInStockMap;
    }

    public HashMap<String, Integer> getStoreViewNeededMap() {
        return storeViewNeededMap;
    }

    public void setStoreViewNeededMap(HashMap<String, Integer> storeViewNeededMap) {
        this.storeViewNeededMap = storeViewNeededMap;
    }

    public HashMap<String, Integer> getStoreViewPausedMap() {
        return storeViewPausedMap;
    }

    public void setStoreViewPausedMap(HashMap<String, Integer> storeViewPausedMap) {
        this.storeViewPausedMap = storeViewPausedMap;
    }

    void readStore(String storeName, int numItemsInViewAll, int numItemsInViewInStock,
                             int numItemsInViewNeeded, int numItemsInViewPaused) {
        getStoreList().add(storeName);
        getStoreListWithBlank().add(storeName);
        getStoreListWithAddNew().add(storeName);
        getStoreViewAllMap().put(storeName, numItemsInViewAll);
        getStoreViewInStockMap().put(storeName, numItemsInViewInStock);
        getStoreViewNeededMap().put(storeName, numItemsInViewNeeded);
        getStoreViewPausedMap().put(storeName, numItemsInViewPaused);
    }

}