package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;

class CategoryData {

    private ArrayList<String> categoryList;
    private ArrayList<String> categoryListWithBlank;
    private ArrayList<String> categoryListWithAddNew;
    private HashMap<String, Integer> categoryViewAllMap;
    private HashMap<String, Integer> categoryViewInStockMap;
    private HashMap<String, Integer> categoryViewNeededMap;
    private HashMap<String, Integer> categoryViewPausedMap;

    CategoryData () {
        categoryList = new ArrayList<>();
        categoryListWithBlank = new ArrayList<>();
        categoryListWithAddNew = new ArrayList<>();
        categoryListWithBlank.add("");
        categoryListWithAddNew.add("");
        categoryListWithAddNew.add("(add new category)");
        categoryViewAllMap = new HashMap<>();
        categoryViewInStockMap = new HashMap<>();
        categoryViewNeededMap = new HashMap<>();
        categoryViewPausedMap = new HashMap<>();
    }

    ArrayList<String> getCategoryList() {
        return categoryList;
    }

    ArrayList<String> getCategoryListWithBlank() {
        return categoryListWithBlank;
    }

    ArrayList<String> getCategoryListWithAddNew() {
        return categoryListWithAddNew;
    }

    HashMap<String, Integer> getCategoryViewAllMap() {
        return categoryViewAllMap;
    }

    HashMap<String, Integer> getCategoryViewInStockMap() {
        return categoryViewInStockMap;
    }

    HashMap<String, Integer> getCategoryViewNeededMap() {
        return categoryViewNeededMap;
    }

    HashMap<String, Integer> getCategoryViewPausedMap() {
        return categoryViewPausedMap;
    }

    void readCategory(String categoryName, int numItemsInViewAll, int numItemsInViewInStock,
                             int numItemsInViewNeeded, int numItemsInViewPaused) {
        categoryList.add(categoryName);
        categoryListWithBlank.add(categoryName);
        categoryListWithAddNew.add(categoryName);
        categoryViewAllMap.put(categoryName, numItemsInViewAll);
        categoryViewInStockMap.put(categoryName, numItemsInViewInStock);
        categoryViewNeededMap.put(categoryName, numItemsInViewNeeded);
        categoryViewPausedMap.put(categoryName, numItemsInViewPaused);
    }

}