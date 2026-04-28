package ryan.android.shopping;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

//@SuppressWarnings("ALL")
class SearchInventoryRVA extends RecyclerView.Adapter  {

    private Shopping shopping;
    private SearchAlgorithm searchAlgorithm;
    private String currentTerm;
    private ArrayList<Item> searchResultsList;

    SearchInventoryRVA(Shopping shopping, SearchAlgorithm searchAlgorithm) {
        this.shopping = shopping;
        this.searchAlgorithm = searchAlgorithm;
        currentTerm = shopping.currentSearchTerm;
    }

    public int getItemViewType(int position) {
        return R.layout.search_inventory_rv;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new SearchInventoryRVH(view, shopping, this);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Item thisItem = null;
        if (!currentTerm.equals("")) {
            searchResultsList = searchAlgorithm.getSearchResults(currentTerm);
            thisItem = searchResultsList.get(position);
        }

        SearchInventoryRVH searchResultsHolder = (SearchInventoryRVH) holder;

        if (thisItem.getStatus().isExpandedInInventory()) {
            searchResultsHolder.itemSmallName.setText(thisItem.getName());
            searchResultsHolder.itemSmallBrand.setText(thisItem.getBrandType());
            searchResultsHolder.itemLargeName.setText(thisItem.getName());
            searchResultsHolder.itemLargeBrand.setText(thisItem.getBrandType());
            searchResultsHolder.itemLargeCategory.setText(thisItem.getCategory().toString());
            searchResultsHolder.itemLargeStore.setText(thisItem.getStore().toString());
            searchResultsHolder.triangleRight.setVisibility(View.GONE);
            searchResultsHolder.triangleDown.setVisibility(View.VISIBLE);
            searchResultsHolder.itemSmall.setVisibility(View.GONE);
            searchResultsHolder.itemLarge.setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isContractedInInventory()) {
            searchResultsHolder.itemSmallName.setText(thisItem.getName());
            searchResultsHolder.itemSmallBrand.setText(thisItem.getBrandType());
            searchResultsHolder.itemLargeName.setText(thisItem.getName());
            searchResultsHolder.itemLargeBrand.setText(thisItem.getBrandType());
            searchResultsHolder.itemLargeCategory.setText(thisItem.getCategory().toString());
            searchResultsHolder.itemLargeStore.setText(thisItem.getStore().toString());
            searchResultsHolder.triangleDown.setVisibility(View.GONE);
            searchResultsHolder.triangleRight.setVisibility(View.VISIBLE);
            searchResultsHolder.itemLarge.setVisibility(View.GONE);
            searchResultsHolder.itemSmall.setVisibility(View.VISIBLE);
        }

        if (thisItem.getStatus().isInStock()) {
            searchResultsHolder.itemSmallPaused.setVisibility(View.GONE);
            searchResultsHolder.itemLargePaused.setVisibility(View.GONE);
            searchResultsHolder.itemSmallNeeded.setVisibility(View.GONE);
            searchResultsHolder.itemLargeNeeded.setVisibility(View.GONE);
            searchResultsHolder.itemSmallInStock.setVisibility(View.VISIBLE);
            searchResultsHolder.itemLargeInStock.setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isNeeded()) {
            searchResultsHolder.itemSmallInStock.setVisibility(View.GONE);
            searchResultsHolder.itemLargeInStock.setVisibility(View.GONE);
            searchResultsHolder.itemSmallPaused.setVisibility(View.GONE);
            searchResultsHolder.itemLargePaused.setVisibility(View.GONE);
            searchResultsHolder.itemSmallNeeded.setVisibility(View.VISIBLE);
            searchResultsHolder.itemLargeNeeded.setVisibility(View.VISIBLE);
        } else if (thisItem.getStatus().isPaused()) {
            searchResultsHolder.itemSmallNeeded.setVisibility(View.GONE);
            searchResultsHolder.itemLargeNeeded.setVisibility(View.GONE);
            searchResultsHolder.itemSmallInStock.setVisibility(View.GONE);
            searchResultsHolder.itemLargeInStock.setVisibility(View.GONE);
            searchResultsHolder.itemSmallPaused.setVisibility(View.VISIBLE);
            searchResultsHolder.itemLargePaused.setVisibility(View.VISIBLE);
        }

        if (thisItem.getStatus().isSelectedInInventory()) {
            searchResultsHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
            searchResultsHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);

        } else {
            if (shopping.itemIsSelectedInInventory && shopping.selectedItemPositionInInventory == position) {
                searchResultsHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_selected);
                searchResultsHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_selected);
            } else {
                searchResultsHolder.itemSmall.setBackgroundResource(R.drawable.list_outline_unselected);
                searchResultsHolder.itemLarge.setBackgroundResource(R.drawable.list_outline_unselected);
            }
        }
    }

    public int getItemCount() {
        return searchAlgorithm.numSearchResults(currentTerm);
    }

    public void setCurrentTerm(String term)   {
        this.currentTerm = term;
    }

    private class SearchInventoryRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
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

        private SearchInventoryRVH(View itemView, Shopping shopping, SearchInventoryRVA adapter) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.itemData = shopping.getItemData();
            this.categoryData  = shopping.getCategoryData();
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
            itemSmallBrand = itemView.findViewById(R.id.itemSmallBrand);
            itemSmallBrandLabel = itemView.findViewById(R.id.itemSmallBrandLabel);
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
            itemSmallBrand.setOnClickListener(this);
            itemSmallBrandLabel.setOnClickListener(this);
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
            searchResultsList.get(position);

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