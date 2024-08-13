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

    public Boolean itemIsSelected;
    public Item selectedItem;
    public int selectedItemPosition;
    public ArrayList<Boolean> itemIsClickedInInventory;
    public ArrayList<Boolean> itemIsClickedInShopByStore;
    public ArrayList<Boolean> itemIsChecked;
    public int storeNum;
    public String reorderItemsCategory;

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

        itemIsSelected = false;
        selectedItem = null;
        selectedItemPosition = 0;
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
        itemIsSelected = false;
        itemIsClickedInInventory = new ArrayList<>();
        itemIsClickedInShopByStore = new ArrayList<>();
    }

    public void loadSampleData() {


        dbCategoryHelper.addNewCategory("Meals", 0);
        dbCategoryHelper.addNewCategory("Sides", 1);
        dbCategoryHelper.addNewCategory("Meat", 2);
        dbCategoryHelper.addNewCategory("Bread/Grains/Cereal", 3);
        dbCategoryHelper.addNewCategory("Eggs/Dairy", 4);
        dbCategoryHelper.addNewCategory("Condiments", 5);
        dbCategoryHelper.addNewCategory("Seasonings", 6);
        dbCategoryHelper.addNewCategory("Drinks", 7);
        dbCategoryHelper.addNewCategory("Snacks", 8);
        dbCategoryHelper.addNewCategory("Desserts", 9);
        dbCategoryHelper.addNewCategory("Candy", 10);
        dbCategoryHelper.addNewCategory("Pet Supplies", 11);
        dbCategoryHelper.addNewCategory("Toiletries", 12);
        dbCategoryHelper.addNewCategory("Household", 13);

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

        //------------------------------------Meals-------------------------------------------------

        dbHelper.addNewItem("Hamburger Helper", "Cheeseburger Macaroni", "Meals", "Vons", 0);
        dbStatusHelper.addNewStatus("Hamburger Helper", "true", "false", "false");

        dbHelper.addNewItem("Buffalo Chicken Bites", "TGIF or Frank's", "Meals", "Vons", 1);
        dbStatusHelper.addNewStatus("Buffalo Chicken Bites", "true", "false", "false");

        dbHelper.addNewItem("Terriyaki Chicken Bites", "InnovAsian", "Meals", "Vons", 2);
        dbStatusHelper.addNewStatus("Terriyaki Chicken Bites", "true", "false", "false");

        dbHelper.addNewItem("TGIF Cheese Sticks", "TGIF (small 10pc)", "Meals", "Vons", 3);
        dbStatusHelper.addNewStatus("TGIF Cheese Sticks", "true", "false", "false");

        dbHelper.addNewItem("Frozen Pizza", "Thin Pepperoni", "Meals", "Vons", 4);
        dbStatusHelper.addNewStatus("Frozen Pizza", "true", "false", "false");

        dbHelper.addNewItem("Hot Dogs", "Bun Size", "Meals", "Vons", 5);
        dbStatusHelper.addNewStatus("Hot Dogs", "true", "false", "false");

        dbHelper.addNewItem("Hot Dog Buns", "(8 pack)", "Meals", "Vons", 6);
        dbStatusHelper.addNewStatus("Hot Dog Buns", "true", "false", "false");

        dbHelper.addNewItem("Fetuccini Alfredo", "Pasta Roni", "Meals", "Vons", 7);
        dbStatusHelper.addNewStatus("Fetuccini Alfredo", "true", "false", "false");

        dbHelper.addNewItem("Mac & Cheese", "Annie’s", "Meals", "Vons", 8);
        dbStatusHelper.addNewStatus("Mac & Cheese", "true", "false", "false");

        dbHelper.addNewItem("Gnocci", "Signature Select", "Meals", "Vons", 9);
        dbStatusHelper.addNewStatus("Gnocci", "true", "false", "false");

        dbHelper.addNewItem("Spaghetti O's", "w/ Meatballs", "Meals", "Vons", 10);
        dbStatusHelper.addNewStatus("Spaghetti O's", "true", "false", "false");

        dbHelper.addNewItem("Chicken Noodle Soup", "Campbell's", "Meals", "Vons", 11);
        dbStatusHelper.addNewStatus("Chicken Noodle Soup", "true", "false", "false");

        dbHelper.addNewItem("Minestrone Soup", "Amy's", "Meals", "Vons", 12);
        dbStatusHelper.addNewStatus("Minestrone Soup", "true", "false", "false");

        dbHelper.addNewItem("Vegetable Barley Soup", "Amy's", "Meals", "Vons", 13);
        dbStatusHelper.addNewStatus("Vegetable Barley Soup", "true", "false", "false");

        dbHelper.addNewItem("Beef Noodles", "Yakisoba", "Meals", "Vons", 14);
        dbStatusHelper.addNewStatus("Beef Noodles", "true", "false", "false");

        //------------------------------------Sides-------------------------------------------------

        dbHelper.addNewItem("Fast Food Fries", "Ore-Ida", "Sides", "Vons", 0);
        dbStatusHelper.addNewStatus("Fast Food Fries", "true", "false", "false");

        dbHelper.addNewItem("Texas Cheesy Bread", "New York Bakery", "Sides", "Vons", 1);
        dbStatusHelper.addNewStatus("Texas Cheesy Bread", "true", "false", "false");

        dbHelper.addNewItem("Chicken Rice", "Knorr", "Sides", "Vons", 2);
        dbStatusHelper.addNewStatus("Chicken Rice", "true", "false", "false");

        //------------------------------------Meat--------------------------------------------------

        dbHelper.addNewItem("Steak", "USDA", "Meat", "Vons", 0);
        dbStatusHelper.addNewStatus("Steak", "true", "false", "false");

        dbHelper.addNewItem("Ground Beef", "(1 pound)", "Meat", "Vons", 1);
        dbStatusHelper.addNewStatus("Ground Beef", "true", "false", "false");

        dbHelper.addNewItem("Meatballs", "Homestyle", "Meat", "Vons", 2);
        dbStatusHelper.addNewStatus("Meatballs", "true", "false", "false");

        dbHelper.addNewItem("Pepperoni", "Hormel", "Meat", "Vons", 3);
        dbStatusHelper.addNewStatus("Pepperoni", "true", "false", "false");

        //------------------------------------Bread/Grains/Cereal-----------------------------------

        dbHelper.addNewItem("Wheat Bread", "Nature's Own", "Bread/Grains/Cereal", "Vons", 0);
        dbStatusHelper.addNewStatus("Wheat Bread", "true", "false", "false");

        dbHelper.addNewItem("Baguette", "French or Sourdough", "Bread/Grains/Cereal", "Vons", 1);
        dbStatusHelper.addNewStatus("Baguette", "true", "false", "false");

        dbHelper.addNewItem("RP Cereal", "Reese's Puffs", "Bread/Grains/Cereal", "Vons", 2);
        dbStatusHelper.addNewStatus("RP Cereal", "true", "false", "false");

        dbHelper.addNewItem("CC Cereal", "Cookie Crisp", "Bread/Grains/Cereal", "Vons", 3);
        dbStatusHelper.addNewStatus("CC Cereal", "true", "false", "false");

        dbHelper.addNewItem("FMW Cereal", "Frosted Mini Wheat", "Bread/Grains/Cereal", "Vons", 4);
        dbStatusHelper.addNewStatus("FMW Cereal", "true", "false", "false");

        dbHelper.addNewItem("Thin Spaghetti", "Barilla Whole Grain", "Bread/Grains/Cereal", "Vons", 5);
        dbStatusHelper.addNewStatus("Thin Spaghetti", "true", "false", "false");

        dbHelper.addNewItem("Spiral Pasta", "Barilla Rotini", "Bread/Grains/Cereal", "Vons", 6);
        dbStatusHelper.addNewStatus("Spiral Pasta", "true", "false", "false");

        //------------------------------------Eggs/Dairy--------------------------------------------

        dbHelper.addNewItem("Milk", "Vitamin D", "Eggs/Dairy", "Vons", 0);
        dbStatusHelper.addNewStatus("Milk", "true", "false", "false");

        dbHelper.addNewItem("Eggs", "Grade AA", "Eggs/Dairy", "Vons", 1);
        dbStatusHelper.addNewStatus("Eggs", "true", "false", "false");

        dbHelper.addNewItem("Honey Yogurt", "Greek Gods", "Eggs/Dairy", "Vons", 2);
        dbStatusHelper.addNewStatus("Honey Yogurt", "true", "false", "false");

        dbHelper.addNewItem("Salted Butter", "Challenge", "Eggs/Dairy", "Vons", 3);
        dbStatusHelper.addNewStatus("Salted Butter", "true", "false", "false");

        dbHelper.addNewItem("Clarified Butter", "Challenge", "Eggs/Dairy", "Ralphs", 4);
        dbStatusHelper.addNewStatus("Clarified Butter", "true", "false", "false");

        dbHelper.addNewItem("Shredded Cheese", "Mexican Blend", "Eggs/Dairy", "Vons", 5);
        dbStatusHelper.addNewStatus("Shredded Cheese", "true", "false", "false");

        dbHelper.addNewItem("String Cheese", "Mozarella", "Eggs/Dairy", "Vons", 6);
        dbStatusHelper.addNewStatus("String Cheese", "true", "false", "false");

        dbHelper.addNewItem("BD Cheese", "Black Diamond", "Eggs/Dairy", "Vons", 7);
        dbStatusHelper.addNewStatus("BD Cheese", "true", "false", "false");

        //------------------------------------Condiments--------------------------------------------

        dbHelper.addNewItem("Parmesan Cheese", "na", "Condiments", "Vons", 0);
        dbStatusHelper.addNewStatus("Parmesan Cheese", "true", "false", "false");

        dbHelper.addNewItem("Ketchup", "Heinz", "Condiments", "Vons", 1);
        dbStatusHelper.addNewStatus("Ketchup", "true", "false", "false");

        dbHelper.addNewItem("Mustard", "Heinz", "Condiments", "Vons", 2);
        dbStatusHelper.addNewStatus("Mustard", "true", "false", "false");

        dbHelper.addNewItem("Pasta Sauce", "Ragu Meat", "Condiments", "Vons", 3);
        dbStatusHelper.addNewStatus("Pasta Sauce", "true", "false", "false");

        dbHelper.addNewItem("Chocolate Syrup", "Ghirardelli", "Condiments", "Target", 4);
        dbStatusHelper.addNewStatus("Chocolate Syrup", "true", "false", "false");

        dbHelper.addNewItem("Caramel Syrup", "Ghirardelli", "Condiments", "Target", 5);
        dbStatusHelper.addNewStatus("Caramel Syrup", "true", "false", "false");

        //------------------------------------Seasonings--------------------------------------------

        dbHelper.addNewItem("Salt & Pepeper", "na", "Seasonings", "Vons", 0);
        dbStatusHelper.addNewStatus("Salt & Pepeper", "true", "false", "false");

        dbHelper.addNewItem("Garlic Salt", "Lawry's", "Seasonings", "Vons", 1);
        dbStatusHelper.addNewStatus("Garlic Salt", "true", "false", "false");

        dbHelper.addNewItem("Lawry's Seasoning Salt", "Lawry's", "Seasonings", "Vons", 2);
        dbStatusHelper.addNewStatus("Lawry's Seasoning Salt", "true", "false", "false");

        dbHelper.addNewItem("Vanilla Extract", "Signature Select", "Seasonings", "Vons", 3);
        dbStatusHelper.addNewStatus("Vanilla Extract", "true", "false", "false");

        dbHelper.addNewItem("Cinnamon Sugar", "McCormick's", "Seasonings", "Vons", 4);
        dbStatusHelper.addNewStatus("Cinnamon Sugar", "true", "false", "false");

        dbHelper.addNewItem("Sprinkles", "3 types", "Seasonings", "Vons", 5);
        dbStatusHelper.addNewStatus("Sprinkles", "true", "false", "false");

        //------------------------------------Drinks------------------------------------------------

        dbHelper.addNewItem("Soda", "Pepsi or Coke", "Drinks", "Stater Bros", 0);
        dbStatusHelper.addNewStatus("Soda", "true", "false", "false");

        dbHelper.addNewItem("Hot Chocolate Mix", "Swiss Miss Dark", "Drinks", "Vons", 1);
        dbStatusHelper.addNewStatus("Hot Chocolate Mix", "true", "false", "false");

        dbHelper.addNewItem("Brown Sugar", "(for hot chocolate)", "Drinks", "Vons", 2);
        dbStatusHelper.addNewStatus("Brown Sugar", "true", "false", "false");

        dbHelper.addNewItem("Bottled Water", "Aquafina", "Drinks", "Vons", 3);
        dbStatusHelper.addNewStatus("Bottled Water", "true", "false", "false");

        //------------------------------------Snacks------------------------------------------------

        dbHelper.addNewItem("Beef Jerky", "Archer Terriyaki", "Snacks", "Vons", 0);
        dbStatusHelper.addNewStatus("Beef Jerky", "true", "false", "false");

        dbHelper.addNewItem("Peanuts", "Honey Roasted", "Snacks", "Vons", 1);
        dbStatusHelper.addNewStatus("Peanuts", "true", "false", "false");

        dbHelper.addNewItem("Shelled Peanuts", "Salted", "Snacks", "Vons", 2);
        dbStatusHelper.addNewStatus("Shelled Peanuts", "true", "false", "false");

        dbHelper.addNewItem("Sunflower Seeds", "Salted", "Snacks", "Vons", 3);
        dbStatusHelper.addNewStatus("Sunflower Seeds", "true", "false", "false");

        dbHelper.addNewItem("Vinegar Chips", "Kettle", "Snacks", "Vons", 4);
        dbStatusHelper.addNewStatus("Vinegar Chips", "true", "false", "false");

        dbHelper.addNewItem("Naan Crisps", "Stonefire", "Snacks", "Vons", 5);
        dbStatusHelper.addNewStatus("Naan Crisps", "true", "false", "false");


        dbHelper.addNewItem("Drizzle Popcorn", "Popcornopolis", "Snacks", "Vons", 6);
        dbStatusHelper.addNewStatus("Drizzle Popcorn", "true", "false", "false");

        dbHelper.addNewItem("Oreo Cakesters", "Nabisco", "Snacks", "Vons", 7);
        dbStatusHelper.addNewStatus("Oreo Cakesters", "true", "false", "false");

        dbHelper.addNewItem("Goldfish", "Cheddar", "Snacks", "Vons", 8);
        dbStatusHelper.addNewStatus("Goldfish", "true", "false", "false");

        dbHelper.addNewItem("Cheez-Its", "Original", "Snacks", "Vons", 9);
        dbStatusHelper.addNewStatus("Cheez-Its", "true", "false", "false");

        dbHelper.addNewItem("Choc. Fudge Pudding", "Snack Pack", "Snacks", "Stater Bros", 10);
        dbStatusHelper.addNewStatus("Choc. Fudge Pudding", "true", "false", "false");

        //------------------------------------Desserts----------------------------------------------

        dbHelper.addNewItem("Choc. Ice Cream", "Thrifty", "Desserts", "Rite Aid", 0);
        dbStatusHelper.addNewStatus("Choc. Ice Cream", "true", "false", "false");

        dbHelper.addNewItem("Churros", "Tio Pepe’s", "Desserts", "Smart & Final", 1);
        dbStatusHelper.addNewStatus("Churros", "true", "false", "false");

        dbHelper.addNewItem("Choc. Chip Muffin Mix", "Betty Crocker", "Desserts", "Vons", 2);
        dbStatusHelper.addNewStatus("Choc. Chip Muffin Mix", "true", "false", "false");

        dbHelper.addNewItem("Choc. Chip Cookie Mix", "Gluten Free", "Desserts", "Stater Bros", 3);
        dbStatusHelper.addNewStatus("Choc. Chip Cookie Mix", "true", "false", "false");

        dbHelper.addNewItem("Gingerbread Cookie Mix", "Betty Crocker", "Desserts", "Amazon", 4);
        dbStatusHelper.addNewStatus("Gingerbread Cookie Mix", "true", "false", "false");

        dbHelper.addNewItem("Oreos", "for crumbs", "Desserts", "Vons", 5);
        dbStatusHelper.addNewStatus("Oreos", "true", "false", "false");

        dbHelper.addNewItem("Choc. Malt Mix", "Nestle", "Desserts", "Stater Bros", 6);
        dbStatusHelper.addNewStatus("Choc. Malt Mix", "true", "false", "false");

        //------------------------------------Candy-------------------------------------------------

        dbHelper.addNewItem("Chocolate Carmels", "Ghiradelli", "Candy", "Walmart", 0);
        dbStatusHelper.addNewStatus("Chocolate Carmels", "true", "false", "false");

        dbHelper.addNewItem("Reese's PB Cups", "(individually wrapped)", "Candy", "Walmart", 1);
        dbStatusHelper.addNewStatus("Reese's PB Cups", "true", "false", "false");

        dbHelper.addNewItem("Candy Corn", "na", "Candy", "Amazon", 2);
        dbStatusHelper.addNewStatus("Candy Corn", "true", "false", "false");

        dbHelper.addNewItem("Hot Tamales", "na", "Candy", "Vons", 3);
        dbStatusHelper.addNewStatus("Hot Tamales", "true", "false", "false");

        dbHelper.addNewItem("Smarties", "na", "Candy", "Rite Aid", 4);
        dbStatusHelper.addNewStatus("Smarties", "true", "false", "false");

        dbHelper.addNewItem("Orange Pez", "na", "Candy", "Amazon", 5);
        dbStatusHelper.addNewStatus("Orange Pez", "true", "false", "false");

        dbHelper.addNewItem("Sno Caps", "na", "Candy", "Dollar Tree", 6);
        dbStatusHelper.addNewStatus("Sno Caps", "true", "false", "false");

        dbHelper.addNewItem("Mini M&M's", "na", "Candy", "Vons", 7);
        dbStatusHelper.addNewStatus("Mini M&M's", "true", "false", "false");

        //------------------------------------Pet Supplies-------------------------------------------

        dbHelper.addNewItem("Cat Food (wet)", "Fancy Feast", "Pet Supplies", "Vons", 0);
        dbStatusHelper.addNewStatus("Cat Food (wet)", "true", "false", "false");

        dbHelper.addNewItem("Cat Food (dry)", "Purina Pro Plan", "Pet Supplies", "Pet Supplies Plus", 1);
        dbStatusHelper.addNewStatus("Cat Food (dry)", "true", "false", "false");

        dbHelper.addNewItem("Kitty Liter", "XXX", "Pet Supplies", "Cosco", 2);
        dbStatusHelper.addNewStatus("Kitty Liter", "true", "false", "false");


        //------------------------------------Toiletries--------------------------------------------

        dbHelper.addNewItem("Hand Soap", "Lavender & Chamomile", "Toiletries", "Vons", 0);
        dbStatusHelper.addNewStatus("Hand Soap", "true", "false", "false");

        dbHelper.addNewItem("Body Wash", "Jason Lavender", "Toiletries", "Vons", 1);
        dbStatusHelper.addNewStatus("Body Wash", "true", "false", "false");

        dbHelper.addNewItem("Shampoo", "Suave 2 in 1", "Toiletries", "Vons", 2);
        dbStatusHelper.addNewStatus("Shampoo", "true", "false", "false");

        dbHelper.addNewItem("Deodorant", "Old Spice", "Toiletries", "Vons", 3);
        dbStatusHelper.addNewStatus("Deodorant", "true", "false", "false");

        dbHelper.addNewItem("Toothpaste", "Tom's Antiplaque & Whitening", "Toiletries", "Amazon", 4);
        dbStatusHelper.addNewStatus("Toothpaste", "true", "false", "false");

        dbHelper.addNewItem("Floss", "Reach Mint Waxed", "Toiletries", "Amazon", 5);
        dbStatusHelper.addNewStatus("Floss", "true", "false", "false");

        dbHelper.addNewItem("Shaving Cream", "Sandalwood", "Toiletries", "Amazon", 6);
        dbStatusHelper.addNewStatus("Shaving Cream", "true", "false", "false");

        dbHelper.addNewItem("Shaving Razors", "Gillette ProGlide", "Toiletries", "Amazon", 7);
        dbStatusHelper.addNewStatus("Shaving Razors", "true", "false", "false");

        dbHelper.addNewItem("Mouthwash", "Crest Whitening", "Toiletries", "Vons", 8);
        dbStatusHelper.addNewStatus("Mouthwash", "true", "false", "false");

        dbHelper.addNewItem("Cotton Swabs", "Q-Tips", "Toiletries", "Vons", 9);
        dbStatusHelper.addNewStatus("Cotton Swabs", "true", "false", "false");

        dbHelper.addNewItem("Sunscreen", "Hawaiian Tropic Sheer 50spf", "Toiletries", "Amazon", 10);
        dbStatusHelper.addNewStatus("Sunscreen", "true", "false", "false");

        //------------------------------------Household-------------------------------------------------

        dbHelper.addNewItem("Febreeze Air Spray", "Heavy Duty", "Household", "Vons", 0);
        dbStatusHelper.addNewStatus("Febreeze Air Spray", "true", "false", "false");

        dbHelper.addNewItem("All Purpose Cleaner", "Meyer's Lavender", "Household", "Vons", 1);
        dbStatusHelper.addNewStatus("All Purpose Cleaner", "true", "false", "false");

        dbHelper.addNewItem("Rocco & Roxie", "Stain Cleaner", "Household", "Amazon", 2);
        dbStatusHelper.addNewStatus("Rocco & Roxie", "true", "false", "false");

        dbHelper.addNewItem("Laundry Sanitizer", "Lysol", "Household", "Vons", 3);
        dbStatusHelper.addNewStatus("Laundry Sanitizer", "true", "false", "false");

        dbHelper.addNewItem("Laundry Detergent", "Open Nature Lavender", "Household", "Vons", 4);
        dbStatusHelper.addNewStatus("Laundry Detergent", "true", "false", "false");

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

        dbHelper.addNewItem("Scrub Sponges", "Non-Scratch", "Household", "Vons", 12);
        dbStatusHelper.addNewStatus("Scrub Sponges", "true", "false", "false");

        dbHelper.addNewItem("Dishwashing Brush", "Great Value", "Household", "Walmart", 13);
        dbStatusHelper.addNewStatus("Dishwashing Brush", "true", "false", "false");

        dbHelper.addNewItem("Small Trash Bags", "13 gallon", "Household", "Walmart", 14);
        dbStatusHelper.addNewStatus("Small Trash Bags", "true", "false", "false");

        dbHelper.addNewItem("Big Trash Bags", "33 gallon", "Household", "Walmart", 15);
        dbStatusHelper.addNewStatus("Big Trash Bags", "true", "false", "false");

        dbHelper.addNewItem("Compactor Bags", "18 gallon", "Household", "Walmart", 16);
        dbStatusHelper.addNewStatus("Compactor Bags", "true", "false", "false");

        dbHelper.addNewItem("Dawn Powerwash", "Dish Cleaner", "Household", "Vons", 17);
        dbStatusHelper.addNewStatus("Dawn Powerwash", "true", "false", "false");

        dbHelper.addNewItem("Dish Soap", "Dawn Platinum", "Household", "Vons", 18);
        dbStatusHelper.addNewStatus("Dish Soap", "true", "false", "false");

        dbHelper.addNewItem("Paper Towels", "Sparkle", "Household", "Walmart", 19);
        dbStatusHelper.addNewStatus("Paper Towels", "true", "false", "false");

        dbHelper.addNewItem("Toilet Paper", "Angel Soft", "Household", "Walmart", 20);
        dbStatusHelper.addNewStatus("Toilet Paper", "true", "false", "false");

        /*
        dbHelper.addNewItem("", "", "", "");
        dbStatusHelper.addNewStatus("", "true", "false", "false");
        */

        dbHelper = new DBHelper(this);
        dbStatusHelper = new DBStatusHelper(this);
        dbCategoryHelper = new DBCategoryHelper(this);
        dbStoreHelper = new DBStoreHelper(this);
        itemData = dbHelper.readItemData();
        statusData = dbStatusHelper.readStatusData();
        itemData.updateStatuses(statusData);
        categoryData = dbCategoryHelper.readCategoryData();
        storeData = dbStoreHelper.readStoreData();
        itemIsSelected = false;
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
    }

}