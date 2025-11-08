package ryan.android.shopping;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Objects;

public class ReorderStoresRVA extends RecyclerView.Adapter<ReorderStoresRVA.ReorderStoresRVH> {

    private final Shopping shopping;
    private final ItemData itemData;
    private final StoreData storeData;
    private final DBStoreHelper dbStoreHelper;
    private final RecyclerView recyclerView;

    ReorderStoresRVA(Shopping shopping, RecyclerView recyclerView, ItemData itemData, StoreData storeData, DBStoreHelper dbStore) {
        this.shopping = shopping;
        this.itemData = itemData;
        this.storeData = storeData;
        this.dbStoreHelper = dbStore;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.reorder_rv;
    }

    @NonNull
    @Override
    public ReorderStoresRVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ReorderStoresRVH(view, shopping, recyclerView, itemData, storeData, dbStoreHelper);
    }

    @Override
    public void onBindViewHolder(@NonNull ReorderStoresRVH holder, int position) {

        ArrayList<String> storeList = storeData.getStoreList();
        holder.name.setText(storeList.get(position));

    }

    @Override
    public int getItemCount() {
        return storeData.getStoreList().size();
    }

    public static class ReorderStoresRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Shopping shopping;
        private final ItemData itemData;
        private final StoreData storeData;
        private final DBStoreHelper dbStoreHelper;
        private final RecyclerView recyclerView;

        final TextView name;
        final ImageView arrowDown;
        final ImageView arrowUp;

        ReorderStoresRVH(final View itemView, Shopping shopping, RecyclerView recyclerView,
                         ItemData itemData, StoreData storeData, DBStoreHelper dbStore) {

            super(itemView);
            this.shopping = shopping;
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

        boolean storeContains(int storePosition, int itemPosition) {
            int index = 0;
            for (int i = 0; i < storeData.getStoreList().size(); i++) {
                String store = storeData.getStoreList().get(i);
                System.out.println("Store: " + store);
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
                    return (storePosition == i);
                }
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int position = getAdapterPosition();
            if (id == arrowDown.getId()) {
                if (position == storeData.getStoreList().size() - 1) {
                    //Toast.makeText(shopping, "Down Arrow on last item.", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbStoreHelper.swapOrder(position, position + 1);
                shopping.updateStoreData();

                // Check if selected item is in one of the categories being moved and shift it
                if (shopping.selectedItemPositionInInventory != 0) {

                    if (storeContains(position, shopping.selectedItemPositionInInventory)) {

                        String store = storeData.getStoreList().get(position + 1);
                        int offset = itemData.getStoreMap().get(store).getItemList().size();
                        shopping.selectedItemPositionInInventory += offset + 1;

                    } else if (storeContains(position + 1, shopping.selectedItemPositionInInventory)) {

                        String store = storeData.getStoreList().get(position);
                        int offset = itemData.getStoreMap().get(store).getItemList().size();
                        shopping.selectedItemPositionInInventory -= offset + 1;

                    }
                }
                shopping.reorderCategoriesViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                shopping.loadFragment(new ReorderCategories());

            } else if (id == arrowUp.getId()) {
                if (position == 0) {
                    //Toast.makeText(shopping, "Up Arrow on first item.", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbStoreHelper.swapOrder(position - 1, position);
                shopping.updateStoreData();

                // Check if selected item is in one of the categories being moved and shift it
                if (shopping.selectedItemPositionInInventory != 0) {

                    if (storeContains(position, shopping.selectedItemPositionInInventory)) {

                        String store = storeData.getStoreList().get(position - 1);
                        int offset = itemData.getStoreMap().get(store).getItemList().size();
                        shopping.selectedItemPositionInInventory -= offset + 1;

                    } else if (storeContains(position - 1, shopping.selectedItemPositionInInventory)) {

                        String store = storeData.getStoreList().get(position);
                        int offset = itemData.getStoreMap().get(store).getItemList().size();
                        shopping.selectedItemPositionInInventory += offset + 1;

                    }
                }
                shopping.reorderCategoriesViewState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
                shopping.loadFragment(new ReorderCategories());
            }
        }
    }
}