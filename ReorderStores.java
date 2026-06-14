package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ReorderStores extends Fragment {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private StoreData storeData;
    private DBStoreHelper dbStoreHelper;
    private RecyclerView recyclerView;
    private ReorderStoresRVA rvAdapter;
    private Button finishReorderingButton;
    private Button cancelButton;

    public ReorderStores() {}

    private ReorderStores getThis() {
        return this;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        getThis().view = view;
    }

    public Shopping getShopping() {
        return shopping;
    }

    public void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        getThis().itemData = itemData;
    }

    public StoreData getStoreData() {
        return storeData;
    }

    public void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
    }

    public DBStoreHelper getDbStoreHelper() {
        return dbStoreHelper;
    }

    public void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
        getThis().dbStoreHelper = dbStoreHelper;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        getThis().recyclerView = recyclerView;
    }

    public ReorderStoresRVA getRvAdapter() {
        return rvAdapter;
    }

    public void setRvAdapter(ReorderStoresRVA rvAdapter) {
        getThis().rvAdapter = rvAdapter;
    }

    public Button getFinishReorderingButton() {
        return finishReorderingButton;
    }

    public void setFinishReorderingButton(Button finishReorderingButton) {
        getThis().finishReorderingButton = finishReorderingButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.reorder_stores, container, false));

        setShopping((Shopping) getActivity());
        setItemData(getShopping().getItemData());
        setStoreData(getShopping().getStoreData());
        setDbStoreHelper(new DBStoreHelper(getActivity()));

        setRecyclerView((RecyclerView) getView().findViewById(R.id.reorderStoresRecyclerView));
        getRecyclerView().setHasFixedSize(false);
        getRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setRvAdapter(new ReorderStoresRVA(getView(), getShopping(), getRecyclerView(), getItemData(), getStoreData(), getDbStoreHelper()));
        getRecyclerView().setAdapter(getRvAdapter());
        getRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getReorderStoresViewState());

        setFinishReorderingButton((Button) getView().findViewById(R.id.finishReorderingButton));
        setCancelButton((Button) getView().findViewById(R.id.cancelButton));

        getFinishReorderingButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().loadFragment(new FullInventory());
            }
        });

        getCancelButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().loadFragment(new FullInventory());
            }
        });

        return view;
    }

    public void onDestroyView() {
        getRecyclerView().setAdapter(null);
        super.onDestroyView();
    }
}