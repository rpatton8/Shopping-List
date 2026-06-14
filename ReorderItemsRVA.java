package ryan.android.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

class ReorderItemsRVA extends RecyclerView.Adapter<ReorderItemsRVA.ReorderItemsRVH> {

    private View view;
    private Context context;
    private Shopping shopping;
    private ItemData itemData;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBItemHelper dbItemHelper;
    private RecyclerView recyclerView;
    private ScrollView scrollView;

    private String reorderBy;
    static final String REORDER_BY_CATEGORY = ShoppingApp.getStringRes(R.string.cvReorderByCategory);
    static final String REORDER_BY_STORE = ShoppingApp.getStringRes(R.string.cvReorderByStore);

    private String category;
    private String store;

    ReorderItemsRVA(View view, Shopping shopping, Context context, RecyclerView recyclerView, ScrollView scrollView,
                    ItemData itemData, CategoryData categoryData, StoreData storeData, DBItemHelper dbItemHelper) {

        setView(view);
        setContext(context);
        setShopping(shopping);
        setItemData(itemData);
        setCategoryData(categoryData);
        setStoreData(storeData) ;
        setDbItemHelper(dbItemHelper);
        setRecyclerView(recyclerView);
        setScrollView(scrollView);
        setCategory(getShopping().getReorderItemsCategory());
        setStore(getShopping().getReorderItemsStore());
        setReorderBy(REORDER_BY_CATEGORY);
    }

    private ReorderItemsRVA getThis() {
        return this;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    private Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        getThis().context = context;
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

    public StoreData getStoreData() {
        return storeData;
    }

    public void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
    }

    public DBItemHelper getDbItemHelper() {
        return dbItemHelper;
    }

    public void setDbItemHelper(DBItemHelper dbItemHelper) {
        getThis().dbItemHelper = dbItemHelper;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        getThis().recyclerView = recyclerView;
    }

    public ScrollView getScrollView() {
        return scrollView;
    }

    public void setScrollView(ScrollView scrollView) {
        getThis().scrollView = scrollView;
    }

    public String getReorderBy() {
        return reorderBy;
    }

    public void setReorderBy(String reorderBy) {
        getThis().reorderBy = reorderBy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        getThis().category = category;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        getThis().store = store;
    }

    public int getItemViewType(int position) {
        return R.layout.reorder_items_rv;
    }

    public ReorderItemsRVH onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        return new ReorderItemsRVH(getView(), getShopping(), getThis(), getScrollView(), getRecyclerView(), getItemData(), getCategoryData(), getStoreData());
    }

    public void onBindViewHolder(ReorderItemsRVH holder, int position) {

        if (getReorderBy().equals(REORDER_BY_CATEGORY)) {

            if (!getCategory().equals(getContext().getString(R.string.emptyString))) {
                ArrayList<Item> categoryList = getItemData().getCategoryMap().get(getCategory()).getCategoryItemsList();
                holder.getItemName().setText(categoryList.get(position).getItemName());
            }

        } else if (getReorderBy().equals(REORDER_BY_STORE)) {

            if (!getStore().equals(getContext().getString(R.string.emptyString))) {
                ArrayList<Item> storeList = getItemData().getStoreMap().get(getStore()).getStoreItemsList();
                holder.getItemName().setText(storeList.get(position).getItemName());
            }
        }
    }

    public int getItemCount() {
        if (getReorderBy().equals(REORDER_BY_CATEGORY)) {
            return getCategoryData().getCategoryList().size();
        } else if (getReorderBy().equals(REORDER_BY_STORE)) {
            return getStoreData().getStoreList().size();
        } else return -1;
    }

    public void changeCategory(String category) {
        getThis().setCategory(category);
        getShopping().setReorderItemsCategory(category);
    }

    public void changeStore(String store) {
        getThis().setStore(store);
        getShopping().setReorderItemsStore(store);
    }

    private List<Item> getCategoryList() {
        return getItemData().getCategoryMap().get(getCategory()).getCategoryItemsList();
    }

    private List<Item> getStoreList() {
        return getItemData().getStoreMap().get(getStore()).getStoreItemsList();
    }

    private void swapOrderByCategory(int order1, int order2) {
        getDbItemHelper().swapOrderByCategory(getCategory(), order1, order2);
    }

    private void swapOrderByStore(int order1, int order2) {
        getDbItemHelper().swapOrderByStore(getStore(), order1, order2);
    }

    class ReorderItemsRVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Shopping shopping;
        private ReorderItemsRVA adapter;
        private RecyclerView recyclerView;
        private ScrollView scrollView;
        private ItemData itemData;
        private CategoryData categoryData;
        private StoreData storeData;

        private TextView itemName;
        private ImageView triangleDown;
        private ImageView triangleUp;

        ReorderItemsRVH(View itemView, Shopping shopping, ReorderItemsRVA adapter, ScrollView scrollView, RecyclerView recyclerView,
                        ItemData itemData, CategoryData categoryData, StoreData storeData) {

            super(itemView);
            setShopping(shopping);
            setAdapter(adapter);
            setItemData(itemData);
            setCategoryData(categoryData);
            setStoreData(storeData);
            setRecyclerView(recyclerView);
            setScrollView(scrollView);

            setItemName((TextView) itemView.findViewById(R.id.itemName));
            setTriangleDown((ImageView) itemView.findViewById(R.id.triangleDown));
            setTriangleUp((ImageView) itemView.findViewById(R.id.triangleUp));

            getTriangleDown().setOnClickListener(this);
            getTriangleUp().setOnClickListener(this);
        }

        private ReorderItemsRVH getThis() {
            return this;
        }

        public Shopping getShopping() {
            return shopping;
        }

        public void setShopping(Shopping shopping) {
            getThis().shopping = shopping;
        }

        public ReorderItemsRVA getAdapter() {
            return adapter;
        }

        public void setAdapter(ReorderItemsRVA adapter) {
            getThis().adapter = adapter;
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }

        public void setRecyclerView(RecyclerView recyclerView) {
            getThis().recyclerView = recyclerView;
        }

        public ScrollView getScrollView() {
            return scrollView;
        }

        public void setScrollView(ScrollView scrollView) {
            getThis().scrollView = scrollView;
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

        public TextView getItemName() {
            return itemName;
        }

        public void setItemName(TextView itemName) {
            getThis().itemName = itemName;
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

        private boolean storeContains(int storePosition, int itemPosition) {
            int index = 0;
            for (int i = 0; i < getStoreData().getStoreList().size(); i++) {
                String store = getStoreData().getStoreList().get(i);
                int numItemsInStore;
                if (getItemData().getStoreMap().get(store) == null) {
                    numItemsInStore = 0;
                } else {
                    numItemsInStore = getItemData().getStoreMap().get(store).getStoreItemsList().size();
                }
                index += numItemsInStore + 1;
                if (itemPosition == index) return false;
                if (index > itemPosition) {
                    return storePosition == i;
                }
            }
            return false;
        }

        public void onClick(View v) {
            int id = v.getId();
            int position = getAdapterPosition();
            if (id == getTriangleDown().getId()) {
                if (position == getAdapter().getCategoryList().size() - 1) {
                    // Down arrow on last item
                    return;
                }
                getAdapter().swapOrderByCategory(position, position + 1);
                //dbStoreHelper.swapOrder(position, position + 1);
                getShopping().updateItemData();
                getShopping().setReorderItemsRecyclerViewState(getRecyclerView().getLayoutManager().onSaveInstanceState());
                //getShopping().reorderItemsScrollViewState = scrollView.getLayoutManager().onSaveInstanceState();
                getShopping().loadFragment(new ReorderItems());
            } else if (id == getTriangleUp().getId()) {
                if (position == 0) {
                    // Up Arrow on first item
                    return;
                }
                getAdapter().swapOrderByCategory(position - 1, position);
                //dbStoreHelper.swapOrder(position - 1, position);
                getShopping().updateItemData();
                getShopping().setReorderItemsRecyclerViewState(getRecyclerView().getLayoutManager().onSaveInstanceState());
                //getShopping().reorderItemsScrollViewState = scrollView.getLayoutManager().onSaveInstanceState();
                getShopping().loadFragment(new ReorderItems());
            }
        }
    }
}