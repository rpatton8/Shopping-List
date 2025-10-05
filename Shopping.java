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
import java.util.ArrayList;

public class Shopping extends AppCompatActivity {

    private ItemData itemData;
    private StatusData statusData;
    private CategoryData categoryData;
    private StoreData storeData;
    public DBItemHelper dbItemHelper;
    public DBStatusHelper dbStatusHelper;
    public DBCategoryHelper dbCategoryHelper;
    public DBStoreHelper dbStoreHelper;

    public Boolean itemIsSelectedInInventory;
    public Boolean itemIsSelectedInShoppingList;
    public Item selectedItemInInventory;
    public Item selectedItemInShoppingList;
    public int selectedItemPositionInInventory;
    public int selectedItemPositionInShoppingList;
    public ArrayList<Boolean> itemIsClickedInInventory;
    public ArrayList<Boolean> itemIsClickedInShoppingList;
    public ArrayList<Boolean> itemIsChecked;
    public int storeNum;
    public String reorderItemsCategory;
    public Boolean editItemInInventory;
    public Boolean editItemInShoppingList;

    public String inventoryView;
    public final String INVENTORY_ALL = "view all";
    public final String INVENTORY_INSTOCK = "view instock";
    public final String INVENTORY_NEEDED = "view store";
    public final String INVENTORY_PAUSED = "view paused";

    public String inventorySortBy;
    public String SORT_BY_CATEGORY = "category";
    public String SORT_BY_STORE = "store";

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
        itemData = dbItemHelper.readItemData();
        statusData = dbStatusHelper.readStatusData();
        itemData.updateStatuses(statusData);
        categoryData = dbCategoryHelper.readCategoryData();
        storeData = dbStoreHelper.readStoreData();

        itemIsSelectedInInventory = false;
        itemIsSelectedInShoppingList = false;
        selectedItemInInventory = null;
        selectedItemInShoppingList = null;
        selectedItemPositionInInventory = 0;
        selectedItemPositionInShoppingList = 0;
        itemIsClickedInInventory = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i++) {
            itemIsClickedInInventory.add(false);
        }
        itemIsClickedInShoppingList = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i++) {
            itemIsClickedInShoppingList.add(false);
        }
        itemIsChecked = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i++) {
            itemIsChecked.add(false);
        }

        storeNum = 0;
        reorderItemsCategory = "";
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

        itemData = dbItemHelper.readItemData();
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

    public ArrayList<Boolean> getClickedInventoryList() {
        return itemIsClickedInInventory;
    }

    public ArrayList<Boolean> getClickedShoppingList() {
        return itemIsClickedInShoppingList;
    }

    public ArrayList<Boolean> getCheckedList() {
        return itemIsChecked;
    }

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
        itemData = dbItemHelper.readItemData();
        statusData = dbStatusHelper.readStatusData();
        itemData.updateStatuses(statusData);
        categoryData = dbCategoryHelper.readCategoryData();
        storeData = dbStoreHelper.readStoreData();
        itemIsSelectedInInventory = false;
        itemIsSelectedInShoppingList = false;
        itemIsClickedInInventory = new ArrayList<>();
        itemIsClickedInShoppingList = new ArrayList<>();
        itemIsChecked = new ArrayList<>();
        storeNum = 0;
        reorderItemsCategory = "";
        editItemInInventory = false;
        editItemInShoppingList = false;
        inventoryView = INVENTORY_ALL;
        inventorySortBy = SORT_BY_CATEGORY;
    }

//------------------------------------------------------------------------------------------------//
//-------------------------------------Sort By Category-------------------------------------------//
//------------------------------------------------------------------------------------------------//

    public void loadSampleData() {

        dbCategoryHelper.addNewCategory("Meals", 0);
        dbCategoryHelper.addNewCategory("Soups", 1);
        dbCategoryHelper.addNewCategory("Sides", 2);
        dbCategoryHelper.addNewCategory("Meat", 3);
        dbCategoryHelper.addNewCategory("Bread/Grains/Cereal", 4);
        dbCategoryHelper.addNewCategory("Eggs/Dairy", 5);
        dbCategoryHelper.addNewCategory("Condiments", 6);
        dbCategoryHelper.addNewCategory("Seasonings", 7);
        dbCategoryHelper.addNewCategory("Drinks", 8);
        dbCategoryHelper.addNewCategory("Snacks", 9);
        dbCategoryHelper.addNewCategory("Desserts", 10);
        dbCategoryHelper.addNewCategory("Candy", 11);
        dbCategoryHelper.addNewCategory("Pet Supplies", 12);
        dbCategoryHelper.addNewCategory("Toiletries", 13);
        dbCategoryHelper.addNewCategory("Household", 14);
        dbCategoryHelper.addNewCategory("Supplements", 15);

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
        dbStoreHelper.addNewStore("Yorba Linda Feed Store", 15);


        //------------------------------------Meals-------------------------------------------------

        dbItemHelper.addNewItem("Sausage Biscuits", "Jimmy Dean Frozen", "Meals", "Vons", 0);
        dbStatusHelper.addNewStatus("Sausage Biscuits", "true", "false", "false");

        dbItemHelper.addNewItem("Hamburger Helper", "Cheeseburger Macaroni", "Meals", "Vons", 1);
        dbStatusHelper.addNewStatus("Hamburger Helper", "false", "false", "true");

        dbItemHelper.addNewItem("Buffalo Chicken Bites", "TGIF or Frank's", "Meals", "Vons", 2);
        dbStatusHelper.addNewStatus("Buffalo Chicken Bites", "false", "false", "true");

        dbItemHelper.addNewItem("Terriyaki Chicken Bites", "InnovAsian", "Meals", "Vons", 3);
        dbStatusHelper.addNewStatus("Terriyaki Chicken Bites", "false", "false", "true");

        dbItemHelper.addNewItem("TGIF Cheese Sticks", "TGIF (small 10pc)", "Meals", "Vons", 4);
        dbStatusHelper.addNewStatus("TGIF Cheese Sticks", "false", "false", "true");

        dbItemHelper.addNewItem("Frozen Pizza", "Thin Pepperoni", "Meals", "Vons", 5);
        dbStatusHelper.addNewStatus("Frozen Pizza", "false", "true", "false");

        dbItemHelper.addNewItem("Corn Dogs", "Foster Farms", "Meals", "Vons", 6);
        dbStatusHelper.addNewStatus("Corn Dogs", "false", "true", "false");

        dbItemHelper.addNewItem("Hot Dogs", "Bun Size", "Meals", "Vons", 7);
        dbStatusHelper.addNewStatus("Hot Dogs", "false", "true", "false");

        dbItemHelper.addNewItem("Hot Dog Buns", "(8 pack)", "Meals", "Vons", 8);
        dbStatusHelper.addNewStatus("Hot Dog Buns", "false", "true", "false");

        dbItemHelper.addNewItem("Hamburger Patties", "to do", "Meals", "Vons", 9);
        dbStatusHelper.addNewStatus("Hamburger Patties", "false", "false", "true");

        dbItemHelper.addNewItem("Hamburger Buns", "(8 pack)", "Meals", "Vons", 10);
        dbStatusHelper.addNewStatus("Hamburger Buns", "false", "false", "true");

        dbItemHelper.addNewItem("Pasta Roni 1", "Angel Hair Pasta", "Meals", "Vons", 11);
        dbStatusHelper.addNewStatus("Pasta Roni 1", "true", "false", "false");

        dbItemHelper.addNewItem("Pasta Roni 2", "Fetuccini Alfredo", "Meals", "Vons", 12);
        dbStatusHelper.addNewStatus("Pasta Roni 2", "true", "false", "false");

        dbItemHelper.addNewItem("Mac & Cheese", "Annie’s", "Meals", "Vons", 13);
        dbStatusHelper.addNewStatus("Mac & Cheese", "true", "false", "false");

        dbItemHelper.addNewItem("Gnocci", "Signature Select", "Meals", "Vons", 14);
        dbStatusHelper.addNewStatus("Gnocci", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Meals", 15);
        categoryData.getCategoryViewInStockMap().put("Meals", 5);
        categoryData.getCategoryViewNeededMap().put("Meals", 4);
        categoryData.getCategoryViewPausedMap().put("Meals", 6);
        dbCategoryHelper.setCategoryViews("Meals", 15, 5, 4, 6);


        //------------------------------------Soups-------------------------------------------------

        dbItemHelper.addNewItem("Spaghetti O's", "w/ Meatballs", "Soups", "Vons", 0);
        dbStatusHelper.addNewStatus("Spaghetti O's", "true", "false", "false");

        dbItemHelper.addNewItem("Chicken Noodle Soup", "Campbell's", "Soups", "Vons", 1);
        dbStatusHelper.addNewStatus("Chicken Noodle Soup", "true", "false", "false");

        dbItemHelper.addNewItem("Minestrone Soup", "Amy's", "Soups", "Vons", 2);
        dbStatusHelper.addNewStatus("Minestrone Soup", "false", "true", "false");

        dbItemHelper.addNewItem("Vegetable Barley Soup", "Amy's", "Soups", "Vons", 3);
        dbStatusHelper.addNewStatus("Vegetable Barley Soup", "false", "true", "false");

        dbItemHelper.addNewItem("Beef Noodles", "Yakisoba", "Soups", "Stater Bros", 4);
        dbStatusHelper.addNewStatus("Beef Noodles", "false", "false", "true");

        dbItemHelper.addNewItem("Cup of Noodles", "Nissin", "Soups", "Vons", 5);
        dbStatusHelper.addNewStatus("Cup of Noodles", "false", "false", "true");

        dbItemHelper.addNewItem("Ramen Noodles", "Nissin", "Soups", "Dollar Tree", 6);
        dbStatusHelper.addNewStatus("Ramen Noodles", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Soups", 7);
        categoryData.getCategoryViewInStockMap().put("Soups", 3);
        categoryData.getCategoryViewNeededMap().put("Soups", 2);
        categoryData.getCategoryViewPausedMap().put("Soups", 2);
        dbCategoryHelper.setCategoryViews("Soups", 7, 3, 2, 2);


        //------------------------------------Sides-------------------------------------------------

        dbItemHelper.addNewItem("Fast Food Fries", "Ore-Ida", "Sides", "Vons", 0);
        dbStatusHelper.addNewStatus("Fast Food Fries", "false", "true", "false");

        dbItemHelper.addNewItem("Texas Cheesy Bread", "New York Bakery", "Sides", "Vons", 1);
        dbStatusHelper.addNewStatus("Texas Cheesy Bread", "true", "false", "false");

        dbItemHelper.addNewItem("Chicken Rice", "Knorr", "Sides", "Vons", 2);
        dbStatusHelper.addNewStatus("Chicken Rice", "true", "false", "false");

        dbItemHelper.addNewItem("Canned Corn", "Del Monte", "Sides", "Vons", 3);
        dbStatusHelper.addNewStatus("Canned Corn", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Sides", 4);
        categoryData.getCategoryViewInStockMap().put("Sides", 3);
        categoryData.getCategoryViewNeededMap().put("Sides", 1);
        categoryData.getCategoryViewPausedMap().put("Sides", 0);
        dbCategoryHelper.setCategoryViews("Sides", 4, 3, 1, 0);


        //------------------------------------Meat--------------------------------------------------

        dbItemHelper.addNewItem("Steak", "USDA", "Meat", "Vons", 0);
        dbStatusHelper.addNewStatus("Steak", "false", "false", "true");

        dbItemHelper.addNewItem("Ground Beef", "(1 pound)", "Meat", "Vons", 1);
        dbStatusHelper.addNewStatus("Ground Beef", "false", "false", "true");

        dbItemHelper.addNewItem("Meatballs", "Homestyle", "Meat", "Vons", 2);
        dbStatusHelper.addNewStatus("Meatballs", "false", "false", "true");

        dbItemHelper.addNewItem("Pepperoni", "Hormel", "Meat", "Vons", 3);
        dbStatusHelper.addNewStatus("Pepperoni", "true", "false", "false");

        dbItemHelper.addNewItem("Quick Steak", "Gary's", "Meat", "Sam's Club", 4);
        dbStatusHelper.addNewStatus("Quick Steak", "false", "true", "false");

        dbItemHelper.addNewItem("Chicken Breast", "na", "Meat", "Vons", 5);
        dbStatusHelper.addNewStatus("Chicken Breast", "false", "false", "true");

        dbItemHelper.addNewItem("Sliced Turkey", "to do", "Meat", "Vons", 6);
        dbStatusHelper.addNewStatus("Sliced Turkey", "false", "false", "true");

        dbItemHelper.addNewItem("Sliced Ham", "to do", "Meat", "Vons", 7);
        dbStatusHelper.addNewStatus("Sliced Ham", "false", "false", "true");

        dbItemHelper.addNewItem("Ham Steak", "to do", "Meat", "Vons", 8);
        dbStatusHelper.addNewStatus("Ham Steak", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Meat", 9);
        categoryData.getCategoryViewInStockMap().put("Meat", 2);
        categoryData.getCategoryViewNeededMap().put("Meat", 1);
        categoryData.getCategoryViewPausedMap().put("Meat", 6);
        dbCategoryHelper.setCategoryViews("Meat", 9, 2, 1, 6);


        //------------------------------------Bread/Grains/Cereal-----------------------------------

        dbItemHelper.addNewItem("Thin Spaghetti", "Barilla Whole Grain", "Bread/Grains/Cereal", "Vons", 0);
        dbStatusHelper.addNewStatus("Thin Spaghetti", "true", "false", "false");

        dbItemHelper.addNewItem("Spiral Pasta", "Barilla Rotini", "Bread/Grains/Cereal", "Vons", 1);
        dbStatusHelper.addNewStatus("Spiral Pasta", "true", "false", "false");

        dbItemHelper.addNewItem("Wheat Bread", "Nature's Own", "Bread/Grains/Cereal", "Vons", 2);
        dbStatusHelper.addNewStatus("Wheat Bread", "true", "false", "false");

        dbItemHelper.addNewItem("Baguette", "French", "Bread/Grains/Cereal", "Vons", 3);
        dbStatusHelper.addNewStatus("Baguette", "false", "true", "false");

        dbItemHelper.addNewItem("Sourdough Bread", "San Luis Sourdough", "Bread/Grains/Cereal", "Vons", 4);
        dbStatusHelper.addNewStatus("Sourdough Bread", "true", "false", "false");

        dbItemHelper.addNewItem("Thomas Muffins", "Original", "Bread/Grains/Cereal", "Vons", 5);
        dbStatusHelper.addNewStatus("Thomas Muffins", "false", "true", "false");

        dbItemHelper.addNewItem("RP Cereal", "Reese's Puffs", "Bread/Grains/Cereal", "Vons", 6);
        dbStatusHelper.addNewStatus("RP Cereal", "true", "false", "false");

        dbItemHelper.addNewItem("CC Cereal", "Cookie Crisp", "Bread/Grains/Cereal", "Vons", 7);
        dbStatusHelper.addNewStatus("CC Cereal", "true", "false", "false");

        dbItemHelper.addNewItem("FMW Cereal", "Frosted Mini Wheat", "Bread/Grains/Cereal", "Vons", 8);
        dbStatusHelper.addNewStatus("FMW Cereal", "true", "false", "false");

        dbItemHelper.addNewItem("Eggo Waffles", "Homestyle", "Bread/Grains/Cereal", "Vons", 9);
        dbStatusHelper.addNewStatus("Eggo Waffles", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Bread/Grains/Cereal", 10);
        categoryData.getCategoryViewInStockMap().put("Bread/Grains/Cereal", 8);
        categoryData.getCategoryViewNeededMap().put("Bread/Grains/Cereal", 2);
        categoryData.getCategoryViewPausedMap().put("Bread/Grains/Cereal", 0);
        dbCategoryHelper.setCategoryViews("Bread/Grains/Cereal", 10, 8, 2, 0);


        //----------------------------------------Eggs/Dairy----------------------------------------

        dbItemHelper.addNewItem("Milk", "Vitamin D", "Eggs/Dairy", "Vons", 0);
        dbStatusHelper.addNewStatus("Milk", "false", "true", "false");

        dbItemHelper.addNewItem("Eggs", "Grade AA", "Eggs/Dairy", "Vons", 1);
        dbStatusHelper.addNewStatus("Eggs", "false", "true", "false");

        dbItemHelper.addNewItem("Honey Yogurt", "Greek Gods", "Eggs/Dairy", "Vons", 2);
        dbStatusHelper.addNewStatus("Honey Yogurt", "false", "false", "true");

        dbItemHelper.addNewItem("Salted Butter", "Challenge", "Eggs/Dairy", "Vons", 3);
        dbStatusHelper.addNewStatus("Salted Butter", "true", "false", "false");

        dbItemHelper.addNewItem("Clarified Butter", "Challenge", "Eggs/Dairy", "Ralphs", 4);
        dbStatusHelper.addNewStatus("Clarified Butter", "false", "false", "true");

        dbItemHelper.addNewItem("Shredded Cheese", "Mexican Blend", "Eggs/Dairy", "Vons", 5);
        dbStatusHelper.addNewStatus("Shredded Cheese", "true", "false", "false");

        dbItemHelper.addNewItem("String Cheese", "Mozarella", "Eggs/Dairy", "Vons", 6);
        dbStatusHelper.addNewStatus("String Cheese", "false", "true", "false");

        dbItemHelper.addNewItem("BD Cheese", "Black Diamond", "Eggs/Dairy", "Vons", 7);
        dbStatusHelper.addNewStatus("BD Cheese", "false", "false", "true");

        dbItemHelper.addNewItem("Non-Stick Spray", "Pam Original", "Eggs/Dairy", "Vons", 8);
        dbStatusHelper.addNewStatus("Non-Stick Spray", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Eggs/Dairy", 9);
        categoryData.getCategoryViewInStockMap().put("Eggs/Dairy", 3);
        categoryData.getCategoryViewNeededMap().put("Eggs/Dairy", 3);
        categoryData.getCategoryViewPausedMap().put("Eggs/Dairy", 3);
        dbCategoryHelper.setCategoryViews("Eggs/Dairy", 9, 3, 3, 3);


        //------------------------------------Condiments--------------------------------------------

        dbItemHelper.addNewItem("Parmesan Cheese", "Kraft", "Condiments", "Vons", 0);
        dbStatusHelper.addNewStatus("Parmesan Cheese", "true", "false", "false");

        dbItemHelper.addNewItem("A1 Sauce", "Original", "Condiments", "Vons", 1);
        dbStatusHelper.addNewStatus("A1 Sauce", "true", "false", "false");

        dbItemHelper.addNewItem("Ketchup", "Heinz", "Condiments", "Vons", 2);
        dbStatusHelper.addNewStatus("Ketchup", "true", "false", "false");

        dbItemHelper.addNewItem("Mustard", "Heinz", "Condiments", "Vons", 3);
        dbStatusHelper.addNewStatus("Mustard", "true", "false", "false");

        dbItemHelper.addNewItem("Pasta Sauce", "Ragu Meat", "Condiments", "Vons", 4);
        dbStatusHelper.addNewStatus("Pasta Sauce", "true", "false", "false");

        dbItemHelper.addNewItem("Chocolate Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 5);
        dbStatusHelper.addNewStatus("Chocolate Syrup", "false", "true", "false");

        dbItemHelper.addNewItem("Caramel Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 6);
        dbStatusHelper.addNewStatus("Caramel Syrup", "false", "true", "false");

        dbItemHelper.addNewItem("Maple Syrup", "Pearl Milling", "Condiments", "Vons", 7);
        dbStatusHelper.addNewStatus("Maple Syrup", "true", "false", "false");

        dbItemHelper.addNewItem("Honey", "Local Hive Clover", "Condiments", "Vons", 8);
        dbStatusHelper.addNewStatus("Honey", "true", "false", "false");

        dbItemHelper.addNewItem("Peanut Butter", "Skippy Creamy", "Condiments", "Vons", 9);
        dbStatusHelper.addNewStatus("Peanut Butter", "true", "false", "false");

        dbItemHelper.addNewItem("Soy Sauce", "Kikoman", "Condiments", "Vons", 10);
        dbStatusHelper.addNewStatus("Soy Sauce", "true", "false", "false");

        dbItemHelper.addNewItem("Brown Sugar", "(for hot chocolate)", "Condiments", "Vons", 11);
        dbStatusHelper.addNewStatus("Brown Sugar", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Condiments", 12);
        categoryData.getCategoryViewInStockMap().put("Condiments", 10);
        categoryData.getCategoryViewNeededMap().put("Condiments", 2);
        categoryData.getCategoryViewPausedMap().put("Condiments", 0);
        dbCategoryHelper.setCategoryViews("Condiments", 12, 10, 2, 0);


        //------------------------------------Seasonings--------------------------------------------

        dbItemHelper.addNewItem("Salt & Pepeper", "na", "Seasonings", "Vons", 0);
        dbStatusHelper.addNewStatus("Salt & Pepeper", "true", "false", "false");

        dbItemHelper.addNewItem("Garlic Salt", "Lawry's", "Seasonings", "Vons", 1);
        dbStatusHelper.addNewStatus("Garlic Salt", "true", "false", "false");

        dbItemHelper.addNewItem("Lawry's Seasoning Salt", "Lawry's", "Seasonings", "Vons", 2);
        dbStatusHelper.addNewStatus("Lawry's Seasoning Salt", "true", "false", "false");

        dbItemHelper.addNewItem("Ranch Dip Mix", "Laura Scudder's", "Seasonings", "Vons", 3);
        dbStatusHelper.addNewStatus("Ranch Dip Mix", "false", "false", "true");

        dbItemHelper.addNewItem("Vanilla Extract", "Signature Select", "Seasonings", "Vons", 4);
        dbStatusHelper.addNewStatus("Vanilla Extract", "true", "false", "false");

        dbItemHelper.addNewItem("Cinnamon Sugar", "McCormick's", "Seasonings", "Vons", 5);
        dbStatusHelper.addNewStatus("Cinnamon Sugar", "false", "false", "true");

        dbItemHelper.addNewItem("Sprinkles", "3 types", "Seasonings", "Vons", 6);
        dbStatusHelper.addNewStatus("Sprinkles", "false", "false", "true");


        categoryData.getCategoryViewAllMap().put("Seasonings", 7);
        categoryData.getCategoryViewInStockMap().put("Seasonings", 4);
        categoryData.getCategoryViewNeededMap().put("Seasonings", 0);
        categoryData.getCategoryViewPausedMap().put("Seasonings", 3);
        dbCategoryHelper.setCategoryViews("Seasonings", 7, 4, 0, 3);


        //------------------------------------Drinks------------------------------------------------

        dbItemHelper.addNewItem("Soda Bottles", "Pepsi or Coke", "Drinks", "Vons", 0);
        dbStatusHelper.addNewStatus("Soda Bottles", "true", "false", "false");

        dbItemHelper.addNewItem("Soda Cans", "Pepsi or Coke", "Drinks", "Costco", 1);
        dbStatusHelper.addNewStatus("Soda Cans", "true", "false", "false");

        dbItemHelper.addNewItem("Hot Chocolate Mix", "Swiss Miss Dark", "Drinks", "Vons", 2);
        dbStatusHelper.addNewStatus("Hot Chocolate Mix", "true", "false", "false");

        dbItemHelper.addNewItem("Bottled Water", "Aquafina", "Drinks", "Vons", 3);
        dbStatusHelper.addNewStatus("Bottled Water", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Drinks", 4);
        categoryData.getCategoryViewInStockMap().put("Drinks", 4);
        categoryData.getCategoryViewNeededMap().put("Drinks", 0);
        categoryData.getCategoryViewPausedMap().put("Drinks", 0);
        dbCategoryHelper.setCategoryViews("Drinks", 4, 4, 0, 0);


        //------------------------------------Snacks------------------------------------------------

        dbItemHelper.addNewItem("Beef Jerky", "Archer Terriyaki", "Snacks", "Vons", 0);
        dbStatusHelper.addNewStatus("Beef Jerky", "false", "false", "true");

        dbItemHelper.addNewItem("Peanuts", "Honey Roasted", "Snacks", "Vons", 1);
        dbStatusHelper.addNewStatus("Peanuts", "false", "true", "false");

        dbItemHelper.addNewItem("Shell Peanuts", "Salted", "Snacks", "Vons", 2);
        dbStatusHelper.addNewStatus("Shell Peanuts", "false", "true", "false");

        dbItemHelper.addNewItem("Sunflower Seeds", "Salted", "Snacks", "Vons", 3);
        dbStatusHelper.addNewStatus("Sunflower Seeds", "true", "false", "false");

        dbItemHelper.addNewItem("Vinegar Chips", "Kettle", "Snacks", "Vons", 4);
        dbStatusHelper.addNewStatus("Vinegar Chips", "true", "false", "false");

        dbItemHelper.addNewItem("BBQ Chips", "Kettle", "Snacks", "Vons", 5);
        dbStatusHelper.addNewStatus("BBQ Chips", "false", "false", "true");

        dbItemHelper.addNewItem("Doritos", "Cool Ranch", "Snacks", "Vons", 6);
        dbStatusHelper.addNewStatus("Doritos", "false", "false", "true");

        dbItemHelper.addNewItem("Lay's Chips", "Classic", "Snacks", "Vons", 7);
        dbStatusHelper.addNewStatus("Lay's Chips", "true", "false", "false");

        dbItemHelper.addNewItem("Naan Crisps", "Stonefire", "Snacks", "Vons", 8);
        dbStatusHelper.addNewStatus("Naan Crisps", "true", "false", "false");

        dbItemHelper.addNewItem("Oreo Cakesters", "Nabisco", "Snacks", "Vons", 9);
        dbStatusHelper.addNewStatus("Oreo Cakesters", "false", "false", "true");

        dbItemHelper.addNewItem("Goldfish", "Cheddar", "Snacks", "Vons", 10);
        dbStatusHelper.addNewStatus("Goldfish", "false", "true", "false");

        dbItemHelper.addNewItem("Cheez-Its", "Original", "Snacks", "Vons", 11);
        dbStatusHelper.addNewStatus("Cheez-Its", "false", "true", "false");

        dbItemHelper.addNewItem("Famous Amos Cookies", "12 Pack", "Snacks", "Vons", 12);
        dbStatusHelper.addNewStatus("Famous Amos Cookies", "false", "true", "false");

        dbItemHelper.addNewItem("Dark Chocolate Pretzels", "Flipz", "Snacks", "CVS", 13);
        dbStatusHelper.addNewStatus("Dark Chocolate Pretzels", "true", "false", "false");

        dbItemHelper.addNewItem("Choc. Fudge Pudding", "Snack Pack", "Snacks", "Stater Bros", 14);
        dbStatusHelper.addNewStatus("Choc. Fudge Pudding", "false", "false", "true");

        dbItemHelper.addNewItem("Choc. Fudge Pirouette", "Pepperidge Farm", "Snacks", "Vons", 15);
        dbStatusHelper.addNewStatus("Choc. Fudge Pirouette", "false", "false", "true");

        dbItemHelper.addNewItem("Muddy Buddies", "Brownie Supreme", "Snacks", "Amazon", 16);
        dbStatusHelper.addNewStatus("Muddy Buddies", "true", "false", "false");

        dbItemHelper.addNewItem("Fortune Cookies", "to do", "Snacks", "Amazon", 17);
        dbStatusHelper.addNewStatus("Fortune Cookies", "true", "false", "false");

        dbItemHelper.addNewItem("Communion Wafers", "to do", "Snacks", "Amazon", 18);
        dbStatusHelper.addNewStatus("Communion Wafers", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Snacks", 19);
        categoryData.getCategoryViewInStockMap().put("Snacks", 8);
        categoryData.getCategoryViewNeededMap().put("Snacks", 5);
        categoryData.getCategoryViewPausedMap().put("Snacks", 6);
        dbCategoryHelper.setCategoryViews("Snacks", 19, 8, 5, 6);


        //------------------------------------Desserts----------------------------------------------

        dbItemHelper.addNewItem("Choc. Malted Crunch Ice Cream", "Thrifty", "Desserts", "Vons", 0);
        dbStatusHelper.addNewStatus("Choc. Malted Crunch Ice Cream", "false", "true", "false");

        dbItemHelper.addNewItem("Churros", "Tio Pepe’s or Hola!", "Desserts", "Smart & Final", 1);
        dbStatusHelper.addNewStatus("Churros", "false", "false", "true");

        dbItemHelper.addNewItem("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 2);
        dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "false", "false", "true");

        dbItemHelper.addNewItem("Choc. Chip Cookie Mix", "Gluten Free", "Desserts", "Stater Bros", 3);
        dbStatusHelper.addNewStatus("Choc. Chip Cookie Mix", "false", "false", "true");

        dbItemHelper.addNewItem("Gingerbread Cookie Mix", "Betty Crocker", "Desserts", "Amazon", 4);
        dbStatusHelper.addNewStatus("Gingerbread Cookie Mix", "false", "false", "true");

        dbItemHelper.addNewItem("Oreos", "for crumbs", "Desserts", "Vons", 5);
        dbStatusHelper.addNewStatus("Oreos", "true", "false", "false");

        dbItemHelper.addNewItem("Choc. Malt Mix", "Nestle", "Desserts", "Stater Bros", 6);
        dbStatusHelper.addNewStatus("Choc. Malt Mix", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Desserts", 7);
        categoryData.getCategoryViewInStockMap().put("Desserts", 2);
        categoryData.getCategoryViewNeededMap().put("Desserts", 1);
        categoryData.getCategoryViewPausedMap().put("Desserts", 4);
        dbCategoryHelper.setCategoryViews("Desserts", 7, 2, 1, 4);


        //------------------------------------Candy-------------------------------------------------

        dbItemHelper.addNewItem("Dark Chocolate Caramels", "Ghiradelli", "Candy", "Walmart", 0);
        dbStatusHelper.addNewStatus("Dark Chocolate Caramels", "true", "false", "false");

        dbItemHelper.addNewItem("Reese's PB Cups", "(individually wrapped)", "Candy", "Vons", 1);
        dbStatusHelper.addNewStatus("Reese's PB Cups", "true", "false", "false");

        dbItemHelper.addNewItem("Candy Corn", "Brach's", "Candy", "CVS", 2);
        dbStatusHelper.addNewStatus("Candy Corn", "true", "false", "false");

        dbItemHelper.addNewItem("Hot Tamales", "na", "Candy", "Vons", 3);
        dbStatusHelper.addNewStatus("Hot Tamales", "true", "false", "false");

        dbItemHelper.addNewItem("Smarties", "na", "Candy", "Rite Aid", 4);
        dbStatusHelper.addNewStatus("Smarties", "true", "false", "false");

        dbItemHelper.addNewItem("Sno Caps", "na", "Candy", "Dollar Tree", 5);
        dbStatusHelper.addNewStatus("Sno Caps", "true", "false", "false");

        dbItemHelper.addNewItem("Mini M&M's", "na", "Candy", "Vons", 6);
        dbStatusHelper.addNewStatus("Mini M&M's", "false", "true", "false");

        dbItemHelper.addNewItem("Caramel Squares", "na", "Candy", "Sprouts", 7);
        dbStatusHelper.addNewStatus("Caramel Squares", "false", "false", "true");

        dbItemHelper.addNewItem("Jelly Beans", "Sizzling Cinnamon", "Candy", "Amazon", 8);
        dbStatusHelper.addNewStatus("Jelly Beans", "false", "false", "true");

        dbItemHelper.addNewItem("Tootsie Rolls", "na", "Candy", "Vons", 9);
        dbStatusHelper.addNewStatus("Tootsie Rolls", "true", "false", "false");

        dbItemHelper.addNewItem("Fun Dip Sticks", "na", "Candy", "Smart & Final", 10);
        dbStatusHelper.addNewStatus("Fun Dip Sticks", "false", "false", "true");

        dbItemHelper.addNewItem("72% Intense Dark Chocolate", "Ghiradelli", "Candy", "Walmart", 11);
        dbStatusHelper.addNewStatus("72% Intense Dark Chocolate", "true", "false", "false");

        dbItemHelper.addNewItem("Orange Tic Tacs", "na", "Candy", "Smart & Final", 12);
        dbStatusHelper.addNewStatus("Orange Tic Tacs", "false", "true", "false");

        dbItemHelper.addNewItem("Orange Pez", "na", "Candy", "Amazon", 13);
        dbStatusHelper.addNewStatus("Orange Pez", "false", "true", "false");

        dbItemHelper.addNewItem("Vanilla Taffy", "na", "Candy", "Amazon", 14);
        dbStatusHelper.addNewStatus("Vanilla Taffy", "false", "true", "false");

        dbItemHelper.addNewItem("Vanilla Tootsie Rolls", "na", "Candy", "Amazon", 15);
        dbStatusHelper.addNewStatus("Vanilla Tootsie Rolls", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Candy", 16);
        categoryData.getCategoryViewInStockMap().put("Candy", 9);
        categoryData.getCategoryViewNeededMap().put("Candy", 4);
        categoryData.getCategoryViewPausedMap().put("Candy", 3);
        dbCategoryHelper.setCategoryViews("Candy", 16, 9, 4, 3);


        //------------------------------------Pet Supplies-------------------------------------------

        dbItemHelper.addNewItem("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 0);
        dbStatusHelper.addNewStatus("Cat Food (wet)", "true", "false", "false");

        dbItemHelper.addNewItem("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 1);
        dbStatusHelper.addNewStatus("Cat Food (dry)", "true", "false", "false");

        dbItemHelper.addNewItem("Delectables", "Squeeze Up 20 pack", "Pet Supplies", "Vons", 2);
        dbStatusHelper.addNewStatus("Delectables", "true", "false", "false");

        dbItemHelper.addNewItem("Kitty Liter", "Scoop Away Complete", "Pet Supplies", "Costco", 3);
        dbStatusHelper.addNewStatus("Kitty Liter", "false", "true", "false");

        dbItemHelper.addNewItem("Dog Food (dry)", "Canidae All Life Stages", "Pet Supplies", "Yorba Linda Feed Store", 4);
        dbStatusHelper.addNewStatus("Dog Food (dry)", "true", "false", "false");

        dbItemHelper.addNewItem("Chicken Broth", "Kirkland Organic", "Pet Supplies", "Costco", 5);
        dbStatusHelper.addNewStatus("Chicken Broth", "true", "false", "false");

        dbItemHelper.addNewItem("Frozen Vegetables", "Kirkland Organic", "Pet Supplies", "Costco", 6);
        dbStatusHelper.addNewStatus("Frozen Vegetables", "true", "false", "false");

        dbItemHelper.addNewItem("Mashed Potatoes", "Main St. Bistro", "Pet Supplies", "Costco", 7);
        dbStatusHelper.addNewStatus("Mashed Potatoes", "true", "false", "false");

        dbItemHelper.addNewItem("100% Pure Pumpkin", "Libby's", "Pet Supplies", "Vons", 8);
        dbStatusHelper.addNewStatus("100% Pure Pumpkin", "true", "false", "false");

        dbItemHelper.addNewItem("Poop Bags", "Amazon Basics", "Pet Supplies", "Amazon", 9);
        dbStatusHelper.addNewStatus("Poop Bags", "true", "false", "false");

        dbItemHelper.addNewItem("Nitrile Gloves", "GMG 100 pack", "Pet Supplies", "Amazon", 10);
        dbStatusHelper.addNewStatus("Nitrile Gloves", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Pet Supplies", 11);
        categoryData.getCategoryViewInStockMap().put("Pet Supplies", 10);
        categoryData.getCategoryViewNeededMap().put("Pet Supplies", 1);
        categoryData.getCategoryViewPausedMap().put("Pet Supplies", 0);
        dbCategoryHelper.setCategoryViews("Pet Supplies", 11, 10, 1, 0);


        //------------------------------------Toiletries--------------------------------------------

        dbItemHelper.addNewItem("Hand Soap", "Lavender & Chamomile", "Toiletries", "Dollar Tree", 0);
        dbStatusHelper.addNewStatus("Hand Soap", "true", "false", "false");

        dbItemHelper.addNewItem("Body Wash", "Suave Mandarin", "Toiletries", "Vons", 1);
        dbStatusHelper.addNewStatus("Body Wash", "true", "false", "false");

        dbItemHelper.addNewItem("Shampoo", "Suave 2 in 1", "Toiletries", "Vons", 2);
        dbStatusHelper.addNewStatus("Shampoo", "true", "false", "false");

        dbItemHelper.addNewItem("Bar Soap", "Zum Bar Sea Salt", "Toiletries", "Sprouts", 3);
        dbStatusHelper.addNewStatus("Bar Soap", "true", "false", "false");

        dbItemHelper.addNewItem("Deodorant", "Old Spice", "Toiletries", "Vons", 4);
        dbStatusHelper.addNewStatus("Deodorant", "true", "false", "false");

        dbItemHelper.addNewItem("Toothpaste", "Tom's Antiplaque & Whitening", "Toiletries", "Amazon", 5);
        dbStatusHelper.addNewStatus("Toothpaste", "true", "false", "false");

        dbItemHelper.addNewItem("Floss", "Reach Mint Waxed", "Toiletries", "Amazon", 6);
        dbStatusHelper.addNewStatus("Floss", "true", "false", "false");

        dbItemHelper.addNewItem("Shaving Cream", "Sandalwood", "Toiletries", "Amazon", 7);
        dbStatusHelper.addNewStatus("Shaving Cream", "true", "false", "false");

        dbItemHelper.addNewItem("Shaving Razors", "Gillette ProGlide", "Toiletries", "Amazon", 8);
        dbStatusHelper.addNewStatus("Shaving Razors", "true", "false", "false");

        dbItemHelper.addNewItem("Mouthwash", "Crest Whitening", "Toiletries", "Vons", 9);
        dbStatusHelper.addNewStatus("Mouthwash", "true", "false", "false");

        dbItemHelper.addNewItem("Cotton Swabs", "Q-Tips", "Toiletries", "Vons", 10);
        dbStatusHelper.addNewStatus("Cotton Swabs", "true", "false", "false");

        dbItemHelper.addNewItem("Sunscreen", "Hawaiian Tropic Sheer 50spf", "Toiletries", "Amazon", 11);
        dbStatusHelper.addNewStatus("Sunscreen", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Toiletries", 12);
        categoryData.getCategoryViewInStockMap().put("Toiletries", 12);
        categoryData.getCategoryViewNeededMap().put("Toiletries", 0);
        categoryData.getCategoryViewPausedMap().put("Toiletries", 0);
        dbCategoryHelper.setCategoryViews("Toiletries", 12, 12, 0, 0);


        //------------------------------------Household-------------------------------------------------

        dbItemHelper.addNewItem("Febreeze Air Spray", "Heavy Duty", "Household", "Vons", 0);
        dbStatusHelper.addNewStatus("Febreeze Air Spray", "true", "false", "false");

        dbItemHelper.addNewItem("All Purpose Cleaner", "Meyer's Lavender", "Household", "Vons", 1);
        dbStatusHelper.addNewStatus("All Purpose Cleaner", "true", "false", "false");

        dbItemHelper.addNewItem("Pet Stain Cleaner", "Rocco & Roxie", "Household", "Amazon", 2);
        dbStatusHelper.addNewStatus("Pet Stain Cleaner", "true", "false", "false");

        dbItemHelper.addNewItem("Laundry Detergent", "Woolite", "Household", "Vons", 3);
        dbStatusHelper.addNewStatus("Laundry Detergent", "true", "false", "false");

        dbItemHelper.addNewItem("Laundry Sanitizer", "Lysol", "Household", "Vons", 4);
        dbStatusHelper.addNewStatus("Laundry Sanitizer", "true", "false", "false");

        dbItemHelper.addNewItem("Dryer Sheets", "Simply Done Fresh Linen", "Household", "Stater Bros", 5);
        dbStatusHelper.addNewStatus("Dryer Sheets", "true", "false", "false");

        dbItemHelper.addNewItem("Aluminum Foil", "Reynolds Wrap", "Household", "Vons", 6);
        dbStatusHelper.addNewStatus("Aluminum Foil", "true", "false", "false");

        dbItemHelper.addNewItem("Zip-Lock Bags (small)", "Sandwich", "Household", "Vons", 7);
        dbStatusHelper.addNewStatus("Zip-Lock Bags (small)", "true", "false", "false");

        dbItemHelper.addNewItem("Zip-Lock Bags (large)", "Freezer Gallon", "Household", "Vons", 8);
        dbStatusHelper.addNewStatus("Zip-Lock Bags (large)", "true", "false", "false");

        dbItemHelper.addNewItem("Saran Wrap", "Plastic Wrap", "Household", "Vons", 9);
        dbStatusHelper.addNewStatus("Saran Wrap", "true", "false", "false");

        dbItemHelper.addNewItem("Rubbing Alcohol", "Isopropyl", "Household", "CVS", 10);
        dbStatusHelper.addNewStatus("Rubbing Alcohol", "true", "false", "false");

        dbItemHelper.addNewItem("Hydrogen Peroxide", "na", "Household", "CVS", 11);
        dbStatusHelper.addNewStatus("Hydrogen Peroxide", "true", "false", "false");

        dbItemHelper.addNewItem("Night Light Bulbs", "C7 E12", "Household", "Amazon", 12);
        dbStatusHelper.addNewStatus("Night Light Bulbs", "true", "false", "false");

        dbItemHelper.addNewItem("Scrub Sponges", "Non-Scratch", "Household", "Vons", 13);
        dbStatusHelper.addNewStatus("Scrub Sponges", "true", "false", "false");

        dbItemHelper.addNewItem("Dishwashing Brush", "Great Value", "Household", "Walmart", 14);
        dbStatusHelper.addNewStatus("Dishwashing Brush", "true", "false", "false");

        dbItemHelper.addNewItem("Small Trash Bags", "13 gallon", "Household", "Walmart", 15);
        dbStatusHelper.addNewStatus("Small Trash Bags", "true", "false", "false");

        dbItemHelper.addNewItem("Large Trash Bags", "33 gallon", "Household", "Walmart", 16);
        dbStatusHelper.addNewStatus("Large Trash Bags", "true", "false", "false");

        dbItemHelper.addNewItem("Compactor Bags", "18 gallon", "Household", "Walmart", 17);
        dbStatusHelper.addNewStatus("Compactor Bags", "true", "false", "false");

        dbItemHelper.addNewItem("Dawn Powerwash", "Dish Cleaner", "Household", "Vons", 18);
        dbStatusHelper.addNewStatus("Dawn Powerwash", "true", "false", "false");

        dbItemHelper.addNewItem("Dish Soap", "Dawn Platinum", "Household", "Vons", 19);
        dbStatusHelper.addNewStatus("Dish Soap", "true", "false", "false");

        dbItemHelper.addNewItem("Paper Plates", "to do", "Household", "Sam's Club", 20);
        dbStatusHelper.addNewStatus("Paper Plates", "true", "false", "false");

        dbItemHelper.addNewItem("Paper Towels", "Sparkle", "Household", "Walmart", 21);
        dbStatusHelper.addNewStatus("Paper Towels", "true", "false", "false");

        dbItemHelper.addNewItem("Toilet Paper", "Angel Soft", "Household", "Walmart", 22);
        dbStatusHelper.addNewStatus("Toilet Paper", "true", "false", "false");

        dbItemHelper.addNewItem("Multipurpose Paper", "Tru Red 20/96", "Household", "Staples", 23);
        dbStatusHelper.addNewStatus("Multipurpose Paper", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Household", 24);
        categoryData.getCategoryViewInStockMap().put("Household", 24);
        categoryData.getCategoryViewNeededMap().put("Household", 0);
        categoryData.getCategoryViewPausedMap().put("Household", 0);
        dbCategoryHelper.setCategoryViews("Household", 24, 24, 0, 0);


        //------------------------------------Supplements-------------------------------------------------

        dbItemHelper.addNewItem("Triple Omega", "Nature Made", "Supplements", "Vons", 0);
        dbStatusHelper.addNewStatus("Triple Omega", "true", "false", "false");

        dbItemHelper.addNewItem("Multivitamin", "One a Day Men's", "Supplements", "Vons", 1);
        dbStatusHelper.addNewStatus("Multivitamin", "true", "false", "false");

        dbItemHelper.addNewItem("Vitamin C", "Amazon Elements 1000 mg", "Supplements", "Amazon", 2);
        dbStatusHelper.addNewStatus("Vitamin C", "true", "false", "false");

        dbItemHelper.addNewItem("Magnesium", "Nature Made 400mg", "Supplements", "Vons", 3);
        dbStatusHelper.addNewStatus("Magnesium", "true", "false", "false");

        dbItemHelper.addNewItem("Zinc", "Sandhu Herbals 50mg", "Supplements", "Vons", 4);
        dbStatusHelper.addNewStatus("Zinc", "true", "false", "false");

        dbItemHelper.addNewItem("Calcium", "Nature's Truth 1200 mg", "Supplements", "Vons", 5);
        dbStatusHelper.addNewStatus("Calcium", "true", "false", "false");

        dbItemHelper.addNewItem("Biotin", "Natrol Biotin 10,000mcg", "Supplements", "Vons", 6);
        dbStatusHelper.addNewStatus("Biotin", "true", "false", "false");

        dbItemHelper.addNewItem("Vitamin D3", "Nature Made 5000 IU", "Supplements", "Vons", 7);
        dbStatusHelper.addNewStatus("Vitamin D3", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Supplements", 8);
        categoryData.getCategoryViewInStockMap().put("Supplements", 8);
        categoryData.getCategoryViewNeededMap().put("Supplements", 0);
        categoryData.getCategoryViewPausedMap().put("Supplements", 0);
        dbCategoryHelper.setCategoryViews("Supplements", 8, 8, 0, 0);

        //------------------------------------------------------------------------------------------

            // total category items = 174

//------------------------------------------------------------------------------------------------//
//----------------------------------------Sort By Store-------------------------------------------//
//------------------------------------------------------------------------------------------------//

        //------------------------------------Vons--------------------------------------------------

        //dbItemHelper.addNewItem("Sausage Biscuits", "Jimmy Dean Frozen", "Meals", "Vons", 0);
        //dbStatusHelper.addNewStatus("Sausage Biscuits", "true", "false", "false");

        //dbItemHelper.addNewItem("Hamburger Helper", "Cheeseburger Macaroni", "Meals", "Vons", 1);
        //dbStatusHelper.addNewStatus("Hamburger Helper", "false", "false", "true");

        //dbItemHelper.addNewItem("Buffalo Chicken Bites", "TGIF or Frank's", "Meals", "Vons", 2);
        //dbStatusHelper.addNewStatus("Buffalo Chicken Bites", "false", "false", "true");

        //dbItemHelper.addNewItem("Terriyaki Chicken Bites", "InnovAsian", "Meals", "Vons", 3);
        //dbStatusHelper.addNewStatus("Terriyaki Chicken Bites", "false", "false", "true");

        //dbItemHelper.addNewItem("TGIF Cheese Sticks", "TGIF (small 10pc)", "Meals", "Vons", 4);
        //dbStatusHelper.addNewStatus("TGIF Cheese Sticks", "false", "false", "true");

        //dbItemHelper.addNewItem("Frozen Pizza", "Thin Pepperoni", "Meals", "Vons", 5);
        //dbStatusHelper.addNewStatus("Frozen Pizza", "false", "true", "false");

        //dbItemHelper.addNewItem("Corn Dogs", "Foster Farms", "Meals", "Vons", 6);
        //dbStatusHelper.addNewStatus("Corn Dogs", "false", "true", "false");

        //dbItemHelper.addNewItem("Hot Dogs", "Bun Size", "Meals", "Vons", 7);
        //dbStatusHelper.addNewStatus("Hot Dogs", "false", "true", "false");

        //dbItemHelper.addNewItem("Hot Dog Buns", "(8 pack)", "Meals", "Vons", 8);
        //dbStatusHelper.addNewStatus("Hot Dog Buns", "false", "true", "false");

        //dbItemHelper.addNewItem("Hamburger Patties", "to do", "Meals", "Vons", 9);
        //dbStatusHelper.addNewStatus("Hamburger Patties", "false", "false", "true");

        //dbItemHelper.addNewItem("Hamburger Buns", "(8 pack)", "Meals", "Vons", 10);
        //dbStatusHelper.addNewStatus("Hamburger Buns", "false", "false", "true");

        //dbItemHelper.addNewItem("Pasta Roni 1", "Angel Hair Pasta", "Meals", "Vons", 11);
        //dbStatusHelper.addNewStatus("Pasta Roni 1", "true", "false", "false");

        //dbItemHelper.addNewItem("Pasta Roni 2", "Fetuccini Alfredo", "Meals", "Vons", 12);
        //dbStatusHelper.addNewStatus("Pasta Roni 2", "true", "false", "false");

        //dbItemHelper.addNewItem("Mac & Cheese", "Annie’s", "Meals", "Vons", 13);
        //dbStatusHelper.addNewStatus("Mac & Cheese", "true", "false", "false");

        //dbItemHelper.addNewItem("Gnocci", "Signature Select", "Meals", "Vons", 14);
        //dbStatusHelper.addNewStatus("Gnocci", "true", "false", "false");

        //dbItemHelper.addNewItem("Spaghetti O's", "w/ Meatballs", "Soups", "Vons", 15);
        //dbStatusHelper.addNewStatus("Spaghetti O's", "true", "false", "false");

        //dbItemHelper.addNewItem("Chicken Noodle Soup", "Campbell's", "Soups", "Vons", 16);
        //dbStatusHelper.addNewStatus("Chicken Noodle Soup", "true", "false", "false");

        //dbItemHelper.addNewItem("Minestrone Soup", "Amy's", "Soups", "Vons", 17);
        //dbStatusHelper.addNewStatus("Minestrone Soup", "false", "true", "false");

        //dbItemHelper.addNewItem("Vegetable Barley Soup", "Amy's", "Soups", "Vons", 18);
        //dbStatusHelper.addNewStatus("Vegetable Barley Soup", "false", "true", "false");

        //dbItemHelper.addNewItem("Cup of Noodles", "Nissin", "Soups", "Vons", 19);
        //dbStatusHelper.addNewStatus("Cup of Noodles", "false", "false", "true");

        //dbItemHelper.addNewItem("Fast Food Fries", "Ore-Ida", "Sides", "Vons", 20);
        //dbStatusHelper.addNewStatus("Fast Food Fries", "false", "true", "false");

        //dbItemHelper.addNewItem("Texas Cheesy Bread", "New York Bakery", "Sides", "Vons", 21);
        //dbStatusHelper.addNewStatus("Texas Cheesy Bread", "true", "false", "false");

        //dbItemHelper.addNewItem("Chicken Rice", "Knorr", "Sides", "Vons", 22);
        //dbStatusHelper.addNewStatus("Chicken Rice", "true", "false", "false");

        //dbItemHelper.addNewItem("Canned Corn", "Del Monte", "Sides", "Vons", 23);
        //dbStatusHelper.addNewStatus("Canned Corn", "true", "false", "false");

        //dbItemHelper.addNewItem("Steak", "USDA", "Meat", "Vons", 24);
        //dbStatusHelper.addNewStatus("Steak", "false", "false", "true");

        //dbItemHelper.addNewItem("Ground Beef", "(1 pound)", "Meat", "Vons", 25);
        //dbStatusHelper.addNewStatus("Ground Beef", "false", "false", "true");

        //dbItemHelper.addNewItem("Meatballs", "Homestyle", "Meat", "Vons", 26);
        //dbStatusHelper.addNewStatus("Meatballs", "false", "false", "true");

        //dbItemHelper.addNewItem("Pepperoni", "Hormel", "Meat", "Vons", 27);
        //dbStatusHelper.addNewStatus("Pepperoni", "true", "false", "false");

        //dbItemHelper.addNewItem("Chicken Breast", "na", "Meat", "Vons", 28);
        //dbStatusHelper.addNewStatus("Chicken Breast", "false", "false", "true");

        //dbItemHelper.addNewItem("Sliced Turkey", "to do", "Meat", "Vons", 29);
        //dbStatusHelper.addNewStatus("Sliced Turkey", "false", "false", "true");

        //dbItemHelper.addNewItem("Sliced Ham", "to do", "Meat", "Vons", 30);
        //dbStatusHelper.addNewStatus("Sliced Ham", "false", "false", "true");

        //dbItemHelper.addNewItem("Ham Steak", "to do", "Meat", "Vons", 31);
        //dbStatusHelper.addNewStatus("Ham Steak", "true", "false", "false");

        //dbItemHelper.addNewItem("Thin Spaghetti", "Barilla Whole Grain", "Bread/Grains/Cereal", "Vons", 32);
        //dbStatusHelper.addNewStatus("Thin Spaghetti", "true", "false", "false");

        //dbItemHelper.addNewItem("Spiral Pasta", "Barilla Rotini", "Bread/Grains/Cereal", "Vons", 33);
        //dbStatusHelper.addNewStatus("Spiral Pasta", "true", "false", "false");

        //dbItemHelper.addNewItem("Wheat Bread", "Nature's Own", "Bread/Grains/Cereal", "Vons", 34);
        //dbStatusHelper.addNewStatus("Wheat Bread", "true", "false", "false");

        //dbItemHelper.addNewItem("Baguette", "French", "Bread/Grains/Cereal", "Vons", 35);
        //dbStatusHelper.addNewStatus("Baguette", "false", "true", "false");

        //dbItemHelper.addNewItem("Sourdough Bread", "San Luis Sourdough", "Bread/Grains/Cereal", "Vons", 36);
        //dbStatusHelper.addNewStatus("Sourdough Bread", "true", "false", "false");

        //dbItemHelper.addNewItem("Thomas Muffins", "Original", "Bread/Grains/Cereal", "Vons", 37);
        //dbStatusHelper.addNewStatus("Thomas Muffins", "false", "true", "false");

        //dbItemHelper.addNewItem("RP Cereal", "Reese's Puffs", "Bread/Grains/Cereal", "Vons", 38);
        //dbStatusHelper.addNewStatus("RP Cereal", "true", "false", "false");

        //dbItemHelper.addNewItem("CC Cereal", "Cookie Crisp", "Bread/Grains/Cereal", "Vons", 39);
        //dbStatusHelper.addNewStatus("CC Cereal", "true", "false", "false");

        //dbItemHelper.addNewItem("FMW Cereal", "Frosted Mini Wheat", "Bread/Grains/Cereal", "Vons", 40);
        //dbStatusHelper.addNewStatus("FMW Cereal", "true", "false", "false");

        //dbItemHelper.addNewItem("Eggo Waffles", "Homestyle", "Bread/Grains/Cereal", "Vons", 41);
        //dbStatusHelper.addNewStatus("Eggo Waffles", "true", "false", "false");

        //dbItemHelper.addNewItem("Milk", "Vitamin D", "Eggs/Dairy", "Vons", 42);
        //dbStatusHelper.addNewStatus("Milk", "false", "true", "false");

        //dbItemHelper.addNewItem("Eggs", "Grade AA", "Eggs/Dairy", "Vons", 43);
        //dbStatusHelper.addNewStatus("Eggs", "false", "true", "false");

        //dbItemHelper.addNewItem("Honey Yogurt", "Greek Gods", "Eggs/Dairy", "Vons", 44);
        //dbStatusHelper.addNewStatus("Honey Yogurt", "false", "false", "true");

        //dbItemHelper.addNewItem("Salted Butter", "Challenge", "Eggs/Dairy", "Vons", 45);
        //dbStatusHelper.addNewStatus("Salted Butter", "true", "false", "false");

        //dbItemHelper.addNewItem("Shredded Cheese", "Mexican Blend", "Eggs/Dairy", "Vons", 46);
        //dbStatusHelper.addNewStatus("Shredded Cheese", "true", "false", "false");

        //dbItemHelper.addNewItem("String Cheese", "Mozarella", "Eggs/Dairy", "Vons", 47);
        //dbStatusHelper.addNewStatus("String Cheese", "false", "true", "false");

        //dbItemHelper.addNewItem("BD Cheese", "Black Diamond", "Eggs/Dairy", "Vons", 48);
        //dbStatusHelper.addNewStatus("BD Cheese", "false", "false", "true");

        //dbItemHelper.addNewItem("Non-Stick Spray", "Pam Original", "Eggs/Dairy", "Vons", 49);
        //dbStatusHelper.addNewStatus("Non-Stick Spray", "true", "false", "false");

        //dbItemHelper.addNewItem("Parmesan Cheese", "Kraft", "Condiments", "Vons", 50);
        //dbStatusHelper.addNewStatus("Parmesan Cheese", "true", "false", "false");

        //dbItemHelper.addNewItem("A1 Sauce", "Original", "Condiments", "Vons", 51);
        //dbStatusHelper.addNewStatus("A1 Sauce", "true", "false", "false");

        //dbItemHelper.addNewItem("Ketchup", "Heinz", "Condiments", "Vons", 52);
        //dbStatusHelper.addNewStatus("Ketchup", "true", "false", "false");

        //dbItemHelper.addNewItem("Mustard", "Heinz", "Condiments", "Vons", 53);
        //dbStatusHelper.addNewStatus("Mustard", "true", "false", "false");

        //dbItemHelper.addNewItem("Pasta Sauce", "Ragu Meat", "Condiments", "Vons", 54);
        //dbStatusHelper.addNewStatus("Pasta Sauce", "true", "false", "false");

        //dbItemHelper.addNewItem("Maple Syrup", "Pearl Milling", "Condiments", "Vons", 55);
        //dbStatusHelper.addNewStatus("Maple Syrup", "true", "false", "false");

        //dbItemHelper.addNewItem("Honey", "Local Hive Clover", "Condiments", "Vons", 56);
        //dbStatusHelper.addNewStatus("Honey", "true", "false", "false");

        //dbItemHelper.addNewItem("Peanut Butter", "Skippy Creamy", "Condiments", "Vons", 57);
        //dbStatusHelper.addNewStatus("Peanut Butter", "true", "false", "false");

        //dbItemHelper.addNewItem("Soy Sauce", "Kikoman", "Condiments", "Vons", 58);
        //dbStatusHelper.addNewStatus("Soy Sauce", "true", "false", "false");

        //dbItemHelper.addNewItem("Brown Sugar", "(for hot chocolate)", "Condiments", "Vons", 59);
        //dbStatusHelper.addNewStatus("Brown Sugar", "true", "false", "false");

        //dbItemHelper.addNewItem("Salt & Pepeper", "na", "Seasonings", "Vons", 60);
        //dbStatusHelper.addNewStatus("Salt & Pepeper", "true", "false", "false");

        //dbItemHelper.addNewItem("Garlic Salt", "Lawry's", "Seasonings", "Vons", 61);
        //dbStatusHelper.addNewStatus("Garlic Salt", "true", "false", "false");

        //dbItemHelper.addNewItem("Lawry's Seasoning Salt", "Lawry's", "Seasonings", "Vons", 62);
        //dbStatusHelper.addNewStatus("Lawry's Seasoning Salt", "true", "false", "false");

        //dbItemHelper.addNewItem("Ranch Dip Mix", "Laura Scudder's", "Seasonings", "Vons", 63);
        //dbStatusHelper.addNewStatus("Ranch Dip Mix", "false", "false", "true");

        //dbItemHelper.addNewItem("Vanilla Extract", "Signature Select", "Seasonings", "Vons", 64);
        //dbStatusHelper.addNewStatus("Vanilla Extract", "true", "false", "false");

        //dbItemHelper.addNewItem("Cinnamon Sugar", "McCormick's", "Seasonings", "Vons", 65);
        //dbStatusHelper.addNewStatus("Cinnamon Sugar", "false", "false", "true");

        //dbItemHelper.addNewItem("Sprinkles", "3 types", "Seasonings", "Vons", 66);
        //dbStatusHelper.addNewStatus("Sprinkles", "false", "false", "true");

        //dbItemHelper.addNewItem("Soda Bottles", "Pepsi or Coke", "Drinks", "Vons", 67);
        //dbStatusHelper.addNewStatus("Soda Bottles", "true", "false", "false");

        //dbItemHelper.addNewItem("Hot Chocolate Mix", "Swiss Miss Dark", "Drinks", "Vons", 68);
        //dbStatusHelper.addNewStatus("Hot Chocolate Mix", "true", "false", "false");

        //dbItemHelper.addNewItem("Bottled Water", "Aquafina", "Drinks", "Vons", 69);
        //dbStatusHelper.addNewStatus("Bottled Water", "true", "false", "false");

        //dbItemHelper.addNewItem("Beef Jerky", "Archer Terriyaki", "Snacks", "Vons", 70);
        //dbStatusHelper.addNewStatus("Beef Jerky", "false", "false", "true");

        //dbItemHelper.addNewItem("Peanuts", "Honey Roasted", "Snacks", "Vons", 71);
        //dbStatusHelper.addNewStatus("Peanuts", "false", "true", "false");

        //dbItemHelper.addNewItem("Shell Peanuts", "Salted", "Snacks", "Vons", 72);
        //dbStatusHelper.addNewStatus("Shell Peanuts", "false", "true", "false");

        //dbItemHelper.addNewItem("Sunflower Seeds", "Salted", "Snacks", "Vons", 73);
        //dbStatusHelper.addNewStatus("Sunflower Seeds", "true", "false", "false");

        //dbItemHelper.addNewItem("Vinegar Chips", "Kettle", "Snacks", "Vons", 74);
        //dbStatusHelper.addNewStatus("Vinegar Chips", "true", "false", "false");

        //dbItemHelper.addNewItem("BBQ Chips", "Kettle", "Snacks", "Vons", 75);
        //dbStatusHelper.addNewStatus("BBQ Chips", "false", "false", "true");

        //dbItemHelper.addNewItem("Doritos", "Cool Ranch", "Snacks", "Vons", 76);
        //dbStatusHelper.addNewStatus("Doritos", "false", "false", "true");

        //dbItemHelper.addNewItem("Lay's Chips", "Classic", "Snacks", "Vons", 77);
        //dbStatusHelper.addNewStatus("Lay's Chips", "true", "false", "false");

        //dbItemHelper.addNewItem("Naan Crisps", "Stonefire", "Snacks", "Vons", 78);
        //dbStatusHelper.addNewStatus("Naan Crisps", "true", "false", "false");

        //dbItemHelper.addNewItem("Oreo Cakesters", "Nabisco", "Snacks", "Vons", 79);
        //dbStatusHelper.addNewStatus("Oreo Cakesters", "false", "false", "true");

        //dbItemHelper.addNewItem("Goldfish", "Cheddar", "Snacks", "Vons", 80);
        //dbStatusHelper.addNewStatus("Goldfish", "false", "true", "false");

        //dbItemHelper.addNewItem("Cheez-Its", "Original", "Snacks", "Vons", 81);
        //dbStatusHelper.addNewStatus("Cheez-Its", "false", "true", "false");

        //dbItemHelper.addNewItem("Famous Amos Cookies", "12 Pack", "Snacks", "Vons", 82);
        //dbStatusHelper.addNewStatus("Famous Amos Cookies", "false", "true", "false");

        //dbItemHelper.addNewItem("Choc. Fudge Pirouette", "Pepperidge Farm", "Snacks", "Vons", 83);
        //dbStatusHelper.addNewStatus("Choc. Fudge Pirouette", "false", "false", "true");

        //dbItemHelper.addNewItem("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 84);
        //dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "false", "false", "true");

        //dbItemHelper.addNewItem("Oreos", "for crumbs", "Desserts", "Vons", 85);
        //dbStatusHelper.addNewStatus("Oreos", "true", "false", "false");

        //dbItemHelper.addNewItem("Choc. Malted Crunch Ice Cream", "Thrifty", "Desserts", "Vons", 86);
        //dbStatusHelper.addNewStatus("Choc. Malted Crunch Ice Cream", "false", "true", "false");

        //dbItemHelper.addNewItem("Reese's PB Cups", "(individually wrapped)", "Candy", "Vons", 87);
        //dbStatusHelper.addNewStatus("Reese's PB Cups", "true", "false", "false");

        //dbItemHelper.addNewItem("Hot Tamales", "na", "Candy", "Vons", 88);
        //dbStatusHelper.addNewStatus("Hot Tamales", "true", "false", "false");

        //dbItemHelper.addNewItem("Mini M&M's", "na", "Candy", "Vons", 89);
        //dbStatusHelper.addNewStatus("Mini M&M's", "false", "true", "false");

        //dbItemHelper.addNewItem("Tootsie Rolls", "na", "Candy", "Vons", 90);
        //dbStatusHelper.addNewStatus("Tootsie Rolls", "true", "false", "false");

        //dbItemHelper.addNewItem("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 91);
        //dbStatusHelper.addNewStatus("Cat Food (wet)", "true", "false", "false");

        //dbItemHelper.addNewItem("100% Pure Pumpkin", "Libby's", "Pet Supplies", "Vons", 92);
        //dbStatusHelper.addNewStatus("100% Pure Pumpkin", "true", "false", "false");

        //dbItemHelper.addNewItem("Delectables", "Squeeze Up 20 pack", "Pet Supplies", "Vons", 93);
        //dbStatusHelper.addNewStatus("Delectables", "true", "false", "false");

        //dbItemHelper.addNewItem("Body Wash", "Suave Mandarin", "Toiletries", "Vons", 94);
        //dbStatusHelper.addNewStatus("Body Wash", "true", "false", "false");

        //dbItemHelper.addNewItem("Shampoo", "Suave 2 in 1", "Toiletries", "Vons", 95);
        //dbStatusHelper.addNewStatus("Shampoo", "true", "false", "false");

        //dbItemHelper.addNewItem("Deodorant", "Old Spice", "Toiletries", "Vons", 96);
        //dbStatusHelper.addNewStatus("Deodorant", "true", "false", "false");

        //dbItemHelper.addNewItem("Mouthwash", "Crest Whitening", "Toiletries", "Vons", 97);
        //dbStatusHelper.addNewStatus("Mouthwash", "true", "false", "false");

        //dbItemHelper.addNewItem("Cotton Swabs", "Q-Tips", "Toiletries", "Vons", 98);
        //dbStatusHelper.addNewStatus("Cotton Swabs", "true", "false", "false");

        //dbItemHelper.addNewItem("Febreeze Air Spray", "Heavy Duty", "Household", "Vons", 99);
        //dbStatusHelper.addNewStatus("Febreeze Air Spray", "true", "false", "false");

        //dbItemHelper.addNewItem("All Purpose Cleaner", "Meyer's Lavender", "Household", "Vons", 100);
        //dbStatusHelper.addNewStatus("All Purpose Cleaner", "true", "false", "false");

        //dbItemHelper.addNewItem("Laundry Detergent", "Woolite", "Household", "Vons", 101);
        //dbStatusHelper.addNewStatus("Laundry Detergent", "true", "false", "false");

        //dbItemHelper.addNewItem("Laundry Sanitizer", "Lysol", "Household", "Vons", 102);
        //dbStatusHelper.addNewStatus("Laundry Sanitizer", "true", "false", "false");

        //dbItemHelper.addNewItem("Aluminum Foil", "Reynolds Wrap", "Household", "Vons", 103);
        //dbStatusHelper.addNewStatus("Aluminum Foil", "true", "false", "false");

        //dbItemHelper.addNewItem("Zip-Lock Bags (small)", "Sandwich", "Household", "Vons", 104);
        //dbStatusHelper.addNewStatus("Zip-Lock Bags (small)", "true", "false", "false");

        //dbItemHelper.addNewItem("Zip-Lock Bags (large)", "Freezer Gallon", "Household", "Vons", 105);
        //dbStatusHelper.addNewStatus("Zip-Lock Bags (large)", "true", "false", "false");

        //dbItemHelper.addNewItem("Saran Wrap", "Plastic Wrap", "Household", "Vons", 106);
        //dbStatusHelper.addNewStatus("Saran Wrap", "true", "false", "false");

        //dbItemHelper.addNewItem("Scrub Sponges", "Non-Scratch", "Household", "Vons", 107);
        //dbStatusHelper.addNewStatus("Scrub Sponges", "true", "false", "false");

        //dbItemHelper.addNewItem("Dawn Powerwash", "Dish Cleaner", "Household", "Vons", 108);
        //dbStatusHelper.addNewStatus("Dawn Powerwash", "true", "false", "false");

        //dbItemHelper.addNewItem("Dish Soap", "Dawn Platinum", "Household", "Vons", 109);
        //dbStatusHelper.addNewStatus("Dish Soap", "true", "false", "false");

        //dbItemHelper.addNewItem("Multivitamin", "One a Day Men's", "Supplements", "Vons", 110);
        //dbStatusHelper.addNewStatus("Multivitamin", "true", "false", "false");

        //dbItemHelper.addNewItem("Triple Omega", "Nature Made", "Supplements", "Vons", 111);
        //dbStatusHelper.addNewStatus("Triple Omega", "true", "false", "false");

        //dbItemHelper.addNewItem("Magnesium", "Nature Made 400mg", "Supplements", "Vons", 112);
        //dbStatusHelper.addNewStatus("Magnesium", "true", "false", "false");

        //dbItemHelper.addNewItem("Zinc", "Sandhu Herbals 50mg", "Supplements", "Vons", 113);
        //dbStatusHelper.addNewStatus("Zinc", "true", "false", "false");

        //dbItemHelper.addNewItem("Calcium", "Nature's Truth 1200 mg", "Supplements", "Vons", 114);
        //dbStatusHelper.addNewStatus("Calcium", "true", "false", "false");

        //dbItemHelper.addNewItem("Biotin", "Natrol Biotin 10,000mcg", "Supplements", "Vons", 115);
        //dbStatusHelper.addNewStatus("Biotin", "true", "false", "false");

        //dbItemHelper.addNewItem("Vitamin D3", "Nature Made 5000 IU", "Supplements", "Vons", 116);
        //dbStatusHelper.addNewStatus("Vitamin D3", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Vons", 117);
        storeData.getStoreViewInStockMap().put("Vons", 74);
        storeData.getStoreViewNeededMap().put("Vons", 19);
        storeData.getStoreViewPausedMap().put("Vons", 24);
        dbStoreHelper.setStoreViews("Vons", 117, 74, 19, 24);


        //------------------------------------Rite Aid----------------------------------------------


        //dbItemHelper.addNewItem("Smarties", "na", "Candy", "Rite Aid", 0);
        //dbStatusHelper.addNewStatus("Smarties", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Rite Aid", 1);
        storeData.getStoreViewInStockMap().put("Rite Aid", 1);
        storeData.getStoreViewNeededMap().put("Rite Aid", 0);
        storeData.getStoreViewPausedMap().put("Rite Aid", 0);
        dbStoreHelper.setStoreViews("Rite Aid", 1, 1, 0, 0);


        //------------------------------------Smart & Final-----------------------------------------

        //dbItemHelper.addNewItem("Churros", "Tio Pepe’s or Hola!", "Desserts", "Smart & Final", 0);
        //dbStatusHelper.addNewStatus("Churros", "false", "false", "true");

        //dbItemHelper.addNewItem("Fun Dip Sticks", "na", "Candy", "Smart & Final", 1);
        //dbStatusHelper.addNewStatus("Fun Dip Sticks", "false", "false", "true");

        //dbItemHelper.addNewItem("Orange Tic Tacs", "na", "Candy", "Smart & Final", 2);
        //dbStatusHelper.addNewStatus("Orange Tic Tacs", "false", "true", "false");


        storeData.getStoreViewAllMap().put("Smart & Final", 3);
        storeData.getStoreViewInStockMap().put("Smart & Final", 0);
        storeData.getStoreViewNeededMap().put("Smart & Final", 1);
        storeData.getStoreViewPausedMap().put("Smart & Final", 2);
        dbStoreHelper.setStoreViews("Smart & Final", 3, 0, 1, 2);


        //------------------------------------Costco------------------------------------------------

        //dbItemHelper.addNewItem("Soda Cans", "Pepsi or Coke", "Drinks", "Costco", 0);
        //dbStatusHelper.addNewStatus("Soda Cans", "true", "false", "false");

        //dbItemHelper.addNewItem("Kitty Liter", "Scoop Away Complete", "Pet Supplies", "Costco", 1);
        //dbStatusHelper.addNewStatus("Kitty Liter", "false", "true", "false");

        //dbItemHelper.addNewItem("Chicken Broth", "Kirkland Organic", "Pet Supplies", "Costco", 2);
        //dbStatusHelper.addNewStatus("Chicken Broth", "true", "false", "false");

        //dbItemHelper.addNewItem("Frozen Vegetables", "Kirkland Organic", "Pet Supplies", "Costco", 3);
        //dbStatusHelper.addNewStatus("Frozen Vegetables", "true", "false", "false");

        //dbItemHelper.addNewItem("Mashed Potatoes", "Main St. Bistro", "Pet Supplies", "Costco", 4);
        //dbStatusHelper.addNewStatus("Mashed Potatoes", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Costco", 5);
        storeData.getStoreViewInStockMap().put("Costco", 4);
        storeData.getStoreViewNeededMap().put("Costco", 1);
        storeData.getStoreViewPausedMap().put("Costco", 0);
        dbStoreHelper.setStoreViews("Costco", 5, 4, 1, 0);


        //------------------------------------Walmart-----------------------------------------------

        //dbItemHelper.addNewItem("Dark Chocolate Caramels", "Ghiradelli", "Candy", "Walmart", 0);
        //dbStatusHelper.addNewStatus("Dark Chocolate Caramels", "true", "false", "false");

        //dbItemHelper.addNewItem("Dishwashing Brush", "Great Value", "Household", "Walmart", 1);
        //dbStatusHelper.addNewStatus("Dishwashing Brush", "true", "false", "false");

        //dbItemHelper.addNewItem("Small Trash Bags", "13 gallon", "Household", "Walmart", 2);
        //dbStatusHelper.addNewStatus("Small Trash Bags", "true", "false", "false");

        //dbItemHelper.addNewItem("Large Trash Bags", "33 gallon", "Household", "Walmart", 3);
        //dbStatusHelper.addNewStatus("Large Trash Bags", "true", "false", "false");

        //dbItemHelper.addNewItem("Compactor Bags", "18 gallon", "Household", "Walmart", 4);
        //dbStatusHelper.addNewStatus("Compactor Bags", "true", "false", "false");

        //dbItemHelper.addNewItem("Paper Towels", "Sparkle", "Household", "Walmart", 5);
        //dbStatusHelper.addNewStatus("Paper Towels", "true", "false", "false");

        //dbItemHelper.addNewItem("Toilet Paper", "Angel Soft", "Household", "Walmart", 6);
        //dbStatusHelper.addNewStatus("Toilet Paper", "true", "false", "false");

        //dbItemHelper.addNewItem("72% Intense Dark Chocolate", "Ghiradelli", "Candy", "Walmart", 7);
        //dbStatusHelper.addNewStatus("72% Intense Dark Chocolate", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Walmart", 8);
        storeData.getStoreViewInStockMap().put("Walmart", 8);
        storeData.getStoreViewNeededMap().put("Walmart", 0);
        storeData.getStoreViewPausedMap().put("Walmart", 0);
        dbStoreHelper.setStoreViews("Walmart", 8, 8, 0, 0);


        //------------------------------------Amazon------------------------------------------------

        //dbItemHelper.addNewItem("Muddy Buddies", "Brownie Supreme", "Snacks", "Amazon", 0);
        //dbStatusHelper.addNewStatus("Muddy Buddies", "true", "false", "false");

        //dbItemHelper.addNewItem("Gingerbread Cookie Mix", "Betty Crocker", "Desserts", "Amazon", 1);
        //dbStatusHelper.addNewStatus("Gingerbread Cookie Mix", "false", "false", "true");

        //dbItemHelper.addNewItem("Jelly Beans", "Sizzling Cinnamon", "Candy", "Amazon", 2);
        //dbStatusHelper.addNewStatus("Jelly Beans", "false", "false", "true");

        //dbItemHelper.addNewItem("Orange Pez", "na", "Candy", "Amazon", 3);
        //dbStatusHelper.addNewStatus("Orange Pez", "false", "true", "false");

        //dbItemHelper.addNewItem("Vanilla Taffy", "na", "Candy", "Amazon", 4);
        //dbStatusHelper.addNewStatus("Vanilla Taffy", "false", "true", "false");

        //dbItemHelper.addNewItem("Vanilla Tootsie Rolls", "na", "Candy", "Amazon", 5);
        //dbStatusHelper.addNewStatus("Vanilla Tootsie Rolls", "true", "false", "false");

        //dbItemHelper.addNewItem("Poop Bags", "Amazon Basics", "Pet Supplies", "Amazon", 6);
        //dbStatusHelper.addNewStatus("Poop Bags", "true", "false", "false");

        //dbItemHelper.addNewItem("Nitrile Gloves", "GMG 100 pack", "Pet Supplies", "Amazon", 7);
        //dbStatusHelper.addNewStatus("Nitrile Gloves", "true", "false", "false");

        //dbItemHelper.addNewItem("Toothpaste", "Tom's Antiplaque & Whitening", "Toiletries", "Amazon", 8);
        //dbStatusHelper.addNewStatus("Toothpaste", "true", "false", "false");

        //dbItemHelper.addNewItem("Floss", "Reach Mint Waxed", "Toiletries", "Amazon", 9);
        //dbStatusHelper.addNewStatus("Floss", "true", "false", "false");

        //dbItemHelper.addNewItem("Shaving Cream", "Sandalwood", "Toiletries", "Amazon", 10);
        //dbStatusHelper.addNewStatus("Shaving Cream", "true", "false", "false");

        //dbItemHelper.addNewItem("Shaving Razors", "Gillette ProGlide", "Toiletries", "Amazon", 11);
        //dbStatusHelper.addNewStatus("Shaving Razors", "true", "false", "false");

        //dbItemHelper.addNewItem("Sunscreen", "Hawaiian Tropic Sheer 50spf", "Toiletries", "Amazon", 12);
        //dbStatusHelper.addNewStatus("Sunscreen", "true", "false", "false");

        //dbItemHelper.addNewItem("Pet Stain Cleaner", "Rocco & Roxie", "Household", "Amazon", 13);
        //dbStatusHelper.addNewStatus("Pet Stain Cleaner", "true", "false", "false");

        //dbItemHelper.addNewItem("Night Light Bulbs", "C7 E12", "Household", "Amazon", 14);
        //dbStatusHelper.addNewStatus("Night Light Bulbs", "true", "false", "false");

        //dbItemHelper.addNewItem("Vitamin C", "Amazon Elements 1000 mg", "Supplements", "Amazon", 15);
        //dbStatusHelper.addNewStatus("Vitamin C", "true", "false", "false");

        //dbItemHelper.addNewItem("Fortune Cookies", "to do", "Snacks", "Amazon", 16);
        //dbStatusHelper.addNewStatus("Fortune Cookies", "true", "false", "false");

        //dbItemHelper.addNewItem("Communion Wafers", "to do", "Snacks", "Amazon", 17);
        //dbStatusHelper.addNewStatus("Communion Wafers", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Amazon", 18);
        storeData.getStoreViewInStockMap().put("Amazon", 14);
        storeData.getStoreViewNeededMap().put("Amazon", 2);
        storeData.getStoreViewPausedMap().put("Amazon", 2);
        dbStoreHelper.setStoreViews("Amazon", 18, 14, 2, 2);


        //------------------------------------Stater Bros-------------------------------------------

        //dbItemHelper.addNewItem("Beef Noodles", "Yakisoba", "Soups", "Stater Bros", 0);
        //dbStatusHelper.addNewStatus("Beef Noodles", "false", "false", "true");

        //dbItemHelper.addNewItem("Choc. Chip Cookie Mix", "Gluten Free", "Desserts", "Stater Bros", 1);
        //dbStatusHelper.addNewStatus("Choc. Chip Cookie Mix", "true", "false", "false");

        //dbItemHelper.addNewItem("Choc. Malt Mix", "Nestle", "Desserts", "Stater Bros", 2);
        //dbStatusHelper.addNewStatus("Choc. Malt Mix", "true", "false", "false");

        //dbItemHelper.addNewItem("Choc. Fudge Pudding", "Snack Pack", "Snacks", "Stater Bros", 3);
        //dbStatusHelper.addNewStatus("Choc. Fudge Pudding", "false", "false", "true");

        //dbItemHelper.addNewItem("Dryer Sheets", "Simply Done Fresh Linen", "Household", "Stater Bros", 4);
        //dbStatusHelper.addNewStatus("Dryer Sheets", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Stater Bros", 5);
        storeData.getStoreViewInStockMap().put("Stater Bros", 3);
        storeData.getStoreViewNeededMap().put("Stater Bros", 0);
        storeData.getStoreViewPausedMap().put("Stater Bros", 2);
        dbStoreHelper.setStoreViews("Stater Bros", 5, 3, 0, 2);


        //------------------------------------CVS---------------------------------------------------

        //dbItemHelper.addNewItem("Dark Chocolate Pretzels", "Flipz", "Snacks", "CVS", 0);
        //dbStatusHelper.addNewStatus("Dark Chocolate Pretzels", "true", "false", "false");

        //dbItemHelper.addNewItem("Candy Corn", "Brach's", "Candy", "CVS", 1);
        //dbStatusHelper.addNewStatus("Candy Corn", "true", "false", "false");

        //dbItemHelper.addNewItem("Rubbing Alcohol", "Isopropyl", "Household", "CVS", 2);
        //dbStatusHelper.addNewStatus("Rubbing Alcohol", "true", "false", "false");

        //dbItemHelper.addNewItem("Hydrogen Peroxide", "na", "Household", "CVS", 3);
        //dbStatusHelper.addNewStatus("Hydrogen Peroxide", "true", "false", "false");


        storeData.getStoreViewAllMap().put("CVS", 4);
        storeData.getStoreViewInStockMap().put("CVS", 4);
        storeData.getStoreViewNeededMap().put("CVS", 0);
        storeData.getStoreViewPausedMap().put("CVS", 0);
        dbStoreHelper.setStoreViews("CVS", 4, 4, 0, 0);


        //------------------------------------Dollar Tree-------------------------------------------

        //dbItemHelper.addNewItem("Sno Caps", "na", "Candy", "Dollar Tree", 0);
        //dbStatusHelper.addNewStatus("Sno Caps", "true", "false", "false");

        //dbItemHelper.addNewItem("Hand Soap", "Lavender & Chamomile", "Toiletries", "Dollar Tree", 1);
        //dbStatusHelper.addNewStatus("Hand Soap", "true", "false", "false");

        //dbItemHelper.addNewItem("Ramen Noodles", "Nissin", "Soups", "Dollar Tree", 2);
        //dbStatusHelper.addNewStatus("Ramen Noodles", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Dollar Tree", 3);
        storeData.getStoreViewInStockMap().put("Dollar Tree", 3);
        storeData.getStoreViewNeededMap().put("Dollar Tree", 0);
        storeData.getStoreViewPausedMap().put("Dollar Tree", 0);
        dbStoreHelper.setStoreViews("Dollar Tree", 3, 3, 0, 0);


        //------------------------------------Ralphs------------------------------------------------

        //dbItemHelper.addNewItem("Clarified Butter", "Challenge", "Eggs/Dairy", "Ralphs", 0);
        //dbStatusHelper.addNewStatus("Clarified Butter", "false", "false", "true");


        storeData.getStoreViewAllMap().put("Ralphs", 1);
        storeData.getStoreViewInStockMap().put("Ralphs", 0);
        storeData.getStoreViewNeededMap().put("Ralphs", 0);
        storeData.getStoreViewPausedMap().put("Ralphs", 1);
        dbStoreHelper.setStoreViews("Ralphs", 1, 0, 0, 1);


        //------------------------------------Target------------------------------------------------

        //dbItemHelper.addNewItem("Chocolate Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 0);
        //dbStatusHelper.addNewStatus("Chocolate Syrup", "false", "true", "false");

        //dbItemHelper.addNewItem("Caramel Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 1);
        //dbStatusHelper.addNewStatus("Caramel Syrup", "false", "true", "false");


        storeData.getStoreViewAllMap().put("Target", 2);
        storeData.getStoreViewInStockMap().put("Target", 0);
        storeData.getStoreViewNeededMap().put("Target", 2);
        storeData.getStoreViewPausedMap().put("Target", 0);
        dbStoreHelper.setStoreViews("Target", 2, 0, 2, 0);


        //------------------------------------Pet Supplies Plus-------------------------------------

        //dbItemHelper.addNewItem("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 0);
        //dbStatusHelper.addNewStatus("Cat Food (dry)", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Pet Supplies Plus", 1);
        storeData.getStoreViewInStockMap().put("Pet Supplies Plus", 1);
        storeData.getStoreViewNeededMap().put("Pet Supplies Plus", 0);
        storeData.getStoreViewPausedMap().put("Pet Supplies Plus", 0);
        dbStoreHelper.setStoreViews("Pet Supplies Plus", 1, 1, 0, 0);


        //------------------------------------Sprouts-------------------------------------------

        //dbItemHelper.addNewItem("Caramel Squares", "na", "Candy", "Sprouts", 0);
        //dbStatusHelper.addNewStatus("Caramel Squares", "false", "false", "true");

        //dbItemHelper.addNewItem("Bar Soap", "Zum Bar Sea Salt", "Toiletries", "Sprouts", 1);
        //dbStatusHelper.addNewStatus("Bar Soap", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Sprouts", 2);
        storeData.getStoreViewInStockMap().put("Sprouts", 1);
        storeData.getStoreViewNeededMap().put("Sprouts", 0);
        storeData.getStoreViewPausedMap().put("Sprouts", 1);
        dbStoreHelper.setStoreViews("Sprouts", 2, 1, 0, 1);


        //------------------------------------Sam's Club--------------------------------------------

        //dbItemHelper.addNewItem("Quick Steak", "Gary's", "Meat", "Sam's Club", 0);
        //dbStatusHelper.addNewStatus("Quick Steak", "false", "true", "false");

        //dbItemHelper.addNewItem("Paper Plates", "to do", "Household", "Sam's Club", 1);
        //dbStatusHelper.addNewStatus("Paper Plates", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Sam's Club", 2);
        storeData.getStoreViewInStockMap().put("Sam's Club", 1);
        storeData.getStoreViewNeededMap().put("Sam's Club", 1);
        storeData.getStoreViewPausedMap().put("Sam's Club", 0);
        dbStoreHelper.setStoreViews("Sam's Club", 2, 1, 1, 0);


        //---------------------------------------Staples--------------------------------------------

        //dbItemHelper.addNewItem("Multipurpose Paper", "Tru Red 20/96", "Household", "Staples", 0);
        //dbStatusHelper.addNewStatus("Multipurpose Paper", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Staples", 1);
        storeData.getStoreViewInStockMap().put("Staples", 1);
        storeData.getStoreViewNeededMap().put("Staples", 0);
        storeData.getStoreViewPausedMap().put("Staples", 0);
        dbStoreHelper.setStoreViews("Staples", 1, 1, 0, 0);


        //------------------------------------Yorba Linda Feed Store--------------------------------

        //dbItemHelper.addNewItem("Dog Food (dry)", "Canidae All Life Stages", "Pet Supplies", "Yorba Linda Feed Store", 0);
        //dbStatusHelper.addNewStatus("Dog Food (dry)", "true", "false", "false");


        storeData.getStoreViewAllMap().put("Yorba Linda Feed Store", 1);
        storeData.getStoreViewInStockMap().put("Yorba Linda Feed Store", 1);
        storeData.getStoreViewNeededMap().put("Yorba Linda Feed Store", 0);
        storeData.getStoreViewPausedMap().put("Yorba Linda Feed Store", 0);
        dbStoreHelper.setStoreViews("Yorba Linda Feed Store", 1, 1, 0, 0);

        //------------------------------------------------------------------------------------------


        dbItemHelper = new DBItemHelper(this);
        dbStatusHelper = new DBStatusHelper(this);
        dbCategoryHelper = new DBCategoryHelper(this);
        dbStoreHelper = new DBStoreHelper(this);
        itemData = dbItemHelper.readItemData();
        statusData = dbStatusHelper.readStatusData();
        itemData.updateStatuses(statusData);
        categoryData = dbCategoryHelper.readCategoryData();
        storeData = dbStoreHelper.readStoreData();
        itemIsSelectedInInventory = false;
        itemIsSelectedInShoppingList = false;
        itemIsClickedInInventory = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i ++) {
            itemIsClickedInInventory.add(false);
        }
        itemIsClickedInShoppingList = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i ++) {
            itemIsClickedInShoppingList.add(false);
        }
        itemIsChecked = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i ++) {
            itemIsChecked.add(false);
        }
        storeNum = 0;
        reorderItemsCategory = "";
        editItemInInventory = false;
        editItemInShoppingList = false;
        inventoryView = INVENTORY_ALL;
        inventorySortBy = SORT_BY_CATEGORY;

    }
}