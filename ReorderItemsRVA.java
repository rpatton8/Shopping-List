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

public class ReorderItemsRVA extends RecyclerView.Adapter<ReorderItemsRVA.ReorderItemsRVH> {

    private Shopping shopping;
    private ItemData itemData;
    private StoreData storeData;
    private DBHelper dbHelper;
    private DBStoreHelper dbStoreHelper;
    private RecyclerView recyclerView;

    private String category;

    public ReorderItemsRVA(Shopping shopping, RecyclerView recyclerView, ItemData itemData, StoreData storeData,
                           DBHelper dbHelper, DBStoreHelper dbStore) {
        this.shopping = shopping;
        this.itemData = itemData;
        this.storeData = storeData;
        this.dbHelper = dbHelper;
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

    public List<Item> getCategoryList() {
        return itemData.getCategoryMap().get(category).getItemList();
    }

    public void swapOrder(int order1, int order2) {
        dbHelper.swapOrder(category, order1, order2);
    }

    public static class ReorderItemsRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        ReorderItemsRVA adapter;
        private ItemData itemData;
        private StoreData storeData;
        private DBStoreHelper dbStoreHelper;
        private RecyclerView recyclerView;

        public TextView name;
        public ImageView arrowDown;
        public ImageView arrowUp;

        public ReorderItemsRVH(final View itemView, Shopping shopping, ReorderItemsRVA adapter, RecyclerView recyclerView,
                               ItemData itemData, StoreData storeData, DBStoreHelper dbStore) {

            super(itemView);
            this.shopping = shopping;
            this.adapter = adapter;
            this.itemData = itemData;
            this.storeData = storeData;
            this.dbStoreHelper = dbStore;
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
                shopping.reorderItemsViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ReorderItems());
            } else if (id == arrowUp.getId()) {
                if (position == 0) {
                    Toast.makeText(shopping, "Up Arrow on first item.", Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.swapOrder(position - 1, position);
                //dbStoreHelper.swapOrder(position - 1, position);
                shopping.updateItemData();
                shopping.reorderItemsViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ReorderItems());
            }
        }

    }

}