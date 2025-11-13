package ryan.android.shopping;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import java.util.HashMap;
import java.util.Map;

public class Shopping extends AppCompatActivity {

    private ItemData itemData;
    private StatusData statusData;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBItemHelper dbItemHelper;
    private DBStatusHelper dbStatusHelper;
    private DBCategoryHelper dbCategoryHelper;
    private DBStoreHelper dbStoreHelper;

    public Boolean itemIsSelectedInInventory;
    public Boolean itemIsSelectedInShoppingList;
    public Item selectedItemInInventory;
    public Item selectedItemInShoppingList;
    public int selectedItemPositionInInventory;
    public int selectedItemPositionInShoppingList;
    public Map<Item, Boolean> itemIsClickedInInventory;
    public Map<Item, Boolean> itemIsClickedInShoppingList;
    public Map<Item, Boolean> itemIsChecked;

    public String mainTitle;
    public int storeListOrderNum;
    public String reorderItemsCategory;
    public String reorderItemsStore;
    public Boolean editItemInInventory;
    public Boolean editItemInShoppingList;

    public String inventoryView;
    public static final String INVENTORY_ALL = "view all";
    public static final String INVENTORY_INSTOCK = "view instock";
    public static final String INVENTORY_NEEDED = "view needed";
    public static final String INVENTORY_PAUSED = "view paused";

    public String inventorySortBy;
    public static final String SORT_BY_CATEGORY = "category";
    public static final String SORT_BY_STORE = "store";
    public static final String SORT_ALPHABETICAL = "alphabetical";

    public Parcelable reorderCategoriesViewState;
    public Parcelable reorderStoresViewState;
    public Parcelable reorderItemsViewState;
    public Parcelable shoppingListViewState;
    public Parcelable fullInventoryViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping);

        dbItemHelper = new DBItemHelper(this);
        dbStatusHelper = new DBStatusHelper(this);
        dbCategoryHelper = new DBCategoryHelper(this);
        dbStoreHelper = new DBStoreHelper(this);

        itemData = new ItemData();
        dbItemHelper.readItemDataByCategory(itemData);
        dbItemHelper.readItemDataByStore(itemData);
        statusData = dbStatusHelper.readStatusData();
        itemData.updateStatusesByCategory(statusData);
        categoryData = dbCategoryHelper.readCategoryData();
        storeData = dbStoreHelper.readStoreData();

        itemIsSelectedInInventory = false;
        itemIsSelectedInShoppingList = false;
        selectedItemInInventory = null;
        selectedItemInShoppingList = null;
        selectedItemPositionInInventory = 0;
        selectedItemPositionInShoppingList = 0;

        //itemIsClickedInInventory = new HashMap<>();
        //itemIsClickedInShoppingList = new HashMap<>();
        //itemIsChecked = new HashMap<>();

        storeListOrderNum = 0;
        reorderItemsCategory = "";
        reorderItemsStore = "";
        editItemInInventory = false;
        editItemInShoppingList = false;

        inventoryView = INVENTORY_ALL;
        inventorySortBy = SORT_BY_CATEGORY;

        Button fullInventory = findViewById(R.id.fullInventoryTopMenu);
        fullInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getFragmentManager().findFragmentById(R.id.fragments);
                if (f instanceof FullInventory) return;
                //fullInventoryViewState = null;
                loadFragment(new FullInventory());
            }
        });

        Button shoppingList = findViewById(R.id.shoppingListTopMenu);
        shoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getFragmentManager().findFragmentById(R.id.fragments);
                if (f instanceof ShoppingList) return;
                //shoppingListViewState = null;
                loadFragment(new ShoppingList());
            }
        });
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void updateItemData() {
        dbItemHelper.readItemDataByCategory(itemData);
        dbItemHelper.readItemDataByStore(itemData);
    }

    public StatusData getStatusData() {
        return statusData;
    }

    public void updateStatusData() {
        statusData = dbStatusHelper.readStatusData();
    }

    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void updateCategoryData() {
        categoryData = dbCategoryHelper.readCategoryData();
    }

    public StoreData getStoreData() {
        return storeData;
    }

    public void updateStoreData() {
        storeData = dbStoreHelper.readStoreData();
    }

    //public Map<Item, Boolean> getClickedInventoryMap() {
    //    return itemIsClickedInInventory;
    //}

    //public Map<Item, Boolean> getClickedShoppingListMap() {
    //    return itemIsClickedInShoppingList;
    //}

    //public Map<Item, Boolean> getCheckedMap() {
    //    return itemIsChecked;
    //}

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.commit();
    }

    public void clearAllData() {
        dbItemHelper.deleteDatabase();
        dbStatusHelper.deleteDatabase();
        dbCategoryHelper.deleteDatabase();
        dbStoreHelper.deleteDatabase();

        dbItemHelper = new DBItemHelper(this);
        dbStatusHelper = new DBStatusHelper(this);
        dbCategoryHelper = new DBCategoryHelper(this);
        dbStoreHelper = new DBStoreHelper(this);

        dbItemHelper.readItemDataByCategory(itemData);
        dbItemHelper.readItemDataByStore(itemData);
        statusData = dbStatusHelper.readStatusData();
        itemData.updateStatusesByCategory(statusData);
        categoryData = dbCategoryHelper.readCategoryData();
        storeData = dbStoreHelper.readStoreData();

        itemIsSelectedInInventory = false;
        itemIsSelectedInShoppingList = false;
        itemIsClickedInInventory = new HashMap<>();
        itemIsClickedInShoppingList = new HashMap<>();
        itemIsChecked = new HashMap<>();

        storeListOrderNum = 0;
        reorderItemsCategory = "";
        editItemInInventory = false;
        editItemInShoppingList = false;
        inventoryView = INVENTORY_ALL;
        inventorySortBy = SORT_BY_CATEGORY;
    }

    public void loadStoresAndCategories() {

        dbCategoryHelper.addNewCategory("Meals", 0);
        dbCategoryHelper.addNewCategory("Soups", 1);
        dbCategoryHelper.addNewCategory("Sides", 2);
        dbCategoryHelper.addNewCategory("Meat", 3);
        dbCategoryHelper.addNewCategory("Bread/Grains/Cereal", 4);
        dbCategoryHelper.addNewCategory("Eggs/Dairy", 5);
        dbCategoryHelper.addNewCategory("Condiments", 6);
        dbCategoryHelper.addNewCategory("Seasonings", 7);
        dbCategoryHelper.addNewCategory("Misc/Ingredients", 8);
        dbCategoryHelper.addNewCategory("Drinks", 9);
        dbCategoryHelper.addNewCategory("Snacks", 10);
        dbCategoryHelper.addNewCategory("Desserts", 11);
        dbCategoryHelper.addNewCategory("Candy", 12);
        dbCategoryHelper.addNewCategory("Pet Supplies", 13);
        dbCategoryHelper.addNewCategory("Toiletries", 14);
        dbCategoryHelper.addNewCategory("Household", 15);
        dbCategoryHelper.addNewCategory("Supplements", 16);

        dbStoreHelper.addNewStore("Vons", 0);
        dbStoreHelper.addNewStore("Rite Aid", 1);
        dbStoreHelper.addNewStore("Smart & Final", 2);
        dbStoreHelper.addNewStore("Costco", 3);
        dbStoreHelper.addNewStore("Walmart", 4);
        dbStoreHelper.addNewStore("Amazon", 5);
        dbStoreHelper.addNewStore("Stater Bros", 6);
        dbStoreHelper.addNewStore("CVS", 7);
        dbStoreHelper.addNewStore("Dollar Tree", 8);
        dbStoreHelper.addNewStore("Ralphs", 9);
        dbStoreHelper.addNewStore("Target", 10);
        dbStoreHelper.addNewStore("Pet Supplies Plus", 11);
        dbStoreHelper.addNewStore("Sprouts", 12);
        dbStoreHelper.addNewStore("Sam's Club", 13);
        dbStoreHelper.addNewStore("Staples", 14);
        dbStoreHelper.addNewStore("Woodranch", 15);
        dbStoreHelper.addNewStore("Yorba Linda Feed Store", 16);

    }

//------------------------------------------------------------------------------------------------//
//-------------------------------------Sort By Category-------------------------------------------//
//------------------------------------------------------------------------------------------------//

    public void loadCategoryData() {

        //------------------------------------Meals-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Sausage Biscuits", "Jimmy Dean Frozen", "Meals", "Vons", 0);
        dbStatusHelper.addNewStatus("Sausage Biscuits", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hamburger Helper", "Cheeseburger Macaroni", "Meals", "Vons", 1);
        dbStatusHelper.addNewStatus("Hamburger Helper", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Buffalo Chicken Bites", "TGIF or Frank's", "Meals", "Vons", 2);
        dbStatusHelper.addNewStatus("Buffalo Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Terriyaki Chicken Bites", "InnovAsian", "Meals", "Vons", 3);
        dbStatusHelper.addNewStatus("Terriyaki Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("TGIF Cheese Sticks", "TGIF (small 10pc)", "Meals", "Vons", 4);
        dbStatusHelper.addNewStatus("TGIF Cheese Sticks", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Frozen Pizza", "Thin Pepperoni", "Meals", "Vons", 5);
        dbStatusHelper.addNewStatus("Frozen Pizza", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Corn Dogs", "Foster Farms", "Meals", "Vons", 6);
        dbStatusHelper.addNewStatus("Corn Dogs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hot Dogs", "Bun Size", "Meals", "Vons", 7);
        dbStatusHelper.addNewStatus("Hot Dogs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hot Dog Buns", "(8 pack)", "Meals", "Vons", 8);
        dbStatusHelper.addNewStatus("Hot Dog Buns", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hamburger Patties", "to do", "Meals", "Vons", 9);
        dbStatusHelper.addNewStatus("Hamburger Patties", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hamburger Buns", "(8 pack)", "Meals", "Vons", 10);
        dbStatusHelper.addNewStatus("Hamburger Buns", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Pasta Roni 1", "Angel Hair Pasta", "Meals", "Vons", 11);
        dbStatusHelper.addNewStatus("Pasta Roni 1", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Pasta Roni 2", "Fetuccini Alfredo", "Meals", "Vons", 12);
        dbStatusHelper.addNewStatus("Pasta Roni 2", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Mac & Cheese", "Annie’s", "Meals", "Vons", 13);
        dbStatusHelper.addNewStatus("Mac & Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Gnocci", "Signature Select", "Meals", "Vons", 14);
        dbStatusHelper.addNewStatus("Gnocci", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Tortellini", "Barilla 3 Cheese", "Meals", "Vons", 15);
        dbStatusHelper.addNewStatus("Tortellini", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Meals", 16);
        categoryData.getCategoryViewInStockMap().put("Meals", 16);
        categoryData.getCategoryViewNeededMap().put("Meals", 0);
        categoryData.getCategoryViewPausedMap().put("Meals", 0);
        dbCategoryHelper.setCategoryViews("Meals", 16, 16, 0, 0);

        //------------------------------------Soups-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Spaghetti O's", "w/ Meatballs", "Soups", "Vons", 0);
        dbStatusHelper.addNewStatus("Spaghetti O's", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Chicken Noodle Soup", "Campbell's", "Soups", "Vons", 1);
        dbStatusHelper.addNewStatus("Chicken Noodle Soup", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Minestrone Soup", "Amy's", "Soups", "Vons", 2);
        dbStatusHelper.addNewStatus("Minestrone Soup", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vegetable Barley Soup", "Amy's", "Soups", "Vons", 3);
        dbStatusHelper.addNewStatus("Vegetable Barley Soup", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Beef Noodles", "Yakisoba", "Soups", "Stater Bros", 4);
        dbStatusHelper.addNewStatus("Beef Noodles", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Cup of Noodles", "Nissin", "Soups", "Vons", 5);
        dbStatusHelper.addNewStatus("Cup of Noodles", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Ramen Noodles", "Nissin", "Soups", "Dollar Tree", 6);
        dbStatusHelper.addNewStatus("Ramen Noodles", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Soups", 7);
        categoryData.getCategoryViewInStockMap().put("Soups", 7);
        categoryData.getCategoryViewNeededMap().put("Soups", 0);
        categoryData.getCategoryViewPausedMap().put("Soups", 0);
        dbCategoryHelper.setCategoryViews("Soups", 7, 7, 0, 0);

        //------------------------------------Sides-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Frozen French Fries", "Ore-Ida", "Sides", "Vons", 0);
        dbStatusHelper.addNewStatus("Frozen French Fries", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Texas Cheesy Bread", "New York Bakery", "Sides", "Vons", 1);
        dbStatusHelper.addNewStatus("Texas Cheesy Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Garlic Breadsticks", "New York Bakery", "Sides", "Vons", 2);
        dbStatusHelper.addNewStatus("Garlic Breadsticks", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Chicken Rice", "Knorr", "Sides", "Vons", 3);
        dbStatusHelper.addNewStatus("Chicken Rice", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Canned Corn", "Del Monte", "Sides", "Vons", 4);
        dbStatusHelper.addNewStatus("Canned Corn", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Sides", 5);
        categoryData.getCategoryViewInStockMap().put("Sides", 5);
        categoryData.getCategoryViewNeededMap().put("Sides", 0);
        categoryData.getCategoryViewPausedMap().put("Sides", 0);
        dbCategoryHelper.setCategoryViews("Sides", 5, 5, 0, 0);

        //------------------------------------Meat--------------------------------------------------

        dbItemHelper.addNewItemByCategory("Steak", "USDA", "Meat", "Vons", 0);
        dbStatusHelper.addNewStatus("Steak", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Ground Beef", "(1 pound)", "Meat", "Vons", 1);
        dbStatusHelper.addNewStatus("Ground Beef", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Frozen Meatballs", "Homestyle", "Meat", "Vons", 2);
        dbStatusHelper.addNewStatus("Frozen Meatballs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Pepperoni Slices", "Hormel", "Meat", "Vons", 3);
        dbStatusHelper.addNewStatus("Pepperoni Slices", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Quick Steak", "Gary's", "Meat", "Sam's Club", 4);
        dbStatusHelper.addNewStatus("Quick Steak", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Chicken Breast", "na", "Meat", "Vons", 5);
        dbStatusHelper.addNewStatus("Chicken Breast", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sliced Turkey", "to do", "Meat", "Vons", 6);
        dbStatusHelper.addNewStatus("Sliced Turkey", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sliced Ham", "to do", "Meat", "Vons", 7);
        dbStatusHelper.addNewStatus("Sliced Ham", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Ham Steak", "to do", "Meat", "Vons", 8);
        dbStatusHelper.addNewStatus("Ham Steak", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Meat", 9);
        categoryData.getCategoryViewInStockMap().put("Meat", 9);
        categoryData.getCategoryViewNeededMap().put("Meat", 0);
        categoryData.getCategoryViewPausedMap().put("Meat", 0);
        dbCategoryHelper.setCategoryViews("Meat", 9, 9, 0, 0);

        //------------------------------------Bread/Grains/Cereal-----------------------------------

        dbItemHelper.addNewItemByCategory("Thin Spaghetti", "Barilla Whole Grain", "Bread/Grains/Cereal", "Vons", 0);
        dbStatusHelper.addNewStatus("Thin Spaghetti", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Spiral Pasta", "Barilla Rotini", "Bread/Grains/Cereal", "Vons", 1);
        dbStatusHelper.addNewStatus("Spiral Pasta", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Wheat Bread", "Nature's Own", "Bread/Grains/Cereal", "Vons", 2);
        dbStatusHelper.addNewStatus("Wheat Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Baguette", "French", "Bread/Grains/Cereal", "Vons", 3);
        dbStatusHelper.addNewStatus("Baguette", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sourdough Bread", "San Luis Sourdough", "Bread/Grains/Cereal", "Vons", 4);
        dbStatusHelper.addNewStatus("Sourdough Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hard Rolls", "to do", "Bread/Grains/Cereal", "Vons", 5);
        dbStatusHelper.addNewStatus("Hard Rolls", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Thomas Muffins", "Original", "Bread/Grains/Cereal", "Vons", 6);
        dbStatusHelper.addNewStatus("Thomas Muffins", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Reese's Puffs Cereal", "Reese's Puffs", "Bread/Grains/Cereal", "Vons", 7);
        dbStatusHelper.addNewStatus("Reese's Puffs Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Cookie Crisp Cereal", "Cookie Crisp", "Bread/Grains/Cereal", "Vons", 8);
        dbStatusHelper.addNewStatus("Cookie Crisp Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Frosted Mini Wheat Cereal", "Frosted Mini Wheat", "Bread/Grains/Cereal", "Vons", 9);
        dbStatusHelper.addNewStatus("Frosted Mini Wheat Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Eggo Waffles", "Homestyle", "Bread/Grains/Cereal", "Vons", 10);
        dbStatusHelper.addNewStatus("Eggo Waffles", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Small Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 11);
        dbStatusHelper.addNewStatus("Small Flour Tortillas", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Large Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 12);
        dbStatusHelper.addNewStatus("Large Flour Tortillas", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Bread/Grains/Cereal", 13);
        categoryData.getCategoryViewInStockMap().put("Bread/Grains/Cereal", 13);
        categoryData.getCategoryViewNeededMap().put("Bread/Grains/Cereal", 0);
        categoryData.getCategoryViewPausedMap().put("Bread/Grains/Cereal", 0);
        dbCategoryHelper.setCategoryViews("Bread/Grains/Cereal", 13, 13, 0, 0);

        //----------------------------------------Eggs/Dairy----------------------------------------

        dbItemHelper.addNewItemByCategory("Milk", "Vitamin D", "Eggs/Dairy", "Vons", 0);
        dbStatusHelper.addNewStatus("Milk", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Eggs", "Grade AA", "Eggs/Dairy", "Vons", 1);
        dbStatusHelper.addNewStatus("Eggs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Honey Yogurt", "Greek Gods", "Eggs/Dairy", "Vons", 2);
        dbStatusHelper.addNewStatus("Honey Yogurt", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sour Cream", "to do", "Eggs/Dairy", "Vons", 3);
        dbStatusHelper.addNewStatus("Sour Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Salted Butter", "Challenge", "Eggs/Dairy", "Vons", 4);
        dbStatusHelper.addNewStatus("Salted Butter", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Clarified Butter", "Challenge", "Eggs/Dairy", "Ralphs", 5);
        dbStatusHelper.addNewStatus("Clarified Butter", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Shredded Cheese", "Mexican Blend", "Eggs/Dairy", "Vons", 6);
        dbStatusHelper.addNewStatus("Shredded Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("String Cheese", "Mozarella", "Eggs/Dairy", "Vons", 7);
        dbStatusHelper.addNewStatus("String Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("BD Cheese", "Black Diamond", "Eggs/Dairy", "Vons", 8);
        dbStatusHelper.addNewStatus("BD Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sliced Cheese", "Kraft Singles", "Eggs/Dairy", "Vons", 9);
        dbStatusHelper.addNewStatus("Sliced Cheese", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Eggs/Dairy", 10);
        categoryData.getCategoryViewInStockMap().put("Eggs/Dairy", 10);
        categoryData.getCategoryViewNeededMap().put("Eggs/Dairy", 0);
        categoryData.getCategoryViewPausedMap().put("Eggs/Dairy", 0);
        dbCategoryHelper.setCategoryViews("Eggs/Dairy", 10, 10, 0, 0);

        //------------------------------------Condiments--------------------------------------------

        dbItemHelper.addNewItemByCategory("Parmesan Cheese", "Kraft", "Condiments", "Vons", 0);
        dbStatusHelper.addNewStatus("Parmesan Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("A1 Sauce", "Original", "Condiments", "Vons", 1);
        dbStatusHelper.addNewStatus("A1 Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Ketchup", "Heinz", "Condiments", "Vons", 2);
        dbStatusHelper.addNewStatus("Ketchup", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Mustard", "Heinz", "Condiments", "Vons", 3);
        dbStatusHelper.addNewStatus("Mustard", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Pasta Sauce", "Ragu Meat", "Condiments", "Vons", 4);
        dbStatusHelper.addNewStatus("Pasta Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Taco Sauce", "Victoria's Mild", "Condiments", "Vons", 5);
        dbStatusHelper.addNewStatus("Taco Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Red Hot Sauce", "Frank's", "Condiments", "Vons", 6);
        dbStatusHelper.addNewStatus("Red Hot Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Chocolate Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 7);
        dbStatusHelper.addNewStatus("Chocolate Syrup", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Caramel Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 8);
        dbStatusHelper.addNewStatus("Caramel Syrup", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Maple Syrup", "Pearl Milling", "Condiments", "Vons", 9);
        dbStatusHelper.addNewStatus("Maple Syrup", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Honey", "Local Hive Clover", "Condiments", "Vons", 10);
        dbStatusHelper.addNewStatus("Honey", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Peanut Butter", "Skippy Creamy", "Condiments", "Vons", 11);
        dbStatusHelper.addNewStatus("Peanut Butter", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Soy Sauce", "Kikoman", "Condiments", "Vons", 12);
        dbStatusHelper.addNewStatus("Soy Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Woodranch BBQ Sauce", "1 pint", "Condiments", "Woodranch", 13);
        dbStatusHelper.addNewStatus("Woodranch BBQ Sauce", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Condiments", 14);
        categoryData.getCategoryViewInStockMap().put("Condiments", 14);
        categoryData.getCategoryViewNeededMap().put("Condiments", 0);
        categoryData.getCategoryViewPausedMap().put("Condiments", 0);
        dbCategoryHelper.setCategoryViews("Condiments", 14, 14, 0, 0);

        //------------------------------------Seasonings--------------------------------------------

        dbItemHelper.addNewItemByCategory("Salt & Pepeper", "na", "Seasonings", "Vons", 0);
        dbStatusHelper.addNewStatus("Salt & Pepeper", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Garlic Salt", "Lawry's", "Seasonings", "Vons", 1);
        dbStatusHelper.addNewStatus("Garlic Salt", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Lawry's Seasoning Salt", "Lawry's", "Seasonings", "Vons", 2);
        dbStatusHelper.addNewStatus("Lawry's Seasoning Salt", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Taco Seasoning", "any", "Seasonings", "Vons", 3);
        dbStatusHelper.addNewStatus("Taco Seasoning", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Ranch Dip Mix", "Laura Scudder's", "Seasonings", "Vons", 4);
        dbStatusHelper.addNewStatus("Ranch Dip Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vanilla Extract", "Signature Select", "Seasonings", "Vons", 5);
        dbStatusHelper.addNewStatus("Vanilla Extract", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Cinnamon Sugar", "McCormick's", "Seasonings", "Vons", 6);
        dbStatusHelper.addNewStatus("Cinnamon Sugar", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sprinkles", "3 types", "Seasonings", "Vons", 7);
        dbStatusHelper.addNewStatus("Sprinkles", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Seasonings", 8);
        categoryData.getCategoryViewInStockMap().put("Seasonings", 8);
        categoryData.getCategoryViewNeededMap().put("Seasonings", 0);
        categoryData.getCategoryViewPausedMap().put("Seasonings", 0);
        dbCategoryHelper.setCategoryViews("Seasonings", 8, 8, 0, 0);

        //---------------------------------Misc/Ingredients----------------------------------------

        dbItemHelper.addNewItemByCategory("Brown Sugar", "to do", "Misc/Ingredients", "Vons", 0);
        dbStatusHelper.addNewStatus("Brown Sugar", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Taco Shells", "to do", "Misc/Ingredients", "Vons", 1);
        dbStatusHelper.addNewStatus("Taco Shells", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Saltine Crackers", "Premium Original", "Misc/Ingredients", "Vons", 2);
        dbStatusHelper.addNewStatus("Saltine Crackers", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Semi-Sweet Choc. Chips", "Nestle", "Misc/Ingredients", "Vons", 3);
        dbStatusHelper.addNewStatus("Semi-Sweet Choc. Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vegetable Oil", "Crisco", "Misc/Ingredients", "Vons", 4);
        dbStatusHelper.addNewStatus("Vegetable Oil", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Extra Virgin Olive Oil", "to do", "Misc/Ingredients", "Vons", 5);
        dbStatusHelper.addNewStatus("Extra Virgin Olive Oil", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Non-Stick Spray", "Pam Original", "Misc/Ingredients", "Vons", 6);
        dbStatusHelper.addNewStatus("Non-Stick Spray", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Misc/Ingredients", 7);
        categoryData.getCategoryViewInStockMap().put("Misc/Ingredients", 7);
        categoryData.getCategoryViewNeededMap().put("Misc/Ingredients", 0);
        categoryData.getCategoryViewPausedMap().put("Misc/Ingredients", 0);
        dbCategoryHelper.setCategoryViews("Misc/Ingredients", 7, 7, 0, 0);

        //------------------------------------Drinks------------------------------------------------

        dbItemHelper.addNewItemByCategory("Soda Bottles", "Pepsi or Coke", "Drinks", "Vons", 0);
        dbStatusHelper.addNewStatus("Soda Bottles", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Soda Cans", "Pepsi or Coke", "Drinks", "Costco", 1);
        dbStatusHelper.addNewStatus("Soda Cans", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hot Chocolate Mix", "Swiss Miss Dark", "Drinks", "Vons", 2);
        dbStatusHelper.addNewStatus("Hot Chocolate Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Bottled Water", "any", "Drinks", "Vons", 3);
        dbStatusHelper.addNewStatus("Bottled Water", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Drinks", 4);
        categoryData.getCategoryViewInStockMap().put("Drinks", 4);
        categoryData.getCategoryViewNeededMap().put("Drinks", 0);
        categoryData.getCategoryViewPausedMap().put("Drinks", 0);
        dbCategoryHelper.setCategoryViews("Drinks", 4, 4, 0, 0);

        //------------------------------------Snacks------------------------------------------------

        dbItemHelper.addNewItemByCategory("Beef Jerky", "Archer Terriyaki", "Snacks", "Vons", 0);
        dbStatusHelper.addNewStatus("Beef Jerky", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Peanuts", "Honey Roasted", "Snacks", "Vons", 1);
        dbStatusHelper.addNewStatus("Peanuts", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Shell Peanuts", "Salted", "Snacks", "Vons", 2);
        dbStatusHelper.addNewStatus("Shell Peanuts", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sunflower Seeds", "Salted", "Snacks", "Vons", 3);
        dbStatusHelper.addNewStatus("Sunflower Seeds", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vinegar Chips", "Kettle", "Snacks", "Vons", 4);
        dbStatusHelper.addNewStatus("Vinegar Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("BBQ Chips", "Kettle", "Snacks", "Vons", 5);
        dbStatusHelper.addNewStatus("BBQ Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Doritos", "Cool Ranch", "Snacks", "Vons", 6);
        dbStatusHelper.addNewStatus("Doritos", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Lay's Chips", "Classic", "Snacks", "Vons", 7);
        dbStatusHelper.addNewStatus("Lay's Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Naan Crisps", "Stonefire", "Snacks", "Vons", 8);
        dbStatusHelper.addNewStatus("Naan Crisps", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Ritz Crackers", "Original", "Snacks", "Vons", 9);
        dbStatusHelper.addNewStatus("Ritz Crackers", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Goldfish", "Cheddar", "Snacks", "Vons", 10);
        dbStatusHelper.addNewStatus("Goldfish", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Cheez-Its", "Original", "Snacks", "Vons", 11);
        dbStatusHelper.addNewStatus("Cheez-Its", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Famous Amos Cookies", "12 Pack", "Snacks", "Vons", 12);
        dbStatusHelper.addNewStatus("Famous Amos Cookies", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dark Chocolate Pretzels", "Flipz", "Snacks", "CVS", 13);
        dbStatusHelper.addNewStatus("Dark Chocolate Pretzels", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Fudge Pudding", "Snack Pack", "Snacks", "Stater Bros", 14);
        dbStatusHelper.addNewStatus("Choc. Fudge Pudding", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Fudge Pirouette", "Pepperidge Farm", "Snacks", "Vons", 15);
        dbStatusHelper.addNewStatus("Choc. Fudge Pirouette", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Muddy Buddies", "Brownie Supreme", "Snacks", "Amazon", 16);
        dbStatusHelper.addNewStatus("Muddy Buddies", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Fortune Cookies", "to do", "Snacks", "Amazon", 17);
        dbStatusHelper.addNewStatus("Fortune Cookies", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Communion Wafers", "to do", "Snacks", "Amazon", 18);
        dbStatusHelper.addNewStatus("Communion Wafers", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Buttered Popcorn", "Movie Theater Butter", "Snacks", "Vons", 19);
        dbStatusHelper.addNewStatus("Buttered Popcorn", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Caramel Popcorn", "Cretors", "Snacks", "Vons", 20);
        dbStatusHelper.addNewStatus("Caramel Popcorn", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Double Drizzle Popcorn", "to do", "Snacks", "Walmart", 21);
        dbStatusHelper.addNewStatus("Double Drizzle Popcorn", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Snacks", 22);
        categoryData.getCategoryViewInStockMap().put("Snacks", 22);
        categoryData.getCategoryViewNeededMap().put("Snacks", 0);
        categoryData.getCategoryViewPausedMap().put("Snacks", 0);
        dbCategoryHelper.setCategoryViews("Snacks", 22, 22, 0, 0);

        //------------------------------------Desserts----------------------------------------------

        dbItemHelper.addNewItemByCategory("Choc. Malted Crunch Ice Cream", "Thrifty", "Desserts", "Vons", 0);
        dbStatusHelper.addNewStatus("Choc. Malted Crunch Ice Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Churros", "Tio Pepe’s or Hola!", "Desserts", "Smart & Final", 1);
        dbStatusHelper.addNewStatus("Churros", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 2);
        dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Chip Cookie Mix", "Gluten Free", "Desserts", "Stater Bros", 3);
        dbStatusHelper.addNewStatus("Choc. Chip Cookie Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Gingerbread Cookie Mix", "Betty Crocker", "Desserts", "Amazon", 4);
        dbStatusHelper.addNewStatus("Gingerbread Cookie Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreos", "(for crumbs)", "Desserts", "Vons", 5);
        dbStatusHelper.addNewStatus("Oreos", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreo Muffins", "12 pack", "Desserts", "Costco", 6);
        dbStatusHelper.addNewStatus("Oreo Muffins", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreo Cakesters", "Nabisco", "Desserts", "Vons", 7);
        dbStatusHelper.addNewStatus("Oreo Cakesters", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreo Pie Mix", "Jell-O No Bake", "Desserts", "Walmart", 8);
        dbStatusHelper.addNewStatus("Oreo Pie Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Malt Mix", "Nestle", "Desserts", "Stater Bros", 9);
        dbStatusHelper.addNewStatus("Choc. Malt Mix", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Desserts", 10);
        categoryData.getCategoryViewInStockMap().put("Desserts", 10);
        categoryData.getCategoryViewNeededMap().put("Desserts", 0);
        categoryData.getCategoryViewPausedMap().put("Desserts", 0);
        dbCategoryHelper.setCategoryViews("Desserts", 10, 10, 0, 0);

        //------------------------------------Candy-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Dark Chocolate Caramels", "Ghiradelli", "Candy", "Walmart", 0);
        dbStatusHelper.addNewStatus("Dark Chocolate Caramels", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Reese's PB Cups", "(individually wrapped)", "Candy", "Vons", 1);
        dbStatusHelper.addNewStatus("Reese's PB Cups", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Candy Corn", "Brach's", "Candy", "CVS", 2);
        dbStatusHelper.addNewStatus("Candy Corn", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hot Tamales", "na", "Candy", "Vons", 3);
        dbStatusHelper.addNewStatus("Hot Tamales", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Smarties", "na", "Candy", "Rite Aid", 4);
        dbStatusHelper.addNewStatus("Smarties", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sno Caps", "na", "Candy", "Dollar Tree", 5);
        dbStatusHelper.addNewStatus("Sno Caps", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Good & Plenty", "na", "Candy", "Vons", 6);
        dbStatusHelper.addNewStatus("Good & Plenty", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Mini M&M's", "na", "Candy", "Vons", 7);
        dbStatusHelper.addNewStatus("Mini M&M's", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dark Choc. M&M's", "na", "Candy", "Target", 8);
        dbStatusHelper.addNewStatus("Dark Choc. M&M's", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Caramel Squares", "na", "Candy", "Sprouts", 9);
        dbStatusHelper.addNewStatus("Caramel Squares", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Jelly Beans", "Sizzling Cinnamon", "Candy", "Amazon", 10);
        dbStatusHelper.addNewStatus("Jelly Beans", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Tootsie Rolls", "na", "Candy", "Vons", 11);
        dbStatusHelper.addNewStatus("Tootsie Rolls", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Fun Dip Sticks", "na", "Candy", "Smart & Final", 12);
        dbStatusHelper.addNewStatus("Fun Dip Sticks", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("72% Intense Dark Chocolate", "Ghiradelli", "Candy", "Walmart", 13);
        dbStatusHelper.addNewStatus("72% Intense Dark Chocolate", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Orange Tic Tacs", "na", "Candy", "Smart & Final", 14);
        dbStatusHelper.addNewStatus("Orange Tic Tacs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Orange Pez", "na", "Candy", "Amazon", 15);
        dbStatusHelper.addNewStatus("Orange Pez", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vanilla Taffy", "na", "Candy", "Amazon", 16);
        dbStatusHelper.addNewStatus("Vanilla Taffy", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vanilla Tootsie Rolls", "na", "Candy", "Amazon", 17);
        dbStatusHelper.addNewStatus("Vanilla Tootsie Rolls", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sixlets", "na", "Candy", "Amazon", 18);
        dbStatusHelper.addNewStatus("Sixlets", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Candy", 19);
        categoryData.getCategoryViewInStockMap().put("Candy", 19);
        categoryData.getCategoryViewNeededMap().put("Candy", 0);
        categoryData.getCategoryViewPausedMap().put("Candy", 0);
        dbCategoryHelper.setCategoryViews("Candy", 19, 19, 0, 0);

        //------------------------------------Pet Supplies-------------------------------------------

        dbItemHelper.addNewItemByCategory("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 0);
        dbStatusHelper.addNewStatus("Cat Food (wet)", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 1);
        dbStatusHelper.addNewStatus("Cat Food (dry)", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Delectables", "Squeeze Up 20 pack", "Pet Supplies", "Vons", 2);
        dbStatusHelper.addNewStatus("Delectables", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Cat Treats", "Temptations", "Pet Supplies", "Vons", 3);
        dbStatusHelper.addNewStatus("Cat Treats", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Kitty Liter", "Scoop Away Complete", "Pet Supplies", "Costco", 4);
        dbStatusHelper.addNewStatus("Kitty Liter", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dog Food (dry)", "Canidae All Life Stages", "Pet Supplies", "Yorba Linda Feed Store", 5);
        dbStatusHelper.addNewStatus("Dog Food (dry)", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Chicken Broth", "Kirkland Organic", "Pet Supplies", "Costco", 6);
        dbStatusHelper.addNewStatus("Chicken Broth", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Mashed Potatoes", "Main St. Bistro", "Pet Supplies", "Costco", 7);
        dbStatusHelper.addNewStatus("Mashed Potatoes", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Frozen Vegetables", "Kirkland Organic", "Pet Supplies", "Costco", 8);
        dbStatusHelper.addNewStatus("Frozen Vegetables", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Fresh Pet", "Chicken Recipe (6lb)", "Pet Supplies", "Costco", 9);
        dbStatusHelper.addNewStatus("Fresh Pet", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("100% Pure Pumpkin", "Libby's", "Pet Supplies", "Vons", 10);
        dbStatusHelper.addNewStatus("100% Pure Pumpkin", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Poop Bags", "Amazon Basics", "Pet Supplies", "Amazon", 11);
        dbStatusHelper.addNewStatus("Poop Bags", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Nitrile Gloves", "GMG 100 pack", "Pet Supplies", "Amazon", 12);
        dbStatusHelper.addNewStatus("Nitrile Gloves", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Pet Supplies", 13);
        categoryData.getCategoryViewInStockMap().put("Pet Supplies", 13);
        categoryData.getCategoryViewNeededMap().put("Pet Supplies", 0);
        categoryData.getCategoryViewPausedMap().put("Pet Supplies", 0);
        dbCategoryHelper.setCategoryViews("Pet Supplies", 13, 13, 0, 0);

        //------------------------------------Toiletries--------------------------------------------

        dbItemHelper.addNewItemByCategory("Hand Soap", "Lavender & Chamomile", "Toiletries", "Dollar Tree", 0);
        dbStatusHelper.addNewStatus("Hand Soap", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Body Wash", "Suave Mandarin", "Toiletries", "Vons", 1);
        dbStatusHelper.addNewStatus("Body Wash", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Shampoo", "Suave 2 in 1", "Toiletries", "Vons", 2);
        dbStatusHelper.addNewStatus("Shampoo", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Bar Soap", "Zum Bar Sea Salt", "Toiletries", "Sprouts", 3);
        dbStatusHelper.addNewStatus("Bar Soap", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Deodorant", "Old Spice", "Toiletries", "Vons", 4);
        dbStatusHelper.addNewStatus("Deodorant", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Toothpaste", "Tom's Antiplaque & Whitening", "Toiletries", "Amazon", 5);
        dbStatusHelper.addNewStatus("Toothpaste", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Floss", "Reach Mint Waxed", "Toiletries", "Amazon", 6);
        dbStatusHelper.addNewStatus("Floss", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Shaving Cream", "Sandalwood", "Toiletries", "Amazon", 7);
        dbStatusHelper.addNewStatus("Shaving Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Shaving Razors", "Gillette ProGlide", "Toiletries", "Amazon", 8);
        dbStatusHelper.addNewStatus("Shaving Razors", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Mouthwash", "Crest Whitening", "Toiletries", "Vons", 9);
        dbStatusHelper.addNewStatus("Mouthwash", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Cotton Swabs", "Q-Tips", "Toiletries", "Vons", 10);
        dbStatusHelper.addNewStatus("Cotton Swabs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Toothbrush Heads", "Radius Soft", "Toiletries", "Sprouts", 11);
        dbStatusHelper.addNewStatus("Toothbrush Heads", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sunscreen", "Hawaiian Tropic Sheer 50spf", "Toiletries", "Amazon", 12);
        dbStatusHelper.addNewStatus("Sunscreen", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Toiletries", 13);
        categoryData.getCategoryViewInStockMap().put("Toiletries", 13);
        categoryData.getCategoryViewNeededMap().put("Toiletries", 0);
        categoryData.getCategoryViewPausedMap().put("Toiletries", 0);
        dbCategoryHelper.setCategoryViews("Toiletries", 13, 13, 0, 0);

        //------------------------------------Household-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Febreeze Air Spray", "Heavy Duty", "Household", "Vons", 0);
        dbStatusHelper.addNewStatus("Febreeze Air Spray", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("All Purpose Cleaner", "Meyer's Lavender", "Household", "Vons", 1);
        dbStatusHelper.addNewStatus("All Purpose Cleaner", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Pet Stain Cleaner", "Rocco & Roxie", "Household", "Amazon", 2);
        dbStatusHelper.addNewStatus("Pet Stain Cleaner", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Laundry Detergent", "Woolite", "Household", "Vons", 3);
        dbStatusHelper.addNewStatus("Laundry Detergent", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Laundry Sanitizer", "Lysol", "Household", "Vons", 4);
        dbStatusHelper.addNewStatus("Laundry Sanitizer", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dryer Sheets", "Simply Done Fresh Linen", "Household", "Stater Bros", 5);
        dbStatusHelper.addNewStatus("Dryer Sheets", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Little Trees Air Fresheners", "True North", "Household", "Amazon", 6);
        dbStatusHelper.addNewStatus("Little Trees Air Fresheners", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Aluminum Foil", "Reynolds Wrap", "Household", "Vons", 7);
        dbStatusHelper.addNewStatus("Aluminum Foil", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Zip-Lock Bags (small)", "Sandwich", "Household", "Vons", 8);
        dbStatusHelper.addNewStatus("Zip-Lock Bags (small)", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Zip-Lock Bags (large)", "Freezer Gallon", "Household", "Vons", 9);
        dbStatusHelper.addNewStatus("Zip-Lock Bags (large)", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Saran Wrap", "Plastic Wrap", "Household", "Vons", 10);
        dbStatusHelper.addNewStatus("Saran Wrap", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Rubbing Alcohol", "Isopropyl", "Household", "CVS", 11);
        dbStatusHelper.addNewStatus("Rubbing Alcohol", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hydrogen Peroxide", "na", "Household", "CVS", 12);
        dbStatusHelper.addNewStatus("Hydrogen Peroxide", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Night Light Bulbs", "C7 E12", "Household", "Amazon", 13);
        dbStatusHelper.addNewStatus("Night Light Bulbs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Scrub Sponges", "Non-Scratch", "Household", "Vons", 14);
        dbStatusHelper.addNewStatus("Scrub Sponges", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dishwashing Brush", "Great Value", "Household", "Walmart", 15);
        dbStatusHelper.addNewStatus("Dishwashing Brush", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Small Trash Bags", "13 gallon", "Household", "Walmart", 16);
        dbStatusHelper.addNewStatus("Small Trash Bags", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Large Trash Bags", "33 gallon", "Household", "Walmart", 17);
        dbStatusHelper.addNewStatus("Large Trash Bags", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Compactor Bags", "18 gallon", "Household", "Walmart", 18);
        dbStatusHelper.addNewStatus("Compactor Bags", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dawn Powerwash", "Dish Cleaner", "Household", "Vons", 19);
        dbStatusHelper.addNewStatus("Dawn Powerwash", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dish Soap", "Dawn Platinum", "Household", "Vons", 20);
        dbStatusHelper.addNewStatus("Dish Soap", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Paper Plates", "to do", "Household", "Sam's Club", 21);
        dbStatusHelper.addNewStatus("Paper Plates", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Paper Towels", "Sparkle", "Household", "Walmart", 22);
        dbStatusHelper.addNewStatus("Paper Towels", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Toilet Paper", "Angel Soft", "Household", "Walmart", 23);
        dbStatusHelper.addNewStatus("Toilet Paper", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Multipurpose Paper", "Tru Red 20/96", "Household", "Staples", 24);
        dbStatusHelper.addNewStatus("Multipurpose Paper", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Packaging Tape", "Scotch Heavy Duty", "Household", "CVS", 25);
        dbStatusHelper.addNewStatus("Packaging Tape", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Household", 26);
        categoryData.getCategoryViewInStockMap().put("Household", 26);
        categoryData.getCategoryViewNeededMap().put("Household", 0);
        categoryData.getCategoryViewPausedMap().put("Household", 0);
        dbCategoryHelper.setCategoryViews("Household", 26, 26, 0, 0);

        //------------------------------------Supplements-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Triple Omega", "Nature Made", "Supplements", "Vons", 0);
        dbStatusHelper.addNewStatus("Triple Omega", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Multivitamin", "One a Day Men's", "Supplements", "Vons", 1);
        dbStatusHelper.addNewStatus("Multivitamin", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vitamin C", "Amazon Elements 1000 mg", "Supplements", "Amazon", 2);
        dbStatusHelper.addNewStatus("Vitamin C", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Magnesium", "Nature Made 400mg", "Supplements", "Vons", 3);
        dbStatusHelper.addNewStatus("Magnesium", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Zinc", "Sandhu Herbals 50mg", "Supplements", "Vons", 4);
        dbStatusHelper.addNewStatus("Zinc", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Calcium", "Nature's Truth 1200 mg", "Supplements", "Vons", 5);
        dbStatusHelper.addNewStatus("Calcium", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Biotin", "Natrol Biotin 10,000mcg", "Supplements", "Vons", 6);
        dbStatusHelper.addNewStatus("Biotin", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vitamin D3", "Nature Made 5000 IU", "Supplements", "Vons", 7);
        dbStatusHelper.addNewStatus("Vitamin D3", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Supplements", 8);
        categoryData.getCategoryViewInStockMap().put("Supplements", 8);
        categoryData.getCategoryViewNeededMap().put("Supplements", 0);
        categoryData.getCategoryViewPausedMap().put("Supplements", 0);
        dbCategoryHelper.setCategoryViews("Supplements", 8, 8, 0, 0);

        //------------------------------------------------------------------------------------------

        // total category items = 204

    }

//------------------------------------------------------------------------------------------------//
//----------------------------------------Sort By Store-------------------------------------------//
//------------------------------------------------------------------------------------------------//

    public void loadStoreData() {

        //------------------------------------Vons--------------------------------------------------

        dbItemHelper.addNewItemByStore("Sausage Biscuits", "Jimmy Dean Frozen", "Meals", "Vons", 0);
        //dbStatusHelper.addNewStatus("Sausage Biscuits", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hamburger Helper", "Cheeseburger Macaroni", "Meals", "Vons", 1);
        //dbStatusHelper.addNewStatus("Hamburger Helper", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Buffalo Chicken Bites", "TGIF or Frank's", "Meals", "Vons", 2);
        //dbStatusHelper.addNewStatus("Buffalo Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Terriyaki Chicken Bites", "InnovAsian", "Meals", "Vons", 3);
        //dbStatusHelper.addNewStatus("Terriyaki Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("TGIF Cheese Sticks", "TGIF (small 10pc)", "Meals", "Vons", 4);
        //dbStatusHelper.addNewStatus("TGIF Cheese Sticks", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Frozen Pizza", "Thin Pepperoni", "Meals", "Vons", 5);
        //dbStatusHelper.addNewStatus("Frozen Pizza", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Corn Dogs", "Foster Farms", "Meals", "Vons", 6);
        //dbStatusHelper.addNewStatus("Corn Dogs", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hot Dogs", "Bun Size", "Meals", "Vons", 7);
        //dbStatusHelper.addNewStatus("Hot Dogs", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hot Dog Buns", "(8 pack)", "Meals", "Vons", 8);
        //dbStatusHelper.addNewStatus("Hot Dog Buns", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hamburger Patties", "to do", "Meals", "Vons", 9);
        //dbStatusHelper.addNewStatus("Hamburger Patties", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hamburger Buns", "(8 pack)", "Meals", "Vons", 10);
        //dbStatusHelper.addNewStatus("Hamburger Buns", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Pasta Roni 1", "Angel Hair Pasta", "Meals", "Vons", 11);
        //dbStatusHelper.addNewStatus("Pasta Roni 1", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Pasta Roni 2", "Fetuccini Alfredo", "Meals", "Vons", 12);
        //dbStatusHelper.addNewStatus("Pasta Roni 2", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Mac & Cheese", "Annie’s", "Meals", "Vons", 13);
        //dbStatusHelper.addNewStatus("Mac & Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Gnocci", "Signature Select", "Meals", "Vons", 14);
        //dbStatusHelper.addNewStatus("Gnocci", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Spaghetti O's", "w/ Meatballs", "Soups", "Vons", 15);
        //dbStatusHelper.addNewStatus("Spaghetti O's", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Chicken Noodle Soup", "Campbell's", "Soups", "Vons", 16);
        //dbStatusHelper.addNewStatus("Chicken Noodle Soup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Minestrone Soup", "Amy's", "Soups", "Vons", 17);
        //dbStatusHelper.addNewStatus("Minestrone Soup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vegetable Barley Soup", "Amy's", "Soups", "Vons", 18);
        //dbStatusHelper.addNewStatus("Vegetable Barley Soup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cup of Noodles", "Nissin", "Soups", "Vons", 19);
        //dbStatusHelper.addNewStatus("Cup of Noodles", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Frozen French Fries", "Ore-Ida", "Sides", "Vons", 20);
        //dbStatusHelper.addNewStatus("Frozen French Fries", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Texas Cheesy Bread", "New York Bakery", "Sides", "Vons", 21);
        //dbStatusHelper.addNewStatus("Texas Cheesy Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Chicken Rice", "Knorr", "Sides", "Vons", 22);
        //dbStatusHelper.addNewStatus("Chicken Rice", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Canned Corn", "Del Monte", "Sides", "Vons", 23);
        //dbStatusHelper.addNewStatus("Canned Corn", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Steak", "USDA", "Meat", "Vons", 24);
        //dbStatusHelper.addNewStatus("Steak", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ground Beef", "(1 pound)", "Meat", "Vons", 25);
        //dbStatusHelper.addNewStatus("Ground Beef", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Frozen Meatballs", "Homestyle", "Meat", "Vons", 26);
        //dbStatusHelper.addNewStatus("Frozen Meatballs", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Pepperoni Slices", "Hormel", "Meat", "Vons", 27);
        //dbStatusHelper.addNewStatus("Pepperoni Slices", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Chicken Breast", "na", "Meat", "Vons", 28);
        //dbStatusHelper.addNewStatus("Chicken Breast", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sliced Turkey", "to do", "Meat", "Vons", 29);
        //dbStatusHelper.addNewStatus("Sliced Turkey", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sliced Ham", "to do", "Meat", "Vons", 30);
        //dbStatusHelper.addNewStatus("Sliced Ham", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ham Steak", "to do", "Meat", "Vons", 31);
        //dbStatusHelper.addNewStatus("Ham Steak", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Thin Spaghetti", "Barilla Whole Grain", "Bread/Grains/Cereal", "Vons", 32);
        //dbStatusHelper.addNewStatus("Thin Spaghetti", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Spiral Pasta", "Barilla Rotini", "Bread/Grains/Cereal", "Vons", 33);
        //dbStatusHelper.addNewStatus("Spiral Pasta", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Wheat Bread", "Nature's Own", "Bread/Grains/Cereal", "Vons", 34);
        //dbStatusHelper.addNewStatus("Wheat Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Baguette", "French", "Bread/Grains/Cereal", "Vons", 35);
        //dbStatusHelper.addNewStatus("Baguette", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sourdough Bread", "San Luis Sourdough", "Bread/Grains/Cereal", "Vons", 36);
        //dbStatusHelper.addNewStatus("Sourdough Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Thomas Muffins", "Original", "Bread/Grains/Cereal", "Vons", 37);
        //dbStatusHelper.addNewStatus("Thomas Muffins", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Reese's Puffs Cereal", "Reese's Puffs", "Bread/Grains/Cereal", "Vons", 38);
        //dbStatusHelper.addNewStatus("Reese's Puffs Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cookie Crisp Cereal", "Cookie Crisp", "Bread/Grains/Cereal", "Vons", 39);
        //dbStatusHelper.addNewStatus("Cookie Crisp Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Frosted Mini Wheat Cereal", "Frosted Mini Wheat", "Bread/Grains/Cereal", "Vons", 40);
        //dbStatusHelper.addNewStatus("Frosted Mini Wheat Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Eggo Waffles", "Homestyle", "Bread/Grains/Cereal", "Vons", 41);
        //dbStatusHelper.addNewStatus("Eggo Waffles", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Milk", "Vitamin D", "Eggs/Dairy", "Vons", 42);
        //dbStatusHelper.addNewStatus("Milk", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Eggs", "Grade AA", "Eggs/Dairy", "Vons", 43);
        //dbStatusHelper.addNewStatus("Eggs", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Honey Yogurt", "Greek Gods", "Eggs/Dairy", "Vons", 44);
        //dbStatusHelper.addNewStatus("Honey Yogurt", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Salted Butter", "Challenge", "Eggs/Dairy", "Vons", 45);
        //dbStatusHelper.addNewStatus("Salted Butter", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Shredded Cheese", "Mexican Blend", "Eggs/Dairy", "Vons", 46);
        //dbStatusHelper.addNewStatus("Shredded Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("String Cheese", "Mozarella", "Eggs/Dairy", "Vons", 47);
        //dbStatusHelper.addNewStatus("String Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("BD Cheese", "Black Diamond", "Eggs/Dairy", "Vons", 48);
        //dbStatusHelper.addNewStatus("BD Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Non-Stick Spray", "Pam Original", "Misc/Ingredients", "Vons", 49);
        //dbStatusHelper.addNewStatus("Non-Stick Spray", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Parmesan Cheese", "Kraft", "Condiments", "Vons", 50);
        //dbStatusHelper.addNewStatus("Parmesan Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("A1 Sauce", "Original", "Condiments", "Vons", 51);
        //dbStatusHelper.addNewStatus("A1 Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ketchup", "Heinz", "Condiments", "Vons", 52);
        //dbStatusHelper.addNewStatus("Ketchup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Mustard", "Heinz", "Condiments", "Vons", 53);
        //dbStatusHelper.addNewStatus("Mustard", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Pasta Sauce", "Ragu Meat", "Condiments", "Vons", 54);
        //dbStatusHelper.addNewStatus("Pasta Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Maple Syrup", "Pearl Milling", "Condiments", "Vons", 55);
        //dbStatusHelper.addNewStatus("Maple Syrup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Honey", "Local Hive Clover", "Condiments", "Vons", 56);
        //dbStatusHelper.addNewStatus("Honey", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Peanut Butter", "Skippy Creamy", "Condiments", "Vons", 57);
        //dbStatusHelper.addNewStatus("Peanut Butter", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Soy Sauce", "Kikoman", "Condiments", "Vons", 58);
        //dbStatusHelper.addNewStatus("Soy Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Brown Sugar", "to do", "Misc/Ingredients", "Vons", 0);
        //dbStatusHelper.addNewStatus("Brown Sugar", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Salt & Pepeper", "na", "Seasonings", "Vons", 60);
        //dbStatusHelper.addNewStatus("Salt & Pepeper", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Garlic Salt", "Lawry's", "Seasonings", "Vons", 61);
        //dbStatusHelper.addNewStatus("Garlic Salt", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Lawry's Seasoning Salt", "Lawry's", "Seasonings", "Vons", 62);
        //dbStatusHelper.addNewStatus("Lawry's Seasoning Salt", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ranch Dip Mix", "Laura Scudder's", "Seasonings", "Vons", 63);
        //dbStatusHelper.addNewStatus("Ranch Dip Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vanilla Extract", "Signature Select", "Seasonings", "Vons", 64);
        //dbStatusHelper.addNewStatus("Vanilla Extract", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cinnamon Sugar", "McCormick's", "Seasonings", "Vons", 65);
        //dbStatusHelper.addNewStatus("Cinnamon Sugar", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sprinkles", "3 types", "Seasonings", "Vons", 66);
        //dbStatusHelper.addNewStatus("Sprinkles", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Soda Bottles", "Pepsi or Coke", "Drinks", "Vons", 67);
        //dbStatusHelper.addNewStatus("Soda Bottles", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hot Chocolate Mix", "Swiss Miss Dark", "Drinks", "Vons", 68);
        //dbStatusHelper.addNewStatus("Hot Chocolate Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Bottled Water", "any", "Drinks", "Vons", 69);
        //dbStatusHelper.addNewStatus("Bottled Water", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Beef Jerky", "Archer Terriyaki", "Snacks", "Vons", 70);
        //dbStatusHelper.addNewStatus("Beef Jerky", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Peanuts", "Honey Roasted", "Snacks", "Vons", 71);
        //dbStatusHelper.addNewStatus("Peanuts", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Shell Peanuts", "Salted", "Snacks", "Vons", 72);
        //dbStatusHelper.addNewStatus("Shell Peanuts", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sunflower Seeds", "Salted", "Snacks", "Vons", 73);
        //dbStatusHelper.addNewStatus("Sunflower Seeds", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vinegar Chips", "Kettle", "Snacks", "Vons", 74);
        //dbStatusHelper.addNewStatus("Vinegar Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("BBQ Chips", "Kettle", "Snacks", "Vons", 75);
        //dbStatusHelper.addNewStatus("BBQ Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Doritos", "Cool Ranch", "Snacks", "Vons", 76);
        //dbStatusHelper.addNewStatus("Doritos", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Lay's Chips", "Classic", "Snacks", "Vons", 77);
        //dbStatusHelper.addNewStatus("Lay's Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Naan Crisps", "Stonefire", "Snacks", "Vons", 78);
        //dbStatusHelper.addNewStatus("Naan Crisps", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Oreo Cakesters", "Nabisco", "Snacks", "Vons", 79);
        //dbStatusHelper.addNewStatus("Oreo Cakesters", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Goldfish", "Cheddar", "Snacks", "Vons", 80);
        //dbStatusHelper.addNewStatus("Goldfish", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cheez-Its", "Original", "Snacks", "Vons", 81);
        //dbStatusHelper.addNewStatus("Cheez-Its", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Famous Amos Cookies", "12 Pack", "Snacks", "Vons", 82);
        //dbStatusHelper.addNewStatus("Famous Amos Cookies", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Fudge Pirouette", "Pepperidge Farm", "Snacks", "Vons", 83);
        //dbStatusHelper.addNewStatus("Choc. Fudge Pirouette", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 84);
        //dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Oreos", "for crumbs", "Desserts", "Vons", 85);
        //dbStatusHelper.addNewStatus("Oreos", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Malted Crunch Ice Cream", "Thrifty", "Desserts", "Vons", 86);
        //dbStatusHelper.addNewStatus("Choc. Malted Crunch Ice Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Reese's PB Cups", "(individually wrapped)", "Candy", "Vons", 87);
        //dbStatusHelper.addNewStatus("Reese's PB Cups", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hot Tamales", "na", "Candy", "Vons", 88);
        //dbStatusHelper.addNewStatus("Hot Tamales", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Mini M&M's", "na", "Candy", "Vons", 89);
        //dbStatusHelper.addNewStatus("Mini M&M's", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Tootsie Rolls", "na", "Candy", "Vons", 90);
        //dbStatusHelper.addNewStatus("Tootsie Rolls", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 91);
        //dbStatusHelper.addNewStatus("Cat Food (wet)", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("100% Pure Pumpkin", "Libby's", "Pet Supplies", "Vons", 92);
        //dbStatusHelper.addNewStatus("100% Pure Pumpkin", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Delectables", "Squeeze Up 20 pack", "Pet Supplies", "Vons", 93);
        //dbStatusHelper.addNewStatus("Delectables", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Body Wash", "Suave Mandarin", "Toiletries", "Vons", 94);
        //dbStatusHelper.addNewStatus("Body Wash", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Shampoo", "Suave 2 in 1", "Toiletries", "Vons", 95);
        //dbStatusHelper.addNewStatus("Shampoo", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Deodorant", "Old Spice", "Toiletries", "Vons", 96);
        //dbStatusHelper.addNewStatus("Deodorant", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Mouthwash", "Crest Whitening", "Toiletries", "Vons", 97);
        //dbStatusHelper.addNewStatus("Mouthwash", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cotton Swabs", "Q-Tips", "Toiletries", "Vons", 98);
        //dbStatusHelper.addNewStatus("Cotton Swabs", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Febreeze Air Spray", "Heavy Duty", "Household", "Vons", 99);
        //dbStatusHelper.addNewStatus("Febreeze Air Spray", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("All Purpose Cleaner", "Meyer's Lavender", "Household", "Vons", 100);
        //dbStatusHelper.addNewStatus("All Purpose Cleaner", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Laundry Detergent", "Woolite", "Household", "Vons", 101);
        //dbStatusHelper.addNewStatus("Laundry Detergent", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Laundry Sanitizer", "Lysol", "Household", "Vons", 102);
        //dbStatusHelper.addNewStatus("Laundry Sanitizer", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Aluminum Foil", "Reynolds Wrap", "Household", "Vons", 103);
        //dbStatusHelper.addNewStatus("Aluminum Foil", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Zip-Lock Bags (small)", "Sandwich", "Household", "Vons", 104);
        //dbStatusHelper.addNewStatus("Zip-Lock Bags (small)", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Zip-Lock Bags (large)", "Freezer Gallon", "Household", "Vons", 105);
        //dbStatusHelper.addNewStatus("Zip-Lock Bags (large)", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Saran Wrap", "Plastic Wrap", "Household", "Vons", 106);
        //dbStatusHelper.addNewStatus("Saran Wrap", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Scrub Sponges", "Non-Scratch", "Household", "Vons", 107);
        //dbStatusHelper.addNewStatus("Scrub Sponges", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Dawn Powerwash", "Dish Cleaner", "Household", "Vons", 108);
        //dbStatusHelper.addNewStatus("Dawn Powerwash", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Dish Soap", "Dawn Platinum", "Household", "Vons", 109);
        //dbStatusHelper.addNewStatus("Dish Soap", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Multivitamin", "One a Day Men's", "Supplements", "Vons", 110);
        //dbStatusHelper.addNewStatus("Multivitamin", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Triple Omega", "Nature Made", "Supplements", "Vons", 111);
        //dbStatusHelper.addNewStatus("Triple Omega", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Magnesium", "Nature Made 400mg", "Supplements", "Vons", 112);
        //dbStatusHelper.addNewStatus("Magnesium", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Zinc", "Sandhu Herbals 50mg", "Supplements", "Vons", 113);
        //dbStatusHelper.addNewStatus("Zinc", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Calcium", "Nature's Truth 1200 mg", "Supplements", "Vons", 114);
        //dbStatusHelper.addNewStatus("Calcium", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Biotin", "Natrol Biotin 10,000mcg", "Supplements", "Vons", 115);
        //dbStatusHelper.addNewStatus("Biotin", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vitamin D3", "Nature Made 5000 IU", "Supplements", "Vons", 116);
        //dbStatusHelper.addNewStatus("Vitamin D3", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Small Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 117);
        //dbStatusHelper.addNewStatus("Small Flour Tortillas", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Large Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 118);
        //dbStatusHelper.addNewStatus("Large Flour Tortillas", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sour Cream", "to do", "Eggs/Dairy", "Vons", 119);
        //dbStatusHelper.addNewStatus("Sour Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Red Hot Sauce", "Frank's", "Condiments", "Vons", 120);
        //dbStatusHelper.addNewStatus("Red Hot Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Taco Sauce", "Victoria's Mild", "Condiments", "Vons", 121);
        //dbStatusHelper.addNewStatus("Taco Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Taco Seasoning", "any", "Seasonings", "Vons", 122);
        //dbStatusHelper.addNewStatus("Taco Seasoning", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Taco Shells", "to do", "Misc/Ingredients", "Vons", 123);
        //dbStatusHelper.addNewStatus("Taco Shells", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Tortellini", "Barilla 3 Cheese", "Meals", "Vons", 124);
        //dbStatusHelper.addNewStatus("Tortellini", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Garlic Breadsticks", "New York Bakery", "Sides", "Vons", 125);
        //dbStatusHelper.addNewStatus("Garlic Breadsticks", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Good & Plenty", "na", "Candy", "Vons", 126);
        //dbStatusHelper.addNewStatus("Good & Plenty", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sliced Cheese", "Kraft Singles", "Eggs/Dairy", "Vons", 127);
        //dbStatusHelper.addNewStatus("Sliced Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cat Treats", "Temptations", "Pet Supplies", "Vons", 128);
        //dbStatusHelper.addNewStatus("Cat Treats", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hard Rolls", "to do", "Bread/Grains/Cereal", "Vons", 129);
        //dbStatusHelper.addNewStatus("Hard Rolls", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Saltine Crackers", "Premium Original", "Misc/Ingredients", "Vons", 130);
        //dbStatusHelper.addNewStatus("Saltine Crackers", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Semi-Sweet Choc. Chips", "Nestle", "Misc/Ingredients", "Vons", 131);
        //dbStatusHelper.addNewStatus("Semi-Sweet Choc. Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vegetable Oil", "Crisco", "Misc/Ingredients", "Vons", 132);
        //dbStatusHelper.addNewStatus("Vegetable Oil", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Extra Virgin Olive Oil", "to do", "Misc/Ingredients", "Vons", 133);
        //dbStatusHelper.addNewStatus("Extra Virgin Olive Oil", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Buttered Popcorn", "Movie Theater Butter", "Snacks", "Vons", 134);
        //dbStatusHelper.addNewStatus("Buttered Popcorn", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Caramel Popcorn", "Cretors", "Snacks", "Vons", 135);
        //dbStatusHelper.addNewStatus("Caramel Popcorn", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ritz Crackers", "Original", "Snacks", "Vons", 136);
        //dbStatusHelper.addNewStatus("Ritz Crackers", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Vons", 137);
        storeData.getStoreViewInStockMap().put("Vons", 137);
        storeData.getStoreViewNeededMap().put("Vons", 0);
        storeData.getStoreViewPausedMap().put("Vons", 0);
        dbStoreHelper.setStoreViews("Vons", 137, 137, 0, 0);

        //------------------------------------Rite Aid----------------------------------------------

        dbItemHelper.addNewItemByStore("Smarties", "na", "Candy", "Rite Aid", 0);
        //dbStatusHelper.addNewStatus("Smarties", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Rite Aid", 1);
        storeData.getStoreViewInStockMap().put("Rite Aid", 1);
        storeData.getStoreViewNeededMap().put("Rite Aid", 0);
        storeData.getStoreViewPausedMap().put("Rite Aid", 0);
        dbStoreHelper.setStoreViews("Rite Aid", 1, 1, 0, 0);

        //------------------------------------Smart & Final-----------------------------------------

        dbItemHelper.addNewItemByStore("Churros", "Tio Pepe’s or Hola!", "Desserts", "Smart & Final", 0);
        //dbStatusHelper.addNewStatus("Churros", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Fun Dip Sticks", "na", "Candy", "Smart & Final", 1);
        //dbStatusHelper.addNewStatus("Fun Dip Sticks", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Orange Tic Tacs", "na", "Candy", "Smart & Final", 2);
        //dbStatusHelper.addNewStatus("Orange Tic Tacs", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Smart & Final", 3);
        storeData.getStoreViewInStockMap().put("Smart & Final", 3);
        storeData.getStoreViewNeededMap().put("Smart & Final", 0);
        storeData.getStoreViewPausedMap().put("Smart & Final", 0);
        dbStoreHelper.setStoreViews("Smart & Final", 3, 3, 0, 0);

        //------------------------------------Costco------------------------------------------------

        dbItemHelper.addNewItemByStore("Soda Cans", "Pepsi or Coke", "Drinks", "Costco", 0);
        //dbStatusHelper.addNewStatus("Soda Cans", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Kitty Liter", "Scoop Away Complete", "Pet Supplies", "Costco", 1);
        //dbStatusHelper.addNewStatus("Kitty Liter", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Chicken Broth", "Kirkland Organic", "Pet Supplies", "Costco", 2);
        //dbStatusHelper.addNewStatus("Chicken Broth", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Frozen Vegetables", "Kirkland Organic", "Pet Supplies", "Costco", 3);
        //dbStatusHelper.addNewStatus("Frozen Vegetables", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Mashed Potatoes", "Main St. Bistro", "Pet Supplies", "Costco", 4);
        //dbStatusHelper.addNewStatus("Mashed Potatoes", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Fresh Pet", "Chicken Recipe (6lb)", "Pet Supplies", "Costco", 5);
        //dbStatusHelper.addNewStatus("Fresh Pet", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Oreo Muffins", "12 pack", "Desserts", "Costco", 6);
        //dbStatusHelper.addNewStatus("Oreo Muffins", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Costco", 7);
        storeData.getStoreViewInStockMap().put("Costco", 7);
        storeData.getStoreViewNeededMap().put("Costco", 0);
        storeData.getStoreViewPausedMap().put("Costco", 0);
        dbStoreHelper.setStoreViews("Costco", 7, 7, 0, 0);

        //------------------------------------Walmart-----------------------------------------------

        dbItemHelper.addNewItemByStore("Dark Chocolate Caramels", "Ghiradelli", "Candy", "Walmart", 0);
        //dbStatusHelper.addNewStatus("Dark Chocolate Caramels", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Dishwashing Brush", "Great Value", "Household", "Walmart", 1);
        //dbStatusHelper.addNewStatus("Dishwashing Brush", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Small Trash Bags", "13 gallon", "Household", "Walmart", 2);
        //dbStatusHelper.addNewStatus("Small Trash Bags", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Large Trash Bags", "33 gallon", "Household", "Walmart", 3);
        //dbStatusHelper.addNewStatus("Large Trash Bags", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Compactor Bags", "18 gallon", "Household", "Walmart", 4);
        //dbStatusHelper.addNewStatus("Compactor Bags", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Paper Towels", "Sparkle", "Household", "Walmart", 5);
        //dbStatusHelper.addNewStatus("Paper Towels", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Toilet Paper", "Angel Soft", "Household", "Walmart", 6);
        //dbStatusHelper.addNewStatus("Toilet Paper", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("72% Intense Dark Chocolate", "Ghiradelli", "Candy", "Walmart", 7);
        //dbStatusHelper.addNewStatus("72% Intense Dark Chocolate", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Oreo Pie Mix", "Jell-O No Bake", "Desserts", "Walmart", 8);
        //dbStatusHelper.addNewStatus("Oreo Pie Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Double Drizzle Popcorn", "to do", "Snacks", "Walmart", 9);
        //dbStatusHelper.addNewStatus("Double Drizzle Popcorn", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Walmart", 10);
        storeData.getStoreViewInStockMap().put("Walmart", 10);
        storeData.getStoreViewNeededMap().put("Walmart", 0);
        storeData.getStoreViewPausedMap().put("Walmart", 0);
        dbStoreHelper.setStoreViews("Walmart", 10, 10, 0, 0);

        //------------------------------------Amazon------------------------------------------------

        dbItemHelper.addNewItemByStore("Muddy Buddies", "Brownie Supreme", "Snacks", "Amazon", 0);
        //dbStatusHelper.addNewStatus("Muddy Buddies", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Gingerbread Cookie Mix", "Betty Crocker", "Desserts", "Amazon", 1);
        //dbStatusHelper.addNewStatus("Gingerbread Cookie Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Jelly Beans", "Sizzling Cinnamon", "Candy", "Amazon", 2);
        //dbStatusHelper.addNewStatus("Jelly Beans", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Orange Pez", "na", "Candy", "Amazon", 3);
        //dbStatusHelper.addNewStatus("Orange Pez", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vanilla Taffy", "na", "Candy", "Amazon", 4);
        //dbStatusHelper.addNewStatus("Vanilla Taffy", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vanilla Tootsie Rolls", "na", "Candy", "Amazon", 5);
        //dbStatusHelper.addNewStatus("Vanilla Tootsie Rolls", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Poop Bags", "Amazon Basics", "Pet Supplies", "Amazon", 6);
        //dbStatusHelper.addNewStatus("Poop Bags", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Nitrile Gloves", "GMG 100 pack", "Pet Supplies", "Amazon", 7);
        //dbStatusHelper.addNewStatus("Nitrile Gloves", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Toothpaste", "Tom's Antiplaque & Whitening", "Toiletries", "Amazon", 8);
        //dbStatusHelper.addNewStatus("Toothpaste", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Floss", "Reach Mint Waxed", "Toiletries", "Amazon", 9);
        //dbStatusHelper.addNewStatus("Floss", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Shaving Cream", "Sandalwood", "Toiletries", "Amazon", 10);
        //dbStatusHelper.addNewStatus("Shaving Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Shaving Razors", "Gillette ProGlide", "Toiletries", "Amazon", 11);
        //dbStatusHelper.addNewStatus("Shaving Razors", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sunscreen", "Hawaiian Tropic Sheer 50spf", "Toiletries", "Amazon", 12);
        //dbStatusHelper.addNewStatus("Sunscreen", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Pet Stain Cleaner", "Rocco & Roxie", "Household", "Amazon", 13);
        //dbStatusHelper.addNewStatus("Pet Stain Cleaner", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Night Light Bulbs", "C7 E12", "Household", "Amazon", 14);
        //dbStatusHelper.addNewStatus("Night Light Bulbs", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vitamin C", "Amazon Elements 1000 mg", "Supplements", "Amazon", 15);
        //dbStatusHelper.addNewStatus("Vitamin C", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Fortune Cookies", "to do", "Snacks", "Amazon", 16);
        //dbStatusHelper.addNewStatus("Fortune Cookies", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Communion Wafers", "to do", "Snacks", "Amazon", 17);
        //dbStatusHelper.addNewStatus("Communion Wafers", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sixlets", "na", "Candy", "Amazon", 18);
        //dbStatusHelper.addNewStatus("Sixlets", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Little Trees Air Fresheners", "True North", "Household", "Amazon", 19);
        //dbStatusHelper.addNewStatus("Little Trees Air Fresheners", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Amazon", 20);
        storeData.getStoreViewInStockMap().put("Amazon", 20);
        storeData.getStoreViewNeededMap().put("Amazon", 0);
        storeData.getStoreViewPausedMap().put("Amazon", 0);
        dbStoreHelper.setStoreViews("Amazon", 20, 20, 0, 0);

        //------------------------------------Stater Bros-------------------------------------------

        dbItemHelper.addNewItemByStore("Beef Noodles", "Yakisoba", "Soups", "Stater Bros", 0);
        //dbStatusHelper.addNewStatus("Beef Noodles", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Chip Cookie Mix", "Gluten Free", "Desserts", "Stater Bros", 1);
        //dbStatusHelper.addNewStatus("Choc. Chip Cookie Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Malt Mix", "Nestle", "Desserts", "Stater Bros", 2);
        //dbStatusHelper.addNewStatus("Choc. Malt Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Fudge Pudding", "Snack Pack", "Snacks", "Stater Bros", 3);
        //dbStatusHelper.addNewStatus("Choc. Fudge Pudding", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Dryer Sheets", "Simply Done Fresh Linen", "Household", "Stater Bros", 4);
        //dbStatusHelper.addNewStatus("Dryer Sheets", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Stater Bros", 5);
        storeData.getStoreViewInStockMap().put("Stater Bros", 5);
        storeData.getStoreViewNeededMap().put("Stater Bros", 0);
        storeData.getStoreViewPausedMap().put("Stater Bros", 0);
        dbStoreHelper.setStoreViews("Stater Bros", 5, 5, 0, 0);

        //------------------------------------CVS---------------------------------------------------

        dbItemHelper.addNewItemByStore("Dark Chocolate Pretzels", "Flipz", "Snacks", "CVS", 0);
        //dbStatusHelper.addNewStatus("Dark Chocolate Pretzels", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Candy Corn", "Brach's", "Candy", "CVS", 1);
        //dbStatusHelper.addNewStatus("Candy Corn", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Rubbing Alcohol", "Isopropyl", "Household", "CVS", 2);
        //dbStatusHelper.addNewStatus("Rubbing Alcohol", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hydrogen Peroxide", "na", "Household", "CVS", 3);
        //dbStatusHelper.addNewStatus("Hydrogen Peroxide", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Packaging Tape", "Scotch Heavy Duty", "Household", "CVS", 4);
        //dbStatusHelper.addNewStatus("Packaging Tape", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("CVS", 5);
        storeData.getStoreViewInStockMap().put("CVS", 5);
        storeData.getStoreViewNeededMap().put("CVS", 0);
        storeData.getStoreViewPausedMap().put("CVS", 0);
        dbStoreHelper.setStoreViews("CVS", 5, 5, 0, 0);

        //------------------------------------Dollar Tree-------------------------------------------

        dbItemHelper.addNewItemByStore("Sno Caps", "na", "Candy", "Dollar Tree", 0);
        //dbStatusHelper.addNewStatus("Sno Caps", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hand Soap", "Lavender & Chamomile", "Toiletries", "Dollar Tree", 1);
        //dbStatusHelper.addNewStatus("Hand Soap", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ramen Noodles", "Nissin", "Soups", "Dollar Tree", 2);
        //dbStatusHelper.addNewStatus("Ramen Noodles", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Dollar Tree", 3);
        storeData.getStoreViewInStockMap().put("Dollar Tree", 3);
        storeData.getStoreViewNeededMap().put("Dollar Tree", 0);
        storeData.getStoreViewPausedMap().put("Dollar Tree", 0);
        dbStoreHelper.setStoreViews("Dollar Tree", 3, 3, 0, 0);

        //------------------------------------Ralphs------------------------------------------------

        dbItemHelper.addNewItemByStore("Clarified Butter", "Challenge", "Eggs/Dairy", "Ralphs", 0);
        //dbStatusHelper.addNewStatus("Clarified Butter", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Ralphs", 1);
        storeData.getStoreViewInStockMap().put("Ralphs", 1);
        storeData.getStoreViewNeededMap().put("Ralphs", 0);
        storeData.getStoreViewPausedMap().put("Ralphs", 0);
        dbStoreHelper.setStoreViews("Ralphs", 1, 1, 0, 0);

        //------------------------------------Target------------------------------------------------

        dbItemHelper.addNewItemByStore("Chocolate Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 0);
        //dbStatusHelper.addNewStatus("Chocolate Syrup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Caramel Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 1);
        //dbStatusHelper.addNewStatus("Caramel Syrup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Dark Choc. M&M's", "na", "Candy", "Target", 2);
        //dbStatusHelper.addNewStatus("Dark Choc. M&M's", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Target", 3);
        storeData.getStoreViewInStockMap().put("Target", 3);
        storeData.getStoreViewNeededMap().put("Target", 0);
        storeData.getStoreViewPausedMap().put("Target", 0);
        dbStoreHelper.setStoreViews("Target", 3, 3, 0, 0);

        //------------------------------------Pet Supplies Plus-------------------------------------

        dbItemHelper.addNewItemByStore("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 0);
        //dbStatusHelper.addNewStatus("Cat Food (dry)", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Pet Supplies Plus", 1);
        storeData.getStoreViewInStockMap().put("Pet Supplies Plus", 1);
        storeData.getStoreViewNeededMap().put("Pet Supplies Plus", 0);
        storeData.getStoreViewPausedMap().put("Pet Supplies Plus", 0);
        dbStoreHelper.setStoreViews("Pet Supplies Plus", 1, 1, 0, 0);

        //------------------------------------Sprouts-------------------------------------------

        dbItemHelper.addNewItemByStore("Caramel Squares", "na", "Candy", "Sprouts", 0);
        //dbStatusHelper.addNewStatus("Caramel Squares", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Bar Soap", "Zum Bar Sea Salt", "Toiletries", "Sprouts", 1);
        //dbStatusHelper.addNewStatus("Bar Soap", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Toothbrush Heads", "Radius Soft", "Toiletries", "Sprouts", 2);
        //dbStatusHelper.addNewStatus("Toothbrush Heads", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Sprouts", 3);
        storeData.getStoreViewInStockMap().put("Sprouts", 3);
        storeData.getStoreViewNeededMap().put("Sprouts", 0);
        storeData.getStoreViewPausedMap().put("Sprouts", 0);
        dbStoreHelper.setStoreViews("Sprouts", 3, 3, 0, 0);

        //------------------------------------Sam's Club--------------------------------------------

        dbItemHelper.addNewItemByStore("Quick Steak", "Gary's", "Meat", "Sam's Club", 0);
        //dbStatusHelper.addNewStatus("Quick Steak", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Paper Plates", "to do", "Household", "Sam's Club", 1);
        //dbStatusHelper.addNewStatus("Paper Plates", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Sam's Club", 2);
        storeData.getStoreViewInStockMap().put("Sam's Club", 2);
        storeData.getStoreViewNeededMap().put("Sam's Club", 0);
        storeData.getStoreViewPausedMap().put("Sam's Club", 0);
        dbStoreHelper.setStoreViews("Sam's Club", 2, 2, 0, 0);

        //---------------------------------------Staples--------------------------------------------

        dbItemHelper.addNewItemByStore("Multipurpose Paper", "Tru Red 20/96", "Household", "Staples", 0);
        //dbStatusHelper.addNewStatus("Multipurpose Paper", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Staples", 1);
        storeData.getStoreViewInStockMap().put("Staples", 1);
        storeData.getStoreViewNeededMap().put("Staples", 0);
        storeData.getStoreViewPausedMap().put("Staples", 0);
        dbStoreHelper.setStoreViews("Staples", 1, 1, 0, 0);

        //---------------------------------------Woodranch--------------------------------------------

        dbItemHelper.addNewItemByStore("Woodranch BBQ Sauce", "1 pint", "Condiments", "Woodranch", 0);
        //dbStatusHelper.addNewStatus("Woodranch BBQ Sauce", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Woodranch", 1);
        storeData.getStoreViewInStockMap().put("Woodranch", 1);
        storeData.getStoreViewNeededMap().put("Woodranch", 0);
        storeData.getStoreViewPausedMap().put("Woodranch", 0);
        dbStoreHelper.setStoreViews("Woodranch", 1, 1, 0, 0);

        //------------------------------------Yorba Linda Feed Store--------------------------------

        dbItemHelper.addNewItemByStore("Dog Food (dry)", "Canidae All Life Stages", "Pet Supplies", "Yorba Linda Feed Store", 0);
        //dbStatusHelper.addNewStatus("Dog Food (dry)", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Yorba Linda Feed Store", 1);
        storeData.getStoreViewInStockMap().put("Yorba Linda Feed Store", 1);
        storeData.getStoreViewNeededMap().put("Yorba Linda Feed Store", 0);
        storeData.getStoreViewPausedMap().put("Yorba Linda Feed Store", 0);
        dbStoreHelper.setStoreViews("Yorba Linda Feed Store", 1, 1, 0, 0);

        //------------------------------------------------------------------------------------------

        // total store items = 204

    }

//------------------------------------------------------------------------------------------------//

    public void initializeData() {

        itemData = new ItemData();
        dbItemHelper.readItemDataByCategory(itemData);
        dbItemHelper.readItemDataByStore(itemData);
        statusData = dbStatusHelper.readStatusData();
        itemData.updateStatusesByCategory(statusData);
        categoryData = dbCategoryHelper.readCategoryData();
        storeData = dbStoreHelper.readStoreData();

        itemIsSelectedInInventory = false;
        itemIsSelectedInShoppingList = false;
        itemIsClickedInInventory = new HashMap<>();
        itemIsClickedInShoppingList = new HashMap<>();
        itemIsChecked = new HashMap<>();

        storeListOrderNum = 0;
        reorderItemsCategory = "";
        reorderItemsStore = "";
        editItemInInventory = false;
        editItemInShoppingList = false;

        inventoryView = INVENTORY_ALL;
        inventorySortBy = SORT_BY_CATEGORY;
    }
}