package ryan.android.shopping;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FullInventory extends Fragment {

    private View view;
    private View rootView;
    private Shopping shopping;
    private ItemData itemData;
    private StatusData statusData;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBStatusHelper dbStatusHelper;
    private DBCategoryHelper dbCategoryHelper;
    private DBStoreHelper dbStoreHelper;

    private String currentBottomMenu;
    private static final String MENU_ITEM = ShoppingApp.getStringRes(R.string.cvMenuItem);
    private static final String MENU_CATEGORY = ShoppingApp.getStringRes(R.string.cvMenuCategory);
    private static final String MENU_STORE = ShoppingApp.getStringRes(R.string.cvMenuStore);
    private static final String MENU_NONE = ShoppingApp.getStringRes(R.string.cvMenuNone);

    private TextView editDataBreak;
    private Button addPopup;
    private Button editPopup;
    private Button removePopup;
    private Button reorderPopup;
    private Button addRemoveCategory;
    private Button addEditItem;
    private Button addRemoveStore;

    private boolean menuOptionsVisible;
    private boolean searchBoxVisible;
    private boolean keyboardVisible;
    private boolean editControlsExpanded;

    private TextView fullInventoryTitle;
    private TextView fullInventoryOptionsBackground;
    private String lastMainTitle;
    private TextView addNewItemButton;
    private TextView searchButton;
    private LinearLayout searchPopup;
    private EditText searchBox;
    private TextView clearSearchButton;
    private TextView refreshButton;
    private TextView fullInventoryEditButton;

    private RecyclerView fullInventoryRecyclerView;
    private RecyclerView searchInventoryRecyclerView;
    private FullInventoryRVA fullInventoryAdapter;
    private SearchInventoryRVA searchInventoryAdapter;

    private Button viewAll;
    private Button viewInStock;
    private Button viewNeeded;
    private Button viewPaused;
    private Button expandContractItems;
    private Button expandContractCategories;
    private Button expandContractStores;
    private Button sortAlphabetical;
    private Button sortByStore;
    private Button sortByCategory;
    private Button homeScreen;

    public FullInventory() {}

    private FullInventory getThis() {
        return this;
    }

    public View getView() {
        return view;
    }

    private void setView(View view) {
        getThis().view = view;
    }

    private View getRootView() {
        return rootView;
    }

    private void setRootView(View rootView) {
        getThis().rootView = rootView;
    }

    private Shopping getShopping() {
        return shopping;
    }

    private void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    private ItemData getItemData() {
        return itemData;
    }

    private void setItemData(ItemData itemData) {
        getThis().itemData = itemData;
    }

    private StatusData getStatusData() {
        return statusData;
    }

    private void setStatusData(StatusData statusData) {
        getThis().statusData = statusData;
    }

    private CategoryData getCategoryData() {
        return categoryData;
    }

    private void setCategoryData(CategoryData categoryData) {
        getThis().categoryData = categoryData;
    }

    private StoreData getStoreData() {
        return storeData;
    }

    private void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
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

    private String getCurrentBottomMenu() {
        return currentBottomMenu;
    }

    private void setCurrentBottomMenu(String currentBottomMenu) {
        getThis().currentBottomMenu = currentBottomMenu;
    }

    private TextView getEditDataBreak() {
        return editDataBreak;
    }

    private void setEditDataBreak(TextView editDataBreak) {
        getThis().editDataBreak = editDataBreak;
    }

    private Button getAddPopup() {
        return addPopup;
    }

    private void setAddPopup(Button addPopup) {
        getThis().addPopup = addPopup;
    }

    private Button getEditPopup() {
        return editPopup;
    }

    private void setEditPopup(Button editPopup) {
        getThis().editPopup = editPopup;
    }

    private Button getRemovePopup() {
        return removePopup;
    }

    private void setRemovePopup(Button removePopup) {
        getThis().removePopup = removePopup;
    }

    private Button getReorderPopup() {
        return reorderPopup;
    }

    private void setReorderPopup(Button reorderPopup) {
        getThis().reorderPopup = reorderPopup;
    }

    private Button getAddRemoveCategory() {
        return addRemoveCategory;
    }

    private void setAddRemoveCategory(Button addRemoveCategory) {
        getThis().addRemoveCategory = addRemoveCategory;
    }

    private Button getAddEditItem() {
        return addEditItem;
    }

    private void setAddEditItem(Button addEditItem) {
        getThis().addEditItem = addEditItem;
    }

    private Button getAddRemoveStore() {
        return addRemoveStore;
    }

    private void setAddRemoveStore(Button addRemoveStore) {
        getThis().addRemoveStore = addRemoveStore;
    }

    private boolean menuOptionsVisible() {
        return menuOptionsVisible;
    }

    private void setMenuOptionsVisible(boolean menuOptionsVisible) {
        getThis().menuOptionsVisible = menuOptionsVisible;
    }

    private boolean searchBoxVisible() {
        return searchBoxVisible;
    }

    private void setSearchBoxVisible(boolean searchBoxVisible) {
        getThis().searchBoxVisible = searchBoxVisible;
    }

    private boolean keyboardVisible() {
        return keyboardVisible;
    }

    private void setKeyboardVisible(boolean keyboardVisible) {
        getThis().keyboardVisible = keyboardVisible;
    }

    private boolean editControlsExpanded() {
        return editControlsExpanded;
    }

    private void setEditControlsExpanded(boolean editControlsExpanded) {
        getThis().editControlsExpanded = editControlsExpanded;
    }

    private TextView getFullInventoryTitle() {
        return fullInventoryTitle;
    }

    private void setFullInventoryTitle(TextView fullInventoryTitle) {
        getThis().fullInventoryTitle = fullInventoryTitle;
    }

    private TextView getFullInventoryOptionsBackground() {
        return fullInventoryOptionsBackground;
    }

    private void setFullInventoryOptionsBackground(TextView fullInventoryOptionsBackground) {
        getThis().fullInventoryOptionsBackground = fullInventoryOptionsBackground;
    }

    private String getLastMainTitle() {
        return lastMainTitle;
    }

    private void setLastMainTitle(String lastMainTitle) {
        getThis().lastMainTitle = lastMainTitle;
    }

    private TextView getAddNewItemButton() {
        return addNewItemButton;
    }

    private void setAddNewItemButton(TextView addNewItemButton) {
        getThis().addNewItemButton = addNewItemButton;
    }

    private TextView getSearchButton() {
        return searchButton;
    }

    private void setSearchButton(TextView searchButton) {
        getThis().searchButton = searchButton;
    }

    private LinearLayout getSearchPopup() {
        return searchPopup;
    }

    private void setSearchPopup(LinearLayout searchPopup) {
        getThis().searchPopup = searchPopup;
    }

    private EditText getSearchBox() {
        return searchBox;
    }

    private void setSearchBox(EditText searchBox) {
        getThis().searchBox = searchBox;
    }

    private TextView getClearSearchButton() {
        return clearSearchButton;
    }

    private void setClearSearchButton(TextView clearSearchButton) {
        getThis().clearSearchButton = clearSearchButton;
    }

    private TextView getRefreshButton() {
        return refreshButton;
    }

    private void setRefreshButton(TextView refreshButton) {
        getThis().refreshButton = refreshButton;
    }

    private TextView getFullInventoryEditButton() {
        return fullInventoryEditButton;
    }

    private void setFullInventoryEditButton(TextView fullInventoryEditButton) {
        getThis().fullInventoryEditButton = fullInventoryEditButton;
    }

    private RecyclerView getFullInventoryRecyclerView() {
        return fullInventoryRecyclerView;
    }

    private void setFullInventoryRecyclerView(RecyclerView fullInventoryRecyclerView) {
        getThis().fullInventoryRecyclerView = fullInventoryRecyclerView;
    }

    private RecyclerView getSearchInventoryRecyclerView() {
        return searchInventoryRecyclerView;
    }

    private void setSearchInventoryRecyclerView(RecyclerView searchInventoryRecyclerView) {
        getThis().searchInventoryRecyclerView = searchInventoryRecyclerView;
    }

    private FullInventoryRVA getFullInventoryAdapter() {
        return fullInventoryAdapter;
    }

    private void setFullInventoryAdapter(FullInventoryRVA fullInventoryAdapter) {
        getThis().fullInventoryAdapter = fullInventoryAdapter;
    }

    private SearchInventoryRVA getSearchInventoryAdapter() {
        return searchInventoryAdapter;
    }

    private void setSearchInventoryAdapter(SearchInventoryRVA searchInventoryAdapter) {
        getThis().searchInventoryAdapter = searchInventoryAdapter;
    }

    private Button getViewAll() {
        return viewAll;
    }

    private void setViewAll(Button viewAll) {
        getThis().viewAll = viewAll;
    }

    private Button getViewInStock() {
        return viewInStock;
    }

    private void setViewInStock(Button viewInStock) {
        getThis().viewInStock = viewInStock;
    }

    private Button getViewNeeded() {
        return viewNeeded;
    }

    private void setViewNeeded(Button viewNeeded) {
        getThis().viewNeeded = viewNeeded;
    }

    private Button getViewPaused() {
        return viewPaused;
    }

    private void setViewPaused(Button viewPaused) {
        getThis().viewPaused = viewPaused;
    }

    private Button getExpandContractItems() {
        return expandContractItems;
    }

    private void setExpandContractItems(Button expandContractItems) {
        getThis().expandContractItems = expandContractItems;
    }

    private Button getExpandContractCategories() {
        return expandContractCategories;
    }

    private void setExpandContractCategories(Button expandContractCategories) {
        getThis().expandContractCategories = expandContractCategories;
    }

    private Button getExpandContractStores() {
        return expandContractStores;
    }

    private void setExpandContractStores(Button expandContractStores) {
        getThis().expandContractStores = expandContractStores;
    }

    private Button getSortAlphabetical() {
        return sortAlphabetical;
    }

    private void setSortAlphabetical(Button sortAlphabetical) {
        getThis().sortAlphabetical = sortAlphabetical;
    }

    private Button getSortByStore() {
        return sortByStore;
    }

    private void setSortByStore(Button sortByStore) {
        getThis().sortByStore = sortByStore;
    }

    private Button getSortByCategory() {
        return sortByCategory;
    }

    private void setSortByCategory(Button sortByCategory) {
        getThis().sortByCategory = sortByCategory;
    }

    private Button getHomeScreen() {
        return homeScreen;
    }

    private void setHomeScreen(Button homeScreen) {
        getThis().homeScreen = homeScreen;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();
        setView(inflater.inflate(R.layout.full_inventory, container, false));
        setRootView(getView().getRootView());
        getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout() {
                Rect r = new Rect();
                getRootView().getWindowVisibleDisplayFrame(r);
                int screenHeight = getRootView().getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    if (!keyboardVisible()) setKeyboardVisible(true);
                }
                else {
                    if (keyboardVisible()) setKeyboardVisible(false);
                }
            }
        });

        setDbStatusHelper(new DBStatusHelper(getActivity()));
        setDbCategoryHelper(new DBCategoryHelper(getActivity()));
        setDbStoreHelper(new DBStoreHelper(getActivity()));

        setShopping((Shopping) getActivity());
        setItemData(getShopping().getItemData());
        setStatusData(getShopping().getStatusData());
        setCategoryData(getShopping().getCategoryData());
        setStoreData(getShopping().getStoreData());
        getItemData().updateStatuses(getStatusData());

        setFullInventoryRecyclerView((RecyclerView) getView().findViewById(R.id.fullInventoryRecyclerView));
        getFullInventoryRecyclerView().setHasFixedSize(false);
        getFullInventoryRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setFullInventoryAdapter(new FullInventoryRVA(getView(), getContext(), getShopping(),  getItemData(), getCategoryData(), getStoreData(), getDbStatusHelper(), getDbStoreHelper(), getDbCategoryHelper()));
        getFullInventoryRecyclerView().setAdapter(getFullInventoryAdapter());
        getFullInventoryRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getFullInventoryViewState());

        SearchAlgorithm searchAlgorithm = getShopping().getSearchAlgorithm();
        setSearchInventoryRecyclerView((RecyclerView) getView().findViewById(R.id.searchInventoryRecyclerView));
        getSearchInventoryRecyclerView().setHasFixedSize(false);
        getSearchInventoryRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setSearchInventoryAdapter(new SearchInventoryRVA(getView(), getContext(), getShopping(), searchAlgorithm));
        getSearchInventoryRecyclerView().setAdapter(getSearchInventoryAdapter());
        getSearchInventoryRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getSearchInventoryViewState());

        setAddRemoveCategory((Button) getView().findViewById(R.id.addRemoveCategory));
        setAddEditItem((Button) getView().findViewById(R.id.addEditItem));
        setAddRemoveStore((Button) getView().findViewById(R.id.addRemoveStore));
        setEditDataBreak((Button) getView().findViewById(R.id.editDataBreak));
        setAddPopup((Button) getView().findViewById(R.id.addPopup));
        setEditPopup((Button) getView().findViewById(R.id.editPopup));
        setRemovePopup((Button) getView().findViewById(R.id.removePopup));
        setReorderPopup((Button) getView().findViewById(R.id.reorderPopup));

        setMenuOptionsVisible(false);
        setSearchBoxVisible(false);
        setKeyboardVisible(false);
        setEditControlsExpanded(false);
        setCurrentBottomMenu(MENU_NONE);

        setFullInventoryTitle((TextView) getView().findViewById(R.id.fullInventoryTitle));
        if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_CATEGORY)) {
            getFullInventoryTitle().setText(getString(R.string.byCategoryAll));
        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_STORE)) {
            getFullInventoryTitle().setText(getString(R.string.byStoreAll));
        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {
            getFullInventoryTitle().setText(getString(R.string.alphabeticalAll));
        }

        setAddNewItemButton((TextView) getView().findViewById(R.id.addNewItemButton));
        setSearchButton((TextView) getView().findViewById(R.id.searchButton));
        setSearchBox((EditText) getView().findViewById(R.id.searchBox));
        setSearchPopup((LinearLayout) getView().findViewById(R.id.searchPopup));
        setClearSearchButton((TextView) getView().findViewById(R.id.clearSearchButton));
        setRefreshButton((TextView) getView().findViewById(R.id.refreshButton));
        setFullInventoryEditButton((TextView) getView().findViewById(R.id.fullInventoryEditButton));

        setFullInventoryOptionsBackground((TextView) getView().findViewById(R.id.fullInventoryOptionsBackground));
        setViewAll((Button) getView().findViewById(R.id.viewAll));
        setViewInStock((Button) getView().findViewById(R.id.viewInStock));
        setViewNeeded((Button) getView().findViewById(R.id.viewNeeded));
        setViewPaused((Button) getView().findViewById(R.id.viewPaused));
        setExpandContractItems((Button) getView().findViewById(R.id.expandContractItems));
        setExpandContractCategories((Button) getView().findViewById(R.id.expandContractCategories));
        setExpandContractStores((Button) getView().findViewById(R.id.expandContractStores));
        setSortAlphabetical((Button) getView().findViewById(R.id.sortAlphabetical));
        setSortByCategory((Button) getView().findViewById(R.id.sortByCategory));
        setSortByStore((Button) getView().findViewById(R.id.sortByStore));
        setHomeScreen((Button) getView().findViewById(R.id.homeScreen));

        getAddRemoveCategory().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getCurrentBottomMenu().equals(MENU_NONE)) {
                    getEditDataBreak().setText(getString(R.string.category));
                    getEditDataBreak().setVisibility(View.VISIBLE);
                    getAddPopup().setVisibility(View.VISIBLE);
                    getEditPopup().setVisibility(View.VISIBLE);
                    getRemovePopup().setVisibility(View.VISIBLE);
                    getReorderPopup().setVisibility(View.VISIBLE);
                    setCurrentBottomMenu(MENU_CATEGORY);
                    setEditControlsExpanded(true);

                    ViewGroup.LayoutParams recyclerViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    int height;
                    if (searchBoxVisible()) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    recyclerViewParams.height = height;
                    getFullInventoryRecyclerView().setLayoutParams(recyclerViewParams);

                } else if (getCurrentBottomMenu().equals(MENU_CATEGORY)) {
                    getEditDataBreak().setVisibility(View.GONE);
                    getAddPopup().setVisibility(View.GONE);
                    getEditPopup().setVisibility(View.GONE);
                    getRemovePopup().setVisibility(View.GONE);
                    getReorderPopup().setVisibility(View.GONE);
                    setCurrentBottomMenu(MENU_NONE);
                    setEditControlsExpanded(false);

                    ViewGroup.LayoutParams recyclerViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    int height;
                    if (searchBoxVisible()) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    } else
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                    recyclerViewParams.height = height;
                    getFullInventoryRecyclerView().setLayoutParams(recyclerViewParams);
                } else {
                    getEditDataBreak().setText(getString(R.string.category));
                    setCurrentBottomMenu(MENU_CATEGORY);
                }
            }
        });

        getAddEditItem().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getCurrentBottomMenu().equals(MENU_NONE)) {
                    getEditDataBreak().setText(getString(R.string.item));
                    getEditDataBreak().setVisibility(View.VISIBLE);
                    getAddPopup().setVisibility(View.VISIBLE);
                    getEditPopup().setVisibility(View.VISIBLE);
                    getRemovePopup().setVisibility(View.VISIBLE);
                    getReorderPopup().setVisibility(View.VISIBLE);
                    setCurrentBottomMenu(MENU_ITEM);
                    setEditControlsExpanded(true);

                    ViewGroup.LayoutParams recyclerViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    int height;
                    if (searchBoxVisible()) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    recyclerViewParams.height = height;
                    getFullInventoryRecyclerView().setLayoutParams(recyclerViewParams);

                } else if (getCurrentBottomMenu().equals(MENU_ITEM)) {
                    getEditDataBreak().setVisibility(View.GONE);
                    getAddPopup().setVisibility(View.GONE);
                    getEditPopup().setVisibility(View.GONE);
                    getRemovePopup().setVisibility(View.GONE);
                    getReorderPopup().setVisibility(View.GONE);
                    setCurrentBottomMenu(MENU_NONE);
                    setEditControlsExpanded(false);

                    ViewGroup.LayoutParams recyclerViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    int height;
                    if (searchBoxVisible()) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    } else
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                    recyclerViewParams.height = height;
                    getFullInventoryRecyclerView().setLayoutParams(recyclerViewParams);
                } else {
                    getEditDataBreak().setText(getString(R.string.item));
                    setCurrentBottomMenu(MENU_ITEM);
                }
            }
        });

        getAddRemoveStore().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getCurrentBottomMenu().equals(MENU_NONE)) {
                    getEditDataBreak().setText(getString(R.string.store));
                    getEditDataBreak().setVisibility(View.VISIBLE);
                    getAddPopup().setVisibility(View.VISIBLE);
                    getEditPopup().setVisibility(View.VISIBLE);
                    getRemovePopup().setVisibility(View.VISIBLE);
                    getReorderPopup().setVisibility(View.VISIBLE);
                    setCurrentBottomMenu(MENU_STORE);
                    setEditControlsExpanded(true);

                    ViewGroup.LayoutParams recyclerViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    int height;
                    if (searchBoxVisible()) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    recyclerViewParams.height = height;
                    getFullInventoryRecyclerView().setLayoutParams(recyclerViewParams);

                } else if (getCurrentBottomMenu().equals(MENU_STORE)) {
                    getEditDataBreak().setVisibility(View.GONE);
                    getAddPopup().setVisibility(View.GONE);
                    getEditPopup().setVisibility(View.GONE);
                    getRemovePopup().setVisibility(View.GONE);
                    getReorderPopup().setVisibility(View.GONE);
                    setCurrentBottomMenu(MENU_NONE);
                    setEditControlsExpanded(false);

                    ViewGroup.LayoutParams recyclerViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    int height;
                    if (searchBoxVisible()) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    } else
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                    recyclerViewParams.height = height;
                    getFullInventoryRecyclerView().setLayoutParams(recyclerViewParams);
                } else {
                    getEditDataBreak().setText(getString(R.string.store));
                    setCurrentBottomMenu(MENU_STORE);
                }
            }
        });

        getAddPopup().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getCurrentBottomMenu().equals(MENU_CATEGORY)) {
                    getShopping().loadFragment(new AddCategory());
                } else if (getCurrentBottomMenu().equals(MENU_ITEM)) {
                    getShopping().loadFragment(new AddItem());
                } else if (getCurrentBottomMenu().equals(MENU_STORE)) {
                    getShopping().loadFragment(new AddStore());
                }
            }
        });

        getEditPopup().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 if (getCurrentBottomMenu().equals(MENU_CATEGORY)) {
                     getShopping().loadFragment(new EditCategory());
                 } else if (getCurrentBottomMenu().equals(MENU_ITEM)) {
                     if (getShopping().itemIsSelectedInInventory()) {
                         getShopping().setEditItemInInventory(true);
                         getShopping().setEditItemInSearchResults(false);
                         getShopping().setEditItemInShoppingList(false);
                         getShopping().loadFragment(new EditItem());
                     } else if (getShopping().itemIsSelectedInSearchResults()) {
                         getShopping().setEditItemInInventory(false);
                         getShopping().setEditItemInSearchResults(true);
                         getShopping().setEditItemInShoppingList(false);
                         getShopping().loadFragment(new EditItem());
                     } else {
                         getShopping().showAlertDialog(getString(R.string.editItem), getString(R.string.selectItemToEdit), getString(R.string.ok));
                     }
                 } else if (getCurrentBottomMenu().equals(MENU_STORE)) {
                     getShopping().loadFragment(new EditStore());
                 }
             }
        });

        getRemovePopup().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getCurrentBottomMenu().equals(MENU_CATEGORY)) {
                    getShopping().loadFragment(new RemoveCategory());
                } else if (getCurrentBottomMenu().equals(MENU_ITEM)) {
                    if (getShopping().itemIsSelectedInInventory()) {
                        getShopping().loadFragment(new RemoveItem());
                    } else {
                        getShopping().showAlertDialog(getString(R.string.removeItem), getString(R.string.selectItemToRemove), getString(R.string.ok));
                    }
                } else if (getCurrentBottomMenu().equals(MENU_STORE)) {
                    getShopping().loadFragment(new RemoveStore());
                }
            }
        });

        getReorderPopup().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getCurrentBottomMenu().equals(MENU_CATEGORY)) {
                    getShopping().loadFragment(new ReorderCategories());
                } else if (getCurrentBottomMenu().equals(MENU_ITEM)) {
                    getShopping().loadFragment(new ReorderItems());
                } else if (getCurrentBottomMenu().equals(MENU_STORE)) {
                    getShopping().loadFragment(new ReorderStores());
                }
            }
        });

        getAddNewItemButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().loadFragment(new AddItem());
            }
        });

        getSearchButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (searchBoxVisible()) {
                    if (keyboardVisible()) {
                        // searchBox & keyboard both visible
                        getShopping().hideKeyboard();
                        getSearchPopup().setVisibility(View.GONE);
                        getSearchInventoryRecyclerView().setVisibility(View.GONE);
                        getFullInventoryTitle().setText(getLastMainTitle());
                        setSearchBoxVisible(false);
                        setKeyboardVisible(false);
                    } else {
                        // searchBox visible but keyboard not visible
                        getSearchPopup().setVisibility(View.GONE);
                        getSearchInventoryRecyclerView().setVisibility(View.GONE);
                        getFullInventoryTitle().setText(getLastMainTitle());
                        setSearchBoxVisible(false);
                        setKeyboardVisible(false);
                    }

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getFullInventoryRecyclerView().getLayoutParams();
                    int height;
                    if (editControlsExpanded()) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    } else
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                    params.addRule(RelativeLayout.BELOW, R.id.searchPopup);
                    params.height = height;
                    getFullInventoryRecyclerView().setLayoutParams(params);

                } else {
                    // searchBox & keyboard both not visible
                    getShopping().showKeyboard();
                    getSearchPopup().setVisibility(View.VISIBLE);
                    getSearchInventoryRecyclerView().setVisibility(View.VISIBLE);
                    setLastMainTitle(getFullInventoryTitle().getText().toString());
                    getFullInventoryTitle().setText(getString(R.string.searchInventory));
                    getSearchBox().requestFocus();
                    getSearchBox().setSelection(getSearchBox().getText().length());
                    setSearchBoxVisible(true);
                    setKeyboardVisible(true);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getFullInventoryRecyclerView().getLayoutParams();
                    int height;
                    if (editControlsExpanded()) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    params.addRule(RelativeLayout.BELOW, R.id.searchPopup);
                    params.height = height;
                    getFullInventoryRecyclerView().setLayoutParams(params);
                }
            }
        });

        getSearchBox().addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSearchInventoryAdapter().setCurrentSearchTerm(getSearchBox().getText().toString());
                getSearchInventoryAdapter().notifyDataSetChanged();
            }

        });

        getClearSearchButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getSearchBox().getText().toString().equals(getString(R.string.emptyString))) {
                    if (searchBoxVisible()) {
                        if (keyboardVisible()) {
                            // searchBox & keyboard both visible
                            getShopping().hideKeyboard();
                            getSearchPopup().setVisibility(View.GONE);
                            getSearchInventoryRecyclerView().setVisibility(View.GONE);
                            getFullInventoryTitle().setText(getLastMainTitle());
                            setSearchBoxVisible(false);
                            setKeyboardVisible(false);
                        } else {
                            // searchBox visible but keyboard not visible
                            getSearchPopup().setVisibility(View.GONE);
                            getSearchInventoryRecyclerView().setVisibility(View.GONE);
                            getFullInventoryTitle().setText(getLastMainTitle());
                            setSearchBoxVisible(false);
                            setKeyboardVisible(false);
                        }

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getFullInventoryRecyclerView().getLayoutParams();
                        int height;
                        if (editControlsExpanded()) {
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        } else
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        params.addRule(RelativeLayout.BELOW, R.id.searchPopup);
                        params.height = height;
                        getFullInventoryRecyclerView().setLayoutParams(params);

                    } else {
                        // searchBox & keyboard both not visible
                        getShopping().showKeyboard();
                        getSearchPopup().setVisibility(View.VISIBLE);
                        getSearchInventoryRecyclerView().setVisibility(View.VISIBLE);
                        setLastMainTitle(getFullInventoryTitle().getText().toString());
                        getFullInventoryTitle().setText(getString(R.string.searchInventory));
                        getSearchBox().requestFocus();
                        getSearchBox().setSelection(getSearchBox().getText().length());
                        setSearchBoxVisible(true);
                        setKeyboardVisible(true);

                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getFullInventoryRecyclerView().getLayoutParams();
                        int height;
                        if (editControlsExpanded()) {
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        } else
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        params.addRule(RelativeLayout.BELOW, R.id.searchPopup);
                        params.height = height;
                        getFullInventoryRecyclerView().setLayoutParams(params);
                    }
                } else {
                    getSearchBox().setText(getString(R.string.emptyString));
                }
            }
        });

        getRefreshButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                    getFullInventoryAdapter().notifyItemChanged(i);
                }
            }
        });

        getFullInventoryEditButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (menuOptionsVisible()) {
                    hideMenuOptions();
                } else {
                    showMenuOptions();
                }
            }
        });

        getViewAll().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               getShopping().setInventoryView(Shopping.VIEW_ALL);
               if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_CATEGORY)) {
                   getFullInventoryTitle().setText(getString(R.string.byCategoryAll));
                   for (int i = 0; i < getItemData().getItemListByCategory().size(); i++) {
                       getFullInventoryAdapter().notifyItemChanged(i);
                   }
               } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_STORE)) {
                   getFullInventoryTitle().setText(getString(R.string.byStoreAll));
                   for (int i = 0; i < getItemData().getItemListByStore().size(); i++) {
                       getFullInventoryAdapter().notifyItemChanged(i);
                   }
               } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {
                   getFullInventoryTitle().setText(getString(R.string.alphabeticalAll));
                   for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                       getFullInventoryAdapter().notifyItemChanged(i);
                   }
               }
               hideMenuOptions();
           }
        });

        getViewInStock().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setInventoryView(Shopping.VIEW_INSTOCK);
                if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_CATEGORY)) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryInStock));
                    for (int i = 0; i < getItemData().getItemListByCategory().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_STORE)) {
                    getFullInventoryTitle().setText(getString(R.string.byStoreInStock));
                    for (int i = 0; i < getItemData().getItemListByStore().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalInStock));
                    for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        getViewNeeded().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setInventoryView(Shopping.VIEW_NEEDED);
                if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_CATEGORY)) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryNeeded));
                    for (int i = 0; i < getItemData().getItemListByCategory().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_STORE)) {
                    getFullInventoryTitle().setText(getString(R.string.byStoreNeeded));
                    for (int i = 0; i < getItemData().getItemListByStore().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalNeeded));
                    for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        getViewPaused().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setInventoryView(Shopping.VIEW_PAUSED);
                if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_CATEGORY)) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryPaused));
                    for (int i = 0; i < getItemData().getItemListByCategory().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_STORE)) {
                    getFullInventoryTitle().setText(getString(R.string.byStorePaused));
                    for (int i = 0; i < getItemData().getItemListByStore().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalPaused));
                    for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        getExpandContractItems().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getShopping().getItemExpansion().equals(Shopping.ITEMS_CONTRACTED)) {
                    getShopping().setItemExpansion(Shopping.ITEMS_EXPANDED);
                    for (int i = 0; i < getItemData().getItemListByCategory().size(); i++) {
                        Item item = getItemData().getItemListByCategory().get(i);
                        item.getStatus().setAsExpandedInInventory();
                    }
                    for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                } else if (getShopping().getItemExpansion().equals(Shopping.ITEMS_EXPANDED)) {
                    getShopping().setItemExpansion(Shopping.ITEMS_CONTRACTED);
                    for (int i = 0; i < getItemData().getItemListByCategory().size(); i++) {
                        Item item = getItemData().getItemListByCategory().get(i);
                        item.getStatus().setAsContractedInInventory();
                    }
                    for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        getExpandContractCategories().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getShopping().getCategoryTitles().equals(Shopping.CATEGORY_TITLES_EXPANDED)) {
                    getShopping().setCategoryTitles(Shopping.CATEGORY_TITLES_CONTRACTED);
                    for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                        String category = getCategoryData().getCategoryList().get(i);
                        getItemData().getCategoryMap().get(category).setCategoryAsContracted();
                    }
                    for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                } else if (getShopping().getCategoryTitles().equals(Shopping.CATEGORY_TITLES_CONTRACTED)) {
                    getShopping().setCategoryTitles(Shopping.CATEGORY_TITLES_EXPANDED);
                    for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                        String category = getCategoryData().getCategoryList().get(i);
                        getItemData().getCategoryMap().get(category).setCategoryAsExpanded();
                    }
                    for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        getExpandContractStores().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getShopping().getStoreTitles().equals(Shopping.STORE_TITLES_EXPANDED)) {
                    getShopping().setStoreTitles(Shopping.STORE_TITLES_CONTRACTED);
                    for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                        String store = getStoreData().getStoreList().get(i);
                        getItemData().getStoreMap().get(store).setStoreAsContracted();
                    }
                    for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                } else if (getShopping().getStoreTitles().equals(Shopping.STORE_TITLES_CONTRACTED)) {
                    getShopping().setStoreTitles(Shopping.STORE_TITLES_EXPANDED);
                    for (int i = 0; i < getStoreData().getStoreList().size(); i++) {

                        getItemData().getStoreMap().get(getStoreData().getStoreList().get(i)).setStoreAsExpanded();
                    }
                    for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                        getFullInventoryAdapter().notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        getSortAlphabetical().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setInventorySortBy(Shopping.SORT_ALPHABETICAL);
                if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalAll));
                } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalInStock));
                } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalNeeded));
                } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalPaused));
                }
                for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                    getFullInventoryAdapter().notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        getSortByCategory().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setInventorySortBy(Shopping.SORT_BY_CATEGORY);
                if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryAll));
                } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryInStock));
                } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryNeeded));
                } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryPaused));
                }
                for (int i = 0; i < getItemData().getItemListByCategory().size(); i++) {
                    getFullInventoryAdapter().notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        getSortByStore().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setInventorySortBy(Shopping.SORT_BY_STORE);
                if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
                    getFullInventoryTitle().setText(getString(R.string.byStoreAll));
                } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
                    getFullInventoryTitle().setText(getString(R.string.byStoreInStock));
                } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
                    getFullInventoryTitle().setText(getString(R.string.byStoreNeeded));
                } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
                    getFullInventoryTitle().setText(getString(R.string.byStorePaused));
                }
                for (int i = 0; i < getItemData().getItemListByStore().size(); i++) {
                    getFullInventoryAdapter().notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        getHomeScreen().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().loadFragment(new LoadScreen());
            }
        });

        getFullInventoryRecyclerView().setOnTouchListener(new OnSwipeTouchListener(getView().getContext()) {
            void onSwipeLeft() {
                if (getShopping().getSwipingOption().equals(Shopping.SWIPING_ON)) {
                    moveLeftInFullInventory();
                }
            }
            void onSwipeRight() {
                if (getShopping().getSwipingOption().equals(Shopping.SWIPING_ON)) {
                    moveRightInFullInventory();
                }
            }
        });

        return getView();
    }

    private void moveLeftInFullInventory() {
        if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_CATEGORY)) {
            getShopping().setInventorySortBy(Shopping.SORT_BY_STORE);
            if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
                getFullInventoryTitle().setText(getString(R.string.byStoreAll));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
                getFullInventoryTitle().setText(getString(R.string.byStoreInStock));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
                getFullInventoryTitle().setText(getString(R.string.byStoreNeeded));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
                getFullInventoryTitle().setText(getString(R.string.byStorePaused));
            }
            for (int i = 0; i < getItemData().getItemListByStore().size(); i++) {
                getFullInventoryAdapter().notifyItemChanged(i);
            }
        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_STORE)) {
            getShopping().setInventorySortBy(Shopping.SORT_ALPHABETICAL);
            if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalAll));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalInStock));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalNeeded));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalPaused));
            }
            for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                getFullInventoryAdapter().notifyItemChanged(i);
            }
        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {
            getShopping().setInventorySortBy(Shopping.SORT_BY_CATEGORY);
            if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryAll));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryInStock));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryNeeded));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryPaused));
            }
            for (int i = 0; i < getItemData().getItemListByCategory().size(); i++) {
                getFullInventoryAdapter().notifyItemChanged(i);
            }
        }
    }

    private void moveRightInFullInventory() {
        if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_CATEGORY)) {
            getShopping().setInventorySortBy(Shopping.SORT_ALPHABETICAL);
            if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalAll));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalInStock));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalNeeded));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalPaused));
            }
            for (int i = 0; i < getItemData().getItemListAZ().size(); i++) {
                getFullInventoryAdapter().notifyItemChanged(i);
            }
        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_STORE)) {
            getShopping().setInventorySortBy(Shopping.SORT_BY_CATEGORY);
            if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryAll));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryInStock));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryNeeded));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryPaused));
            }
            for (int i = 0; i < getItemData().getItemListByCategory().size(); i++) {
                getFullInventoryAdapter().notifyItemChanged(i);
            }
        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {
            getShopping().setInventorySortBy(Shopping.SORT_BY_STORE);
            if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
                getFullInventoryTitle().setText(getString(R.string.byStoreAll));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
                getFullInventoryTitle().setText(getString(R.string.byStoreInStock));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
                getFullInventoryTitle().setText(getString(R.string.byStoreNeeded));
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
                getFullInventoryTitle().setText(getString(R.string.byStorePaused));
            }
            for (int i = 0; i < getItemData().getItemListByStore().size(); i++) {
                getFullInventoryAdapter().notifyItemChanged(i);
            }
        }
    }

    private void hideMenuOptions() {
        getViewAll().setVisibility(View.GONE);
        getViewInStock().setVisibility(View.GONE);
        getViewNeeded().setVisibility(View.GONE);
        getViewPaused().setVisibility(View.GONE);
        getExpandContractItems().setVisibility(View.GONE);
        getExpandContractCategories().setVisibility(View.GONE);
        getExpandContractStores().setVisibility(View.GONE);
        getSortAlphabetical().setVisibility(View.GONE);
        getSortByCategory().setVisibility(View.GONE);
        getSortByStore().setVisibility(View.GONE);
        getHomeScreen().setVisibility(View.GONE);
        getFullInventoryOptionsBackground().setVisibility(View.GONE);
        setMenuOptionsVisible(false);
    }

    private void showMenuOptions() {
        getViewAll().setVisibility(View.VISIBLE);
        getViewInStock().setVisibility(View.VISIBLE);
        getViewNeeded().setVisibility(View.VISIBLE);
        getViewPaused().setVisibility(View.VISIBLE);
        getExpandContractItems().setVisibility(View.VISIBLE);
        getExpandContractCategories().setVisibility(View.VISIBLE);
        getExpandContractStores().setVisibility(View.VISIBLE);
        getSortAlphabetical().setVisibility(View.VISIBLE);
        getSortByCategory().setVisibility(View.VISIBLE);
        getSortByStore().setVisibility(View.VISIBLE);
        getHomeScreen().setVisibility(View.VISIBLE);
        getFullInventoryOptionsBackground().setVisibility(View.VISIBLE);
        setMenuOptionsVisible(true);
    }

    private class OnSwipeTouchListener implements View.OnTouchListener {

        private Context context;
        private GestureDetector gestureDetector;

        private OnSwipeTouchListener(Context context) {
            setContext(context);
            setGestureDetector(new GestureDetector(getContext(), new GestureListener()));
        }

        private GestureDetector getGestureDetector() {
            return gestureDetector;
        }

        private void setGestureDetector(GestureDetector gestureDetector) {
            getThis().gestureDetector = gestureDetector;
        }

        private OnSwipeTouchListener getThis() {
            return this;
        }

        private Context getContext() {
            return context;
        }

        private void setContext(Context context) {
            getThis().context = context;
        }

        private void onSwipeLeft() {}

        private void onSwipeRight() {}

        public boolean onTouch(View v, MotionEvent event) {
            return getGestureDetector().onTouchEvent(event);
        }

        private class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 200;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) return false;
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > (3 * Math.abs(distanceY)) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                        && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0) onSwipeRight();
                    else onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }

    public void onDestroyView() {
        getShopping().setFullInventoryViewState(getFullInventoryRecyclerView().getLayoutManager().onSaveInstanceState());
        getShopping().setSearchInventoryViewState(getSearchInventoryRecyclerView().getLayoutManager().onSaveInstanceState());
        getFullInventoryRecyclerView().setAdapter(null);
        getSearchInventoryRecyclerView().setAdapter(null);
        super.onDestroyView();
    }

}