package ryan.android.shopping;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import java.util.Objects;

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

    public int storeListOrderNum;
    public String reorderItemsCategory;
    public String reorderItemsStore;
    public String currentSearchTerm;
    public Boolean editItemInInventory;
    public Boolean editItemInShoppingList;

    private SearchAlgorithm searchAlgorithm;

    public String inventoryView;
    public static final String INVENTORY_ALL = "view all";
    public static final String INVENTORY_INSTOCK = "view instock";
    public static final String INVENTORY_NEEDED = "view needed";
    public static final String INVENTORY_PAUSED = "view paused";

    public String inventorySortBy;
    public String defaultSortBy;
    public static final String SORT_BY_CATEGORY = "category";
    public static final String SORT_BY_STORE = "store";
    public static final String SORT_ALPHABETICAL = "alphabetical";

    public String storeTitles;
    public String categoryTitles;
    public static final String TITLES_EXPANDED = "titles expanded";
    public static final String TITLES_CONTRACTED = "titles contracted";

    public String itemExpansion;
    public static final String ITEMS_EXPANDED = "items expanded";
    public static final String ITEMS_CONTRACTED = "items contracted";

    public String reorderingMethod;
    public static final String DRAG_AND_DROP = "drag and drop";
    public static final String UP_AND_DOWN_ARROWS = "up and down arrows";
    public static final String WITH_NUMBERS = "with numbers";

    public String colorScheme;
    public static final String COLOR_SCHEME_1 = "color scheme 1";
    public static final String COLOR_SCHEME_2 = "color scheme 2";
    public static final String COLOR_SCHEME_3 = "color scheme 3";

    public Parcelable shoppingListViewState;
    public Parcelable fullInventoryViewState;
    public Parcelable reorderCategoriesViewState;
    public Parcelable reorderStoresViewState;
    public Parcelable reorderItemsRecyclerViewState;
    //public Parcelable reorderItemsScrollViewState;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping);

        initializeData();
        loadSharedPreferences();
        searchAlgorithm = new SearchAlgorithm();
        for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
            searchAlgorithm.addNewItem(itemData.getItemListAZ().get(i));
        }

        /*ArrayList<Item> searchResultsList = searchInventory.getSearchResults("dark choc");
        if (searchResultsList == null) {
            System.out.println("searchResultList is null");
        } else {
            System.out.println("searchResultList size = " + searchResultsList.size());
            for (int i = 0; i < searchResultsList.size(); i++) {
                System.out.println("Search Result " + i + ": " + searchResultsList.get(i).getName());
            }
        }*/

        Button fullInventory = findViewById(R.id.fullInventoryTopMenu);
        fullInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getFragmentManager().findFragmentById(R.id.fragments);
                if (f instanceof FullInventory) return;
                loadFragment(new FullInventory());
            }
        });

        Button shoppingList = findViewById(R.id.shoppingListTopMenu);
        shoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getFragmentManager().findFragmentById(R.id.fragments);
                if (f instanceof ShoppingList) return;
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

    public SearchAlgorithm getSearchAlgorithm() {
        return searchAlgorithm;
    }

    /*public void updateSearchInventory() {

    }*/

    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.commit();
    }

    public void loadSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);

        String defaultSortBy = sharedPref.getString("default_sort_by", "Default Sort By");
        switch (defaultSortBy) {
            case "alphabetical":
                this.defaultSortBy = SORT_ALPHABETICAL;
                inventorySortBy = SORT_ALPHABETICAL;
                break;
            case "category":
                this.defaultSortBy = SORT_BY_CATEGORY;
                inventorySortBy = SORT_BY_CATEGORY;
                break;
            case "store":
                this.defaultSortBy = SORT_BY_STORE;
                inventorySortBy = SORT_BY_STORE;
                break;
        }

        String reorderingMethod = sharedPref.getString("reorder_method", "Default Reordering");
        switch (reorderingMethod) {
            case "drag and drop":
                this.reorderingMethod = DRAG_AND_DROP;
                break;
            case "up and down arrows":
                this.reorderingMethod = UP_AND_DOWN_ARROWS;
                break;
            case "with numbers":
                this.reorderingMethod = WITH_NUMBERS;
                break;
        }

        String colorScheme = sharedPref.getString("color_scheme", "Default Color Scheme");
        switch (colorScheme) {
            case "color scheme 1":
                this.colorScheme = COLOR_SCHEME_1;
                break;
            case "color scheme 2":
                this.colorScheme = COLOR_SCHEME_2;
                break;
            case "color scheme 3":
                this.colorScheme = COLOR_SCHEME_3;
                break;
        }
    }

    public void clearAllData() {
        dbItemHelper.deleteDatabase();
        dbStatusHelper.deleteDatabase();
        dbCategoryHelper.deleteDatabase();
        dbStoreHelper.deleteDatabase();
    }

    public void initializeData() {
        dbItemHelper = new DBItemHelper(this);
        dbStatusHelper = new DBStatusHelper(this);
        dbCategoryHelper = new DBCategoryHelper(this);
        dbStoreHelper = new DBStoreHelper(this);

        itemData = new ItemData();
        statusData = new StatusData();
        categoryData = new CategoryData();
        storeData = new StoreData();

        dbItemHelper.readItemDataByCategory(itemData);
        dbItemHelper.readItemDataByStore(itemData);
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

        storeListOrderNum = 0;
        reorderItemsCategory = "";
        reorderItemsStore = "";
        currentSearchTerm = "";
        editItemInInventory = false;
        editItemInShoppingList = false;

        inventoryView = INVENTORY_ALL;
        inventorySortBy = defaultSortBy;
        categoryTitles = TITLES_EXPANDED;
        storeTitles = TITLES_EXPANDED;
        itemExpansion = ITEMS_CONTRACTED;
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
        dbStoreHelper.addNewStore("Smart & Final", 1);
        dbStoreHelper.addNewStore("Costco", 2);
        dbStoreHelper.addNewStore("Walmart", 3);
        dbStoreHelper.addNewStore("Amazon", 4);
        dbStoreHelper.addNewStore("Stater Bros", 5);
        dbStoreHelper.addNewStore("Trader Joe's", 6);
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

    public void loadCategoryData1() {

        //------------------------------------Meals-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Sausage Biscuits", "Jimmy Dean Frozen", "Meals", "Vons", 0);
        dbStatusHelper.addNewStatus("Sausage Biscuits", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hamburger Helper", "Cheeseburger Macaroni", "Meals", "Vons", 1);
        dbStatusHelper.addNewStatus("Hamburger Helper", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Buffalo Chicken Bites", "TGIF or Frank's", "Meals", "Vons", 2);
        dbStatusHelper.addNewStatus("Buffalo Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Terriyaki Chicken Bites", "InnovAsian", "Meals", "Vons", 3);
        dbStatusHelper.addNewStatus("Terriyaki Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Crispy Buffalo Wings", "Foster Farms", "Meals", "Costco", 4);
        dbStatusHelper.addNewStatus("Crispy Buffalo Wings", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("TGIF Cheese Sticks", "TGIF (small 10pc)", "Meals", "Vons", 5);
        dbStatusHelper.addNewStatus("TGIF Cheese Sticks", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Frozen Pizza", "Thin Pepperoni", "Meals", "Vons", 6);
        dbStatusHelper.addNewStatus("Frozen Pizza", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Corn Dogs", "Foster Farms", "Meals", "Vons", 7);
        dbStatusHelper.addNewStatus("Corn Dogs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hot Dogs", "Bun Size", "Meals", "Vons", 8);
        dbStatusHelper.addNewStatus("Hot Dogs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hot Dog Buns", "(8 pack)", "Meals", "Vons", 9);
        dbStatusHelper.addNewStatus("Hot Dog Buns", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hamburger Patties", "to do", "Meals", "Vons", 10);
        dbStatusHelper.addNewStatus("Hamburger Patties", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hamburger Buns", "(8 pack)", "Meals", "Vons", 11);
        dbStatusHelper.addNewStatus("Hamburger Buns", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Pasta Roni 1", "Angel Hair Pasta", "Meals", "Vons", 12);
        dbStatusHelper.addNewStatus("Pasta Roni 1", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Pasta Roni 2", "Fettuccine Alfredo", "Meals", "Vons", 13);
        dbStatusHelper.addNewStatus("Pasta Roni 2", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Mac & Cheese", "Annie’s", "Meals", "Vons", 14);
        dbStatusHelper.addNewStatus("Mac & Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Gnocci", "Signature Select", "Meals", "Vons", 15);
        dbStatusHelper.addNewStatus("Gnocci", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Tortellini", "Barilla 3 Cheese", "Meals", "Vons", 16);
        dbStatusHelper.addNewStatus("Tortellini", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Meals", 17);
        categoryData.getCategoryViewInStockMap().put("Meals", 0);
        categoryData.getCategoryViewNeededMap().put("Meals", 0);
        categoryData.getCategoryViewPausedMap().put("Meals", 17);
        dbCategoryHelper.setCategoryViews("Meals", 17, 0, 0, 17);

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
        categoryData.getCategoryViewInStockMap().put("Soups", 0);
        categoryData.getCategoryViewNeededMap().put("Soups", 0);
        categoryData.getCategoryViewPausedMap().put("Soups", 7);
        dbCategoryHelper.setCategoryViews("Soups", 7, 0, 0, 7);

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
        categoryData.getCategoryViewInStockMap().put("Sides", 0);
        categoryData.getCategoryViewNeededMap().put("Sides", 0);
        categoryData.getCategoryViewPausedMap().put("Sides", 5);
        dbCategoryHelper.setCategoryViews("Sides", 5, 0, 0, 5);

        //------------------------------------Meat--------------------------------------------------

        dbItemHelper.addNewItemByCategory("Steak", "USDA", "Meat", "Vons", 0);
        dbStatusHelper.addNewStatus("Steak", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Ground Beef", "(1 pound)", "Meat", "Vons", 1);
        dbStatusHelper.addNewStatus("Ground Beef", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Frozen Meatballs", "Rosina Homestyle", "Meat", "Vons", 2);
        dbStatusHelper.addNewStatus("Frozen Meatballs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Pepperoni Slices", "Hormel (300 slices)", "Meat", "Smart & Final", 3);
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
        categoryData.getCategoryViewInStockMap().put("Meat", 0);
        categoryData.getCategoryViewNeededMap().put("Meat", 0);
        categoryData.getCategoryViewPausedMap().put("Meat", 9);
        dbCategoryHelper.setCategoryViews("Meat", 9, 0, 0, 9);

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

        dbItemHelper.addNewItemByCategory("Honey Smacks Cereal", "Honey Smacks", "Bread/Grains/Cereal", "Vons", 10);
        dbStatusHelper.addNewStatus("Honey Smacks Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Eggo Waffles", "Homestyle", "Bread/Grains/Cereal", "Vons", 11);
        dbStatusHelper.addNewStatus("Eggo Waffles", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Small Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 12);
        dbStatusHelper.addNewStatus("Small Flour Tortillas", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Large Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 13);
        dbStatusHelper.addNewStatus("Large Flour Tortillas", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Bread/Grains/Cereal", 14);
        categoryData.getCategoryViewInStockMap().put("Bread/Grains/Cereal", 0);
        categoryData.getCategoryViewNeededMap().put("Bread/Grains/Cereal", 0);
        categoryData.getCategoryViewPausedMap().put("Bread/Grains/Cereal", 14);
        dbCategoryHelper.setCategoryViews("Bread/Grains/Cereal", 14, 0, 0, 14);

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

        dbItemHelper.addNewItemByCategory("Half & Half", "Lucerne", "Eggs/Dairy", "Vons", 10);
        dbStatusHelper.addNewStatus("Half & Half", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Eggs/Dairy", 11);
        categoryData.getCategoryViewInStockMap().put("Eggs/Dairy", 0);
        categoryData.getCategoryViewNeededMap().put("Eggs/Dairy", 0);
        categoryData.getCategoryViewPausedMap().put("Eggs/Dairy", 11);
        dbCategoryHelper.setCategoryViews("Eggs/Dairy", 11, 0, 0, 11);

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

        dbItemHelper.addNewItemByCategory("Buffalo Sauce", "Frank's Wings", "Condiments", "Vons", 6);
        dbStatusHelper.addNewStatus("Buffalo Sauce", "paused", "unchecked");

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

        dbItemHelper.addNewItemByCategory("Woodranch BBQ Sauce", "(1 pint)", "Condiments", "Woodranch", 13);
        dbStatusHelper.addNewStatus("Woodranch BBQ Sauce", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Condiments", 14);
        categoryData.getCategoryViewInStockMap().put("Condiments", 0);
        categoryData.getCategoryViewNeededMap().put("Condiments", 0);
        categoryData.getCategoryViewPausedMap().put("Condiments", 14);
        dbCategoryHelper.setCategoryViews("Condiments", 14, 0, 0, 14);

        //------------------------------------Seasonings--------------------------------------------

        dbItemHelper.addNewItemByCategory("Salt & Pepper", "na", "Seasonings", "Vons", 0);
        dbStatusHelper.addNewStatus("Salt & Pepper", "paused", "unchecked");

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
        categoryData.getCategoryViewInStockMap().put("Seasonings", 0);
        categoryData.getCategoryViewNeededMap().put("Seasonings", 0);
        categoryData.getCategoryViewPausedMap().put("Seasonings", 8);
        dbCategoryHelper.setCategoryViews("Seasonings", 8, 0, 0, 8);

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

        dbItemHelper.addNewItemByCategory("Movie Theater Butter", "Kernel Seasons", "Misc/Ingredients", "Vons", 7);
        dbStatusHelper.addNewStatus("Movie Theater Butter", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Misc/Ingredients", 8);
        categoryData.getCategoryViewInStockMap().put("Misc/Ingredients", 0);
        categoryData.getCategoryViewNeededMap().put("Misc/Ingredients", 0);
        categoryData.getCategoryViewPausedMap().put("Misc/Ingredients", 8);
        dbCategoryHelper.setCategoryViews("Misc/Ingredients", 8, 0, 0, 8);

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
        categoryData.getCategoryViewInStockMap().put("Drinks", 0);
        categoryData.getCategoryViewNeededMap().put("Drinks", 0);
        categoryData.getCategoryViewPausedMap().put("Drinks", 4);
        dbCategoryHelper.setCategoryViews("Drinks", 4, 0, 0, 4);

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

        dbItemHelper.addNewItemByCategory("Choc. Caramel Swirl Popcorn", "Cretors", "Snacks", "Vons", 21);
        dbStatusHelper.addNewStatus("Choc. Caramel Swirl Popcorn", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Snacks", 22);
        categoryData.getCategoryViewInStockMap().put("Snacks", 0);
        categoryData.getCategoryViewNeededMap().put("Snacks", 0);
        categoryData.getCategoryViewPausedMap().put("Snacks", 22);
        dbCategoryHelper.setCategoryViews("Snacks", 22, 0, 0, 22);

        //------------------------------------Desserts----------------------------------------------

        dbItemHelper.addNewItemByCategory("Choc. Malted Crunch Ice Cream", "Thrifty", "Desserts", "Vons", 0);
        dbStatusHelper.addNewStatus("Choc. Malted Crunch Ice Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hold the Cone", "Chocolate", "Desserts", "Trader Joe's", 1);
        dbStatusHelper.addNewStatus("Hold the Cone", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Churros", "Tio Pepe’s or Hola!", "Desserts", "Smart & Final", 2);
        dbStatusHelper.addNewStatus("Churros", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 3);
        dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Chip Cookie Mix", "Gluten Free", "Desserts", "Stater Bros", 4);
        dbStatusHelper.addNewStatus("Choc. Chip Cookie Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Gingerbread Cookie Mix", "Betty Crocker", "Desserts", "Amazon", 5);
        dbStatusHelper.addNewStatus("Gingerbread Cookie Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreos", "(for crumbs)", "Desserts", "Vons", 6);
        dbStatusHelper.addNewStatus("Oreos", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreo Muffins", "12 pack", "Desserts", "Costco", 7);
        dbStatusHelper.addNewStatus("Oreo Muffins", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreo Cakesters", "Nabisco", "Desserts", "Vons", 8);
        dbStatusHelper.addNewStatus("Oreo Cakesters", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreo Pie Mix", "No Bake Dessert", "Desserts", "Target", 9);
        dbStatusHelper.addNewStatus("Oreo Pie Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Malt Mix", "Nestle", "Desserts", "Stater Bros", 10);
        dbStatusHelper.addNewStatus("Choc. Malt Mix", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Desserts", 11);
        categoryData.getCategoryViewInStockMap().put("Desserts", 0);
        categoryData.getCategoryViewNeededMap().put("Desserts", 0);
        categoryData.getCategoryViewPausedMap().put("Desserts", 11);
        dbCategoryHelper.setCategoryViews("Desserts", 11, 0, 0, 11);

        //------------------------------------Candy-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Dark Chocolate Caramel Squares", "Ghiradelli", "Candy", "Walmart", 0);
        dbStatusHelper.addNewStatus("Dark Chocolate Caramel Squares", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Reese's PB Cups", "(individually wrapped)", "Candy", "Vons", 1);
        dbStatusHelper.addNewStatus("Reese's PB Cups", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Candy Corn", "Brach's", "Candy", "CVS", 2);
        dbStatusHelper.addNewStatus("Candy Corn", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hot Tamales", "na", "Candy", "Vons", 3);
        dbStatusHelper.addNewStatus("Hot Tamales", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Smarties", "na", "Candy", "Dollar  Tree", 4);
        dbStatusHelper.addNewStatus("Smarties", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sno Caps", "na", "Candy", "Dollar Tree", 5);
        dbStatusHelper.addNewStatus("Sno Caps", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Good & Plenty", "na", "Candy", "Vons", 6);
        dbStatusHelper.addNewStatus("Good & Plenty", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Mini M&M's", "na", "Candy", "Vons", 7);
        dbStatusHelper.addNewStatus("Mini M&M's", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dark Choc. M&M's", "na", "Candy", "Target", 8);
        dbStatusHelper.addNewStatus("Dark Choc. M&M's", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sea Salt Caramels", "Favorite Day", "Candy", "Target", 9);
        dbStatusHelper.addNewStatus("Sea Salt Caramels", "paused", "unchecked");

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

        dbItemHelper.addNewItemByCategory("Red Hots", "na", "Candy", "Dollar Tree", 19);
        dbStatusHelper.addNewStatus("Red Hots", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dark Choc. Caramels", "Trader Joe's", "Candy", "Trader Joe's", 20);
        dbStatusHelper.addNewStatus("Dark Choc. Caramels", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Candy", 21);
        categoryData.getCategoryViewInStockMap().put("Candy", 0);
        categoryData.getCategoryViewNeededMap().put("Candy", 0);
        categoryData.getCategoryViewPausedMap().put("Candy", 21);
        dbCategoryHelper.setCategoryViews("Candy", 21, 0, 0, 21);

        //------------------------------------Pet Supplies-------------------------------------------

        dbItemHelper.addNewItemByCategory("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 0);
        dbStatusHelper.addNewStatus("Cat Food (wet)", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 1);
        dbStatusHelper.addNewStatus("Cat Food (dry)", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Delectables", "Squeeze Up 20 pack", "Pet Supplies", "Vons", 2);
        dbStatusHelper.addNewStatus("Delectables", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Cat Treats", "Temptations", "Pet Supplies", "Vons", 3);
        dbStatusHelper.addNewStatus("Cat Treats", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Kitty Litter", "Scoop Away Complete", "Pet Supplies", "Costco", 4);
        dbStatusHelper.addNewStatus("Kitty Litter", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dog Food (dry)", "Canidae All Life Stages", "Pet Supplies", "Yorba Linda Feed Store", 5);
        dbStatusHelper.addNewStatus("Dog Food (dry)", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Chicken Broth", "Kirkland Organic", "Pet Supplies", "Costco", 6);
        dbStatusHelper.addNewStatus("Chicken Broth", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Mashed Potatoes", "Main St. Bistro", "Pet Supplies", "Costco", 7);
        dbStatusHelper.addNewStatus("Mashed Potatoes", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Freshpet", "Chicken Recipe (6lb)", "Pet Supplies", "Costco", 8);
        dbStatusHelper.addNewStatus("Freshpet", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("100% Pure Pumpkin", "Libby's", "Pet Supplies", "Vons", 9);
        dbStatusHelper.addNewStatus("100% Pure Pumpkin", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Poop Bags", "Amazon Basics", "Pet Supplies", "Amazon", 10);
        dbStatusHelper.addNewStatus("Poop Bags", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Nitrile Gloves", "GMG 100 pack", "Pet Supplies", "Amazon", 11);
        dbStatusHelper.addNewStatus("Nitrile Gloves", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Pet Supplies", 12);
        categoryData.getCategoryViewInStockMap().put("Pet Supplies", 0);
        categoryData.getCategoryViewNeededMap().put("Pet Supplies", 0);
        categoryData.getCategoryViewPausedMap().put("Pet Supplies", 12);
        dbCategoryHelper.setCategoryViews("Pet Supplies", 12, 0, 0, 12);

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

        dbItemHelper.addNewItemByCategory("Moisturizing Lotion", "CVS Health w/ hyaluronic acid", "Toiletries", "CVS", 13);
        dbStatusHelper.addNewStatus("Moisturizing Lotion", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Toiletries", 14);
        categoryData.getCategoryViewInStockMap().put("Toiletries", 0);
        categoryData.getCategoryViewNeededMap().put("Toiletries", 0);
        categoryData.getCategoryViewPausedMap().put("Toiletries", 14);
        dbCategoryHelper.setCategoryViews("Toiletries", 14, 0, 0, 14);

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
        categoryData.getCategoryViewInStockMap().put("Household", 0);
        categoryData.getCategoryViewNeededMap().put("Household", 0);
        categoryData.getCategoryViewPausedMap().put("Household", 26);
        dbCategoryHelper.setCategoryViews("Household", 26, 0, 0, 26);

        //------------------------------------Supplements-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Triple Omega", "Nature Made", "Supplements", "Amazon", 0);
        dbStatusHelper.addNewStatus("Triple Omega", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Multivitamin", "One a Day Men's", "Supplements", "Amazon", 1);
        dbStatusHelper.addNewStatus("Multivitamin", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vitamin C", "Amazon Elements 1000 mg", "Supplements", "Amazon", 2);
        dbStatusHelper.addNewStatus("Vitamin C", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Magnesium", "Nature Made 400mg", "Supplements", "Amazon", 3);
        dbStatusHelper.addNewStatus("Magnesium", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Zinc", "Sandhu Herbals 50mg", "Supplements", "Amazon", 4);
        dbStatusHelper.addNewStatus("Zinc", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Calcium", "Nature's Truth 1200 mg", "Supplements", "Amazon", 5);
        dbStatusHelper.addNewStatus("Calcium", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Biotin", "Natrol 10,000mcg", "Supplements", "Amazon", 6);
        dbStatusHelper.addNewStatus("Biotin", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vitamin D3", "Nature Made 5000 IU", "Supplements", "Amazon", 7);
        dbStatusHelper.addNewStatus("Vitamin D3", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hyaluronic Acid", "Horbaach 1000mg", "Supplements", "Amazon", 8);
        dbStatusHelper.addNewStatus("Hyaluronic Acid", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Supplements", 9);
        categoryData.getCategoryViewInStockMap().put("Supplements", 0);
        categoryData.getCategoryViewNeededMap().put("Supplements", 0);
        categoryData.getCategoryViewPausedMap().put("Supplements", 9);
        dbCategoryHelper.setCategoryViews("Supplements", 9, 0, 0, 9);

        //------------------------------------------------------------------------------------------

        // total category items = 212

    }

//------------------------------------------------------------------------------------------------//
//----------------------------------------Sort By Store-------------------------------------------//
//------------------------------------------------------------------------------------------------//

    public void loadStoreData1() {

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

        dbItemHelper.addNewItemByStore("Pasta Roni 2", "Fettuccine Alfredo", "Meals", "Vons", 12);
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

        dbItemHelper.addNewItemByStore("Frozen Meatballs", "Rosina Homestyle", "Meat", "Vons", 26);
        //dbStatusHelper.addNewStatus("Frozen Meatballs", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Chicken Breast", "na", "Meat", "Vons", 27);
        //dbStatusHelper.addNewStatus("Chicken Breast", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sliced Turkey", "to do", "Meat", "Vons", 28);
        //dbStatusHelper.addNewStatus("Sliced Turkey", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sliced Ham", "to do", "Meat", "Vons", 29);
        //dbStatusHelper.addNewStatus("Sliced Ham", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ham Steak", "to do", "Meat", "Vons", 30);
        //dbStatusHelper.addNewStatus("Ham Steak", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Thin Spaghetti", "Barilla Whole Grain", "Bread/Grains/Cereal", "Vons", 31);
        //dbStatusHelper.addNewStatus("Thin Spaghetti", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Spiral Pasta", "Barilla Rotini", "Bread/Grains/Cereal", "Vons", 32);
        //dbStatusHelper.addNewStatus("Spiral Pasta", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Wheat Bread", "Nature's Own", "Bread/Grains/Cereal", "Vons", 33);
        //dbStatusHelper.addNewStatus("Wheat Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Baguette", "French", "Bread/Grains/Cereal", "Vons", 34);
        //dbStatusHelper.addNewStatus("Baguette", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sourdough Bread", "San Luis Sourdough", "Bread/Grains/Cereal", "Vons", 35);
        //dbStatusHelper.addNewStatus("Sourdough Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hard Rolls", "to do", "Bread/Grains/Cereal", "Vons", 36);
        //dbStatusHelper.addNewStatus("Hard Rolls", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Thomas Muffins", "Original", "Bread/Grains/Cereal", "Vons", 37);
        //dbStatusHelper.addNewStatus("Thomas Muffins", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Reese's Puffs Cereal", "Reese's Puffs", "Bread/Grains/Cereal", "Vons", 38);
        //dbStatusHelper.addNewStatus("Reese's Puffs Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cookie Crisp Cereal", "Cookie Crisp", "Bread/Grains/Cereal", "Vons", 39);
        //dbStatusHelper.addNewStatus("Cookie Crisp Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Frosted Mini Wheat Cereal", "Frosted Mini Wheat", "Bread/Grains/Cereal", "Vons", 40);
        //dbStatusHelper.addNewStatus("Frosted Mini Wheat Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Honey Smacks Cereal", "Honey Smacks", "Bread/Grains/Cereal", "Vons", 41);
        //dbStatusHelper.addNewStatus("Honey Smacks Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Eggo Waffles", "Homestyle", "Bread/Grains/Cereal", "Vons", 42);
        //dbStatusHelper.addNewStatus("Eggo Waffles", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Milk", "Vitamin D", "Eggs/Dairy", "Vons", 43);
        //dbStatusHelper.addNewStatus("Milk", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Eggs", "Grade AA", "Eggs/Dairy", "Vons", 44);
        //dbStatusHelper.addNewStatus("Eggs", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Honey Yogurt", "Greek Gods", "Eggs/Dairy", "Vons", 45);
        //dbStatusHelper.addNewStatus("Honey Yogurt", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Salted Butter", "Challenge", "Eggs/Dairy", "Vons", 46);
        //dbStatusHelper.addNewStatus("Salted Butter", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Shredded Cheese", "Mexican Blend", "Eggs/Dairy", "Vons", 47);
        //dbStatusHelper.addNewStatus("Shredded Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("String Cheese", "Mozarella", "Eggs/Dairy", "Vons", 48);
        //dbStatusHelper.addNewStatus("String Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("BD Cheese", "Black Diamond", "Eggs/Dairy", "Vons", 49);
        //dbStatusHelper.addNewStatus("BD Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Non-Stick Spray", "Pam Original", "Misc/Ingredients", "Vons", 50);
        //dbStatusHelper.addNewStatus("Non-Stick Spray", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Parmesan Cheese", "Kraft", "Condiments", "Vons", 51);
        //dbStatusHelper.addNewStatus("Parmesan Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("A1 Sauce", "Original", "Condiments", "Vons", 52);
        //dbStatusHelper.addNewStatus("A1 Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ketchup", "Heinz", "Condiments", "Vons", 53);
        //dbStatusHelper.addNewStatus("Ketchup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Mustard", "Heinz", "Condiments", "Vons", 54);
        //dbStatusHelper.addNewStatus("Mustard", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Pasta Sauce", "Ragu Meat", "Condiments", "Vons", 55);
        //dbStatusHelper.addNewStatus("Pasta Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Maple Syrup", "Pearl Milling", "Condiments", "Vons", 56);
        //dbStatusHelper.addNewStatus("Maple Syrup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Honey", "Local Hive Clover", "Condiments", "Vons", 57);
        //dbStatusHelper.addNewStatus("Honey", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Peanut Butter", "Skippy Creamy", "Condiments", "Vons", 58);
        //dbStatusHelper.addNewStatus("Peanut Butter", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Soy Sauce", "Kikoman", "Condiments", "Vons", 59);
        //dbStatusHelper.addNewStatus("Soy Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Brown Sugar", "to do", "Misc/Ingredients", "Vons", 60);
        //dbStatusHelper.addNewStatus("Brown Sugar", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Salt & Pepper", "na", "Seasonings", "Vons", 61);
        //dbStatusHelper.addNewStatus("Salt & Pepper", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Garlic Salt", "Lawry's", "Seasonings", "Vons", 62);
        //dbStatusHelper.addNewStatus("Garlic Salt", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Lawry's Seasoning Salt", "Lawry's", "Seasonings", "Vons", 63);
        //dbStatusHelper.addNewStatus("Lawry's Seasoning Salt", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ranch Dip Mix", "Laura Scudder's", "Seasonings", "Vons", 64);
        //dbStatusHelper.addNewStatus("Ranch Dip Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vanilla Extract", "Signature Select", "Seasonings", "Vons", 65);
        //dbStatusHelper.addNewStatus("Vanilla Extract", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cinnamon Sugar", "McCormick's", "Seasonings", "Vons", 66);
        //dbStatusHelper.addNewStatus("Cinnamon Sugar", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sprinkles", "3 types", "Seasonings", "Vons", 67);
        //dbStatusHelper.addNewStatus("Sprinkles", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Soda Bottles", "Pepsi or Coke", "Drinks", "Vons", 68);
        //dbStatusHelper.addNewStatus("Soda Bottles", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hot Chocolate Mix", "Swiss Miss Dark", "Drinks", "Vons", 69);
        //dbStatusHelper.addNewStatus("Hot Chocolate Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Bottled Water", "any", "Drinks", "Vons", 70);
        //dbStatusHelper.addNewStatus("Bottled Water", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Beef Jerky", "Archer Terriyaki", "Snacks", "Vons", 71);
        //dbStatusHelper.addNewStatus("Beef Jerky", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Peanuts", "Honey Roasted", "Snacks", "Vons", 72);
        //dbStatusHelper.addNewStatus("Peanuts", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Shell Peanuts", "Salted", "Snacks", "Vons", 73);
        //dbStatusHelper.addNewStatus("Shell Peanuts", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sunflower Seeds", "Salted", "Snacks", "Vons", 74);
        //dbStatusHelper.addNewStatus("Sunflower Seeds", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vinegar Chips", "Kettle", "Snacks", "Vons", 75);
        //dbStatusHelper.addNewStatus("Vinegar Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("BBQ Chips", "Kettle", "Snacks", "Vons", 76);
        //dbStatusHelper.addNewStatus("BBQ Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Doritos", "Cool Ranch", "Snacks", "Vons", 77);
        //dbStatusHelper.addNewStatus("Doritos", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Lay's Chips", "Classic", "Snacks", "Vons", 78);
        //dbStatusHelper.addNewStatus("Lay's Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Naan Crisps", "Stonefire", "Snacks", "Vons", 79);
        //dbStatusHelper.addNewStatus("Naan Crisps", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Oreo Cakesters", "Nabisco", "Snacks", "Vons", 80);
        //dbStatusHelper.addNewStatus("Oreo Cakesters", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Goldfish", "Cheddar", "Snacks", "Vons", 81);
        //dbStatusHelper.addNewStatus("Goldfish", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cheez-Its", "Original", "Snacks", "Vons", 82);
        //dbStatusHelper.addNewStatus("Cheez-Its", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Famous Amos Cookies", "12 Pack", "Snacks", "Vons", 83);
        //dbStatusHelper.addNewStatus("Famous Amos Cookies", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Fudge Pirouette", "Pepperidge Farm", "Snacks", "Vons", 84);
        //dbStatusHelper.addNewStatus("Choc. Fudge Pirouette", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 85);
        //dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Oreos", "(for crumbs)", "Desserts", "Vons", 86);
        //dbStatusHelper.addNewStatus("Oreos", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Malted Crunch Ice Cream", "Thrifty", "Desserts", "Vons", 87);
        //dbStatusHelper.addNewStatus("Choc. Malted Crunch Ice Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Reese's PB Cups", "(individually wrapped)", "Candy", "Vons", 88);
        //dbStatusHelper.addNewStatus("Reese's PB Cups", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hot Tamales", "na", "Candy", "Vons", 89);
        //dbStatusHelper.addNewStatus("Hot Tamales", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Mini M&M's", "na", "Candy", "Vons", 90);
        //dbStatusHelper.addNewStatus("Mini M&M's", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Tootsie Rolls", "na", "Candy", "Vons", 91);
        //dbStatusHelper.addNewStatus("Tootsie Rolls", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 92);
        //dbStatusHelper.addNewStatus("Cat Food (wet)", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("100% Pure Pumpkin", "Libby's", "Pet Supplies", "Vons", 93);
        //dbStatusHelper.addNewStatus("100% Pure Pumpkin", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Delectables", "Squeeze Up 20 pack", "Pet Supplies", "Vons", 94);
        //dbStatusHelper.addNewStatus("Delectables", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Body Wash", "Suave Mandarin", "Toiletries", "Vons", 95);
        //dbStatusHelper.addNewStatus("Body Wash", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Shampoo", "Suave 2 in 1", "Toiletries", "Vons", 96);
        //dbStatusHelper.addNewStatus("Shampoo", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Deodorant", "Old Spice", "Toiletries", "Vons", 97);
        //dbStatusHelper.addNewStatus("Deodorant", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Mouthwash", "Crest Whitening", "Toiletries", "Vons", 98);
        //dbStatusHelper.addNewStatus("Mouthwash", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cotton Swabs", "Q-Tips", "Toiletries", "Vons", 99);
        //dbStatusHelper.addNewStatus("Cotton Swabs", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Febreeze Air Spray", "Heavy Duty", "Household", "Vons", 100);
        //dbStatusHelper.addNewStatus("Febreeze Air Spray", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("All Purpose Cleaner", "Meyer's Lavender", "Household", "Vons", 101);
        //dbStatusHelper.addNewStatus("All Purpose Cleaner", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Laundry Detergent", "Woolite", "Household", "Vons", 102);
        //dbStatusHelper.addNewStatus("Laundry Detergent", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Laundry Sanitizer", "Lysol", "Household", "Vons", 103);
        //dbStatusHelper.addNewStatus("Laundry Sanitizer", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Aluminum Foil", "Reynolds Wrap", "Household", "Vons", 104);
        //dbStatusHelper.addNewStatus("Aluminum Foil", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Zip-Lock Bags (small)", "Sandwich", "Household", "Vons", 105);
        //dbStatusHelper.addNewStatus("Zip-Lock Bags (small)", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Zip-Lock Bags (large)", "Freezer Gallon", "Household", "Vons", 106);
        //dbStatusHelper.addNewStatus("Zip-Lock Bags (large)", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Saran Wrap", "Plastic Wrap", "Household", "Vons", 107);
        //dbStatusHelper.addNewStatus("Saran Wrap", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Scrub Sponges", "Non-Scratch", "Household", "Vons", 108);
        //dbStatusHelper.addNewStatus("Scrub Sponges", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Dawn Powerwash", "Dish Cleaner", "Household", "Vons", 109);
        //dbStatusHelper.addNewStatus("Dawn Powerwash", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Dish Soap", "Dawn Platinum", "Household", "Vons", 110);
        //dbStatusHelper.addNewStatus("Dish Soap", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Small Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 111);
        //dbStatusHelper.addNewStatus("Small Flour Tortillas", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Large Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 112);
        //dbStatusHelper.addNewStatus("Large Flour Tortillas", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sour Cream", "to do", "Eggs/Dairy", "Vons", 113);
        //dbStatusHelper.addNewStatus("Sour Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Buffalo Sauce", "Frank's Wings", "Condiments", "Vons", 114);
        //dbStatusHelper.addNewStatus("Buffalo Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Taco Sauce", "Victoria's Mild", "Condiments", "Vons", 115);
        //dbStatusHelper.addNewStatus("Taco Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Taco Seasoning", "any", "Seasonings", "Vons", 116);
        //dbStatusHelper.addNewStatus("Taco Seasoning", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Taco Shells", "to do", "Misc/Ingredients", "Vons", 117);
        //dbStatusHelper.addNewStatus("Taco Shells", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Tortellini", "Barilla 3 Cheese", "Meals", "Vons", 118);
        //dbStatusHelper.addNewStatus("Tortellini", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Garlic Breadsticks", "New York Bakery", "Sides", "Vons", 119);
        //dbStatusHelper.addNewStatus("Garlic Breadsticks", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Good & Plenty", "na", "Candy", "Vons", 120);
        //dbStatusHelper.addNewStatus("Good & Plenty", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sliced Cheese", "Kraft Singles", "Eggs/Dairy", "Vons", 121);
        //dbStatusHelper.addNewStatus("Sliced Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cat Treats", "Temptations", "Pet Supplies", "Vons", 122);
        //dbStatusHelper.addNewStatus("Cat Treats", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Saltine Crackers", "Premium Original", "Misc/Ingredients", "Vons", 123);
        //dbStatusHelper.addNewStatus("Saltine Crackers", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Semi-Sweet Choc. Chips", "Nestle", "Misc/Ingredients", "Vons", 124);
        //dbStatusHelper.addNewStatus("Semi-Sweet Choc. Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vegetable Oil", "Crisco", "Misc/Ingredients", "Vons", 125);
        //dbStatusHelper.addNewStatus("Vegetable Oil", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Extra Virgin Olive Oil", "to do", "Misc/Ingredients", "Vons", 126);
        //dbStatusHelper.addNewStatus("Extra Virgin Olive Oil", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Buttered Popcorn", "Movie Theater Butter", "Snacks", "Vons", 127);
        //dbStatusHelper.addNewStatus("Buttered Popcorn", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Caramel Popcorn", "Cretors", "Snacks", "Vons", 128);
        //dbStatusHelper.addNewStatus("Caramel Popcorn", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Caramel Swirl Popcorn", "Cretors", "Snacks", "Vons", 129);
        //dbStatusHelper.addNewStatus("Choc. Caramel Swirl Popcorn", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ritz Crackers", "Original", "Snacks", "Vons", 130);
        //dbStatusHelper.addNewStatus("Ritz Crackers", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Half & Half", "Lucerne", "Eggs/Dairy", "Vons", 131);
        //dbStatusHelper.addNewStatus("Half & Half", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Movie Theater Butter", "Kernel Seasons", "Misc/Ingredients", "Vons", 132);
        //dbStatusHelper.addNewStatus("Movie Theater Butter", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Vons", 133);
        storeData.getStoreViewInStockMap().put("Vons", 0);
        storeData.getStoreViewNeededMap().put("Vons", 0);
        storeData.getStoreViewPausedMap().put("Vons", 133);
        dbStoreHelper.setStoreViews("Vons", 133, 0, 0, 133);

        //------------------------------------Smart & Final-----------------------------------------

        dbItemHelper.addNewItemByStore("Churros", "Tio Pepe’s or Hola!", "Desserts", "Smart & Final", 0);
        //dbStatusHelper.addNewStatus("Churros", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Pepperoni Slices", "Hormel (300 slices)", "Meat", "Smart & Final", 1);
        //dbStatusHelper.addNewStatus("Pepperoni Slices", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Fun Dip Sticks", "na", "Candy", "Smart & Final", 2);
        //dbStatusHelper.addNewStatus("Fun Dip Sticks", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Orange Tic Tacs", "na", "Candy", "Smart & Final", 3);
        //dbStatusHelper.addNewStatus("Orange Tic Tacs", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Smart & Final", 4);
        storeData.getStoreViewInStockMap().put("Smart & Final", 0);
        storeData.getStoreViewNeededMap().put("Smart & Final", 0);
        storeData.getStoreViewPausedMap().put("Smart & Final", 4);
        dbStoreHelper.setStoreViews("Smart & Final", 4, 0, 0, 4);

        //------------------------------------Costco------------------------------------------------

        dbItemHelper.addNewItemByStore("Soda Cans", "Pepsi or Coke", "Drinks", "Costco", 0);
        //dbStatusHelper.addNewStatus("Soda Cans", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Kitty Litter", "Scoop Away Complete", "Pet Supplies", "Costco", 1);
        //dbStatusHelper.addNewStatus("Kitty Litter", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Chicken Broth", "Kirkland Organic", "Pet Supplies", "Costco", 2);
        //dbStatusHelper.addNewStatus("Chicken Broth", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Mashed Potatoes", "Main St. Bistro", "Pet Supplies", "Costco", 3);
        //dbStatusHelper.addNewStatus("Mashed Potatoes", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Freshpet", "Chicken Recipe (6lb)", "Pet Supplies", "Costco", 4);
        //dbStatusHelper.addNewStatus("Freshpet", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Oreo Muffins", "12 pack", "Desserts", "Costco", 5);
        //dbStatusHelper.addNewStatus("Oreo Muffins", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Crispy Buffalo Wings", "Foster Farms", "Meals", "Costco", 6);
        //dbStatusHelper.addNewStatus("Crispy Buffalo Wings", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Costco", 7);
        storeData.getStoreViewInStockMap().put("Costco", 0);
        storeData.getStoreViewNeededMap().put("Costco", 0);
        storeData.getStoreViewPausedMap().put("Costco", 7);
        dbStoreHelper.setStoreViews("Costco", 7, 0, 0, 7);

        //------------------------------------Walmart-----------------------------------------------

        dbItemHelper.addNewItemByStore("Dark Chocolate Caramel Squares", "Ghiradelli", "Candy", "Walmart", 0);
        //dbStatusHelper.addNewStatus("Dark Chocolate Caramel Squares", "paused", "unchecked");

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

        storeData.getStoreViewAllMap().put("Walmart", 8);
        storeData.getStoreViewInStockMap().put("Walmart", 0);
        storeData.getStoreViewNeededMap().put("Walmart", 0);
        storeData.getStoreViewPausedMap().put("Walmart", 8);
        dbStoreHelper.setStoreViews("Walmart", 8, 0, 0, 8);

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

        dbItemHelper.addNewItemByStore("Fortune Cookies", "to do", "Snacks", "Amazon", 15);
        //dbStatusHelper.addNewStatus("Fortune Cookies", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Communion Wafers", "to do", "Snacks", "Amazon", 16);
        //dbStatusHelper.addNewStatus("Communion Wafers", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sixlets", "na", "Candy", "Amazon", 17);
        //dbStatusHelper.addNewStatus("Sixlets", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Little Trees Air Fresheners", "True North", "Household", "Amazon", 18);
        //dbStatusHelper.addNewStatus("Little Trees Air Fresheners", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Triple Omega", "Nature Made", "Supplements", "Amazon", 19);
        //dbStatusHelper.addNewStatus("Triple Omega", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Multivitamin", "One a Day Men's", "Supplements", "Amazon", 20);
        //dbStatusHelper.addNewStatus("Multivitamin", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vitamin C", "Amazon Elements 1000 mg", "Supplements", "Amazon", 21);
        //dbStatusHelper.addNewStatus("Vitamin C", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Magnesium", "Nature Made 400mg", "Supplements", "Amazon", 22);
        //dbStatusHelper.addNewStatus("Magnesium", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Zinc", "Sandhu Herbals 50mg", "Supplements", "Amazon", 23);
        //dbStatusHelper.addNewStatus("Zinc", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Calcium", "Nature's Truth 1200 mg", "Supplements", "Amazon", 24);
        //dbStatusHelper.addNewStatus("Calcium", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Biotin", "Natrol 10,000mcg", "Supplements", "Amazon", 25);
        //dbStatusHelper.addNewStatus("Biotin", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vitamin D3", "Nature Made 5000 IU", "Supplements", "Amazon", 26);
        //dbStatusHelper.addNewStatus("Vitamin D3", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hyaluronic Acid", "Horbaach 1000mg", "Supplements", "Amazon", 27);
        //dbStatusHelper.addNewStatus("Hyaluronic Acid", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Amazon", 28);
        storeData.getStoreViewInStockMap().put("Amazon", 0);
        storeData.getStoreViewNeededMap().put("Amazon", 0);
        storeData.getStoreViewPausedMap().put("Amazon", 28);
        dbStoreHelper.setStoreViews("Amazon", 28, 0, 0, 28);

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
        storeData.getStoreViewInStockMap().put("Stater Bros", 0);
        storeData.getStoreViewNeededMap().put("Stater Bros", 0);
        storeData.getStoreViewPausedMap().put("Stater Bros", 5);
        dbStoreHelper.setStoreViews("Stater Bros", 5, 0, 0, 5);

        //------------------------------------Trader Joe's-------------------------------------------

        dbItemHelper.addNewItemByStore("Hold the Cone", "Chocolate", "Desserts", "Trader Joe's", 0);
        //dbStatusHelper.addNewStatus("Hold the Cone", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Dark Choc. Caramels", "Trader Joe's", "Candy", "Trader Joe's", 1);
        //dbStatusHelper.addNewStatus("Dark Choc. Caramels", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Trader Joe's", 2);
        storeData.getStoreViewInStockMap().put("Trader Joe's", 0);
        storeData.getStoreViewNeededMap().put("Trader Joe's", 0);
        storeData.getStoreViewPausedMap().put("Trader Joe's", 2);
        dbStoreHelper.setStoreViews("Trader Joe's", 2, 0, 0, 2);

        //------------------------------------CVS---------------------------------------------------

        dbItemHelper.addNewItemByStore("Dark Chocolate Pretzels", "Flipz", "Snacks", "CVS", 0);
        //dbStatusHelper.addNewStatus("Dark Chocolate Pretzels", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Candy Corn", "Brach's", "Candy", "CVS", 1);
        //dbStatusHelper.addNewStatus("Candy Corn", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Rubbing Alcohol", "Isopropyl", "Household", "CVS", 2);
        //dbStatusHelper.addNewStatus("Rubbing Alcohol", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hydrogen Peroxide", "na", "Household", "CVS", 3);
        //dbStatusHelper.addNewStatus("Hydrogen Peroxide", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Moisturizing Lotion", "CVS Health w/ hyaluronic acid", "Toiletries", "CVS", 4);
        //dbStatusHelper.addNewStatus("Moisturizing Lotion", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Packaging Tape", "Scotch Heavy Duty", "Household", "CVS", 5);
        //dbStatusHelper.addNewStatus("Packaging Tape", "paused", "unchecked");


        storeData.getStoreViewAllMap().put("CVS", 6);
        storeData.getStoreViewInStockMap().put("CVS", 0);
        storeData.getStoreViewNeededMap().put("CVS", 0);
        storeData.getStoreViewPausedMap().put("CVS", 6);
        dbStoreHelper.setStoreViews("CVS", 6, 0, 0, 6);

        //------------------------------------Dollar Tree-------------------------------------------

        dbItemHelper.addNewItemByStore("Sno Caps", "na", "Candy", "Dollar Tree", 0);
        //dbStatusHelper.addNewStatus("Sno Caps", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Smarties", "na", "Candy", "Dollar Tree", 1);
        //dbStatusHelper.addNewStatus("Smarties", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Red Hots", "na", "Candy", "Dollar Tree", 2);
        //dbStatusHelper.addNewStatus("Red Hots", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hand Soap", "Lavender & Chamomile", "Toiletries", "Dollar Tree", 3);
        //dbStatusHelper.addNewStatus("Hand Soap", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ramen Noodles", "Nissin", "Soups", "Dollar Tree", 4);
        //dbStatusHelper.addNewStatus("Ramen Noodles", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Dollar Tree", 5);
        storeData.getStoreViewInStockMap().put("Dollar Tree", 0);
        storeData.getStoreViewNeededMap().put("Dollar Tree", 0);
        storeData.getStoreViewPausedMap().put("Dollar Tree", 5);
        dbStoreHelper.setStoreViews("Dollar Tree", 5, 0, 0, 5);

        //------------------------------------Ralphs------------------------------------------------

        dbItemHelper.addNewItemByStore("Clarified Butter", "Challenge", "Eggs/Dairy", "Ralphs", 0);
        //dbStatusHelper.addNewStatus("Clarified Butter", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Ralphs", 1);
        storeData.getStoreViewInStockMap().put("Ralphs", 0);
        storeData.getStoreViewNeededMap().put("Ralphs", 0);
        storeData.getStoreViewPausedMap().put("Ralphs", 1);
        dbStoreHelper.setStoreViews("Ralphs", 1, 0, 0, 1);

        //------------------------------------Target------------------------------------------------

        dbItemHelper.addNewItemByStore("Chocolate Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 0);
        //dbStatusHelper.addNewStatus("Chocolate Syrup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Caramel Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 1);
        //dbStatusHelper.addNewStatus("Caramel Syrup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Dark Choc. M&M's", "na", "Candy", "Target", 2);
        //dbStatusHelper.addNewStatus("Dark Choc. M&M's", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sea Salt Caramels", "Favorite Day", "Candy", "Target", 3);
        //dbStatusHelper.addNewStatus("Sea Salt Caramels", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Oreo Pie Mix", "No Bake Dessert", "Desserts", "Target", 4);
        //dbStatusHelper.addNewStatus("Oreo Pie Mix", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Target", 5);
        storeData.getStoreViewInStockMap().put("Target", 0);
        storeData.getStoreViewNeededMap().put("Target", 0);
        storeData.getStoreViewPausedMap().put("Target", 5);
        dbStoreHelper.setStoreViews("Target", 5, 0, 0, 5);

        //------------------------------------Pet Supplies Plus-------------------------------------

        dbItemHelper.addNewItemByStore("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 0);
        //dbStatusHelper.addNewStatus("Cat Food (dry)", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Pet Supplies Plus", 1);
        storeData.getStoreViewInStockMap().put("Pet Supplies Plus", 0);
        storeData.getStoreViewNeededMap().put("Pet Supplies Plus", 0);
        storeData.getStoreViewPausedMap().put("Pet Supplies Plus", 1);
        dbStoreHelper.setStoreViews("Pet Supplies Plus", 1, 0, 0, 1);

        //------------------------------------Sprouts-------------------------------------------

        dbItemHelper.addNewItemByStore("Bar Soap", "Zum Bar Sea Salt", "Toiletries", "Sprouts", 0);
        //dbStatusHelper.addNewStatus("Bar Soap", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Toothbrush Heads", "Radius Soft", "Toiletries", "Sprouts", 1);
        //dbStatusHelper.addNewStatus("Toothbrush Heads", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Sprouts", 2);
        storeData.getStoreViewInStockMap().put("Sprouts", 0);
        storeData.getStoreViewNeededMap().put("Sprouts", 0);
        storeData.getStoreViewPausedMap().put("Sprouts", 2);
        dbStoreHelper.setStoreViews("Sprouts", 2, 0, 0, 2);

        //------------------------------------Sam's Club--------------------------------------------

        dbItemHelper.addNewItemByStore("Quick Steak", "Gary's", "Meat", "Sam's Club", 0);
        //dbStatusHelper.addNewStatus("Quick Steak", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Paper Plates", "to do", "Household", "Sam's Club", 1);
        //dbStatusHelper.addNewStatus("Paper Plates", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Sam's Club", 2);
        storeData.getStoreViewInStockMap().put("Sam's Club", 0);
        storeData.getStoreViewNeededMap().put("Sam's Club", 0);
        storeData.getStoreViewPausedMap().put("Sam's Club", 2);
        dbStoreHelper.setStoreViews("Sam's Club", 2, 0, 0, 2);

        //---------------------------------------Staples--------------------------------------------

        dbItemHelper.addNewItemByStore("Multipurpose Paper", "Tru Red 20/96", "Household", "Staples", 0);
        //dbStatusHelper.addNewStatus("Multipurpose Paper", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Staples", 1);
        storeData.getStoreViewInStockMap().put("Staples", 0);
        storeData.getStoreViewNeededMap().put("Staples", 0);
        storeData.getStoreViewPausedMap().put("Staples", 1);
        dbStoreHelper.setStoreViews("Staples", 1, 0, 0, 1);

        //---------------------------------------Woodranch--------------------------------------------

        dbItemHelper.addNewItemByStore("Woodranch BBQ Sauce", "(1 pint)", "Condiments", "Woodranch", 0);
        //dbStatusHelper.addNewStatus("Woodranch BBQ Sauce", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Woodranch", 1);
        storeData.getStoreViewInStockMap().put("Woodranch", 0);
        storeData.getStoreViewNeededMap().put("Woodranch", 0);
        storeData.getStoreViewPausedMap().put("Woodranch", 1);
        dbStoreHelper.setStoreViews("Woodranch", 1, 0, 0, 1);

        //------------------------------------Yorba Linda Feed Store--------------------------------

        dbItemHelper.addNewItemByStore("Dog Food (dry)", "Canidae All Life Stages", "Pet Supplies", "Yorba Linda Feed Store", 0);
        //dbStatusHelper.addNewStatus("Dog Food (dry)", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Yorba Linda Feed Store", 1);
        storeData.getStoreViewInStockMap().put("Yorba Linda Feed Store", 0);
        storeData.getStoreViewNeededMap().put("Yorba Linda Feed Store", 0);
        storeData.getStoreViewPausedMap().put("Yorba Linda Feed Store", 1);
        dbStoreHelper.setStoreViews("Yorba Linda Feed Store", 1, 0, 0, 1);

        //------------------------------------------------------------------------------------------

        // total store items = 212

    }

//------------------------------------------------------------------------------------------------//



//------------------------------------------------------------------------------------------------//
//-------------------------------------Sort By Category-------------------------------------------//
//------------------------------------------------------------------------------------------------//

    public void loadCategoryData2() {

        //------------------------------------Meals-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Sausage Biscuits", "Jimmy Dean Frozen", "Meals", "Vons", 0);
        dbStatusHelper.addNewStatus("Sausage Biscuits", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Hamburger Helper", "Cheeseburger Macaroni", "Meals", "Vons", 1);
        dbStatusHelper.addNewStatus("Hamburger Helper", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Buffalo Chicken Bites", "TGIF or Frank's", "Meals", "Vons", 2);
        dbStatusHelper.addNewStatus("Buffalo Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Terriyaki Chicken Bites", "InnovAsian", "Meals", "Vons", 3);
        dbStatusHelper.addNewStatus("Terriyaki Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Crispy Buffalo Wings", "Foster Farms", "Meals", "Costco", 4);
        dbStatusHelper.addNewStatus("Crispy Buffalo Wings", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("TGIF Cheese Sticks", "TGIF (small 10pc)", "Meals", "Vons", 5);
        dbStatusHelper.addNewStatus("TGIF Cheese Sticks", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Frozen Pizza", "Thin Pepperoni", "Meals", "Vons", 6);
        dbStatusHelper.addNewStatus("Frozen Pizza", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Corn Dogs", "Foster Farms", "Meals", "Vons", 7);
        dbStatusHelper.addNewStatus("Corn Dogs", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Hot Dogs", "Bun Size", "Meals", "Vons", 8);
        dbStatusHelper.addNewStatus("Hot Dogs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hot Dog Buns", "(8 pack)", "Meals", "Vons", 9);
        dbStatusHelper.addNewStatus("Hot Dog Buns", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hamburger Patties", "to do", "Meals", "Vons", 10);
        dbStatusHelper.addNewStatus("Hamburger Patties", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hamburger Buns", "(8 pack)", "Meals", "Vons", 11);
        dbStatusHelper.addNewStatus("Hamburger Buns", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Pasta Roni 1", "Angel Hair Pasta", "Meals", "Vons", 12);
        dbStatusHelper.addNewStatus("Pasta Roni 1", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Pasta Roni 2", "Fettuccine Alfredo", "Meals", "Vons", 13);
        dbStatusHelper.addNewStatus("Pasta Roni 2", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Mac & Cheese", "Annie’s", "Meals", "Vons", 14);
        dbStatusHelper.addNewStatus("Mac & Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Gnocci", "Signature Select", "Meals", "Vons", 15);
        dbStatusHelper.addNewStatus("Gnocci", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Tortellini", "Barilla 3 Cheese", "Meals", "Vons", 16);
        dbStatusHelper.addNewStatus("Tortellini", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Meals", 17);
        categoryData.getCategoryViewInStockMap().put("Meals", 4);
        categoryData.getCategoryViewNeededMap().put("Meals", 2);
        categoryData.getCategoryViewPausedMap().put("Meals", 11);
        dbCategoryHelper.setCategoryViews("Meals", 17, 4, 2, 11);

        //------------------------------------Soups-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Spaghetti O's", "w/ Meatballs", "Soups", "Vons", 0);
        dbStatusHelper.addNewStatus("Spaghetti O's", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Chicken Noodle Soup", "Campbell's", "Soups", "Vons", 1);
        dbStatusHelper.addNewStatus("Chicken Noodle Soup", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Minestrone Soup", "Amy's", "Soups", "Vons", 2);
        dbStatusHelper.addNewStatus("Minestrone Soup", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vegetable Barley Soup", "Amy's", "Soups", "Vons", 3);
        dbStatusHelper.addNewStatus("Vegetable Barley Soup", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Beef Noodles", "Yakisoba", "Soups", "Stater Bros", 4);
        dbStatusHelper.addNewStatus("Beef Noodles", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Cup of Noodles", "Nissin", "Soups", "Vons", 5);
        dbStatusHelper.addNewStatus("Cup of Noodles", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Ramen Noodles", "Nissin", "Soups", "Dollar Tree", 6);
        dbStatusHelper.addNewStatus("Ramen Noodles", "instock", "unchecked");

        categoryData.getCategoryViewAllMap().put("Soups", 7);
        categoryData.getCategoryViewInStockMap().put("Soups", 2);
        categoryData.getCategoryViewNeededMap().put("Soups", 2);
        categoryData.getCategoryViewPausedMap().put("Soups", 3);
        dbCategoryHelper.setCategoryViews("Soups", 7, 2, 2, 3);

        //------------------------------------Sides-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Frozen French Fries", "Ore-Ida", "Sides", "Vons", 0);
        dbStatusHelper.addNewStatus("Frozen French Fries", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Texas Cheesy Bread", "New York Bakery", "Sides", "Vons", 1);
        dbStatusHelper.addNewStatus("Texas Cheesy Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Garlic Breadsticks", "New York Bakery", "Sides", "Vons", 2);
        dbStatusHelper.addNewStatus("Garlic Breadsticks", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Chicken Rice", "Knorr", "Sides", "Vons", 3);
        dbStatusHelper.addNewStatus("Chicken Rice", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Canned Corn", "Del Monte", "Sides", "Vons", 4);
        dbStatusHelper.addNewStatus("Canned Corn", "instock", "unchecked");

        categoryData.getCategoryViewAllMap().put("Sides", 5);
        categoryData.getCategoryViewInStockMap().put("Sides", 2);
        categoryData.getCategoryViewNeededMap().put("Sides", 1);
        categoryData.getCategoryViewPausedMap().put("Sides", 2);
        dbCategoryHelper.setCategoryViews("Sides", 5, 2, 1, 2);

        //------------------------------------Meat--------------------------------------------------

        dbItemHelper.addNewItemByCategory("Steak", "USDA", "Meat", "Vons", 0);
        dbStatusHelper.addNewStatus("Steak", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Ground Beef", "(1 pound)", "Meat", "Vons", 1);
        dbStatusHelper.addNewStatus("Ground Beef", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Frozen Meatballs", "Rosina Homestyle", "Meat", "Vons", 2);
        dbStatusHelper.addNewStatus("Frozen Meatballs", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Pepperoni Slices", "Hormel (300 slices)", "Meat", "Smart & Final", 3);
        dbStatusHelper.addNewStatus("Pepperoni Slices", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Quick Steak", "Gary's", "Meat", "Sam's Club", 4);
        dbStatusHelper.addNewStatus("Quick Steak", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Chicken Breast", "na", "Meat", "Vons", 5);
        dbStatusHelper.addNewStatus("Chicken Breast", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sliced Turkey", "to do", "Meat", "Vons", 6);
        dbStatusHelper.addNewStatus("Sliced Turkey", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sliced Ham", "to do", "Meat", "Vons", 7);
        dbStatusHelper.addNewStatus("Sliced Ham", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Ham Steak", "to do", "Meat", "Vons", 8);
        dbStatusHelper.addNewStatus("Ham Steak", "needed", "unchecked");

        categoryData.getCategoryViewAllMap().put("Meat", 9);
        categoryData.getCategoryViewInStockMap().put("Meat", 3);
        categoryData.getCategoryViewNeededMap().put("Meat", 1);
        categoryData.getCategoryViewPausedMap().put("Meat", 5);
        dbCategoryHelper.setCategoryViews("Meat", 9, 3, 1, 5);

        //------------------------------------Bread/Grains/Cereal-----------------------------------

        dbItemHelper.addNewItemByCategory("Thin Spaghetti", "Barilla Whole Grain", "Bread/Grains/Cereal", "Vons", 0);
        dbStatusHelper.addNewStatus("Thin Spaghetti", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Spiral Pasta", "Barilla Rotini", "Bread/Grains/Cereal", "Vons", 1);
        dbStatusHelper.addNewStatus("Spiral Pasta", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Wheat Bread", "Nature's Own", "Bread/Grains/Cereal", "Vons", 2);
        dbStatusHelper.addNewStatus("Wheat Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Baguette", "French", "Bread/Grains/Cereal", "Vons", 3);
        dbStatusHelper.addNewStatus("Baguette", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sourdough Bread", "San Luis Sourdough", "Bread/Grains/Cereal", "Vons", 4);
        dbStatusHelper.addNewStatus("Sourdough Bread", "needed", "unchecked");

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

        dbItemHelper.addNewItemByCategory("Honey Smacks Cereal", "Honey Smacks", "Bread/Grains/Cereal", "Vons", 10);
        dbStatusHelper.addNewStatus("Honey Smacks Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Eggo Waffles", "Homestyle", "Bread/Grains/Cereal", "Vons", 11);
        dbStatusHelper.addNewStatus("Eggo Waffles", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Small Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 12);
        dbStatusHelper.addNewStatus("Small Flour Tortillas", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Large Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 13);
        dbStatusHelper.addNewStatus("Large Flour Tortillas", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Bread/Grains/Cereal", 14);
        categoryData.getCategoryViewInStockMap().put("Bread/Grains/Cereal", 2);
        categoryData.getCategoryViewNeededMap().put("Bread/Grains/Cereal", 1);
        categoryData.getCategoryViewPausedMap().put("Bread/Grains/Cereal", 11);
        dbCategoryHelper.setCategoryViews("Bread/Grains/Cereal", 14, 2, 1, 11);

        //----------------------------------------Eggs/Dairy----------------------------------------

        dbItemHelper.addNewItemByCategory("Milk", "Vitamin D", "Eggs/Dairy", "Vons", 0);
        dbStatusHelper.addNewStatus("Milk", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Eggs", "Grade AA", "Eggs/Dairy", "Vons", 1);
        dbStatusHelper.addNewStatus("Eggs", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Honey Yogurt", "Greek Gods", "Eggs/Dairy", "Vons", 2);
        dbStatusHelper.addNewStatus("Honey Yogurt", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sour Cream", "to do", "Eggs/Dairy", "Vons", 3);
        dbStatusHelper.addNewStatus("Sour Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Salted Butter", "Challenge", "Eggs/Dairy", "Vons", 4);
        dbStatusHelper.addNewStatus("Salted Butter", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Clarified Butter", "Challenge", "Eggs/Dairy", "Ralphs", 5);
        dbStatusHelper.addNewStatus("Clarified Butter", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Shredded Cheese", "Mexican Blend", "Eggs/Dairy", "Vons", 6);
        dbStatusHelper.addNewStatus("Shredded Cheese", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("String Cheese", "Mozarella", "Eggs/Dairy", "Vons", 7);
        dbStatusHelper.addNewStatus("String Cheese", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("BD Cheese", "Black Diamond", "Eggs/Dairy", "Vons", 8);
        dbStatusHelper.addNewStatus("BD Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sliced Cheese", "Kraft Singles", "Eggs/Dairy", "Vons", 9);
        dbStatusHelper.addNewStatus("Sliced Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Half & Half", "Lucerne", "Eggs/Dairy", "Vons", 10);
        dbStatusHelper.addNewStatus("Half & Half", "instock", "unchecked");

        categoryData.getCategoryViewAllMap().put("Eggs/Dairy", 11);
        categoryData.getCategoryViewInStockMap().put("Eggs/Dairy", 4);
        categoryData.getCategoryViewNeededMap().put("Eggs/Dairy", 2);
        categoryData.getCategoryViewPausedMap().put("Eggs/Dairy", 5);
        dbCategoryHelper.setCategoryViews("Eggs/Dairy", 11, 4, 2, 5);

        //------------------------------------Condiments--------------------------------------------

        dbItemHelper.addNewItemByCategory("Parmesan Cheese", "Kraft", "Condiments", "Vons", 0);
        dbStatusHelper.addNewStatus("Parmesan Cheese", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("A1 Sauce", "Original", "Condiments", "Vons", 1);
        dbStatusHelper.addNewStatus("A1 Sauce", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Ketchup", "Heinz", "Condiments", "Vons", 2);
        dbStatusHelper.addNewStatus("Ketchup", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Mustard", "Heinz", "Condiments", "Vons", 3);
        dbStatusHelper.addNewStatus("Mustard", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Pasta Sauce", "Ragu Meat", "Condiments", "Vons", 4);
        dbStatusHelper.addNewStatus("Pasta Sauce", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Taco Sauce", "Victoria's Mild", "Condiments", "Vons", 5);
        dbStatusHelper.addNewStatus("Taco Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Buffalo Sauce", "Frank's Wings", "Condiments", "Vons", 6);
        dbStatusHelper.addNewStatus("Buffalo Sauce", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Chocolate Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 7);
        dbStatusHelper.addNewStatus("Chocolate Syrup", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Caramel Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 8);
        dbStatusHelper.addNewStatus("Caramel Syrup", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Maple Syrup", "Pearl Milling", "Condiments", "Vons", 9);
        dbStatusHelper.addNewStatus("Maple Syrup", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Honey", "Local Hive Clover", "Condiments", "Vons", 10);
        dbStatusHelper.addNewStatus("Honey", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Peanut Butter", "Skippy Creamy", "Condiments", "Vons", 11);
        dbStatusHelper.addNewStatus("Peanut Butter", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Soy Sauce", "Kikoman", "Condiments", "Vons", 12);
        dbStatusHelper.addNewStatus("Soy Sauce", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Woodranch BBQ Sauce", "(1 pint)", "Condiments", "Woodranch", 13);
        dbStatusHelper.addNewStatus("Woodranch BBQ Sauce", "instock", "unchecked");

        categoryData.getCategoryViewAllMap().put("Condiments", 14);
        categoryData.getCategoryViewInStockMap().put("Condiments", 11);
        categoryData.getCategoryViewNeededMap().put("Condiments", 1);
        categoryData.getCategoryViewPausedMap().put("Condiments", 2);
        dbCategoryHelper.setCategoryViews("Condiments", 14, 11, 1, 2);

        //------------------------------------Seasonings--------------------------------------------

        dbItemHelper.addNewItemByCategory("Salt & Pepper", "na", "Seasonings", "Vons", 0);
        dbStatusHelper.addNewStatus("Salt & Pepper", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Garlic Salt", "Lawry's", "Seasonings", "Vons", 1);
        dbStatusHelper.addNewStatus("Garlic Salt", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Lawry's Seasoning Salt", "Lawry's", "Seasonings", "Vons", 2);
        dbStatusHelper.addNewStatus("Lawry's Seasoning Salt", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Taco Seasoning", "any", "Seasonings", "Vons", 3);
        dbStatusHelper.addNewStatus("Taco Seasoning", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Ranch Dip Mix", "Laura Scudder's", "Seasonings", "Vons", 4);
        dbStatusHelper.addNewStatus("Ranch Dip Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vanilla Extract", "Signature Select", "Seasonings", "Vons", 5);
        dbStatusHelper.addNewStatus("Vanilla Extract", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Cinnamon Sugar", "McCormick's", "Seasonings", "Vons", 6);
        dbStatusHelper.addNewStatus("Cinnamon Sugar", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Sprinkles", "3 types", "Seasonings", "Vons", 7);
        dbStatusHelper.addNewStatus("Sprinkles", "needed", "unchecked");

        categoryData.getCategoryViewAllMap().put("Seasonings", 8);
        categoryData.getCategoryViewInStockMap().put("Seasonings", 5);
        categoryData.getCategoryViewNeededMap().put("Seasonings", 1);
        categoryData.getCategoryViewPausedMap().put("Seasonings", 2);
        dbCategoryHelper.setCategoryViews("Seasonings", 8, 5, 1, 2);

        //---------------------------------Misc/Ingredients----------------------------------------

        dbItemHelper.addNewItemByCategory("Brown Sugar", "to do", "Misc/Ingredients", "Vons", 0);
        dbStatusHelper.addNewStatus("Brown Sugar", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Taco Shells", "to do", "Misc/Ingredients", "Vons", 1);
        dbStatusHelper.addNewStatus("Taco Shells", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Saltine Crackers", "Premium Original", "Misc/Ingredients", "Vons", 2);
        dbStatusHelper.addNewStatus("Saltine Crackers", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Semi-Sweet Choc. Chips", "Nestle", "Misc/Ingredients", "Vons", 3);
        dbStatusHelper.addNewStatus("Semi-Sweet Choc. Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Vegetable Oil", "Crisco", "Misc/Ingredients", "Vons", 4);
        dbStatusHelper.addNewStatus("Vegetable Oil", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Extra Virgin Olive Oil", "to do", "Misc/Ingredients", "Vons", 5);
        dbStatusHelper.addNewStatus("Extra Virgin Olive Oil", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Non-Stick Spray", "Pam Original", "Misc/Ingredients", "Vons", 6);
        dbStatusHelper.addNewStatus("Non-Stick Spray", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Movie Theater Butter", "Kernel Seasons", "Misc/Ingredients", "Vons", 7);
        dbStatusHelper.addNewStatus("Movie Theater Butter", "instock", "unchecked");

        categoryData.getCategoryViewAllMap().put("Misc/Ingredients", 8);
        categoryData.getCategoryViewInStockMap().put("Misc/Ingredients", 5);
        categoryData.getCategoryViewNeededMap().put("Misc/Ingredients", 1);
        categoryData.getCategoryViewPausedMap().put("Misc/Ingredients", 2);
        dbCategoryHelper.setCategoryViews("Misc/Ingredients", 8, 5, 1, 2);

        //------------------------------------Drinks------------------------------------------------

        dbItemHelper.addNewItemByCategory("Soda Bottles", "Pepsi or Coke", "Drinks", "Vons", 0);
        dbStatusHelper.addNewStatus("Soda Bottles", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Soda Cans", "Pepsi or Coke", "Drinks", "Costco", 1);
        dbStatusHelper.addNewStatus("Soda Cans", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Hot Chocolate Mix", "Swiss Miss Dark", "Drinks", "Vons", 2);
        dbStatusHelper.addNewStatus("Hot Chocolate Mix", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Bottled Water", "any", "Drinks", "Vons", 3);
        dbStatusHelper.addNewStatus("Bottled Water", "needed", "unchecked");

        categoryData.getCategoryViewAllMap().put("Drinks", 4);
        categoryData.getCategoryViewInStockMap().put("Drinks", 3);
        categoryData.getCategoryViewNeededMap().put("Drinks", 1);
        categoryData.getCategoryViewPausedMap().put("Drinks", 0);
        dbCategoryHelper.setCategoryViews("Drinks", 4, 3, 1, 0);

        //------------------------------------Snacks------------------------------------------------

        dbItemHelper.addNewItemByCategory("Beef Jerky", "Archer Terriyaki", "Snacks", "Vons", 0);
        dbStatusHelper.addNewStatus("Beef Jerky", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Peanuts", "Honey Roasted", "Snacks", "Vons", 1);
        dbStatusHelper.addNewStatus("Peanuts", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Shell Peanuts", "Salted", "Snacks", "Vons", 2);
        dbStatusHelper.addNewStatus("Shell Peanuts", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sunflower Seeds", "Salted", "Snacks", "Vons", 3);
        dbStatusHelper.addNewStatus("Sunflower Seeds", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Vinegar Chips", "Kettle", "Snacks", "Vons", 4);
        dbStatusHelper.addNewStatus("Vinegar Chips", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("BBQ Chips", "Kettle", "Snacks", "Vons", 5);
        dbStatusHelper.addNewStatus("BBQ Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Doritos", "Cool Ranch", "Snacks", "Vons", 6);
        dbStatusHelper.addNewStatus("Doritos", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Lay's Chips", "Classic", "Snacks", "Vons", 7);
        dbStatusHelper.addNewStatus("Lay's Chips", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Naan Crisps", "Stonefire", "Snacks", "Vons", 8);
        dbStatusHelper.addNewStatus("Naan Crisps", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Ritz Crackers", "Original", "Snacks", "Vons", 9);
        dbStatusHelper.addNewStatus("Ritz Crackers", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Goldfish", "Cheddar", "Snacks", "Vons", 10);
        dbStatusHelper.addNewStatus("Goldfish", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Cheez-Its", "Original", "Snacks", "Vons", 11);
        dbStatusHelper.addNewStatus("Cheez-Its", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Famous Amos Cookies", "12 Pack", "Snacks", "Vons", 12);
        dbStatusHelper.addNewStatus("Famous Amos Cookies", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dark Chocolate Pretzels", "Flipz", "Snacks", "CVS", 13);
        dbStatusHelper.addNewStatus("Dark Chocolate Pretzels", "instock", "unchecked");

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
        dbStatusHelper.addNewStatus("Buttered Popcorn", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Caramel Popcorn", "Cretors", "Snacks", "Vons", 20);
        dbStatusHelper.addNewStatus("Caramel Popcorn", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Caramel Swirl Popcorn", "Cretors", "Snacks", "Vons", 21);
        dbStatusHelper.addNewStatus("Choc. Caramel Swirl Popcorn", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Snacks", 22);
        categoryData.getCategoryViewInStockMap().put("Snacks", 4);
        categoryData.getCategoryViewNeededMap().put("Snacks", 4);
        categoryData.getCategoryViewPausedMap().put("Snacks", 14);
        dbCategoryHelper.setCategoryViews("Snacks", 22, 4, 4, 14);

        //------------------------------------Desserts----------------------------------------------

        dbItemHelper.addNewItemByCategory("Choc. Malted Crunch Ice Cream", "Thrifty", "Desserts", "Vons", 0);
        dbStatusHelper.addNewStatus("Choc. Malted Crunch Ice Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hold the Cone", "Chocolate", "Desserts", "Trader Joe's", 1);
        dbStatusHelper.addNewStatus("Hold the Cone", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Churros", "Tio Pepe’s or Hola!", "Desserts", "Smart & Final", 2);
        dbStatusHelper.addNewStatus("Churros", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 3);
        dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Chip Cookie Mix", "Gluten Free", "Desserts", "Stater Bros", 4);
        dbStatusHelper.addNewStatus("Choc. Chip Cookie Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Gingerbread Cookie Mix", "Betty Crocker", "Desserts", "Amazon", 5);
        dbStatusHelper.addNewStatus("Gingerbread Cookie Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreos", "(for crumbs)", "Desserts", "Vons", 6);
        dbStatusHelper.addNewStatus("Oreos", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreo Muffins", "12 pack", "Desserts", "Costco", 7);
        dbStatusHelper.addNewStatus("Oreo Muffins", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreo Cakesters", "Nabisco", "Desserts", "Vons", 8);
        dbStatusHelper.addNewStatus("Oreo Cakesters", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Oreo Pie Mix", "No Bake Dessert", "Desserts", "Target", 9);
        dbStatusHelper.addNewStatus("Oreo Pie Mix", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Choc. Malt Mix", "Nestle", "Desserts", "Stater Bros", 10);
        dbStatusHelper.addNewStatus("Choc. Malt Mix", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Desserts", 11);
        categoryData.getCategoryViewInStockMap().put("Desserts", 2);
        categoryData.getCategoryViewNeededMap().put("Desserts", 2);
        categoryData.getCategoryViewPausedMap().put("Desserts", 7);
        dbCategoryHelper.setCategoryViews("Desserts", 11, 0, 0, 11);

        //------------------------------------Candy-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Dark Chocolate Caramel Squares", "Ghiradelli", "Candy", "Walmart", 0);
        dbStatusHelper.addNewStatus("Dark Chocolate Caramel Squares", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Reese's PB Cups", "(individually wrapped)", "Candy", "Vons", 1);
        dbStatusHelper.addNewStatus("Reese's PB Cups", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Candy Corn", "Brach's", "Candy", "CVS", 2);
        dbStatusHelper.addNewStatus("Candy Corn", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Hot Tamales", "na", "Candy", "Vons", 3);
        dbStatusHelper.addNewStatus("Hot Tamales", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Smarties", "na", "Candy", "Dollar  Tree", 4);
        dbStatusHelper.addNewStatus("Smarties", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Sno Caps", "na", "Candy", "Dollar Tree", 5);
        dbStatusHelper.addNewStatus("Sno Caps", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Good & Plenty", "na", "Candy", "Vons", 6);
        dbStatusHelper.addNewStatus("Good & Plenty", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Mini M&M's", "na", "Candy", "Vons", 7);
        dbStatusHelper.addNewStatus("Mini M&M's", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Dark Choc. M&M's", "na", "Candy", "Target", 8);
        dbStatusHelper.addNewStatus("Dark Choc. M&M's", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Sea Salt Caramels", "Favorite Day", "Candy", "Target", 9);
        dbStatusHelper.addNewStatus("Sea Salt Caramels", "instock", "unchecked");

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

        dbItemHelper.addNewItemByCategory("Red Hots", "na", "Candy", "Dollar Tree", 19);
        dbStatusHelper.addNewStatus("Red Hots", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Dark Choc. Caramels", "Trader Joe's", "Candy", "Trader Joe's", 20);
        dbStatusHelper.addNewStatus("Dark Choc. Caramels", "paused", "unchecked");

        categoryData.getCategoryViewAllMap().put("Candy", 21);
        categoryData.getCategoryViewInStockMap().put("Candy", 3);
        categoryData.getCategoryViewNeededMap().put("Candy", 2);
        categoryData.getCategoryViewPausedMap().put("Candy", 16);
        dbCategoryHelper.setCategoryViews("Candy", 21, 3, 2, 16);

        //------------------------------------Pet Supplies-------------------------------------------

        dbItemHelper.addNewItemByCategory("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 0);
        dbStatusHelper.addNewStatus("Cat Food (wet)", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 1);
        dbStatusHelper.addNewStatus("Cat Food (dry)", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Delectables", "Squeeze Up 20 pack", "Pet Supplies", "Vons", 2);
        dbStatusHelper.addNewStatus("Delectables", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Cat Treats", "Temptations", "Pet Supplies", "Vons", 3);
        dbStatusHelper.addNewStatus("Cat Treats", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Kitty Litter", "Scoop Away Complete", "Pet Supplies", "Costco", 4);
        dbStatusHelper.addNewStatus("Kitty Litter", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Dog Food (dry)", "Canidae All Life Stages", "Pet Supplies", "Yorba Linda Feed Store", 5);
        dbStatusHelper.addNewStatus("Dog Food (dry)", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Chicken Broth", "Kirkland Organic", "Pet Supplies", "Costco", 6);
        dbStatusHelper.addNewStatus("Chicken Broth", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Mashed Potatoes", "Main St. Bistro", "Pet Supplies", "Costco", 7);
        dbStatusHelper.addNewStatus("Mashed Potatoes", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Freshpet", "Chicken Recipe (6lb)", "Pet Supplies", "Costco", 8);
        dbStatusHelper.addNewStatus("Freshpet", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("100% Pure Pumpkin", "Libby's", "Pet Supplies", "Vons", 9);
        dbStatusHelper.addNewStatus("100% Pure Pumpkin", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Poop Bags", "Amazon Basics", "Pet Supplies", "Amazon", 10);
        dbStatusHelper.addNewStatus("Poop Bags", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Nitrile Gloves", "GMG 100 pack", "Pet Supplies", "Amazon", 11);
        dbStatusHelper.addNewStatus("Nitrile Gloves", "instock", "unchecked");

        categoryData.getCategoryViewAllMap().put("Pet Supplies", 12);
        categoryData.getCategoryViewInStockMap().put("Pet Supplies", 9);
        categoryData.getCategoryViewNeededMap().put("Pet Supplies", 2);
        categoryData.getCategoryViewPausedMap().put("Pet Supplies", 1);
        dbCategoryHelper.setCategoryViews("Pet Supplies", 12, 9, 2, 1);

        //------------------------------------Toiletries--------------------------------------------

        dbItemHelper.addNewItemByCategory("Hand Soap", "Lavender & Chamomile", "Toiletries", "Dollar Tree", 0);
        dbStatusHelper.addNewStatus("Hand Soap", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Body Wash", "Suave Mandarin", "Toiletries", "Vons", 1);
        dbStatusHelper.addNewStatus("Body Wash", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Shampoo", "Suave 2 in 1", "Toiletries", "Vons", 2);
        dbStatusHelper.addNewStatus("Shampoo", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Bar Soap", "Zum Bar Sea Salt", "Toiletries", "Sprouts", 3);
        dbStatusHelper.addNewStatus("Bar Soap", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Deodorant", "Old Spice", "Toiletries", "Vons", 4);
        dbStatusHelper.addNewStatus("Deodorant", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Toothpaste", "Tom's Antiplaque & Whitening", "Toiletries", "Amazon", 5);
        dbStatusHelper.addNewStatus("Toothpaste", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Floss", "Reach Mint Waxed", "Toiletries", "Amazon", 6);
        dbStatusHelper.addNewStatus("Floss", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Shaving Cream", "Sandalwood", "Toiletries", "Amazon", 7);
        dbStatusHelper.addNewStatus("Shaving Cream", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Shaving Razors", "Gillette ProGlide", "Toiletries", "Amazon", 8);
        dbStatusHelper.addNewStatus("Shaving Razors", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Mouthwash", "Crest Whitening", "Toiletries", "Vons", 9);
        dbStatusHelper.addNewStatus("Mouthwash", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Cotton Swabs", "Q-Tips", "Toiletries", "Vons", 10);
        dbStatusHelper.addNewStatus("Cotton Swabs", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Toothbrush Heads", "Radius Soft", "Toiletries", "Sprouts", 11);
        dbStatusHelper.addNewStatus("Toothbrush Heads", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Sunscreen", "Hawaiian Tropic Sheer 50spf", "Toiletries", "Amazon", 12);
        dbStatusHelper.addNewStatus("Sunscreen", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Moisturizing Lotion", "CVS Health w/ hyaluronic acid", "Toiletries", "CVS", 13);
        dbStatusHelper.addNewStatus("Moisturizing Lotion", "instock", "unchecked");

        categoryData.getCategoryViewAllMap().put("Toiletries", 14);
        categoryData.getCategoryViewInStockMap().put("Toiletries", 13);
        categoryData.getCategoryViewNeededMap().put("Toiletries", 1);
        categoryData.getCategoryViewPausedMap().put("Toiletries", 0);
        dbCategoryHelper.setCategoryViews("Toiletries", 14, 13, 1, 0);

        //------------------------------------Household-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Febreeze Air Spray", "Heavy Duty", "Household", "Vons", 0);
        dbStatusHelper.addNewStatus("Febreeze Air Spray", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("All Purpose Cleaner", "Meyer's Lavender", "Household", "Vons", 1);
        dbStatusHelper.addNewStatus("All Purpose Cleaner", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Pet Stain Cleaner", "Rocco & Roxie", "Household", "Amazon", 2);
        dbStatusHelper.addNewStatus("Pet Stain Cleaner", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Laundry Detergent", "Woolite", "Household", "Vons", 3);
        dbStatusHelper.addNewStatus("Laundry Detergent", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Laundry Sanitizer", "Lysol", "Household", "Vons", 4);
        dbStatusHelper.addNewStatus("Laundry Sanitizer", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Dryer Sheets", "Simply Done Fresh Linen", "Household", "Stater Bros", 5);
        dbStatusHelper.addNewStatus("Dryer Sheets", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Little Trees Air Fresheners", "True North", "Household", "Amazon", 6);
        dbStatusHelper.addNewStatus("Little Trees Air Fresheners", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Aluminum Foil", "Reynolds Wrap", "Household", "Vons", 7);
        dbStatusHelper.addNewStatus("Aluminum Foil", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Zip-Lock Bags (small)", "Sandwich", "Household", "Vons", 8);
        dbStatusHelper.addNewStatus("Zip-Lock Bags (small)", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Zip-Lock Bags (large)", "Freezer Gallon", "Household", "Vons", 9);
        dbStatusHelper.addNewStatus("Zip-Lock Bags (large)", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Saran Wrap", "Plastic Wrap", "Household", "Vons", 10);
        dbStatusHelper.addNewStatus("Saran Wrap", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Rubbing Alcohol", "Isopropyl", "Household", "CVS", 11);
        dbStatusHelper.addNewStatus("Rubbing Alcohol", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Hydrogen Peroxide", "na", "Household", "CVS", 12);
        dbStatusHelper.addNewStatus("Hydrogen Peroxide", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Night Light Bulbs", "C7 E12", "Household", "Amazon", 13);
        dbStatusHelper.addNewStatus("Night Light Bulbs", "paused", "unchecked");

        dbItemHelper.addNewItemByCategory("Scrub Sponges", "Non-Scratch", "Household", "Vons", 14);
        dbStatusHelper.addNewStatus("Scrub Sponges", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Dishwashing Brush", "Great Value", "Household", "Walmart", 15);
        dbStatusHelper.addNewStatus("Dishwashing Brush", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Small Trash Bags", "13 gallon", "Household", "Walmart", 16);
        dbStatusHelper.addNewStatus("Small Trash Bags", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Large Trash Bags", "33 gallon", "Household", "Walmart", 17);
        dbStatusHelper.addNewStatus("Large Trash Bags", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Compactor Bags", "18 gallon", "Household", "Walmart", 18);
        dbStatusHelper.addNewStatus("Compactor Bags", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Dawn Powerwash", "Dish Cleaner", "Household", "Vons", 19);
        dbStatusHelper.addNewStatus("Dawn Powerwash", "needed", "unchecked");

        dbItemHelper.addNewItemByCategory("Dish Soap", "Dawn Platinum", "Household", "Vons", 20);
        dbStatusHelper.addNewStatus("Dish Soap", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Paper Plates", "to do", "Household", "Sam's Club", 21);
        dbStatusHelper.addNewStatus("Paper Plates", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Paper Towels", "Sparkle", "Household", "Walmart", 22);
        dbStatusHelper.addNewStatus("Paper Towels", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Toilet Paper", "Angel Soft", "Household", "Walmart", 23);
        dbStatusHelper.addNewStatus("Toilet Paper", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Multipurpose Paper", "Tru Red 20/96", "Household", "Staples", 24);
        dbStatusHelper.addNewStatus("Multipurpose Paper", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Packaging Tape", "Scotch Heavy Duty", "Household", "CVS", 25);
        dbStatusHelper.addNewStatus("Packaging Tape", "instock", "unchecked");

        categoryData.getCategoryViewAllMap().put("Household", 26);
        categoryData.getCategoryViewInStockMap().put("Household", 22);
        categoryData.getCategoryViewNeededMap().put("Household", 3);
        categoryData.getCategoryViewPausedMap().put("Household", 1);
        dbCategoryHelper.setCategoryViews("Household", 26, 22, 3, 1);

        //------------------------------------Supplements-------------------------------------------------

        dbItemHelper.addNewItemByCategory("Triple Omega", "Nature Made", "Supplements", "Amazon", 0);
        dbStatusHelper.addNewStatus("Triple Omega", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Multivitamin", "One a Day Men's", "Supplements", "Amazon", 1);
        dbStatusHelper.addNewStatus("Multivitamin", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Vitamin C", "Amazon Elements 1000 mg", "Supplements", "Amazon", 2);
        dbStatusHelper.addNewStatus("Vitamin C", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Magnesium", "Nature Made 400mg", "Supplements", "Amazon", 3);
        dbStatusHelper.addNewStatus("Magnesium", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Zinc", "Sandhu Herbals 50mg", "Supplements", "Amazon", 4);
        dbStatusHelper.addNewStatus("Zinc", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Calcium", "Nature's Truth 1200 mg", "Supplements", "Amazon", 5);
        dbStatusHelper.addNewStatus("Calcium", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Biotin", "Natrol 10,000mcg", "Supplements", "Amazon", 6);
        dbStatusHelper.addNewStatus("Biotin", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Vitamin D3", "Nature Made 5000 IU", "Supplements", "Amazon", 7);
        dbStatusHelper.addNewStatus("Vitamin D3", "instock", "unchecked");

        dbItemHelper.addNewItemByCategory("Hyaluronic Acid", "Horbaach 1000mg", "Supplements", "Amazon", 8);
        dbStatusHelper.addNewStatus("Hyaluronic Acid", "instock", "unchecked");

        categoryData.getCategoryViewAllMap().put("Supplements", 9);
        categoryData.getCategoryViewInStockMap().put("Supplements", 9);
        categoryData.getCategoryViewNeededMap().put("Supplements", 0);
        categoryData.getCategoryViewPausedMap().put("Supplements", 0);
        dbCategoryHelper.setCategoryViews("Supplements", 9, 9, 0, 0);

        //------------------------------------------------------------------------------------------

        // total category items = 212

    }

//------------------------------------------------------------------------------------------------//
//----------------------------------------Sort By Store-------------------------------------------//
//------------------------------------------------------------------------------------------------//

    public void loadStoreData2() {

        //------------------------------------Vons--------------------------------------------------

        dbItemHelper.addNewItemByStore("Sausage Biscuits", "Jimmy Dean Frozen", "Meals", "Vons", 0);
        //dbStatusHelper.addNewStatus("Sausage Biscuits", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Hamburger Helper", "Cheeseburger Macaroni", "Meals", "Vons", 1);
        //dbStatusHelper.addNewStatus("Hamburger Helper", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Buffalo Chicken Bites", "TGIF or Frank's", "Meals", "Vons", 2);
        //dbStatusHelper.addNewStatus("Buffalo Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Terriyaki Chicken Bites", "InnovAsian", "Meals", "Vons", 3);
        //dbStatusHelper.addNewStatus("Terriyaki Chicken Bites", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("TGIF Cheese Sticks", "TGIF (small 10pc)", "Meals", "Vons", 4);
        //dbStatusHelper.addNewStatus("TGIF Cheese Sticks", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Frozen Pizza", "Thin Pepperoni", "Meals", "Vons", 5);
        //dbStatusHelper.addNewStatus("Frozen Pizza", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Corn Dogs", "Foster Farms", "Meals", "Vons", 6);
        //dbStatusHelper.addNewStatus("Corn Dogs", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Hot Dogs", "Bun Size", "Meals", "Vons", 7);
        //dbStatusHelper.addNewStatus("Hot Dogs", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hot Dog Buns", "(8 pack)", "Meals", "Vons", 8);
        //dbStatusHelper.addNewStatus("Hot Dog Buns", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hamburger Patties", "to do", "Meals", "Vons", 9);
        //dbStatusHelper.addNewStatus("Hamburger Patties", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hamburger Buns", "(8 pack)", "Meals", "Vons", 10);
        //dbStatusHelper.addNewStatus("Hamburger Buns", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Pasta Roni 1", "Angel Hair Pasta", "Meals", "Vons", 11);
        //dbStatusHelper.addNewStatus("Pasta Roni 1", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Pasta Roni 2", "Fettuccine Alfredo", "Meals", "Vons", 12);
        //dbStatusHelper.addNewStatus("Pasta Roni 2", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Mac & Cheese", "Annie’s", "Meals", "Vons", 13);
        //dbStatusHelper.addNewStatus("Mac & Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Gnocci", "Signature Select", "Meals", "Vons", 14);
        //dbStatusHelper.addNewStatus("Gnocci", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Spaghetti O's", "w/ Meatballs", "Soups", "Vons", 15);
        //dbStatusHelper.addNewStatus("Spaghetti O's", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Chicken Noodle Soup", "Campbell's", "Soups", "Vons", 16);
        //dbStatusHelper.addNewStatus("Chicken Noodle Soup", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Minestrone Soup", "Amy's", "Soups", "Vons", 17);
        //dbStatusHelper.addNewStatus("Minestrone Soup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vegetable Barley Soup", "Amy's", "Soups", "Vons", 18);
        //dbStatusHelper.addNewStatus("Vegetable Barley Soup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cup of Noodles", "Nissin", "Soups", "Vons", 19);
        //dbStatusHelper.addNewStatus("Cup of Noodles", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Frozen French Fries", "Ore-Ida", "Sides", "Vons", 20);
        //dbStatusHelper.addNewStatus("Frozen French Fries", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Texas Cheesy Bread", "New York Bakery", "Sides", "Vons", 21);
        //dbStatusHelper.addNewStatus("Texas Cheesy Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Chicken Rice", "Knorr", "Sides", "Vons", 22);
        //dbStatusHelper.addNewStatus("Chicken Rice", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Canned Corn", "Del Monte", "Sides", "Vons", 23);
        //dbStatusHelper.addNewStatus("Canned Corn", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Steak", "USDA", "Meat", "Vons", 24);
        //dbStatusHelper.addNewStatus("Steak", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ground Beef", "(1 pound)", "Meat", "Vons", 25);
        //dbStatusHelper.addNewStatus("Ground Beef", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Frozen Meatballs", "Rosina Homestyle", "Meat", "Vons", 26);
        //dbStatusHelper.addNewStatus("Frozen Meatballs", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Chicken Breast", "na", "Meat", "Vons", 27);
        //dbStatusHelper.addNewStatus("Chicken Breast", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sliced Turkey", "to do", "Meat", "Vons", 28);
        //dbStatusHelper.addNewStatus("Sliced Turkey", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sliced Ham", "to do", "Meat", "Vons", 29);
        //dbStatusHelper.addNewStatus("Sliced Ham", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ham Steak", "to do", "Meat", "Vons", 30);
        //dbStatusHelper.addNewStatus("Ham Steak", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Thin Spaghetti", "Barilla Whole Grain", "Bread/Grains/Cereal", "Vons", 31);
        //dbStatusHelper.addNewStatus("Thin Spaghetti", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Spiral Pasta", "Barilla Rotini", "Bread/Grains/Cereal", "Vons", 32);
        //dbStatusHelper.addNewStatus("Spiral Pasta", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Wheat Bread", "Nature's Own", "Bread/Grains/Cereal", "Vons", 33);
        //dbStatusHelper.addNewStatus("Wheat Bread", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Baguette", "French", "Bread/Grains/Cereal", "Vons", 34);
        //dbStatusHelper.addNewStatus("Baguette", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sourdough Bread", "San Luis Sourdough", "Bread/Grains/Cereal", "Vons", 35);
        //dbStatusHelper.addNewStatus("Sourdough Bread", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Hard Rolls", "to do", "Bread/Grains/Cereal", "Vons", 36);
        //dbStatusHelper.addNewStatus("Hard Rolls", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Thomas Muffins", "Original", "Bread/Grains/Cereal", "Vons", 37);
        //dbStatusHelper.addNewStatus("Thomas Muffins", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Reese's Puffs Cereal", "Reese's Puffs", "Bread/Grains/Cereal", "Vons", 38);
        //dbStatusHelper.addNewStatus("Reese's Puffs Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cookie Crisp Cereal", "Cookie Crisp", "Bread/Grains/Cereal", "Vons", 39);
        //dbStatusHelper.addNewStatus("Cookie Crisp Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Frosted Mini Wheat Cereal", "Frosted Mini Wheat", "Bread/Grains/Cereal", "Vons", 40);
        //dbStatusHelper.addNewStatus("Frosted Mini Wheat Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Honey Smacks Cereal", "Honey Smacks", "Bread/Grains/Cereal", "Vons", 41);
        //dbStatusHelper.addNewStatus("Honey Smacks Cereal", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Eggo Waffles", "Homestyle", "Bread/Grains/Cereal", "Vons", 42);
        //dbStatusHelper.addNewStatus("Eggo Waffles", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Milk", "Vitamin D", "Eggs/Dairy", "Vons", 43);
        //dbStatusHelper.addNewStatus("Milk", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Eggs", "Grade AA", "Eggs/Dairy", "Vons", 44);
        //dbStatusHelper.addNewStatus("Eggs", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Honey Yogurt", "Greek Gods", "Eggs/Dairy", "Vons", 45);
        //dbStatusHelper.addNewStatus("Honey Yogurt", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Salted Butter", "Challenge", "Eggs/Dairy", "Vons", 46);
        //dbStatusHelper.addNewStatus("Salted Butter", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Shredded Cheese", "Mexican Blend", "Eggs/Dairy", "Vons", 47);
        //dbStatusHelper.addNewStatus("Shredded Cheese", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("String Cheese", "Mozarella", "Eggs/Dairy", "Vons", 48);
        //dbStatusHelper.addNewStatus("String Cheese", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("BD Cheese", "Black Diamond", "Eggs/Dairy", "Vons", 49);
        //dbStatusHelper.addNewStatus("BD Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Non-Stick Spray", "Pam Original", "Misc/Ingredients", "Vons", 50);
        //dbStatusHelper.addNewStatus("Non-Stick Spray", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Parmesan Cheese", "Kraft", "Condiments", "Vons", 51);
        //dbStatusHelper.addNewStatus("Parmesan Cheese", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("A1 Sauce", "Original", "Condiments", "Vons", 52);
        //dbStatusHelper.addNewStatus("A1 Sauce", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Ketchup", "Heinz", "Condiments", "Vons", 53);
        //dbStatusHelper.addNewStatus("Ketchup", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Mustard", "Heinz", "Condiments", "Vons", 54);
        //dbStatusHelper.addNewStatus("Mustard", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Pasta Sauce", "Ragu Meat", "Condiments", "Vons", 55);
        //dbStatusHelper.addNewStatus("Pasta Sauce", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Maple Syrup", "Pearl Milling", "Condiments", "Vons", 56);
        //dbStatusHelper.addNewStatus("Maple Syrup", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Honey", "Local Hive Clover", "Condiments", "Vons", 57);
        //dbStatusHelper.addNewStatus("Honey", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Peanut Butter", "Skippy Creamy", "Condiments", "Vons", 58);
        //dbStatusHelper.addNewStatus("Peanut Butter", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Soy Sauce", "Kikoman", "Condiments", "Vons", 59);
        //dbStatusHelper.addNewStatus("Soy Sauce", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Brown Sugar", "to do", "Misc/Ingredients", "Vons", 60);
        //dbStatusHelper.addNewStatus("Brown Sugar", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Salt & Pepper", "na", "Seasonings", "Vons", 61);
        //dbStatusHelper.addNewStatus("Salt & Pepper", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Garlic Salt", "Lawry's", "Seasonings", "Vons", 62);
        //dbStatusHelper.addNewStatus("Garlic Salt", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Lawry's Seasoning Salt", "Lawry's", "Seasonings", "Vons", 63);
        //dbStatusHelper.addNewStatus("Lawry's Seasoning Salt", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Ranch Dip Mix", "Laura Scudder's", "Seasonings", "Vons", 64);
        //dbStatusHelper.addNewStatus("Ranch Dip Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vanilla Extract", "Signature Select", "Seasonings", "Vons", 65);
        //dbStatusHelper.addNewStatus("Vanilla Extract", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Cinnamon Sugar", "McCormick's", "Seasonings", "Vons", 66);
        //dbStatusHelper.addNewStatus("Cinnamon Sugar", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Sprinkles", "3 types", "Seasonings", "Vons", 67);
        //dbStatusHelper.addNewStatus("Sprinkles", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Soda Bottles", "Pepsi or Coke", "Drinks", "Vons", 68);
        //dbStatusHelper.addNewStatus("Soda Bottles", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Hot Chocolate Mix", "Swiss Miss Dark", "Drinks", "Vons", 69);
        //dbStatusHelper.addNewStatus("Hot Chocolate Mix", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Bottled Water", "any", "Drinks", "Vons", 70);
        //dbStatusHelper.addNewStatus("Bottled Water", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Beef Jerky", "Archer Terriyaki", "Snacks", "Vons", 71);
        //dbStatusHelper.addNewStatus("Beef Jerky", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Peanuts", "Honey Roasted", "Snacks", "Vons", 72);
        //dbStatusHelper.addNewStatus("Peanuts", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Shell Peanuts", "Salted", "Snacks", "Vons", 73);
        //dbStatusHelper.addNewStatus("Shell Peanuts", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sunflower Seeds", "Salted", "Snacks", "Vons", 74);
        //dbStatusHelper.addNewStatus("Sunflower Seeds", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Vinegar Chips", "Kettle", "Snacks", "Vons", 75);
        //dbStatusHelper.addNewStatus("Vinegar Chips", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("BBQ Chips", "Kettle", "Snacks", "Vons", 76);
        //dbStatusHelper.addNewStatus("BBQ Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Doritos", "Cool Ranch", "Snacks", "Vons", 77);
        //dbStatusHelper.addNewStatus("Doritos", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Lay's Chips", "Classic", "Snacks", "Vons", 78);
        //dbStatusHelper.addNewStatus("Lay's Chips", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Naan Crisps", "Stonefire", "Snacks", "Vons", 79);
        //dbStatusHelper.addNewStatus("Naan Crisps", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Oreo Cakesters", "Nabisco", "Snacks", "Vons", 80);
        //dbStatusHelper.addNewStatus("Oreo Cakesters", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Goldfish", "Cheddar", "Snacks", "Vons", 81);
        //dbStatusHelper.addNewStatus("Goldfish", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cheez-Its", "Original", "Snacks", "Vons", 82);
        //dbStatusHelper.addNewStatus("Cheez-Its", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Famous Amos Cookies", "12 Pack", "Snacks", "Vons", 83);
        //dbStatusHelper.addNewStatus("Famous Amos Cookies", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Fudge Pirouette", "Pepperidge Farm", "Snacks", "Vons", 84);
        //dbStatusHelper.addNewStatus("Choc. Fudge Pirouette", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 85);
        //dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Oreos", "(for crumbs)", "Desserts", "Vons", 86);
        //dbStatusHelper.addNewStatus("Oreos", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Malted Crunch Ice Cream", "Thrifty", "Desserts", "Vons", 87);
        //dbStatusHelper.addNewStatus("Choc. Malted Crunch Ice Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Reese's PB Cups", "(individually wrapped)", "Candy", "Vons", 88);
        //dbStatusHelper.addNewStatus("Reese's PB Cups", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hot Tamales", "na", "Candy", "Vons", 89);
        //dbStatusHelper.addNewStatus("Hot Tamales", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Mini M&M's", "na", "Candy", "Vons", 90);
        //dbStatusHelper.addNewStatus("Mini M&M's", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Tootsie Rolls", "na", "Candy", "Vons", 91);
        //dbStatusHelper.addNewStatus("Tootsie Rolls", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 92);
        //dbStatusHelper.addNewStatus("Cat Food (wet)", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("100% Pure Pumpkin", "Libby's", "Pet Supplies", "Vons", 93);
        //dbStatusHelper.addNewStatus("100% Pure Pumpkin", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Delectables", "Squeeze Up 20 pack", "Pet Supplies", "Vons", 94);
        //dbStatusHelper.addNewStatus("Delectables", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Body Wash", "Suave Mandarin", "Toiletries", "Vons", 95);
        //dbStatusHelper.addNewStatus("Body Wash", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Shampoo", "Suave 2 in 1", "Toiletries", "Vons", 96);
        //dbStatusHelper.addNewStatus("Shampoo", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Deodorant", "Old Spice", "Toiletries", "Vons", 97);
        //dbStatusHelper.addNewStatus("Deodorant", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Mouthwash", "Crest Whitening", "Toiletries", "Vons", 98);
        //dbStatusHelper.addNewStatus("Mouthwash", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Cotton Swabs", "Q-Tips", "Toiletries", "Vons", 99);
        //dbStatusHelper.addNewStatus("Cotton Swabs", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Febreeze Air Spray", "Heavy Duty", "Household", "Vons", 100);
        //dbStatusHelper.addNewStatus("Febreeze Air Spray", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("All Purpose Cleaner", "Meyer's Lavender", "Household", "Vons", 101);
        //dbStatusHelper.addNewStatus("All Purpose Cleaner", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Laundry Detergent", "Woolite", "Household", "Vons", 102);
        //dbStatusHelper.addNewStatus("Laundry Detergent", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Laundry Sanitizer", "Lysol", "Household", "Vons", 103);
        //dbStatusHelper.addNewStatus("Laundry Sanitizer", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Aluminum Foil", "Reynolds Wrap", "Household", "Vons", 104);
        //dbStatusHelper.addNewStatus("Aluminum Foil", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Zip-Lock Bags (small)", "Sandwich", "Household", "Vons", 105);
        //dbStatusHelper.addNewStatus("Zip-Lock Bags (small)", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Zip-Lock Bags (large)", "Freezer Gallon", "Household", "Vons", 106);
        //dbStatusHelper.addNewStatus("Zip-Lock Bags (large)", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Saran Wrap", "Plastic Wrap", "Household", "Vons", 107);
        //dbStatusHelper.addNewStatus("Saran Wrap", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Scrub Sponges", "Non-Scratch", "Household", "Vons", 108);
        //dbStatusHelper.addNewStatus("Scrub Sponges", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Dawn Powerwash", "Dish Cleaner", "Household", "Vons", 109);
        //dbStatusHelper.addNewStatus("Dawn Powerwash", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Dish Soap", "Dawn Platinum", "Household", "Vons", 110);
        //dbStatusHelper.addNewStatus("Dish Soap", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Small Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 111);
        //dbStatusHelper.addNewStatus("Small Flour Tortillas", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Large Flour Tortillas", "to do", "Bread/Grains/Cereal", "Vons", 112);
        //dbStatusHelper.addNewStatus("Large Flour Tortillas", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sour Cream", "to do", "Eggs/Dairy", "Vons", 113);
        //dbStatusHelper.addNewStatus("Sour Cream", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Buffalo Sauce", "Frank's Wings", "Condiments", "Vons", 114);
        //dbStatusHelper.addNewStatus("Buffalo Sauce", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Taco Sauce", "Victoria's Mild", "Condiments", "Vons", 115);
        //dbStatusHelper.addNewStatus("Taco Sauce", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Taco Seasoning", "any", "Seasonings", "Vons", 116);
        //dbStatusHelper.addNewStatus("Taco Seasoning", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Taco Shells", "to do", "Misc/Ingredients", "Vons", 117);
        //dbStatusHelper.addNewStatus("Taco Shells", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Tortellini", "Barilla 3 Cheese", "Meals", "Vons", 118);
        //dbStatusHelper.addNewStatus("Tortellini", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Garlic Breadsticks", "New York Bakery", "Sides", "Vons", 119);
        //dbStatusHelper.addNewStatus("Garlic Breadsticks", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Good & Plenty", "na", "Candy", "Vons", 120);
        //dbStatusHelper.addNewStatus("Good & Plenty", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sliced Cheese", "Kraft Singles", "Eggs/Dairy", "Vons", 121);
        //dbStatusHelper.addNewStatus("Sliced Cheese", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Cat Treats", "Temptations", "Pet Supplies", "Vons", 122);
        //dbStatusHelper.addNewStatus("Cat Treats", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Saltine Crackers", "Premium Original", "Misc/Ingredients", "Vons", 123);
        //dbStatusHelper.addNewStatus("Saltine Crackers", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Semi-Sweet Choc. Chips", "Nestle", "Misc/Ingredients", "Vons", 124);
        //dbStatusHelper.addNewStatus("Semi-Sweet Choc. Chips", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Vegetable Oil", "Crisco", "Misc/Ingredients", "Vons", 125);
        //dbStatusHelper.addNewStatus("Vegetable Oil", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Extra Virgin Olive Oil", "to do", "Misc/Ingredients", "Vons", 126);
        //dbStatusHelper.addNewStatus("Extra Virgin Olive Oil", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Buttered Popcorn", "Movie Theater Butter", "Snacks", "Vons", 127);
        //dbStatusHelper.addNewStatus("Buttered Popcorn", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Caramel Popcorn", "Cretors", "Snacks", "Vons", 128);
        //dbStatusHelper.addNewStatus("Caramel Popcorn", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Choc. Caramel Swirl Popcorn", "Cretors", "Snacks", "Vons", 129);
        //dbStatusHelper.addNewStatus("Choc. Caramel Swirl Popcorn", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Ritz Crackers", "Original", "Snacks", "Vons", 130);
        //dbStatusHelper.addNewStatus("Ritz Crackers", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Half & Half", "Lucerne", "Eggs/Dairy", "Vons", 131);
        //dbStatusHelper.addNewStatus("Half & Half", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Movie Theater Butter", "Kernel Seasons", "Misc/Ingredients", "Vons", 132);
        //dbStatusHelper.addNewStatus("Movie Theater Butter", "instock", "unchecked");

        storeData.getStoreViewAllMap().put("Vons", 133);
        storeData.getStoreViewInStockMap().put("Vons", 53);
        storeData.getStoreViewNeededMap().put("Vons", 22);
        storeData.getStoreViewPausedMap().put("Vons", 58);
        dbStoreHelper.setStoreViews("Vons", 133, 53, 22, 58);

        //------------------------------------Smart & Final-----------------------------------------

        dbItemHelper.addNewItemByStore("Churros", "Tio Pepe’s or Hola!", "Desserts", "Smart & Final", 0);
        //dbStatusHelper.addNewStatus("Churros", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Pepperoni Slices", "Hormel (300 slices)", "Meat", "Smart & Final", 1);
        //dbStatusHelper.addNewStatus("Pepperoni Slices", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Fun Dip Sticks", "na", "Candy", "Smart & Final", 2);
        //dbStatusHelper.addNewStatus("Fun Dip Sticks", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Orange Tic Tacs", "na", "Candy", "Smart & Final", 3);
        //dbStatusHelper.addNewStatus("Orange Tic Tacs", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Smart & Final", 4);
        storeData.getStoreViewInStockMap().put("Smart & Final", 2);
        storeData.getStoreViewNeededMap().put("Smart & Final", 0);
        storeData.getStoreViewPausedMap().put("Smart & Final", 2);
        dbStoreHelper.setStoreViews("Smart & Final", 4, 2, 0, 2);

        //------------------------------------Costco------------------------------------------------

        dbItemHelper.addNewItemByStore("Soda Cans", "Pepsi or Coke", "Drinks", "Costco", 0);
        //dbStatusHelper.addNewStatus("Soda Cans", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Kitty Litter", "Scoop Away Complete", "Pet Supplies", "Costco", 1);
        //dbStatusHelper.addNewStatus("Kitty Litter", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Chicken Broth", "Kirkland Organic", "Pet Supplies", "Costco", 2);
        //dbStatusHelper.addNewStatus("Chicken Broth", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Mashed Potatoes", "Main St. Bistro", "Pet Supplies", "Costco", 3);
        //dbStatusHelper.addNewStatus("Mashed Potatoes", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Freshpet", "Chicken Recipe (6lb)", "Pet Supplies", "Costco", 4);
        //dbStatusHelper.addNewStatus("Freshpet", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Oreo Muffins", "12 pack", "Desserts", "Costco", 5);
        //dbStatusHelper.addNewStatus("Oreo Muffins", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Crispy Buffalo Wings", "Foster Farms", "Meals", "Costco", 6);
        //dbStatusHelper.addNewStatus("Crispy Buffalo Wings", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Costco", 7);
        storeData.getStoreViewInStockMap().put("Costco", 4);
        storeData.getStoreViewNeededMap().put("Costco", 1);
        storeData.getStoreViewPausedMap().put("Costco", 2);
        dbStoreHelper.setStoreViews("Costco", 7, 4, 1, 2);

        //------------------------------------Walmart-----------------------------------------------

        dbItemHelper.addNewItemByStore("Dark Chocolate Caramel Squares", "Ghiradelli", "Candy", "Walmart", 0);
        //dbStatusHelper.addNewStatus("Dark Chocolate Caramel Squares", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Dishwashing Brush", "Great Value", "Household", "Walmart", 1);
        //dbStatusHelper.addNewStatus("Dishwashing Brush", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Small Trash Bags", "13 gallon", "Household", "Walmart", 2);
        //dbStatusHelper.addNewStatus("Small Trash Bags", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Large Trash Bags", "33 gallon", "Household", "Walmart", 3);
        //dbStatusHelper.addNewStatus("Large Trash Bags", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Compactor Bags", "18 gallon", "Household", "Walmart", 4);
        //dbStatusHelper.addNewStatus("Compactor Bags", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Paper Towels", "Sparkle", "Household", "Walmart", 5);
        //dbStatusHelper.addNewStatus("Paper Towels", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Toilet Paper", "Angel Soft", "Household", "Walmart", 6);
        //dbStatusHelper.addNewStatus("Toilet Paper", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("72% Intense Dark Chocolate", "Ghiradelli", "Candy", "Walmart", 7);
        //dbStatusHelper.addNewStatus("72% Intense Dark Chocolate", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Walmart", 8);
        storeData.getStoreViewInStockMap().put("Walmart", 5);
        storeData.getStoreViewNeededMap().put("Walmart", 2);
        storeData.getStoreViewPausedMap().put("Walmart", 1);
        dbStoreHelper.setStoreViews("Walmart", 8, 5, 2, 1);

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
        //dbStatusHelper.addNewStatus("Poop Bags", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Nitrile Gloves", "GMG 100 pack", "Pet Supplies", "Amazon", 7);
        //dbStatusHelper.addNewStatus("Nitrile Gloves", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Toothpaste", "Tom's Antiplaque & Whitening", "Toiletries", "Amazon", 8);
        //dbStatusHelper.addNewStatus("Toothpaste", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Floss", "Reach Mint Waxed", "Toiletries", "Amazon", 9);
        //dbStatusHelper.addNewStatus("Floss", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Shaving Cream", "Sandalwood", "Toiletries", "Amazon", 10);
        //dbStatusHelper.addNewStatus("Shaving Cream", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Shaving Razors", "Gillette ProGlide", "Toiletries", "Amazon", 11);
        //dbStatusHelper.addNewStatus("Shaving Razors", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Sunscreen", "Hawaiian Tropic Sheer 50spf", "Toiletries", "Amazon", 12);
        //dbStatusHelper.addNewStatus("Sunscreen", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Pet Stain Cleaner", "Rocco & Roxie", "Household", "Amazon", 13);
        //dbStatusHelper.addNewStatus("Pet Stain Cleaner", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Night Light Bulbs", "C7 E12", "Household", "Amazon", 14);
        //dbStatusHelper.addNewStatus("Night Light Bulbs", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Fortune Cookies", "to do", "Snacks", "Amazon", 15);
        //dbStatusHelper.addNewStatus("Fortune Cookies", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Communion Wafers", "to do", "Snacks", "Amazon", 16);
        //dbStatusHelper.addNewStatus("Communion Wafers", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sixlets", "na", "Candy", "Amazon", 17);
        //dbStatusHelper.addNewStatus("Sixlets", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Little Trees Air Fresheners", "True North", "Household", "Amazon", 18);
        //dbStatusHelper.addNewStatus("Little Trees Air Fresheners", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Triple Omega", "Nature Made", "Supplements", "Amazon", 19);
        //dbStatusHelper.addNewStatus("Triple Omega", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Multivitamin", "One a Day Men's", "Supplements", "Amazon", 20);
        //dbStatusHelper.addNewStatus("Multivitamin", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Vitamin C", "Amazon Elements 1000 mg", "Supplements", "Amazon", 21);
        //dbStatusHelper.addNewStatus("Vitamin C", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Magnesium", "Nature Made 400mg", "Supplements", "Amazon", 22);
        //dbStatusHelper.addNewStatus("Magnesium", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Zinc", "Sandhu Herbals 50mg", "Supplements", "Amazon", 23);
        //dbStatusHelper.addNewStatus("Zinc", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Calcium", "Nature's Truth 1200 mg", "Supplements", "Amazon", 24);
        //dbStatusHelper.addNewStatus("Calcium", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Biotin", "Natrol 10,000mcg", "Supplements", "Amazon", 25);
        //dbStatusHelper.addNewStatus("Biotin", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Vitamin D3", "Nature Made 5000 IU", "Supplements", "Amazon", 26);
        //dbStatusHelper.addNewStatus("Vitamin D3", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Hyaluronic Acid", "Horbaach 1000mg", "Supplements", "Amazon", 27);
        //dbStatusHelper.addNewStatus("Hyaluronic Acid", "instock", "unchecked");

        storeData.getStoreViewAllMap().put("Amazon", 28);
        storeData.getStoreViewInStockMap().put("Amazon", 18);
        storeData.getStoreViewNeededMap().put("Amazon", 0);
        storeData.getStoreViewPausedMap().put("Amazon", 10);
        dbStoreHelper.setStoreViews("Amazon", 28, 18, 0, 10);

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
        //dbStatusHelper.addNewStatus("Dryer Sheets", "instock", "unchecked");

        storeData.getStoreViewAllMap().put("Stater Bros", 5);
        storeData.getStoreViewInStockMap().put("Stater Bros", 1);
        storeData.getStoreViewNeededMap().put("Stater Bros", 0);
        storeData.getStoreViewPausedMap().put("Stater Bros", 4);
        dbStoreHelper.setStoreViews("Stater Bros", 5, 1, 0, 4);

        //------------------------------------Trader Joe's-------------------------------------------

        dbItemHelper.addNewItemByStore("Hold the Cone", "Chocolate", "Desserts", "Trader Joe's", 0);
        //dbStatusHelper.addNewStatus("Hold the Cone", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Dark Choc. Caramels", "Trader Joe's", "Candy", "Trader Joe's", 1);
        //dbStatusHelper.addNewStatus("Dark Choc. Caramels", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Trader Joe's", 2);
        storeData.getStoreViewInStockMap().put("Trader Joe's", 0);
        storeData.getStoreViewNeededMap().put("Trader Joe's", 1);
        storeData.getStoreViewPausedMap().put("Trader Joe's", 1);
        dbStoreHelper.setStoreViews("Trader Joe's", 2, 0, 1, 1);

        //------------------------------------CVS---------------------------------------------------

        dbItemHelper.addNewItemByStore("Dark Chocolate Pretzels", "Flipz", "Snacks", "CVS", 0);
        //dbStatusHelper.addNewStatus("Dark Chocolate Pretzels", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Candy Corn", "Brach's", "Candy", "CVS", 1);
        //dbStatusHelper.addNewStatus("Candy Corn", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Rubbing Alcohol", "Isopropyl", "Household", "CVS", 2);
        //dbStatusHelper.addNewStatus("Rubbing Alcohol", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Hydrogen Peroxide", "na", "Household", "CVS", 3);
        //dbStatusHelper.addNewStatus("Hydrogen Peroxide", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Moisturizing Lotion", "CVS Health w/ hyaluronic acid", "Toiletries", "CVS", 4);
        //dbStatusHelper.addNewStatus("Moisturizing Lotion", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Packaging Tape", "Scotch Heavy Duty", "Household", "CVS", 5);
        //dbStatusHelper.addNewStatus("Packaging Tape", "instock", "unchecked");


        storeData.getStoreViewAllMap().put("CVS", 6);
        storeData.getStoreViewInStockMap().put("CVS", 5);
        storeData.getStoreViewNeededMap().put("CVS", 0);
        storeData.getStoreViewPausedMap().put("CVS", 1);
        dbStoreHelper.setStoreViews("CVS", 6, 5, 0, 1);

        //------------------------------------Dollar Tree-------------------------------------------

        dbItemHelper.addNewItemByStore("Sno Caps", "na", "Candy", "Dollar Tree", 0);
        //dbStatusHelper.addNewStatus("Sno Caps", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Smarties", "na", "Candy", "Dollar Tree", 1);
        //dbStatusHelper.addNewStatus("Smarties", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Red Hots", "na", "Candy", "Dollar Tree", 2);
        //dbStatusHelper.addNewStatus("Red Hots", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Hand Soap", "Lavender & Chamomile", "Toiletries", "Dollar Tree", 3);
        //dbStatusHelper.addNewStatus("Hand Soap", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Ramen Noodles", "Nissin", "Soups", "Dollar Tree", 4);
        //dbStatusHelper.addNewStatus("Ramen Noodles", "instock", "unchecked");

        storeData.getStoreViewAllMap().put("Dollar Tree", 5);
        storeData.getStoreViewInStockMap().put("Dollar Tree", 4);
        storeData.getStoreViewNeededMap().put("Dollar Tree", 0);
        storeData.getStoreViewPausedMap().put("Dollar Tree", 1);
        dbStoreHelper.setStoreViews("Dollar Tree", 5, 4, 0, 1);

        //------------------------------------Ralphs------------------------------------------------

        dbItemHelper.addNewItemByStore("Clarified Butter", "Challenge", "Eggs/Dairy", "Ralphs", 0);
        //dbStatusHelper.addNewStatus("Clarified Butter", "paused", "unchecked");

        storeData.getStoreViewAllMap().put("Ralphs", 1);
        storeData.getStoreViewInStockMap().put("Ralphs", 0);
        storeData.getStoreViewNeededMap().put("Ralphs", 0);
        storeData.getStoreViewPausedMap().put("Ralphs", 1);
        dbStoreHelper.setStoreViews("Ralphs", 1, 0, 0, 1);

        //------------------------------------Target------------------------------------------------

        dbItemHelper.addNewItemByStore("Chocolate Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 0);
        //dbStatusHelper.addNewStatus("Chocolate Syrup", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Caramel Syrup", "Ghirardelli or Hershey's", "Condiments", "Target", 1);
        //dbStatusHelper.addNewStatus("Caramel Syrup", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Dark Choc. M&M's", "na", "Candy", "Target", 2);
        //dbStatusHelper.addNewStatus("Dark Choc. M&M's", "paused", "unchecked");

        dbItemHelper.addNewItemByStore("Sea Salt Caramels", "Favorite Day", "Candy", "Target", 3);
        //dbStatusHelper.addNewStatus("Sea Salt Caramels", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Oreo Pie Mix", "No Bake Dessert", "Desserts", "Target", 4);
        //dbStatusHelper.addNewStatus("Oreo Pie Mix", "instock", "unchecked");

        storeData.getStoreViewAllMap().put("Target", 5);
        storeData.getStoreViewInStockMap().put("Target", 4);
        storeData.getStoreViewNeededMap().put("Target", 0);
        storeData.getStoreViewPausedMap().put("Target", 1);
        dbStoreHelper.setStoreViews("Target", 5, 4, 0, 1);

        //------------------------------------Pet Supplies Plus-------------------------------------

        dbItemHelper.addNewItemByStore("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 0);
        //dbStatusHelper.addNewStatus("Cat Food (dry)", "instock", "unchecked");

        storeData.getStoreViewAllMap().put("Pet Supplies Plus", 1);
        storeData.getStoreViewInStockMap().put("Pet Supplies Plus", 1);
        storeData.getStoreViewNeededMap().put("Pet Supplies Plus", 0);
        storeData.getStoreViewPausedMap().put("Pet Supplies Plus", 0);
        dbStoreHelper.setStoreViews("Pet Supplies Plus", 1, 1, 0, 0);

        //------------------------------------Sprouts-------------------------------------------

        dbItemHelper.addNewItemByStore("Bar Soap", "Zum Bar Sea Salt", "Toiletries", "Sprouts", 0);
        //dbStatusHelper.addNewStatus("Bar Soap", "needed", "unchecked");

        dbItemHelper.addNewItemByStore("Toothbrush Heads", "Radius Soft", "Toiletries", "Sprouts", 1);
        //dbStatusHelper.addNewStatus("Toothbrush Heads", "instock", "unchecked");

        storeData.getStoreViewAllMap().put("Sprouts", 2);
        storeData.getStoreViewInStockMap().put("Sprouts", 1);
        storeData.getStoreViewNeededMap().put("Sprouts", 1);
        storeData.getStoreViewPausedMap().put("Sprouts", 0);
        dbStoreHelper.setStoreViews("Sprouts", 2, 1, 1, 0);

        //------------------------------------Sam's Club--------------------------------------------

        dbItemHelper.addNewItemByStore("Quick Steak", "Gary's", "Meat", "Sam's Club", 0);
        //dbStatusHelper.addNewStatus("Quick Steak", "instock", "unchecked");

        dbItemHelper.addNewItemByStore("Paper Plates", "to do", "Household", "Sam's Club", 1);
        //dbStatusHelper.addNewStatus("Paper Plates", "instock", "unchecked");

        storeData.getStoreViewAllMap().put("Sam's Club", 2);
        storeData.getStoreViewInStockMap().put("Sam's Club", 2);
        storeData.getStoreViewNeededMap().put("Sam's Club", 0);
        storeData.getStoreViewPausedMap().put("Sam's Club", 0);
        dbStoreHelper.setStoreViews("Sam's Club", 2, 2, 0, 0);

        //---------------------------------------Staples--------------------------------------------

        dbItemHelper.addNewItemByStore("Multipurpose Paper", "Tru Red 20/96", "Household", "Staples", 0);
        //dbStatusHelper.addNewStatus("Multipurpose Paper", "instock", "unchecked");

        storeData.getStoreViewAllMap().put("Staples", 1);
        storeData.getStoreViewInStockMap().put("Staples", 1);
        storeData.getStoreViewNeededMap().put("Staples", 0);
        storeData.getStoreViewPausedMap().put("Staples", 0);
        dbStoreHelper.setStoreViews("Staples", 1, 1, 0, 0);

        //---------------------------------------Woodranch--------------------------------------------

        dbItemHelper.addNewItemByStore("Woodranch BBQ Sauce", "(1 pint)", "Condiments", "Woodranch", 0);
        //dbStatusHelper.addNewStatus("Woodranch BBQ Sauce", "instock", "unchecked");

        storeData.getStoreViewAllMap().put("Woodranch", 1);
        storeData.getStoreViewInStockMap().put("Woodranch", 1);
        storeData.getStoreViewNeededMap().put("Woodranch", 0);
        storeData.getStoreViewPausedMap().put("Woodranch", 0);
        dbStoreHelper.setStoreViews("Woodranch", 1, 1, 0, 0);

        //------------------------------------Yorba Linda Feed Store--------------------------------

        dbItemHelper.addNewItemByStore("Dog Food (dry)", "Canidae All Life Stages", "Pet Supplies", "Yorba Linda Feed Store", 0);
        //dbStatusHelper.addNewStatus("Dog Food (dry)", "instock", "unchecked");

        storeData.getStoreViewAllMap().put("Yorba Linda Feed Store", 1);
        storeData.getStoreViewInStockMap().put("Yorba Linda Feed Store", 1);
        storeData.getStoreViewNeededMap().put("Yorba Linda Feed Store", 0);
        storeData.getStoreViewPausedMap().put("Yorba Linda Feed Store", 0);
        dbStoreHelper.setStoreViews("Yorba Linda Feed Store", 1, 1, 0, 0);

        //------------------------------------------------------------------------------------------

        // total store items = 212

    }

//------------------------------------------------------------------------------------------------//

}