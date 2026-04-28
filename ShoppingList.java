package ryan.android.shopping;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//@SuppressWarnings("ALL")
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
        shoppingListRecyclerView.getLayoutManager().onRestoreInstanceState(shopping.shoppingListViewState);

        shoppingListTitle = view.findViewById(R.id.shoppingListTitle);
        shoppingListLeftArrow = view.findViewById(R.id.shoppingListLeftArrow);
        shoppingListRightArrow = view.findViewById(R.id.shoppingListRightArrow);
        clearCheckedItems = view.findViewById(R.id.clearCheckedItems);
        editSelectedItem = view.findViewById(R.id.editSelectedItem);

        if (shopping.storeListOrderNum == 0) {
            shoppingListTitle.setText(R.string.allStores);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) shoppingListTitle.getLayoutParams();
            params.bottomMargin = 0;
            shoppingListTitle.setLayoutParams(params);
        } else {
            shoppingListTitle.setText(storeData.getStoreList().get(shopping.storeListOrderNum - 1));
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

        shoppingListRecyclerView.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
            public void onSwipeLeft() {
                moveLeftInShoppingList();
            }
            public void onSwipeRight() {
                moveRightInShoppingList();
            }
        });

        clearCheckedItems.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                for (int i =  0; i < itemData.getItemListAZ().size(); i++) {
                    Item item  = itemData.getItemListAZ().get(i);
                    if (item.getStatus().isChecked()) {
                        item.getStatus().setAsInStock();
                        item.getStatus().setAsUnchecked();
                        dbStatusHelper.updateStatus(item.getName(), "instock", "unchecked");
                        shopping.updateStatusData();

                        String store = item.getStore().toString();
                        int numItemsViewAll = shopping.getStoreData().getStoreViewAllMap().get(store);
                        int numItemsInStock = shopping.getStoreData().getStoreViewInStockMap().get(store);
                        int numItemsNeeded = shopping.getStoreData().getStoreViewNeededMap().get(store);
                        int numItemsPaused = shopping.getStoreData().getStoreViewPausedMap().get(store);
                        dbStoreHelper.setStoreViews(store, numItemsViewAll, (numItemsInStock + 1), (numItemsNeeded - 1), numItemsPaused);
                        shopping.updateStoreData();
                    }
                }

                shopping.shoppingListViewState = shoppingListRecyclerView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ShoppingList());
            }
        });

        editSelectedItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (shopping.itemIsSelectedInShoppingList) {
                    shopping.shoppingListViewState = shoppingListRecyclerView.getLayoutManager().onSaveInstanceState();
                    shopping.editItemInInventory = false;
                    shopping.editItemInShoppingList = true;
                    shopping.loadFragment(new EditItem());
                } else {
                    Toast toast = Toast.makeText(getActivity(), "Please select an item to edit.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            }
        });

        return view;
    }

    private void moveRightInShoppingList() {

        Toast.makeText(getActivity(), "Swipe Right", Toast.LENGTH_SHORT).show();

        if (shopping.storeListOrderNum == storeData.getStoreList().size()) shopping.storeListOrderNum = 0;
        else shopping.storeListOrderNum++;

        while (shopping.storeListOrderNum != storeData.getStoreList().size()) {
            if (shopping.storeListOrderNum == 0) break;
            String storeName = storeData.getStoreList().get(shopping.storeListOrderNum - 1);
            if (storeData.getStoreViewNeededMap().get(storeName) > 0) break;
            else shopping.storeListOrderNum++;
        }

        if (shopping.storeListOrderNum == storeData.getStoreList().size()) {
            String storeName = storeData.getStoreList().get(shopping.storeListOrderNum - 1);
            if (storeData.getStoreViewNeededMap().get(storeName) <= 0) shopping.storeListOrderNum = 0;
        }

        if (shopping.storeListOrderNum == 0) shoppingListTitle.setText(R.string.allStores);
        else shoppingListTitle.setText(storeData.getStoreList().get(shopping.storeListOrderNum - 1));
        shopping.loadFragment(new ShoppingList());

    }

    private void moveLeftInShoppingList() {

        Toast.makeText(getActivity(), "Swipe Left", Toast.LENGTH_SHORT).show();

        if (shopping.storeListOrderNum == 0) shopping.storeListOrderNum = storeData.getStoreList().size();
        else shopping.storeListOrderNum--;

        while (shopping.storeListOrderNum != 0) {
            String storeName = storeData.getStoreList().get(shopping.storeListOrderNum - 1);
            if (storeData.getStoreViewNeededMap().get(storeName) > 0) break;
            else shopping.storeListOrderNum--;
        }

        if (shopping.storeListOrderNum == 0) shoppingListTitle.setText(R.string.allStores);
        else shoppingListTitle.setText(storeData.getStoreList().get(shopping.storeListOrderNum - 1));
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
        shopping.shoppingListViewState = shoppingListRecyclerView.getLayoutManager().onSaveInstanceState();
        shoppingListRecyclerView.setAdapter(null);
        super.onDestroyView();
    }
}