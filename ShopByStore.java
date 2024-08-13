package ryan.android.shopping;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShopByStore extends Fragment {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private StatusData statusData;
    private StoreData storeData;
    private DBStatusHelper dbStatusHelper;
    private DBStoreHelper dbStoreHelper;

    private TextView byStoreTitle;
    private Button markAsPaused;
    private Button markAsInStock;
    private RecyclerView shopByStoreRecyclerView;

    public ShopByStore() {}

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        container.removeAllViews();
        view = inflater.inflate(R.layout.shop_by_store, container, false);

        dbStatusHelper = new DBStatusHelper(getActivity());
        dbStoreHelper = new DBStoreHelper(getActivity());

        shopping = (Shopping) getActivity();
        itemData = shopping.getItemData();
        statusData = shopping.getStatusData();
        storeData = shopping.getStoreData();
        itemData.updateStatuses(statusData);

        shopByStoreRecyclerView = view.findViewById(R.id.byStoreRecyclerView);
        shopByStoreRecyclerView.setHasFixedSize(false);
        shopByStoreRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ShopByStoreRVA adapter = new ShopByStoreRVA(shopping, itemData, storeData);
        shopByStoreRecyclerView.setAdapter(adapter);
        shopByStoreRecyclerView.getLayoutManager().onRestoreInstanceState(shopping.shopByStoreViewState);

        byStoreTitle = view.findViewById(R.id.byStoreTitle);
        markAsPaused = view.findViewById(R.id.markAsPaused);
        markAsInStock = view.findViewById(R.id.markAsInStock);

        if (shopping.storeNum == 0) byStoreTitle.setText("All Stores");
        else byStoreTitle.setText(storeData.getStoreList().get(shopping.storeNum - 1));

        markAsPaused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < itemData.getItemList().size(); i++) {
                    //Toast.makeText(shopping, "i = " + i + ", item =  " + itemData.getItemList().get(i).getName(), Toast.LENGTH_SHORT).show();
                    if (shopping.getCheckedList().get(i)) {
                        Toast.makeText(shopping, "i = " + i, Toast.LENGTH_SHORT).show();
                        Item item = shopping.getItemData().getItemList().get(i);
                        item.getStatus().setAsPaused();
                        item.getStatus().setAsUnchecked();
                        //shopping.getItemData().getItemList().get(i).getStatus().setAsPaused();
                        //shopping.getItemData().getItemList().get(i).getStatus().setAsUnchecked();
                        shopping.getCheckedList().set(i, false);
                        dbStatusHelper.updateStatus(item.getName(), "false", "false", "true");
                        //shopping.updateStatusData();
                        //---------------------------------
                        Store thisStore = item.getStore(0);
                        int numItemsNeeded = shopping.getStoreData().getItemsNeededMap().get(thisStore.toString());
                        //Toast.makeText(shopping, thisStore.toString() + " has " + (numItemsNeeded) + " items needed.", Toast.LENGTH_SHORT).show();
                        dbStoreHelper.setItemsNeeded(thisStore.toString(), numItemsNeeded - 1);
                        shopping.updateStoreData();
                    }
                }
                shopping.shopByStoreViewState = shopByStoreRecyclerView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ShopByStore());
            }
        });

        markAsInStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < itemData.getItemList().size(); i++) {
                    if (shopping.getCheckedList().get(i)) {
                        Item item = shopping.getItemData().getItemList().get(i);
                        item.getStatus().setAsInStock();
                        item.getStatus().setAsUnchecked();
                        shopping.getCheckedList().set(i, false);
                        dbStatusHelper.updateStatus(item.getName(), "true", "false", "false");
                        //---------------------------------
                        Store thisStore = item.getStore(0);
                        int numItemsNeeded = shopping.getStoreData().getItemsNeededMap().get(thisStore.toString());
                        Toast.makeText(shopping, thisStore.toString() + " has " + (numItemsNeeded - 1) + " items needed.", Toast.LENGTH_SHORT).show();
                        dbStoreHelper.setItemsNeeded(thisStore.toString(), numItemsNeeded - 1);
                        shopping.updateStoreData();
                    }
                }
                shopping.shopByStoreViewState = shopByStoreRecyclerView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ShopByStore());
            }
        });

        shopByStoreRecyclerView.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
            @Override
            public void onSwipeRight() {
                if (shopping.storeNum == 0) shopping.storeNum = storeData.getStoreList().size();
                else shopping.storeNum--;

                while (shopping.storeNum != 0) {
                    String storeName = storeData.getStoreList().get(shopping.storeNum - 1);
                    if (storeData.getItemsNeededMap().get(storeName) > 0) break;
                    else shopping.storeNum--;
                }

                if (shopping.storeNum == 0) byStoreTitle.setText("All Stores");
                else byStoreTitle.setText(storeData.getStoreList().get(shopping.storeNum - 1));
                shopping.loadFragment(new ShopByStore());
            }
            @Override
            public void onSwipeLeft() {
                if (shopping.storeNum == storeData.getStoreList().size()) shopping.storeNum = 0;
                else shopping.storeNum++;

                while (shopping.storeNum != storeData.getStoreList().size()) {
                    if (shopping.storeNum == 0) break;
                    String storeName = storeData.getStoreList().get(shopping.storeNum - 1);
                    if (storeData.getItemsNeededMap().get(storeName) > 0) break;
                    else shopping.storeNum++;
                }

                if (shopping.storeNum == storeData.getStoreList().size()) {
                    String storeName = storeData.getStoreList().get(shopping.storeNum - 1);
                    if (storeData.getItemsNeededMap().get(storeName) <= 0) shopping.storeNum = 0;
                }

                if (shopping.storeNum == 0) byStoreTitle.setText("All Stores");
                else byStoreTitle.setText(storeData.getStoreList().get(shopping.storeNum - 1));
                shopping.loadFragment(new ShopByStore());
            }

        });

        return view;
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 == null || e2 == null) return false;
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }

    @Override
    public void onDestroyView() {
        shopping.shopByStoreViewState = shopByStoreRecyclerView.getLayoutManager().onSaveInstanceState();
        shopByStoreRecyclerView.setAdapter(null);
        super.onDestroyView();
    }

}
