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

class IndividualCategoriesRVA extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View view;
    private Context context;
    private Shopping shopping;
    private ItemData itemData;
    private String category;

    IndividualCategoriesRVA(View view, Context context, Shopping shopping, ItemData itemData) {
        setView(view);
        setContext(context);
        setShopping(shopping);
        setItemData(itemData);
        setCategory(getShopping().getIndividualCategory());
    }

    private IndividualCategoriesRVA getThis() {
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

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        getThis().itemData = itemData;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        getThis().category = category;
    }

    public int getItemViewType(int position) {
        return R.layout.individual_categories_rv;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        return new IndividualCategoriesRVH(getView(), getContext(), getShopping(), getThis());
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemData().getCategoryMap().get(getCategory()) == null) return;
        ArrayList<Item> categoryList = getItemData().getCategoryMap().get(getCategory()).getCategoryItemsList();
        Item thisItem = categoryList.get(position);

        IndividualCategoriesRVH individualCategoriesHolder = (IndividualCategoriesRVH) holder;

        if (thisItem.getStatus().isExpandedInInventory()) {
            individualCategoriesHolder.getItemSmallName().setText(thisItem.getItemName());
            individualCategoriesHolder.getItemLargeName().setText(thisItem.getItemName());
            individualCategoriesHolder.getItemLargeBrand().setText(thisItem.getBrandType());
            individualCategoriesHolder.getItemLargeStore().setText(thisItem.getStore().toString());
            individualCategoriesHolder.getTriangleRight().setVisibility(View.GONE);
            individualCategoriesHolder.getTriangleDown().setVisibility(View.VISIBLE);
            individualCategoriesHolder.getItemSmall().setVisibility(View.GONE);
            individualCategoriesHolder.getItemLarge().setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isContractedInInventory()) {
            individualCategoriesHolder.getItemSmallName().setText(thisItem.getItemName());
            individualCategoriesHolder.getItemLargeName().setText(thisItem.getItemName());
            individualCategoriesHolder.getItemLargeBrand().setText(thisItem.getBrandType());
            individualCategoriesHolder.getItemLargeStore().setText(thisItem.getStore().toString());
            individualCategoriesHolder.getTriangleDown().setVisibility(View.GONE);
            individualCategoriesHolder.getTriangleRight().setVisibility(View.VISIBLE);
            individualCategoriesHolder.getItemLarge().setVisibility(View.GONE);
            individualCategoriesHolder.getItemSmall().setVisibility(View.VISIBLE);
        }

        if (thisItem.getStatus().isInStock()) {
            individualCategoriesHolder.getItemSmallPaused().setVisibility(View.GONE);
            individualCategoriesHolder.getItemLargePaused().setVisibility(View.GONE);
            individualCategoriesHolder.getItemSmallNeeded().setVisibility(View.GONE);
            individualCategoriesHolder.getItemLargeNeeded().setVisibility(View.GONE);
            individualCategoriesHolder.getItemSmallInStock().setVisibility(View.VISIBLE);
            individualCategoriesHolder.getItemLargeInStock().setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isNeeded()) {
            individualCategoriesHolder.getItemSmallInStock().setVisibility(View.GONE);
            individualCategoriesHolder.getItemLargeInStock().setVisibility(View.GONE);
            individualCategoriesHolder.getItemSmallPaused().setVisibility(View.GONE);
            individualCategoriesHolder.getItemLargePaused().setVisibility(View.GONE);
            individualCategoriesHolder.getItemSmallNeeded().setVisibility(View.VISIBLE);
            individualCategoriesHolder.getItemLargeNeeded().setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isPaused()) {
            individualCategoriesHolder.getItemSmallNeeded().setVisibility(View.GONE);
            individualCategoriesHolder.getItemLargeNeeded().setVisibility(View.GONE);
            individualCategoriesHolder.getItemSmallInStock().setVisibility(View.GONE);
            individualCategoriesHolder.getItemLargeInStock().setVisibility(View.GONE);
            individualCategoriesHolder.getItemSmallPaused().setVisibility(View.VISIBLE);
            individualCategoriesHolder.getItemLargePaused().setVisibility(View.VISIBLE);
        }

        if (thisItem.getStatus().isSelectedInInventory()) {
            individualCategoriesHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
            individualCategoriesHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

        } else {
            if (getShopping().itemIsSelectedInSearchResults() && getShopping().getSelectedItemPositionInSearchResults() == position) {
                individualCategoriesHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                individualCategoriesHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);
            } else {
                individualCategoriesHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_unselected);
                individualCategoriesHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_unselected);
            }
        }
    }

    public int getItemCount() {
        if (getItemData().getCategoryMap().get(getCategory()) == null) return 0;
        return getItemData().getCategoryMap().get(getCategory()).getCategoryItemsList().size();
    }

    void changeCategory(String category) {
        getThis().setCategory(category);
        getShopping().setIndividualCategory(category);
    }

    private class IndividualCategoriesRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
        private Context context;
        private Shopping shopping;
        private IndividualCategoriesRVA adapter;
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
        private TextView itemLargeInStock;
        private TextView itemLargeNeeded;
        private TextView itemLargePaused;
        private TextView itemLargeBrand;
        private TextView itemLargeBrandLabel;
        private TextView itemLargeStore;
        private TextView itemLargeStoreLabel;

        private final long doubleClickTimeout = 350;
        private long lastClickTime = 0;

        private IndividualCategoriesRVH(View view, Context context, Shopping shopping, IndividualCategoriesRVA adapter) {

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
            setItemLargeInStock(getView().findViewById(R.id.itemLargeInStock));
            setItemLargeNeeded(getView().findViewById(R.id.itemLargeNeeded));
            setItemLargePaused(getView().findViewById(R.id.itemLargePaused));
            setItemLargeBrand(getView().findViewById(R.id.itemLargeBrand));
            setItemLargeBrandLabel(getView().findViewById(R.id.itemLargeBrandLabel));
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
            getItemLargeStore().setOnClickListener(getThis());
            getItemLargeStoreLabel().setOnClickListener(getThis());
        }

        private IndividualCategoriesRVH getThis() {
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

        private IndividualCategoriesRVA getAdapter() {
            return adapter;
        }

        private void setAdapter(IndividualCategoriesRVA adapter) {
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

            if (getItemData().getCategoryMap().get(getCategory()) == null) return;
            ArrayList<Item> categoryList = getItemData().getCategoryMap().get(getCategory()).getCategoryItemsList();
            Item thisItem = categoryList.get(position);

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
                int id = v.getId();
                if (id == getItemSmallInStock().getId() || id == getItemLargeInStock().getId() ||
                        id == getItemSmallNeeded().getId() || id == getItemLargeNeeded().getId() ||
                        id == getItemSmallPaused().getId() || id == getItemLargePaused().getId()) {
                    return;
                }
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            setLastClickTime(clickTime);
        }

        private void onDoubleClick(View v) {

            int position = getBindingAdapterPosition();

            if (getItemData().getCategoryMap().get(getCategory()) == null) return;
            ArrayList<Item> categoryList = getItemData().getCategoryMap().get(getCategory()).getCategoryItemsList();
            Item thisItem = categoryList.get(position);

            getShopping().setPictureDialogInInventory(false);
            getShopping().setPictureDialogInSearchResults(false);
            getShopping().setPictureDialogInIndividualCategories(true);
            getShopping().setPictureDialogInIndividualStores(false);
            getShopping().setPictureDialogInShoppingList(false);
            getShopping().showPictureDialog(thisItem);
        }

        private void onSingleClick(View v) {

            int id = v.getId();
            int position = getBindingAdapterPosition();

            if (getItemData().getCategoryMap().get(getCategory()) == null) return;
            ArrayList<Item> categoryList = getItemData().getCategoryMap().get(getCategory()).getCategoryItemsList();
            Item thisItem = categoryList.get(position);

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