package ryan.android.shopping;

import java.util.ArrayList;

public class Item {

    private String name;
    private String brandType;
    private ArrayList<Category> categories;
    private ArrayList<Store> stores;
    private Status status;

    public Item(String name, String brandType, String category, String store) {
        this.name = name;
        this.brandType = brandType;
        categories = new ArrayList<>();
        categories.add(new Category(category, this));
        stores = new ArrayList<>();
        stores.add(new Store(store, this));
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brandType;
    }

    public Category getCategory(int position) {
        return categories.get(position);
    }

    public Store getStore(int position) {
        return stores.get(position);
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void addStore(Store store) {
        stores.add(store);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}