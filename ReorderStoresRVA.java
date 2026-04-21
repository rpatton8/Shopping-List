package ryan.android.shopping;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

//@SuppressWarnings("ALL")
class ReorderStoresRVA extends RecyclerView.Adapter<ReorderStoresRVA.ReorderStoresRVH> {

    private Shopping shopping;
    private ItemData itemData;
    private StoreData storeData;
    private DBStoreHelper dbStoreHelper;
    private RecyclerView recyclerView;

    ReorderStoresRVA(Shopping shopping, RecyclerView recyclerView, ItemData itemData, StoreData storeData, DBStoreHelper dbStore) {
        this.shopping = shopping;
        this.itemData  = itemData;
        this.storeData = storeData;
        this.dbStoreHelper = dbStore;
        this.recyclerView = recyclerView;
    }

    public int getItemViewType(int position) {
        return R.layout.reorder_stores_rv;
    }

    public ReorderStoresRVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ReorderStoresRVH(view, shopping, recyclerView, itemData, storeData, dbStoreHelper);
    }

    public void onBindViewHolder(ReorderStoresRVH holder, int position) {
        ArrayList<String> storeList = storeData.getStoreList();
        holder.storeName.setText(storeList.get(position));
    }

    public int getItemCount() {
        return storeData.getStoreList().size();
    }

    static class ReorderStoresRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private ItemData itemData;
        private StoreData storeData;
        private DBStoreHelper dbStoreHelper;
        private RecyclerView recyclerView;

        private TextView storeName;
        private ImageView arrowDown;
        private ImageView arrowUp;

        ReorderStoresRVH(View itemView, Shopping shopping, RecyclerView recyclerView,
                         ItemData itemData, StoreData storeData, DBStoreHelper dbStore) {

            super(itemView);
            this.shopping = shopping;
            this.itemData = itemData;
            this.storeData = storeData;
            this.dbStoreHelper = dbStore;
            this.recyclerView = recyclerView;

            storeName = itemView.findViewById(R.id.storeName);
            arrowDown = itemView.findViewById(R.id.arrowDown);
            arrowUp = itemView.findViewById(R.id.arrowUp);

            arrowDown.setOnClickListener(this);
            arrowUp.setOnClickListener(this);

        }

        private boolean storeContains(int storePosition, int itemPosition) {
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

        public void onClick(View v) {
            int id = v.getId();
            int position = getAdapterPosition();
            if (id == arrowDown.getId()) {
                if (position == storeData.getStoreList().size() - 1) {
                    // Down arrow on last item
                    return;
                }
                dbStoreHelper.swapOrder(position, position + 1);
                shopping.updateStoreData();

                // Check if selected item is in one of the stores being moved and shift it
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
                shopping.reorderStoresViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ReorderStores());

            } else if (id == arrowUp.getId()) {
                if (position == 0) {
                    // Up arrow on first item
                    return;
                }
                dbStoreHelper.swapOrder(position - 1, position);
                shopping.updateStoreData();

                // Check if selected item is in one of the stores being moved and shift it
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
                shopping.reorderStoresViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ReorderStores());
            }
        }
    }
}