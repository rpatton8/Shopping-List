package ryan.android.shopping;

import android.app.Fragment;
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
    private ArrayAdapter categorySpinnerAdapter;
    private ArrayList<String> storeSpinnerData;
    private ArrayAdapter storeSpinnerAdapter;

    public AddItem() {}

    private AddItem getThis() {
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

    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(CategoryData categoryData) {
        getThis().categoryData = categoryData;
    }

    public StoreData getStoreData() {
        return storeData;
    }

    public void setStoreData(StoreData storeData) {
        getThis().storeData = storeData;
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

    public DBCategoryHelper getDbCategoryHelper() {
        return dbCategoryHelper;
    }

    public void setDbCategoryHelper(DBCategoryHelper dbCategoryHelper) {
        getThis().dbCategoryHelper = dbCategoryHelper;
    }

    public DBStoreHelper getDbStoreHelper() {
        return dbStoreHelper;
    }

    public void setDbStoreHelper(DBStoreHelper dbStoreHelper) {
        getThis().dbStoreHelper = dbStoreHelper;
    }

    public EditText getItemNameInput() {
        return itemNameInput;
    }

    public void setItemNameInput(EditText itemNameInput) {
        getThis().itemNameInput = itemNameInput;
    }

    public EditText getItemBrandTypeInput() {
        return itemBrandTypeInput;
    }

    public void setItemBrandTypeInput(EditText itemBrandTypeInput) {
        getThis().itemBrandTypeInput = itemBrandTypeInput;
    }

    public Spinner getCategorySpinner() {
        return categorySpinner;
    }

    public void setCategorySpinner(Spinner categorySpinner) {
        getThis().categorySpinner = categorySpinner;
    }

    public Spinner getStoreSpinner() {
        return storeSpinner;
    }

    public void setStoreSpinner(Spinner storeSpinner) {
        getThis().storeSpinner = storeSpinner;
    }

    public EditText getItemCategoryInput() {
        return itemCategoryInput;
    }

    public void setItemCategoryInput(EditText itemCategoryInput) {
        getThis().itemCategoryInput = itemCategoryInput;
    }

    public EditText getItemStoreInput() {
        return itemStoreInput;
    }

    public void setItemStoreInput(EditText itemStoreInput) {
        getThis().itemStoreInput = itemStoreInput;
    }

    public CheckBox getQuantityCheckbox() {
        return quantityCheckbox;
    }

    public void setQuantityCheckbox(CheckBox quantityCheckbox) {
        getThis().quantityCheckbox = quantityCheckbox;
    }

    public EditText getQuantityInput() {
        return quantityInput;
    }

    public void setQuantityInput(EditText quantityInput) {
        getThis().quantityInput = quantityInput;
    }

    public CheckBox getPriceCheckbox() {
        return priceCheckbox;
    }

    public void setPriceCheckbox(CheckBox priceCheckbox) {
        getThis().priceCheckbox = priceCheckbox;
    }

    public EditText getPriceInput() {
        return priceInput;
    }

    public void setPriceInput(EditText priceInput) {
        getThis().priceInput = priceInput;
    }

    public CheckBox getLocationCheckbox() {
        return locationCheckbox;
    }

    public void setLocationCheckbox(CheckBox locationCheckbox) {
        getThis().locationCheckbox = locationCheckbox;
    }

    public EditText getLocationInput() {
        return locationInput;
    }

    public void setLocationInput(EditText locationInput) {
        getThis().locationInput = locationInput;
    }

    public CheckBox getNoteCheckbox() {
        return noteCheckbox;
    }

    public void setNoteCheckbox(CheckBox noteCheckbox) {
        getThis().noteCheckbox = noteCheckbox;
    }

    public EditText getNoteInput() {
        return noteInput;
    }

    public void setNoteInput(EditText noteInput) {
        getThis().noteInput = noteInput;
    }

    public Button getAddItemButton() {
        return addItemButton;
    }

    public void setAddItemButton(Button addItemButton) {
        getThis().addItemButton = addItemButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        getThis().cancelButton = cancelButton;
    }

    public ArrayList<String> getCategorySpinnerData() {
        return categorySpinnerData;
    }

    public void setCategorySpinnerData(ArrayList<String> categorySpinnerData) {
        getThis().categorySpinnerData = categorySpinnerData;
    }

    public ArrayAdapter getCategorySpinnerAdapter() {
        return categorySpinnerAdapter;
    }

    public void setCategorySpinnerAdapter(ArrayAdapter categorySpinnerAdapter) {
        getThis().categorySpinnerAdapter = categorySpinnerAdapter;
    }

    public ArrayList<String> getStoreSpinnerData() {
        return storeSpinnerData;
    }

    public void setStoreSpinnerData(ArrayList<String> storeSpinnerData) {
        getThis().storeSpinnerData = storeSpinnerData;
    }

    public ArrayAdapter getStoreSpinnerAdapter() {
        return storeSpinnerAdapter;
    }

    public void setStoreSpinnerAdapter(ArrayAdapter storeSpinnerAdapter) {
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

        setItemNameInput((EditText) getView().findViewById(R.id.itemNameInput));
        setItemBrandTypeInput((EditText) getView().findViewById(R.id.itemBrandTypeInput));
        setItemCategoryInput((EditText) getView().findViewById(R.id.itemCategoryInput));
        setItemStoreInput((EditText) getView().findViewById(R.id.itemStoreInput));

        getItemNameInput().setText(getString(R.string.emptyString));
        getItemBrandTypeInput().setText(getString(R.string.emptyString));
        getItemCategoryInput().setText(getString(R.string.emptyString));
        getItemStoreInput().setText(getString(R.string.emptyString));

        setQuantityCheckbox((CheckBox) getView().findViewById(R.id.quantityCheckbox));
        setQuantityInput((EditText) getView().findViewById(R.id.quantityInput));
        setPriceCheckbox((CheckBox) getView().findViewById(R.id.priceCheckbox));
        setPriceInput((EditText) getView().findViewById(R.id.priceInput));
        setLocationCheckbox((CheckBox) getView().findViewById(R.id.locationCheckbox));
        setLocationInput((EditText) getView().findViewById(R.id.locationInput));
        setNoteCheckbox((CheckBox) getView().findViewById(R.id.noteCheckbox));
        setNoteInput((EditText) getView().findViewById(R.id.noteInput));

        getQuantityInput().setText(getString(R.string.emptyString));
        getPriceInput().setText(getString(R.string.emptyString));
        getLocationInput().setText(getString(R.string.emptyString));
        getNoteInput().setText(getString(R.string.emptyString));

        setAddItemButton((Button) getView().findViewById(R.id.addItemButton));
        setCancelButton((Button) getView().findViewById(R.id.cancelButton));

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
        setCategorySpinner((Spinner) getView().findViewById(R.id.categorySpinner));
        setCategorySpinnerAdapter(new ArrayAdapter<>(getThis().getActivity(), android.R.layout.simple_spinner_item, getCategorySpinnerData()));
        getCategorySpinnerAdapter().setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getCategorySpinner().setAdapter(getCategorySpinnerAdapter());

        setStoreSpinnerData(getStoreData().getStoreListWithAddNew());
        setStoreSpinner((Spinner) getView().findViewById(R.id.storeSpinner));
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
                    int numStores = storeData.getStoreList().size();
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

        return view;
    }
}