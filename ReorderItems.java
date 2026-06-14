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

    public void setView(View view) {
        getThis().view = view;
    }

    public Shopping getShopping() {
        return shopping;
    }

    public void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    public DBItemHelper getDbItemHelper() {
        return dbItemHelper;
    }

    public void setDbItemHelper(DBItemHelper dbItemHelper) {
        getThis().dbItemHelper = dbItemHelper;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        getThis().itemData = itemData;
    }

    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(CategoryData categoryData) {
        getThis().categoryData = categoryData;
    }

    public StoreData getStoreData() {
        return storeData;
    }

    public void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        getThis().recyclerView = recyclerView;
    }

    public ReorderItemsRVA getRvAdapter() {
        return rvAdapter;
    }

    public void setRvAdapter(ReorderItemsRVA rvAdapter) {
        getThis().rvAdapter = rvAdapter;
    }

    public ScrollView getScrollView() {
        return scrollView;
    }

    public void setScrollView(ScrollView scrollView) {
        getThis().scrollView = scrollView;
    }

    public LinearLayout getCategoryLayout() {
        return categoryLayout;
    }

    public void setCategoryLayout(LinearLayout categoryLayout) {
        getThis().categoryLayout = categoryLayout;
    }

    public RadioButton getCategoryRadioButton() {
        return categoryRadioButton;
    }

    public void setCategoryRadioButton(RadioButton categoryRadioButton) {
        getThis().categoryRadioButton = categoryRadioButton;
    }

    public Spinner getCategorySpinner() {
        return categorySpinner;
    }

    public void setCategorySpinner(Spinner categorySpinner) {
        getThis().categorySpinner = categorySpinner;
    }

    public ArrayList<String> getCategorySpinnerData() {
        return categorySpinnerData;
    }

    public void setCategorySpinnerData(ArrayList<String> categorySpinnerData) {
        getThis().categorySpinnerData = categorySpinnerData;
    }

    public ArrayAdapter<String> getCategorySpinnerAdapter() {
        return categorySpinnerAdapter;
    }

    public void setCategorySpinnerAdapter(ArrayAdapter<String> categorySpinnerAdapter) {
        getThis().categorySpinnerAdapter = categorySpinnerAdapter;
    }

    public LinearLayout getStoreLayout() {
        return storeLayout;
    }

    public void setStoreLayout(LinearLayout storeLayout) {
        getThis().storeLayout = storeLayout;
    }

    public RadioButton getStoreRadioButton() {
        return storeRadioButton;
    }

    public void setStoreRadioButton(RadioButton storeRadioButton) {
        getThis().storeRadioButton = storeRadioButton;
    }

    public Spinner getStoreSpinner() {
        return storeSpinner;
    }

    public void setStoreSpinner(Spinner storeSpinner) {
        getThis().storeSpinner = storeSpinner;
    }

    public ArrayList<String> getStoreSpinnerData() {
        return storeSpinnerData;
    }

    public void setStoreSpinnerData(ArrayList<String> storeSpinnerData) {
        getThis().storeSpinnerData = storeSpinnerData;
    }

    public ArrayAdapter<String> getStoreSpinnerAdapter() {
        return storeSpinnerAdapter;
    }

    public void setStoreSpinnerAdapter(ArrayAdapter<String> storeSpinnerAdapter) {
        getThis().storeSpinnerAdapter = storeSpinnerAdapter;
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

        setView(inflater.inflate(R.layout.reorder_items, container, false));

        setShopping((Shopping) getActivity());
        setItemData(getShopping().getItemData());
        setCategoryData(getShopping().getCategoryData());
        setStoreData(getShopping().getStoreData());
        setDbItemHelper(new DBItemHelper(getActivity()));

        setCategoryLayout((LinearLayout) getView().findViewById(R.id.categoryLayout));
        setStoreLayout((LinearLayout) getView().findViewById(R.id.storeLayout));
        setCategoryRadioButton((RadioButton) getView().findViewById(R.id.categoryRadioButton));
        setStoreRadioButton((RadioButton) getView().findViewById(R.id.storeRadioButton));
        setFinishReorderingButton((Button) getView().findViewById(R.id.finishReorderingButton));
        setCancelButton((Button) getView().findViewById(R.id.cancelButton));

        setCategorySpinnerData(getCategoryData().getCategoryListWithBlank());
        setCategorySpinner((Spinner) getView().findViewById(R.id.categorySpinner));
        setCategorySpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getCategorySpinnerData()));
        getCategorySpinnerAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getCategorySpinner().setAdapter(getCategorySpinnerAdapter());
        int categorySpinnerPosition = getCategorySpinnerAdapter().getPosition(getShopping().getReorderItemsCategory());
        getCategorySpinner().setSelection(categorySpinnerPosition);

        setStoreSpinnerData(getStoreData().getStoreListWithBlank());
        setStoreSpinner((Spinner) getView().findViewById(R.id.storeSpinner));
        setStoreSpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getStoreSpinnerData()));
        getStoreSpinnerAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getStoreSpinner().setAdapter(getStoreSpinnerAdapter());
        int storeSpinnerPosition = getStoreSpinnerAdapter().getPosition(getShopping().getReorderItemsStore());
        getStoreSpinner().setSelection(storeSpinnerPosition);

        setRecyclerView((RecyclerView) getView().findViewById(R.id.reorderItemsRecyclerView));
        getRecyclerView().setHasFixedSize(false);
        getRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setRvAdapter(new ReorderItemsRVA(getView(), getShopping(), getContext(), getRecyclerView(), getScrollView(), getItemData(), getCategoryData(), getStoreData(), getDbItemHelper()));
        getRecyclerView().setAdapter(getRvAdapter());
        getRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getReorderItemsRecyclerViewState());
        //getScrollView().getLayoutManager().onRestoreInstanceState(getShopping().getReorderItemsScrollViewState());

        setScrollView((ScrollView) getView().findViewById(R.id.reorderItemsScrollView));
        getScrollView().getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            public void onScrollChanged() {
                int scrollY = getScrollView().getScrollY();
                if (scrollY < 578) getRecyclerView().setNestedScrollingEnabled(false);
                else getRecyclerView().setNestedScrollingEnabled(true);
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

        return view;
    }

    public void onDestroyView() {
        getRecyclerView().setAdapter(null);
        super.onDestroyView();
    }
}