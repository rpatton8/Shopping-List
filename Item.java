package ryan.android.shopping;

public class Item {

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

    public String getBrand() {
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

}