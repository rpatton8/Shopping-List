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
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import java.util.ArrayList;

//@SuppressWarnings("ALL")
public class ReorderItems extends Fragment {

    private View view;
    private Shopping shopping;
    private DBItemHelper dbItemHelper;
    private ItemData itemData;
    private CategoryData categoryData;
    private StoreData storeData;
    private RecyclerView recyclerView;
    private ReorderItemsRVA rvAdapter;
    private ScrollView scrollView;

    private LinearLayout categoryLayout;
    private RadioButton categoryRadioButton;
    private Spinner categorySpinner;
    private ArrayList<String> categorySpinnerData;
    private ArrayAdapter<String> categorySpinnerAdapter;
    private LinearLayout storeLayout;
    private RadioButton storeRadioButton;
    private Spinner storeSpinner;
    private ArrayList<String> storeSpinnerData;
    private ArrayAdapter<String> storeSpinnerAdapter;
    private Button finishReorderingButton;
    private Button cancelButton;

    public ReorderItems() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.reorder_items, container, false);

        shopping = (Shopping) getActivity();
        itemData = shopping.getItemData();
        categoryData = shopping.getCategoryData();
        storeData = shopping.getStoreData();
        dbItemHelper = new DBItemHelper(getActivity());

        categoryLayout = view.findViewById(R.id.categoryLayout);
        storeLayout = view.findViewById(R.id.storeLayout);
        categoryRadioButton = view.findViewById(R.id.categoryRadioButton);
        storeRadioButton = view.findViewById(R.id.storeRadioButton);
        finishReorderingButton = view.findViewById(R.id.finishReorderingButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        categorySpinnerData = categoryData.getCategoryListWithBlank();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        categorySpinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categorySpinnerData);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);
        int categorySpinnerPosition = categorySpinnerAdapter.getPosition(shopping.reorderItemsCategory);
        categorySpinner.setSelection(categorySpinnerPosition);

        storeSpinnerData = storeData.getStoreListWithBlank();
        storeSpinner = view.findViewById(R.id.storeSpinner);
        storeSpinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, storeSpinnerData);
        storeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(storeSpinnerAdapter);
        int storeSpinnerPosition = storeSpinnerAdapter.getPosition(shopping.reorderItemsStore);
        storeSpinner.setSelection(storeSpinnerPosition);

        recyclerView = view.findViewById(R.id.reorderItemsRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAdapter = new ReorderItemsRVA(shopping, recyclerView, scrollView, itemData, categoryData, storeData, dbItemHelper);
        //rvAdapter.changeCategory("Candy");
        recyclerView.setAdapter(rvAdapter);
        recyclerView.getLayoutManager().onRestoreInstanceState(shopping.reorderItemsRecyclerViewState);
        //scrollView.getLayoutManager().onRestoreInstanceState(shopping.reorderItemsScrollViewState);

        scrollView = view.findViewById(R.id.reorderItemsScrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if (scrollY < 578) recyclerView.setNestedScrollingEnabled(false);
                else recyclerView.setNestedScrollingEnabled(true);
            }
        });

        categoryRadioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rvAdapter.reorderBy = ReorderItemsRVA.REORDER_BY_CATEGORY;
                categoryLayout.setVisibility(View.VISIBLE);
                storeLayout.setVisibility(View.GONE);
                rvAdapter.notifyDataSetChanged();
            }
        });

        storeRadioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rvAdapter.reorderBy = ReorderItemsRVA.REORDER_BY_STORE;
                storeLayout.setVisibility(View.VISIBLE);
                categoryLayout.setVisibility(View.GONE);
                rvAdapter.notifyDataSetChanged();
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapter, View view, int i, long l) {

                String selectedItem =  adapter.getItemAtPosition(i).toString();
                rvAdapter.changeCategory(selectedItem);
                rvAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                rvAdapter.changeStore(selectedItem);
                rvAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {}

        });

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