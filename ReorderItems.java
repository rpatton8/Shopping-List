package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Objects;

public class ReorderItems extends Fragment {

    private Shopping shopping;
    private ReorderItemsRVA rvAdapter;
    private ScrollView scrollView;
    private RecyclerView recyclerView;
    private LinearLayout categoryLayout;
    private LinearLayout storeLayout;

    public ReorderItems() {}

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reorder_items, container, false);

        shopping = (Shopping) getActivity();
        DBItemHelper dbItemHelper = new DBItemHelper(getActivity());
        ItemData itemData = shopping.getItemData();
        CategoryData categoryData = shopping.getCategoryData();
        StoreData storeData = shopping.getStoreData();

        categoryLayout = view.findViewById(R.id.categoryLayout);
        storeLayout = view.findViewById(R.id.storeLayout);
        Button categoryRadioButton = view.findViewById(R.id.categoryRadioButton);
        Button storeRadioButton = view.findViewById(R.id.storeRadioButton);
        Button finishReorderingButton = view.findViewById(R.id.finishReorderingButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        ArrayList<String> categorySpinnerData = categoryData.getCategoryListWithBlank();
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categorySpinnerData);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);
        int categorySpinnerPosition = categorySpinnerAdapter.getPosition(shopping.reorderItemsCategory);
        categorySpinner.setSelection(categorySpinnerPosition);

        ArrayList<String> storeSpinnerData = storeData.getStoreListWithBlank();
        Spinner storeSpinner = view.findViewById(R.id.storeSpinner);
        ArrayAdapter<String> storeSpinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, storeSpinnerData);
        storeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(storeSpinnerAdapter);
        int storeSpinnerPosition = storeSpinnerAdapter.getPosition(shopping.reorderItemsStore);
        storeSpinner.setSelection(storeSpinnerPosition);

        recyclerView = view.findViewById(R.id.reorderItemsRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //recyclerView.setNestedScrollingEnabled(false);
        rvAdapter = new ReorderItemsRVA(shopping, recyclerView, scrollView, itemData, categoryData, storeData, dbItemHelper);
        //rvAdapter.changeCategory("Candy");
        recyclerView.setAdapter(rvAdapter);
        Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(shopping.reorderItemsRecyclerViewState);
        //Objects.requireNonNull(scrollView.getLayoutManager()).onRestoreInstanceState(shopping.reorderItemsScrollViewState);

        scrollView = view.findViewById(R.id.reorderItemsScrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if (scrollY < 578) recyclerView.setNestedScrollingEnabled(false);
                else recyclerView.setNestedScrollingEnabled(true);
            }
        });

        categoryRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvAdapter.reorderBy = ReorderItemsRVA.REORDER_BY_CATEGORY;
                categoryLayout.setVisibility(View.VISIBLE);
                storeLayout.setVisibility(View.GONE);
                rvAdapter.notifyDataSetChanged();
            }
        });

        storeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvAdapter.reorderBy = ReorderItemsRVA.REORDER_BY_STORE;
                storeLayout.setVisibility(View.VISIBLE);
                categoryLayout.setVisibility(View.GONE);
                rvAdapter.notifyDataSetChanged();
            }
        });

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

        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {

                String selectedItem =  adapter.getItemAtPosition(i).toString();
                rvAdapter.changeStore(selectedItem);
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

    @Override
    public void onDestroyView() {
        recyclerView.setAdapter(null);
        super.onDestroyView();
    }
}