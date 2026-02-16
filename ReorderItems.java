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
import android.widget.TextView;

import java.util.ArrayList;

public class ReorderItems extends Fragment {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private StatusData statusData;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBItemHelper dbItemHelper;
    private DBStatusHelper dbStatusHelper;
    private DBStoreHelper dbStoreHelper;

    private RecyclerView recyclerView;
    private Spinner categorySpinner;
    private Button finishReorderingButton;
    private Button cancelButton;

    public ReorderItems() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.reorder_items, container, false);

        shopping = (Shopping) getActivity();
        dbItemHelper = new DBItemHelper(getActivity());
        dbStatusHelper = new DBStatusHelper(getActivity());
        dbStoreHelper = new DBStoreHelper(getActivity());

        itemData = shopping.getItemData();
        statusData = shopping.getStatusData();
        categoryData = shopping.getCategoryData();
        storeData = shopping.getStoreData();
        itemData.updateStatuses(statusData);

        finishReorderingButton = view.findViewById(R.id.finishReorderingButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        ArrayList<String> spinnerData = categoryData.getCategoryListWithBlank();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, spinnerData);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        int spinnerPosition = spinnerAdapter.getPosition(shopping.reorderItemsCategory);
        categorySpinner.setSelection(spinnerPosition);

        recyclerView = view.findViewById(R.id.reorderItemsRecyclerView);
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