package ryan.android.shopping;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ReorderItemsRVA extends RecyclerView.Adapter<ReorderItemsRVA.ReorderItemsRVH> {

    private final Shopping shopping;
    private final ItemData itemData;
    private final CategoryData categoryData;
    private final StoreData storeData;
    private final DBItemHelper dbItemHelper;
    private final RecyclerView recyclerView;
    private final ScrollView scrollView;

    public String reorderBy;
    public static final String REORDER_BY_CATEGORY = "reorder by category";
    public static final String REORDER_BY_STORE = "reorder by store";

    private String category;
    private String store;

    ReorderItemsRVA(Shopping shopping, RecyclerView recyclerView, ScrollView scrollView, ItemData itemData,
                    CategoryData categoryData, StoreData storeData, DBItemHelper dbItemHelper) {
        this.shopping = shopping;
        this.itemData = itemData;
        this.categoryData = categoryData;
        this.storeData =  storeData;
        this.dbItemHelper = dbItemHelper;
        this.recyclerView = recyclerView;
        this.scrollView = scrollView;
        this.category = shopping.reorderItemsCategory;
        this.store = shopping.reorderItemsStore;
        this.reorderBy = REORDER_BY_CATEGORY;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.reorder_items_rv;
    }

    @NonNull
    @Override
    public ReorderItemsRVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ReorderItemsRVH(view, shopping, this, scrollView, recyclerView, itemData, categoryData, storeData);
    }

    @Override
    public void onBindViewHolder(@NonNull ReorderItemsRVH holder, int position) {

        if (reorderBy.equals(REORDER_BY_CATEGORY)) {

            if (!category.equals("")) {
                ArrayList<Item> categoryList = itemData.getCategoryMap().get(category).getItemList();
                holder.itemName.setText(categoryList.get(position).getName());
            }

        } else if (reorderBy.equals(REORDER_BY_STORE)) {

            if (!store.equals("")) {
                ArrayList<Item> storeList = itemData.getStoreMap().get(store).getItemList();
                holder.itemName.setText(storeList.get(position).getName());
            }
        }
    }

    @Override
    public int getItemCount() {
        switch (reorderBy) {
            case REORDER_BY_CATEGORY:
                return categoryData.getCategoryList().size();
            case REORDER_BY_STORE:
                return storeData.getStoreList().size();
            default:
                return -1;
        }
    }

    public void changeCategory(String category) {
        this.category = category;
        shopping.reorderItemsCategory = category;
    }

    public void changeStore(String store) {
        this.store = store;
        shopping.reorderItemsStore = store;
    }

    public List<Item> getCategoryList() {
        return itemData.getCategoryMap().get(category).getItemList();
    }

    public List<Item> getStoreList() {
        return itemData.getStoreMap().get(store).getItemList();
    }

    public void swapOrder(int order1, int order2) {
        dbItemHelper.swapOrderByCategory(category, order1, order2);
    }

    public static class ReorderItemsRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Shopping shopping;
        private final ReorderItemsRVA adapter;
        private final RecyclerView recyclerView;
        private final ScrollView scrollView;
        private final ItemData itemData;
        private final CategoryData categoryData;
        private final StoreData storeData;

        final TextView itemName;
        final ImageView arrowDown;
        final ImageView arrowUp;

        ReorderItemsRVH(final View itemView, Shopping shopping, ReorderItemsRVA adapter, ScrollView scrollView, RecyclerView recyclerView,
                        ItemData itemData, CategoryData categoryData, StoreData storeData) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.recyclerView = recyclerView;
            this.scrollView = scrollView;
            this.itemData = itemData;
            this.categoryData = categoryData;
            this.storeData = storeData;

            itemName = itemView.findViewById(R.id.itemName);
            arrowDown = itemView.findViewById(R.id.arrowDown);
            arrowUp = itemView.findViewById(R.id.arrowUp);

            arrowDown.setOnClickListener(this);
            arrowUp.setOnClickListener(this);
        }

        boolean categoryContains(int categoryPosition, int itemPosition) {
            int index = 0;
            for (int i = 0; i < categoryData.getCategoryList().size(); i++) {
                String category = categoryData.getCategoryList().get(i);
                int numItemsInCategory;
                if (itemData.getCategoryMap().get(category) == null) {
                    numItemsInCategory = 0;
                } else {
                    numItemsInCategory = itemData.getCategoryMap().get(category).getItemList().size();
                }
                System.out.println(category + " has " + numItemsInCategory + " items.");
                index += numItemsInCategory + 1;
                if (itemPosition == index) return false;
                if (index > itemPosition) {
                    return categoryPosition == i;
                }
            }
            return false;
        }

        boolean storeContains(int storePosition, int itemPosition) {
            int index = 0;
            for (int i = 0; i < storeData.getStoreList().size(); i++) {
                String store = storeData.getStoreList().get(i);
                int numItemsInStore;
                if (itemData.getStoreMap().get(store) == null) {
                    numItemsInStore = 0;
                } else {
                    numItemsInStore = itemData.getStoreMap().get(store).getItemList().size();
                }
                System.out.println(store + " has " + numItemsInStore + " items.");
                index += numItemsInStore + 1;
                if (itemPosition == index) return false;
                if (index > itemPosition) {
                    return storePosition == i;
                }
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int position = getAdapterPosition();
            if (id == arrowDown.getId()) {
                if (position == adapter.getCategoryList().size() - 1) {
                    Toast.makeText(shopping, "Down Arrow on last item.", Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.swapOrder(position, position + 1);
                //dbStoreHelper.swapOrder(position, position + 1);
                shopping.updateItemData();
                shopping.reorderItemsRecyclerViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                //shopping.reorderItemsScrollViewState = Objects.requireNonNull(scrollView.getLayoutManager()).onSaveInstanceState();
                //outState.putInt("SCROLL_POS", scrollView.getScrollY());
                shopping.loadFragment(new ReorderItems());
            } else if (id == arrowUp.getId()) {
                if (position == 0) {
                    Toast.makeText(shopping, "Up Arrow on first item.", Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.swapOrder(position - 1, position);
                //dbStoreHelper.swapOrder(position - 1, position);
                shopping.updateItemData();
                shopping.reorderItemsRecyclerViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                //shopping.reorderItemsScrollViewState = Objects.requireNonNull(scrollView.getLayoutManager()).onSaveInstanceState();
                shopping.loadFragment(new ReorderItems());
            }
        }
    }
}