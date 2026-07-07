package ryan.android.shopping;

import android.content.Context;
import android.os.SystemClock;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

class IndividualStoresRVA extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View view;
    private Context context;
    private Shopping shopping;
    private SearchAlgorithm searchAlgorithm;
    private String currentSearchTerm;
    private ArrayList<Item> searchResultsList;

    IndividualStoresRVA(View view, Context context, Shopping shopping) {
        setView(view);
        setContext(context);
        setShopping(shopping);
        setSearchAlgorithm(searchAlgorithm);
        setCurrentSearchTerm(getContext().getString(R.string.emptyString));
        setSearchResultsList(new ArrayList<>());
    }

    private IndividualStoresRVA getThis() {
        return this;
    }

    private View getView() {
        return view;
    }

    private void setView(View view) {
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

    private SearchAlgorithm getSearchAlgorithm() {
        return searchAlgorithm;
    }

    private void setSearchAlgorithm(SearchAlgorithm searchAlgorithm) {
        getThis().searchAlgorithm = searchAlgorithm;
    }

    private String getCurrentSearchTerm() {
        return currentSearchTerm;
    }

    void setCurrentSearchTerm(String currentSearchTerm) {
        getThis().currentSearchTerm = currentSearchTerm;
    }

    private ArrayList<Item> getSearchResultsList() {
        return searchResultsList;
    }

    private void setSearchResultsList(ArrayList<Item> searchResultsList) {
        getThis().searchResultsList = searchResultsList;
    }

    public int getItemViewType(int position) {
        return R.layout.individual_stores_rv;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        return new IndividualStoresRVH(getView(), getContext(), getShopping(), getThis());
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Item thisItem = null;
        String searchTerm = getCurrentSearchTerm();
        if (searchTerm != null && !searchTerm.equals(getContext().getString(R.string.emptyString))) {
            setSearchResultsList(getSearchAlgorithm().getSearchResults(searchTerm));
            ArrayList<Item> results = getSearchResultsList();
            if (results != null && position < results.size()) {
                thisItem = results.get(position);
            }
        }

        if (thisItem == null) {
            return;
        }

        IndividualStoresRVH searchResultsHolder = (IndividualStoresRVH) holder;

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
        return getSearchAlgorithm().numSearchResults(getCurrentSearchTerm());
    }

    private class IndividualStoresRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
        private Context context;
        private Shopping shopping;
        private IndividualStoresRVA adapter;
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

        private final long doubleClickTimeout = 300;
        private long lastClickTime = 0;

        private IndividualStoresRVH(View view, Context context, Shopping shopping, IndividualStoresRVA adapter) {

            super(view);
            setView(view);
            setContext(context);
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(getShopping().getItemData());
            setCategoryData(getShopping().getCategoryData()) ;
            setDbStatusHelper(new DBStatusHelper(shopping));

            setTriangleRight(getView().findViewById(R.id.triangleButtonRight));
            setTriangleDown(getView().findViewById(R.id.triangleButtonDown));
            setItemSmall(getView().findViewById(R.id.itemSmall));
            setItemLarge(getView().findViewById(R.id.itemLarge));
            setItemSmallName(getView().findViewById(R.id.itemSmallName));
            setItemLargeName(getView().findViewById(R.id.itemLargeName));
            setItemSmallInStock(getView().findViewById(R.id.itemSmallInStock));
            setItemSmallNeeded(getView().findViewById(R.id.itemSmallNeeded));
            setItemSmallPaused(getView().findViewById(R.id.itemSmallPaused));
            setItemSmallBrand(getView().findViewById(R.id.itemSmallBrand));
            setItemSmallBrandLabel(getView().findViewById(R.id.itemSmallBrandLabel));
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
            getItemSmallBrand().setOnClickListener(getThis());
            getItemSmallBrandLabel().setOnClickListener(getThis());
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

        private IndividualStoresRVH getThis() {
            return this;
        }

        public View getView() {
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

        private Context getContext() {
            return context;
        }

        private void setContext(Context context) {
            getThis().context = context;
        }

        private IndividualStoresRVA getAdapter() {
            return adapter;
        }

        private void setAdapter(IndividualStoresRVA adapter) {
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

        private TextView getItemSmallBrand() {
            return itemSmallBrand;
        }

        private void setItemSmallBrand(TextView itemSmallBrand) {
            getThis().itemSmallBrand = itemSmallBrand;
        }

        private TextView getItemSmallBrandLabel() {
            return itemSmallBrandLabel;
        }

        private void setItemSmallBrandLabel(TextView itemSmallBrandLabel) {
            getThis().itemSmallBrandLabel = itemSmallBrandLabel;
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

            if (position < 0 || position >= getSearchResultsList().size()) return;

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

                    if (currentlySelected >= 0 && currentlySelected < getItemData().getItemListAZ().size()) {
                        Item lastItem = getItemData().getItemListAZ().get(currentlySelected);
                        lastItem.getStatus().setAsUnselectedInSearchResults();
                        getAdapter().notifyItemChanged(currentlySelected);
                    }

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

        private void onDoubleClick(View v) {

            int position = getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;
            if (position >= getSearchResultsList().size()) return;

            Item thisItem = getSearchResultsList().get(position);
            getShopping().setPictureDialogInInventory(false);
            getShopping().setPictureDialogInSearchResults(true);
            getShopping().setPictureDialogInShoppingList(false);
            getShopping().showPictureDialog(thisItem);
        }

        private void onSingleClick(View v) {

            int id = v.getId();
            int position = getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;
            if (position >= getSearchResultsList().size()) return;

            Item thisItem = getSearchResultsList().get(position);

            if (id == getItemSmallName().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getItemLargeName().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getItemLargeBrandLabel().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getItemLargeBrand().getId()) {
                selectOrUnselectItem(position);
            } else if (id == getTriangleRight().getId()) {
                if (getTriangleRight().getVisibility() == View.VISIBLE && getTriangleDown().getVisibility() == View.GONE) {
                    getTriangleRight().setVisibility(View.GONE);
                    getTriangleDown().setVisibility(View.VISIBLE);
                    getItemSmall().setVisibility(View.GONE);
                    getItemLarge().setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsExpandedInSearchResults();
                }
            } else if (id == getTriangleDown().getId()) {
                if (getTriangleDown().getVisibility() == View.VISIBLE && getTriangleRight().getVisibility() == View.GONE) {
                    getTriangleDown().setVisibility(View.GONE);
                    getTriangleRight().setVisibility(View.VISIBLE);
                    getItemLarge().setVisibility(View.GONE);
                    getItemSmall().setVisibility(View.VISIBLE);

                    thisItem.getStatus().setAsContractedInSearchResults();
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