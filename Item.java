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

    public void setContext(Context context) {
        getThis().context = context;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        getThis().itemName = itemName;
    }

    public String getBrandType() {
        return brandType;
    }

    public void setBrandType(String brandType) {
        getThis().brandType = brandType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        getThis().category = category;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        getThis().store = store;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        getThis().status = status;
    }

    public int getCategoryOrder() {
        return categoryOrder;
    }

    public void setCategoryOrder(int categoryOrder) {
        getThis().categoryOrder = categoryOrder;
    }

    public int getStoreOrder() {
        return storeOrder;
    }

    public void setStoreOrder(int storeOrder) {
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