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
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;

public class FullInventory extends Fragment {

    private Shopping shopping;
    private View rootView;
    private ItemData itemData;
    private CategoryData categoryData;
    private StoreData storeData;
    private FullInventoryRVA fullInventoryAdapter;
    private SearchInventoryRVA searchInventoryAdapter;

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
    private RecyclerView searchInventoryRecyclerView;

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

    private TextView fullInventoryOptionsBackground;

    public FullInventory() {}

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

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
        itemData = shopping.getItemData();
        StatusData statusData = shopping.getStatusData();
        categoryData = shopping.getCategoryData();
        storeData = shopping.getStoreData();
        itemData.updateStatuses(statusData);

        fullInventoryRecyclerView = view.findViewById(R.id.fullInventoryRecyclerView);
        fullInventoryRecyclerView.setHasFixedSize(false);
        fullInventoryRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        fullInventoryAdapter = new FullInventoryRVA(shopping, itemData, categoryData, storeData, dbStatusHelper, dbStoreHelper, dbCategoryHelper);
        fullInventoryRecyclerView.setAdapter(fullInventoryAdapter);
        Objects.requireNonNull(fullInventoryRecyclerView.getLayoutManager()).onRestoreInstanceState(shopping.fullInventoryViewState);

        SearchInventory searchInventory = shopping.getSearchInventory();
        searchInventoryRecyclerView = view.findViewById(R.id.searchInventoryRecyclerView);
        searchInventoryRecyclerView.setHasFixedSize(false);
        searchInventoryRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        searchInventoryAdapter = new SearchInventoryRVA(shopping, searchInventory);
        searchInventoryRecyclerView.setAdapter(searchInventoryAdapter);
        //Objects.requireNonNull(searchInventoryRecyclerView.getLayoutManager()).onRestoreInstanceState(shopping.searchInventoryViewState);

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
        switch (shopping.inventorySortBy) {
            case Shopping.SORT_BY_CATEGORY:
                fullInventoryTitle.setText(R.string.byCategoryAll);
                break;
            case Shopping.SORT_BY_STORE:
                fullInventoryTitle.setText(R.string.byStoreAll);
                break;
            case Shopping.SORT_ALPHABETICAL:
                fullInventoryTitle.setText(R.string.alphabeticalAll);
                break;
        }

        TextView searchButton = view.findViewById(R.id.searchButton);
        searchBox = view.findViewById(R.id.searchBox);
        searchPopup = view.findViewById(R.id.searchPopup);
        //searchResults = view.findViewById(R.id.searchResults);
        TextView voiceSearchButton = view.findViewById(R.id.voiceSearchButton);
        TextView clearSearchButton = view.findViewById(R.id.clearSearchButton);
        TextView refreshButton = view.findViewById(R.id.refreshButton);
        TextView fullInventoryEditButton = view.findViewById(R.id.fullInventoryEditButton);

        fullInventoryOptionsBackground = view.findViewById(R.id.fullInventoryOptionsBackground);
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
        homeScreen = view.findViewById(R.id.homeScreen);

        addRemoveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentBottomMenu) {
                    case MENU_NONE: {
                        editDataBreak.setText(R.string.category);
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
                        } else
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);


                        break;
                    }
                    case MENU_CATEGORY: {
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
                        } else
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                        break;
                    }
                    default:
                        editDataBreak.setText(R.string.category);
                        currentBottomMenu = MENU_CATEGORY;
                        break;
                }
            }
        });

        addEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentBottomMenu) {
                    case MENU_NONE: {
                        editDataBreak.setText(R.string.item);
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
                        } else
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                        break;
                    }
                    case MENU_ITEM: {
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
                        } else
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);
                        break;
                    }
                    default:
                        editDataBreak.setText(R.string.item);
                        currentBottomMenu = MENU_ITEM;
                        break;
                }
            }
        });

        addRemoveStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentBottomMenu) {
                    case MENU_NONE: {
                        editDataBreak.setText(R.string.store);
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
                        } else
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                        break;
                    }
                    case MENU_STORE: {
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
                        } else
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                        break;
                    }
                    default:
                        editDataBreak.setText(R.string.store);
                        currentBottomMenu = MENU_STORE;
                        break;
                }
            }
        });

        addPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentBottomMenu) {
                    case MENU_CATEGORY:
                        shopping.loadFragment(new AddCategory());
                        break;
                    case MENU_ITEM:
                        shopping.loadFragment(new AddItem());
                        break;
                    case MENU_STORE:
                        shopping.loadFragment(new AddStore());
                        break;
                }
            }
        });

        editPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentBottomMenu) {
                    case MENU_CATEGORY:
                        shopping.loadFragment(new EditCategory());
                        break;
                    case MENU_ITEM:
                        if (shopping.itemIsSelectedInInventory) {
                            shopping.editItemInInventory = true;
                            shopping.editItemInShoppingList = false;
                            shopping.loadFragment(new EditItem());
                        } else {
                            Toast.makeText(getActivity(), "Please select an item to edit.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MENU_STORE:
                        shopping.loadFragment(new EditStore());
                        break;
                }
            }
        });

        removePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentBottomMenu) {
                    case MENU_CATEGORY:
                        shopping.loadFragment(new RemoveCategory());
                        break;
                    case MENU_ITEM:
                        if (shopping.itemIsSelectedInInventory) {
                            shopping.loadFragment(new RemoveItem());
                        } else {
                            Toast.makeText(getActivity(), "Please select an item to remove.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MENU_STORE:
                        shopping.loadFragment(new RemoveStore());
                        break;
                }
            }
        });

        reorderPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentBottomMenu) {
                    case MENU_CATEGORY:
                        shopping.loadFragment(new ReorderCategories());
                        break;
                    case MENU_ITEM:
                        shopping.loadFragment(new ReorderItems());
                        break;
                    case MENU_STORE:
                        shopping.loadFragment(new ReorderStores());
                        break;
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
                        //searchResults.setVisibility(View.GONE);
                        searchInventoryRecyclerView.setVisibility(View.GONE);
                        searchBoxVisible = false;
                        keyboardVisible = false;
                    } else {
                        // searchBox visible but keyboard not visible
                        searchPopup.setVisibility(View.GONE);
                        searchInventoryRecyclerView.setVisibility(View.GONE);
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
                    //searchResults.setVisibility(View.VISIBLE);
                    searchInventoryRecyclerView.setVisibility(View.VISIBLE);
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
                        searchInventoryRecyclerView.setVisibility(View.VISIBLE);
                        //searchResults.setVisibility(View.VISIBLE);
                        searchBox.requestFocus();
                        searchBox.setSelection(searchBox.getText().length());
                        searchBoxVisible = true;
                        keyboardVisible = false;
                    } else {
                        // searchBox visible but keyboard not visible
                        searchPopup.setVisibility(View.GONE);
                        searchInventoryRecyclerView.setVisibility(View.GONE);
                        //searchResults.setVisibility(View.GONE);
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
                    searchInventoryRecyclerView.setVisibility(View.VISIBLE);
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

        searchBox.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchInventoryAdapter.setCurrentTerm(searchBox.getText().toString());
                searchInventoryAdapter.notifyDataSetChanged();
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
                for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                    fullInventoryAdapter.notifyItemChanged(i);
                }
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
                switch (shopping.inventorySortBy) {
                    case Shopping.SORT_BY_CATEGORY:
                        fullInventoryTitle.setText(R.string.byCategoryAll);
                        for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                    case Shopping.SORT_BY_STORE:
                        fullInventoryTitle.setText(R.string.byStoreAll);
                        for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                    case Shopping.SORT_ALPHABETICAL:
                        fullInventoryTitle.setText(R.string.alphabeticalAll);
                        for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                }
                hideMenuOptions();
            }
        });

        viewInStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = Shopping.INVENTORY_INSTOCK;
                switch (shopping.inventorySortBy) {
                    case Shopping.SORT_BY_CATEGORY:
                        fullInventoryTitle.setText(R.string.byCategoryInStock);
                        for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                    case Shopping.SORT_BY_STORE:
                        fullInventoryTitle.setText(R.string.byStoreInStock);
                        for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                    case Shopping.SORT_ALPHABETICAL:
                        fullInventoryTitle.setText(R.string.alphabeticalInStock);
                        for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                }
                hideMenuOptions();
            }
        });

        viewNeeded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = Shopping.INVENTORY_NEEDED;
                switch (shopping.inventorySortBy) {
                    case Shopping.SORT_BY_CATEGORY:
                        fullInventoryTitle.setText(R.string.byCategoryNeeded);
                        for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                    case Shopping.SORT_BY_STORE:
                        fullInventoryTitle.setText(R.string.byStoreNeeded);
                        for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                    case Shopping.SORT_ALPHABETICAL:
                        fullInventoryTitle.setText(R.string.alphabeticalNeeded);
                        for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                }
                hideMenuOptions();
            }
        });

        viewPaused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = Shopping.INVENTORY_PAUSED;
                switch (shopping.inventorySortBy) {
                    case Shopping.SORT_BY_CATEGORY:
                        fullInventoryTitle.setText(R.string.byCategoryPaused);
                        for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                    case Shopping.SORT_BY_STORE:
                        fullInventoryTitle.setText(R.string.byStorePaused);
                        for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                    case Shopping.SORT_ALPHABETICAL:
                        fullInventoryTitle.setText(R.string.alphabeticalPaused);
                        for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                        break;
                }
                hideMenuOptions();
            }
        });

        expandContractItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopping.itemExpansion.equals(Shopping.ITEMS_CONTRACTED)) {
                    shopping.itemExpansion = Shopping.ITEMS_EXPANDED;
                    for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                        Item item = itemData.getItemListByCategory().get(i);
                        item.getStatus().setAsExpandedInInventory();
                    }
                    for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                        fullInventoryAdapter.notifyItemChanged(i);
                    }
                } else if (shopping.itemExpansion.equals(Shopping.ITEMS_EXPANDED)) {
                    shopping.itemExpansion = Shopping.ITEMS_CONTRACTED;
                    for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                        Item item = itemData.getItemListByCategory().get(i);
                        item.getStatus().setAsContractedInInventory();
                    }
                    for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                        fullInventoryAdapter.notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        expandContractCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopping.categoryTitles.equals(Shopping.TITLES_EXPANDED)) {
                    shopping.categoryTitles = Shopping.TITLES_CONTRACTED;
                    for (int i = 0; i < categoryData.getCategoryList().size(); i++) {
                        itemData.getCategoryMap().get(categoryData.getCategoryList().get(i)).setAsContracted();
                    }
                    for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                        fullInventoryAdapter.notifyItemChanged(i);
                    }
                } else if (shopping.categoryTitles.equals(Shopping.TITLES_CONTRACTED)) {
                    shopping.categoryTitles = Shopping.TITLES_EXPANDED;
                    for (int i = 0; i < categoryData.getCategoryList().size(); i++) {
                        itemData.getCategoryMap().get(categoryData.getCategoryList().get(i)).setAsExpanded();
                    }
                    for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                        fullInventoryAdapter.notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        expandContractStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopping.storeTitles.equals(Shopping.TITLES_EXPANDED)) {
                    shopping.storeTitles = Shopping.TITLES_CONTRACTED;
                    for (int i = 0; i < storeData.getStoreList().size(); i++) {
                        itemData.getStoreMap().get(storeData.getStoreList().get(i)).setAsContracted();
                    }
                    for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                        fullInventoryAdapter.notifyItemChanged(i);
                    }
                } else if (shopping.storeTitles.equals(Shopping.TITLES_CONTRACTED)) {
                    shopping.storeTitles = Shopping.TITLES_EXPANDED;
                    for (int i = 0; i < storeData.getStoreList().size(); i++) {
                        itemData.getStoreMap().get(storeData.getStoreList().get(i)).setAsExpanded();
                    }
                    for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                        fullInventoryAdapter.notifyItemChanged(i);
                    }
                }
                hideMenuOptions();
            }
        });

        sortAlphabetical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventorySortBy = Shopping.SORT_ALPHABETICAL;
                switch (shopping.inventoryView) {
                    case Shopping.INVENTORY_ALL:
                        fullInventoryTitle.setText(R.string.alphabeticalAll);
                        break;
                    case Shopping.INVENTORY_INSTOCK:
                        fullInventoryTitle.setText(R.string.alphabeticalInStock);
                        break;
                    case Shopping.INVENTORY_NEEDED:
                        fullInventoryTitle.setText(R.string.alphabeticalNeeded);
                        break;
                    case Shopping.INVENTORY_PAUSED:
                        fullInventoryTitle.setText(R.string.alphabeticalPaused);
                        break;
                }
                for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                    fullInventoryAdapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        sortByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventorySortBy = Shopping.SORT_BY_CATEGORY;
                switch (shopping.inventoryView) {
                    case Shopping.INVENTORY_ALL:
                        fullInventoryTitle.setText(R.string.byCategoryAll);
                        break;
                    case Shopping.INVENTORY_INSTOCK:
                        fullInventoryTitle.setText(R.string.byCategoryInStock);
                        break;
                    case Shopping.INVENTORY_NEEDED:
                        fullInventoryTitle.setText(R.string.byCategoryNeeded);
                        break;
                    case Shopping.INVENTORY_PAUSED:
                        fullInventoryTitle.setText(R.string.byCategoryPaused);
                        break;
                }
                for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                    fullInventoryAdapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        sortByStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventorySortBy = Shopping.SORT_BY_STORE;
                switch (shopping.inventoryView) {
                    case Shopping.INVENTORY_ALL:
                        fullInventoryTitle.setText(R.string.byStoreAll);
                        break;
                    case Shopping.INVENTORY_INSTOCK:
                        fullInventoryTitle.setText(R.string.byStoreInStock);
                        break;
                    case Shopping.INVENTORY_NEEDED:
                        fullInventoryTitle.setText(R.string.byStoreNeeded);
                        break;
                    case Shopping.INVENTORY_PAUSED:
                        fullInventoryTitle.setText(R.string.byStorePaused);
                        break;
                }
                for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                    fullInventoryAdapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        homeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.loadFragment(new LoadScreen());
            }
        });

        fullInventoryRecyclerView.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {

            @Override
            public void onSwipeRight() {
                Toast.makeText(getActivity(), "Swipe Right", Toast.LENGTH_SHORT).show();
                switch(shopping.inventorySortBy) {
                    case Shopping.SORT_BY_CATEGORY:
                        shopping.inventorySortBy = Shopping.SORT_ALPHABETICAL;
                        switch (shopping.inventoryView) {
                            case Shopping.INVENTORY_ALL:
                                fullInventoryTitle.setText(R.string.alphabeticalAll);
                                break;
                            case Shopping.INVENTORY_INSTOCK:
                                fullInventoryTitle.setText(R.string.alphabeticalInStock);
                                break;
                            case Shopping.INVENTORY_NEEDED:
                                fullInventoryTitle.setText(R.string.alphabeticalNeeded);
                                break;
                            case Shopping.INVENTORY_PAUSED:
                                fullInventoryTitle.setText(R.string.alphabeticalPaused);
                                break;
                        }
                        for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                    case Shopping.SORT_BY_STORE:
                        shopping.inventorySortBy = Shopping.SORT_BY_CATEGORY;
                        switch (shopping.inventoryView) {
                            case Shopping.INVENTORY_ALL:
                                fullInventoryTitle.setText(R.string.byCategoryAll);
                                break;
                            case Shopping.INVENTORY_INSTOCK:
                                fullInventoryTitle.setText(R.string.byCategoryInStock);
                                break;
                            case Shopping.INVENTORY_NEEDED:
                                fullInventoryTitle.setText(R.string.byCategoryNeeded);
                                break;
                            case Shopping.INVENTORY_PAUSED:
                                fullInventoryTitle.setText(R.string.byCategoryPaused);
                                break;
                        }
                        for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                    case Shopping.SORT_ALPHABETICAL:
                        shopping.inventorySortBy = Shopping.SORT_BY_STORE;
                        switch (shopping.inventoryView) {
                            case Shopping.INVENTORY_ALL:
                                fullInventoryTitle.setText(R.string.byStoreAll);
                                break;
                            case Shopping.INVENTORY_INSTOCK:
                                fullInventoryTitle.setText(R.string.byStoreInStock);
                                break;
                            case Shopping.INVENTORY_NEEDED:
                                fullInventoryTitle.setText(R.string.byStoreNeeded);
                                break;
                            case Shopping.INVENTORY_PAUSED:
                                fullInventoryTitle.setText(R.string.byStorePaused);
                                break;
                        }
                        for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                }
            }

            @Override
            public void onSwipeLeft() {
                Toast.makeText(getActivity(), "Swipe Left", Toast.LENGTH_SHORT).show();
                switch(shopping.inventorySortBy) {
                    case Shopping.SORT_BY_CATEGORY:
                        shopping.inventorySortBy = Shopping.SORT_BY_STORE;
                        switch (shopping.inventoryView) {
                            case Shopping.INVENTORY_ALL:
                                fullInventoryTitle.setText(R.string.byStoreAll);
                                break;
                            case Shopping.INVENTORY_INSTOCK:
                                fullInventoryTitle.setText(R.string.byStoreInStock);
                                break;
                            case Shopping.INVENTORY_NEEDED:
                                fullInventoryTitle.setText(R.string.byStoreNeeded);
                                break;
                            case Shopping.INVENTORY_PAUSED:
                                fullInventoryTitle.setText(R.string.byStorePaused);
                                break;
                        }
                        for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                    case Shopping.SORT_BY_STORE:
                        shopping.inventorySortBy = Shopping.SORT_ALPHABETICAL;
                        switch (shopping.inventoryView) {
                            case Shopping.INVENTORY_ALL:
                                fullInventoryTitle.setText(R.string.alphabeticalAll);
                                break;
                            case Shopping.INVENTORY_INSTOCK:
                                fullInventoryTitle.setText(R.string.alphabeticalInStock);
                                break;
                            case Shopping.INVENTORY_NEEDED:
                                fullInventoryTitle.setText(R.string.alphabeticalNeeded);
                                break;
                            case Shopping.INVENTORY_PAUSED:
                                fullInventoryTitle.setText(R.string.alphabeticalPaused);
                                break;
                        }
                        for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                    case Shopping.SORT_ALPHABETICAL:
                        shopping.inventorySortBy = Shopping.SORT_BY_CATEGORY;
                        switch (shopping.inventoryView) {
                            case Shopping.INVENTORY_ALL:
                                fullInventoryTitle.setText(R.string.byCategoryAll);
                                break;
                            case Shopping.INVENTORY_INSTOCK:
                                fullInventoryTitle.setText(R.string.byCategoryInStock);
                                break;
                            case Shopping.INVENTORY_NEEDED:
                                fullInventoryTitle.setText(R.string.byCategoryNeeded);
                                break;
                            case Shopping.INVENTORY_PAUSED:
                                fullInventoryTitle.setText(R.string.byCategoryPaused);
                                break;
                        }
                        for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                            fullInventoryAdapter.notifyItemChanged(i);
                        }
                }
            }
        });

        return view;
    }

    private void startVoiceRecognition() {
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

    private void hideMenuOptions() {
        viewAll.setVisibility(View.GONE);
        viewInStock.setVisibility(View.GONE);
        viewNeeded.setVisibility(View.GONE);
        viewPaused.setVisibility(View.GONE);
        expandContractItems.setVisibility(View.GONE);
        expandContractCategories.setVisibility(View.GONE);
        expandContractStores.setVisibility(View.GONE);
        sortAlphabetical.setVisibility(View.GONE);
        sortByCategory.setVisibility(View.GONE);
        sortByStore.setVisibility(View.GONE);
        homeScreen.setVisibility(View.GONE);
        fullInventoryOptionsBackground.setVisibility(View.GONE);
        menuOptionsVisible = false;
    }

    private void showMenuOptions() {
        viewAll.setVisibility(View.VISIBLE);
        viewInStock.setVisibility(View.VISIBLE);
        viewNeeded.setVisibility(View.VISIBLE);
        viewPaused.setVisibility(View.VISIBLE);
        expandContractItems.setVisibility(View.VISIBLE);
        expandContractCategories.setVisibility(View.VISIBLE);
        expandContractStores.setVisibility(View.VISIBLE);
        sortAlphabetical.setVisibility(View.VISIBLE);
        sortByCategory.setVisibility(View.VISIBLE);
        sortByStore.setVisibility(View.VISIBLE);
        homeScreen.setVisibility(View.VISIBLE);
        fullInventoryOptionsBackground.setVisibility(View.VISIBLE);
        menuOptionsVisible = true;
    }

    class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new OnSwipeTouchListener.GestureListener());
        }

        void onSwipeLeft() {
        }

        void onSwipeRight() {
        }

        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

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