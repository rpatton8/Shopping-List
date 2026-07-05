package ryan.android.shopping;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;

public class AddItem extends Fragment {

    private View view;
    private Shopping shopping;
    private ItemData itemData;
    private CategoryData categoryData;
    private StoreData storeData;
    private DBItemHelper dbItemHelper;
    private DBStatusHelper dbStatusHelper;
    private DBCategoryHelper dbCategoryHelper;
    private DBStoreHelper dbStoreHelper;

    private EditText itemNameInput;
    private EditText itemBrandTypeInput;
    private Spinner categorySpinner;
    private Spinner storeSpinner;
    private EditText itemCategoryInput;
    private EditText itemStoreInput;
    private CheckBox quantityCheckbox;
    private EditText quantityInput;
    private CheckBox priceCheckbox;
    private EditText priceInput;
    private CheckBox locationCheckbox;
    private EditText locationInput;
    private CheckBox noteCheckbox;
    private EditText noteInput;
    private Button addItemButton;
    private Button cancelButton;

    private ArrayList<String> categorySpinnerData;
    private ArrayAdapter<String> categorySpinnerAdapter;
    private ArrayList<String> storeSpinnerData;
    private ArrayAdapter<String> storeSpinnerAdapter;

    public AddItem() {}

    private AddItem getThis() {
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

    private CategoryData getCategoryData() {
        return categoryData;
    }

    private void setCategoryData(CategoryData categoryData) {
        getThis().categoryData = categoryData;
    }

    private StoreData getStoreData() {
        return storeData;
    }

    private void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
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

    private DBCategoryHelper getDbCategoryHelper() {
        return dbCategoryHelper;
    }

    private void setDbCategoryHelper(DBCategoryHelper dbCategoryHelper) {
        getThis().dbCategoryHelper = dbCategoryHelper;
    }

    private DBStoreHelper getDbStoreHelper() {
        return dbStoreHelper;
    }

    private void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
        getThis().dbStoreHelper = dbStoreHelper;
    }

    private EditText getItemNameInput() {
        return itemNameInput;
    }

    private void setItemNameInput(EditText itemNameInput) {
        getThis().itemNameInput = itemNameInput;
    }

    private EditText getItemBrandTypeInput() {
        return itemBrandTypeInput;
    }

    private void setItemBrandTypeInput(EditText itemBrandTypeInput) {
        getThis().itemBrandTypeInput = itemBrandTypeInput;
    }

    private Spinner getCategorySpinner() {
        return categorySpinner;
    }

    private void setCategorySpinner(Spinner categorySpinner) {
        getThis().categorySpinner = categorySpinner;
    }

    private Spinner getStoreSpinner() {
        return storeSpinner;
    }

    private void setStoreSpinner(Spinner storeSpinner) {
        getThis().storeSpinner = storeSpinner;
    }

    private EditText getItemCategoryInput() {
        return itemCategoryInput;
    }

    private void setItemCategoryInput(EditText itemCategoryInput) {
        getThis().itemCategoryInput = itemCategoryInput;
    }

    private EditText getItemStoreInput() {
        return itemStoreInput;
    }

    private void setItemStoreInput(EditText itemStoreInput) {
        getThis().itemStoreInput = itemStoreInput;
    }

    private CheckBox getQuantityCheckbox() {
        return quantityCheckbox;
    }

    private void setQuantityCheckbox(CheckBox quantityCheckbox) {
        getThis().quantityCheckbox = quantityCheckbox;
    }

    private EditText getQuantityInput() {
        return quantityInput;
    }

    private void setQuantityInput(EditText quantityInput) {
        getThis().quantityInput = quantityInput;
    }

    private CheckBox getPriceCheckbox() {
        return priceCheckbox;
    }

    private void setPriceCheckbox(CheckBox priceCheckbox) {
        getThis().priceCheckbox = priceCheckbox;
    }

    private EditText getPriceInput() {
        return priceInput;
    }

    private void setPriceInput(EditText priceInput) {
        getThis().priceInput = priceInput;
    }

    private CheckBox getLocationCheckbox() {
        return locationCheckbox;
    }

    private void setLocationCheckbox(CheckBox locationCheckbox) {
        getThis().locationCheckbox = locationCheckbox;
    }

    private EditText getLocationInput() {
        return locationInput;
    }

    private void setLocationInput(EditText locationInput) {
        getThis().locationInput = locationInput;
    }

    private CheckBox getNoteCheckbox() {
        return noteCheckbox;
    }

    private void setNoteCheckbox(CheckBox noteCheckbox) {
        getThis().noteCheckbox = noteCheckbox;
    }

    private EditText getNoteInput() {
        return noteInput;
    }

    private void setNoteInput(EditText noteInput) {
        getThis().noteInput = noteInput;
    }

    private Button getAddItemButton() {
        return addItemButton;
    }

    private void setAddItemButton(Button addItemButton) {
        getThis().addItemButton = addItemButton;
    }

    private Button getCancelButton() {
        return cancelButton;
    }

    private void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    private ArrayList<String> getCategorySpinnerData() {
        return categorySpinnerData;
    }

    private void setCategorySpinnerData(ArrayList<String> categorySpinnerData) {
        getThis().categorySpinnerData = categorySpinnerData;
    }

    private ArrayAdapter<String> getCategorySpinnerAdapter() {
        return categorySpinnerAdapter;
    }

    private void setCategorySpinnerAdapter(ArrayAdapter<String> categorySpinnerAdapter) {
        getThis().categorySpinnerAdapter = categorySpinnerAdapter;
    }

    private ArrayList<String> getStoreSpinnerData() {
        return storeSpinnerData;
    }

    private void setStoreSpinnerData(ArrayList<String> storeSpinnerData) {
        getThis().storeSpinnerData = storeSpinnerData;
    }

    private ArrayAdapter<String> getStoreSpinnerAdapter() {
        return storeSpinnerAdapter;
    }

    private void setStoreSpinnerAdapter(ArrayAdapter<String> storeSpinnerAdapter) {
        getThis().storeSpinnerAdapter = storeSpinnerAdapter;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.add_item, container, false));

        setShopping((Shopping) getActivity());
        setDbItemHelper(new DBItemHelper(getActivity()));
        setDbStatusHelper(new DBStatusHelper(getActivity()));
        setDbCategoryHelper(new DBCategoryHelper(getActivity()));
        setDbStoreHelper(new DBStoreHelper(getActivity()));
        setItemData(getShopping().getItemData());
        setCategoryData(getShopping().getCategoryData());
        setStoreData(getShopping().getStoreData());

        setItemNameInput(getView().findViewById(R.id.itemNameInput));
        setItemBrandTypeInput(getView().findViewById(R.id.itemBrandTypeInput));
        setItemCategoryInput(getView().findViewById(R.id.itemCategoryInput));
        setItemStoreInput(getView().findViewById(R.id.itemStoreInput));

        getItemNameInput().setText(getString(R.string.emptyString));
        getItemBrandTypeInput().setText(getString(R.string.emptyString));
        getItemCategoryInput().setText(getString(R.string.emptyString));
        getItemStoreInput().setText(getString(R.string.emptyString));

        setQuantityCheckbox(getView().findViewById(R.id.quantityCheckbox));
        setQuantityInput(getView().findViewById(R.id.quantityInput));
        setPriceCheckbox(getView().findViewById(R.id.priceCheckbox));
        setPriceInput(getView().findViewById(R.id.priceInput));
        setLocationCheckbox(getView().findViewById(R.id.locationCheckbox));
        setLocationInput(getView().findViewById(R.id.locationInput));
        setNoteCheckbox(getView().findViewById(R.id.noteCheckbox));
        setNoteInput(getView().findViewById(R.id.noteInput));

        getQuantityInput().setText(getString(R.string.emptyString));
        getPriceInput().setText(getString(R.string.emptyString));
        getLocationInput().setText(getString(R.string.emptyString));
        getNoteInput().setText(getString(R.string.emptyString));

        setAddItemButton(getView().findViewById(R.id.addItemButton));
        setCancelButton(getView().findViewById(R.id.cancelButton));

        getQuantityCheckbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) getQuantityInput().setVisibility(View.VISIBLE);
                else getQuantityInput().setVisibility(View.GONE);
            }
        });

        getPriceCheckbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) getPriceInput().setVisibility(View.VISIBLE);
                else getPriceInput().setVisibility(View.GONE);
            }
        });

        getLocationCheckbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) getLocationInput().setVisibility(View.VISIBLE);
                else getLocationInput().setVisibility(View.GONE);
            }
        });

        getNoteCheckbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) getNoteInput().setVisibility(View.VISIBLE);
                else getNoteInput().setVisibility(View.GONE);
            }
        });

        setCategorySpinnerData(getCategoryData().getCategoryListWithAddNew());
        setCategorySpinner(getView().findViewById(R.id.categorySpinner));
        setCategorySpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getCategorySpinnerData()));
        getCategorySpinnerAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getCategorySpinner().setAdapter(getCategorySpinnerAdapter());

        setStoreSpinnerData(getStoreData().getStoreListWithAddNew());
        setStoreSpinner(getView().findViewById(R.id.storeSpinner));
        setStoreSpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getStoreSpinnerData()));
        getStoreSpinnerAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getStoreSpinner().setAdapter(getStoreSpinnerAdapter());

        getCategorySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals(getString(R.string.addNewCategory))) {
                    getItemCategoryInput().setVisibility(View.VISIBLE);
                } else {
                    getItemCategoryInput().setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        getStoreSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals(getString(R.string.addNewStore))) {
                    getItemStoreInput().setVisibility(View.VISIBLE);
                } else {
                    getItemStoreInput().setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        getAddItemButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String itemName = getItemNameInput().getText().toString();
                String itemType = getItemBrandTypeInput().getText().toString();
                String itemCategory = getItemCategoryInput().getText().toString();
                String itemStore = getItemStoreInput().getText().toString();

                if (itemName.isEmpty() || itemType.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (getCategorySpinner().getSelectedItem().toString().equals(getString(R.string.emptyString)) || getStoreSpinner().getSelectedItem().toString().equals(getString(R.string.emptyString))) {
                    getShopping().showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (getCategorySpinner().getSelectedItem().toString().equals(getString(R.string.addNewCategory)) && itemCategory.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (getStoreSpinner().getSelectedItem().toString().equals(getString(R.string.addNewStore)) && itemStore.isEmpty()) {
                    getShopping().showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                }

                if (getCategorySpinner().getSelectedItem().toString().equals(getString(R.string.addNewCategory))) {
                    int numCategories = getCategoryData().getCategoryList().size();
                    getDbCategoryHelper().addNewCategory(itemCategory, numCategories);
                    getShopping().updateCategoryData();
                }

                if (getStoreSpinner().getSelectedItem().toString().equals(getString(R.string.addNewStore))) {
                    int numStores = getStoreData().getStoreList().size();
                    getDbStoreHelper().addNewStore(itemStore, numStores);
                    getShopping().updateStoreData();
                }

                if (!getCategorySpinner().getSelectedItem().toString().equals(getString(R.string.addNewCategory))) {
                    itemCategory = getCategorySpinner().getSelectedItem().toString();
                }

                if (!getStoreSpinner().getSelectedItem().toString().equals(getString(R.string.addNewStore))) {
                    itemStore = getStoreSpinner().getSelectedItem().toString();
                }

                int itemsInCategory;
                if (getItemData().getCategoryMap().get(itemCategory) == null) itemsInCategory = 0;
                else itemsInCategory = getItemData().getCategoryMap().get(itemCategory).getCategoryItemsList().size();

                int itemsInStore;
                if (getItemData().getStoreMap().get(itemStore) == null) itemsInStore = 0;
                else itemsInStore = getItemData().getStoreMap().get(itemStore).getStoreItemsList().size();

                getDbItemHelper().addNewItemByCategory(itemName, itemType, itemCategory, itemStore, itemsInCategory);
                getDbItemHelper().addNewItemByStore(itemName, itemType, itemCategory, itemStore, itemsInStore);
                getDbStatusHelper().addNewStatus(itemName, getString(R.string.paused), getString(R.string.unchecked));

                getShopping().updateItemData();
                getShopping().updateStatusData();

                getShopping().hideKeyboard();
                getShopping().loadFragment(new FullInventory());
            }
        });

        getCancelButton().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getShopping().hideKeyboard();
                getShopping().loadFragment(new FullInventory());
            }
        });

        return getView();
    }
}