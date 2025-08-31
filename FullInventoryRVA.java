package ryan.android.shopping;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FullInventoryRVA extends RecyclerView.Adapter {

    private Shopping shopping;
    private ItemData itemData;
    private CategoryData categoryData;
    private DBStatusHelper dbStatusHelper;
    private DBStoreHelper dbStoreHelper;

    public FullInventoryRVA(Shopping shopping, ItemData itemData, CategoryData categoryData,
                            DBStatusHelper dbStatus, DBStoreHelper dbStore) {
        this.shopping = shopping;
        this.itemData = itemData;
        this.categoryData = categoryData;
        this.dbStatusHelper = dbStatus;
        this.dbStoreHelper = dbStore;
    }

    @Override
    public int getItemViewType(final int position) {

        if (position == 0) return R.layout.full_inventory_rv_title;
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
            if (position == index) return R.layout.full_inventory_rv_title;
        }
        return R.layout.full_inventory_rv_item;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == R.layout.full_inventory_rv_title) {
            return new FullInventoryTitleRVH(view);
        } else return new FullInventoryRVH(view, shopping, this, itemData, categoryData, dbStatusHelper, dbStoreHelper);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Item thisItem = null;
        String category = null;
        boolean isTitle = false;
        int adjustedPosition = 0;

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

            FullInventoryTitleRVH titleHolder = (FullInventoryTitleRVH) holder;

            titleHolder.fullInventoryRvTitle.setText(category);
            //titleHolder.fullInventoryRvTitle.setVisibility(View.VISIBLE);

            /*if (categoryData.getCategoryViewAllMap().get(category) == 0) {
                titleHolder.fullInventoryRvTitle.setVisibility(View.GONE);
            }
            if (categoryData.getCategoryViewInStockMap().get(category) == 0) {
                titleHolder.fullInventoryRvTitle.setVisibility(View.GONE);
            }
            if (categoryData.getCategoryViewNeededMap().get(category) == 0) {
                titleHolder.fullInventoryRvTitle.setVisibility(View.GONE);
            }
            if (categoryData.getCategoryViewPausedMap().get(category) == 0) {
                titleHolder.fullInventoryRvTitle.setVisibility(View.GONE);
            }*/

        } else {  // item data

            FullInventoryRVH itemHolder = (FullInventoryRVH) holder;

            if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                itemHolder.itemSmallName.setText(thisItem.getName());
                itemHolder.itemLargeName.setText(thisItem.getName());
                itemHolder.itemLargeBrand.setText(thisItem.getBrand());
                itemHolder.itemLargeStore.setText(thisItem.getStore(0).toString());
                itemHolder.triangleRight.setVisibility(View.GONE);
                itemHolder.triangleDown.setVisibility(View.VISIBLE);
                itemHolder.itemSmall.setVisibility(View.GONE);
                itemHolder.itemLarge.setVisibility(View.VISIBLE);
            } else {
                itemHolder.itemSmallName.setText(thisItem.getName());
                itemHolder.itemLargeName.setText(thisItem.getName());
                itemHolder.itemLargeBrand.setText(thisItem.getBrand());
                itemHolder.itemLargeStore.setText(thisItem.getStore(0).toString());
                itemHolder.triangleDown.setVisibility(View.GONE);
                itemHolder.triangleRight.setVisibility(View.VISIBLE);
                itemHolder.itemLarge.setVisibility(View.GONE);
                itemHolder.itemSmall.setVisibility(View.VISIBLE);
            }
            if (thisItem.getStatus().isInStock()) {
                itemHolder.itemSmallPaused.setVisibility(View.GONE);
                itemHolder.itemLargePaused.setVisibility(View.GONE);
                itemHolder.itemSmallNeeded.setVisibility(View.GONE);
                itemHolder.itemLargeNeeded.setVisibility(View.GONE);
                itemHolder.itemSmallInStock.setVisibility(View.VISIBLE);
                itemHolder.itemLargeInStock.setVisibility(View.VISIBLE);
            } else if (thisItem.getStatus().isNeeded()) {
                itemHolder.itemSmallInStock.setVisibility(View.GONE);
                itemHolder.itemLargeInStock.setVisibility(View.GONE);
                itemHolder.itemSmallPaused.setVisibility(View.GONE);
                itemHolder.itemLargePaused.setVisibility(View.GONE);
                itemHolder.itemSmallNeeded.setVisibility(View.VISIBLE);
                itemHolder.itemLargeNeeded.setVisibility(View.VISIBLE);
            } else if (thisItem.getStatus().isPaused()) {
                itemHolder.itemSmallNeeded.setVisibility(View.GONE);
                itemHolder.itemLargeNeeded.setVisibility(View.GONE);
                itemHolder.itemSmallInStock.setVisibility(View.GONE);
                itemHolder.itemLargeInStock.setVisibility(View.GONE);
                itemHolder.itemSmallPaused.setVisibility(View.VISIBLE);
                itemHolder.itemLargePaused.setVisibility(View.VISIBLE);
            }
            if (thisItem.getStatus().isSelectedInInventory()) {
                itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

            } else {
                if (shopping.itemIsSelectedInInventory && shopping.selectedItemPositionInInventory == position) {
                    itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                    itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);
                } else {
                    itemHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_unselected);
                    itemHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_unselected);
                }
            }

            if (shopping.inventoryView == shopping.INVENTORY_ALL) {

                if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                    itemHolder.itemSmall.setVisibility(View.GONE);
                    itemHolder.itemLarge.setVisibility(View.VISIBLE);
                    itemHolder.triangleRight.setVisibility(View.GONE);
                    itemHolder.triangleDown.setVisibility(View.VISIBLE);
                } else {
                    itemHolder.itemSmall.setVisibility(View.VISIBLE);
                    itemHolder.itemLarge.setVisibility(View.GONE);
                    itemHolder.triangleRight.setVisibility(View.VISIBLE);
                    itemHolder.triangleDown.setVisibility(View.GONE);
                }

            } else if (shopping.inventoryView == shopping.INVENTORY_INSTOCK) {
                if (thisItem.getStatus().isInStock()) {
                    if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.VISIBLE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.VISIBLE);
                    } else {
                        itemHolder.itemSmall.setVisibility(View.VISIBLE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.VISIBLE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    } else {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    } else {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    }
                }

            } else if (shopping.inventoryView == shopping.INVENTORY_NEEDED) {
                if (thisItem.getStatus().isInStock()) {
                    if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    } else {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.VISIBLE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.VISIBLE);

                    } else {
                        itemHolder.itemSmall.setVisibility(View.VISIBLE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.VISIBLE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    } else {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    }
                }

            } else if (shopping.inventoryView == shopping.INVENTORY_PAUSED) {
                if (thisItem.getStatus().isInStock()) {
                    if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    } else {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isNeeded()) {
                    if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    } else {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    }
                } else if (thisItem.getStatus().isPaused()) {
                    if (shopping.getClickedInventoryList().get(adjustedPosition)) {
                        itemHolder.itemSmall.setVisibility(View.GONE);
                        itemHolder.itemLarge.setVisibility(View.VISIBLE);
                        itemHolder.triangleRight.setVisibility(View.GONE);
                        itemHolder.triangleDown.setVisibility(View.VISIBLE);
                    } else {
                        itemHolder.itemSmall.setVisibility(View.VISIBLE);
                        itemHolder.itemLarge.setVisibility(View.GONE);
                        itemHolder.triangleRight.setVisibility(View.VISIBLE);
                        itemHolder.triangleDown.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {

        return (itemData.getItemList().size() + categoryData.getCategoryList().size());
    }

    public static class FullInventoryTitleRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView fullInventoryRvTitle;

        public FullInventoryTitleRVH(View itemView) {

            super(itemView);

            fullInventoryRvTitle = itemView.findViewById(R.id.fullInventoryRvTitle);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public static class FullInventoryRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private FullInventoryRVA adapter;
        private ItemData itemData;
        private CategoryData categoryData;
        private DBStatusHelper dbStatusHelper;
        private DBStoreHelper dbStoreHelper;

        public Button triangleRight;
        public Button triangleDown;
        public LinearLayout itemSmall;
        public LinearLayout itemLarge;
        public TextView itemSmallName;
        public TextView itemLargeName;
        public TextView itemSmallInStock;
        public TextView itemSmallNeeded;
        public TextView itemSmallPaused;
        public TextView itemLargeInStock;
        public TextView itemLargeNeeded;
        public TextView itemLargePaused;
        public TextView itemLargeBrand;
        public TextView itemLargeStore;
        public TextView itemLargeBrandLabel;
        public TextView itemLargeStoreLabel;

        public FullInventoryRVH(final View itemView, Shopping shopping, FullInventoryRVA adapter, ItemData itemData,
                                CategoryData categoryData, DBStatusHelper dbStatus, DBStoreHelper dbStore) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.itemData = itemData;
            this.categoryData = categoryData;
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
            itemLargeStore = itemView.findViewById(R.id.itemLargeStore);
            itemLargeBrandLabel = itemView.findViewById(R.id.itemLargeBrandLabel);
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
            itemLargeStore.setOnClickListener(this);
            itemLargeBrandLabel.setOnClickListener(this);
            itemLargeStoreLabel.setOnClickListener(this);
        }

        private void selectOrUnselectItem(int position) {

            Item thisItem = null;
            String category = null;
            boolean isTitle = false;
            int adjustedPosition = 0;

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

        public Item getItemWithCategories(int position) {

            Item thisItem = null;
            String category = null;
            boolean isTitle = false;
            int adjustedPosition = 0;

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
            return thisItem;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int position = getAdapterPosition();

            Item thisItem = null;
            String category = null;
            boolean isTitle = false;
            int adjustedPosition = 0;

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
                        thisItem.getStatus().setAsClickedInInventory();
                        shopping.getClickedInventoryList().set(adjustedPosition, true);
                    }
                } else if (id == triangleDown.getId()) {
                    if (triangleDown.getVisibility() == View.VISIBLE && triangleRight.getVisibility() == View.GONE) {
                        triangleDown.setVisibility(View.GONE);
                        triangleRight.setVisibility(View.VISIBLE);
                        itemLarge.setVisibility(View.GONE);
                        itemSmall.setVisibility(View.VISIBLE);
                        thisItem.getStatus().setAsUnclickedInInventory();
                        shopping.getClickedInventoryList().set(adjustedPosition, false);
                    }
                } else if (id == itemSmallInStock.getId()) {
                    if (itemSmallInStock.getVisibility() == View.VISIBLE) {
                        itemSmallInStock.setVisibility(View.GONE);
                        itemLargeInStock.setVisibility(View.GONE);
                        itemSmallPaused.setVisibility(View.GONE);
                        itemLargePaused.setVisibility(View.GONE);
                        itemSmallNeeded.setVisibility(View.VISIBLE);
                        itemLargeNeeded.setVisibility(View.VISIBLE);

                        Item item = thisItem;
                        item.getStatus().setAsNeeded();
                        dbStatusHelper.updateStatus(item.getName(), "false", "true", "false");

                        Store thisStore = thisItem.getStore(0);
                        int numItemsNeeded = shopping.getStoreData().getStoreItemsNeededMap().get(thisStore.toString());
                        dbStoreHelper.setStoreItemsNeeded(thisStore.toString(), numItemsNeeded + 1);
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

                        Item item = thisItem;
                        item.getStatus().setAsPaused();
                        dbStatusHelper.updateStatus(item.getName(), "false", "false", "true");

                        Store thisStore = thisItem.getStore(0);
                        int numItemsNeeded = shopping.getStoreData().getStoreItemsNeededMap().get(thisStore.toString());
                        dbStoreHelper.setStoreItemsNeeded(thisStore.toString(), numItemsNeeded - 1);



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

                        Item item = thisItem;
                        item.getStatus().setAsInStock();
                        dbStatusHelper.updateStatus(item.getName(), "true", "false", "false");
                    }
                } else if (id == itemLargeInStock.getId()) {
                    if (itemLargeInStock.getVisibility() == View.VISIBLE) {
                        itemLargeInStock.setVisibility(View.GONE);
                        itemSmallInStock.setVisibility(View.GONE);
                        itemLargePaused.setVisibility(View.GONE);
                        itemSmallPaused.setVisibility(View.GONE);
                        itemLargeNeeded.setVisibility(View.VISIBLE);
                        itemSmallNeeded.setVisibility(View.VISIBLE);

                        Item item = thisItem;
                        item.getStatus().setAsNeeded();
                        dbStatusHelper.updateStatus(item.getName(), "false", "true", "false");

                        Store thisStore = thisItem.getStore(0);
                        int numItemsNeeded = shopping.getStoreData().getStoreItemsNeededMap().get(thisStore.toString());
                        dbStoreHelper.setStoreItemsNeeded(thisStore.toString(), numItemsNeeded + 1);
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

                        Item item = thisItem;
                        item.getStatus().setAsPaused();
                        dbStatusHelper.updateStatus(item.getName(), "false", "false", "true");

                        Store thisStore = thisItem.getStore(0);
                        int numItemsNeeded = shopping.getStoreData().getStoreItemsNeededMap().get(thisStore.toString());
                        dbStoreHelper.setStoreItemsNeeded(thisStore.toString(), numItemsNeeded - 1);
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

                        Item item = thisItem;
                        item.getStatus().setAsInStock();
                        dbStatusHelper.updateStatus(item.getName(), "true", "false", "false");
                    }
                }
            }

        }

    }

}