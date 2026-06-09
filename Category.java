package ryan.android.shopping;

import android.content.Context;

import java.util.ArrayList;

class Category {

    private Context context;
    private String categoryName;
    private ArrayList<Item> categoryItems;
    private boolean isExpanded;
    private boolean isContracted;

    Category(Context context, String name, Item item) {
        this.context = context;
        this.categoryName = name;
        categoryItems = new ArrayList<>();
        categoryItems.add(item);
        isExpanded = true;
        isContracted = false;
    }

    private Context getContext() {
        return context;
    }

    String getName() {
        return categoryName;
    }

    void addItem(Item item) {
        categoryItems.add(item);
    }

    ArrayList<Item> getCategoryItemsList() {
        return categoryItems;
    }

    boolean categoryIsExpanded() {
        return isExpanded;
    }

    void setCategoryAsExpanded() {
        isExpanded = true;
        isContracted = false;
    }

    boolean categoryIsContracted() {
        return isContracted;
    }

    void setCategoryAsContracted() {
        isExpanded = false;
        isContracted = true;
    }

    public String toString() {
        return categoryName;
    }

    void printCategory() {
        
    }

}