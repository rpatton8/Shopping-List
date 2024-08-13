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

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private DBHelper dbHelper;
    private DBStatusHelper dbStatusHelper;

    private EditText itemNameInput;
    private Button removeItemButton;
    private Button cancelButton;

    public RemoveItem() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.remove_item, container, false);

        shopping = (Shopping) getActivity();
        itemData = shopping.getItemData();
        dbHelper = new DBHelper(getActivity());
        dbStatusHelper = new DBStatusHelper(getActivity());

        itemNameInput = view.findViewById(R.id.itemNameInput);
        removeItemButton = view.findViewById(R.id.removeItemButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        itemNameInput.setText(shopping.selectedItem.getName());

        removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemName = itemNameInput.getText().toString();

                if (itemName.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter item name to remove.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Item item = itemData.getItemMap().get(itemName);
                String category = item.getCategory(0).toString();
                int orderNum = itemData.getCategoryMap().get(category).getItemList().indexOf(item);
                dbHelper.deleteItem(itemName);
                for (int i = orderNum + 1; i < itemData.getCategoryMap().get(category).getItemList().size(); i++) {
                    dbHelper.moveOrderDownOne(category, i);
                }
                dbStatusHelper.deleteStatus(itemName);
                shopping.updateItemData();
                shopping.updateStatusData();
                shopping.itemIsSelected = false;

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