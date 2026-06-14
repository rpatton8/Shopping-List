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
        setCategoryList(new ArrayList<String>());
        setCategoryListWithBlank(new ArrayList<String>());
        setCategoryListWithAddNew(new ArrayList<String>());
        getCategoryListWithBlank().add(getContext().getString(R.string.emptyString));
        getCategoryListWithAddNew().add(getContext().getString(R.string.emptyString));
        getCategoryListWithAddNew().add(getContext().getString(R.string.addNewCategory));
        setCategoryViewAllMap(new HashMap<String, Integer>());
        setCategoryViewInStockMap(new HashMap<String, Integer>());
        setCategoryViewNeededMap(new HashMap<String, Integer>());
        setCategoryViewPausedMap(new HashMap<String, Integer>());
    }

    private CategoryData getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<String> categoryList) {
        this.categoryList = categoryList;
    }

    public ArrayList<String> getCategoryListWithBlank() {
        return categoryListWithBlank;
    }

    public void setCategoryListWithBlank(ArrayList<String> categoryListWithBlank) {
        this.categoryListWithBlank = categoryListWithBlank;
    }

    public ArrayList<String> getCategoryListWithAddNew() {
        return categoryListWithAddNew;
    }

    public void setCategoryListWithAddNew(ArrayList<String> categoryListWithAddNew) {
        this.categoryListWithAddNew = categoryListWithAddNew;
    }

    public HashMap<String, Integer> getCategoryViewAllMap() {
        return categoryViewAllMap;
    }

    public void setCategoryViewAllMap(HashMap<String, Integer> categoryViewAllMap) {
        this.categoryViewAllMap = categoryViewAllMap;
    }

    public HashMap<String, Integer> getCategoryViewInStockMap() {
        return categoryViewInStockMap;
    }

    public void setCategoryViewInStockMap(HashMap<String, Integer> categoryViewInStockMap) {
        this.categoryViewInStockMap = categoryViewInStockMap;
    }

    public HashMap<String, Integer> getCategoryViewNeededMap() {
        return categoryViewNeededMap;
    }

    public void setCategoryViewNeededMap(HashMap<String, Integer> categoryViewNeededMap) {
        this.categoryViewNeededMap = categoryViewNeededMap;
    }

    public HashMap<String, Integer> getCategoryViewPausedMap() {
        return categoryViewPausedMap;
    }

    public void setCategoryViewPausedMap(HashMap<String, Integer> categoryViewPausedMap) {
        this.categoryViewPausedMap = categoryViewPausedMap;
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