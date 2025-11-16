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
import java.util.Objects;

public class FullInventory extends Fragment {

    private Shopping shopping;
    private ItemData itemData;

    private String currentBottomMenu;
    private final String MENU_ITEM = "item";
    private final String MENU_CATEGORY = "category";
    private final String MENU_STORE = "store";
    private final String MENU_NONE = "none";

    private RecyclerView fullInventoryRecyclerView;
    private TextView editDataBreak;
    private Button addPopup;
    private Button editPopup;
    private Button removePopup;
    private Button reorderPopup;

    private boolean menuOptionsVisible;
    private boolean searchBoxVisible;
    private boolean editControlsExpanded;
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
    private Button sortByCategory;
    private Button sortByStore;

    public FullInventory() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();
        View view = inflater.inflate(R.layout.full_inventory, container, false);

        DBStatusHelper dbStatusHelper = new DBStatusHelper(getActivity());
        DBStoreHelper dbStoreHelper = new DBStoreHelper(getActivity());
        DBCategoryHelper dbCategoryHelper = new DBCategoryHelper(getActivity());

        shopping = (Shopping) getActivity();
        itemData = shopping.getItemData();
        StatusData statusData = shopping.getStatusData();
        CategoryData categoryData = shopping.getCategoryData();
        StoreData storeData = shopping.getStoreData();
        itemData.updateStatusesByCategory(statusData);

        fullInventoryRecyclerView = view.findViewById(R.id.fullInventoryRecyclerView);
        fullInventoryRecyclerView.setHasFixedSize(false);
        fullInventoryRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        final FullInventoryRVA adapter = new FullInventoryRVA(shopping, itemData, categoryData, storeData, dbStatusHelper, dbStoreHelper, dbCategoryHelper);
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
        editControlsExpanded = false;

        TextView fullInventorySearchButton = view.findViewById(R.id.fullInventorySearchButton);
        searchBox = view.findViewById(R.id.searchBox);
        searchPopup = view.findViewById(R.id.searchPopup);
        TextView fullInventoryEditButton = view.findViewById(R.id.fullInventoryEditButton);
        viewAll = view.findViewById(R.id.viewAll);
        viewInStock = view.findViewById(R.id.viewInStock);
        viewNeeded = view.findViewById(R.id.viewNeeded);
        viewPaused = view.findViewById(R.id.viewPaused);
        expandContractItems = view.findViewById(R.id.expandContractItems);
        expandContractCategories = view.findViewById(R.id.expandContractCategories);
        expandContractStores = view.findViewById(R.id.expandContractStores);
        sortByStore = view.findViewById(R.id.sortByStore);
        sortByCategory = view.findViewById(R.id.sortByCategory);
        sortAlphabetical = view.findViewById(R.id.sortAlphabetical);

        addRemoveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewGroup.LayoutParams recyclerViewParams = fullInventoryRecyclerView.getLayoutParams();
                int height;

                switch (currentBottomMenu) {

                    case MENU_NONE:
                        editDataBreak.setText(R.string.category);
                        editDataBreak.setVisibility(View.VISIBLE);
                        addPopup.setVisibility(View.VISIBLE);
                        editPopup.setVisibility(View.VISIBLE);
                        removePopup.setVisibility(View.VISIBLE);
                        reorderPopup.setVisibility(View.VISIBLE);
                        currentBottomMenu = MENU_CATEGORY;
                        editControlsExpanded = true;

                        if (searchBoxVisible) {
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                    case MENU_CATEGORY:
                        editDataBreak.setVisibility(View.GONE);
                        addPopup.setVisibility(View.GONE);
                        editPopup.setVisibility(View.GONE);
                        removePopup.setVisibility(View.GONE);
                        reorderPopup.setVisibility(View.GONE);
                        currentBottomMenu = MENU_NONE;
                        editControlsExpanded = false;

                        if (searchBoxVisible) {
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                    default:
                        editDataBreak.setText(R.string.category);
                        currentBottomMenu = MENU_CATEGORY;

                }
            }
        });

        addEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewGroup.LayoutParams recyclerViewParams = fullInventoryRecyclerView.getLayoutParams();
                int height;

                switch (currentBottomMenu) {
                    case MENU_NONE:
                        editDataBreak.setText(R.string.item);
                        editDataBreak.setVisibility(View.VISIBLE);
                        addPopup.setVisibility(View.VISIBLE);
                        editPopup.setVisibility(View.VISIBLE);
                        removePopup.setVisibility(View.VISIBLE);
                        reorderPopup.setVisibility(View.VISIBLE);
                        currentBottomMenu = MENU_ITEM;
                        editControlsExpanded = true;

                        if (searchBoxVisible) {
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                    case MENU_ITEM:
                        editDataBreak.setVisibility(View.GONE);
                        addPopup.setVisibility(View.GONE);
                        editPopup.setVisibility(View.GONE);
                        removePopup.setVisibility(View.GONE);
                        reorderPopup.setVisibility(View.GONE);
                        currentBottomMenu = MENU_NONE;
                        editControlsExpanded = false;

                        if (searchBoxVisible) {
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                    default:
                        editDataBreak.setText(R.string.item);
                        currentBottomMenu = MENU_ITEM;

                }
            }
        });

        addRemoveStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewGroup.LayoutParams recyclerViewParams = fullInventoryRecyclerView.getLayoutParams();
                int height;

                switch (currentBottomMenu) {

                    case MENU_NONE:
                        editDataBreak.setText(R.string.store);
                        editDataBreak.setVisibility(View.VISIBLE);
                        addPopup.setVisibility(View.VISIBLE);
                        editPopup.setVisibility(View.VISIBLE);
                        removePopup.setVisibility(View.VISIBLE);
                        reorderPopup.setVisibility(View.VISIBLE);
                        currentBottomMenu = MENU_STORE;
                        editControlsExpanded = true;

                        if (searchBoxVisible) {
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                        } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                    case MENU_STORE:
                        editDataBreak.setVisibility(View.GONE);
                        addPopup.setVisibility(View.GONE);
                        editPopup.setVisibility(View.GONE);
                        removePopup.setVisibility(View.GONE);
                        reorderPopup.setVisibility(View.GONE);
                        currentBottomMenu = MENU_NONE;
                        editControlsExpanded = false;


                        if (searchBoxVisible) {
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
                        } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
                        recyclerViewParams.height = height;
                        fullInventoryRecyclerView.setLayoutParams(recyclerViewParams);

                    default:
                        editDataBreak.setText(R.string.store);
                        currentBottomMenu = MENU_STORE;

                }
            }
        });

        addPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentBottomMenu) {
                    case MENU_CATEGORY:
                        shopping.loadFragment(new AddCategory());
                    case MENU_ITEM:
                        shopping.loadFragment(new AddItem());
                    case MENU_STORE:
                        shopping.loadFragment(new AddStore());
                }
            }
        });

        editPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentBottomMenu) {
                    case MENU_CATEGORY:
                        shopping.loadFragment(new EditCategory());
                    case MENU_ITEM:
                        if (shopping.itemIsSelectedInInventory) {
                            shopping.editItemInInventory = true;
                            shopping.editItemInShoppingList = false;
                            shopping.loadFragment(new EditItem());
                        } else {
                            Toast.makeText(getActivity(), "Please select an item to edit.", Toast.LENGTH_SHORT).show();
                        }
                    case MENU_STORE:
                        shopping.loadFragment(new EditStore());
                }
            }
        });

        removePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentBottomMenu) {
                    case MENU_CATEGORY:
                        shopping.loadFragment(new RemoveCategory());
                    case MENU_ITEM:
                        if (shopping.itemIsSelectedInInventory) {
                            shopping.loadFragment(new RemoveItem());
                        } else {
                            Toast.makeText(getActivity(), "Please select an item to remove.", Toast.LENGTH_SHORT).show();
                        }
                    case MENU_STORE:
                        shopping.loadFragment(new RemoveStore());
                }
            }
        });

        reorderPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(currentBottomMenu) {
                    case MENU_CATEGORY:
                        shopping.loadFragment(new ReorderCategories());
                    case MENU_ITEM:
                        shopping.loadFragment(new ReorderItems());
                    case MENU_STORE:
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
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 547, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 621, getResources().getDisplayMetrics());
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
                        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 494, getResources().getDisplayMetrics());
                    } else height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 568, getResources().getDisplayMetrics());
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
                shopping.inventoryView = Shopping.INVENTORY_ALL;
                for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                    adapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        viewInStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = Shopping.INVENTORY_INSTOCK;
                for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                    adapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });


        viewNeeded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = Shopping.INVENTORY_NEEDED;
                for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                    adapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        viewPaused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventoryView = Shopping.INVENTORY_PAUSED;
                for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                    adapter.notifyItemChanged(i);
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
                shopping.inventoryView = Shopping.INVENTORY_ALL;
                for (int i = 0; i < itemData.getItemListAZ().size(); i++) {
                    adapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        sortByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventorySortBy = Shopping.SORT_BY_CATEGORY;
                shopping.inventoryView = Shopping.INVENTORY_ALL;
                for (int i = 0; i < itemData.getItemListByCategory().size(); i++) {
                    adapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        sortByStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.inventorySortBy = Shopping.SORT_BY_STORE;
                shopping.inventoryView = Shopping.INVENTORY_ALL;
                for (int i = 0; i < itemData.getItemListByStore().size(); i++) {
                    adapter.notifyItemChanged(i);
                }
                hideMenuOptions();
            }
        });

        return view;
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
        sortAlphabetical.setVisibility(View.GONE);
        sortByCategory.setVisibility(View.VISIBLE);
        sortByStore.setVisibility(View.VISIBLE);
        menuOptionsVisible = true;
    }

    @Override
    public void onDestroyView() {
        shopping.fullInventoryViewState = Objects.requireNonNull(fullInventoryRecyclerView.getLayoutManager()).onSaveInstanceState();
        fullInventoryRecyclerView.setAdapter(null);
        super.onDestroyView();
    }

}