package ryan.android.shopping;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

class ReorderStoresRVA extends RecyclerView.Adapter<ReorderStoresRVA.ReorderStoresRVH> {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private StoreData storeData;
    private DBStoreHelper dbStoreHelper;
    private RecyclerView recyclerView;

    ReorderStoresRVA(View view, Shopping shopping, RecyclerView recyclerView, ItemData itemData, StoreData storeData, DBStoreHelper dbStore) {
        setView(view);
        setShopping(shopping);
        setItemData(itemData) ;
        setStoreData(storeData);
        setDbStoreHelper(dbStore);
        setRecyclerView(recyclerView);
    }

    private ReorderStoresRVA getThis() {
        return this;
    }

    private View getView() {
        return view;
    }

    private void setView(View view) {
        getThis().view = view;
    }

    private Shopping getShopping() {
        return shopping;
    }

    private void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    private ItemData getItemData() {
        return itemData;
    }

    private void setItemData(ItemData itemData) {
        getThis().itemData = itemData;
    }

    private StoreData getStoreData() {
        return storeData;
    }

    private void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
    }

    private DBStoreHelper getDbStoreHelper() {
        return dbStoreHelper;
    }

    private void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
        getThis().dbStoreHelper = dbStoreHelper;
    }

    private RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        getThis().recyclerView = recyclerView;
    }

    public int getItemViewType(int position) {
        return R.layout.reorder_stores_rv;
    }

    public ReorderStoresRVH onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        return new ReorderStoresRVH(getView(), getShopping(), getRecyclerView(), getItemData(), getStoreData(), getDbStoreHelper());
    }

    public void onBindViewHolder(ReorderStoresRVH holder, int position) {
        ArrayList<String> storeList = getStoreData().getStoreList();
        if (position >= 0 && position < storeList.size()) {
            holder.getStoreName().setText(storeList.get(position));
        }
    }

    public int getItemCount() {
        return getStoreData().getStoreList().size();
    }

    static class ReorderStoresRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private ItemData itemData;
        private StoreData storeData;
        private DBStoreHelper dbStoreHelper;
        private RecyclerView recyclerView;

        private TextView storeName;
        private ImageView triangleDown;
        private ImageView triangleUp;

        ReorderStoresRVH(View itemView, Shopping shopping, RecyclerView recyclerView,
                         ItemData itemData, StoreData storeData, DBStoreHelper dbStore) {

            super(itemView);
            setShopping(shopping);
            setItemData(itemData);
            setStoreData(storeData);
            setDbStoreHelper(dbStore);
            setRecyclerView(recyclerView);

            setStoreName(itemView.findViewById(R.id.storeName));
            setTriangleDown(itemView.findViewById(R.id.triangleDown));
            setTriangleUp(itemView.findViewById(R.id.triangleUp));

            getTriangleDown().setOnClickListener(getThis());
            getTriangleUp().setOnClickListener(getThis());

        }

        private ReorderStoresRVH getThis() {
            return this;
        }

        private Shopping getShopping() {
            return shopping;
        }

        private void setShopping(Shopping shopping) {
            getThis().shopping = shopping;
        }

        private ItemData getItemData() {
            return itemData;
        }

        private void setItemData(ItemData itemData) {
            getThis().itemData = itemData;
        }

        private StoreData getStoreData() {
            return storeData;
        }

        private void setStoreData(StoreData storeData) {
            getThis().storeData = storeData;
        }

        private DBStoreHelper getDbStoreHelper() {
            return dbStoreHelper;
        }

        private void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
            getThis().dbStoreHelper = dbStoreHelper;
        }

        private RecyclerView getRecyclerView() {
            return recyclerView;
        }

        private void setRecyclerView(RecyclerView recyclerView) {
            getThis().recyclerView = recyclerView;
        }

        private TextView getStoreName() {
            return storeName;
        }

        private void setStoreName(TextView storeName) {
            getThis().storeName = storeName;
        }

        private ImageView getTriangleDown() {
            return triangleDown;
        }

        private void setTriangleDown(ImageView triangleDown) {
            getThis().triangleDown = triangleDown;
        }

        private ImageView getTriangleUp() {
            return triangleUp;
        }

        private void setTriangleUp(ImageView triangleUp) {
            getThis().triangleUp = triangleUp;
        }

        private boolean storeContains(int storePosition, int itemPosition) {
            int index = 0;
            for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                String store = getStoreData().getStoreList().get(i);
                int numItemsInStore;
                if (getItemData().getStoreMap().get(store) == null) {
                    numItemsInStore = 0;
                } else {
                    numItemsInStore = getItemData().getStoreMap().get(store).getStoreItemsList().size();
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
            int position = getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            if (id == getTriangleDown().getId()) {
                if (position == getStoreData().getStoreList().size() - 1) {
                    // Down arrow on last store
                    return;
                }
                getDbStoreHelper().swapOrder(position, position + 1);
                getShopping().updateStoreData();

                // Check if selected item is in one of the stores being moved and shift it
                if (getShopping().getSelectedItemPositionInInventory() != 0) {

                    if (storeContains(position, getShopping().getSelectedItemPositionInInventory())) {

                        String store = getStoreData().getStoreList().get(position + 1);
                        int offset = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                        getShopping().setSelectedItemPositionInInventory(getShopping().getSelectedItemPositionInInventory() + offset + 1);

                    } else if (storeContains(position + 1, getShopping().getSelectedItemPositionInInventory())) {

                        String store = getStoreData().getStoreList().get(position);
                        int offset = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                        getShopping().setSelectedItemPositionInInventory(getShopping().getSelectedItemPositionInInventory() - offset + 1);

                    }
                }
                getShopping().setReorderStoresViewState(getRecyclerView().getLayoutManager().onSaveInstanceState());
                getShopping().loadFragment(new ReorderStores());

            } else if (id == getTriangleUp().getId()) {
                if (position == 0) {
                    // Up arrow on first item
                    return;
                }
                getDbStoreHelper().swapOrder(position - 1, position);
                getShopping().updateStoreData();

                // Check if selected item is in one of the stores being moved and shift it
                if (getShopping().getSelectedItemPositionInInventory() != 0) {

                    if (storeContains(position, getShopping().getSelectedItemPositionInInventory())) {

                        String store = getStoreData().getStoreList().get(position - 1);
                        int offset = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                        getShopping().setSelectedItemPositionInInventory(getShopping().getSelectedItemPositionInInventory() - offset + 1);

                    } else if (storeContains(position - 1, getShopping().getSelectedItemPositionInInventory())) {

                        String store = getStoreData().getStoreList().get(position);
                        int offset = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                        getShopping().setSelectedItemPositionInInventory(getShopping().getSelectedItemPositionInInventory() + offset + 1);
                    }
                }
                getShopping().setReorderStoresViewState(getRecyclerView().getLayoutManager().onSaveInstanceState());
                getShopping().loadFragment(new ReorderStores());
            }
        }
    }
}