package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;

public class ShoppingList extends Fragment {

    private Shopping shopping;
    private StoreData storeData;

    private TextView shoppingListTitle;
    private RecyclerView shoppingListRecyclerView;

    public ShoppingList() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        container.removeAllViews();
        View view = inflater.inflate(R.layout.shopping_list, container, false);

        shopping = (Shopping) getActivity();
        ItemData  itemData = shopping.getItemData();
        StatusData statusData = shopping.getStatusData();
        storeData = shopping.getStoreData();
        itemData.updateStatuses(statusData);

        shoppingListRecyclerView = view.findViewById(R.id.shoppingListRecyclerView);
        shoppingListRecyclerView.setHasFixedSize(false);
        shoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ShoppingListRVA adapter = new ShoppingListRVA(shopping, itemData, storeData);
        shoppingListRecyclerView.setAdapter(adapter);
        Objects.requireNonNull(shoppingListRecyclerView.getLayoutManager()).onRestoreInstanceState(shopping.shoppingListViewState);

        shoppingListTitle = view.findViewById(R.id.shoppingListTitle);
        TextView shoppingListLeftArrow = view.findViewById(R.id.shoppingListLeftArrow);
        TextView shoppingListRightArrow = view.findViewById(R.id.shoppingListRightArrow);
        Button clearCheckedItems = view.findViewById(R.id.clearCheckedItems);
        Button editSelectedItem = view.findViewById(R.id.editSelectedItem);

        if (shopping.storeListOrderNum == 0) {
            shoppingListTitle.setText("All Stores");
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
            @Override
            public void onClick(View view) {
                if (shopping.storeListOrderNum == 0) shopping.storeListOrderNum = storeData.getStoreList().size();
                else shopping.storeListOrderNum--;

                while (shopping.storeListOrderNum != 0) {
                    String storeName = storeData.getStoreList().get(shopping.storeListOrderNum - 1);
                    if (storeData.getStoreViewNeededMap().get(storeName) > 0) break;
                    else shopping.storeListOrderNum--;
                }

                if (shopping.storeListOrderNum == 0) shoppingListTitle.setText("All Stores");
                else shoppingListTitle.setText(storeData.getStoreList().get(shopping.storeListOrderNum - 1));
                shopping.loadFragment(new ShoppingList());
            }
        });

        shoppingListRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                if (shopping.storeListOrderNum == 0) shoppingListTitle.setText("All Stores");
                else shoppingListTitle.setText(storeData.getStoreList().get(shopping.storeListOrderNum - 1));
                shopping.loadFragment(new ShoppingList());
            }
        });

        clearCheckedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*for (int i = 0; i < itemData.getItemList().size(); i++) {
                    if (shopping.getCheckedList().get(i)) {
                        Item item = shopping.getItemData().getItemList().get(i);
                        item.getStatus().setAsInStock();
                        item.getStatus().setAsUnchecked();
                        shopping.getCheckedList().set(i, false);
                        //dbStatusHelper.updateStatus(item.getName(), "true", "false", "false");
                        shopping.updateStatusData();

                        Store thisStore = item.getStore(0);
                        int numItemsNeeded = shopping.getStoreData().getStoreItemsNeededMap().get(thisStore.toString());
                        dbStoreHelper.setStoreItemsNeeded(thisStore.toString(), numItemsNeeded - 1);
                        shopping.updateStoreData();
                    }
                }*/

                shopping.shoppingListViewState = Objects.requireNonNull(shoppingListRecyclerView.getLayoutManager()).onSaveInstanceState();
                shopping.loadFragment(new ShoppingList());
            }
        });

        editSelectedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopping.itemIsSelectedInShoppingList) {
                    shopping.shoppingListViewState = Objects.requireNonNull(shoppingListRecyclerView.getLayoutManager()).onSaveInstanceState();
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

    @Override
    public void onDestroyView() {
        shopping.shoppingListViewState = Objects.requireNonNull(shoppingListRecyclerView.getLayoutManager()).onSaveInstanceState();
        shoppingListRecyclerView.setAdapter(null);
        super.onDestroyView();
    }

}