package ryan.android.shopping;

import android.content.Context;

class Item implements Comparable<Item> {

    private Context context;

    private String name;
    private String brandType;
    private Category category;
    private Store store;
    private Status status;
    private int categoryOrder;
    private int storeOrder;

    Item(Context context, String name, String brandType, String category, String store) {
        this.context = context;
        this.name = name;
        this.brandType = brandType;
        this.category = new Category(category, this);
        this.store = new Store(store, this);
    }

    private Context getContext() {
        return context;
    }

    String getName() {
        return name;
    }

    String getBrandType() {
        return brandType;
    }

    Category getCategory() {
        return category;
    }

    Store getStore() {
        return store;
    }

    void setCategory(Category category) {
        this.category = category;
    }

    void setStore(Store store) {
        this.store = store;
    }

    void setStatus(Status status) {
        this.status = status;
    }

    Status getStatus() {
        return status;
    }

    void setCategoryOrder(int categoryOrder) {
        this.categoryOrder = categoryOrder;
    }

    int getCategoryOrder() {
        return this.categoryOrder;
    }

    void setStoreOrder(int storeOrder) {
        this.storeOrder = storeOrder;
    }

    int getStoreOrder() {
        return this.storeOrder;
    }

    void printItem() {
        System.out.println(getContext().getString(R.string.piLineBreak));
        System.out.println(getContext().getString(R.string.piItemName) + name);
        System.out.println(getContext().getString(R.string.piBrandType) + brandType);
        System.out.println(getContext().getString(R.string.piCategory) + category.toString());
        System.out.println(getContext().getString(R.string.piStore) + store.toString());
        System.out.println(getContext().getString(R.string.piStatus) + status.toString());

        // add optional data here

        System.out.println(getContext().getString(R.string.piCategoryOrder) + categoryOrder);
        System.out.println(getContext().getString(R.string.piStoreOrder) + storeOrder);
        System.out.println(getContext().getString(R.string.piLineBreak));
    }

    public int compareTo(Item item) {
        return this.name.compareTo(item.name);
    }

}