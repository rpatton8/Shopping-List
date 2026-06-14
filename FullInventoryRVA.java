package ryan.android.shopping;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class FullInventoryRVA extends RecyclerView.Adapter {

    private View view;
    private Context context;
    private Shopping shopping;
    private ItemData itemData;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBStatusHelper dbStatusHelper;
    private DBCategoryHelper dbCategoryHelper;
    private DBStoreHelper dbStoreHelper;

    FullInventoryRVA(View view, Context context, Shopping shopping, ItemData itemData, CategoryData categoryData,
                     StoreData storeData, DBStatusHelper dbStatus, DBStoreHelper dbStore, DBCategoryHelper dbCategory) {
        setView(view);
        setContext(context);
        setShopping(shopping);
        setItemData(itemData);
        setCategoryData(categoryData);
        setStoreData(storeData);
        setDbStatusHelper(dbStatus);
        setDbCategoryHelper(dbCategory);
        setDbStoreHelper(dbStore);
    }

    private FullInventoryRVA getThis() {
        return this;
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        getThis().context = context;
    }

    private View getView() {
        return view;
    }

    private void setView(View view) {
        getThis().view = view;
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

    public int getItemViewType(int position) {

        if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_CATEGORY)) {
            if (position == 0) return R.layout.sort_by_category_rv_title;
            int index = 0;
            for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                String category = getCategoryData().getCategoryList().get(i);
                int numItemsInCategory;
                if (getItemData().getCategoryMap().get(category) == null) {
                    numItemsInCategory = 0;
                } else {
                    numItemsInCategory = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                }
                index += numItemsInCategory + 1;
                if (position == index) return R.layout.sort_by_category_rv_title;
            }
            return R.layout.sort_by_category_rv_item;

        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_STORE)) {

            if (position == 0) return R.layout.sort_by_store_rv_title;
            int index = 0;
            for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                String store = getStoreData().getStoreList().get(i);
                int numItemsInStore;
                if (getItemData().getStoreMap().get(store) == null) {
                    numItemsInStore = 0;
                } else {
                    numItemsInStore = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                }
                index += numItemsInStore + 1;
                if (position == index) return R.layout.sort_by_store_rv_title;
            }
            return R.layout.sort_by_store_rv_item;

        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {

            return R.layout.sort_alphabetical_rv_item;

        } else return -1;

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        setView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));

        if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_CATEGORY)) {

            if (viewType == R.layout.sort_by_category_rv_title) {
                return new SortByCategoryTitleRVH(getView(), getShopping(), getThis(), getItemData(), getCategoryData(), getDbStatusHelper(), getDbCategoryHelper());
            } else if (viewType == R.layout.sort_by_category_rv_item) {
                return new SortByCategoryItemRVH(getView(), getContext(), getShopping(), getThis(), getItemData(), getCategoryData(), getDbStatusHelper(), getDbCategoryHelper());
            } else return new RecyclerView.ViewHolder(getView()) {};

        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_STORE)) {

            if (viewType == R.layout.sort_by_store_rv_title) {
                return new SortByStoreTitleRVH(getView(), getShopping(), getThis(), getItemData(), getStoreData(), getDbStatusHelper(), getDbStoreHelper());
            } else if (viewType == R.layout.sort_by_store_rv_item) {
                return new SortByStoreItemRVH(getView(), getContext(), getShopping(), getThis(), getItemData(), getStoreData(), getDbStatusHelper(), getDbStoreHelper());
            } else return new RecyclerView.ViewHolder(getView()) {};

        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {

            if (viewType == R.layout.sort_alphabetical_rv_item) {
                return new SortAlphabeticalItemRVH(getView(), getContext(), getShopping(), getThis(), getItemData(), getDbStatusHelper());
            } else return new RecyclerView.ViewHolder(getView()) {};

        } else return new RecyclerView.ViewHolder(getView()) {};

    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_CATEGORY)) {
            onBindViewHolderByCategory(holder, position);
        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_STORE)) {
            onBindViewHolderByStore(holder, position);
        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {
            onBindViewHolderAlphabetical(holder, position);
        }

    }

//-----------------------------------Sort By Category-----------------------------------------------

    private void onBindViewHolderByCategory(RecyclerView.ViewHolder holder, int position) {

        Item thisItem = null;
        String category = null;
        boolean isTitle = false;
        int adjustedPosition;

        if (position == 0) {
            isTitle = true;
            category = getCategoryData().getCategoryList().get(0);
        } else {
            int index = 0;
            adjustedPosition = position;
            for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                category = getCategoryData().getCategoryList().get(i);
                int numItemsInCategory;
                if (getItemData().getCategoryMap().get(category) == null) {
                    numItemsInCategory = 0;
                } else {
                    numItemsInCategory = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                }
                index += numItemsInCategory;
                adjustedPosition--;
                if (index == adjustedPosition) {
                    isTitle = true;
                    category = getCategoryData().getCategoryList().get(i + 1);
                    break;
                } else if (index >= adjustedPosition) {
                    isTitle = false;
                    thisItem = getItemData().getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                    break;
                }
            }
        }

        if (isTitle) { // titles

            SortByCategoryTitleRVH categoryTitleHolder = (SortByCategoryTitleRVH) holder;

            categoryTitleHolder.getCategoryTitleText().setText(category);
            categoryTitleHolder.getSortByCategoryRvTitle().setVisibility(View.VISIBLE);

            if (categoryTitleHolder.isExpanded()) {
                //getItemData().getCategoryMap().get(category).setCategoryAsContracted();
                categoryTitleHolder.getTriangleButtonDown1().setVisibility(View.VISIBLE);
                categoryTitleHolder.getTriangleButtonDown2().setVisibility(View.VISIBLE);
                categoryTitleHolder.getTriangleButtonRight().setVisibility(View.GONE);
                categoryTitleHolder.getTriangleButtonLeft().setVisibility(View.GONE);

            } else if (categoryTitleHolder.isContracted()) {
                //getItemData().getCategoryMap().get(category).setCategoryAsExpanded();
                categoryTitleHolder.getTriangleButtonDown1().setVisibility(View.GONE);
                categoryTitleHolder.getTriangleButtonDown2().setVisibility(View.GONE);
                categoryTitleHolder.getTriangleButtonRight().setVisibility(View.VISIBLE);
                categoryTitleHolder.getTriangleButtonLeft().setVisibility(View.VISIBLE);
            }

            if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL) && getCategoryData().getCategoryViewAllMap().get(category) == 0) {
                categoryTitleHolder.getSortByCategoryRvTitle().setVisibility(View.GONE);
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK) && getCategoryData().getCategoryViewInStockMap().get(category) == 0) {
                categoryTitleHolder.getSortByCategoryRvTitle().setVisibility(View.GONE);
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED) && getCategoryData().getCategoryViewNeededMap().get(category) == 0) {
                categoryTitleHolder.getSortByCategoryRvTitle().setVisibility(View.GONE);
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED) && getCategoryData().getCategoryViewPausedMap().get(category) == 0) {
                categoryTitleHolder.getSortByCategoryRvTitle().setVisibility(View.GONE);
            }

        } else {  // item data

            SortByCategoryItemRVH categoryItemHolder = (SortByCategoryItemRVH) holder;

            if (thisItem.getCategory().categoryIsContracted()) {
                categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                return;
            }

            if (thisItem.getStatus().isExpandedInInventory()) {
                categoryItemHolder.getItemSmallName().setText(thisItem.getItemName());
                categoryItemHolder.getItemLargeName().setText(thisItem.getItemName());
                categoryItemHolder.getItemLargeBrand().setText(thisItem.getBrandType());
                categoryItemHolder.getItemLargeCategory().setText(thisItem.getCategory().toString());
                categoryItemHolder.getItemLargeStore().setText(thisItem.getStore().toString());
                categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                categoryItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
                categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                categoryItemHolder.getItemLarge().setVisibility(View.VISIBLE);
            } else if (thisItem.getStatus().isContractedInInventory()) {
                categoryItemHolder.getItemSmallName().setText(thisItem.getItemName());
                categoryItemHolder.getItemLargeName().setText(thisItem.getItemName());
                categoryItemHolder.getItemLargeBrand().setText(thisItem.getBrandType());
                categoryItemHolder.getItemLargeCategory().setText(thisItem.getCategory().toString());
                categoryItemHolder.getItemLargeStore().setText(thisItem.getStore().toString());
                categoryItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                categoryItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                categoryItemHolder.getItemLarge().setVisibility(View.GONE);
            }

            if (thisItem.getStatus().isInStock()) {
                categoryItemHolder.getItemSmallPaused().setVisibility(View.GONE);
                categoryItemHolder.getItemLargePaused().setVisibility(View.GONE);
                categoryItemHolder.getItemSmallNeeded().setVisibility(View.GONE);
                categoryItemHolder.getItemLargeNeeded().setVisibility(View.GONE);
                categoryItemHolder.getItemSmallInStock().setVisibility(View.VISIBLE);
                categoryItemHolder.getItemLargeInStock().setVisibility(View.VISIBLE);
            } else if (thisItem.getStatus().isNeeded()) {
                categoryItemHolder.getItemSmallInStock().setVisibility(View.GONE);
                categoryItemHolder.getItemLargeInStock().setVisibility(View.GONE);
                categoryItemHolder.getItemSmallPaused().setVisibility(View.GONE);
                categoryItemHolder.getItemLargePaused().setVisibility(View.GONE);
                categoryItemHolder.getItemSmallNeeded().setVisibility(View.VISIBLE);
                categoryItemHolder.getItemLargeNeeded().setVisibility(View.VISIBLE);
            } else if (thisItem.getStatus().isPaused()) {
                categoryItemHolder.getItemSmallNeeded().setVisibility(View.GONE);
                categoryItemHolder.getItemLargeNeeded().setVisibility(View.GONE);
                categoryItemHolder.getItemSmallInStock().setVisibility(View.GONE);
                categoryItemHolder.getItemLargeInStock().setVisibility(View.GONE);
                categoryItemHolder.getItemSmallPaused().setVisibility(View.VISIBLE);
                categoryItemHolder.getItemLargePaused().setVisibility(View.VISIBLE);
            }

            if (thisItem.getStatus().isSelectedInInventory()) {
                categoryItemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                categoryItemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

            } else {
                if (getShopping().itemIsSelectedInInventory() && getShopping().getSelectedItemPositionInInventory() == position) {
                    categoryItemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                    categoryItemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);
                } else {
                    categoryItemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_unselected);
                    categoryItemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_unselected);
                }
            }
            //--------------------------------By Category - All---------------------------------
            if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                    categoryItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                    categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                    categoryItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    categoryItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                    categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                    categoryItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                    categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                }
            //--------------------------------By Category - In Stock----------------------------
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
                if (thisItem.getStatus().isInStock()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                }
            //--------------------------------By Category - Needed------------------------------
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
                if (thisItem.getStatus().isInStock()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.VISIBLE);

                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                }
            //--------------------------------By Category - Paused------------------------------
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
                if (thisItem.getStatus().isInStock()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.GONE);
                        categoryItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        categoryItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                        categoryItemHolder.getItemLarge().setVisibility(View.GONE);
                        categoryItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                        categoryItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                }
            }
        }
    }

//------------------------------------------Sort By Store-------------------------------------------

    private void onBindViewHolderByStore(RecyclerView.ViewHolder holder, int position) {

        Item thisItem = null;
        String store = null;
        boolean isTitle = false;
        int adjustedPosition;

        if (position == 0) {
            isTitle = true;
            store = getStoreData().getStoreList().get(0);
        } else {
            int index = 0;
            adjustedPosition = position;
            for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                store = getStoreData().getStoreList().get(i);
                int numItemsInStore;
                if (getItemData().getStoreMap().get(store) == null) {
                    numItemsInStore = 0;
                } else {
                    numItemsInStore = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                }
                index += numItemsInStore;
                adjustedPosition--;
                if (index == adjustedPosition) {
                    isTitle = true;
                    store = getStoreData().getStoreList().get(i + 1);
                    break;
                } else if (index >= adjustedPosition) {
                    isTitle = false;
                    thisItem = getItemData().getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                    break;
                }
            }
        }

        if (isTitle) { // titles

            SortByStoreTitleRVH storeTitleHolder = (SortByStoreTitleRVH) holder;

            storeTitleHolder.getStoreTitleText().setText(store);
            storeTitleHolder.getSortByStoreRvTitle().setVisibility(View.VISIBLE);

            if (storeTitleHolder.isExpanded()) {

                //getItemData().getStoreMap().get(store).setStoreAsContracted();
                storeTitleHolder.getTriangleButtonDown1().setVisibility(View.VISIBLE);
                storeTitleHolder.getTriangleButtonDown2().setVisibility(View.VISIBLE);
                storeTitleHolder.getTriangleButtonRight().setVisibility(View.GONE);
                storeTitleHolder.getTriangleButtonLeft().setVisibility(View.GONE);


            } else if (storeTitleHolder.isContracted()) {

                //getItemData().getStoreMap().get(store).setStoreAsExpanded();
                storeTitleHolder.getTriangleButtonDown1().setVisibility(View.GONE);
                storeTitleHolder.getTriangleButtonDown2().setVisibility(View.GONE);
                storeTitleHolder.getTriangleButtonRight().setVisibility(View.VISIBLE);
                storeTitleHolder.getTriangleButtonLeft().setVisibility(View.VISIBLE);

            }

            if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL) && getStoreData().getStoreViewAllMap().get(store) == 0) {
                storeTitleHolder.getSortByStoreRvTitle().setVisibility(View.GONE);
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK) && getStoreData().getStoreViewInStockMap().get(store) == 0) {
                storeTitleHolder.getSortByStoreRvTitle().setVisibility(View.GONE);
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED) && getStoreData().getStoreViewNeededMap().get(store) == 0) {
                storeTitleHolder.getSortByStoreRvTitle().setVisibility(View.GONE);
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED) && getStoreData().getStoreViewPausedMap().get(store) == 0) {
                storeTitleHolder.getSortByStoreRvTitle().setVisibility(View.GONE);
            }

        } else {  // item data

            SortByStoreItemRVH storeItemHolder = (SortByStoreItemRVH) holder;

            if (thisItem.getStore().storeIsContracted()) {
                storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                storeItemHolder.getItemSmall().setVisibility(View.GONE);
                storeItemHolder.getItemLarge().setVisibility(View.GONE);
                return;
            }

            if (thisItem.getStatus().isExpandedInInventory()) {
                storeItemHolder.getItemSmallName().setText(thisItem.getItemName());
                storeItemHolder.getItemLargeName().setText(thisItem.getItemName());
                storeItemHolder.getItemLargeBrand().setText(thisItem.getBrandType());
                storeItemHolder.getItemLargeCategory().setText(thisItem.getCategory().toString());
                storeItemHolder.getItemLargeStore().setText(thisItem.getStore().toString());
                storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                storeItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
                storeItemHolder.getItemSmall().setVisibility(View.GONE);
                storeItemHolder.getItemLarge().setVisibility(View.VISIBLE);
            } else if (thisItem.getStatus().isContractedInInventory()) {
                storeItemHolder.getItemSmallName().setText(thisItem.getItemName());
                storeItemHolder.getItemLargeName().setText(thisItem.getItemName());
                storeItemHolder.getItemLargeBrand().setText(thisItem.getBrandType());
                storeItemHolder.getItemLargeCategory().setText(thisItem.getCategory().toString());
                storeItemHolder.getItemLargeStore().setText(thisItem.getStore().toString());
                storeItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                storeItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                storeItemHolder.getItemLarge().setVisibility(View.GONE);
            }

            if (thisItem.getStatus().isInStock()) {
                storeItemHolder.getItemSmallPaused().setVisibility(View.GONE);
                storeItemHolder.getItemLargePaused().setVisibility(View.GONE);
                storeItemHolder.getItemSmallNeeded().setVisibility(View.GONE);
                storeItemHolder.getItemLargeNeeded().setVisibility(View.GONE);
                storeItemHolder.getItemSmallInStock().setVisibility(View.VISIBLE);
                storeItemHolder.getItemLargeInStock().setVisibility(View.VISIBLE);
            } else if (thisItem.getStatus().isNeeded()) {
                storeItemHolder.getItemSmallInStock().setVisibility(View.GONE);
                storeItemHolder.getItemLargeInStock().setVisibility(View.GONE);
                storeItemHolder.getItemSmallPaused().setVisibility(View.GONE);
                storeItemHolder.getItemLargePaused().setVisibility(View.GONE);
                storeItemHolder.getItemSmallNeeded().setVisibility(View.VISIBLE);
                storeItemHolder.getItemLargeNeeded().setVisibility(View.VISIBLE);
            } else if (thisItem.getStatus().isPaused()) {
                storeItemHolder.getItemSmallNeeded().setVisibility(View.GONE);
                storeItemHolder.getItemLargeNeeded().setVisibility(View.GONE);
                storeItemHolder.getItemSmallInStock().setVisibility(View.GONE);
                storeItemHolder.getItemLargeInStock().setVisibility(View.GONE);
                storeItemHolder.getItemSmallPaused().setVisibility(View.VISIBLE);
                storeItemHolder.getItemLargePaused().setVisibility(View.VISIBLE);
            }
            if (thisItem.getStatus().isSelectedInInventory()) {
                storeItemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                storeItemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

            } else {
                if (getShopping().itemIsSelectedInInventory() && getShopping().getSelectedItemPositionInInventory() == position) {
                    storeItemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                    storeItemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);
                } else {
                    storeItemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_unselected);
                    storeItemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_unselected);
                }
            }
            //--------------------------------By Store - All------------------------------------
            if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    storeItemHolder.getItemSmall().setVisibility(View.GONE);
                    storeItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                    storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                    storeItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    storeItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                    storeItemHolder.getItemLarge().setVisibility(View.GONE);
                    storeItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                    storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                }
            //--------------------------------By Store - In Stock-------------------------------
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
                if (thisItem.getStatus().isInStock()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                }
            //--------------------------------By Store - Needed---------------------------------
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
                if (thisItem.getStatus().isInStock()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.VISIBLE);

                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                }
            //--------------------------------By Store - Paused---------------------------------
            } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
                if (thisItem.getStatus().isInStock()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (thisItem.getStatus().isExpandedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.GONE);
                        storeItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                        storeItemHolder.getTriangleRight().setVisibility(View.GONE);
                        storeItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
                    } else if (thisItem.getStatus().isContractedInInventory()) {
                        storeItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                        storeItemHolder.getItemLarge().setVisibility(View.GONE);
                        storeItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                        storeItemHolder.getTriangleDown().setVisibility(View.GONE);
                    }
                }
            }
        }
    }

//------------------------------------------Alphabetical--------------------------------------------

    private void onBindViewHolderAlphabetical(RecyclerView.ViewHolder holder, int position) {

        Item thisItem = getItemData().getItemListAZ().get(position);

        SortAlphabeticalItemRVH alphabeticalItemHolder = (SortAlphabeticalItemRVH) holder;

        if (thisItem.getStatus().isExpandedInInventory()) {
            alphabeticalItemHolder.getItemSmallName().setText(thisItem.getItemName());
            alphabeticalItemHolder.getItemLargeName().setText(thisItem.getItemName());
            alphabeticalItemHolder.getItemLargeBrand().setText(thisItem.getBrandType());
            alphabeticalItemHolder.getItemLargeCategory().setText(thisItem.getCategory().toString());
            alphabeticalItemHolder.getItemLargeStore().setText(thisItem.getStore().toString());
            alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
            alphabeticalItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
            alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemLarge().setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isContractedInInventory()) {
            alphabeticalItemHolder.getItemSmallName().setText(thisItem.getItemName());
            alphabeticalItemHolder.getItemLargeName().setText(thisItem.getItemName());
            alphabeticalItemHolder.getItemLargeBrand().setText(thisItem.getBrandType());
            alphabeticalItemHolder.getItemLargeCategory().setText(thisItem.getCategory().toString());
            alphabeticalItemHolder.getItemLargeStore().setText(thisItem.getStore().toString());
            alphabeticalItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
            alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemSmall().setVisibility(View.VISIBLE);
            alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
        }

        if (thisItem.getStatus().isInStock()) {
            alphabeticalItemHolder.getItemSmallPaused().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemLargePaused().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemSmallNeeded().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemLargeNeeded().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemSmallInStock().setVisibility(View.VISIBLE);
            alphabeticalItemHolder.getItemLargeInStock().setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isNeeded()) {
            alphabeticalItemHolder.getItemSmallInStock().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemLargeInStock().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemSmallPaused().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemLargePaused().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemSmallNeeded().setVisibility(View.VISIBLE);
            alphabeticalItemHolder.getItemLargeNeeded().setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isPaused()) {
            alphabeticalItemHolder.getItemSmallNeeded().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemLargeNeeded().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemSmallInStock().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemLargeInStock().setVisibility(View.GONE);
            alphabeticalItemHolder.getItemSmallPaused().setVisibility(View.VISIBLE);
            alphabeticalItemHolder.getItemLargePaused().setVisibility(View.VISIBLE);
        }

        if (thisItem.getStatus().isSelectedInInventory()) {
            alphabeticalItemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
            alphabeticalItemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

        } else {
            if (getShopping().itemIsSelectedInInventory() && getShopping().getSelectedItemPositionInInventory() == position) {
                alphabeticalItemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                alphabeticalItemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);
            } else {
                alphabeticalItemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_unselected);
                alphabeticalItemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_unselected);
            }
        }
        //--------------------------------Alphabetical - All------------------------------------
        if (getShopping().getInventoryView().equals(Shopping.VIEW_ALL)) {
            if (thisItem.getStatus().isExpandedInInventory()) {
                alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                alphabeticalItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                alphabeticalItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
            } else if (thisItem.getStatus().isContractedInInventory()) {
                alphabeticalItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                alphabeticalItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
            }
        //--------------------------------Alphabetical - In Stock-------------------------------
        } else if (getShopping().getInventoryView().equals(Shopping.VIEW_INSTOCK)) {
            if (thisItem.getStatus().isInStock()) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                }
            } else if (thisItem.getStatus().isNeeded()) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                }
            } else if (thisItem.getStatus().isPaused()) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                }
            }
        //--------------------------------Alphabetical - Needed---------------------------------
        } else if (getShopping().getInventoryView().equals(Shopping.VIEW_NEEDED)) {
            if (thisItem.getStatus().isInStock()) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                }
            } else if (thisItem.getStatus().isNeeded()) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.VISIBLE);

                } else if (thisItem.getStatus().isContractedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                }
            } else if (thisItem.getStatus().isPaused()) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                }
            }
        //--------------------------------Alphabetical - Paused---------------------------------
        } else if (getShopping().getInventoryView().equals(Shopping.VIEW_PAUSED)) {
            if (thisItem.getStatus().isInStock()) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                }
            } else if (thisItem.getStatus().isNeeded()) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                }
            } else if (thisItem.getStatus().isPaused()) {
                if (thisItem.getStatus().isExpandedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.GONE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isContractedInInventory()) {
                    alphabeticalItemHolder.getItemSmall().setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.getItemLarge().setVisibility(View.GONE);
                    alphabeticalItemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                    alphabeticalItemHolder.getTriangleDown().setVisibility(View.GONE);
                }
            }
        }
    }

    public int getItemCount() {
        if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_CATEGORY)) {
            return (getItemData().getItemListByCategory().size() + getCategoryData().getCategoryList().size());
        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_BY_STORE)) {
            return (getItemData().getItemListByStore().size() + getStoreData().getStoreList().size());
        } else if (getShopping().getInventorySortBy().equals(Shopping.SORT_ALPHABETICAL)) {
            return (getItemData().getItemListAZ().size());
        } else return -1;
    }

    //---------------------------------------Category Titles----------------------------------------

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
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(itemData);
            setCategoryData(categoryData);
            setDbStatusHelper(dbStatus);
            setDbCategoryHelper(dbCategory);

            if (shopping.getCategoryTitles().equals(Shopping.CATEGORY_TITLES_EXPANDED)) {
                getThis().setAsExpanded();
            } else if (shopping.getCategoryTitles().equals(Shopping.CATEGORY_TITLES_CONTRACTED)) {
                getThis().setAsContracted();
            }

            setSortByCategoryRvTitle((LinearLayout) itemView.findViewById(R.id.sortByCategoryRvTitle));
            setCategoryTitleText((TextView) itemView.findViewById(R.id.categoryTitleText));
            setTriangleButtonDown1((ImageView) itemView.findViewById(R.id.triangleButtonDown1));
            setTriangleButtonDown2((ImageView) itemView.findViewById(R.id.triangleButtonDown2));
            setTriangleButtonRight((ImageView) itemView.findViewById(R.id.triangleButtonRight));
            setTriangleButtonLeft((ImageView) itemView.findViewById(R.id.triangleButtonLeft));

            getCategoryTitleText().setOnClickListener(this);
            getTriangleButtonDown1().setOnClickListener(this);
            getTriangleButtonDown2().setOnClickListener(this);
            getTriangleButtonRight().setOnClickListener(this);
            getTriangleButtonLeft().setOnClickListener(this);

        }

        private SortByCategoryTitleRVH getThis() {
            return this;
        }

        private Shopping getShopping() {
            return shopping;
        }

        private void setShopping(Shopping shopping) {
            getThis().shopping = shopping;
        }

        private FullInventoryRVA getAdapter() {
            return adapter;
        }

        private void setAdapter(FullInventoryRVA adapter) {
            getThis().adapter = adapter;
        }

        private ItemData getItemData() {
            return itemData;
        }

        private void setItemData(ItemData itemData) {
            getThis().itemData = itemData;
        }

        private CategoryData getCategoryData() {
            return categoryData;
        }

        private void setCategoryData(CategoryData categoryData) {
            getThis().categoryData = categoryData;
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

        private LinearLayout getSortByCategoryRvTitle() {
            return sortByCategoryRvTitle;
        }

        private void setSortByCategoryRvTitle(LinearLayout sortByCategoryRvTitle) {
            getThis().sortByCategoryRvTitle = sortByCategoryRvTitle;
        }

        private TextView getCategoryTitleText() {
            return categoryTitleText;
        }

        private void setCategoryTitleText(TextView categoryTitleText) {
            getThis().categoryTitleText = categoryTitleText;
        }

        private ImageView getTriangleButtonDown1() {
            return triangleButtonDown1;
        }

        private void setTriangleButtonDown1(ImageView triangleButtonDown1) {
            getThis().triangleButtonDown1 = triangleButtonDown1;
        }

        private ImageView getTriangleButtonDown2() {
            return triangleButtonDown2;
        }

        private void setTriangleButtonDown2(ImageView triangleButtonDown2) {
            getThis().triangleButtonDown2 = triangleButtonDown2;
        }

        private ImageView getTriangleButtonRight() {
            return triangleButtonRight;
        }

        private void setTriangleButtonRight(ImageView triangleButtonRight) {
            getThis().triangleButtonRight = triangleButtonRight;
        }

        private ImageView getTriangleButtonLeft() {
            return triangleButtonLeft;
        }

        private void setTriangleButtonLeft(ImageView triangleButtonLeft) {
            getThis().triangleButtonLeft = triangleButtonLeft;
        }

        private boolean isExpanded() {
            return isExpanded;
        }

        private void setAsExpanded() {
            isExpanded = true;
            isContracted = false;
        }

        private boolean isContracted() {
            return isContracted;
        }

        private void setAsContracted() {
            isExpanded = false;
            isContracted = true;
        }

        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            String category;
            Item thisItem = null;
            int adjustedPosition;

            int index = 0;
            adjustedPosition = position;
            for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                category = getCategoryData().getCategoryList().get(i);
                int numItemsInCategory;
                if (getItemData().getCategoryMap().get(category) == null) {
                    numItemsInCategory = 0;
                } else {
                    numItemsInCategory = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                }
                index += numItemsInCategory;
                adjustedPosition--;
                if (index >= adjustedPosition) {
                    thisItem = getItemData().getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                    break;
                }
            }

            if (id == getTriangleButtonDown1().getId() || id == getTriangleButtonDown2().getId()) {
                contractTitle();
                thisItem.getCategory().setCategoryAsContracted();
            } else if (id == getTriangleButtonRight().getId() || id == getTriangleButtonLeft().getId()) {
                expandTitle();
                thisItem.getCategory().setCategoryAsExpanded();
            } else if (id == getCategoryTitleText().getId() && isExpanded()) {
                contractTitle();
                thisItem.getCategory().setCategoryAsContracted();
            } else if (id == getCategoryTitleText().getId() && isContracted()) {
                expandTitle();
                thisItem.getCategory().setCategoryAsExpanded();
            }
        }

        private void expandTitle() {
            getThis().setAsExpanded();
            getTriangleButtonDown1().setVisibility(View.VISIBLE);
            getTriangleButtonDown2().setVisibility(View.VISIBLE);
            getTriangleButtonRight().setVisibility(View.GONE);
            getTriangleButtonLeft().setVisibility(View.GONE);
        }

        private void contractTitle() {
            getThis().setAsContracted();
            getTriangleButtonDown1().setVisibility(View.GONE);
            getTriangleButtonDown2().setVisibility(View.GONE);
            getTriangleButtonRight().setVisibility(View.VISIBLE);
            getTriangleButtonLeft().setVisibility(View.VISIBLE);
        }



    }

//-----------------------------------------Category Items-------------------------------------------

    private class SortByCategoryItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private Context context;
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

        private final long doubleClickTimeout = 400;
        private long lastClickTime = 0;

        private SortByCategoryItemRVH(View itemView, Context context, Shopping shopping, FullInventoryRVA adapter,  ItemData itemData,
                                      CategoryData categoryData, DBStatusHelper dbStatus, DBCategoryHelper dbCategory) {

            super(itemView);
            setContext(context);
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(itemData);
            setCategoryData(categoryData);
            setDbStatusHelper(dbStatus);
            setDbCategoryHelper(dbCategory);

            setTriangleRight((Button) itemView.findViewById(R.id.triangleButtonRight));
            setTriangleDown((Button) itemView.findViewById(R.id.triangleButtonDown));
            setItemSmall((LinearLayout) itemView.findViewById(R.id.itemSmall));
            setItemLarge((LinearLayout) itemView.findViewById(R.id.itemLarge));
            setItemSmallName((TextView) itemView.findViewById(R.id.itemSmallName));
            setItemLargeName((TextView) itemView.findViewById(R.id.itemLargeName));
            setItemSmallInStock((TextView) itemView.findViewById(R.id.itemSmallInStock));
            setItemSmallNeeded((TextView) itemView.findViewById(R.id.itemSmallNeeded));
            setItemSmallPaused((TextView) itemView.findViewById(R.id.itemSmallPaused));
            setItemLargeInStock((TextView) itemView.findViewById(R.id.itemLargeInStock));
            setItemLargeNeeded((TextView) itemView.findViewById(R.id.itemLargeNeeded));
            setItemLargePaused((TextView) itemView.findViewById(R.id.itemLargePaused));
            setItemLargeBrand((TextView) itemView.findViewById(R.id.itemLargeBrand));
            setItemLargeBrandLabel((TextView) itemView.findViewById(R.id.itemLargeBrandLabel));
            setItemLargeCategory((TextView) itemView.findViewById(R.id.itemLargeCategory));
            setItemLargeCategoryLabel((TextView) itemView.findViewById(R.id.itemLargeCategoryLabel));
            setItemLargeStore((TextView) itemView.findViewById(R.id.itemLargeStore));
            setItemLargeStoreLabel((TextView) itemView.findViewById(R.id.itemLargeStoreLabel));

            getTriangleRight().setOnClickListener(this);
            getTriangleDown().setOnClickListener(this);
            getItemSmall().setOnClickListener(this);
            getItemLarge().setOnClickListener(this);
            getItemSmallName().setOnClickListener(this);
            getItemLargeName().setOnClickListener(this);
            getItemSmallInStock().setOnClickListener(this);
            getItemSmallNeeded().setOnClickListener(this);
            getItemSmallPaused().setOnClickListener(this);
            getItemLargeInStock().setOnClickListener(this);
            getItemLargeNeeded().setOnClickListener(this);
            getItemLargePaused().setOnClickListener(this);
            getItemLargeBrand().setOnClickListener(this);
            getItemLargeBrandLabel().setOnClickListener(this);
            getItemLargeCategory().setOnClickListener(this);
            getItemLargeCategoryLabel().setOnClickListener(this);
            getItemLargeStore().setOnClickListener(this);
            getItemLargeStoreLabel().setOnClickListener(this);
        }

        private SortByCategoryItemRVH getThis() {
            return this;
        }

        private Context getContext() {
            return context;
        }

        private void setContext(Context context) {
            getThis().context = context;
        }

        private Shopping getShopping() {
            return shopping;
        }

        private void setShopping(Shopping shopping) {
            getThis().shopping = shopping;
        }

        private FullInventoryRVA getAdapter() {
            return adapter;
        }

        private void setAdapter(FullInventoryRVA adapter) {
            getThis().adapter = adapter;
        }

        private ItemData getItemData() {
            return itemData;
        }

        private void setItemData(ItemData itemData) {
            getThis().itemData = itemData;
        }

        private CategoryData getCategoryData() {
            return categoryData;
        }

        private void setCategoryData(CategoryData categoryData) {
            getThis().categoryData = categoryData;
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

        private Button getTriangleRight() {
            return triangleRight;
        }

        private void setTriangleRight(Button triangleRight) {
            getThis().triangleRight = triangleRight;
        }

        private Button getTriangleDown() {
            return triangleDown;
        }

        private void setTriangleDown(Button triangleDown) {
            getThis().triangleDown = triangleDown;
        }

        private LinearLayout getItemSmall() {
            return itemSmall;
        }

        private void setItemSmall(LinearLayout itemSmall) {
            getThis().itemSmall = itemSmall;
        }

        private LinearLayout getItemLarge() {
            return itemLarge;
        }

        private void setItemLarge(LinearLayout itemLarge) {
            getThis().itemLarge = itemLarge;
        }

        private TextView getItemSmallName() {
            return itemSmallName;
        }

        private void setItemSmallName(TextView itemSmallName) {
            getThis().itemSmallName = itemSmallName;
        }

        private TextView getItemLargeName() {
            return itemLargeName;
        }

        private void setItemLargeName(TextView itemLargeName) {
            getThis().itemLargeName = itemLargeName;
        }

        private TextView getItemSmallInStock() {
            return itemSmallInStock;
        }

        private void setItemSmallInStock(TextView itemSmallInStock) {
            getThis().itemSmallInStock = itemSmallInStock;
        }

        private TextView getItemSmallNeeded() {
            return itemSmallNeeded;
        }

        private void setItemSmallNeeded(TextView itemSmallNeeded) {
            getThis().itemSmallNeeded = itemSmallNeeded;
        }

        private TextView getItemSmallPaused() {
            return itemSmallPaused;
        }

        private void setItemSmallPaused(TextView itemSmallPaused) {
            getThis().itemSmallPaused = itemSmallPaused;
        }

        private TextView getItemLargeInStock() {
            return itemLargeInStock;
        }

        private void setItemLargeInStock(TextView itemLargeInStock) {
            getThis().itemLargeInStock = itemLargeInStock;
        }

        private TextView getItemLargeNeeded() {
            return itemLargeNeeded;
        }

        private void setItemLargeNeeded(TextView itemLargeNeeded) {
            getThis().itemLargeNeeded = itemLargeNeeded;
        }

        private TextView getItemLargePaused() {
            return itemLargePaused;
        }

        private void setItemLargePaused(TextView itemLargePaused) {
            getThis().itemLargePaused = itemLargePaused;
        }

        private TextView getItemLargeBrand() {
            return itemLargeBrand;
        }

        private void setItemLargeBrand(TextView itemLargeBrand) {
            getThis().itemLargeBrand = itemLargeBrand;
        }

        private TextView getItemLargeBrandLabel() {
            return itemLargeBrandLabel;
        }

        private void setItemLargeBrandLabel(TextView itemLargeBrandLabel) {
            getThis().itemLargeBrandLabel = itemLargeBrandLabel;
        }

        private TextView getItemLargeCategory() {
            return itemLargeCategory;
        }

        private void setItemLargeCategory(TextView itemLargeCategory) {
            getThis().itemLargeCategory = itemLargeCategory;
        }

        private TextView getItemLargeCategoryLabel() {
            return itemLargeCategoryLabel;
        }

        private void setItemLargeCategoryLabel(TextView itemLargeCategoryLabel) {
            getThis().itemLargeCategoryLabel = itemLargeCategoryLabel;
        }

        private TextView getItemLargeStore() {
            return itemLargeStore;
        }

        private void setItemLargeStore(TextView itemLargeStore) {
            getThis().itemLargeStore = itemLargeStore;
        }

        private TextView getItemLargeStoreLabel() {
            return itemLargeStoreLabel;
        }

        private void setItemLargeStoreLabel(TextView itemLargeStoreLabel) {
            getThis().itemLargeStoreLabel = itemLargeStoreLabel;
        }

        private long getDoubleClickTimeout() {
            return doubleClickTimeout;
        }

        private long getLastClickTime() {
            return lastClickTime;
        }

        private void setLastClickTime(long lastClickTime) {
            getThis().lastClickTime = lastClickTime;
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
                for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                    category = getCategoryData().getCategoryList().get(i);
                    int numItemsInCategory;
                    if (getItemData().getCategoryMap().get(category) == null) {
                        numItemsInCategory = 0;
                    } else {
                        numItemsInCategory = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                    }
                    index += numItemsInCategory;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = getItemData().getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

                if (thisItem.getStatus().isSelectedInInventory() || thisItem == getShopping().getSelectedItemInInventory()) {
                    // selected item is this item
                    thisItem.getStatus().setAsUnselectedInInventory();
                    getItemSmall().setBackgroundResource(R.drawable.list_outline_unselected);
                    getItemLarge().setBackgroundResource(R.drawable.list_outline_unselected);

                    getShopping().setItemIsSelectedInInventory(false);
                    getShopping().setSelectedItemInInventory(null);
                } else {
                    if (getShopping().itemIsSelectedInInventory()) {
                        // selected item is another item
                        int currentlySelected = getShopping().getSelectedItemPositionInInventory();
                        thisItem.getStatus().setAsSelectedInInventory();
                        getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                        getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

                        getShopping().setSelectedItemPositionInInventory(position);
                        getShopping().setItemIsSelectedInInventory(true);
                        getShopping().setSelectedItemInInventory(thisItem);

                        Item lastItem = getItemWithCategories(currentlySelected);
                        lastItem.getStatus().setAsUnselectedInInventory();
                        adapter.notifyItemChanged(currentlySelected);

                    } else {
                        // nothing is selected
                        thisItem.getStatus().setAsSelectedInInventory();
                        getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                        getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

                        getShopping().setSelectedItemPositionInInventory(position);
                        getShopping().setItemIsSelectedInInventory(true);
                        getShopping().setSelectedItemInInventory(thisItem);
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
            for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                category = getCategoryData().getCategoryList().get(i);
                int numItemsInCategory;
                if (getItemData().getCategoryMap().get(category) == null) {
                    numItemsInCategory = 0;
                } else {
                    numItemsInCategory = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                }
                index += numItemsInCategory;
                adjustedPosition--;
                if (index == adjustedPosition) {
                    break;
                } else if (index >= adjustedPosition) {
                    thisItem = getItemData().getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                    break;
                }
            }
            return thisItem;
        }

        public void onClick(View v) {
            long clickTime = SystemClock.uptimeMillis();
            if (clickTime - getLastClickTime() < getDoubleClickTimeout()) {
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            setLastClickTime(clickTime);
        }

        private void onDoubleClick(View v) {

            int position = getAdapterPosition();
            String category;
            Item thisItem = null;
            int adjustedPosition;

            int index = 0;
            adjustedPosition = position;
            for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                category = getCategoryData().getCategoryList().get(i);
                int numItemsInCategory;
                if (getItemData().getCategoryMap().get(category) == null) {
                    numItemsInCategory = 0;
                } else {
                    numItemsInCategory = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                }
                index += numItemsInCategory;
                adjustedPosition--;
                if (index == adjustedPosition) {
                    break;
                } else if (index >= adjustedPosition) {
                    thisItem = getItemData().getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                    break;
                }
            }
            getShopping().setPictureDialogInInventory(true);
            getShopping().setPictureDialogInSearchResults(false);
            getShopping().setPictureDialogInShoppingList(false);
            getShopping().showPictureDialog(thisItem);
        }

        private void onSingleClick(View v) {

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
                for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                    category = getCategoryData().getCategoryList().get(i);
                    int numItemsInCategory;
                    if (getItemData().getCategoryMap().get(category) == null) {
                        numItemsInCategory = 0;
                    } else {
                        numItemsInCategory = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                    }
                    index += numItemsInCategory;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = getItemData().getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

                if (id == getItemSmallName().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getItemLargeName().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getItemLargeBrandLabel().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getItemLargeBrand().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getItemLargeStoreLabel().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getItemLargeStore().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getTriangleRight().getId()) {
                    if (getTriangleRight().getVisibility() == View.VISIBLE && getTriangleDown().getVisibility() == View.GONE) {
                        getTriangleRight().setVisibility(View.GONE);
                        getTriangleDown().setVisibility(View.VISIBLE);
                        getItemSmall().setVisibility(View.GONE);
                        getItemLarge().setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsExpandedInInventory();
                    }
                } else if (id == getTriangleDown().getId()) {
                    if (getTriangleDown().getVisibility() == View.VISIBLE && getTriangleRight().getVisibility() == View.GONE) {
                        getTriangleDown().setVisibility(View.GONE);
                        getTriangleRight().setVisibility(View.VISIBLE);
                        getItemLarge().setVisibility(View.GONE);
                        getItemSmall().setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsContractedInInventory();
                    }
                } else if (id == getItemSmallInStock().getId()) {
                    if (getItemSmallInStock().getVisibility() == View.VISIBLE) {
                        getItemSmallInStock().setVisibility(View.GONE);
                        getItemLargeInStock().setVisibility(View.GONE);
                        getItemSmallPaused().setVisibility(View.GONE);
                        getItemLargePaused().setVisibility(View.GONE);
                        getItemSmallNeeded().setVisibility(View.VISIBLE);
                        getItemLargeNeeded().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsNeeded();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.needed), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = getShopping().getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = getShopping().getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = getShopping().getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = getShopping().getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        getDbCategoryHelper().setCategoryViews(thisCategory, numItemsViewAll, (numItemsInStock - 1), (numItemsNeeded + 1), numItemsPaused);
                        getShopping().updateCategoryData();
                    }
                } else if (id == getItemSmallNeeded().getId()) {
                    if (getItemSmallNeeded().getVisibility() == View.VISIBLE) {
                        getItemSmallNeeded().setVisibility(View.GONE);
                        getItemLargeNeeded().setVisibility(View.GONE);
                        getItemSmallInStock().setVisibility(View.GONE);
                        getItemLargeInStock().setVisibility(View.GONE);
                        getItemSmallPaused().setVisibility(View.VISIBLE);
                        getItemLargePaused().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsPaused();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.paused), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = getShopping().getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = getShopping().getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = getShopping().getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = getShopping().getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        getDbCategoryHelper().setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock, (numItemsNeeded - 1), (numItemsPaused + 1));
                        getShopping().updateCategoryData();
                    }
                } else if (id == getItemSmallPaused().getId()) {
                    if (getItemSmallPaused().getVisibility() == View.VISIBLE) {
                        getItemSmallPaused().setVisibility(View.GONE);
                        getItemLargePaused().setVisibility(View.GONE);
                        getItemSmallNeeded().setVisibility(View.GONE);
                        getItemLargeNeeded().setVisibility(View.GONE);
                        getItemSmallInStock().setVisibility(View.VISIBLE);
                        getItemLargeInStock().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsInStock();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.instock), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = getShopping().getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = getShopping().getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = getShopping().getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = getShopping().getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        getDbCategoryHelper().setCategoryViews(thisCategory, numItemsViewAll, (numItemsInStock + 1), numItemsNeeded, (numItemsPaused - 1));
                        getShopping().updateCategoryData();
                    }
                } else if (id == getItemLargeInStock().getId()) {
                    if (getItemLargeInStock().getVisibility() == View.VISIBLE) {
                        getItemLargeInStock().setVisibility(View.GONE);
                        getItemSmallInStock().setVisibility(View.GONE);
                        getItemLargePaused().setVisibility(View.GONE);
                        getItemSmallPaused().setVisibility(View.GONE);
                        getItemLargeNeeded().setVisibility(View.VISIBLE);
                        getItemSmallNeeded().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsNeeded();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.needed), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = getShopping().getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = getShopping().getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = getShopping().getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = getShopping().getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        getDbCategoryHelper().setCategoryViews(thisCategory, numItemsViewAll, (numItemsInStock - 1), (numItemsNeeded + 1), numItemsPaused);
                        getShopping().updateCategoryData();
                    }
                } else if (id == getItemLargeNeeded().getId()) {
                    if (getItemLargeNeeded().getVisibility() == View.VISIBLE) {
                        getItemLargeNeeded().setVisibility(View.GONE);
                        getItemSmallNeeded().setVisibility(View.GONE);
                        getItemLargeInStock().setVisibility(View.GONE);
                        getItemSmallInStock().setVisibility(View.GONE);
                        getItemLargePaused().setVisibility(View.VISIBLE);
                        getItemSmallPaused().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsPaused();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.paused), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = getShopping().getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = getShopping().getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = getShopping().getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = getShopping().getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        getDbCategoryHelper().setCategoryViews(thisCategory, numItemsViewAll, numItemsInStock, (numItemsNeeded - 1), (numItemsPaused + 1));
                        getShopping().updateCategoryData();
                    }
                } else if (id == getItemLargePaused().getId()) {
                    if (getItemLargePaused().getVisibility() == View.VISIBLE) {
                        getItemLargePaused().setVisibility(View.GONE);
                        getItemSmallPaused().setVisibility(View.GONE);
                        getItemLargeNeeded().setVisibility(View.GONE);
                        getItemSmallNeeded().setVisibility(View.GONE);
                        getItemLargeInStock().setVisibility(View.VISIBLE);
                        getItemSmallInStock().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsInStock();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.instock), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisCategory = thisItem.getCategory().toString();
                        int numItemsInStock = getShopping().getCategoryData().getCategoryViewInStockMap().get(thisCategory);
                        int numItemsNeeded = getShopping().getCategoryData().getCategoryViewNeededMap().get(thisCategory);
                        int numItemsPaused = getShopping().getCategoryData().getCategoryViewPausedMap().get(thisCategory);
                        int numItemsViewAll = getShopping().getCategoryData().getCategoryViewAllMap().get(thisCategory);
                        getDbCategoryHelper().setCategoryViews(thisCategory, numItemsViewAll, (numItemsInStock + 1), numItemsNeeded, (numItemsPaused - 1));
                        getShopping().updateCategoryData();
                    }
                }
            }
        }
    }

//------------------------------------------Store Titles--------------------------------------------

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
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(itemData);
            setStoreData(storeData);
            setDbStatusHelper(dbStatus);
            setDbStoreHelper(dbStore);

            if (shopping.getStoreTitles().equals(Shopping.STORE_TITLES_EXPANDED)) {
                getThis().setAsExpanded();
            } else if (shopping.getStoreTitles().equals(Shopping.STORE_TITLES_CONTRACTED)) {
                getThis().setAsContracted();
            }

            setSortByStoreRvTitle((LinearLayout) itemView.findViewById(R.id.sortByStoreRvTitle));
            setStoreTitleText((TextView) itemView.findViewById(R.id.storeTitleText));
            setTriangleButtonDown1((ImageView) itemView.findViewById(R.id.triangleButtonDown1));
            setTriangleButtonDown2((ImageView) itemView.findViewById(R.id.triangleButtonDown2));
            setTriangleButtonRight((ImageView) itemView.findViewById(R.id.triangleButtonRight));
            setTriangleButtonLeft((ImageView) itemView.findViewById(R.id.triangleButtonLeft));

            getStoreTitleText().setOnClickListener(this);
            getTriangleButtonDown1().setOnClickListener(this);
            getTriangleButtonDown2().setOnClickListener(this);
            getTriangleButtonRight().setOnClickListener(this);
            getTriangleButtonLeft().setOnClickListener(this);

        }

        private SortByStoreTitleRVH getThis() {
            return this;
        }

        private Shopping getShopping() {
            return shopping;
        }

        private void setShopping(Shopping shopping) {
            getThis().shopping = shopping;
        }

        private FullInventoryRVA getAdapter() {
            return adapter;
        }

        private void setAdapter(FullInventoryRVA adapter) {
            getThis().adapter = adapter;
        }

        private ItemData getItemData() {
            return itemData;
        }

        private void setItemData(ItemData itemData) {
            getThis().itemData = itemData;
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

        private DBStoreHelper getDbStoreHelper() {
            return dbStoreHelper;
        }

        private void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
            getThis().dbStoreHelper = dbStoreHelper;
        }

        private LinearLayout getSortByStoreRvTitle() {
            return sortByStoreRvTitle;
        }

        private void setSortByStoreRvTitle(LinearLayout sortByStoreRvTitle) {
            getThis().sortByStoreRvTitle = sortByStoreRvTitle;
        }

        private TextView getStoreTitleText() {
            return storeTitleText;
        }

        private void setStoreTitleText(TextView storeTitleText) {
            getThis().storeTitleText = storeTitleText;
        }

        private ImageView getTriangleButtonDown1() {
            return triangleButtonDown1;
        }

        private void setTriangleButtonDown1(ImageView triangleButtonDown1) {
            getThis().triangleButtonDown1 = triangleButtonDown1;
        }

        private ImageView getTriangleButtonDown2() {
            return triangleButtonDown2;
        }

        private void setTriangleButtonDown2(ImageView triangleButtonDown2) {
            getThis().triangleButtonDown2 = triangleButtonDown2;
        }

        private ImageView getTriangleButtonRight() {
            return triangleButtonRight;
        }

        private void setTriangleButtonRight(ImageView triangleButtonRight) {
            getThis().triangleButtonRight = triangleButtonRight;
        }

        private ImageView getTriangleButtonLeft() {
            return triangleButtonLeft;
        }

        private void setTriangleButtonLeft(ImageView triangleButtonLeft) {
            getThis().triangleButtonLeft = triangleButtonLeft;
        }

        private boolean isExpanded() {
            return isExpanded;
        }

        private void setAsExpanded() {
            isExpanded = true;
            isContracted = false;
        }

        private boolean isContracted() {
            return isContracted;
        }

        private void setAsContracted() {
            isExpanded = false;
            isContracted = true;
        }

        public void onClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            String store;
            Item thisItem = null;
            int adjustedPosition;

            int index = 0;
            adjustedPosition = position;
            for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                store = getStoreData().getStoreList().get(i);
                int numItemsInStore;
                if (getItemData().getStoreMap().get(store) == null) {
                    numItemsInStore = 0;
                } else {
                    numItemsInStore = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                }
                index += numItemsInStore;
                adjustedPosition--;
                if (index >= adjustedPosition) {
                    thisItem = getItemData().getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                    break;
                }
            }

            if (id == getTriangleButtonDown1().getId() || id == getTriangleButtonDown2().getId()) {
                contractTitle();
                thisItem.getStore().setStoreAsContracted();
            } else if (id == getTriangleButtonRight().getId() || id == getTriangleButtonLeft().getId()) {
                expandTitle();
                thisItem.getStore().setStoreAsExpanded();
            } else if (id == getStoreTitleText().getId() && isExpanded()) {
                contractTitle();
                thisItem.getStore().setStoreAsContracted();
            } else if (id == getStoreTitleText().getId() && isContracted()) {
                expandTitle();
                thisItem.getStore().setStoreAsExpanded();
            }
        }

        private void expandTitle() {
            getThis().setAsExpanded();
            getTriangleButtonDown1().setVisibility(View.VISIBLE);
            getTriangleButtonDown2().setVisibility(View.VISIBLE);
            getTriangleButtonRight().setVisibility(View.GONE);
            getTriangleButtonLeft().setVisibility(View.GONE);
        }

        private void contractTitle() {
            getThis().setAsContracted();
            getTriangleButtonDown1().setVisibility(View.GONE);
            getTriangleButtonDown2().setVisibility(View.GONE);
            getTriangleButtonRight().setVisibility(View.VISIBLE);
            getTriangleButtonLeft().setVisibility(View.VISIBLE);
        }


    }

//-------------------------------------------Store Items--------------------------------------------

    private class SortByStoreItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private Context context;
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

        private final long doubleClickTimeout = 400;
        private long lastClickTime = 0;

        private SortByStoreItemRVH(View itemView, Context context, Shopping shopping, FullInventoryRVA adapter,
                                  ItemData itemData, StoreData storeData, DBStatusHelper dbStatus, DBStoreHelper dbStore) {

            super(itemView);
            setContext(context);
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(itemData);
            setStoreData(storeData);
            setDbStatusHelper(dbStatus);
            setDbStoreHelper(dbStore);

            setTriangleRight((Button) itemView.findViewById(R.id.triangleButtonRight));
            setTriangleDown((Button) itemView.findViewById(R.id.triangleButtonDown));
            setItemSmall((LinearLayout) itemView.findViewById(R.id.itemSmall));
            setItemLarge((LinearLayout) itemView.findViewById(R.id.itemLarge));
            setItemSmallName((TextView) itemView.findViewById(R.id.itemSmallName));
            setItemLargeName((TextView) itemView.findViewById(R.id.itemLargeName));
            setItemSmallInStock((TextView) itemView.findViewById(R.id.itemSmallInStock));
            setItemSmallNeeded((TextView) itemView.findViewById(R.id.itemSmallNeeded));
            setItemSmallPaused((TextView) itemView.findViewById(R.id.itemSmallPaused));
            setItemLargeInStock((TextView) itemView.findViewById(R.id.itemLargeInStock));
            setItemLargeNeeded((TextView) itemView.findViewById(R.id.itemLargeNeeded));
            setItemLargePaused((TextView) itemView.findViewById(R.id.itemLargePaused));
            setItemLargeBrand((TextView) itemView.findViewById(R.id.itemLargeBrand));
            setItemLargeBrandLabel((TextView) itemView.findViewById(R.id.itemLargeBrandLabel));
            setItemLargeCategory((TextView) itemView.findViewById(R.id.itemLargeCategory));
            setItemLargeCategoryLabel((TextView) itemView.findViewById(R.id.itemLargeCategoryLabel));
            setItemLargeStore((TextView) itemView.findViewById(R.id.itemLargeStore));
            setItemLargeStoreLabel((TextView) itemView.findViewById(R.id.itemLargeStoreLabel));

            getTriangleRight().setOnClickListener(this);
            getTriangleDown().setOnClickListener(this);
            getItemSmall().setOnClickListener(this);
            getItemLarge().setOnClickListener(this);
            getItemSmallName().setOnClickListener(this);
            getItemLargeName().setOnClickListener(this);
            getItemSmallInStock().setOnClickListener(this);
            getItemSmallNeeded().setOnClickListener(this);
            getItemSmallPaused().setOnClickListener(this);
            getItemLargeInStock().setOnClickListener(this);
            getItemLargeNeeded().setOnClickListener(this);
            getItemLargePaused().setOnClickListener(this);
            getItemLargeBrand().setOnClickListener(this);
            getItemLargeBrandLabel().setOnClickListener(this);
            getItemLargeCategory().setOnClickListener(this);
            getItemLargeCategoryLabel().setOnClickListener(this);
            getItemLargeStore().setOnClickListener(this);
            getItemLargeStoreLabel().setOnClickListener(this);
        }

        private SortByStoreItemRVH getThis() {
            return this;
        }

        private Context getContext() {
            return context;
        }

        private void setContext(Context context) {
            getThis().context = context;
        }

        private Shopping getShopping() {
            return shopping;
        }

        private void setShopping(Shopping shopping) {
            getThis().shopping = shopping;
        }

        private FullInventoryRVA getAdapter() {
            return adapter;
        }

        private void setAdapter(FullInventoryRVA adapter) {
            getThis().adapter = adapter;
        }

        private ItemData getItemData() {
            return itemData;
        }

        private void setItemData(ItemData itemData) {
            getThis().itemData = itemData;
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

        private DBStoreHelper getDbStoreHelper() {
            return dbStoreHelper;
        }

        private void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
            getThis().dbStoreHelper = dbStoreHelper;
        }

        private Button getTriangleRight() {
            return triangleRight;
        }

        private void setTriangleRight(Button triangleRight) {
            getThis().triangleRight = triangleRight;
        }

        private Button getTriangleDown() {
            return triangleDown;
        }

        private void setTriangleDown(Button triangleDown) {
            getThis().triangleDown = triangleDown;
        }

        private LinearLayout getItemSmall() {
            return itemSmall;
        }

        private void setItemSmall(LinearLayout itemSmall) {
            getThis().itemSmall = itemSmall;
        }

        private LinearLayout getItemLarge() {
            return itemLarge;
        }

        private void setItemLarge(LinearLayout itemLarge) {
            getThis().itemLarge = itemLarge;
        }

        private TextView getItemSmallName() {
            return itemSmallName;
        }

        private void setItemSmallName(TextView itemSmallName) {
            getThis().itemSmallName = itemSmallName;
        }

        private TextView getItemLargeName() {
            return itemLargeName;
        }

        private void setItemLargeName(TextView itemLargeName) {
            getThis().itemLargeName = itemLargeName;
        }

        private TextView getItemSmallInStock() {
            return itemSmallInStock;
        }

        private void setItemSmallInStock(TextView itemSmallInStock) {
            getThis().itemSmallInStock = itemSmallInStock;
        }

        private TextView getItemSmallNeeded() {
            return itemSmallNeeded;
        }

        private void setItemSmallNeeded(TextView itemSmallNeeded) {
            getThis().itemSmallNeeded = itemSmallNeeded;
        }

        private TextView getItemSmallPaused() {
            return itemSmallPaused;
        }

        private void setItemSmallPaused(TextView itemSmallPaused) {
            getThis().itemSmallPaused = itemSmallPaused;
        }

        private TextView getItemLargeInStock() {
            return itemLargeInStock;
        }

        private void setItemLargeInStock(TextView itemLargeInStock) {
            getThis().itemLargeInStock = itemLargeInStock;
        }

        private TextView getItemLargeNeeded() {
            return itemLargeNeeded;
        }

        private void setItemLargeNeeded(TextView itemLargeNeeded) {
            getThis().itemLargeNeeded = itemLargeNeeded;
        }

        private TextView getItemLargePaused() {
            return itemLargePaused;
        }

        private void setItemLargePaused(TextView itemLargePaused) {
            getThis().itemLargePaused = itemLargePaused;
        }

        private TextView getItemLargeBrand() {
            return itemLargeBrand;
        }

        private void setItemLargeBrand(TextView itemLargeBrand) {
            getThis().itemLargeBrand = itemLargeBrand;
        }

        private TextView getItemLargeBrandLabel() {
            return itemLargeBrandLabel;
        }

        private void setItemLargeBrandLabel(TextView itemLargeBrandLabel) {
            getThis().itemLargeBrandLabel = itemLargeBrandLabel;
        }

        private TextView getItemLargeCategory() {
            return itemLargeCategory;
        }

        private void setItemLargeCategory(TextView itemLargeCategory) {
            getThis().itemLargeCategory = itemLargeCategory;
        }

        private TextView getItemLargeCategoryLabel() {
            return itemLargeCategoryLabel;
        }

        private void setItemLargeCategoryLabel(TextView itemLargeCategoryLabel) {
            getThis().itemLargeCategoryLabel = itemLargeCategoryLabel;
        }

        private TextView getItemLargeStore() {
            return itemLargeStore;
        }

        private void setItemLargeStore(TextView itemLargeStore) {
            getThis().itemLargeStore = itemLargeStore;
        }

        private TextView getItemLargeStoreLabel() {
            return itemLargeStoreLabel;
        }

        private void setItemLargeStoreLabel(TextView itemLargeStoreLabel) {
            getThis().itemLargeStoreLabel = itemLargeStoreLabel;
        }

        private long getDoubleClickTimeout() {
            return doubleClickTimeout;
        }

        private long getLastClickTime() {
            return lastClickTime;
        }

        private void setLastClickTime(long lastClickTime) {
            getThis().lastClickTime = lastClickTime;
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
                for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                    store = getStoreData().getStoreList().get(i);
                    int numItemsInStore;
                    if (getItemData().getStoreMap().get(store) == null) {
                        numItemsInStore = 0;
                    } else {
                        numItemsInStore = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                    }
                    index += numItemsInStore;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = getItemData().getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

                if (thisItem.getStatus().isSelectedInInventory() || thisItem == getShopping().getSelectedItemInInventory()) {
                    // selected item is this item
                    thisItem.getStatus().setAsUnselectedInInventory();
                    getItemSmall().setBackgroundResource(R.drawable.list_outline_unselected);
                    getItemLarge().setBackgroundResource(R.drawable.list_outline_unselected);

                    getShopping().setItemIsSelectedInInventory(false);
                    getShopping().setSelectedItemInInventory(null);
                } else {
                    if (getShopping().itemIsSelectedInInventory()) {
                        // selected item is another item
                        int currentlySelected = getShopping().getSelectedItemPositionInInventory();
                        thisItem.getStatus().setAsSelectedInInventory();
                        getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                        getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

                        getShopping().setSelectedItemPositionInInventory(position);
                        getShopping().setItemIsSelectedInInventory(true);
                        getShopping().setSelectedItemInInventory(thisItem);

                        Item lastItem = getItemWithStores(currentlySelected);
                        lastItem.getStatus().setAsUnselectedInInventory();
                        adapter.notifyItemChanged(currentlySelected);

                    } else {
                        // nothing is selected
                        thisItem.getStatus().setAsSelectedInInventory();
                        getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                        getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

                        getShopping().setSelectedItemPositionInInventory(position);
                        getShopping().setItemIsSelectedInInventory(true);
                        getShopping().setSelectedItemInInventory(thisItem);
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
            for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                store = getStoreData().getStoreList().get(i);
                int numItemsInStore;
                if (getItemData().getStoreMap().get(store) == null) {
                    numItemsInStore = 0;
                } else {
                    numItemsInStore = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                }
                index += numItemsInStore;
                adjustedPosition--;
                if (index == adjustedPosition) {
                    break;
                } else if (index >= adjustedPosition) {
                    thisItem = getItemData().getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                    break;
                }
            }
            return thisItem;
        }

        public void onClick(View v) {
            long clickTime = SystemClock.uptimeMillis();
            if (clickTime - getLastClickTime() < getDoubleClickTimeout()) {
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            setLastClickTime(clickTime);
        }

        private void onDoubleClick(View v) {

            int position = getAdapterPosition();
            String store;
            Item thisItem = null;
            int adjustedPosition;

            int index = 0;
            adjustedPosition = position;
            for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                store = getStoreData().getStoreList().get(i);
                int numItemsInStore;
                if (getItemData().getStoreMap().get(store) == null) {
                    numItemsInStore = 0;
                } else {
                    numItemsInStore = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                }
                index += numItemsInStore;
                adjustedPosition--;
                if (index == adjustedPosition) {
                    break;
                } else if (index >= adjustedPosition) {
                    thisItem = getItemData().getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                    break;
                }
            }
            getShopping().setPictureDialogInInventory(true);
            getShopping().setPictureDialogInSearchResults(false);
            getShopping().setPictureDialogInShoppingList(false);
            getShopping().showPictureDialog(thisItem);
        }

        private void onSingleClick(View v) {

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
                for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                    store = getStoreData().getStoreList().get(i);
                    int numItemsInStore;
                    if (getItemData().getStoreMap().get(store) == null) {
                        numItemsInStore = 0;
                    } else {
                        numItemsInStore = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                    }
                    index += numItemsInStore;
                    adjustedPosition--;
                    if (index == adjustedPosition) {
                        isTitle = true;
                        break;
                    } else if (index >= adjustedPosition) {
                        isTitle = false;
                        thisItem = getItemData().getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle) {

                if (id == getItemSmallName().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getItemLargeName().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getItemLargeBrandLabel().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getItemLargeBrand().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getItemLargeCategoryLabel().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getItemLargeCategory().getId()) {
                    selectOrUnselectItem(position);
                } else if (id == getTriangleRight().getId()) {
                    if (getTriangleRight().getVisibility() == View.VISIBLE && getTriangleDown().getVisibility() == View.GONE) {
                        getTriangleRight().setVisibility(View.GONE);
                        getTriangleDown().setVisibility(View.VISIBLE);
                        getItemSmall().setVisibility(View.GONE);
                        getItemLarge().setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsExpandedInShoppingList();
                    }
                } else if (id == getTriangleDown().getId()) {
                    if (getTriangleDown().getVisibility() == View.VISIBLE && getTriangleRight().getVisibility() == View.GONE) {
                        getTriangleDown().setVisibility(View.GONE);
                        getTriangleRight().setVisibility(View.VISIBLE);
                        getItemLarge().setVisibility(View.GONE);
                        getItemSmall().setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsContractedInShoppingList();
                    }

                } else if (id == getItemSmallInStock().getId()) {
                    if (getItemSmallInStock().getVisibility() == View.VISIBLE) {
                        getItemSmallInStock().setVisibility(View.GONE);
                        getItemLargeInStock().setVisibility(View.GONE);
                        getItemSmallPaused().setVisibility(View.GONE);
                        getItemLargePaused().setVisibility(View.GONE);
                        getItemSmallNeeded().setVisibility(View.VISIBLE);
                        getItemLargeNeeded().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsNeeded();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.needed), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = getShopping().getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = getShopping().getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = getShopping().getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = getShopping().getStoreData().getStoreViewAllMap().get(thisStore);
                        getDbStoreHelper().setStoreViews(thisStore, numItemsViewAll, (numItemsInStock - 1), (numItemsNeeded + 1), numItemsPaused);
                        getShopping().updateStoreData();
                    }
                } else if (id == getItemSmallNeeded().getId()) {
                    if (getItemSmallNeeded().getVisibility() == View.VISIBLE) {
                        getItemSmallNeeded().setVisibility(View.GONE);
                        getItemLargeNeeded().setVisibility(View.GONE);
                        getItemSmallInStock().setVisibility(View.GONE);
                        getItemLargeInStock().setVisibility(View.GONE);
                        getItemSmallPaused().setVisibility(View.VISIBLE);
                        getItemLargePaused().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsPaused();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.paused), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = getShopping().getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = getShopping().getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = getShopping().getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = getShopping().getStoreData().getStoreViewAllMap().get(thisStore);
                        getDbStoreHelper().setStoreViews(thisStore, numItemsViewAll, numItemsInStock, (numItemsNeeded - 1), (numItemsPaused + 1));
                        getShopping().updateStoreData();
                    }
                } else if (id == getItemSmallPaused().getId()) {
                    if (getItemSmallPaused().getVisibility() == View.VISIBLE) {
                        getItemSmallPaused().setVisibility(View.GONE);
                        getItemLargePaused().setVisibility(View.GONE);
                        getItemSmallNeeded().setVisibility(View.GONE);
                        getItemLargeNeeded().setVisibility(View.GONE);
                        getItemSmallInStock().setVisibility(View.VISIBLE);
                        getItemLargeInStock().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsInStock();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.instock), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = getShopping().getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = getShopping().getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = getShopping().getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = getShopping().getStoreData().getStoreViewAllMap().get(thisStore);
                        getDbStoreHelper().setStoreViews(thisStore, numItemsViewAll, (numItemsInStock + 1), numItemsNeeded, (numItemsPaused - 1));
                        getShopping().updateStoreData();
                    }
                } else if (id == getItemLargeInStock().getId()) {
                    if (getItemLargeInStock().getVisibility() == View.VISIBLE) {
                        getItemLargeInStock().setVisibility(View.GONE);
                        getItemSmallInStock().setVisibility(View.GONE);
                        getItemLargePaused().setVisibility(View.GONE);
                        getItemSmallPaused().setVisibility(View.GONE);
                        getItemLargeNeeded().setVisibility(View.VISIBLE);
                        getItemSmallNeeded().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsNeeded();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.needed), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = getShopping().getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = getShopping().getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = getShopping().getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = getShopping().getStoreData().getStoreViewAllMap().get(thisStore);
                        getDbStoreHelper().setStoreViews(thisStore, numItemsViewAll, (numItemsInStock - 1), (numItemsNeeded + 1), numItemsPaused);
                        getShopping().updateStoreData();
                    }
                } else if (id == getItemLargeNeeded().getId()) {
                    if (getItemLargeNeeded().getVisibility() == View.VISIBLE) {
                        getItemLargeNeeded().setVisibility(View.GONE);
                        getItemSmallNeeded().setVisibility(View.GONE);
                        getItemLargeInStock().setVisibility(View.GONE);
                        getItemSmallInStock().setVisibility(View.GONE);
                        getItemLargePaused().setVisibility(View.VISIBLE);
                        getItemSmallPaused().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsPaused();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.paused), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = getShopping().getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = getShopping().getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = getShopping().getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = getShopping().getStoreData().getStoreViewAllMap().get(thisStore);
                        getDbStoreHelper().setStoreViews(thisStore, numItemsViewAll, numItemsInStock, (numItemsNeeded - 1), (numItemsPaused + 1));
                        getShopping().updateStoreData();
                    }
                } else if (id == getItemLargePaused().getId()) {
                    if (getItemLargePaused().getVisibility() == View.VISIBLE) {
                        getItemLargePaused().setVisibility(View.GONE);
                        getItemSmallPaused().setVisibility(View.GONE);
                        getItemLargeNeeded().setVisibility(View.GONE);
                        getItemSmallNeeded().setVisibility(View.GONE);
                        getItemLargeInStock().setVisibility(View.VISIBLE);
                        getItemSmallInStock().setVisibility(View.VISIBLE);

                        thisItem.getStatus().setAsInStock();
                        getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.instock), getContext().getString(R.string.unchecked));
                        getShopping().updateStatusData();

                        String thisStore = thisItem.getStore().toString();
                        int numItemsInStock = getShopping().getStoreData().getStoreViewInStockMap().get(thisStore);
                        int numItemsNeeded = getShopping().getStoreData().getStoreViewNeededMap().get(thisStore);
                        int numItemsPaused = getShopping().getStoreData().getStoreViewPausedMap().get(thisStore);
                        int numItemsViewAll = getShopping().getStoreData().getStoreViewAllMap().get(thisStore);
                        getDbStoreHelper().setStoreViews(thisStore, numItemsViewAll, numItemsInStock + 1, numItemsNeeded, numItemsPaused - 1);
                        getShopping().updateStoreData();
                    }
                }
            }
        }
    }

//---------------------------------------Alphabetical Items-----------------------------------------

    private class SortAlphabeticalItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private Context context;
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

        private final long doubleClickTimeout = 400;
        private long lastClickTime = 0;

        private SortAlphabeticalItemRVH(View itemView, Context context, Shopping shopping, FullInventoryRVA adapter, ItemData itemData, DBStatusHelper dbStatus) {

            super(itemView);
            setShopping(shopping);
            setContext(context);
            setAdapter(adapter);
            setItemData(itemData);
            setDbStatusHelper(dbStatus);

            setTriangleRight((Button) itemView.findViewById(R.id.triangleButtonRight));
            setTriangleDown((Button) itemView.findViewById(R.id.triangleButtonDown));
            setItemSmall((LinearLayout) itemView.findViewById(R.id.itemSmall));
            setItemLarge((LinearLayout) itemView.findViewById(R.id.itemLarge));
            setItemSmallName((TextView) itemView.findViewById(R.id.itemSmallName));
            setItemLargeName((TextView) itemView.findViewById(R.id.itemLargeName));
            setItemSmallInStock((TextView) itemView.findViewById(R.id.itemSmallInStock));
            setItemSmallNeeded((TextView) itemView.findViewById(R.id.itemSmallNeeded));
            setItemSmallPaused((TextView) itemView.findViewById(R.id.itemSmallPaused));
            setItemLargeInStock((TextView) itemView.findViewById(R.id.itemLargeInStock));
            setItemLargeNeeded((TextView) itemView.findViewById(R.id.itemLargeNeeded));
            setItemLargePaused((TextView) itemView.findViewById(R.id.itemLargePaused));
            setItemLargeBrand((TextView) itemView.findViewById(R.id.itemLargeBrand));
            setItemLargeBrandLabel((TextView) itemView.findViewById(R.id.itemLargeBrandLabel));
            setItemLargeCategory((TextView) itemView.findViewById(R.id.itemLargeCategory));
            setItemLargeCategoryLabel((TextView) itemView.findViewById(R.id.itemLargeCategoryLabel));
            setItemLargeStore((TextView) itemView.findViewById(R.id.itemLargeStore));
            setItemLargeStoreLabel((TextView) itemView.findViewById(R.id.itemLargeStoreLabel));

            getTriangleRight().setOnClickListener(this);
            getTriangleDown().setOnClickListener(this);
            getItemSmall().setOnClickListener(this);
            getItemLarge().setOnClickListener(this);
            getItemSmallName().setOnClickListener(this);
            getItemLargeName().setOnClickListener(this);
            getItemSmallInStock().setOnClickListener(this);
            getItemSmallNeeded().setOnClickListener(this);
            getItemSmallPaused().setOnClickListener(this);
            getItemLargeInStock().setOnClickListener(this);
            getItemLargeNeeded().setOnClickListener(this);
            getItemLargePaused().setOnClickListener(this);
            getItemLargeBrand().setOnClickListener(this);
            getItemLargeBrandLabel().setOnClickListener(this);
            getItemLargeCategory().setOnClickListener(this);
            getItemLargeCategoryLabel().setOnClickListener(this);
            getItemLargeStore().setOnClickListener(this);
            getItemLargeStoreLabel().setOnClickListener(this);
        }

        private SortAlphabeticalItemRVH getThis() {
            return this;
        }

        private Context getContext() {
            return context;
        }

        private void setContext(Context context) {
            getThis().context = context;
        }

        private Shopping getShopping() {
            return shopping;
        }

        private void setShopping(Shopping shopping) {
            getThis().shopping = shopping;
        }

        private FullInventoryRVA getAdapter() {
            return adapter;
        }

        private void setAdapter(FullInventoryRVA adapter) {
            getThis().adapter = adapter;
        }

        private ItemData getItemData() {
            return itemData;
        }

        private void setItemData(ItemData itemData) {
            getThis().itemData = itemData;
        }

        private DBStatusHelper getDbStatusHelper() {
            return dbStatusHelper;
        }

        private void setDbStatusHelper(DBStatusHelper dbStatusHelper) {
            getThis().dbStatusHelper = dbStatusHelper;
        }

        private Button getTriangleRight() {
            return triangleRight;
        }

        private void setTriangleRight(Button triangleRight) {
            getThis().triangleRight = triangleRight;
        }

        private Button getTriangleDown() {
            return triangleDown;
        }

        private void setTriangleDown(Button triangleDown) {
            getThis().triangleDown = triangleDown;
        }

        private LinearLayout getItemSmall() {
            return itemSmall;
        }

        private void setItemSmall(LinearLayout itemSmall) {
            getThis().itemSmall = itemSmall;
        }

        private LinearLayout getItemLarge() {
            return itemLarge;
        }

        private void setItemLarge(LinearLayout itemLarge) {
            getThis().itemLarge = itemLarge;
        }

        private TextView getItemSmallName() {
            return itemSmallName;
        }

        private void setItemSmallName(TextView itemSmallName) {
            getThis().itemSmallName = itemSmallName;
        }

        private TextView getItemLargeName() {
            return itemLargeName;
        }

        private void setItemLargeName(TextView itemLargeName) {
            getThis().itemLargeName = itemLargeName;
        }

        private TextView getItemSmallInStock() {
            return itemSmallInStock;
        }

        private void setItemSmallInStock(TextView itemSmallInStock) {
            getThis().itemSmallInStock = itemSmallInStock;
        }

        private TextView getItemSmallNeeded() {
            return itemSmallNeeded;
        }

        private void setItemSmallNeeded(TextView itemSmallNeeded) {
            getThis().itemSmallNeeded = itemSmallNeeded;
        }

        private TextView getItemSmallPaused() {
            return itemSmallPaused;
        }

        private void setItemSmallPaused(TextView itemSmallPaused) {
            getThis().itemSmallPaused = itemSmallPaused;
        }

        private TextView getItemLargeInStock() {
            return itemLargeInStock;
        }

        private void setItemLargeInStock(TextView itemLargeInStock) {
            getThis().itemLargeInStock = itemLargeInStock;
        }

        private TextView getItemLargeNeeded() {
            return itemLargeNeeded;
        }

        private void setItemLargeNeeded(TextView itemLargeNeeded) {
            getThis().itemLargeNeeded = itemLargeNeeded;
        }

        private TextView getItemLargePaused() {
            return itemLargePaused;
        }

        private void setItemLargePaused(TextView itemLargePaused) {
            getThis().itemLargePaused = itemLargePaused;
        }

        private TextView getItemLargeBrand() {
            return itemLargeBrand;
        }

        private void setItemLargeBrand(TextView itemLargeBrand) {
            getThis().itemLargeBrand = itemLargeBrand;
        }

        private TextView getItemLargeBrandLabel() {
            return itemLargeBrandLabel;
        }

        private void setItemLargeBrandLabel(TextView itemLargeBrandLabel) {
            getThis().itemLargeBrandLabel = itemLargeBrandLabel;
        }

        private TextView getItemLargeCategory() {
            return itemLargeCategory;
        }

        private void setItemLargeCategory(TextView itemLargeCategory) {
            getThis().itemLargeCategory = itemLargeCategory;
        }

        private TextView getItemLargeCategoryLabel() {
            return itemLargeCategoryLabel;
        }

        private void setItemLargeCategoryLabel(TextView itemLargeCategoryLabel) {
            getThis().itemLargeCategoryLabel = itemLargeCategoryLabel;
        }

        private TextView getItemLargeStore() {
            return itemLargeStore;
        }

        private void setItemLargeStore(TextView itemLargeStore) {
            getThis().itemLargeStore = itemLargeStore;
        }

        private TextView getItemLargeStoreLabel() {
            return itemLargeStoreLabel;
        }

        private void setItemLargeStoreLabel(TextView itemLargeStoreLabel) {
            getThis().itemLargeStoreLabel = itemLargeStoreLabel;
        }

        private long getDoubleClickTimeout() {
            return doubleClickTimeout;
        }

        private long getLastClickTime() {
            return lastClickTime;
        }

        private void setLastClickTime(long lastClickTime) {
            getThis().lastClickTime = lastClickTime;
        }

        private void selectOrUnselectItem(int position) {

            Item thisItem = getItemData().getItemListAZ().get(position);

            if (thisItem.getStatus().isSelectedInInventory() || thisItem == getShopping().getSelectedItemInInventory()) {
                // selected item is this item
                thisItem.getStatus().setAsUnselectedInInventory();
                getItemSmall().setBackgroundResource(R.drawable.list_outline_unselected);
                getItemLarge().setBackgroundResource(R.drawable.list_outline_unselected);

                getShopping().setItemIsSelectedInInventory(false);
                getShopping().setSelectedItemInInventory(null);
            } else {
                if (getShopping().itemIsSelectedInInventory()) {
                    // selected item is another item
                    int currentlySelected = getShopping().getSelectedItemPositionInInventory();
                    thisItem.getStatus().setAsSelectedInInventory();
                    getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                    getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

                    getShopping().setSelectedItemPositionInInventory(position);
                    getShopping().setItemIsSelectedInInventory(true);
                    getShopping().setSelectedItemInInventory(thisItem);

                    Item lastItem = getItemData().getItemListAZ().get(currentlySelected);
                    lastItem.getStatus().setAsUnselectedInInventory();
                    adapter.notifyItemChanged(currentlySelected);

                } else {
                    // nothing is selected
                    thisItem.getStatus().setAsSelectedInInventory();
                    getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                    getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

                    getShopping().setSelectedItemPositionInInventory(position);
                    getShopping().setItemIsSelectedInInventory(true);
                    getShopping().setSelectedItemInInventory(thisItem);
                }
            }
        }

        public void onClick(View v) {
            long clickTime = SystemClock.uptimeMillis();
            if (clickTime - getLastClickTime() < getDoubleClickTimeout()) {
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            setLastClickTime(clickTime);
        }

        private void onDoubleClick(View v) {

            int position = getAdapterPosition();
            Item thisItem = getItemData().getItemListAZ().get(position);
            getShopping().setPictureDialogInInventory(true);
            getShopping().setPictureDialogInSearchResults(false);
            getShopping().setPictureDialogInShoppingList(false);
            getShopping().showPictureDialog(thisItem);

        }

        private void onSingleClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            Item thisItem = getItemData().getItemListAZ().get(position);

            if (id == getItemSmallName().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getItemLargeName().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getItemLargeBrandLabel().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getItemLargeBrand().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getItemLargeCategoryLabel().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getItemLargeCategory().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getItemLargeStoreLabel().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getItemLargeStore().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getTriangleRight().getId()) {
                if (getTriangleRight().getVisibility() == View.VISIBLE && getTriangleDown().getVisibility() == View.GONE) {
                    getTriangleRight().setVisibility(View.GONE);
                    getTriangleDown().setVisibility(View.VISIBLE);
                    getItemSmall().setVisibility(View.GONE);
                    getItemLarge().setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsExpandedInShoppingList();
                }
            } else if (id == getTriangleDown().getId()) {
                if (getTriangleDown().getVisibility() == View.VISIBLE && getTriangleRight().getVisibility() == View.GONE) {
                    getTriangleDown().setVisibility(View.GONE);
                    getTriangleRight().setVisibility(View.VISIBLE);
                    getItemLarge().setVisibility(View.GONE);
                    getItemSmall().setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsContractedInShoppingList();
                }
            } else if (id == getItemSmallInStock().getId()) {
                if (getItemSmallInStock().getVisibility() == View.VISIBLE) {
                    getItemSmallInStock().setVisibility(View.GONE);
                    getItemLargeInStock().setVisibility(View.GONE);
                    getItemSmallPaused().setVisibility(View.GONE);
                    getItemLargePaused().setVisibility(View.GONE);
                    getItemSmallNeeded().setVisibility(View.VISIBLE);
                    getItemLargeNeeded().setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsNeeded();
                    getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.needed), getContext().getString(R.string.unchecked));
                }
            } else if (id == getItemSmallNeeded().getId()) {
                if (getItemSmallNeeded().getVisibility() == View.VISIBLE) {
                    getItemSmallNeeded().setVisibility(View.GONE);
                    getItemLargeNeeded().setVisibility(View.GONE);
                    getItemSmallInStock().setVisibility(View.GONE);
                    getItemLargeInStock().setVisibility(View.GONE);
                    getItemSmallPaused().setVisibility(View.VISIBLE);
                    getItemLargePaused().setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsPaused();
                    getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.paused), getContext().getString(R.string.unchecked));
                }
            } else if (id == getItemSmallPaused().getId()) {
                if (getItemSmallPaused().getVisibility() == View.VISIBLE) {
                    getItemSmallPaused().setVisibility(View.GONE);
                    getItemLargePaused().setVisibility(View.GONE);
                    getItemSmallNeeded().setVisibility(View.GONE);
                    getItemLargeNeeded().setVisibility(View.GONE);
                    getItemSmallInStock().setVisibility(View.VISIBLE);
                    getItemLargeInStock().setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsInStock();
                    getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.instock), getContext().getString(R.string.unchecked));
                }
            } else if (id == getItemLargeInStock().getId()) {
                if (getItemLargeInStock().getVisibility() == View.VISIBLE) {
                    getItemLargeInStock().setVisibility(View.GONE);
                    getItemSmallInStock().setVisibility(View.GONE);
                    getItemLargePaused().setVisibility(View.GONE);
                    getItemSmallPaused().setVisibility(View.GONE);
                    getItemLargeNeeded().setVisibility(View.VISIBLE);
                    getItemSmallNeeded().setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsNeeded();
                    getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.needed), getContext().getString(R.string.unchecked));
                }
            } else if (id == getItemLargeNeeded().getId()) {
                if (getItemLargeNeeded().getVisibility() == View.VISIBLE) {
                    getItemLargeNeeded().setVisibility(View.GONE);
                    getItemSmallNeeded().setVisibility(View.GONE);
                    getItemLargeInStock().setVisibility(View.GONE);
                    getItemSmallInStock().setVisibility(View.GONE);
                    getItemLargePaused().setVisibility(View.VISIBLE);
                    getItemSmallPaused().setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsPaused();
                    getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.paused), getContext().getString(R.string.unchecked));
                }
            } else if (id == getItemLargePaused().getId()) {
                if (getItemLargePaused().getVisibility() == View.VISIBLE) {
                    getItemLargePaused().setVisibility(View.GONE);
                    getItemSmallPaused().setVisibility(View.GONE);
                    getItemLargeNeeded().setVisibility(View.GONE);
                    getItemSmallNeeded().setVisibility(View.GONE);
                    getItemLargeInStock().setVisibility(View.VISIBLE);
                    getItemSmallInStock().setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsInStock();
                    getDbStatusHelper().updateStatus(thisItem.getItemName(), getContext().getString(R.string.instock), getContext().getString(R.string.unchecked));
                }
            }
        }
    }
}