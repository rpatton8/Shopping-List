package ryan.android.shopping;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

class SearchInventoryRVA extends RecyclerView.Adapter  {

    private View view;
    private Context context;
    private Shopping shopping;
    private SearchAlgorithm searchAlgorithm;
    private String currentSearchTerm;
    private ArrayList<Item> searchResultsList;

    SearchInventoryRVA(View view, Context context, Shopping shopping, SearchAlgorithm searchAlgorithm) {
        setView(view);
        setContext(context);
        setShopping(shopping);
        setSearchAlgorithm(searchAlgorithm);
        setCurrentSearchTerm(getContext().getString(R.string.emptyString));
        setSearchResultsList(new ArrayList<Item>());
    }

    private SearchInventoryRVA getThis() {
        return this;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        getThis().view = view;
    }

    private Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        getThis().context = context;
    }

    public Shopping getShopping() {
        return shopping;
    }

    public void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    public SearchAlgorithm getSearchAlgorithm() {
        return searchAlgorithm;
    }

    public void setSearchAlgorithm(SearchAlgorithm searchAlgorithm) {
        getThis().searchAlgorithm = searchAlgorithm;
    }

    public String getCurrentSearchTerm() {
        return currentSearchTerm;
    }

    public void setCurrentSearchTerm(String currentSearchTerm) {
        getThis().currentSearchTerm = currentSearchTerm;
    }

    public ArrayList<Item> getSearchResultsList() {
        return searchResultsList;
    }

    public void setSearchResultsList(ArrayList<Item> searchResultsList) {
        getThis().searchResultsList = searchResultsList;
    }

    public int getItemViewType(int position) {
        return R.layout.search_inventory_rv;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new SearchInventoryRVH(getView(), getContext(), getShopping(), getThis());
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Item thisItem = null;
        if (!getCurrentSearchTerm().equals(getContext().getString(R.string.emptyString))) {
            setSearchResultsList(getSearchAlgorithm().getSearchResults(currentSearchTerm));
            thisItem = getSearchResultsList().get(position);
        }

        SearchInventoryRVH searchResultsHolder = (SearchInventoryRVH) holder;

        if (thisItem.getStatus().isExpandedInInventory()) {
            searchResultsHolder.getItemSmallName().setText(thisItem.getItemName());
            searchResultsHolder.getItemSmallBrand().setText(thisItem.getBrandType());
            searchResultsHolder.getItemLargeName().setText(thisItem.getItemName());
            searchResultsHolder.getItemLargeBrand().setText(thisItem.getBrandType());
            searchResultsHolder.getItemLargeCategory().setText(thisItem.getCategory().toString());
            searchResultsHolder.getItemLargeStore().setText(thisItem.getStore().toString());
            searchResultsHolder.getTriangleRight().setVisibility(View.GONE);
            searchResultsHolder.getTriangleDown().setVisibility(View.VISIBLE);
            searchResultsHolder.getItemSmall().setVisibility(View.GONE);
            searchResultsHolder.getItemLarge().setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isContractedInInventory()) {
            searchResultsHolder.getItemSmallName().setText(thisItem.getItemName());
            searchResultsHolder.getItemSmallBrand().setText(thisItem.getBrandType());
            searchResultsHolder.getItemLargeName().setText(thisItem.getItemName());
            searchResultsHolder.getItemLargeBrand().setText(thisItem.getBrandType());
            searchResultsHolder.getItemLargeCategory().setText(thisItem.getCategory().toString());
            searchResultsHolder.getItemLargeStore().setText(thisItem.getStore().toString());
            searchResultsHolder.getTriangleDown().setVisibility(View.GONE);
            searchResultsHolder.getTriangleRight().setVisibility(View.VISIBLE);
            searchResultsHolder.getItemLarge().setVisibility(View.GONE);
            searchResultsHolder.getItemSmall().setVisibility(View.VISIBLE);
        }

        if (thisItem.getStatus().isInStock()) {
            searchResultsHolder.getItemSmallPaused().setVisibility(View.GONE);
            searchResultsHolder.getItemLargePaused().setVisibility(View.GONE);
            searchResultsHolder.getItemSmallNeeded().setVisibility(View.GONE);
            searchResultsHolder.getItemLargeNeeded().setVisibility(View.GONE);
            searchResultsHolder.getItemSmallInStock().setVisibility(View.VISIBLE);
            searchResultsHolder.getItemLargeInStock().setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isNeeded()) {
            searchResultsHolder.getItemSmallInStock().setVisibility(View.GONE);
            searchResultsHolder.getItemLargeInStock().setVisibility(View.GONE);
            searchResultsHolder.getItemSmallPaused().setVisibility(View.GONE);
            searchResultsHolder.getItemLargePaused().setVisibility(View.GONE);
            searchResultsHolder.getItemSmallNeeded().setVisibility(View.VISIBLE);
            searchResultsHolder.getItemLargeNeeded().setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isPaused()) {
            searchResultsHolder.getItemSmallNeeded().setVisibility(View.GONE);
            searchResultsHolder.getItemLargeNeeded().setVisibility(View.GONE);
            searchResultsHolder.getItemSmallInStock().setVisibility(View.GONE);
            searchResultsHolder.getItemLargeInStock().setVisibility(View.GONE);
            searchResultsHolder.getItemSmallPaused().setVisibility(View.VISIBLE);
            searchResultsHolder.getItemLargePaused().setVisibility(View.VISIBLE);
        }

        if (thisItem.getStatus().isSelectedInInventory()) {
            searchResultsHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
            searchResultsHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

        } else {
            if (getShopping().itemIsSelectedInSearchResults() && getShopping().getSelectedItemPositionInSearchResults() == position) {
                searchResultsHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                searchResultsHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);
            } else {
                searchResultsHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_unselected);
                searchResultsHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_unselected);
            }
        }
    }

    public int getItemCount() {
        return searchAlgorithm.numSearchResults(currentSearchTerm);
    }

    private class SearchInventoryRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private Context context;
        private SearchInventoryRVA adapter;
        private ItemData itemData;
        private CategoryData categoryData;
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
        private TextView itemSmallBrand;
        private TextView itemSmallBrandLabel;
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

        private SearchInventoryRVH(View itemView, Context context, Shopping shopping, SearchInventoryRVA adapter) {

            super(itemView);
            setContext(context);
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(getShopping().getItemData());
            setCategoryData(getShopping().getCategoryData()) ;
            setDbStatusHelper(new DBStatusHelper(shopping));

            setTriangleRight((Button) itemView.findViewById(R.id.triangleButtonRight));
            setTriangleDown((Button) itemView.findViewById(R.id.triangleButtonDown));
            setItemSmall((LinearLayout) itemView.findViewById(R.id.itemSmall));
            setItemLarge((LinearLayout) itemView.findViewById(R.id.itemLarge));
            setItemSmallName((TextView) itemView.findViewById(R.id.itemSmallName));
            setItemLargeName((TextView) itemView.findViewById(R.id.itemLargeName));
            setItemSmallInStock((TextView) itemView.findViewById(R.id.itemSmallInStock));
            setItemSmallNeeded((TextView) itemView.findViewById(R.id.itemSmallNeeded));
            setItemSmallPaused((TextView) itemView.findViewById(R.id.itemSmallPaused));
            setItemSmallBrand((TextView) itemView.findViewById(R.id.itemSmallBrand));
            setItemSmallBrandLabel((TextView) itemView.findViewById(R.id.itemSmallBrandLabel));
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
            getItemSmallBrand().setOnClickListener(this);
            getItemSmallBrandLabel().setOnClickListener(this);
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

        private SearchInventoryRVH getThis() {
            return this;
        }

        public Shopping getShopping() {
            return shopping;
        }

        public void setShopping(Shopping shopping) {
            getThis().shopping = shopping;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            getThis().context = context;
        }

        public SearchInventoryRVA getAdapter() {
            return adapter;
        }

        public void setAdapter(SearchInventoryRVA adapter) {
            getThis().adapter = adapter;
        }

        public ItemData getItemData() {
            return itemData;
        }

        public void setItemData(ItemData itemData) {
            getThis().itemData = itemData;
        }

        public CategoryData getCategoryData() {
            return categoryData;
        }

        public void setCategoryData(CategoryData categoryData) {
            getThis().categoryData = categoryData;
        }

        public DBStatusHelper getDbStatusHelper() {
            return dbStatusHelper;
        }

        public void setDbStatusHelper(DBStatusHelper dbStatusHelper) {
            getThis().dbStatusHelper = dbStatusHelper;
        }

        public Button getTriangleRight() {
            return triangleRight;
        }

        public void setTriangleRight(Button triangleRight) {
            getThis().triangleRight = triangleRight;
        }

        public Button getTriangleDown() {
            return triangleDown;
        }

        public void setTriangleDown(Button triangleDown) {
            getThis().triangleDown = triangleDown;
        }

        public LinearLayout getItemSmall() {
            return itemSmall;
        }

        public void setItemSmall(LinearLayout itemSmall) {
            getThis().itemSmall = itemSmall;
        }

        public LinearLayout getItemLarge() {
            return itemLarge;
        }

        public void setItemLarge(LinearLayout itemLarge) {
            getThis().itemLarge = itemLarge;
        }

        public TextView getItemSmallName() {
            return itemSmallName;
        }

        public void setItemSmallName(TextView itemSmallName) {
            getThis().itemSmallName = itemSmallName;
        }

        public TextView getItemLargeName() {
            return itemLargeName;
        }

        public void setItemLargeName(TextView itemLargeName) {
            getThis().itemLargeName = itemLargeName;
        }

        public TextView getItemSmallInStock() {
            return itemSmallInStock;
        }

        public void setItemSmallInStock(TextView itemSmallInStock) {
            getThis().itemSmallInStock = itemSmallInStock;
        }

        public TextView getItemSmallNeeded() {
            return itemSmallNeeded;
        }

        public void setItemSmallNeeded(TextView itemSmallNeeded) {
            getThis().itemSmallNeeded = itemSmallNeeded;
        }

        public TextView getItemSmallPaused() {
            return itemSmallPaused;
        }

        public void setItemSmallPaused(TextView itemSmallPaused) {
            getThis().itemSmallPaused = itemSmallPaused;
        }

        public TextView getItemSmallBrand() {
            return itemSmallBrand;
        }

        public void setItemSmallBrand(TextView itemSmallBrand) {
            getThis().itemSmallBrand = itemSmallBrand;
        }

        public TextView getItemSmallBrandLabel() {
            return itemSmallBrandLabel;
        }

        public void setItemSmallBrandLabel(TextView itemSmallBrandLabel) {
            getThis().itemSmallBrandLabel = itemSmallBrandLabel;
        }

        public TextView getItemLargeInStock() {
            return itemLargeInStock;
        }

        public void setItemLargeInStock(TextView itemLargeInStock) {
            getThis().itemLargeInStock = itemLargeInStock;
        }

        public TextView getItemLargeNeeded() {
            return itemLargeNeeded;
        }

        public void setItemLargeNeeded(TextView itemLargeNeeded) {
            getThis().itemLargeNeeded = itemLargeNeeded;
        }

        public TextView getItemLargePaused() {
            return itemLargePaused;
        }

        public void setItemLargePaused(TextView itemLargePaused) {
            getThis().itemLargePaused = itemLargePaused;
        }

        public TextView getItemLargeBrand() {
            return itemLargeBrand;
        }

        public void setItemLargeBrand(TextView itemLargeBrand) {
            getThis().itemLargeBrand = itemLargeBrand;
        }

        public TextView getItemLargeBrandLabel() {
            return itemLargeBrandLabel;
        }

        public void setItemLargeBrandLabel(TextView itemLargeBrandLabel) {
            getThis().itemLargeBrandLabel = itemLargeBrandLabel;
        }

        public TextView getItemLargeCategory() {
            return itemLargeCategory;
        }

        public void setItemLargeCategory(TextView itemLargeCategory) {
            getThis().itemLargeCategory = itemLargeCategory;
        }

        public TextView getItemLargeCategoryLabel() {
            return itemLargeCategoryLabel;
        }

        public void setItemLargeCategoryLabel(TextView itemLargeCategoryLabel) {
            getThis().itemLargeCategoryLabel = itemLargeCategoryLabel;
        }

        public TextView getItemLargeStore() {
            return itemLargeStore;
        }

        public void setItemLargeStore(TextView itemLargeStore) {
            getThis().itemLargeStore = itemLargeStore;
        }

        public TextView getItemLargeStoreLabel() {
            return itemLargeStoreLabel;
        }

        public void setItemLargeStoreLabel(TextView itemLargeStoreLabel) {
            getThis().itemLargeStoreLabel = itemLargeStoreLabel;
        }

        public long getDoubleClickTimeout() {
            return doubleClickTimeout;
        }

        public long getLastClickTime() {
            return lastClickTime;
        }

        public void setLastClickTime(long lastClickTime) {
            getThis().lastClickTime = lastClickTime;
        }

        private void selectOrUnselectItem(int position) {

            Item thisItem = getSearchResultsList().get(position);

            if (thisItem.getStatus().isSelectedInSearchResults() || thisItem == getShopping().getSelectedItemInSearchResults()) {
                // selected item is this item
                thisItem.getStatus().setAsUnselectedInSearchResults();
                getItemSmall().setBackgroundResource(R.drawable.list_outline_unselected);
                getItemLarge().setBackgroundResource(R.drawable.list_outline_unselected);

                getShopping().setItemIsSelectedInSearchResults(false);
                getShopping().setSelectedItemInSearchResults(null);
            } else {
                if (getShopping().itemIsSelectedInSearchResults()) {
                    // selected item is another item
                    int currentlySelected = getShopping().getSelectedItemPositionInSearchResults();
                    thisItem.getStatus().setAsSelectedInSearchResults();
                    getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                    getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

                    getShopping().setSelectedItemPositionInSearchResults(position);
                    getShopping().setItemIsSelectedInSearchResults(true);
                    getShopping().setSelectedItemInSearchResults(thisItem);

                    Item lastItem = getItemData().getItemListAZ().get(currentlySelected);
                    lastItem.getStatus().setAsUnselectedInSearchResults();
                    getAdapter().notifyItemChanged(currentlySelected);

                } else {
                    // nothing is selected
                    thisItem.getStatus().setAsSelectedInSearchResults();
                    getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                    getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

                    getShopping().setSelectedItemPositionInSearchResults(position);
                    getShopping().setItemIsSelectedInSearchResults(true);
                    getShopping().setSelectedItemInSearchResults(thisItem);
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

        void onDoubleClick(View v) {

            int position = getAdapterPosition();
            Item thisItem = getSearchResultsList().get(position);
            getShopping().setPictureDialogInInventory(false);
            getShopping().setPictureDialogInSearchResults(true);
            getShopping().setPictureDialogInShoppingList(false);
            getShopping().showPictureDialog(thisItem);
        }

        void onSingleClick(View v) {

            int id = v.getId();
            int position = getAdapterPosition();

            Item thisItem = searchResultsList.get(position);

            if (id == itemSmallName.getId()) {
                selectOrUnselectItem(position);
            } else if (id == itemLargeName.getId()) {
                selectOrUnselectItem(position);
            } else if (id == itemLargeBrandLabel.getId()) {
                selectOrUnselectItem(position);
            } else if (id == itemLargeBrand.getId()) {
                selectOrUnselectItem(position);
            } else if (id == triangleRight.getId()) {
                if (triangleRight.getVisibility() == View.VISIBLE && triangleDown.getVisibility() == View.GONE) {
                    triangleRight.setVisibility(View.GONE);
                    triangleDown.setVisibility(View.VISIBLE);
                    itemSmall.setVisibility(View.GONE);
                    itemLarge.setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsExpandedInSearchResults();
                }
            } else if (id == triangleDown.getId()) {
                if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                    triangleDown.setVisibility(View.GONE);
                    triangleRight.setVisibility(View.VISIBLE);
                    itemLarge.setVisibility(View.GONE);
                    itemSmall.setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsContractedInSearchResults();
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
                    dbStatusHelper.updateStatus(thisItem.getItemName(), getContext().getString(R.string.needed), getContext().getString(R.string.unchecked));
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
                    dbStatusHelper.updateStatus(thisItem.getItemName(), getContext().getString(R.string.paused), getContext().getString(R.string.unchecked));
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
                    dbStatusHelper.updateStatus(thisItem.getItemName(), getContext().getString(R.string.instock), getContext().getString(R.string.unchecked));
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
                    dbStatusHelper.updateStatus(thisItem.getItemName(), getContext().getString(R.string.needed), getContext().getString(R.string.unchecked));
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
                    dbStatusHelper.updateStatus(thisItem.getItemName(), getContext().getString(R.string.paused), getContext().getString(R.string.unchecked));
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
                    dbStatusHelper.updateStatus(thisItem.getItemName(), getContext().getString(R.string.instock), getContext().getString(R.string.unchecked));
                }
            }
        }
    }
}