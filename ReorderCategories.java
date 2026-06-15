package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ReorderCategories extends Fragment {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private CategoryData categoryData;
    private DBCategoryHelper dbCategoryHelper;
    private RecyclerView recyclerView;
    private ReorderCategoriesRVA rvAdapter;
    private Button finishReorderingButton;
    private Button cancelButton;

    public ReorderCategories() {}

    private ReorderCategories getThis() {
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

    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(CategoryData categoryData) {
        getThis().categoryData = categoryData;
    }

    public DBCategoryHelper getDbCategoryHelper() {
        return dbCategoryHelper;
    }

    public void setDbCategoryHelper(DBCategoryHelper dbCategoryHelper) {
        getThis().dbCategoryHelper = dbCategoryHelper;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        getThis().recyclerView = recyclerView;
    }

    public ReorderCategoriesRVA getRvAdapter() {
        return rvAdapter;
    }

    public void setRvAdapter(ReorderCategoriesRVA rvAdapter) {
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

        setView(inflater.inflate(R.layout.reorder_categories, container, false));

        setShopping((Shopping) getActivity());
        setItemData(getShopping().getItemData());
        setCategoryData(getShopping().getCategoryData());
        setDbCategoryHelper(new DBCategoryHelper(getActivity()));

        setRecyclerView((RecyclerView) getView().findViewById(R.id.reorderCategoriesRecyclerView));
        getRecyclerView().setHasFixedSize(false);
        getRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setRvAdapter(new ReorderCategoriesRVA(getView(), getShopping(), getRecyclerView(), getItemData(), getCategoryData(), getDbCategoryHelper()));
        getRecyclerView().setAdapter(getRvAdapter());
        getRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getReorderCategoriesViewState());

        setFinishReorderingButton((Button) getView().findViewById(R.id.finishReorderingButton));;
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

        return getView();
    }

    public void onDestroyView() {
        getRecyclerView().setAdapter(null);
        super.onDestroyView();
    }
}