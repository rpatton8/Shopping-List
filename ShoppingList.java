package ryan.android.shopping;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private AlertDialog alertDialog2;
    private View alertDialog2View;
    private TextView alertDialog2Title;
    private TextView alertDialog2Message;
    private TextView alertDialog2ButtonLeft;
    private TextView alertDialog2ButtonRight;
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

    private AlertDialog getAlertDialog2() {
        return alertDialog2;
    }

    private void setAlertDialog2(AlertDialog alertDialog2) {
        getThis().alertDialog2 = alertDialog2;
    }

    private View getAlertDialog2View() {
        return alertDialog2View;
    }

    private void setAlertDialog2View(View alertDialog2View) {
        getThis().alertDialog2View = alertDialog2View;
    }

    private TextView getAlertDialog2Title() {
        return alertDialog2Title;
    }

    private void setAlertDialog2Title(TextView alertDialog2Title) {
        getThis().alertDialog2Title = alertDialog2Title;
    }

    private TextView getAlertDialog2Message() {
        return alertDialog2Message;
    }

    private void setAlertDialog2Message(TextView alertDialog2Message) {
        getThis().alertDialog2Message = alertDialog2Message;
    }

    private TextView getAlertDialog2ButtonLeft() {
        return alertDialog2ButtonLeft;
    }

    private void setAlertDialog2ButtonLeft(TextView alertDialog2ButtonLeft) {
        getThis().alertDialog2ButtonLeft = alertDialog2ButtonLeft;
    }

    private TextView getAlertDialog2ButtonRight() {
        return alertDialog2ButtonRight;
    }

    private void setAlertDialog2ButtonRight(TextView alertDialog2ButtonRight) {
        getThis().alertDialog2ButtonRight = alertDialog2ButtonRight;
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

        setShoppingListRecyclerView(getView().findViewById(R.id.shoppingListRecyclerView));
        getShoppingListRecyclerView().setHasFixedSize(false);
        getShoppingListRecyclerView().setLayoutManager(new LinearLayoutManager(getView().getContext()));
        setShoppingListAdapter(new ShoppingListRVA(getView(), getShopping(), getItemData(), getStoreData()));
        getShoppingListRecyclerView().setAdapter(getShoppingListAdapter());
        getShoppingListRecyclerView().getLayoutManager().onRestoreInstanceState(getShopping().getShoppingListViewState());

        setShoppingListTitle(getView().findViewById(R.id.shoppingListTitle));
        setShoppingListLeftArrow(getView().findViewById(R.id.shoppingListLeftArrow));
        setShoppingListRightArrow(getView().findViewById(R.id.shoppingListRightArrow));
        setClearCheckedItems(getView().findViewById(R.id.clearCheckedItems));
        setEditSelectedItem(getView().findViewById(R.id.editSelectedItem));

        if (getShopping().getShoppingListStoreOrderNum() == 0) {
            getShoppingListTitle().setText(getString(R.string.allStores));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getShoppingListTitle().getLayoutParams();
            params.bottomMargin = 0;
            getShoppingListTitle().setLayoutParams(params);
        } else {
            getShoppingListTitle().setText(getStoreData().getStoreList().get(getShopping().getShoppingListStoreOrderNum() - 1));
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
                moveRightInShoppingList();
            }
        });

        getClearCheckedItems().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                setAlertDialog2View(inflater.inflate(R.layout.alert_dialog_2, null));
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(getAlertDialog2View());

                setAlertDialog2Title(alertDialog2View.findViewById(R.id.alertDialog2Title));
                getAlertDialog2Title().setText(R.string.clearItems);

                setAlertDialog2Message(alertDialog2View.findViewById(R.id.alertDialog2Message));
                getAlertDialog2Message().setText(R.string.wantToClear);

                setAlertDialog2ButtonLeft(alertDialog2View.findViewById(R.id.alertDialog2ButtonLeft));
                getAlertDialog2ButtonLeft().setText(R.string.no);

                setAlertDialog2ButtonRight(alertDialog2View.findViewById(R.id.alertDialog2ButtonRight));
                getAlertDialog2ButtonRight().setText(R.string.yes);

                getAlertDialog2ButtonLeft().setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        getAlertDialog2().dismiss();
                    }
                });

                getAlertDialog2ButtonRight().setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
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
                        getAlertDialog2().dismiss();
                    }
                });

                setAlertDialog2(builder.create());
                getAlertDialog2().getWindow().setDimAmount(0.2f);
                getAlertDialog2().setCanceledOnTouchOutside(false);
                getAlertDialog2().setCancelable(false);
                getAlertDialog2().show();
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
                if (Shopping.SWIPING_ON.equals(getShopping().getSwipingOption())) {
                    moveLeftInShoppingList();
                }
            }
            void onSwipeRight() {
                if (Shopping.SWIPING_ON.equals(getShopping().getSwipingOption())) {
                    moveRightInShoppingList();
                }
            }
        });

        return getView();
    }

    private void moveLeftInShoppingList() {
        if (getShopping().getShoppingListStoreOrderNum() == 0) getShopping().setShoppingListStoreOrderNum(getStoreData().getStoreList().size());
        else getShopping().setShoppingListStoreOrderNum(getShopping().getShoppingListStoreOrderNum() - 1);

        while (getShopping().getShoppingListStoreOrderNum() != 0) {
            String storeName = getStoreData().getStoreList().get(getShopping().getShoppingListStoreOrderNum() - 1);
            if (getStoreData().getStoreViewNeededMap().get(storeName) > 0) break;
            else getShopping().setShoppingListStoreOrderNum(getShopping().getShoppingListStoreOrderNum() - 1);
        }

        if (getShopping().getShoppingListStoreOrderNum() == 0) getShoppingListTitle().setText(getString(R.string.allStores));
        else getShoppingListTitle().setText(getStoreData().getStoreList().get(getShopping().getShoppingListStoreOrderNum() - 1));
        getShopping().loadFragment(new ShoppingList());
    }

    private void moveRightInShoppingList() {
        if (getShopping().getShoppingListStoreOrderNum() == getStoreData().getStoreList().size()) getShopping().setShoppingListStoreOrderNum(0);
        else getShopping().setShoppingListStoreOrderNum(getShopping().getShoppingListStoreOrderNum() + 1);

        while (getShopping().getShoppingListStoreOrderNum() != getStoreData().getStoreList().size()) {
            if (getShopping().getShoppingListStoreOrderNum() == 0) break;
            String storeName = getStoreData().getStoreList().get(getShopping().getShoppingListStoreOrderNum() - 1);
            if (getStoreData().getStoreViewNeededMap().get(storeName) > 0) break;
            else getShopping().setShoppingListStoreOrderNum(getShopping().getShoppingListStoreOrderNum() + 1);
        }

        if (getShopping().getShoppingListStoreOrderNum() == getStoreData().getStoreList().size()) {
            String storeName = getStoreData().getStoreList().get(getShopping().getShoppingListStoreOrderNum() - 1);
            if (getStoreData().getStoreViewNeededMap().get(storeName) <= 0) getShopping().setShoppingListStoreOrderNum(0);
        }

        if (getShopping().getShoppingListStoreOrderNum() == 0) getShoppingListTitle().setText(getString(R.string.allStores));
        else getShoppingListTitle().setText(getStoreData().getStoreList().get(getShopping().getShoppingListStoreOrderNum() - 1));
        getShopping().loadFragment(new ShoppingList());

    }

    private static class OnSwipeTouchListener implements View.OnTouchListener {

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