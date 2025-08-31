package ryan.android.shopping;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopByStoreRVA extends RecyclerView.Adapter {

    private Shopping shopping;
    private ItemData itemData;
    private StoreData storeData;

    public ShopByStoreRVA(Shopping shopping, ItemData itemData, StoreData storeData) {
        this.shopping = shopping;
        this.itemData = itemData;
        this.storeData = storeData;
    }

    @Override
    public int getItemViewType(final int position) {

        if (position == 0) return R.layout.shop_by_store_rv_title;
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
            if (position == index) return R.layout.shop_by_store_rv_title;
        }
        return R.layout.shop_by_store_rv_item;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == R.layout.shop_by_store_rv_title) {
            return new ShopByStoreTitleRVH(view);
        } else return new ShopByStoreRVH(view, shopping, this, itemData, storeData);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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

            ShopByStoreTitleRVH titleHolder = (ShopByStoreTitleRVH) holder;

            titleHolder.shopByStoreRvTitle.setText(store);
            titleHolder.shopByStoreRvTitle.setVisibility(View.VISIBLE);


            int storeItemsNeeded = storeData.getStoreItemsNeededMap().get(store);
            Toast.makeText(shopping, "Store = " + store + ", itemsNeeded = " + storeItemsNeeded, Toast.LENGTH_SHORT).show();
            if (storeData.getStoreItemsNeededMap().get(store) == 0) {
                titleHolder.shopByStoreRvTitle.setVisibility(View.GONE);
            }


            //--------------------XXX------------------------------

        } else {  // item data

            ShopByStoreRVH itemHolder = (ShopByStoreRVH) holder;

            if (thisItem != null && thisItem.getStatus().isNeeded()) {
                if (shopping.getClickedShopByStoreList().get(adjustedPosition)) {
                    itemHolder.itemSmallName.setText(thisItem.getName());
                    itemHolder.itemLargeName.setText(thisItem.getName());
                    itemHolder.itemLargeBrand.setText(thisItem.getBrand());

                    itemHolder.itemLargeCategory.setText(thisItem.getCategory(0).toString());

                    itemHolder.triangleRight.setVisibility(View.GONE);
                    itemHolder.triangleDown.setVisibility(View.VISIBLE);
                    itemHolder.itemSmall.setVisibility(View.GONE);
                    itemHolder.itemLarge.setVisibility(View.VISIBLE);
                } else {
                    itemHolder.itemSmallName.setText(thisItem.getName());
                    itemHolder.itemLargeName.setText(thisItem.getName());
                    itemHolder.itemLargeBrand.setText(thisItem.getBrand());

                    itemHolder.itemLargeCategory.setText(thisItem.getCategory(0).toString());

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

            if (thisItem.getStatus().isSelectedInShopByStore()) {
                itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

            } else {
                if (shopping.itemIsSelectedInShopByStore && shopping.selectedItemPositionInShopByStore == position) {
                    itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                    itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);
                } else {
                    itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_unselected);
                    itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_unselected);
                }
            }

            if ((shopping.storeNum != 0) &&
                    !thisItem.getStore(0).toString().equals(storeData.getStoreList().get(shopping.storeNum - 1))) {
                itemHolder.triangleDown.setVisibility(View.GONE);
                itemHolder.triangleRight.setVisibility(View.GONE);
                itemHolder.itemLarge.setVisibility(View.GONE);
                itemHolder.itemSmall.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {

        return (itemData.getItemList().size() + storeData.getStoreList().size());

    }

    public static class ShopByStoreTitleRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView shopByStoreRvTitle;

        public ShopByStoreTitleRVH(View itemView) {

            super(itemView);

            shopByStoreRvTitle = itemView.findViewById(R.id.shopByStoreRvTitle);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public static class ShopByStoreRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private ShopByStoreRVA adapter;
        private ItemData itemData;
        private StoreData storeData;

        public Button triangleRight;
        public Button triangleDown;
        public LinearLayout itemSmall;
        public LinearLayout itemLarge;
        public TextView itemSmallName;
        public TextView itemLargeName;
        public ImageView checkboxUnCheckedSmall;
        public ImageView checkboxCheckedSmall;
        public ImageView checkboxUnCheckedLarge;
        public ImageView checkboxCheckedLarge;
        public TextView itemLargeBrand;
        public TextView itemLargeBrandLabel;
        public TextView itemLargeCategory;
        public TextView itemLargeCategoryLabel;

        public ShopByStoreRVH(final View itemView, Shopping shopping, ShopByStoreRVA adapter, ItemData itemData, StoreData storeData) {

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

            if (!isTitle) {
                if (thisItem.getStatus().isSelectedInShopByStore() || thisItem == shopping.selectedItemInShopByStore) {
                    // selected item is this item
                    thisItem.getStatus().setAsUnselectedInShopByStore();
                    itemSmall.setBackgroundResource(R.drawable.list_outline_unselected);
                    itemLarge.setBackgroundResource(R.drawable.list_outline_unselected);

                    shopping.itemIsSelectedInShopByStore = false;
                    shopping.selectedItemInShopByStore = null;
                } else {
                    if (shopping.itemIsSelectedInShopByStore) {
                        // selected item is another item
                        int currentlySelected = shopping.selectedItemPositionInShopByStore;
                        thisItem.getStatus().setAsSelectedInShopByStore();
                        itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                        itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

                        shopping.selectedItemPositionInShopByStore = position;
                        shopping.itemIsSelectedInShopByStore = true;
                        shopping.selectedItemInShopByStore = thisItem;

                        Item lastItem = getItemWithStores(currentlySelected);
                        if (lastItem != null) {
                            lastItem.getStatus().setAsUnselectedInShopByStore();
                            adapter.notifyItemChanged(currentlySelected);
                        }

                    } else {
                        // nothing is selected
                        thisItem.getStatus().setAsSelectedInShopByStore();
                        itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                        itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

                        shopping.selectedItemPositionInShopByStore = position;
                        shopping.itemIsSelectedInShopByStore = true;
                        shopping.selectedItemInShopByStore = thisItem;
                    }
                }
            }
        }


        public Item getItemWithStores(int position) {

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
            return thisItem;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int position = getAdapterPosition();

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
                        thisItem.getStatus().setAsClickedInShopByStore();
                        shopping.getClickedShopByStoreList().set(adjustedPosition, true);
                    }
                } else if (id == triangleDown.getId()) {
                    if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                        triangleDown.setVisibility(View.GONE);
                        triangleRight.setVisibility(View.VISIBLE);
                        itemLarge.setVisibility(View.GONE);
                        itemSmall.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsUnclickedInShopByStore();
                        shopping.getClickedShopByStoreList().set(adjustedPosition, false);
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