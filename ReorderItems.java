package ryan.android.shopping;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

    private ReorderItems getThis() {
        return this;
    }

    public View getView() {
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

    private DBItemHelper getDbItemHelper() {
        return dbItemHelper;
    }

    private void setDbItemHelper(DBItemHelper dbItemHelper) {
        getThis().dbItemHelper = dbItemHelper;
    }

    private ItemData getItemData() {
        return itemData;
    }

    private void setItemData(ItemData itemData) {
        getThis().itemData = itemData;
    }

    private CategoryData getCategoryData() {
        return categoryData;
    }

    private void setCategoryData(CategoryData categoryData) {
        getThis().categoryData = categoryData;
    }

    private StoreData getStoreData() {
        return storeData;
    }

    private void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
    }

    private RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        getThis().recyclerView = recyclerView;
    }

    private ReorderItemsRVA getRvAdapter() {
        return rvAdapter;
    }

    private void setRvAdapter(ReorderItemsRVA rvAdapter) {
        getThis().rvAdapter = rvAdapter;
    }

    private ScrollView getScrollView() {
        return scrollView;
    }

    private void setScrollView(ScrollView scrollView) {
        getThis().scrollView = scrollView;
    }

    private LinearLayout getCategoryLayout() {
        return categoryLayout;
    }

    private void setCategoryLayout(LinearLayout categoryLayout) {
        getThis().categoryLayout = categoryLayout;
    }

    private RadioButton getCategoryRadioButton() {
        return categoryRadioButton;
    }

    private void setCategoryRadioButton(RadioButton categoryRadioButton) {
        getThis().categoryRadioButton = categoryRadioButton;
    }

    private Spinner getCategorySpinner() {
        return categorySpinner;
    }

    private void setCategorySpinner(Spinner categorySpinner) {
        getThis().categorySpinner = categorySpinner;
    }

    private ArrayList<String> getCategorySpinnerData() {
        return categorySpinnerData;
    }

    private void setCategorySpinnerData(ArrayList<String> categorySpinnerData) {
        getThis().categorySpinnerData = categorySpinnerData;
    }

    private ArrayAdapter<String> getCategorySpinnerAdapter() {
        return categorySpinnerAdapter;
    }

    private void setCategorySpinnerAdapter(ArrayAdapter<String> categorySpinnerAdapter) {
        getThis().categorySpinnerAdapter = categorySpinnerAdapter;
    }

    private LinearLayout getStoreLayout() {
        return storeLayout;
    }

    private void setStoreLayout(LinearLayout storeLayout) {
        getThis().storeLayout = storeLayout;
    }

    private RadioButton getStoreRadioButton() {
        return storeRadioButton;
    }

    private void setStoreRadioButton(RadioButton storeRadioButton) {
        getThis().storeRadioButton = storeRadioButton;
    }

    private Spinner getStoreSpinner() {
        return storeSpinner;
    }

    private void setStoreSpinner(Spinner storeSpinner) {
        getThis().storeSpinner = storeSpinner;
    }

    private ArrayList<String> getStoreSpinnerData() {
        return storeSpinnerData;
    }

    private void setStoreSpinnerData(ArrayList<String> storeSpinnerData) {
        getThis().storeSpinnerData = storeSpinnerData;
    }

    private ArrayAdapter<String> getStoreSpinnerAdapter() {
        return storeSpinnerAdapter;
    }

    private void setStoreSpinnerAdapter(ArrayAdapter<String> storeSpinnerAdapter) {
        getThis().storeSpinnerAdapter = storeSpinnerAdapter;
    }

    private Button getFinishReorderingButton() {
        return finishReorderingButton;
    }

    private void setFinishReorderingButton(Button finishReorderingButton) {
        getThis().finishReorderingButton = finishReorderingButton;
    }

    private Button getCancelButton() {
        return cancelButton;
    }

    private void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.reorder_items, container, false));

        setShopping((Shopping) getActivity());
        setItemData(getShopping().getItemData());
        setCategoryData(getShopping().getCategoryData());
        setStoreData(getShopping().getStoreData());
        setDbItemHelper(new DBItemHelper(getActivity()));

        setCategoryLayout(getView().findViewById(R.id.categoryLayout));
        setStoreLayout(getView().findViewById(R.id.storeLayout));
        setCategoryRadioButton(getView().findViewById(R.id.categoryRadioButton));
        setStoreRadioButton(getView().findViewById(R.id.storeRadioButton));
        setFinishReorderingButton(getView().findViewById(R.id.finishReorderingButton));
        setCancelButton(getView().findViewById(R.id.cancelButton));

        setCategorySpinnerData(getCategoryData().getCategoryListWithBlank());
        setCategorySpinner(getView().findViewById(R.id.categorySpinner));
        setCategorySpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getCategorySpinnerData()));
        getCategorySpinnerAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getCategorySpinner().setAdapter(getCategorySpinnerAdapter());
        int categorySpinnerPosition = getCategorySpinnerAdapter().getPosition(getShopping().getReorderItemsCategory());
        getCategorySpinner().setSelection(categorySpinnerPosition);

        setStoreSpinnerData(getStoreData().getStoreListWithBlank());
        setStoreSpinner(getView().findViewById(R.id.storeSpinner));
        setStoreSpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getStoreSpinnerData()));
        getStoreSpinnerAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getStoreSpinner().setAdapter(getStoreSpinnerAdapter());
        int storeSpinnerPosition = getStoreSpinnerAdapter().getPosition(getShopping().getReorderItemsStore());
        getStoreSpinner().setSelection(storeSpinnerPosition);

        setRecyclerView(getView().findViewById(R.id.reorderItemsRecyclerView));
        getRecyclerView().setHasFixedSize(false);
        getRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setRvAdapter(new ReorderItemsRVA(getView(), getShopping(), getContext(), getRecyclerView(), getScrollView(), getItemData(), getCategoryData(), getStoreData(), getDbItemHelper()));
        getRecyclerView().setAdapter(getRvAdapter());
        getRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getReorderItemsRecyclerViewState());
        //getScrollView().getLayoutManager().onRestoreInstanceState(getShopping().getReorderItemsScrollViewState());

        setScrollView(getView().findViewById(R.id.reorderItemsScrollView));
        getScrollView().getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            public void onScrollChanged() {
                int scrollY = getScrollView().getScrollY();
                getRecyclerView().setNestedScrollingEnabled(scrollY >= 578);
            }
        });

        getCategoryRadioButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getRvAdapter().setReorderBy(ReorderItemsRVA.REORDER_BY_CATEGORY);
                getCategoryLayout().setVisibility(View.VISIBLE);
                getStoreLayout().setVisibility(View.GONE);
                getRvAdapter().notifyDataSetChanged();
            }
        });

        getStoreRadioButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getRvAdapter().setReorderBy(ReorderItemsRVA.REORDER_BY_STORE);
                getStoreLayout().setVisibility(View.VISIBLE);
                getCategoryLayout().setVisibility(View.GONE);
                getRvAdapter().notifyDataSetChanged();
            }
        });

        getCategorySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapter, View view, int i, long l) {

                String selectedItem =  adapter.getItemAtPosition(i).toString();
                getRvAdapter().changeCategory(selectedItem);
                getRvAdapter().notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getStoreSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                getRvAdapter().changeStore(selectedItem);
                getRvAdapter().notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {}

        });

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

        return getView();
    }

    public void onDestroyView() {
        getRecyclerView().setAdapter(null);
        super.onDestroyView();
    }
}