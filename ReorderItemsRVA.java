package ryan.android.shopping;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReorderItemsRVA extends RecyclerView.Adapter<ReorderItemsRVA.ReorderItemsRVH> {

    private final Shopping shopping;
    private final ItemData itemData;
    private final StoreData storeData;
    private final DBItemHelper dbItemHelper;
    private final DBStoreHelper dbStoreHelper;
    private final RecyclerView recyclerView;

    private String category;
    private String store;

    ReorderItemsRVA(Shopping shopping, RecyclerView recyclerView, ItemData itemData, StoreData storeData,
                    DBItemHelper dbItemHelper, DBStoreHelper dbStore) {
        this.shopping = shopping;
        this.itemData = itemData;
        this.storeData = storeData;
        this.dbItemHelper = dbItemHelper;
        this.dbStoreHelper = dbStore;
        this.recyclerView = recyclerView;
        this.category = shopping.reorderItemsCategory;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.reorder_rv;
    }

    @NonNull
    @Override
    public ReorderItemsRVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ReorderItemsRVH(view, shopping, this, recyclerView, itemData, storeData, dbStoreHelper);
    }

    @Override
    public void onBindViewHolder(@NonNull ReorderItemsRVH holder, int position) {

        ArrayList<Item> categoryList = itemData.getCategoryMap().get(category).getItemList();
        holder.name.setText(categoryList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        if (itemData.getCategoryMap().get(category) == null) return 0;
        else return itemData.getCategoryMap().get(category).getItemList().size();
    }

    public void changeCategory(String category) {
        this.category = category;
        shopping.reorderItemsCategory = category;
    }

    private List<Item> getCategoryList() {
        return itemData.getCategoryMap().get(category).getItemList();
    }

    private void swapOrder(int order1, int order2) {
        dbItemHelper.swapOrderByCategory(category, order1, order2);
    }

    public static class ReorderItemsRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Shopping shopping;
        private final ReorderItemsRVA adapter;
        private final RecyclerView recyclerView;

        final TextView name;
        final ImageView arrowDown;
        final ImageView arrowUp;

        ReorderItemsRVH(final View itemView, Shopping shopping, ReorderItemsRVA adapter, RecyclerView recyclerView,
                               ItemData itemData, StoreData storeData, DBStoreHelper dbStore) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.recyclerView = recyclerView;

            name = itemView.findViewById(R.id.name);
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
                shopping.reorderItemsViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                shopping.loadFragment(new ReorderItems());
            } else if (id == arrowUp.getId()) {
                if (position == 0) {
                    Toast.makeText(shopping, "Up Arrow on first item.", Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.swapOrder(position - 1, position);
                //dbStoreHelper.swapOrder(position - 1, position);
                shopping.updateItemData();
                shopping.reorderItemsViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                shopping.loadFragment(new ReorderItems());
            }
        }
    }
}