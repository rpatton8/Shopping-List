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

    private Button fullInventory;
    private Button shopByStore;

    private ItemData itemData;
    private StatusData statusData;
    private CategoryData categoryData;
    private StoreData storeData;
    public DBHelper dbHelper;
    public DBStatusHelper dbStatusHelper;
    public DBCategoryHelper dbCategoryHelper;
    public DBStoreHelper dbStoreHelper;

    public Boolean itemIsSelectedInInventory;
    public Boolean itemIsSelectedInShopByStore;
    public Item selectedItemInInventory;
    public Item selectedItemInShopByStore;
    public int selectedItemPositionInInventory;
    public int selectedItemPositionInShopByStore;
    public ArrayList<Boolean> itemIsClickedInInventory;
    public ArrayList<Boolean> itemIsClickedInShopByStore;
    public ArrayList<Boolean> itemIsChecked;
    public int storeNum;
    public String reorderItemsCategory;
    public Boolean editItemInInventory;
    public Boolean editItemInShopByStore;

    public String inventoryView;
    public static final String INVENTORY_ALL = "view all";
    public static final String INVENTORY_INSTOCK = "view instock";
    public static final String INVENTORY_NEEDED = "view store";
    public static final String INVENTORY_PAUSED = "view paused";

    public Parcelable reorderCategoriesViewState;
    public Parcelable reorderStoresViewState;
    public Parcelable reorderItemsViewState;
    public Parcelable shopByStoreViewState;
    public Parcelable fullInventoryViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping);

        dbHelper = new DBHelper(this);
        dbStatusHelper = new DBStatusHelper(this);
        dbCategoryHelper = new DBCategoryHelper(this);
        dbStoreHelper = new DBStoreHelper(this);
        itemData = dbHelper.readItemData();
        statusData = dbStatusHelper.readStatusData();
        itemData.updateStatuses(statusData);
        categoryData = dbCategoryHelper.readCategoryData();
        storeData = dbStoreHelper.readStoreData();

        itemIsSelectedInInventory = false;
        itemIsSelectedInShopByStore = false;
        selectedItemInInventory = null;
        selectedItemInShopByStore = null;
        selectedItemPositionInInventory = 0;
        selectedItemPositionInShopByStore = 0;
        itemIsClickedInInventory = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i++) {
            itemIsClickedInInventory.add(false);
        }
        itemIsClickedInShopByStore = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i++) {
            itemIsClickedInShopByStore.add(false);
        }
        itemIsChecked = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i++) {
            itemIsChecked.add(false);
        }

        storeNum = 0;
        reorderItemsCategory = "";
        editItemInInventory = false;
        editItemInShopByStore = false;

        fullInventory = findViewById(R.id.fullInventoryTopMenu);
        fullInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getFragmentManager().findFragmentById(R.id.fragments);
                if (f instanceof FullInventory) return;
                //fullInventoryViewState = null;
                loadFragment(new FullInventory());
            }
        });

        shopByStore = findViewById(R.id.byStoreTopMenu);
        shopByStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getFragmentManager().findFragmentById(R.id.fragments);
                if (f instanceof ShopByStore) return;
                //shopByStoreViewState = null;
                loadFragment(new ShopByStore());
            }
        });

    }

    public ItemData getItemData() {
        return itemData;
    }


    public void updateItemData() {
        itemData = dbHelper.readItemData();
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

    public ArrayList<Boolean> getClickedShopByStoreList() {
        return itemIsClickedInShopByStore;
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
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.commit();
    }

    public void clearAllData() {
        dbHelper.deleteDatabase();
        dbStatusHelper.deleteDatabase();
        dbCategoryHelper.deleteDatabase();
        dbStoreHelper.deleteDatabase();
        dbHelper = new DBHelper(this);
        dbStatusHelper = new DBStatusHelper(this);
        dbCategoryHelper = new DBCategoryHelper(this);
        dbStoreHelper = new DBStoreHelper(this);
        itemData = dbHelper.readItemData();
        statusData = dbStatusHelper.readStatusData();
        itemData.updateStatuses(statusData);
        categoryData = dbCategoryHelper.readCategoryData();
        storeData = dbStoreHelper.readStoreData();
        itemIsSelectedInInventory = false;
        itemIsSelectedInShopByStore = false;
        itemIsClickedInInventory = new ArrayList<>();
        itemIsClickedInShopByStore = new ArrayList<>();
        itemIsChecked = new ArrayList<>();
        storeNum = 0;
        reorderItemsCategory = "";
        editItemInInventory = false;
        editItemInShopByStore = false;
    }

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
        dbStoreHelper.addNewStore("Yorba Linda Feed Store", 14);

        dbStoreHelper.setStoreItemsNeeded("Vons", 18);
        dbStoreHelper.setStoreItemsNeeded("Rite Aid", 1);
        dbStoreHelper.setStoreItemsNeeded("Smart & Final", 1);
        dbStoreHelper.setStoreItemsNeeded("Costco", 1);
        dbStoreHelper.setStoreItemsNeeded("Walmart", 0);
        dbStoreHelper.setStoreItemsNeeded("Amazon", 2);
        dbStoreHelper.setStoreItemsNeeded("Stater Bros", 0);
        dbStoreHelper.setStoreItemsNeeded("CVS", 0);
        dbStoreHelper.setStoreItemsNeeded("Dollar Tree", 1);
        dbStoreHelper.setStoreItemsNeeded("Ralphs", 0);
        dbStoreHelper.setStoreItemsNeeded("Target", 2);
        dbStoreHelper.setStoreItemsNeeded("Pet Supplies Plus", 0);
        dbStoreHelper.setStoreItemsNeeded("Sprouts", 0);
        dbStoreHelper.setStoreItemsNeeded("Sam's Club", 1);
        dbStoreHelper.setStoreItemsNeeded("Yorba Linda Feed Store", 0);
        //total items needed = 27

        storeData.getStoreItemsNeededMap().put("Vons", 18);
        storeData.getStoreItemsNeededMap().put("Rite Aid", 1);
        storeData.getStoreItemsNeededMap().put("Smart & Final", 1);
        storeData.getStoreItemsNeededMap().put("Costco", 1);
        storeData.getStoreItemsNeededMap().put("Walmart", 0);
        storeData.getStoreItemsNeededMap().put("Amazon", 2);
        storeData.getStoreItemsNeededMap().put("Stater Bros", 0);
        storeData.getStoreItemsNeededMap().put("CVS", 0);
        storeData.getStoreItemsNeededMap().put("Dollar Tree", 1);
        storeData.getStoreItemsNeededMap().put("Ralphs", 0);
        storeData.getStoreItemsNeededMap().put("Target", 2);
        storeData.getStoreItemsNeededMap().put("Pet Supplies Plus", 0);
        storeData.getStoreItemsNeededMap().put("Sprouts", 0);
        storeData.getStoreItemsNeededMap().put("Sam's Club", 1);
        storeData.getStoreItemsNeededMap().put("Yorba Linda Feed Store", 0);

        //------------------------------------Meals-------------------------------------------------

        dbHelper.addNewItem("Sausage Biscuits", "Jimmy Dean Frozen", "Meals", "Vons", 0);
        dbStatusHelper.addNewStatus("Sausage Biscuits", "true", "false", "false");

        dbHelper.addNewItem("Hamburger Helper", "Cheeseburger Macaroni", "Meals", "Vons", 1);
        dbStatusHelper.addNewStatus("Hamburger Helper", "false", "false", "true");

        dbHelper.addNewItem("Buffalo Chicken Bites", "TGIF or Frank's", "Meals", "Vons", 2);
        dbStatusHelper.addNewStatus("Buffalo Chicken Bites", "false", "false", "true");

        dbHelper.addNewItem("Terriyaki Chicken Bites", "InnovAsian", "Meals", "Vons", 3);
        dbStatusHelper.addNewStatus("Terriyaki Chicken Bites", "false", "false", "true");

        dbHelper.addNewItem("TGIF Cheese Sticks", "TGIF (small 10pc)", "Meals", "Vons", 4);
        dbStatusHelper.addNewStatus("TGIF Cheese Sticks", "false", "false", "true");

        dbHelper.addNewItem("Frozen Pizza", "Thin Pepperoni", "Meals", "Vons", 5);
        dbStatusHelper.addNewStatus("Frozen Pizza", "false", "true", "false");

        dbHelper.addNewItem("Corn Dogs", "Foster Farms", "Meals", "Vons", 6);
        dbStatusHelper.addNewStatus("Corn Dogs", "false", "true", "false");

        dbHelper.addNewItem("Hot Dogs", "Bun Size", "Meals", "Vons", 7);
        dbStatusHelper.addNewStatus("Hot Dogs", "false", "true", "false");

        dbHelper.addNewItem("Hot Dog Buns", "(8 pack)", "Meals", "Vons", 8);
        dbStatusHelper.addNewStatus("Hot Dog Buns", "false", "true", "false");

        dbHelper.addNewItem("Hamburger Patties", "to do", "Meals", "Vons", 9);
        dbStatusHelper.addNewStatus("Hamburger Patties", "false", "false", "true");

        dbHelper.addNewItem("Hamburger Buns", "(8 pack)", "Meals", "Vons", 10);
        dbStatusHelper.addNewStatus("Hamburger Buns", "false", "false", "true");

        dbHelper.addNewItem("Fetuccini Alfredo", "Pasta Roni", "Meals", "Vons", 11);
        dbStatusHelper.addNewStatus("Fetuccini Alfredo", "true", "false", "false");

        dbHelper.addNewItem("Mac & Cheese", "Annie’s", "Meals", "Vons", 12);
        dbStatusHelper.addNewStatus("Mac & Cheese", "true", "false", "false");

        dbHelper.addNewItem("Gnocci", "Signature Select", "Meals", "Vons", 13);
        dbStatusHelper.addNewStatus("Gnocci", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Meals", 14);
        categoryData.getCategoryViewInStockMap().put("Meals", 4);
        categoryData.getCategoryViewNeededMap().put("Meals", 4);
        categoryData.getCategoryViewPausedMap().put("Meals", 6);
        dbCategoryHelper.setCategoryViews("Meals", 14,4,4,6);


        //------------------------------------Soups-------------------------------------------------

        dbHelper.addNewItem("Spaghetti O's", "w/ Meatballs", "Soups", "Vons", 0);
        dbStatusHelper.addNewStatus("Spaghetti O's", "true", "false", "false");

        dbHelper.addNewItem("Chicken Noodle Soup", "Campbell's", "Soups", "Vons", 1);
        dbStatusHelper.addNewStatus("Chicken Noodle Soup", "true", "false", "false");

        dbHelper.addNewItem("Minestrone Soup", "Amy's", "Soups", "Vons", 2);
        dbStatusHelper.addNewStatus("Minestrone Soup", "false", "true", "false");

        dbHelper.addNewItem("Vegetable Barley Soup", "Amy's", "Soups", "Vons", 3);
        dbStatusHelper.addNewStatus("Vegetable Barley Soup", "false", "true", "false");

        dbHelper.addNewItem("Beef Noodles", "Yakisoba", "Soups", "Stater Bros", 4);
        dbStatusHelper.addNewStatus("Beef Noodles", "false", "false", "true");

        dbHelper.addNewItem("Cup of Noodles", "Nissin", "Soups", "Vons", 5);
        dbStatusHelper.addNewStatus("Cup of Noodles", "false", "false", "true");


        categoryData.getCategoryViewAllMap().put("Soups", 6);
        categoryData.getCategoryViewInStockMap().put("Soups", 2);
        categoryData.getCategoryViewNeededMap().put("Soups", 2);
        categoryData.getCategoryViewPausedMap().put("Soups", 2);
        dbCategoryHelper.setCategoryViews("Soups", 6,2,2,2);


        //------------------------------------Sides-------------------------------------------------

        dbHelper.addNewItem("Fast Food Fries", "Ore-Ida", "Sides", "Vons", 0);
        dbStatusHelper.addNewStatus("Fast Food Fries", "false", "true", "false");

        dbHelper.addNewItem("Texas Cheesy Bread", "New York Bakery", "Sides", "Vons", 1);
        dbStatusHelper.addNewStatus("Texas Cheesy Bread", "true", "false", "false");

        dbHelper.addNewItem("Chicken Rice", "Knorr", "Sides", "Vons", 2);
        dbStatusHelper.addNewStatus("Chicken Rice", "true", "false", "false");

        dbHelper.addNewItem("Canned Corn", "Del Monte", "Sides", "Vons", 3);
        dbStatusHelper.addNewStatus("Canned Corn", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Sides", 4);
        categoryData.getCategoryViewInStockMap().put("Sides", 3);
        categoryData.getCategoryViewNeededMap().put("Sides", 1);
        categoryData.getCategoryViewPausedMap().put("Sides", 0);
        dbCategoryHelper.setCategoryViews("Sides", 4,3,1,0);


        //------------------------------------Meat--------------------------------------------------

        dbHelper.addNewItem("Steak", "USDA", "Meat", "Vons", 0);
        dbStatusHelper.addNewStatus("Steak", "false", "false", "true");

        dbHelper.addNewItem("Ground Beef", "(1 pound)", "Meat", "Vons", 1);
        dbStatusHelper.addNewStatus("Ground Beef", "false", "false", "true");

        dbHelper.addNewItem("Meatballs", "Homestyle", "Meat", "Vons", 2);
        dbStatusHelper.addNewStatus("Meatballs", "false", "false", "true");

        dbHelper.addNewItem("Pepperoni", "Hormel", "Meat", "Vons", 3);
        dbStatusHelper.addNewStatus("Pepperoni", "true", "false", "false");

        dbHelper.addNewItem("Quick Steak", "Gary's", "Meat", "Sam's Club", 4);
        dbStatusHelper.addNewStatus("Quick Steak", "false", "true", "false");

        dbHelper.addNewItem("Chicken Breast", "na", "Meat", "Vons", 5);
        dbStatusHelper.addNewStatus("Chicken Breast", "false", "false", "true");

        dbHelper.addNewItem("Sliced Turkey", "to do", "Meat", "Vons", 6);
        dbStatusHelper.addNewStatus("Sliced Turkey", "false", "false", "true");

        dbHelper.addNewItem("Sliced Ham", "to do", "Meat", "Vons", 7);
        dbStatusHelper.addNewStatus("Sliced Ham", "false", "false", "true");

        dbHelper.addNewItem("Ham Steak", "to do", "Meat", "Vons", 8);
        dbStatusHelper.addNewStatus("Ham Steak", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Meat", 9);
        categoryData.getCategoryViewInStockMap().put("Meat", 2);
        categoryData.getCategoryViewNeededMap().put("Meat", 1);
        categoryData.getCategoryViewPausedMap().put("Meat", 6);
        dbCategoryHelper.setCategoryViews("Meat", 9,2,1,6);


        //------------------------------------Bread/Grains/Cereal-----------------------------------

        dbHelper.addNewItem("Thin Spaghetti", "Barilla Whole Grain", "Bread/Grains/Cereal", "Vons", 0);
        dbStatusHelper.addNewStatus("Thin Spaghetti", "true", "false", "false");

        dbHelper.addNewItem("Spiral Pasta", "Barilla Rotini", "Bread/Grains/Cereal", "Vons", 1);
        dbStatusHelper.addNewStatus("Spiral Pasta", "true", "false", "false");

        dbHelper.addNewItem("Wheat Bread", "Nature's Own", "Bread/Grains/Cereal", "Vons", 2);
        dbStatusHelper.addNewStatus("Wheat Bread", "true", "false", "false");

        dbHelper.addNewItem("Baguette", "French", "Bread/Grains/Cereal", "Vons", 3);
        dbStatusHelper.addNewStatus("Baguette", "false", "true", "false");

        dbHelper.addNewItem("Sourdough Bread", "San Luis Sourdough", "Bread/Grains/Cereal", "Vons", 4);
        dbStatusHelper.addNewStatus("Sourdough Bread", "true", "false", "false");

        dbHelper.addNewItem("Thomas Muffins", "Original", "Bread/Grains/Cereal", "Vons", 5);
        dbStatusHelper.addNewStatus("Thomas Muffins", "false", "true", "false");

        dbHelper.addNewItem("RP Cereal", "Reese's Puffs", "Bread/Grains/Cereal", "Vons", 6);
        dbStatusHelper.addNewStatus("RP Cereal", "true", "false", "false");

        dbHelper.addNewItem("CC Cereal", "Cookie Crisp", "Bread/Grains/Cereal", "Vons", 7);
        dbStatusHelper.addNewStatus("CC Cereal", "true", "false", "false");

        dbHelper.addNewItem("FMW Cereal", "Frosted Mini Wheat", "Bread/Grains/Cereal", "Vons", 8);
        dbStatusHelper.addNewStatus("FMW Cereal", "true", "false", "false");

        dbHelper.addNewItem("Eggo Waffles", "Homestyle", "Bread/Grains/Cereal", "Vons", 9);
        dbStatusHelper.addNewStatus("Eggo Waffles", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Bread/Grains/Cereal", 10);
        categoryData.getCategoryViewInStockMap().put("Bread/Grains/Cereal", 8);
        categoryData.getCategoryViewNeededMap().put("Bread/Grains/Cereal", 2);
        categoryData.getCategoryViewPausedMap().put("Bread/Grains/Cereal", 0);
        dbCategoryHelper.setCategoryViews("Bread/Grains/Cereal", 10,8,2,0);


        //------------------------------------Eggs/Dairy--------------------------------------------

        dbHelper.addNewItem("Milk", "Vitamin D", "Eggs/Dairy", "Vons", 0);
        dbStatusHelper.addNewStatus("Milk", "false", "true", "false");

        dbHelper.addNewItem("Eggs", "Grade AA", "Eggs/Dairy", "Vons", 1);
        dbStatusHelper.addNewStatus("Eggs", "false", "true", "false");

        dbHelper.addNewItem("Honey Yogurt", "Greek Gods", "Eggs/Dairy", "Vons", 2);
        dbStatusHelper.addNewStatus("Honey Yogurt", "false", "false", "true");

        dbHelper.addNewItem("Salted Butter", "Challenge", "Eggs/Dairy", "Vons", 3);
        dbStatusHelper.addNewStatus("Salted Butter", "true", "false", "false");

        dbHelper.addNewItem("Clarified Butter", "Challenge", "Eggs/Dairy", "Ralphs", 4);
        dbStatusHelper.addNewStatus("Clarified Butter", "false", "false", "true");

        dbHelper.addNewItem("Shredded Cheese", "Mexican Blend", "Eggs/Dairy", "Vons", 5);
        dbStatusHelper.addNewStatus("Shredded Cheese", "true", "false", "false");

        dbHelper.addNewItem("String Cheese", "Mozarella", "Eggs/Dairy", "Vons", 6);
        dbStatusHelper.addNewStatus("String Cheese", "false", "true", "false");

        dbHelper.addNewItem("BD Cheese", "Black Diamond", "Eggs/Dairy", "Vons", 7);
        dbStatusHelper.addNewStatus("BD Cheese", "false", "false", "true");

        dbHelper.addNewItem("Non-Stick Spray", "Pam Original", "Eggs/Dairy", "Vons", 8);
        dbStatusHelper.addNewStatus("Non-Stick Spray", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Eggs/Dairy", 9);
        categoryData.getCategoryViewInStockMap().put("Eggs/Dairy", 3);
        categoryData.getCategoryViewNeededMap().put("Eggs/Dairy", 3);
        categoryData.getCategoryViewPausedMap().put("Eggs/Dairy", 3);
        dbCategoryHelper.setCategoryViews("Eggs/Dairy", 9,3,3,3);


        //------------------------------------Condiments--------------------------------------------

        dbHelper.addNewItem("Parmesan Cheese", "Kraft", "Condiments", "Vons", 0);
        dbStatusHelper.addNewStatus("Parmesan Cheese", "true", "false", "false");

        dbHelper.addNewItem("A1 Sauce", "Original", "Condiments", "Vons", 1);
        dbStatusHelper.addNewStatus("A1 Sauce", "true", "false", "false");

        dbHelper.addNewItem("Ketchup", "Heinz", "Condiments", "Vons", 2);
        dbStatusHelper.addNewStatus("Ketchup", "true", "false", "false");

        dbHelper.addNewItem("Mustard", "Heinz", "Condiments", "Vons", 3);
        dbStatusHelper.addNewStatus("Mustard", "true", "false", "false");

        dbHelper.addNewItem("Pasta Sauce", "Ragu Meat", "Condiments", "Vons", 4);
        dbStatusHelper.addNewStatus("Pasta Sauce", "true", "false", "false");

        dbHelper.addNewItem("Chocolate Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 5);
        dbStatusHelper.addNewStatus("Chocolate Syrup", "false", "true", "false");

        dbHelper.addNewItem("Caramel Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 6);
        dbStatusHelper.addNewStatus("Caramel Syrup", "false", "true", "false");

        dbHelper.addNewItem("Maple Syrup", "Pearl Milling", "Condiments", "Vons", 7);
        dbStatusHelper.addNewStatus("Maple Syrup", "true", "false", "false");

        dbHelper.addNewItem("Honey", "Local Hive Clover", "Condiments", "Vons", 8);
        dbStatusHelper.addNewStatus("Honey", "true", "false", "false");

        dbHelper.addNewItem("Peanut Butter", "Skippy Creamy", "Condiments", "Vons", 9);
        dbStatusHelper.addNewStatus("Peanut Butter", "true", "false", "false");

        dbHelper.addNewItem("Soy Sauce", "Kikoman", "Condiments", "Vons", 10);
        dbStatusHelper.addNewStatus("Soy Sauce", "true", "false", "false");

        dbHelper.addNewItem("Brown Sugar", "(for hot chocolate)", "Condiments", "Vons", 11);
        dbStatusHelper.addNewStatus("Brown Sugar", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Condiments", 12);
        categoryData.getCategoryViewInStockMap().put("Condiments", 10);
        categoryData.getCategoryViewNeededMap().put("Condiments", 2);
        categoryData.getCategoryViewPausedMap().put("Condiments", 0);
        dbCategoryHelper.setCategoryViews("Condiments", 12,10,2,0);


        //------------------------------------Seasonings--------------------------------------------

        dbHelper.addNewItem("Salt & Pepeper", "na", "Seasonings", "Vons", 0);
        dbStatusHelper.addNewStatus("Salt & Pepeper", "true", "false", "false");

        dbHelper.addNewItem("Garlic Salt", "Lawry's", "Seasonings", "Vons", 1);
        dbStatusHelper.addNewStatus("Garlic Salt", "true", "false", "false");

        dbHelper.addNewItem("Lawry's Seasoning Salt", "Lawry's", "Seasonings", "Vons", 2);
        dbStatusHelper.addNewStatus("Lawry's Seasoning Salt", "true", "false", "false");

        dbHelper.addNewItem("Ranch Dip Mix", "Laura Scudder's", "Seasonings", "Vons", 3);
        dbStatusHelper.addNewStatus("Ranch Dip Mix", "false", "false", "true");

        dbHelper.addNewItem("Vanilla Extract", "Signature Select", "Seasonings", "Vons", 4);
        dbStatusHelper.addNewStatus("Vanilla Extract", "true", "false", "false");

        dbHelper.addNewItem("Cinnamon Sugar", "McCormick's", "Seasonings", "Vons", 5);
        dbStatusHelper.addNewStatus("Cinnamon Sugar", "false", "false", "true");

        dbHelper.addNewItem("Sprinkles", "3 types", "Seasonings", "Vons", 6);
        dbStatusHelper.addNewStatus("Sprinkles", "false", "false", "true");


        categoryData.getCategoryViewAllMap().put("Seasonings", 7);
        categoryData.getCategoryViewInStockMap().put("Seasonings", 4);
        categoryData.getCategoryViewNeededMap().put("Seasonings", 0);
        categoryData.getCategoryViewPausedMap().put("Seasonings", 3);
        dbCategoryHelper.setCategoryViews("Seasonings", 7,4,0,3);


        //------------------------------------Drinks------------------------------------------------

        dbHelper.addNewItem("Soda Bottles", "Pepsi or Coke", "Drinks", "Vons", 0);
        dbStatusHelper.addNewStatus("Soda Bottles", "true", "false", "false");

        dbHelper.addNewItem("Soda Cans", "Pepsi or Coke", "Drinks", "Costco", 0);
        dbStatusHelper.addNewStatus("Soda Cans", "true", "false", "false");

        dbHelper.addNewItem("Hot Chocolate Mix", "Swiss Miss Dark", "Drinks", "Vons", 1);
        dbStatusHelper.addNewStatus("Hot Chocolate Mix", "true", "false", "false");

        dbHelper.addNewItem("Bottled Water", "Aquafina", "Drinks", "Vons", 3);
        dbStatusHelper.addNewStatus("Bottled Water", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Drinks", 4);
        categoryData.getCategoryViewInStockMap().put("Drinks", 4);
        categoryData.getCategoryViewNeededMap().put("Drinks", 0);
        categoryData.getCategoryViewPausedMap().put("Drinks", 0);
        dbCategoryHelper.setCategoryViews("Drinks", 4,4,0,0);


        //------------------------------------Snacks------------------------------------------------

        dbHelper.addNewItem("Beef Jerky", "Archer Terriyaki", "Snacks", "Vons", 0);
        dbStatusHelper.addNewStatus("Beef Jerky", "false", "false", "true");

        dbHelper.addNewItem("Peanuts", "Honey Roasted", "Snacks", "Vons", 1);
        dbStatusHelper.addNewStatus("Peanuts", "false", "true", "false");

        dbHelper.addNewItem("Shell Peanuts", "Salted", "Snacks", "Vons", 2);
        dbStatusHelper.addNewStatus("Shell Peanuts", "false", "true", "false");

        dbHelper.addNewItem("Sunflower Seeds", "Salted", "Snacks", "Vons", 3);
        dbStatusHelper.addNewStatus("Sunflower Seeds", "true", "false", "false");

        dbHelper.addNewItem("Vinegar Chips", "Kettle", "Snacks", "Vons", 4);
        dbStatusHelper.addNewStatus("Vinegar Chips", "true", "false", "false");

        dbHelper.addNewItem("BBQ Chips", "Kettle", "Snacks", "Vons", 5);
        dbStatusHelper.addNewStatus("BBQ Chips", "false", "false", "true");

        dbHelper.addNewItem("Doritos", "Cool Ranch", "Snacks", "Vons", 6);
        dbStatusHelper.addNewStatus("Doritos", "false", "false", "true");

        dbHelper.addNewItem("Lay's Chips", "Classic", "Snacks", "Vons", 7);
        dbStatusHelper.addNewStatus("Lay's Chips", "true", "false", "false");

        dbHelper.addNewItem("Naan Crisps", "Stonefire", "Snacks", "Vons", 8);
        dbStatusHelper.addNewStatus("Naan Crisps", "true", "false", "false");

        dbHelper.addNewItem("Oreo Cakesters", "Nabisco", "Snacks", "Vons", 9);
        dbStatusHelper.addNewStatus("Oreo Cakesters", "false", "false", "true");

        dbHelper.addNewItem("Goldfish", "Cheddar", "Snacks", "Vons", 10);
        dbStatusHelper.addNewStatus("Goldfish", "false", "true", "false");

        dbHelper.addNewItem("Cheez-Its", "Original", "Snacks", "Vons", 11);
        dbStatusHelper.addNewStatus("Cheez-Its", "false", "true", "false");

        dbHelper.addNewItem("Famous Amos Cookies", "12 Pack", "Snacks", "Vons", 12);
        dbStatusHelper.addNewStatus("Famous Amos Cookies", "false", "true", "false");

        dbHelper.addNewItem("Dark Chocolate Pretzels", "Flipz", "Snacks", "CVS", 13);
        dbStatusHelper.addNewStatus("Dark Chocolate Pretzels", "true", "false", "false");

        dbHelper.addNewItem("Choc. Fudge Pudding", "Snack Pack", "Snacks", "Stater Bros", 14);
        dbStatusHelper.addNewStatus("Choc. Fudge Pudding", "false", "false", "true");

        dbHelper.addNewItem("Choc. Fudge Pirouette", "Pepperidge Farm", "Snacks", "Vons", 15);
        dbStatusHelper.addNewStatus("Choc. Fudge Pirouette", "false", "false", "true");

        dbHelper.addNewItem("Muddy Buddies", "Brownie Supreme", "Snacks", "Amazon", 16);
        dbStatusHelper.addNewStatus("Muddy Buddies", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Snacks", 17);
        categoryData.getCategoryViewInStockMap().put("Snacks", 6);
        categoryData.getCategoryViewNeededMap().put("Snacks", 5);
        categoryData.getCategoryViewPausedMap().put("Snacks", 6);
        dbCategoryHelper.setCategoryViews("Snacks", 17,6,5,6);


        //------------------------------------Desserts----------------------------------------------

        dbHelper.addNewItem("Choc. Malted Crunch Ice Cream", "Thrifty", "Desserts", "Rite Aid", 0);
        dbStatusHelper.addNewStatus("Choc. Malted Crunch Ice Cream", "false", "true", "false");

        dbHelper.addNewItem("Churros", "Tio Pepe’s", "Desserts", "Smart & Final", 1);
        dbStatusHelper.addNewStatus("Churros", "false", "false", "true");

        dbHelper.addNewItem("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 2);
        dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "false", "false", "true");

        dbHelper.addNewItem("Choc. Chip Cookie Mix", "Gluten Free", "Desserts", "Stater Bros", 3);
        dbStatusHelper.addNewStatus("Choc. Chip Cookie Mix", "false", "false", "true");

        dbHelper.addNewItem("Gingerbread Cookie Mix", "Betty Crocker", "Desserts", "Amazon", 4);
        dbStatusHelper.addNewStatus("Gingerbread Cookie Mix", "false", "false", "true");

        dbHelper.addNewItem("Oreos", "for crumbs", "Desserts", "Vons", 5);
        dbStatusHelper.addNewStatus("Oreos", "true", "false", "false");

        dbHelper.addNewItem("Choc. Malt Mix", "Nestle", "Desserts", "Stater Bros", 6);
        dbStatusHelper.addNewStatus("Choc. Malt Mix", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Desserts", 7);
        categoryData.getCategoryViewInStockMap().put("Desserts", 2);
        categoryData.getCategoryViewNeededMap().put("Desserts", 1);
        categoryData.getCategoryViewPausedMap().put("Desserts", 4);
        dbCategoryHelper.setCategoryViews("Desserts", 7,2,1,4);


        //------------------------------------Candy-------------------------------------------------

        dbHelper.addNewItem("Dark Chocolate Caramels", "Ghiradelli", "Candy", "Walmart", 0);
        dbStatusHelper.addNewStatus("Dark Chocolate Caramels", "true", "false", "false");

        dbHelper.addNewItem("Reese's PB Cups", "(individually wrapped)", "Candy", "Vons", 1);
        dbStatusHelper.addNewStatus("Reese's PB Cups", "true", "false", "false");

        dbHelper.addNewItem("Candy Corn", "Brach's", "Candy", "CVS", 2);
        dbStatusHelper.addNewStatus("Candy Corn", "true", "false", "false");

        dbHelper.addNewItem("Hot Tamales", "na", "Candy", "Vons", 3);
        dbStatusHelper.addNewStatus("Hot Tamales", "true", "false", "false");

        dbHelper.addNewItem("Smarties", "na", "Candy", "Rite Aid", 4);
        dbStatusHelper.addNewStatus("Smarties", "false", "false", "true");

        dbHelper.addNewItem("Sno Caps", "na", "Candy", "Dollar Tree", 5);
        dbStatusHelper.addNewStatus("Sno Caps", "false", "true", "false");

        dbHelper.addNewItem("Mini M&M's", "na", "Candy", "Vons", 6);
        dbStatusHelper.addNewStatus("Mini M&M's", "false", "true", "false");

        dbHelper.addNewItem("Caramel Squares", "na", "Candy", "Sprouts", 7);
        dbStatusHelper.addNewStatus("Caramel Squares", "false", "false", "true");

        dbHelper.addNewItem("Jelly Beans", "Sizzling Cinnamon", "Candy", "Amazon", 8);
        dbStatusHelper.addNewStatus("Jelly Beans", "false", "false", "true");

        dbHelper.addNewItem("Tootsie Rolls", "na", "Candy", "Vons", 9);
        dbStatusHelper.addNewStatus("Tootsie Rolls", "true", "false", "false");

        dbHelper.addNewItem("Fun Dip Sticks", "na", "Candy", "Smart & Final", 10);
        dbStatusHelper.addNewStatus("Fun Dip Sticks", "false", "false", "true");

        dbHelper.addNewItem("72% Intense Dark Chocolate", "Ghiradelli", "Candy", "Walmart", 11);
        dbStatusHelper.addNewStatus("72% Intense Dark Chocolate", "true", "false", "false");

        dbHelper.addNewItem("Orange Tic Tacs", "na", "Candy", "Smart & Final", 12);
        dbStatusHelper.addNewStatus("Orange Tic Tacs", "false", "true", "false");

        dbHelper.addNewItem("Orange Pez", "na", "Candy", "Amazon", 13);
        dbStatusHelper.addNewStatus("Orange Pez", "false", "true", "false");

        dbHelper.addNewItem("Vanilla Taffy", "na", "Candy", "Amazon", 14);
        dbStatusHelper.addNewStatus("Vanilla Taffy", "false", "true", "false");

        dbHelper.addNewItem("Vanilla Tootsie Rolls", "na", "Candy", "Amazon", 15);
        dbStatusHelper.addNewStatus("Vanilla Tootsie Rolls", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Candy", 16);
        categoryData.getCategoryViewInStockMap().put("Candy", 7);
        categoryData.getCategoryViewNeededMap().put("Candy", 5);
        categoryData.getCategoryViewPausedMap().put("Candy", 4);
        dbCategoryHelper.setCategoryViews("Candy", 16,7,5,4);


        //------------------------------------Pet Supplies-------------------------------------------

        dbHelper.addNewItem("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 0);
        dbStatusHelper.addNewStatus("Cat Food (wet)", "true", "false", "false");

        dbHelper.addNewItem("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 1);
        dbStatusHelper.addNewStatus("Cat Food (dry)", "true", "false", "false");

        dbHelper.addNewItem("Delectables", "Squeeze Up 20 pack", "Pet Supplies", "Vons", 2);
        dbStatusHelper.addNewStatus("Delectables", "true", "false", "false");

        dbHelper.addNewItem("Kitty Liter", "Scoop Away Complete", "Pet Supplies", "Costco", 3);
        dbStatusHelper.addNewStatus("Kitty Liter", "false", "true", "false");

        dbHelper.addNewItem("Dog Food (dry)", "Canidae All Life Stages", "Pet Supplies", "Yorba Linda Feed Store", 4);
        dbStatusHelper.addNewStatus("Dog Food (dry)", "true", "false", "false");

        dbHelper.addNewItem("Chicken Broth", "Kirkland Organic", "Pet Supplies", "Costco", 5);
        dbStatusHelper.addNewStatus("Chicken Broth", "true", "false", "false");

        dbHelper.addNewItem("Frozen Vegetables", "Kirkland Organic", "Pet Supplies", "Costco", 6);
        dbStatusHelper.addNewStatus("Frozen Vegetables", "true", "false", "false");

        dbHelper.addNewItem("Mashed Potatoes", "Main St. Bistro", "Pet Supplies", "Costco", 7);
        dbStatusHelper.addNewStatus("Mashed Potatoes", "true", "false", "false");

        dbHelper.addNewItem("100% Pure Pumpkin", "Libby's", "Pet Supplies", "Vons", 8);
        dbStatusHelper.addNewStatus("100% Pure Pumpkin", "true", "false", "false");

        dbHelper.addNewItem("Poop Bags", "Amazon Basics", "Pet Supplies", "Amazon", 9);
        dbStatusHelper.addNewStatus("Poop Bags", "true", "false", "false");

        dbHelper.addNewItem("Nitrile Gloves", "GMG 100 pack", "Pet Supplies", "Amazon", 10);
        dbStatusHelper.addNewStatus("Nitrile Gloves", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Pet Supplies", 11);
        categoryData.getCategoryViewInStockMap().put("Pet Supplies", 10);
        categoryData.getCategoryViewNeededMap().put("Pet Supplies", 1);
        categoryData.getCategoryViewPausedMap().put("Pet Supplies", 0);
        dbCategoryHelper.setCategoryViews("Pet Supplies", 11,10,1,0);



        //------------------------------------Toiletries--------------------------------------------

        dbHelper.addNewItem("Hand Soap", "Lavender & Chamomile", "Toiletries", "Dollar Tree", 0);
        dbStatusHelper.addNewStatus("Hand Soap", "true", "false", "false");

        dbHelper.addNewItem("Body Wash", "Jason Lavender", "Toiletries", "Vons", 1);
        dbStatusHelper.addNewStatus("Body Wash", "true", "false", "false");

        dbHelper.addNewItem("Shampoo", "Suave 2 in 1", "Toiletries", "Vons", 2);
        dbStatusHelper.addNewStatus("Shampoo", "true", "false", "false");

        dbHelper.addNewItem("Bar Soap", "Zum Bar Sea Salt", "Toiletries", "Sprouts", 3);
        dbStatusHelper.addNewStatus("Bar Soap", "true", "false", "false");

        dbHelper.addNewItem("Deodorant", "Old Spice", "Toiletries", "Vons", 4);
        dbStatusHelper.addNewStatus("Deodorant", "true", "false", "false");

        dbHelper.addNewItem("Toothpaste", "Tom's Antiplaque & Whitening", "Toiletries", "Amazon", 5);
        dbStatusHelper.addNewStatus("Toothpaste", "true", "false", "false");

        dbHelper.addNewItem("Floss", "Reach Mint Waxed", "Toiletries", "Amazon", 6);
        dbStatusHelper.addNewStatus("Floss", "true", "false", "false");

        dbHelper.addNewItem("Shaving Cream", "Sandalwood", "Toiletries", "Amazon", 7);
        dbStatusHelper.addNewStatus("Shaving Cream", "true", "false", "false");

        dbHelper.addNewItem("Shaving Razors", "Gillette ProGlide", "Toiletries", "Amazon", 8);
        dbStatusHelper.addNewStatus("Shaving Razors", "true", "false", "false");

        dbHelper.addNewItem("Mouthwash", "Crest Whitening", "Toiletries", "Vons", 9);
        dbStatusHelper.addNewStatus("Mouthwash", "true", "false", "false");

        dbHelper.addNewItem("Cotton Swabs", "Q-Tips", "Toiletries", "Vons", 10);
        dbStatusHelper.addNewStatus("Cotton Swabs", "true", "false", "false");

        dbHelper.addNewItem("Sunscreen", "Hawaiian Tropic Sheer 50spf", "Toiletries", "Amazon", 11);
        dbStatusHelper.addNewStatus("Sunscreen", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Toiletries", 12);
        categoryData.getCategoryViewInStockMap().put("Toiletries", 12);
        categoryData.getCategoryViewNeededMap().put("Toiletries", 0);
        categoryData.getCategoryViewPausedMap().put("Toiletries", 0);
        dbCategoryHelper.setCategoryViews("Toiletries", 12,12,0,0);


        //------------------------------------Household-------------------------------------------------

        dbHelper.addNewItem("Febreeze Air Spray", "Heavy Duty", "Household", "Vons", 0);
        dbStatusHelper.addNewStatus("Febreeze Air Spray", "true", "false", "false");

        dbHelper.addNewItem("All Purpose Cleaner", "Meyer's Lavender", "Household", "Vons", 1);
        dbStatusHelper.addNewStatus("All Purpose Cleaner", "true", "false", "false");

        dbHelper.addNewItem("Pet Stain Cleaner", "Rocco & Roxie", "Household", "Amazon", 2);
        dbStatusHelper.addNewStatus("Pet Stain Cleaner", "true", "false", "false");

        dbHelper.addNewItem("Laundry Detergent", "Woolite", "Household", "Vons", 3);
        dbStatusHelper.addNewStatus("Laundry Detergent", "true", "false", "false");

        dbHelper.addNewItem("Laundry Sanitizer", "Lysol", "Household", "Vons", 4);
        dbStatusHelper.addNewStatus("Laundry Sanitizer", "true", "false", "false");

        dbHelper.addNewItem("Dryer Sheets", "Simply Done Fresh Linen", "Household", "Stater Bros", 5);
        dbStatusHelper.addNewStatus("Dryer Sheets", "true", "false", "false");

        dbHelper.addNewItem("Aluminum Foil", "Reynolds Wrap", "Household", "Vons", 6);
        dbStatusHelper.addNewStatus("Aluminum Foil", "true", "false", "false");

        dbHelper.addNewItem("Zip-Lock Bags (small)", "Sandwich", "Household", "Vons", 7);
        dbStatusHelper.addNewStatus("Zip-Lock Bags (small)", "true", "false", "false");

        dbHelper.addNewItem("Zip-Lock Bags (large)", "Freezer Gallon", "Household", "Vons", 8);
        dbStatusHelper.addNewStatus("Zip-Lock Bags (large)", "true", "false", "false");

        dbHelper.addNewItem("Saran Wrap", "Plastic Wrap", "Household", "Vons", 9);
        dbStatusHelper.addNewStatus("Saran Wrap", "true", "false", "false");

        dbHelper.addNewItem("Rubbing Alcohol", "Isopropyl", "Household", "CVS", 10);
        dbStatusHelper.addNewStatus("Rubbing Alcohol", "true", "false", "false");

        dbHelper.addNewItem("Hydrogen Peroxide", "na", "Household", "CVS", 11);
        dbStatusHelper.addNewStatus("Hydrogen Peroxide", "true", "false", "false");

        dbHelper.addNewItem("Night Light Bulbs", "C7 E12", "Household", "Amazon", 12);
        dbStatusHelper.addNewStatus("Night Light Bulbs", "true", "false", "false");

        dbHelper.addNewItem("Scrub Sponges", "Non-Scratch", "Household", "Vons", 13);
        dbStatusHelper.addNewStatus("Scrub Sponges", "true", "false", "false");

        dbHelper.addNewItem("Dishwashing Brush", "Great Value", "Household", "Walmart", 14);
        dbStatusHelper.addNewStatus("Dishwashing Brush", "true", "false", "false");

        dbHelper.addNewItem("Small Trash Bags", "13 gallon", "Household", "Walmart", 15);
        dbStatusHelper.addNewStatus("Small Trash Bags", "true", "false", "false");

        dbHelper.addNewItem("Large Trash Bags", "33 gallon", "Household", "Walmart", 16);
        dbStatusHelper.addNewStatus("Large Trash Bags", "true", "false", "false");

        dbHelper.addNewItem("Compactor Bags", "18 gallon", "Household", "Walmart", 17);
        dbStatusHelper.addNewStatus("Compactor Bags", "true", "false", "false");

        dbHelper.addNewItem("Dawn Powerwash", "Dish Cleaner", "Household", "Vons", 18);
        dbStatusHelper.addNewStatus("Dawn Powerwash", "true", "false", "false");

        dbHelper.addNewItem("Dish Soap", "Dawn Platinum", "Household", "Vons", 19);
        dbStatusHelper.addNewStatus("Dish Soap", "true", "false", "false");

        dbHelper.addNewItem("Paper Towels", "Sparkle", "Household", "Walmart", 20);
        dbStatusHelper.addNewStatus("Paper Towels", "true", "false", "false");

        dbHelper.addNewItem("Toilet Paper", "Angel Soft", "Household", "Walmart", 21);
        dbStatusHelper.addNewStatus("Toilet Paper", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Household", 22);
        categoryData.getCategoryViewInStockMap().put("Household", 22);
        categoryData.getCategoryViewNeededMap().put("Household", 0);
        categoryData.getCategoryViewPausedMap().put("Household", 0);
        dbCategoryHelper.setCategoryViews("Household", 22,22,0,0);


        //------------------------------------Supplements-------------------------------------------------

        dbHelper.addNewItem("Triple Omega", "Nature Made", "Supplements", "Vons", 0);
        dbStatusHelper.addNewStatus("Triple Omega", "true", "false", "false");

        dbHelper.addNewItem("Multivitamin", "One a Day Men's", "Supplements", "Vons", 1);
        dbStatusHelper.addNewStatus("Multivitamin", "true", "false", "false");

        dbHelper.addNewItem("Vitamin C", "Amazon Elements 1000 mg", "Supplements", "Amazon", 2);
        dbStatusHelper.addNewStatus("Vitamin C", "true", "false", "false");

        dbHelper.addNewItem("Magnesium", "Nature Made 400mg", "Supplements", "Vons", 3);
        dbStatusHelper.addNewStatus("Magnesium", "true", "false", "false");

        dbHelper.addNewItem("Zinc", "Sandhu Herbals 50mg", "Supplements", "Vons", 4);
        dbStatusHelper.addNewStatus("Zinc", "true", "false", "false");

        dbHelper.addNewItem("Calcium", "Nature's Truth 1200 mg", "Supplements", "Vons", 5);
        dbStatusHelper.addNewStatus("Calcium", "true", "false", "false");

        dbHelper.addNewItem("Biotin", "Natrol Biotin 10,000mcg", "Supplements", "Vons", 6);
        dbStatusHelper.addNewStatus("Biotin", "true", "false", "false");

        dbHelper.addNewItem("Vitamin D3", "Nature Made 5000 IU", "Supplements", "Vons", 7);
        dbStatusHelper.addNewStatus("Vitamin D3", "true", "false", "false");


        categoryData.getCategoryViewAllMap().put("Supplements", 8);
        categoryData.getCategoryViewInStockMap().put("Supplements", 8);
        categoryData.getCategoryViewNeededMap().put("Supplements", 0);
        categoryData.getCategoryViewPausedMap().put("Supplements", 10);
        dbCategoryHelper.setCategoryViews("Supplements", 8,8,0,10);


        //------------------------------------------------------------------------------------------------

        dbHelper = new DBHelper(this);
        dbStatusHelper = new DBStatusHelper(this);
        dbCategoryHelper = new DBCategoryHelper(this);
        dbStoreHelper = new DBStoreHelper(this);
        itemData = dbHelper.readItemData();
        statusData = dbStatusHelper.readStatusData();
        itemData.updateStatuses(statusData);
        categoryData = dbCategoryHelper.readCategoryData();
        storeData = dbStoreHelper.readStoreData();
        itemIsSelectedInInventory = false;
        itemIsSelectedInShopByStore = false;
        itemIsClickedInInventory = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i++) {
            itemIsClickedInInventory.add(false);
        }
        itemIsClickedInShopByStore = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i++) {
            itemIsClickedInShopByStore.add(false);
        }
        itemIsChecked = new ArrayList<>();
        for (int i = 0; i < itemData.getItemList().size(); i++) {
            itemIsChecked.add(false);
        }
        storeNum = 0;
        reorderItemsCategory = "";
        editItemInInventory = false;
        editItemInShopByStore = false;
    }

}