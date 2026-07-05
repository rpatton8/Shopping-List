package ryan.android.shopping;

import android.content.Context;

class Item implements Comparable<Item> {

    private Context context;

    private String itemName;
    private String brandType;
    private Category category;
    private Store store;
    private Status status;
    private int categoryOrder;
    private int storeOrder;

    Item(Context context, String itemName, String brandType, String category, String store) {
        setContext(context);
        setItemName(itemName);
        setBrandType(brandType);
        setCategory(new Category(getContext(), category, this));
        setStore(new Store(getContext(), store, this));
    }
    
    private Item getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        getThis().context = context;
    }

    String getItemName() {
        return itemName;
    }

    private void setItemName(String itemName) {
        getThis().itemName = itemName;
    }

    String getBrandType() {
        return brandType;
    }

    private void setBrandType(String brandType) {
        getThis().brandType = brandType;
    }

    Category getCategory() {
        return category;
    }

    void setCategory(Category category) {
        getThis().category = category;
    }

    Store getStore() {
        return store;
    }

    void setStore(Store store) {
        getThis().store = store;
    }

    Status getStatus() {
        if (status == null) {
            setStatus(new Status(getItemName(), getContext().getString(R.string.instock), getContext().getString(R.string.unchecked), getContext()));
        }
        return status;
    }

    void setStatus(Status status) {
        getThis().status = status;
    }

    int getCategoryOrder() {
        return categoryOrder;
    }

    void setCategoryOrder(int categoryOrder) {
        getThis().categoryOrder = categoryOrder;
    }

    int getStoreOrder() {
        return storeOrder;
    }

    void setStoreOrder(int storeOrder) {
        getThis().storeOrder = storeOrder;
    }

    void printItem() {
        System.out.println(getContext().getString(R.string.piLineBreak));
        System.out.println(getContext().getString(R.string.piItemName) + getItemName());
        System.out.println(getContext().getString(R.string.piBrandType) + getBrandType());
        System.out.println(getContext().getString(R.string.piCategory) + getCategory().toString());
        System.out.println(getContext().getString(R.string.piStore) + getStore().toString());
        System.out.println(getContext().getString(R.string.piStatus) + getStatus().toString());
        System.out.println(getContext().getString(R.string.piCategoryOrder) + getCategoryOrder());
        System.out.println(getContext().getString(R.string.piStoreOrder) + getStoreOrder());
        System.out.println(getContext().getString(R.string.piLineBreak));
    }

    public int compareTo(Item item) {
        return getThis().getItemName().compareTo(item.getItemName());
    }

}