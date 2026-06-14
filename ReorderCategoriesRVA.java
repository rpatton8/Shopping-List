package ryan.android.shopping;

import android.support.v7.widget.RecyclerView;
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

    public int getItemViewType(int position) {
        return R.layout.reorder_categories_rv;
    }

    public ReorderCategoriesRVH onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        return new ReorderCategoriesRVH(getView(), getShopping(), getRecyclerView(), getItemData(), getCategoryData(), getDbCategoryHelper());
    }

    public void onBindViewHolder(ReorderCategoriesRVH holder, int position) {
        ArrayList<String> categoryList = getCategoryData().getCategoryList();
        holder.getCategoryName().setText(categoryList.get(position));
    }

    public int getItemCount() {
        return getCategoryData().getCategoryList().size();
    }

    class ReorderCategoriesRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

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

            setCategoryName((TextView) itemView.findViewById(R.id.categoryName));
            setTriangleDown((ImageView) itemView.findViewById(R.id.triangleDown));
            setTriangleUp((ImageView) itemView.findViewById(R.id.triangleUp));

            getTriangleDown().setOnClickListener(this);
            getTriangleUp().setOnClickListener(this);

        }

        private ReorderCategoriesRVH getThis() {
            return this;
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

        public TextView getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(TextView categoryName) {
            getThis().categoryName = categoryName;
        }

        public ImageView getTriangleDown() {
            return triangleDown;
        }

        public void setTriangleDown(ImageView triangleDown) {
            getThis().triangleDown = triangleDown;
        }

        public ImageView getTriangleUp() {
            return triangleUp;
        }

        public void setTriangleUp(ImageView triangleUp) {
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
            int position = getAdapterPosition();
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