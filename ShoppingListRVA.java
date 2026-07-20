package ryan.android.shopping;

import android.os.SystemClock;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class ShoppingListRVA extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private StoreData storeData;

    ShoppingListRVA(View view, Shopping shopping, ItemData itemData, StoreData storeData) {
        setView(view);
        setShopping(shopping);
        setItemData(itemData);
        setStoreData(storeData);
    }

    private ShoppingListRVA getThis() {
        return this;
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

    private StoreData getStoreData() {
        return storeData;
    }

    private void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
    }

    public int getItemViewType(int position) {

        if (position == 0) return R.layout.shopping_list_rv_title;
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
            if (position == index) return R.layout.shopping_list_rv_title;
        }
        return R.layout.shopping_list_rv_item;

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        if (viewType == R.layout.shopping_list_rv_title) {
            return new ShoppingListTitleRVH(getView());
        } else if (viewType == R.layout.shopping_list_rv_item) {
            return new ShoppingListItemRVH(getView(), getShopping(), getThis(), getItemData(), getStoreData());
        } else return new RecyclerView.ViewHolder(getView()) {};
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
                } else if (index >= adjustedPosition && adjustedPosition >= 0) {
                    isTitle = false;
                    if (getItemData().getStoreMap().get(store) != null) {
                        thisItem = getItemData().getStoreMap().get(store).getStoreItemsList().get(numItemsInStore - index + adjustedPosition);
                    }
                    break;
                }
            }
        }
        
        if (isTitle) { // titles

            ShoppingListTitleRVH titleHolder = (ShoppingListTitleRVH) holder;

            titleHolder.getShoppingListRvTitle().setText(store);
            titleHolder.getShoppingListRvTitle().setVisibility(View.VISIBLE);

            if (getStoreData().getStoreViewNeededMap().get(store) == 0) {
                titleHolder.getShoppingListRvTitle().setVisibility(View.GONE);
            }

        } else {  // item data

            ShoppingListItemRVH itemHolder = (ShoppingListItemRVH) holder;

            if (thisItem != null && thisItem.getStatus() != null && thisItem.getStatus().isNeeded()) {
                if (thisItem.getStatus().isExpandedInShoppingList()) {
                    itemHolder.getItemSmallName().setText(thisItem.getItemName());
                    itemHolder.getItemLargeName().setText(thisItem.getItemName());
                    itemHolder.getItemLargeBrand().setText(thisItem.getBrandType());
                    itemHolder.getItemLargeCategory().setText(thisItem.getCategory().toString());

                    itemHolder.getTriangleRight().setVisibility(View.GONE);
                    itemHolder.getTriangleDown().setVisibility(View.VISIBLE);
                    itemHolder.getItemSmall().setVisibility(View.GONE);
                    itemHolder.getItemLarge().setVisibility(View.VISIBLE);
                } else {
                    itemHolder.getItemSmallName().setText(thisItem.getItemName());
                    itemHolder.getItemLargeName().setText(thisItem.getItemName());
                    itemHolder.getItemLargeBrand().setText(thisItem.getBrandType());
                    itemHolder.getItemLargeCategory().setText(thisItem.getCategory().toString());

                    itemHolder.getTriangleDown().setVisibility(View.GONE);
                    itemHolder.getTriangleRight().setVisibility(View.VISIBLE);
                    itemHolder.getItemLarge().setVisibility(View.GONE);
                    itemHolder.getItemSmall().setVisibility(View.VISIBLE);
                }
                if (thisItem.getStatus().isChecked()) {
                    itemHolder.getCheckboxUncheckedSmall().setVisibility(View.GONE);
                    itemHolder.getCheckboxUncheckedLarge().setVisibility(View.GONE);
                    itemHolder.getCheckboxCheckedSmall().setVisibility(View.VISIBLE);
                    itemHolder.getCheckboxCheckedLarge().setVisibility(View.VISIBLE);
                } else if (thisItem.getStatus().isUnchecked()) {
                    itemHolder.getCheckboxCheckedSmall().setVisibility(View.GONE);
                    itemHolder.getCheckboxCheckedLarge().setVisibility(View.GONE);
                    itemHolder.getCheckboxUncheckedSmall().setVisibility(View.VISIBLE);
                    itemHolder.getCheckboxUncheckedLarge().setVisibility(View.VISIBLE);
                }
            } else {
                itemHolder.getTriangleDown().setVisibility(View.GONE);
                itemHolder.getTriangleRight().setVisibility(View.GONE);
                itemHolder.getItemLarge().setVisibility(View.GONE);
                itemHolder.getItemSmall().setVisibility(View.GONE);
            }

            if (thisItem != null && thisItem.getStatus() != null && thisItem.getStatus().isSelectedInShoppingList()) {
                itemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                itemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

            } else {
                if (getShopping().itemIsSelectedInShoppingList() && getShopping().getSelectedItemPositionInShoppingList() == position) {
                    itemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                    itemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);
                } else {
                    itemHolder.getItemSmall().setBackgroundResource(R.drawable.list_outline_unselected);
                    itemHolder.getItemLarge().setBackgroundResource(R.drawable.list_outline_unselected);
                }
            }

            if (thisItem != null && (getShopping().getShoppingListStoreOrderNum() != 0) &&
                    !thisItem.getStore().toString().equals(getStoreData().getStoreList().get(getShopping().getShoppingListStoreOrderNum() - 1))) {
                itemHolder.getTriangleDown().setVisibility(View.GONE);
                itemHolder.getTriangleRight().setVisibility(View.GONE);
                itemHolder.getItemLarge().setVisibility(View.GONE);
                itemHolder.getItemSmall().setVisibility(View.GONE);
            }
        }
    }

    public int getItemCount() {
        return (getItemData().getItemListByStore().size() + getStoreData().getStoreList().size());
    }

    private static class ShoppingListTitleRVH extends RecyclerView.ViewHolder {

        private TextView shoppingListRvTitle;

        private ShoppingListTitleRVH(View itemView) {
            super(itemView);
            setShoppingListRvTitle(itemView.findViewById(R.id.shoppingListRvTitle));
        }

        private ShoppingListTitleRVH getThis() {
            return this;
        }

        private TextView getShoppingListRvTitle() {
            return shoppingListRvTitle;
        }

        private void setShoppingListRvTitle(TextView shoppingListRvTitle) {
            getThis().shoppingListRvTitle = shoppingListRvTitle;
        }
    }

    private static class ShoppingListItemRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private ShoppingListRVA adapter;
        private ItemData itemData;
        private StoreData storeData;
        private Button triangleRight;
        private Button triangleDown;
        private LinearLayout itemSmall;
        private LinearLayout itemLarge;
        private TextView itemSmallName;
        private TextView itemLargeName;
        private ImageView checkboxUncheckedSmall;
        private ImageView checkboxCheckedSmall;
        private ImageView checkboxUncheckedLarge;
        private ImageView checkboxCheckedLarge;
        private TextView itemLargeBrand;
        private TextView itemLargeBrandLabel;
        private TextView itemLargeCategory;
        private TextView itemLargeCategoryLabel;

        private final long doubleClickTimeout = 350;
        private long lastClickTime = 0;

        private ShoppingListItemRVH(View itemView, Shopping shopping, ShoppingListRVA adapter, ItemData itemData, StoreData storeData) {

            super(itemView);
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(itemData);
            setStoreData(storeData);

            setTriangleRight(itemView.findViewById(R.id.triangleButtonRight));
            setTriangleDown(itemView.findViewById(R.id.triangleButtonDown));
            setItemSmall(itemView.findViewById(R.id.itemSmall));
            setItemLarge(itemView.findViewById(R.id.itemLarge));
            setItemSmallName(itemView.findViewById(R.id.itemSmallName));
            setItemLargeName(itemView.findViewById(R.id.itemLargeName));
            setCheckboxUncheckedSmall(itemView.findViewById(R.id.checkboxUncheckedSmall));
            setCheckboxCheckedSmall(itemView.findViewById(R.id.checkboxCheckedSmall));
            setCheckboxUncheckedLarge(itemView.findViewById(R.id.checkboxUncheckedLarge));
            setCheckboxCheckedLarge(itemView.findViewById(R.id.checkboxCheckedLarge));
            setItemLargeBrand(itemView.findViewById(R.id.itemLargeBrand));
            setItemLargeCategory(itemView.findViewById(R.id.itemLargeCategory));
            setItemLargeBrandLabel(itemView.findViewById(R.id.itemLargeBrandLabel));
            setItemLargeCategoryLabel(itemView.findViewById(R.id.itemLargeCategoryLabel));

            getTriangleRight().setOnClickListener(getThis());
            getTriangleDown().setOnClickListener(getThis());
            getCheckboxUncheckedSmall().setOnClickListener(getThis());
            getCheckboxCheckedSmall().setOnClickListener(getThis());
            getCheckboxUncheckedLarge().setOnClickListener(getThis());
            getCheckboxCheckedLarge().setOnClickListener(getThis());
            getItemSmallName().setOnClickListener(getThis());
            getItemLargeName().setOnClickListener(getThis());
            getItemLargeBrandLabel().setOnClickListener(getThis());
            getItemLargeBrand().setOnClickListener(getThis());
            getItemLargeCategoryLabel().setOnClickListener(getThis());
            getItemLargeCategory().setOnClickListener(getThis());
        }

        private ShoppingListItemRVH getThis() {
            return this;
        }

        private Shopping getShopping() {
            return shopping;
        }

        private void setShopping(Shopping shopping) {
            getThis().shopping = shopping;
        }

        private ShoppingListRVA getAdapter() {
            return adapter;
        }

        private void setAdapter(ShoppingListRVA adapter) {
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

        private ImageView getCheckboxUncheckedSmall() {
            return checkboxUncheckedSmall;
        }

        private void setCheckboxUncheckedSmall(ImageView checkboxUncheckedSmall) {
            getThis().checkboxUncheckedSmall = checkboxUncheckedSmall;
        }

        private ImageView getCheckboxCheckedSmall() {
            return checkboxCheckedSmall;
        }

        private void setCheckboxCheckedSmall(ImageView checkboxCheckedSmall) {
            getThis().checkboxCheckedSmall = checkboxCheckedSmall;
        }

        private ImageView getCheckboxUncheckedLarge() {
            return checkboxUncheckedLarge;
        }

        private void setCheckboxUncheckedLarge(ImageView checkboxUncheckedLarge) {
            getThis().checkboxUncheckedLarge = checkboxUncheckedLarge;
        }

        private ImageView getCheckboxCheckedLarge() {
            return checkboxCheckedLarge;
        }

        private void setCheckboxCheckedLarge(ImageView checkboxCheckedLarge) {
            getThis().checkboxCheckedLarge = checkboxCheckedLarge;
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
            String store;
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

                if (thisItem.getStatus().isSelectedInShoppingList() || thisItem == getShopping().getSelectedItemInShoppingList()) {
                    // selected item is this item
                    thisItem.getStatus().setAsUnselectedInShoppingList();
                    getItemSmall().setBackgroundResource(R.drawable.list_outline_unselected);
                    getItemLarge().setBackgroundResource(R.drawable.list_outline_unselected);

                    getShopping().setItemIsSelectedInShoppingList(false);
                    getShopping().setSelectedItemInShoppingList(null);
                } else {
                    if (getShopping().itemIsSelectedInShoppingList()) {
                        // selected item is another item
                        int currentlySelected = getShopping().getSelectedItemPositionInShoppingList();
                        thisItem.getStatus().setAsSelectedInShoppingList();
                        getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                        getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

                        getShopping().setSelectedItemPositionInShoppingList(position);
                        getShopping().setItemIsSelectedInShoppingList(true);
                        getShopping().setSelectedItemInShoppingList(thisItem);

                        Item lastItem = getItemWithStores(currentlySelected);
                        lastItem.getStatus().setAsUnselectedInShoppingList();
                        getAdapter().notifyItemChanged(currentlySelected);

                    } else {
                        // nothing is selected
                        thisItem.getStatus().setAsSelectedInShoppingList();
                        getItemSmall().setBackgroundResource(R.drawable.list_outline_selected);
                        getItemLarge().setBackgroundResource(R.drawable.list_outline_selected);

                        getShopping().setSelectedItemPositionInShoppingList(position);
                        getShopping().setItemIsSelectedInShoppingList(true);
                        getShopping().setSelectedItemInShoppingList(thisItem);
                    }
                }
            }
        }

        private Item getItemWithStores(int position) {

            String store;
            Item thisItem = null;
            int adjustedPosition;

            if (position == 0) return null;

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
                int id = v.getId();
                if (id == getCheckboxUncheckedSmall().getId() || id == getCheckboxUncheckedLarge().getId() ||
                        id == getCheckboxCheckedSmall().getId() || id == getCheckboxCheckedLarge().getId()) {
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
            if (thisItem != null) {
                getShopping().setPictureDialogInInventory(false);
                getShopping().setPictureDialogInSearchResults(false);
                getShopping().setPictureDialogInIndividualCategories(false);
                getShopping().setPictureDialogInIndividualStores(false);
                getShopping().setPictureDialogInShoppingList(true);
                getShopping().showPictureDialog(thisItem);
            }
        }

        private void onSingleClick(View v) {

            int id = v.getId();
            int position = getBindingAdapterPosition();

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
                } else if (id == getCheckboxUncheckedSmall().getId()) {
                    if (getCheckboxUncheckedSmall().getVisibility() == View.VISIBLE) {
                        getCheckboxUncheckedSmall().setVisibility(View.GONE);
                        getCheckboxUncheckedLarge().setVisibility(View.GONE);
                        getCheckboxCheckedSmall().setVisibility(View.VISIBLE);
                        getCheckboxCheckedLarge().setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsChecked();
                    }
                } else if (id == getCheckboxCheckedSmall().getId()) {
                    if (getCheckboxCheckedSmall().getVisibility() == View.VISIBLE) {
                        getCheckboxCheckedSmall().setVisibility(View.GONE);
                        getCheckboxCheckedLarge().setVisibility(View.GONE);
                        getCheckboxUncheckedSmall().setVisibility(View.VISIBLE);
                        getCheckboxUncheckedLarge().setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsUnchecked();
                    }
                } else if (id == getCheckboxUncheckedLarge().getId()) {
                    if (getCheckboxUncheckedLarge().getVisibility() == View.VISIBLE) {
                        getCheckboxUncheckedSmall().setVisibility(View.GONE);
                        getCheckboxUncheckedLarge().setVisibility(View.GONE);
                        getCheckboxCheckedSmall().setVisibility(View.VISIBLE);
                        getCheckboxCheckedLarge().setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsChecked();
                    }
                } else if (id == getCheckboxCheckedLarge().getId()) {
                    if (getCheckboxCheckedLarge().getVisibility() == View.VISIBLE) {
                        getCheckboxCheckedSmall().setVisibility(View.GONE);
                        getCheckboxCheckedLarge().setVisibility(View.GONE);
                        getCheckboxUncheckedSmall().setVisibility(View.VISIBLE);
                        getCheckboxUncheckedLarge().setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsUnchecked();
                    }
                }
            }
        }
    }
}