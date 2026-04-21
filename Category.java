package ryan.android.shopping;

import java.util.ArrayList;

class Category {

    private String categoryName;
    private ArrayList<Item> categoryItems;

    private boolean isExpanded;
    private boolean isContracted;

    Category(String name, Item item) {
        this.categoryName = name;
        categoryItems = new ArrayList<>();
        categoryItems.add(item);
        isExpanded = true;
        isContracted = false;
    }

    String getName() {
        return categoryName;
    }

    void addItem(Item item) {
        categoryItems.add(item);
    }

    ArrayList<Item> getItemList() {
        return categoryItems;
    }

    boolean isExpanded() {
        return isExpanded;
    }

    void setAsExpanded() {
        isExpanded = true;
        isContracted = false;
    }

    boolean isContracted() {
        return isContracted;
    }

    void setAsContracted() {
        isExpanded = false;
        isContracted = true;
    }

    public String toString() {
        return categoryName;
    }

}