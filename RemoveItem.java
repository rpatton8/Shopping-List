package ryan.android.shopping;

import android.app.Fragment;
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

    public void setView(View view) {
        getThis().view = view;
    }

    public Shopping getShopping() {
        return shopping;
    }

    public void setShopping(Shopping shopping) {
        getThis().shopping = shopping;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        getThis().itemData = itemData;
    }

    public DBItemHelper getDbItemHelper() {
        return dbItemHelper;
    }

    public void setDbItemHelper(DBItemHelper dbItemHelper) {
        getThis().dbItemHelper = dbItemHelper;
    }

    public DBStatusHelper getDbStatusHelper() {
        return dbStatusHelper;
    }

    public void setDbStatusHelper(DBStatusHelper dbStatusHelper) {
        getThis().dbStatusHelper = dbStatusHelper;
    }

    public EditText getItemNameInput() {
        return itemNameInput;
    }

    public void setItemNameInput(EditText itemNameInput) {
        getThis().itemNameInput = itemNameInput;
    }

    public Button getRemoveItemButton() {
        return removeItemButton;
    }

    public void setRemoveItemButton(Button removeItemButton) {
        getThis().removeItemButton = removeItemButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.remove_item, container, false));

        setShopping((Shopping) getActivity());
        setItemData(getShopping().getItemData());
        setDbItemHelper(new DBItemHelper(getActivity()));
        setDbStatusHelper(new DBStatusHelper(getActivity()));

        setItemNameInput((EditText) getView().findViewById(R.id.itemNameInput));
        setRemoveItemButton((Button) getView().findViewById(R.id.removeItemButton));
        setCancelButton((Button) getView().findViewById(R.id.cancelButton));

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

        return view;
    }
}