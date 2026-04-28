package ryan.android.shopping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

//@SuppressWarnings("ALL")
public class RemoveItem extends Fragment {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private DBItemHelper dbItemHelper;
    private DBStatusHelper dbStatusHelper;

    private EditText itemNameInput;
    private Button removeItemButton;
    private Button cancelButton;

    public RemoveItem() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.remove_item, container, false);

        shopping = (Shopping) getActivity();
        itemData = shopping.getItemData();
        dbItemHelper = new DBItemHelper(getActivity());
        dbStatusHelper = new DBStatusHelper(getActivity());

        itemNameInput = view.findViewById(R.id.itemNameInput);
        removeItemButton = view.findViewById(R.id.removeItemButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        itemNameInput.setText(shopping.selectedItemInInventory.getName());

        removeItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String itemName = itemNameInput.getText().toString();

                if (itemName.isEmpty()) {
                    shopping.showAlertDialog("Remove Item", "Please enter item name to remove.");
                    return;
                }

                Item item = itemData.getItemMap().get(itemName);

                String category = item.getCategory().toString();
                int categoryOrderNum = itemData.getCategoryMap().get(category).getCategoryItemsList().indexOf(item);
                for (int i = categoryOrderNum + 1; i < itemData.getCategoryMap().get(category).getCategoryItemsList().size(); i++) {
                    dbItemHelper.moveOrderDownOneByCategory(category, i);
                }

                String store = item.getStore().toString();
                int storeOrderNum = itemData.getStoreMap().get(store).getStoreItemsList().indexOf(item);
                for (int i = storeOrderNum + 1; i < itemData.getStoreMap().get(store).getStoreItemsList().size(); i++) {
                    dbItemHelper.moveOrderDownOneByStore(store, i);
                }

                dbItemHelper.deleteItem(itemName);
                dbStatusHelper.deleteStatus(itemName);
                shopping.updateItemData();
                shopping.updateStatusData();

                shopping.itemIsSelectedInInventory = false;

                shopping.loadFragment(new FullInventory());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.loadFragment(new FullInventory());
            }
        });

        return view;
    }
}