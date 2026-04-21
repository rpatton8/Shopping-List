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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.reorder_categories, container, false);

        shopping = (Shopping) getActivity();
        itemData = shopping.getItemData();
        categoryData = shopping.getCategoryData();
        dbCategoryHelper = new DBCategoryHelper(getActivity());

        recyclerView = view.findViewById(R.id.reorderCategoriesRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAdapter = new ReorderCategoriesRVA(shopping, recyclerView, itemData, categoryData, dbCategoryHelper);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.getLayoutManager().onRestoreInstanceState(shopping.reorderCategoriesViewState);

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