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

//@SuppressWarnings("ALL")
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

        itemNameInput.setText("");
        itemBrandTypeInput.setText("");
        itemCategoryInput.setText("");
        itemStoreInput.setText("");

        quantityCheckbox = view.findViewById(R.id.quantityCheckbox);
        quantityInput = view.findViewById(R.id.quantityInput);
        priceCheckbox = view.findViewById(R.id.priceCheckbox);
        priceInput = view.findViewById(R.id.priceInput);
        locationCheckbox = view.findViewById(R.id.locationCheckbox);
        locationInput = view.findViewById(R.id.locationInput);

        quantityInput.setText("");
        priceInput.setText("");
        locationInput.setText("");

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
                if (selectedItem.equals("(add new category)")) {
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
                if (selectedItem.equals("(add new store)")) {
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
                    shopping.showAlertDialog("Add Item", "Please enter all the data.");
                    return;
                } else if (categorySpinner.getSelectedItem().toString().equals("") || storeSpinner.getSelectedItem().toString().equals("")) {
                    shopping.showAlertDialog("Add Item", "Please enter all the data.");
                    return;
                } else if (categorySpinner.getSelectedItem().toString().equals("(add new category)") && itemCategory.isEmpty()) {
                    shopping.showAlertDialog("Add Item", "Please enter all the data.");
                    return;
                } else if (storeSpinner.getSelectedItem().toString().equals("(add new store)") && itemStore.isEmpty()) {
                    shopping.showAlertDialog("Add Item", "Please enter all the data.");
                    return;
                }

                if (categorySpinner.getSelectedItem().toString().equals("(add new category)")) {
                    int numCategories = categoryData.getCategoryList().size();
                    dbCategoryHelper.addNewCategory(itemCategory, numCategories);
                    shopping.updateCategoryData();
                }

                if (storeSpinner.getSelectedItem().toString().equals("(add new store)")) {
                    int numStores = storeData.getStoreList().size();
                    dbStoreHelper.addNewStore(itemStore, numStores);
                    shopping.updateStoreData();
                }

                if (!categorySpinner.getSelectedItem().toString().equals("(add new category)")) {
                    itemCategory = categorySpinner.getSelectedItem().toString();
                }

                if (!storeSpinner.getSelectedItem().toString().equals("(add new store)")) {
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
                dbStatusHelper.addNewStatus(itemName, "paused", "unchecked");

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