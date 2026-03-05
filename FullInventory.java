package ryan.android.shopping;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class FullInventory extends Fragment {

    private Shopping shopping;
    private FullInventoryRVA adapter;
    private View rootView;

    private String currentBottomMenu;
    private static final String MENU_ITEM = "item";
    private static final String MENU_CATEGORY = "category";
    private static final String MENU_STORE = "store";
    private static final String MENU_NONE = "none";

    private TextView editDataBreak;
    private Button addPopup;
    private Button editPopup;
    private Button removePopup;
    private Button reorderPopup;

    private boolean menuOptionsVisible;
    private boolean searchBoxVisible;
    private boolean keyboardVisible;
    private boolean editControlsExpanded;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private RecyclerView fullInventoryRecyclerView;

    private TextView fullInventoryTitle;
    private EditText searchBox;
    private LinearLayout searchPopup;

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

    public FullInventory() {}

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();
        View view = inflater.inflate(R.layout.full_inventory, container, false);
        rootView = view.getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    if (!keyboardVisible) keyboardVisible = true;
                }
                else {
                    if (keyboardVisible) keyboardVisible = false;
                }
            }
        });

        DBStatusHelper dbStatusHelper = new DBStatusHelper(getActivity());
        DBCategoryHelper dbCategoryHelper = new DBCategoryHelper(getActivity());
        DBStoreHelper dbStoreHelper = new DBStoreHelper(getActivity());

        shopping = (Shopping) getActivity();
        final ItemData itemData = shopping.getItemData();
        final StatusData statusData = shopping.getStatusData();
        final CategoryData categoryData = shopping.getCategoryData();
        final StoreData storeData = shopping.getStoreData();
        itemData.updateStatuses(statusData);
        //itemData.printData();

        fullInventoryRecyclerView = view.findViewById(R.id.fullInventoryRecyclerView);
        fullInventoryRecyclerView.setHasFixedSize(false);
        fullInventoryRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new FullInventoryRVA(shopping, itemData, categoryData, storeData, dbStatusHelper, dbStoreHelper, dbCategoryHelper);
        fullInventoryRecyclerView.setAdapter(adapter);
        Objects.requireNonNull(fullInventoryRecyclerView.getLayoutManager()).onRestoreInstanceState(shopping.fullInventoryViewState);

        currentBottomMenu = MENU_NONE;

        Button addRemoveCategory = view.findViewById(R.id.addRemoveCategory);
        Button addEditItem = view.findViewById(R.id.addEditItem);
        Button addRemoveStore = view.findViewById(R.id.addRemoveStore);
        editDataBreak = view.findViewById(R.id.editDataBreak);
        addPopup = view.findViewById(R.id.addPopup);
        editPopup = view.findViewById(R.id.editPopup);
        removePopup = view.findViewById(R.id.removePopup);
        reorderPopup = view.findViewById(R.id.reorderPopup);

        menuOptionsVisible = false;
        searchBoxVisible = false;
        keyboardVisible = false;
        editControlsExpanded = false;

        fullInventoryTitle = view.findViewById(R.id.fullInventoryTitle);
        if (shopping.defaultSortBy.equals(Shopping.SORT_BY_CATEGORY))  {
            fullInventoryTitle.setText("By Category - All");
        } else if (shopping.defaultSortBy.equals(Shopping.SORT_BY_STORE))  {
            fullInventoryTitle.setText("By Store - All");
        } else if(shopping.defaultSortBy.equals(Shopping.SORT_ALPHABETICAL))  {
            fullInventoryTitle.setText("Alphabetical - All");
        }

        TextView searchButton = view.findViewById(R.id.searchButton);
        searchBox = view.findViewById(R.id.searchBox);
        searchPopup = view.findViewById(R.id.searchPopup);
        TextView voiceSearchButton = view.findViewById(R.id.voiceSearchButton);
        TextView clearSearchButton = view.findViewById(R.id.clearSearchButton);
        TextView refreshButton = view.findViewById(R.id.refreshButton);
        TextView fullInventoryEditButton = view.findViewById(R.id.fullInventoryEditButton);

        viewAll = view.findViewById(R.id.viewAll);
        viewInStock = view.findViewById(R.id.viewInStock);
        viewNeeded = view.findViewById(R.id.viewNeeded);
        viewPaused = view.findViewById(R.id.viewPaused);
        expandContractItems = view.findViewById(R.id.expandContractItems);
        expandContractCategories = view.findViewById(R.id.expandContractCategories);
        expandContractStores = view.findViewById(R.id.expandContractStores);
        sortAlphabetical = view.findViewById(R.id.sortAlphabetical);
        sortByCategory = view.findViewById(R.id.sortByCategory);
        sortByStore = view.findViewById(R.id.sortByStore);

        addRemoveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_NONE) {
                    editDataBreak.setText("Category");
                    editDataBreak.setVisibility(View.VISIBLE);
                    addPopup.setVisibility(View.VISIBLE);
                    editPopup.setVisibility(View.VISIBLE);
                    removePopup.setVisibility(View.VISIBLE);
                    reorderPopup.setVisibility(View.VISIBLE);
                    currentBottomMenu = MENU_CATEGORY;
                    editControlsExpanded = true;

                    ViewGroup.LayoutParams recyclerViewParams = fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (searchBoxVisible) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());;
                    recyclerViewParams.height = height;
                    fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);


                } else if (currentBottomMenu == MENU_CATEGORY) {
                    editDataBreak.setVisibility(View.GONE);
                    addPopup.setVisibility(View.GONE);
                    editPopup.setVisibility(View.GONE);
                    removePopup.setVisibility(View.GONE);
                    reorderPopup.setVisibility(View.GONE);
                    currentBottomMenu = MENU_NONE;
                    editControlsExpanded = false;

                    ViewGroup.LayoutParams recyclerViewParams = fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (searchBoxVisible) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());;
                    recyclerViewParams.height = height;
                    fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                } else {
                    editDataBreak.setText("Category");
                    currentBottomMenu = MENU_CATEGORY;
                }
            }
        });

        addEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_NONE) {
                    editDataBreak.setText("Item");
                    editDataBreak.setVisibility(View.VISIBLE);
                    addPopup.setVisibility(View.VISIBLE);
                    editPopup.setVisibility(View.VISIBLE);
                    removePopup.setVisibility(View.VISIBLE);
                    reorderPopup.setVisibility(View.VISIBLE);
                    currentBottomMenu = MENU_ITEM;
                    editControlsExpanded = true;

                    ViewGroup.LayoutParams recyclerViewParams = fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (searchBoxVisible) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());;
                    recyclerViewParams.height = height;
                    fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                } else if (currentBottomMenu == MENU_ITEM) {
                    editDataBreak.setVisibility(View.GONE);
                    addPopup.setVisibility(View.GONE);
                    editPopup.setVisibility(View.GONE);
                    removePopup.setVisibility(View.GONE);
                    reorderPopup.setVisibility(View.GONE);
                    currentBottomMenu = MENU_NONE;
                    editControlsExpanded = false;

                    ViewGroup.LayoutParams recyclerViewParams = fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (searchBoxVisible) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                    recyclerViewParams.height = height;
                    fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);
                } else {
                    editDataBreak.setText("Item");
                    currentBottomMenu = MENU_ITEM;
                }
            }
        });

        addRemoveStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_NONE) {
                    editDataBreak.setText("Store");
                    editDataBreak.setVisibility(View.VISIBLE);
                    addPopup.setVisibility(View.VISIBLE);
                    editPopup.setVisibility(View.VISIBLE);
                    removePopup.setVisibility(View.VISIBLE);
                    reorderPopup.setVisibility(View.VISIBLE);
                    currentBottomMenu = MENU_STORE;
                    editControlsExpanded = true;

                    ViewGroup.LayoutParams recyclerViewParams = fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (searchBoxVisible) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    recyclerViewParams.height = height;
                    fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                } else if (currentBottomMenu == MENU_STORE) {
                    editDataBreak.setVisibility(View.GONE);
                    addPopup.setVisibility(View.GONE);
                    editPopup.setVisibility(View.GONE);
                    removePopup.setVisibility(View.GONE);
                    reorderPopup.setVisibility(View.GONE);
                    currentBottomMenu = MENU_NONE;
                    editControlsExpanded = false;

                    ViewGroup.LayoutParams recyclerViewParams = fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (searchBoxVisible) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                    recyclerViewParams.height = height;
                    fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                } else {
                    editDataBreak.setText("Store");
                    currentBottomMenu = MENU_STORE;
                }
            }
        });

        addPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_CATEGORY) {
                    shopping.loadFragment(new AddCategory());
                } else if (currentBottomMenu == MENU_ITEM) {
                    shopping.loadFragment(new AddItem());
                } else if (currentBottomMenu == MENU_STORE) {
                    shopping.loadFragment(new AddStore());
                }
            }
        });

        editPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_CATEGORY) {
                    shopping.loadFragment(new EditCategory());
                } else if (currentBottomMenu == MENU_ITEM) {
                    if (shopping.itemIsSelectedInInventory) {
                        shopping.editItemInInventory = true;
                        shopping.editItemInShoppingList = false;
                        shopping.loadFragment(new EditItem());
                    } else {
                        Toast.makeText(getActivity(), "Please select an item to edit.", Toast.LENGTH_SHORT).show();
                    }
                } else if (currentBottomMenu == MENU_STORE) {
                    shopping.loadFragment(new EditStore());
                }
            }
        });

        removePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu == MENU_CATEGORY) {
                    shopping.loadFragment(new RemoveCategory());
                } else if (currentBottomMenu == MENU_ITEM) {
                    if (shopping.itemIsSelectedInInventory) {
                        shopping.loadFragment(new RemoveItem());
                    } else {
                        Toast.makeText(getActivity(), "Please select an item to remove.", Toast.LENGTH_SHORT).show();
                    }
                } else if (currentBottomMenu == MENU_STORE) {
                    shopping.loadFragment(new RemoveStore());
                }
            }
        });

        reorderPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBottomMenu.equals(MENU_CATEGORY)) {
                    shopping.loadFragment(new ReorderCategories());
                } else if (currentBottomMenu.equals(MENU_ITEM)) {
                    shopping.loadFragment(new ReorderItems());
                } else if (currentBottomMenu.equals(MENU_STORE)) {
                    shopping.loadFragment(new ReorderStores());
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchBoxVisible) {
                    if (keyboardVisible) {
                        // searchBox & keyboard both visible
                        shopping.hideKeyboard();
                        searchPopup.setVisibility(View.GONE);
                        searchBoxVisible = false;
                        keyboardVisible = false;
                    } else {
                        // searchBox visible but keyboard not visible
                        searchPopup.setVisibility(View.GONE);
                        searchBoxVisible = false;
                        keyboardVisible = false;
                    }

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (editControlsExpanded) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    } else
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                    params.addRule(RelativeLayout.BELOW, R.id.searchPopup);
                    params.height = height;
                    fullInventoryRecyclerView.setLayoutParams(params);

                } else {
                    // searchBox & keyboard both not visible
                    shopping.showKeyboard();
                    searchPopup.setVisibility(View.VISIBLE);
                    searchBox.requestFocus();
                    searchBox.setSelection(searchBox.getText().length());
                    searchBoxVisible = true;
                    keyboardVisible = true;

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (editControlsExpanded) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    params.addRule(RelativeLayout.BELOW, R.id.searchPopup);
                    params.height = height;
                    fullInventoryRecyclerView.setLayoutParams(params);
                }
            }
        });

        voiceSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchBoxVisible) {
                    if (keyboardVisible) {
                        // searchBox & keyboard both visible
                        shopping.hideKeyboard();
                        startVoiceRecognition();
                        searchPopup.setVisibility(View.VISIBLE);
                        searchBox.requestFocus();
                        searchBox.setSelection(searchBox.getText().length());
                        searchBoxVisible = true;
                        keyboardVisible = false;
                    } else {
                        // searchBox visible but keyboard not visible
                        searchPopup.setVisibility(View.GONE);
                        searchBoxVisible = false;
                        keyboardVisible = false;
                    }

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (editControlsExpanded) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    } else
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                    params.addRule(RelativeLayout.BELOW, R.id.searchPopup);
                    params.height = height;
                    fullInventoryRecyclerView.setLayoutParams(params);

                } else {
                    // searchBox & keyboard both not visible
                    startVoiceRecognition();
                    searchPopup.setVisibility(View.VISIBLE);
                    searchBox.requestFocus();
                    searchBox.setSelection(searchBox.getText().length());
                    searchBoxVisible = true;
                    keyboardVisible = false;

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (editControlsExpanded) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                    params.addRule(RelativeLayout.BELOW, R.id.searchPopup);
                    params.height = height;
                    fullInventoryRecyclerView.setLayoutParams(params);
                }
            }
        });

        clearSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBox.setText("");
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
            }
        });
        
        fullInventoryEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuOptionsVisible) {
                    hideMenuOptions();
                } else {
                    showMenuOptions();
                }
            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = Shopping.INVENTORY_ALL;
                if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {
                    fullInventoryTitle.setText("By Category - All");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_STORE)) {
                    fullInventoryTitle.setText("By Store - All");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                } else if (shopping.inventorySortBy.equals(Shopping.SORT_ALPHABETICAL)) {
                    fullInventoryTitle.setText("Alphabetical - All");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        viewInStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = Shopping.INVENTORY_INSTOCK;
                if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {
                    fullInventoryTitle.setText("By Category - In Stock");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_STORE)) {
                    fullInventoryTitle.setText("By Store - In Stock");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                } else if (shopping.inventorySortBy.equals(Shopping.SORT_ALPHABETICAL)) {
                    fullInventoryTitle.setText("Alphabetical - In Stock");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        viewNeeded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = Shopping.INVENTORY_NEEDED;
                if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {
                    fullInventoryTitle.setText("By Category - Needed");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_STORE)) {
                    fullInventoryTitle.setText("By Store - Needed");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                } else if (shopping.inventorySortBy.equals(Shopping.SORT_ALPHABETICAL)) {
                    fullInventoryTitle.setText("Alphabetical - Needed");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        viewPaused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = Shopping.INVENTORY_PAUSED;
                if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {
                    fullInventoryTitle.setText("By Category - Paused");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_STORE)) {
                    fullInventoryTitle.setText("By Store - Paused");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                } else if (shopping.inventorySortBy.equals(Shopping.SORT_ALPHABETICAL)) {
                    fullInventoryTitle.setText("Alphabetical - Paused");
                    //adapter.notifyDataSetChanged();
                    for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                        adapter.notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        expandContractItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                hideMenuOptions();
            }
        });

        expandContractCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                hideMenuOptions();
            }
        });

        expandContractStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                hideMenuOptions();
            }
        });

        sortAlphabetical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventorySortBy = Shopping.SORT_ALPHABETICAL;
                if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                    fullInventoryTitle.setText("Alphabetical - All");
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                    fullInventoryTitle.setText("Alphabetical - In Stock");
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                    fullInventoryTitle.setText("Alphabetical - Needed");
                }else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                    fullInventoryTitle.setText("Alphabetical - Paused");
                }
                adapter.notifyDataSetChanged();
                /*for (int i = 0; i < itemData.getItemListBy().size(); i++) {
                    adapter.notifyItemChanged(i);
                }*/
                hideMenuOptions();
            }
        });

        sortByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventorySortBy = Shopping.SORT_BY_CATEGORY;
                if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                    fullInventoryTitle.setText("By Category - All");
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                    fullInventoryTitle.setText("By Category - In Stock");
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                    fullInventoryTitle.setText("By Category - Needed");
                }else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                    fullInventoryTitle.setText("By Category - Paused");
                }
                adapter.notifyDataSetChanged();
                /*for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                    adapter.notifyItemChanged(i);
                }*/
                hideMenuOptions();
            }
        });

        sortByStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventorySortBy = Shopping.SORT_BY_STORE;
                if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                    fullInventoryTitle.setText("By Store - All");
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                    fullInventoryTitle.setText("By Store - In Stock");
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                    fullInventoryTitle.setText("By Store - Needed");
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                    fullInventoryTitle.setText("By Store - Paused");
                }
                adapter.notifyDataSetChanged();
                /*for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                    adapter.notifyItemChanged(i);
                }*/
                hideMenuOptions();
            }
        });

        fullInventoryRecyclerView.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {

            @Override
            public void onSwipeRight() {
                Toast.makeText(getActivity(), "Swipe Right", Toast.LENGTH_SHORT).show();
                switch(shopping.inventorySortBy) {
                    case Shopping.SORT_BY_CATEGORY:
                        shopping.inventorySortBy = Shopping.SORT_ALPHABETICAL;
                        if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                            fullInventoryTitle.setText("Alphabetical - All");
                            //adapter.notifyDataSetChanged();
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                            fullInventoryTitle.setText("Alphabetical - In Stock");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                            fullInventoryTitle.setText("Alphabetical - Needed");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                            fullInventoryTitle.setText("Alphabetical - Paused");
                        }
                        //adapter.notifyDataSetChanged();
                    case Shopping.SORT_BY_STORE:
                        shopping.inventorySortBy = Shopping.SORT_BY_CATEGORY;
                        if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                            fullInventoryTitle.setText("By Category - All");
                            //adapter.notifyDataSetChanged();
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                            fullInventoryTitle.setText("By Category - In Stock");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                            fullInventoryTitle.setText("By Category - Needed");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                            fullInventoryTitle.setText("By Category - Paused");
                        }
                        //adapter.notifyDataSetChanged();
                    case Shopping.SORT_ALPHABETICAL:
                        shopping.inventorySortBy = Shopping.SORT_BY_STORE;
                        if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                            fullInventoryTitle.setText("By Store - All");
                            //adapter.notifyDataSetChanged();
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                            fullInventoryTitle.setText("By Store - In Stock");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                            fullInventoryTitle.setText("By Store - Needed");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                            fullInventoryTitle.setText("By Store - Paused");
                        }
                        //adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onSwipeLeft() {
                Toast.makeText(getActivity(), "Swipe Left", Toast.LENGTH_SHORT).show();
                switch(shopping.inventorySortBy) {
                    case Shopping.SORT_BY_CATEGORY:
                        shopping.inventorySortBy = Shopping.SORT_BY_STORE;
                        if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                            fullInventoryTitle.setText("By Store - All");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                            fullInventoryTitle.setText("By Store - In Stock");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                            fullInventoryTitle.setText("By Store - Needed");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                            fullInventoryTitle.setText("By Store - Paused");
                        }
                        //adapter.notifyDataSetChanged();
                    case Shopping.SORT_BY_STORE:
                        shopping.inventorySortBy = Shopping.SORT_ALPHABETICAL;
                        if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                            fullInventoryTitle.setText("Alphabetical - All");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                            fullInventoryTitle.setText("Alphabetical - In Stock");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                            fullInventoryTitle.setText("Alphabetical - Needed");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                            fullInventoryTitle.setText("Alphabetical - Paused");
                        }
                        //adapter.notifyDataSetChanged();
                    case Shopping.SORT_ALPHABETICAL:
                        shopping.inventorySortBy = Shopping.SORT_BY_CATEGORY;
                        if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                            fullInventoryTitle.setText("By Category - All");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                            fullInventoryTitle.setText("By Category - In Stock");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                            fullInventoryTitle.setText("By Category - Needed");
                        } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                            fullInventoryTitle.setText("By Category - Paused");
                        }
                        //adapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }

    public void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchBox.setText(result.get(0));
                    searchBox.setSelection(searchBox.getText().length());
                }
                break;
            }
        }
    }

    public void hideMenuOptions() {
        viewAll.setVisibility(View.GONE);
        viewInStock.setVisibility(View.GONE);
        viewNeeded.setVisibility(View.GONE);
        viewPaused.setVisibility(View.GONE);
        expandContractItems.setVisibility(View.GONE);
        expandContractCategories.setVisibility(View.GONE);
        expandContractStores.setVisibility(View.GONE);
        sortAlphabetical.setVisibility(View.GONE);
        sortByStore.setVisibility(View.GONE);
        sortByCategory.setVisibility(View.GONE);
        menuOptionsVisible = false;
    }

    public void showMenuOptions() {
        viewAll.setVisibility(View.VISIBLE);
        viewInStock.setVisibility(View.VISIBLE);
        viewNeeded.setVisibility(View.VISIBLE);
        viewPaused.setVisibility(View.VISIBLE);
        expandContractItems.setVisibility(View.VISIBLE);
        expandContractCategories.setVisibility(View.VISIBLE);
        expandContractStores.setVisibility(View.VISIBLE);
        sortAlphabetical.setVisibility(View.VISIBLE);
        sortByStore.setVisibility(View.VISIBLE);
        sortByCategory.setVisibility(View.VISIBLE);
        menuOptionsVisible = true;
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new OnSwipeTouchListener.GestureListener());
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) return false;
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                        && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0) onSwipeRight();
                    else onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }

    @Override
    public void onDestroyView() {
        shopping.fullInventoryViewState = Objects.requireNonNull(fullInventoryRecyclerView.getLayoutManager()).onSaveInstanceState();
        fullInventoryRecyclerView.setAdapter(null);
        super.onDestroyView();
    }
}