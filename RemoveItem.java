package ryan.android.shopping;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

    private RemoveItem getThis() {
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

    private DBItemHelper getDbItemHelper() {
        return dbItemHelper;
    }

    private void setDbItemHelper(DBItemHelper dbItemHelper) {
        getThis().dbItemHelper = dbItemHelper;
    }

    private DBStatusHelper getDbStatusHelper() {
        return dbStatusHelper;
    }

    private void setDbStatusHelper(DBStatusHelper dbStatusHelper) {
        getThis().dbStatusHelper = dbStatusHelper;
    }

    private EditText getItemNameInput() {
        return itemNameInput;
    }

    private void setItemNameInput(EditText itemNameInput) {
        getThis().itemNameInput = itemNameInput;
    }

    private Button getRemoveItemButton() {
        return removeItemButton;
    }

    private void setRemoveItemButton(Button removeItemButton) {
        getThis().removeItemButton = removeItemButton;
    }

    private Button getCancelButton() {
        return cancelButton;
    }

    private void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.remove_item, container, false));

        setShopping((Shopping) getActivity());
        setItemData(getShopping().getItemData());
        setDbItemHelper(new DBItemHelper(getActivity()));
        setDbStatusHelper(new DBStatusHelper(getActivity()));

        setItemNameInput(getView().findViewById(R.id.itemNameInput));
        setRemoveItemButton(getView().findViewById(R.id.removeItemButton));
        setCancelButton(getView().findViewById(R.id.cancelButton));

        getItemNameInput().setText(getShopping().getSelectedItemInInventory().getItemName());

        getRemoveItemButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String itemName = getItemNameInput().getText().toString();

                if (itemName.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.removeItem), getString(R.string.enterItemToRemove), getString(R.string.ok));
                    return;
                }

                Item item = getItemData().getItemMap().get(itemName);

                String category = item.getCategory().toString();
                int categoryOrderNum = getItemData().getCategoryMap().get(category).getCategoryItemsList().indexOf(item);
                for (int i = categoryOrderNum + 1; i < getItemData().getCategoryMap().get(category).getCategoryItemsList().size(); i++) {
                    getDbItemHelper().moveOrderDownOneByCategory(category, i);
                }

                String store = item.getStore().toString();
                int storeOrderNum = getItemData().getStoreMap().get(store).getStoreItemsList().indexOf(item);
                for (int i = storeOrderNum + 1; i < getItemData().getStoreMap().get(store).getStoreItemsList().size(); i++) {
                    getDbItemHelper().moveOrderDownOneByStore(store, i);
                }

                getDbItemHelper().deleteItem(itemName);
                getDbStatusHelper().deleteStatus(itemName);
                getShopping().updateItemData();
                getShopping().updateStatusData();

                getShopping().setItemIsSelectedInInventory(false);

                getShopping().loadFragment(new FullInventory());
            }
        });

        getCancelButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().loadFragment(new FullInventory());
            }
        });

        return getView();
    }
}