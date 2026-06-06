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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();
        view = inflater.inflate(R.layout.shopping_list, container, false);

        shopping = (Shopping) getActivity();
        itemData = shopping.getItemData();
        statusData = shopping.getStatusData();
        storeData = shopping.getStoreData();
        itemData.updateStatuses(statusData);
        dbStatusHelper  = new DBStatusHelper(getActivity());
        dbStoreHelper  = new DBStoreHelper(getActivity());

        shoppingListRecyclerView = view.findViewById(R.id.shoppingListRecyclerView);
        shoppingListRecyclerView.setHasFixedSize(false);
        shoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        shoppingListAdapter = new ShoppingListRVA(shopping, itemData, storeData);
        shoppingListRecyclerView.setAdapter(shoppingListAdapter);
        shoppingListRecyclerView.getLayoutManager().onRestoreInstanceState(shopping.getShoppingListViewState());

        shoppingListTitle = view.findViewById(R.id.shoppingListTitle);
        shoppingListLeftArrow = view.findViewById(R.id.shoppingListLeftArrow);
        shoppingListRightArrow = view.findViewById(R.id.shoppingListRightArrow);
        clearCheckedItems = view.findViewById(R.id.clearCheckedItems);
        editSelectedItem = view.findViewById(R.id.editSelectedItem);

        if (shopping.getStoreListOrderNum() == 0) {
            shoppingListTitle.setText(getString(R.string.allStores));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) shoppingListTitle.getLayoutParams();
            params.bottomMargin = 0;
            shoppingListTitle.setLayoutParams(params);
        } else {
            shoppingListTitle.setText(storeData.getStoreList().get(shopping.getStoreListOrderNum() - 1));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) shoppingListTitle.getLayoutParams();
            params.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
            shoppingListTitle.setLayoutParams(params);
        }

        shoppingListLeftArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                moveLeftInShoppingList();
            }
        });

        shoppingListRightArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                moveRightInShoppingList();
            }
        });

        clearCheckedItems.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.clearItems);
                builder.setMessage(R.string.wantToClear);
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i =  0; i < itemData.getItemListAZ().size(); i++) {
                            Item item  = itemData.getItemListAZ().get(i);
                            if (item.getStatus().isChecked()) {
                                item.getStatus().setAsInStock();
                                item.getStatus().setAsUnchecked();
                                dbStatusHelper.updateStatus(item.getName(), getContext().getString(R.string.instock), getContext().getString(R.string.unchecked));
                                shopping.updateStatusData();

                                String store = item.getStore().toString();
                                int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(store);
                                int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(store);
                                int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(store);
                                int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(store);
                                dbStoreHelper.setStoreViews(store, numItemsViewAll, (numItemsInStock + 1), (numItemsNeeded - 1), numItemsPaused);
                                shopping.updateStoreData();

                                shopping.setShoppingListViewState(shoppingListRecyclerView.getLayoutManager().onSaveInstanceState());
                                shopping.loadFragment(new ShoppingList());
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

        editSelectedItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (shopping.itemIsSelectedInShoppingList()) {
                    shopping.setShoppingListViewState(shoppingListRecyclerView.getLayoutManager().onSaveInstanceState());
                    shopping.setEditItemInInventory(false);
                    shopping.setEditItemInSearchResults(false);
                    shopping.setEditItemInShoppingList(true);
                    shopping.loadFragment(new EditItem());
                } else {
                    shopping.showAlertDialog(getString(R.string.editItem), getString(R.string.selectItemToEdit), getString(R.string.ok));
                }
            }
        });

        shoppingListRecyclerView.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
            void onSwipeLeft() {
                if (shopping.getSwipingOption().equals(Shopping.SWIPING_ON)) {
                    moveLeftInShoppingList();
                }
            }
            void onSwipeRight() {
                if (shopping.getSwipingOption().equals(Shopping.SWIPING_ON)) {
                    moveRightInShoppingList();
                }
            }
        });

        return view;
    }

    private void moveLeftInShoppingList() {
        if (shopping.getStoreListOrderNum() == 0) shopping.setStoreListOrderNum(storeData.getStoreList().size());
        else shopping.setStoreListOrderNum(shopping.getStoreListOrderNum() - 1);

        while (shopping.getStoreListOrderNum() != 0) {
            String storeName = storeData.getStoreList().get(shopping.getStoreListOrderNum() - 1);
            if (storeData.getStoreViewNeededMap().get(storeName) > 0) break;
            else shopping.setStoreListOrderNum(shopping.getStoreListOrderNum() - 1);
        }

        if (shopping.getStoreListOrderNum() == 0) shoppingListTitle.setText(getString(R.string.allStores));
        else shoppingListTitle.setText(storeData.getStoreList().get(shopping.getStoreListOrderNum() - 1));
        shopping.loadFragment(new ShoppingList());
    }

    private void moveRightInShoppingList() {
        if (shopping.getStoreListOrderNum() == storeData.getStoreList().size()) shopping.setStoreListOrderNum(0);
        else shopping.setStoreListOrderNum(shopping.getStoreListOrderNum() + 1);

        while (shopping.getStoreListOrderNum() != storeData.getStoreList().size()) {
            if (shopping.getStoreListOrderNum() == 0) break;
            String storeName = storeData.getStoreList().get(shopping.getStoreListOrderNum() - 1);
            if (storeData.getStoreViewNeededMap().get(storeName) > 0) break;
            else shopping.setStoreListOrderNum(shopping.getStoreListOrderNum() + 1);
        }

        if (shopping.getStoreListOrderNum() == storeData.getStoreList().size()) {
            String storeName = storeData.getStoreList().get(shopping.getStoreListOrderNum() - 1);
            if (storeData.getStoreViewNeededMap().get(storeName) <= 0) shopping.setStoreListOrderNum(0);
        }

        if (shopping.getStoreListOrderNum() == 0) shoppingListTitle.setText(getString(R.string.allStores));
        else shoppingListTitle.setText(storeData.getStoreList().get(shopping.getStoreListOrderNum() - 1));
        shopping.loadFragment(new ShoppingList());

    }

    private class OnSwipeTouchListener implements View.OnTouchListener {

        private GestureDetector gestureDetector;

        OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        void onSwipeLeft() {}

        void onSwipeRight() {}

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
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
        shopping.setShoppingListViewState(shoppingListRecyclerView.getLayoutManager().onSaveInstanceState());
        shoppingListRecyclerView.setAdapter(null);
        super.onDestroyView();
    }
}