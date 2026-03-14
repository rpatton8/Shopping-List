package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class RemoveItem extends Fragment {

    private Shopping shopping;
    private ItemData itemData;
    private DBItemHelper dbItemHelper;
    private DBStatusHelper dbStatusHelper;
    private EditText itemNameInput;

    public RemoveItem() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.remove_item, container, false);

        shopping = (Shopping) getActivity();
        itemData = shopping.getItemData();
        dbItemHelper = new DBItemHelper(getActivity());
        dbStatusHelper = new DBStatusHelper(getActivity());

        itemNameInput = view.findViewById(R.id.itemNameInput);
        Button removeItemButton = view.findViewById(R.id.removeItemButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        if(shopping.editItemInInventory) {
            itemNameInput.setText(shopping.selectedItemInInventory.getName());
        } else if (shopping.editItemInShoppingList) {
            itemNameInput.setText(shopping.selectedItemInShoppingList.getName());
        }

        removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemName = itemNameInput.getText().toString();

                if (itemName.isEmpty()) {
                    shopping.showAlertDialog("Remove Item", "Please enter item name to remove.");
                    return;
                }

                Item item = itemData.getItemMap().get(itemName);
                String category = item.getCategory().toString();
                int categoryOrderNum = itemData.getCategoryMap().get(category).getItemList().indexOf(item);
                for (int i = categoryOrderNum + 1; i < itemData.getCategoryMap().get(category).getItemList().size(); i++) {
                    dbItemHelper.moveOrderDownOneByCategory(category, i);
                }

                String store = item.getStore().toString();
                int storeOrderNum = itemData.getStoreMap().get(store).getItemList().indexOf(item);
                for (int i = storeOrderNum + 1; i < itemData.getStoreMap().get(store).getItemList().size(); i++) {
                    dbItemHelper.moveOrderDownOneByStore(store, i);
                }

                dbItemHelper.deleteItem(itemName);
                dbStatusHelper.deleteStatus(itemName);
                shopping.updateItemData();
                shopping.updateStatusData();

                if(shopping.editItemInInventory) {
                    shopping.itemIsSelectedInInventory = false;
                } else if (shopping.editItemInShoppingList) {
                    shopping.itemIsSelectedInShoppingList = false;
                }

                shopping.loadFragment(new FullInventory());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopping.loadFragment(new FullInventory());
            }
        });
        return view;
    }
}