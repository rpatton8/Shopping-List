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
        dbStatusHelper.addNewStatus("Sausage Biscuits", "instock", "unchecked");

        dbItemHelper.addNewItem("Hamburger Helper", "Cheeseburger Macaroni", "Meals", "Vons", 1);
        dbStatusHelper.addNewStatus("Hamburger Helper", "paused", "unchecked");

        dbItemHelper.addNewItem("Buffalo Chicken Bites", "TGIF or Frank's", "Meals", "Vons", 2);
        dbStatusHelper.addNewStatus("Buffalo Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItem("Terriyaki Chicken Bites", "InnovAsian", "Meals", "Vons", 3);
        dbStatusHelper.addNewStatus("Terriyaki Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItem("TGIF Cheese Sticks", "TGIF (small 10pc)", "Meals", "Vons", 4);
        dbStatusHelper.addNewStatus("TGIF Cheese Sticks", "paused", "unchecked");

        dbItemHelper.addNewItem("Frozen Pizza", "Thin Pepperoni", "Meals", "Vons", 5);
        dbStatusHelper.addNewStatus("Frozen Pizza", "needed", "unchecked");

        dbItemHelper.addNewItem("Corn Dogs", "Foster Farms", "Meals", "Vons", 6);
        dbStatusHelper.addNewStatus("Corn Dogs", "needed", "unchecked");

        dbItemHelper.addNewItem("Hot Dogs", "Bun Size", "Meals", "Vons", 7);
        dbStatusHelper.addNewStatus("Hot Dogs", "needed", "unchecked");

        dbItemHelper.addNewItem("Hot Dog Buns", "(8 pack)", "Meals", "Vons", 8);
        dbStatusHelper.addNewStatus("Hot Dog Buns", "needed", "unchecked");

        dbItemHelper.addNewItem("Hamburger Patties", "to do", "Meals", "Vons", 9);
        dbStatusHelper.addNewStatus("Hamburger Patties", "paused", "unchecked");

        dbItemHelper.addNewItem("Hamburger Buns", "(8 pack)", "Meals", "Vons", 10);
        dbStatusHelper.addNewStatus("Hamburger Buns", "paused", "unchecked");

        dbItemHelper.addNewItem("Pasta Roni 1", "Angel Hair Pasta", "Meals", "Vons", 11);
        dbStatusHelper.addNewStatus("Pasta Roni 1", "instock", "unchecked");

        dbItemHelper.addNewItem("Pasta Roni 2", "Fetuccini Alfredo", "Meals", "Vons", 12);
        dbStatusHelper.addNewStatus("Pasta Roni 2", "instock", "unchecked");

        dbItemHelper.addNewItem("Mac & Cheese", "Annie’s", "Meals", "Vons", 13);
        dbStatusHelper.addNewStatus("Mac & Cheese", "instock", "unchecked");

        dbItemHelper.addNewItem("Gnocci", "Signature Select", "Meals", "Vons", 14);
        dbStatusHelper.addNewStatus("Gnocci", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Meals", 15);
        categoryData.getCategoryViewInStockMap().put("Meals", 5);
        categoryData.getCategoryViewNeededMap().put("Meals", 4);
        categoryData.getCategoryViewPausedMap().put("Meals", 6);
        dbCategoryHelper.setCategoryViews("Meals", 15, 5, 4, 6);


        //------------------------------------Soups-------------------------------------------------

        dbItemHelper.addNewItem("Spaghetti O's", "w/ Meatballs", "Soups", "Vons", 0);
        dbStatusHelper.addNewStatus("Spaghetti O's", "instock", "unchecked");

        dbItemHelper.addNewItem("Chicken Noodle Soup", "Campbell's", "Soups", "Vons", 1);
        dbStatusHelper.addNewStatus("Chicken Noodle Soup", "instock", "unchecked");

        dbItemHelper.addNewItem("Minestrone Soup", "Amy's", "Soups", "Vons", 2);
        dbStatusHelper.addNewStatus("Minestrone Soup", "needed", "unchecked");

        dbItemHelper.addNewItem("Vegetable Barley Soup", "Amy's", "Soups", "Vons", 3);
        dbStatusHelper.addNewStatus("Vegetable Barley Soup", "needed", "unchecked");

        dbItemHelper.addNewItem("Beef Noodles", "Yakisoba", "Soups", "Stater Bros", 4);
        dbStatusHelper.addNewStatus("Beef Noodles", "paused", "unchecked");

        dbItemHelper.addNewItem("Cup of Noodles", "Nissin", "Soups", "Vons", 5);
        dbStatusHelper.addNewStatus("Cup of Noodles", "paused", "unchecked");

        dbItemHelper.addNewItem("Ramen Noodles", "Nissin", "Soups", "Dollar Tree", 6);
        dbStatusHelper.addNewStatus("Ramen Noodles", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Soups", 7);
        categoryData.getCategoryViewInStockMap().put("Soups", 3);
        categoryData.getCategoryViewNeededMap().put("Soups", 2);
        categoryData.getCategoryViewPausedMap().put("Soups", 2);
        dbCategoryHelper.setCategoryViews("Soups", 7, 3, 2, 2);


        //------------------------------------Sides-------------------------------------------------

        dbItemHelper.addNewItem("Frozen French Fries", "Ore-Ida", "Sides", "Vons", 0);
        dbStatusHelper.addNewStatus("Frozen French Fries", "needed", "unchecked");

        dbItemHelper.addNewItem("Texas Cheesy Bread", "New York Bakery", "Sides", "Vons", 1);
        dbStatusHelper.addNewStatus("Texas Cheesy Bread", "instock", "unchecked");

        dbItemHelper.addNewItem("Chicken Rice", "Knorr", "Sides", "Vons", 2);
        dbStatusHelper.addNewStatus("Chicken Rice", "instock", "unchecked");

        dbItemHelper.addNewItem("Canned Corn", "Del Monte", "Sides", "Vons", 3);
        dbStatusHelper.addNewStatus("Canned Corn", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Sides", 4);
        categoryData.getCategoryViewInStockMap().put("Sides", 3);
        categoryData.getCategoryViewNeededMap().put("Sides", 1);
        categoryData.getCategoryViewPausedMap().put("Sides", 0);
        dbCategoryHelper.setCategoryViews("Sides", 4, 3, 1, 0);


        //------------------------------------Meat--------------------------------------------------

        dbItemHelper.addNewItem("Steak", "USDA", "Meat", "Vons", 0);
        dbStatusHelper.addNewStatus("Steak", "paused", "unchecked");

        dbItemHelper.addNewItem("Ground Beef", "(1 pound)", "Meat", "Vons", 1);
        dbStatusHelper.addNewStatus("Ground Beef", "paused", "unchecked");

        dbItemHelper.addNewItem("Meatballs", "Homestyle", "Meat", "Vons", 2);
        dbStatusHelper.addNewStatus("Meatballs", "paused", "unchecked");

        dbItemHelper.addNewItem("Pepperoni", "Hormel", "Meat", "Vons", 3);
        dbStatusHelper.addNewStatus("Pepperoni", "instock", "unchecked");

        dbItemHelper.addNewItem("Quick Steak", "Gary's", "Meat", "Sam's Club", 4);
        dbStatusHelper.addNewStatus("Quick Steak", "needed", "unchecked");

        dbItemHelper.addNewItem("Chicken Breast", "na", "Meat", "Vons", 5);
        dbStatusHelper.addNewStatus("Chicken Breast", "paused", "unchecked");

        dbItemHelper.addNewItem("Sliced Turkey", "to do", "Meat", "Vons", 6);
        dbStatusHelper.addNewStatus("Sliced Turkey", "paused", "unchecked");

        dbItemHelper.addNewItem("Sliced Ham", "to do", "Meat", "Vons", 7);
        dbStatusHelper.addNewStatus("Sliced Ham", "paused", "unchecked");

        dbItemHelper.addNewItem("Ham Steak", "to do", "Meat", "Vons", 8);
        dbStatusHelper.addNewStatus("Ham Steak", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Meat", 9);
        categoryData.getCategoryViewInStockMap().put("Meat", 2);
        categoryData.getCategoryViewNeededMap().put("Meat", 1);
        categoryData.getCategoryViewPausedMap().put("Meat", 6);
        dbCategoryHelper.setCategoryViews("Meat", 9, 2, 1, 6);


        //------------------------------------Bread/Grains/Cereal-----------------------------------

        dbItemHelper.addNewItem("Thin Spaghetti", "Barilla Whole Grain", "Bread/Grains/Cereal", "Vons", 0);
        dbStatusHelper.addNewStatus("Thin Spaghetti", "instock", "unchecked");

        dbItemHelper.addNewItem("Spiral Pasta", "Barilla Rotini", "Bread/Grains/Cereal", "Vons", 1);
        dbStatusHelper.addNewStatus("Spiral Pasta", "instock", "unchecked");

        dbItemHelper.addNewItem("Wheat Bread", "Nature's Own", "Bread/Grains/Cereal", "Vons", 2);
        dbStatusHelper.addNewStatus("Wheat Bread", "instock", "unchecked");

        dbItemHelper.addNewItem("Baguette", "French", "Bread/Grains/Cereal", "Vons", 3);
        dbStatusHelper.addNewStatus("Baguette", "needed", "unchecked");

        dbItemHelper.addNewItem("Sourdough Bread", "San Luis Sourdough", "Bread/Grains/Cereal", "Vons", 4);
        dbStatusHelper.addNewStatus("Sourdough Bread", "instock", "unchecked");

        dbItemHelper.addNewItem("Thomas Muffins", "Original", "Bread/Grains/Cereal", "Vons", 5);
        dbStatusHelper.addNewStatus("Thomas Muffins", "needed", "unchecked");

        dbItemHelper.addNewItem("RP Cereal", "Reese's Puffs", "Bread/Grains/Cereal", "Vons", 6);
        dbStatusHelper.addNewStatus("RP Cereal", "instock", "unchecked");

        dbItemHelper.addNewItem("CC Cereal", "Cookie Crisp", "Bread/Grains/Cereal", "Vons", 7);
        dbStatusHelper.addNewStatus("CC Cereal", "instock", "unchecked");

        dbItemHelper.addNewItem("FMW Cereal", "Frosted Mini Wheat", "Bread/Grains/Cereal", "Vons", 8);
        dbStatusHelper.addNewStatus("FMW Cereal", "instock", "unchecked");

        dbItemHelper.addNewItem("Eggo Waffles", "Homestyle", "Bread/Grains/Cereal", "Vons", 9);
        dbStatusHelper.addNewStatus("Eggo Waffles", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Bread/Grains/Cereal", 10);
        categoryData.getCategoryViewInStockMap().put("Bread/Grains/Cereal", 8);
        categoryData.getCategoryViewNeededMap().put("Bread/Grains/Cereal", 2);
        categoryData.getCategoryViewPausedMap().put("Bread/Grains/Cereal", 0);
        dbCategoryHelper.setCategoryViews("Bread/Grains/Cereal", 10, 8, 2, 0);


        //----------------------------------------Eggs/Dairy----------------------------------------

        dbItemHelper.addNewItem("Milk", "Vitamin D", "Eggs/Dairy", "Vons", 0);
        dbStatusHelper.addNewStatus("Milk", "needed", "unchecked");

        dbItemHelper.addNewItem("Eggs", "Grade AA", "Eggs/Dairy", "Vons", 1);
        dbStatusHelper.addNewStatus("Eggs", "needed", "unchecked");

        dbItemHelper.addNewItem("Honey Yogurt", "Greek Gods", "Eggs/Dairy", "Vons", 2);
        dbStatusHelper.addNewStatus("Honey Yogurt", "paused", "unchecked");

        dbItemHelper.addNewItem("Salted Butter", "Challenge", "Eggs/Dairy", "Vons", 3);
        dbStatusHelper.addNewStatus("Salted Butter", "instock", "unchecked");

        dbItemHelper.addNewItem("Clarified Butter", "Challenge", "Eggs/Dairy", "Ralphs", 4);
        dbStatusHelper.addNewStatus("Clarified Butter", "paused", "unchecked");

        dbItemHelper.addNewItem("Shredded Cheese", "Mexican Blend", "Eggs/Dairy", "Vons", 5);
        dbStatusHelper.addNewStatus("Shredded Cheese", "instock", "unchecked");

        dbItemHelper.addNewItem("String Cheese", "Mozarella", "Eggs/Dairy", "Vons", 6);
        dbStatusHelper.addNewStatus("String Cheese", "needed", "unchecked");

        dbItemHelper.addNewItem("BD Cheese", "Black Diamond", "Eggs/Dairy", "Vons", 7);
        dbStatusHelper.addNewStatus("BD Cheese", "paused", "unchecked");

        dbItemHelper.addNewItem("Non-Stick Spray", "Pam Original", "Eggs/Dairy", "Vons", 8);
        dbStatusHelper.addNewStatus("Non-Stick Spray", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Eggs/Dairy", 9);
        categoryData.getCategoryViewInStockMap().put("Eggs/Dairy", 3);
        categoryData.getCategoryViewNeededMap().put("Eggs/Dairy", 3);
        categoryData.getCategoryViewPausedMap().put("Eggs/Dairy", 3);
        dbCategoryHelper.setCategoryViews("Eggs/Dairy", 9, 3, 3, 3);


        //------------------------------------Condiments--------------------------------------------

        dbItemHelper.addNewItem("Parmesan Cheese", "Kraft", "Condiments", "Vons", 0);
        dbStatusHelper.addNewStatus("Parmesan Cheese", "instock", "unchecked");

        dbItemHelper.addNewItem("A1 Sauce", "Original", "Condiments", "Vons", 1);
        dbStatusHelper.addNewStatus("A1 Sauce", "instock", "unchecked");

        dbItemHelper.addNewItem("Ketchup", "Heinz", "Condiments", "Vons", 2);
        dbStatusHelper.addNewStatus("Ketchup", "instock", "unchecked");

        dbItemHelper.addNewItem("Mustard", "Heinz", "Condiments", "Vons", 3);
        dbStatusHelper.addNewStatus("Mustard", "instock", "unchecked");

        dbItemHelper.addNewItem("Pasta Sauce", "Ragu Meat", "Condiments", "Vons", 4);
        dbStatusHelper.addNewStatus("Pasta Sauce", "instock", "unchecked");

        dbItemHelper.addNewItem("Chocolate Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 5);
        dbStatusHelper.addNewStatus("Chocolate Syrup", "needed", "unchecked");

        dbItemHelper.addNewItem("Caramel Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 6);
        dbStatusHelper.addNewStatus("Caramel Syrup", "needed", "unchecked");

        dbItemHelper.addNewItem("Maple Syrup", "Pearl Milling", "Condiments", "Vons", 7);
        dbStatusHelper.addNewStatus("Maple Syrup", "instock", "unchecked");

        dbItemHelper.addNewItem("Honey", "Local Hive Clover", "Condiments", "Vons", 8);
        dbStatusHelper.addNewStatus("Honey", "instock", "unchecked");

        dbItemHelper.addNewItem("Peanut Butter", "Skippy Creamy", "Condiments", "Vons", 9);
        dbStatusHelper.addNewStatus("Peanut Butter", "instock", "unchecked");

        dbItemHelper.addNewItem("Soy Sauce", "Kikoman", "Condiments", "Vons", 10);
        dbStatusHelper.addNewStatus("Soy Sauce", "instock", "unchecked");

        dbItemHelper.addNewItem("Brown Sugar", "(for hot chocolate)", "Condiments", "Vons", 11);
        dbStatusHelper.addNewStatus("Brown Sugar", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Condiments", 12);
        categoryData.getCategoryViewInStockMap().put("Condiments", 10);
        categoryData.getCategoryViewNeededMap().put("Condiments", 2);
        categoryData.getCategoryViewPausedMap().put("Condiments", 0);
        dbCategoryHelper.setCategoryViews("Condiments", 12, 10, 2, 0);


        //------------------------------------Seasonings--------------------------------------------

        dbItemHelper.addNewItem("Salt & Pepeper", "na", "Seasonings", "Vons", 0);
        dbStatusHelper.addNewStatus("Salt & Pepeper", "instock", "unchecked");

        dbItemHelper.addNewItem("Garlic Salt", "Lawry's", "Seasonings", "Vons", 1);
        dbStatusHelper.addNewStatus("Garlic Salt", "instock", "unchecked");

        dbItemHelper.addNewItem("Lawry's Seasoning Salt", "Lawry's", "Seasonings", "Vons", 2);
        dbStatusHelper.addNewStatus("Lawry's Seasoning Salt", "instock", "unchecked");

        dbItemHelper.addNewItem("Ranch Dip Mix", "Laura Scudder's", "Seasonings", "Vons", 3);
        dbStatusHelper.addNewStatus("Ranch Dip Mix", "paused", "unchecked");

        dbItemHelper.addNewItem("Vanilla Extract", "Signature Select", "Seasonings", "Vons", 4);
        dbStatusHelper.addNewStatus("Vanilla Extract", "instock", "unchecked");

        dbItemHelper.addNewItem("Cinnamon Sugar", "McCormick's", "Seasonings", "Vons", 5);
        dbStatusHelper.addNewStatus("Cinnamon Sugar", "paused", "unchecked");

        dbItemHelper.addNewItem("Sprinkles", "3 types", "Seasonings", "Vons", 6);
        dbStatusHelper.addNewStatus("Sprinkles", "paused", "unchecked");


        categoryData.getCategoryViewAllMap().put("Seasonings", 7);
        categoryData.getCategoryViewInStockMap().put("Seasonings", 4);
        categoryData.getCategoryViewNeededMap().put("Seasonings", 0);
        categoryData.getCategoryViewPausedMap().put("Seasonings", 3);
        dbCategoryHelper.setCategoryViews("Seasonings", 7, 4, 0, 3);


        //------------------------------------Drinks------------------------------------------------

        dbItemHelper.addNewItem("Soda Bottles", "Pepsi or Coke", "Drinks", "Vons", 0);
        dbStatusHelper.addNewStatus("Soda Bottles", "instock", "unchecked");

        dbItemHelper.addNewItem("Soda Cans", "Pepsi or Coke", "Drinks", "Costco", 1);
        dbStatusHelper.addNewStatus("Soda Cans", "instock", "unchecked");

        dbItemHelper.addNewItem("Hot Chocolate Mix", "Swiss Miss Dark", "Drinks", "Vons", 2);
        dbStatusHelper.addNewStatus("Hot Chocolate Mix", "instock", "unchecked");

        dbItemHelper.addNewItem("Bottled Water", "Aquafina", "Drinks", "Vons", 3);
        dbStatusHelper.addNewStatus("Bottled Water", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Drinks", 4);
        categoryData.getCategoryViewInStockMap().put("Drinks", 4);
        categoryData.getCategoryViewNeededMap().put("Drinks", 0);
        categoryData.getCategoryViewPausedMap().put("Drinks", 0);
        dbCategoryHelper.setCategoryViews("Drinks", 4, 4, 0, 0);


        //------------------------------------Snacks------------------------------------------------

        dbItemHelper.addNewItem("Beef Jerky", "Archer Terriyaki", "Snacks", "Vons", 0);
        dbStatusHelper.addNewStatus("Beef Jerky", "paused", "unchecked");

        dbItemHelper.addNewItem("Peanuts", "Honey Roasted", "Snacks", "Vons", 1);
        dbStatusHelper.addNewStatus("Peanuts", "needed", "unchecked");

        dbItemHelper.addNewItem("Shell Peanuts", "Salted", "Snacks", "Vons", 2);
        dbStatusHelper.addNewStatus("Shell Peanuts", "needed", "unchecked");

        dbItemHelper.addNewItem("Sunflower Seeds", "Salted", "Snacks", "Vons", 3);
        dbStatusHelper.addNewStatus("Sunflower Seeds", "instock", "unchecked");

        dbItemHelper.addNewItem("Vinegar Chips", "Kettle", "Snacks", "Vons", 4);
        dbStatusHelper.addNewStatus("Vinegar Chips", "instock", "unchecked");

        dbItemHelper.addNewItem("BBQ Chips", "Kettle", "Snacks", "Vons", 5);
        dbStatusHelper.addNewStatus("BBQ Chips", "paused", "unchecked");

        dbItemHelper.addNewItem("Doritos", "Cool Ranch", "Snacks", "Vons", 6);
        dbStatusHelper.addNewStatus("Doritos", "paused", "unchecked");

        dbItemHelper.addNewItem("Lay's Chips", "Classic", "Snacks", "Vons", 7);
        dbStatusHelper.addNewStatus("Lay's Chips", "instock", "unchecked");

        dbItemHelper.addNewItem("Naan Crisps", "Stonefire", "Snacks", "Vons", 8);
        dbStatusHelper.addNewStatus("Naan Crisps", "instock", "unchecked");

        dbItemHelper.addNewItem("Oreo Cakesters", "Nabisco", "Snacks", "Vons", 9);
        dbStatusHelper.addNewStatus("Oreo Cakesters", "paused", "unchecked");

        dbItemHelper.addNewItem("Goldfish", "Cheddar", "Snacks", "Vons", 10);
        dbStatusHelper.addNewStatus("Goldfish", "needed", "unchecked");

        dbItemHelper.addNewItem("Cheez-Its", "Original", "Snacks", "Vons", 11);
        dbStatusHelper.addNewStatus("Cheez-Its", "needed", "unchecked");

        dbItemHelper.addNewItem("Famous Amos Cookies", "12 Pack", "Snacks", "Vons", 12);
        dbStatusHelper.addNewStatus("Famous Amos Cookies", "needed", "unchecked");

        dbItemHelper.addNewItem("Dark Chocolate Pretzels", "Flipz", "Snacks", "CVS", 13);
        dbStatusHelper.addNewStatus("Dark Chocolate Pretzels", "instock", "unchecked");

        dbItemHelper.addNewItem("Choc. Fudge Pudding", "Snack Pack", "Snacks", "Stater Bros", 14);
        dbStatusHelper.addNewStatus("Choc. Fudge Pudding", "paused", "unchecked");

        dbItemHelper.addNewItem("Choc. Fudge Pirouette", "Pepperidge Farm", "Snacks", "Vons", 15);
        dbStatusHelper.addNewStatus("Choc. Fudge Pirouette", "paused", "unchecked");

        dbItemHelper.addNewItem("Muddy Buddies", "Brownie Supreme", "Snacks", "Amazon", 16);
        dbStatusHelper.addNewStatus("Muddy Buddies", "instock", "unchecked");

        dbItemHelper.addNewItem("Fortune Cookies", "to do", "Snacks", "Amazon", 17);
        dbStatusHelper.addNewStatus("Fortune Cookies", "instock", "unchecked");

        dbItemHelper.addNewItem("Communion Wafers", "to do", "Snacks", "Amazon", 18);
        dbStatusHelper.addNewStatus("Communion Wafers", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Snacks", 19);
        categoryData.getCategoryViewInStockMap().put("Snacks", 8);
        categoryData.getCategoryViewNeededMap().put("Snacks", 5);
        categoryData.getCategoryViewPausedMap().put("Snacks", 6);
        dbCategoryHelper.setCategoryViews("Snacks", 19, 8, 5, 6);


        //------------------------------------Desserts----------------------------------------------

        dbItemHelper.addNewItem("Choc. Malted Crunch Ice Cream", "Thrifty", "Desserts", "Vons", 0);
        dbStatusHelper.addNewStatus("Choc. Malted Crunch Ice Cream", "needed", "unchecked");

        dbItemHelper.addNewItem("Churros", "Tio Pepe’s or Hola!", "Desserts", "Smart & Final", 1);
        dbStatusHelper.addNewStatus("Churros", "paused", "unchecked");

        dbItemHelper.addNewItem("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 2);
        dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "paused", "unchecked");

        dbItemHelper.addNewItem("Choc. Chip Cookie Mix", "Gluten Free", "Desserts", "Stater Bros", 3);
        dbStatusHelper.addNewStatus("Choc. Chip Cookie Mix", "paused", "unchecked");

        dbItemHelper.addNewItem("Gingerbread Cookie Mix", "Betty Crocker", "Desserts", "Amazon", 4);
        dbStatusHelper.addNewStatus("Gingerbread Cookie Mix", "paused", "unchecked");

        dbItemHelper.addNewItem("Oreos", "for crumbs", "Desserts", "Vons", 5);
        dbStatusHelper.addNewStatus("Oreos", "instock", "unchecked");

        dbItemHelper.addNewItem("Choc. Malt Mix", "Nestle", "Desserts", "Stater Bros", 6);
        dbStatusHelper.addNewStatus("Choc. Malt Mix", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Desserts", 7);
        categoryData.getCategoryViewInStockMap().put("Desserts", 2);
        categoryData.getCategoryViewNeededMap().put("Desserts", 1);
        categoryData.getCategoryViewPausedMap().put("Desserts", 4);
        dbCategoryHelper.setCategoryViews("Desserts", 7, 2, 1, 4);


        //------------------------------------Candy-------------------------------------------------

        dbItemHelper.addNewItem("Dark Chocolate Caramels", "Ghiradelli", "Candy", "Walmart", 0);
        dbStatusHelper.addNewStatus("Dark Chocolate Caramels", "instock", "unchecked");

        dbItemHelper.addNewItem("Reese's PB Cups", "(individually wrapped)", "Candy", "Vons", 1);
        dbStatusHelper.addNewStatus("Reese's PB Cups", "instock", "unchecked");

        dbItemHelper.addNewItem("Candy Corn", "Brach's", "Candy", "CVS", 2);
        dbStatusHelper.addNewStatus("Candy Corn", "instock", "unchecked");

        dbItemHelper.addNewItem("Hot Tamales", "na", "Candy", "Vons", 3);
        dbStatusHelper.addNewStatus("Hot Tamales", "instock", "unchecked");

        dbItemHelper.addNewItem("Smarties", "na", "Candy", "Rite Aid", 4);
        dbStatusHelper.addNewStatus("Smarties", "instock", "unchecked");

        dbItemHelper.addNewItem("Sno Caps", "na", "Candy", "Dollar Tree", 5);
        dbStatusHelper.addNewStatus("Sno Caps", "instock", "unchecked");

        dbItemHelper.addNewItem("Mini M&M's", "na", "Candy", "Vons", 6);
        dbStatusHelper.addNewStatus("Mini M&M's", "needed", "unchecked");

        dbItemHelper.addNewItem("Caramel Squares", "na", "Candy", "Sprouts", 7);
        dbStatusHelper.addNewStatus("Caramel Squares", "paused", "unchecked");

        dbItemHelper.addNewItem("Jelly Beans", "Sizzling Cinnamon", "Candy", "Amazon", 8);
        dbStatusHelper.addNewStatus("Jelly Beans", "paused", "unchecked");

        dbItemHelper.addNewItem("Tootsie Rolls", "na", "Candy", "Vons", 9);
        dbStatusHelper.addNewStatus("Tootsie Rolls", "instock", "unchecked");

        dbItemHelper.addNewItem("Fun Dip Sticks", "na", "Candy", "Smart & Final", 10);
        dbStatusHelper.addNewStatus("Fun Dip Sticks", "paused", "unchecked");

        dbItemHelper.addNewItem("72% Intense Dark Chocolate", "Ghiradelli", "Candy", "Walmart", 11);
        dbStatusHelper.addNewStatus("72% Intense Dark Chocolate", "instock", "unchecked");

        dbItemHelper.addNewItem("Orange Tic Tacs", "na", "Candy", "Smart & Final", 12);
        dbStatusHelper.addNewStatus("Orange Tic Tacs", "needed", "unchecked");

        dbItemHelper.addNewItem("Orange Pez", "na", "Candy", "Amazon", 13);
        dbStatusHelper.addNewStatus("Orange Pez", "needed", "unchecked");

        dbItemHelper.addNewItem("Vanilla Taffy", "na", "Candy", "Amazon", 14);
        dbStatusHelper.addNewStatus("Vanilla Taffy", "needed", "unchecked");

        dbItemHelper.addNewItem("Vanilla Tootsie Rolls", "na", "Candy", "Amazon", 15);
        dbStatusHelper.addNewStatus("Vanilla Tootsie Rolls", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Candy", 16);
        categoryData.getCategoryViewInStockMap().put("Candy", 9);
        categoryData.getCategoryViewNeededMap().put("Candy", 4);
        categoryData.getCategoryViewPausedMap().put("Candy", 3);
        dbCategoryHelper.setCategoryViews("Candy", 16, 9, 4, 3);


        //------------------------------------Pet Supplies-------------------------------------------

        dbItemHelper.addNewItem("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 0);
        dbStatusHelper.addNewStatus("Cat Food (wet)", "instock", "unchecked");

        dbItemHelper.addNewItem("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 1);
        dbStatusHelper.addNewStatus("Cat Food (dry)", "instock", "unchecked");

        dbItemHelper.addNewItem("Delectables", "Squeeze Up 20 pack", "Pet Supplies", "Vons", 2);
        dbStatusHelper.addNewStatus("Delectables", "instock", "unchecked");

        dbItemHelper.addNewItem("Kitty Liter", "Scoop Away Complete", "Pet Supplies", "Costco", 3);
        dbStatusHelper.addNewStatus("Kitty Liter", "needed", "unchecked");

        dbItemHelper.addNewItem("Dog Food (dry)", "Canidae All Life Stages", "Pet Supplies", "Yorba Linda Feed Store", 4);
        dbStatusHelper.addNewStatus("Dog Food (dry)", "instock", "unchecked");

        dbItemHelper.addNewItem("Chicken Broth", "Kirkland Organic", "Pet Supplies", "Costco", 5);
        dbStatusHelper.addNewStatus("Chicken Broth", "instock", "unchecked");

        dbItemHelper.addNewItem("Frozen Vegetables", "Kirkland Organic", "Pet Supplies", "Costco", 6);
        dbStatusHelper.addNewStatus("Frozen Vegetables", "instock", "unchecked");

        dbItemHelper.addNewItem("Mashed Potatoes", "Main St. Bistro", "Pet Supplies", "Costco", 7);
        dbStatusHelper.addNewStatus("Mashed Potatoes", "instock", "unchecked");

        dbItemHelper.addNewItem("100% Pure Pumpkin", "Libby's", "Pet Supplies", "Vons", 8);
        dbStatusHelper.addNewStatus("100% Pure Pumpkin", "instock", "unchecked");

        dbItemHelper.addNewItem("Poop Bags", "Amazon Basics", "Pet Supplies", "Amazon", 9);
        dbStatusHelper.addNewStatus("Poop Bags", "instock", "unchecked");

        dbItemHelper.addNewItem("Nitrile Gloves", "GMG 100 pack", "Pet Supplies", "Amazon", 10);
        dbStatusHelper.addNewStatus("Nitrile Gloves", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Pet Supplies", 11);
        categoryData.getCategoryViewInStockMap().put("Pet Supplies", 10);
        categoryData.getCategoryViewNeededMap().put("Pet Supplies", 1);
        categoryData.getCategoryViewPausedMap().put("Pet Supplies", 0);
        dbCategoryHelper.setCategoryViews("Pet Supplies", 11, 10, 1, 0);


        //------------------------------------Toiletries--------------------------------------------

        dbItemHelper.addNewItem("Hand Soap", "Lavender & Chamomile", "Toiletries", "Dollar Tree", 0);
        dbStatusHelper.addNewStatus("Hand Soap", "instock", "unchecked");

        dbItemHelper.addNewItem("Body Wash", "Suave Mandarin", "Toiletries", "Vons", 1);
        dbStatusHelper.addNewStatus("Body Wash", "instock", "unchecked");

        dbItemHelper.addNewItem("Shampoo", "Suave 2 in 1", "Toiletries", "Vons", 2);
        dbStatusHelper.addNewStatus("Shampoo", "instock", "unchecked");

        dbItemHelper.addNewItem("Bar Soap", "Zum Bar Sea Salt", "Toiletries", "Sprouts", 3);
        dbStatusHelper.addNewStatus("Bar Soap", "instock", "unchecked");

        dbItemHelper.addNewItem("Deodorant", "Old Spice", "Toiletries", "Vons", 4);
        dbStatusHelper.addNewStatus("Deodorant", "instock", "unchecked");

        dbItemHelper.addNewItem("Toothpaste", "Tom's Antiplaque & Whitening", "Toiletries", "Amazon", 5);
        dbStatusHelper.addNewStatus("Toothpaste", "instock", "unchecked");

        dbItemHelper.addNewItem("Floss", "Reach Mint Waxed", "Toiletries", "Amazon", 6);
        dbStatusHelper.addNewStatus("Floss", "instock", "unchecked");

        dbItemHelper.addNewItem("Shaving Cream", "Sandalwood", "Toiletries", "Amazon", 7);
        dbStatusHelper.addNewStatus("Shaving Cream", "instock", "unchecked");

        dbItemHelper.addNewItem("Shaving Razors", "Gillette ProGlide", "Toiletries", "Amazon", 8);
        dbStatusHelper.addNewStatus("Shaving Razors", "instock", "unchecked");

        dbItemHelper.addNewItem("Mouthwash", "Crest Whitening", "Toiletries", "Vons", 9);
        dbStatusHelper.addNewStatus("Mouthwash", "instock", "unchecked");

        dbItemHelper.addNewItem("Cotton Swabs", "Q-Tips", "Toiletries", "Vons", 10);
        dbStatusHelper.addNewStatus("Cotton Swabs", "instock", "unchecked");

        dbItemHelper.addNewItem("Sunscreen", "Hawaiian Tropic Sheer 50spf", "Toiletries", "Amazon", 11);
        dbStatusHelper.addNewStatus("Sunscreen", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Toiletries", 12);
        categoryData.getCategoryViewInStockMap().put("Toiletries", 12);
        categoryData.getCategoryViewNeededMap().put("Toiletries", 0);
        categoryData.getCategoryViewPausedMap().put("Toiletries", 0);
        dbCategoryHelper.setCategoryViews("Toiletries", 12, 12, 0, 0);


        //------------------------------------Household-------------------------------------------------

        dbItemHelper.addNewItem("Febreeze Air Spray", "Heavy Duty", "Household", "Vons", 0);
        dbStatusHelper.addNewStatus("Febreeze Air Spray", "instock", "unchecked");

        dbItemHelper.addNewItem("All Purpose Cleaner", "Meyer's Lavender", "Household", "Vons", 1);
        dbStatusHelper.addNewStatus("All Purpose Cleaner", "instock", "unchecked");

        dbItemHelper.addNewItem("Pet Stain Cleaner", "Rocco & Roxie", "Household", "Amazon", 2);
        dbStatusHelper.addNewStatus("Pet Stain Cleaner", "instock", "unchecked");

        dbItemHelper.addNewItem("Laundry Detergent", "Woolite", "Household", "Vons", 3);
        dbStatusHelper.addNewStatus("Laundry Detergent", "instock", "unchecked");

        dbItemHelper.addNewItem("Laundry Sanitizer", "Lysol", "Household", "Vons", 4);
        dbStatusHelper.addNewStatus("Laundry Sanitizer", "instock", "unchecked");

        dbItemHelper.addNewItem("Dryer Sheets", "Simply Done Fresh Linen", "Household", "Stater Bros", 5);
        dbStatusHelper.addNewStatus("Dryer Sheets", "instock", "unchecked");

        dbItemHelper.addNewItem("Aluminum Foil", "Reynolds Wrap", "Household", "Vons", 6);
        dbStatusHelper.addNewStatus("Aluminum Foil", "instock", "unchecked");

        dbItemHelper.addNewItem("Zip-Lock Bags (small)", "Sandwich", "Household", "Vons", 7);
        dbStatusHelper.addNewStatus("Zip-Lock Bags (small)", "instock", "unchecked");

        dbItemHelper.addNewItem("Zip-Lock Bags (large)", "Freezer Gallon", "Household", "Vons", 8);
        dbStatusHelper.addNewStatus("Zip-Lock Bags (large)", "instock", "unchecked");

        dbItemHelper.addNewItem("Saran Wrap", "Plastic Wrap", "Household", "Vons", 9);
        dbStatusHelper.addNewStatus("Saran Wrap", "instock", "unchecked");

        dbItemHelper.addNewItem("Rubbing Alcohol", "Isopropyl", "Household", "CVS", 10);
        dbStatusHelper.addNewStatus("Rubbing Alcohol", "instock", "unchecked");

        dbItemHelper.addNewItem("Hydrogen Peroxide", "na", "Household", "CVS", 11);
        dbStatusHelper.addNewStatus("Hydrogen Peroxide", "instock", "unchecked");

        dbItemHelper.addNewItem("Night Light Bulbs", "C7 E12", "Household", "Amazon", 12);
        dbStatusHelper.addNewStatus("Night Light Bulbs", "instock", "unchecked");

        dbItemHelper.addNewItem("Scrub Sponges", "Non-Scratch", "Household", "Vons", 13);
        dbStatusHelper.addNewStatus("Scrub Sponges", "instock", "unchecked");

        dbItemHelper.addNewItem("Dishwashing Brush", "Great Value", "Household", "Walmart", 14);
        dbStatusHelper.addNewStatus("Dishwashing Brush", "instock", "unchecked");

        dbItemHelper.addNewItem("Small Trash Bags", "13 gallon", "Household", "Walmart", 15);
        dbStatusHelper.addNewStatus("Small Trash Bags", "instock", "unchecked");

        dbItemHelper.addNewItem("Large Trash Bags", "33 gallon", "Household", "Walmart", 16);
        dbStatusHelper.addNewStatus("Large Trash Bags", "instock", "unchecked");

        dbItemHelper.addNewItem("Compactor Bags", "18 gallon", "Household", "Walmart", 17);
        dbStatusHelper.addNewStatus("Compactor Bags", "instock", "unchecked");

        dbItemHelper.addNewItem("Dawn Powerwash", "Dish Cleaner", "Household", "Vons", 18);
        dbStatusHelper.addNewStatus("Dawn Powerwash", "instock", "unchecked");

        dbItemHelper.addNewItem("Dish Soap", "Dawn Platinum", "Household", "Vons", 19);
        dbStatusHelper.addNewStatus("Dish Soap", "instock", "unchecked");

        dbItemHelper.addNewItem("Paper Plates", "to do", "Household", "Sam's Club", 20);
        dbStatusHelper.addNewStatus("Paper Plates", "instock", "unchecked");

        dbItemHelper.addNewItem("Paper Towels", "Sparkle", "Household", "Walmart", 21);
        dbStatusHelper.addNewStatus("Paper Towels", "instock", "unchecked");

        dbItemHelper.addNewItem("Toilet Paper", "Angel Soft", "Household", "Walmart", 22);
        dbStatusHelper.addNewStatus("Toilet Paper", "instock", "unchecked");

        dbItemHelper.addNewItem("Multipurpose Paper", "Tru Red 20/96", "Household", "Staples", 23);
        dbStatusHelper.addNewStatus("Multipurpose Paper", "instock", "unchecked");


        categoryData.getCategoryViewAllMap().put("Household", 24);
        categoryData.getCategoryViewInStockMap().put("Household", 24);
        categoryData.getCategoryViewNeededMap().put("Household", 0);
        categoryData.getCategoryViewPausedMap().put("Household", 0);
        dbCategoryHelper.setCategoryViews("Household", 24, 24, 0, 0);


        //------------------------------------Supplements-------------------------------------------------

        dbItemHelper.addNewItem("Triple Omega", "Nature Made", "Supplements", "Vons", 0);
        dbStatusHelper.addNewStatus("Triple Omega", "instock", "unchecked");

        dbItemHelper.addNewItem("Multivitamin", "One a Day Men's", "Supplements", "Vons", 1);
        dbStatusHelper.addNewStatus("Multivitamin", "instock", "unchecked");

        dbItemHelper.addNewItem("Vitamin C", "Amazon Elements 1000 mg", "Supplements", "Amazon", 2);
        dbStatusHelper.addNewStatus("Vitamin C", "instock", "unchecked");

        dbItemHelper.addNewItem("Magnesium", "Nature Made 400mg", "Supplements", "Vons", 3);
        dbStatusHelper.addNewStatus("Magnesium", "instock", "unchecked");

        dbItemHelper.addNewItem("Zinc", "Sandhu Herbals 50mg", "Supplements", "Vons", 4);
        dbStatusHelper.addNewStatus("Zinc", "instock", "unchecked");

        dbItemHelper.addNewItem("Calcium", "Nature's Truth 1200 mg", "Supplements", "Vons", 5);
        dbStatusHelper.addNewStatus("Calcium", "instock", "unchecked");

        dbItemHelper.addNewItem("Biotin", "Natrol Biotin 10,000mcg", "Supplements", "Vons", 6);
        dbStatusHelper.addNewStatus("Biotin", "instock", "unchecked");

        dbItemHelper.addNewItem("Vitamin D3", "Nature Made 5000 IU", "Supplements", "Vons", 7);
        dbStatusHelper.addNewStatus("Vitamin D3", "instock", "unchecked");


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
        //dbStatusHelper.addNewStatus("Sausage Biscuits", "instock", "unchecked");

        //dbItemHelper.addNewItem("Hamburger Helper", "Cheeseburger Macaroni", "Meals", "Vons", 1);
        //dbStatusHelper.addNewStatus("Hamburger Helper", "paused", "unchecked");

        //dbItemHelper.addNewItem("Buffalo Chicken Bites", "TGIF or Frank's", "Meals", "Vons", 2);
        //dbStatusHelper.addNewStatus("Buffalo Chicken Bites", "paused", "unchecked");

        //dbItemHelper.addNewItem("Terriyaki Chicken Bites", "InnovAsian", "Meals", "Vons", 3);
        //dbStatusHelper.addNewStatus("Terriyaki Chicken Bites", "paused", "unchecked");

        //dbItemHelper.addNewItem("TGIF Cheese Sticks", "TGIF (small 10pc)", "Meals", "Vons", 4);
        //dbStatusHelper.addNewStatus("TGIF Cheese Sticks", "paused", "unchecked");

        //dbItemHelper.addNewItem("Frozen Pizza", "Thin Pepperoni", "Meals", "Vons", 5);
        //dbStatusHelper.addNewStatus("Frozen Pizza", "needed", "unchecked");

        //dbItemHelper.addNewItem("Corn Dogs", "Foster Farms", "Meals", "Vons", 6);
        //dbStatusHelper.addNewStatus("Corn Dogs", "needed", "unchecked");

        //dbItemHelper.addNewItem("Hot Dogs", "Bun Size", "Meals", "Vons", 7);
        //dbStatusHelper.addNewStatus("Hot Dogs", "needed", "unchecked");

        //dbItemHelper.addNewItem("Hot Dog Buns", "(8 pack)", "Meals", "Vons", 8);
        //dbStatusHelper.addNewStatus("Hot Dog Buns", "needed", "unchecked");

        //dbItemHelper.addNewItem("Hamburger Patties", "to do", "Meals", "Vons", 9);
        //dbStatusHelper.addNewStatus("Hamburger Patties", "paused", "unchecked");

        //dbItemHelper.addNewItem("Hamburger Buns", "(8 pack)", "Meals", "Vons", 10);
        //dbStatusHelper.addNewStatus("Hamburger Buns", "paused", "unchecked");

        //dbItemHelper.addNewItem("Pasta Roni 1", "Angel Hair Pasta", "Meals", "Vons", 11);
        //dbStatusHelper.addNewStatus("Pasta Roni 1", "instock", "unchecked");

        //dbItemHelper.addNewItem("Pasta Roni 2", "Fetuccini Alfredo", "Meals", "Vons", 12);
        //dbStatusHelper.addNewStatus("Pasta Roni 2", "instock", "unchecked");

        //dbItemHelper.addNewItem("Mac & Cheese", "Annie’s", "Meals", "Vons", 13);
        //dbStatusHelper.addNewStatus("Mac & Cheese", "instock", "unchecked");

        //dbItemHelper.addNewItem("Gnocci", "Signature Select", "Meals", "Vons", 14);
        //dbStatusHelper.addNewStatus("Gnocci", "instock", "unchecked");

        //dbItemHelper.addNewItem("Spaghetti O's", "w/ Meatballs", "Soups", "Vons", 15);
        //dbStatusHelper.addNewStatus("Spaghetti O's", "instock", "unchecked");

        //dbItemHelper.addNewItem("Chicken Noodle Soup", "Campbell's", "Soups", "Vons", 16);
        //dbStatusHelper.addNewStatus("Chicken Noodle Soup", "instock", "unchecked");

        //dbItemHelper.addNewItem("Minestrone Soup", "Amy's", "Soups", "Vons", 17);
        //dbStatusHelper.addNewStatus("Minestrone Soup", "needed", "unchecked");

        //dbItemHelper.addNewItem("Vegetable Barley Soup", "Amy's", "Soups", "Vons", 18);
        //dbStatusHelper.addNewStatus("Vegetable Barley Soup", "needed", "unchecked");

        //dbItemHelper.addNewItem("Cup of Noodles", "Nissin", "Soups", "Vons", 19);
        //dbStatusHelper.addNewStatus("Cup of Noodles", "paused", "unchecked");

        //dbItemHelper.addNewItem("Fast Food Fries", "Ore-Ida", "Sides", "Vons", 20);
        //dbStatusHelper.addNewStatus("Fast Food Fries", "needed", "unchecked");

        //dbItemHelper.addNewItem("Texas Cheesy Bread", "New York Bakery", "Sides", "Vons", 21);
        //dbStatusHelper.addNewStatus("Texas Cheesy Bread", "instock", "unchecked");

        //dbItemHelper.addNewItem("Chicken Rice", "Knorr", "Sides", "Vons", 22);
        //dbStatusHelper.addNewStatus("Chicken Rice", "instock", "unchecked");

        //dbItemHelper.addNewItem("Canned Corn", "Del Monte", "Sides", "Vons", 23);
        //dbStatusHelper.addNewStatus("Canned Corn", "instock", "unchecked");

        //dbItemHelper.addNewItem("Steak", "USDA", "Meat", "Vons", 24);
        //dbStatusHelper.addNewStatus("Steak", "paused", "unchecked");

        //dbItemHelper.addNewItem("Ground Beef", "(1 pound)", "Meat", "Vons", 25);
        //dbStatusHelper.addNewStatus("Ground Beef", "paused", "unchecked");

        //dbItemHelper.addNewItem("Meatballs", "Homestyle", "Meat", "Vons", 26);
        //dbStatusHelper.addNewStatus("Meatballs", "paused", "unchecked");

        //dbItemHelper.addNewItem("Pepperoni", "Hormel", "Meat", "Vons", 27);
        //dbStatusHelper.addNewStatus("Pepperoni", "instock", "unchecked");

        //dbItemHelper.addNewItem("Chicken Breast", "na", "Meat", "Vons", 28);
        //dbStatusHelper.addNewStatus("Chicken Breast", "paused", "unchecked");

        //dbItemHelper.addNewItem("Sliced Turkey", "to do", "Meat", "Vons", 29);
        //dbStatusHelper.addNewStatus("Sliced Turkey", "paused", "unchecked");

        //dbItemHelper.addNewItem("Sliced Ham", "to do", "Meat", "Vons", 30);
        //dbStatusHelper.addNewStatus("Sliced Ham", "paused", "unchecked");

        //dbItemHelper.addNewItem("Ham Steak", "to do", "Meat", "Vons", 31);
        //dbStatusHelper.addNewStatus("Ham Steak", "instock", "unchecked");

        //dbItemHelper.addNewItem("Thin Spaghetti", "Barilla Whole Grain", "Bread/Grains/Cereal", "Vons", 32);
        //dbStatusHelper.addNewStatus("Thin Spaghetti", "instock", "unchecked");

        //dbItemHelper.addNewItem("Spiral Pasta", "Barilla Rotini", "Bread/Grains/Cereal", "Vons", 33);
        //dbStatusHelper.addNewStatus("Spiral Pasta", "instock", "unchecked");

        //dbItemHelper.addNewItem("Wheat Bread", "Nature's Own", "Bread/Grains/Cereal", "Vons", 34);
        //dbStatusHelper.addNewStatus("Wheat Bread", "instock", "unchecked");

        //dbItemHelper.addNewItem("Baguette", "French", "Bread/Grains/Cereal", "Vons", 35);
        //dbStatusHelper.addNewStatus("Baguette", "needed", "unchecked");

        //dbItemHelper.addNewItem("Sourdough Bread", "San Luis Sourdough", "Bread/Grains/Cereal", "Vons", 36);
        //dbStatusHelper.addNewStatus("Sourdough Bread", "instock", "unchecked");

        //dbItemHelper.addNewItem("Thomas Muffins", "Original", "Bread/Grains/Cereal", "Vons", 37);
        //dbStatusHelper.addNewStatus("Thomas Muffins", "needed", "unchecked");

        //dbItemHelper.addNewItem("RP Cereal", "Reese's Puffs", "Bread/Grains/Cereal", "Vons", 38);
        //dbStatusHelper.addNewStatus("RP Cereal", "instock", "unchecked");

        //dbItemHelper.addNewItem("CC Cereal", "Cookie Crisp", "Bread/Grains/Cereal", "Vons", 39);
        //dbStatusHelper.addNewStatus("CC Cereal", "instock", "unchecked");

        //dbItemHelper.addNewItem("FMW Cereal", "Frosted Mini Wheat", "Bread/Grains/Cereal", "Vons", 40);
        //dbStatusHelper.addNewStatus("FMW Cereal", "instock", "unchecked");

        //dbItemHelper.addNewItem("Eggo Waffles", "Homestyle", "Bread/Grains/Cereal", "Vons", 41);
        //dbStatusHelper.addNewStatus("Eggo Waffles", "instock", "unchecked");

        //dbItemHelper.addNewItem("Milk", "Vitamin D", "Eggs/Dairy", "Vons", 42);
        //dbStatusHelper.addNewStatus("Milk", "needed", "unchecked");

        //dbItemHelper.addNewItem("Eggs", "Grade AA", "Eggs/Dairy", "Vons", 43);
        //dbStatusHelper.addNewStatus("Eggs", "needed", "unchecked");

        //dbItemHelper.addNewItem("Honey Yogurt", "Greek Gods", "Eggs/Dairy", "Vons", 44);
        //dbStatusHelper.addNewStatus("Honey Yogurt", "paused", "unchecked");

        //dbItemHelper.addNewItem("Salted Butter", "Challenge", "Eggs/Dairy", "Vons", 45);
        //dbStatusHelper.addNewStatus("Salted Butter", "instock", "unchecked");

        //dbItemHelper.addNewItem("Shredded Cheese", "Mexican Blend", "Eggs/Dairy", "Vons", 46);
        //dbStatusHelper.addNewStatus("Shredded Cheese", "instock", "unchecked");

        //dbItemHelper.addNewItem("String Cheese", "Mozarella", "Eggs/Dairy", "Vons", 47);
        //dbStatusHelper.addNewStatus("String Cheese", "needed", "unchecked");

        //dbItemHelper.addNewItem("BD Cheese", "Black Diamond", "Eggs/Dairy", "Vons", 48);
        //dbStatusHelper.addNewStatus("BD Cheese", "paused", "unchecked");

        //dbItemHelper.addNewItem("Non-Stick Spray", "Pam Original", "Eggs/Dairy", "Vons", 49);
        //dbStatusHelper.addNewStatus("Non-Stick Spray", "instock", "unchecked");

        //dbItemHelper.addNewItem("Parmesan Cheese", "Kraft", "Condiments", "Vons", 50);
        //dbStatusHelper.addNewStatus("Parmesan Cheese", "instock", "unchecked");

        //dbItemHelper.addNewItem("A1 Sauce", "Original", "Condiments", "Vons", 51);
        //dbStatusHelper.addNewStatus("A1 Sauce", "instock", "unchecked");

        //dbItemHelper.addNewItem("Ketchup", "Heinz", "Condiments", "Vons", 52);
        //dbStatusHelper.addNewStatus("Ketchup", "instock", "unchecked");

        //dbItemHelper.addNewItem("Mustard", "Heinz", "Condiments", "Vons", 53);
        //dbStatusHelper.addNewStatus("Mustard", "instock", "unchecked");

        //dbItemHelper.addNewItem("Pasta Sauce", "Ragu Meat", "Condiments", "Vons", 54);
        //dbStatusHelper.addNewStatus("Pasta Sauce", "instock", "unchecked");

        //dbItemHelper.addNewItem("Maple Syrup", "Pearl Milling", "Condiments", "Vons", 55);
        //dbStatusHelper.addNewStatus("Maple Syrup", "instock", "unchecked");

        //dbItemHelper.addNewItem("Honey", "Local Hive Clover", "Condiments", "Vons", 56);
        //dbStatusHelper.addNewStatus("Honey", "instock", "unchecked");

        //dbItemHelper.addNewItem("Peanut Butter", "Skippy Creamy", "Condiments", "Vons", 57);
        //dbStatusHelper.addNewStatus("Peanut Butter", "instock", "unchecked");

        //dbItemHelper.addNewItem("Soy Sauce", "Kikoman", "Condiments", "Vons", 58);
        //dbStatusHelper.addNewStatus("Soy Sauce", "instock", "unchecked");

        //dbItemHelper.addNewItem("Brown Sugar", "(for hot chocolate)", "Condiments", "Vons", 59);
        //dbStatusHelper.addNewStatus("Brown Sugar", "instock", "unchecked");

        //dbItemHelper.addNewItem("Salt & Pepeper", "na", "Seasonings", "Vons", 60);
        //dbStatusHelper.addNewStatus("Salt & Pepeper", "instock", "unchecked");

        //dbItemHelper.addNewItem("Garlic Salt", "Lawry's", "Seasonings", "Vons", 61);
        //dbStatusHelper.addNewStatus("Garlic Salt", "instock", "unchecked");

        //dbItemHelper.addNewItem("Lawry's Seasoning Salt", "Lawry's", "Seasonings", "Vons", 62);
        //dbStatusHelper.addNewStatus("Lawry's Seasoning Salt", "instock", "unchecked");

        //dbItemHelper.addNewItem("Ranch Dip Mix", "Laura Scudder's", "Seasonings", "Vons", 63);
        //dbStatusHelper.addNewStatus("Ranch Dip Mix", "paused", "unchecked");

        //dbItemHelper.addNewItem("Vanilla Extract", "Signature Select", "Seasonings", "Vons", 64);
        //dbStatusHelper.addNewStatus("Vanilla Extract", "instock", "unchecked");

        //dbItemHelper.addNewItem("Cinnamon Sugar", "McCormick's", "Seasonings", "Vons", 65);
        //dbStatusHelper.addNewStatus("Cinnamon Sugar", "paused", "unchecked");

        //dbItemHelper.addNewItem("Sprinkles", "3 types", "Seasonings", "Vons", 66);
        //dbStatusHelper.addNewStatus("Sprinkles", "paused", "unchecked");

        //dbItemHelper.addNewItem("Soda Bottles", "Pepsi or Coke", "Drinks", "Vons", 67);
        //dbStatusHelper.addNewStatus("Soda Bottles", "instock", "unchecked");

        //dbItemHelper.addNewItem("Hot Chocolate Mix", "Swiss Miss Dark", "Drinks", "Vons", 68);
        //dbStatusHelper.addNewStatus("Hot Chocolate Mix", "instock", "unchecked");

        //dbItemHelper.addNewItem("Bottled Water", "Aquafina", "Drinks", "Vons", 69);
        //dbStatusHelper.addNewStatus("Bottled Water", "instock", "unchecked");

        //dbItemHelper.addNewItem("Beef Jerky", "Archer Terriyaki", "Snacks", "Vons", 70);
        //dbStatusHelper.addNewStatus("Beef Jerky", "paused", "unchecked");

        //dbItemHelper.addNewItem("Peanuts", "Honey Roasted", "Snacks", "Vons", 71);
        //dbStatusHelper.addNewStatus("Peanuts", "needed", "unchecked");

        //dbItemHelper.addNewItem("Shell Peanuts", "Salted", "Snacks", "Vons", 72);
        //dbStatusHelper.addNewStatus("Shell Peanuts", "needed", "unchecked");

        //dbItemHelper.addNewItem("Sunflower Seeds", "Salted", "Snacks", "Vons", 73);
        //dbStatusHelper.addNewStatus("Sunflower Seeds", "instock", "unchecked");

        //dbItemHelper.addNewItem("Vinegar Chips", "Kettle", "Snacks", "Vons", 74);
        //dbStatusHelper.addNewStatus("Vinegar Chips", "instock", "unchecked");

        //dbItemHelper.addNewItem("BBQ Chips", "Kettle", "Snacks", "Vons", 75);
        //dbStatusHelper.addNewStatus("BBQ Chips", "paused", "unchecked");

        //dbItemHelper.addNewItem("Doritos", "Cool Ranch", "Snacks", "Vons", 76);
        //dbStatusHelper.addNewStatus("Doritos", "paused", "unchecked");

        //dbItemHelper.addNewItem("Lay's Chips", "Classic", "Snacks", "Vons", 77);
        //dbStatusHelper.addNewStatus("Lay's Chips", "instock", "unchecked");

        //dbItemHelper.addNewItem("Naan Crisps", "Stonefire", "Snacks", "Vons", 78);
        //dbStatusHelper.addNewStatus("Naan Crisps", "instock", "unchecked");

        //dbItemHelper.addNewItem("Oreo Cakesters", "Nabisco", "Snacks", "Vons", 79);
        //dbStatusHelper.addNewStatus("Oreo Cakesters", "paused", "unchecked");

        //dbItemHelper.addNewItem("Goldfish", "Cheddar", "Snacks", "Vons", 80);
        //dbStatusHelper.addNewStatus("Goldfish", "needed", "unchecked");

        //dbItemHelper.addNewItem("Cheez-Its", "Original", "Snacks", "Vons", 81);
        //dbStatusHelper.addNewStatus("Cheez-Its", "needed", "unchecked");

        //dbItemHelper.addNewItem("Famous Amos Cookies", "12 Pack", "Snacks", "Vons", 82);
        //dbStatusHelper.addNewStatus("Famous Amos Cookies", "needed", "unchecked");

        //dbItemHelper.addNewItem("Choc. Fudge Pirouette", "Pepperidge Farm", "Snacks", "Vons", 83);
        //dbStatusHelper.addNewStatus("Choc. Fudge Pirouette", "paused", "unchecked");

        //dbItemHelper.addNewItem("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 84);
        //dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "paused", "unchecked");

        //dbItemHelper.addNewItem("Oreos", "for crumbs", "Desserts", "Vons", 85);
        //dbStatusHelper.addNewStatus("Oreos", "instock", "unchecked");

        //dbItemHelper.addNewItem("Choc. Malted Crunch Ice Cream", "Thrifty", "Desserts", "Vons", 86);
        //dbStatusHelper.addNewStatus("Choc. Malted Crunch Ice Cream", "needed", "unchecked");

        //dbItemHelper.addNewItem("Reese's PB Cups", "(individually wrapped)", "Candy", "Vons", 87);
        //dbStatusHelper.addNewStatus("Reese's PB Cups", "instock", "unchecked");

        //dbItemHelper.addNewItem("Hot Tamales", "na", "Candy", "Vons", 88);
        //dbStatusHelper.addNewStatus("Hot Tamales", "instock", "unchecked");

        //dbItemHelper.addNewItem("Mini M&M's", "na", "Candy", "Vons", 89);
        //dbStatusHelper.addNewStatus("Mini M&M's", "needed", "unchecked");

        //dbItemHelper.addNewItem("Tootsie Rolls", "na", "Candy", "Vons", 90);
        //dbStatusHelper.addNewStatus("Tootsie Rolls", "instock", "unchecked");

        //dbItemHelper.addNewItem("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 91);
        //dbStatusHelper.addNewStatus("Cat Food (wet)", "instock", "unchecked");

        //dbItemHelper.addNewItem("100% Pure Pumpkin", "Libby's", "Pet Supplies", "Vons", 92);
        //dbStatusHelper.addNewStatus("100% Pure Pumpkin", "instock", "unchecked");

        //dbItemHelper.addNewItem("Delectables", "Squeeze Up 20 pack", "Pet Supplies", "Vons", 93);
        //dbStatusHelper.addNewStatus("Delectables", "instock", "unchecked");

        //dbItemHelper.addNewItem("Body Wash", "Suave Mandarin", "Toiletries", "Vons", 94);
        //dbStatusHelper.addNewStatus("Body Wash", "instock", "unchecked");

        //dbItemHelper.addNewItem("Shampoo", "Suave 2 in 1", "Toiletries", "Vons", 95);
        //dbStatusHelper.addNewStatus("Shampoo", "instock", "unchecked");

        //dbItemHelper.addNewItem("Deodorant", "Old Spice", "Toiletries", "Vons", 96);
        //dbStatusHelper.addNewStatus("Deodorant", "instock", "unchecked");

        //dbItemHelper.addNewItem("Mouthwash", "Crest Whitening", "Toiletries", "Vons", 97);
        //dbStatusHelper.addNewStatus("Mouthwash", "instock", "unchecked");

        //dbItemHelper.addNewItem("Cotton Swabs", "Q-Tips", "Toiletries", "Vons", 98);
        //dbStatusHelper.addNewStatus("Cotton Swabs", "instock", "unchecked");

        //dbItemHelper.addNewItem("Febreeze Air Spray", "Heavy Duty", "Household", "Vons", 99);
        //dbStatusHelper.addNewStatus("Febreeze Air Spray", "instock", "unchecked");

        //dbItemHelper.addNewItem("All Purpose Cleaner", "Meyer's Lavender", "Household", "Vons", 100);
        //dbStatusHelper.addNewStatus("All Purpose Cleaner", "instock", "unchecked");

        //dbItemHelper.addNewItem("Laundry Detergent", "Woolite", "Household", "Vons", 101);
        //dbStatusHelper.addNewStatus("Laundry Detergent", "instock", "unchecked");

        //dbItemHelper.addNewItem("Laundry Sanitizer", "Lysol", "Household", "Vons", 102);
        //dbStatusHelper.addNewStatus("Laundry Sanitizer", "instock", "unchecked");

        //dbItemHelper.addNewItem("Aluminum Foil", "Reynolds Wrap", "Household", "Vons", 103);
        //dbStatusHelper.addNewStatus("Aluminum Foil", "instock", "unchecked");

        //dbItemHelper.addNewItem("Zip-Lock Bags (small)", "Sandwich", "Household", "Vons", 104);
        //dbStatusHelper.addNewStatus("Zip-Lock Bags (small)", "instock", "unchecked");

        //dbItemHelper.addNewItem("Zip-Lock Bags (large)", "Freezer Gallon", "Household", "Vons", 105);
        //dbStatusHelper.addNewStatus("Zip-Lock Bags (large)", "instock", "unchecked");

        //dbItemHelper.addNewItem("Saran Wrap", "Plastic Wrap", "Household", "Vons", 106);
        //dbStatusHelper.addNewStatus("Saran Wrap", "instock", "unchecked");

        //dbItemHelper.addNewItem("Scrub Sponges", "Non-Scratch", "Household", "Vons", 107);
        //dbStatusHelper.addNewStatus("Scrub Sponges", "instock", "unchecked");

        //dbItemHelper.addNewItem("Dawn Powerwash", "Dish Cleaner", "Household", "Vons", 108);
        //dbStatusHelper.addNewStatus("Dawn Powerwash", "instock", "unchecked");

        //dbItemHelper.addNewItem("Dish Soap", "Dawn Platinum", "Household", "Vons", 109);
        //dbStatusHelper.addNewStatus("Dish Soap", "instock", "unchecked");

        //dbItemHelper.addNewItem("Multivitamin", "One a Day Men's", "Supplements", "Vons", 110);
        //dbStatusHelper.addNewStatus("Multivitamin", "instock", "unchecked");

        //dbItemHelper.addNewItem("Triple Omega", "Nature Made", "Supplements", "Vons", 111);
        //dbStatusHelper.addNewStatus("Triple Omega", "instock", "unchecked");

        //dbItemHelper.addNewItem("Magnesium", "Nature Made 400mg", "Supplements", "Vons", 112);
        //dbStatusHelper.addNewStatus("Magnesium", "instock", "unchecked");

        //dbItemHelper.addNewItem("Zinc", "Sandhu Herbals 50mg", "Supplements", "Vons", 113);
        //dbStatusHelper.addNewStatus("Zinc", "instock", "unchecked");

        //dbItemHelper.addNewItem("Calcium", "Nature's Truth 1200 mg", "Supplements", "Vons", 114);
        //dbStatusHelper.addNewStatus("Calcium", "instock", "unchecked");

        //dbItemHelper.addNewItem("Biotin", "Natrol Biotin 10,000mcg", "Supplements", "Vons", 115);
        //dbStatusHelper.addNewStatus("Biotin", "instock", "unchecked");

        //dbItemHelper.addNewItem("Vitamin D3", "Nature Made 5000 IU", "Supplements", "Vons", 116);
        //dbStatusHelper.addNewStatus("Vitamin D3", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("Vons", 117);
        storeData.getStoreViewInStockMap().put("Vons", 74);
        storeData.getStoreViewNeededMap().put("Vons", 19);
        storeData.getStoreViewPausedMap().put("Vons", 24);
        dbStoreHelper.setStoreViews("Vons", 117, 74, 19, 24);


        //------------------------------------Rite Aid----------------------------------------------


        //dbItemHelper.addNewItem("Smarties", "na", "Candy", "Rite Aid", 0);
        //dbStatusHelper.addNewStatus("Smarties", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("Rite Aid", 1);
        storeData.getStoreViewInStockMap().put("Rite Aid", 1);
        storeData.getStoreViewNeededMap().put("Rite Aid", 0);
        storeData.getStoreViewPausedMap().put("Rite Aid", 0);
        dbStoreHelper.setStoreViews("Rite Aid", 1, 1, 0, 0);


        //------------------------------------Smart & Final-----------------------------------------

        //dbItemHelper.addNewItem("Churros", "Tio Pepe’s or Hola!", "Desserts", "Smart & Final", 0);
        //dbStatusHelper.addNewStatus("Churros", "paused", "unchecked");

        //dbItemHelper.addNewItem("Fun Dip Sticks", "na", "Candy", "Smart & Final", 1);
        //dbStatusHelper.addNewStatus("Fun Dip Sticks", "paused", "unchecked");

        //dbItemHelper.addNewItem("Orange Tic Tacs", "na", "Candy", "Smart & Final", 2);
        //dbStatusHelper.addNewStatus("Orange Tic Tacs", "needed", "unchecked");


        storeData.getStoreViewAllMap().put("Smart & Final", 3);
        storeData.getStoreViewInStockMap().put("Smart & Final", 0);
        storeData.getStoreViewNeededMap().put("Smart & Final", 1);
        storeData.getStoreViewPausedMap().put("Smart & Final", 2);
        dbStoreHelper.setStoreViews("Smart & Final", 3, 0, 1, 2);


        //------------------------------------Costco------------------------------------------------

        //dbItemHelper.addNewItem("Soda Cans", "Pepsi or Coke", "Drinks", "Costco", 0);
        //dbStatusHelper.addNewStatus("Soda Cans", "instock", "unchecked");

        //dbItemHelper.addNewItem("Kitty Liter", "Scoop Away Complete", "Pet Supplies", "Costco", 1);
        //dbStatusHelper.addNewStatus("Kitty Liter", "needed", "unchecked");

        //dbItemHelper.addNewItem("Chicken Broth", "Kirkland Organic", "Pet Supplies", "Costco", 2);
        //dbStatusHelper.addNewStatus("Chicken Broth", "instock", "unchecked");

        //dbItemHelper.addNewItem("Frozen Vegetables", "Kirkland Organic", "Pet Supplies", "Costco", 3);
        //dbStatusHelper.addNewStatus("Frozen Vegetables", "instock", "unchecked");

        //dbItemHelper.addNewItem("Mashed Potatoes", "Main St. Bistro", "Pet Supplies", "Costco", 4);
        //dbStatusHelper.addNewStatus("Mashed Potatoes", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("Costco", 5);
        storeData.getStoreViewInStockMap().put("Costco", 4);
        storeData.getStoreViewNeededMap().put("Costco", 1);
        storeData.getStoreViewPausedMap().put("Costco", 0);
        dbStoreHelper.setStoreViews("Costco", 5, 4, 1, 0);


        //------------------------------------Walmart-----------------------------------------------

        //dbItemHelper.addNewItem("Dark Chocolate Caramels", "Ghiradelli", "Candy", "Walmart", 0);
        //dbStatusHelper.addNewStatus("Dark Chocolate Caramels", "instock", "unchecked");

        //dbItemHelper.addNewItem("Dishwashing Brush", "Great Value", "Household", "Walmart", 1);
        //dbStatusHelper.addNewStatus("Dishwashing Brush", "instock", "unchecked");

        //dbItemHelper.addNewItem("Small Trash Bags", "13 gallon", "Household", "Walmart", 2);
        //dbStatusHelper.addNewStatus("Small Trash Bags", "instock", "unchecked");

        //dbItemHelper.addNewItem("Large Trash Bags", "33 gallon", "Household", "Walmart", 3);
        //dbStatusHelper.addNewStatus("Large Trash Bags", "instock", "unchecked");

        //dbItemHelper.addNewItem("Compactor Bags", "18 gallon", "Household", "Walmart", 4);
        //dbStatusHelper.addNewStatus("Compactor Bags", "instock", "unchecked");

        //dbItemHelper.addNewItem("Paper Towels", "Sparkle", "Household", "Walmart", 5);
        //dbStatusHelper.addNewStatus("Paper Towels", "instock", "unchecked");

        //dbItemHelper.addNewItem("Toilet Paper", "Angel Soft", "Household", "Walmart", 6);
        //dbStatusHelper.addNewStatus("Toilet Paper", "instock", "unchecked");

        //dbItemHelper.addNewItem("72% Intense Dark Chocolate", "Ghiradelli", "Candy", "Walmart", 7);
        //dbStatusHelper.addNewStatus("72% Intense Dark Chocolate", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("Walmart", 8);
        storeData.getStoreViewInStockMap().put("Walmart", 8);
        storeData.getStoreViewNeededMap().put("Walmart", 0);
        storeData.getStoreViewPausedMap().put("Walmart", 0);
        dbStoreHelper.setStoreViews("Walmart", 8, 8, 0, 0);


        //------------------------------------Amazon------------------------------------------------

        //dbItemHelper.addNewItem("Muddy Buddies", "Brownie Supreme", "Snacks", "Amazon", 0);
        //dbStatusHelper.addNewStatus("Muddy Buddies", "instock", "unchecked");

        //dbItemHelper.addNewItem("Gingerbread Cookie Mix", "Betty Crocker", "Desserts", "Amazon", 1);
        //dbStatusHelper.addNewStatus("Gingerbread Cookie Mix", "paused", "unchecked");

        //dbItemHelper.addNewItem("Jelly Beans", "Sizzling Cinnamon", "Candy", "Amazon", 2);
        //dbStatusHelper.addNewStatus("Jelly Beans", "paused", "unchecked");

        //dbItemHelper.addNewItem("Orange Pez", "na", "Candy", "Amazon", 3);
        //dbStatusHelper.addNewStatus("Orange Pez", "needed", "unchecked");

        //dbItemHelper.addNewItem("Vanilla Taffy", "na", "Candy", "Amazon", 4);
        //dbStatusHelper.addNewStatus("Vanilla Taffy", "needed", "unchecked");

        //dbItemHelper.addNewItem("Vanilla Tootsie Rolls", "na", "Candy", "Amazon", 5);
        //dbStatusHelper.addNewStatus("Vanilla Tootsie Rolls", "instock", "unchecked");

        //dbItemHelper.addNewItem("Poop Bags", "Amazon Basics", "Pet Supplies", "Amazon", 6);
        //dbStatusHelper.addNewStatus("Poop Bags", "instock", "unchecked");

        //dbItemHelper.addNewItem("Nitrile Gloves", "GMG 100 pack", "Pet Supplies", "Amazon", 7);
        //dbStatusHelper.addNewStatus("Nitrile Gloves", "instock", "unchecked");

        //dbItemHelper.addNewItem("Toothpaste", "Tom's Antiplaque & Whitening", "Toiletries", "Amazon", 8);
        //dbStatusHelper.addNewStatus("Toothpaste", "instock", "unchecked");

        //dbItemHelper.addNewItem("Floss", "Reach Mint Waxed", "Toiletries", "Amazon", 9);
        //dbStatusHelper.addNewStatus("Floss", "instock", "unchecked");

        //dbItemHelper.addNewItem("Shaving Cream", "Sandalwood", "Toiletries", "Amazon", 10);
        //dbStatusHelper.addNewStatus("Shaving Cream", "instock", "unchecked");

        //dbItemHelper.addNewItem("Shaving Razors", "Gillette ProGlide", "Toiletries", "Amazon", 11);
        //dbStatusHelper.addNewStatus("Shaving Razors", "instock", "unchecked");

        //dbItemHelper.addNewItem("Sunscreen", "Hawaiian Tropic Sheer 50spf", "Toiletries", "Amazon", 12);
        //dbStatusHelper.addNewStatus("Sunscreen", "instock", "unchecked");

        //dbItemHelper.addNewItem("Pet Stain Cleaner", "Rocco & Roxie", "Household", "Amazon", 13);
        //dbStatusHelper.addNewStatus("Pet Stain Cleaner", "instock", "unchecked");

        //dbItemHelper.addNewItem("Night Light Bulbs", "C7 E12", "Household", "Amazon", 14);
        //dbStatusHelper.addNewStatus("Night Light Bulbs", "instock", "unchecked");

        //dbItemHelper.addNewItem("Vitamin C", "Amazon Elements 1000 mg", "Supplements", "Amazon", 15);
        //dbStatusHelper.addNewStatus("Vitamin C", "instock", "unchecked");

        //dbItemHelper.addNewItem("Fortune Cookies", "to do", "Snacks", "Amazon", 16);
        //dbStatusHelper.addNewStatus("Fortune Cookies", "instock", "unchecked");

        //dbItemHelper.addNewItem("Communion Wafers", "to do", "Snacks", "Amazon", 17);
        //dbStatusHelper.addNewStatus("Communion Wafers", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("Amazon", 18);
        storeData.getStoreViewInStockMap().put("Amazon", 14);
        storeData.getStoreViewNeededMap().put("Amazon", 2);
        storeData.getStoreViewPausedMap().put("Amazon", 2);
        dbStoreHelper.setStoreViews("Amazon", 18, 14, 2, 2);


        //------------------------------------Stater Bros-------------------------------------------

        //dbItemHelper.addNewItem("Beef Noodles", "Yakisoba", "Soups", "Stater Bros", 0);
        //dbStatusHelper.addNewStatus("Beef Noodles", "paused", "unchecked");

        //dbItemHelper.addNewItem("Choc. Chip Cookie Mix", "Gluten Free", "Desserts", "Stater Bros", 1);
        //dbStatusHelper.addNewStatus("Choc. Chip Cookie Mix", "paused", "unchecked");

        //dbItemHelper.addNewItem("Choc. Malt Mix", "Nestle", "Desserts", "Stater Bros", 2);
        //dbStatusHelper.addNewStatus("Choc. Malt Mix", "instock", "unchecked");

        //dbItemHelper.addNewItem("Choc. Fudge Pudding", "Snack Pack", "Snacks", "Stater Bros", 3);
        //dbStatusHelper.addNewStatus("Choc. Fudge Pudding", "paused", "unchecked");

        //dbItemHelper.addNewItem("Dryer Sheets", "Simply Done Fresh Linen", "Household", "Stater Bros", 4);
        //dbStatusHelper.addNewStatus("Dryer Sheets", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("Stater Bros", 5);
        storeData.getStoreViewInStockMap().put("Stater Bros", 2);
        storeData.getStoreViewNeededMap().put("Stater Bros", 0);
        storeData.getStoreViewPausedMap().put("Stater Bros", 3);
        dbStoreHelper.setStoreViews("Stater Bros", 5, 2, 0, 3);


        //------------------------------------CVS---------------------------------------------------

        //dbItemHelper.addNewItem("Dark Chocolate Pretzels", "Flipz", "Snacks", "CVS", 0);
        //dbStatusHelper.addNewStatus("Dark Chocolate Pretzels", "instock", "unchecked");

        //dbItemHelper.addNewItem("Candy Corn", "Brach's", "Candy", "CVS", 1);
        //dbStatusHelper.addNewStatus("Candy Corn", "instock", "unchecked");

        //dbItemHelper.addNewItem("Rubbing Alcohol", "Isopropyl", "Household", "CVS", 2);
        //dbStatusHelper.addNewStatus("Rubbing Alcohol", "instock", "unchecked");

        //dbItemHelper.addNewItem("Hydrogen Peroxide", "na", "Household", "CVS", 3);
        //dbStatusHelper.addNewStatus("Hydrogen Peroxide", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("CVS", 4);
        storeData.getStoreViewInStockMap().put("CVS", 4);
        storeData.getStoreViewNeededMap().put("CVS", 0);
        storeData.getStoreViewPausedMap().put("CVS", 0);
        dbStoreHelper.setStoreViews("CVS", 4, 4, 0, 0);


        //------------------------------------Dollar Tree-------------------------------------------

        //dbItemHelper.addNewItem("Sno Caps", "na", "Candy", "Dollar Tree", 0);
        //dbStatusHelper.addNewStatus("Sno Caps", "instock", "unchecked");

        //dbItemHelper.addNewItem("Hand Soap", "Lavender & Chamomile", "Toiletries", "Dollar Tree", 1);
        //dbStatusHelper.addNewStatus("Hand Soap", "instock", "unchecked");

        //dbItemHelper.addNewItem("Ramen Noodles", "Nissin", "Soups", "Dollar Tree", 2);
        //dbStatusHelper.addNewStatus("Ramen Noodles", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("Dollar Tree", 3);
        storeData.getStoreViewInStockMap().put("Dollar Tree", 3);
        storeData.getStoreViewNeededMap().put("Dollar Tree", 0);
        storeData.getStoreViewPausedMap().put("Dollar Tree", 0);
        dbStoreHelper.setStoreViews("Dollar Tree", 3, 3, 0, 0);


        //------------------------------------Ralphs------------------------------------------------

        //dbItemHelper.addNewItem("Clarified Butter", "Challenge", "Eggs/Dairy", "Ralphs", 0);
        //dbStatusHelper.addNewStatus("Clarified Butter", "paused", "unchecked");


        storeData.getStoreViewAllMap().put("Ralphs", 1);
        storeData.getStoreViewInStockMap().put("Ralphs", 0);
        storeData.getStoreViewNeededMap().put("Ralphs", 0);
        storeData.getStoreViewPausedMap().put("Ralphs", 1);
        dbStoreHelper.setStoreViews("Ralphs", 1, 0, 0, 1);


        //------------------------------------Target------------------------------------------------

        //dbItemHelper.addNewItem("Chocolate Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 0);
        //dbStatusHelper.addNewStatus("Chocolate Syrup", "needed", "unchecked");

        //dbItemHelper.addNewItem("Caramel Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 1);
        //dbStatusHelper.addNewStatus("Caramel Syrup", "needed", "unchecked");


        storeData.getStoreViewAllMap().put("Target", 2);
        storeData.getStoreViewInStockMap().put("Target", 0);
        storeData.getStoreViewNeededMap().put("Target", 2);
        storeData.getStoreViewPausedMap().put("Target", 0);
        dbStoreHelper.setStoreViews("Target", 2, 0, 2, 0);


        //------------------------------------Pet Supplies Plus-------------------------------------

        //dbItemHelper.addNewItem("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 0);
        //dbStatusHelper.addNewStatus("Cat Food (dry)", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("Pet Supplies Plus", 1);
        storeData.getStoreViewInStockMap().put("Pet Supplies Plus", 1);
        storeData.getStoreViewNeededMap().put("Pet Supplies Plus", 0);
        storeData.getStoreViewPausedMap().put("Pet Supplies Plus", 0);
        dbStoreHelper.setStoreViews("Pet Supplies Plus", 1, 1, 0, 0);


        //------------------------------------Sprouts-------------------------------------------

        //dbItemHelper.addNewItem("Caramel Squares", "na", "Candy", "Sprouts", 0);
        //dbStatusHelper.addNewStatus("Caramel Squares", "paused", "unchecked");

        //dbItemHelper.addNewItem("Bar Soap", "Zum Bar Sea Salt", "Toiletries", "Sprouts", 1);
        //dbStatusHelper.addNewStatus("Bar Soap", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("Sprouts", 2);
        storeData.getStoreViewInStockMap().put("Sprouts", 1);
        storeData.getStoreViewNeededMap().put("Sprouts", 0);
        storeData.getStoreViewPausedMap().put("Sprouts", 1);
        dbStoreHelper.setStoreViews("Sprouts", 2, 1, 0, 1);


        //------------------------------------Sam's Club--------------------------------------------

        //dbItemHelper.addNewItem("Quick Steak", "Gary's", "Meat", "Sam's Club", 0);
        //dbStatusHelper.addNewStatus("Quick Steak", "needed", "unchecked");

        //dbItemHelper.addNewItem("Paper Plates", "to do", "Household", "Sam's Club", 1);
        //dbStatusHelper.addNewStatus("Paper Plates", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("Sam's Club", 2);
        storeData.getStoreViewInStockMap().put("Sam's Club", 1);
        storeData.getStoreViewNeededMap().put("Sam's Club", 1);
        storeData.getStoreViewPausedMap().put("Sam's Club", 0);
        dbStoreHelper.setStoreViews("Sam's Club", 2, 1, 1, 0);


        //---------------------------------------Staples--------------------------------------------

        //dbItemHelper.addNewItem("Multipurpose Paper", "Tru Red 20/96", "Household", "Staples", 0);
        //dbStatusHelper.addNewStatus("Multipurpose Paper", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("Staples", 1);
        storeData.getStoreViewInStockMap().put("Staples", 1);
        storeData.getStoreViewNeededMap().put("Staples", 0);
        storeData.getStoreViewPausedMap().put("Staples", 0);
        dbStoreHelper.setStoreViews("Staples", 1, 1, 0, 0);


        //------------------------------------Yorba Linda Feed Store--------------------------------

        //dbItemHelper.addNewItem("Dog Food (dry)", "Canidae All Life Stages", "Pet Supplies", "Yorba Linda Feed Store", 0);
        //dbStatusHelper.addNewStatus("Dog Food (dry)", "instock", "unchecked");


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