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

class FullInventoryRVA extends RecyclerView.Adapter {

    private final Shopping shopping;
    private final ItemData itemData;
    private final StoreData storeData;
    private final CategoryData categoryData;
    private final DBStatusHelper dbStatusHelper;
    private final DBStoreHelper dbStoreHelper;
    private final DBCategoryHelper dbCategoryHelper;

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

    @Override
    public int getItemViewType(final int position) {

        switch (shopping.inventorySortBy) {

            case Shopping.SORT_BY_CATEGORY:
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

            case Shopping.SORT_BY_STORE:
                if (position == 0) return R.layout.sort_by_store_rv_title;
                index = 0;
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

            case Shopping.SORT_ALPHABETICALLY:
                return R.layout.sort_alphabetical_rv_item;

            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        switch(viewType) {
            case R.layout.sort_by_category_rv_title:
                return new SortByCategoryTitleRVH(view);
            case R.layout.sort_by_category_rv_item:
                return new SortByCategoryItemRVH(view, shopping, this, itemData, categoryData, dbStatusHelper, dbCategoryHelper);
            case R.layout.sort_by_store_rv_title:
                return new SortByStoreTitleRVH(view);
            case R.layout.sort_by_store_rv_item:
                return new SortByStoreItemRVH(view, shopping, this, itemData, storeData, dbStatusHelper, dbStoreHelper);
            case R.layout.sort_alphabetical_rv_item:
                return new SortAlphabeticalItemRVH(view, shopping, this, itemData, categoryData, dbStatusHelper, dbCategoryHelper);
            default:
                return new RecyclerView.ViewHolder(view) {};
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(shopping.inventorySortBy) {
            case Shopping.SORT_BY_CATEGORY:
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
                    titleHolder.leftArrow.setVisibility(View.VISIBLE);
                    titleHolder.rightArrow.setVisibility(View.VISIBLE);


                    System.out.println("Category title = " + category);

                    if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL) && categoryData.getCategoryViewAllMap().get(category) == 0) {
                        titleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                        titleHolder.leftArrow.setVisibility(View.GONE);
                        titleHolder.rightArrow.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK) && categoryData.getCategoryViewInStockMap().get(category) == 0) {
                        titleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                        titleHolder.leftArrow.setVisibility(View.GONE);
                        titleHolder.rightArrow.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED) && categoryData.getCategoryViewNeededMap().get(category) == 0) {
                        titleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                        titleHolder.leftArrow.setVisibility(View.GONE);
                        titleHolder.rightArrow.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED) && categoryData.getCategoryViewPausedMap().get(category) == 0) {
                        titleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                        titleHolder.leftArrow.setVisibility(View.GONE);
                        titleHolder.rightArrow.setVisibility(View.GONE);
                    }

                } else {  // item data

                    SortByCategoryItemRVH itemHolder = (SortByCategoryItemRVH) holder;

                    assert thisItem != null;

                    if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                        itemHolder.itemSmallName.setText(thisItem.getName());
                        itemHolder.itemLargeName.setText(thisItem.getName());
                        itemHolder.itemLargeBrand.setText(thisItem.getBrand());
                        itemHolder.itemLargeStore.setText(thisItem.getStore().toString());
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.VISIBLE);
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.VISIBLE);
                    } else {
                        itemHolder.itemSmallName.setText(thisItem.getName());
                        itemHolder.itemLargeName.setText(thisItem.getName());
                        itemHolder.itemLargeBrand.setText(thisItem.getBrand());
                        itemHolder.itemLargeStore.setText(thisItem.getStore().toString());
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

                    switch(shopping.inventoryView) {

                        case Shopping.INVENTORY_ALL:

                            if (shopping.getClickedInventoryList().get(adjustedPosition)) {
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

                        case Shopping.INVENTORY_INSTOCK:

                            if (thisItem.getStatus().isInStock()) {
                                if (shopping.getClickedInventoryList().get(adjustedPosition)) {
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
                                if (shopping.getClickedInventoryList().get(adjustedPosition)) {
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
                                if (shopping.getClickedInventoryList().get(adjustedPosition)) {
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

                        case Shopping.INVENTORY_NEEDED:

                            if (thisItem.getStatus().isInStock()) {
                                if (shopping.getClickedInventoryList().get(adjustedPosition)) {
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
                                if (shopping.getClickedInventoryList().get(adjustedPosition)) {
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
                                if (shopping.getClickedInventoryList().get(adjustedPosition)) {
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

                        case Shopping.INVENTORY_PAUSED:

                            if (thisItem.getStatus().isInStock()) {
                                if (shopping.getClickedInventoryList().get(adjustedPosition)) {
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
                                if (shopping.getClickedInventoryList().get(adjustedPosition)) {
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
                                if (shopping.getClickedInventoryList().get(adjustedPosition)) {
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

            case Shopping.SORT_BY_STORE:
                thisItem = null;
                String store = null;
                isTitle = false;
                adjustedPosition = 0;

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
                    titleHolder.leftArrow.setVisibility(View.VISIBLE);
                    titleHolder.rightArrow.setVisibility(View.VISIBLE);

                    System.out.println("Store title = " + store);

                    if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL) && storeData.getStoreViewAllMap().get(store) == 0) {
                        titleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                        titleHolder.leftArrow.setVisibility(View.GONE);
                        titleHolder.rightArrow.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK) && storeData.getStoreViewInStockMap().get(store) == 0) {
                        titleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                        titleHolder.leftArrow.setVisibility(View.GONE);
                        titleHolder.rightArrow.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED) && storeData.getStoreViewNeededMap().get(store) == 0) {
                        titleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                        titleHolder.leftArrow.setVisibility(View.GONE);
                        titleHolder.rightArrow.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED) && storeData.getStoreViewPausedMap().get(store) == 0) {
                        titleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                        titleHolder.leftArrow.setVisibility(View.GONE);
                        titleHolder.rightArrow.setVisibility(View.GONE);
                    }

                } else {  // item data

                    SortByStoreItemRVH itemHolder = (SortByStoreItemRVH) holder;

                    if (thisItem != null && thisItem.getStatus().isNeeded()) {
                        if (shopping.getClickedShoppingList().get(adjustedPosition)) {
                            itemHolder.itemSmallName.setText(thisItem.getName());
                            itemHolder.itemLargeName.setText(thisItem.getName());
                            itemHolder.itemLargeBrand.setText(thisItem.getBrand());
                            itemHolder.itemLargeCategory.setText(thisItem.getCategory().toString());
                            itemHolder.triangleRight.setVisibility(View.GONE);
                            itemHolder.triangleDown.setVisibility(View.VISIBLE);
                            itemHolder.itemSmall.setVisibility(View.GONE);
                            itemHolder.itemLarge.setVisibility(View.VISIBLE);
                        } else {
                            itemHolder.itemSmallName.setText(thisItem.getName());
                            itemHolder.itemLargeName.setText(thisItem.getName());
                            itemHolder.itemLargeBrand.setText(thisItem.getBrand());
                            itemHolder.itemLargeCategory.setText(thisItem.getCategory().toString());
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

                    assert thisItem != null;

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
                            !thisItem.getStore().toString().equals(storeData.getStoreList().get(shopping.storeListOrderNum - 1))) {
                        itemHolder.triangleDown.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.itemSmall.setVisibility(View.GONE);
                    }
                }

            case Shopping.SORT_ALPHABETICALLY:
                thisItem = null;
                category = null;
                isTitle = false;
                adjustedPosition = 0;

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
        }
    }

    @Override
    public int getItemCount() {

        switch(shopping.inventorySortBy) {
            case Shopping.SORT_BY_CATEGORY:
                return (itemData.getItemListByCategory().size() + categoryData.getCategoryList().size());
            case Shopping.SORT_BY_STORE:
                return (itemData.getItemListByStore().size() + storeData.getStoreList().size());
            case Shopping.SORT_ALPHABETICALLY:
                return itemData.getItemListAZ().size();
            default:
                return -1;
        }
    }

    static class SortByCategoryTitleRVH extends RecyclerView.ViewHolder {

        private final TextView sortByCategoryRvTitle;
        private final ImageView leftArrow;
        private final ImageView rightArrow;

        SortByCategoryTitleRVH(View itemView) {

            super(itemView);

            sortByCategoryRvTitle = itemView.findViewById(R.id.sortByCategoryRvTitle);
            leftArrow = itemView.findViewById(R.id.leftArrow);
            rightArrow = itemView.findViewById(R.id.rightArrow);
        }

    }

    static class SortByCategoryItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Shopping shopping;
        private final FullInventoryRVA adapter;
        private final ItemData itemData;
        private final CategoryData categoryData;
        private final DBStatusHelper dbStatusHelper;
        private final DBCategoryHelper dbCategoryHelper;

        private final Button triangleRight;
        private final Button triangleDown;
        private final LinearLayout itemSmall;
        private final LinearLayout itemLarge;
        private final TextView itemSmallName;
        private final TextView itemLargeName;
        private final TextView itemSmallInStock;
        private final TextView itemSmallNeeded;
        private final TextView itemSmallPaused;
        private final TextView itemLargeInStock;
        private final TextView itemLargeNeeded;
        private final TextView itemLargePaused;
        private final TextView itemLargeBrand;
        private final TextView itemLargeBrandLabel;
        private final TextView itemLargeStore;
        private final TextView itemLargeStoreLabel;

        SortByCategoryItemRVH(final View itemView, Shopping shopping, FullInventoryRVA adapter, ItemData itemData,
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

            String category;
            boolean isTitle = false;
            Item thisItem = null;
            int adjustedPosition;

            if (position == 0) {
                isTitle = true;
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

        Item getItemWithCategories(int position) {

            String category;
            Item thisItem = null;
            int adjustedPosition;

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
                    break;
                } else if (index >= adjustedPosition) {
                    thisItem = itemData.getCategoryMap().get(category).getItemList().get(numItemsInCategory - index + adjustedPosition);
                    break;
                }
            }
            return thisItem;
        }

        @Override
        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            String category;
            boolean isTitle = false;
            Item thisItem = null;
            int adjustedPosition = 0;

            if (position == 0) {
                isTitle = true;
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
                        thisItem.getStatus().setAsClickedInInventory();
                        shopping.getClickedInventoryList().set(adjustedPosition, true);
                    }
                } else if (id == triangleDown.getId()) {
                    if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                        triangleDown.setVisibility(View.GONE);
                        triangleRight.setVisibility(View.VISIBLE);
                        itemLarge.setVisibility(View.GONE);
                        itemSmall.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsUnclickedInInventory();
                        shopping.getClickedInventoryList().set(adjustedPosition, false);
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

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock - 1, numItemsNeeded + 1, numItemsPaused);
                        //shopping.updateCategoryData();
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

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock, numItemsNeeded - 1, numItemsPaused + 1);
                        //shopping.updateCategoryData();
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

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock + 1, numItemsNeeded, numItemsPaused - 1);
                        //shopping.updateCategoryData();
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

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock - 1, numItemsNeeded + 1, numItemsPaused);
                        //shopping.updateCategoryData();
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

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock, numItemsNeeded - 1, numItemsPaused + 1);
                        //shopping.updateCategoryData();
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

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock + 1, numItemsNeeded, numItemsPaused - 1);
                        //shopping.updateCategoryData();
                    }
                }
            }
        }
    }

    static class SortByStoreTitleRVH extends RecyclerView.ViewHolder {

        private final TextView sortByStoreRvTitle;
        private final ImageView leftArrow;
        private final ImageView rightArrow;

        SortByStoreTitleRVH(View itemView) {

            super(itemView);

            sortByStoreRvTitle = itemView.findViewById(R.id.sortByStoreRvTitle);
            leftArrow = itemView.findViewById(R.id.leftArrow);
            rightArrow = itemView.findViewById(R.id.rightArrow);
        }

    }

    static class SortByStoreItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Shopping shopping;
        private final FullInventoryRVA adapter;
        private final ItemData itemData;
        private final StoreData storeData;
        private final DBStatusHelper dbStatusHelper;
        private final DBStoreHelper dbStoreHelper;

        private final Button triangleRight;
        private final Button triangleDown;
        private final LinearLayout itemSmall;
        private final LinearLayout itemLarge;
        private final TextView itemSmallName;
        private final TextView itemLargeName;
        private final TextView itemSmallInStock;
        private final TextView itemSmallNeeded;
        private final TextView itemSmallPaused;
        private final TextView itemLargeInStock;
        private final TextView itemLargeNeeded;
        private final TextView itemLargePaused;
        private final TextView itemLargeBrand;
        private final TextView itemLargeBrandLabel;
        private final TextView itemLargeCategory;
        private final TextView itemLargeCategoryLabel;

        SortByStoreItemRVH(final View itemView, Shopping shopping, FullInventoryRVA adapter,
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

            String store;
            boolean isTitle = false;
            Item thisItem = null;
            int adjustedPosition;

            if (position == 0) {
                isTitle = true;
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

            String store;
            Item thisItem = null;
            int adjustedPosition;

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
                    break;
                } else if (index >= adjustedPosition) {
                    thisItem = itemData.getStoreMap().get(store).getItemList().get(numItemsInStore - index + adjustedPosition);
                    break;
                }
            }
            return thisItem;
        }

        @Override
        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            String store;
            boolean isTitle = false;
            Item thisItem = null;
            int adjustedPosition = 0;

            if (position == 0) {
                isTitle = true;
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

                        thisItem.getStatus().setAsClickedInShoppingList();
                        shopping.getClickedShoppingList().set(adjustedPosition, true);
                    }
                } else if (id == triangleDown.getId()) {
                    if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                        triangleDown.setVisibility(View.GONE);
                        triangleRight.setVisibility(View.VISIBLE);
                        itemLarge.setVisibility(View.GONE);
                        itemSmall.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsUnclickedInShoppingList();
                        shopping.getClickedShoppingList().set(adjustedPosition, false);
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

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock - 1, numItemsNeeded + 1, numItemsPaused);
                        //shopping.updateStoreData();
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

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock, numItemsNeeded - 1, numItemsPaused + 1);
                        //shopping.updateStoreData();
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

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock + 1, numItemsNeeded, numItemsPaused - 1);
                        //shopping.updateStoreData();
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

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock - 1, numItemsNeeded + 1, numItemsPaused);
                        //shopping.updateStoreData();
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

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock, numItemsNeeded - 1, numItemsPaused + 1);
                        //shopping.updateStoreData();
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

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock + 1, numItemsNeeded, numItemsPaused - 1);
                        //shopping.updateStoreData();
                    }
                }
            }
        }
    }
}