package ryan.android.shopping;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ReorderCategoriesRVA extends RecyclerView.Adapter<ReorderCategoriesRVA.ReorderCategoriesRVH> {

    private Shopping shopping;
    private ItemData itemData;
    private CategoryData categoryData;
    private DBCategoryHelper dbCategoryHelper;
    private RecyclerView recyclerView;

    public ReorderCategoriesRVA(Shopping shopping, RecyclerView recyclerView, ItemData itemData,
                                CategoryData categoryData, DBCategoryHelper dbCategory) {
        this.shopping = shopping;
        this.itemData = itemData;
        this.categoryData = categoryData;
        this.dbCategoryHelper = dbCategory;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.reorder_rv;
    }

    @NonNull
    @Override
    public ReorderCategoriesRVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ReorderCategoriesRVH(view, shopping, recyclerView, itemData, categoryData, dbCategoryHelper);
    }

    @Override
    public void onBindViewHolder(@NonNull ReorderCategoriesRVH holder, int position) {

        ArrayList<String> categoryList = categoryData.getCategoryList();
        holder.name.setText(categoryList.get(position));

    }

    @Override
    public int getItemCount() {
        return categoryData.getCategoryList().size();
    }

    public static class ReorderCategoriesRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private ItemData itemData;
        private CategoryData categoryData;
        private DBCategoryHelper dbCategoryHelper;
        private RecyclerView recyclerView;

        public TextView name;
        public ImageView arrowDown;
        public ImageView arrowUp;

        ReorderCategoriesRVH(final View itemView, Shopping shopping, RecyclerView recyclerView,
                                    ItemData itemData, CategoryData categoryData, DBCategoryHelper dbCategory) {

            super(itemView);
            this.shopping = shopping;
            this.itemData = itemData;
            this.categoryData = categoryData;
            this.dbCategoryHelper = dbCategory;
            this.recyclerView = recyclerView;

            name = itemView.findViewById(R.id.name);
            arrowDown = itemView.findViewById(R.id.arrowDown);
            arrowUp = itemView.findViewById(R.id.arrowUp);

            arrowDown.setOnClickListener(this);
            arrowUp.setOnClickListener(this);

        }

        boolean categoryContains(int categoryPosition, int itemPosition) {
            int index = 0;
            for (int i = 0; i < categoryData.getCategoryList().size(); i++) {
                String category = categoryData.getCategoryList().get(i);
                System.out.println("Category: " + category);
                int numItemsInCategory;
                if (itemData.getCategoryMap().get(category) == null) {
                    numItemsInCategory = 0;
                } else {
                    numItemsInCategory = itemData.getCategoryMap().get(category).getItemList().size();
                }
                System.out.println(category + " has " + numItemsInCategory + " items.");
                index += numItemsInCategory + 1;
                if (itemPosition == index) return false;
                if (index > itemPosition) {
                    return (categoryPosition == i);
                }
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int position = getAdapterPosition();
            if (id == arrowDown.getId()) {
                if (position == categoryData.getCategoryList().size() - 1) {
                    //Toast.makeText(shopping, "Down Arrow on last item.", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbCategoryHelper.swapOrder(position, position + 1);
                shopping.updateCategoryData();

                // Check if selected item is in one of the categories being moved and shift it
                if (shopping.selectedItemPositionInInventory != 0) {

                    if (categoryContains(position, shopping.selectedItemPositionInInventory)) {

                        String category = categoryData.getCategoryList().get(position + 1);
                        int offset = itemData.getCategoryMap().get(category).getItemList().size();
                        shopping.selectedItemPositionInInventory += offset + 1;

                    } else if (categoryContains(position + 1, shopping.selectedItemPositionInInventory)) {

                        String category = categoryData.getCategoryList().get(position);
                        int offset = itemData.getCategoryMap().get(category).getItemList().size();
                        shopping.selectedItemPositionInInventory -= offset + 1;

                    }
                }
                shopping.reorderCategoriesViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ReorderCategories());

            } else if (id == arrowUp.getId()) {
                if (position == 0) {
                    //Toast.makeText(shopping, "Up Arrow on first item.", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbCategoryHelper.swapOrder(position - 1, position);
                shopping.updateCategoryData();

                // Check if selected item is in one of the categories being moved and shift it
                if (shopping.selectedItemPositionInInventory != 0) {

                    if (categoryContains(position, shopping.selectedItemPositionInInventory)) {

                        String category = categoryData.getCategoryList().get(position - 1);
                        int offset = itemData.getCategoryMap().get(category).getItemList().size();
                        shopping.selectedItemPositionInInventory -= offset + 1;

                    } else if (categoryContains(position - 1, shopping.selectedItemPositionInInventory)) {

                        String category = categoryData.getCategoryList().get(position);
                        int offset = itemData.getCategoryMap().get(category).getItemList().size();
                        shopping.selectedItemPositionInInventory += offset + 1;

                    }
                }
                shopping.reorderCategoriesViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ReorderCategories());
            }
        }
    }
}