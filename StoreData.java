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
        setStoreList(new ArrayList<>());
        setStoreListWithBlank(new ArrayList<>());
        setStoreListWithAddNew(new ArrayList<>());
        getStoreListWithBlank().add(getContext().getString(R.string.emptyString));
        getStoreListWithAddNew().add(getContext().getString(R.string.emptyString));
        getStoreListWithAddNew().add(getContext().getString(R.string.addNewStore));
        setStoreViewAllMap(new HashMap<>());
        setStoreViewInStockMap(new HashMap<>());
        setStoreViewNeededMap(new HashMap<>());
        setStoreViewPausedMap(new HashMap<>());
    }

    private StoreData getThis() {
        return this;
    }

    private Context getContext() {
        return getThis().context;
    }

    private void setContext(Context context) {
        getThis().context = context;
    }

    public ArrayList<String> getStoreList() {
        return getThis().storeList;
    }

    private void setStoreList(ArrayList<String> storeList) {
        getThis().storeList = storeList;
    }

    public ArrayList<String> getStoreListWithBlank() {
        return getThis().storeListWithBlank;
    }

    private void setStoreListWithBlank(ArrayList<String> storeListWithBlank) {
        getThis().storeListWithBlank = storeListWithBlank;
    }

    public ArrayList<String> getStoreListWithAddNew() {
        return getThis().storeListWithAddNew;
    }

    private void setStoreListWithAddNew(ArrayList<String> storeListWithAddNew) {
        getThis().storeListWithAddNew = storeListWithAddNew;
    }

    public HashMap<String, Integer> getStoreViewAllMap() {
        return getThis().storeViewAllMap;
    }

    private void setStoreViewAllMap(HashMap<String, Integer> storeViewAllMap) {
        getThis().storeViewAllMap = storeViewAllMap;
    }

    public HashMap<String, Integer> getStoreViewInStockMap() {
        return getThis().storeViewInStockMap;
    }

    private void setStoreViewInStockMap(HashMap<String, Integer> storeViewInStockMap) {
        getThis().storeViewInStockMap = storeViewInStockMap;
    }

    public HashMap<String, Integer> getStoreViewNeededMap() {
        return getThis().storeViewNeededMap;
    }

    private void setStoreViewNeededMap(HashMap<String, Integer> storeViewNeededMap) {
        getThis().storeViewNeededMap = storeViewNeededMap;
    }

    public HashMap<String, Integer> getStoreViewPausedMap() {
        return getThis().storeViewPausedMap;
    }

    private void setStoreViewPausedMap(HashMap<String, Integer> storeViewPausedMap) {
        getThis().storeViewPausedMap = storeViewPausedMap;
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