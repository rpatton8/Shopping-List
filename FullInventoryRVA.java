package ryan.android.shopping;

import android.content.Context;
import android.os.SystemClock;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class FullInventoryRVA extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

        if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getInventorySortBy())) {
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

        } else if (Shopping.SORT_BY_STORE.equals(getShopping().getInventorySortBy())) {

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

        } else if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getInventorySortBy())) {

            return R.layout.sort_alphabetical_rv_item;

        } else return -1;

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        setView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));

        if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getInventorySortBy())) {

            if (viewType == R.layout.sort_by_category_rv_title) {
                return new SortByCategoryTitleRVH(getView(), getShopping(), getThis(), getItemData(), getCategoryData(), getDbStatusHelper(), getDbCategoryHelper());
            } else if (viewType == R.layout.sort_by_category_rv_item) {
                return new SortByCategoryItemRVH(getView(), getContext(), getShopping(), getThis(), getItemData(), getCategoryData(), getDbStatusHelper(), getDbCategoryHelper());
            } else return new RecyclerView.ViewHolder(getView()) {};

        } else if (Shopping.SORT_BY_STORE.equals(getShopping().getInventorySortBy())) {

            if (viewType == R.layout.sort_by_store_rv_title) {
                return new SortByStoreTitleRVH(getView(), getShopping(), getThis(), getItemData(), getStoreData(), getDbStatusHelper(), getDbStoreHelper());
            } else if (viewType == R.layout.sort_by_store_rv_item) {
                return new SortByStoreItemRVH(getView(), getContext(), getShopping(), getThis(), getItemData(), getStoreData(), getDbStatusHelper(), getDbStoreHelper());
            } else return new RecyclerView.ViewHolder(getView()) {};

        } else if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getInventorySortBy())) {

            if (viewType == R.layout.sort_alphabetical_rv_item) {
                return new SortAlphabeticalItemRVH(getView(), getContext(), getShopping(), getThis(), getItemData(), getDbStatusHelper());
            } else return new RecyclerView.ViewHolder(getView()) {};

        } else return new RecyclerView.ViewHolder(getView()) {};

    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getInventorySortBy())) {
            onBindViewHolderByCategory(holder, position);
        } else if (Shopping.SORT_BY_STORE.equals(getShopping().getInventorySortBy())) {
            onBindViewHolderByStore(holder, position);
        } else if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getInventorySortBy())) {
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
                    if (i + 1 < getCategoryData().getCategoryList().size()) {
                        category = getCategoryData().getCategoryList().get(i + 1);
                    }
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
            categoryTitleHolder.setCategory(category);
            categoryTitleHolder.getCategoryTitleText().setText(category);
            categoryTitleHolder.getSortByCategoryRvTitle().setVisibility(View.VISIBLE);

            if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView()) && getCategoryData().getCategoryViewAllMap().get(category) == 0) {
                categoryTitleHolder.getSortByCategoryRvTitle().setVisibility(View.GONE);
            } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView()) && getCategoryData().getCategoryViewInStockMap().get(category) == 0) {
                categoryTitleHolder.getSortByCategoryRvTitle().setVisibility(View.GONE);
            } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView()) && getCategoryData().getCategoryViewNeededMap().get(category) == 0) {
                categoryTitleHolder.getSortByCategoryRvTitle().setVisibility(View.GONE);
            } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView()) && getCategoryData().getCategoryViewPausedMap().get(category) == 0) {
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
            if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
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
            } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
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
            } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
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
            } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
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
                    if (i + 1 < getStoreData().getStoreList().size()) {
                        store = getStoreData().getStoreList().get(i + 1);
                    }
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
            storeTitleHolder.setStore(store);
            storeTitleHolder.getStoreTitleText().setText(store);
            storeTitleHolder.getSortByStoreRvTitle().setVisibility(View.VISIBLE);

            if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView()) && getStoreData().getStoreViewAllMap().get(store) == 0) {
                storeTitleHolder.getSortByStoreRvTitle().setVisibility(View.GONE);
            } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView()) && getStoreData().getStoreViewInStockMap().get(store) == 0) {
                storeTitleHolder.getSortByStoreRvTitle().setVisibility(View.GONE);
            } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView()) && getStoreData().getStoreViewNeededMap().get(store) == 0) {
                storeTitleHolder.getSortByStoreRvTitle().setVisibility(View.GONE);
            } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView()) && getStoreData().getStoreViewPausedMap().get(store) == 0) {
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
            if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
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
            } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
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
            } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
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
            } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
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
        if (Shopping.VIEW_ALL.equals(getShopping().getInventoryView())) {
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
        } else if (Shopping.VIEW_INSTOCK.equals(getShopping().getInventoryView())) {
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
        } else if (Shopping.VIEW_NEEDED.equals(getShopping().getInventoryView())) {
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
        } else if (Shopping.VIEW_PAUSED.equals(getShopping().getInventoryView())) {
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
        if (Shopping.SORT_BY_CATEGORY.equals(getShopping().getInventorySortBy())) {
            return (getItemData().getItemListByCategory().size() + getCategoryData().getCategoryList().size());
        } else if (Shopping.SORT_BY_STORE.equals(getShopping().getInventorySortBy())) {
            return (getItemData().getItemListByStore().size() + getStoreData().getStoreList().size());
        } else if (Shopping.SORT_ALPHABETICAL.equals(getShopping().getInventorySortBy())) {
            return (getItemData().getItemListAZ().size());
        } else return -1;
    }

    //---------------------------------------Category Titles----------------------------------------

    private static class SortByCategoryTitleRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
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

        private String category;
        private boolean isExpanded;
        private boolean isContracted;

        private SortByCategoryTitleRVH(View view, Shopping shopping, FullInventoryRVA adapter, ItemData itemData,
                                       CategoryData categoryData, DBStatusHelper dbStatus, DBCategoryHelper dbCategory) {

            super(view);
            setView(view);
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(itemData);
            setCategoryData(categoryData);
            setDbStatusHelper(dbStatus);
            setDbCategoryHelper(dbCategory);

            setSortByCategoryRvTitle(getView().findViewById(R.id.sortByCategoryRvTitle));
            setCategoryTitleText(getView().findViewById(R.id.categoryTitleText));
            setTriangleButtonDown1(getView().findViewById(R.id.triangleButtonDown1));
            setTriangleButtonDown2(getView().findViewById(R.id.triangleButtonDown2));
            setTriangleButtonRight(getView().findViewById(R.id.triangleButtonRight));
            setTriangleButtonLeft(getView().findViewById(R.id.triangleButtonLeft));

            getCategoryTitleText().setOnClickListener(getThis());
            getTriangleButtonDown1().setOnClickListener(getThis());
            getTriangleButtonDown2().setOnClickListener(getThis());
            getTriangleButtonRight().setOnClickListener(getThis());
            getTriangleButtonLeft().setOnClickListener(getThis());

        }

        private SortByCategoryTitleRVH getThis() {
            return this;
        }

        private View getView() {
            return view;
        }

        public void setView(View view) {
            getThis().view = view;
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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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
            if (category == null) return;
            Category thisCategory = getItemData().getCategoryMap().get(category);
            if (thisCategory == null) return;

            if (id == getTriangleButtonDown1().getId() || id == getTriangleButtonDown2().getId()) {
                contractTitle();
                thisCategory.setCategoryAsContracted();
            } else if (id == getTriangleButtonRight().getId() || id == getTriangleButtonLeft().getId()) {
                expandTitle();
                thisCategory.setCategoryAsExpanded();
            } else if (id == getCategoryTitleText().getId() && isExpanded()) {
                contractTitle();
                thisCategory.setCategoryAsContracted();
            } else if (id == getCategoryTitleText().getId() && isContracted()) {
                expandTitle();
                thisCategory.setCategoryAsExpanded();
            }

            getAdapter().notifyDataSetChanged();
        }

        private void expandTitle() {
            setAsExpanded();
            getTriangleButtonDown1().setVisibility(View.VISIBLE);
            getTriangleButtonDown2().setVisibility(View.VISIBLE);
            getTriangleButtonRight().setVisibility(View.GONE);
            getTriangleButtonLeft().setVisibility(View.GONE);
        }

        private void contractTitle() {
            setAsContracted();
            getTriangleButtonDown1().setVisibility(View.GONE);
            getTriangleButtonDown2().setVisibility(View.GONE);
            getTriangleButtonRight().setVisibility(View.VISIBLE);
            getTriangleButtonLeft().setVisibility(View.VISIBLE);
        }
    }

//-----------------------------------------Category Items-------------------------------------------

    private static class SortByCategoryItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
        private Context context;
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

        private final long doubleClickTimeout = 300;
        private long lastClickTime = 0;

        private SortByCategoryItemRVH(View view, Context context, Shopping shopping, FullInventoryRVA adapter,  ItemData itemData,
                                      CategoryData categoryData, DBStatusHelper dbStatus, DBCategoryHelper dbCategory) {

            super(view);
            setView(view);
            setContext(context);
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(itemData);
            setCategoryData(categoryData);
            setDbStatusHelper(dbStatus);
            setDbCategoryHelper(dbCategory);

            setTriangleRight(getView().findViewById(R.id.triangleButtonRight));
            setTriangleDown(getView().findViewById(R.id.triangleButtonDown));
            setItemSmall(getView().findViewById(R.id.itemSmall));
            setItemLarge(getView().findViewById(R.id.itemLarge));
            setItemSmallName(getView().findViewById(R.id.itemSmallName));
            setItemLargeName(getView().findViewById(R.id.itemLargeName));
            setItemSmallInStock(getView().findViewById(R.id.itemSmallInStock));
            setItemSmallNeeded(getView().findViewById(R.id.itemSmallNeeded));
            setItemSmallPaused(getView().findViewById(R.id.itemSmallPaused));
            setItemLargeInStock(getView().findViewById(R.id.itemLargeInStock));
            setItemLargeNeeded(getView().findViewById(R.id.itemLargeNeeded));
            setItemLargePaused(getView().findViewById(R.id.itemLargePaused));
            setItemLargeBrand(getView().findViewById(R.id.itemLargeBrand));
            setItemLargeBrandLabel(getView().findViewById(R.id.itemLargeBrandLabel));
            setItemLargeCategory(getView().findViewById(R.id.itemLargeCategory));
            setItemLargeCategoryLabel(getView().findViewById(R.id.itemLargeCategoryLabel));
            setItemLargeStore(getView().findViewById(R.id.itemLargeStore));
            setItemLargeStoreLabel(getView().findViewById(R.id.itemLargeStoreLabel));

            getTriangleRight().setOnClickListener(getThis());
            getTriangleDown().setOnClickListener(getThis());
            getItemSmall().setOnClickListener(getThis());
            getItemLarge().setOnClickListener(getThis());
            getItemSmallName().setOnClickListener(getThis());
            getItemLargeName().setOnClickListener(getThis());
            getItemSmallInStock().setOnClickListener(getThis());
            getItemSmallNeeded().setOnClickListener(getThis());
            getItemSmallPaused().setOnClickListener(getThis());
            getItemLargeInStock().setOnClickListener(getThis());
            getItemLargeNeeded().setOnClickListener(getThis());
            getItemLargePaused().setOnClickListener(getThis());
            getItemLargeBrand().setOnClickListener(getThis());
            getItemLargeBrandLabel().setOnClickListener(getThis());
            getItemLargeCategory().setOnClickListener(getThis());
            getItemLargeCategoryLabel().setOnClickListener(getThis());
            getItemLargeStore().setOnClickListener(getThis());
            getItemLargeStoreLabel().setOnClickListener(getThis());
        }

        private SortByCategoryItemRVH getThis() {
            return this;
        }

        private View getView() {
            return view;
        }

        public void setView(View view) {
            getThis().view = view;
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
                        getAdapter().notifyItemChanged(currentlySelected);

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
                } else if (index >= adjustedPosition && adjustedPosition >= 0) {
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

            int position = getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            String category;
            Item thisItem = null;
            int adjustedPosition;

            if (position == 0) return;

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
                } else if (index >= adjustedPosition && adjustedPosition >= 0) {
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
            int position = getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

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
                    } else if (index >= adjustedPosition && adjustedPosition >= 0) {
                        isTitle = false;
                        thisItem = getItemData().getCategoryMap().get(category).getCategoryItemsList().get(numItemsInCategory - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle && thisItem != null) {

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

    private static class SortByStoreTitleRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
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

        private String store;
        private boolean isExpanded;
        private boolean isContracted;

        private SortByStoreTitleRVH(View view, Shopping shopping, FullInventoryRVA adapter, ItemData itemData,
                                    StoreData storeData, DBStatusHelper dbStatus, DBStoreHelper dbStore) {

            super(view);
            setView(view);
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(itemData);
            setStoreData(storeData);
            setDbStatusHelper(dbStatus);
            setDbStoreHelper(dbStore);

            setSortByStoreRvTitle(getView().findViewById(R.id.sortByStoreRvTitle));
            setStoreTitleText(getView().findViewById(R.id.storeTitleText));
            setTriangleButtonDown1(getView().findViewById(R.id.triangleButtonDown1));
            setTriangleButtonDown2(getView().findViewById(R.id.triangleButtonDown2));
            setTriangleButtonRight(getView().findViewById(R.id.triangleButtonRight));
            setTriangleButtonLeft(getView().findViewById(R.id.triangleButtonLeft));

            getStoreTitleText().setOnClickListener(getThis());
            getTriangleButtonDown1().setOnClickListener(getThis());
            getTriangleButtonDown2().setOnClickListener(getThis());
            getTriangleButtonRight().setOnClickListener(getThis());
            getTriangleButtonLeft().setOnClickListener(getThis());

        }

        private SortByStoreTitleRVH getThis() {
            return this;
        }

        private View getView() {
            return view;
        }

        public void setView(View view) {
            getThis().view = view;
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

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
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
            if (store == null) return;
            Store thisStore = getItemData().getStoreMap().get(store);
            if (thisStore == null) return;

            if (id == getTriangleButtonDown1().getId() || id == getTriangleButtonDown2().getId()) {
                contractTitle();
                thisStore.setStoreAsContracted();
            } else if (id == getTriangleButtonRight().getId() || id == getTriangleButtonLeft().getId()) {
                expandTitle();
                thisStore.setStoreAsExpanded();
            } else if (id == getStoreTitleText().getId() && isExpanded()) {
                contractTitle();
                thisStore.setStoreAsContracted();
            } else if (id == getStoreTitleText().getId() && isContracted()) {
                expandTitle();
                thisStore.setStoreAsExpanded();
            }

            getAdapter().notifyDataSetChanged();
        }

        private void expandTitle() {
            setAsExpanded();
            getTriangleButtonDown1().setVisibility(View.VISIBLE);
            getTriangleButtonDown2().setVisibility(View.VISIBLE);
            getTriangleButtonRight().setVisibility(View.GONE);
            getTriangleButtonLeft().setVisibility(View.GONE);
        }

        private void contractTitle() {
            setAsContracted();
            getTriangleButtonDown1().setVisibility(View.GONE);
            getTriangleButtonDown2().setVisibility(View.GONE);
            getTriangleButtonRight().setVisibility(View.VISIBLE);
            getTriangleButtonLeft().setVisibility(View.VISIBLE);
        }
    }

//-------------------------------------------Store Items--------------------------------------------

    private static class SortByStoreItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
        private Context context;
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

        private final long doubleClickTimeout = 300;
        private long lastClickTime = 0;

        private SortByStoreItemRVH(View view, Context context, Shopping shopping, FullInventoryRVA adapter,
                                  ItemData itemData, StoreData storeData, DBStatusHelper dbStatus, DBStoreHelper dbStore) {

            super(view);
            setView(view);
            setContext(context);
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(itemData);
            setStoreData(storeData);
            setDbStatusHelper(dbStatus);
            setDbStoreHelper(dbStore);

            setTriangleRight(getView().findViewById(R.id.triangleButtonRight));
            setTriangleDown(getView().findViewById(R.id.triangleButtonDown));
            setItemSmall(getView().findViewById(R.id.itemSmall));
            setItemLarge(getView().findViewById(R.id.itemLarge));
            setItemSmallName(getView().findViewById(R.id.itemSmallName));
            setItemLargeName(getView().findViewById(R.id.itemLargeName));
            setItemSmallInStock(getView().findViewById(R.id.itemSmallInStock));
            setItemSmallNeeded(getView().findViewById(R.id.itemSmallNeeded));
            setItemSmallPaused(getView().findViewById(R.id.itemSmallPaused));
            setItemLargeInStock(getView().findViewById(R.id.itemLargeInStock));
            setItemLargeNeeded(getView().findViewById(R.id.itemLargeNeeded));
            setItemLargePaused(getView().findViewById(R.id.itemLargePaused));
            setItemLargeBrand(getView().findViewById(R.id.itemLargeBrand));
            setItemLargeBrandLabel(getView().findViewById(R.id.itemLargeBrandLabel));
            setItemLargeCategory(getView().findViewById(R.id.itemLargeCategory));
            setItemLargeCategoryLabel(getView().findViewById(R.id.itemLargeCategoryLabel));
            setItemLargeStore(getView().findViewById(R.id.itemLargeStore));
            setItemLargeStoreLabel(getView().findViewById(R.id.itemLargeStoreLabel));

            getTriangleRight().setOnClickListener(getThis());
            getTriangleDown().setOnClickListener(getThis());
            getItemSmall().setOnClickListener(getThis());
            getItemLarge().setOnClickListener(getThis());
            getItemSmallName().setOnClickListener(getThis());
            getItemLargeName().setOnClickListener(getThis());
            getItemSmallInStock().setOnClickListener(getThis());
            getItemSmallNeeded().setOnClickListener(getThis());
            getItemSmallPaused().setOnClickListener(getThis());
            getItemLargeInStock().setOnClickListener(getThis());
            getItemLargeNeeded().setOnClickListener(getThis());
            getItemLargePaused().setOnClickListener(getThis());
            getItemLargeBrand().setOnClickListener(getThis());
            getItemLargeBrandLabel().setOnClickListener(getThis());
            getItemLargeCategory().setOnClickListener(getThis());
            getItemLargeCategoryLabel().setOnClickListener(getThis());
            getItemLargeStore().setOnClickListener(getThis());
            getItemLargeStoreLabel().setOnClickListener(getThis());
        }

        private SortByStoreItemRVH getThis() {
            return this;
        }

        private View getView() {
            return view;
        }

        public void setView(View view) {
            getThis().view = view;
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
                        getAdapter().notifyItemChanged(currentlySelected);

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
                } else if (index >= adjustedPosition && adjustedPosition >= 0) {
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

            int position = getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            String store;
            Item thisItem = null;
            int adjustedPosition;

            if (position == 0) return;

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
                } else if (index >= adjustedPosition && adjustedPosition >= 0) {
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
            int position = getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

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
                    } else if (index >= adjustedPosition && adjustedPosition >= 0) {
                        isTitle = false;
                        thisItem = getItemData().getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                        break;
                    }
                }
            }

            if (!isTitle && thisItem != null) {

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

    private static class SortAlphabeticalItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
        private Context context;
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

        private final long doubleClickTimeout = 300;
        private long lastClickTime = 0;

        private SortAlphabeticalItemRVH(View view, Context context, Shopping shopping, FullInventoryRVA adapter, ItemData itemData, DBStatusHelper dbStatus) {

            super(view);
            setView(view);
            setShopping(shopping);
            setContext(context);
            setAdapter(adapter);
            setItemData(itemData);
            setDbStatusHelper(dbStatus);

            setTriangleRight(getView().findViewById(R.id.triangleButtonRight));
            setTriangleDown(getView().findViewById(R.id.triangleButtonDown));
            setItemSmall(getView().findViewById(R.id.itemSmall));
            setItemLarge(getView().findViewById(R.id.itemLarge));
            setItemSmallName(getView().findViewById(R.id.itemSmallName));
            setItemLargeName(getView().findViewById(R.id.itemLargeName));
            setItemSmallInStock(getView().findViewById(R.id.itemSmallInStock));
            setItemSmallNeeded(getView().findViewById(R.id.itemSmallNeeded));
            setItemSmallPaused(getView().findViewById(R.id.itemSmallPaused));
            setItemLargeInStock(getView().findViewById(R.id.itemLargeInStock));
            setItemLargeNeeded(getView().findViewById(R.id.itemLargeNeeded));
            setItemLargePaused(getView().findViewById(R.id.itemLargePaused));
            setItemLargeBrand(getView().findViewById(R.id.itemLargeBrand));
            setItemLargeBrandLabel(getView().findViewById(R.id.itemLargeBrandLabel));
            setItemLargeCategory(getView().findViewById(R.id.itemLargeCategory));
            setItemLargeCategoryLabel(getView().findViewById(R.id.itemLargeCategoryLabel));
            setItemLargeStore(getView().findViewById(R.id.itemLargeStore));
            setItemLargeStoreLabel(getView().findViewById(R.id.itemLargeStoreLabel));

            getTriangleRight().setOnClickListener(getThis());
            getTriangleDown().setOnClickListener(getThis());
            getItemSmall().setOnClickListener(getThis());
            getItemLarge().setOnClickListener(getThis());
            getItemSmallName().setOnClickListener(getThis());
            getItemLargeName().setOnClickListener(getThis());
            getItemSmallInStock().setOnClickListener(getThis());
            getItemSmallNeeded().setOnClickListener(getThis());
            getItemSmallPaused().setOnClickListener(getThis());
            getItemLargeInStock().setOnClickListener(getThis());
            getItemLargeNeeded().setOnClickListener(getThis());
            getItemLargePaused().setOnClickListener(getThis());
            getItemLargeBrand().setOnClickListener(getThis());
            getItemLargeBrandLabel().setOnClickListener(getThis());
            getItemLargeCategory().setOnClickListener(getThis());
            getItemLargeCategoryLabel().setOnClickListener(getThis());
            getItemLargeStore().setOnClickListener(getThis());
            getItemLargeStoreLabel().setOnClickListener(getThis());
        }

        private SortAlphabeticalItemRVH getThis() {
            return this;
        }

        private View getView() {
            return view;
        }

        public void setView(View view) {
            getThis().view = view;
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

            if (position < 0 || position >= getItemData().getItemListAZ().size()) return;
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

                    if (currentlySelected >= 0 && currentlySelected < getItemData().getItemListAZ().size()) {
                        Item lastItem = getItemData().getItemListAZ().get(currentlySelected);
                        lastItem.getStatus().setAsUnselectedInInventory();
                        getAdapter().notifyItemChanged(currentlySelected);
                    }

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

            int position = getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;
            if (position >= getItemData().getItemListAZ().size()) return;
            Item thisItem = getItemData().getItemListAZ().get(position);

            getShopping().setPictureDialogInInventory(true);
            getShopping().setPictureDialogInSearchResults(false);
            getShopping().setPictureDialogInShoppingList(false);
            getShopping().showPictureDialog(thisItem);

        }

        private void onSingleClick(View v) {

            int id = v.getId();
            int position = getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;
            if (position >= getItemData().getItemListAZ().size()) return;

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