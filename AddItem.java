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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_item, container, false);

        shopping = (Shopping) getActivity();
        dbItemHelper = new DBItemHelper(getActivity());
        dbStatusHelper = new DBStatusHelper(getActivity());
        dbCategoryHelper = new DBCategoryHelper(getActivity());
        dbStoreHelper = new DBStoreHelper(getActivity());
        itemData = shopping.getItemData();
        categoryData = shopping.getCategoryData();
        storeData = shopping.getStoreData();

        itemNameInput = view.findViewById(R.id.itemNameInput);
        itemBrandTypeInput = view.findViewById(R.id.itemBrandTypeInput);
        itemCategoryInput = view.findViewById(R.id.itemCategoryInput);
        itemStoreInput = view.findViewById(R.id.itemStoreInput);

        itemNameInput.setText(getString(R.string.emptyString));
        itemBrandTypeInput.setText(getString(R.string.emptyString));
        itemCategoryInput.setText(getString(R.string.emptyString));
        itemStoreInput.setText(getString(R.string.emptyString));

        quantityCheckbox = view.findViewById(R.id.quantityCheckbox);
        quantityInput = view.findViewById(R.id.quantityInput);
        priceCheckbox = view.findViewById(R.id.priceCheckbox);
        priceInput = view.findViewById(R.id.priceInput);
        locationCheckbox = view.findViewById(R.id.locationCheckbox);
        locationInput = view.findViewById(R.id.locationInput);
        noteCheckbox = view.findViewById(R.id.noteCheckbox);
        noteInput = view.findViewById(R.id.noteInput);

        quantityInput.setText(getString(R.string.emptyString));
        priceInput.setText(getString(R.string.emptyString));
        locationInput.setText(getString(R.string.emptyString));
        noteInput.setText(getString(R.string.emptyString));

        addItemButton = view.findViewById(R.id.addItemButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        quantityCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) quantityInput.setVisibility(View.VISIBLE);
                else quantityInput.setVisibility(View.GONE);
            }
        });

        priceCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) priceInput.setVisibility(View.VISIBLE);
                else priceInput.setVisibility(View.GONE);
            }
        });

        locationCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) locationInput.setVisibility(View.VISIBLE);
                else locationInput.setVisibility(View.GONE);
            }
        });

        noteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) noteInput.setVisibility(View.VISIBLE);
                else noteInput.setVisibility(View.GONE);
            }
        });

        categorySpinnerData = categoryData.getCategoryListWithAddNew();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        categorySpinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categorySpinnerData);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);

        storeSpinnerData = storeData.getStoreListWithAddNew();
        storeSpinner = view.findViewById(R.id.storeSpinner);
        storeSpinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, storeSpinnerData);
        storeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(storeSpinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals(getString(R.string.addNewCategory))) {
                    itemCategoryInput.setVisibility(View.VISIBLE);
                } else {
                    itemCategoryInput.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals(getString(R.string.addNewStore))) {
                    itemStoreInput.setVisibility(View.VISIBLE);
                } else {
                    itemStoreInput.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String itemName = itemNameInput.getText().toString();
                String itemType = itemBrandTypeInput.getText().toString();
                String itemCategory = itemCategoryInput.getText().toString();
                String itemStore = itemStoreInput.getText().toString();

                if (itemName.isEmpty() || itemType.isEmpty()) {
                    shopping.showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (categorySpinner.getSelectedItem().toString().equals(getString(R.string.emptyString)) || storeSpinner.getSelectedItem().toString().equals(getString(R.string.emptyString))) {
                    shopping.showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (categorySpinner.getSelectedItem().toString().equals(getString(R.string.addNewCategory)) && itemCategory.isEmpty()) {
                    shopping.showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                } else if (storeSpinner.getSelectedItem().toString().equals(getString(R.string.addNewStore)) && itemStore.isEmpty()) {
                    shopping.showAlertDialog(getString(R.string.addItem), getString(R.string.enterAllData), getString(R.string.ok));
                    return;
                }

                if (categorySpinner.getSelectedItem().toString().equals(getString(R.string.addNewCategory))) {
                    int numCategories = categoryData.getCategoryList().size();
                    dbCategoryHelper.addNewCategory(itemCategory, numCategories);
                    shopping.updateCategoryData();
                }

                if (storeSpinner.getSelectedItem().toString().equals(getString(R.string.addNewStore))) {
                    int numStores = storeData.getStoreList().size();
                    dbStoreHelper.addNewStore(itemStore, numStores);
                    shopping.updateStoreData();
                }

                if (!categorySpinner.getSelectedItem().toString().equals(getString(R.string.addNewCategory))) {
                    itemCategory = categorySpinner.getSelectedItem().toString();
                }

                if (!storeSpinner.getSelectedItem().toString().equals(getString(R.string.addNewStore))) {
                    itemStore = storeSpinner.getSelectedItem().toString();
                }

                int itemsInCategory;
                if (itemData.getCategoryMap().get(itemCategory) == null) itemsInCategory = 0;
                else itemsInCategory = itemData.getCategoryMap().get(itemCategory).getCategoryItemsList().size();

                int itemsInStore;
                if (itemData.getStoreMap().get(itemStore) == null) itemsInStore = 0;
                else itemsInStore = itemData.getStoreMap().get(itemStore).getStoreItemsList().size();

                dbItemHelper.addNewItemByCategory(itemName, itemType, itemCategory, itemStore, itemsInCategory);
                dbItemHelper.addNewItemByStore(itemName, itemType, itemCategory, itemStore, itemsInStore);
                dbStatusHelper.addNewStatus(itemName, getString(R.string.paused), getString(R.string.unchecked));

                shopping.updateItemData();
                shopping.updateStatusData();

                shopping.hideKeyboard();
                shopping.loadFragment(new FullInventory());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shopping.hideKeyboard();
                shopping.loadFragment(new FullInventory());
            }
        });

        return view;
    }
}