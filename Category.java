package ryan.android.shopping;

import java.util.ArrayList;

public class Category {

    private final String categoryName;
    private final ArrayList<Item> categoryItems;
    private boolean isExpanded;
    private boolean isContracted;

    Category(String name, Item item) {
        this.categoryName = name;
        categoryItems = new ArrayList<>();
        categoryItems.add(item);
        isExpanded = true;
        isContracted = false;
    }

    public String getName() {
        return categoryName;
    }

    public void addItem(Item item) {
        categoryItems.add(item);
    }

    public ArrayList<Item> getItemList() {
        return categoryItems;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setAsExpanded() {
        isExpanded = true;
        isContracted = false;
    }

    public boolean isContracted() {
        return isExpanded;
    }

    public void setAsContracted() {
        isExpanded = false;
        isContracted = true;
    }

    @Override
    public String toString() {
        return categoryName;
    }

}