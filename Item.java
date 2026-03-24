package ryan.android.shopping;

import android.support.annotation.NonNull;

public class Item implements Comparable<Item> {

    private String name;
    private String brandType;
    private Category category;
    private Store store;
    private Status status;
    private int categoryOrder;
    private int storeOrder;

    public Item(String name, String brandType, String category, String store) {
        this.name = name;
        this.brandType = brandType;
        this.category = new Category(category, this);
        this.store = new Store(store, this);
    }

    public String getName() {
        return name;
    }

    public String getBrandType() {
        return brandType;
    }

    public Category getCategory() {
        return category;
    }

    public Store getStore() {
        return store;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setCategoryOrder(int categoryOrder) {
        this.categoryOrder = categoryOrder;
    }

    public int getCategoryOrder() {
        return this.categoryOrder;
    }

    public void setStoreOrder(int storeOrder) {
        this.storeOrder = storeOrder;
    }

    public int getStoreOrder() {
        return this.storeOrder;
    }

    public void printItem() {
        System.out.println("--------------------------");
        System.out.println("Item Name: " + name);
        System.out.println("Brand/Type: " + brandType);
        System.out.println("Category: " + category.toString());
        System.out.println("Store: " + store.toString());
        System.out.println("Status: " + status.toString());
        System.out.println("Category Order: " + categoryOrder);
        System.out.println("Store Order: " + storeOrder);
    }

    @Override
    public int compareTo(@NonNull Item item) {
        return this.name.compareTo(item.name);
    }

}