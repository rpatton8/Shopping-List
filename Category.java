package ryan.android.shopping;

import android.content.Context;
import java.util.ArrayList;

class Category {

    private Context context;
    private Shopping shopping;
    private String categoryName;
    private ArrayList<Item> categoryItems;
    private boolean isExpanded;
    private boolean isContracted;

    Category(Context context, Shopping shopping, String name, Item item) {
        setContext(context);
        setShopping(shopping);
        setCategoryName(name);
        setCategoryItems(new ArrayList<>());
        getCategoryItems().add(item);

        if (Shopping.CATEGORY_TITLES_EXPANDED.equals(getShopping().getDefaultCategoryTitles())) {
            setCategoryAsExpanded();
        } else if (Shopping.CATEGORY_TITLES_CONTRACTED.equals(getShopping().getDefaultCategoryTitles())) {
            setCategoryAsContracted();
        }
    }

    private Category getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    public Shopping getShopping() {
        return shopping;
    }

    public void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    private void setContext(Context context) {
        getThis().context = context;
    }

    private String getCategoryName() {
        return categoryName;
    }

    private void setCategoryName(String categoryName) {
        getThis().categoryName = categoryName;
    }

    private ArrayList<Item> getCategoryItems() {
        return categoryItems;
    }

    private void setCategoryItems(ArrayList<Item> categoryItems) {
        getThis().categoryItems = categoryItems;
    }

    private boolean isExpanded() {
        return isExpanded;
    }

    private void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    private boolean isContracted() {
        return isContracted;
    }

    private void setContracted(boolean contracted) {
        isContracted = contracted;
    }

    void addItem(Item item) {
        getCategoryItems().add(item);
    }

    ArrayList<Item> getCategoryItemsList() {
        return getCategoryItems();
    }

    boolean categoryIsExpanded() {
        return isExpanded();
    }

    void setCategoryAsExpanded() {
        setExpanded(true);
        setContracted(false);
    }

    boolean categoryIsContracted() {
        return isContracted();
    }

    void setCategoryAsContracted() {
        setExpanded(false);
        setContracted(true);
    }

    public String toString() {
        return getCategoryName();
    }

    void printCategory() {
        System.out.println(getContext().getString(R.string.psCategoryName) + getCategoryName());
        for (int i = 0; i < getCategoryItems().size(); i++) {
            System.out.println(getContext().getString(R.string.psItemNum) + (i + 1) + getContext().getString(R.string.psColon) + getCategoryItems().get(i).getItemName());
        }
    }
}