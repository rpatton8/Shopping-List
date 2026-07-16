package ryan.android.shopping;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

class ReorderCategoriesRVA extends RecyclerView.Adapter<ReorderCategoriesRVA.ReorderCategoriesRVH> {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private CategoryData categoryData;
    private DBCategoryHelper dbCategoryHelper;
    private RecyclerView recyclerView;

    ReorderCategoriesRVA(View view, Shopping shopping, RecyclerView recyclerView, ItemData itemData, CategoryData categoryData, DBCategoryHelper dbCategory) {
        setView(view);
        setShopping(shopping);
        setItemData(itemData) ;
        setCategoryData(categoryData);
        setDbCategoryHelper(dbCategory);
        setRecyclerView(recyclerView);
    }

    private ReorderCategoriesRVA getThis() {
        return this;
    }

    private View getView() {
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

    private DBCategoryHelper getDbCategoryHelper() {
        return dbCategoryHelper;
    }

    private void setDbCategoryHelper(DBCategoryHelper dbCategoryHelper) {
        getThis().dbCategoryHelper = dbCategoryHelper;
    }

    private RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        getThis().recyclerView = recyclerView;
    }

    public int getItemViewType(int position) {
        return R.layout.reorder_categories_rv;
    }

    public ReorderCategoriesRVH onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        return new ReorderCategoriesRVH(getView(), getShopping(), getRecyclerView(), getItemData(), getCategoryData(), getDbCategoryHelper());
    }

    public void onBindViewHolder(ReorderCategoriesRVH holder, int position) {
        ArrayList<String> categoryList = getCategoryData().getCategoryList();
        if (position >= 0 && position < categoryList.size()) {
            holder.getCategoryName().setText(categoryList.get(position));
        }
    }

    public int getItemCount() {
        return getCategoryData().getCategoryList().size();
    }

    static class ReorderCategoriesRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private ItemData itemData;
        private CategoryData categoryData;
        private DBCategoryHelper dbCategoryHelper;
        private RecyclerView recyclerView;

        private TextView categoryName;
        private ImageView triangleDown;
        private ImageView triangleUp;

        ReorderCategoriesRVH(View itemView, Shopping shopping, RecyclerView recyclerView,
                             ItemData itemData, CategoryData categoryData, DBCategoryHelper dbCategory) {

            super(itemView);
            setShopping(shopping);
            setItemData(itemData) ;
            setCategoryData(categoryData);
            setDbCategoryHelper(dbCategory);
            setRecyclerView(recyclerView);

            setCategoryName(itemView.findViewById(R.id.categoryName));
            setTriangleDown(itemView.findViewById(R.id.triangleDown));
            setTriangleUp(itemView.findViewById(R.id.triangleUp));

            getTriangleDown().setOnClickListener(getThis());
            getTriangleUp().setOnClickListener(getThis());

        }

        private ReorderCategoriesRVH getThis() {
            return this;
        }

        private Shopping getShopping() {
            return shopping;
        }

        private void setShopping(Shopping shopping) {
            getThis().shopping = shopping;
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

        private DBCategoryHelper getDbCategoryHelper() {
            return dbCategoryHelper;
        }

        private void setDbCategoryHelper(DBCategoryHelper dbCategoryHelper) {
            getThis().dbCategoryHelper = dbCategoryHelper;
        }

        private RecyclerView getRecyclerView() {
            return recyclerView;
        }

        private void setRecyclerView(RecyclerView recyclerView) {
            getThis().recyclerView = recyclerView;
        }

        private TextView getCategoryName() {
            return categoryName;
        }

        private void setCategoryName(TextView categoryName) {
            getThis().categoryName = categoryName;
        }

        private ImageView getTriangleDown() {
            return triangleDown;
        }

        private void setTriangleDown(ImageView triangleDown) {
            getThis().triangleDown = triangleDown;
        }

        private ImageView getTriangleUp() {
            return triangleUp;
        }

        private void setTriangleUp(ImageView triangleUp) {
            getThis().triangleUp = triangleUp;
        }

        private boolean categoryContains(int categoryPosition, int itemPosition) {
            int index = 0;
            for (int i = 0; i < getCategoryData().getCategoryList().size(); i++) {
                String category = getCategoryData().getCategoryList().get(i);
                int numItemsInCategory;
                if (getItemData().getCategoryMap().get(category) == null) {
                    numItemsInCategory = 0;
                } else {
                    numItemsInCategory = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                }
                index += numItemsInCategory + 1;
                if (itemPosition == index) return false;
                if (index > itemPosition) {
                    return categoryPosition == i;
                }
            }
            return false;
        }

        public void onClick(View v) {
            int id = v.getId();
            int position = getBindingAdapterPosition();

            if (id == getTriangleDown().getId()) {
                if (position == getCategoryData().getCategoryList().size() - 1) {
                    // Down arrow on last category
                    return;
                }
                getDbCategoryHelper().swapOrder(position, position + 1);
                getShopping().updateCategoryData();

                // Check if selected item is in one of the categories being moved and shift it
                if (getShopping().getSelectedItemPositionInInventory() != 0) {

                    if (categoryContains(position, getShopping().getSelectedItemPositionInInventory())) {

                        String category = getCategoryData().getCategoryList().get(position + 1);
                        int offset = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                        getShopping().setSelectedItemPositionInInventory(getShopping().getSelectedItemPositionInInventory() + offset + 1);

                    } else if (categoryContains(position + 1, getShopping().getSelectedItemPositionInInventory())) {

                        String category = getCategoryData().getCategoryList().get(position);
                        int offset = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                        getShopping().setSelectedItemPositionInInventory(getShopping().getSelectedItemPositionInInventory() - offset + 1);

                    }
                }
                getShopping().setReorderCategoriesViewState(getRecyclerView().getLayoutManager().onSaveInstanceState());
                getShopping().loadFragment(new ReorderCategories());

            } else if (id == getTriangleUp().getId()) {
                if (position == 0) {
                    // Up arrow on first item
                    return;
                }
                getDbCategoryHelper().swapOrder(position - 1, position);
                getShopping().updateCategoryData();

                // Check if selected item is in one of the categories being moved and shift it
                if (getShopping().getSelectedItemPositionInInventory() != 0) {

                    if (categoryContains(position, getShopping().getSelectedItemPositionInInventory())) {

                        String category = getCategoryData().getCategoryList().get(position - 1);
                        int offset = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                        getShopping().setSelectedItemPositionInInventory(getShopping().getSelectedItemPositionInInventory() - offset + 1);

                    } else if (categoryContains(position - 1, getShopping().getSelectedItemPositionInInventory())) {

                        String category = getCategoryData().getCategoryList().get(position);
                        int offset = getItemData().getCategoryMap().get(category).getCategoryItemsList().size();
                        getShopping().setSelectedItemPositionInInventory(getShopping().getSelectedItemPositionInInventory() + offset + 1);
                    }
                }
                getShopping().setReorderCategoriesViewState(getRecyclerView().getLayoutManager().onSaveInstanceState());
                getShopping().loadFragment(new ReorderCategories());
            }
        }
    }
}