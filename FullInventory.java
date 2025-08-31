package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FullInventory extends Fragment {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private StatusData statusData;
    private CategoryData categoryData;
    private DBStatusHelper dbStatusHelper;
    private DBStoreHelper dbStoreHelper;

    private String currentBottomMenu;
    private static final String MENU_ITEM = "item";
    private static final String MENU_CATEGORY = "category";
    private static final String MENU_STORE = "store";
    private static final String MENU_NONE = "none";

    private RecyclerView fullInventoryRecyclerView;
    private Button addRemoveCategory;
    private Button addEditItem;
    private Button addRemoveStore;
    private TextView editDataBreak;
    private Button addPopup;
    private Button editPopup;
    private Button removePopup;
    private Button reorderPopup;

    private boolean menuOptionsVisible;
    private boolean searchBoxVisible;
    private boolean editControlsExpanded;
    private TextView fullInventorySearchButton;
    private EditText searchBox;
    private LinearLayout searchPopup;
    private TextView fullInventoryEditButton;
    private Button viewAll;
    private Button viewInStock;
    private Button viewNeeded;
    private Button viewPaused;
    private Button expandContractItems;
    private Button expandContractCategories;
    private Button sortByStore;
    private Button sortByCategory;

    public FullInventory() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();
        view = inflater.inflate(R.layout.full_inventory, container, false);

        dbStatusHelper = new DBStatusHelper(getActivity());
        dbStoreHelper = new DBStoreHelper(getActivity());

        shopping = (Shopping) getActivity();
        itemData = shopping.getItemData();
        statusData = shopping.getStatusData();
        categoryData = shopping.getCategoryData();
        itemData.updateStatuses(statusData);

        fullInventoryRecyclerView = view.findViewById(R.id.fullInventoryRecyclerView);
        fullInventoryRecyclerView.setHasFixedSize(false);
        fullInventoryRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        final FullInventoryRVA adapter = new FullInventoryRVA(shopping, itemData, categoryData, dbStatusHelper, dbStoreHelper);
        fullInventoryRecyclerView.setAdapter(adapter);
        fullInventoryRecyclerView.getLayoutManager().onRestoreInstanceState(shopping.fullInventoryViewState);

        currentBottomMenu = MENU_NONE;

        addRemoveCategory = view.findViewById(R.id.addRemoveCategory);
        addEditItem = view.findViewById(R.id.addEditItem);
        addRemoveStore = view.findViewById(R.id.addRemoveStore);
        editDataBreak = view.findViewById(R.id.editDataBreak);
        addPopup = view.findViewById(R.id.addPopup);
        editPopup = view.findViewById(R.id.editPopup);
        removePopup = view.findViewById(R.id.removePopup);
        reorderPopup = view.findViewById(R.id.reorderPopup);

        menuOptionsVisible = false;
        searchBoxVisible = false;
        editControlsExpanded = false;

        fullInventorySearchButton = view.findViewById(R.id.fullInventorySearchButton);
        searchBox = view.findViewById(R.id.searchBox);
        searchPopup = view.findViewById(R.id.searchPopup);
        fullInventoryEditButton = view.findViewById(R.id.fullInventoryEditButton);
        viewAll = view.findViewById(R.id.viewAll);
        viewInStock = view.findViewById(R.id.viewInStock);
        viewNeeded = view.findViewById(R.id.viewNeeded);
        viewPaused = view.findViewById(R.id.viewPaused);
        expandContractItems = view.findViewById(R.id.expandContractItems);
        expandContractCategories = view.findViewById(R.id.expandContractCategories);
        sortByStore = view.findViewById(R.id.sortByStore);
        sortByCategory = view.findViewById(R.id.sortByCategory);

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
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 432, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 485, getResources().getDisplayMetrics());;
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
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 506, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 559, getResources().getDisplayMetrics());;
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
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 432, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 485, getResources().getDisplayMetrics());;
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
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 506, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 559, getResources().getDisplayMetrics());
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
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 432, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 485, getResources().getDisplayMetrics());
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
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 506, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 559, getResources().getDisplayMetrics());
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
                        shopping.editItemInShopByStore = false;
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
                if (currentBottomMenu == MENU_CATEGORY) {
                    shopping.loadFragment(new ReorderCategories());
                } else if (currentBottomMenu == MENU_ITEM) {
                    shopping.loadFragment(new ReorderItems());
                } else if (currentBottomMenu == MENU_STORE) {
                    shopping.loadFragment(new ReorderStores());
                }
            }
        });

        fullInventorySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchBoxVisible) {
                    searchPopup.setVisibility(View.GONE);
                    searchBox.setVisibility(View.GONE);
                    searchBoxVisible = false;

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (editControlsExpanded) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 485, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 559, getResources().getDisplayMetrics());
                    params.addRule(RelativeLayout.BELOW, R.id.fullInventoryTitle);
                    params.height = height;
                    fullInventoryRecyclerView.setLayoutParams(params);

                } else {
                    searchPopup.setVisibility(View.VISIBLE);
                    searchBox.setVisibility(View.VISIBLE);
                    searchBoxVisible = true;

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fullInventoryRecyclerView.getLayoutParams();
                    int height;
                    if (editControlsExpanded) {
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 432, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 506, getResources().getDisplayMetrics());
                    params.addRule(RelativeLayout.BELOW, R.id.searchPopup);
                    params.height = height;
                    fullInventoryRecyclerView.setLayoutParams(params);

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
                shopping.inventoryView = shopping.INVENTORY_ALL;
                for (int i = 0; i < itemData.getItemList().size(); i++) {
                    adapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        viewInStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = shopping.INVENTORY_INSTOCK;
                for (int i = 0; i < itemData.getItemList().size(); i++) {
                    adapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });


        viewNeeded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = shopping.INVENTORY_NEEDED;
                for (int i = 0; i < itemData.getItemList().size(); i++) {
                    adapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        viewPaused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = shopping.INVENTORY_PAUSED;
                for (int i = 0; i < itemData.getItemList().size(); i++) {
                    adapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        expandContractItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        expandContractCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });


        sortByStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        sortByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });

        return view;
    }

    public void hideMenuOptions() {
        viewAll.setVisibility(View.GONE);
        viewInStock.setVisibility(View.GONE);
        viewNeeded.setVisibility(View.GONE);
        viewPaused.setVisibility(View.GONE);
        expandContractItems.setVisibility(View.GONE);
        expandContractCategories.setVisibility(View.GONE);
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
        sortByStore.setVisibility(View.VISIBLE);
        sortByCategory.setVisibility(View.VISIBLE);
        menuOptionsVisible = true;
    }

    @Override
    public void onDestroyView() {
        shopping.fullInventoryViewState = fullInventoryRecyclerView.getLayoutManager().onSaveInstanceState();
        fullInventoryRecyclerView.setAdapter(null);
        super.onDestroyView();
    }

}