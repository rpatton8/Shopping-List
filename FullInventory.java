package ryan.android.shopping;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

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
    private boolean searchPopupVisible;
    private boolean keyboardVisible;
    private boolean individualCategoriesVisible;
    private boolean individualStoresVisible;
    private boolean editControlsExpanded;

    private TextView fullInventoryTitle;
    private TextView fullInventoryOptionsBackground;
    private String lastMainTitle;
    private TextView addNewItemButton;
    private TextView searchButton;
    private LinearLayout searchPopup;
    private EditText searchBox;
    private TextView clearSearchButton;
    private ImageView refreshButton;
    private ImageView fullInventoryEditButton;

    private LinearLayout individualCategoriesLayout;
    private Spinner individualCategoriesSpinner;
    private ArrayList<String> individualCategoriesSpinnerData;
    private ArrayAdapter<String> individualCategoriesSpinnerAdapter;
    private int individualCategoriesSpinnerPosition;
    private TextView individualCategoriesLeftArrow;
    private TextView individualCategoriesRightArrow;
    private LinearLayout individualStoresLayout;
    private Spinner individualStoresSpinner;
    private ArrayList<String> individualStoresSpinnerData;
    private ArrayAdapter<String> individualStoresSpinnerAdapter;
    private int individualStoresSpinnerPosition;
    private TextView individualStoresLeftArrow;
    private TextView individualStoresRightArrow;

    private RecyclerView fullInventoryRecyclerView;
    private RecyclerView searchInventoryRecyclerView;
    private RecyclerView individualCategoriesRecyclerView;
    private RecyclerView individualStoresRecyclerView;
    private FullInventoryRVA fullInventoryAdapter;
    private SearchInventoryRVA searchInventoryAdapter;
    private IndividualCategoriesRVA individualCategoriesAdapter;
    private IndividualStoresRVA individualStoresAdapter;

    private Button sortAlphabetical;
    private Button sortByStore;
    private Button sortByCategory;
    private Button viewAll;
    private Button viewInStock;
    private Button viewNeeded;
    private Button viewPaused;
    private Button expandItems;
    private Button contractItems;
    private Button expandCategories;
    private Button contractCategories;
    private Button expandStores;
    private Button contractStores;
    private Button individualCategories;
    private Button individualStores;

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

    private boolean searchPopupVisible() {
        return searchPopupVisible;
    }

    private void setSearchPopupVisible(boolean searchPopupVisible) {
        getThis().searchPopupVisible = searchPopupVisible;
    }

    private boolean keyboardVisible() {
        return keyboardVisible;
    }

    private void setKeyboardVisible(boolean keyboardVisible) {
        getThis().keyboardVisible = keyboardVisible;
    }


    public boolean individualCategoriesVisible() {
        return individualCategoriesVisible;
    }

    public void setIndividualCategoriesVisible(boolean individualCategoriesVisible) {
        getThis().individualCategoriesVisible = individualCategoriesVisible;
    }

    public boolean individualStoresVisible() {
        return individualStoresVisible;
    }

    public void setIndividualStoresVisible(boolean individualStoresVisible) {
        getThis().individualStoresVisible = individualStoresVisible;
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

    private ImageView getRefreshButton() {
        return refreshButton;
    }

    private void setRefreshButton(ImageView refreshButton) {
        getThis().refreshButton = refreshButton;
    }

    private ImageView getFullInventoryEditButton() {
        return fullInventoryEditButton;
    }

    private void setFullInventoryEditButton(ImageView fullInventoryEditButton) {
        getThis().fullInventoryEditButton = fullInventoryEditButton;
    }


    public LinearLayout getIndividualCategoriesLayout() {
        return individualCategoriesLayout;
    }

    public void setIndividualCategoriesLayout(LinearLayout individualCategoriesLayout) {
        getThis().individualCategoriesLayout = individualCategoriesLayout;
    }

    public Spinner getIndividualCategoriesSpinner() {
        return individualCategoriesSpinner;
    }

    public void setIndividualCategoriesSpinner(Spinner individualCategoriesSpinner) {
        getThis().individualCategoriesSpinner = individualCategoriesSpinner;
    }

    public ArrayList<String> getIndividualCategoriesSpinnerData() {
        return individualCategoriesSpinnerData;
    }

    public void setIndividualCategoriesSpinnerData(ArrayList<String> individualCategoriesSpinnerData) {
        getThis().individualCategoriesSpinnerData = individualCategoriesSpinnerData;
    }

    public ArrayAdapter<String> getIndividualCategoriesSpinnerAdapter() {
        return individualCategoriesSpinnerAdapter;
    }

    public void setIndividualCategoriesSpinnerAdapter(ArrayAdapter<String> individualCategoriesSpinnerAdapter) {
        getThis().individualCategoriesSpinnerAdapter = individualCategoriesSpinnerAdapter;
    }

    public int getIndividualCategoriesSpinnerPosition() {
        return individualCategoriesSpinnerPosition;
    }

    public void setIndividualCategoriesSpinnerPosition(int individualCategoriesSpinnerPosition) {
        getThis().individualCategoriesSpinnerPosition = individualCategoriesSpinnerPosition;
    }

    public TextView getIndividualCategoriesLeftArrow() {
        return individualCategoriesLeftArrow;
    }

    public void setIndividualCategoriesLeftArrow(TextView individualCategoriesLeftArrow) {
        getThis().individualCategoriesLeftArrow = individualCategoriesLeftArrow;
    }

    public TextView getIndividualCategoriesRightArrow() {
        return individualCategoriesRightArrow;
    }

    public void setIndividualCategoriesRightArrow(TextView individualCategoriesRightArrow) {
        getThis().individualCategoriesRightArrow = individualCategoriesRightArrow;
    }

    public LinearLayout getIndividualStoresLayout() {
        return individualStoresLayout;
    }

    public void setIndividualStoresLayout(LinearLayout individualStoresLayout) {
        getThis().individualStoresLayout = individualStoresLayout;
    }

    public Spinner getIndividualStoresSpinner() {
        return individualStoresSpinner;
    }

    public void setIndividualStoresSpinner(Spinner individualStoresSpinner) {
        getThis().individualStoresSpinner = individualStoresSpinner;
    }

    public ArrayList<String> getIndividualStoresSpinnerData() {
        return individualStoresSpinnerData;
    }

    public void setIndividualStoresSpinnerData(ArrayList<String> individualStoresSpinnerData) {
        getThis().individualStoresSpinnerData = individualStoresSpinnerData;
    }

    public ArrayAdapter<String> getIndividualStoresSpinnerAdapter() {
        return individualStoresSpinnerAdapter;
    }

    public void setIndividualStoresSpinnerAdapter(ArrayAdapter<String> individualStoresSpinnerAdapter) {
        getThis().individualStoresSpinnerAdapter = individualStoresSpinnerAdapter;
    }

    public int getIndividualStoresSpinnerPosition() {
        return individualStoresSpinnerPosition;
    }

    public void setIndividualStoresSpinnerPosition(int individualStoresSpinnerPosition) {
        getThis().individualStoresSpinnerPosition = individualStoresSpinnerPosition;
    }

    public TextView getIndividualStoresLeftArrow() {
        return individualStoresLeftArrow;
    }

    public void setIndividualStoresLeftArrow(TextView individualStoresLeftArrow) {
        getThis().individualStoresLeftArrow = individualStoresLeftArrow;
    }

    public TextView getIndividualStoresRightArrow() {
        return individualStoresRightArrow;
    }

    public void setIndividualStoresRightArrow(TextView individualStoresRightArrow) {
        getThis().individualStoresRightArrow = individualStoresRightArrow;
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

    public RecyclerView getIndividualCategoriesRecyclerView() {
        return individualCategoriesRecyclerView;
    }

    public void setIndividualCategoriesRecyclerView(RecyclerView individualCategoriesRecyclerView) {
        getThis().individualCategoriesRecyclerView = individualCategoriesRecyclerView;
    }

    public RecyclerView getIndividualStoresRecyclerView() {
        return individualStoresRecyclerView;
    }

    public void setIndividualStoresRecyclerView(RecyclerView individualStoresRecyclerView) {
        getThis().individualStoresRecyclerView = individualStoresRecyclerView;
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

    private IndividualCategoriesRVA getIndividualCategoriesAdapter() {
        return individualCategoriesAdapter;
    }

    private void setIndividualCategoriesAdapter(IndividualCategoriesRVA individualCategoriesAdapter) {
        getThis().individualCategoriesAdapter = individualCategoriesAdapter;
    }

    private IndividualStoresRVA getIndividualStoresAdapter() {
        return individualStoresAdapter;
    }

    private void setIndividualStoresAdapter(IndividualStoresRVA individualStoresAdapter) {
        getThis().individualStoresAdapter = individualStoresAdapter;
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

    public Button getExpandItems() {
        return expandItems;
    }

    public void setExpandItems(Button expandItems) {
        getThis().expandItems = expandItems;
    }

    public Button getContractItems() {
        return contractItems;
    }

    public void setContractItems(Button contractItems) {
        getThis().contractItems = contractItems;
    }

    public Button getExpandCategories() {
        return expandCategories;
    }

    public void setExpandCategories(Button expandCategories) {
        getThis().expandCategories = expandCategories;
    }

    public Button getContractCategories() {
        return contractCategories;
    }

    public void setContractCategories(Button contractCategories) {
        getThis().contractCategories = contractCategories;
    }

    public Button getExpandStores() {
        return expandStores;
    }

    public void setExpandStores(Button expandStores) {
        getThis().expandStores = expandStores;
    }

    public Button getContractStores() {
        return contractStores;
    }

    public void setContractStores(Button contractStores) {
        getThis().contractStores = contractStores;
    }

    private Button getIndividualCategories() {
        return individualCategories;
    }

    private void setIndividualCategories(Button individualCategories) {
        getThis().individualCategories = individualCategories;
    }

    private Button getIndividualStores() {
        return individualStores;
    }

    private void setIndividualStores(Button individualStores) {
        getThis().individualStores = individualStores;
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
                } else {
                    if (keyboardVisible()) setKeyboardVisible(false);
                }
            }
        });

        setShopping((Shopping) getActivity());
        setItemData(getShopping().getItemData());
        setStatusData(getShopping().getStatusData());
        setCategoryData(getShopping().getCategoryData());
        setStoreData(getShopping().getStoreData());
        getItemData().updateStatuses(getStatusData());

        setDbStatusHelper(new DBStatusHelper(getActivity()));
        setDbCategoryHelper(new DBCategoryHelper(getActivity()));
        setDbStoreHelper(new DBStoreHelper(getActivity()));

        setFullInventoryRecyclerView(getView().findViewById(R.id.fullInventoryRecyclerView));
        getFullInventoryRecyclerView().setHasFixedSize(false);
        getFullInventoryRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setFullInventoryAdapter(new FullInventoryRVA(getView(), getContext(), getShopping(),  getItemData(), getCategoryData(), getStoreData(), getDbStatusHelper(), getDbStoreHelper(), getDbCategoryHelper()));
        getFullInventoryRecyclerView().setAdapter(getFullInventoryAdapter());
        getFullInventoryRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getFullInventoryViewState());

        SearchAlgorithm searchAlgorithm = getShopping().getSearchAlgorithm();
        
        setSearchInventoryRecyclerView(getView().findViewById(R.id.searchInventoryRecyclerView));
        getSearchInventoryRecyclerView().setHasFixedSize(false);
        getSearchInventoryRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setSearchInventoryAdapter(new SearchInventoryRVA(getView(), getContext(), getShopping(), searchAlgorithm));
        getSearchInventoryRecyclerView().setAdapter(getSearchInventoryAdapter());
        getSearchInventoryRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getSearchInventoryViewState());

        setIndividualCategoriesRecyclerView(getView().findViewById(R.id.individualCategoriesRecyclerView));
        getIndividualCategoriesRecyclerView().setHasFixedSize(false);
        getIndividualCategoriesRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setIndividualCategoriesAdapter(new IndividualCategoriesRVA(getView(), getContext(), getShopping(), getItemData()));
        getIndividualCategoriesRecyclerView().setAdapter(getIndividualCategoriesAdapter());
        getIndividualCategoriesRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getIndividualCategoriesViewState());

        setIndividualStoresRecyclerView(getView().findViewById(R.id.individualStoresRecyclerView));
        getIndividualStoresRecyclerView().setHasFixedSize(false);
        getIndividualStoresRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setIndividualStoresAdapter(new IndividualStoresRVA(getView(), getContext(), getShopping(), getItemData()));
        getIndividualStoresRecyclerView().setAdapter(getIndividualStoresAdapter());
        getIndividualStoresRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getIndividualStoresViewState());

        setAddRemoveCategory(getView().findViewById(R.id.addRemoveCategory));
        setAddEditItem(getView().findViewById(R.id.addEditItem));
        setAddRemoveStore(getView().findViewById(R.id.addRemoveStore));
        setEditDataBreak(getView().findViewById(R.id.editDataBreak));
        setAddPopup(getView().findViewById(R.id.addPopup));
        setEditPopup(getView().findViewById(R.id.editPopup));
        setRemovePopup(getView().findViewById(R.id.removePopup));
        setReorderPopup(getView().findViewById(R.id.reorderPopup));

        setMenuOptionsVisible(false);
        setSearchPopupVisible(false);
        setKeyboardVisible(false);
        setIndividualCategoriesVisible(false);
        setIndividualStoresVisible(false);
        setEditControlsExpanded(false);
        setCurrentBottomMenu(MENU_NONE);

        setFullInventoryTitle(getView().findViewById(R.id.fullInventoryTitle));
        if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getInventorySortBy())) {
            getFullInventoryTitle().setText(getString(R.string.byCategoryAll));
        } else if (Shopping.SORT_BY_STORE.equals(getShopping().getInventorySortBy())) {
            getFullInventoryTitle().setText(getString(R.string.byStoreAll));
        } else if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getInventorySortBy())) {
            getFullInventoryTitle().setText(getString(R.string.alphabeticalAll));
        }

        setAddNewItemButton(getView().findViewById(R.id.addNewItemButton));
        setSearchButton(getView().findViewById(R.id.searchButton));
        setSearchBox(getView().findViewById(R.id.searchBox));
        setSearchPopup(getView().findViewById(R.id.searchPopup));
        setClearSearchButton(getView().findViewById(R.id.clearSearchButton));
        setRefreshButton(getView().findViewById(R.id.refreshButton));
        setFullInventoryEditButton(getView().findViewById(R.id.fullInventoryEditButton));

        setIndividualCategoriesLayout(getView().findViewById(R.id.individualCategoriesLayout));
        setIndividualCategoriesSpinner(getView().findViewById(R.id.individualCategoriesSpinner));
        setIndividualCategoriesLeftArrow(getView().findViewById(R.id.individualCategoriesLeftArrow));
        setIndividualCategoriesRightArrow(getView().findViewById(R.id.individualCategoriesRightArrow));
        setIndividualStoresLayout(getView().findViewById(R.id.individualStoresLayout));
        setIndividualStoresSpinner(getView().findViewById(R.id.individualStoresSpinner));
        setIndividualStoresLeftArrow(getView().findViewById(R.id.individualStoresLeftArrow));
        setIndividualStoresRightArrow(getView().findViewById(R.id.individualStoresRightArrow));

        setIndividualCategoriesSpinnerData(getCategoryData().getCategoryListWithBlank());
        setIndividualCategoriesSpinner(getView().findViewById(R.id.individualCategoriesSpinner));
        setIndividualCategoriesSpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), R.layout.spinner_outer_item_2, getIndividualCategoriesSpinnerData()));
        getIndividualCategoriesSpinnerAdapter().setDropDownViewResource(R.layout.spinner_inner_items_2);
        getIndividualCategoriesSpinner().setAdapter(getIndividualCategoriesSpinnerAdapter());
        setIndividualCategoriesSpinnerPosition(getIndividualCategoriesSpinnerAdapter().getPosition(getShopping().getIndividualCategory()));
        getIndividualCategoriesSpinner().setSelection(getIndividualCategoriesSpinnerPosition());

        getIndividualCategoriesSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                getIndividualCategoriesAdapter().changeCategory(selectedItem);
                getIndividualCategoriesAdapter().notifyDataSetChanged();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        getIndividualCategoriesLeftArrow().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setIndividualCategoriesSpinnerPosition(getIndividualCategoriesSpinnerAdapter().getPosition(getShopping().getIndividualCategory()));
                if ((getIndividualCategoriesSpinnerPosition() - 1) <= 0) {
                    setIndividualCategoriesSpinnerPosition(getIndividualCategoriesSpinnerData().size());
                } else if ((getIndividualCategoriesSpinnerPosition() - 1) > getIndividualCategoriesSpinnerData().size()) {
                    setIndividualCategoriesSpinnerPosition(0);
                }
                getIndividualCategoriesSpinner().setSelection(getIndividualCategoriesSpinnerPosition() - 1);
            }
        });

        getIndividualCategoriesRightArrow().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setIndividualCategoriesSpinnerPosition(getIndividualCategoriesSpinnerAdapter().getPosition(getShopping().getIndividualCategory()));
                if ((getIndividualCategoriesSpinnerPosition() + 1) < 0) {
                    setIndividualCategoriesSpinnerPosition(getIndividualCategoriesSpinnerData().size());
                } else if ((getIndividualCategoriesSpinnerPosition() + 1) >= getIndividualCategoriesSpinnerData().size()) {
                    setIndividualCategoriesSpinnerPosition(0);
                }
                getIndividualCategoriesSpinner().setSelection(getIndividualCategoriesSpinnerPosition() + 1);
            }
        });

        setIndividualStoresSpinnerData(getStoreData().getStoreListWithBlank());
        setIndividualStoresSpinner(getView().findViewById(R.id.individualStoresSpinner));
        setIndividualStoresSpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), R.layout.spinner_outer_item_2, getIndividualStoresSpinnerData()));
        getIndividualStoresSpinnerAdapter().setDropDownViewResource(R.layout.spinner_inner_items_2);
        getIndividualStoresSpinner().setAdapter(getIndividualStoresSpinnerAdapter());
        setIndividualStoresSpinnerPosition(getIndividualStoresSpinnerAdapter().getPosition(getShopping().getIndividualStore()));
        getIndividualStoresSpinner().setSelection(getIndividualStoresSpinnerPosition());

        getIndividualStoresSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                getIndividualStoresAdapter().changeStore(selectedItem);
                getIndividualStoresAdapter().notifyDataSetChanged();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        getIndividualStoresLeftArrow().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setIndividualStoresSpinnerPosition(getIndividualStoresSpinnerAdapter().getPosition(getShopping().getIndividualStore()));
                if ((getIndividualStoresSpinnerPosition() - 1) <= 0) {
                    setIndividualStoresSpinnerPosition(getIndividualStoresSpinnerData().size());
                } else if ((getIndividualStoresSpinnerPosition() - 1) > getIndividualStoresSpinnerData().size()) {
                    setIndividualStoresSpinnerPosition(0);
                }
                getIndividualStoresSpinner().setSelection(getIndividualStoresSpinnerPosition() - 1);
            }
        });

        getIndividualStoresRightArrow().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setIndividualStoresSpinnerPosition(getIndividualStoresSpinnerAdapter().getPosition(getShopping().getIndividualStore()));
                if ((getIndividualStoresSpinnerPosition() + 1) < 0) {
                    setIndividualStoresSpinnerPosition(getIndividualStoresSpinnerData().size());
                } else if ((getIndividualStoresSpinnerPosition() + 1) >= getIndividualStoresSpinnerData().size()) {
                    setIndividualStoresSpinnerPosition(0);
                }
                getIndividualStoresSpinner().setSelection(getIndividualStoresSpinnerPosition() + 1);
            }
        });

        setFullInventoryOptionsBackground(getView().findViewById(R.id.fullInventoryOptionsBackground));
        setSortAlphabetical(getView().findViewById(R.id.sortAlphabetical));
        setSortByCategory(getView().findViewById(R.id.sortByCategory));
        setSortByStore(getView().findViewById(R.id.sortByStore));
        setViewAll(getView().findViewById(R.id.viewAll));
        setViewInStock(getView().findViewById(R.id.viewInStock));
        setViewNeeded(getView().findViewById(R.id.viewNeeded));
        setViewPaused(getView().findViewById(R.id.viewPaused));
        setExpandItems(getView().findViewById(R.id.expandItems));
        setContractItems(getView().findViewById(R.id.contractItems));
        setExpandCategories(getView().findViewById(R.id.expandCategories));
        setContractCategories(getView().findViewById(R.id.contractCategories));
        setExpandStores(getView().findViewById(R.id.expandStores));
        setContractStores(getView().findViewById(R.id.contractStores));
        setIndividualCategories(getView().findViewById(R.id.individualCategories));
        setIndividualStores(getView().findViewById(R.id.individualStores));

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

                    ViewGroup.LayoutParams fullInventoryViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams searchInventoryViewParams = getSearchInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualCategoriesViewParams = getIndividualCategoriesRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualStoresViewParams = getIndividualStoresRecyclerView().getLayoutParams();
                    int fullInventoryHeight;
                    int searchInventoryHeight;
                    int individualCategoriesHeight;
                    int individualStoresHeight;
                    if (searchPopupVisible()) {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    }
                    fullInventoryViewParams.height = fullInventoryHeight;
                    searchInventoryViewParams.height = searchInventoryHeight;
                    individualCategoriesViewParams.height = individualCategoriesHeight;
                    individualStoresViewParams.height = individualStoresHeight;
                    getFullInventoryRecyclerView().setLayoutParams(fullInventoryViewParams);
                    getSearchInventoryRecyclerView().setLayoutParams(searchInventoryViewParams);
                    getIndividualCategoriesRecyclerView().setLayoutParams(individualCategoriesViewParams);
                    getIndividualStoresRecyclerView().setLayoutParams(individualStoresViewParams);

                } else if (getCurrentBottomMenu().equals(MENU_CATEGORY)) {
                    getEditDataBreak().setVisibility(View.GONE);
                    getAddPopup().setVisibility(View.GONE);
                    getEditPopup().setVisibility(View.GONE);
                    getRemovePopup().setVisibility(View.GONE);
                    getReorderPopup().setVisibility(View.GONE);
                    setCurrentBottomMenu(MENU_NONE);
                    setEditControlsExpanded(false);

                    ViewGroup.LayoutParams fullInventoryViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams searchInventoryViewParams = getSearchInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualCategoriesViewParams = getIndividualCategoriesRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualStoresViewParams = getIndividualStoresRecyclerView().getLayoutParams();
                    int fullInventoryHeight;
                    int searchInventoryHeight;
                    int individualCategoriesHeight;
                    int individualStoresHeight;
                    if (searchPopupVisible()) {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    } else {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    }
                    fullInventoryViewParams.height = fullInventoryHeight;
                    searchInventoryViewParams.height = searchInventoryHeight;
                    individualCategoriesViewParams.height = individualCategoriesHeight;
                    individualStoresViewParams.height = individualStoresHeight;
                    getFullInventoryRecyclerView().setLayoutParams(fullInventoryViewParams);
                    getSearchInventoryRecyclerView().setLayoutParams(searchInventoryViewParams);
                    getIndividualCategoriesRecyclerView().setLayoutParams(individualCategoriesViewParams);
                    getIndividualStoresRecyclerView().setLayoutParams(individualStoresViewParams);
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

                    ViewGroup.LayoutParams fullInventoryViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams searchInventoryViewParams = getSearchInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualCategoriesViewParams = getIndividualCategoriesRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualStoresViewParams = getIndividualStoresRecyclerView().getLayoutParams();
                    int fullInventoryHeight;
                    int searchInventoryHeight;
                    int individualCategoriesHeight;
                    int individualStoresHeight;
                    if (searchPopupVisible()) {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    }
                    fullInventoryViewParams.height = fullInventoryHeight;
                    searchInventoryViewParams.height = searchInventoryHeight;
                    individualCategoriesViewParams.height = individualCategoriesHeight;
                    individualStoresViewParams.height = individualStoresHeight;
                    getFullInventoryRecyclerView().setLayoutParams(fullInventoryViewParams);
                    getSearchInventoryRecyclerView().setLayoutParams(searchInventoryViewParams);
                    getIndividualCategoriesRecyclerView().setLayoutParams(individualCategoriesViewParams);
                    getIndividualStoresRecyclerView().setLayoutParams(individualStoresViewParams);

                } else if (getCurrentBottomMenu().equals(MENU_ITEM)) {
                    getEditDataBreak().setVisibility(View.GONE);
                    getAddPopup().setVisibility(View.GONE);
                    getEditPopup().setVisibility(View.GONE);
                    getRemovePopup().setVisibility(View.GONE);
                    getReorderPopup().setVisibility(View.GONE);
                    setCurrentBottomMenu(MENU_NONE);
                    setEditControlsExpanded(false);

                    ViewGroup.LayoutParams fullInventoryViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams searchInventoryViewParams = getSearchInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualCategoriesViewParams = getIndividualCategoriesRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualStoresViewParams = getIndividualStoresRecyclerView().getLayoutParams();
                    int fullInventoryHeight;
                    int searchInventoryHeight;
                    int individualCategoriesHeight;
                    int individualStoresHeight;
                    if (searchPopupVisible()) {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    } else {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                    }
                    fullInventoryViewParams.height = fullInventoryHeight;
                    searchInventoryViewParams.height = searchInventoryHeight;
                    individualCategoriesViewParams.height = individualCategoriesHeight;
                    individualStoresViewParams.height = individualStoresHeight;
                    getFullInventoryRecyclerView().setLayoutParams(fullInventoryViewParams);
                    getSearchInventoryRecyclerView().setLayoutParams(searchInventoryViewParams);
                    getIndividualCategoriesRecyclerView().setLayoutParams(individualCategoriesViewParams);
                    getIndividualStoresRecyclerView().setLayoutParams(individualStoresViewParams);
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

                    ViewGroup.LayoutParams fullInventoryViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams searchInventoryViewParams = getSearchInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualCategoriesViewParams = getIndividualCategoriesRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualStoresViewParams = getIndividualStoresRecyclerView().getLayoutParams();
                    int fullInventoryHeight;
                    int searchInventoryHeight;
                    int individualCategoriesHeight;
                    int individualStoresHeight;
                    if (searchPopupVisible()) {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    }
                    fullInventoryViewParams.height = fullInventoryHeight;
                    searchInventoryViewParams.height = searchInventoryHeight;
                    individualCategoriesViewParams.height = individualCategoriesHeight;
                    individualStoresViewParams.height = individualStoresHeight;
                    getFullInventoryRecyclerView().setLayoutParams(fullInventoryViewParams);
                    getSearchInventoryRecyclerView().setLayoutParams(searchInventoryViewParams);
                    getIndividualCategoriesRecyclerView().setLayoutParams(individualCategoriesViewParams);
                    getIndividualStoresRecyclerView().setLayoutParams(individualStoresViewParams);

                } else if (getCurrentBottomMenu().equals(MENU_STORE)) {
                    getEditDataBreak().setVisibility(View.GONE);
                    getAddPopup().setVisibility(View.GONE);
                    getEditPopup().setVisibility(View.GONE);
                    getRemovePopup().setVisibility(View.GONE);
                    getReorderPopup().setVisibility(View.GONE);
                    setCurrentBottomMenu(MENU_NONE);
                    setEditControlsExpanded(false);

                    ViewGroup.LayoutParams fullInventoryViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams searchInventoryViewParams = getSearchInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualCategoriesViewParams = getIndividualCategoriesRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualStoresViewParams = getIndividualStoresRecyclerView().getLayoutParams();
                    int fullInventoryHeight;
                    int searchInventoryHeight;
                    int individualCategoriesHeight;
                    int individualStoresHeight;
                    if (searchPopupVisible()) {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    } else {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                    }
                    fullInventoryViewParams.height = fullInventoryHeight;
                    searchInventoryViewParams.height = searchInventoryHeight;
                    individualCategoriesViewParams.height = individualCategoriesHeight;
                    individualStoresViewParams.height = individualStoresHeight;
                    getFullInventoryRecyclerView().setLayoutParams(fullInventoryViewParams);
                    getSearchInventoryRecyclerView().setLayoutParams(searchInventoryViewParams);
                    getIndividualCategoriesRecyclerView().setLayoutParams(individualCategoriesViewParams);
                    getIndividualStoresRecyclerView().setLayoutParams(individualStoresViewParams);
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
                if (searchPopupVisible()) {
                    if (keyboardVisible()) {
                       getShopping().hideKeyboard();
                        setKeyboardVisible(false);
                    }
                    getSearchPopup().setVisibility(View.GONE);
                    getSearchInventoryRecyclerView().setVisibility(View.GONE);
                    getFullInventoryTitle().setText(getLastMainTitle());
                    setSearchPopupVisible(false);

                    ViewGroup.LayoutParams fullInventoryViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams searchInventoryViewParams = getSearchInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualCategoriesViewParams = getIndividualCategoriesRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualStoresViewParams = getIndividualStoresRecyclerView().getLayoutParams();
                    int fullInventoryHeight;
                    int searchInventoryHeight;
                    int individualCategoriesHeight;
                    int individualStoresHeight;
                    if (editControlsExpanded()) {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 450, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 450, getResources().getDisplayMetrics());
                    } else {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    }
                    fullInventoryViewParams.height = fullInventoryHeight;
                    searchInventoryViewParams.height = searchInventoryHeight;
                    individualCategoriesViewParams.height = individualCategoriesHeight;
                    individualStoresViewParams.height = individualStoresHeight;
                    getFullInventoryRecyclerView().setLayoutParams(fullInventoryViewParams);
                    getSearchInventoryRecyclerView().setLayoutParams(searchInventoryViewParams);
                    getIndividualCategoriesRecyclerView().setLayoutParams(individualCategoriesViewParams);
                    getIndividualStoresRecyclerView().setLayoutParams(individualStoresViewParams);

                } else {
                    // searchBox & keyboard both not visible
                    getShopping().showKeyboard();
                    setKeyboardVisible(true);
                    getSearchPopup().setVisibility(View.VISIBLE);
                    setSearchPopupVisible(true);
                    getSearchInventoryRecyclerView().setVisibility(View.VISIBLE);
                    getFullInventoryRecyclerView().setVisibility(View.GONE);
                    getIndividualCategoriesLayout().setVisibility(View.GONE);
                    getIndividualStoresLayout().setVisibility(View.GONE);
                    setLastMainTitle(getFullInventoryTitle().getText().toString());
                    getFullInventoryTitle().setText(getString(R.string.searchInventory));
                    getSearchBox().requestFocus();
                    getSearchBox().setSelection(getSearchBox().getText().length());


                    ViewGroup.LayoutParams fullInventoryViewParams = getFullInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams searchInventoryViewParams = getSearchInventoryRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualCategoriesViewParams = getIndividualCategoriesRecyclerView().getLayoutParams();
                    ViewGroup.LayoutParams individualStoresViewParams = getIndividualStoresRecyclerView().getLayoutParams();
                    int fullInventoryHeight;
                    int searchInventoryHeight;
                    int individualCategoriesHeight;
                    int individualStoresHeight;
                    if (editControlsExpanded()) {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else {
                        fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 450, getResources().getDisplayMetrics());
                        individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 450, getResources().getDisplayMetrics());
                    }
                    fullInventoryViewParams.height = fullInventoryHeight;
                    searchInventoryViewParams.height = searchInventoryHeight;
                    individualCategoriesViewParams.height = individualCategoriesHeight;
                    individualStoresViewParams.height = individualStoresHeight;
                    getFullInventoryRecyclerView().setLayoutParams(fullInventoryViewParams);
                    getSearchInventoryRecyclerView().setLayoutParams(searchInventoryViewParams);
                    getIndividualCategoriesRecyclerView().setLayoutParams(individualCategoriesViewParams);
                    getIndividualStoresRecyclerView().setLayoutParams(individualStoresViewParams);
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
                    if (searchPopupVisible()) {
                        if (keyboardVisible()) {
                            getShopping().hideKeyboard();
                        }
                        getSearchPopup().setVisibility(View.GONE);
                        getSearchInventoryRecyclerView().setVisibility(View.GONE);
                        getFullInventoryTitle().setText(getLastMainTitle());
                        if (getLastMainTitle().equals("Individual Categories")) {
                            getIndividualCategoriesLayout().setVisibility(View.VISIBLE);
                        } else if (getLastMainTitle().equals("Individual Stores")) {
                            getIndividualStoresLayout().setVisibility(View.VISIBLE);
                        } else {
                            getFullInventoryRecyclerView().setVisibility(View.VISIBLE);
                        }
                        setSearchPopupVisible(false);
                        setKeyboardVisible(false);

                        ViewGroup.LayoutParams fullInventoryViewParams = getFullInventoryRecyclerView().getLayoutParams();
                        ViewGroup.LayoutParams searchInventoryViewParams = getSearchInventoryRecyclerView().getLayoutParams();
                        ViewGroup.LayoutParams individualCategoriesViewParams = getIndividualCategoriesRecyclerView().getLayoutParams();
                        ViewGroup.LayoutParams individualStoresViewParams = getIndividualStoresRecyclerView().getLayoutParams();
                        int fullInventoryHeight;
                        int searchInventoryHeight;
                        int individualCategoriesHeight;
                        int individualStoresHeight;
                        if (editControlsExpanded()) {
                            fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                            searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                            individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                            individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        } else {
                            fullInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                            searchInventoryHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                            individualCategoriesHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                            individualStoresHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        }
                        fullInventoryViewParams.height = fullInventoryHeight;
                        searchInventoryViewParams.height = searchInventoryHeight;
                        individualCategoriesViewParams.height = individualCategoriesHeight;
                        individualStoresViewParams.height = individualStoresHeight;
                        getFullInventoryRecyclerView().setLayoutParams(fullInventoryViewParams);
                        getSearchInventoryRecyclerView().setLayoutParams(searchInventoryViewParams);
                        getIndividualCategoriesRecyclerView().setLayoutParams(individualCategoriesViewParams);
                        getIndividualStoresRecyclerView().setLayoutParams(individualStoresViewParams);

                    }
                } else {
                    getSearchBox().setText(getString(R.string.emptyString));
                }
            }
        });

        getRefreshButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFullInventoryAdapter().notifyDataSetChanged();
            }
        });

        getFullInventoryEditButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (menuOptionsVisible()) {
                    getFullInventoryTitle().setText(getLastMainTitle());
                    hideMenuOptions();
                } else {
                    setLastMainTitle(getFullInventoryTitle().getText().toString());
                    getFullInventoryTitle().setText(getString(R.string.inventoryOptions));
                    showMenuOptions();
                }
            }
        });

        getSortAlphabetical().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setInventorySortBy(Shopping.SORT_ALPHABETICAL);
                if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalAll));
                } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalInStock));
                } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalNeeded));
                } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalPaused));
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                if (searchPopupVisible()) {
                    setSearchPopupVisible(false);
                    getSearchPopup().setVisibility(View.GONE);
                }
                if (individualCategoriesVisible()) {
                    setIndividualCategoriesVisible(false);
                    getIndividualCategoriesLayout().setVisibility(View.GONE);
                }
                if (individualStoresVisible()) {
                    setIndividualStoresVisible(false);
                    getIndividualStoresLayout().setVisibility(View.GONE);
                }
                getFullInventoryRecyclerView().setVisibility(View.VISIBLE);
                hideMenuOptions();
            }
        });

        getSortByCategory().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setInventorySortBy(Shopping.SORT_BY_CATEGORY);
                if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryAll));
                } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryInStock));
                } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryNeeded));
                } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryPaused));
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                if (searchPopupVisible()) {
                    setSearchPopupVisible(false);
                    getSearchPopup().setVisibility(View.GONE);
                }
                if (individualCategoriesVisible()) {
                    setIndividualCategoriesVisible(false);
                    getIndividualCategoriesLayout().setVisibility(View.GONE);
                }
                if (individualStoresVisible()) {
                    setIndividualStoresVisible(false);
                    getIndividualStoresLayout().setVisibility(View.GONE);
                }
                getFullInventoryRecyclerView().setVisibility(View.VISIBLE);
                hideMenuOptions();
            }
        });

        getSortByStore().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().setInventorySortBy(Shopping.SORT_BY_STORE);
                if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.byStoreAll));
                } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.byStoreInStock));
                } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.byStoreNeeded));
                } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
                    getFullInventoryTitle().setText(getString(R.string.byStorePaused));
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                if (searchPopupVisible()) {
                    setSearchPopupVisible(false);
                    getSearchPopup().setVisibility(View.GONE);
                }
                if (individualCategoriesVisible()) {
                    setIndividualCategoriesVisible(false);
                    getIndividualCategoriesLayout().setVisibility(View.GONE);
                }
                if (individualStoresVisible()) {
                    setIndividualStoresVisible(false);
                    getIndividualStoresLayout().setVisibility(View.GONE);
                }
                getFullInventoryRecyclerView().setVisibility(View.VISIBLE);
                hideMenuOptions();
            }
        });

        getViewAll().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (individualCategoriesVisible() || individualStoresVisible()) {
                    hideMenuOptions();
                    return;
                }
               getShopping().setInventoryView(Shopping.VIEW_ALL);
               if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getInventorySortBy())) {
                   getFullInventoryTitle().setText(getString(R.string.byCategoryAll));
               } else if (Shopping.SORT_BY_STORE.equals(getShopping().getInventorySortBy())) {
                   getFullInventoryTitle().setText(getString(R.string.byStoreAll));
               } else if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getInventorySortBy())) {
                   getFullInventoryTitle().setText(getString(R.string.alphabeticalAll));
               }
               getFullInventoryAdapter().notifyDataSetChanged();
               hideMenuOptions();
           }
        });

        getViewInStock().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (individualCategoriesVisible() || individualStoresVisible()) {
                    hideMenuOptions();
                    return;
                }
                getShopping().setInventoryView(Shopping.VIEW_INSTOCK);
                if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getInventorySortBy())) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryInStock));
                } else if (Shopping.SORT_BY_STORE.equals(getShopping().getInventorySortBy())) {
                    getFullInventoryTitle().setText(getString(R.string.byStoreInStock));
                } else if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getInventorySortBy())) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalInStock));
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                hideMenuOptions();
            }
        });

        getViewNeeded().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (individualCategoriesVisible() || individualStoresVisible()) {
                    hideMenuOptions();
                    return;
                }
                getShopping().setInventoryView(Shopping.VIEW_NEEDED);
                if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getInventorySortBy())) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryNeeded));
                } else if (Shopping.SORT_BY_STORE.equals(getShopping().getInventorySortBy())) {
                    getFullInventoryTitle().setText(getString(R.string.byStoreNeeded));
                } else if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getInventorySortBy())) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalNeeded));
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                hideMenuOptions();
            }
        });

        getViewPaused().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (individualCategoriesVisible() || individualStoresVisible()) {
                    hideMenuOptions();
                    return;
                }
                getShopping().setInventoryView(Shopping.VIEW_PAUSED);
                if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getInventorySortBy())) {
                    getFullInventoryTitle().setText(getString(R.string.byCategoryPaused));
                } else if (Shopping.SORT_BY_STORE.equals(getShopping().getInventorySortBy())) {
                    getFullInventoryTitle().setText(getString(R.string.byStorePaused));
                } else if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getInventorySortBy())) {
                    getFullInventoryTitle().setText(getString(R.string.alphabeticalPaused));
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                hideMenuOptions();
            }
        });

        getExpandItems().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Shopping.ITEMS_CONTRACTED.equals(getShopping().getItemExpansion())) {
                    getShopping().setItemExpansion(Shopping.ITEMS_EXPANDED);
                    for (int i = 0; i < getItemData().getItemListByCategory().size(); i++) {
                        Item item = getItemData().getItemListByCategory().get(i);
                        item.getStatus().setAsExpandedInInventory();
                    }
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                hideMenuOptions();
            }
        });

        getContractItems().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Shopping.ITEMS_EXPANDED.equals(getShopping().getItemExpansion())) {
                    getShopping().setItemExpansion(Shopping.ITEMS_CONTRACTED);
                    for (int i = 0; i < getItemData().getItemListByCategory().size(); i++) {
                        Item item = getItemData().getItemListByCategory().get(i);
                        item.getStatus().setAsContractedInInventory();
                    }
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                hideMenuOptions();
            }
        });

        getExpandCategories().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Shopping.CATEGORY_TITLES_CONTRACTED.equals(getShopping().getCategoryTitles())) {
                    getShopping().setCategoryTitles(Shopping.CATEGORY_TITLES_EXPANDED);
                    for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                        String category = getCategoryData().getCategoryList().get(i);
                        if (getItemData().getCategoryMap().get(category) != null) {
                            getItemData().getCategoryMap().get(category).setCategoryAsExpanded();
                        }
                    }
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                hideMenuOptions();
            }
        });

        getContractCategories().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Shopping.CATEGORY_TITLES_EXPANDED.equals(getShopping().getCategoryTitles())) {
                    getShopping().setCategoryTitles(Shopping.CATEGORY_TITLES_CONTRACTED);
                    for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                        String category = getCategoryData().getCategoryList().get(i);
                        if (getItemData().getCategoryMap().get(category) != null) {
                            getItemData().getCategoryMap().get(category).setCategoryAsContracted();
                        }
                    }
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                hideMenuOptions();
            }
        });

        getExpandStores().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Shopping.STORE_TITLES_CONTRACTED.equals(getShopping().getStoreTitles())) {
                    getShopping().setStoreTitles(Shopping.STORE_TITLES_EXPANDED);
                    for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                        String store = getStoreData().getStoreList().get(i);
                        if (getItemData().getStoreMap().get(store) != null) {
                            getItemData().getStoreMap().get(store).setStoreAsExpanded();
                        }
                    }
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                hideMenuOptions();
            }
        });

        getContractStores().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Shopping.STORE_TITLES_EXPANDED.equals(getShopping().getStoreTitles())) {
                    getShopping().setStoreTitles(Shopping.STORE_TITLES_CONTRACTED);
                    for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                        String store = getStoreData().getStoreList().get(i);
                        if (getItemData().getStoreMap().get(store) != null) {
                            getItemData().getStoreMap().get(store).setStoreAsContracted();
                        }
                    }
                }
                getFullInventoryAdapter().notifyDataSetChanged();
                hideMenuOptions();
            }
        });

        getIndividualCategories().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (individualCategoriesVisible) {
                    hideMenuOptions();
                    getFullInventoryTitle().setText(getString(R.string.individualCategories));
                    return;
                }
                if (individualStoresVisible) {
                    setIndividualStoresVisible(false);
                    individualStoresLayout.setVisibility(View.GONE);
                }
                getFullInventoryTitle().setText(getString(R.string.individualCategories));
                getFullInventoryAdapter().notifyDataSetChanged();
                setIndividualCategoriesVisible(true);
                individualCategoriesLayout.setVisibility(View.VISIBLE);
                individualCategoriesRecyclerView.setVisibility(View.VISIBLE);
                fullInventoryRecyclerView.setVisibility(View.GONE);
                hideMenuOptions();
            }
        });

        getIndividualStores().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (individualStoresVisible) {
                    hideMenuOptions();
                    getFullInventoryTitle().setText(getString(R.string.individualStores));
                    return;
                }
                if (individualCategoriesVisible) {
                    setIndividualCategoriesVisible(false);
                    individualCategoriesLayout.setVisibility(View.GONE);
                }
                getFullInventoryTitle().setText(getString(R.string.individualStores));
                getFullInventoryAdapter().notifyDataSetChanged();
                setIndividualStoresVisible(true);
                individualStoresLayout.setVisibility(View.VISIBLE);
                individualStoresRecyclerView.setVisibility(View.VISIBLE);
                fullInventoryRecyclerView.setVisibility(View.GONE);
                hideMenuOptions();
            }
        });

        getFullInventoryRecyclerView().setOnTouchListener(new OnSwipeTouchListener(getView().getContext()) {
            void onSwipeLeft() {
                if (Shopping.SWIPING_ON.equals(getShopping().getSwipingOption())) {
                    moveLeftInFullInventory();
                }
            }
            void onSwipeRight() {
                if (Shopping.SWIPING_ON.equals(getShopping().getSwipingOption())) {
                    moveRightInFullInventory();
                }
            }
        });

        getFullInventoryRecyclerView().addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {
                        outRect.top += getResources().getDimensionPixelSize(R.dimen.padding_standard);
                    }
                }
            }
        });

        return getView();
    }

    private void moveLeftInFullInventory() {
        if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getInventorySortBy())) {
            getShopping().setInventorySortBy(Shopping.SORT_BY_STORE);
            if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byStoreAll));
            } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byStoreInStock));
            } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byStoreNeeded));
            } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byStorePaused));
            }
        } else if (Shopping.SORT_BY_STORE.equals(getShopping().getInventorySortBy())) {
            getShopping().setInventorySortBy(Shopping.SORT_ALPHABETICAL);
            if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalAll));
            } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalInStock));
            } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalNeeded));
            } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalPaused));
            }
        } else if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getInventorySortBy())) {
            getShopping().setInventorySortBy(Shopping.SORT_BY_CATEGORY);
            if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryAll));
            } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryInStock));
            } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryNeeded));
            } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryPaused));
            }
        }
        getFullInventoryAdapter().notifyDataSetChanged();
    }

    private void moveRightInFullInventory() {
        if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getInventorySortBy())) {
            getShopping().setInventorySortBy(Shopping.SORT_ALPHABETICAL);
            if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalAll));
            } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalInStock));
            } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalNeeded));
            } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.alphabeticalPaused));
            }
        } else if (Shopping.SORT_BY_STORE.equals(getShopping().getInventorySortBy())) {
            getShopping().setInventorySortBy(Shopping.SORT_BY_CATEGORY);
            if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryAll));
            } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryInStock));
            } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryNeeded));
            } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byCategoryPaused));
            }
        } else if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getInventorySortBy())) {
            getShopping().setInventorySortBy(Shopping.SORT_BY_STORE);
            if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byStoreAll));
            } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byStoreInStock));
            } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byStoreNeeded));
            } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
                getFullInventoryTitle().setText(getString(R.string.byStorePaused));
            }
        }
        getFullInventoryAdapter().notifyDataSetChanged();
    }

    private void hideMenuOptions() {
        getSortAlphabetical().setVisibility(View.GONE);
        getSortByCategory().setVisibility(View.GONE);
        getSortByStore().setVisibility(View.GONE);
        getViewAll().setVisibility(View.GONE);
        getViewInStock().setVisibility(View.GONE);
        getViewNeeded().setVisibility(View.GONE);
        getViewPaused().setVisibility(View.GONE);
        getExpandItems().setVisibility(View.GONE);
        getContractItems().setVisibility(View.GONE);
        getExpandCategories().setVisibility(View.GONE);
        getContractCategories().setVisibility(View.GONE);
        getExpandStores().setVisibility(View.GONE);
        getContractStores().setVisibility(View.GONE);
        getIndividualCategories().setVisibility(View.GONE);
        getIndividualStores().setVisibility(View.GONE);
        getFullInventoryOptionsBackground().setVisibility(View.GONE);
        setMenuOptionsVisible(false);
    }

    private void showMenuOptions() {
        getSortAlphabetical().setVisibility(View.VISIBLE);
        getSortByCategory().setVisibility(View.VISIBLE);
        getSortByStore().setVisibility(View.VISIBLE);
        getViewAll().setVisibility(View.VISIBLE);
        getViewInStock().setVisibility(View.VISIBLE);
        getViewNeeded().setVisibility(View.VISIBLE);
        getViewPaused().setVisibility(View.VISIBLE);
        getExpandItems().setVisibility(View.VISIBLE);
        getContractItems().setVisibility(View.VISIBLE);
        getExpandCategories().setVisibility(View.VISIBLE);
        getContractCategories().setVisibility(View.VISIBLE);
        getExpandStores().setVisibility(View.VISIBLE);
        getContractStores().setVisibility(View.VISIBLE);
        getIndividualCategories().setVisibility(View.VISIBLE);
        getIndividualStores().setVisibility(View.VISIBLE);
        getFullInventoryOptionsBackground().setVisibility(View.VISIBLE);
        setMenuOptionsVisible(true);
    }

    private static class OnSwipeTouchListener implements View.OnTouchListener {

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
        getShopping().setIndividualCategoriesViewState(getIndividualCategoriesRecyclerView().getLayoutManager().onSaveInstanceState());
        getShopping().setIndividualStoresViewState(getIndividualStoresRecyclerView().getLayoutManager().onSaveInstanceState());
        getFullInventoryRecyclerView().setAdapter(null);
        getSearchInventoryRecyclerView().setAdapter(null);
        getIndividualCategoriesRecyclerView().setAdapter(null);
        getIndividualStoresRecyclerView().setAdapter(null);
        super.onDestroyView();
    }

}