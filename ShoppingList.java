package ryan.android.shopping;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShoppingList extends Fragment {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private StatusData statusData;
    private StoreData storeData;
    private DBStatusHelper dbStatusHelper;
    private DBStoreHelper dbStoreHelper;

    private TextView shoppingListTitle;
    private RecyclerView shoppingListRecyclerView;
    private ShoppingListRVA shoppingListAdapter;
    private TextView shoppingListLeftArrow;
    private TextView shoppingListRightArrow;
    private Button clearCheckedItems;
    private Button editSelectedItem;

    public ShoppingList() {}

    private ShoppingList getThis() {
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

    private ItemData getItemData() {
        return itemData;
    }

    private void setItemData(ItemData itemData) {
        getThis().itemData = itemData;
    }

    private StatusData getStatusData() {
        return statusData;
    }

    private void setStatusData(StatusData statusData) {
        getThis().statusData = statusData;
    }

    private StoreData getStoreData() {
        return storeData;
    }

    private void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
    }

    private DBStatusHelper getDbStatusHelper() {
        return dbStatusHelper;
    }

    private void setDbStatusHelper(DBStatusHelper dbStatusHelper) {
        getThis().dbStatusHelper = dbStatusHelper;
    }

    private DBStoreHelper getDbStoreHelper() {
        return dbStoreHelper;
    }

    private void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
        getThis().dbStoreHelper = dbStoreHelper;
    }

    private TextView getShoppingListTitle() {
        return shoppingListTitle;
    }

    private void setShoppingListTitle(TextView shoppingListTitle) {
        getThis().shoppingListTitle = shoppingListTitle;
    }

    private RecyclerView getShoppingListRecyclerView() {
        return shoppingListRecyclerView;
    }

    private void setShoppingListRecyclerView(RecyclerView shoppingListRecyclerView) {
        getThis().shoppingListRecyclerView = shoppingListRecyclerView;
    }

    private ShoppingListRVA getShoppingListAdapter() {
        return shoppingListAdapter;
    }

    private void setShoppingListAdapter(ShoppingListRVA shoppingListAdapter) {
        getThis().shoppingListAdapter = shoppingListAdapter;
    }

    private TextView getShoppingListLeftArrow() {
        return shoppingListLeftArrow;
    }

    private void setShoppingListLeftArrow(TextView shoppingListLeftArrow) {
        getThis().shoppingListLeftArrow = shoppingListLeftArrow;
    }

    private TextView getShoppingListRightArrow() {
        return shoppingListRightArrow;
    }

    private void setShoppingListRightArrow(TextView shoppingListRightArrow) {
        getThis().shoppingListRightArrow = shoppingListRightArrow;
    }

    private Button getClearCheckedItems() {
        return clearCheckedItems;
    }

    private void setClearCheckedItems(Button clearCheckedItems) {
        getThis().clearCheckedItems = clearCheckedItems;
    }

    private Button getEditSelectedItem() {
        return editSelectedItem;
    }

    private void setEditSelectedItem(Button editSelectedItem) {
        getThis().editSelectedItem = editSelectedItem;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();
        setView(inflater.inflate(R.layout.shopping_list, container, false));

        setShopping((Shopping) getActivity());
        setItemData(getShopping().getItemData());
        setStatusData(getShopping().getStatusData());
        setStoreData(getShopping().getStoreData());
        getItemData().updateStatuses(getStatusData());
        setDbStatusHelper(new DBStatusHelper(getActivity()));
        setDbStoreHelper(new DBStoreHelper(getActivity()));

        setShoppingListRecyclerView((RecyclerView) getView().findViewById(R.id.shoppingListRecyclerView));
        getShoppingListRecyclerView().setHasFixedSize(false);
        getShoppingListRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setShoppingListAdapter(new ShoppingListRVA(getView(), getShopping(), getItemData(), getStoreData()));
        getShoppingListRecyclerView().setAdapter(getShoppingListAdapter());
        getShoppingListRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getShoppingListViewState());

        setShoppingListTitle((TextView) getView().findViewById(R.id.shoppingListTitle));
        setShoppingListLeftArrow((TextView) getView().findViewById(R.id.shoppingListLeftArrow));
        setShoppingListRightArrow((TextView) getView().findViewById(R.id.shoppingListRightArrow));
        setClearCheckedItems((Button) getView().findViewById(R.id.clearCheckedItems));
        setEditSelectedItem((Button) getView().findViewById(R.id.editSelectedItem));

        if (getShopping().getStoreListOrderNum() == 0) {
            getShoppingListTitle().setText(getString(R.string.allStores));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getShoppingListTitle().getLayoutParams();
            params.bottomMargin = 0;
            getShoppingListTitle().setLayoutParams(params);
        } else {
            getShoppingListTitle().setText(getStoreData().getStoreList().get(getShopping().getStoreListOrderNum() - 1));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getShoppingListTitle().getLayoutParams();
            params.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
            getShoppingListTitle().setLayoutParams(params);
        }

        getShoppingListLeftArrow().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                moveLeftInShoppingList();
            }
        });

        getShoppingListRightArrow().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.showAlertDialog(getString(R.string.clearItems),  getString(R.string.wantToClear), getString(R.string.y));
                //moveRightInShoppingList();
            }
        });

        getClearCheckedItems().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.clearItems);
                builder.setMessage(R.string.wantToClear);
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i =  0; i < getItemData().getItemListAZ().size(); i++) {
                            Item item  = getItemData().getItemListAZ().get(i);
                            if (item.getStatus().isChecked()) {
                                item.getStatus().setAsInStock();
                                item.getStatus().setAsUnchecked();
                                getDbStatusHelper().updateStatus(item.getItemName(), getString(R.string.instock), getString(R.string.unchecked));
                                getShopping().updateStatusData();

                                String store = item.getStore().toString();
                                int numItemsViewAll = getShopping().getStoreData().getStoreViewAllMap().get(store);
                                int numItemsInStock = getShopping().getStoreData().getStoreViewInStockMap().get(store);
                                int numItemsNeeded = getShopping().getStoreData().getStoreViewNeededMap().get(store);
                                int numItemsPaused = getShopping().getStoreData().getStoreViewPausedMap().get(store);
                                getDbStoreHelper().setStoreViews(store, numItemsViewAll, (numItemsInStock + 1), (numItemsNeeded - 1), numItemsPaused);
                                getShopping().updateStoreData();

                                getShopping().setShoppingListViewState(getShoppingListRecyclerView().getLayoutManager().onSaveInstanceState());
                                getShopping().loadFragment(new ShoppingList());
                            }
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.getWindow().setDimAmount(0.2f);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        getEditSelectedItem().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getShopping().itemIsSelectedInShoppingList()) {
                    getShopping().setShoppingListViewState(getShoppingListRecyclerView().getLayoutManager().onSaveInstanceState());
                    getShopping().setEditItemInInventory(false);
                    getShopping().setEditItemInSearchResults(false);
                    getShopping().setEditItemInShoppingList(true);
                    getShopping().loadFragment(new EditItem());
                } else {
                    getShopping().showAlertDialog(getString(R.string.editItem), getString(R.string.selectItemToEdit), getString(R.string.ok));
                }
            }
        });

        getShoppingListRecyclerView().setOnTouchListener(new OnSwipeTouchListener(getView().getContext()) {
            void onSwipeLeft() {
                if (getShopping().getSwipingOption().equals(Shopping.SWIPING_ON)) {
                    moveLeftInShoppingList();
                }
            }
            void onSwipeRight() {
                if (getShopping().getSwipingOption().equals(Shopping.SWIPING_ON)) {
                    moveRightInShoppingList();
                }
            }
        });

        return getView();
    }

    private void moveLeftInShoppingList() {
        if (getShopping().getStoreListOrderNum() == 0) getShopping().setStoreListOrderNum(getStoreData().getStoreList().size());
        else getShopping().setStoreListOrderNum(getShopping().getStoreListOrderNum() - 1);

        while (getShopping().getStoreListOrderNum() != 0) {
            String storeName = getStoreData().getStoreList().get(getShopping().getStoreListOrderNum() - 1);
            if (getStoreData().getStoreViewNeededMap().get(storeName) > 0) break;
            else getShopping().setStoreListOrderNum(getShopping().getStoreListOrderNum() - 1);
        }

        if (getShopping().getStoreListOrderNum() == 0) getShoppingListTitle().setText(getString(R.string.allStores));
        else getShoppingListTitle().setText(getStoreData().getStoreList().get(getShopping().getStoreListOrderNum() - 1));
        getShopping().loadFragment(new ShoppingList());
    }

    private void moveRightInShoppingList() {
        if (getShopping().getStoreListOrderNum() == getStoreData().getStoreList().size()) getShopping().setStoreListOrderNum(0);
        else getShopping().setStoreListOrderNum(getShopping().getStoreListOrderNum() + 1);

        while (getShopping().getStoreListOrderNum() != getStoreData().getStoreList().size()) {
            if (getShopping().getStoreListOrderNum() == 0) break;
            String storeName = getStoreData().getStoreList().get(getShopping().getStoreListOrderNum() - 1);
            if (getStoreData().getStoreViewNeededMap().get(storeName) > 0) break;
            else getShopping().setStoreListOrderNum(getShopping().getStoreListOrderNum() + 1);
        }

        if (getShopping().getStoreListOrderNum() == getStoreData().getStoreList().size()) {
            String storeName = getStoreData().getStoreList().get(getShopping().getStoreListOrderNum() - 1);
            if (getStoreData().getStoreViewNeededMap().get(storeName) <= 0) getShopping().setStoreListOrderNum(0);
        }

        if (getShopping().getStoreListOrderNum() == 0) getShoppingListTitle().setText(getString(R.string.allStores));
        else getShoppingListTitle().setText(getStoreData().getStoreList().get(getShopping().getStoreListOrderNum() - 1));
        getShopping().loadFragment(new ShoppingList());

    }

    private class OnSwipeTouchListener implements View.OnTouchListener {

        private Context context;
        private GestureDetector gestureDetector;

        private OnSwipeTouchListener(Context context) {
            setContext(context);
            setGestureDetector(new GestureDetector(getContext(), new GestureListener()));
        }

        private GestureDetector getGestureDetector() {
            return gestureDetector;
        }

        private void setGestureDetector(GestureDetector gestureDetector) {
            getThis().gestureDetector = gestureDetector;
        }

        private OnSwipeTouchListener getThis() {
            return this;
        }

        private Context getContext() {
            return context;
        }

        private void setContext(Context context) {
            getThis().context = context;
        }

        private void onSwipeLeft() {}

        private void onSwipeRight() {}

        public boolean onTouch(View v, MotionEvent event) {
            return getGestureDetector().onTouchEvent(event);
        }

        private class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 200;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) return false;
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > (3 * Math.abs(distanceY)) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                        && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0) onSwipeRight();
                    else onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }

    public void onDestroyView() {
        getShopping().setShoppingListViewState(getShoppingListRecyclerView().getLayoutManager().onSaveInstanceState());
        getShoppingListRecyclerView().setAdapter(null);
        super.onDestroyView();
    }
}