package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

public class ReorderCategories extends Fragment {

    private Shopping shopping;
    private RecyclerView recyclerView;

    public ReorderCategories() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reorder_categories, container, false);

        shopping = (Shopping) getActivity();
        ItemData itemData = shopping.getItemData();
        CategoryData categoryData = shopping.getCategoryData();
        DBCategoryHelper dbCategoryHelper = new DBCategoryHelper(getActivity());

        recyclerView = view.findViewById(R.id.reorderCategoriesRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ReorderCategoriesRVA adapter = new ReorderCategoriesRVA(shopping, recyclerView, itemData, categoryData, dbCategoryHelper);
        recyclerView.setAdapter(adapter);
        Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(shopping.reorderCategoriesViewState);

        Button finishReorderingButton = view.findViewById(R.id.finishReorderingButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

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