package ryan.android.shopping;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FullInventoryRVA extends RecyclerView.Adapter {

    private Shopping shopping;
    private ItemData itemData;
    private StoreData storeData;
    private CategoryData categoryData;
    private DBStatusHelper dbStatusHelper;
    private DBCategoryHelper dbCategoryHelper;
    private DBStoreHelper dbStoreHelper;

    FullInventoryRVA(Shopping shopping, ItemData itemData, CategoryData categoryData, StoreData storeData,
                     DBStatusHelper dbStatus, DBStoreHelper dbStore, DBCategoryHelper dbCategory) {
        this.shopping = shopping;
        this.itemData = itemData;
        this.categoryData = categoryData;
        this.storeData = storeData;
        this.dbStatusHelper = dbStatus;
        this.dbStoreHelper = dbStore;
        this.dbCategoryHelper = dbCategory;
    }

    public int getItemViewType(final int position) {
        if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {

            if (position == 0) return R.layout.sort_by_category_rv_title;
            int index = 0;
            for (int i = 0; i < categoryData.getCategoryList().size(); i++) {
                String category = categoryData.getCategoryList().get(i);
                int numItemsInCategory;
                if (itemData.getCategoryMap().get(category) == null) {
                    numItemsInCategory = 0;
                } else {
                    numItemsInCategory = itemData.getCategoryMap().get(category).getItemList().size();
                }
                index += numItemsInCategory + 1;
                if (position == index) return R.layout.sort_by_category_rv_title;
            }
            return R.layout.sort_by_category_rv_item;

        } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_STORE)) {

            if (position == 0) return R.layout.sort_by_store_rv_title;
            int index = 0;
            for (int i = 0; i < storeData.getStoreList().size(); i++) {
                String store = storeData.getStoreList().get(i);

                int numItemsInStore;
                if (itemData.getStoreMap().get(store) == null) {
                    numItemsInStore = 0;
                } else {
                    numItemsInStore = itemData.getStoreMap().get(store).getItemList().size();
                }
                index += numItemsInStore + 1;
                if (position == index) return R.layout.sort_by_store_rv_title;
            }
            return R.layout.sort_by_store_rv_item;

        } else return -1;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {

            //View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            if (viewType == R.layout.sort_by_category_rv_title) {
                return new SortByCategoryTitleRVH(view, shopping, this);
            } else if (viewType == R.layout.sort_by_category_rv_item) {
                return new SortByCategoryItemRVH(view, shopping, this, itemData, categoryData, dbStatusHelper, dbCategoryHelper);
            }  else return new RecyclerView.ViewHolder(view) {
            };

        } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_STORE)) {

            //View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            if (viewType == R.layout.sort_by_store_rv_title) {
                return new SortByStoreTitleRVH(view, shopping, this);
            } else if (viewType == R.layout.sort_by_store_rv_item) {
                return new SortByStoreItemRVH(view, shopping, this, itemData, storeData, dbStatusHelper, dbStoreHelper);
            } else return new RecyclerView.ViewHolder(view) {
            };

        } else return new RecyclerView.ViewHolder(view) {};

    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {

            Item thisItem = null;
            String category = null;
            boolean isTitle = false;
            int adjustedPosition = 0;

            if (position == 0) {
                isTitle = true;
                category = categoryData.getCategoryList().get(0);
            } else {
                int index = 0;
                adjustedPosition = position;
                for (int i = 0; i < categoryData.getCategoryList().size(); i++) {
                    category = categoryData.getCategoryList().get(i);
                    int numItemsInCategory;
                    if (itemData.getCategoryMap().get(category) == null) {
                        numItemsInCategory = 0;
                    } else {
                        numItemsInCategory = itemData.getCategoryMap().get(category).getItemList().size();
                    }
                    index += numItemsInCategory;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        category = categoryData.getCategoryList().get(i + 1);
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getCategoryMap().get(category).getItemList().get(numItemsInCategory - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (isTitle) { // titles

                SortByCategoryTitleRVH titleHolder = (SortByCategoryTitleRVH) holder;

                titleHolder.sortByCategoryRvTitle.setText(category);
                titleHolder.sortByCategoryRvTitle.setVisibility(View.VISIBLE);

                if (shopping.categoryTitles.equals(Shopping.TITLES_EXPANDED)) {
                    titleHolder.triangleButtonDown1.setVisibility(View.VISIBLE);
                    titleHolder.triangleButtonDown2.setVisibility(View.VISIBLE);
                    titleHolder.triangleButtonRight.setVisibility(View.GONE);
                    titleHolder.triangleButtonLeft.setVisibility(View.GONE);
                } else if (shopping.categoryTitles.equals(Shopping.TITLES_CONTRACTED)) {
                    titleHolder.triangleButtonDown1.setVisibility(View.GONE);
                    titleHolder.triangleButtonDown2.setVisibility(View.GONE);
                    titleHolder.triangleButtonRight.setVisibility(View.VISIBLE);
                    titleHolder.triangleButtonLeft.setVisibility(View.VISIBLE);
                }

                if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL) && categoryData.getCategoryViewAllMap().get(category) == 0) {
                    titleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                    titleHolder.triangleButtonDown1.setVisibility(View.GONE);
                    titleHolder.triangleButtonDown2.setVisibility(View.GONE);
                    titleHolder.triangleButtonRight.setVisibility(View.GONE);
                    titleHolder.triangleButtonLeft.setVisibility(View.GONE);
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK) && categoryData.getCategoryViewInStockMap().get(category) == 0) {
                    titleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                    titleHolder.triangleButtonDown1.setVisibility(View.GONE);
                    titleHolder.triangleButtonDown2.setVisibility(View.GONE);
                    titleHolder.triangleButtonRight.setVisibility(View.GONE);
                    titleHolder.triangleButtonLeft.setVisibility(View.GONE);
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED) && categoryData.getCategoryViewNeededMap().get(category) == 0) {
                    titleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                    titleHolder.triangleButtonDown1.setVisibility(View.GONE);
                    titleHolder.triangleButtonDown2.setVisibility(View.GONE);
                    titleHolder.triangleButtonRight.setVisibility(View.GONE);
                    titleHolder.triangleButtonLeft.setVisibility(View.GONE);
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED) && categoryData.getCategoryViewPausedMap().get(category) == 0) {
                    titleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                    titleHolder.triangleButtonDown1.setVisibility(View.GONE);
                    titleHolder.triangleButtonDown2.setVisibility(View.GONE);
                    titleHolder.triangleButtonRight.setVisibility(View.GONE);
                    titleHolder.triangleButtonLeft.setVisibility(View.GONE);
                }

            } else {  // item data

                SortByCategoryItemRVH itemHolder = (SortByCategoryItemRVH) holder;

                assert thisItem != null;

                if (thisItem.getStatus().isExpandedInInventory()) {
                    itemHolder.itemSmallName.setText(thisItem.getName());
                    itemHolder.itemLargeName.setText(thisItem.getName());
                    itemHolder.itemLargeBrand.setText(thisItem.getBrand());
                    itemHolder.itemLargeStore.setText(thisItem.getStore(0).toString());
                    itemHolder.triangleRight.setVisibility(View.GONE);
                    itemHolder.triangleDown.setVisibility(View.VISIBLE);
                    itemHolder.itemSmall.setVisibility(View.GONE);
                    itemHolder.itemLarge.setVisibility(View.VISIBLE);
                } else {
                    itemHolder.itemSmallName.setText(thisItem.getName());
                    itemHolder.itemLargeName.setText(thisItem.getName());
                    itemHolder.itemLargeBrand.setText(thisItem.getBrand());
                    itemHolder.itemLargeStore.setText(thisItem.getStore(0).toString());
                    itemHolder.triangleDown.setVisibility(View.GONE);
                    itemHolder.triangleRight.setVisibility(View.VISIBLE);
                    itemHolder.itemLarge.setVisibility(View.GONE);
                    itemHolder.itemSmall.setVisibility(View.VISIBLE);
                }
                if (thisItem.getStatus().isInStock()) {
                    itemHolder.itemSmallPaused.setVisibility(View.GONE);
                    itemHolder.itemLargePaused.setVisibility(View.GONE);
                    itemHolder.itemSmallNeeded.setVisibility(View.GONE);
                    itemHolder.itemLargeNeeded.setVisibility(View.GONE);
                    itemHolder.itemSmallInStock.setVisibility(View.VISIBLE);
                    itemHolder.itemLargeInStock.setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isNeeded()) {
                    itemHolder.itemSmallInStock.setVisibility(View.GONE);
                    itemHolder.itemLargeInStock.setVisibility(View.GONE);
                    itemHolder.itemSmallPaused.setVisibility(View.GONE);
                    itemHolder.itemLargePaused.setVisibility(View.GONE);
                    itemHolder.itemSmallNeeded.setVisibility(View.VISIBLE);
                    itemHolder.itemLargeNeeded.setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isPaused()) {
                    itemHolder.itemSmallNeeded.setVisibility(View.GONE);
                    itemHolder.itemLargeNeeded.setVisibility(View.GONE);
                    itemHolder.itemSmallInStock.setVisibility(View.GONE);
                    itemHolder.itemLargeInStock.setVisibility(View.GONE);
                    itemHolder.itemSmallPaused.setVisibility(View.VISIBLE);
                    itemHolder.itemLargePaused.setVisibility(View.VISIBLE);
                }
                if (thisItem.getStatus().isSelectedInInventory()) {
                    itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                    itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

                } else {
                    if (shopping.itemIsSelectedInInventory && shopping.selectedItemPositionInInventory == position) {
                        itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                        itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);
                    } else {
                        itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_unselected);
                        itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_unselected);
                    }
                }

                if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {

                    if (thisItem.getStatus().isExpandedInInventory()) {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.VISIBLE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.VISIBLE);
                    } else {
                        itemHolder.itemSmall.setVisibility(View.VISIBLE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.VISIBLE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    }

                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                    if (thisItem.getStatus().isInStock()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.VISIBLE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.VISIBLE);
                        } else {
                            itemHolder.itemSmall.setVisibility(View.VISIBLE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.VISIBLE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isNeeded()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        } else {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isPaused()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        } else {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    }

                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                    if (thisItem.getStatus().isInStock()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        } else {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isNeeded()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.VISIBLE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.VISIBLE);

                        } else {
                            itemHolder.itemSmall.setVisibility(View.VISIBLE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.VISIBLE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isPaused()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        } else {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    }

                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                    if (thisItem.getStatus().isInStock()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        } else {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isNeeded()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        } else {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isPaused()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.VISIBLE);
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.VISIBLE);
                        } else {
                            itemHolder.itemSmall.setVisibility(View.VISIBLE);
                            itemHolder.itemLarge.setVisibility(View.GONE);
                            itemHolder.triangleRight.setVisibility(View.VISIBLE);
                            itemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    }
                }
            }

        } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_STORE)) {

            Item thisItem = null;
            String store = null;
            boolean isTitle = false;
            int adjustedPosition = 0;

            if (position == 0) {
                isTitle = true;
                store = storeData.getStoreList().get(0);
            } else {
                int index = 0;
                adjustedPosition = position;
                for (int i = 0; i < storeData.getStoreList().size(); i++) {
                    store = storeData.getStoreList().get(i);
                    int numItemsInStore;
                    if (itemData.getStoreMap().get(store) == null) {
                        numItemsInStore = 0;
                    } else {
                        numItemsInStore = itemData.getStoreMap().get(store).getItemList().size();
                    }
                    index += numItemsInStore;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        store = storeData.getStoreList().get(i + 1);
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getStoreMap().get(store).getItemList().get(numItemsInStore - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (isTitle) { // titles

                SortByStoreTitleRVH titleHolder = (SortByStoreTitleRVH) holder;

                titleHolder.sortByStoreRvTitle.setText(store);
                titleHolder.sortByStoreRvTitle.setVisibility(View.VISIBLE);

                if (storeData.getStoreViewNeededMap().get(store) == 0) {
                    titleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                }

            } else {  // item data

                SortByStoreItemRVH itemHolder = (SortByStoreItemRVH) holder;

                assert thisItem != null;

                if (thisItem.getStatus().isNeeded()) {
                    if (thisItem.getStatus().isExpandedInShoppingList()) {
                        itemHolder.itemSmallName.setText(thisItem.getName());
                        itemHolder.itemLargeName.setText(thisItem.getName());
                        itemHolder.itemLargeBrand.setText(thisItem.getBrand());
                        itemHolder.itemLargeCategory.setText(thisItem.getCategory(0).toString());
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.VISIBLE);
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.VISIBLE);
                    } else {
                        itemHolder.itemSmallName.setText(thisItem.getName());
                        itemHolder.itemLargeName.setText(thisItem.getName());
                        itemHolder.itemLargeBrand.setText(thisItem.getBrand());
                        itemHolder.itemLargeCategory.setText(thisItem.getCategory(0).toString());
                        itemHolder.triangleDown.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.VISIBLE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.itemSmall.setVisibility(View.VISIBLE);
                    }

                } else {
                    itemHolder.triangleDown.setVisibility(View.GONE);
                    itemHolder.triangleRight.setVisibility(View.GONE);
                    itemHolder.itemLarge.setVisibility(View.GONE);
                    itemHolder.itemSmall.setVisibility(View.GONE);
                }

                if (thisItem.getStatus().isSelectedInShoppingList()) {
                    itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                    itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

                } else {
                    if (shopping.itemIsSelectedInShoppingList && shopping.selectedItemPositionInShoppingList == position) {
                        itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                        itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);
                    } else {
                        itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_unselected);
                        itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_unselected);
                    }
                }

                if ((shopping.storeListOrderNum != 0) &&
                        !thisItem.getStore(0).toString().equals(storeData.getStoreList().get(shopping.storeListOrderNum - 1))) {
                    itemHolder.triangleDown.setVisibility(View.GONE);
                    itemHolder.triangleRight.setVisibility(View.GONE);
                    itemHolder.itemLarge.setVisibility(View.GONE);
                    itemHolder.itemSmall.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {

            return (itemData.getItemList().size() + categoryData.getCategoryList().size());

        } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_STORE)) {

            return (itemData.getItemList().size() + storeData.getStoreList().size());

        } else return -1;
    }

    public static class SortByCategoryTitleRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView sortByCategoryRvTitle;
        private ImageView triangleButtonDown1;
        private ImageView triangleButtonDown2;
        private ImageView triangleButtonRight;
        private ImageView triangleButtonLeft;

        boolean isExpanded;
        boolean isContracted;

        SortByCategoryTitleRVH(View itemView, Shopping shopping, FullInventoryRVA adapter) {

            super(itemView);

            sortByCategoryRvTitle = itemView.findViewById(R.id.sortByCategoryRvTitle);
            triangleButtonDown1 = itemView.findViewById(R.id.triangleButtonDown1);
            triangleButtonDown2 = itemView.findViewById(R.id.triangleButtonDown2);
            triangleButtonRight = itemView.findViewById(R.id.triangleButtonRight);
            triangleButtonLeft = itemView.findViewById(R.id.triangleButtonLeft);

            sortByCategoryRvTitle.setOnClickListener(this);
            triangleButtonDown1.setOnClickListener(this);
            triangleButtonDown2.setOnClickListener(this);
            triangleButtonRight.setOnClickListener(this);
            triangleButtonLeft.setOnClickListener(this);

            isExpanded = false;
            isContracted = true;
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == triangleButtonDown1.getId() || id == triangleButtonDown2.getId()) {
                //
            } else if (id == triangleButtonRight.getId() || id == triangleButtonLeft.getId()) {
                //
            } else if (id == sortByCategoryRvTitle.getId() && isExpanded) {
                //
            } else if (id == sortByCategoryRvTitle.getId() && isContracted) {
                //
            }
        }
    }

    public static class SortByCategoryItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private FullInventoryRVA adapter;
        private ItemData itemData;
        private CategoryData categoryData;
        private DBStatusHelper dbStatusHelper;
        private DBCategoryHelper dbCategoryHelper;

        private Button triangleRight;
        private Button triangleDown;
        private LinearLayout itemSmall;
        private LinearLayout itemLarge;
        private TextView itemSmallName;
        private TextView itemLargeName;
        private TextView itemSmallInStock;
        private TextView itemSmallNeeded;
        private TextView itemSmallPaused;
        private TextView itemLargeInStock;
        private TextView itemLargeNeeded;
        private TextView itemLargePaused;
        private TextView itemLargeBrand;
        private TextView itemLargeBrandLabel;
        private TextView itemLargeStore;
        private TextView itemLargeStoreLabel;

        public SortByCategoryItemRVH(final View itemView, Shopping shopping, FullInventoryRVA adapter, ItemData itemData,
                                    CategoryData categoryData, DBStatusHelper dbStatus, DBCategoryHelper dbCategory) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.itemData = itemData;
            this.categoryData = categoryData;
            this.dbStatusHelper = dbStatus;
            this.dbCategoryHelper = dbCategory;

            triangleRight = itemView.findViewById(R.id.triangleButtonRight);
            triangleDown = itemView.findViewById(R.id.triangleButtonDown);
            itemSmall = itemView.findViewById(R.id.itemSmall);
            itemLarge = itemView.findViewById(R.id.itemLarge);
            itemSmallName = itemView.findViewById(R.id.itemSmallName);
            itemLargeName = itemView.findViewById(R.id.itemLargeName);
            itemSmallInStock = itemView.findViewById(R.id.itemSmallInStock);
            itemSmallNeeded = itemView.findViewById(R.id.itemSmallNeeded);
            itemSmallPaused = itemView.findViewById(R.id.itemSmallPaused);
            itemLargeInStock = itemView.findViewById(R.id.itemLargeInStock);
            itemLargeNeeded = itemView.findViewById(R.id.itemLargeNeeded);
            itemLargePaused = itemView.findViewById(R.id.itemLargePaused);
            itemLargeBrand = itemView.findViewById(R.id.itemLargeBrand);
            itemLargeBrandLabel = itemView.findViewById(R.id.itemLargeBrandLabel);
            itemLargeStore = itemView.findViewById(R.id.itemLargeStore);
            itemLargeStoreLabel = itemView.findViewById(R.id.itemLargeStoreLabel);

            triangleRight.setOnClickListener(this);
            triangleDown.setOnClickListener(this);
            itemSmall.setOnClickListener(this);
            itemLarge.setOnClickListener(this);
            itemSmallName.setOnClickListener(this);
            itemLargeName.setOnClickListener(this);
            itemSmallInStock.setOnClickListener(this);
            itemSmallNeeded.setOnClickListener(this);
            itemSmallPaused.setOnClickListener(this);
            itemLargeInStock.setOnClickListener(this);
            itemLargeNeeded.setOnClickListener(this);
            itemLargePaused.setOnClickListener(this);
            itemLargeBrand.setOnClickListener(this);
            itemLargeBrandLabel.setOnClickListener(this);
            itemLargeStore.setOnClickListener(this);
            itemLargeStoreLabel.setOnClickListener(this);
        }

        private void selectOrUnselectItem(int position) {

            Item thisItem = null;
            String category = null;
            boolean isTitle = false;
            int adjustedPosition = 0;

            if (position == 0) {
                isTitle = true;
                category = categoryData.getCategoryList().get(0);
            } else {
                int index = 0;
                adjustedPosition = position;
                for (int i = 0; i < categoryData.getCategoryList().size(); i++) {
                    category = categoryData.getCategoryList().get(i);
                    int numItemsInCategory;
                    if (itemData.getCategoryMap().get(category) == null) {
                        numItemsInCategory = 0;
                    } else {
                        numItemsInCategory = itemData.getCategoryMap().get(category).getItemList().size();
                    }
                    index += numItemsInCategory;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        category = categoryData.getCategoryList().get(i + 1);
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getCategoryMap().get(category).getItemList().get(numItemsInCategory - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

                assert thisItem != null;

                if (thisItem.getStatus().isSelectedInInventory() || thisItem == shopping.selectedItemInInventory) {
                    // selected item is this item
                    thisItem.getStatus().setAsUnselectedInInventory();
                    itemSmall.setBackgroundResource(R.drawable.list_outline_unselected);
                    itemLarge.setBackgroundResource(R.drawable.list_outline_unselected);

                    shopping.itemIsSelectedInInventory = false;
                    shopping.selectedItemInInventory = null;
                } else {
                    if (shopping.itemIsSelectedInInventory) {
                        // selected item is another item
                        int currentlySelected = shopping.selectedItemPositionInInventory;
                        thisItem.getStatus().setAsSelectedInInventory();
                        itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                        itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

                        shopping.selectedItemPositionInInventory = position;
                        shopping.itemIsSelectedInInventory = true;
                        shopping.selectedItemInInventory = thisItem;

                        Item lastItem = getItemWithCategories(currentlySelected);
                        if (lastItem != null) {
                            lastItem.getStatus().setAsUnselectedInInventory();
                            adapter.notifyItemChanged(currentlySelected);
                        }

                    } else {
                        // nothing is selected
                        thisItem.getStatus().setAsSelectedInInventory();
                        itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                        itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

                        shopping.selectedItemPositionInInventory = position;
                        shopping.itemIsSelectedInInventory = true;
                        shopping.selectedItemInInventory = thisItem;
                    }
                }
            }
        }

        public Item getItemWithCategories(int position) {

            Item thisItem = null;
            String category = null;
            boolean isTitle = false;
            int adjustedPosition = 0;

            if (position == 0) {
                isTitle = true;
                category = categoryData.getCategoryList().get(0);
            } else {
                int index = 0;
                adjustedPosition = position;
                for (int i = 0; i < categoryData.getCategoryList().size(); i++) {
                    category = categoryData.getCategoryList().get(i);
                    int numItemsInCategory;
                    if (itemData.getCategoryMap().get(category) == null) {
                        numItemsInCategory = 0;
                    } else {
                        numItemsInCategory = itemData.getCategoryMap().get(category).getItemList().size();
                    }
                    index += numItemsInCategory;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        category = categoryData.getCategoryList().get(i + 1);
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getCategoryMap().get(category).getItemList().get(numItemsInCategory - index + adjustedPosition);
                        break;
                    }
                }
            }
            return thisItem;
        }

        @Override
        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            Item thisItem = null;
            String category = null;
            boolean isTitle = false;
            int adjustedPosition = 0;

            if (position == 0) {
                isTitle = true;
                category = categoryData.getCategoryList().get(0);
            } else {
                int index = 0;
                adjustedPosition = position;
                for (int i = 0; i < categoryData.getCategoryList().size(); i++) {
                    category = categoryData.getCategoryList().get(i);
                    int numItemsInCategory;
                    if (itemData.getCategoryMap().get(category) == null) {
                        numItemsInCategory = 0;
                    } else {
                        numItemsInCategory = itemData.getCategoryMap().get(category).getItemList().size();
                    }
                    index += numItemsInCategory;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        category = categoryData.getCategoryList().get(i + 1);
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getCategoryMap().get(category).getItemList().get(numItemsInCategory - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

                assert thisItem != null;

                if (id == itemSmallName.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == itemLargeName.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == itemLargeBrandLabel.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == itemLargeBrand.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == itemLargeStoreLabel.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == itemLargeStore.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == triangleRight.getId()) {
                    if (triangleRight.getVisibility() == View.VISIBLE && triangleDown.getVisibility() == View.GONE) {
                        triangleRight.setVisibility(View.GONE);
                        triangleDown.setVisibility(View.VISIBLE);
                        itemSmall.setVisibility(View.GONE);
                        itemLarge.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsExpandedInInventory();
                        //shopping.getClickedInventoryList().set(adjustedPosition, true);
                    }
                } else if (id == triangleDown.getId()) {
                    if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                        triangleDown.setVisibility(View.GONE);
                        triangleRight.setVisibility(View.VISIBLE);
                        itemLarge.setVisibility(View.GONE);
                        itemSmall.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsContractedInInventory();
                        //shopping.getClickedInventoryList().set(adjustedPosition, false);
                    }
                } else if (id == itemSmallInStock.getId()) {
                    if (itemSmallInStock.getVisibility() == View.VISIBLE) {
                        itemSmallInStock.setVisibility(View.GONE);
                        itemLargeInStock.setVisibility(View.GONE);
                        itemSmallPaused.setVisibility(View.GONE);
                        itemLargePaused.setVisibility(View.GONE);
                        itemSmallNeeded.setVisibility(View.VISIBLE);
                        itemLargeNeeded.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsNeeded();
                        dbStatusHelper.updateStatus(thisItem.getName(), "needed", "unchecked");

                        String thisCategory = thisItem.getCategory(0).toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock - 1, numItemsNeeded + 1, numItemsPaused);
                        shopping.updateCategoryData();
                    }
                } else if (id == itemSmallNeeded.getId()) {
                    if (itemSmallNeeded.getVisibility() == View.VISIBLE) {
                        itemSmallNeeded.setVisibility(View.GONE);
                        itemLargeNeeded.setVisibility(View.GONE);
                        itemSmallInStock.setVisibility(View.GONE);
                        itemLargeInStock.setVisibility(View.GONE);
                        itemSmallPaused.setVisibility(View.VISIBLE);
                        itemLargePaused.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsPaused();
                        dbStatusHelper.updateStatus(thisItem.getName(), "paused", "unchecked");

                        String thisCategory = thisItem.getCategory(0).toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock, numItemsNeeded - 1, numItemsPaused + 1);
                        shopping.updateCategoryData();
                    }
                } else if (id == itemSmallPaused.getId()) {
                    if (itemSmallPaused.getVisibility() == View.VISIBLE) {
                        itemSmallPaused.setVisibility(View.GONE);
                        itemLargePaused.setVisibility(View.GONE);
                        itemSmallNeeded.setVisibility(View.GONE);
                        itemLargeNeeded.setVisibility(View.GONE);
                        itemSmallInStock.setVisibility(View.VISIBLE);
                        itemLargeInStock.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsInStock();
                        dbStatusHelper.updateStatus(thisItem.getName(), "instock", "unchecked");

                        String thisCategory = thisItem.getCategory(0).toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock + 1, numItemsNeeded, numItemsPaused - 1);
                        shopping.updateCategoryData();
                    }
                } else if (id == itemLargeInStock.getId()) {
                    if (itemLargeInStock.getVisibility() == View.VISIBLE) {
                        itemLargeInStock.setVisibility(View.GONE);
                        itemSmallInStock.setVisibility(View.GONE);
                        itemLargePaused.setVisibility(View.GONE);
                        itemSmallPaused.setVisibility(View.GONE);
                        itemLargeNeeded.setVisibility(View.VISIBLE);
                        itemSmallNeeded.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsNeeded();
                        dbStatusHelper.updateStatus(thisItem.getName(), "needed", "unchecked");

                        String thisCategory = thisItem.getCategory(0).toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock - 1, numItemsNeeded + 1, numItemsPaused);
                        shopping.updateCategoryData();
                    }
                } else if (id == itemLargeNeeded.getId()) {
                    if (itemLargeNeeded.getVisibility() == View.VISIBLE) {
                        itemLargeNeeded.setVisibility(View.GONE);
                        itemSmallNeeded.setVisibility(View.GONE);
                        itemLargeInStock.setVisibility(View.GONE);
                        itemSmallInStock.setVisibility(View.GONE);
                        itemLargePaused.setVisibility(View.VISIBLE);
                        itemSmallPaused.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsPaused();
                        dbStatusHelper.updateStatus(thisItem.getName(), "paused", "unchecked");

                        String thisCategory = thisItem.getCategory(0).toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock, numItemsNeeded - 1, numItemsPaused + 1);
                        shopping.updateCategoryData();
                    }
                } else if (id == itemLargePaused.getId()) {
                    if (itemLargePaused.getVisibility() == View.VISIBLE) {
                        itemLargePaused.setVisibility(View.GONE);
                        itemSmallPaused.setVisibility(View.GONE);
                        itemLargeNeeded.setVisibility(View.GONE);
                        itemSmallNeeded.setVisibility(View.GONE);
                        itemLargeInStock.setVisibility(View.VISIBLE);
                        itemSmallInStock.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsInStock();
                        dbStatusHelper.updateStatus(thisItem.getName(), "instock", "unchecked");

                        String thisCategory = thisItem.getCategory(0).toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock + 1, numItemsNeeded, numItemsPaused - 1);
                        shopping.updateCategoryData();
                    }
                }
            }
        }
    }

    public static class SortByStoreTitleRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView sortByStoreRvTitle;
        private ImageView triangleButtonDown1;
        private ImageView triangleButtonDown2;
        private ImageView triangleButtonRight;
        private ImageView triangleButtonLeft;

        boolean isExpanded;
        boolean isContracted;

        SortByStoreTitleRVH(View itemView, Shopping shopping, FullInventoryRVA adapter) {

            super(itemView);

            sortByStoreRvTitle = itemView.findViewById(R.id.sortByStoreRvTitle);
            triangleButtonDown1 = itemView.findViewById(R.id.triangleButtonDown1);
            triangleButtonDown2 = itemView.findViewById(R.id.triangleButtonDown2);
            triangleButtonRight = itemView.findViewById(R.id.triangleButtonRight);
            triangleButtonLeft = itemView.findViewById(R.id.triangleButtonLeft);

            sortByStoreRvTitle.setOnClickListener(this);
            triangleButtonDown1.setOnClickListener(this);
            triangleButtonDown2.setOnClickListener(this);
            triangleButtonRight.setOnClickListener(this);
            triangleButtonLeft.setOnClickListener(this);

            isExpanded = false;
            isContracted = true;
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == triangleButtonDown1.getId() || id == triangleButtonDown2.getId()) {
                //
            } else if (id == triangleButtonRight.getId() || id == triangleButtonLeft.getId()) {
                //
            } else if (id == sortByStoreRvTitle.getId() && isExpanded) {
                //
            } else if (id == sortByStoreRvTitle.getId() && isContracted) {

            }
        }
    }

    public static class SortByStoreItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private FullInventoryRVA adapter;
        private ItemData itemData;
        private StoreData storeData;
        private DBStatusHelper dbStatusHelper;
        private DBStoreHelper dbStoreHelper;

        private Button triangleRight;
        private Button triangleDown;
        private LinearLayout itemSmall;
        private LinearLayout itemLarge;
        private TextView itemSmallName;
        private TextView itemLargeName;
        private TextView itemSmallInStock;
        private TextView itemSmallNeeded;
        private TextView itemSmallPaused;
        private TextView itemLargeInStock;
        private TextView itemLargeNeeded;
        private TextView itemLargePaused;
        private TextView itemLargeBrand;
        private TextView itemLargeBrandLabel;
        private TextView itemLargeCategory;
        private TextView itemLargeCategoryLabel;

        public SortByStoreItemRVH(final View itemView, Shopping shopping, FullInventoryRVA adapter,
                                  ItemData itemData, StoreData storeData, DBStatusHelper dbStatus, DBStoreHelper dbStore) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.itemData = itemData;
            this.storeData = storeData;
            this.dbStatusHelper = dbStatus;
            this.dbStoreHelper = dbStore;

            triangleRight = itemView.findViewById(R.id.triangleButtonRight);
            triangleDown = itemView.findViewById(R.id.triangleButtonDown);
            itemSmall = itemView.findViewById(R.id.itemSmall);
            itemLarge = itemView.findViewById(R.id.itemLarge);
            itemSmallName = itemView.findViewById(R.id.itemSmallName);
            itemLargeName = itemView.findViewById(R.id.itemLargeName);
            itemSmallInStock = itemView.findViewById(R.id.itemSmallInStock);
            itemSmallNeeded = itemView.findViewById(R.id.itemSmallNeeded);
            itemSmallPaused = itemView.findViewById(R.id.itemSmallPaused);
            itemLargeInStock = itemView.findViewById(R.id.itemLargeInStock);
            itemLargeNeeded = itemView.findViewById(R.id.itemLargeNeeded);
            itemLargePaused = itemView.findViewById(R.id.itemLargePaused);
            itemLargeBrand = itemView.findViewById(R.id.itemLargeBrand);
            itemLargeBrandLabel = itemView.findViewById(R.id.itemLargeBrandLabel);
            itemLargeCategory = itemView.findViewById(R.id.itemLargeCategory);
            itemLargeCategoryLabel = itemView.findViewById(R.id.itemLargeCategoryLabel);

            triangleRight.setOnClickListener(this);
            triangleDown.setOnClickListener(this);
            itemSmall.setOnClickListener(this);
            itemLarge.setOnClickListener(this);
            itemSmallName.setOnClickListener(this);
            itemLargeName.setOnClickListener(this);
            itemSmallInStock.setOnClickListener(this);
            itemSmallNeeded.setOnClickListener(this);
            itemSmallPaused.setOnClickListener(this);
            itemLargeInStock.setOnClickListener(this);
            itemLargeNeeded.setOnClickListener(this);
            itemLargePaused.setOnClickListener(this);
            itemLargeBrand.setOnClickListener(this);
            itemLargeBrandLabel.setOnClickListener(this);
            itemLargeCategory.setOnClickListener(this);
            itemLargeCategoryLabel.setOnClickListener(this);
        }

        private void selectOrUnselectItem(int position) {

            Item thisItem = null;
            String store = null;
            boolean isTitle = false;
            int adjustedPosition = 0;

            if (position == 0) {
                isTitle = true;
                store = storeData.getStoreList().get(0);
            } else {
                int index = 0;
                adjustedPosition = position;
                for (int i = 0; i < storeData.getStoreList().size(); i++) {
                    store = storeData.getStoreList().get(i);
                    int numItemsInStore;
                    if (itemData.getStoreMap().get(store) == null) {
                        numItemsInStore = 0;
                    } else {
                        numItemsInStore = itemData.getStoreMap().get(store).getItemList().size();
                    }
                    index += numItemsInStore;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        store = storeData.getStoreList().get(i + 1);
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getStoreMap().get(store).getItemList().get(numItemsInStore - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

                assert thisItem != null;

                if (thisItem.getStatus().isSelectedInShoppingList() || thisItem == shopping.selectedItemInShoppingList) {
                    // selected item is this item
                    thisItem.getStatus().setAsUnselectedInShoppingList();
                    itemSmall.setBackgroundResource(R.drawable.list_outline_unselected);
                    itemLarge.setBackgroundResource(R.drawable.list_outline_unselected);

                    shopping.itemIsSelectedInShoppingList = false;
                    shopping.selectedItemInShoppingList = null;
                } else {
                    if (shopping.itemIsSelectedInShoppingList) {
                        // selected item is another item
                        int currentlySelected = shopping.selectedItemPositionInShoppingList;
                        thisItem.getStatus().setAsSelectedInShoppingList();
                        itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                        itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

                        shopping.selectedItemPositionInShoppingList = position;
                        shopping.itemIsSelectedInShoppingList = true;
                        shopping.selectedItemInShoppingList = thisItem;

                        Item lastItem = getItemWithStores(currentlySelected);
                        if (lastItem != null) {
                            lastItem.getStatus().setAsUnselectedInShoppingList();
                            adapter.notifyItemChanged(currentlySelected);
                        }

                    } else {
                        // nothing is selected
                        thisItem.getStatus().setAsSelectedInShoppingList();
                        itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                        itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

                        shopping.selectedItemPositionInShoppingList = position;
                        shopping.itemIsSelectedInShoppingList = true;
                        shopping.selectedItemInShoppingList = thisItem;
                    }
                }
            }
        }

        Item getItemWithStores(int position) {

            Item thisItem = null;
            String store = null;
            boolean isTitle = false;
            int adjustedPosition = 0;

            if (position == 0) {
                isTitle = true;
                store = storeData.getStoreList().get(0);
            } else {
                int index = 0;
                adjustedPosition = position;
                for (int i = 0; i < storeData.getStoreList().size(); i++) {
                    store = storeData.getStoreList().get(i);
                    int numItemsInStore;
                    if (itemData.getStoreMap().get(store) == null) {
                        numItemsInStore = 0;
                    } else {
                        numItemsInStore = itemData.getStoreMap().get(store).getItemList().size();
                    }
                    index += numItemsInStore;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        store = storeData.getStoreList().get(i + 1);
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getStoreMap().get(store).getItemList().get(numItemsInStore - index + adjustedPosition);
                        break;
                    }
                }
            }
            return thisItem;
        }

        @Override
        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            Item thisItem = null;
            String store = null;
            boolean isTitle = false;
            int adjustedPosition = 0;

            if (position == 0) {
                isTitle = true;
                store = storeData.getStoreList().get(0);
            } else {
                int index = 0;
                adjustedPosition = position;
                for (int i = 0; i < storeData.getStoreList().size(); i++) {
                    store = storeData.getStoreList().get(i);
                    int numItemsInStore;
                    if (itemData.getStoreMap().get(store) == null) {
                        numItemsInStore = 0;
                    } else {
                        numItemsInStore = itemData.getStoreMap().get(store).getItemList().size();
                    }
                    index += numItemsInStore;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        store = storeData.getStoreList().get(i + 1);
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getStoreMap().get(store).getItemList().get(numItemsInStore - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

                assert thisItem != null;

                if (id == itemSmallName.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == itemLargeName.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == itemLargeBrandLabel.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == itemLargeBrand.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == itemLargeCategoryLabel.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == itemLargeCategory.getId()) {
                    selectOrUnselectItem(position);
                } else if (id == triangleRight.getId()) {
                    if (triangleRight.getVisibility() == View.VISIBLE && triangleDown.getVisibility() == View.GONE) {
                        triangleRight.setVisibility(View.GONE);
                        triangleDown.setVisibility(View.VISIBLE);
                        itemSmall.setVisibility(View.GONE);
                        itemLarge.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsExpandedInShoppingList();
                        //shopping.getClickedShoppingList().set(adjustedPosition, true);
                    }
                } else if (id == triangleDown.getId()) {
                    if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                        triangleDown.setVisibility(View.GONE);
                        triangleRight.setVisibility(View.VISIBLE);
                        itemLarge.setVisibility(View.GONE);
                        itemSmall.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsContractedInShoppingList();
                        //shopping.getClickedShoppingList().set(adjustedPosition, false);
                    }

                } else if (id == itemSmallInStock.getId()) {
                    if (itemSmallInStock.getVisibility() == View.VISIBLE) {
                        itemSmallInStock.setVisibility(View.GONE);
                        itemLargeInStock.setVisibility(View.GONE);
                        itemSmallPaused.setVisibility(View.GONE);
                        itemLargePaused.setVisibility(View.GONE);
                        itemSmallNeeded.setVisibility(View.VISIBLE);
                        itemLargeNeeded.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsNeeded();
                        dbStatusHelper.updateStatus(thisItem.getName(), "needed", "unchecked");

                        String thisStore = thisItem.getStore(0).toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock - 1, numItemsNeeded + 1, numItemsPaused);
                        shopping.updateStoreData();
                    }
                } else if (id == itemSmallNeeded.getId()) {
                    if (itemSmallNeeded.getVisibility() == View.VISIBLE) {
                        itemSmallNeeded.setVisibility(View.GONE);
                        itemLargeNeeded.setVisibility(View.GONE);
                        itemSmallInStock.setVisibility(View.GONE);
                        itemLargeInStock.setVisibility(View.GONE);
                        itemSmallPaused.setVisibility(View.VISIBLE);
                        itemLargePaused.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsPaused();
                        dbStatusHelper.updateStatus(thisItem.getName(), "paused", "unchecked");

                        String thisStore = thisItem.getStore(0).toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock, numItemsNeeded - 1, numItemsPaused + 1);
                        shopping.updateStoreData();
                    }
                } else if (id == itemSmallPaused.getId()) {
                    if (itemSmallPaused.getVisibility() == View.VISIBLE) {
                        itemSmallPaused.setVisibility(View.GONE);
                        itemLargePaused.setVisibility(View.GONE);
                        itemSmallNeeded.setVisibility(View.GONE);
                        itemLargeNeeded.setVisibility(View.GONE);
                        itemSmallInStock.setVisibility(View.VISIBLE);
                        itemLargeInStock.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsInStock();
                        dbStatusHelper.updateStatus(thisItem.getName(), "instock", "unchecked");

                        String thisStore = thisItem.getStore(0).toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock + 1, numItemsNeeded, numItemsPaused - 1);
                        shopping.updateStoreData();
                    }
                } else if (id == itemLargeInStock.getId()) {
                    if (itemLargeInStock.getVisibility() == View.VISIBLE) {
                        itemLargeInStock.setVisibility(View.GONE);
                        itemSmallInStock.setVisibility(View.GONE);
                        itemLargePaused.setVisibility(View.GONE);
                        itemSmallPaused.setVisibility(View.GONE);
                        itemLargeNeeded.setVisibility(View.VISIBLE);
                        itemSmallNeeded.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsNeeded();
                        dbStatusHelper.updateStatus(thisItem.getName(), "needed", "unchecked");

                        String thisStore = thisItem.getStore(0).toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock - 1, numItemsNeeded + 1, numItemsPaused);
                        shopping.updateStoreData();
                    }
                } else if (id == itemLargeNeeded.getId()) {
                    if (itemLargeNeeded.getVisibility() == View.VISIBLE) {
                        itemLargeNeeded.setVisibility(View.GONE);
                        itemSmallNeeded.setVisibility(View.GONE);
                        itemLargeInStock.setVisibility(View.GONE);
                        itemSmallInStock.setVisibility(View.GONE);
                        itemLargePaused.setVisibility(View.VISIBLE);
                        itemSmallPaused.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsPaused();
                        dbStatusHelper.updateStatus(thisItem.getName(), "paused", "unchecked");

                        String thisStore = thisItem.getStore(0).toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock, numItemsNeeded - 1, numItemsPaused + 1);
                        shopping.updateStoreData();
                    }
                } else if (id == itemLargePaused.getId()) {
                    if (itemLargePaused.getVisibility() == View.VISIBLE) {
                        itemLargePaused.setVisibility(View.GONE);
                        itemSmallPaused.setVisibility(View.GONE);
                        itemLargeNeeded.setVisibility(View.GONE);
                        itemSmallNeeded.setVisibility(View.GONE);
                        itemLargeInStock.setVisibility(View.VISIBLE);
                        itemSmallInStock.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsInStock();
                        dbStatusHelper.updateStatus(thisItem.getName(), "instock", "unchecked");

                        String thisStore = thisItem.getStore(0).toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock + 1, numItemsNeeded, numItemsPaused - 1);
                        shopping.updateStoreData();
                    }
                }
            }
        }
    }
}