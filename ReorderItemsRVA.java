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

public class ReorderItemsRVA extends RecyclerView.Adapter<ReorderItemsRVA.ReorderItemsRVH> {

    private Shopping shopping;
    private ItemData itemData;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBItemHelper dbItemHelper;
    private RecyclerView recyclerView;
    private ScrollView scrollView;

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
        return new ReorderItemsRVH(view, shopping, this, scrollView, recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReorderItemsRVH holder, int position) {

        //ArrayList<Item> categoryList = itemData.getCategoryMap().get(category).getItemList();
        //holder.name.setText(categoryList.get(position).getName());

        //holder.name.setText(categoryData.getCategoryList().get(position));

        if (reorderBy == REORDER_BY_CATEGORY) {
            if (category == null) System.out.println("category is null");
            else System.out.println("category = " + category);
            ArrayList<Item> categoryList = itemData.getCategoryMap().get(category).getItemList();
            holder.itemName.setText(categoryList.get(position).getName());
            //holder.name.setText(categoryData.getCategoryList().get(position));
            //String category = categoryData.getCategoryList().get(position);
            //itemData.getCategoryMap().get(category).getItemList()

            //holder.name.setText(itemData.getCategoryMap().get(category).getItemList().get(position).getName());

        } else if (reorderBy == REORDER_BY_STORE) {
            ArrayList<Item> storeList = itemData.getStoreMap().get(store).getItemList();
            holder.itemName.setText(storeList.get(position).getName());
            //holder.name.setText(storeData.getStoreList().get(position));
        }


        /*System.out.println("ScrollView  y-cord = " + scrollView.getScrollY());
        if (scrollView.getScrollY() <= 200) {
            recyclerView.setNestedScrollingEnabled(false);
        } else if (scrollView.getScrollY() > 200) {
            recyclerView.setNestedScrollingEnabled(true);
        }*/

    }

    @Override
    public int getItemCount() {
        if (reorderBy == REORDER_BY_CATEGORY) {
            return categoryData.getCategoryList().size();
        } else if (reorderBy == REORDER_BY_STORE) {
            return storeData.getStoreList().size();
        } else return -1;

        //System.out.println("item count = " + itemData.getCategoryMap().get(category).getItemList().size());
        /*if (itemData.getCategoryMap().get(category) == null) {
            System.out.println("category is null");
            return 0;
        } else {
            System.out.println("category is not null");
            return itemData.getCategoryMap().get(category).getItemList().size();
        }*/
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

        private Shopping shopping;
        private ReorderItemsRVA adapter;
        private RecyclerView recyclerView;
        private ScrollView scrollView;

        public TextView itemName;
        public ImageView arrowDown;
        public ImageView arrowUp;

        ReorderItemsRVH(final View itemView, Shopping shopping, ReorderItemsRVA adapter, ScrollView scrollView, RecyclerView recyclerView) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.recyclerView = recyclerView;
            this.scrollView = scrollView;

            itemName = itemView.findViewById(R.id.itemName);
            arrowDown = itemView.findViewById(R.id.arrowDown);
            arrowUp = itemView.findViewById(R.id.arrowUp);

            arrowDown.setOnClickListener(this);
            arrowUp.setOnClickListener(this);

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