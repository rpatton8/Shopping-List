package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
    private TextView byStoreLeftArrow;
    private TextView byStoreRightArrow;
    private Button clearCheckedItems;
    private Button editSelectedItem;
    private RecyclerView shopByStoreRecyclerView;

    public ShopByStore() {}

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
        byStoreLeftArrow = view.findViewById(R.id.byStoreLeftArrow);
        byStoreRightArrow = view.findViewById(R.id.byStoreRightArrow);
        clearCheckedItems = view.findViewById(R.id.clearCheckedItems);
        editSelectedItem = view.findViewById(R.id.editSelectedItem);

        if (shopping.storeNum == 0) {
            byStoreTitle.setText("All Stores");
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) byStoreTitle.getLayoutParams();
            params.bottomMargin = 0;
            byStoreTitle.setLayoutParams(params);
        } else {
            byStoreTitle.setText(storeData.getStoreList().get(shopping.storeNum - 1));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) byStoreTitle.getLayoutParams();
            params.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
            byStoreTitle.setLayoutParams(params);
        }

        byStoreLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopping.storeNum == 0) shopping.storeNum = storeData.getStoreList().size();
                else shopping.storeNum--;

                while (shopping.storeNum != 0) {
                    String storeName = storeData.getStoreList().get(shopping.storeNum - 1);
                    if (storeData.getStoreItemsNeededMap().get(storeName) > 0) break;
                    else shopping.storeNum--;
                }

                if (shopping.storeNum == 0) byStoreTitle.setText("All Stores");
                else byStoreTitle.setText(storeData.getStoreList().get(shopping.storeNum - 1));
                shopping.loadFragment(new ShopByStore());
            }
        });

        byStoreRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopping.storeNum == storeData.getStoreList().size()) shopping.storeNum = 0;
                else shopping.storeNum++;

                while (shopping.storeNum != storeData.getStoreList().size()) {
                    if (shopping.storeNum == 0) break;
                    String storeName = storeData.getStoreList().get(shopping.storeNum - 1);
                    if (storeData.getStoreItemsNeededMap().get(storeName) > 0) break;
                    else shopping.storeNum++;
                }

                if (shopping.storeNum == storeData.getStoreList().size()) {
                    String storeName = storeData.getStoreList().get(shopping.storeNum - 1);
                    if (storeData.getStoreItemsNeededMap().get(storeName) <= 0) shopping.storeNum = 0;
                }

                if (shopping.storeNum == 0) byStoreTitle.setText("All Stores");
                else byStoreTitle.setText(storeData.getStoreList().get(shopping.storeNum - 1));
                shopping.loadFragment(new ShopByStore());
            }
        });

        clearCheckedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < itemData.getItemList().size(); i++) {
                    if (shopping.getCheckedList().get(i)) {
                        Item item = shopping.getItemData().getItemList().get(i);
                        item.getStatus().setAsInStock();
                        item.getStatus().setAsUnchecked();
                        shopping.getCheckedList().set(i, false);
                        dbStatusHelper.updateStatus(item.getName(), "true", "false", "false");
                        shopping.updateStatusData();

                        Store thisStore = item.getStore(0);
                        int numItemsNeeded = shopping.getStoreData().getStoreItemsNeededMap().get(thisStore.toString());
                        dbStoreHelper.setStoreItemsNeeded(thisStore.toString(), numItemsNeeded - 1);
                        shopping.updateStoreData();
                    }
                }

                shopping.shopByStoreViewState = shopByStoreRecyclerView.getLayoutManager().onSaveInstanceState();
                shopping.loadFragment(new ShopByStore());
            }
        });

        editSelectedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopping.itemIsSelectedInShopByStore) {
                    shopping.shopByStoreViewState = shopByStoreRecyclerView.getLayoutManager().onSaveInstanceState();
                    shopping.editItemInInventory = false;
                    shopping.editItemInShopByStore = true;
                    shopping.loadFragment(new EditItem());
                } else {
                    Toast.makeText(getActivity(), "Please select an item to edit.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        shopping.shopByStoreViewState = shopByStoreRecyclerView.getLayoutManager().onSaveInstanceState();
        shopByStoreRecyclerView.setAdapter(null);
        super.onDestroyView();
    }

}
