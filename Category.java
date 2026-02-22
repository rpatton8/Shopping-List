package ryan.android.shopping;

import java.util.ArrayList;

public class Category {

    private String categoryName;
    private ArrayList<Item> categoryItems;

    public boolean isExpanded = true;
    public boolean isContracted = false;

    public Category(String name, Item item) {
        this.categoryName = name;
        categoryItems = new ArrayList<>();
        categoryItems.add(item);
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

    @Override
    public String toString() {
        return categoryName;
    }

}