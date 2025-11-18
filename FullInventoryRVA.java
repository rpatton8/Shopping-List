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

            case Shopping.SORT_ALPHABETICAL:

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
                return new SortAlphabeticalItemRVH(view, shopping, this, itemData, storeData, categoryData, dbStatusHelper, dbStoreHelper);
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
                int adjustedPosition;

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

                    SortByCategoryTitleRVH categoryTitleHolder = (SortByCategoryTitleRVH) holder;

                    categoryTitleHolder.sortByCategoryRvTitle.setText(category);
                    categoryTitleHolder.sortByCategoryRvTitle.setVisibility(View.VISIBLE);
                    if (shopping.categoryTitles.equals(Shopping.TITLES_EXPANDED)) {
                        categoryTitleHolder.triangleButtonDown1.setVisibility(View.VISIBLE);
                        categoryTitleHolder.triangleButtonDown2.setVisibility(View.VISIBLE);
                        categoryTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonLeft.setVisibility(View.GONE);
                    } else if (shopping.categoryTitles.equals(Shopping.TITLES_CONTRACTED)) {
                        categoryTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonRight.setVisibility(View.VISIBLE);
                        categoryTitleHolder.triangleButtonLeft.setVisibility(View.VISIBLE);
                    }

                    if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL) && categoryData.getCategoryViewAllMap().get(category) == 0) {
                        categoryTitleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonLeft.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK) && categoryData.getCategoryViewInStockMap().get(category) == 0) {
                        categoryTitleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonLeft.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED) && categoryData.getCategoryViewNeededMap().get(category) == 0) {
                        categoryTitleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonLeft.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED) && categoryData.getCategoryViewPausedMap().get(category) == 0) {
                        categoryTitleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                        categoryTitleHolder.triangleButtonLeft.setVisibility(View.GONE);
                    }

                } else {  // item data

                    SortByCategoryItemRVH categoryItemHolder = (SortByCategoryItemRVH) holder;

                    assert thisItem != null;

                    if (thisItem.getStatus().isClickedInInventory()) {
                        categoryItemHolder.itemSmallName.setText(thisItem.getName());
                        categoryItemHolder.itemLargeName.setText(thisItem.getName());
                        categoryItemHolder.itemLargeBrand.setText(thisItem.getBrand());
                        categoryItemHolder.itemLargeStore.setText(thisItem.getStore().toString());
                        categoryItemHolder.triangleRight.setVisibility(View.GONE);
                        categoryItemHolder.triangleDown.setVisibility(View.VISIBLE);
                        categoryItemHolder.itemSmall.setVisibility(View.GONE);
                        categoryItemHolder.itemLarge.setVisibility(View.VISIBLE);
                    } else {
                        categoryItemHolder.itemSmallName.setText(thisItem.getName());
                        categoryItemHolder.itemLargeName.setText(thisItem.getName());
                        categoryItemHolder.itemLargeBrand.setText(thisItem.getBrand());
                        categoryItemHolder.itemLargeStore.setText(thisItem.getStore().toString());
                        categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        categoryItemHolder.triangleRight.setVisibility(View.VISIBLE);
                        categoryItemHolder.itemLarge.setVisibility(View.GONE);
                        categoryItemHolder.itemSmall.setVisibility(View.VISIBLE);
                    }

                    if (thisItem.getStatus().isInStock()) {
                        categoryItemHolder.itemSmallPaused.setVisibility(View.GONE);
                        categoryItemHolder.itemLargePaused.setVisibility(View.GONE);
                        categoryItemHolder.itemSmallNeeded.setVisibility(View.GONE);
                        categoryItemHolder.itemLargeNeeded.setVisibility(View.GONE);
                        categoryItemHolder.itemSmallInStock.setVisibility(View.VISIBLE);
                        categoryItemHolder.itemLargeInStock.setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isNeeded()) {
                        categoryItemHolder.itemSmallInStock.setVisibility(View.GONE);
                        categoryItemHolder.itemLargeInStock.setVisibility(View.GONE);
                        categoryItemHolder.itemSmallPaused.setVisibility(View.GONE);
                        categoryItemHolder.itemLargePaused.setVisibility(View.GONE);
                        categoryItemHolder.itemSmallNeeded.setVisibility(View.VISIBLE);
                        categoryItemHolder.itemLargeNeeded.setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isPaused()) {
                        categoryItemHolder.itemSmallNeeded.setVisibility(View.GONE);
                        categoryItemHolder.itemLargeNeeded.setVisibility(View.GONE);
                        categoryItemHolder.itemSmallInStock.setVisibility(View.GONE);
                        categoryItemHolder.itemLargeInStock.setVisibility(View.GONE);
                        categoryItemHolder.itemSmallPaused.setVisibility(View.VISIBLE);
                        categoryItemHolder.itemLargePaused.setVisibility(View.VISIBLE);
                    }

                    if (thisItem.getStatus().isSelectedInInventory()) {
                        categoryItemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                        categoryItemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

                    } else {
                        if (shopping.itemIsSelectedInInventory && shopping.selectedItemPositionInInventory == position) {
                            categoryItemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                            categoryItemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);
                        } else {
                            categoryItemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_unselected);
                            categoryItemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_unselected);
                        }
                    }

                    switch(shopping.inventoryView) {

                        case Shopping.INVENTORY_ALL:

                            if (thisItem.getStatus().isClickedInInventory()) {
                                categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                categoryItemHolder.itemLarge.setVisibility(View.VISIBLE);
                                categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                categoryItemHolder.triangleDown.setVisibility(View.VISIBLE);
                            } else {
                                categoryItemHolder.itemSmall.setVisibility(View.VISIBLE);
                                categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                categoryItemHolder.triangleRight.setVisibility(View.VISIBLE);
                                categoryItemHolder.triangleDown.setVisibility(View.GONE);
                            }

                        case Shopping.INVENTORY_INSTOCK:

                            if (thisItem.getStatus().isInStock()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.VISIBLE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.VISIBLE);
                                } else {
                                    categoryItemHolder.itemSmall.setVisibility(View.VISIBLE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.VISIBLE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isNeeded()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isPaused()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            }

                        case Shopping.INVENTORY_NEEDED:

                            if (thisItem.getStatus().isInStock()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isNeeded()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.VISIBLE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.VISIBLE);

                                } else {
                                    categoryItemHolder.itemSmall.setVisibility(View.VISIBLE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.VISIBLE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isPaused()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            }

                        case Shopping.INVENTORY_PAUSED:

                            if (thisItem.getStatus().isInStock()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isNeeded()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isPaused()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                                    categoryItemHolder.itemLarge.setVisibility(View.VISIBLE);
                                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                                    categoryItemHolder.triangleDown.setVisibility(View.VISIBLE);
                                } else {
                                    categoryItemHolder.itemSmall.setVisibility(View.VISIBLE);
                                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                                    categoryItemHolder.triangleRight.setVisibility(View.VISIBLE);
                                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            }
                    }
                }

            case Shopping.SORT_BY_STORE:

                thisItem = null;
                String store = null;
                isTitle = false;

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

                    SortByStoreTitleRVH storeTitleHolder = (SortByStoreTitleRVH) holder;

                    storeTitleHolder.sortByStoreRvTitle.setText(store);
                    storeTitleHolder.sortByStoreRvTitle.setVisibility(View.VISIBLE);
                    if (shopping.storeTitles.equals(Shopping.TITLES_EXPANDED)) {
                        storeTitleHolder.triangleButtonDown1.setVisibility(View.VISIBLE);
                        storeTitleHolder.triangleButtonDown2.setVisibility(View.VISIBLE);
                        storeTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonLeft.setVisibility(View.GONE);
                    } else if (shopping.storeTitles.equals(Shopping.TITLES_CONTRACTED)) {
                        storeTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonRight.setVisibility(View.VISIBLE);
                        storeTitleHolder.triangleButtonLeft.setVisibility(View.VISIBLE);
                    }

                    if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL) && storeData.getStoreViewAllMap().get(store) == 0) {
                        storeTitleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonLeft.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK) && storeData.getStoreViewInStockMap().get(store) == 0) {
                        storeTitleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonLeft.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED) && storeData.getStoreViewNeededMap().get(store) == 0) {
                        storeTitleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonLeft.setVisibility(View.GONE);
                    } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED) && storeData.getStoreViewPausedMap().get(store) == 0) {
                        storeTitleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                        storeTitleHolder.triangleButtonLeft.setVisibility(View.GONE);
                    }

                } else {  // item data

                    SortByStoreItemRVH storeItemHolder = (SortByStoreItemRVH) holder;

                    assert thisItem != null;

                    if (thisItem.getStatus().isClickedInInventory()) {
                        storeItemHolder.itemSmallName.setText(thisItem.getName());
                        storeItemHolder.itemLargeName.setText(thisItem.getName());
                        storeItemHolder.itemLargeBrand.setText(thisItem.getBrand());
                        storeItemHolder.itemLargeCategory.setText(thisItem.getStore().toString());
                        storeItemHolder.triangleRight.setVisibility(View.GONE);
                        storeItemHolder.triangleDown.setVisibility(View.VISIBLE);
                        storeItemHolder.itemSmall.setVisibility(View.GONE);
                        storeItemHolder.itemLarge.setVisibility(View.VISIBLE);
                    } else {
                        storeItemHolder.itemSmallName.setText(thisItem.getName());
                        storeItemHolder.itemLargeName.setText(thisItem.getName());
                        storeItemHolder.itemLargeBrand.setText(thisItem.getBrand());
                        storeItemHolder.itemLargeCategory.setText(thisItem.getStore().toString());
                        storeItemHolder.triangleDown.setVisibility(View.GONE);
                        storeItemHolder.triangleRight.setVisibility(View.VISIBLE);
                        storeItemHolder.itemLarge.setVisibility(View.GONE);
                        storeItemHolder.itemSmall.setVisibility(View.VISIBLE);
                    }

                    if (thisItem.getStatus().isInStock()) {
                        storeItemHolder.itemSmallPaused.setVisibility(View.GONE);
                        storeItemHolder.itemLargePaused.setVisibility(View.GONE);
                        storeItemHolder.itemSmallNeeded.setVisibility(View.GONE);
                        storeItemHolder.itemLargeNeeded.setVisibility(View.GONE);
                        storeItemHolder.itemSmallInStock.setVisibility(View.VISIBLE);
                        storeItemHolder.itemLargeInStock.setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isNeeded()) {
                        storeItemHolder.itemSmallInStock.setVisibility(View.GONE);
                        storeItemHolder.itemLargeInStock.setVisibility(View.GONE);
                        storeItemHolder.itemSmallPaused.setVisibility(View.GONE);
                        storeItemHolder.itemLargePaused.setVisibility(View.GONE);
                        storeItemHolder.itemSmallNeeded.setVisibility(View.VISIBLE);
                        storeItemHolder.itemLargeNeeded.setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isPaused()) {
                        storeItemHolder.itemSmallNeeded.setVisibility(View.GONE);
                        storeItemHolder.itemLargeNeeded.setVisibility(View.GONE);
                        storeItemHolder.itemSmallInStock.setVisibility(View.GONE);
                        storeItemHolder.itemLargeInStock.setVisibility(View.GONE);
                        storeItemHolder.itemSmallPaused.setVisibility(View.VISIBLE);
                        storeItemHolder.itemLargePaused.setVisibility(View.VISIBLE);
                    }

                    if (thisItem.getStatus().isSelectedInInventory()) {
                        storeItemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                        storeItemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

                    } else {
                        if (shopping.itemIsSelectedInInventory && shopping.selectedItemPositionInInventory == position) {
                            storeItemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                            storeItemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);
                        } else {
                            storeItemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_unselected);
                            storeItemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_unselected);
                        }
                    }

                    switch(shopping.inventoryView) {

                        case Shopping.INVENTORY_ALL:

                            if (thisItem.getStatus().isClickedInInventory()) {
                                storeItemHolder.itemSmall.setVisibility(View.GONE);
                                storeItemHolder.itemLarge.setVisibility(View.VISIBLE);
                                storeItemHolder.triangleRight.setVisibility(View.GONE);
                                storeItemHolder.triangleDown.setVisibility(View.VISIBLE);
                            } else {
                                storeItemHolder.itemSmall.setVisibility(View.VISIBLE);
                                storeItemHolder.itemLarge.setVisibility(View.GONE);
                                storeItemHolder.triangleRight.setVisibility(View.VISIBLE);
                                storeItemHolder.triangleDown.setVisibility(View.GONE);
                            }

                        case Shopping.INVENTORY_INSTOCK:

                            if (thisItem.getStatus().isInStock()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.VISIBLE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.VISIBLE);
                                } else {
                                    storeItemHolder.itemSmall.setVisibility(View.VISIBLE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.VISIBLE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isNeeded()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isPaused()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            }

                        case Shopping.INVENTORY_NEEDED:

                            if (thisItem.getStatus().isInStock()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isNeeded()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.VISIBLE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.VISIBLE);

                                } else {
                                    storeItemHolder.itemSmall.setVisibility(View.VISIBLE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.VISIBLE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isPaused()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            }

                        case Shopping.INVENTORY_PAUSED:

                            if (thisItem.getStatus().isInStock()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isNeeded()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                } else {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            } else if (thisItem.getStatus().isPaused()) {
                                if (thisItem.getStatus().isClickedInInventory()) {
                                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                                    storeItemHolder.itemLarge.setVisibility(View.VISIBLE);
                                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                                    storeItemHolder.triangleDown.setVisibility(View.VISIBLE);
                                } else {
                                    storeItemHolder.itemSmall.setVisibility(View.VISIBLE);
                                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                                    storeItemHolder.triangleRight.setVisibility(View.VISIBLE);
                                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                                }
                            }
                    }
                }

            case Shopping.SORT_ALPHABETICAL:

                thisItem = itemData.getItemListAZ().get(position);

                SortAlphabeticalItemRVH alphabeticalItemHolder = (SortAlphabeticalItemRVH) holder;

                assert thisItem != null;

                if (thisItem.getStatus().isClickedInInventory()) {
                    alphabeticalItemHolder.itemSmallName.setText(thisItem.getName());
                    alphabeticalItemHolder.itemLargeName.setText(thisItem.getName());
                    alphabeticalItemHolder.itemLargeBrand.setText(thisItem.getBrand());
                    alphabeticalItemHolder.itemLargeStore.setText(thisItem.getStore().toString());
                    alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                    alphabeticalItemHolder.triangleDown.setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemLarge.setVisibility(View.VISIBLE);
                } else {
                    alphabeticalItemHolder.itemSmallName.setText(thisItem.getName());
                    alphabeticalItemHolder.itemLargeName.setText(thisItem.getName());
                    alphabeticalItemHolder.itemLargeBrand.setText(thisItem.getBrand());
                    alphabeticalItemHolder.itemLargeStore.setText(thisItem.getStore().toString());
                    alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    alphabeticalItemHolder.triangleRight.setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemSmall.setVisibility(View.VISIBLE);
                }

                if (thisItem.getStatus().isInStock()) {
                    alphabeticalItemHolder.itemSmallPaused.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemLargePaused.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemSmallNeeded.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemLargeNeeded.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemSmallInStock.setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.itemLargeInStock.setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isNeeded()) {
                    alphabeticalItemHolder.itemSmallInStock.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemLargeInStock.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemSmallPaused.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemLargePaused.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemSmallNeeded.setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.itemLargeNeeded.setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isPaused()) {
                    alphabeticalItemHolder.itemSmallNeeded.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemLargeNeeded.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemSmallInStock.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemLargeInStock.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemSmallPaused.setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.itemLargePaused.setVisibility(View.VISIBLE);
                }

                if (thisItem.getStatus().isSelectedInInventory()) {
                    alphabeticalItemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                    alphabeticalItemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

                } else {
                    if (shopping.itemIsSelectedInInventory && shopping.selectedItemPositionInInventory == position) {
                        alphabeticalItemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                        alphabeticalItemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);
                    } else {
                        alphabeticalItemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_unselected);
                        alphabeticalItemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_unselected);
                    }
                }

                switch(shopping.inventoryView) {

                    case Shopping.INVENTORY_ALL:

                        if (thisItem.getStatus().isClickedInInventory()) {
                            alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                            alphabeticalItemHolder.itemLarge.setVisibility(View.VISIBLE);
                            alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                            alphabeticalItemHolder.triangleDown.setVisibility(View.VISIBLE);
                        } else {
                            alphabeticalItemHolder.itemSmall.setVisibility(View.VISIBLE);
                            alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                            alphabeticalItemHolder.triangleRight.setVisibility(View.VISIBLE);
                            alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                        }

                    case Shopping.INVENTORY_INSTOCK:

                        if (thisItem.getStatus().isInStock()) {
                            if (thisItem.getStatus().isClickedInInventory()) {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.VISIBLE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.VISIBLE);
                            } else {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.VISIBLE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.VISIBLE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            }
                        } else if (thisItem.getStatus().isNeeded()) {
                            if (thisItem.getStatus().isClickedInInventory()) {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            } else {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            }
                        } else if (thisItem.getStatus().isPaused()) {
                            if (thisItem.getStatus().isClickedInInventory()) {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            } else {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            }
                        }

                    case Shopping.INVENTORY_NEEDED:

                        if (thisItem.getStatus().isInStock()) {
                            if (thisItem.getStatus().isClickedInInventory()) {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            } else {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            }
                        } else if (thisItem.getStatus().isNeeded()) {
                            if (thisItem.getStatus().isClickedInInventory()) {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.VISIBLE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.VISIBLE);

                            } else {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.VISIBLE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.VISIBLE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            }
                        } else if (thisItem.getStatus().isPaused()) {
                            if (thisItem.getStatus().isClickedInInventory()) {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            } else {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            }
                        }

                    case Shopping.INVENTORY_PAUSED:

                        if (thisItem.getStatus().isInStock()) {
                            if (thisItem.getStatus().isClickedInInventory()) {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            } else {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            }
                        } else if (thisItem.getStatus().isNeeded()) {
                            if (thisItem.getStatus().isClickedInInventory()) {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            } else {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                            }
                        } else if (thisItem.getStatus().isPaused()) {
                            if (thisItem.getStatus().isClickedInInventory()) {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.VISIBLE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.VISIBLE);
                            } else {
                                alphabeticalItemHolder.itemSmall.setVisibility(View.VISIBLE);
                                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                                alphabeticalItemHolder.triangleRight.setVisibility(View.VISIBLE);
                                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
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
            case Shopping.SORT_ALPHABETICAL:
                return itemData.getItemListAZ().size();
            default:
                return -1;
        }
    }

    static class SortByCategoryTitleRVH extends RecyclerView.ViewHolder {

        private final TextView sortByCategoryRvTitle;
        //private final ImageView leftArrow;
        //private final ImageView rightArrow;
        private final ImageView triangleButtonDown1;
        private final ImageView triangleButtonDown2;
        private final ImageView triangleButtonRight;
        private final ImageView triangleButtonLeft;

        SortByCategoryTitleRVH(View itemView) {

            super(itemView);

            sortByCategoryRvTitle = itemView.findViewById(R.id.sortByCategoryRvTitle);
            //leftArrow = itemView.findViewById(R.id.leftArrow);
            //rightArrow = itemView.findViewById(R.id.rightArrow);
            triangleButtonDown1 = itemView.findViewById(R.id.triangleButtonDown1);
            triangleButtonDown2 = itemView.findViewById(R.id.triangleButtonDown2);
            triangleButtonRight = itemView.findViewById(R.id.triangleButtonRight);
            triangleButtonLeft = itemView.findViewById(R.id.triangleButtonLeft);
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
                    }
                } else if (id == triangleDown.getId()) {
                    if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                        triangleDown.setVisibility(View.GONE);
                        triangleRight.setVisibility(View.VISIBLE);
                        itemLarge.setVisibility(View.GONE);
                        itemSmall.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsUnclickedInInventory();
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

                        String thisCategory = thisItem.getCategory().toString();
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

                        String thisCategory = thisItem.getCategory().toString();
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

                        String thisCategory = thisItem.getCategory().toString();
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

                        String thisCategory = thisItem.getCategory().toString();
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

                        String thisCategory = thisItem.getCategory().toString();
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

    static class SortByStoreTitleRVH extends RecyclerView.ViewHolder {

        private final TextView sortByStoreRvTitle;
        //private final ImageView leftArrow;
        //private final ImageView rightArrow;
        private final ImageView triangleButtonDown1;
        private final ImageView triangleButtonDown2;
        private final ImageView triangleButtonRight;
        private final ImageView triangleButtonLeft;

        SortByStoreTitleRVH(View itemView) {

            super(itemView);

            sortByStoreRvTitle = itemView.findViewById(R.id.sortByStoreRvTitle);
            //leftArrow = itemView.findViewById(R.id.leftArrow);
            //rightArrow = itemView.findViewById(R.id.rightArrow);
            triangleButtonDown1 = itemView.findViewById(R.id.triangleButtonDown1);
            triangleButtonDown2 = itemView.findViewById(R.id.triangleButtonDown2);
            triangleButtonRight = itemView.findViewById(R.id.triangleButtonRight);
            triangleButtonLeft = itemView.findViewById(R.id.triangleButtonLeft);
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
                    }
                } else if (id == triangleDown.getId()) {
                    if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                        triangleDown.setVisibility(View.GONE);
                        triangleRight.setVisibility(View.VISIBLE);
                        itemLarge.setVisibility(View.GONE);
                        itemSmall.setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsUnclickedInShoppingList();
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

                        String thisStore = thisItem.getStore().toString();
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

                        String thisStore = thisItem.getStore().toString();
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

                        String thisStore = thisItem.getStore().toString();
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

                        String thisStore = thisItem.getStore().toString();
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

                        String thisStore = thisItem.getStore().toString();
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

    static class SortAlphabeticalItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Shopping shopping;
        private final FullInventoryRVA adapter;
        private final ItemData itemData;
        private final StoreData storeData;
        private final CategoryData categoryData;
        private final DBStatusHelper dbStatusHelper;
        private final DBStoreHelper dbStoreHelper;
        //private final DBCategoryHelper dbCategoryHelper;

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
        private final TextView itemLargeStore;
        private final TextView itemLargeStoreLabel;

        SortAlphabeticalItemRVH(final View itemView, Shopping shopping, FullInventoryRVA adapter, ItemData itemData,
                              StoreData storeData, CategoryData categoryData, DBStatusHelper dbStatus, DBStoreHelper dbStore) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.itemData = itemData;
            this.storeData = storeData;
            this.categoryData = categoryData;
            this.dbStatusHelper = dbStatus;
            this.dbStoreHelper = dbStore;
            //this.dbCategoryHelper = dbCategory;

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

            Item thisItem = itemData.getItemListAZ().get(position);

            //assert thisItem != null;

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

                    Item lastItem = itemData.getItemListAZ().get(currentlySelected);
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

        @Override
        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            Item thisItem = itemData.getItemListAZ().get(position);

            //assert thisItem != null;

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
                }
            } else if (id == triangleDown.getId()) {
                if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                    triangleDown.setVisibility(View.GONE);
                    triangleRight.setVisibility(View.VISIBLE);
                    itemLarge.setVisibility(View.GONE);
                    itemSmall.setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsUnclickedInShoppingList();
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
                }
            }
        }
    }
}