package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//@SuppressWarnings("ALL")
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.reorder_stores, container, false);

        shopping = (Shopping) getActivity();
        itemData = shopping.getItemData();
        storeData = shopping.getStoreData();
        dbStoreHelper = new DBStoreHelper(getActivity());

        recyclerView = view.findViewById(R.id.reorderStoresRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAdapter = new ReorderStoresRVA(shopping, recyclerView, itemData, storeData, dbStoreHelper);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.getLayoutManager().onRestoreInstanceState(shopping.reorderStoresViewState);

        finishReorderingButton = view.findViewById(R.id.finishReorderingButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        finishReorderingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.loadFragment(new FullInventory());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.loadFragment(new FullInventory());
            }
        });

        return view;
    }

    public void onDestroyView() {
        recyclerView.setAdapter(null);
        super.onDestroyView();
    }
}