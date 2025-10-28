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

class ShoppingListRVA extends RecyclerView.Adapter {

    private final Shopping shopping;
    private final ItemData itemData;
    private final StoreData storeData;

    ShoppingListRVA(Shopping shopping, ItemData itemData, StoreData storeData) {
        this.shopping = shopping;
        this.itemData = itemData;
        this.storeData = storeData;
    }

    @Override
    public int getItemViewType(final int position) {

        if (position == 0) return R.layout.shopping_list_rv_title;
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
            if (position == index) return R.layout.shopping_list_rv_title;
        }
        return R.layout.shopping_list_rv_item;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == R.layout.shopping_list_rv_title) {
            return new ShoppingListTitleRVH(view);
        } else return new ShoppingListRVH(view, shopping, this, itemData, storeData);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

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

            ShoppingListTitleRVH titleHolder = (ShoppingListTitleRVH) holder;

            titleHolder.shoppingListRvTitle.setText(store);
            titleHolder.shoppingListRvTitle.setVisibility(View.VISIBLE);

            if (storeData.getStoreViewNeededMap().get(store) == 0) {
                titleHolder.shoppingListRvTitle.setVisibility(View.GONE);
            }

        } else {  // item data

            ShoppingListRVH itemHolder = (ShoppingListRVH) holder;

            assert thisItem != null;

            if (thisItem.getStatus().isNeeded()) {
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
                if (thisItem.getStatus().isChecked()) {
                    itemHolder.checkboxUnCheckedSmall.setVisibility(View.GONE);
                    itemHolder.checkboxUnCheckedLarge.setVisibility(View.GONE);
                    itemHolder.checkboxCheckedSmall.setVisibility(View.VISIBLE);
                    itemHolder.checkboxCheckedLarge.setVisibility(View.VISIBLE);
                } else {
                    itemHolder.checkboxCheckedSmall.setVisibility(View.GONE);
                    itemHolder.checkboxCheckedLarge.setVisibility(View.GONE);
                    itemHolder.checkboxUnCheckedSmall.setVisibility(View.VISIBLE);
                    itemHolder.checkboxUnCheckedLarge.setVisibility(View.VISIBLE);
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
                    !thisItem.getStore().toString().equals(storeData.getStoreList().get(shopping.storeListOrderNum - 1))) {
                itemHolder.triangleDown.setVisibility(View.GONE);
                itemHolder.triangleRight.setVisibility(View.GONE);
                itemHolder.itemLarge.setVisibility(View.GONE);
                itemHolder.itemSmall.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {

        return (itemData.getItemListByCategory().size() + storeData.getStoreList().size());

    }

    static class ShoppingListTitleRVH extends RecyclerView.ViewHolder {

        private final TextView shoppingListRvTitle;

        ShoppingListTitleRVH(View itemView) {

            super(itemView);

            shoppingListRvTitle = itemView.findViewById(R.id.shoppingListRvTitle);

        }
    }

    static class ShoppingListRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Shopping shopping;
        private final ShoppingListRVA adapter;
        private final ItemData itemData;
        private final StoreData storeData;

        final Button triangleRight;
        final Button triangleDown;
        final LinearLayout itemSmall;
        final LinearLayout itemLarge;
        final TextView itemSmallName;
        final TextView itemLargeName;
        final ImageView checkboxUnCheckedSmall;
        final ImageView checkboxCheckedSmall;
        final ImageView checkboxUnCheckedLarge;
        final ImageView checkboxCheckedLarge;
        final TextView itemLargeBrand;
        final TextView itemLargeBrandLabel;
        final TextView itemLargeCategory;
        final TextView itemLargeCategoryLabel;

        ShoppingListRVH(final View itemView, Shopping shopping, ShoppingListRVA adapter, ItemData itemData, StoreData storeData) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.itemData = itemData;
            this.storeData = storeData;

            triangleRight = itemView.findViewById(R.id.triangleButtonRight);
            triangleDown = itemView.findViewById(R.id.triangleButtonDown);
            itemSmall = itemView.findViewById(R.id.itemSmall);
            itemLarge = itemView.findViewById(R.id.itemLarge);
            itemSmallName = itemView.findViewById(R.id.itemSmallName);
            itemLargeName = itemView.findViewById(R.id.itemLargeName);
            checkboxUnCheckedSmall = itemView.findViewById(R.id.checkboxUnCheckedSmall);
            checkboxCheckedSmall = itemView.findViewById(R.id.checkboxCheckedSmall);
            checkboxUnCheckedLarge = itemView.findViewById(R.id.checkboxUnCheckedLarge);
            checkboxCheckedLarge = itemView.findViewById(R.id.checkboxCheckedLarge);
            itemLargeBrand = itemView.findViewById(R.id.itemLargeBrand);
            itemLargeCategory = itemView.findViewById(R.id.itemLargeCategory);
            itemLargeBrandLabel = itemView.findViewById(R.id.itemLargeBrandLabel);
            itemLargeCategoryLabel = itemView.findViewById(R.id.itemLargeCategoryLabel);

            triangleRight.setOnClickListener(this);
            triangleDown.setOnClickListener(this);
            checkboxUnCheckedSmall.setOnClickListener(this);
            checkboxCheckedSmall.setOnClickListener(this);
            checkboxUnCheckedLarge.setOnClickListener(this);
            checkboxCheckedLarge.setOnClickListener(this);
            itemSmallName.setOnClickListener(this);
            itemLargeName.setOnClickListener(this);
            itemLargeBrandLabel.setOnClickListener(this);
            itemLargeBrand.setOnClickListener(this);
            itemLargeCategoryLabel.setOnClickListener(this);
            itemLargeCategory.setOnClickListener(this);
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
                } else if (id == checkboxUnCheckedSmall.getId()) {
                    if (checkboxUnCheckedSmall.getVisibility() == View.VISIBLE) {
                        checkboxUnCheckedSmall.setVisibility(View.GONE);
                        checkboxUnCheckedLarge.setVisibility(View.GONE);
                        checkboxCheckedSmall.setVisibility(View.VISIBLE);
                        checkboxCheckedLarge.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsChecked();
                        shopping.getCheckedList().set(adjustedPosition, true);
                    }
                } else if (id == checkboxCheckedSmall.getId()) {
                    if (checkboxCheckedSmall.getVisibility() == View.VISIBLE) {
                        checkboxCheckedSmall.setVisibility(View.GONE);
                        checkboxCheckedLarge.setVisibility(View.GONE);
                        checkboxUnCheckedSmall.setVisibility(View.VISIBLE);
                        checkboxUnCheckedLarge.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsUnchecked();
                        shopping.getCheckedList().set(adjustedPosition, false);
                    }
                } else if (id == checkboxUnCheckedLarge.getId()) {
                    if (checkboxUnCheckedLarge.getVisibility() == View.VISIBLE) {
                        checkboxUnCheckedSmall.setVisibility(View.GONE);
                        checkboxUnCheckedLarge.setVisibility(View.GONE);
                        checkboxCheckedSmall.setVisibility(View.VISIBLE);
                        checkboxCheckedLarge.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsChecked();
                        shopping.getCheckedList().set(adjustedPosition, true);
                    }
                } else if (id == checkboxCheckedLarge.getId()) {
                    if (checkboxCheckedLarge.getVisibility() == View.VISIBLE) {
                        checkboxCheckedSmall.setVisibility(View.GONE);
                        checkboxCheckedLarge.setVisibility(View.GONE);
                        checkboxUnCheckedSmall.setVisibility(View.VISIBLE);
                        checkboxUnCheckedLarge.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsUnchecked();
                        shopping.getCheckedList().set(adjustedPosition, false);
                    }
                }
            }
        }
    }
}