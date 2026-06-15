package ryan.android.shopping;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Shopping extends AppCompatActivity {

    private ItemData itemData;
    private StatusData statusData;
    private CategoryData categoryData;
    private StoreData storeData;

    private DBItemHelper dbItemHelper;
    private DBStatusHelper dbStatusHelper;
    private DBCategoryHelper dbCategoryHelper;
    private DBStoreHelper dbStoreHelper;

    private boolean itemIsSelectedInInventory;
    private boolean itemIsSelectedInSearchResults;
    private boolean itemIsSelectedInShoppingList;
    private Item selectedItemInInventory;
    private Item selectedItemInSearchResults;
    private Item selectedItemInShoppingList;
    private int selectedItemPositionInInventory;
    private int selectedItemPositionInSearchResults;
    private int selectedItemPositionInShoppingList;
    private Item itemInPictureDialog;

    private int storeListOrderNum;
    private String reorderItemsCategory;
    private String reorderItemsStore;
    private boolean editItemInInventory;
    private boolean editItemInSearchResults;
    private boolean editItemInShoppingList;
    private boolean editItemInPictureDialog;
    private boolean pictureDialogInInventory;
    private boolean pictureDialogInSearchResults;
    private boolean pictureDialogInShoppingList;

    private SearchAlgorithm searchAlgorithm;
    private AlertDialog alertDialog;
    private View alertDialogView;
    private TextView alertDialogTitle;
    private TextView alertDialogMessage;
    private TextView alertDialogButton;
    private AlertDialog pictureDialog;
    private View pictureDialogView;
    private TextView cameraButton;
    private TextView editButton;
    private TextView takeButton;
    private TextView cancelButton;
    private LinearLayout cameraEditButtons;
    private LinearLayout takeCancelButtons;

    private String inventoryView;
    static final String VIEW_ALL = ShoppingApp.getStringRes(R.string.cvViewAll);
    static final String VIEW_INSTOCK = ShoppingApp.getStringRes(R.string.cvViewInstock);
    static final String VIEW_NEEDED = ShoppingApp.getStringRes(R.string.cvViewNeeded);
    static final String VIEW_PAUSED = ShoppingApp.getStringRes(R.string.cvViewPaused);

    private String inventorySortBy;
    private String defaultSortBy;
    static final String SORT_BY_CATEGORY = ShoppingApp.getStringRes(R.string.cvCategory);
    static final String SORT_BY_STORE = ShoppingApp.getStringRes(R.string.cvStore);
    static final String SORT_ALPHABETICAL = ShoppingApp.getStringRes(R.string.cvAlphabetical);

    private String categoryTitles;
    private String defaultCategoryTitles;
    static final String CATEGORY_TITLES_EXPANDED = ShoppingApp.getStringRes(R.string.cvCategoryTitlesExpanded);
    static final String CATEGORY_TITLES_CONTRACTED = ShoppingApp.getStringRes(R.string.cvCategoryTitlesContracted);

    private String storeTitles;
    private String defaultStoreTitles;
    static final String STORE_TITLES_EXPANDED = ShoppingApp.getStringRes(R.string.cvStoreTitlesExpanded);
    static final String STORE_TITLES_CONTRACTED = ShoppingApp.getStringRes(R.string.cvStoreTitlesContracted);

    private String itemExpansion;
    static final String ITEMS_EXPANDED = ShoppingApp.getStringRes(R.string.cvItemsExpanded);
    static final String ITEMS_CONTRACTED = ShoppingApp.getStringRes(R.string.cvItemsContracted);

    private String reorderingMethod;
    static final String DRAG_AND_DROP = ShoppingApp.getStringRes(R.string.cvDragAndDrop);
    static final String UP_AND_DOWN_ARROWS = ShoppingApp.getStringRes(R.string.cvUpAndDownArrows);
    static final String WITH_NUMBERS = ShoppingApp.getStringRes(R.string.cvWithNumbers);

    private String colorScheme;
    static final String COLOR_SCHEME_1 = ShoppingApp.getStringRes(R.string.cvColorScheme1);
    static final String COLOR_SCHEME_2 = ShoppingApp.getStringRes(R.string.cvColorScheme2);
    static final String COLOR_SCHEME_3 = ShoppingApp.getStringRes(R.string.cvColorScheme3);

    private String swipingOption;
    static final String SWIPING_ON = ShoppingApp.getStringRes(R.string.cvSwipingOn);
    static final String SWIPING_OFF = ShoppingApp.getStringRes(R.string.cvSwipingOff);

    private String picturesOption;
    static final String PICTURES_ON = ShoppingApp.getStringRes(R.string.cvPicturesOn);
    static final String PICTURES_OFF = ShoppingApp.getStringRes(R.string.cvPicturesOff);

    private String optionalDataQuantity;
    private String optionalDataPrice;
    private String optionalDataLocation;
    private String optionalDataNote;
    static final String OPTIONAL_DATA_ON = ShoppingApp.getStringRes(R.string.cvOptionalDataOn);
    static final String OPTIONAL_DATA_OFF = ShoppingApp.getStringRes(R.string.cvOptionalDataOff);

    private String reorderCategoryEmoji;
    private String reorderItemByCategoryEmoji;
    private String reorderItemByStoreEmoji;
    private String reorderStoreEmoji;

    private Parcelable shoppingListViewState;
    private Parcelable fullInventoryViewState;
    private Parcelable searchInventoryViewState;
    private Parcelable reorderCategoriesViewState;
    private Parcelable reorderStoresViewState;
    private Parcelable reorderItemsRecyclerViewState;
    private Parcelable reorderItemsScrollViewState;  // check instances of this

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping);

        initializeData();
        loadSharedPreferences();

        setSearchAlgorithm(new SearchAlgorithm(getBaseContext()));
        for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
            getSearchAlgorithm().addNewItem(getItemData().getItemListAZ().get(i));
        }

        Button fullInventory = findViewById(R.id.fullInventoryTopMenu);
        fullInventory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = getFragmentManager().findFragmentById(R.id.fragments);
                if (f instanceof FullInventory) return;
                loadFragment(new FullInventory());
            }
        });

        Button shoppingList = findViewById(R.id.shoppingListTopMenu);
        shoppingList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment f = getFragmentManager().findFragmentById(R.id.fragments);
                if (f instanceof ShoppingList) return;
                loadFragment(new ShoppingList());
            }
        });
    }

    private Shopping getThis() {
        return this;
    }

    ItemData getItemData() {
        return itemData;
    }

    private void setItemData(ItemData itemData) {
        getThis().itemData = itemData;
    }

    void updateItemData() {
        getDbItemHelper().readItemDataByCategory(getItemData());
        getDbItemHelper().readItemDataByStore(getItemData());
    }

    StatusData getStatusData() {
        return statusData;
    }

    private void setStatusData(StatusData statusData) {
        getThis().statusData = statusData;
    }

    void updateStatusData() {
        setStatusData(getDbStatusHelper().readStatusData());
    }

    CategoryData getCategoryData() {
        return categoryData;
    }

    private void setCategoryData(CategoryData categoryData) {
        getThis().categoryData = categoryData;
    }

    void updateCategoryData() {
        setCategoryData(getDbCategoryHelper().readCategoryData());
    }

    StoreData getStoreData() {
        return storeData;
    }

    private void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
    }

    void updateStoreData() {
        setStoreData(getDbStoreHelper().readStoreData());
    }

    private DBItemHelper getDbItemHelper() {
        return dbItemHelper;
    }

    private void setDbItemHelper(DBItemHelper dbItemHelper) {
        getThis().dbItemHelper = dbItemHelper;
    }

    private DBStatusHelper getDbStatusHelper() {
        return dbStatusHelper;
    }

    private void setDbStatusHelper(DBStatusHelper dbStatusHelper) {
        getThis().dbStatusHelper = dbStatusHelper;
    }

    private DBCategoryHelper getDbCategoryHelper() {
        return dbCategoryHelper;
    }

    private void setDbCategoryHelper(DBCategoryHelper dbCategoryHelper) {
        getThis().dbCategoryHelper = dbCategoryHelper;
    }

    private DBStoreHelper getDbStoreHelper() {
        return dbStoreHelper;
    }

    private void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
        getThis().dbStoreHelper = dbStoreHelper;
    }

    boolean itemIsSelectedInInventory() {
        return itemIsSelectedInInventory;
    }

    void setItemIsSelectedInInventory(boolean itemIsSelectedInInventory) {
        getThis().itemIsSelectedInInventory = itemIsSelectedInInventory;
    }

    boolean itemIsSelectedInSearchResults() {
        return itemIsSelectedInSearchResults;
    }

    void setItemIsSelectedInSearchResults(boolean itemIsSelectedInSearchResults) {
        getThis().itemIsSelectedInSearchResults = itemIsSelectedInSearchResults;
    }

    boolean itemIsSelectedInShoppingList() {
        return itemIsSelectedInShoppingList;
    }

    void setItemIsSelectedInShoppingList(boolean itemIsSelectedInShoppingList) {
        getThis().itemIsSelectedInShoppingList = itemIsSelectedInShoppingList;
    }

    Item getSelectedItemInInventory() {
        return selectedItemInInventory;
    }

    void setSelectedItemInInventory(Item selectedItemInInventory) {
        getThis().selectedItemInInventory = selectedItemInInventory;
    }

    Item getSelectedItemInSearchResults() {
        return selectedItemInSearchResults;
    }

    void setSelectedItemInSearchResults(Item selectedItemInSearchResults) {
        getThis().selectedItemInSearchResults = selectedItemInSearchResults;
    }

    Item getSelectedItemInShoppingList() {
        return selectedItemInShoppingList;
    }

    void setSelectedItemInShoppingList(Item selectedItemInShoppingList) {
        getThis().selectedItemInShoppingList = selectedItemInShoppingList;
    }

    int getSelectedItemPositionInInventory() {
        return selectedItemPositionInInventory;
    }

    void setSelectedItemPositionInInventory(int selectedItemPositionInInventory) {
        getThis().selectedItemPositionInInventory = selectedItemPositionInInventory;
    }

    int getSelectedItemPositionInSearchResults() {
        return selectedItemPositionInSearchResults;
    }

    void setSelectedItemPositionInSearchResults(int selectedItemPositionInSearchResults) {
        getThis().selectedItemPositionInSearchResults = selectedItemPositionInSearchResults;
    }

    int getSelectedItemPositionInShoppingList() {
        return selectedItemPositionInShoppingList;
    }

    void setSelectedItemPositionInShoppingList(int selectedItemPositionInShoppingList) {
        getThis().selectedItemPositionInShoppingList = selectedItemPositionInShoppingList;
    }

    Item getItemInPictureDialog() {
        return itemInPictureDialog;
    }

    void setItemInPictureDialog(Item itemInPictureDialog) {
        getThis().itemInPictureDialog = itemInPictureDialog;
    }

    int getStoreListOrderNum() {
        return storeListOrderNum;
    }

    void setStoreListOrderNum(int storeListOrderNum) {
        getThis().storeListOrderNum = storeListOrderNum;
    }

    String getReorderItemsCategory() {
        return reorderItemsCategory;
    }

    void setReorderItemsCategory(String reorderItemsCategory) {
        getThis().reorderItemsCategory = reorderItemsCategory;
    }

    String getReorderItemsStore() {
        return reorderItemsStore;
    }

    void setReorderItemsStore(String reorderItemsStore) {
        getThis().reorderItemsStore = reorderItemsStore;
    }

    boolean editItemInInventory() {
        return editItemInInventory;
    }

    void setEditItemInInventory(boolean editItemInInventory) {
        getThis().editItemInInventory = editItemInInventory;
    }

    boolean editItemInSearchResults() {
        return editItemInSearchResults;
    }

    void setEditItemInSearchResults(boolean editItemInSearchResults) {
        getThis().editItemInSearchResults = editItemInSearchResults;
    }

    boolean editItemInShoppingList() {
        return editItemInShoppingList;
    }

    void setEditItemInShoppingList(boolean editItemInShoppingList) {
        getThis().editItemInShoppingList = editItemInShoppingList;
    }

    boolean editItemInPictureDialog() {
        return editItemInPictureDialog;
    }

    void setEditItemInPictureDialog(boolean editItemInPictureDialog) {
        getThis().editItemInPictureDialog = editItemInPictureDialog;
    }

    boolean pictureDialogInInventory() {
        return pictureDialogInInventory;
    }

    void setPictureDialogInInventory(boolean pictureDialogInInventory) {
        getThis().pictureDialogInInventory = pictureDialogInInventory;
    }

    boolean pictureDialogInSearchResults() {
        return pictureDialogInSearchResults;
    }

    void setPictureDialogInSearchResults(boolean pictureDialogInSearchResults) {
        getThis().pictureDialogInSearchResults = pictureDialogInSearchResults;
    }

    boolean pictureDialogInShoppingList() {
        return pictureDialogInShoppingList;
    }

    void setPictureDialogInShoppingList(boolean pictureDialogInShoppingList) {
        getThis().pictureDialogInShoppingList = pictureDialogInShoppingList;
    }

    SearchAlgorithm getSearchAlgorithm() {
        return searchAlgorithm;
    }

    private void setSearchAlgorithm(SearchAlgorithm searchAlgorithm) {
        getThis().searchAlgorithm = searchAlgorithm;
    }

    private AlertDialog getAlertDialog() {
        return alertDialog;
    }

    private void setAlertDialog(AlertDialog alertDialog) {
        getThis().alertDialog = alertDialog;
    }

    public View getAlertDialogView() {
        return alertDialogView;
    }

    public void setAlertDialogView(View alertDialogView) {
        getThis().alertDialogView = alertDialogView;
    }

    public TextView getAlertDialogTitle() {
        return alertDialogTitle;
    }

    public void setAlertDialogTitle(TextView alertDialogTitle) {
        this.alertDialogTitle = alertDialogTitle;
    }

    public TextView getAlertDialogMessage() {
        return alertDialogMessage;
    }

    public void setAlertDialogMessage(TextView alertDialogMessage) {
        this.alertDialogMessage = alertDialogMessage;
    }

    private TextView getAlertDialogButton() {
        return alertDialogButton;
    }

    private void setAlertDialogButton(TextView alertDialogButton) {
        getThis().alertDialogButton = alertDialogButton;
    }

    private AlertDialog getPictureDialog() {
        return pictureDialog;
    }

    private void setPictureDialog(AlertDialog pictureDialog) {
        getThis().pictureDialog = pictureDialog;
    }

    private View getPictureDialogView() {
        return pictureDialogView;
    }

    private void setPictureDialogView(View pictureDialogView) {
        getThis().pictureDialogView = pictureDialogView;
    }

    private TextView getCameraButton() {
        return cameraButton;
    }

    private void setCameraButton(TextView cameraButton) {
        getThis().cameraButton = cameraButton;
    }

    private TextView getEditButton() {
        return editButton;
    }

    private void setEditButton(TextView editButton) {
        getThis().editButton = editButton;
    }

    private TextView getTakeButton() {
        return takeButton;
    }

    private void setTakeButton(TextView takeButton) {
        getThis().takeButton = takeButton;
    }

    private TextView getCancelButton() {
        return cancelButton;
    }

    private void setCancelButton(TextView cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    private LinearLayout getCameraEditButtons() {
        return cameraEditButtons;
    }

    private void setCameraEditButtons(LinearLayout cameraEditButtons) {
        getThis().cameraEditButtons = cameraEditButtons;
    }

    private LinearLayout getTakeCancelButtons() {
        return takeCancelButtons;
    }

    private void setTakeCancelButtons(LinearLayout takeCancelButtons) {
        getThis().takeCancelButtons = takeCancelButtons;
    }

    String getInventoryView() {
        return inventoryView;
    }

    void setInventoryView(String inventoryView) {
        getThis().inventoryView = inventoryView;
    }

    String getInventorySortBy() {
        return inventorySortBy;
    }

    void setInventorySortBy(String inventorySortBy) {
        getThis().inventorySortBy = inventorySortBy;
    }

    String getDefaultSortBy() {
        return defaultSortBy;
    }

    void setDefaultSortBy(String defaultSortBy) {
        getThis().defaultSortBy = defaultSortBy;
    }

    String getCategoryTitles() {
        return categoryTitles;
    }

    void setCategoryTitles(String categoryTitles) {
        getThis().categoryTitles = categoryTitles;
    }

    String getDefaultCategoryTitles() {
        return defaultCategoryTitles;
    }

    void setDefaultCategoryTitles(String defaultCategoryTitles) {
        getThis().defaultCategoryTitles = defaultCategoryTitles;
    }

    String getStoreTitles() {
        return storeTitles;
    }

    void setStoreTitles(String storeTitles) {
        getThis().storeTitles = storeTitles;
    }

    String getDefaultStoreTitles() {
        return defaultStoreTitles;
    }

    void setDefaultStoreTitles(String defaultStoreTitles) {
        getThis().defaultStoreTitles = defaultStoreTitles;
    }

    String getItemExpansion() {
        return itemExpansion;
    }

    void setItemExpansion(String itemExpansion) {
        getThis().itemExpansion = itemExpansion;
    }

    String getReorderingMethod() {
        return reorderingMethod;
    }

    void setReorderingMethod(String reorderingMethod) {
        getThis().reorderingMethod = reorderingMethod;
    }

    String getColorScheme() {
        return colorScheme;
    }

    void setColorScheme(String colorScheme) {
        getThis().colorScheme = colorScheme;
    }

    String getSwipingOption() {
        return swipingOption;
    }

    void setSwipingOption(String swipingOption) {
        getThis().swipingOption = swipingOption;
    }

    String getPicturesOption() {
        return picturesOption;
    }

    void setPicturesOption(String picturesOption) {
        getThis().picturesOption = picturesOption;
    }

    String getOptionalDataQuantity() {
        return optionalDataQuantity;
    }

    void setOptionalDataQuantity(String optionalDataQuantity) {
        getThis().optionalDataQuantity = optionalDataQuantity;
    }

    String getOptionalDataPrice() {
        return optionalDataPrice;
    }

    void setOptionalDataPrice(String optionalDataPrice) {
        getThis().optionalDataPrice = optionalDataPrice;
    }

    String getOptionalDataLocation() {
        return optionalDataLocation;
    }

    void setOptionalDataLocation(String optionalDataLocation) {
        getThis().optionalDataLocation = optionalDataLocation;
    }

    String getOptionalDataNote() {
        return optionalDataNote;
    }

    void setOptionalDataNote(String optionalDataNote) {
        getThis().optionalDataNote = optionalDataNote;
    }

    String getReorderCategoryEmoji() {
        return reorderCategoryEmoji;
    }

    void setReorderCategoryEmoji(String reorderCategoryEmoji) {
        getThis().reorderCategoryEmoji = reorderCategoryEmoji;
    }

    String getReorderItemByCategoryEmoji() {
        return reorderItemByCategoryEmoji;
    }

    void setReorderItemByCategoryEmoji(String reorderItemByCategoryEmoji) {
        getThis().reorderItemByCategoryEmoji = reorderItemByCategoryEmoji;
    }

    String getReorderItemByStoreEmoji() {
        return reorderItemByStoreEmoji;
    }

    void setReorderItemByStoreEmoji(String reorderItemByStoreEmoji) {
        getThis().reorderItemByStoreEmoji = reorderItemByStoreEmoji;
    }

    String getReorderStoreEmoji() {
        return reorderStoreEmoji;
    }

    void setReorderStoreEmoji(String reorderStoreEmoji) {
        getThis().reorderStoreEmoji = reorderStoreEmoji;
    }

    Parcelable getShoppingListViewState() {
        return shoppingListViewState;
    }

    void setShoppingListViewState(Parcelable shoppingListViewState) {
        getThis().shoppingListViewState = shoppingListViewState;
    }

    Parcelable getFullInventoryViewState() {
        return fullInventoryViewState;
    }

    void setFullInventoryViewState(Parcelable fullInventoryViewState) {
        getThis().fullInventoryViewState = fullInventoryViewState;
    }

    Parcelable getSearchInventoryViewState() {
        return searchInventoryViewState;
    }

    void setSearchInventoryViewState(Parcelable searchInventoryViewState) {
        getThis().searchInventoryViewState = searchInventoryViewState;
    }

    Parcelable getReorderCategoriesViewState() {
        return reorderCategoriesViewState;
    }

    void setReorderCategoriesViewState(Parcelable reorderCategoriesViewState) {
        getThis().reorderCategoriesViewState = reorderCategoriesViewState;
    }

    Parcelable getReorderStoresViewState() {
        return reorderStoresViewState;
    }

    void setReorderStoresViewState(Parcelable reorderStoresViewState) {
        getThis().reorderStoresViewState = reorderStoresViewState;
    }

    Parcelable getReorderItemsRecyclerViewState() {
        return reorderItemsRecyclerViewState;
    }

    void setReorderItemsRecyclerViewState(Parcelable reorderItemsRecyclerViewState) {
        getThis().reorderItemsRecyclerViewState = reorderItemsRecyclerViewState;
    }

    Parcelable getReorderItemsScrollViewState() {
        return reorderItemsScrollViewState;
    }

    void setReorderItemsScrollViewState(Parcelable reorderItemsScrollViewState) {
        getThis().reorderItemsScrollViewState = reorderItemsScrollViewState;
    }

    void showAlertDialog(String title, String message, String button) {

        LayoutInflater inflater = LayoutInflater.from(getThis());
        setAlertDialogView(inflater.inflate(R.layout.alert_dialog, null));
        AlertDialog.Builder builder = new AlertDialog.Builder(getThis());
        builder.setView(getAlertDialogView());

        setAlertDialogTitle((TextView) getAlertDialogView().findViewById(R.id.alertDialogTitle));
        getAlertDialogTitle().setText(title);

        setAlertDialogMessage((TextView) getAlertDialogView().findViewById(R.id.alertDialogMessage));
        getAlertDialogMessage().setText(message);

        setAlertDialogButton((TextView) getAlertDialogView().findViewById(R.id.alertDialogButton));
        getAlertDialogButton().setText(button);

        getAlertDialogButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getAlertDialog().dismiss();

            }
        });

        setAlertDialog(builder.create());
        getAlertDialog().getWindow().setDimAmount(0.2f);
        getAlertDialog().setCanceledOnTouchOutside(false);
        getAlertDialog().setCancelable(false);
        getAlertDialog().show();

    }

    void showPictureDialog(Item item) {

        setItemInPictureDialog(item);
        LayoutInflater inflater = LayoutInflater.from(getThis());
        setPictureDialogView(inflater.inflate(R.layout.picture_dialog, null));
        AlertDialog.Builder builder = new AlertDialog.Builder(getThis());
        builder.setView(getPictureDialogView());

        TextView pictureDialogTitle = getPictureDialogView().findViewById(R.id.pictureDialogTitle);
        pictureDialogTitle.setText(item.getItemName());
        
        setCameraButton((TextView) getPictureDialogView().findViewById(R.id.cameraButton));
        setEditButton((TextView) getPictureDialogView().findViewById(R.id.editButton));
        setTakeButton((TextView) getPictureDialogView().findViewById(R.id.takeButton));
        setCancelButton((TextView) getPictureDialogView().findViewById(R.id.cancelButton));
        setCameraEditButtons((LinearLayout) getPictureDialogView().findViewById(R.id.cameraEditButtons));
        setTakeCancelButtons((LinearLayout) getPictureDialogView().findViewById(R.id.takeCancelButtons));

        getCameraButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getCameraEditButtons().setVisibility(View.GONE);
                getTakeCancelButtons().setVisibility(View.VISIBLE);

            }
        });

        getTakeButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getTakeCancelButtons().setVisibility(View.GONE);
                getCameraEditButtons().setVisibility(View.VISIBLE);
            }
        });

        getCancelButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getTakeCancelButtons().setVisibility(View.GONE);
                getCameraEditButtons().setVisibility(View.VISIBLE);
            }
        });

        getEditButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setEditItemInPictureDialog(true);
                setEditItemInInventory(false);
                setEditItemInSearchResults(false);
                setEditItemInShoppingList(false);
                getPictureDialog().dismiss();
                loadFragment(new EditItem());
            }
        });

        getPictureDialogView().setOnClickListener(new View.OnClickListener() {

            private long doubleClickTimeout = 400;
            private long lastClickTime = 0;

            public void onClick(View v) {
                long clickTime = SystemClock.uptimeMillis();
                if (clickTime - getLastClickTime() < getDoubleClickTimeout()) {
                    onDoubleClick(v);
                }
                setLastClickTime(clickTime);
            }

            private void onDoubleClick(View v) {
                if (pictureDialogInInventory()) setPictureDialogInInventory(false);
                if (pictureDialogInSearchResults()) setPictureDialogInSearchResults(false);
                if (pictureDialogInShoppingList()) setPictureDialogInShoppingList(false);
                getPictureDialog().dismiss();
            }

            private long getDoubleClickTimeout() {
                return doubleClickTimeout;
            }

            private long getLastClickTime() {
                return lastClickTime;
            }

            private void setLastClickTime(long lastClickTime) {
                this.lastClickTime = lastClickTime;
            }
        });

        setPictureDialog(builder.create());
        getPictureDialog().getWindow().setDimAmount(0.2f);
        getPictureDialog().show();
    }

    void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getThis().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getThis().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragments, fragment);
        fragmentTransaction.commit();
    }

    void loadSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.spPreferencesFile), Context.MODE_PRIVATE);

        String defaultSortBy = sharedPref.getString(getString(R.string.sp_default_sort_by), getString(R.string.spDefaultSortBy));
        if (defaultSortBy.equals(getString(R.string.spSortAlphabetically))) {
            getThis().setDefaultSortBy(SORT_ALPHABETICAL);
            getThis().setInventorySortBy(SORT_ALPHABETICAL);
        } else if (defaultSortBy.equals(getString(R.string.spSortByCategory))) {
            getThis().setDefaultSortBy(SORT_BY_CATEGORY);
            getThis().setInventorySortBy(SORT_BY_CATEGORY);
        } else if (defaultSortBy.equals(getString(R.string.spSortByStore))) {
            getThis().setDefaultSortBy(SORT_BY_STORE);
            getThis().setInventorySortBy(SORT_BY_STORE);
        }

        String defaultCategoryTitles = sharedPref.getString(getString(R.string.sp_default_category_titles), getString(R.string.spDefaultCategoryTitles));
        if (defaultCategoryTitles.equals(getString(R.string.spCategoryTitlesExpanded))) {
            getThis().setDefaultCategoryTitles(CATEGORY_TITLES_EXPANDED);
            getThis().setCategoryTitles(CATEGORY_TITLES_EXPANDED);
        } else if (defaultCategoryTitles.equals(getString(R.string.spCategoryTitlesContracted))) {
            getThis().setDefaultCategoryTitles(CATEGORY_TITLES_CONTRACTED);
            getThis().setCategoryTitles(CATEGORY_TITLES_CONTRACTED);
        }

        String defaultStoreTitles = sharedPref.getString(getString(R.string.sp_default_store_titles), getString(R.string.spDefaultStoreTitles));
        if (defaultStoreTitles.equals(getString(R.string.spStoreTitlesExpanded))) {
            getThis().setDefaultStoreTitles(STORE_TITLES_EXPANDED);
            getThis().setStoreTitles(STORE_TITLES_EXPANDED);
        } else if (defaultStoreTitles.equals(getString(R.string.spStoreTitlesContracted))) {
            getThis().setDefaultStoreTitles(STORE_TITLES_CONTRACTED);
            getThis().setStoreTitles(STORE_TITLES_CONTRACTED);
        }

        String reorderingMethod = sharedPref.getString(getString(R.string.sp_reorder_method), getString(R.string.spDefaultReordering));
        if (reorderingMethod.equals(getString(R.string.spDragAndDrop))) {
            getThis().setReorderingMethod(DRAG_AND_DROP);
        } else if (reorderingMethod.equals(getString(R.string.spUpAndDownArrows))) {
            getThis().setReorderingMethod(UP_AND_DOWN_ARROWS);
        } else if (reorderingMethod.equals(getString(R.string.spWithNumbers))) {
            getThis().setReorderingMethod(WITH_NUMBERS);
        }

        String colorScheme = sharedPref.getString(getString(R.string.sp_color_scheme), getString(R.string.spDefaultColorScheme));
        if (colorScheme.equals(getString(R.string.spColorScheme1))) {
            getThis().setColorScheme(COLOR_SCHEME_1);
        } else if (colorScheme.equals(getString(R.string.spColorScheme2))) {
            getThis().setColorScheme(COLOR_SCHEME_2);
        } else if (colorScheme.equals(getString(R.string.spColorScheme3))) {
            getThis().setColorScheme(COLOR_SCHEME_3);
        }

        String swipingOption = sharedPref.getString(getString(R.string.sp_swiping_option), getString(R.string.spDefaultSwipingOption));
        if (swipingOption.equals(getString(R.string.spSwipingOn))) {
            getThis().setSwipingOption(SWIPING_ON);
        } else if (swipingOption.equals(getString(R.string.spSwipingOff))) {
            getThis().setSwipingOption(SWIPING_OFF);
        }

        String picturesOption = sharedPref.getString(getString(R.string.sp_pictures_option), getString(R.string.spDefaultPicturesOption));
        if (picturesOption.equals(getString(R.string.spPicturesOn))) {
            getThis().setPicturesOption(PICTURES_ON);
        } else if (picturesOption.equals(getString(R.string.spPicturesOff))) {
            getThis().setPicturesOption(PICTURES_OFF);
        }

        String optionalDataQuantity = sharedPref.getString(getString(R.string.sp_optional_data_quantity), getString(R.string.spDefaultOptionalDataQuantity));
        if (optionalDataQuantity.equals(getString(R.string.spOptionalDataOn))) {
            getThis().setOptionalDataQuantity(OPTIONAL_DATA_ON);
        } else if (optionalDataQuantity.equals(getString(R.string.spOptionalDataOff))) {
            getThis().setOptionalDataQuantity(OPTIONAL_DATA_OFF);
        }

        String optionalDataPrice = sharedPref.getString(getString(R.string.sp_optional_data_price), getString(R.string.spDefaultOptionalDataPrice));
        if (optionalDataPrice.equals(getString(R.string.spOptionalDataOn))) {
            getThis().setOptionalDataPrice(OPTIONAL_DATA_ON);
        } else if (optionalDataPrice.equals(getString(R.string.spOptionalDataOff))) {
            getThis().setOptionalDataPrice(OPTIONAL_DATA_OFF);
        }

        String optionalDataLocation = sharedPref.getString(getString(R.string.sp_optional_data_location), getString(R.string.spDefaultOptionalDataLocation));
        if (optionalDataLocation.equals(getString(R.string.spOptionalDataOn))) {
            getThis().setOptionalDataLocation(OPTIONAL_DATA_ON);
        } else if (optionalDataLocation.equals(getString(R.string.spOptionalDataOff))) {
            getThis().setOptionalDataLocation(OPTIONAL_DATA_OFF);
        }

        String optionalDataNote = sharedPref.getString(getString(R.string.sp_optional_data_note), getString(R.string.spDefaultOptionalDataNote));
        if (optionalDataNote.equals(getString(R.string.spOptionalDataOn))) {
            getThis().setOptionalDataNote(OPTIONAL_DATA_ON);
        } else if (optionalDataNote.equals(getString(R.string.spOptionalDataOff))) {
            getThis().setOptionalDataNote(OPTIONAL_DATA_OFF);
        }

        String reorderCategoryEmoji = sharedPref.getString(getString(R.string.sp_reorder_category_emoji), getString(R.string.spDefaultReorderCategoryEmoji));
        getThis().setReorderCategoryEmoji(reorderCategoryEmoji);

        String reorderItemByCategoryEmoji = sharedPref.getString(getString(R.string.sp_reorder_item_by_category_emoji), getString(R.string.spDefaultReorderItemByCategoryEmoji));
        getThis().setReorderItemByCategoryEmoji(reorderItemByCategoryEmoji);

        String reorderItemByStoreEmoji = sharedPref.getString(getString(R.string.sp_reorder_item_by_store_emoji), getString(R.string.spDefaultReorderItemByStoreEmoji));
        getThis().setReorderItemByStoreEmoji(reorderItemByStoreEmoji);

        String reorderStoreEmoji = sharedPref.getString(getString(R.string.sp_reorder_store_emoji), getString(R.string.spDefaultReorderStoreEmoji));
        getThis().setReorderStoreEmoji(reorderStoreEmoji);

    }

    void clearAllData() {
        getDbItemHelper().deleteDatabase();
        getDbStatusHelper().deleteDatabase();
        getDbCategoryHelper().deleteDatabase();
        getDbStoreHelper().deleteDatabase();
    }

    void initializeData() {
        setDbItemHelper(new DBItemHelper(this));
        setDbStatusHelper(new DBStatusHelper(this));
        setDbCategoryHelper(new DBCategoryHelper(this));
        setDbStoreHelper(new DBStoreHelper(this));

        setItemData(new ItemData(getBaseContext()));
        setStatusData(new StatusData(getBaseContext()));
        setCategoryData(new CategoryData(getBaseContext()));
        setStoreData(new StoreData(getBaseContext()));

        getDbItemHelper().readItemDataByCategory(getItemData());
        getDbItemHelper().readItemDataByStore(getItemData());
        setStatusData(getDbStatusHelper().readStatusData());
        getItemData().updateStatuses(getStatusData());
        setCategoryData(getDbCategoryHelper().readCategoryData());
        setStoreData(getDbStoreHelper().readStoreData());

        setItemIsSelectedInInventory(false);
        setItemIsSelectedInSearchResults(false);
        setItemIsSelectedInShoppingList(false);
        setSelectedItemInInventory(null);
        setSelectedItemInSearchResults(null);
        setSelectedItemInShoppingList(null);
        setSelectedItemPositionInInventory(0);
        setSelectedItemPositionInSearchResults(0);
        setSelectedItemPositionInShoppingList(0);

        setStoreListOrderNum(0);
        setReorderItemsCategory(getString(R.string.emptyString));
        setReorderItemsStore(getString(R.string.emptyString));
        setEditItemInInventory(false);
        setEditItemInSearchResults(false);
        setEditItemInShoppingList(false);
        setEditItemInPictureDialog(false);
        setPictureDialogInInventory(false);
        setPictureDialogInSearchResults(false);
        setPictureDialogInShoppingList(false);

        setInventoryView(VIEW_ALL);
        setInventorySortBy(defaultSortBy);
        setCategoryTitles(defaultCategoryTitles);
        setStoreTitles(defaultStoreTitles);
        setItemExpansion(ITEMS_CONTRACTED);
        setReorderingMethod(UP_AND_DOWN_ARROWS);
        setSwipingOption(SWIPING_ON);
        setPicturesOption(PICTURES_ON);
        setOptionalDataQuantity(OPTIONAL_DATA_OFF);
        setOptionalDataPrice(OPTIONAL_DATA_OFF);
        setOptionalDataLocation(OPTIONAL_DATA_OFF);
        setOptionalDataNote(OPTIONAL_DATA_OFF);

    }

    void loadStoresAndCategories() {

        getDbCategoryHelper().addNewCategory(getString(R.string.meals), 0);
        getDbCategoryHelper().addNewCategory(getString(R.string.soups), 1);
        getDbCategoryHelper().addNewCategory(getString(R.string.sides), 2);
        getDbCategoryHelper().addNewCategory(getString(R.string.meat), 3);
        getDbCategoryHelper().addNewCategory(getString(R.string.breadGrainsCereal), 4);
        getDbCategoryHelper().addNewCategory(getString(R.string.eggsDairy), 5);
        getDbCategoryHelper().addNewCategory(getString(R.string.condiments), 6);
        getDbCategoryHelper().addNewCategory(getString(R.string.seasonings), 7);
        getDbCategoryHelper().addNewCategory(getString(R.string.miscIngredients), 8);
        getDbCategoryHelper().addNewCategory(getString(R.string.drinks), 9);
        getDbCategoryHelper().addNewCategory(getString(R.string.snacks), 10);
        getDbCategoryHelper().addNewCategory(getString(R.string.desserts), 11);
        getDbCategoryHelper().addNewCategory(getString(R.string.candy), 12);
        getDbCategoryHelper().addNewCategory(getString(R.string.petSupplies), 13);
        getDbCategoryHelper().addNewCategory(getString(R.string.toiletries), 14);
        getDbCategoryHelper().addNewCategory(getString(R.string.household), 15);
        getDbCategoryHelper().addNewCategory(getString(R.string.supplements), 16);

        getDbStoreHelper().addNewStore(getString(R.string.vons), 0);
        getDbStoreHelper().addNewStore(getString(R.string.smartFinal), 1);
        getDbStoreHelper().addNewStore(getString(R.string.costco), 2);
        getDbStoreHelper().addNewStore(getString(R.string.walmart), 3);
        getDbStoreHelper().addNewStore(getString(R.string.amazon), 4);
        getDbStoreHelper().addNewStore(getString(R.string.staterBros), 5);
        getDbStoreHelper().addNewStore(getString(R.string.traderJoes), 6);
        getDbStoreHelper().addNewStore(getString(R.string.cvs), 7);
        getDbStoreHelper().addNewStore(getString(R.string.dollarTree), 8);
        getDbStoreHelper().addNewStore(getString(R.string.ralphs), 9);
        getDbStoreHelper().addNewStore(getString(R.string.target), 10);
        getDbStoreHelper().addNewStore(getString(R.string.petSuppliesPlus), 11);
        getDbStoreHelper().addNewStore(getString(R.string.sprouts), 12);
        getDbStoreHelper().addNewStore(getString(R.string.samsClub), 13);
        getDbStoreHelper().addNewStore(getString(R.string.staples), 14);
        getDbStoreHelper().addNewStore(getString(R.string.woodranch), 15);
        getDbStoreHelper().addNewStore(getString(R.string.yorbaLindaFeedStore), 16);

    }

//------------------------------------------------------------------------------------------------//
//-------------------------------------Sort By Category-------------------------------------------//
//------------------------------------------------------------------------------------------------//

    void loadCategoryData1() {

        //------------------------------------Meals-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.sausageBiscuits), getString(R.string.jimmyDeanFrozen), getString(R.string.meals), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.sausageBiscuits), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hamburgerHelper), getString(R.string.cheeseburgerMacaroni), getString(R.string.meals), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.hamburgerHelper), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.buffaloChickenBites), getString(R.string.tGIForFranks), getString(R.string.meals), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.buffaloChickenBites), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.terriyakiChickenBites), getString(R.string.innovAsian), getString(R.string.meals), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.terriyakiChickenBites), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.crispyBuffaloWings), getString(R.string.fosterFarms), getString(R.string.meals), getString(R.string.costco), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.crispyBuffaloWings), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tgifCheeseSticks), getString(R.string.tgifSmall10pc), getString(R.string.meals), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.tgifCheeseSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.mozarellaCheeseSticks), getString(R.string.farmRich), getString(R.string.meals), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.mozarellaCheeseSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.frozenPizza), getString(R.string.thinPepperoni), getString(R.string.meals), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.frozenPizza), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cornDogs), getString(R.string.fosterFarms), getString(R.string.meals), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.cornDogs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hotDogs), getString(R.string.bunSize), getString(R.string.meals), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.hotDogs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hotDogBuns), getString(R.string.pack8), getString(R.string.meals), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.hotDogBuns), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hamburgerPatties), getString(R.string.toDo), getString(R.string.meals), getString(R.string.vons), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.hamburgerPatties), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hamburgerBuns), getString(R.string.pack8), getString(R.string.meals), getString(R.string.vons), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.hamburgerBuns), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.pastaRoni1), getString(R.string.angelHairPasta), getString(R.string.meals), getString(R.string.vons), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.pastaRoni1), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.pastaRoni2), getString(R.string.fettuccineAlfredo), getString(R.string.meals), getString(R.string.vons), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.pastaRoni2), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.macAndCheese), getString(R.string.annies), getString(R.string.meals), getString(R.string.vons), 15);
        getDbStatusHelper().addNewStatus(getString(R.string.macAndCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.gnocci), getString(R.string.signatureSelect), getString(R.string.meals), getString(R.string.vons), 16);
        getDbStatusHelper().addNewStatus(getString(R.string.gnocci), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tortellini), getString(R.string.barilla3Cheese), getString(R.string.meals), getString(R.string.vons), 17);
        getDbStatusHelper().addNewStatus(getString(R.string.tortellini), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.meals), 18);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.meals), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.meals), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.meals), 18);
        getDbCategoryHelper().setCategoryViews(getString(R.string.meals), 18, 0, 0, 18);

        //------------------------------------Soups-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.spaghettiOs), getString(R.string.wMeatballs), getString(R.string.soups), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.spaghettiOs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chickenNoodleSoup), getString(R.string.campbells), getString(R.string.soups), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.chickenNoodleSoup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.minestroneSoup), getString(R.string.amys), getString(R.string.soups), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.minestroneSoup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vegetableBarleySoup), getString(R.string.amys), getString(R.string.soups), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.vegetableBarleySoup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.beefNoodles), getString(R.string.yakisoba), getString(R.string.soups), getString(R.string.staterBros), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.beefNoodles), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cupOfNoodles), getString(R.string.nissin), getString(R.string.soups), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.cupOfNoodles), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.ramenNoodles), getString(R.string.nissin), getString(R.string.soups), getString(R.string.dollarTree), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.ramenNoodles), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.soups), 7);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.soups), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.soups), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.soups), 7);
        getDbCategoryHelper().setCategoryViews(getString(R.string.soups), 7, 0, 0, 7);

        //------------------------------------Sides-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.frozenFrenchFries), getString(R.string.oreIda), getString(R.string.sides), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.frozenFrenchFries), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.texasCheesyBread), getString(R.string.newYorkBakery), getString(R.string.sides), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.texasCheesyBread), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.garlicBreadsticks), getString(R.string.newYorkBakery), getString(R.string.sides), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.garlicBreadsticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chickenRice), getString(R.string.knorr), getString(R.string.sides), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.chickenRice), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cannedCorn), getString(R.string.delMonte), getString(R.string.sides), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.cannedCorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.crescentRolls), getString(R.string.signatureSelect), getString(R.string.sides), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.crescentRolls), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.sides), 6);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.sides), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.sides), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.sides), 6);
        getDbCategoryHelper().setCategoryViews(getString(R.string.sides), 6, 0, 0, 6);

        //------------------------------------Meat--------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.steak), getString(R.string.usda), getString(R.string.meat), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.steak), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.groundBeef), getString(R.string.pound1), getString(R.string.meat), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.groundBeef), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.frozenMeatballs), getString(R.string.rosinaHomestyle), getString(R.string.meat), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.frozenMeatballs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.pepperoniSlices), getString(R.string.hormel300Slices), getString(R.string.meat), getString(R.string.smartFinal), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.pepperoniSlices), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.quickSteak), getString(R.string.garys), getString(R.string.meat), getString(R.string.samsClub), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.quickSteak), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chickenBreast), getString(R.string.na), getString(R.string.meat), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.chickenBreast), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.slicedTurkey), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.slicedTurkey), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.slicedHam), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.slicedHam), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hamSteak), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.hamSteak), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.beefLitlSmokies), getString(R.string.hillshireFarm), getString(R.string.meat), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.beefLitlSmokies), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.meat), 10);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.meat), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.meat), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.meat), 10);
        getDbCategoryHelper().setCategoryViews(getString(R.string.meat), 10, 0, 0, 10);

        //------------------------------------Bread/Grains/Cereal-----------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.thinSpaghetti), getString(R.string.barillaWholeGrain), getString(R.string.breadGrainsCereal), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.thinSpaghetti), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.spiralPasta), getString(R.string.barillaRotini), getString(R.string.breadGrainsCereal), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.spiralPasta), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.wheatBread), getString(R.string.naturesOwn), getString(R.string.breadGrainsCereal), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.wheatBread), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.baguette), getString(R.string.frenchOrSourdough), getString(R.string.breadGrainsCereal), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.baguette), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sourdoughBread), getString(R.string.sanLuis), getString(R.string.breadGrainsCereal), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.sourdoughBread), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hardRolls), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.hardRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.thomasMuffins), getString(R.string.original), getString(R.string.breadGrainsCereal), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.thomasMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.reesesPuffsCereal), getString(R.string.generalMills), getString(R.string.breadGrainsCereal), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.reesesPuffsCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cookieCrispCereal), getString(R.string.generalMills), getString(R.string.breadGrainsCereal), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.cookieCrispCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.frostedMiniWheatCereal), getString(R.string.kelloggs), getString(R.string.breadGrainsCereal), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.frostedMiniWheatCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.honeySmacksCereal), getString(R.string.kelloggs), getString(R.string.breadGrainsCereal), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.honeySmacksCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.eggoWaffles), getString(R.string.homestyle), getString(R.string.breadGrainsCereal), getString(R.string.vons), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.eggoWaffles), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.smallFlourTortillas), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.smallFlourTortillas), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.largeFlourTortillas), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.largeFlourTortillas), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.breadGrainsCereal), 14);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.breadGrainsCereal), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.breadGrainsCereal), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.breadGrainsCereal), 14);
        getDbCategoryHelper().setCategoryViews(getString(R.string.breadGrainsCereal), 14, 0, 0, 14);

        //----------------------------------------Eggs/Dairy----------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.milk), getString(R.string.vitaminD), getString(R.string.eggsDairy), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.milk), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.eggs), getString(R.string.gradeAAxLarge), getString(R.string.eggsDairy), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.eggs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.honeyYogurt), getString(R.string.greekGods), getString(R.string.eggsDairy), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.honeyYogurt), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sourCream), getString(R.string.toDo), getString(R.string.eggsDairy), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.sourCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.saltedButter), getString(R.string.challenge), getString(R.string.eggsDairy), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.saltedButter), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.clarifiedButter), getString(R.string.challenge), getString(R.string.eggsDairy), getString(R.string.ralphs), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.clarifiedButter), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.shreddedCheese), getString(R.string.mexicanBlend), getString(R.string.eggsDairy), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.shreddedCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.stringCheese), getString(R.string.mozarella), getString(R.string.eggsDairy), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.stringCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.bdCheese), getString(R.string.blackDiamond), getString(R.string.eggsDairy), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.bdCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.slicedCheese), getString(R.string.kraftSingles), getString(R.string.eggsDairy), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.slicedCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.halfAndHalf), getString(R.string.lucerne), getString(R.string.eggsDairy), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.halfAndHalf), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.eggsDairy), 11);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.eggsDairy), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.eggsDairy), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.eggsDairy), 11);
        getDbCategoryHelper().setCategoryViews(getString(R.string.eggsDairy), 11, 0, 0, 11);

        //------------------------------------Condiments--------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.parmesanCheese), getString(R.string.kraft), getString(R.string.condiments), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.parmesanCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.a1sauce), getString(R.string.original), getString(R.string.condiments), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.a1sauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.ketchup), getString(R.string.heinz), getString(R.string.condiments), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.ketchup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.mustard), getString(R.string.heinz), getString(R.string.condiments), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.mustard), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.pastaSauce), getString(R.string.raguMeat), getString(R.string.condiments), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.pastaSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.marinaraSauce), getString(R.string.signatureTraditional), getString(R.string.condiments), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.marinaraSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tacoSauce), getString(R.string.victoriasMild), getString(R.string.condiments), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.tacoSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.buffaloSauce), getString(R.string.franksWings), getString(R.string.condiments), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.buffaloSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocolateSyrup), getString(R.string.ghirardelliOrHersheys), getString(R.string.condiments), getString(R.string.target), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.chocolateSyrup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.caramelSyrup), getString(R.string.ghirardelliOrHersheys), getString(R.string.condiments), getString(R.string.target), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.caramelSyrup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.mapleSyrup), getString(R.string.pearlMilling), getString(R.string.condiments), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.mapleSyrup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.honey), getString(R.string.localHiveClover), getString(R.string.condiments), getString(R.string.vons), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.honey), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.peanutButter), getString(R.string.skippyCreamy), getString(R.string.condiments), getString(R.string.vons), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.peanutButter), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.soySauce), getString(R.string.kikoman), getString(R.string.condiments), getString(R.string.vons), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.soySauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.woodranchBBQSauce), getString(R.string.pint1), getString(R.string.condiments), getString(R.string.woodranch), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.woodranchBBQSauce), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.condiments), 15);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.condiments), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.condiments), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.condiments), 15);
        getDbCategoryHelper().setCategoryViews(getString(R.string.condiments), 15, 0, 0, 15);

        //------------------------------------Seasonings--------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.saltAndPepper), getString(R.string.na), getString(R.string.seasonings), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.saltAndPepper), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.garlicSalt), getString(R.string.lawrys), getString(R.string.seasonings), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.garlicSalt), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.lawrysSeasoningSalt), getString(R.string.lawrys), getString(R.string.seasonings), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.lawrysSeasoningSalt), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tacoSeasoning), getString(R.string.any), getString(R.string.seasonings), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.tacoSeasoning), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.ranchDipMix), getString(R.string.lauraScudders), getString(R.string.seasonings), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.ranchDipMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vanillaExtract), getString(R.string.signatureSelect), getString(R.string.seasonings), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.vanillaExtract), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cinnamonSugar), getString(R.string.mcCormicks), getString(R.string.seasonings), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.cinnamonSugar), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sprinkles), getString(R.string.types3), getString(R.string.seasonings), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.sprinkles), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.seasonings), 8);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.seasonings), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.seasonings), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.seasonings), 8);
        getDbCategoryHelper().setCategoryViews(getString(R.string.seasonings), 8, 0, 0, 8);

        //---------------------------------Misc/Ingredients----------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.brownSugar), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.brownSugar), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tacoShells), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.tacoShells), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.saltineCrackers), getString(R.string.premiumOriginal), getString(R.string.miscIngredients), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.saltineCrackers), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.semiSweetChocChips), getString(R.string.nestle), getString(R.string.miscIngredients), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.semiSweetChocChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vegetableOil), getString(R.string.crisco), getString(R.string.miscIngredients), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.vegetableOil), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.extraVirginOliveOil), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.extraVirginOliveOil), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.nonStickSpray), getString(R.string.pamOriginal), getString(R.string.miscIngredients), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.nonStickSpray), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.movieTheaterButter), getString(R.string.kernelSeasons), getString(R.string.miscIngredients), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.movieTheaterButter), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.miscIngredients), 8);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.miscIngredients), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.miscIngredients), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.miscIngredients), 8);
        getDbCategoryHelper().setCategoryViews(getString(R.string.miscIngredients), 8, 0, 0, 8);

        //------------------------------------Drinks------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.sodaBottles2L), getString(R.string.pepsiOrCoke), getString(R.string.drinks), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.sodaBottles2L), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sodaCans), getString(R.string.pepsiOrCoke), getString(R.string.drinks), getString(R.string.costco), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.sodaCans), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hotChocolateMix), getString(R.string.swissMissDark), getString(R.string.drinks), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.hotChocolateMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.bottledWater), getString(R.string.refreshe40pack), getString(R.string.drinks), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.bottledWater), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.drinks), 4);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.drinks), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.drinks), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.drinks), 4);
        getDbCategoryHelper().setCategoryViews(getString(R.string.drinks), 4, 0, 0, 4);

        //------------------------------------Snacks------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.beefJerky), getString(R.string.archerTerriyaki), getString(R.string.snacks), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.beefJerky), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.peanuts), getString(R.string.honeyRoasted), getString(R.string.snacks), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.peanuts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.shellPeanuts), getString(R.string.salted), getString(R.string.snacks), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.shellPeanuts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sunflowerSeeds), getString(R.string.salted), getString(R.string.snacks), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.sunflowerSeeds), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vinegarChips), getString(R.string.kettle), getString(R.string.snacks), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.vinegarChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.bbqChips), getString(R.string.kettle), getString(R.string.snacks), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.bbqChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.doritos), getString(R.string.coolRanch), getString(R.string.snacks), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.doritos), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.laysChips), getString(R.string.classic), getString(R.string.snacks), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.laysChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.naanCrisps), getString(R.string.stonefire), getString(R.string.snacks), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.naanCrisps), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.ritzCrackers), getString(R.string.original), getString(R.string.snacks), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.ritzCrackers), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.goldfish), getString(R.string.cheddar), getString(R.string.snacks), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.goldfish), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cheezIts), getString(R.string.original), getString(R.string.snacks), getString(R.string.vons), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.cheezIts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.famousAmosCookies), getString(R.string.resealableBag), getString(R.string.snacks), getString(R.string.vons), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.famousAmosCookies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.darkChocolatePretzels), getString(R.string.flipz), getString(R.string.snacks), getString(R.string.cvs), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.darkChocolatePretzels), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocFudgePudding), getString(R.string.snackPack), getString(R.string.snacks), getString(R.string.staterBros), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.chocFudgePudding), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocFudgePirouette), getString(R.string.pepperidgeFarm), getString(R.string.snacks), getString(R.string.vons), 15);
        getDbStatusHelper().addNewStatus(getString(R.string.chocFudgePirouette), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.muddyBuddies), getString(R.string.brownieSupreme), getString(R.string.snacks), getString(R.string.amazon), 16);
        getDbStatusHelper().addNewStatus(getString(R.string.muddyBuddies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.fortuneCookies), getString(R.string.toDo), getString(R.string.snacks), getString(R.string.amazon), 17);
        getDbStatusHelper().addNewStatus(getString(R.string.fortuneCookies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.communionWafers), getString(R.string.cavanaghAltarBread), getString(R.string.snacks), getString(R.string.amazon), 18);
        getDbStatusHelper().addNewStatus(getString(R.string.communionWafers), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.butteredPopcorn), getString(R.string.popSecretMiniBags), getString(R.string.snacks), getString(R.string.vons), 19);
        getDbStatusHelper().addNewStatus(getString(R.string.butteredPopcorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.caramelPopcorn), getString(R.string.cretors), getString(R.string.snacks), getString(R.string.vons), 20);
        getDbStatusHelper().addNewStatus(getString(R.string.caramelPopcorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocCaramelSwirlPopcorn), getString(R.string.cretors), getString(R.string.snacks), getString(R.string.vons), 21);
        getDbStatusHelper().addNewStatus(getString(R.string.chocCaramelSwirlPopcorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.gingerSnaps), getString(R.string.firstStreet), getString(R.string.snacks), getString(R.string.smartFinal), 22);
        getDbStatusHelper().addNewStatus(getString(R.string.gingerSnaps), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.snacks), 23);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.snacks), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.snacks), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.snacks), 23);
        getDbCategoryHelper().setCategoryViews(getString(R.string.snacks), 23, 0, 0, 23);

        //------------------------------------Desserts----------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocMaltedCrunchIceCream), getString(R.string.thrifty), getString(R.string.desserts), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.chocMaltedCrunchIceCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocTruffleIceCream), getString(R.string.breyers), getString(R.string.desserts), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.chocTruffleIceCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.holdTheCone), getString(R.string.chocolate), getString(R.string.desserts), getString(R.string.traderJoes), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.holdTheCone), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.churros), getString(R.string.tioPepesOrHola), getString(R.string.desserts), getString(R.string.smartFinal), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.churros), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocChipMuffinMix), getString(R.string.bettyCrocker), getString(R.string.desserts), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.chocChipMuffinMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocChipCookieMix), getString(R.string.bettyCrockerGlutenFree), getString(R.string.desserts), getString(R.string.staterBros), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.chocChipCookieMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.gingerbreadCookieMix), getString(R.string.bettyCrocker), getString(R.string.desserts), getString(R.string.amazon), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.gingerbreadCookieMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.oreos), getString(R.string.forCrumbs), getString(R.string.desserts), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.oreos), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.oreoMuffins), getString(R.string.pack12), getString(R.string.desserts), getString(R.string.costco), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.oreoMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.oreoCakesters), getString(R.string.nabisco), getString(R.string.desserts), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.oreoCakesters), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.oreoPieMix), getString(R.string.noBakeDessert), getString(R.string.desserts), getString(R.string.target), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.oreoPieMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocMaltMix), getString(R.string.nestle), getString(R.string.desserts), getString(R.string.staterBros), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.chocMaltMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.doubleChocMuffins), getString(R.string.count4), getString(R.string.desserts), getString(R.string.vons), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.doubleChocMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.pieFilling), getString(R.string.jelloChocFudge), getString(R.string.desserts), getString(R.string.target), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.pieFilling), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.desserts), 14);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.desserts), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.desserts), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.desserts), 14);
        getDbCategoryHelper().setCategoryViews(getString(R.string.desserts), 14, 0, 0, 14);

        //------------------------------------Candy-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.darkChocolateCaramelSquares), getString(R.string.ghiradelli), getString(R.string.candy), getString(R.string.walmart), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.darkChocolateCaramelSquares), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.reesesPBCups), getString(R.string.individuallyWrapped), getString(R.string.candy), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.reesesPBCups), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.candyCorn), getString(R.string.brachs), getString(R.string.candy), getString(R.string.cvs), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.candyCorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hotTamales), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.hotTamales), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.smarties), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.smarties), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.snoCaps), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.snoCaps), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.goodAndPlenty), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.goodAndPlenty), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.miniMAndMs), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.miniMAndMs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.darkChocMandMs), getString(R.string.na), getString(R.string.candy), getString(R.string.target), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.darkChocMandMs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.seaSaltCaramels), getString(R.string.favoriteDay), getString(R.string.candy), getString(R.string.target), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.seaSaltCaramels), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.jellyBeans), getString(R.string.sizzlingCinnamon), getString(R.string.candy), getString(R.string.amazon), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.jellyBeans), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tootsieRolls), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.tootsieRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.funDipSticks), getString(R.string.na), getString(R.string.candy), getString(R.string.smartFinal), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.funDipSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.intenseDarkChocolate), getString(R.string.ghiradelli), getString(R.string.candy), getString(R.string.walmart), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.intenseDarkChocolate), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.orangeTicTacs), getString(R.string.na), getString(R.string.candy), getString(R.string.smartFinal), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.orangeTicTacs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.orangePez), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 15);
        getDbStatusHelper().addNewStatus(getString(R.string.orangePez), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vanillaTaffy), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 16);
        getDbStatusHelper().addNewStatus(getString(R.string.vanillaTaffy), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vanillaTootsieRolls), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 17);
        getDbStatusHelper().addNewStatus(getString(R.string.vanillaTootsieRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sixlets), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 18);
        getDbStatusHelper().addNewStatus(getString(R.string.sixlets), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.redHots), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 19);
        getDbStatusHelper().addNewStatus(getString(R.string.redHots), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.darkChocCaramels), getString(R.string.traderJoes), getString(R.string.candy), getString(R.string.traderJoes), 20);
        getDbStatusHelper().addNewStatus(getString(R.string.darkChocCaramels), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.darkChocPeanutButterCups), getString(R.string.traderJoes), getString(R.string.candy), getString(R.string.traderJoes), 21);
        getDbStatusHelper().addNewStatus(getString(R.string.darkChocPeanutButterCups), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.candy), 22);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.candy), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.candy), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.candy), 22);
        getDbCategoryHelper().setCategoryViews(getString(R.string.candy), 22, 0, 0, 22);

        //------------------------------------Pet Supplies-------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.catFoodWet), getString(R.string.fancyFeast), getString(R.string.petSupplies), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.catFoodWet), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.catFoodDry), getString(R.string.purinaProPlan), getString(R.string.petSupplies), getString(R.string.petSuppliesPlus), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.catFoodDry), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.delectables), getString(R.string.squeezeUp20pack), getString(R.string.petSupplies), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.delectables), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.catTreats), getString(R.string.temptations), getString(R.string.petSupplies), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.catTreats), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.kittyLitter), getString(R.string.scoopAwayComplete), getString(R.string.petSupplies), getString(R.string.costco), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.kittyLitter), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.dogFoodDry), getString(R.string.canidaeAllLifeStages), getString(R.string.petSupplies), getString(R.string.yorbaLindaFeedStore), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.dogFoodDry), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chickenBroth), getString(R.string.kirklandOrganic), getString(R.string.petSupplies), getString(R.string.costco), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.chickenBroth), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.mashedPotatoes), getString(R.string.mainStBistro), getString(R.string.petSupplies), getString(R.string.costco), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.mashedPotatoes), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.freshpet), getString(R.string.chickenRecipe), getString(R.string.petSupplies), getString(R.string.costco), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.freshpet), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.purePumpkin), getString(R.string.libbys), getString(R.string.petSupplies), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.purePumpkin), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.poopBags), getString(R.string.amazonBasics), getString(R.string.petSupplies), getString(R.string.amazon), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.poopBags), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.nitrileGloves), getString(R.string.gmg100Pack), getString(R.string.petSupplies), getString(R.string.amazon), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.nitrileGloves), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.petSupplies), 12);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.petSupplies), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.petSupplies), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.petSupplies), 12);
        getDbCategoryHelper().setCategoryViews(getString(R.string.petSupplies), 12, 0, 0, 12);

        //------------------------------------Toiletries--------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.handSoap), getString(R.string.lavenderAndChamomile), getString(R.string.toiletries), getString(R.string.dollarTree), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.handSoap), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.bodyWash), getString(R.string.suaveMandarin), getString(R.string.toiletries), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.bodyWash), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.shampoo), getString(R.string.suave2in1), getString(R.string.toiletries), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.shampoo), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.barSoap), getString(R.string.zumBarSeaSalt), getString(R.string.toiletries), getString(R.string.sprouts), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.barSoap), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.deodorant), getString(R.string.oldSpice), getString(R.string.toiletries), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.deodorant), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.toothpaste), getString(R.string.ultrabrite), getString(R.string.toiletries), getString(R.string.amazon), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.toothpaste), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.floss), getString(R.string.reachMintWaxed), getString(R.string.toiletries), getString(R.string.amazon), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.floss), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.shavingCream), getString(R.string.sandalwood), getString(R.string.toiletries), getString(R.string.amazon), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.shavingCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.shavingRazors), getString(R.string.gilletteProGlide), getString(R.string.toiletries), getString(R.string.amazon), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.shavingRazors), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.mouthwash), getString(R.string.crestWhitening), getString(R.string.toiletries), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.mouthwash), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cottonSwabs), getString(R.string.qTips), getString(R.string.toiletries), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.cottonSwabs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.toothbrushHeads), getString(R.string.radiusSoft), getString(R.string.toiletries), getString(R.string.sprouts), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.toothbrushHeads), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sunscreen), getString(R.string.hawaiianTropicSheer50spf), getString(R.string.toiletries), getString(R.string.amazon), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.sunscreen), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.moisturizingLotion), getString(R.string.cvsHealthHyaluronicAcid), getString(R.string.toiletries), getString(R.string.cvs), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.moisturizingLotion), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.faceSunscreen), getString(R.string.aveenoProtectHydrate), getString(R.string.toiletries), getString(R.string.cvs), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.faceSunscreen), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.toiletries), 15);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.toiletries), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.toiletries), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.toiletries), 15);
        getDbCategoryHelper().setCategoryViews(getString(R.string.toiletries), 15, 0, 0, 15);

        //------------------------------------Household-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.febreezeAirSpray), getString(R.string.heavyDuty), getString(R.string.household), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.febreezeAirSpray), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.allPurposeCleaner), getString(R.string.meyersLavender), getString(R.string.household), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.allPurposeCleaner), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.petStainCleaner), getString(R.string.roccoAndRoxie), getString(R.string.household), getString(R.string.amazon), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.petStainCleaner), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.laundryDetergent), getString(R.string.woolite), getString(R.string.household), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.laundryDetergent), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.laundrySanitizer), getString(R.string.lysol), getString(R.string.household), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.laundrySanitizer), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.dryerSheets), getString(R.string.simplyDoneFreshLinen), getString(R.string.household), getString(R.string.staterBros), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.dryerSheets), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.littleTreesAirFresheners), getString(R.string.trueNorth), getString(R.string.household), getString(R.string.amazon), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.littleTreesAirFresheners), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.aluminumFoil), getString(R.string.reynoldsWrap), getString(R.string.household), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.aluminumFoil), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.zipLockBagsSmall), getString(R.string.sandwich), getString(R.string.household), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.zipLockBagsSmall), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.zipLockBagsLarge), getString(R.string.freezerGallon), getString(R.string.household), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.zipLockBagsLarge), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.saranWrap), getString(R.string.plasticWrap), getString(R.string.household), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.saranWrap), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.rubbingAlcohol), getString(R.string.isopropyl), getString(R.string.household), getString(R.string.cvs), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.rubbingAlcohol), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hydrogenPeroxide), getString(R.string.na), getString(R.string.household), getString(R.string.cvs), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.hydrogenPeroxide), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.nightLightBulbs), getString(R.string.c7E12), getString(R.string.household), getString(R.string.amazon), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.nightLightBulbs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.scrubSponges), getString(R.string.nonScratch), getString(R.string.household), getString(R.string.vons), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.scrubSponges), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.dishwashingBrush), getString(R.string.greatValue), getString(R.string.household), getString(R.string.walmart), 15);
        getDbStatusHelper().addNewStatus(getString(R.string.dishwashingBrush), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.smallTrashBags), getString(R.string.gallon13), getString(R.string.household), getString(R.string.walmart), 16);
        getDbStatusHelper().addNewStatus(getString(R.string.smallTrashBags), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.largeTrashBags), getString(R.string.gallon33), getString(R.string.household), getString(R.string.walmart), 17);
        getDbStatusHelper().addNewStatus(getString(R.string.largeTrashBags), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.compactorBags), getString(R.string.gallon18), getString(R.string.household), getString(R.string.walmart), 18);
        getDbStatusHelper().addNewStatus(getString(R.string.compactorBags), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.dawnPowerwash), getString(R.string.dishCleaner), getString(R.string.household), getString(R.string.vons), 19);
        getDbStatusHelper().addNewStatus(getString(R.string.dawnPowerwash), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.dishSoap), getString(R.string.dawnPlatinum), getString(R.string.household), getString(R.string.vons), 20);
        getDbStatusHelper().addNewStatus(getString(R.string.dishSoap), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.paperPlates), getString(R.string.toDo), getString(R.string.household), getString(R.string.samsClub), 21);
        getDbStatusHelper().addNewStatus(getString(R.string.paperPlates), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.paperTowels), getString(R.string.kirklandPremium), getString(R.string.household), getString(R.string.costco), 22);
        getDbStatusHelper().addNewStatus(getString(R.string.paperTowels), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.toiletPaper), getString(R.string.kirklandUltraSoft), getString(R.string.household), getString(R.string.costco), 23);
        getDbStatusHelper().addNewStatus(getString(R.string.toiletPaper), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.multipurposePaper), getString(R.string.truRed), getString(R.string.household), getString(R.string.staples), 24);
        getDbStatusHelper().addNewStatus(getString(R.string.multipurposePaper), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.packagingTape), getString(R.string.scotchHeavyDuty), getString(R.string.household), getString(R.string.cvs), 25);
        getDbStatusHelper().addNewStatus(getString(R.string.packagingTape), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.household), 26);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.household), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.household), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.household), 26);
        getDbCategoryHelper().setCategoryViews(getString(R.string.household), 26, 0, 0, 26);

        //------------------------------------Supplements-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.tripleOmega), getString(R.string.natureMade), getString(R.string.supplements), getString(R.string.amazon), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.tripleOmega), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.multivitamin), getString(R.string.oneADayMens), getString(R.string.supplements), getString(R.string.amazon), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.multivitamin), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vitaminC), getString(R.string.amazonElements1000mg), getString(R.string.supplements), getString(R.string.amazon), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.vitaminC), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.magnesium), getString(R.string.natureMade400mg), getString(R.string.supplements), getString(R.string.amazon), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.magnesium), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.zinc), getString(R.string.sandhuHerbals50mg), getString(R.string.supplements), getString(R.string.amazon), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.zinc), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.calcium), getString(R.string.naturesTruth1200mg), getString(R.string.supplements), getString(R.string.amazon), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.calcium), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.biotin), getString(R.string.natrol10000mcg), getString(R.string.supplements), getString(R.string.amazon), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.biotin), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vitaminD3), getString(R.string.natureMade5000IU), getString(R.string.supplements), getString(R.string.amazon), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.vitaminD3), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hyaluronicAcid), getString(R.string.horbaach1000mg), getString(R.string.supplements), getString(R.string.amazon), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.hyaluronicAcid), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.supplements), 9);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.supplements), 0);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.supplements), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.supplements), 9);
        getDbCategoryHelper().setCategoryViews(getString(R.string.supplements), 9, 0, 0, 9);

        //------------------------------------------------------------------------------------------

        // total category items = 222

    }

//------------------------------------------------------------------------------------------------//
//----------------------------------------Sort By Store-------------------------------------------//
//------------------------------------------------------------------------------------------------//

    void loadStoreData1() {

        //------------------------------------Vons--------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.sausageBiscuits), getString(R.string.jimmyDeanFrozen), getString(R.string.meals), getString(R.string.vons), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.sausageBiscuits), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hamburgerHelper), getString(R.string.cheeseburgerMacaroni), getString(R.string.meals), getString(R.string.vons), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.hamburgerHelper), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.buffaloChickenBites), getString(R.string.tGIForFranks), getString(R.string.meals), getString(R.string.vons), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.buffaloChickenBites), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.terriyakiChickenBites), getString(R.string.innovAsian), getString(R.string.meals), getString(R.string.vons), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.terriyakiChickenBites), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tgifCheeseSticks), getString(R.string.tgifSmall10pc), getString(R.string.meals), getString(R.string.vons), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.tgifCheeseSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.mozarellaCheeseSticks), getString(R.string.farmRich), getString(R.string.meals), getString(R.string.vons), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.mozarellaCheeseSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.frozenPizza), getString(R.string.thinPepperoni), getString(R.string.meals), getString(R.string.vons), 6);
        //getDbStatusHelper().addNewStatus(getString(R.string.frozenPizza), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cornDogs), getString(R.string.fosterFarms), getString(R.string.meals), getString(R.string.vons), 7);
        //getDbStatusHelper().addNewStatus(getString(R.string.cornDogs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hotDogs), getString(R.string.bunSize), getString(R.string.meals), getString(R.string.vons), 8);
        //getDbStatusHelper().addNewStatus(getString(R.string.hotDogs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hotDogBuns), getString(R.string.pack8), getString(R.string.meals), getString(R.string.vons), 9);
        //getDbStatusHelper().addNewStatus(getString(R.string.hotDogBuns), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hamburgerPatties), getString(R.string.toDo), getString(R.string.meals), getString(R.string.vons), 10);
        //getDbStatusHelper().addNewStatus(getString(R.string.hamburgerPatties), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hamburgerBuns), getString(R.string.pack8), getString(R.string.meals), getString(R.string.vons), 11);
        //getDbStatusHelper().addNewStatus(getString(R.string.hamburgerBuns), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.pastaRoni1), getString(R.string.angelHairPasta), getString(R.string.meals), getString(R.string.vons), 12);
        //getDbStatusHelper().addNewStatus(getString(R.string.pastaRoni1), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.pastaRoni2), getString(R.string.fettuccineAlfredo), getString(R.string.meals), getString(R.string.vons), 13);
        //getDbStatusHelper().addNewStatus(getString(R.string.pastaRoni2), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.macAndCheese), getString(R.string.annies), getString(R.string.meals), getString(R.string.vons), 14);
        //getDbStatusHelper().addNewStatus(getString(R.string.macAndCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.gnocci), getString(R.string.signatureSelect), getString(R.string.meals), getString(R.string.vons), 15);
        //getDbStatusHelper().addNewStatus(getString(R.string.gnocci), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.spaghettiOs), getString(R.string.wMeatballs), getString(R.string.soups), getString(R.string.vons), 16);
        //getDbStatusHelper().addNewStatus(getString(R.string.spaghettiOs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chickenNoodleSoup), getString(R.string.campbells), getString(R.string.soups), getString(R.string.vons), 17);
        //getDbStatusHelper().addNewStatus(getString(R.string.chickenNoodleSoup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.minestroneSoup), getString(R.string.amys), getString(R.string.soups), getString(R.string.vons), 18);
        //getDbStatusHelper().addNewStatus(getString(R.string.minestroneSoup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vegetableBarleySoup), getString(R.string.amys), getString(R.string.soups), getString(R.string.vons), 19);
        //getDbStatusHelper().addNewStatus(getString(R.string.vegetableBarleySoup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cupOfNoodles), getString(R.string.nissin), getString(R.string.soups), getString(R.string.vons), 20);
        //getDbStatusHelper().addNewStatus(getString(R.string.cupOfNoodles), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.frozenFrenchFries), getString(R.string.oreIda), getString(R.string.sides), getString(R.string.vons), 21);
        //getDbStatusHelper().addNewStatus(getString(R.string.frozenFrenchFries), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.texasCheesyBread), getString(R.string.newYorkBakery), getString(R.string.sides), getString(R.string.vons), 22);
        //getDbStatusHelper().addNewStatus(getString(R.string.texasCheesyBread), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chickenRice), getString(R.string.knorr), getString(R.string.sides), getString(R.string.vons), 23);
        //getDbStatusHelper().addNewStatus(getString(R.string.chickenRice), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cannedCorn), getString(R.string.delMonte), getString(R.string.sides), getString(R.string.vons), 24);
        //getDbStatusHelper().addNewStatus(getString(R.string.cannedCorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.steak), getString(R.string.usda), getString(R.string.meat), getString(R.string.vons), 25);
        //getDbStatusHelper().addNewStatus(getString(R.string.steak), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.groundBeef), getString(R.string.pound1), getString(R.string.meat), getString(R.string.vons), 26);
        //getDbStatusHelper().addNewStatus(getString(R.string.groundBeef), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.frozenMeatballs), getString(R.string.rosinaHomestyle), getString(R.string.meat), getString(R.string.vons), 27);
        //getDbStatusHelper().addNewStatus(getString(R.string.frozenMeatballs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chickenBreast), getString(R.string.na), getString(R.string.meat), getString(R.string.vons), 28);
        //getDbStatusHelper().addNewStatus(getString(R.string.chickenBreast), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.slicedTurkey), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 29);
        //getDbStatusHelper().addNewStatus(getString(R.string.slicedTurkey), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.slicedHam), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 30);
        //getDbStatusHelper().addNewStatus(getString(R.string.slicedHam), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hamSteak), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 31);
        //getDbStatusHelper().addNewStatus(getString(R.string.hamSteak), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.thinSpaghetti), getString(R.string.barillaWholeGrain), getString(R.string.breadGrainsCereal), getString(R.string.vons), 32);
        //getDbStatusHelper().addNewStatus(getString(R.string.thinSpaghetti), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.spiralPasta), getString(R.string.barillaRotini), getString(R.string.breadGrainsCereal), getString(R.string.vons), 33);
        //getDbStatusHelper().addNewStatus(getString(R.string.spiralPasta), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.wheatBread), getString(R.string.naturesOwn), getString(R.string.breadGrainsCereal), getString(R.string.vons), 34);
        //getDbStatusHelper().addNewStatus(getString(R.string.wheatBread), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.baguette), getString(R.string.frenchOrSourdough), getString(R.string.breadGrainsCereal), getString(R.string.vons), 35);
        //getDbStatusHelper().addNewStatus(getString(R.string.baguette), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sourdoughBread), getString(R.string.sanLuis), getString(R.string.breadGrainsCereal), getString(R.string.vons), 36);
        //getDbStatusHelper().addNewStatus(getString(R.string.sourdoughBread), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hardRolls), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 37);
        //getDbStatusHelper().addNewStatus(getString(R.string.hardRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.thomasMuffins), getString(R.string.original), getString(R.string.breadGrainsCereal), getString(R.string.vons), 38);
        //getDbStatusHelper().addNewStatus(getString(R.string.thomasMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.reesesPuffsCereal), getString(R.string.generalMills), getString(R.string.breadGrainsCereal), getString(R.string.vons), 39);
        //getDbStatusHelper().addNewStatus(getString(R.string.reesesPuffsCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cookieCrispCereal), getString(R.string.generalMills), getString(R.string.breadGrainsCereal), getString(R.string.vons), 40);
        //getDbStatusHelper().addNewStatus(getString(R.string.cookieCrispCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.frostedMiniWheatCereal), getString(R.string.kelloggs), getString(R.string.breadGrainsCereal), getString(R.string.vons), 41);
        //getDbStatusHelper().addNewStatus(getString(R.string.frostedMiniWheatCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.honeySmacksCereal), getString(R.string.kelloggs), getString(R.string.breadGrainsCereal), getString(R.string.vons), 42);
        //getDbStatusHelper().addNewStatus(getString(R.string.honeySmacksCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.eggoWaffles), getString(R.string.homestyle), getString(R.string.breadGrainsCereal), getString(R.string.vons), 43);
        //getDbStatusHelper().addNewStatus(getString(R.string.eggoWaffles), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.milk), getString(R.string.vitaminD), getString(R.string.eggsDairy), getString(R.string.vons), 44);
        //getDbStatusHelper().addNewStatus(getString(R.string.milk), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.eggs), getString(R.string.gradeAAxLarge), getString(R.string.eggsDairy), getString(R.string.vons), 45);
        //getDbStatusHelper().addNewStatus(getString(R.string.eggs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.honeyYogurt), getString(R.string.greekGods), getString(R.string.eggsDairy), getString(R.string.vons), 46);
        //getDbStatusHelper().addNewStatus(getString(R.string.honeyYogurt), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.saltedButter), getString(R.string.challenge), getString(R.string.eggsDairy), getString(R.string.vons), 47);
        //getDbStatusHelper().addNewStatus(getString(R.string.saltedButter), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.shreddedCheese), getString(R.string.mexicanBlend), getString(R.string.eggsDairy), getString(R.string.vons), 48);
        //getDbStatusHelper().addNewStatus(getString(R.string.shreddedCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.stringCheese), getString(R.string.mozarella), getString(R.string.eggsDairy), getString(R.string.vons), 49);
        //getDbStatusHelper().addNewStatus(getString(R.string.stringCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.bdCheese), getString(R.string.blackDiamond), getString(R.string.eggsDairy), getString(R.string.vons), 50);
        //getDbStatusHelper().addNewStatus(getString(R.string.bdCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.nonStickSpray), getString(R.string.pamOriginal), getString(R.string.miscIngredients), getString(R.string.vons), 51);
        //getDbStatusHelper().addNewStatus(getString(R.string.nonStickSpray), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.parmesanCheese), getString(R.string.kraft), getString(R.string.condiments), getString(R.string.vons), 52);
        //getDbStatusHelper().addNewStatus(getString(R.string.parmesanCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.a1sauce), getString(R.string.original), getString(R.string.condiments), getString(R.string.vons), 53);
        //getDbStatusHelper().addNewStatus(getString(R.string.a1sauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.ketchup), getString(R.string.heinz), getString(R.string.condiments), getString(R.string.vons), 54);
        //getDbStatusHelper().addNewStatus(getString(R.string.ketchup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.mustard), getString(R.string.heinz), getString(R.string.condiments), getString(R.string.vons), 55);
        //getDbStatusHelper().addNewStatus(getString(R.string.mustard), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.pastaSauce), getString(R.string.raguMeat), getString(R.string.condiments), getString(R.string.vons), 56);
        //getDbStatusHelper().addNewStatus(getString(R.string.pastaSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.marinaraSauce), getString(R.string.signatureTraditional), getString(R.string.condiments), getString(R.string.vons), 57);
        //getDbStatusHelper().addNewStatus(getString(R.string.marinaraSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.mapleSyrup), getString(R.string.pearlMilling), getString(R.string.condiments), getString(R.string.vons), 58);
        //getDbStatusHelper().addNewStatus(getString(R.string.mapleSyrup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.honey), getString(R.string.localHiveClover), getString(R.string.condiments), getString(R.string.vons), 59);
        //getDbStatusHelper().addNewStatus(getString(R.string.honey), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.peanutButter), getString(R.string.skippyCreamy), getString(R.string.condiments), getString(R.string.vons), 60);
        //getDbStatusHelper().addNewStatus(getString(R.string.peanutButter), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.soySauce), getString(R.string.kikoman), getString(R.string.condiments), getString(R.string.vons), 61);
        //getDbStatusHelper().addNewStatus(getString(R.string.soySauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.brownSugar), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 62);
        //getDbStatusHelper().addNewStatus(getString(R.string.brownSugar), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.saltAndPepper), getString(R.string.na), getString(R.string.seasonings), getString(R.string.vons), 63);
        //getDbStatusHelper().addNewStatus(getString(R.string.saltAndPepper), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.garlicSalt), getString(R.string.lawrys), getString(R.string.seasonings), getString(R.string.vons), 64);
        //getDbStatusHelper().addNewStatus(getString(R.string.garlicSalt), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.lawrysSeasoningSalt), getString(R.string.lawrys), getString(R.string.seasonings), getString(R.string.vons), 65);
        //getDbStatusHelper().addNewStatus(getString(R.string.lawrysSeasoningSalt), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.ranchDipMix), getString(R.string.lauraScudders), getString(R.string.seasonings), getString(R.string.vons), 66);
        //getDbStatusHelper().addNewStatus(getString(R.string.ranchDipMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vanillaExtract), getString(R.string.signatureSelect), getString(R.string.seasonings), getString(R.string.vons), 67);
        //getDbStatusHelper().addNewStatus(getString(R.string.vanillaExtract), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cinnamonSugar), getString(R.string.mcCormicks), getString(R.string.seasonings), getString(R.string.vons), 68);
        //getDbStatusHelper().addNewStatus(getString(R.string.cinnamonSugar), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sprinkles), getString(R.string.types3), getString(R.string.seasonings), getString(R.string.vons), 69);
        //getDbStatusHelper().addNewStatus(getString(R.string.sprinkles), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sodaBottles2L), getString(R.string.pepsiOrCoke), getString(R.string.drinks), getString(R.string.vons), 70);
        //getDbStatusHelper().addNewStatus(getString(R.string.sodaBottles2L), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hotChocolateMix), getString(R.string.swissMissDark), getString(R.string.drinks), getString(R.string.vons), 71);
        //getDbStatusHelper().addNewStatus(getString(R.string.hotChocolateMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.bottledWater), getString(R.string.refreshe40pack), getString(R.string.drinks), getString(R.string.vons), 72);
        //getDbStatusHelper().addNewStatus(getString(R.string.bottledWater), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.beefJerky), getString(R.string.archerTerriyaki), getString(R.string.snacks), getString(R.string.vons), 73);
        //getDbStatusHelper().addNewStatus(getString(R.string.beefJerky), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.peanuts), getString(R.string.honeyRoasted), getString(R.string.snacks), getString(R.string.vons), 74);
        //getDbStatusHelper().addNewStatus(getString(R.string.peanuts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.shellPeanuts), getString(R.string.salted), getString(R.string.snacks), getString(R.string.vons), 75);
        //getDbStatusHelper().addNewStatus(getString(R.string.shellPeanuts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sunflowerSeeds), getString(R.string.salted), getString(R.string.snacks), getString(R.string.vons), 76);
        //getDbStatusHelper().addNewStatus(getString(R.string.sunflowerSeeds), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vinegarChips), getString(R.string.kettle), getString(R.string.snacks), getString(R.string.vons), 77);
        //getDbStatusHelper().addNewStatus(getString(R.string.vinegarChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.bbqChips), getString(R.string.kettle), getString(R.string.snacks), getString(R.string.vons), 78);
        //getDbStatusHelper().addNewStatus(getString(R.string.bbqChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.doritos), getString(R.string.coolRanch), getString(R.string.snacks), getString(R.string.vons), 79);
        //getDbStatusHelper().addNewStatus(getString(R.string.doritos), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.laysChips), getString(R.string.classic), getString(R.string.snacks), getString(R.string.vons), 80);
        //getDbStatusHelper().addNewStatus(getString(R.string.laysChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.naanCrisps), getString(R.string.stonefire), getString(R.string.snacks), getString(R.string.vons), 81);
        //getDbStatusHelper().addNewStatus(getString(R.string.naanCrisps), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.oreoCakesters), getString(R.string.nabisco), getString(R.string.snacks), getString(R.string.vons), 82);
        //getDbStatusHelper().addNewStatus(getString(R.string.oreoCakesters), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.goldfish), getString(R.string.cheddar), getString(R.string.snacks), getString(R.string.vons), 83);
        //getDbStatusHelper().addNewStatus(getString(R.string.goldfish), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cheezIts), getString(R.string.original), getString(R.string.snacks), getString(R.string.vons), 84);
        //getDbStatusHelper().addNewStatus(getString(R.string.cheezIts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.famousAmosCookies), getString(R.string.resealableBag), getString(R.string.snacks), getString(R.string.vons), 85);
        //getDbStatusHelper().addNewStatus(getString(R.string.famousAmosCookies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocFudgePirouette), getString(R.string.pepperidgeFarm), getString(R.string.snacks), getString(R.string.vons), 86);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocFudgePirouette), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocChipMuffinMix), getString(R.string.bettyCrocker), getString(R.string.desserts), getString(R.string.vons), 87);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocChipMuffinMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.oreos), getString(R.string.forCrumbs), getString(R.string.desserts), getString(R.string.vons), 88);
        //getDbStatusHelper().addNewStatus(getString(R.string.oreos), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocMaltedCrunchIceCream), getString(R.string.thrifty), getString(R.string.desserts), getString(R.string.vons), 89);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocMaltedCrunchIceCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocTruffleIceCream), getString(R.string.breyers), getString(R.string.desserts), getString(R.string.vons), 90);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocTruffleIceCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.reesesPBCups), getString(R.string.individuallyWrapped), getString(R.string.candy), getString(R.string.vons), 91);
        //getDbStatusHelper().addNewStatus(getString(R.string.reesesPBCups), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hotTamales), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 92);
        //getDbStatusHelper().addNewStatus(getString(R.string.hotTamales), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.miniMAndMs), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 93);
        //getDbStatusHelper().addNewStatus(getString(R.string.miniMAndMs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tootsieRolls), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 94);
        //getDbStatusHelper().addNewStatus(getString(R.string.tootsieRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.catFoodWet), getString(R.string.fancyFeast), getString(R.string.petSupplies), getString(R.string.vons), 95);
        //getDbStatusHelper().addNewStatus(getString(R.string.catFoodWet), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.purePumpkin), getString(R.string.libbys), getString(R.string.petSupplies), getString(R.string.vons), 96);
        //getDbStatusHelper().addNewStatus(getString(R.string.purePumpkin), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.delectables), getString(R.string.squeezeUp20pack), getString(R.string.petSupplies), getString(R.string.vons), 97);
        //getDbStatusHelper().addNewStatus(getString(R.string.delectables), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.bodyWash), getString(R.string.suaveMandarin), getString(R.string.toiletries), getString(R.string.vons), 98);
        //getDbStatusHelper().addNewStatus(getString(R.string.bodyWash), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.shampoo), getString(R.string.suave2in1), getString(R.string.toiletries), getString(R.string.vons), 99);
        //getDbStatusHelper().addNewStatus(getString(R.string.shampoo), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.deodorant), getString(R.string.oldSpice), getString(R.string.toiletries), getString(R.string.vons), 100);
        //getDbStatusHelper().addNewStatus(getString(R.string.deodorant), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.mouthwash), getString(R.string.crestWhitening), getString(R.string.toiletries), getString(R.string.vons), 101);
        //getDbStatusHelper().addNewStatus(getString(R.string.mouthwash), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cottonSwabs), getString(R.string.qTips), getString(R.string.toiletries), getString(R.string.vons), 102);
        //getDbStatusHelper().addNewStatus(getString(R.string.cottonSwabs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.febreezeAirSpray), getString(R.string.heavyDuty), getString(R.string.household), getString(R.string.vons), 103);
        //getDbStatusHelper().addNewStatus(getString(R.string.febreezeAirSpray), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.allPurposeCleaner), getString(R.string.meyersLavender), getString(R.string.household), getString(R.string.vons), 104);
        //getDbStatusHelper().addNewStatus(getString(R.string.allPurposeCleaner), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.laundryDetergent), getString(R.string.woolite), getString(R.string.household), getString(R.string.vons), 105);
        //getDbStatusHelper().addNewStatus(getString(R.string.laundryDetergent), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.laundrySanitizer), getString(R.string.lysol), getString(R.string.household), getString(R.string.vons), 106);
        //getDbStatusHelper().addNewStatus(getString(R.string.laundrySanitizer), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.aluminumFoil), getString(R.string.reynoldsWrap), getString(R.string.household), getString(R.string.vons), 107);
        //getDbStatusHelper().addNewStatus(getString(R.string.aluminumFoil), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.zipLockBagsSmall), getString(R.string.sandwich), getString(R.string.household), getString(R.string.vons), 108);
        //getDbStatusHelper().addNewStatus(getString(R.string.zipLockBagsSmall), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.zipLockBagsLarge), getString(R.string.freezerGallon), getString(R.string.household), getString(R.string.vons), 109);
        //getDbStatusHelper().addNewStatus(getString(R.string.zipLockBagsLarge), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.saranWrap), getString(R.string.plasticWrap), getString(R.string.household), getString(R.string.vons), 110);
        //getDbStatusHelper().addNewStatus(getString(R.string.saranWrap), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.scrubSponges), getString(R.string.nonScratch), getString(R.string.household), getString(R.string.vons), 111);
        //getDbStatusHelper().addNewStatus(getString(R.string.scrubSponges), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.dawnPowerwash), getString(R.string.dishCleaner), getString(R.string.household), getString(R.string.vons), 112);
        //getDbStatusHelper().addNewStatus(getString(R.string.dawnPowerwash), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.dishSoap), getString(R.string.dawnPlatinum), getString(R.string.household), getString(R.string.vons), 113);
        //getDbStatusHelper().addNewStatus(getString(R.string.dishSoap), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.smallFlourTortillas), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 114);
        //getDbStatusHelper().addNewStatus(getString(R.string.smallFlourTortillas), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.largeFlourTortillas), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 115);
        //getDbStatusHelper().addNewStatus(getString(R.string.largeFlourTortillas), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sourCream), getString(R.string.toDo), getString(R.string.eggsDairy), getString(R.string.vons), 116);
        //getDbStatusHelper().addNewStatus(getString(R.string.sourCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.buffaloSauce), getString(R.string.franksWings), getString(R.string.condiments), getString(R.string.vons), 117);
        //getDbStatusHelper().addNewStatus(getString(R.string.buffaloSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tacoSauce), getString(R.string.victoriasMild), getString(R.string.condiments), getString(R.string.vons), 118);
        //getDbStatusHelper().addNewStatus(getString(R.string.tacoSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tacoSeasoning), getString(R.string.any), getString(R.string.seasonings), getString(R.string.vons), 119);
        //getDbStatusHelper().addNewStatus(getString(R.string.tacoSeasoning), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tacoShells), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 120);
        //getDbStatusHelper().addNewStatus(getString(R.string.tacoShells), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tortellini), getString(R.string.barilla3Cheese), getString(R.string.meals), getString(R.string.vons), 121);
        //getDbStatusHelper().addNewStatus(getString(R.string.tortellini), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.garlicBreadsticks), getString(R.string.newYorkBakery), getString(R.string.sides), getString(R.string.vons), 122);
        //getDbStatusHelper().addNewStatus(getString(R.string.garlicBreadsticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.goodAndPlenty), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 123);
        //getDbStatusHelper().addNewStatus(getString(R.string.goodAndPlenty), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.slicedCheese), getString(R.string.kraftSingles), getString(R.string.eggsDairy), getString(R.string.vons), 124);
        //getDbStatusHelper().addNewStatus(getString(R.string.slicedCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.catTreats), getString(R.string.temptations), getString(R.string.petSupplies), getString(R.string.vons), 125);
        //getDbStatusHelper().addNewStatus(getString(R.string.catTreats), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.saltineCrackers), getString(R.string.premiumOriginal), getString(R.string.miscIngredients), getString(R.string.vons), 126);
        //getDbStatusHelper().addNewStatus(getString(R.string.saltineCrackers), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.semiSweetChocChips), getString(R.string.nestle), getString(R.string.miscIngredients), getString(R.string.vons), 127);
        //getDbStatusHelper().addNewStatus(getString(R.string.semiSweetChocChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vegetableOil), getString(R.string.crisco), getString(R.string.miscIngredients), getString(R.string.vons), 128);
        //getDbStatusHelper().addNewStatus(getString(R.string.vegetableOil), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.extraVirginOliveOil), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 129);
        //getDbStatusHelper().addNewStatus(getString(R.string.extraVirginOliveOil), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.butteredPopcorn), getString(R.string.popSecretMiniBags), getString(R.string.snacks), getString(R.string.vons), 130);
        //getDbStatusHelper().addNewStatus(getString(R.string.butteredPopcorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.caramelPopcorn), getString(R.string.cretors), getString(R.string.snacks), getString(R.string.vons), 131);
        //getDbStatusHelper().addNewStatus(getString(R.string.caramelPopcorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocCaramelSwirlPopcorn), getString(R.string.cretors), getString(R.string.snacks), getString(R.string.vons), 132);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocCaramelSwirlPopcorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.ritzCrackers), getString(R.string.original), getString(R.string.snacks), getString(R.string.vons), 133);
        //getDbStatusHelper().addNewStatus(getString(R.string.ritzCrackers), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.halfAndHalf), getString(R.string.lucerne), getString(R.string.eggsDairy), getString(R.string.vons), 134);
        //getDbStatusHelper().addNewStatus(getString(R.string.halfAndHalf), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.movieTheaterButter), getString(R.string.kernelSeasons), getString(R.string.miscIngredients), getString(R.string.vons), 135);
        //getDbStatusHelper().addNewStatus(getString(R.string.movieTheaterButter), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.crescentRolls), getString(R.string.signatureSelect), getString(R.string.sides), getString(R.string.vons), 136);
        //getDbStatusHelper().addNewStatus(getString(R.string.crescentRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.beefLitlSmokies), getString(R.string.hillshireFarm), getString(R.string.meat), getString(R.string.vons), 137);
        //getDbStatusHelper().addNewStatus(getString(R.string.beefLitlSmokies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.doubleChocMuffins), getString(R.string.count4), getString(R.string.desserts), getString(R.string.vons), 138);
        //getDbStatusHelper().addNewStatus(getString(R.string.doubleChocMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.vons), 139);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.vons), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.vons), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.vons), 139);
        getDbStoreHelper().setStoreViews(getString(R.string.vons), 139, 0, 0, 139);

        //------------------------------------Smart & Final-----------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.churros), getString(R.string.tioPepesOrHola), getString(R.string.desserts), getString(R.string.smartFinal), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.churros), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.pepperoniSlices), getString(R.string.hormel300Slices), getString(R.string.meat), getString(R.string.smartFinal), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.pepperoniSlices), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.funDipSticks), getString(R.string.na), getString(R.string.candy), getString(R.string.smartFinal), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.funDipSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.orangeTicTacs), getString(R.string.na), getString(R.string.candy), getString(R.string.smartFinal), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.orangeTicTacs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.gingerSnaps), getString(R.string.firstStreet), getString(R.string.snacks), getString(R.string.smartFinal), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.gingerSnaps), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.smartFinal), 5);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.smartFinal), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.smartFinal), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.smartFinal), 5);
        getDbStoreHelper().setStoreViews(getString(R.string.smartFinal), 5, 0, 0, 5);

        //------------------------------------Costco------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.sodaCans), getString(R.string.pepsiOrCoke), getString(R.string.drinks), getString(R.string.costco), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.sodaCans), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.kittyLitter), getString(R.string.scoopAwayComplete), getString(R.string.petSupplies), getString(R.string.costco), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.kittyLitter), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chickenBroth), getString(R.string.kirklandOrganic), getString(R.string.petSupplies), getString(R.string.costco), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.chickenBroth), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.mashedPotatoes), getString(R.string.mainStBistro), getString(R.string.petSupplies), getString(R.string.costco), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.mashedPotatoes), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.freshpet), getString(R.string.chickenRecipe), getString(R.string.petSupplies), getString(R.string.costco), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.freshpet), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.oreoMuffins), getString(R.string.pack12), getString(R.string.desserts), getString(R.string.costco), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.oreoMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.crispyBuffaloWings), getString(R.string.fosterFarms), getString(R.string.meals), getString(R.string.costco), 6);
        //getDbStatusHelper().addNewStatus(getString(R.string.crispyBuffaloWings), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.paperTowels), getString(R.string.kirklandPremium), getString(R.string.household), getString(R.string.costco), 7);
        //getDbStatusHelper().addNewStatus(getString(R.string.paperTowels), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.toiletPaper), getString(R.string.kirklandUltraSoft), getString(R.string.household), getString(R.string.costco), 8);
        //getDbStatusHelper().addNewStatus(getString(R.string.toiletPaper), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.costco), 9);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.costco), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.costco), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.costco), 9);
        getDbStoreHelper().setStoreViews(getString(R.string.costco), 9, 0, 0, 9);

        //------------------------------------Walmart-----------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.darkChocolateCaramelSquares), getString(R.string.ghiradelli), getString(R.string.candy), getString(R.string.walmart), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.darkChocolateCaramelSquares), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.dishwashingBrush), getString(R.string.greatValue), getString(R.string.household), getString(R.string.walmart), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.dishwashingBrush), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.smallTrashBags), getString(R.string.gallon13), getString(R.string.household), getString(R.string.walmart), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.smallTrashBags), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.largeTrashBags), getString(R.string.gallon33), getString(R.string.household), getString(R.string.walmart), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.largeTrashBags), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.compactorBags), getString(R.string.gallon18), getString(R.string.household), getString(R.string.walmart), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.compactorBags), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.intenseDarkChocolate), getString(R.string.ghiradelli), getString(R.string.candy), getString(R.string.walmart), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.intenseDarkChocolate), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.walmart), 6);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.walmart), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.walmart), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.walmart), 6);
        getDbStoreHelper().setStoreViews(getString(R.string.walmart), 6, 0, 0, 6);

        //------------------------------------Amazon------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.muddyBuddies), getString(R.string.brownieSupreme), getString(R.string.snacks), getString(R.string.amazon), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.muddyBuddies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.gingerbreadCookieMix), getString(R.string.bettyCrocker), getString(R.string.desserts), getString(R.string.amazon), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.gingerbreadCookieMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.jellyBeans), getString(R.string.sizzlingCinnamon), getString(R.string.candy), getString(R.string.amazon), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.jellyBeans), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.orangePez), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.orangePez), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vanillaTaffy), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.vanillaTaffy), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vanillaTootsieRolls), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.vanillaTootsieRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.poopBags), getString(R.string.amazonBasics), getString(R.string.petSupplies), getString(R.string.amazon), 6);
        //getDbStatusHelper().addNewStatus(getString(R.string.poopBags), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.nitrileGloves), getString(R.string.gmg100Pack), getString(R.string.petSupplies), getString(R.string.amazon), 7);
        //getDbStatusHelper().addNewStatus(getString(R.string.nitrileGloves), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.toothpaste), getString(R.string.ultrabrite), getString(R.string.toiletries), getString(R.string.amazon), 8);
        //getDbStatusHelper().addNewStatus(getString(R.string.toothpaste), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.floss), getString(R.string.reachMintWaxed), getString(R.string.toiletries), getString(R.string.amazon), 9);
        //getDbStatusHelper().addNewStatus(getString(R.string.floss), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.shavingCream), getString(R.string.sandalwood), getString(R.string.toiletries), getString(R.string.amazon), 10);
        //getDbStatusHelper().addNewStatus(getString(R.string.shavingCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.shavingRazors), getString(R.string.gilletteProGlide), getString(R.string.toiletries), getString(R.string.amazon), 11);
        //getDbStatusHelper().addNewStatus(getString(R.string.shavingRazors), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sunscreen), getString(R.string.hawaiianTropicSheer50spf), getString(R.string.toiletries), getString(R.string.amazon), 12);
        //getDbStatusHelper().addNewStatus(getString(R.string.sunscreen), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.petStainCleaner), getString(R.string.roccoAndRoxie), getString(R.string.household), getString(R.string.amazon), 13);
        //getDbStatusHelper().addNewStatus(getString(R.string.petStainCleaner), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.nightLightBulbs), getString(R.string.c7E12), getString(R.string.household), getString(R.string.amazon), 14);
        //getDbStatusHelper().addNewStatus(getString(R.string.nightLightBulbs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.fortuneCookies), getString(R.string.toDo), getString(R.string.snacks), getString(R.string.amazon), 15);
        //getDbStatusHelper().addNewStatus(getString(R.string.fortuneCookies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.communionWafers), getString(R.string.cavanaghAltarBread), getString(R.string.snacks), getString(R.string.amazon), 16);
        //getDbStatusHelper().addNewStatus(getString(R.string.communionWafers), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sixlets), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 17);
        //getDbStatusHelper().addNewStatus(getString(R.string.sixlets), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.littleTreesAirFresheners), getString(R.string.trueNorth), getString(R.string.household), getString(R.string.amazon), 18);
        //getDbStatusHelper().addNewStatus(getString(R.string.littleTreesAirFresheners), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tripleOmega), getString(R.string.natureMade), getString(R.string.supplements), getString(R.string.amazon), 19);
        //getDbStatusHelper().addNewStatus(getString(R.string.tripleOmega), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.multivitamin), getString(R.string.oneADayMens), getString(R.string.supplements), getString(R.string.amazon), 20);
        //getDbStatusHelper().addNewStatus(getString(R.string.multivitamin), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vitaminC), getString(R.string.amazonElements1000mg), getString(R.string.supplements), getString(R.string.amazon), 21);
        //getDbStatusHelper().addNewStatus(getString(R.string.vitaminC), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.magnesium), getString(R.string.natureMade400mg), getString(R.string.supplements), getString(R.string.amazon), 22);
        //getDbStatusHelper().addNewStatus(getString(R.string.magnesium), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.zinc), getString(R.string.sandhuHerbals50mg), getString(R.string.supplements), getString(R.string.amazon), 23);
        //getDbStatusHelper().addNewStatus(getString(R.string.zinc), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.calcium), getString(R.string.naturesTruth1200mg), getString(R.string.supplements), getString(R.string.amazon), 24);
        //getDbStatusHelper().addNewStatus(getString(R.string.calcium), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.biotin), getString(R.string.natrol10000mcg), getString(R.string.supplements), getString(R.string.amazon), 25);
        //getDbStatusHelper().addNewStatus(getString(R.string.biotin), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vitaminD3), getString(R.string.natureMade5000IU), getString(R.string.supplements), getString(R.string.amazon), 26);
        //getDbStatusHelper().addNewStatus(getString(R.string.vitaminD3), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hyaluronicAcid), getString(R.string.horbaach1000mg), getString(R.string.supplements), getString(R.string.amazon), 27);
        //getDbStatusHelper().addNewStatus(getString(R.string.hyaluronicAcid), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.amazon), 28);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.amazon), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.amazon), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.amazon), 28);
        getDbStoreHelper().setStoreViews(getString(R.string.amazon), 28, 0, 0, 28);

        //------------------------------------Stater Bros-------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.beefNoodles), getString(R.string.yakisoba), getString(R.string.soups), getString(R.string.staterBros), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.beefNoodles), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocChipCookieMix), getString(R.string.bettyCrockerGlutenFree), getString(R.string.desserts), getString(R.string.staterBros), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocChipCookieMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocMaltMix), getString(R.string.nestle), getString(R.string.desserts), getString(R.string.staterBros), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocMaltMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocFudgePudding), getString(R.string.snackPack), getString(R.string.snacks), getString(R.string.staterBros), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocFudgePudding), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.dryerSheets), getString(R.string.simplyDoneFreshLinen), getString(R.string.household), getString(R.string.staterBros), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.dryerSheets), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.staterBros), 5);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.staterBros), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.staterBros), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.staterBros), 5);
        getDbStoreHelper().setStoreViews(getString(R.string.staterBros), 5, 0, 0, 5);

        //------------------------------------Trader Joe's-------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.holdTheCone), getString(R.string.chocolate), getString(R.string.desserts), getString(R.string.traderJoes), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.holdTheCone), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.darkChocCaramels), getString(R.string.traderJoes), getString(R.string.candy), getString(R.string.traderJoes), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.darkChocCaramels), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.darkChocPeanutButterCups), getString(R.string.traderJoes), getString(R.string.candy), getString(R.string.traderJoes), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.darkChocPeanutButterCups), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.traderJoes), 3);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.traderJoes), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.traderJoes), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.traderJoes), 3);
        getDbStoreHelper().setStoreViews(getString(R.string.traderJoes), 3, 0, 0, 3);

        //------------------------------------CVS---------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.darkChocolatePretzels), getString(R.string.flipz), getString(R.string.snacks), getString(R.string.cvs), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.darkChocolatePretzels), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.candyCorn), getString(R.string.brachs), getString(R.string.candy), getString(R.string.cvs), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.candyCorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.rubbingAlcohol), getString(R.string.isopropyl), getString(R.string.household), getString(R.string.cvs), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.rubbingAlcohol), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hydrogenPeroxide), getString(R.string.na), getString(R.string.household), getString(R.string.cvs), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.hydrogenPeroxide), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.moisturizingLotion), getString(R.string.cvsHealthHyaluronicAcid), getString(R.string.toiletries), getString(R.string.cvs), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.moisturizingLotion), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.packagingTape), getString(R.string.scotchHeavyDuty), getString(R.string.household), getString(R.string.cvs), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.packagingTape), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.faceSunscreen), getString(R.string.aveenoProtectHydrate), getString(R.string.toiletries), getString(R.string.cvs), 6);
        //getDbStatusHelper().addNewStatus(getString(R.string.faceSunscreen), getString(R.string.paused), getString(R.string.unchecked));


        getStoreData().getStoreViewAllMap().put(getString(R.string.cvs), 7);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.cvs), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.cvs), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.cvs), 7);
        getDbStoreHelper().setStoreViews(getString(R.string.cvs), 7, 0, 0, 7);

        //------------------------------------Dollar Tree-------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.snoCaps), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.snoCaps), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.smarties), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.smarties), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.redHots), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.redHots), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.handSoap), getString(R.string.lavenderAndChamomile), getString(R.string.toiletries), getString(R.string.dollarTree), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.handSoap), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.ramenNoodles), getString(R.string.nissin), getString(R.string.soups), getString(R.string.dollarTree), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.ramenNoodles), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.dollarTree), 5);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.dollarTree), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.dollarTree), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.dollarTree), 5);
        getDbStoreHelper().setStoreViews(getString(R.string.dollarTree), 5, 0, 0, 5);

        //------------------------------------Ralphs------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.clarifiedButter), getString(R.string.challenge), getString(R.string.eggsDairy), getString(R.string.ralphs), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.clarifiedButter), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.ralphs), 1);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.ralphs), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.ralphs), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.ralphs), 1);
        getDbStoreHelper().setStoreViews(getString(R.string.ralphs), 1, 0, 0, 1);

        //------------------------------------Target------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.chocolateSyrup), getString(R.string.ghirardelliOrHersheys), getString(R.string.condiments), getString(R.string.target), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocolateSyrup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.caramelSyrup), getString(R.string.ghirardelliOrHersheys), getString(R.string.condiments), getString(R.string.target), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.caramelSyrup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.darkChocMandMs), getString(R.string.na), getString(R.string.candy), getString(R.string.target), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.darkChocMandMs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.seaSaltCaramels), getString(R.string.favoriteDay), getString(R.string.candy), getString(R.string.target), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.seaSaltCaramels), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.oreoPieMix), getString(R.string.noBakeDessert), getString(R.string.desserts), getString(R.string.target), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.oreoPieMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.pieFilling), getString(R.string.jelloChocFudge), getString(R.string.desserts), getString(R.string.target), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.pieFilling), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.target), 6);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.target), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.target), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.target), 6);
        getDbStoreHelper().setStoreViews(getString(R.string.target), 6, 0, 0, 6);

        //------------------------------------Pet Supplies Plus-------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.catFoodDry), getString(R.string.purinaProPlan), getString(R.string.petSupplies), getString(R.string.petSuppliesPlus), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.catFoodDry), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.petSuppliesPlus), 1);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.petSuppliesPlus), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.petSuppliesPlus), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.petSuppliesPlus), 1);
        getDbStoreHelper().setStoreViews(getString(R.string.petSuppliesPlus), 1, 0, 0, 1);

        //------------------------------------Sprouts-------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.barSoap), getString(R.string.zumBarSeaSalt), getString(R.string.toiletries), getString(R.string.sprouts), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.barSoap), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.toothbrushHeads), getString(R.string.radiusSoft), getString(R.string.toiletries), getString(R.string.sprouts), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.toothbrushHeads), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.sprouts), 2);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.sprouts), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.sprouts), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.sprouts), 2);
        getDbStoreHelper().setStoreViews(getString(R.string.sprouts), 2, 0, 0, 2);

        //------------------------------------Sam's Club--------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.quickSteak), getString(R.string.garys), getString(R.string.meat), getString(R.string.samsClub), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.quickSteak), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.paperPlates), getString(R.string.toDo), getString(R.string.household), getString(R.string.samsClub), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.paperPlates), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.samsClub), 2);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.samsClub), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.samsClub), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.samsClub), 2);
        getDbStoreHelper().setStoreViews(getString(R.string.samsClub), 2, 0, 0, 2);

        //---------------------------------------Staples--------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.multipurposePaper), getString(R.string.truRed), getString(R.string.household), getString(R.string.staples), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.multipurposePaper), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.staples), 1);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.staples), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.staples), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.staples), 1);
        getDbStoreHelper().setStoreViews(getString(R.string.staples), 1, 0, 0, 1);

        //---------------------------------------Woodranch--------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.woodranchBBQSauce), getString(R.string.pint1), getString(R.string.condiments), getString(R.string.woodranch), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.woodranchBBQSauce), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.woodranch), 1);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.woodranch), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.woodranch), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.woodranch), 1);
        getDbStoreHelper().setStoreViews(getString(R.string.woodranch), 1, 0, 0, 1);

        //------------------------------------Yorba Linda Feed Store--------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.dogFoodDry), getString(R.string.canidaeAllLifeStages), getString(R.string.petSupplies), getString(R.string.yorbaLindaFeedStore), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.dogFoodDry), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.yorbaLindaFeedStore), 1);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.yorbaLindaFeedStore), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.yorbaLindaFeedStore), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.yorbaLindaFeedStore), 1);
        getDbStoreHelper().setStoreViews(getString(R.string.yorbaLindaFeedStore), 1, 0, 0, 1);

        //------------------------------------------------------------------------------------------

        // total store items = 222

    }

//------------------------------------------------------------------------------------------------//
//-------------------------------------Sort By Category-------------------------------------------//
//------------------------------------------------------------------------------------------------//

    void loadCategoryData2() {

        //------------------------------------Meals-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.sausageBiscuits), getString(R.string.jimmyDeanFrozen), getString(R.string.meals), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.sausageBiscuits), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hamburgerHelper), getString(R.string.cheeseburgerMacaroni), getString(R.string.meals), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.hamburgerHelper), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.buffaloChickenBites), getString(R.string.tGIForFranks), getString(R.string.meals), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.buffaloChickenBites), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.terriyakiChickenBites), getString(R.string.innovAsian), getString(R.string.meals), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.terriyakiChickenBites), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.crispyBuffaloWings), getString(R.string.fosterFarms), getString(R.string.meals), getString(R.string.costco), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.crispyBuffaloWings), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tgifCheeseSticks), getString(R.string.tgifSmall10pc), getString(R.string.meals), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.tgifCheeseSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.mozarellaCheeseSticks), getString(R.string.farmRich), getString(R.string.meals), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.mozarellaCheeseSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.frozenPizza), getString(R.string.thinPepperoni), getString(R.string.meals), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.frozenPizza), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cornDogs), getString(R.string.fosterFarms), getString(R.string.meals), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.cornDogs), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hotDogs), getString(R.string.bunSize), getString(R.string.meals), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.hotDogs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hotDogBuns), getString(R.string.pack8), getString(R.string.meals), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.hotDogBuns), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hamburgerPatties), getString(R.string.toDo), getString(R.string.meals), getString(R.string.vons), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.hamburgerPatties), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hamburgerBuns), getString(R.string.pack8), getString(R.string.meals), getString(R.string.vons), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.hamburgerBuns), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.pastaRoni1), getString(R.string.angelHairPasta), getString(R.string.meals), getString(R.string.vons), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.pastaRoni1), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.pastaRoni2), getString(R.string.fettuccineAlfredo), getString(R.string.meals), getString(R.string.vons), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.pastaRoni2), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.macAndCheese), getString(R.string.annies), getString(R.string.meals), getString(R.string.vons), 15);
        getDbStatusHelper().addNewStatus(getString(R.string.macAndCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.gnocci), getString(R.string.signatureSelect), getString(R.string.meals), getString(R.string.vons), 16);
        getDbStatusHelper().addNewStatus(getString(R.string.gnocci), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tortellini), getString(R.string.barilla3Cheese), getString(R.string.meals), getString(R.string.vons), 17);
        getDbStatusHelper().addNewStatus(getString(R.string.tortellini), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.meals), 18);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.meals), 4);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.meals), 2);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.meals), 12);
        getDbCategoryHelper().setCategoryViews(getString(R.string.meals), 18, 4, 2, 12);

        //------------------------------------Soups-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.spaghettiOs), getString(R.string.wMeatballs), getString(R.string.soups), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.spaghettiOs), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chickenNoodleSoup), getString(R.string.campbells), getString(R.string.soups), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.chickenNoodleSoup), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.minestroneSoup), getString(R.string.amys), getString(R.string.soups), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.minestroneSoup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vegetableBarleySoup), getString(R.string.amys), getString(R.string.soups), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.vegetableBarleySoup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.beefNoodles), getString(R.string.yakisoba), getString(R.string.soups), getString(R.string.staterBros), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.beefNoodles), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cupOfNoodles), getString(R.string.nissin), getString(R.string.soups), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.cupOfNoodles), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.ramenNoodles), getString(R.string.nissin), getString(R.string.soups), getString(R.string.dollarTree), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.ramenNoodles), getString(R.string.instock), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.soups), 7);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.soups), 2);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.soups), 2);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.soups), 3);
        getDbCategoryHelper().setCategoryViews(getString(R.string.soups), 7, 2, 2, 3);

        //------------------------------------Sides-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.frozenFrenchFries), getString(R.string.oreIda), getString(R.string.sides), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.frozenFrenchFries), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.texasCheesyBread), getString(R.string.newYorkBakery), getString(R.string.sides), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.texasCheesyBread), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.garlicBreadsticks), getString(R.string.newYorkBakery), getString(R.string.sides), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.garlicBreadsticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chickenRice), getString(R.string.knorr), getString(R.string.sides), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.chickenRice), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cannedCorn), getString(R.string.delMonte), getString(R.string.sides), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.cannedCorn), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.crescentRolls), getString(R.string.signatureSelect), getString(R.string.sides), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.crescentRolls), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.sides), 6);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.sides), 2);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.sides), 1);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.sides), 3);
        getDbCategoryHelper().setCategoryViews(getString(R.string.sides), 6, 2, 1, 3);

        //------------------------------------Meat--------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.steak), getString(R.string.usda), getString(R.string.meat), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.steak), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.groundBeef), getString(R.string.pound1), getString(R.string.meat), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.groundBeef), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.frozenMeatballs), getString(R.string.rosinaHomestyle), getString(R.string.meat), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.frozenMeatballs), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.pepperoniSlices), getString(R.string.hormel300Slices), getString(R.string.meat), getString(R.string.smartFinal), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.pepperoniSlices), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.quickSteak), getString(R.string.garys), getString(R.string.meat), getString(R.string.samsClub), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.quickSteak), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chickenBreast), getString(R.string.na), getString(R.string.meat), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.chickenBreast), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.slicedTurkey), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.slicedTurkey), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.slicedHam), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.slicedHam), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hamSteak), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.hamSteak), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.beefLitlSmokies), getString(R.string.hillshireFarm), getString(R.string.meat), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.beefLitlSmokies), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.meat), 10);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.meat), 3);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.meat), 1);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.meat), 6);
        getDbCategoryHelper().setCategoryViews(getString(R.string.meat), 10, 3, 1, 6);

        //------------------------------------Bread/Grains/Cereal-----------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.thinSpaghetti), getString(R.string.barillaWholeGrain), getString(R.string.breadGrainsCereal), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.thinSpaghetti), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.spiralPasta), getString(R.string.barillaRotini), getString(R.string.breadGrainsCereal), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.spiralPasta), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.wheatBread), getString(R.string.naturesOwn), getString(R.string.breadGrainsCereal), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.wheatBread), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.baguette), getString(R.string.frenchOrSourdough), getString(R.string.breadGrainsCereal), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.baguette), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sourdoughBread), getString(R.string.sanLuis), getString(R.string.breadGrainsCereal), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.sourdoughBread), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hardRolls), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.hardRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.thomasMuffins), getString(R.string.original), getString(R.string.breadGrainsCereal), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.thomasMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.reesesPuffsCereal), getString(R.string.generalMills), getString(R.string.breadGrainsCereal), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.reesesPuffsCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cookieCrispCereal), getString(R.string.generalMills), getString(R.string.breadGrainsCereal), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.cookieCrispCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.frostedMiniWheatCereal), getString(R.string.kelloggs), getString(R.string.breadGrainsCereal), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.frostedMiniWheatCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.honeySmacksCereal), getString(R.string.kelloggs), getString(R.string.breadGrainsCereal), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.honeySmacksCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.eggoWaffles), getString(R.string.homestyle), getString(R.string.breadGrainsCereal), getString(R.string.vons), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.eggoWaffles), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.smallFlourTortillas), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.smallFlourTortillas), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.largeFlourTortillas), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.largeFlourTortillas), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.breadGrainsCereal), 14);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.breadGrainsCereal), 2);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.breadGrainsCereal), 1);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.breadGrainsCereal), 11);
        getDbCategoryHelper().setCategoryViews(getString(R.string.breadGrainsCereal), 14, 2, 1, 11);

        //----------------------------------------Eggs/Dairy----------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.milk), getString(R.string.vitaminD), getString(R.string.eggsDairy), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.milk), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.eggs), getString(R.string.gradeAAxLarge), getString(R.string.eggsDairy), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.eggs), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.honeyYogurt), getString(R.string.greekGods), getString(R.string.eggsDairy), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.honeyYogurt), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sourCream), getString(R.string.toDo), getString(R.string.eggsDairy), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.sourCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.saltedButter), getString(R.string.challenge), getString(R.string.eggsDairy), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.saltedButter), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.clarifiedButter), getString(R.string.challenge), getString(R.string.eggsDairy), getString(R.string.ralphs), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.clarifiedButter), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.shreddedCheese), getString(R.string.mexicanBlend), getString(R.string.eggsDairy), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.shreddedCheese), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.stringCheese), getString(R.string.mozarella), getString(R.string.eggsDairy), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.stringCheese), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.bdCheese), getString(R.string.blackDiamond), getString(R.string.eggsDairy), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.bdCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.slicedCheese), getString(R.string.kraftSingles), getString(R.string.eggsDairy), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.slicedCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.halfAndHalf), getString(R.string.lucerne), getString(R.string.eggsDairy), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.halfAndHalf), getString(R.string.instock), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.eggsDairy), 11);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.eggsDairy), 4);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.eggsDairy), 2);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.eggsDairy), 5);
        getDbCategoryHelper().setCategoryViews(getString(R.string.eggsDairy), 11, 4, 2, 5);

        //------------------------------------Condiments--------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.parmesanCheese), getString(R.string.kraft), getString(R.string.condiments), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.parmesanCheese), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.a1sauce), getString(R.string.original), getString(R.string.condiments), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.a1sauce), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.ketchup), getString(R.string.heinz), getString(R.string.condiments), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.ketchup), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.mustard), getString(R.string.heinz), getString(R.string.condiments), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.mustard), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.pastaSauce), getString(R.string.raguMeat), getString(R.string.condiments), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.pastaSauce), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.marinaraSauce), getString(R.string.signatureTraditional), getString(R.string.condiments), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.marinaraSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tacoSauce), getString(R.string.victoriasMild), getString(R.string.condiments), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.tacoSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.buffaloSauce), getString(R.string.franksWings), getString(R.string.condiments), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.buffaloSauce), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocolateSyrup), getString(R.string.ghirardelliOrHersheys), getString(R.string.condiments), getString(R.string.target), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.chocolateSyrup), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.caramelSyrup), getString(R.string.ghirardelliOrHersheys), getString(R.string.condiments), getString(R.string.target), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.caramelSyrup), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.mapleSyrup), getString(R.string.pearlMilling), getString(R.string.condiments), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.mapleSyrup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.honey), getString(R.string.localHiveClover), getString(R.string.condiments), getString(R.string.vons), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.honey), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.peanutButter), getString(R.string.skippyCreamy), getString(R.string.condiments), getString(R.string.vons), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.peanutButter), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.soySauce), getString(R.string.kikoman), getString(R.string.condiments), getString(R.string.vons), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.soySauce), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.woodranchBBQSauce), getString(R.string.pint1), getString(R.string.condiments), getString(R.string.woodranch), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.woodranchBBQSauce), getString(R.string.instock), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.condiments), 15);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.condiments), 11);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.condiments), 1);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.condiments), 3);
        getDbCategoryHelper().setCategoryViews(getString(R.string.condiments), 15, 11, 1, 3);

        //------------------------------------Seasonings--------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.saltAndPepper), getString(R.string.na), getString(R.string.seasonings), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.saltAndPepper), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.garlicSalt), getString(R.string.lawrys), getString(R.string.seasonings), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.garlicSalt), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.lawrysSeasoningSalt), getString(R.string.lawrys), getString(R.string.seasonings), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.lawrysSeasoningSalt), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tacoSeasoning), getString(R.string.any), getString(R.string.seasonings), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.tacoSeasoning), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.ranchDipMix), getString(R.string.lauraScudders), getString(R.string.seasonings), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.ranchDipMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vanillaExtract), getString(R.string.signatureSelect), getString(R.string.seasonings), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.vanillaExtract), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cinnamonSugar), getString(R.string.mcCormicks), getString(R.string.seasonings), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.cinnamonSugar), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sprinkles), getString(R.string.types3), getString(R.string.seasonings), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.sprinkles), getString(R.string.needed), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.seasonings), 8);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.seasonings), 5);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.seasonings), 1);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.seasonings), 2);
        getDbCategoryHelper().setCategoryViews(getString(R.string.seasonings), 8, 5, 1, 2);

        //---------------------------------Misc/Ingredients----------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.brownSugar), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.brownSugar), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tacoShells), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.tacoShells), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.saltineCrackers), getString(R.string.premiumOriginal), getString(R.string.miscIngredients), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.saltineCrackers), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.semiSweetChocChips), getString(R.string.nestle), getString(R.string.miscIngredients), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.semiSweetChocChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vegetableOil), getString(R.string.crisco), getString(R.string.miscIngredients), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.vegetableOil), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.extraVirginOliveOil), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.extraVirginOliveOil), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.nonStickSpray), getString(R.string.pamOriginal), getString(R.string.miscIngredients), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.nonStickSpray), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.movieTheaterButter), getString(R.string.kernelSeasons), getString(R.string.miscIngredients), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.movieTheaterButter), getString(R.string.instock), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.miscIngredients), 8);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.miscIngredients), 5);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.miscIngredients), 1);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.miscIngredients), 2);
        getDbCategoryHelper().setCategoryViews(getString(R.string.miscIngredients), 8, 5, 1, 2);

        //------------------------------------Drinks------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.sodaBottles2L), getString(R.string.pepsiOrCoke), getString(R.string.drinks), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.sodaBottles2L), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sodaCans), getString(R.string.pepsiOrCoke), getString(R.string.drinks), getString(R.string.costco), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.sodaCans), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hotChocolateMix), getString(R.string.swissMissDark), getString(R.string.drinks), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.hotChocolateMix), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.bottledWater), getString(R.string.refreshe40pack), getString(R.string.drinks), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.bottledWater), getString(R.string.needed), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.drinks), 4);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.drinks), 3);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.drinks), 1);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.drinks), 0);
        getDbCategoryHelper().setCategoryViews(getString(R.string.drinks), 4, 3, 1, 0);

        //------------------------------------Snacks------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.beefJerky), getString(R.string.archerTerriyaki), getString(R.string.snacks), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.beefJerky), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.peanuts), getString(R.string.honeyRoasted), getString(R.string.snacks), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.peanuts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.shellPeanuts), getString(R.string.salted), getString(R.string.snacks), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.shellPeanuts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sunflowerSeeds), getString(R.string.salted), getString(R.string.snacks), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.sunflowerSeeds), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vinegarChips), getString(R.string.kettle), getString(R.string.snacks), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.vinegarChips), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.bbqChips), getString(R.string.kettle), getString(R.string.snacks), getString(R.string.vons), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.bbqChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.doritos), getString(R.string.coolRanch), getString(R.string.snacks), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.doritos), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.laysChips), getString(R.string.classic), getString(R.string.snacks), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.laysChips), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.naanCrisps), getString(R.string.stonefire), getString(R.string.snacks), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.naanCrisps), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.ritzCrackers), getString(R.string.original), getString(R.string.snacks), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.ritzCrackers), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.goldfish), getString(R.string.cheddar), getString(R.string.snacks), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.goldfish), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cheezIts), getString(R.string.original), getString(R.string.snacks), getString(R.string.vons), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.cheezIts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.famousAmosCookies), getString(R.string.resealableBag), getString(R.string.snacks), getString(R.string.vons), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.famousAmosCookies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.darkChocolatePretzels), getString(R.string.flipz), getString(R.string.snacks), getString(R.string.cvs), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.darkChocolatePretzels), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocFudgePudding), getString(R.string.snackPack), getString(R.string.snacks), getString(R.string.staterBros), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.chocFudgePudding), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocFudgePirouette), getString(R.string.pepperidgeFarm), getString(R.string.snacks), getString(R.string.vons), 15);
        getDbStatusHelper().addNewStatus(getString(R.string.chocFudgePirouette), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.muddyBuddies), getString(R.string.brownieSupreme), getString(R.string.snacks), getString(R.string.amazon), 16);
        getDbStatusHelper().addNewStatus(getString(R.string.muddyBuddies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.fortuneCookies), getString(R.string.toDo), getString(R.string.snacks), getString(R.string.amazon), 17);
        getDbStatusHelper().addNewStatus(getString(R.string.fortuneCookies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.communionWafers), getString(R.string.cavanaghAltarBread), getString(R.string.snacks), getString(R.string.amazon), 18);
        getDbStatusHelper().addNewStatus(getString(R.string.communionWafers), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.butteredPopcorn), getString(R.string.popSecretMiniBags), getString(R.string.snacks), getString(R.string.vons), 19);
        getDbStatusHelper().addNewStatus(getString(R.string.butteredPopcorn), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.caramelPopcorn), getString(R.string.cretors), getString(R.string.snacks), getString(R.string.vons), 20);
        getDbStatusHelper().addNewStatus(getString(R.string.caramelPopcorn), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocCaramelSwirlPopcorn), getString(R.string.cretors), getString(R.string.snacks), getString(R.string.vons), 21);
        getDbStatusHelper().addNewStatus(getString(R.string.chocCaramelSwirlPopcorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.gingerSnaps), getString(R.string.firstStreet), getString(R.string.snacks), getString(R.string.smartFinal), 22);
        getDbStatusHelper().addNewStatus(getString(R.string.gingerSnaps), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.snacks), 23);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.snacks), 4);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.snacks), 4);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.snacks), 15);
        getDbCategoryHelper().setCategoryViews(getString(R.string.snacks), 23, 4, 4, 15);

        //------------------------------------Desserts----------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocMaltedCrunchIceCream), getString(R.string.thrifty), getString(R.string.desserts), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.chocMaltedCrunchIceCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocTruffleIceCream), getString(R.string.breyers), getString(R.string.desserts), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.chocTruffleIceCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.holdTheCone), getString(R.string.chocolate), getString(R.string.desserts), getString(R.string.traderJoes), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.holdTheCone), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.churros), getString(R.string.tioPepesOrHola), getString(R.string.desserts), getString(R.string.smartFinal), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.churros), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocChipMuffinMix), getString(R.string.bettyCrocker), getString(R.string.desserts), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.chocChipMuffinMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocChipCookieMix), getString(R.string.bettyCrockerGlutenFree), getString(R.string.desserts), getString(R.string.staterBros), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.chocChipCookieMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.gingerbreadCookieMix), getString(R.string.bettyCrocker), getString(R.string.desserts), getString(R.string.amazon), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.gingerbreadCookieMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.oreos), getString(R.string.forCrumbs), getString(R.string.desserts), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.oreos), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.oreoMuffins), getString(R.string.pack12), getString(R.string.desserts), getString(R.string.costco), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.oreoMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.oreoCakesters), getString(R.string.nabisco), getString(R.string.desserts), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.oreoCakesters), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.oreoPieMix), getString(R.string.noBakeDessert), getString(R.string.desserts), getString(R.string.target), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.oreoPieMix), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chocMaltMix), getString(R.string.nestle), getString(R.string.desserts), getString(R.string.staterBros), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.chocMaltMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.doubleChocMuffins), getString(R.string.count4), getString(R.string.desserts), getString(R.string.vons), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.doubleChocMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.pieFilling), getString(R.string.jelloChocFudge), getString(R.string.desserts), getString(R.string.target), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.pieFilling), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.desserts), 14);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.desserts), 2);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.desserts), 2);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.desserts), 10);
        getDbCategoryHelper().setCategoryViews(getString(R.string.desserts), 12, 2, 2, 10);

        //------------------------------------Candy-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.darkChocolateCaramelSquares), getString(R.string.ghiradelli), getString(R.string.candy), getString(R.string.walmart), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.darkChocolateCaramelSquares), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.reesesPBCups), getString(R.string.individuallyWrapped), getString(R.string.candy), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.reesesPBCups), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.candyCorn), getString(R.string.brachs), getString(R.string.candy), getString(R.string.cvs), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.candyCorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hotTamales), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.hotTamales), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.smarties), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.smarties), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.snoCaps), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.snoCaps), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.goodAndPlenty), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.goodAndPlenty), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.miniMAndMs), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.miniMAndMs), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.darkChocMandMs), getString(R.string.na), getString(R.string.candy), getString(R.string.target), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.darkChocMandMs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.seaSaltCaramels), getString(R.string.favoriteDay), getString(R.string.candy), getString(R.string.target), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.seaSaltCaramels), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.jellyBeans), getString(R.string.sizzlingCinnamon), getString(R.string.candy), getString(R.string.amazon), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.jellyBeans), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.tootsieRolls), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.tootsieRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.funDipSticks), getString(R.string.na), getString(R.string.candy), getString(R.string.smartFinal), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.funDipSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.intenseDarkChocolate), getString(R.string.ghiradelli), getString(R.string.candy), getString(R.string.walmart), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.intenseDarkChocolate), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.orangeTicTacs), getString(R.string.na), getString(R.string.candy), getString(R.string.smartFinal), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.orangeTicTacs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.orangePez), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 15);
        getDbStatusHelper().addNewStatus(getString(R.string.orangePez), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vanillaTaffy), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 16);
        getDbStatusHelper().addNewStatus(getString(R.string.vanillaTaffy), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vanillaTootsieRolls), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 17);
        getDbStatusHelper().addNewStatus(getString(R.string.vanillaTootsieRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sixlets), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 18);
        getDbStatusHelper().addNewStatus(getString(R.string.sixlets), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.redHots), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 19);
        getDbStatusHelper().addNewStatus(getString(R.string.redHots), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.darkChocCaramels), getString(R.string.traderJoes), getString(R.string.candy), getString(R.string.traderJoes), 20);
        getDbStatusHelper().addNewStatus(getString(R.string.darkChocCaramels), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.darkChocPeanutButterCups), getString(R.string.traderJoes), getString(R.string.candy), getString(R.string.traderJoes), 21);
        getDbStatusHelper().addNewStatus(getString(R.string.darkChocPeanutButterCups), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.candy), 22);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.candy), 3);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.candy), 2);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.candy), 17);
        getDbCategoryHelper().setCategoryViews(getString(R.string.candy), 22, 3, 2, 17);

        //------------------------------------Pet Supplies-------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.catFoodWet), getString(R.string.fancyFeast), getString(R.string.petSupplies), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.catFoodWet), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.catFoodDry), getString(R.string.purinaProPlan), getString(R.string.petSupplies), getString(R.string.petSuppliesPlus), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.catFoodDry), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.delectables), getString(R.string.squeezeUp20pack), getString(R.string.petSupplies), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.delectables), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.catTreats), getString(R.string.temptations), getString(R.string.petSupplies), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.catTreats), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.kittyLitter), getString(R.string.scoopAwayComplete), getString(R.string.petSupplies), getString(R.string.costco), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.kittyLitter), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.dogFoodDry), getString(R.string.canidaeAllLifeStages), getString(R.string.petSupplies), getString(R.string.yorbaLindaFeedStore), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.dogFoodDry), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.chickenBroth), getString(R.string.kirklandOrganic), getString(R.string.petSupplies), getString(R.string.costco), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.chickenBroth), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.mashedPotatoes), getString(R.string.mainStBistro), getString(R.string.petSupplies), getString(R.string.costco), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.mashedPotatoes), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.freshpet), getString(R.string.chickenRecipe), getString(R.string.petSupplies), getString(R.string.costco), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.freshpet), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.purePumpkin), getString(R.string.libbys), getString(R.string.petSupplies), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.purePumpkin), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.poopBags), getString(R.string.amazonBasics), getString(R.string.petSupplies), getString(R.string.amazon), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.poopBags), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.nitrileGloves), getString(R.string.gmg100Pack), getString(R.string.petSupplies), getString(R.string.amazon), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.nitrileGloves), getString(R.string.instock), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.petSupplies), 12);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.petSupplies), 9);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.petSupplies), 2);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.petSupplies), 1);
        getDbCategoryHelper().setCategoryViews(getString(R.string.petSupplies), 12, 9, 2, 1);

        //------------------------------------Toiletries--------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.handSoap), getString(R.string.lavenderAndChamomile), getString(R.string.toiletries), getString(R.string.dollarTree), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.handSoap), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.bodyWash), getString(R.string.suaveMandarin), getString(R.string.toiletries), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.bodyWash), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.shampoo), getString(R.string.suave2in1), getString(R.string.toiletries), getString(R.string.vons), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.shampoo), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.barSoap), getString(R.string.zumBarSeaSalt), getString(R.string.toiletries), getString(R.string.sprouts), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.barSoap), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.deodorant), getString(R.string.oldSpice), getString(R.string.toiletries), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.deodorant), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.toothpaste), getString(R.string.ultrabrite), getString(R.string.toiletries), getString(R.string.amazon), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.toothpaste), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.floss), getString(R.string.reachMintWaxed), getString(R.string.toiletries), getString(R.string.amazon), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.floss), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.shavingCream), getString(R.string.sandalwood), getString(R.string.toiletries), getString(R.string.amazon), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.shavingCream), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.shavingRazors), getString(R.string.gilletteProGlide), getString(R.string.toiletries), getString(R.string.amazon), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.shavingRazors), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.mouthwash), getString(R.string.crestWhitening), getString(R.string.toiletries), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.mouthwash), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.cottonSwabs), getString(R.string.qTips), getString(R.string.toiletries), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.cottonSwabs), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.toothbrushHeads), getString(R.string.radiusSoft), getString(R.string.toiletries), getString(R.string.sprouts), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.toothbrushHeads), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.sunscreen), getString(R.string.hawaiianTropicSheer50spf), getString(R.string.toiletries), getString(R.string.amazon), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.sunscreen), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.moisturizingLotion), getString(R.string.cvsHealthHyaluronicAcid), getString(R.string.toiletries), getString(R.string.cvs), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.moisturizingLotion), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.faceSunscreen), getString(R.string.aveenoProtectHydrate), getString(R.string.toiletries), getString(R.string.cvs), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.faceSunscreen), getString(R.string.paused), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.toiletries), 15);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.toiletries), 13);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.toiletries), 1);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.toiletries), 1);
        getDbCategoryHelper().setCategoryViews(getString(R.string.toiletries), 15, 13, 1, 1);

        //------------------------------------Household-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.febreezeAirSpray), getString(R.string.heavyDuty), getString(R.string.household), getString(R.string.vons), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.febreezeAirSpray), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.allPurposeCleaner), getString(R.string.meyersLavender), getString(R.string.household), getString(R.string.vons), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.allPurposeCleaner), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.petStainCleaner), getString(R.string.roccoAndRoxie), getString(R.string.household), getString(R.string.amazon), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.petStainCleaner), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.laundryDetergent), getString(R.string.woolite), getString(R.string.household), getString(R.string.vons), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.laundryDetergent), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.laundrySanitizer), getString(R.string.lysol), getString(R.string.household), getString(R.string.vons), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.laundrySanitizer), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.dryerSheets), getString(R.string.simplyDoneFreshLinen), getString(R.string.household), getString(R.string.staterBros), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.dryerSheets), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.littleTreesAirFresheners), getString(R.string.trueNorth), getString(R.string.household), getString(R.string.amazon), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.littleTreesAirFresheners), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.aluminumFoil), getString(R.string.reynoldsWrap), getString(R.string.household), getString(R.string.vons), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.aluminumFoil), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.zipLockBagsSmall), getString(R.string.sandwich), getString(R.string.household), getString(R.string.vons), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.zipLockBagsSmall), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.zipLockBagsLarge), getString(R.string.freezerGallon), getString(R.string.household), getString(R.string.vons), 9);
        getDbStatusHelper().addNewStatus(getString(R.string.zipLockBagsLarge), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.saranWrap), getString(R.string.plasticWrap), getString(R.string.household), getString(R.string.vons), 10);
        getDbStatusHelper().addNewStatus(getString(R.string.saranWrap), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.rubbingAlcohol), getString(R.string.isopropyl), getString(R.string.household), getString(R.string.cvs), 11);
        getDbStatusHelper().addNewStatus(getString(R.string.rubbingAlcohol), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hydrogenPeroxide), getString(R.string.na), getString(R.string.household), getString(R.string.cvs), 12);
        getDbStatusHelper().addNewStatus(getString(R.string.hydrogenPeroxide), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.nightLightBulbs), getString(R.string.c7E12), getString(R.string.household), getString(R.string.amazon), 13);
        getDbStatusHelper().addNewStatus(getString(R.string.nightLightBulbs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.scrubSponges), getString(R.string.nonScratch), getString(R.string.household), getString(R.string.vons), 14);
        getDbStatusHelper().addNewStatus(getString(R.string.scrubSponges), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.dishwashingBrush), getString(R.string.greatValue), getString(R.string.household), getString(R.string.walmart), 15);
        getDbStatusHelper().addNewStatus(getString(R.string.dishwashingBrush), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.smallTrashBags), getString(R.string.gallon13), getString(R.string.household), getString(R.string.walmart), 16);
        getDbStatusHelper().addNewStatus(getString(R.string.smallTrashBags), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.largeTrashBags), getString(R.string.gallon33), getString(R.string.household), getString(R.string.walmart), 17);
        getDbStatusHelper().addNewStatus(getString(R.string.largeTrashBags), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.compactorBags), getString(R.string.gallon18), getString(R.string.household), getString(R.string.walmart), 18);
        getDbStatusHelper().addNewStatus(getString(R.string.compactorBags), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.dawnPowerwash), getString(R.string.dishCleaner), getString(R.string.household), getString(R.string.vons), 19);
        getDbStatusHelper().addNewStatus(getString(R.string.dawnPowerwash), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.dishSoap), getString(R.string.dawnPlatinum), getString(R.string.household), getString(R.string.vons), 20);
        getDbStatusHelper().addNewStatus(getString(R.string.dishSoap), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.paperPlates), getString(R.string.toDo), getString(R.string.household), getString(R.string.samsClub), 21);
        getDbStatusHelper().addNewStatus(getString(R.string.paperPlates), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.paperTowels), getString(R.string.kirklandPremium), getString(R.string.household), getString(R.string.costco), 22);
        getDbStatusHelper().addNewStatus(getString(R.string.paperTowels), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.toiletPaper), getString(R.string.kirklandUltraSoft), getString(R.string.household), getString(R.string.costco), 23);
        getDbStatusHelper().addNewStatus(getString(R.string.toiletPaper), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.multipurposePaper), getString(R.string.truRed), getString(R.string.household), getString(R.string.staples), 24);
        getDbStatusHelper().addNewStatus(getString(R.string.multipurposePaper), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.packagingTape), getString(R.string.scotchHeavyDuty), getString(R.string.household), getString(R.string.cvs), 25);
        getDbStatusHelper().addNewStatus(getString(R.string.packagingTape), getString(R.string.instock), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.household), 26);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.household), 22);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.household), 3);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.household), 1);
        getDbCategoryHelper().setCategoryViews(getString(R.string.household), 26, 22, 3, 1);

        //------------------------------------Supplements-------------------------------------------------

        getDbItemHelper().addNewItemByCategory(getString(R.string.tripleOmega), getString(R.string.natureMade), getString(R.string.supplements), getString(R.string.amazon), 0);
        getDbStatusHelper().addNewStatus(getString(R.string.tripleOmega), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.multivitamin), getString(R.string.oneADayMens), getString(R.string.supplements), getString(R.string.amazon), 1);
        getDbStatusHelper().addNewStatus(getString(R.string.multivitamin), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vitaminC), getString(R.string.amazonElements1000mg), getString(R.string.supplements), getString(R.string.amazon), 2);
        getDbStatusHelper().addNewStatus(getString(R.string.vitaminC), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.magnesium), getString(R.string.natureMade400mg), getString(R.string.supplements), getString(R.string.amazon), 3);
        getDbStatusHelper().addNewStatus(getString(R.string.magnesium), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.zinc), getString(R.string.sandhuHerbals50mg), getString(R.string.supplements), getString(R.string.amazon), 4);
        getDbStatusHelper().addNewStatus(getString(R.string.zinc), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.calcium), getString(R.string.naturesTruth1200mg), getString(R.string.supplements), getString(R.string.amazon), 5);
        getDbStatusHelper().addNewStatus(getString(R.string.calcium), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.biotin), getString(R.string.natrol10000mcg), getString(R.string.supplements), getString(R.string.amazon), 6);
        getDbStatusHelper().addNewStatus(getString(R.string.biotin), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.vitaminD3), getString(R.string.natureMade5000IU), getString(R.string.supplements), getString(R.string.amazon), 7);
        getDbStatusHelper().addNewStatus(getString(R.string.vitaminD3), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByCategory(getString(R.string.hyaluronicAcid), getString(R.string.horbaach1000mg), getString(R.string.supplements), getString(R.string.amazon), 8);
        getDbStatusHelper().addNewStatus(getString(R.string.hyaluronicAcid), getString(R.string.instock), getString(R.string.unchecked));

        getCategoryData().getCategoryViewAllMap().put(getString(R.string.supplements), 9);
        getCategoryData().getCategoryViewInStockMap().put(getString(R.string.supplements), 9);
        getCategoryData().getCategoryViewNeededMap().put(getString(R.string.supplements), 0);
        getCategoryData().getCategoryViewPausedMap().put(getString(R.string.supplements), 0);
        getDbCategoryHelper().setCategoryViews(getString(R.string.supplements), 9, 9, 0, 0);

        //------------------------------------------------------------------------------------------

        // total category items = 222

    }

//------------------------------------------------------------------------------------------------//
//----------------------------------------Sort By Store-------------------------------------------//
//------------------------------------------------------------------------------------------------//

    void loadStoreData2() {

        //------------------------------------Vons--------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.sausageBiscuits), getString(R.string.jimmyDeanFrozen), getString(R.string.meals), getString(R.string.vons), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.sausageBiscuits), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hamburgerHelper), getString(R.string.cheeseburgerMacaroni), getString(R.string.meals), getString(R.string.vons), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.hamburgerHelper), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.buffaloChickenBites), getString(R.string.tGIForFranks), getString(R.string.meals), getString(R.string.vons), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.buffaloChickenBites), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.terriyakiChickenBites), getString(R.string.innovAsian), getString(R.string.meals), getString(R.string.vons), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.terriyakiChickenBites), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tgifCheeseSticks), getString(R.string.tgifSmall10pc), getString(R.string.meals), getString(R.string.vons), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.tgifCheeseSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.mozarellaCheeseSticks), getString(R.string.farmRich), getString(R.string.meals), getString(R.string.vons), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.mozarellaCheeseSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.frozenPizza), getString(R.string.thinPepperoni), getString(R.string.meals), getString(R.string.vons), 6);
        //getDbStatusHelper().addNewStatus(getString(R.string.frozenPizza), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cornDogs), getString(R.string.fosterFarms), getString(R.string.meals), getString(R.string.vons), 7);
        //getDbStatusHelper().addNewStatus(getString(R.string.cornDogs), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hotDogs), getString(R.string.bunSize), getString(R.string.meals), getString(R.string.vons), 8);
        //getDbStatusHelper().addNewStatus(getString(R.string.hotDogs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hotDogBuns), getString(R.string.pack8), getString(R.string.meals), getString(R.string.vons), 9);
        //getDbStatusHelper().addNewStatus(getString(R.string.hotDogBuns), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hamburgerPatties), getString(R.string.toDo), getString(R.string.meals), getString(R.string.vons), 10);
        //getDbStatusHelper().addNewStatus(getString(R.string.hamburgerPatties), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hamburgerBuns), getString(R.string.pack8), getString(R.string.meals), getString(R.string.vons), 11);
        //getDbStatusHelper().addNewStatus(getString(R.string.hamburgerBuns), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.pastaRoni1), getString(R.string.angelHairPasta), getString(R.string.meals), getString(R.string.vons), 12);
        //getDbStatusHelper().addNewStatus(getString(R.string.pastaRoni1), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.pastaRoni2), getString(R.string.fettuccineAlfredo), getString(R.string.meals), getString(R.string.vons), 13);
        //getDbStatusHelper().addNewStatus(getString(R.string.pastaRoni2), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.macAndCheese), getString(R.string.annies), getString(R.string.meals), getString(R.string.vons), 14);
        //getDbStatusHelper().addNewStatus(getString(R.string.macAndCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.gnocci), getString(R.string.signatureSelect), getString(R.string.meals), getString(R.string.vons), 15);
        //getDbStatusHelper().addNewStatus(getString(R.string.gnocci), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.spaghettiOs), getString(R.string.wMeatballs), getString(R.string.soups), getString(R.string.vons), 16);
        //getDbStatusHelper().addNewStatus(getString(R.string.spaghettiOs), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chickenNoodleSoup), getString(R.string.campbells), getString(R.string.soups), getString(R.string.vons), 17);
        //getDbStatusHelper().addNewStatus(getString(R.string.chickenNoodleSoup), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.minestroneSoup), getString(R.string.amys), getString(R.string.soups), getString(R.string.vons), 18);
        //getDbStatusHelper().addNewStatus(getString(R.string.minestroneSoup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vegetableBarleySoup), getString(R.string.amys), getString(R.string.soups), getString(R.string.vons), 19);
        //getDbStatusHelper().addNewStatus(getString(R.string.vegetableBarleySoup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cupOfNoodles), getString(R.string.nissin), getString(R.string.soups), getString(R.string.vons), 20);
        //getDbStatusHelper().addNewStatus(getString(R.string.cupOfNoodles), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.frozenFrenchFries), getString(R.string.oreIda), getString(R.string.sides), getString(R.string.vons), 21);
        //getDbStatusHelper().addNewStatus(getString(R.string.frozenFrenchFries), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.texasCheesyBread), getString(R.string.newYorkBakery), getString(R.string.sides), getString(R.string.vons), 22);
        //getDbStatusHelper().addNewStatus(getString(R.string.texasCheesyBread), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chickenRice), getString(R.string.knorr), getString(R.string.sides), getString(R.string.vons), 23);
        //getDbStatusHelper().addNewStatus(getString(R.string.chickenRice), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cannedCorn), getString(R.string.delMonte), getString(R.string.sides), getString(R.string.vons), 24);
        //getDbStatusHelper().addNewStatus(getString(R.string.cannedCorn), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.steak), getString(R.string.usda), getString(R.string.meat), getString(R.string.vons), 25);
        //getDbStatusHelper().addNewStatus(getString(R.string.steak), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.groundBeef), getString(R.string.pound1), getString(R.string.meat), getString(R.string.vons), 26);
        //getDbStatusHelper().addNewStatus(getString(R.string.groundBeef), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.frozenMeatballs), getString(R.string.rosinaHomestyle), getString(R.string.meat), getString(R.string.vons), 27);
        //getDbStatusHelper().addNewStatus(getString(R.string.frozenMeatballs), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chickenBreast), getString(R.string.na), getString(R.string.meat), getString(R.string.vons), 28);
        //getDbStatusHelper().addNewStatus(getString(R.string.chickenBreast), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.slicedTurkey), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 29);
        //getDbStatusHelper().addNewStatus(getString(R.string.slicedTurkey), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.slicedHam), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 30);
        //getDbStatusHelper().addNewStatus(getString(R.string.slicedHam), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hamSteak), getString(R.string.toDo), getString(R.string.meat), getString(R.string.vons), 31);
        //getDbStatusHelper().addNewStatus(getString(R.string.hamSteak), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.thinSpaghetti), getString(R.string.barillaWholeGrain), getString(R.string.breadGrainsCereal), getString(R.string.vons), 32);
        //getDbStatusHelper().addNewStatus(getString(R.string.thinSpaghetti), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.spiralPasta), getString(R.string.barillaRotini), getString(R.string.breadGrainsCereal), getString(R.string.vons), 33);
        //getDbStatusHelper().addNewStatus(getString(R.string.spiralPasta), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.wheatBread), getString(R.string.naturesOwn), getString(R.string.breadGrainsCereal), getString(R.string.vons), 34);
        //getDbStatusHelper().addNewStatus(getString(R.string.wheatBread), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.baguette), getString(R.string.frenchOrSourdough), getString(R.string.breadGrainsCereal), getString(R.string.vons), 35);
        //getDbStatusHelper().addNewStatus(getString(R.string.baguette), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sourdoughBread), getString(R.string.sanLuis), getString(R.string.breadGrainsCereal), getString(R.string.vons), 36);
        //getDbStatusHelper().addNewStatus(getString(R.string.sourdoughBread), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hardRolls), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 37);
        //getDbStatusHelper().addNewStatus(getString(R.string.hardRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.thomasMuffins), getString(R.string.original), getString(R.string.breadGrainsCereal), getString(R.string.vons), 38);
        //getDbStatusHelper().addNewStatus(getString(R.string.thomasMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.reesesPuffsCereal), getString(R.string.generalMills), getString(R.string.breadGrainsCereal), getString(R.string.vons), 39);
        //getDbStatusHelper().addNewStatus(getString(R.string.reesesPuffsCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cookieCrispCereal), getString(R.string.generalMills), getString(R.string.breadGrainsCereal), getString(R.string.vons), 40);
        //getDbStatusHelper().addNewStatus(getString(R.string.cookieCrispCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.frostedMiniWheatCereal), getString(R.string.kelloggs), getString(R.string.breadGrainsCereal), getString(R.string.vons), 41);
        //getDbStatusHelper().addNewStatus(getString(R.string.frostedMiniWheatCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.honeySmacksCereal), getString(R.string.kelloggs), getString(R.string.breadGrainsCereal), getString(R.string.vons), 42);
        //getDbStatusHelper().addNewStatus(getString(R.string.honeySmacksCereal), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.eggoWaffles), getString(R.string.homestyle), getString(R.string.breadGrainsCereal), getString(R.string.vons), 43);
        //getDbStatusHelper().addNewStatus(getString(R.string.eggoWaffles), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.milk), getString(R.string.vitaminD), getString(R.string.eggsDairy), getString(R.string.vons), 44);
        //getDbStatusHelper().addNewStatus(getString(R.string.milk), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.eggs), getString(R.string.gradeAAxLarge), getString(R.string.eggsDairy), getString(R.string.vons), 45);
        //getDbStatusHelper().addNewStatus(getString(R.string.eggs), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.honeyYogurt), getString(R.string.greekGods), getString(R.string.eggsDairy), getString(R.string.vons), 46);
        //getDbStatusHelper().addNewStatus(getString(R.string.honeyYogurt), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.saltedButter), getString(R.string.challenge), getString(R.string.eggsDairy), getString(R.string.vons), 47);
        //getDbStatusHelper().addNewStatus(getString(R.string.saltedButter), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.shreddedCheese), getString(R.string.mexicanBlend), getString(R.string.eggsDairy), getString(R.string.vons), 48);
        //getDbStatusHelper().addNewStatus(getString(R.string.shreddedCheese), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.stringCheese), getString(R.string.mozarella), getString(R.string.eggsDairy), getString(R.string.vons), 49);
        //getDbStatusHelper().addNewStatus(getString(R.string.stringCheese), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.bdCheese), getString(R.string.blackDiamond), getString(R.string.eggsDairy), getString(R.string.vons), 50);
        //getDbStatusHelper().addNewStatus(getString(R.string.bdCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.nonStickSpray), getString(R.string.pamOriginal), getString(R.string.miscIngredients), getString(R.string.vons), 51);
        //getDbStatusHelper().addNewStatus(getString(R.string.nonStickSpray), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.parmesanCheese), getString(R.string.kraft), getString(R.string.condiments), getString(R.string.vons), 52);
        //getDbStatusHelper().addNewStatus(getString(R.string.parmesanCheese), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.a1sauce), getString(R.string.original), getString(R.string.condiments), getString(R.string.vons), 53);
        //getDbStatusHelper().addNewStatus(getString(R.string.a1sauce), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.ketchup), getString(R.string.heinz), getString(R.string.condiments), getString(R.string.vons), 54);
        //getDbStatusHelper().addNewStatus(getString(R.string.ketchup), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.mustard), getString(R.string.heinz), getString(R.string.condiments), getString(R.string.vons), 55);
        //getDbStatusHelper().addNewStatus(getString(R.string.mustard), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.pastaSauce), getString(R.string.raguMeat), getString(R.string.condiments), getString(R.string.vons), 56);
        //getDbStatusHelper().addNewStatus(getString(R.string.pastaSauce), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.marinaraSauce), getString(R.string.signatureTraditional), getString(R.string.condiments), getString(R.string.vons), 57);
        //getDbStatusHelper().addNewStatus(getString(R.string.marinaraSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.mapleSyrup), getString(R.string.pearlMilling), getString(R.string.condiments), getString(R.string.vons), 58);
        //getDbStatusHelper().addNewStatus(getString(R.string.mapleSyrup), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.honey), getString(R.string.localHiveClover), getString(R.string.condiments), getString(R.string.vons), 59);
        //getDbStatusHelper().addNewStatus(getString(R.string.honey), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.peanutButter), getString(R.string.skippyCreamy), getString(R.string.condiments), getString(R.string.vons), 60);
        //getDbStatusHelper().addNewStatus(getString(R.string.peanutButter), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.soySauce), getString(R.string.kikoman), getString(R.string.condiments), getString(R.string.vons), 61);
        //getDbStatusHelper().addNewStatus(getString(R.string.soySauce), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.brownSugar), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 62);
        //getDbStatusHelper().addNewStatus(getString(R.string.brownSugar), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.saltAndPepper), getString(R.string.na), getString(R.string.seasonings), getString(R.string.vons), 63);
        //getDbStatusHelper().addNewStatus(getString(R.string.saltAndPepper), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.garlicSalt), getString(R.string.lawrys), getString(R.string.seasonings), getString(R.string.vons), 64);
        //getDbStatusHelper().addNewStatus(getString(R.string.garlicSalt), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.lawrysSeasoningSalt), getString(R.string.lawrys), getString(R.string.seasonings), getString(R.string.vons), 65);
        //getDbStatusHelper().addNewStatus(getString(R.string.lawrysSeasoningSalt), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.ranchDipMix), getString(R.string.lauraScudders), getString(R.string.seasonings), getString(R.string.vons), 66);
        //getDbStatusHelper().addNewStatus(getString(R.string.ranchDipMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vanillaExtract), getString(R.string.signatureSelect), getString(R.string.seasonings), getString(R.string.vons), 67);
        //getDbStatusHelper().addNewStatus(getString(R.string.vanillaExtract), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cinnamonSugar), getString(R.string.mcCormicks), getString(R.string.seasonings), getString(R.string.vons), 68);
        //getDbStatusHelper().addNewStatus(getString(R.string.cinnamonSugar), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sprinkles), getString(R.string.types3), getString(R.string.seasonings), getString(R.string.vons), 69);
        //getDbStatusHelper().addNewStatus(getString(R.string.sprinkles), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sodaBottles2L), getString(R.string.pepsiOrCoke), getString(R.string.drinks), getString(R.string.vons), 70);
        //getDbStatusHelper().addNewStatus(getString(R.string.sodaBottles2L), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hotChocolateMix), getString(R.string.swissMissDark), getString(R.string.drinks), getString(R.string.vons), 71);
        //getDbStatusHelper().addNewStatus(getString(R.string.hotChocolateMix), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.bottledWater), getString(R.string.refreshe40pack), getString(R.string.drinks), getString(R.string.vons), 72);
        //getDbStatusHelper().addNewStatus(getString(R.string.bottledWater), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.beefJerky), getString(R.string.archerTerriyaki), getString(R.string.snacks), getString(R.string.vons), 73);
        //getDbStatusHelper().addNewStatus(getString(R.string.beefJerky), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.peanuts), getString(R.string.honeyRoasted), getString(R.string.snacks), getString(R.string.vons), 74);
        //getDbStatusHelper().addNewStatus(getString(R.string.peanuts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.shellPeanuts), getString(R.string.salted), getString(R.string.snacks), getString(R.string.vons), 75);
        //getDbStatusHelper().addNewStatus(getString(R.string.shellPeanuts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sunflowerSeeds), getString(R.string.salted), getString(R.string.snacks), getString(R.string.vons), 76);
        //getDbStatusHelper().addNewStatus(getString(R.string.sunflowerSeeds), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vinegarChips), getString(R.string.kettle), getString(R.string.snacks), getString(R.string.vons), 77);
        //getDbStatusHelper().addNewStatus(getString(R.string.vinegarChips), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.bbqChips), getString(R.string.kettle), getString(R.string.snacks), getString(R.string.vons), 78);
        //getDbStatusHelper().addNewStatus(getString(R.string.bbqChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.doritos), getString(R.string.coolRanch), getString(R.string.snacks), getString(R.string.vons), 79);
        //getDbStatusHelper().addNewStatus(getString(R.string.doritos), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.laysChips), getString(R.string.classic), getString(R.string.snacks), getString(R.string.vons), 80);
        //getDbStatusHelper().addNewStatus(getString(R.string.laysChips), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.naanCrisps), getString(R.string.stonefire), getString(R.string.snacks), getString(R.string.vons), 81);
        //getDbStatusHelper().addNewStatus(getString(R.string.naanCrisps), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.oreoCakesters), getString(R.string.nabisco), getString(R.string.snacks), getString(R.string.vons), 82);
        //getDbStatusHelper().addNewStatus(getString(R.string.oreoCakesters), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.goldfish), getString(R.string.cheddar), getString(R.string.snacks), getString(R.string.vons), 83);
        //getDbStatusHelper().addNewStatus(getString(R.string.goldfish), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cheezIts), getString(R.string.original), getString(R.string.snacks), getString(R.string.vons), 84);
        //getDbStatusHelper().addNewStatus(getString(R.string.cheezIts), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.famousAmosCookies), getString(R.string.resealableBag), getString(R.string.snacks), getString(R.string.vons), 85);
        //getDbStatusHelper().addNewStatus(getString(R.string.famousAmosCookies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocFudgePirouette), getString(R.string.pepperidgeFarm), getString(R.string.snacks), getString(R.string.vons), 86);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocFudgePirouette), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocChipMuffinMix), getString(R.string.bettyCrocker), getString(R.string.desserts), getString(R.string.vons), 87);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocChipMuffinMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.oreos), getString(R.string.forCrumbs), getString(R.string.desserts), getString(R.string.vons), 88);
        //getDbStatusHelper().addNewStatus(getString(R.string.oreos), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocMaltedCrunchIceCream), getString(R.string.thrifty), getString(R.string.desserts), getString(R.string.vons), 89);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocMaltedCrunchIceCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocTruffleIceCream), getString(R.string.breyers), getString(R.string.desserts), getString(R.string.vons), 90);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocTruffleIceCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.reesesPBCups), getString(R.string.individuallyWrapped), getString(R.string.candy), getString(R.string.vons), 91);
        //getDbStatusHelper().addNewStatus(getString(R.string.reesesPBCups), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hotTamales), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 92);
        //getDbStatusHelper().addNewStatus(getString(R.string.hotTamales), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.miniMAndMs), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 93);
        //getDbStatusHelper().addNewStatus(getString(R.string.miniMAndMs), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tootsieRolls), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 94);
        //getDbStatusHelper().addNewStatus(getString(R.string.tootsieRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.catFoodWet), getString(R.string.fancyFeast), getString(R.string.petSupplies), getString(R.string.vons), 95);
        //getDbStatusHelper().addNewStatus(getString(R.string.catFoodWet), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.purePumpkin), getString(R.string.libbys), getString(R.string.petSupplies), getString(R.string.vons), 96);
        //getDbStatusHelper().addNewStatus(getString(R.string.purePumpkin), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.delectables), getString(R.string.squeezeUp20pack), getString(R.string.petSupplies), getString(R.string.vons), 97);
        //getDbStatusHelper().addNewStatus(getString(R.string.delectables), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.bodyWash), getString(R.string.suaveMandarin), getString(R.string.toiletries), getString(R.string.vons), 98);
        //getDbStatusHelper().addNewStatus(getString(R.string.bodyWash), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.shampoo), getString(R.string.suave2in1), getString(R.string.toiletries), getString(R.string.vons), 99);
        //getDbStatusHelper().addNewStatus(getString(R.string.shampoo), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.deodorant), getString(R.string.oldSpice), getString(R.string.toiletries), getString(R.string.vons), 100);
        //getDbStatusHelper().addNewStatus(getString(R.string.deodorant), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.mouthwash), getString(R.string.crestWhitening), getString(R.string.toiletries), getString(R.string.vons), 101);
        //getDbStatusHelper().addNewStatus(getString(R.string.mouthwash), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.cottonSwabs), getString(R.string.qTips), getString(R.string.toiletries), getString(R.string.vons), 102);
        //getDbStatusHelper().addNewStatus(getString(R.string.cottonSwabs), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.febreezeAirSpray), getString(R.string.heavyDuty), getString(R.string.household), getString(R.string.vons), 103);
        //getDbStatusHelper().addNewStatus(getString(R.string.febreezeAirSpray), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.allPurposeCleaner), getString(R.string.meyersLavender), getString(R.string.household), getString(R.string.vons), 104);
        //getDbStatusHelper().addNewStatus(getString(R.string.allPurposeCleaner), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.laundryDetergent), getString(R.string.woolite), getString(R.string.household), getString(R.string.vons), 105);
        //getDbStatusHelper().addNewStatus(getString(R.string.laundryDetergent), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.laundrySanitizer), getString(R.string.lysol), getString(R.string.household), getString(R.string.vons), 106);
        //getDbStatusHelper().addNewStatus(getString(R.string.laundrySanitizer), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.aluminumFoil), getString(R.string.reynoldsWrap), getString(R.string.household), getString(R.string.vons), 107);
        //getDbStatusHelper().addNewStatus(getString(R.string.aluminumFoil), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.zipLockBagsSmall), getString(R.string.sandwich), getString(R.string.household), getString(R.string.vons), 108);
        //getDbStatusHelper().addNewStatus(getString(R.string.zipLockBagsSmall), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.zipLockBagsLarge), getString(R.string.freezerGallon), getString(R.string.household), getString(R.string.vons), 109);
        //getDbStatusHelper().addNewStatus(getString(R.string.zipLockBagsLarge), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.saranWrap), getString(R.string.plasticWrap), getString(R.string.household), getString(R.string.vons), 110);
        //getDbStatusHelper().addNewStatus(getString(R.string.saranWrap), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.scrubSponges), getString(R.string.nonScratch), getString(R.string.household), getString(R.string.vons), 111);
        //getDbStatusHelper().addNewStatus(getString(R.string.scrubSponges), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.dawnPowerwash), getString(R.string.dishCleaner), getString(R.string.household), getString(R.string.vons), 112);
        //getDbStatusHelper().addNewStatus(getString(R.string.dawnPowerwash), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.dishSoap), getString(R.string.dawnPlatinum), getString(R.string.household), getString(R.string.vons), 113);
        //getDbStatusHelper().addNewStatus(getString(R.string.dishSoap), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.smallFlourTortillas), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 114);
        //getDbStatusHelper().addNewStatus(getString(R.string.smallFlourTortillas), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.largeFlourTortillas), getString(R.string.toDo), getString(R.string.breadGrainsCereal), getString(R.string.vons), 115);
        //getDbStatusHelper().addNewStatus(getString(R.string.largeFlourTortillas), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sourCream), getString(R.string.toDo), getString(R.string.eggsDairy), getString(R.string.vons), 116);
        //getDbStatusHelper().addNewStatus(getString(R.string.sourCream), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.buffaloSauce), getString(R.string.franksWings), getString(R.string.condiments), getString(R.string.vons), 117);
        //getDbStatusHelper().addNewStatus(getString(R.string.buffaloSauce), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tacoSauce), getString(R.string.victoriasMild), getString(R.string.condiments), getString(R.string.vons), 118);
        //getDbStatusHelper().addNewStatus(getString(R.string.tacoSauce), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tacoSeasoning), getString(R.string.any), getString(R.string.seasonings), getString(R.string.vons), 119);
        //getDbStatusHelper().addNewStatus(getString(R.string.tacoSeasoning), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tacoShells), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 120);
        //getDbStatusHelper().addNewStatus(getString(R.string.tacoShells), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tortellini), getString(R.string.barilla3Cheese), getString(R.string.meals), getString(R.string.vons), 121);
        //getDbStatusHelper().addNewStatus(getString(R.string.tortellini), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.garlicBreadsticks), getString(R.string.newYorkBakery), getString(R.string.sides), getString(R.string.vons), 122);
        //getDbStatusHelper().addNewStatus(getString(R.string.garlicBreadsticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.goodAndPlenty), getString(R.string.na), getString(R.string.candy), getString(R.string.vons), 123);
        //getDbStatusHelper().addNewStatus(getString(R.string.goodAndPlenty), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.slicedCheese), getString(R.string.kraftSingles), getString(R.string.eggsDairy), getString(R.string.vons), 124);
        //getDbStatusHelper().addNewStatus(getString(R.string.slicedCheese), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.catTreats), getString(R.string.temptations), getString(R.string.petSupplies), getString(R.string.vons), 125);
        //getDbStatusHelper().addNewStatus(getString(R.string.catTreats), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.saltineCrackers), getString(R.string.premiumOriginal), getString(R.string.miscIngredients), getString(R.string.vons), 126);
        //getDbStatusHelper().addNewStatus(getString(R.string.saltineCrackers), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.semiSweetChocChips), getString(R.string.nestle), getString(R.string.miscIngredients), getString(R.string.vons), 127);
        //getDbStatusHelper().addNewStatus(getString(R.string.semiSweetChocChips), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vegetableOil), getString(R.string.crisco), getString(R.string.miscIngredients), getString(R.string.vons), 128);
        //getDbStatusHelper().addNewStatus(getString(R.string.vegetableOil), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.extraVirginOliveOil), getString(R.string.toDo), getString(R.string.miscIngredients), getString(R.string.vons), 129);
        //getDbStatusHelper().addNewStatus(getString(R.string.extraVirginOliveOil), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.butteredPopcorn), getString(R.string.popSecretMiniBags), getString(R.string.snacks), getString(R.string.vons), 130);
        //getDbStatusHelper().addNewStatus(getString(R.string.butteredPopcorn), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.caramelPopcorn), getString(R.string.cretors), getString(R.string.snacks), getString(R.string.vons), 131);
        //getDbStatusHelper().addNewStatus(getString(R.string.caramelPopcorn), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocCaramelSwirlPopcorn), getString(R.string.cretors), getString(R.string.snacks), getString(R.string.vons), 132);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocCaramelSwirlPopcorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.ritzCrackers), getString(R.string.original), getString(R.string.snacks), getString(R.string.vons), 133);
        //getDbStatusHelper().addNewStatus(getString(R.string.ritzCrackers), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.halfAndHalf), getString(R.string.lucerne), getString(R.string.eggsDairy), getString(R.string.vons), 134);
        //getDbStatusHelper().addNewStatus(getString(R.string.halfAndHalf), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.movieTheaterButter), getString(R.string.kernelSeasons), getString(R.string.miscIngredients), getString(R.string.vons), 135);
        //getDbStatusHelper().addNewStatus(getString(R.string.movieTheaterButter), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.crescentRolls), getString(R.string.signatureSelect), getString(R.string.sides), getString(R.string.vons), 136);
        //getDbStatusHelper().addNewStatus(getString(R.string.crescentRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.beefLitlSmokies), getString(R.string.hillshireFarm), getString(R.string.meat), getString(R.string.vons), 137);
        //getDbStatusHelper().addNewStatus(getString(R.string.beefLitlSmokies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.doubleChocMuffins), getString(R.string.count4), getString(R.string.desserts), getString(R.string.vons), 138);
        //getDbStatusHelper().addNewStatus(getString(R.string.doubleChocMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.vons), 139);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.vons), 53);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.vons), 22);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.vons), 64);
        getDbStoreHelper().setStoreViews(getString(R.string.vons), 139, 53, 22, 64);

        //------------------------------------Smart & Final-----------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.churros), getString(R.string.tioPepesOrHola), getString(R.string.desserts), getString(R.string.smartFinal), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.churros), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.pepperoniSlices), getString(R.string.hormel300Slices), getString(R.string.meat), getString(R.string.smartFinal), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.pepperoniSlices), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.funDipSticks), getString(R.string.na), getString(R.string.candy), getString(R.string.smartFinal), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.funDipSticks), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.orangeTicTacs), getString(R.string.na), getString(R.string.candy), getString(R.string.smartFinal), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.orangeTicTacs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.gingerSnaps), getString(R.string.firstStreet), getString(R.string.snacks), getString(R.string.smartFinal), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.gingerSnaps), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.smartFinal), 5);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.smartFinal), 2);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.smartFinal), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.smartFinal), 3);
        getDbStoreHelper().setStoreViews(getString(R.string.smartFinal), 5, 2, 0, 3);

        //------------------------------------Costco------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.sodaCans), getString(R.string.pepsiOrCoke), getString(R.string.drinks), getString(R.string.costco), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.sodaCans), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.kittyLitter), getString(R.string.scoopAwayComplete), getString(R.string.petSupplies), getString(R.string.costco), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.kittyLitter), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chickenBroth), getString(R.string.kirklandOrganic), getString(R.string.petSupplies), getString(R.string.costco), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.chickenBroth), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.mashedPotatoes), getString(R.string.mainStBistro), getString(R.string.petSupplies), getString(R.string.costco), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.mashedPotatoes), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.freshpet), getString(R.string.chickenRecipe), getString(R.string.petSupplies), getString(R.string.costco), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.freshpet), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.oreoMuffins), getString(R.string.pack12), getString(R.string.desserts), getString(R.string.costco), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.oreoMuffins), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.crispyBuffaloWings), getString(R.string.fosterFarms), getString(R.string.meals), getString(R.string.costco), 6);
        //getDbStatusHelper().addNewStatus(getString(R.string.crispyBuffaloWings), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.paperTowels), getString(R.string.kirklandPremium), getString(R.string.household), getString(R.string.costco), 7);
        //getDbStatusHelper().addNewStatus(getString(R.string.paperTowels), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.toiletPaper), getString(R.string.kirklandUltraSoft), getString(R.string.household), getString(R.string.costco), 8);
        //getDbStatusHelper().addNewStatus(getString(R.string.toiletPaper), getString(R.string.instock), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.costco), 9);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.costco), 6);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.costco), 1);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.costco), 2);
        getDbStoreHelper().setStoreViews(getString(R.string.costco), 9, 6, 1, 2);

        //------------------------------------Walmart-----------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.darkChocolateCaramelSquares), getString(R.string.ghiradelli), getString(R.string.candy), getString(R.string.walmart), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.darkChocolateCaramelSquares), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.dishwashingBrush), getString(R.string.greatValue), getString(R.string.household), getString(R.string.walmart), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.dishwashingBrush), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.smallTrashBags), getString(R.string.gallon13), getString(R.string.household), getString(R.string.walmart), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.smallTrashBags), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.largeTrashBags), getString(R.string.gallon33), getString(R.string.household), getString(R.string.walmart), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.largeTrashBags), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.compactorBags), getString(R.string.gallon18), getString(R.string.household), getString(R.string.walmart), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.compactorBags), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.intenseDarkChocolate), getString(R.string.ghiradelli), getString(R.string.candy), getString(R.string.walmart), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.intenseDarkChocolate), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.walmart), 6);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.walmart), 3);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.walmart), 2);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.walmart), 1);
        getDbStoreHelper().setStoreViews(getString(R.string.walmart), 6, 3, 2, 1);

        //------------------------------------Amazon------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.muddyBuddies), getString(R.string.brownieSupreme), getString(R.string.snacks), getString(R.string.amazon), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.muddyBuddies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.gingerbreadCookieMix), getString(R.string.bettyCrocker), getString(R.string.desserts), getString(R.string.amazon), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.gingerbreadCookieMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.jellyBeans), getString(R.string.sizzlingCinnamon), getString(R.string.candy), getString(R.string.amazon), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.jellyBeans), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.orangePez), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.orangePez), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vanillaTaffy), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.vanillaTaffy), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vanillaTootsieRolls), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.vanillaTootsieRolls), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.poopBags), getString(R.string.amazonBasics), getString(R.string.petSupplies), getString(R.string.amazon), 6);
        //getDbStatusHelper().addNewStatus(getString(R.string.poopBags), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.nitrileGloves), getString(R.string.gmg100Pack), getString(R.string.petSupplies), getString(R.string.amazon), 7);
        //getDbStatusHelper().addNewStatus(getString(R.string.nitrileGloves), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.toothpaste), getString(R.string.ultrabrite), getString(R.string.toiletries), getString(R.string.amazon), 8);
        //getDbStatusHelper().addNewStatus(getString(R.string.toothpaste), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.floss), getString(R.string.reachMintWaxed), getString(R.string.toiletries), getString(R.string.amazon), 9);
        //getDbStatusHelper().addNewStatus(getString(R.string.floss), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.shavingCream), getString(R.string.sandalwood), getString(R.string.toiletries), getString(R.string.amazon), 10);
        //getDbStatusHelper().addNewStatus(getString(R.string.shavingCream), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.shavingRazors), getString(R.string.gilletteProGlide), getString(R.string.toiletries), getString(R.string.amazon), 11);
        //getDbStatusHelper().addNewStatus(getString(R.string.shavingRazors), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sunscreen), getString(R.string.hawaiianTropicSheer50spf), getString(R.string.toiletries), getString(R.string.amazon), 12);
        //getDbStatusHelper().addNewStatus(getString(R.string.sunscreen), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.petStainCleaner), getString(R.string.roccoAndRoxie), getString(R.string.household), getString(R.string.amazon), 13);
        //getDbStatusHelper().addNewStatus(getString(R.string.petStainCleaner), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.nightLightBulbs), getString(R.string.c7E12), getString(R.string.household), getString(R.string.amazon), 14);
        //getDbStatusHelper().addNewStatus(getString(R.string.nightLightBulbs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.fortuneCookies), getString(R.string.toDo), getString(R.string.snacks), getString(R.string.amazon), 15);
        //getDbStatusHelper().addNewStatus(getString(R.string.fortuneCookies), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.communionWafers), getString(R.string.cavanaghAltarBread), getString(R.string.snacks), getString(R.string.amazon), 16);
        //getDbStatusHelper().addNewStatus(getString(R.string.communionWafers), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.sixlets), getString(R.string.na), getString(R.string.candy), getString(R.string.amazon), 17);
        //getDbStatusHelper().addNewStatus(getString(R.string.sixlets), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.littleTreesAirFresheners), getString(R.string.trueNorth), getString(R.string.household), getString(R.string.amazon), 18);
        //getDbStatusHelper().addNewStatus(getString(R.string.littleTreesAirFresheners), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.tripleOmega), getString(R.string.natureMade), getString(R.string.supplements), getString(R.string.amazon), 19);
        //getDbStatusHelper().addNewStatus(getString(R.string.tripleOmega), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.multivitamin), getString(R.string.oneADayMens), getString(R.string.supplements), getString(R.string.amazon), 20);
        //getDbStatusHelper().addNewStatus(getString(R.string.multivitamin), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vitaminC), getString(R.string.amazonElements1000mg), getString(R.string.supplements), getString(R.string.amazon), 21);
        //getDbStatusHelper().addNewStatus(getString(R.string.vitaminC), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.magnesium), getString(R.string.natureMade400mg), getString(R.string.supplements), getString(R.string.amazon), 22);
        //getDbStatusHelper().addNewStatus(getString(R.string.magnesium), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.zinc), getString(R.string.sandhuHerbals50mg), getString(R.string.supplements), getString(R.string.amazon), 23);
        //getDbStatusHelper().addNewStatus(getString(R.string.zinc), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.calcium), getString(R.string.naturesTruth1200mg), getString(R.string.supplements), getString(R.string.amazon), 24);
        //getDbStatusHelper().addNewStatus(getString(R.string.calcium), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.biotin), getString(R.string.natrol10000mcg), getString(R.string.supplements), getString(R.string.amazon), 25);
        //getDbStatusHelper().addNewStatus(getString(R.string.biotin), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.vitaminD3), getString(R.string.natureMade5000IU), getString(R.string.supplements), getString(R.string.amazon), 26);
        //getDbStatusHelper().addNewStatus(getString(R.string.vitaminD3), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hyaluronicAcid), getString(R.string.horbaach1000mg), getString(R.string.supplements), getString(R.string.amazon), 27);
        //getDbStatusHelper().addNewStatus(getString(R.string.hyaluronicAcid), getString(R.string.instock), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.amazon), 28);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.amazon), 18);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.amazon), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.amazon), 10);
        getDbStoreHelper().setStoreViews(getString(R.string.amazon), 28, 18, 0, 10);

        //------------------------------------Stater Bros-------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.beefNoodles), getString(R.string.yakisoba), getString(R.string.soups), getString(R.string.staterBros), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.beefNoodles), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocChipCookieMix), getString(R.string.bettyCrockerGlutenFree), getString(R.string.desserts), getString(R.string.staterBros), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocChipCookieMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocMaltMix), getString(R.string.nestle), getString(R.string.desserts), getString(R.string.staterBros), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocMaltMix), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.chocFudgePudding), getString(R.string.snackPack), getString(R.string.snacks), getString(R.string.staterBros), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocFudgePudding), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.dryerSheets), getString(R.string.simplyDoneFreshLinen), getString(R.string.household), getString(R.string.staterBros), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.dryerSheets), getString(R.string.instock), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.staterBros), 5);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.staterBros), 1);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.staterBros), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.staterBros), 4);
        getDbStoreHelper().setStoreViews(getString(R.string.staterBros), 5, 1, 0, 4);

        //------------------------------------Trader Joe's-------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.holdTheCone), getString(R.string.chocolate), getString(R.string.desserts), getString(R.string.traderJoes), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.holdTheCone), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.darkChocCaramels), getString(R.string.traderJoes), getString(R.string.candy), getString(R.string.traderJoes), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.darkChocCaramels), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.darkChocPeanutButterCups), getString(R.string.traderJoes), getString(R.string.candy), getString(R.string.traderJoes), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.darkChocPeanutButterCups), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.traderJoes), 3);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.traderJoes), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.traderJoes), 1);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.traderJoes), 2);
        getDbStoreHelper().setStoreViews(getString(R.string.traderJoes), 3, 0, 1, 2);

        //------------------------------------CVS---------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.darkChocolatePretzels), getString(R.string.flipz), getString(R.string.snacks), getString(R.string.cvs), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.darkChocolatePretzels), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.candyCorn), getString(R.string.brachs), getString(R.string.candy), getString(R.string.cvs), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.candyCorn), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.rubbingAlcohol), getString(R.string.isopropyl), getString(R.string.household), getString(R.string.cvs), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.rubbingAlcohol), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.hydrogenPeroxide), getString(R.string.na), getString(R.string.household), getString(R.string.cvs), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.hydrogenPeroxide), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.moisturizingLotion), getString(R.string.cvsHealthHyaluronicAcid), getString(R.string.toiletries), getString(R.string.cvs), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.moisturizingLotion), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.packagingTape), getString(R.string.scotchHeavyDuty), getString(R.string.household), getString(R.string.cvs), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.packagingTape), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.faceSunscreen), getString(R.string.aveenoProtectHydrate), getString(R.string.toiletries), getString(R.string.cvs), 6);
        //getDbStatusHelper().addNewStatus(getString(R.string.faceSunscreen), getString(R.string.paused), getString(R.string.unchecked));


        getStoreData().getStoreViewAllMap().put(getString(R.string.cvs), 7);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.cvs), 5);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.cvs), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.cvs), 2);
        getDbStoreHelper().setStoreViews(getString(R.string.cvs), 7, 5, 0, 2);

        //------------------------------------Dollar Tree-------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.snoCaps), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.snoCaps), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.smarties), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.smarties), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.redHots), getString(R.string.na), getString(R.string.candy), getString(R.string.dollarTree), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.redHots), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.handSoap), getString(R.string.lavenderAndChamomile), getString(R.string.toiletries), getString(R.string.dollarTree), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.handSoap), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.ramenNoodles), getString(R.string.nissin), getString(R.string.soups), getString(R.string.dollarTree), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.ramenNoodles), getString(R.string.instock), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.dollarTree), 5);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.dollarTree), 4);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.dollarTree), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.dollarTree), 1);
        getDbStoreHelper().setStoreViews(getString(R.string.dollarTree), 5, 4, 0, 1);

        //------------------------------------Ralphs------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.clarifiedButter), getString(R.string.challenge), getString(R.string.eggsDairy), getString(R.string.ralphs), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.clarifiedButter), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.ralphs), 1);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.ralphs), 0);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.ralphs), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.ralphs), 1);
        getDbStoreHelper().setStoreViews(getString(R.string.ralphs), 1, 0, 0, 1);

        //------------------------------------Target------------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.chocolateSyrup), getString(R.string.ghirardelliOrHersheys), getString(R.string.condiments), getString(R.string.target), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.chocolateSyrup), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.caramelSyrup), getString(R.string.ghirardelliOrHersheys), getString(R.string.condiments), getString(R.string.target), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.caramelSyrup), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.darkChocMandMs), getString(R.string.na), getString(R.string.candy), getString(R.string.target), 2);
        //getDbStatusHelper().addNewStatus(getString(R.string.darkChocMandMs), getString(R.string.paused), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.seaSaltCaramels), getString(R.string.favoriteDay), getString(R.string.candy), getString(R.string.target), 3);
        //getDbStatusHelper().addNewStatus(getString(R.string.seaSaltCaramels), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.oreoPieMix), getString(R.string.noBakeDessert), getString(R.string.desserts), getString(R.string.target), 4);
        //getDbStatusHelper().addNewStatus(getString(R.string.oreoPieMix), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.pieFilling), getString(R.string.jelloChocFudge), getString(R.string.desserts), getString(R.string.target), 5);
        //getDbStatusHelper().addNewStatus(getString(R.string.pieFilling), getString(R.string.paused), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.target), 6);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.target), 4);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.target), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.target), 2);
        getDbStoreHelper().setStoreViews(getString(R.string.target), 6, 4, 0, 2);

        //------------------------------------Pet Supplies Plus-------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.catFoodDry), getString(R.string.purinaProPlan), getString(R.string.petSupplies), getString(R.string.petSuppliesPlus), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.catFoodDry), getString(R.string.instock), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.petSuppliesPlus), 1);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.petSuppliesPlus), 1);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.petSuppliesPlus), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.petSuppliesPlus), 0);
        getDbStoreHelper().setStoreViews(getString(R.string.petSuppliesPlus), 1, 1, 0, 0);

        //------------------------------------Sprouts-------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.barSoap), getString(R.string.zumBarSeaSalt), getString(R.string.toiletries), getString(R.string.sprouts), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.barSoap), getString(R.string.needed), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.toothbrushHeads), getString(R.string.radiusSoft), getString(R.string.toiletries), getString(R.string.sprouts), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.toothbrushHeads), getString(R.string.instock), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.sprouts), 2);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.sprouts), 1);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.sprouts), 1);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.sprouts), 0);
        getDbStoreHelper().setStoreViews(getString(R.string.sprouts), 2, 1, 1, 0);

        //------------------------------------Sam's Club--------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.quickSteak), getString(R.string.garys), getString(R.string.meat), getString(R.string.samsClub), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.quickSteak), getString(R.string.instock), getString(R.string.unchecked));

        getDbItemHelper().addNewItemByStore(getString(R.string.paperPlates), getString(R.string.toDo), getString(R.string.household), getString(R.string.samsClub), 1);
        //getDbStatusHelper().addNewStatus(getString(R.string.paperPlates), getString(R.string.instock), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.samsClub), 2);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.samsClub), 2);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.samsClub), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.samsClub), 0);
        getDbStoreHelper().setStoreViews(getString(R.string.samsClub), 2, 2, 0, 0);

        //---------------------------------------Staples--------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.multipurposePaper), getString(R.string.truRed), getString(R.string.household), getString(R.string.staples), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.multipurposePaper), getString(R.string.instock), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.staples), 1);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.staples), 1);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.staples), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.staples), 0);
        getDbStoreHelper().setStoreViews(getString(R.string.staples), 1, 1, 0, 0);

        //---------------------------------------Woodranch--------------------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.woodranchBBQSauce), getString(R.string.pint1), getString(R.string.condiments), getString(R.string.woodranch), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.woodranchBBQSauce), getString(R.string.instock), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.woodranch), 1);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.woodranch), 1);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.woodranch), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.woodranch), 0);
        getDbStoreHelper().setStoreViews(getString(R.string.woodranch), 1, 1, 0, 0);

        //------------------------------------Yorba Linda Feed Store--------------------------------

        getDbItemHelper().addNewItemByStore(getString(R.string.dogFoodDry), getString(R.string.canidaeAllLifeStages), getString(R.string.petSupplies), getString(R.string.yorbaLindaFeedStore), 0);
        //getDbStatusHelper().addNewStatus(getString(R.string.dogFoodDry), getString(R.string.instock), getString(R.string.unchecked));

        getStoreData().getStoreViewAllMap().put(getString(R.string.yorbaLindaFeedStore), 1);
        getStoreData().getStoreViewInStockMap().put(getString(R.string.yorbaLindaFeedStore), 1);
        getStoreData().getStoreViewNeededMap().put(getString(R.string.yorbaLindaFeedStore), 0);
        getStoreData().getStoreViewPausedMap().put(getString(R.string.yorbaLindaFeedStore), 0);
        getDbStoreHelper().setStoreViews(getString(R.string.yorbaLindaFeedStore), 1, 1, 0, 0);

        //------------------------------------------------------------------------------------------

        // total store items = 222

    }

//------------------------------------------------------------------------------------------------//

}