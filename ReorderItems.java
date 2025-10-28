package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;

public class ReorderItems extends Fragment {

    private Shopping shopping;

    public ReorderItems() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reorder_items, container, false);

        shopping = (Shopping) getActivity();
        DBItemHelper dbItemHelper = new DBItemHelper(getActivity());
        DBStoreHelper dbStoreHelper = new DBStoreHelper(getActivity());

        ItemData itemData = shopping.getItemData();
        StatusData statusData = shopping.getStatusData();
        CategoryData categoryData = shopping.getCategoryData();
        StoreData storeData = shopping.getStoreData();
        itemData.updateStatusesByCategory(statusData);

        Button finishReorderingButton = view.findViewById(R.id.finishReorderingButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        ArrayList<String> spinnerData = categoryData.getCategoryListWithBlank();
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        int spinnerPosition = spinnerAdapter.getPosition(shopping.reorderItemsCategory);
        categorySpinner.setSelection(spinnerPosition);

        RecyclerView recyclerView = view.findViewById(R.id.reorderItemsRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        final ReorderItemsRVA rvAdapter = new ReorderItemsRVA(shopping, recyclerView, itemData, storeData, dbItemHelper, dbStoreHelper);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.getLayoutManager().onRestoreInstanceState(shopping.reorderItemsViewState);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                rvAdapter.changeCategory(selectedItem);
                rvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        finishReorderingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.loadFragment(new FullInventory());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.loadFragment(new FullInventory());
            }
        });

        return view;
    }
}