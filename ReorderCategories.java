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
    private Button finishReorderingButton;
    private Button cancelButton;

    public ReorderCategories() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reorder_categories, container, false);

        shopping = (Shopping) getActivity();
        itemData = shopping.getItemData();
        categoryData = shopping.getCategoryData();
        dbCategoryHelper = new DBCategoryHelper(getActivity());

        recyclerView = view.findViewById(R.id.reorderCategoriesRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ReorderCategoriesRVA adapter = new ReorderCategoriesRVA(shopping, recyclerView, itemData, categoryData, dbCategoryHelper);
        recyclerView.setAdapter(adapter);
        recyclerView.getLayoutManager().onRestoreInstanceState(shopping.reorderCategoriesViewState);
        //recyclerView.getLayoutManager().scrollToPosition(3);

        finishReorderingButton = view.findViewById(R.id.finishReorderingButton);
        cancelButton = view.findViewById(R.id.cancelButton);

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