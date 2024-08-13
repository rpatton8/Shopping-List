package ryan.android.shopping;

import java.util.ArrayList;

public class CategoryData {

    private ArrayList<String> categoryList;
    private ArrayList<String> categoryListWithBlank;
    private ArrayList<String> categoryListWithAddNew;

    public CategoryData () {
        categoryList = new ArrayList<>();
        categoryListWithBlank = new ArrayList<>();
        categoryListWithAddNew = new ArrayList<>();
        categoryListWithBlank.add("");
        categoryListWithAddNew.add("");
        categoryListWithAddNew.add("(add new category)");
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


    public void readCategory(String categoryName) {
        categoryList.add(categoryName);
        categoryListWithBlank.add(categoryName);
        categoryListWithAddNew.add(categoryName);
    }

}