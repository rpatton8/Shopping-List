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
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;

public class AddItem extends Fragment {

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
    private EditText quantityInput;
    private EditText priceInput;
    private EditText locationInput;

    public AddItem() {}

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_item, container, false);

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
        Button addItemButton = view.findViewById(R.id.addItemButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        itemNameInput.setText("");
        itemBrandTypeInput.setText("");
        itemCategoryInput.setText("");
        itemStoreInput.setText("");

        CheckBox quantityCheckbox = view.findViewById(R.id.quantityCheckbox);
        quantityInput = view.findViewById(R.id.quantityInput);
        CheckBox priceCheckbox = view.findViewById(R.id.priceCheckbox);
        priceInput = view.findViewById(R.id.priceInput);
        CheckBox locationCheckbox = view.findViewById(R.id.locationCheckbox);
        locationInput = view.findViewById(R.id.locationInput);

        quantityCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) quantityInput.setVisibility(View.VISIBLE);
            else quantityInput.setVisibility(View.GONE);

        });

        priceCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) priceInput.setVisibility(View.VISIBLE);
            else priceInput.setVisibility(View.GONE);

        });

        locationCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) locationInput.setVisibility(View.VISIBLE);
            else locationInput.setVisibility(View.GONE);

        });

        ArrayList<String> categorySpinnerData = categoryData.getCategoryListWithAddNew();
        categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter categorySpinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categorySpinnerData);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);

        ArrayList<String> storeSpinnerData = storeData.getStoreListWithAddNew();
        storeSpinner = view.findViewById(R.id.storeSpinner);
        ArrayAdapter storeSpinnerAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, storeSpinnerData);
        storeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(storeSpinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals("(add new category)")) {
                    itemCategoryInput.setVisibility(View.VISIBLE);
                } else {
                    itemCategoryInput.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String selectedItem =  adapter.getItemAtPosition(i).toString();
                if (selectedItem.equals("(add new store)")) {
                    itemStoreInput.setVisibility(View.VISIBLE);
                } else {
                    itemStoreInput.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addItemButton.setOnClickListener(v -> {

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
            else itemsInCategory = itemData.getCategoryMap().get(itemCategory).getItemList().size();

            int itemsInStore;
            if (itemData.getStoreMap().get(itemStore) == null) itemsInStore = 0;
            else itemsInStore = itemData.getStoreMap().get(itemStore).getItemList().size();

            dbItemHelper.addNewItemByCategory(itemName, itemType, itemCategory, itemStore, itemsInCategory);
            dbItemHelper.addNewItemByStore(itemName, itemType, itemCategory, itemStore, itemsInStore);
            dbStatusHelper.addNewStatus(itemName, "paused", "unchecked");

            shopping.updateItemData();
            shopping.updateStatusData();

            shopping.hideKeyboard();
            shopping.loadFragment(new FullInventory());
        });

        cancelButton.setOnClickListener(v -> {
            shopping.hideKeyboard();
            shopping.loadFragment(new FullInventory());
        });

        return view;
    }
}