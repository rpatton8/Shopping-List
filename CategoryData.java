package ryan.android.shopping;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;

class CategoryData {

    private Context context;
    private ArrayList<String> categoryList;
    private ArrayList<String> categoryListWithBlank;
    private ArrayList<String> categoryListWithAddNew;
    private HashMap<String, Integer> categoryViewAllMap;
    private HashMap<String, Integer> categoryViewInStockMap;
    private HashMap<String, Integer> categoryViewNeededMap;
    private HashMap<String, Integer> categoryViewPausedMap;

    CategoryData (Context context) {
        setContext(context);
        setCategoryList(new ArrayList<>());
        setCategoryListWithBlank(new ArrayList<>());
        setCategoryListWithAddNew(new ArrayList<>());
        getCategoryListWithBlank().add(getContext().getString(R.string.emptyString));
        getCategoryListWithAddNew().add(getContext().getString(R.string.emptyString));
        getCategoryListWithAddNew().add(getContext().getString(R.string.addNewCategory));
        setCategoryViewAllMap(new HashMap<>());
        setCategoryViewInStockMap(new HashMap<>());
        setCategoryViewNeededMap(new HashMap<>());
        setCategoryViewPausedMap(new HashMap<>());
    }

    private CategoryData getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        getThis().context = context;
    }

    public ArrayList<String> getCategoryList() {
        return categoryList;
    }

    private void setCategoryList(ArrayList<String> categoryList) {
        getThis().categoryList = categoryList;
    }

    public ArrayList<String> getCategoryListWithBlank() {
        return categoryListWithBlank;
    }

    private void setCategoryListWithBlank(ArrayList<String> categoryListWithBlank) {
        getThis().categoryListWithBlank = categoryListWithBlank;
    }

    public ArrayList<String> getCategoryListWithAddNew() {
        return categoryListWithAddNew;
    }

    private void setCategoryListWithAddNew(ArrayList<String> categoryListWithAddNew) {
        getThis().categoryListWithAddNew = categoryListWithAddNew;
    }

    public HashMap<String, Integer> getCategoryViewAllMap() {
        return categoryViewAllMap;
    }

    private void setCategoryViewAllMap(HashMap<String, Integer> categoryViewAllMap) {
        getThis().categoryViewAllMap = categoryViewAllMap;
    }

    public HashMap<String, Integer> getCategoryViewInStockMap() {
        return categoryViewInStockMap;
    }

    private void setCategoryViewInStockMap(HashMap<String, Integer> categoryViewInStockMap) {
        getThis().categoryViewInStockMap = categoryViewInStockMap;
    }

    public HashMap<String, Integer> getCategoryViewNeededMap() {
        return categoryViewNeededMap;
    }

    private void setCategoryViewNeededMap(HashMap<String, Integer> categoryViewNeededMap) {
        getThis().categoryViewNeededMap = categoryViewNeededMap;
    }

    public HashMap<String, Integer> getCategoryViewPausedMap() {
        return categoryViewPausedMap;
    }

    private void setCategoryViewPausedMap(HashMap<String, Integer> categoryViewPausedMap) {
        getThis().categoryViewPausedMap = categoryViewPausedMap;
    }

    void readCategory(String categoryName, int numItemsInViewAll, int numItemsInViewInStock,
                             int numItemsInViewNeeded, int numItemsInViewPaused) {
        getCategoryList().add(categoryName);
        getCategoryListWithBlank().add(categoryName);
        getCategoryListWithAddNew().add(categoryName);
        getCategoryViewAllMap().put(categoryName, numItemsInViewAll);
        getCategoryViewInStockMap().put(categoryName, numItemsInViewInStock);
        getCategoryViewNeededMap().put(categoryName, numItemsInViewNeeded);
        getCategoryViewPausedMap().put(categoryName, numItemsInViewPaused);
    }

}