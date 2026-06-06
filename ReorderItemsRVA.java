package ryan.android.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

class ReorderItemsRVA extends RecyclerView.Adapter<ReorderItemsRVA.ReorderItemsRVH> {

    private Shopping shopping;
    private Context context;
    private ItemData itemData;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBItemHelper dbItemHelper;
    private RecyclerView recyclerView;
    private ScrollView scrollView;

    String reorderBy;
    static final String REORDER_BY_CATEGORY = ShoppingApp.getStringRes(R.string.cvReorderByCategory);
    static final String REORDER_BY_STORE = ShoppingApp.getStringRes(R.string.cvReorderByStore);

    private String category;
    private String store;

    ReorderItemsRVA(Shopping shopping, Context context, RecyclerView recyclerView, ScrollView scrollView,
                    ItemData itemData, CategoryData categoryData, StoreData storeData, DBItemHelper dbItemHelper) {
        this.shopping = shopping;
        this.context = context;
        this.itemData = itemData;
        this.categoryData = categoryData;
        this.storeData =  storeData;
        this.dbItemHelper = dbItemHelper;
        this.recyclerView = recyclerView;
        this.scrollView = scrollView;
        this.category = shopping.getReorderItemsCategory();
        this.store = shopping.getReorderItemsStore();
        this.reorderBy = REORDER_BY_CATEGORY;
    }

    private Context getContext() {
        return context;
    }

    public int getItemViewType(int position) {
        return R.layout.reorder_items_rv;
    }

    public ReorderItemsRVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ReorderItemsRVH(view, shopping, this, scrollView, recyclerView, itemData, categoryData, storeData);
    }

    public void onBindViewHolder(ReorderItemsRVH holder, int position) {

        if (reorderBy.equals(REORDER_BY_CATEGORY)) {

            if (!category.equals(getContext().getString(R.string.emptyString))) {
                ArrayList<Item> categoryList = itemData.getCategoryMap().get(category).getCategoryItemsList();
                holder.itemName.setText(categoryList.get(position).getName());
            }

        } else if (reorderBy.equals(REORDER_BY_STORE)) {

            if (!store.equals(getContext().getString(R.string.emptyString))) {
                ArrayList<Item> storeList = itemData.getStoreMap().get(store).getStoreItemsList();
                holder.itemName.setText(storeList.get(position).getName());
            }
        }
    }

    public int getItemCount() {
        if (reorderBy.equals(REORDER_BY_CATEGORY)) {
            return categoryData.getCategoryList().size();
        } else if (reorderBy.equals(REORDER_BY_STORE)) {
            return storeData.getStoreList().size();
        } else return -1;
    }

    public void changeCategory(String category) {
        this.category = category;
        shopping.setReorderItemsCategory(category);
    }

    public void changeStore(String store) {
        this.store = store;
        shopping.setReorderItemsStore(store);
    }

    private List<Item> getCategoryList() {
        return itemData.getCategoryMap().get(category).getCategoryItemsList();
    }

    private List<Item> getStoreList() {
        return itemData.getStoreMap().get(store).getStoreItemsList();
    }

    private void swapOrderByCategory(int order1, int order2) {
        dbItemHelper.swapOrderByCategory(category, order1, order2);
    }

    private void swapOrderByStore(int order1, int order2) {
        dbItemHelper.swapOrderByStore(category, order1, order2);
    }

    class ReorderItemsRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private ReorderItemsRVA adapter;
        private RecyclerView recyclerView;
        private ScrollView scrollView;
        private ItemData itemData;
        private CategoryData categoryData;
        private StoreData storeData;

        private TextView itemName;
        private ImageView triangleDown;
        private ImageView triangleUp;

        ReorderItemsRVH(View itemView, Shopping shopping, ReorderItemsRVA adapter, ScrollView scrollView, RecyclerView recyclerView,
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
            triangleDown = itemView.findViewById(R.id.triangleDown);
            triangleUp = itemView.findViewById(R.id.triangleUp);

            triangleDown.setOnClickListener(this);
            triangleUp.setOnClickListener(this);
        }

        private boolean categoryContains(int categoryPosition, int itemPosition) {
            int index = 0;
            for (int i = 0; i < categoryData.getCategoryList().size(); i++) {
                String category = categoryData.getCategoryList().get(i);
                int numItemsInCategory;
                if (itemData.getCategoryMap().get(category) == null) {
                    numItemsInCategory = 0;
                } else {
                    numItemsInCategory = itemData.getCategoryMap().get(category).getCategoryItemsList().size();
                }
                index += numItemsInCategory + 1;
                if (itemPosition == index) return false;
                if (index > itemPosition) {
                    return categoryPosition == i;
                }
            }
            return false;
        }

        private boolean storeContains(int storePosition, int itemPosition) {
            int index = 0;
            for (int i = 0; i < storeData.getStoreList().size(); i++) {
                String store = storeData.getStoreList().get(i);
                int numItemsInStore;
                if (itemData.getStoreMap().get(store) == null) {
                    numItemsInStore = 0;
                } else {
                    numItemsInStore = itemData.getStoreMap().get(store).getStoreItemsList().size();
                }
                index += numItemsInStore + 1;
                if (itemPosition == index) return false;
                if (index > itemPosition) {
                    return storePosition == i;
                }
            }
            return false;
        }

        public void onClick(View v) {
            int id = v.getId();
            int position = getAdapterPosition();
            if (id == triangleDown.getId()) {
                if (position == adapter.getCategoryList().size() - 1) {
                    // Down arrow on last item
                    return;
                }
                adapter.swapOrderByCategory(position, position + 1);
                //dbStoreHelper.swapOrder(position, position + 1);
                shopping.updateItemData();
                shopping.setReorderItemsRecyclerViewState(recyclerView.getLayoutManager().onSaveInstanceState());
                //shopping.reorderItemsScrollViewState = scrollView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ReorderItems());
            } else if (id == triangleUp.getId()) {
                if (position == 0) {
                    // Up Arrow on first item
                    return;
                }
                adapter.swapOrderByCategory(position - 1, position);
                //dbStoreHelper.swapOrder(position - 1, position);
                shopping.updateItemData();
                shopping.setReorderItemsRecyclerViewState(recyclerView.getLayoutManager().onSaveInstanceState());
                //shopping.reorderItemsScrollViewState = scrollView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ReorderItems());
            }
        }
    }
}