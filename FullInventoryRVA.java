package ryan.android.shopping;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//@SuppressWarnings("ALL")
class FullInventoryRVA extends RecyclerView.Adapter {

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

    public int getItemViewType(int position) {

        if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {
            if (position == 0) return R.layout.sort_by_category_rv_title;
            int index = 0;
            for (int i = 0; i < categoryData.getCategoryList().size(); i++) {
                String category = categoryData.getCategoryList().get(i);
                int numItemsInCategory;
                if (itemData.getCategoryMap().get(category) == null) {
                    numItemsInCategory = 0;
                } else {
                    numItemsInCategory = itemData.getCategoryMap().get(category).getCategoryItemsList().size();
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
                    numItemsInStore = itemData.getStoreMap().get(store).getStoreItemsList().size();
                }
                index += numItemsInStore + 1;
                if (position == index) return R.layout.sort_by_store_rv_title;
            }
            return R.layout.sort_by_store_rv_item;

        } else if (shopping.inventorySortBy.equals(Shopping.SORT_ALPHABETICAL)) {

            return R.layout.sort_alphabetical_rv_item;

        } else return -1;

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {

            if (viewType == R.layout.sort_by_category_rv_title) {
                return new SortByCategoryTitleRVH(view, shopping, this, itemData, categoryData, dbStatusHelper, dbCategoryHelper);
            } else if (viewType == R.layout.sort_by_category_rv_item) {
                return new SortByCategoryItemRVH(view, shopping, this, itemData, categoryData, dbStatusHelper, dbCategoryHelper);
            } else return new RecyclerView.ViewHolder(view) {};

        } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_STORE)) {

            if (viewType == R.layout.sort_by_store_rv_title) {
                return new SortByStoreTitleRVH(view, shopping, this, itemData, storeData, dbStatusHelper, dbStoreHelper);
            } else if (viewType == R.layout.sort_by_store_rv_item) {
                return new SortByStoreItemRVH(view, shopping, this, itemData, storeData, dbStatusHelper, dbStoreHelper);
            } else return new RecyclerView.ViewHolder(view) {};

        } else if (shopping.inventorySortBy.equals(Shopping.SORT_ALPHABETICAL)) {

            if (viewType == R.layout.sort_alphabetical_rv_item) {
                return new SortAlphabeticalItemRVH(view, shopping, this);
            } else return new RecyclerView.ViewHolder(view) {};

        } else return new RecyclerView.ViewHolder(view) {};

    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

//-----------------------------------Sort By Category-----------------------------------------------
        if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {
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
                        numItemsInCategory = itemData.getCategoryMap().get(category).getCategoryItemsList().size();
                    }
                    index += numItemsInCategory;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        category = categoryData.getCategoryList().get(i + 1);
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (isTitle) { // titles

                SortByCategoryTitleRVH categoryTitleHolder = (SortByCategoryTitleRVH) holder;

                categoryTitleHolder.categoryTitleText.setText(category);
                categoryTitleHolder.sortByCategoryRvTitle.setVisibility(View.VISIBLE);

                if (categoryTitleHolder.isExpanded()) {
                    //itemData.getCategoryMap().get(category).setCategoryAsContracted();
                    categoryTitleHolder.triangleButtonDown1.setVisibility(View.VISIBLE);
                    categoryTitleHolder.triangleButtonDown2.setVisibility(View.VISIBLE);
                    categoryTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                    categoryTitleHolder.triangleButtonLeft.setVisibility(View.GONE);

                } else if (categoryTitleHolder.isContracted()) {
                    //itemData.getCategoryMap().get(category).setCategoryAsExpanded();
                    categoryTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                    categoryTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                    categoryTitleHolder.triangleButtonRight.setVisibility(View.VISIBLE);
                    categoryTitleHolder.triangleButtonLeft.setVisibility(View.VISIBLE);
                }

                if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL) && categoryData.getCategoryViewAllMap().get(category) == 0) {
                    categoryTitleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK) && categoryData.getCategoryViewInStockMap().get(category) == 0) {
                    categoryTitleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED) && categoryData.getCategoryViewNeededMap().get(category) == 0) {
                    categoryTitleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED) && categoryData.getCategoryViewPausedMap().get(category) == 0) {
                    categoryTitleHolder.sortByCategoryRvTitle.setVisibility(View.GONE);
                }

            } else {  // item data

                SortByCategoryItemRVH categoryItemHolder = (SortByCategoryItemRVH) holder;

                if (thisItem.getCategory().categoryIsContracted()) {
                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
                    return;
                }

                if (thisItem.getStatus().isExpandedInInventory()) {
                    categoryItemHolder.itemSmallName.setText(thisItem.getName());
                    categoryItemHolder.itemLargeName.setText(thisItem.getName());
                    categoryItemHolder.itemLargeBrand.setText(thisItem.getBrandType());
                    categoryItemHolder.itemLargeCategory.setText(thisItem.getCategory().toString());
                    categoryItemHolder.itemLargeStore.setText(thisItem.getStore().toString());
                    categoryItemHolder.triangleRight.setVisibility(View.GONE);
                    categoryItemHolder.triangleDown.setVisibility(View.VISIBLE);
                    categoryItemHolder.itemSmall.setVisibility(View.GONE);
                    categoryItemHolder.itemLarge.setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    categoryItemHolder.itemSmallName.setText(thisItem.getName());
                    categoryItemHolder.itemLargeName.setText(thisItem.getName());
                    categoryItemHolder.itemLargeBrand.setText(thisItem.getBrandType());
                    categoryItemHolder.itemLargeCategory.setText(thisItem.getCategory().toString());
                    categoryItemHolder.itemLargeStore.setText(thisItem.getStore().toString());
                    categoryItemHolder.triangleRight.setVisibility(View.VISIBLE);
                    categoryItemHolder.triangleDown.setVisibility(View.GONE);
                    categoryItemHolder.itemSmall.setVisibility(View.VISIBLE);
                    categoryItemHolder.itemLarge.setVisibility(View.GONE);
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
                //--------------------------------By Category - All---------------------------------
                if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        categoryItemHolder.itemSmall.setVisibility(View.GONE);
                        categoryItemHolder.itemLarge.setVisibility(View.VISIBLE);
                        categoryItemHolder.triangleRight.setVisibility(View.GONE);
                        categoryItemHolder.triangleDown.setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        categoryItemHolder.itemSmall.setVisibility(View.VISIBLE);
                        categoryItemHolder.itemLarge.setVisibility(View.GONE);
                        categoryItemHolder.triangleRight.setVisibility(View.VISIBLE);
                        categoryItemHolder.triangleDown.setVisibility(View.GONE);
                    }
                //--------------------------------By Category - In Stock----------------------------
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                    if (thisItem.getStatus().isInStock()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.VISIBLE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.VISIBLE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.VISIBLE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.VISIBLE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isNeeded()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isPaused()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    }
                //--------------------------------By Category - Needed------------------------------
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                    if (thisItem.getStatus().isInStock()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isNeeded()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.VISIBLE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.VISIBLE);

                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.VISIBLE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.VISIBLE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isPaused()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    }
                //--------------------------------By Category - Paused------------------------------
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                    if (thisItem.getStatus().isInStock()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isNeeded()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isPaused()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.GONE);
                            categoryItemHolder.itemLarge.setVisibility(View.VISIBLE);
                            categoryItemHolder.triangleRight.setVisibility(View.GONE);
                            categoryItemHolder.triangleDown.setVisibility(View.VISIBLE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            categoryItemHolder.itemSmall.setVisibility(View.VISIBLE);
                            categoryItemHolder.itemLarge.setVisibility(View.GONE);
                            categoryItemHolder.triangleRight.setVisibility(View.VISIBLE);
                            categoryItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    }
                }
            }
//------------------------------------------Sort By Store-------------------------------------------
        } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_STORE)) {
            Item thisItem = null;
            String store = null;
            boolean isTitle = false;
            int adjustedPosition;

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
                        numItemsInStore = itemData.getStoreMap().get(store).getStoreItemsList().size();
                    }
                    index += numItemsInStore;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        store = storeData.getStoreList().get(i + 1);
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (isTitle) { // titles

                SortByStoreTitleRVH storeTitleHolder = (SortByStoreTitleRVH) holder;

                storeTitleHolder.storeTitleText.setText(store);
                storeTitleHolder.sortByStoreRvTitle.setVisibility(View.VISIBLE);

                if (storeTitleHolder.isExpanded()) {

                    //itemData.getStoreMap().get(store).setStoreAsContracted();
                    storeTitleHolder.triangleButtonDown1.setVisibility(View.VISIBLE);
                    storeTitleHolder.triangleButtonDown2.setVisibility(View.VISIBLE);
                    storeTitleHolder.triangleButtonRight.setVisibility(View.GONE);
                    storeTitleHolder.triangleButtonLeft.setVisibility(View.GONE);


                } else if (storeTitleHolder.isContracted()) {

                    //itemData.getStoreMap().get(store).setStoreAsExpanded();
                    storeTitleHolder.triangleButtonDown1.setVisibility(View.GONE);
                    storeTitleHolder.triangleButtonDown2.setVisibility(View.GONE);
                    storeTitleHolder.triangleButtonRight.setVisibility(View.VISIBLE);
                    storeTitleHolder.triangleButtonLeft.setVisibility(View.VISIBLE);

                }

                if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL) && storeData.getStoreViewAllMap().get(store) == 0) {
                    storeTitleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK) && storeData.getStoreViewInStockMap().get(store) == 0) {
                    storeTitleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED) && storeData.getStoreViewNeededMap().get(store) == 0) {
                    storeTitleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED) && storeData.getStoreViewPausedMap().get(store) == 0) {
                    storeTitleHolder.sortByStoreRvTitle.setVisibility(View.GONE);
                }

            } else {  // item data

                SortByStoreItemRVH storeItemHolder = (SortByStoreItemRVH) holder;

                if (thisItem.getStore().storeIsContracted()) {
                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                    storeItemHolder.itemLarge.setVisibility(View.GONE);
                    return;
                }

                if (thisItem.getStatus().isExpandedInInventory()) {
                    storeItemHolder.itemSmallName.setText(thisItem.getName());
                    storeItemHolder.itemLargeName.setText(thisItem.getName());
                    storeItemHolder.itemLargeBrand.setText(thisItem.getBrandType());
                    storeItemHolder.itemLargeCategory.setText(thisItem.getCategory().toString());
                    storeItemHolder.itemLargeStore.setText(thisItem.getStore().toString());
                    storeItemHolder.triangleRight.setVisibility(View.GONE);
                    storeItemHolder.triangleDown.setVisibility(View.VISIBLE);
                    storeItemHolder.itemSmall.setVisibility(View.GONE);
                    storeItemHolder.itemLarge.setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    storeItemHolder.itemSmallName.setText(thisItem.getName());
                    storeItemHolder.itemLargeName.setText(thisItem.getName());
                    storeItemHolder.itemLargeBrand.setText(thisItem.getBrandType());
                    storeItemHolder.itemLargeCategory.setText(thisItem.getCategory().toString());
                    storeItemHolder.itemLargeStore.setText(thisItem.getStore().toString());
                    storeItemHolder.triangleRight.setVisibility(View.VISIBLE);
                    storeItemHolder.triangleDown.setVisibility(View.GONE);
                    storeItemHolder.itemSmall.setVisibility(View.VISIBLE);
                    storeItemHolder.itemLarge.setVisibility(View.GONE);
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
                //--------------------------------By Store - All------------------------------------
                if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        storeItemHolder.itemSmall.setVisibility(View.GONE);
                        storeItemHolder.itemLarge.setVisibility(View.VISIBLE);
                        storeItemHolder.triangleRight.setVisibility(View.GONE);
                        storeItemHolder.triangleDown.setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        storeItemHolder.itemSmall.setVisibility(View.VISIBLE);
                        storeItemHolder.itemLarge.setVisibility(View.GONE);
                        storeItemHolder.triangleRight.setVisibility(View.VISIBLE);
                        storeItemHolder.triangleDown.setVisibility(View.GONE);
                    }
                //--------------------------------By Store - In Stock-------------------------------
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                    if (thisItem.getStatus().isInStock()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.VISIBLE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.VISIBLE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.VISIBLE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.VISIBLE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isNeeded()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isPaused()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    }
                //--------------------------------By Store - Needed---------------------------------
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                    if (thisItem.getStatus().isInStock()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isNeeded()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.VISIBLE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.VISIBLE);

                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.VISIBLE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.VISIBLE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isPaused()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    }
                //--------------------------------By Store - Paused---------------------------------
                } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                    if (thisItem.getStatus().isInStock()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isNeeded()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    } else if (thisItem.getStatus().isPaused()) {
                        if (thisItem.getStatus().isExpandedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.GONE);
                            storeItemHolder.itemLarge.setVisibility(View.VISIBLE);
                            storeItemHolder.triangleRight.setVisibility(View.GONE);
                            storeItemHolder.triangleDown.setVisibility(View.VISIBLE);
                        } else if (thisItem.getStatus().isContractedInInventory()) {
                            storeItemHolder.itemSmall.setVisibility(View.VISIBLE);
                            storeItemHolder.itemLarge.setVisibility(View.GONE);
                            storeItemHolder.triangleRight.setVisibility(View.VISIBLE);
                            storeItemHolder.triangleDown.setVisibility(View.GONE);
                        }
                    }
                }
            }
//------------------------------------------Alphabetical--------------------------------------------
        } else if (shopping.inventorySortBy.equals(Shopping.SORT_ALPHABETICAL)) {

            Item thisItem = itemData.getItemListAZ().get(position);

            SortAlphabeticalItemRVH alphabeticalItemHolder = (SortAlphabeticalItemRVH) holder;

            if (thisItem.getStatus().isExpandedInInventory()) {
                alphabeticalItemHolder.itemSmallName.setText(thisItem.getName());
                alphabeticalItemHolder.itemLargeName.setText(thisItem.getName());
                alphabeticalItemHolder.itemLargeBrand.setText(thisItem.getBrandType());
                alphabeticalItemHolder.itemLargeCategory.setText(thisItem.getCategory().toString());
                alphabeticalItemHolder.itemLargeStore.setText(thisItem.getStore().toString());
                alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                alphabeticalItemHolder.triangleDown.setVisibility(View.VISIBLE);
                alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                alphabeticalItemHolder.itemLarge.setVisibility(View.VISIBLE);
            } else if (thisItem.getStatus().isContractedInInventory()) {
                alphabeticalItemHolder.itemSmallName.setText(thisItem.getName());
                alphabeticalItemHolder.itemLargeName.setText(thisItem.getName());
                alphabeticalItemHolder.itemLargeBrand.setText(thisItem.getBrandType());
                alphabeticalItemHolder.itemLargeCategory.setText(thisItem.getCategory().toString());
                alphabeticalItemHolder.itemLargeStore.setText(thisItem.getStore().toString());
                alphabeticalItemHolder.triangleRight.setVisibility(View.VISIBLE);
                alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                alphabeticalItemHolder.itemSmall.setVisibility(View.VISIBLE);
                alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
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
            //--------------------------------Alphabetical - All------------------------------------
            if (shopping.inventoryView.equals(Shopping.INVENTORY_ALL)) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                    alphabeticalItemHolder.itemLarge.setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                    alphabeticalItemHolder.triangleDown.setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    alphabeticalItemHolder.itemSmall.setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                    alphabeticalItemHolder.triangleRight.setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                }
            //--------------------------------Alphabetical - In Stock-------------------------------
            } else if (shopping.inventoryView.equals(Shopping.INVENTORY_INSTOCK)) {
                if (thisItem.getStatus().isInStock()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.VISIBLE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.VISIBLE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.VISIBLE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    }
                }
            //--------------------------------Alphabetical - Needed---------------------------------
            } else if (shopping.inventoryView.equals(Shopping.INVENTORY_NEEDED)) {
                if (thisItem.getStatus().isInStock()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.VISIBLE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.VISIBLE);

                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.VISIBLE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.VISIBLE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    }
                }
            //--------------------------------Alphabetical - Paused---------------------------------
            } else if (shopping.inventoryView.equals(Shopping.INVENTORY_PAUSED)) {
                if (thisItem.getStatus().isInStock()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.GONE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.VISIBLE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        alphabeticalItemHolder.itemSmall.setVisibility(View.VISIBLE);
                        alphabeticalItemHolder.itemLarge.setVisibility(View.GONE);
                        alphabeticalItemHolder.triangleRight.setVisibility(View.VISIBLE);
                        alphabeticalItemHolder.triangleDown.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    public int getItemCount() {

        if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {

            return (itemData.getItemListByCategory().size() + categoryData.getCategoryList().size());

        } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_STORE)) {

            return (itemData.getItemListByStore().size() + storeData.getStoreList().size());

        } else if (shopping.inventorySortBy.equals(Shopping.SORT_ALPHABETICAL)) {

            return (itemData.getItemListAZ().size());

        } else return -1;

    }

    private class SortByCategoryTitleRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private FullInventoryRVA adapter;
        private ItemData itemData;
        private CategoryData categoryData;
        private DBStatusHelper dbStatusHelper;
        private DBCategoryHelper dbCategoryHelper;

        private LinearLayout sortByCategoryRvTitle;
        private TextView categoryTitleText;
        private ImageView triangleButtonDown1;
        private ImageView triangleButtonDown2;
        private ImageView triangleButtonRight;
        private ImageView triangleButtonLeft;

        private boolean isExpanded;
        private boolean isContracted;

        private SortByCategoryTitleRVH(View itemView, Shopping shopping, FullInventoryRVA adapter, ItemData itemData,
                                       CategoryData categoryData, DBStatusHelper dbStatus, DBCategoryHelper dbCategory) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.itemData = itemData;
            this.categoryData = categoryData;
            this.dbStatusHelper = dbStatus;
            this.dbCategoryHelper = dbCategory;

            isExpanded = true;
            isContracted = false;

            sortByCategoryRvTitle = itemView.findViewById(R.id.sortByCategoryRvTitle);
            categoryTitleText = itemView.findViewById(R.id.categoryTitleText);
            triangleButtonDown1 = itemView.findViewById(R.id.triangleButtonDown1);
            triangleButtonDown2 = itemView.findViewById(R.id.triangleButtonDown2);
            triangleButtonRight = itemView.findViewById(R.id.triangleButtonRight);
            triangleButtonLeft = itemView.findViewById(R.id.triangleButtonLeft);

            categoryTitleText.setOnClickListener(this);
            triangleButtonDown1.setOnClickListener(this);
            triangleButtonDown2.setOnClickListener(this);
            triangleButtonRight.setOnClickListener(this);
            triangleButtonLeft.setOnClickListener(this);

        }

        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

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
                    numItemsInCategory = itemData.getCategoryMap().get(category).getCategoryItemsList().size();
                }
                index += numItemsInCategory;
                adjustedPosition--;
                if (index >= adjustedPosition) {
                    thisItem = itemData.getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                    break;
                }
            }


            if (id == triangleButtonDown1.getId() || id == triangleButtonDown2.getId()) {
                contractTitle();
                thisItem.getCategory().setCategoryAsContracted();
            } else if (id == triangleButtonRight.getId() || id == triangleButtonLeft.getId()) {
                expandTitle();
                thisItem.getCategory().setCategoryAsExpanded();
            } else if (id == categoryTitleText.getId() && isExpanded()) {
                contractTitle();
                thisItem.getCategory().setCategoryAsContracted();
            } else if (id == categoryTitleText.getId() && isContracted()) {
                expandTitle();
                thisItem.getCategory().setCategoryAsExpanded();
            }
        }

        private void expandTitle() {
            this.isExpanded = true;
            this.isContracted = false;
            triangleButtonDown1.setVisibility(View.VISIBLE);
            triangleButtonDown2.setVisibility(View.VISIBLE);
            triangleButtonRight.setVisibility(View.GONE);
            triangleButtonLeft.setVisibility(View.GONE);
        }

        private void contractTitle() {
            this.isExpanded = false;
            this.isContracted = true;
            triangleButtonDown1.setVisibility(View.GONE);
            triangleButtonDown2.setVisibility(View.GONE);
            triangleButtonRight.setVisibility(View.VISIBLE);
            triangleButtonLeft.setVisibility(View.VISIBLE);
        }

        boolean isExpanded() {
            return isExpanded;
        }

        void setAsExpanded() {
            isExpanded = true;
            isContracted = false;
        }

        boolean isContracted() {
            return isContracted;
        }

        void setAsContracted() {
            isExpanded = false;
            isContracted = true;
        }

    }

    private class SortByCategoryItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        private TextView itemLargeCategory;
        private TextView itemLargeCategoryLabel;
        private TextView itemLargeStore;
        private TextView itemLargeStoreLabel;

        private SortByCategoryItemRVH(View itemView, Shopping shopping, FullInventoryRVA adapter, ItemData itemData,
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
            itemLargeCategory.setOnClickListener(this);
            itemLargeCategoryLabel.setOnClickListener(this);
            itemLargeStore.setOnClickListener(this);
            itemLargeStoreLabel.setOnClickListener(this);
        }

        private void selectOrUnselectItem(int position) {

            Item thisItem = null;
            String category;
            boolean isTitle = false;
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
                        numItemsInCategory = itemData.getCategoryMap().get(category).getCategoryItemsList().size();
                    }
                    index += numItemsInCategory;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

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

        private Item getItemWithCategories(int position) {

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
                    numItemsInCategory = itemData.getCategoryMap().get(category).getCategoryItemsList().size();
                }
                index += numItemsInCategory;
                adjustedPosition--;
                if (index == adjustedPosition) {
                    break;
                } else if (index >= adjustedPosition) {
                    thisItem = itemData.getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                    break;
                }
            }
            return thisItem;
        }

        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            String category;
            Item thisItem = null;
            boolean isTitle = false;
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
                        numItemsInCategory = itemData.getCategoryMap().get(category).getCategoryItemsList().size();
                    }
                    index += numItemsInCategory;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

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
                    }
                } else if (id == triangleDown.getId()) {
                    if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                        triangleDown.setVisibility(View.GONE);
                        triangleRight.setVisibility(View.VISIBLE);
                        itemLarge.setVisibility(View.GONE);
                        itemSmall.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsContractedInInventory();
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
                        shopping.updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, (numItemsInStock - 1), (numItemsNeeded + 1), numItemsPaused);
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
                        shopping.updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock, (numItemsNeeded - 1), (numItemsPaused + 1));
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
                        shopping.updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, (numItemsInStock + 1), numItemsNeeded, (numItemsPaused - 1));
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
                        shopping.updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, (numItemsInStock - 1), (numItemsNeeded + 1), numItemsPaused);
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
                        shopping.updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock, (numItemsNeeded - 1), (numItemsPaused + 1));
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
                        shopping.updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = shopping.getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = shopping.getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = shopping.getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = shopping.getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        dbCategoryHelper.setCategoryViews(thisCategory, numItemsViewAll, (numItemsInStock + 1), numItemsNeeded, (numItemsPaused - 1));
                        shopping.updateCategoryData();
                    }
                }
            }
        }
    }

    private class SortByStoreTitleRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private FullInventoryRVA adapter;
        private ItemData itemData;
        private StoreData storeData;
        private DBStatusHelper dbStatusHelper;
        private DBStoreHelper dbStoreHelper;

        private LinearLayout sortByStoreRvTitle;
        private TextView storeTitleText;
        private ImageView triangleButtonDown1;
        private ImageView triangleButtonDown2;
        private ImageView triangleButtonRight;
        private ImageView triangleButtonLeft;

        private boolean isExpanded;
        private boolean isContracted;

        private SortByStoreTitleRVH(View itemView, Shopping shopping, FullInventoryRVA adapter, ItemData itemData,
                                    StoreData storeData, DBStatusHelper dbStatus, DBStoreHelper dbStore) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.itemData = itemData;
            this.storeData = storeData;
            this.dbStatusHelper = dbStatus;
            this.dbStoreHelper = dbStore;

            isExpanded = true;
            isContracted = false;

            sortByStoreRvTitle = itemView.findViewById(R.id.sortByStoreRvTitle);
            storeTitleText = itemView.findViewById(R.id.storeTitleText);
            triangleButtonDown1 = itemView.findViewById(R.id.triangleButtonDown1);
            triangleButtonDown2 = itemView.findViewById(R.id.triangleButtonDown2);
            triangleButtonRight = itemView.findViewById(R.id.triangleButtonRight);
            triangleButtonLeft = itemView.findViewById(R.id.triangleButtonLeft);

            storeTitleText.setOnClickListener(this);
            triangleButtonDown1.setOnClickListener(this);
            triangleButtonDown2.setOnClickListener(this);
            triangleButtonRight.setOnClickListener(this);
            triangleButtonLeft.setOnClickListener(this);

        }

        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

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
                    numItemsInStore = itemData.getStoreMap().get(store).getStoreItemsList().size();
                }
                index += numItemsInStore;
                adjustedPosition--;
                if (index >= adjustedPosition) {
                    thisItem = itemData.getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                    break;
                }
            }


            if (id == triangleButtonDown1.getId() || id == triangleButtonDown2.getId()) {
                contractTitle();
                thisItem.getStore().setStoreAsContracted();
            } else if (id == triangleButtonRight.getId() || id == triangleButtonLeft.getId()) {
                expandTitle();
                thisItem.getStore().setStoreAsExpanded();
            } else if (id == storeTitleText.getId() && isExpanded()) {
                contractTitle();
                thisItem.getStore().setStoreAsContracted();
            } else if (id == storeTitleText.getId() && isContracted()) {
                expandTitle();
                thisItem.getStore().setStoreAsExpanded();
            }
        }

        private void expandTitle() {
            this.isExpanded = true;
            this.isContracted = false;
            triangleButtonDown1.setVisibility(View.VISIBLE);
            triangleButtonDown2.setVisibility(View.VISIBLE);
            triangleButtonRight.setVisibility(View.GONE);
            triangleButtonLeft.setVisibility(View.GONE);
        }

        private void contractTitle() {
            this.isExpanded = false;
            this.isContracted = true;
            triangleButtonDown1.setVisibility(View.GONE);
            triangleButtonDown2.setVisibility(View.GONE);
            triangleButtonRight.setVisibility(View.VISIBLE);
            triangleButtonLeft.setVisibility(View.VISIBLE);
        }

        boolean isExpanded() {
            return isExpanded;
        }

        void setAsExpanded() {
            isExpanded = true;
            isContracted = false;
        }

        boolean isContracted() {
            return isContracted;
        }

        void setAsContracted() {
            isExpanded = false;
            isContracted = true;
        }
    }

    private class SortByStoreItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        private TextView itemLargeStore;
        private TextView itemLargeStoreLabel;

        private SortByStoreItemRVH(View itemView, Shopping shopping, FullInventoryRVA adapter,
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
            itemLargeCategory.setOnClickListener(this);
            itemLargeCategoryLabel.setOnClickListener(this);
            itemLargeStore.setOnClickListener(this);
            itemLargeStoreLabel.setOnClickListener(this);
        }

        private void selectOrUnselectItem(int position) {

            String store;
            Item thisItem = null;
            boolean isTitle = false;
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
                        numItemsInStore = itemData.getStoreMap().get(store).getStoreItemsList().size();
                    }
                    index += numItemsInStore;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

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

                        Item lastItem = getItemWithStores(currentlySelected);
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

        private Item getItemWithStores(int position) {

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
                    numItemsInStore = itemData.getStoreMap().get(store).getStoreItemsList().size();
                }
                index += numItemsInStore;
                adjustedPosition--;
                if (index == adjustedPosition) {
                    break;
                } else if (index >= adjustedPosition) {
                    thisItem = itemData.getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                    break;
                }
            }
            return thisItem;
        }

        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            String store;
            Item thisItem = null;
            boolean isTitle = false;
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
                        numItemsInStore = itemData.getStoreMap().get(store).getStoreItemsList().size();
                    }
                    index += numItemsInStore;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = itemData.getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

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
                    }
                } else if (id == triangleDown.getId()) {
                    if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                        triangleDown.setVisibility(View.GONE);
                        triangleRight.setVisibility(View.VISIBLE);
                        itemLarge.setVisibility(View.GONE);
                        itemSmall.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsContractedInShoppingList();
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
                        shopping.updateStatusData();

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, (numItemsInStock - 1), (numItemsNeeded + 1), numItemsPaused);
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
                        shopping.updateStatusData();

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock, (numItemsNeeded - 1), (numItemsPaused + 1));
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
                        shopping.updateStatusData();

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, (numItemsInStock + 1), numItemsNeeded, (numItemsPaused - 1));
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
                        shopping.updateStatusData();

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, (numItemsInStock - 1), (numItemsNeeded + 1), numItemsPaused);
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
                        shopping.updateStatusData();

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(thisStore);
                        dbStoreHelper.setStoreViews(thisStore, numItemsViewAll, numItemsInStock, (numItemsNeeded - 1), (numItemsPaused + 1));
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
                        shopping.updateStatusData();

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

    private class SortAlphabeticalItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private FullInventoryRVA adapter;
        private ItemData itemData;
        private DBStatusHelper dbStatusHelper;

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
        private TextView itemLargeStore;
        private TextView itemLargeStoreLabel;

        private SortAlphabeticalItemRVH(View itemView, Shopping shopping, FullInventoryRVA adapter) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.itemData = shopping.getItemData();
            this.dbStatusHelper = new DBStatusHelper(shopping);

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
            itemLargeCategory.setOnClickListener(this);
            itemLargeCategoryLabel.setOnClickListener(this);
            itemLargeStore.setOnClickListener(this);
            itemLargeStoreLabel.setOnClickListener(this);
        }

        private void selectOrUnselectItem(int position) {

            Item thisItem = itemData.getItemListAZ().get(position);

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

        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            Item thisItem = itemData.getItemListAZ().get(position);

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
            } else if (id == itemLargeStore.getId()) {
                selectOrUnselectItem(position);
            } else if (id == itemLargeStore.getId()) {
                selectOrUnselectItem(position);
            } else if (id == triangleRight.getId()) {
                if (triangleRight.getVisibility() == View.VISIBLE && triangleDown.getVisibility() == View.GONE) {
                    triangleRight.setVisibility(View.GONE);
                    triangleDown.setVisibility(View.VISIBLE);
                    itemSmall.setVisibility(View.GONE);
                    itemLarge.setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsExpandedInShoppingList();
                }
            } else if (id == triangleDown.getId()) {
                if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                    triangleDown.setVisibility(View.GONE);
                    triangleRight.setVisibility(View.VISIBLE);
                    itemLarge.setVisibility(View.GONE);
                    itemSmall.setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsContractedInShoppingList();
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