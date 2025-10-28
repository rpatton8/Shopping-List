package ryan.android.shopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryData {

    private final ArrayList<String> categoryList;
    private final ArrayList<String> categoryListWithBlank;
    private final ArrayList<String> categoryListWithAddNew;
    private final Map<String, Integer> categoryViewAllMap;
    private final Map<String, Integer> categoryViewInStockMap;
    private final Map<String, Integer> categoryViewNeededMap;
    private final Map<String, Integer> categoryViewPausedMap;

    public CategoryData () {
        categoryList = new ArrayList<>();
        categoryListWithBlank = new ArrayList<>();
        categoryListWithBlank.add("");
        categoryListWithAddNew = new ArrayList<>();
        categoryListWithAddNew.add("");
        categoryListWithAddNew.add("(add new category)");
        categoryViewAllMap = new HashMap<>();
        categoryViewInStockMap = new HashMap<>();
        categoryViewNeededMap = new HashMap<>();
        categoryViewPausedMap = new HashMap<>();
    }

    public ArrayList<String> getCategoryList() {
        return categoryList;
    }

    public ArrayList<String> getCategoryListWithBlank() {
        return categoryListWithBlank;
    }

    public ArrayList<String> getCategoryListWithAddNew() {
        return categoryListWithAddNew;
    }

    public Map<String, Integer> getCategoryViewAllMap() {
        return categoryViewAllMap;
    }

    public Map<String, Integer> getCategoryViewInStockMap() {
        return categoryViewInStockMap;
    }

    public Map<String, Integer> getCategoryViewNeededMap() {
        return categoryViewNeededMap;
    }

    public Map<String, Integer> getCategoryViewPausedMap() {
        return categoryViewPausedMap;
    }

    public void readCategory(String categoryName, int numItemsInViewAll, int numItemsInViewInStock,
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