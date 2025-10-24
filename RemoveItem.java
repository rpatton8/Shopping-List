package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                    Toast.makeText(getActivity(), "Please enter item name to remove.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Item item = itemData.getItemMap().get(itemName);
                String category = item.getCategory().toString();
                String store = item.getStore().toString();
                int orderNum = itemData.getCategoryMap().get(category).getItemList().indexOf(item);
                dbItemHelper.deleteItem(itemName);
                for (int i = orderNum + 1; i < itemData.getCategoryMap().get(category).getItemList().size(); i++) {
                    if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {
                        dbItemHelper.moveOrderDownOneByCategory(category, i);
                    } else if (shopping.inventorySortBy.equals(Shopping.SORT_BY_CATEGORY)) {
                        dbItemHelper.moveOrderDownOneByStore(store, i);
                    }
                }
                dbStatusHelper.deleteStatus(itemName);
                shopping.updateItemData();
                shopping.updateStatusData();

                if(shopping.editItemInInventory) {
                    shopping.itemIsSelectedInInventory = false;
                } else if (shopping.editItemInShoppingList) {
                    shopping.itemIsSelectedInShoppingList = false;
                }

                Toast.makeText(getActivity(), "Item has been removed.", Toast.LENGTH_SHORT).show();

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